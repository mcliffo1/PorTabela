package mcliffo1;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.stream.Stream;

public class Tabela {
    private AnchorPane root;
    private List<List<Celula>> tabelaList;
    private Random random;
    private List<String> colNames;
    private List<Celula> coluna;
    private String name;
    private double dragOffsetX;
    private double dragOffsetY;
    private Text tableLabel;



    public Tabela(AnchorPane root, String name) {
        tabelaList = new ArrayList<>();
        this.root = root;
        this.name = name;
        root.getProperties().put("mainTable", this);
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
    this.random = new Random();
    tabelaList = new ArrayList<>();

    // Default name pool to choose from
    String[] defaultNames = {"Name", "Height", "Length", "Width", "Shape", "Weight", "Color", "Depth", "Volume", "Mass"};

    // Randomly assign column names from the list
    colNames = new ArrayList<>();
    for (int i = 0; i < dimX; i++) {
        String randomName = defaultNames[random.nextInt(defaultNames.length)];
        // Optional: ensure uniqueness
        while (colNames.contains(randomName)) {
            randomName = defaultNames[random.nextInt(defaultNames.length)];
        }
        colNames.add(randomName);
    }

    // Add draggable label
    tableLabel = new Text(startX, startY - 10, name);
    tableLabel.setFont(Font.font(16));
    tableLabel.setFill(Color.DARKBLUE);
    root.getChildren().add(tableLabel);

    tableLabel.setOnMousePressed(event -> {
        dragOffsetX = event.getSceneX();
        dragOffsetY = event.getSceneY();
    });

    tableLabel.setOnMouseDragged(event -> {
        double deltaX = event.getSceneX() - dragOffsetX;
        double deltaY = event.getSceneY() - dragOffsetY;

        dragOffsetX = event.getSceneX();
        dragOffsetY = event.getSceneY();

        moveTable(deltaX, deltaY);
    });

    // Create the actual table with headers and data
    for (int i = 0; i < dimX; i++) {
        int currentX = startX + i * (width + Xpadding);
        List<Celula> coluna = new ArrayList<>();

        for (int j = 0; j < dimY; j++) {
            int currentY = startY + j * (height + Ypadding);
            Celula celula;

            if (j == 0) {
                celula = new Celula(currentX, currentY, width, height, root, colNames.get(i)); // Header
            } else {
                celula = new Celula(currentX, currentY, width, height, root, random.nextInt(100)); // Random value
            }

            coluna.add(celula);
        }

        tabelaList.add(coluna);
    }
}

    public void tabelaFromList(int startX, int startY, int width, int height, List<String> colNamesInput, List<Integer> valores) {
    tabelaList = new ArrayList<>();
    colNames = colNamesInput;
    int Xpadding = 1;
    int Ypadding = 1;

    int numCols = colNames.size();
    int numRows = (valores.size() / numCols) + 1; // +1 for header

    tableLabel = new Text(startX, startY - 10, name);
    tableLabel.setFont(Font.font(16));
    tableLabel.setFill(Color.DARKBLUE);
    root.getChildren().add(tableLabel);
    tableLabel.setOnMousePressed(event -> {
    dragOffsetX = event.getSceneX();
    dragOffsetY = event.getSceneY();
    });

    tableLabel.setOnMouseDragged(event -> {
    double deltaX = event.getSceneX() - dragOffsetX;
    double deltaY = event.getSceneY() - dragOffsetY;

    dragOffsetX = event.getSceneX();
    dragOffsetY = event.getSceneY();

    moveTable(deltaX, deltaY);
    });

    for (int i = 0; i < numCols; i++) {
        int currentX = startX + i * (width + Xpadding);
        List<Celula> coluna = new ArrayList<>();

        for (int j = 0; j < numRows; j++) {
            int currentY = startY + j * (height + Ypadding);

            Celula celula;
            if (j == 0) {
                celula = new Celula(currentX, currentY, width, height, root, colNames.get(i));
            } else {
                int index = (j - 1) * numCols + i;
                celula = new Celula(currentX, currentY, width, height, root, valores.get(index));
            }

            coluna.add(celula);
        }

        tabelaList.add(coluna);
    }
}

    private void moveTable(double deltaX, double deltaY) {
    for (List<Celula> coluna : tabelaList) {
        for (Celula celula : coluna) {
            celula.moveBy(deltaX, deltaY);
        }
    }

    if (tableLabel != null) {
        tableLabel.setLayoutX(tableLabel.getLayoutX() + deltaX);
        tableLabel.setLayoutY(tableLabel.getLayoutY() + deltaY);
    }
}


    // public void tabelaFromResultSet(ResultSet rs, AnchorPane root, int startX, int startY, int width, int height, String name) throws SQLException {
    //     ResultSetMetaData meta = rs.getMetaData();
    //     int colCount = meta.getColumnCount();

    // tableLabel = new Text(startX, startY - 10, "Results");
    // tableLabel.setFont(Font.font(16));
    // tableLabel.setFill(Color.DARKBLUE);
    // root.getChildren().add(tableLabel);
    // tableLabel.setOnMousePressed(event -> {
    // dragOffsetX = event.getSceneX();
    // dragOffsetY = event.getSceneY();
    // });

    // tableLabel.setOnMouseDragged(event -> {
    // double deltaX = event.getSceneX() - dragOffsetX;
    // double deltaY = event.getSceneY() - dragOffsetY;

    // dragOffsetX = event.getSceneX();
    // dragOffsetY = event.getSceneY();

    // moveTable(deltaX, deltaY);
    // });

    //     List<String> colNames = new ArrayList<>();
    //     for (int i = 1; i <= colCount; i++) {
    //         colNames.add(meta.getColumnName(i));
    //     }

    //     List<Integer> allVals = new ArrayList<>();
    //     while (rs.next()) {
    //         for (int i = 1; i <= colCount; i++) {
    //             allVals.add(rs.getInt(i));
    //         }
    //     }

    //     Tabela newTabela = new Tabela(root, name);
    //     newTabela.tabelaFromList(startX, startY, width, height, colNames, allVals);
    // }


    public void dropTables(){
        List<List<Celula>> list = this.getList();
        for(int i = 0; i < list.size(); i++){
            List<Celula> coluna = list.get(i);
            for(int j = 0; j < coluna.size(); j++){
                root.getChildren().removeAll(coluna.get(j).getShape(), coluna.get(j).getText());
                //System.out.println(coluna.size());
            }
        }
        root.getChildren().remove(this.tableLabel);
    }
    public void saveTabelaInfo(){

    }

    public List<String> getColNames() { 
        return colNames;
    }

   
public int score() {
    int score = 0;
    for (List<Celula> coluna : tabelaList) {
        // Start at j = 1 if you want to skip the header row (which holds the name)
        for (int j = 1; j < coluna.size(); j++) {
            score += coluna.get(j).getValor();
        }
    }
    return score;
}

    public List<List<Celula>> getList(){
        return tabelaList;
    }

    public void setList(List<List<Celula>> list){
        tabelaList = list;
    }

public void updateTable(Boolean rowOrColumn) {
    if (tabelaList == null || tabelaList.isEmpty()) return;

    int numCols = tabelaList.size();
    int numRows = tabelaList.get(0).size(); // includes header

    List<Integer> updatedValues = new ArrayList<>();

    Random rand = new Random();

    if (rowOrColumn) {
        // ADD ROW
        for (int col = 0; col < numCols; col++) {
            for (int row = 1; row < numRows; row++) {
                updatedValues.add(tabelaList.get(col).get(row).getValor());
            }
        }

        // Add one new row of random values
        for (int i = 0; i < numCols; i++) {
            updatedValues.add(rand.nextInt(100)); // random value
        }

    } else {
        // ADD COLUMN
        for (int col = 0; col < numCols; col++) {
            for (int row = 1; row < numRows; row++) {
                updatedValues.add(tabelaList.get(col).get(row).getValor());
            }
        }

        // New column values
        for (int row = 1; row < numRows; row++) {
            updatedValues.add(rand.nextInt(100));
        }

        // Add new column name
        colNames.add("Col" + (colNames.size() + 1));
    }

    // Drop old table visuals
    dropTables();

    // Determine new dimensions
    int newNumCols = rowOrColumn ? numCols : numCols + 1;
    int newNumRows = rowOrColumn ? numRows + 1 : numRows;

    // Rebuild with same cell dimensions and starting location (change if needed)
    int cellWidth = 60;
    int cellHeight = 20;
    int startX = 800;
    int startY = 200;

    // Recreate table
    tabelaFromList(startX, startY, cellWidth, cellHeight, colNames, updatedValues);
}









    public void removeRow(int rowNum) { // Antiquated
    // Validate row index
    if (rowNum < 0 || tabelaList.isEmpty() || rowNum >= tabelaList.get(0).size()) {
        System.out.println("Invalid row number.");
        //System.out.println(tabelaList);

        return;
    }
    // Loop through each column and remove the cell at the specified row
    for (List<Celula> coluna : tabelaList) {
        if (coluna.size() > rowNum) {
            Celula celulaToRemove = coluna.remove(rowNum);
            // Remove both rectangle and text from the AnchorPane
            root.getChildren().removeAll(
                celulaToRemove.getShape(),  // Rectangle
                celulaToRemove.getText()       // Text (needs accessor!)
            );
        }
    }
}

public void removeColumn(int colNum) { // Antiquated
    // Validate column index
    if (colNum < 0 || colNum >= tabelaList.size()) {
        System.out.println("Invalid column number.");
        return;
    }

    // Get the column to remove
    List<Celula> colunaToRemove = tabelaList.remove(colNum);

    // Remove each cell's visual components from the UI
    for (Celula celula : colunaToRemove) {
        root.getChildren().removeAll(
            celula.getShape(),
            celula.getText() // Make sure Celula has getText()
        );
    }

    // Optionally: Remove column name if your table is tightly coupled to colNames
    if (colNames != null && colNum < colNames.size()) {
        colNames.remove(colNum);
    }
}

    public String getTableName(){
        return name;
    }

    

}
