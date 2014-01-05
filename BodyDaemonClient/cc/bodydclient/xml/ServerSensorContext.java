/**
 * 
 */

/**
 * @author carlos
 *
 */
package cc.bodydclient.xml;

import org.xml.sax.Attributes;

public class ServerSensorContext extends AbstractServerContext
{
	public static final String SERVERPROP = "serverprop";
	private Attributes atts = null;
	private ServerMessageFormatter formatter = null;
	protected AbstractServerContext parent;
	
	protected ServerSensorContext()
	{
		
	}
	
	public ServerSensorContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		this.formatter = serverFormatter;
		this.atts = atts;
	}
	
	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#processElement()
	 */
	public void processElement()
	{
		String type = atts.getValue("type");
		String value =  atts.getValue("value");
		formatter.setSensor(type, value);
	}
	
	public String getName()
	{
		return ServerLivedataContext.SENSOR;
	}

	public void setParentContext(AbstractServerContext parent)
	{
		this.parent = parent;
	}
	
	public AbstractServerContext getParentContext()
	{
		return parent;
	}

	public AbstractServerContext getChildContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		AbstractServerContext context = null;
		if(elementName.equalsIgnoreCase(SERVERPROP)) {
			context = new ServerServerpropContext(serverFormatter, elementName, atts);
			((ServerServerpropContext)context).setParentContext(this);
		}
		return context;
	}
	
	public void processElementValue(String s)
	{	
	}
}
