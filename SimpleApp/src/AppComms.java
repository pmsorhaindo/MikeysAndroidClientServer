import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class AppComms implements Runnable {

	private InetAddress serverAddress;
	private int serverPort = 1337;
	private Socket appSocket;
    private PrintStream out;
    private BufferedReader in;
    private boolean runningCommThread = true;
	
	public AppComms(InetAddress newAddress){
		serverAddress = newAddress;
		System.out.println("serverAddress assigned" + serverAddress);
	}

	@Override
	public void run() {
		try {
			
			appSocket = new Socket(serverAddress, serverPort);
            
			System.out.println("Server socket initialized!");
			out = new PrintStream(appSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(appSocket.getInputStream()));
            
            String input;
            /*while ((input = in.readLine()) != null){
                out.print("Hey Server!!"  + "\n");
                System.out.println("sending message to server");
                
                if ((input = in.readLine()) != null){
                        System.out.println("Recieved Response from server: "+input);
                }
            }*/
            
            
            while(runningCommThread)
            {
                    Random r = new Random();
                    try {
                            Thread.sleep(r.nextInt(4999));
                    } catch (InterruptedException e) {
                            
                            runningCommThread=false;
                    }       
            		System.out.println("Beaming Location to server...");
                    out.print("Some XML containing current location"  + "\n");


                    if ((input = in.readLine()) != null){
                            System.out.println("ping: "+input);
                    }
            }
            
        } catch (UnknownHostException e) {
            System.err.println("Finding Host fail"); //TODO make this error message nice
            System.exit(1);
        } catch (IOException e) {
            System.err.println("O fail");
            System.exit(1);
        }
        try{
                out.close();
                in.close();
                appSocket.close();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
}

	
}
