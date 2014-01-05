/**
 * 
 */
package cc.bodydclient.xml;

import org.xml.sax.Attributes;

/**
 * @author carlos
 *
 */
public class ServerDataContext extends AbstractServerContext
{

	private Attributes atts = null;
	private ServerMessageFormatter formatter = null;
	public static final String SENSOR = "sensor";
	protected AbstractServerContext parent;
	
	protected ServerDataContext()
	{
		
	}
	
	public ServerDataContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		this.atts = atts;
		this.formatter = serverFormatter;
	}
	
	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#processElement()
	 */
	public void processElement()
	{
		String time = atts.getValue("time");
		formatter.setTime(time);
	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#getName()
	 */
	public String getName()
	{
		return ServerHistoryContext.DATA;
	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#getChildContext(cc.bodydclient.xml.ServerMessageFormatter, java.lang.String, org.xml.sax.Attributes)
	 */
	public AbstractServerContext getChildContext(
			ServerMessageFormatter serverFormatter, String elementName,
			Attributes atts)
	{
		AbstractServerContext context = null;
		if(elementName.equalsIgnoreCase(SENSOR)) {
			context = new ServerSensorContext(serverFormatter, elementName, atts);
			((ServerSensorContext)context).setParentContext(this);
		}
		return context;
	}

	public void setParentContext(AbstractServerContext parent)
	{
		this.parent = parent;
	}
	
	public AbstractServerContext getParentContext()
	{
		return parent;
	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#processElementValue(java.lang.String)
	 */
	public void processElementValue(String s)
	{
	}

}
