package com.mikey;

public class ServerComm {

	public String processInput(String input) {
		
		if (input!= null)
		{
			return "response to non null input!";
		}
		else
		{
			return "catching null input!";
		}
	}
}
