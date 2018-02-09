package Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class StudentClient extends Thread {
	String serverIP;
	int hostPort;
	
	int id;
	
	String type = "Student";
	

	public StudentClient(String ip, int port, int i) {
		serverIP = ip;
		hostPort = port;
		id = i;
	}

	public void run() {		
		try {
			Socket soc = new Socket(serverIP, hostPort);
			PrintWriter pw = new PrintWriter(soc.getOutputStream());
			BufferedReader brf = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			
			pw.println(type);
			pw.println(id);
			
			pw.println("arrive");
			pw.println("fileIn");
			pw.println("takeASeat");
			pw.println("getGrade");
			
			System.out.println("StudentClient-" + id + ": writing to Helper" + '\n'
								+ "   " + type + '\n'
								+ "   " + id + '\n'
								+ "   " + "arrive" + '\n'
								+ "   " + "fileIn" + '\n'
								+ "   " + "takeASeat" + '\n'
								+ "   " + "getGrade");
			
			pw.flush();
			
			String line;
			while ((line = brf.readLine()) == null) {}
			
			System.out.println(line);
			
			soc.close();
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
