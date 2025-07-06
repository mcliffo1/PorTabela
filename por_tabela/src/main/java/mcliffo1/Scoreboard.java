package mcliffo1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

public class Scoreboard {
    private int score;
    private int threshold;
    private Text scoreText;
    private Boolean win;
    private VBox scoreboard;
    private PlayerCharacter player;
    private Tabela table;
    private int round;
    private AnchorPane root;
    private ItemManager items;
 
    Scoreboard(AnchorPane root){
        this.scoreboard = new VBox();
        this.player = (PlayerCharacter) root.getProperties().get("PlayerCharacter");
        this.table = (Tabela) root.getProperties().get("mainTable");
        this.root = root;
        scoreboard.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-background-radius: 8;");
        scoreboard.setPrefWidth(150);
        this.round = 0;
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
        player.setText("Awesome!");
        round++;
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            int newThresh = (int) Math.floor(((round * 0.05) + 0.5) * table.score());
            this.setThreshold(newThresh);
            this.score = 0;
            this.scoreText.setText(score + " / " + threshold);
            scoreboard.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10; -fx-background-radius: 8;");
            onWinTableUpgrade();
        });
        pause.play();
        //right here we call tabela.upgrade() hopefully that doesn't get fucky with the sqlExectutor might need to give
        // tabela a function that interfaces with executor

    } else {
        this.scoreboard.setStyle("-fx-background-color: rgba(243, 49, 27, 0.6); -fx-padding: 10; -fx-background-radius: 8;");
        player.setText("Aww man!");
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            this.scoreText.setText("You lose. Score: " + score);

        });
        pause.play();
    }
}


public void onWinTableUpgrade() {
    VBox upgradeBox = new VBox(20);

    this.items = (ItemManager) root.getProperties().get("itemManager");

    upgradeBox.setAlignment(Pos.CENTER);
    upgradeBox.setStyle(
        "-fx-background-color: rgba(144,238,144,0.95); " + // light green
        "-fx-padding: 30; " +
        "-fx-background-radius: 20; " +
        "-fx-border-color: white; " +
        "-fx-border-width: 3;"
    );
    upgradeBox.setLayoutX(800); // center assuming width ~350
    upgradeBox.setLayoutY(300); // center assuming height ~300

    // Title text
    Text rewardText = new Text("Nice job! Select your reward!");
    rewardText.setFont(Font.font("Arial", 24));
    rewardText.setFill(Color.DARKGREEN);

    // Buttons (2.5x larger)
    Button rowButton = new Button("Add Row");
    Button colButton = new Button("Add Column");
    Button itemButton = new Button("Item");

    rowButton.setPrefSize(250, 50);
    colButton.setPrefSize(250, 50);
    itemButton.setPrefSize(250, 50);

    String buttonStyle = "-fx-font-size: 18px; -fx-background-radius: 10; -fx-font-weight: bold;";
    rowButton.setStyle(buttonStyle);
    colButton.setStyle(buttonStyle);
    itemButton.setStyle(buttonStyle);

    // Add to VBox
    upgradeBox.getChildren().addAll(rewardText, rowButton, colButton, itemButton);
    scoreboard.getChildren().add(upgradeBox);

    // Button actions
    rowButton.setOnAction(e -> {
        scoreboard.getChildren().remove(upgradeBox);
        table.updateTable(true);
    });

    colButton.setOnAction(e -> {
        scoreboard.getChildren().remove(upgradeBox);
        table.updateTable(false);
    });

    itemButton.setOnAction(e -> {
        //System.out.println("Item reward coming soon!");
        scoreboard.getChildren().remove(upgradeBox);
        items.showItemSelection(root);
    });
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
