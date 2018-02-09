package Server;
// SocketInfo.java
import java.net.*;

public class SocketInfo {
	public static void main(String args[]) {
		System.out.println("Remote Address\tLocal Address\tLP\tRP");
		for(int i=1; i<(int) Math.pow(2,16);i++) {
			try {
				Socket s=new Socket("eniac.cs.qc.edu",i);
				if (s!=null) {
					System.out.print( s.getInetAddress()+"\t");
					System.out.print( s.getLocalAddress()+"\t");
					System.out.print( s.getLocalPort()+"\t");
					System.out.println( s.getPort()+"\t");
				} // end IF
			} // end TRY
			catch (Exception e) {}
		} // end FOR
	} // end METHOD main
} // end CLASS SocketInfo