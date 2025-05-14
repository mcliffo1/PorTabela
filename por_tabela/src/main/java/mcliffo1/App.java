package mcliffo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Rectangle2D celula;
    private AnchorPane root;
    @Override
    public void start(Stage stage) throws IOException {
        root = new AnchorPane();
        scene = new Scene(root, 1700, 800);
        stage.setScene(scene);
        stage.show();
        load();
    }

    //static void setRoot(String fxml) throws IOException {
    //    scene.setRoot(loadFXML(fxml));
    //}

    //private static Parent loadFXML(String fxml) throws IOException {
    //    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    //    return fxmlLoader.load();
    //}

    public static void main(String[] args) {
        launch();
    }

    public void load(){
        Rectangle celula = new Rectangle(0, 0, 10, 10);
        celula.setX(200);
        celula.setY(200);
        celula.setWidth(100);
        celula.setHeight(100);
        root.getChildren().add(celula);

        Rectangle celula2 = new Rectangle(10, 20, 20, 20);
        root.getChildren().add(celula2);

        Celula celula3 = new Celula(15, 20, 500, 45, root, 20);
    }
}