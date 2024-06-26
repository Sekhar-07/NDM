import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
public class ClientHandler extends Thread {
	Socket client =null;
	JDesktopPane desktop =null;
	JFrame jf =null;
	String clientIP;
	   JTextArea properties;
	int port =5555;
	JInternalFrame iframe = new JInternalFrame("Client screen",true,true);
	JPanel panel = new JPanel();
	boolean c =true;
	public ClientHandler(Socket client,JDesktopPane desktop,int port) {
		this.client = client;
		this.desktop = desktop;
	    this.port =port;
		clientIP =(((InetSocketAddress) client.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
		System.out.println(port);
		start();
	}
	
	public void run() {
		ServerMainFrame();
        Rectangle clientScreenDim = null;
        ObjectInputStream ois = null;
        drawGUI();
   
        try{
            ois = new ObjectInputStream(client.getInputStream());
            clientScreenDim =(Rectangle) ois.readObject();
            System.out.println("dim Received");
        }catch(IOException ex){
        	System.out.println("dim not Received");
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
        	System.out.println("dim not Received");
            ex.printStackTrace();
        }
        
        try{
          @SuppressWarnings("unchecked")
		HashMap<String,String> props  =(HashMap<String,String>) ois.readObject();
          for (Map.Entry<String,String> p : props.entrySet()) {
        	  properties.append("\n"+ p.getKey() + " : "+p.getValue());
          }

        }catch(IOException ex){

            ex.printStackTrace();
        }catch(ClassNotFoundException ex){

            ex.printStackTrace();
        }
        
        new ScreenReceiver(ois,panel);
        new CommandSender(client,panel,clientScreenDim);
	}
	
	public void drawGUI() {
		iframe.setLayout(new BorderLayout());
        iframe.getContentPane().add(panel,BorderLayout.CENTER);
        iframe.setSize(100,100);
        desktop.add(iframe);
        try {
            iframe.setMaximum(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
        panel.setFocusable(true);
        iframe.setVisible(true);
	}
	  public void ServerMainFrame() {
	    	jf = new JFrame("This is Server Window");
	    	jf.setSize(800,820);
	    	JTabbedPane jtp = new JTabbedPane();
	    	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	jtp.addTab("Port Scanner",new PortScan());
	    	jtp.addTab("System Properties",new SystemProperties());
	    	jtp.addTab("Running Process", null);
	    	jtp.add("Desktop Locking", null);
	    	jf.add(jtp);
	    	jf.setVisible(true);
	    	
	    }
	   private class PortScan extends JPanel{
		   JButton tcpConnect =null ,tcpSyn=null ,udp =null;
		   JTextArea jtxt=null;
		   PortScan(){
			   setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			   jtxt = new JTextArea(5,45);
			   jtxt.setFont(new Font("Monaco", Font.PLAIN, 15));
			   jtxt.setMargin(new Insets(12, 12, 12, 12));
			   JScrollPane jsp = new JScrollPane(jtxt);
			   tcpSyn = new JButton("TCP SYN Scan");
			   tcpSyn.setAlignmentX(CENTER_ALIGNMENT);
			   tcpSyn.setMaximumSize(new Dimension(300,50));
			   tcpSyn.setMaximumSize(new Dimension(300,50));
			   
				tcpSyn.addActionListener(new ActionListener() { 
					  public void actionPerformed(ActionEvent e) { 
						
						  new PortScanner().TcpSYN(clientIP,port,jtxt);
					   
					  } 
					  
					} );
				tcpConnect = new JButton("TCP CONNECT Scan");
				tcpConnect.setAlignmentX(CENTER_ALIGNMENT);
				tcpConnect.setMaximumSize(new Dimension(300,50));
				tcpConnect.setMaximumSize(new Dimension(300,50));
			
				tcpConnect.addActionListener(new ActionListener() { 
					  public void actionPerformed(ActionEvent e) { 
						 
						  new PortScanner().TcpConnect(clientIP,port,jtxt);
						
					   
					  } 
					  
					} );
				udp = new JButton("UDP Scan");
				udp.setAlignmentX(CENTER_ALIGNMENT);
				udp.setMaximumSize(new Dimension(300,50));
				udp.setMaximumSize(new Dimension(300,50));
			
				udp.addActionListener(new ActionListener() { 
					  public void actionPerformed(ActionEvent e) { 
						  new PortScanner().UdpScan(clientIP,port,jtxt);
					   
					  } 
					  
					} );
				
			
				
				add(Box.createVerticalStrut(20));
				add(tcpSyn);
				add(Box.createVerticalStrut(20));
				add(tcpConnect);
				add(Box.createVerticalStrut(20));
				add(udp);
				add(Box.createVerticalStrut(20));
				add(jsp);
		   }
	   }
	   
	   private class SystemProperties extends JPanel{
		   public SystemProperties(){
			   properties = new JTextArea(40,50);
			   properties.setFont(new Font("Monaco", Font.PLAIN, 15));
			   properties.setMargin(new Insets(12, 12, 12, 12));
			   properties.setCaretPosition(0);
			   JScrollPane jsp = new JScrollPane(properties);
			   add(jsp);
			 
		   }
	   }
	   
	   private class DesktopLock extends JPanel{
		   
		   public DesktopLock() {
			   
		   }
	   }
	   
	   

}