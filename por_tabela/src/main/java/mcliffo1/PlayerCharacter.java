package mcliffo1;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;


public class PlayerCharacter {
    
    PlayerCharacter(AnchorPane root){
        Group stickFigureGroup = new Group();
        
        Circle head = new Circle(20, 20, 15);
        head.setStroke(Color.BLACK);
        head.setFill(Color.WHITE);

        Line body = new Line(20, 35, 20, 70);
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(2);

        Line leftArm = new Line(20, 45, 5, 60);
        leftArm.setStroke(Color.BLACK);
        leftArm.setStrokeWidth(2);

        Line rightArm = new Line(20, 45, 35, 60);
        rightArm.setStroke(Color.BLACK);
        rightArm.setStrokeWidth(2);

        Line leftLeg = new Line(20, 70, 5, 90);
        leftLeg.setStroke(Color.BLACK);
        leftLeg.setStrokeWidth(2);

        Line rightLeg = new Line(20, 70, 35, 90);
        rightLeg.setStroke(Color.BLACK);
        rightLeg.setStrokeWidth(2);

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

        stickFigureGroup.getChildren().addAll(bubble, text, head, body, leftArm, rightArm, leftLeg, rightLeg);

        double padding = 20;
        stickFigureGroup.setLayoutX(padding + 150);
        stickFigureGroup.setLayoutY(800 - 100 - padding - 150);

        root.getChildren().add(stickFigureGroup);
        }
        // TODO: Methods: SetText, AnimatePlayer
}
