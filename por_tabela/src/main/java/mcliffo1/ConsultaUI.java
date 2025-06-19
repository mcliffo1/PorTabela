package mcliffo1;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.util.*;
import java.util.stream.Collectors;

public class ConsultaUI {
    private Tabela tabela;
    private AnchorPane root;
    private StringBuilder queryString = new StringBuilder();
    private Text queryDisplay;
    private Random rand = new Random();

    public ConsultaUI(AnchorPane root, Tabela tabela) {
        this.root = root;
        this.tabela = tabela;
        renderUI();
    }

    private void renderUI() {
        int minX = 50, maxX = 500;
        int minY = 50, maxY = 400;
        int width = 100, height = 40;

        List<String> sqlKeywords = Arrays.asList("SELECT", "FROM", "WHERE", "AND", "OR", ">", "<", "=", "*");

        List<String> allButtons = new ArrayList<>(sqlKeywords);
        allButtons.addAll(tabela.getColNames());
        allButtons.add(tabela.getTableName());

        for (String word : allButtons) {
            int x = rand.nextInt(maxX - minX) + minX;
            int y = rand.nextInt(maxY - minY) + minY;

            Rectangle r = new Rectangle(x, y, width, height);
            r.setFill(Color.TRANSPARENT);
            r.setStroke(Color.BLACK);

            Text t = new Text(word);
            t.setFont(new Font(14));
            t.setX(r.getX() + 10);
            t.setY(r.getY() + 25);

            r.setOnMouseClicked((MouseEvent e) -> {
                queryString.append(word).append(" ");
                updateQueryDisplay();
            });

            root.getChildren().addAll(r, t);
        }

        queryDisplay = new Text("Query: ");
        queryDisplay.setFont(new Font(16));
        queryDisplay.setX(50);
        queryDisplay.setY(450);
        root.getChildren().add(queryDisplay);

        Button undoButton = new Button("Undo");
        undoButton.setLayoutX(50);
        undoButton.setLayoutY(470);
        undoButton.setOnAction(e -> {
            removeLastWord();
            updateQueryDisplay();
        });

        Button clearButton = new Button("Clear");
        clearButton.setLayoutX(120);
        clearButton.setLayoutY(470);
        clearButton.setOnAction(e -> {
            queryString.setLength(0);
            updateQueryDisplay();
        });

        Button executeButton = new Button("Execute");
        executeButton.setLayoutX(200);
        executeButton.setLayoutY(470);
        executeButton.setOnAction(e -> {
            parseAndExecuteQuery();
        });

        root.getChildren().addAll(undoButton, clearButton, executeButton);
    }

    private void updateQueryDisplay() {
        queryDisplay.setText("Query: " + queryString.toString());
    }

    private void removeLastWord() {
        String[] words = queryString.toString().trim().split(" ");
        if (words.length == 0) return;
        queryString.setLength(0);
        for (int i = 0; i < words.length - 1; i++) {
            queryString.append(words[i]).append(" ");
        }
    }

    private void parseAndExecuteQuery() {
    String query = queryString.toString().trim();
    System.out.println("Executing Query: " + query);

    try {
        String upperQuery = query.toUpperCase();
        if (!upperQuery.startsWith("SELECT") || !upperQuery.contains("FROM") || !upperQuery.contains("WHERE")) {
            System.out.println("Invalid query format.");
            return;
        }

        // Parse SELECT part
        String[] fromSplit = query.split("(?i)FROM"); // case-insensitive split
        String selectPart = fromSplit[0].replaceAll("(?i)SELECT", "").trim();
        List<String> selectedColNames = Arrays.stream(selectPart.split(","))
                                              .map(String::trim)
                                              .collect(Collectors.toList());

        List<Integer> selectedColIndexes = new ArrayList<>();
        for (String col : selectedColNames) {
            int idx = tabela.getColNames().indexOf(col);
            if (idx >= 0) selectedColIndexes.add(idx);
            else {
                System.out.println("Column not found: " + col);
                return;
            }
        }

        // Parse WHERE part
        String[] whereSplit = fromSplit[1].split("(?i)WHERE");
        String wherePart = whereSplit[1].trim();
        String[] whereTokens = wherePart.split(" ");

        if (whereTokens.length < 3) {
            System.out.println("Invalid WHERE clause.");
            return;
        }

        String criteriaCol = whereTokens[0].trim();
        String operator = whereTokens[1].trim();
        int criteriaValue = Integer.parseInt(whereTokens[2].trim());

        int criteriaColIdx = tabela.getColNames().indexOf(criteriaCol);
        if (criteriaColIdx == -1) {
            System.out.println("Invalid WHERE column: " + criteriaCol);
            return;
        }

        // Clear previous Celulas from the UI
        for (List<Celula> row : tabela.getList()) {
            for (Celula c : row) {
                root.getChildren().remove(c);
            }
        }

        // Build new filtered list
        List<List<Celula>> originalData = tabela.getList();
        List<List<Celula>> newTabelaList = new ArrayList<>();

        // Add new header row (copy of selected headers)
        List<Celula> headerRow = new ArrayList<>();
        int j = 0;
        int startX = 400;
        int width = 40;
        int Xpadding = 20;
        for (int colIndex : selectedColIndexes) {
            int currentX = startX + j * (width + Xpadding);
            Celula original = originalData.get(0).get(colIndex);
            headerRow.add(new Celula(startX, 300, 40, 10, root, original.getTitle()));
            j++;
        }
        newTabelaList.add(headerRow);

        // Add matching rows
        for (int i = 1; i < originalData.size(); i++) {
            List<Celula> row = originalData.get(i);
            Celula criteriaCell = row.get(criteriaColIdx);
            int val = criteriaCell.getValor();
            int currentX = startX + j * (width + Xpadding);

            boolean matches = false;
            switch (operator) {
                case ">":
                    matches = val > criteriaValue;
                    break;
                case "<":
                    matches = val < criteriaValue;
                    break;
                case "=":
                    matches = val == criteriaValue;
                    break;
                default:
                    matches = false;
                    break;
            }

            if (matches) {
                List<Celula> newRow = new ArrayList<>();
                for (int colIndex : selectedColIndexes) {
                    Celula original = row.get(colIndex);
                    newRow.add(new Celula(startX, 300, 40, 10, root, original.getTitle()));
                }
                newTabelaList.add(newRow);
                
            }
            j++;
        }

        // Update table and render
        tabela.setList(newTabelaList);
        //tabela.gerarTabela(10, 15, 800, 300, 60, 20, 1);

    } catch (Exception e) {
        System.out.println("Failed to parse query. Check syntax.");
        e.printStackTrace();
    }
}

}
