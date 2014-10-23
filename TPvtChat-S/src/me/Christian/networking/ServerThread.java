package me.Christian.networking;

import java.io.*;
import java.net.*;


public class ServerThread implements Runnable
{
	
	private Server server;
	private Socket socket;

	public ServerThread( Server server, Socket socket ) {
		this.server = server;
		this.socket = socket;
	}

	public void run(){
		try {
			DataInputStream din = new DataInputStream( socket.getInputStream() );
			while (true) {
				String message = din.readUTF();
				String Args[] = message.split(" ");
				if(Args.length < 1){

				}else{
					CheckMessage.ParseMessage(socket, Args, message);
				}
			}
		} catch( EOFException ie ) {
		} catch( IOException ie ) {
			ie.printStackTrace();
		} finally {
			server.removeConnection( socket );
		}
	}
}