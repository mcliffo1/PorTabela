package mcliffo1;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemManager {
    private final List<Items> availableItems;
    private final List<Items> allItems;
    private final Random random;

    private HBox unlockedItemsBox;    
    int unlockedItemCount = 0;


public ItemManager(AnchorPane root) {
    root.getProperties().put("itemManager", this);
    this.availableItems = new ArrayList<>();
    this.allItems = new ArrayList<>();
    this.random = new Random();
    loadDefaultItems();

    // Create and add container for unlocked items at fixed position
    unlockedItemsBox = new HBox(10);
    unlockedItemsBox.setLayoutX(50);
    unlockedItemsBox.setLayoutY(600);
    root.getChildren().add(unlockedItemsBox);
}

public void unlockItem(AnchorPane root, Items selected) {
    VBox itemDisplay = new VBox(5);
    itemDisplay.setAlignment(Pos.CENTER);

    ImageView itemImage = new ImageView();
    itemImage.setFitWidth(64);
    itemImage.setFitHeight(64);
    itemImage.setImage(loadImage(selected.getImagePath()));

    Text nameText = new Text(selected.getName());
    nameText.setFont(Font.font(16));

    itemDisplay.getChildren().addAll(itemImage);

    // You can position here or add to a container as before
    itemDisplay.setLayoutX(100 + unlockedItemCount * 80);
    itemDisplay.setLayoutY(600);
    root.getChildren().add(itemDisplay);

    System.out.println("You unlocked " + selected.getName());

    unlockedItemCount++;
}


    private void loadDefaultItems() {
        allItems.add(new ColumnMultiplier(1.5));
        allItems.add(new rowMultiplier(1.25));
        // Add more items as needed
        availableItems.addAll(allItems);
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
            image.setImage(loadImage(item.getImagePath()));

            // Pre-size the label to avoid layout jitter
            String display = item.getName();
            String hover = item.getDescription();
            String widest = (display.length() > hover.length()) ? display : hover;

            Text name = new Text(widest); // pre-set widest to fix layout
            name.setFont(Font.font(14));
            name.setText(display);
            name.setTextAlignment(TextAlignment.CENTER);
            name.setWrappingWidth(100); // Optional: makes sure text wraps uniformly

            image.setOnMouseEntered(e -> name.setText(hover));
            image.setOnMouseExited(e -> name.setText(display));

            image.setOnMouseClicked((MouseEvent e) -> {
                unlockItem(root, item);        // unlock the clicked item explicitly
                root.getChildren().remove(wrapper);  // remove selection UI
            });


            itemBox.getChildren().addAll(image, name);
            itemSelectionBox.getChildren().add(itemBox);
        }

        root.getChildren().add(wrapper);
    }

    private Image loadImage(String path) {
        try {
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load image: " + path);
            return new Image(getClass().getResource("/images/fallback.png").toExternalForm());
        }
    }
    public List<Items> getItems(){
        return availableItems;
    }
}
