package mcliffo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
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
        // Rectangle celula = new Rectangle(0, 0, 10, 10);
        // celula.setX(200);
        // celula.setY(200);
        // celula.setWidth(100);
        // celula.setHeight(100);
        // root.getChildren().add(celula);

        // Rectangle celula2 = new Rectangle(10, 20, 20, 20);
        // root.getChildren().add(celula2);

        // Celula celula3 = new Celula(15, 20, 500, 45, root, 20);
        Tabela tabela = new Tabela(root);
        tabela.gerarTabela(15, 15, 550,100, 60, 20);
        
        Group stickFigureGroup = new Group();

    // Stick figure parts:

    // Head (circle)
    Circle head = new Circle(20, 20, 15);
    head.setStroke(Color.BLACK);
    head.setFill(Color.WHITE);

    // Body (line)
    Line body = new Line(20, 35, 20, 70);
    body.setStroke(Color.BLACK);
    body.setStrokeWidth(2);

    // Left arm
    Line leftArm = new Line(20, 45, 5, 60);
    leftArm.setStroke(Color.BLACK);
    leftArm.setStrokeWidth(2);

    // Right arm
    Line rightArm = new Line(20, 45, 35, 60);
    rightArm.setStroke(Color.BLACK);
    rightArm.setStrokeWidth(2);

    // Left leg
    Line leftLeg = new Line(20, 70, 5, 90);
    leftLeg.setStroke(Color.BLACK);
    leftLeg.setStrokeWidth(2);

    // Right leg
    Line rightLeg = new Line(20, 70, 35, 90);
    rightLeg.setStroke(Color.BLACK);
    rightLeg.setStrokeWidth(2);

    // Add all stick figure parts to group
    stickFigureGroup.getChildren().addAll(head, body, leftArm, rightArm, leftLeg, rightLeg);

    // Speech bubble (polygon with tail)
    Polygon bubble = new Polygon(
    90.0, -140.0,
    290.0, -140.0,
    290.0, -40.0,
    160.0, -40.0,
    130.0, -10.0,
    120.0, -40.0
);



    bubble.setFill(Color.LIGHTYELLOW);
    bubble.setStroke(Color.BLACK);

    Text text = new Text(110, -110, "Placeholder.txt");
text.setFill(Color.BLACK);
text.setStyle("-fx-font-size: 18px;");

    // Add bubble and text to group
    stickFigureGroup.getChildren().addAll(bubble, text);

    // Position group at bottom left (with some padding)
    double padding = 20;
    stickFigureGroup.setLayoutX(padding + 150);
    stickFigureGroup.setLayoutY(scene.getHeight() - 100 - padding - 150);


    // Add to root pane
    root.getChildren().add(stickFigureGroup);

    

     VBox scoreboard = new VBox();
scoreboard.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-background-radius: 8;");
scoreboard.setPrefWidth(150);

// Create the score text
int score = 3456; // example current score
Text scoreText = new Text(score + " / 10000");
scoreText.setFill(Color.WHITE);
scoreText.setFont(Font.font(18));

scoreboard.getChildren().add(scoreText);

// Position scoreboard at top left
scoreboard.setLayoutX(150); // 20 px from left edge
scoreboard.setLayoutY(150); // 20 px from top edge

root.getChildren().add(scoreboard);
    }
    
}