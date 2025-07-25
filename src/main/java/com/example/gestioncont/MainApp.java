package com.example.gestioncont;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 643, 500);
        stage.setTitle("Gestion des contacts");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args){
        launch(args);
    }
}
