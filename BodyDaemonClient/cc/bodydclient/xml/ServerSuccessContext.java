/**
 * 
 */
package cc.bodydclient.xml;

import org.xml.sax.Attributes;

/**
 * @author carlos
 *
 */
public class ServerSuccessContext extends AbstractServerContext
{
	protected ServerSuccessContext()
	{
		
	}
	
	public ServerSuccessContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		//this.formatter = serverFormatter;
		//this.atts = atts;
	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#processElement()
	 */
	public void processElement()
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#getName()
	 */
	public String getName()
	{
		return AbstractServerContext.BODYDML + " " + AbstractServerContext.BODYDML_STATUS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#getChildContext(cc.bodydclient.xml.ServerMessageFormatter, java.lang.String, org.xml.sax.Attributes)
	 */
	public AbstractServerContext getChildContext(
			ServerMessageFormatter serverFormatter, String elementName,
			Attributes atts)
	{
		AbstractServerContext context = null;
		if(elementName.equalsIgnoreCase(AbstractServerContext.HISTORY)) {
			context = new ServerHistoryContext(serverFormatter, elementName, atts);
			((ServerHistoryContext)context).setParentContext(this);
		} else {
			context = new ServerLivedataContext(serverFormatter, elementName, atts);
			((ServerLivedataContext)context).setParentContext(this);
		}
		return context;
	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#setParentContext(cc.bodydclient.xml.AbstractServerContext)
	 */
	public void setParentContext(AbstractServerContext context)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#getParentContext()
	 */
	public AbstractServerContext getParentContext()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cc.bodydclient.xml.AbstractServerContext#processElementValue(java.lang.String)
	 */
	public void processElementValue(String s)
	{
		// TODO Auto-generated method stub

	}

}
