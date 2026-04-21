package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * VisualizerApp — Main launcher for the Data Structures Visualizer.
 *
 * Run with:  mvn javafx:run
 *
 * Select a data structure from the left sidebar to see:
 *   - A live canvas showing nodes at random positions
 *   - Step-by-step animated operations
 *   - An adjustable speed slider
 *   - Operation buttons (add, remove, search, etc.)
 */
public class VisualizerApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Structures Visualizer");

        // ── Sidebar ────────────────────────────────────────────────────────
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(200);
        sidebar.setPadding(new Insets(16));
        sidebar.setStyle("-fx-background-color: #1e1e2e;");

        Label title = new Label("Data Structures");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        title.setTextFill(Color.web("#cdd6f4"));

        String[] structures = {
            "Singly Linked List",
            "Doubly Linked List",
            "Stack",
            "Queue",
            "Binary Search Tree",
            "Graph",
            "HashMap"
        };

        ToggleGroup tg = new ToggleGroup();

        // ── Content area ───────────────────────────────────────────────────
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #181825;");

        // Panels — one per structure
        Pane[] panels = {
            new SinglyLinkedListVisualizer(),
            new DoublyLinkedListVisualizer(),
            new StackVisualizer(),
            new QueueVisualizer(),
            new BSTVisualizer(),
            new GraphVisualizer(),
            new HashMapVisualizer()
        };

        // Show the first panel by default
        contentArea.getChildren().add(panels[0]);

        sidebar.getChildren().add(title);
        sidebar.getChildren().add(new Separator());

        for (int i = 0; i < structures.length; i++) {
            final int idx = i;
            ToggleButton btn = new ToggleButton(structures[i]);
            btn.setToggleGroup(tg);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle(navBtnStyle(false));
            btn.setTextFill(Color.web("#cdd6f4"));

            btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                btn.setStyle(navBtnStyle(isSelected));
                if (isSelected) {
                    contentArea.getChildren().setAll(panels[idx]);
                }
            });

            if (i == 0) btn.setSelected(true);
            sidebar.getChildren().add(btn);
        }

        // ── Root layout ────────────────────────────────────────────────────
        HBox root = new HBox(sidebar, contentArea);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        Scene scene = new Scene(root, 1100, 680);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    private String navBtnStyle(boolean selected) {
        String bg = selected ? "#313244" : "transparent";
        return "-fx-background-color: " + bg + ";"
             + "-fx-background-radius: 6;"
             + "-fx-padding: 8 12;"
             + "-fx-cursor: hand;"
             + "-fx-border-width: 0;";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
