package mcliffo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private AnchorPane root;
    private static Scoreboard scoreboard;
    @Override
    public void start(Stage stage) throws IOException {
        root = new AnchorPane();
        scene = new Scene(root, 1700, 800);
        stage.setScene(scene);
        stage.show();
        load();
    }

    public static void main(String[] args) {
        launch();

    }

    public void load(){ // TODO: Add a incialize assets in the future that handles the first few lines of load.
        Tabela tabela = new Tabela(root);
        tabela.gerarTabela(15, 15, 550,100, 60, 20);
        PlayerCharacter player = new PlayerCharacter(root);
        Scoreboard scoreboard = new Scoreboard(root);
    }
    
}