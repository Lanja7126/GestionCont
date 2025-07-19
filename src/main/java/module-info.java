module com.example.gestioncont {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.gestioncont to javafx.fxml;
    exports com.example.gestioncont;
}