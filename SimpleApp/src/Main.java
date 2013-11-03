import java.net.InetAddress;
import java.net.UnknownHostException;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			System.err.println("Unable to determine server address!");
			e.printStackTrace();
		}
    	
    	Thread comms = new Thread(new AppComms(serverAddress),"comms");
    	
    	comms.start();

	}

}
