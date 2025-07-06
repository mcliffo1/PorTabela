package mcliffo1;

import javafx.animation.FillTransition;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.sql.*;
import java.util.*;

public class SqlExecutor {
    private Connection conn;
    private Tabela outputTable;
    private Tabela sourceTable;
    private AnchorPane root;
    private TextArea sqlInput;
    private PlayerCharacter character;
    private Scoreboard scoreboard;

    public SqlExecutor(AnchorPane root, Scoreboard scoreboard, PlayerCharacter character) {
        root.getProperties().put("sqlExecutor", this);
        this.root = root;
        this.scoreboard = scoreboard;
        this.character = character;
        try {
            conn = DriverManager.getConnection("jdbc:h2:mem:");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setupUI();
    }

    private void setupUI() {
        sqlInput = new TextArea();
        sqlInput.setPromptText("Type SQL query here...");
        sqlInput.setLayoutX(100);
        sqlInput.setLayoutY(690);
        sqlInput.setPrefWidth(600);
        sqlInput.setPrefHeight(100);

        Button runButton = new Button("Run SQL");
        runButton.setLayoutX(710);
        runButton.setLayoutY(690);
        runButton.setOnAction(e -> {
            String query = sqlInput.getText();
            try {
                runQuery(query);
            } catch (SQLException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(sqlInput, runButton);
    }

    public void populateFromTabela(Tabela input) {
        try {
            this.sourceTable = input;

            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS " + input.getTableName());

            List<String> colNames = input.getColNames();
            StringBuilder sb = new StringBuilder("CREATE TABLE ");
            sb.append(input.getTableName()).append(" (");

            for (int i = 0; i < colNames.size(); i++) {
                sb.append(colNames.get(i)).append(" INT");
                if (i < colNames.size() - 1) sb.append(", ");
            }
            sb.append(")");
            stmt.execute(sb.toString());

            PreparedStatement ps = conn.prepareStatement(generateInsertSQL(input));

            List<List<Celula>> data = input.getList();
            int numRows = data.get(0).size();

            for (int row = 1; row < numRows; row++) {
                for (int col = 0; col < data.size(); col++) {
                    ps.setInt(col + 1, data.get(col).get(row).getValor());
                }
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateInsertSQL(Tabela t) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(t.getTableName()).append(" VALUES (");
        for (int i = 0; i < t.getColNames().size(); i++) {
            sb.append("?");
            if (i < t.getColNames().size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public void runQuery(String query) throws SQLException, InterruptedException {
        this.character.setText(query);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        boolean hasResultSet = stmt.execute(query);

        if (!hasResultSet) return;

        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData meta = rs.getMetaData();

        List<String> colNames = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            colNames.add(meta.getColumnName(i));
        }

        List<int[]> matchedPositions = new ArrayList<>();
        int currentRow = 0;
        while (rs.next()) {
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                matchedPositions.add(new int[]{currentRow, i - 1});
            }
            currentRow++;
        }

        if (outputTable != null) {
            outputTable.dropTables();
        }

        rs.beforeFirst();
        List<Integer> flatValues = new ArrayList<>();
        while (rs.next()) {
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                flatValues.add(rs.getInt(i));
            }
        }

        outputTable = new Tabela(root, "RESULT");
        outputTable.tabelaFromList(500, 30, 60, 20, colNames, flatValues);

        scoreboard.setScore(outputTable.score());
        highlightOriginalTableCells(matchedPositions);
    }

    private void highlightOriginalTableCells(List<int[]> matchedPositions) {
        if (sourceTable == null) return;

        Set<String> matchedKeys = new HashSet<>();
        for (int[] pos : matchedPositions) {
            matchedKeys.add(pos[0] + "," + pos[1]);
        }

        List<List<Celula>> tableData = sourceTable.getList();
        for (int col = 0; col < tableData.size(); col++) {
            List<Celula> column = tableData.get(col);
            for (int row = 1; row < column.size(); row++) {
                String key = (row - 1) + "," + col;
                Color color = matchedKeys.contains(key) ? Color.LIGHTGREEN : Color.SALMON;
                animateFill(column.get(row).getShape(), color);
            }
        }
    }

    private void animateFill(javafx.scene.shape.Shape shape, Color color) {
        FillTransition ft = new FillTransition(Duration.millis(600), shape);
        ft.setToValue(color);
        ft.setCycleCount(1);
        ft.play();
    }

    public void updateScoreFromResult() throws InterruptedException {
        if (outputTable == null) return;

        List<List<Celula>> data = outputTable.getList();
        int total = 0;

        for (List<Celula> col : data) {
            for (int i = 1; i < col.size(); i++) {
                total += col.get(i).getValor();
            }
        }

        scoreboard.setScore(total);
    }
}
