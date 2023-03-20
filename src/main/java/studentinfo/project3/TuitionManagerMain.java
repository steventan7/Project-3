package studentinfo.project3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This class contains methods that will launch the behavior of the graphical user interface roster
 *
 * @author Steven Tan, David Fabian
 */
public class TuitionManagerMain extends Application {

    /**
     * This method initiates the Graphical User Interface that manages the student roster.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TuitionManagerMain.class.getResource("TuitionManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 832, 600);
        stage.setTitle("TuitionManager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method launches the stand-alone JavaFX application
     */
    public static void main(String[] args) {
        launch();
    }
}