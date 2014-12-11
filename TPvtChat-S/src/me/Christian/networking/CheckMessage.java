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


	public static Hashtable<String, Boolean> ChannelList = new Hashtable<String, Boolean>();
	public static Hashtable<Socket, String> ChannelUsers = new Hashtable<Socket, String>();

	public static Hashtable<String, String> ChannelPWList = new Hashtable<String, String>();

	public static void ParseMessage(Socket socket, String[] args, String FullMsg){
		if(!FullMsg.startsWith(".")){
			for (Enumeration<Socket> e = Usernames.keys(); e.hasMoreElements();)
			{
				Socket matcher = (Socket)e.nextElement();
				if(GetUserChannel(matcher).equals(GetUserChannel(socket))){
					Server.reply(matcher, FullMsg);
				}
			}
			//Server.sendToAll(FullMsg);
		}else{
			if(args[0].equals(".connect")){
				//Server.sendToAll(".connect " + args[1]);
				if(!Usernames.containsKey(socket)){
					if(IsUserConnected(args[1])){
						Server.reply(socket, ".System Username allready exists. Disconnected.");
						Timeline aftertick = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Main.server.removeConnection(socket);
							}
						}));
						aftertick.play();
					}else{

						Usernames.put(socket, "Main§"+args[1]);
						ChannelUsers.put(socket, "Main");

						synchronized( Usernames ) {
							for (Enumeration<Socket> e = Usernames.keys(); e.hasMoreElements();)
							{
								Socket mn = (Socket)e.nextElement();
								String hw = Usernames.get(mn);
								if(GetUserChannel(mn).equals("Main")){
									if(mn != socket){
										Server.reply(mn, ".connect " + GetUserName(socket));
										Server.reply(socket, ".connect " + GetUserNameFromString(hw));
									}
								}
							}
						}
						Server.reply(socket, ".connect " + GetUserName(socket));
					}
				}
			}else if(args[0].equals(".namechange")){
				Server.sendToAll(".namechange " + args[1] + " " + args[2]);
				if(!Usernames.containsKey(socket)){
					Usernames.put(socket, GetUserChannel(socket)+"§"+args[2]);
					Main.LogMe("User: " + args[1] + " changed Name to: " + args[2]);
				}else{
					String sn = GetUserChannel(socket);
					Usernames.remove(socket);
					Usernames.put(socket, sn+"§"+args[2]);
					Main.LogMe("User: " + args[1] + " changed Name to: " + args[2]);
				}
			}else if(args[0].equals(".disconnect")){
				if(Usernames.containsKey(socket)){
					String pn = GetUserChannel(socket);
					Usernames.remove(socket);
					ChannelUsers.remove(socket);
					Server.sendToAllInChannel(".disconnect " + args[1], pn);
				}

				// TO BE EDITED!
			}else if(args[0].equals(".NOPEadmin")){
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
					Server.reply(socket, ".confirmAdmin");
					AdminList.put(socket, Usernames.get(socket));
				}else{
					System.out.println(socket.getInetAddress().toString() + " " + ComputerIP);
				}
			}else if(args[0].equals(".kick")){
				if(AdminList.containsKey(socket)){
					System.out.println(Usernames.get(args[1]) + "hi");
					for (Enumeration<Socket> e = Usernames.keys(); e.hasMoreElements();)
					{
						Enumeration<Socket> tempsocket = e;
						Socket hn = null;
						if(e.hasMoreElements()){
							hn = tempsocket.nextElement();
						}
						String hw = Usernames.get(hn);
						if(hw.equals(args[1])){
							if(hn != null){
								Server.sendToAll(".System REMOVING " + Usernames.get(hn) + " IP: " + hn.toString());
								Main.server.removeConnection(hn);
								System.out.println("x123" + hw);
							}
						}else{
							System.out.println("x1" + hw);
						}
						System.out.println("x2" + hw);
					}
				}else{
					// do smth
				}
			}else if(args[0].equals(".channel")){
				// do smth
				if(ChannelList.containsKey(args[1])){
					String temppw;
					if(ChannelPWList.containsKey(args[1])){
						temppw = ChannelPWList.get(args[1]);
						if(temppw != " "){
							if(args.length >= 2){
								if(temppw.equals(args[2])){
									Server.reply(socket, ".System You got connected to: " + args[1]);
									ChangeUserChannel(socket, args[1]);
									synchronized( Usernames ) {
										for (Enumeration<Socket> e = Usernames.keys(); e.hasMoreElements();)
										{
											Socket mn = (Socket)e.nextElement();
											String hw = Usernames.get(mn);
											if(GetUserChannel(mn).equals(args[1])){
												Server.reply(mn, ".connect " + GetUserName(socket));
												Server.reply(socket, ".connect " + GetUserNameFromString(hw));
											}
										}
									}
								}else{
									Server.reply(socket, ".System Connection to PRIVATE Channel Refused");
								}
							}
						}else{
							Server.reply(socket, ".System You got connected to: " + args[1]);
							ChangeUserChannel(socket, args[1]);
							synchronized( Usernames ) {
								for (Enumeration<Socket> e = Usernames.keys(); e.hasMoreElements();)
								{
									Socket mn = (Socket)e.nextElement();
									String hw = Usernames.get(mn);
									if(GetUserChannel(mn).equals(args[1])){
										Server.reply(mn, ".connect " + GetUserName(socket));
										Server.reply(socket, ".connect " + GetUserNameFromString(hw));
									}
								}
							}
						}
					}

				}else{
					ChannelList.put(args[1], true);
					if(args.length > 2){
						CreateChannel(args[1], args[2]);
						Server.reply(socket, ".System Created Channel: " + args[1] + " pw:" + args[2]);
						Server.reply(socket, ".connect " + GetUserName(socket));
						ChangeUserChannel(socket, args[1]);
					}else{
						CreateChannel(args[1], "");
						Server.reply(socket, ".System Created Channel: " + args[1]);
						Server.reply(socket, ".connect " + GetUserName(socket));
						ChangeUserChannel(socket, args[1]);
					}
				}
			}
		}
	}

	public static String GetUserChannel(Socket s){
		String channelname = null;

		String UsernameANDchannel = Usernames.get(s);
		String tobereplaced = "§";
		String temp[] = UsernameANDchannel.split(tobereplaced);
		if(temp.length >= 1){
			channelname = temp[0];
		}else{
			Main.server.removeConnection(s);
		}

		return channelname;
	}

	public static String GetUserNameFromString(String st){
		String usernameZ = null;

		String UsernameANDchannel = st;
		String tobereplaced = "§";
		String temp[] = UsernameANDchannel.split(tobereplaced);
		if(temp.length >= 1){
			usernameZ = temp[1];
		}
		return usernameZ;
	}

	public static String GetUserName(Socket s){
		String usernameZ = null;

		String UsernameANDchannel = Usernames.get(s);
		String tobereplaced = "§";
		String temp[] = UsernameANDchannel.split(tobereplaced);
		if(temp.length >= 1){
			usernameZ = temp[1];
		}else{
			Main.server.removeConnection(s);
		}
		return usernameZ;
	}

	public static void ChangeUserChannel(Socket s, String channel){
		String xn = GetUserName(s);
		String cname = GetUserChannel(s);
		
		Usernames.remove(s);
		Usernames.put(s, channel+"§"+xn);
		ChannelUsers.remove(s);
		ChannelUsers.put(s, channel);
		
		Server.sendToAllInChannel(".disconnect " + xn, cname);
		Main.LogMe("Sending: '.disconnect " + xn + "' to Everyone in " + cname);
		
		Main.LogMe("User: " + Crypter.hashit(s.toString()) + " changed channel to: " + channel);
	}

	public static void CreateChannel(String channelname, String password){
		if(password.equals("")){
			ChannelPWList.put(channelname, " ");
		}else{
			ChannelPWList.put(channelname, password);
		}
		ChannelList.put(channelname, true);
		Main.LogMe("Created channel: " +channelname + " ;pw: " + Crypter.hashit(password));
	}

	public static boolean IsUserConnected(String Username){
		boolean connected = false;
		for (Enumeration<Socket> e = Usernames.keys(); e.hasMoreElements();)
		{
			String hw = GetUserNameFromString(Usernames.get((Socket)e.nextElement()));
			if(hw.equals(Username)){
				connected = true;
				return true;
			}
		}
		return connected;
	}
}

