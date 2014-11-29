package me.Christian.mainpack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

import me.Christian.networking.Server;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{
	public static int ServerPort = 1505;
	private static Thread Thread_MainServer;
	public static Server server;
	public static String MyIP;

	public static void main(String[] args){
		try{
			server = new Server();
		}catch (Exception e){
			e.printStackTrace();
		}
		if(args.length > 0){
			if(!args[0].equals("")){
				try{
					ServerPort = Integer.valueOf(args[0]);
					System.out.println("Using custom port: " + args[0]);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		Thread_MainServer = new Thread(server);
		Thread_MainServer.start();
		URL url = null;
		BufferedReader in = null;
		String ipAddress = "";
		try {
			url = new URL("http://bot.whatismyipaddress.com");
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			ipAddress = in.readLine().trim();
			/* IF not connected to internet, then
			 * the above code will return one empty
			 * String, we can check it's length and
			 * if length is not greater than zero, 
			 * then we can go for LAN IP or Local IP
			 * or PRIVATE IP
			 */
			if (!(ipAddress.length() > 0)) {
				try {
					InetAddress ip = InetAddress.getLocalHost();
					System.out.println((ip.getHostAddress()).trim());
					ipAddress = (ip.getHostAddress()).trim();
				} catch(Exception exp) {
					ipAddress = "ERROR";
				}
			}
		} catch (Exception ex) {
			// This try will give the Private IP of the Host.
			try {
				InetAddress ip = InetAddress.getLocalHost();
				System.out.println((ip.getHostAddress()).trim());
				ipAddress = (ip.getHostAddress()).trim();
			} catch(Exception exp) {
				ipAddress = "ERROR";
			}
			//ex.printStackTrace();
		}
		MyIP = ipAddress;
		System.out.println("Hosting on public ip: " + MyIP);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});;
		BorderPane MainBorderPane = new BorderPane();

		primaryStage.setScene(new Scene(MainBorderPane, 600, 200 ));
		primaryStage.show();
	}
}
