package me.Christian.networking;

import java.io.*;
import java.net.*;
import java.util.*;

import me.Christian.mainpack.Main;
import javafx.application.Platform;

public class Server implements Runnable
{
	private ServerSocket ss;

	public static Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();
	public static DataOutputStream dout;
	
	public void run() {
		
		try {
			ss = new ServerSocket( Main.ServerPort );
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Listening on " + ss);
		System.out.println("Finished [1]: Server");

		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

			dout = null;
			try {
				dout = new DataOutputStream( s.getOutputStream() );
			} catch (IOException e) {
				e.printStackTrace();
			}
			Server.reply(s, ".System Connected to PvtChatS1");

			outputStreams.put( s, dout );
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Append some funny stuff
					//s.getInetAddress().toString().replace("/", "") + " connected
				}
			});
			System.out.println(s.getInetAddress().toString().replace("/", "") + " connected");
	        Thread n = new Thread(new ServerThread(this, s));
	        n.start();
		}
	}


	static Enumeration<DataOutputStream> getOutputStreams() {
		return outputStreams.elements();
	}

	public static void sendToAll( String message ) {
		synchronized( outputStreams ) {
			for (Enumeration<DataOutputStream> e = getOutputStreams(); e.hasMoreElements(); ) {
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try {
					dout.writeUTF(message);
				} catch( IOException ie ) { ie.printStackTrace(); }
			}
		}
	}
	
	public static void sendToAllInChannel(String message, String channel){
		for (Enumeration<Socket> e = CheckMessage.Usernames.keys(); e.hasMoreElements();)
		{
			Socket matcher = (Socket)e.nextElement();
			if(CheckMessage.GetUserChannel(matcher).equals(channel)){
				Server.reply(matcher, message);
			}
		}
	}
	
	

	public static void reply(Socket socket, String message){
		DataOutputStream dout;
		try {
			dout = new DataOutputStream( socket.getOutputStream() );
			dout.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void removeConnection( Socket s ) {
		synchronized( outputStreams ) {
			outputStreams.remove(s);
			try {
				s.close();
				if(outputStreams.contains(s.toString())){
					outputStreams.remove(s);
				}
			} catch( IOException ie ) {
				ie.printStackTrace();
				System.out.println("Error closing connection");
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Append some funny stuff
					//s.getInetAddress().toString().replace("/", "") + " diconnected"
				}
			});
			System.out.println(s.getInetAddress().toString().replace("/", "") + " diconnected");
			if(CheckMessage.Usernames.get(s) != null && CheckMessage.Usernames.get(s) != "null"){
				String pn = CheckMessage.GetUserChannel(s);
				Server.sendToAllInChannel(".disconnect " + CheckMessage.GetUserNameFromString(CheckMessage.Usernames.get(s)), pn);
			}
			if(CheckMessage.Usernames.containsKey(s)){
				CheckMessage.Usernames.remove(s);
			}
			if(CheckMessage.ChannelUsers.containsKey(s)){
				CheckMessage.ChannelUsers.remove(s);
			}
		}
	}
}
