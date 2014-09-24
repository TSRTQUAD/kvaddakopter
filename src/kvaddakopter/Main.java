package kvaddakopter;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	
	/**
	 * Path for base View FXML-file
	 */
	private String mainViewPath = "/kvaddakopter/gui/views/Main.fxml";
	
	
	/**
	 * Path for base View FXML-file
	 */
	private String mainCss = "application.css";
	
	
	/**
	 * Application title
	 */
	private String applicationTitle = "KvaddaKopter - Mission Planner";
	
	/**
	 * Runs once when the application is started.
	 * @param primaryStage Stage used for application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(this.mainViewPath));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource(this.mainCss).toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle(this.applicationTitle);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Main loop for Application.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	
}
