/**
 * 
 */

/**
 * @author carlos
 *
 */

package cc.bodydclient.xml;

import org.xml.sax.Attributes;

public class ServerLivedataContext extends AbstractServerContext
{
	//private Attributes atts = null;
	private ServerMessageFormatter formatter = null;
	public static final String SENSOR = "sensor";
	protected AbstractServerContext parent;
	
	protected ServerLivedataContext()
	{
		
	}
	
	public ServerLivedataContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		this.formatter = serverFormatter;
		//this.atts = atts;
	}
	
	public void processElement()
	{
		formatter.setMessageType(AbstractServerContext.LIVEDATA);
	}
	
	public String getName()
	{
		return AbstractServerContext.LIVEDATA;
	}

	public AbstractServerContext getChildContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
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
		return  null;
	}
	
	public void processElementValue(String s) { }
}
