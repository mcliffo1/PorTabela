package mcliffo1;

import javafx.scene.layout.AnchorPane;
import mcliffo1.Scoreboard;
import mcliffo1.Tabela;

public abstract class Items {
    
    protected String name;
    protected String description;
    

    public Items(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public abstract String getImagePath();


    // Called when item is activated (e.g. after a round win)
    public abstract void applyEffect();
    public abstract int applyScoringEffect(int colNum, int rowNum, int valor);
}
