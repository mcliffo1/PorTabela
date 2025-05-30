package mcliffo1;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.util.*;

public class ConsultaUI {
    private Tabela tabela;
    private AnchorPane root;
    private Set<Integer> selectedColumns = new HashSet<>();
    private Integer selectedCriteriaCol = null;
    private Integer selectedCriteria = null;
    private String conditionType = ">";
    private Random rand = new Random();

    public ConsultaUI(AnchorPane root, Tabela tabela) {
        this.root = root;
        this.tabela = tabela;
        renderUI();
    }

    private void renderUI() {
    int x = 20;
    int y = 20;
    int width = 100, height = 40;
    int spacing = 10;

    List<String> colNames = tabela.getColNames();
    if (colNames == null || colNames.isEmpty()) return;

    // -- Column Selectors --
    int yColSelectors = y;
    for (int i = 0; i < colNames.size(); i++) {
        int colIndex = i;
        String label = colNames.get(i);
        Rectangle r = new Rectangle(x + i * (width + spacing), yColSelectors, width, height);
        r.setFill(Color.LIGHTBLUE);
        r.setStroke(Color.BLACK);

        Text t = new Text(label);
        t.setFont(new Font(14));
        t.setX(r.getX() + 10);
        t.setY(r.getY() + 25);

        r.setOnMouseClicked(e -> {
            if (selectedColumns.contains(colIndex)) {
                r.setFill(Color.LIGHTBLUE);
                selectedColumns.remove(colIndex);
            } else {
                r.setFill(Color.DARKBLUE);
                selectedColumns.add(colIndex);
            }
        });

        root.getChildren().addAll(r, t);
    }

    // Update y for next row
    y += height + spacing;

    // -- Criteria Column Selectors --
    int yCriteriaCol = y;
    for (int i = 0; i < colNames.size(); i++) {
        int colIndex = i;
        String label = "Criteria: " + colNames.get(i);
        Rectangle r = new Rectangle(x + i * (width + spacing), yCriteriaCol, width, height);
        r.setFill(Color.LIGHTGREEN);
        r.setStroke(Color.BLACK);

        Text t = new Text(label);
        t.setFont(new Font(12));
        t.setX(r.getX() + 5);
        t.setY(r.getY() + 25);

        int finalY = yCriteriaCol;
        r.setOnMouseClicked(e -> {
            selectedCriteriaCol = colIndex;
            resetRowFill(finalY, Color.LIGHTGREEN);
            r.setFill(Color.DARKGREEN);
        });

        root.getChildren().addAll(r, t);
    }

    // Update y for next row
    y += height + spacing;

    // -- Operators --
    int yOpRow = y;
    String[] ops = {">", "<", "="};
    for (int i = 0; i < ops.length; i++) {
        String op = ops[i];
        Rectangle r = new Rectangle(x + i * (width + spacing), yOpRow, width, height);
        r.setFill(Color.LIGHTPINK);
        r.setStroke(Color.BLACK);

        Text t = new Text(op);
        t.setFont(new Font(18));
        t.setX(r.getX() + 40);
        t.setY(r.getY() + 25);

        int finalY = yOpRow;
        r.setOnMouseClicked(e -> {
            conditionType = op;
            resetRowFill(finalY, Color.LIGHTPINK);
            r.setFill(Color.DEEPPINK);
        });

        root.getChildren().addAll(r, t);
    }

    // Update y for next row
    y += height + spacing;

    // -- Criteria Value Selectors --
    int yValRow = y;
    for (int i = 0; i < 5; i++) {
        int value = rand.nextInt(100) + 1;
        Rectangle r = new Rectangle(x + i * (width + spacing), yValRow, width, height);
        r.setFill(Color.LIGHTYELLOW);
        r.setStroke(Color.BLACK);

        Text t = new Text(String.valueOf(value));
        t.setFont(new Font(16));
        t.setX(r.getX() + 35);
        t.setY(r.getY() + 25);

        int finalY = yValRow;
        r.setOnMouseClicked(e -> {
            selectedCriteria = value;
            resetRowFill(finalY, Color.LIGHTYELLOW);
            r.setFill(Color.GOLD);
        });

        root.getChildren().addAll(r, t);
    }

    // Update y for next row
    y += height + 2 * spacing;

    // -- Execute Button --
    Button runConsulta = new Button("Run Consulta");
    runConsulta.setLayoutX(x);
    runConsulta.setLayoutY(y);
    runConsulta.setOnAction(e -> {
        if (selectedCriteriaCol == null || selectedCriteria == null || selectedColumns.isEmpty()) {
            System.out.println("Please select columns, a criteria column, and a criteria value.");
            return;
        }

        boolean higher = conditionType.equals(">");
        boolean lower = conditionType.equals("<");
        boolean equal = conditionType.equals("=");

        System.out.println("Running consulta...");
        tabela.consultaV1(
            new ArrayList<>(selectedColumns),
            selectedCriteria,
            selectedCriteriaCol,
            higher ? true : lower ? false : null // tweak your consulta method to handle null = equals
        );
    });

    root.getChildren().add(runConsulta);
}


    private void resetRowFill(int yRow, Color baseColor) {
    for (javafx.scene.Node node : root.getChildren()) {
        if (node instanceof Rectangle) {
            Rectangle r = (Rectangle) node;
            if (r.getY() == yRow) {
                r.setFill(baseColor);
            }
        }
    }
}

}
