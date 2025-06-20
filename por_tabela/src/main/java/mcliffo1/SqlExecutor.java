package mcliffo1;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlExecutor {
    private Connection conn;
    private Tabela outputTable;
    private AnchorPane root;
    private TextArea sqlInput;

    public SqlExecutor(AnchorPane root) {
        this.root = root;
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
        sqlInput.setLayoutX(50);
        sqlInput.setLayoutY(50);
        sqlInput.setPrefWidth(600);
        sqlInput.setPrefHeight(100);

        Button runButton = new Button("Run SQL");
        runButton.setLayoutX(660);
        runButton.setLayoutY(50);
        runButton.setOnAction(e -> {
            String query = sqlInput.getText();
            try {
                runQuery(query);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(sqlInput, runButton);
    }

    public void populateFromTabela(Tabela input) {
        try {
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

    public void runQuery(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        boolean hasResultSet = stmt.execute(query);

        if (!hasResultSet) return;

        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData meta = rs.getMetaData();

        List<String> colNames = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            colNames.add(meta.getColumnName(i));
        }

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
        outputTable.tabelaFromList(800, 200, 60, 20, colNames, flatValues);
    }

    public void runQueryProgrammatically(String query) {
        try {
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
