package cc.bodydclient;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class ClientApp
{
	Client client;
	String host;
	int port;
	JFrame parentFrame;
	ClientPanel clientPanel;
	
	public ClientApp(JFrame parentF, String host)
	{
		parentFrame = parentF;
		this.host = host;
		this.port = 59000; // default BodyDaemon port is 59000
	}
	
	public ClientApp(JFrame parentF, String host, int port)
	{
		parentFrame = parentF;
		this.host = host;
		this.port = port;
	}
	
    void start()
    {
    		// Construct the main panel:
		clientPanel = new ClientPanel(parentFrame.getBounds().width, parentFrame.getBounds().height);
		clientPanel.setLocation(0,0);
		parentFrame.getContentPane().add(clientPanel);
		parentFrame.show();
		
		if (clientPanel != null)
			clientPanel.init();
	      
    		client = new Client(host, port, clientPanel);
    		client.start();
    }

	public static void main(String args[])
	{
		// Setup custom event trapping (to trap escape key globally)
		// NOTE: this won't work with MS Jview (comment out if running in that JRE)
		// NOTE: this interferes with Alt-F4 key (OS key to close window)
		// thanks to Mark Napier for this code
		EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
		eq.push(new ClientAppEventQueue());
		
		
		String host;
		int port;
		if((args.length != 1))
			throw new IllegalArgumentException("Usage: ClientApp <host> or <host>:<port>");
		else if(args[0].indexOf(":") < 0) {
			host = args[0];
			port = -1;
		} else {
			int idx = args[0].indexOf (":");
			host = args[0].substring(0, idx);
			port = Integer.parseInt (args[0].substring(idx + 1));
        }
        
		// GUI stuff
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("BodyDaemon Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 400, 300);
		frame.setBackground(Color.white);
		frame.setForeground(Color.black);
		frame.setVisible(true);
		
		// ClientApp
		ClientApp clientApp;
		if(port == -1) {
			// Make the ClientApp Window from frame
			clientApp = new ClientApp(frame, host);
		} else {
			clientApp = new ClientApp(frame, host, port);			
		}
		//clientApp.setVisible(true);
		clientApp.start();
	} 
}
