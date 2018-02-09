package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Helper extends Thread {
	String threadType;
	String methodName;
	
	Socket incoming;
	
	Student student;
	Teacher teacher;
	
	Object lock = new Object();
	
	int id;
	static int total = 0;
	
	static Vector<Student> students = new Vector<Student>();
	static Vector<Teacher> teachers = new Vector<Teacher>();
	
	
	Helper(Socket soc) {
		incoming = soc;
	}
	
	public void run() {
		synchronized (lock) {
			total++;
			id = total;
		}
		
		try {
			System.out.println("Helper-" + id + ": started");
			BufferedReader brf = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			PrintWriter pw = new PrintWriter(incoming.getOutputStream());
			
			String number;
			String capacity;
			String numSeats;
			String method;
			String type;
			
			while ((type = brf.readLine()) == null) {}

			if (type.equals("Student")) {
				number = brf.readLine();
				
				System.out.println("Helper-" + id + ": reading " + type + " from StudentClient-" + number);
				pw.println("SERVER REPLY! Helper-" + id + ": received message from StudentClient-" + number);
				pw.flush();
				
				while(teachers.isEmpty()) {}
				
				student = new Student(Integer.parseInt(number), teachers.get(0));
				System.out.println("Helper-" + id + ": creating " + type + "-" + number);
				students.add(student);
				
				for (int i = 0; i <= 3; i++) {
					method = brf.readLine();
					System.out.println("Helper-" + id + ": reading " + method + " from StudentClient-" + number);
					method(type, method);
				}
			}
			
			
			else if (type.equals("Teacher")) {
				number = brf.readLine();
				capacity = brf.readLine();
				numSeats = brf.readLine();
				
				System.out.println("Helper-" + id + ": reading " + type + " from TeacherClient-" + number);
				pw.println("SERVER REPLY! Helper-" + id + ": received message from TeacherClient-" + number);
				pw.flush();
			
				teacher = new Teacher(Integer.parseInt(number), Integer.parseInt(capacity), Integer.parseInt(numSeats));
				System.out.println("Helper-" + id + ": creating " + type + "-" + number);
				
				if (teachers.isEmpty()) {
					teachers.add(teacher);
				}
				
				else {
					teachers.set(0, teacher);
				}
				
				for (int i = 0; i <= 3; i++) {
					method = brf.readLine();
					System.out.println("Helper-" + id + ": reading " + method + " from TeacherClient");
					method(type, method);
				}
			}
			
			incoming.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void method(String type, String m) {
        if (type == "Student") {
        	System.out.println("Student-" + student.getId() + ": running " + m);
        }
        
        else if (type == "Teacher") {
        	System.out.println("Teacher-" + teacher.getId() + ": running " + m);
        }
        
		switch (m) {
        //STUDENT
        	case "arrive":  		
        		student.arrive();
            	break;
            case "fileIn":
            	student.fileIn();
            	break;
            case "takeASeat":
            	student.takeASeat();
            	break;
            case "getGrade":
            	student.getGrade();
            	break;
            	
        //TEACHER
            case "open":
            	teacher.open();
            	break;
            case "close":
            	teacher.close();
            	break;    	
            case "startExam":
            	teacher.startExam();
            	break;
            case "endExam":
            	teacher.finishExam();
            	break;
            	
            default: System.out.println("Invalid method name");
            	break;
        }
	}
}
