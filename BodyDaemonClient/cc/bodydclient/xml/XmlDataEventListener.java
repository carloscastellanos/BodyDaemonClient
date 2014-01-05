/**
 * 
 */

/**
 * @author carlos
 *
 */

package cc.bodydclient.xml;

import java.util.Hashtable;
import java.util.EventListener;

public interface XmlDataEventListener extends EventListener
{
	public abstract void xmlDataEvent(Hashtable data);
}
