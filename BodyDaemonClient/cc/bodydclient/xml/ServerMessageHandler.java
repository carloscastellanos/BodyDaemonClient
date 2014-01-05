/**
 * 
 */

/**
 * @author carlos
 *
 */

package cc.bodydclient.xml;

import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ServerMessageHandler extends DefaultHandler
{
	private static Vector xmlListeners = new Vector();
	private ServerMessageFormatter formatter = null;
	private AbstractServerContext context = null;
	private AbstractServerContext prevContext = null;
	private StringBuffer buffer = null;
	
	public ServerMessageHandler()
	{
		formatter = new ServerMessageFormatter();
	}
	
	public void startElement(String namepaceURI, String localName,
					String qName, Attributes atts) throws SAXException
	{
		System.out.println("startElement " + localName);
		if (localName.equalsIgnoreCase(ServerSensorContext.SERVERPROP)) {
			buffer = new StringBuffer();
		}
		if(prevContext != null) {
			// assign the child of the previous context to the current one
			// make sure we're not at the deepest node/element (<serverprop>)
			if((context = prevContext.getChildContext(formatter, localName, atts)) == null) {
				// if we are then go back to the parent (<sensor>)
				// we do it this way (instead of just calling prevContext.getParentContext())
				// so that we can get send the new attributes passed to this method
				AbstractServerContext newContext = prevContext.getParentContext().getParentContext();
				context = newContext.getChildContext(formatter, localName, atts);
			}
		} else {
			context = AbstractServerContext.getContext(formatter, localName, atts);
		}
		// save the current context for the next time startElemet() is called
		prevContext = context;
		// process the element with the given context
		//System.out.println("context=" + context.getName());
		context.processElement();
	}
	
	public void endElement(String namepaceURI, String localName,
			String qName) throws SAXException
	{
		String accumulatedText = null;
		System.out.println("endElement " + localName);
		if (localName.equalsIgnoreCase(ServerSensorContext.SERVERPROP)) {
			accumulatedText = buffer.toString();
			System.out.println("--elementValue=" + accumulatedText);
			buffer = null;
		}
		
		if(context.getName().equalsIgnoreCase(ServerSensorContext.SERVERPROP) && accumulatedText != null)
			context.processElementValue(accumulatedText);
	}
	
	public void characters(char[] text, int start, int length) throws SAXException
	{
		/*
		if(context.getName().equalsIgnoreCase(ServerSensorContext.SERVERPROP)) {
			String str = new String(text);
			context.processElementValue(str);
		}
		*/
		
		if (buffer != null) {
			buffer.append(text, start, length); 
		}
	}
	
	public void startDocument() throws SAXException
	{
		System.out.println("startDocument");	
	}
	
	public void endDocument() throws SAXException
	{
		System.out.println("endDocument");
		prevContext = null;
		context = null;
		notifyXmlDataEventListeners(formatter.getFormattedData());
		//formatter.clear();
		
	}
	
	public void fatalError(SAXParseException e) throws SAXException
	{
		System.out.println("### Fatal xml parsing error! ### " + e);
	}
	
	public void warning(SAXParseException e) throws SAXException
	{
		System.out.println("### xml parse warning ### " + e);
	}
	
	public void clearContext()
	{
		prevContext = null;
		context = null;
	}
	
    public synchronized void addXmlDataEventListener(XmlDataEventListener xmldel)
    {
    		if(xmldel != null && xmlListeners.indexOf(xmldel) == -1) {
    			xmlListeners.add(xmldel);
    			System.out.println("[+ XmlDataEventListener] " + xmldel);
        }
    	
    }

    public synchronized void removeXmlDataEventListener(XmlDataEventListener xmldel)
    {
    		if(xmlListeners.contains(xmldel)) {
            xmlListeners.remove(xmlListeners.indexOf(xmldel));
            System.out.println("[- XmlDataEventListener] " + xmldel);
        }
    }
    
    private synchronized void notifyXmlDataEventListeners(Hashtable data)
    {
	    if(xmlListeners == null) {
	        return;
        } else {
            ListIterator iter = xmlListeners.listIterator();
            while(iter.hasNext()) {
                	((XmlDataEventListener) iter.next()).xmlDataEvent(new Hashtable(data));
            	}
        } 
    }
}
