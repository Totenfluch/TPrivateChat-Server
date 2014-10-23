package me.Christian.networking;


import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import me.Christian.mainpack.Main;

public class CheckMessage {

	public static Hashtable<Socket, String> Usernames = new Hashtable<Socket, String>();
	public static Hashtable<Socket, String> AdminList = new Hashtable<Socket, String>();

	public static void ParseMessage(Socket socket, String[] args, String FullMsg){
		if(!FullMsg.startsWith(".")){
			Server.sendToAll(FullMsg);
		}else{
			if(args[0].equals(".connect")){
				Server.sendToAll(".connect " + args[1]);
				if(!Usernames.containsKey(socket)){
					Usernames.put(socket, args[1]);
				}

				synchronized( Usernames ) {
					for (Enumeration<Socket> e = Usernames.keys(); e.hasMoreElements();)
					{
						String hw = Usernames.get(e.nextElement());
						Server.reply(socket, ".connect " + hw);
					}
				}
			}else if(args[0].equals(".namechange")){
				Server.sendToAll(".namechange " + args[1] + " " + args[2]);
				if(!Usernames.containsKey(socket)){
					Usernames.put(socket, args[2]);
				}else{
					Usernames.remove(socket);
					Usernames.put(socket, args[2]);
				}
			}else if(args[0].equals(".disconnect")){
				if(Usernames.containsKey(socket)){
					Usernames.remove(socket);
					Server.sendToAll(".disconnect " + args[1]);
				}
			}else if(args[0].equals(".admin")){
				InetAddress lComputerIP = null;
				try {
					lComputerIP = InetAddress.getLocalHost();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				String ComputerIP = lComputerIP.getHostAddress();
				if(socket.getInetAddress().toString().contains(Main.MyIP) || socket.getInetAddress().toString().contains("127.0.0.1")){
					String n = "." + Usernames.get(socket);
					Usernames.remove(socket);
					Usernames.put(socket, n);
					Timeline aftertick = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							Server.sendToAll(".namechange " + Usernames.get(socket).replaceFirst(".", "") + " " + Usernames.get(socket));
							Server.sendToAll(".System " + Usernames.get(socket) + " is now a Admin.");
						}
					}));
					aftertick.play();
				}else{
					System.out.println(socket.getInetAddress().toString() + " " + ComputerIP);
				}
				Server.reply(socket, ".confirmAdmin");
				AdminList.put(socket, Usernames.get(socket));
			}
		}
	}
}

