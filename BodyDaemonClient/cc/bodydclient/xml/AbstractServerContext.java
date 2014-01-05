/**
 * 
 */

/**
 * @author carlos
 *
 */

package cc.bodydclient.xml;

import org.xml.sax.Attributes;

public abstract class AbstractServerContext
{
	public static final String BODYDML = "bodydml";
	public static final String BODYDML_STATUS_SUCCESS = "success";
	public static final String BODYDML_STATUS_FAIL = "fail";
	public static final String LIVEDATA = "livedata";
	public static final String HISTORY = "history";
	
	protected AbstractServerContext()
	{
	}
	
	protected AbstractServerContext(ServerMessageFormatter serverFormatter,
											String elementName, Attributes atts)
	{
	}
	
	// return the appropriate context for the given elemeent
	public static AbstractServerContext getContext(ServerMessageFormatter serverFormatter,
											String elementName, Attributes atts)
	{
		AbstractServerContext context = null;
		
		// check the bodydml elements
		if(elementName.equalsIgnoreCase(BODYDML)) {
			if((atts.getValue("status").equalsIgnoreCase(BODYDML_STATUS_FAIL)))
				context = new ServerFailContext(serverFormatter, elementName, atts);
			else
				context = new ServerSuccessContext(serverFormatter, elementName, atts);
		} else if(elementName.equalsIgnoreCase(LIVEDATA)) {
			context = new ServerLivedataContext(serverFormatter, elementName, atts);
		} else if(elementName.equalsIgnoreCase(HISTORY)) {
			context = new ServerHistoryContext(serverFormatter, elementName, atts);
		}
		
		return context;
	}
	
	public abstract void processElement();
	
	public abstract String getName();
	
	public abstract AbstractServerContext getChildContext(ServerMessageFormatter serverFormatter,
			String elementName, Attributes atts);
	public abstract void setParentContext(AbstractServerContext context);
	public abstract AbstractServerContext getParentContext();
	public abstract void processElementValue(String s);
}
