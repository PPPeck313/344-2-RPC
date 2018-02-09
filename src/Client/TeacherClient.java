package Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TeacherClient extends Thread {
	String serverIP;
	int hostPort;
	
	int id;
	int capacity;
	int numSeats;
	
	String type = "Teacher";
	
	public TeacherClient(String ip, int port, int i, int cap, int se) {
		serverIP = ip;
		hostPort = port;
		id = i;
		capacity = cap;
		numSeats = se;
	}

	public void run() {
		try {
			Socket soc = new Socket(serverIP, hostPort);
			PrintWriter pw = new PrintWriter(soc.getOutputStream());
			BufferedReader brf = new BufferedReader(new InputStreamReader(soc.getInputStream()));

			pw.println(type);
			pw.println(id);
			pw.println(capacity);
			pw.println(numSeats);
			
			pw.println("open");
			pw.println("close");
			pw.println("startExam");
			pw.println("endExam");
			
			System.out.println("TeacherClient-" + id + ": writing to Helper" + '\n'
								+ "   " + type + '\n'
								+ "   " + id + '\n'
								+ "   " + capacity + '\n'
								+ "   " + numSeats + '\n'
								+ "   " + "open" + '\n'
								+ "   " + "close" + '\n'
								+ "   " + "startExam" + '\n'
								+ "   " + "endExam");
			
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