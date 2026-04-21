package com.example;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

/**
 * BaseVisualizer — shared scaffold for all data-structure visualizer panels.
 *
 * Layout (top to bottom):
 *   ┌─────────────────────────────────────────┐
 *   │  Canvas (nodes drawn here)              │
 *   ├─────────────────────────────────────────┤
 *   │  Status bar  (current operation text)   │
 *   ├─────────────────────────────────────────┤
 *   │  Controls: buttons | Speed slider        │
 *   └─────────────────────────────────────────┘
 *
 * Subclasses override buildControls() and implement their own drawing logic.
 */
public abstract class BaseVisualizer extends VBox {

    // ── Shared style constants ────────────────────────────────────────────
    protected static final Color NODE_FILL       = Color.web("#89b4fa");
    protected static final Color NODE_STROKE     = Color.web("#cdd6f4");
    protected static final Color HIGHLIGHT_FILL  = Color.web("#a6e3a1");
    protected static final Color COMPARE_FILL    = Color.web("#f38ba8");
    protected static final Color ARROW_COLOR     = Color.web("#cdd6f4");
    protected static final Color TEXT_COLOR      = Color.web("#1e1e2e");
    protected static final Color LABEL_COLOR     = Color.web("#cdd6f4");
    protected static final double NODE_RADIUS    = 26;

    // ── Canvas ────────────────────────────────────────────────────────────
    protected final Pane canvas = new Pane();

    // ── Status bar ────────────────────────────────────────────────────────
    protected final Label statusLabel = new Label("Select an operation below to begin.");

    // ── Speed control (ms per step) ───────────────────────────────────────
    protected final Slider speedSlider = new Slider(100, 2000, 600);
    /** Returns the current animation step duration in milliseconds. */
    protected double stepMs() { return speedSlider.getValue(); }

    // ── Sequential animation queue ────────────────────────────────────────
    protected SequentialTransition timeline = new SequentialTransition();

    public BaseVisualizer() {
        setSpacing(0);
        setStyle("-fx-background-color: #181825;");

        // Canvas
        canvas.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 0;");
        VBox.setVgrow(canvas, Priority.ALWAYS);

        // Status bar
        statusLabel.setFont(Font.font("Monospaced", 13));
        statusLabel.setTextFill(Color.web("#a6adc8"));
        statusLabel.setPadding(new Insets(6, 14, 6, 14));
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setStyle("-fx-background-color: #181825; -fx-border-color: #313244; -fx-border-width: 1 0 0 0;");
    }

    /**
     * Must be called by each subclass constructor AFTER super() so that
     * subclass field initializers (TextFields, etc.) have already run.
     *
     *   public MyVisualizer() {
     *       super();
     *       // subclass fields are now initialized
     *       completeSetup();
     *       randomize();
     *       redraw();
     *   }
     */
    protected void completeSetup() {
        // Speed row
        Label slowLbl  = styledLabel("Slow");
        Label fastLbl  = styledLabel("Fast");
        Label speedLbl = styledLabel("Speed:");
        speedSlider.setPrefWidth(180);
        speedSlider.setStyle("-fx-control-inner-background: #313244;");
        speedSlider.setMajorTickUnit(500);
        HBox speedRow = new HBox(8, speedLbl, fastLbl, speedSlider, slowLbl);
        speedRow.setAlignment(Pos.CENTER_LEFT);

        // Control buttons (subclass fields are initialized by now)
        HBox controls = buildControls();
        controls.setSpacing(8);
        controls.setAlignment(Pos.CENTER_LEFT);

        HBox bottomBar = new HBox(24, controls, new Separator(), speedRow);
        bottomBar.setPadding(new Insets(10, 14, 10, 14));
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setStyle("-fx-background-color: #11111b; -fx-border-color: #313244; -fx-border-width: 1 0 0 0;");
        HBox.setHgrow(controls, Priority.ALWAYS);

        getChildren().addAll(canvas, statusLabel, bottomBar);
    }

    /** Subclasses return an HBox containing their operation buttons. */
    protected abstract HBox buildControls();

    // ── Helpers ───────────────────────────────────────────────────────────

    protected Label styledLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(LABEL_COLOR);
        l.setFont(Font.font("System", 12));
        return l;
    }

    protected Button actionButton(String text, Color accent) {
        Button b = new Button(text);
        String hex = toHex(accent);
        b.setStyle("-fx-background-color: " + hex + ";"
                 + "-fx-text-fill: #1e1e2e;"
                 + "-fx-background-radius: 6;"
                 + "-fx-padding: 6 14;"
                 + "-fx-cursor: hand;"
                 + "-fx-font-weight: bold;");
        return b;
    }

    /** Draw a filled circle node with a centered value label. */
    protected StackPane makeNode(int value, Color fill) {
        Circle c = new Circle(NODE_RADIUS, fill);
        c.setStroke(NODE_STROKE);
        c.setStrokeWidth(2);
        Label lbl = new Label(String.valueOf(value));
        lbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbl.setTextFill(TEXT_COLOR);
        StackPane sp = new StackPane(c, lbl);
        sp.setPrefSize(NODE_RADIUS * 2, NODE_RADIUS * 2);
        return sp;
    }

    /** Draw an arrow from (x1,y1) to (x2,y2). */
    protected Line makeArrow(double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(ARROW_COLOR);
        line.setStrokeWidth(2);
        return line;
    }

    /** Draw arrowhead polygon at the tip of an arrow. */
    protected Polygon arrowHead(double tipX, double tipY, double angle) {
        double size = 9;
        Polygon head = new Polygon(
            tipX, tipY,
            tipX - size * Math.cos(angle - 0.4), tipY - size * Math.sin(angle - 0.4),
            tipX - size * Math.cos(angle + 0.4), tipY - size * Math.sin(angle + 0.4)
        );
        head.setFill(ARROW_COLOR);
        return head;
    }

    protected void setStatus(String text) {
        statusLabel.setText(text);
    }

    /** Flash a node fill color then return to base. */
    protected Animation flashNode(StackPane node, Color flashColor, Color returnColor) {
        Circle c = (Circle) node.getChildren().get(0);
        FillTransition ft1 = new FillTransition(Duration.millis(120), c, returnColor, flashColor);
        FillTransition ft2 = new FillTransition(Duration.millis(300), c, flashColor, returnColor);
        return new SequentialTransition(ft1, ft2);
    }

    /** Pause step used to pace animations. */
    protected PauseTransition pause() {
        return new PauseTransition(Duration.millis(stepMs()));
    }

    protected PauseTransition pause(double ms) {
        return new PauseTransition(Duration.millis(ms));
    }

    private static String toHex(Color c) {
        return String.format("#%02x%02x%02x",
            (int)(c.getRed()   * 255),
            (int)(c.getGreen() * 255),
            (int)(c.getBlue()  * 255));
    }
}
