/**
 * 
 */


/**
 * @author carlos
 *
 */

package cc.bodydclient.xml;

import java.util.Hashtable;

public class ServerMessageFormatter
{
	private Hashtable formattedData = null;
	private static final int hSize = 2;
	private String msgType = null;
	private String currTime = null;
	private String sensorType = null;
	
	public ServerMessageFormatter()
	{
		formattedData = new Hashtable();
	}
	
	public void setMessageType(String type)
	{
		formattedData.put(type, new Hashtable());
		msgType = type;
	}
	
	public void setSensor(String type, String value)
	{
		Hashtable h1, h2;
		h1 = (Hashtable)(formattedData.get(msgType));
		
		if(msgType.equalsIgnoreCase(AbstractServerContext.LIVEDATA)) {
			// put sensor Hashtable
			h1.put(type, new Hashtable(hSize));
			h2 = (Hashtable)(h1.get(type));
			// put the sensor value
			h2.put(type, value);
			h1.put(msgType, new Hashtable(h2));
			// save te sensor type
			sensorType = type;
		} else if(msgType.equalsIgnoreCase(AbstractServerContext.HISTORY)) {
			h2 = (Hashtable)h1.get(currTime);
			// put the sensor value
			h2.put(type, value);
			h1.put(currTime, new Hashtable(h2));
		}
		
		formattedData.put(msgType, new Hashtable(h1));
	}
	
	public void setServerProp(String type, String elementValue)
	{
		Hashtable h1, h2;
		h1 = (Hashtable)(formattedData.get(msgType));
		
		if(msgType.equalsIgnoreCase(AbstractServerContext.LIVEDATA)) {
			h2 = (Hashtable)(h1.get(sensorType));
			// put prop value
			h2.put(type, elementValue);
			h1.put(msgType, new Hashtable(h2));
		} else if(msgType.equalsIgnoreCase(AbstractServerContext.HISTORY)) {
			h2 = (Hashtable)h1.get(currTime);
			// put prop value
			h2.put(type, elementValue);
			h1.put(currTime, new Hashtable(h2));
		}
		
		formattedData.put(msgType, new Hashtable(h1));
	}
	
	public void setTime(String time)
	{
		Hashtable h1 = (Hashtable)(formattedData.get(msgType));
		// put time Hashtable
		h1.put(time, new Hashtable());
		formattedData.put(msgType, new Hashtable(h1));
		// save the time
		currTime = time;
	}
	
	public void clear()
	{
		formattedData.clear();
	}
	
	public Hashtable getFormattedData()
	{	
		return formattedData;
	}
}
