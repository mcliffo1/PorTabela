package mcliffo1;

import javafx.animation.FillTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
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

        Set<Integer> resultValues = new HashSet<>();
        rs.beforeFirst();
        while (rs.next()) {
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                resultValues.add(rs.getInt(i));
            }
        }

        rs.beforeFirst();
        List<Integer> flatValues = new ArrayList<>();
        while (rs.next()) {
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                flatValues.add(rs.getInt(i));
            }
        }

        if (outputTable != null) {
            outputTable.dropTables();
        }

        outputTable = new Tabela(root, "RESULT");
        outputTable.tabelaFromList(500, 30, 60, 20, colNames, flatValues);

        scoreboard.setScore(outputTable.score());
        highlightMatchingCells(resultValues);
    }

    private void highlightMatchingCells(Set<Integer> resultValues) {
        if (sourceTable == null) return;

        List<List<Celula>> data = sourceTable.getList();
        for (List<Celula> column : data) {
            for (int i = 1; i < column.size(); i++) {
                Celula cell = column.get(i);
                int value = cell.getValor();
                Color color = resultValues.contains(value) ? Color.LIGHTGREEN : Color.SALMON;
                animateHighlight(cell.getShape(), color);
            }
        }
    }

    private void animateHighlight(javafx.scene.shape.Shape shape, Color highlightColor) {
        FillTransition fill = new FillTransition(Duration.millis(500), shape);
        fill.setToValue(highlightColor);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), shape);
        scale.setToX(1.2);
        scale.setToY(1.2);

        fill.setOnFinished(e -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(ev -> {
                FillTransition restoreFill = new FillTransition(Duration.millis(500), shape);
                restoreFill.setToValue(Color.GRAY);
                restoreFill.play();

                ScaleTransition restoreScale = new ScaleTransition(Duration.millis(500), shape);
                restoreScale.setToX(1);
                restoreScale.setToY(1);
                restoreScale.play();
            });
            pause.play();
        });

        fill.play();
        scale.play();
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
