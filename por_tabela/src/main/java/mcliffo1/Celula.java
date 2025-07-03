package mcliffo1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class Celula {
    private int valor;
    private String title;
    private Rectangle celula;
    private Text texto;  // Text node for displaying the value

    public Celula(int minX, int minY, int width, int height, AnchorPane root, int novoValor) {
        this.valor = novoValor;

        // Create and style the rectangle
        celula = new Rectangle(minX, minY, width, height);
        celula.setFill(novoValor == 0 ? Color.WHITE : Color.GRAY);
        //celula.setFill(Color.GRAY);
        celula.setStroke(Color.BLACK);

        // Create the text node with the value
        texto = new Text(String.valueOf(valor));
        texto.setFont(new Font(16));
        // Center the text inside the rectangle
        texto.setX(minX + width / 2.0 - 5);  // Adjust -5 for centering text width
        texto.setY(minY + height / 2.0 + 5); // Adjust +5 to align text vertically

        // Add both the rectangle and text to the UI
        root.getChildren().addAll(celula, texto);
    }

    public Celula(int minX, int minY, int width, int height, AnchorPane root, String text) {
        this.title = text;

        // Create and style the rectangle
        celula = new Rectangle(minX, minY, width, height);
        //celula.setFill(novoValor == 0 ? Color.WHITE : Color.GRAY);
        celula.setFill(Color.LIGHTGRAY);
        celula.setStroke(Color.BLACK);

        // Create the text node with the value
        texto = new Text(text);
        texto.setFont(new Font(16));
        // Center the text inside the rectangle
        texto.setX(minX + width / 2.0 - 20);  // Adjust -5 for centering text width
        texto.setY(minY + height / 2.0 + 5); // Adjust +5 to align text vertically

        // Add both the rectangle and text to the UI
        root.getChildren().addAll(celula, texto);
    }

    public void moveBy(double dx, double dy) {
    celula.setX(celula.getX() + dx);
    celula.setY(celula.getY() + dy);
    texto.setX(texto.getX() + dx);
    texto.setY(texto.getY() + dy);
    //itemVisual.setx...

    }


    public void setValor(int novo) {
        valor = novo;
        texto.setText(String.valueOf(novo)); // Update text when value changes
    }

    public int getValor() {
        return valor;
    }

    public Shape getShape() {
        return celula;
    }

    public Text getText() {
        return texto;
    }

    public String getTitle() {
        if(title != null){
            return title;
        }
        else{
            return "Null";
        }
    }
}
