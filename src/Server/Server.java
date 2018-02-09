package Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class Server extends Thread {
	int port;
	int helpers = 0;
	
    Server(int p) {
    	port = p;
    }
    
    public void run() {
        try {
        	URL connect = new URL("http://checkip.amazonaws.com/");
    	    URLConnection con = connect.openConnection();
    	    String str = null;
    	    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	    str = reader.readLine();
    	    
    		ServerSocket server = new ServerSocket(port);
    		
    	    System.out.println("Server: local IP: " + InetAddress.getLocalHost().getHostAddress());
    	    System.out.println("Server: external IP: " + str);
    		System.out.println("Server: started on port " + port);
    		
        	while (true) {
        		System.out.println("Server: listening for connection...");
        		Socket connection = server.accept();
        		System.out.println("Server: connection established!");
        		helpers++;
        		System.out.println("Server: creating Helper " + helpers);
        		Helper help = new Helper(connection);
        		help.start();
        	}
        }
        
        catch (Exception e) {
			System.out.println("Unable to listen to port.");
			e.printStackTrace();
        }
    }

	public static void main(String[] args) {
	    if (args.length != 1) {
	    	System.out.println("Invalid argument: <host_port>");
	        System.exit(1);
	    }
	    
		Server pro2 = new Server(Integer.parseInt(args[0]));
		pro2.start();
	}
}