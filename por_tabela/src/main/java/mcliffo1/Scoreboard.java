package mcliffo1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Scoreboard {
    private int score;
    private int threshold;
    private Text scoreText;
    private Boolean win;
    private VBox scoreboard;
 
    Scoreboard(AnchorPane root){
        VBox scoreboard = new VBox();
        scoreboard.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-background-radius: 8;");
        scoreboard.setPrefWidth(150);

        int score = 0;
        int threshold = 300;
        Text scoreText = new Text(score + " / " + threshold);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font(18));

        scoreboard.getChildren().add(scoreText);

        // Position scoreboard at top left
        scoreboard.setLayoutX(150); // 20 px from left edge
        scoreboard.setLayoutY(150); // 20 px from top edge

        root.getChildren().add(scoreboard);
        
    }
    public void setScore(int newScore){
        score = newScore;
        scoreText.setText(score + " / " + threshold);
        // scoreboard.getChildren().add(scoreText); Not sure if necessary or not.
    }

    public int getScore(){
        return score;
    }

    public void setThreshold(int newThreshold){
        threshold = newThreshold;
        scoreText.setText(score + " / " + threshold);
    }

    public int getThreshold(){
        return threshold;
    }

    public boolean isWin(){
        win = false;
        if(score > threshold){
            win = true;
        }
        return win;
    }

    // TODO: add some arguments to the constructor so the scoreboard can be inicialized in different spots? Idk maybe just make 
    // sure that location is proportionate to screen size changes.
}
