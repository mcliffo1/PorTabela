package mcliffo1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Tabela {
    private AnchorPane root;
    private List<List<Celula>> celulas;
    private Random random;

    public Tabela(AnchorPane root) {
        celulas = new ArrayList<>();
        this.root = root;
    }
    
    //TODO: Dar valores aleatorios
    // public void gerarTabela(int dimX, int dimY, int X, int Y, int width, int height) {
    //         int Xpadding = 50;
    //         int Ypadding = 50;
    //     for(int i = 0; i < dimX; i++) {
    //         Celula celula = new Celula(X, Y, width, height, root, 0);
    //         //celula.getShape().setFill(Color.WHITE);
    //         List Coluna = new ArrayList<Celula>();
    //         Coluna.add(celula);
    //         for(int j = 0; j < dimY - 1; j++) {
    //             Celula celulay = new Celula(X, Y + Ypadding, width, height + Ypadding, root, 1);
    //             Ypadding += height;
    //             List linha = new ArrayList<Celula>();
    //             linha.add(celulay);
    //         }
    //         Xpadding += width;
    //     }
    //     // Maybe make rows and columns and then add just those to the scene graph rather
    //     // than adding each cell.
    // }


    public void gerarTabela(int dimX, int dimY, int startX, int startY, int width, int height) {
    int Xpadding = 1;
    int Ypadding = 1;

    List<List<Celula>> tabela = new ArrayList<>();

    for (int i = 0; i < dimX; i++) {
        int currentX = startX + i * (width + Xpadding);
        List<Celula> coluna = new ArrayList<>();

        for (int j = 0; j < dimY; j++) {
            int currentY = startY + j * (height + Ypadding);
            Celula celula = new Celula(currentX, currentY, width, height, root, 0);
            // celula.getShape().setFill(Color.WHITE);
            coluna.add(celula);
        }

        tabela.add(coluna);
    }
}

    //     //TODO: Dar valores aleatorios
    // public void gerarTabela(int dimX, int dimY, int minX, int minY, int X, int Y) {
    //         int Xpadding = 0;
    //         int Ypadding = 0;
    //     for(int i = 0; i < dimX; i++); {
    //         Celula celula = new Celula(500 + Xpadding, 900 + Ypadding, minX, minY, root, 0);
    //         //List Coluna = new ArrayList<Celula>();
    //         //Coluna.add(celula);
    //         for(int j = 0; j < dimY - 1; j++); {
    //             Celula celulay = new Celula(500 + Xpadding, 900 + Ypadding, minX, minY, root, 0);
    //             Ypadding += minY;
    //             //List linha = new ArrayList<Celula>();
    //             //linha.add(celulay);
    //         }
    //         Xpadding += minX;
    //     }
    //     // Maybe make rows and columns and then add just those to the scene graph rather
    //     // than adding each cell.
    // }

    public void gerarTabela(int dimX, int dimY) {

    }
}
