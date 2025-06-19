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
import java.util.stream.Stream;

public class Tabela {
    private AnchorPane root;
    private List<List<Celula>> tabelaList;
    private Random random;
    private List<String> colNames;
    private List<Celula> coluna;
    private String name;

    public Tabela(AnchorPane root, String name) {
        tabelaList = new ArrayList<>();
        this.root = root;
        this.name = name;
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
        colNames = new ArrayList<>();
        colNames.add("Name");
        colNames.add("Height");
        colNames.add("Length");
        colNames.add("Width");
        colNames.add("Shape");
        // Make this better

        for (int i = 0; i < dimX; i++) {
            int currentX = startX + i * (width + Xpadding);

            List<Celula> coluna = new ArrayList<>();

            for (int j = 0; j < dimY; j++) {
                int currentY = startY + j * (height + Ypadding);
                if(i < dimX && j < 1){
                    Celula celula = new Celula(currentX, currentY, width, height, root, colNames.get(i));
                    coluna.add(celula);
                }
                else{
                    Celula celula = new Celula(currentX, currentY, width, height, root, random.nextInt(100));
                    coluna.add(celula);
                }
            }
            tabelaList.add(coluna);
        }

    }
    public void tabelaFromList(int startX, int startY, int width, int height, List<String> colNamesInput,List<Integer> valores){
        tabelaList = new ArrayList<>();
        colNames = colNamesInput;
        int Xpadding = 1;
        int Ypadding = 1;
        int dimY = (colNames.size() + valores.size()) / colNames.size();
        for (int i = 0; i < colNames.size(); i++) {
            int currentX = startX + i * (width + Xpadding);

            List<Celula> coluna = new ArrayList<>();

            for (int j = 0; j < dimY; j++) {
                int currentY = startY + j * (height + Ypadding);
                if(i < colNames.size() && j < 1){
                    Celula celula = new Celula(currentX, currentY, width, height, root, colNames.get(i));
                    coluna.add(celula);
                }
                else{
                    Celula celula = new Celula(currentX, currentY, width, height, root, valores.get(i));
                    coluna.add(celula);
                }
            }
            tabelaList.add(coluna);
        }
    } // We are going to have to include abilities into this in the future


    public void tabelaFromResultSet(ResultSet rs, AnchorPane root, int startX, int startY, int width, int height, String name) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();

        List<String> colNames = new ArrayList<>();
        for (int i = 1; i <= colCount; i++) {
            colNames.add(meta.getColumnName(i));
        }

        List<Integer> allVals = new ArrayList<>();
        while (rs.next()) {
            for (int i = 1; i <= colCount; i++) {
                allVals.add(rs.getInt(i));
            }
        }

        Tabela newTabela = new Tabela(root, name);
        newTabela.tabelaFromList(startX, startY, width, height, colNames, allVals);
    }


    public void dropTables(){
        List<List<Celula>> list = this.getList();
        for(int i = 0; i < list.size(); i++){
            List<Celula> coluna = list.get(i);
            for(int j = 0; j < coluna.size(); j++){
                root.getChildren().removeAll(coluna.get(j).getShape(), coluna.get(j).getText());
                System.out.println(coluna.size());
            }
        }
    }
    public void saveTabelaInfo(){

    }

    public List<String> getColNames() { 
        return colNames;
    }

   public void consultaV1(List<Integer> SelectCols, int criteria, int criteriaColuna, boolean higherLower) {
    // Validate criteria column
    if (criteriaColuna < 0 || criteriaColuna >= tabelaList.size()) {
        System.out.println("Invalid criteria column index.");
        return;
    }

    // STEP 1: Determine which rows satisfy the condition (excluding header row)
    List<Integer> validRows = new ArrayList<>();
    validRows.add(0); // Always keep header row
    for (int rowIndex = 1; rowIndex < tabelaList.get(0).size(); rowIndex++) {
        Celula criteriaCell = tabelaList.get(criteriaColuna).get(rowIndex);
        int value = criteriaCell.getValor();
        boolean condition = false;

        if (higherLower && value > criteria) condition = true;
        else if (!higherLower && value < criteria) condition = true;
        else if (value == criteria) condition = true;

        if (condition) {
            validRows.add(rowIndex);
        }
    }

    // STEP 2: Remove all rows NOT in validRows from each column
    for (List<Celula> coluna : tabelaList) {
        for (int rowIndex = coluna.size() - 1; rowIndex >= 0; rowIndex--) {
            if (!validRows.contains(rowIndex)) {
                Celula cellToRemove = coluna.remove(rowIndex);
                root.getChildren().removeAll(cellToRemove.getShape(), cellToRemove.getText());
            }
        }
    }

    // STEP 3: Remove columns NOT in SelectCols
    for (int colIndex = tabelaList.size() - 1; colIndex >= 0; colIndex--) {
        if (!SelectCols.contains(colIndex)) {
            List<Celula> columnToRemove = tabelaList.remove(colIndex);
            for (Celula celula : columnToRemove) {
                root.getChildren().removeAll(celula.getShape(), celula.getText());
            }
            if (colNames != null && colIndex < colNames.size()) {
                colNames.remove(colIndex);
            }
        }
    }
}

    public void score() {

    }

    public List<List<Celula>> getList(){
        return tabelaList;
    }

    public void setList(List<List<Celula>> list){
        tabelaList = list;
    }


    public void removeRow(int rowNum) {
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

public void removeColumn(int colNum) {
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
