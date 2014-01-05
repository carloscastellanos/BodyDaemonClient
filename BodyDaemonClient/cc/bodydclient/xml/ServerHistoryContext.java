/**
 * 
 */
package cc.bodydclient.xml;

import org.xml.sax.Attributes;

/**
 * @author carlos
 *
 */
public class ServerHistoryContext extends AbstractServerContext
{
	//private Attributes atts = null;
	private ServerMessageFormatter formatter = null;
	public static final String DATA = "data";
	protected AbstractServerContext parent;
	
	protected ServerHistoryContext()
	{
		
	}
	
	public ServerHistoryContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		//this.atts = atts;
		this.formatter = serverFormatter;
	}
	
	/* (non-Javadoc)
	 * @see cc.bodydclient.AbstractServerContext#processElement()
	 */
	public void processElement()
	{
		formatter.setMessageType(AbstractServerContext.HISTORY);
	}
	
	public String getName()
	{
		return AbstractServerContext.HISTORY;
	}

	public AbstractServerContext getChildContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		AbstractServerContext context = null;
		if(elementName.equalsIgnoreCase(DATA)) {
			context = new ServerDataContext(serverFormatter, elementName, atts);
			((ServerDataContext)context).setParentContext(this);
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
