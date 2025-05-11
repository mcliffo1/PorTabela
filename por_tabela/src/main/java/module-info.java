module mcliffo1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens mcliffo1 to javafx.fxml;
    exports mcliffo1;
}
