package Server;
import java.util.Random;
import java.util.Vector;

class Teacher extends Thread {
	public static long time = System.currentTimeMillis();
	
	public int administered = 0;
	public int administeredMax = 1;
	
	public int arrived = 0;
	public int waiting = 0;
	public int current = 0;
	public int capacity = 0;
	
	public int numSeats = 0;
	public int numTables = 0;
	
	//multiple locks for specificity
	public Object outside = new Object();
	public Object inside = new Object();
	public Object teacher = new Object();
	public Object exam = new Object();
	
	public Vector<Object> tables = new Vector<Object>(numTables);
	public Vector<Student> line = new Vector<Student>();
	public Vector<Object> grading = new Vector<Object>();
	
	
	/*
	 * RUN
	 */
    public void run() {
    	while (administered < administeredMax) {//Four exams are administrated throughout the day. administeredMax = 4
	    	open();
	    	close();
	    	startExam();
	    	finishExam();
	    }//The instructor will terminate after all four exams are administered.
    	
    	dismiss();
    	
    	msg("Survived exam day!");
    }
	
	
	/* 
	 * CONSTRUCTOR 
	 */
	public Teacher(int id, int cap, int seats) {
		setName("Teacher-" + id);
		capacity = cap;
		numSeats = seats;
		numTables = capacity / numSeats;//Each table has numSeats seats.

		for (int i = 0; i <= numTables; i++) {
			tables.add(new Object());//Students that sit at the same table should wait on the same object.
		}
	}
	
	
	/* 
	 * HELPER FUNCTIONS 
	 */
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis() - time) + "] "+ getName() + ": " + m);
	}

	public void wait(Object ob) {
		try {
			ob.wait();
		}
		
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
    public void sleep(int seconds, String verb) {
    	try {
    		int time = seconds * 1000;//convert to milliseconds
    		msg(verb + " for " + time + " ms");
    		Thread.sleep(time);
    	}
    	
    	catch (InterruptedException e) {
    		msg(verb + " interrupted!");
    	}
    }
	
    public void sleepRandom(int seconds, String verb) {
    	try {
    		Random rand = new Random();
    		int time = rand.nextInt((seconds * 1000) + 1);//convert to milliseconds
    		msg(verb + " for " + time + " ms");
    		Thread.sleep(time);
    	}
    	
    	catch (InterruptedException e) {
    		msg(verb + " interrupted!");
    	}
    }
    
    public void doubleCheck() {
    	if (current != 0) {
    		synchronized (teacher) {
    			wait(teacher);
    		}
    	}
    }
    
    
    /* 
     * PRIMARY FUNCTIONS 
     */
    public void open() {
    	sleepRandom(5, "Commuting");
    
	    waiting = 0;
	    		
	    synchronized (outside) {
		   	outside.notifyAll();
	    }
	    		
	    msg("Classroom opened. Waiting for students to be seated.");
    }
    	
    public void close() {
    	try {
    		synchronized (teacher) {
    			teacher.wait(1000);
    		}
		} 
    		
    	catch (InterruptedException e) {
			e.printStackTrace();
		}
    		
    	synchronized (inside) {
    		inside.notify();
    	}
    		
    	doubleCheck();
    }
    
    public void startExam() {
    	msg("Handing out exams.");
		
    	waiting = 0;
    		
	    for (int i = 0; i <= numTables; i++) {
	    	synchronized (tables.get(i)) {
	    		tables.get(i).notifyAll();
	    	}
    	}//When it’s time to start the exam, the instructor hands out the exams (by signaling the students)
    		
	    doubleCheck();
	    
	    msg("Setting timer. Test has begun.");
    		
    	try {
    		synchronized (teacher) {
    			teacher.wait(5000);
    		}
		} 
    	
    	catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void finishExam() {
    	synchronized (exam) {
    		exam.notifyAll();
    	}
    		
    	waiting = 0;
    		
    	doubleCheck();
    		
    	while (current > 0) {
    		gradeTest();
    	}
    		
    	administered++;
    	msg("Administered " + administered + " exam(s)");
    	
    	if (administered == administeredMax) {	 
    		dismiss();
    	}
    	
    	else {
    		sleep(5, "Prepping for next exam");//The exams are scheduled every 2 hours
    	}
    }
    
    public void gradeTest() {
		Random rand = new Random();
		
		if (!line.isEmpty()) {
    		sleepRandom(2, "Grading " + line.get(0).getName() + "'s exam");
    		//It will take the instructor a very brief (random) time (up to 2 units) to check an exam. FCFS.
    		
    		int grade = rand.nextInt(100 + 1);
    		msg("Gave " + line.get(0).getName() + " a " + grade);
    		//After each exam is checked, the instructor will assign a grade and notify the student.
    			
    		line.get(0).grades.add(grade);
    		line.remove(0);
    			
    		Object notOb = grading.get(0);
    		grading.remove(notOb);
    			
    		synchronized (notOb) {
    			notOb.notify();
    		}
		}
    }
    
    public void dismiss() {
       	while (arrived != 0) {
	    	synchronized (outside) {	
	    		outside.notifyAll();
			}
	    	
	    	try {
	    		synchronized (teacher) {
	    			teacher.wait(1000);
	    		}
			} 
	    	
	    	catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
       	
       	msg("Survived exam day!");
    }
}