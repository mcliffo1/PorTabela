module mcliffo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens mcliffo1 to javafx.fxml;
    exports mcliffo1;
}
