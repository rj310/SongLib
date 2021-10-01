package app;
/*
 * Created By:
 * Rohan Joshi: rj408
 * Nicholas Cheniara: njc129
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.ListController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/testview.fxml"));
		AnchorPane root = (AnchorPane)loader.load();

		ListController listController = 
				loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 500, 435);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}
