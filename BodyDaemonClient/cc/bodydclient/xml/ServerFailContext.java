/**
 * 
 */
package cc.bodydclient.xml;

import org.xml.sax.Attributes;

/**
 * @author carlos
 *
 */
public class ServerFailContext extends AbstractServerContext
{
	
	public ServerFailContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		
	}
	
	public void processElement()
	{
		throw new RuntimeException("=== ServerFailContext: Error message received from server! ===");
	}

	public String getName()
	{
		return AbstractServerContext.BODYDML + " " + AbstractServerContext.BODYDML_STATUS_FAIL;
	}

	public void setParentContext(AbstractServerContext parent)
	{

	}
	
	public AbstractServerContext getParentContext()
	{
		return null;
	}

	public AbstractServerContext getChildContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts)
	{
		return null;
	}
	
	public void processElementValue(String s)
	{
		
	}
}
