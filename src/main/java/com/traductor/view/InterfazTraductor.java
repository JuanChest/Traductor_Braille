package com.traductor.view;

import com.traductor.controller.TraductorController;
import com.traductor.model.TraductorBraille;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Interfaz gráfica para el traductor de texto a Braille.
 *
 * @see TraductorController
 * @see TraductorBraille
 */
public class InterfazTraductor extends Application {
    private TextArea textoEntrada;
    private TextArea textoSalida;
    private TraductorController ctrl;
    
    /**
     * Inicializa y configura la interfaz gráfica.
     *
     * @param primaryStage El escenario principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        
        // Inicializar el controlador
        TraductorBraille traductor = new TraductorBraille();
        ctrl = new TraductorController(this, traductor);
        
        // Crear componentes de la interfaz
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        
        Label lblTitulo = new Label("Traductor de Texto a Braille");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label lblEntrada = new Label("Texto de Entrada:");
        textoEntrada = new TextArea();
        textoEntrada.setPrefRowCount(5);
        textoEntrada.setPrefColumnCount(40);
        textoEntrada.setWrapText(true);
        textoEntrada.setPromptText("Ingrese el texto a traducir...");
        
        Button btnTraducir = new Button("Traducir a Braille");
        btnTraducir.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        btnTraducir.setOnAction(e -> setControlador(ctrl));
        
        Label lblSalida = new Label("Resultado en Braille:");
        textoSalida = new TextArea();
        textoSalida.setPrefRowCount(5);
        textoSalida.setPrefColumnCount(40);
        textoSalida.setWrapText(true);
        textoSalida.setEditable(false);
        textoSalida.setStyle("-fx-control-inner-background: #f0f0f0; -fx-font-size: 24px; -fx-font-family: 'Segoe UI Symbol', 'Arial Unicode MS', 'DejaVu Sans';");
        
        
        root.getChildren().addAll(
            lblTitulo,
            lblEntrada,
            textoEntrada,
            btnTraducir,
            lblSalida,
            textoSalida
        );
        
        Scene scene = new Scene(root, 650, 580);
        primaryStage.setTitle("Traductor Braille");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    /**
     * Obtiene el texto ingresado por el usuario.
     *
     * @return El texto de entrada.
     */
    public String obtenerTexto() {
        return textoEntrada.getText();
    }
    
    /**
     * Muestra el resultado de la traducción.
     *
     * @param resultado El texto traducido a mostrar.
     */
    public void mostrarResultado(String resultado) {
        textoSalida.setText(resultado);
    }
    
    /**
     * Establece el controlador y ejecuta la traducción.
     *
     * @param ctrl El controlador a utilizar.
     */
    public void setControlador(TraductorController ctrl) {
        ctrl.traducir();
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
