/**
 * 
 */
package cc.bodydclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import cc.bodydclient.xml.ServerMessageHandler;
import cc.bodydclient.xml.XmlDataEventListener;

import de.sciss.net.OSCBundle;
import de.sciss.net.OSCMessage;

/**
 * @author carlos
 *
 */
public class ClientHandler implements Runnable, XmlDataEventListener
{
	String host = null;
	int port = 59000;
	private InputStream in;
	private OutputStream out;
	private Socket socket;
	private Thread runner = null; // we're running this class as a thread
	private boolean running = false;
	private static OSCHandler oscHandler; // OSCHandler thread
	
	String xml;
	ClientPanel clientPanel;
	
	// xml elements and attributes
    public static final String XML_DECL = "<?xml version=\"1.0\"?>";
    public static final String ROOT_ELEMENT_NAME = "bodydml";
    public static final String ADD_ELEMENT_NAME = "add";
    public static final String REMOVE_ELEMENT_NAME = "remove";
    public static final String SENSOR_ATTRIBUTE_NAME = "sensor";
    public static final String REQUESTTYPE_ATTRIBUTE_NAME = "requesttype";
    public static final String HISTORY_ELEMENT_NAME = "history";
    public static final String COUNT_ATTRIBUTE_NAME = "count";
    public static final String INTERVAL_ATTRIBUTE_NAME = "interval";
    
    private ServerMessageHandler messageHandler;
    
    // store a list of all the current ClientHandler objects (will probably only be one)
    static Vector handlers = new Vector();
	
    static final String all = "/all";
    
    // livedata stuff
    static final String liveMsg = "/livedata";
    static final String gsrMsg = liveMsg+"/gsr/timeout";
    static final String ecgMsg = liveMsg+"/ecg/maxsockets";
    static final String emgMsg = liveMsg+"/emg/nudgepower";
    static final String respMsg = liveMsg+"/respiration/datarate";
    static final String avgMsg = liveMsg+"/avg/powerlevel";
    // history stuff
    static final String historyMsg = "/history";
    static final String gsrHistMsg = historyMsg+"/data/gsr/timeout";
    static final String ecgHistMsg = historyMsg+"/data/ecg/maxsockets";
    static final String emgHistMsg = historyMsg+"/data/emg/nudgepower";
    static final String respHistMsg = historyMsg+"/data/respiration/datarate";
    static final String avgHistMsg = historyMsg+"/data/avg/powerlevel";
    
    // fail
    static final String failMsg = "/fail";
    
	public ClientHandler(String host, int port, String xml, ClientPanel panel)
	{
		this.host = host;
		this.port = port;
		this.xml = xml;
		this.clientPanel = panel;
	}
	
	
	public void xmlDataEvent(Hashtable data)
	{
		ArrayList messages = new ArrayList();
		String sensor=null;
		String serverprop=null;
		Object[] vals = null;
		
		if(data.containsKey(cc.bodydclient.xml.AbstractServerContext.LIVEDATA)) {
			messages.add(new OSCMessage(all+liveMsg, OSCMessage.NO_ARGS));
			Hashtable h1 = (Hashtable)(data.get(cc.bodydclient.xml.AbstractServerContext.LIVEDATA));
			Enumeration keys = h1.keys();
			while(keys.hasMoreElements()) {
				String currKey = (String)keys.nextElement();
				Hashtable h2 = (Hashtable)(h1.get(currKey));
				
				// collect the data to put in Object arrays of OSCMessage objects
				// then create an OSCMessage and add to the ArrayList
				if(currKey.equalsIgnoreCase("GSR")) {
					sensor = (String)(h2.get("GSR"));
					serverprop = (String)(h2.get("timeout"));
					vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(serverprop)};
					//msgHolder = new OSCMessage(gsrMsg, vals);
					messages.add(new OSCMessage(gsrMsg, vals));
				} else if(currKey.equalsIgnoreCase("ECG")) {
					sensor = (String)(h2.get("ECG"));
					serverprop = (String)(h2.get("maxsockets"));
					vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(serverprop)};
					//msgHolder = new OSCMessage(ecgMsg, vals);
					messages.add(new OSCMessage(ecgMsg, vals));
				} else if(currKey.equalsIgnoreCase("EMG")) {
					sensor = (String)(h2.get("EMG"));
					serverprop = (String)(h2.get("nudgepower"));
					// Boolean values have to be converted to Integer to send via OSC
					vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(sensor)};
					//msgHolder = new OSCMessage(emgMsg, vals);
					messages.add(new OSCMessage(emgMsg, vals));
				} else if(currKey.equalsIgnoreCase("Respiration")) {
					sensor = (String)(h2.get("Respiration"));
					serverprop = (String)(h2.get("datarate"));
					vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(serverprop)};
					//msgHolder = new OSCMessage(respMsg, vals);
					messages.add(new OSCMessage(respMsg, vals));
				} else if(currKey.equalsIgnoreCase("avg")) {
					sensor = (String)(h2.get("avg"));
					serverprop = (String)(h2.get("powerlevel"));
					vals = new Object[] {Double.valueOf(sensor), Double.valueOf(serverprop)};
					//msgHolder = new OSCMessage(respMsg, vals);
					messages.add(new OSCMessage(avgMsg, vals));
				}
			} // end while
		} else if(data.containsKey(cc.bodydclient.xml.AbstractServerContext.HISTORY)) {
			// logging stuff...
			messages.add(new OSCMessage(all+historyMsg, OSCMessage.NO_ARGS));
			Hashtable h1 = (Hashtable)(data.get(cc.bodydclient.xml.AbstractServerContext.HISTORY));
			Enumeration keys = h1.keys();
			while(keys.hasMoreElements()) {
				String timeKey = (String)keys.nextElement();
				Hashtable h2 = (Hashtable)(h1.get(timeKey));
				Enumeration sensorKeys = h2.keys();
				System.out.println("time=" + timeKey);
				while(sensorKeys.hasMoreElements()) {
					String sensorKey = (String)sensorKeys.nextElement();
					sensor = (String)(h2.get(sensorKey));
					
					if(sensorKey.equalsIgnoreCase("GSR")) {
						serverprop = (String)(h2.get("timeout"));
						vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(serverprop)};
						messages.add(new OSCMessage(gsrHistMsg, vals));
					} else if(sensorKey.equalsIgnoreCase("ECG")) {
						serverprop = (String)(h2.get("maxsockets"));
						vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(serverprop)};
						messages.add(new OSCMessage(ecgHistMsg, vals));
					} else if(sensorKey.equalsIgnoreCase("EMG")) {
						serverprop = (String)(h2.get("nudgepower"));
						// Boolean values have to be converted to Integer to send via OSC
						vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(sensor)};
						messages.add(new OSCMessage(emgHistMsg, vals));
					} else if(sensorKey.equalsIgnoreCase("Respiration")) {
						serverprop = (String)(h2.get("datarate"));
						vals = new Object[] {Integer.valueOf(sensor), Integer.valueOf(serverprop)};
						messages.add(new OSCMessage(respHistMsg, vals));
					} else if(sensorKey.equalsIgnoreCase("avg")) {
						serverprop = (String)(h2.get("powerlevel"));
						vals = new Object[] {Double.valueOf(sensor), Double.valueOf(serverprop)};
						messages.add(new OSCMessage(avgHistMsg, vals));		
					}
				} // end inner while
			} // end outer while
		}

		if(oscHandler.isConnected) {
			// create the OSCBundle
			OSCBundle bundle = new OSCBundle();
			ListIterator iter = messages.listIterator();
			while(iter.hasNext()) {
				bundle.addPacket((OSCMessage) iter.next());
			}
			// send it!
			oscHandler.sendOSC(bundle);
		}
	}
	
	private void connectToServer() throws IOException
	{
		System.out.println("Connecting to server at " + host + ":" + port);
		try {
			// socket
			socket = new Socket(host, port);
			try {
				socket.setTcpNoDelay(true); // good idea to do this, avoid delays in sending data
				socket.setSoTimeout(30000); // socket timeout
			} catch(SocketException se) {
				System.out.println("*** Socket error: " +
						"unable to set the socket timeout and/or tcp no delay! ***");
			}
			// i/o streams
			in = socket.getInputStream();
			out = socket.getOutputStream();
			// for xml handling/parsing
			messageHandler = new ServerMessageHandler();
			messageHandler.addXmlDataEventListener(this);
			System.out.println(">>>>>> Connected to server at: " + socket.getInetAddress() + " <<<<<<");
			// send the initial request for data to the BodyDaemon server
			sendXmlRequest(xml);
		}
		catch(IOException ioe) {
			if(!(socket == null)) {
				socket.close();
				socket = null;
			}
			throw ioe;
		}
	}
	
	private void disconnectFromServer() throws IOException
	{
		try {
			System.out.println("Disconnecting from server at " + host + ":" + port);
			
			// remove xml handling
			messageHandler.removeXmlDataEventListener(this);
			messageHandler = null;
			
            // close the input/output streams and the socket
			if(out != null) {
				out.close();
				out = null;
			}
			if(in != null) {
				in.close();
				in = null;
			}
			if(socket != null) {
				socket.close();
				socket = null;
			}
		} catch(IOException ioe) {
			throw ioe;
		}
	}
	
    protected void handleMessage(ServerMessageHandler smh, String str) throws IOException, SAXException
    {
		// ----- read in data the client sends ----- //
    		System.out.println("+++ Client: receiving xml... +++");
    		System.out.println(str);
    		// get an xml parser
    		XMLReader reader = new SAXParser();
    		reader.setContentHandler(smh);
    		
    		// begin reading from the input stream
    		StringReader sr = new StringReader(str);
    		InputSource is = new InputSource(sr);
    		System.out.println("+++ Client: parsing xml... +++");
    		reader.parse(is);
    		
    		/*
    		// --- Begin Validation --- //
    	    // create a SchemaFactory capable of understanding WXS schemas
    	    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    	    // load a WXS schema, represented by a Schema instance
    	    Source schemaFile = new StreamSource(new File("xml/bodyd_schema_client.xsd"));
    	    Schema schema = factory.newSchema(schemaFile);

    	    // create a Validator instance, which can be used to validate an instance document
    	    Validator validator = schema.newValidator();

    	    // validate the SAX source
    	    try {
    	    		validator.validate(new SAXSource(is));
    	         // parse it if it's valid
        		reader.parse(is);
    	    } catch (SAXException e) {
    	    		sendMessage(FAIL_MESSAGE);
    	    		System.out.println("xml document is invalid!");
    	    }
    	    // --- End Validation --- //
    	    */
    }
	
	protected void sendXmlRequest(String message)
	{
		if(message == null) return;
		
		this.xml = message;
		
		// ----- send a request to the server ----- //
		if(!Thread.interrupted() && socket.isConnected()) {
			try {
				this.out.write(xml.getBytes());
				this.out.write((byte)0); // EOM
				this.out.flush();
				System.out.println("message: " + xml + " sent to the BodyDaemon server");
			} catch(Exception e) {
				System.out.println("Error writing to the socket! " + e);
			}
		}
	}
	
    // open an input and output stream and start a new Thread
    public synchronized void start()
    {
    		if(runner == null) {
    			runner = new Thread(this);
    			runner.start();
    			running = true;
    		}
    }
    
    // interrupt the runner Thread and close the i/o streams
    public synchronized void stop()
    {
    		try {
    			if(runner != null) {
    				if(runner != Thread.currentThread())
    					runner.interrupt();
    				runner = null;
    			}
    			if(oscHandler != null) {
    				if(oscHandler != Thread.currentThread()) {
    					oscHandler.oscDisconnect();
    					oscHandler.interrupt();
    				}
    				oscHandler = null;
    			}
			disconnectFromServer();
			running = false;
    		} catch (IOException ioe) {
    			System.out.println("Error when closing disconnecting from server " + ioe);
    		}
    		handlers.removeElement(this);
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		// start the OSCHandler thread
		oscHandler = new OSCHandler(this);
		oscHandler.setDaemon(true);
		oscHandler.start();
		
		OSCMessage msg;
		
		// Get socket to the BodyDaemon server
		while(socket == null && !Thread.interrupted()) {
			try {
				connectToServer();
			} catch(IOException ex) {
				System.out.println("Error connecting to server at " + host);
				System.out.println(ex);
				System.out.println("Will try again in 30 seconds");
				try {
					Thread.sleep(30000);  // retry in 30 secs
				} catch (InterruptedException ie) {
					System.out.println("Thread " + this + " was interrupted: " + ie);
                }
            }
		} // end while loop
		
		// connected, now read data from the server
		try {
			handlers.addElement(this);
			while(running && !Thread.interrupted() && socket.isConnected()) {
				try {
					StringBuffer sb = new StringBuffer();
					byte[] buf = new byte[1024];
					// receive server messages
					int avail = in.available();
					while(avail > 0) {
						int amt = avail;
						if(amt > buf.length)
							amt = buf.length;
						try {
							amt = in.read(buf, 0, amt);
						} catch(SocketTimeoutException ste) {
							System.out.println("Socket timed out!  Server at " + host + " disconnected.");
							//socket.close();
							this.stop();
							break;
						} catch(IOException ioe) {
							System.out.println("Error reading from the socket! " + ioe);
						}
    			
						int marker = 0;
						for(int i = 0; i < amt; i++) {
							// scan for the zero-byte EOM (end of message) delimeter
							if(buf[i] == (byte)0) {
								String tmp = new String(buf, marker, i - marker);
								sb.append(tmp);
								// ============================================== //
								// handle the xml request... 
								// this will eventually trigger the xmlDataEvent
								// callback method which will send out OSC data
								// and data up to ClientApp
								// ============================================== //
								try {
									handleMessage(messageHandler, sb.toString());
								} catch(SAXException se) {
									System.out.println("=== Parsing error === " + se);
									System.out.println("sending request again...");
					        			 // clear the current context so we can start parsing from the top
					        			messageHandler.clearContext();
									sendXmlRequest(this.xml);;
								} catch (IOException ioex) {
									System.out.println(" === Error reading from the socket! === " + ioex);
									in.close();
									//socket.close();
									break;
								}
								clientPanel.drawLine("*** XML received from BodyDaemon server ***");
								clientPanel.drawLine(sb.toString());
								// set the length of the String buffer to 0
								sb.setLength(0);
								marker = i + 1;
							}
						} // end for loop
						if(marker < amt) {
							// save everything so far, still waiting for the final EOM
							sb.append(new String(buf, marker, amt - marker));
						}
						avail = in.available();
						// sleep for a little while to let any display operations work
						Thread.sleep(5);
					} // == end inner while loop == //
				} catch(RuntimeException re) {
					msg = new OSCMessage(all+failMsg, OSCMessage.NO_ARGS);
					oscHandler.sendOSC(msg);
					System.out.println("=== Server runtime error === " + "sending request again...");
	        			if(runner == Thread.currentThread())
	        				re.printStackTrace();
	        			 // clear the current context so we can start parsing from the top
	        			messageHandler.clearContext();
					sendXmlRequest(this.xml);
				} catch(SocketTimeoutException ste) {
					System.out.println("=== Socket timed out!  Server at " + host + " disconnected. ===");
					this.stop();
					//socket.close();
					break;
				} catch(Exception xx) {
					System.out.println("Error reading from the socket! " + xx);
					this.stop();
					break;
				}
			} // == end outer while loop == //
		} catch(Exception e) {
			System.out.println("=== Server error === " + e.getClass());
			if(runner == Thread.currentThread())
				e.printStackTrace();
		}
        finally {
        		if(running)
        			stop();
        }

	}

}
