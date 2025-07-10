package mcliffo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private AnchorPane root;
    private static Scoreboard scoreboard;
    private List<String> colNames;
    private List<Integer> listValues;
    private AnchorPane gameRoot;


    @Override
    public void start(Stage stage) throws IOException, SQLException {
        gameRoot = new AnchorPane(); // game content will be injected later
        StackPane root = new StackPane(); // holds either splash or gameRoot
        Scene scene = new Scene(root, 1700, 850); // adjust to your preferred size
        Pane splashScreen = createSplashScreen(root);

        root.getChildren().addAll(gameRoot, splashScreen);
        gameRoot.setVisible(false); // only show game after button click

        stage.setTitle("PorTabela");
        stage.setScene(scene);
        stage.show();
        //root = new AnchorPane();
        // scene = new Scene(root, 1700, 800);
        // stage.setScene(scene);
        // stage.show();
        //load();
    }
    private Pane createSplashScreen(StackPane root) {
        VBox splashLayout = new VBox(20);
        splashLayout.setStyle("-fx-background-color: black;");
        splashLayout.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView splashImage = new ImageView(new Image(
                getClass().getResource("/images/homepage.jpg").toExternalForm()
        ));
        splashImage.setPreserveRatio(true);
        splashImage.setFitWidth(1080); // resize as needed

        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 20;");
        startButton.setOnAction(e -> {
            root.getChildren().remove(splashLayout); // remove splash
            gameRoot.setVisible(true);               // reveal game
            try {
                load();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }                        // load actual game logic
        });

        splashLayout.getChildren().addAll(splashImage, startButton);
        return splashLayout;
    }


    public static void main(String[] args) {
        launch();

    }

    public void load() throws SQLException{ 
        Tabela tabela = new Tabela(gameRoot, "Tabela");
        tabela.gerarTabela(5, 5, 1000,300, 60, 20);
        PlayerCharacter player = new PlayerCharacter(gameRoot);
        Scoreboard scoreboard = new Scoreboard(gameRoot);
        SqlExecutor sqlExec = new SqlExecutor(gameRoot, scoreboard, player);
        ItemManager itemManager = new ItemManager(gameRoot);
        sqlExec.populateFromTabela(tabela); // this could technically be done with the root calls 

    }
    
}