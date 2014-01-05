/**
 * 
 */

/**
 * @author carlos
 *
 */

package cc.bodydclient;

import java.util.Vector;

public class Client implements Runnable
{
	private String host = null;
	private int port = 59000;
	private Thread runner = null; // we're running this class as a thread
	private boolean running = false;
	private static ClientHandler clientHandler = null; // seperate thread for i/o operations with the BodyDaemon server
	
	String xmlMsg;
    public static final String DEFAULT_XML_MSG = 
    			ClientHandler.XML_DECL + "<bodydml requesttype=\"default\"></bodydml>";
    

    // store a list of all the current Client objects (will probably only be one)
    static Vector clients = new Vector();
    
    // ClientApp
	private ClientPanel clientPanel;
    
    // constructors
	public Client(String host, int port, ClientPanel panel)
    {
		this.host = host;
		this.port = port;
    		this.clientPanel = panel;
    		this.xmlMsg = DEFAULT_XML_MSG;
    }
    
    public Client(String host, int port, ClientPanel panel, String msg)
    {
    		this.host = host;
    		this.port = port;
    		this.clientPanel = panel;
    		this.xmlMsg = msg;
    }

    // open an input and output stream and start a new Thread
    public synchronized void start()
    {
    		if(runner == null) {
    			runner = new Thread(this);
    			runner.start();
    			running = true;
    			System.out.println("*** BodyDClient started. ***");
    		}
    }
    
    // interrupt the runner Thread and close the i/o streams
    public synchronized void stop()
    {
    		clientHandler.stop();
    		if(runner != null) {
    			if(runner != Thread.currentThread())
    				runner.interrupt();
    			runner = null;
    		}
		running = false;
		clientHandler = null;
    		clients.removeElement(this);
    		System.out.println("*** BodyDClient stopped. ***");
    }
    
	public void run()
	{
		clients.addElement(this);
		clientHandler = new ClientHandler(host, port, xmlMsg, clientPanel);
		clientHandler.start();

		while(running && !Thread.interrupted()) {
			
		}
		
		//stop();
	}
	
/*
	private void notifyClientApp()
	{
		app.prop1 = xxx;
		app.prop2 = xxx;
		// etc...
	}
*/	
	/*
    public void addXmlClientEventListener(XmlClientEventListener xmlcel)
    {
    		if(xmlcel != null && xmlClientListeners.indexOf(xmlcel) == -1) {
    			xmlClientListeners.add(xmlcel);
    			System.out.println("[+ XmlClientEventListener] " + xmlcel);
        }
    	
    }

    public void removeXmlClientEventListener(XmlDataEventListener xmlcel)
    {
    		if(xmlClientListeners.contains(xmlcel)) {
            xmlClientListeners.remove(xmlClientListeners.indexOf(xmlcel));
            System.out.println("[- XmlClientEventListener] " + xmlcel);
        }
    }
    
    private void notifyXmlClientEventListeners(Hashtable data)
    {
	    if(xmlClientListeners == null) {
	        return;
        } else {
            ListIterator iter = xmlClientListeners.listIterator();
            while(iter.hasNext()) {
                	((XmlClientEventListener) iter.next()).xmlClientEvent(data);
            	}
        } 
    }
    */
	
	
}
