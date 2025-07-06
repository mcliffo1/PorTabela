package mcliffo1;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ItemManager {
    private List<Items> availableItems;

    public ItemManager(AnchorPane root) {
        root.getProperties().put("itemManager", this);
        availableItems = new ArrayList<>();
        loadDefaultItems();
    }

    private void loadDefaultItems() {
        availableItems.add(new ColumnMultiplier(1.5));
        //availableItems.add(new ExtraRewardItem());
        // Add more
    }

    public List<Items> getAvailableItems() {
        return availableItems;
    }

    public void showItemSelection(AnchorPane root, Tabela tabela, Scoreboard scoreboard) {
        VBox itemBox = new VBox(20);
        itemBox.setLayoutX(600);
        itemBox.setLayoutY(200);
        itemBox.setStyle("-fx-background-color: lightgoldenrodyellow; -fx-padding: 20; -fx-background-radius: 10;");
        itemBox.setAlignment(Pos.CENTER);

        Text prompt = new Text("Choose your item:");
        prompt.setFont(Font.font(20));

        itemBox.getChildren().add(prompt);

        for (Items item : availableItems) {
            Button itemButton = new Button(item.getName() + "\n" + item.getDescription());
            itemButton.setPrefSize(300, 80);
            itemButton.setWrapText(true);
            itemButton.setStyle("-fx-font-size: 14px; -fx-background-radius: 10;");
            itemBox.getChildren().add(itemButton);
        }

        root.getChildren().add(itemBox);
    }
}
