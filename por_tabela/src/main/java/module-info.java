module mcliffo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens mcliffo1 to javafx.fxml;
    exports mcliffo1;
}
