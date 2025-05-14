package mcliffo1;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;

public class Celula {
    private int valor;
    private Rectangle celula;

    public Celula(int minX, int minY, int X, int Y, AnchorPane root, int novoValor){
        celula = new Rectangle(minX, minY, X, Y);
        valor = novoValor;
        celula.fillProperty();
        root.getChildren().add(celula);
    }


    public void setValor(int novo){
        valor = novo;
    }
    public int getValor(){
        return valor;
    }
}
