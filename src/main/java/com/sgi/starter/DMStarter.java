package com.sgi.starter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class DMStarter extends Application {


	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
			primaryStage.getIcons().add(
					new Image(
							DMStarter.class.getResourceAsStream( "/images/icon.png" )));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			primaryStage.setTitle("Rapid Download Manager");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setFullScreen(false);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}


	}

	public static void main(String[] args) {
		launch(args);
	}
}
