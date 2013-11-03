package com.mikey;

import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
    
	private Socket socket;
	//private SomeDBType db;
	BufferedReader in;
	PrintStream out;
	String input,output;

    public ServerThread(Socket socket) { //TODO pass in reference to database as parameter here!
        this.socket = socket;
        // assign db        
    }
        
    public void run() {
            
            ServerComm com = new ServerComm();
            BufferedReader in=null;
            PrintStream out=null;
            
            
                try{
                    //out = new PrintStream(socket.getOutputStream(), true);
                        //Thread response = new Thread(new Writer(,),"notListeningActuallyWriting");
                    in = new BufferedReader(
                                            new InputStreamReader(
                                            socket.getInputStream()));
                    out = new PrintStream(socket.getOutputStream());
                        while(true){
                                
                                while ((input = in.readLine()) != null){
                                        
                                		System.out.println(" Recieved msg from device: ");
                                	
                                		output = com.processInput(input);
                                        
                                		out.print(output+ "asdf" + "\n");
                                        
                                        if (output=="Close"){
                                                out.close();
                                            in.close();
                                            socket.close();
                                            break;
                                        }
                                }
                        }
                } catch (IOException e) {
                            e.printStackTrace();
                }
        }   
}