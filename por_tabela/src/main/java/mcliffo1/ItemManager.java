package mcliffo1;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemManager {
    private List<Items> availableItems;
    private List<Items> allItems;
    private Random random;

    public ItemManager(AnchorPane root) {
        root.getProperties().put("itemManager", this);
        this.availableItems = new ArrayList<>();
        this.allItems = new ArrayList<>();
        this.random = new Random();
        loadDefaultItems();
    }

    private void loadDefaultItems() {
        allItems.add(new ColumnMultiplier(1.5));
        // allItems.add(new ExtraRewardItem());
        // Add more items as needed
        availableItems.addAll(allItems);
    }

    public void unlockRandomItem(AnchorPane root) {
        if (allItems.isEmpty()) return;

        Items selected = allItems.get(random.nextInt(allItems.size()));

        VBox itemDisplay = new VBox(5);
        itemDisplay.setAlignment(Pos.CENTER);
        itemDisplay.setLayoutX(50);
        itemDisplay.setLayoutY(600);

        ImageView itemImage = new ImageView();
        itemImage.setFitWidth(64);
        itemImage.setFitHeight(64);
        // TODO: itemImage.setImage(new Image("file:path/to/" + selected.getImageFilename()));

        Text nameText = new Text(selected.getName());
        nameText.setFont(Font.font(16));

        itemDisplay.getChildren().addAll(itemImage, nameText);
        root.getChildren().add(itemDisplay);
        System.out.println("You unlocked" + selected.getName());
    }

    public void showItemSelection(AnchorPane root) {
        HBox itemSelectionBox = new HBox(30);
        itemSelectionBox.setLayoutX(500);
        itemSelectionBox.setLayoutY(200);
        itemSelectionBox.setStyle("-fx-background-color: lightgoldenrodyellow; -fx-padding: 30; -fx-background-radius: 15; -fx-border-color: black; -fx-border-width: 2;");
        itemSelectionBox.setAlignment(Pos.CENTER);

        Text prompt = new Text("Choose your item:");
        prompt.setFont(Font.font(22));

        VBox wrapper = new VBox(20, prompt, itemSelectionBox);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setLayoutX(450);
        wrapper.setLayoutY(120);

        List<Items> shuffled = new ArrayList<>(allItems);
        Collections.shuffle(shuffled);
        List<Items> choices = shuffled.subList(0, Math.min(3, shuffled.size()));

        for (Items item : choices) {
            VBox itemBox = new VBox(10);
            itemBox.setAlignment(Pos.CENTER);

            ImageView image = new ImageView();
            image.setFitWidth(64);
            image.setFitHeight(64);
            // TODO: image.setImage(new Image("file:path/to/" + item.getImageFilename()));
            // TODO: Will have to work on the logic of this function and related stuff.
            Text name = new Text(item.getName());
            name.setFont(Font.font(14));

            image.setOnMouseEntered(e -> name.setText(item.getDescription()));
            image.setOnMouseExited(e -> name.setText(item.getName()));

            image.setOnMouseClicked((MouseEvent e) -> {
                unlockRandomItem(root);
                root.getChildren().remove(wrapper);
            });

            itemBox.getChildren().addAll(image, name);
            itemSelectionBox.getChildren().add(itemBox);
        }

        root.getChildren().add(wrapper);
    }
}
