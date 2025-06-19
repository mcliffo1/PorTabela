package mcliffo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private AnchorPane root;
    private static Scoreboard scoreboard;
    private List<String> colNames;
    private List<Integer> listValues;
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        root = new AnchorPane();
        scene = new Scene(root, 1700, 800);
        stage.setScene(scene);
        stage.show();
        load();
    }

    public static void main(String[] args) {
        launch();

    }

    public void load() throws SQLException{ // TODO: Add a incialize assets in the future that handles the first few lines of load.
        Tabela tabela = new Tabela(root, "Tabela");
        tabela.gerarTabela(5, 15, 800,300, 60, 20);
        //tabela.dropTables();
        // TODO: the two above lines should all be the same function really.
        PlayerCharacter player = new PlayerCharacter(root);
        Scoreboard scoreboard = new Scoreboard(root);
        //ConsultaUI consultaUI = new ConsultaUI(root, tabela);
        SqlExecutor sqlExec = new SqlExecutor(root);
        sqlExec.populateFromTabela(tabela);
        //ResultSet rs = sqlExec.runQuery("SELECT Height, Width FROM tabela WHERE Length > 50");
        Tabela filteredTabela = new Tabela(root, "tabResultado");
        //filteredTabela.tabelaFromResultSet(rs, root, 700, 100, 60, 20, "QueryResult");



        //tabela.consultaV1(indices, 15, 2, false);
    }
    
}