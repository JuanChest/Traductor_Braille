package com.traductor;

import com.traductor.controller.TraductorController;
import com.traductor.model.TraductorBraille;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación Traductor Braille.
 * Carga la interfaz FXML y configura el controlador.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/traductor/view/traductor.fxml"));
        Parent root = loader.load();

        // Obtener el controlador y configurar el modelo
        TraductorController controller = loader.getController();
        TraductorBraille traductor = new TraductorBraille();
        controller.setTraductor(traductor);

        // Configurar la escena y el escenario
        Scene scene = new Scene(root, 650, 580);
        primaryStage.setTitle("Traductor Braille");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
