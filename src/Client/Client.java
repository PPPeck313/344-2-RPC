package Client;
import java.util.Vector;

public class Client extends Thread {
	String serverIP;
	int hostPort;
	
	int numStudents;
	int numTeachers;
	int capacity;
	int numSeats;
	
	public Vector<StudentClient> studentClients = new Vector<StudentClient>();
	public Vector<TeacherClient> teacherClients = new Vector<TeacherClient>();
	
	Client(String ip, int p, int st, int cap, int se) {
		serverIP = ip;
		hostPort = p;
		numStudents = st;
		numTeachers = 1;
		capacity = cap;
		numSeats = se;
	}
	
	public void run() {
		for (int i = 0; i < numTeachers; i++) {
			teacherClients.add(new TeacherClient(serverIP, hostPort, i + 1, capacity, numSeats));
			teacherClients.get(i).start();
		}
		
		for (int i = 0; i < numStudents; i++) {
			studentClients.add(new StudentClient(serverIP, hostPort, i + 1));
			studentClients.get(i).start();
		}
	}
	
	public static void main(String[] args) {
	    if (args.length != 5) {
	    	System.out.println("Invalid argument: <host_ip> <host_port> <num_students> <class_capacity> <num_seats>");
	        System.exit(1);
	    }
	    
	    String ip = args[0];
	    int port = Integer.valueOf(args[1]);
	    int students = Integer.valueOf(args[2]);
		int cap = Integer.valueOf(args[3]);
		int seats = Integer.valueOf(args[4]);
		
		Client cli = new Client(ip, port, students, cap, seats);
		cli.start();
	}
}
