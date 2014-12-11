package me.Christian.mainpack;

import me.Christian.networking.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{
	public static int ServerPort = 1505;
	private static Thread Thread_MainServer;
	public static Server server;
	public static String MyIP;
	public static TextArea LogArea;
	
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

		new Thread(new GetIp()).start();


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


		LogArea = new TextArea();
		LogArea.setWrapText(true);
		LogArea.setPadding(new Insets(10, 10, 10, 10));
		LogArea.setEditable(false);


		MainBorderPane.setCenter(LogArea);


		HBox top = new HBox();

		Button Restart = new Button("Restart");


		Button OfflineMode = new Button("Offline Mode");

		top.setSpacing(10);
		top.setPadding(new Insets(10, 10, 10, 10));

		top.getChildren().addAll(Restart, OfflineMode);

		MainBorderPane.setTop(top);


		primaryStage.setScene(new Scene(MainBorderPane, 600, 400 ));
		primaryStage.show();
	}
	
	public static void LogMe(String text){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				LogArea.appendText(text+"\n");
			}
		});
	}


}
