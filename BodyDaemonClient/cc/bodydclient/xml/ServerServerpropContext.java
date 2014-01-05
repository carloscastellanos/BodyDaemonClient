/**
 * 
 */
package cc.bodydclient.xml;

import org.xml.sax.Attributes;

/**
 * @author carlos
 *
 */
public class ServerServerpropContext extends AbstractServerContext
{
	private Attributes atts = null;
	private ServerMessageFormatter formatter = null;
	protected AbstractServerContext parent;
	private String type = null;
	
	protected ServerServerpropContext()
	{
		
	}
	
	public ServerServerpropContext(ServerMessageFormatter serverFormatter,
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
		type = atts.getValue("type");
	}
	
	public String getName()
	{
		return ServerSensorContext.SERVERPROP;
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
		return null;
	}
	
	public void processElementValue(String s)
	{
		if(type != null) {
			formatter.setServerProp(type, s);
			type = null;
		}
	}

}
