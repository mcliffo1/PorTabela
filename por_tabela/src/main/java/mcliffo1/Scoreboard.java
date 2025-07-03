package mcliffo1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Scoreboard {
    private int score;
    private int threshold;
    private Text scoreText;
    private Boolean win;
    private VBox scoreboard;
 
    Scoreboard(AnchorPane root){
        this.scoreboard = new VBox();
        scoreboard.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-background-radius: 8;");
        scoreboard.setPrefWidth(150);

        this.score = 0;
        this.threshold = 300;
        this.scoreText = new Text(score + " / " + threshold);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font(18));

        scoreboard.getChildren().add(scoreText);

        // Position scoreboard at top left
        scoreboard.setLayoutX(150); // 20 px from left edge
        scoreboard.setLayoutY(300); // 20 px from top edge

        root.getChildren().add(scoreboard);
        
    }
    public void setScore(int newScore) throws InterruptedException{
        this.score = newScore;
        scoreText.setText(score + " / " + threshold);
        this.roundWinCheck();
        
        //if(score > this.threshold){
        //    this.setThreshold(threshold += score /2);
        //}
        // scoreboard.getChildren().add(scoreText); Not sure if necessary or not.
    }



public void roundWinCheck() {
    if (this.score > this.threshold) {
        this.scoreboard.setStyle("-fx-background-color: rgba(121, 249, 42, 0.6); -fx-padding: 10; -fx-background-radius: 8;");
        
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            this.setThreshold(threshold + score / 2); //maybe like set tablevalue * (0.5 + (0.1*roundnum))
            this.score = 0;
            this.scoreText.setText(score + " / " + threshold);
            scoreboard.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-background-radius: 8;");
            //onWinTableUpgrade
        });
        pause.play();
        //right here we call tabela.upgrade() hopefully that doesn't get fucky with the sqlExectutor might need to give
        // tabela a function that interfaces with executor

    } else {
        this.scoreboard.setStyle("-fx-background-color: rgba(243, 49, 27, 0.6); -fx-padding: 10; -fx-background-radius: 8;");

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            this.scoreText.setText("You lose. Score: " + score);

        });
        pause.play();
    }
}
    public void onWinTableUpgrade(){
        //create three buttons in the middle of the screen, row, column, and item, with visualizations!
        // the user clicks them, and gets one of them. An item at random, or just calls tabela.updateTable
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
