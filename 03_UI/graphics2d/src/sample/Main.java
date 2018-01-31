package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    @FXML
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);

        canvas = new Canvas();

        // broken?
        //canvas.widthProperty().bind(primaryStage.widthProperty());
        //canvas.heightProperty().bind(primaryStage.heightProperty());
        canvas.setHeight(600);
        canvas.setWidth(800);

        root.getChildren().add(canvas);
        primaryStage.setTitle("UI Demo - 2D Graphics");
        primaryStage.setScene(scene);
        primaryStage.show();

        draw(root);

        drawAnimation(root);
    }

    private void draw(Group group) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        System.out.println("width: " + canvas.getWidth());
        System.out.println("height: " + canvas.getHeight());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // line
        gc.setStroke(Color.RED);
        gc.strokeLine(50, 50, 150, 250);

        // rectangles
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.fillRect(250, 50, 100, 75);
        gc.strokeRect(250, 175, 100, 75);

        // rounded rectangles
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.BEIGE);
        gc.fillRoundRect(450, 50, 100, 75, 10, 10);
        gc.strokeRoundRect(450, 175, 100, 75, 20, 20);

        // ovals (ellipses)
        gc.setFill(Color.CORAL);
        gc.setStroke(Color.CORAL);
        gc.strokeOval(650, 50, 100, 75);
        gc.fillOval(650, 175, 100, 75);

        // arcs
        gc.setFill(Color.DARKCYAN);
        gc.setStroke(Color.DARKCYAN);
        /*
        ArcType.ROUND - pie shaped
        ArcType.CHORD - endpoint connected to startpoint with line
         */
        gc.strokeArc(50, 350, 100, 75, 115.0, 45.0, ArcType.ROUND);
        gc.fillArc(50, 500, 100, 75, 45.0, 115.0, ArcType.ROUND);

        // polygons (one filled semi-transparent)
        gc.setFill(Color.color(0.8, 0.0, 0.3, 0.5));
        gc.setStroke(Color.HOTPINK);
        gc.strokePolygon(new double[] {250, 310, 300, 250}, new double[] {350, 360, 380, 400}, 4);
        gc.fillPolygon(new double[] {250, 310, 300, 250}, new double[] {500, 510, 530, 550}, 4);

        // text (with adjusted font)
        Font font = new Font("Arial", 24);
        gc.setFont(font);
        gc.setFill(Color.OLIVE);
        gc.setStroke(Color.OLIVE);
        gc.strokeText("CSCI2020u", 450, 400);
        gc.fillText("CSCI2020u", 450, 550);

        // image
        Image image = new Image(getClass().getClassLoader().getResource("images/disk.png").toString());
        gc.drawImage(image, 685, 400);
    }

    private Timeline timeline = null;

    private int frameOffsetX = 0;
    private int frameOffsetY = 0;
    private final int frameWidth = 118;
    private final int frameHeight = 150;
    private final int totalWidth = 118;
    private final int totalHeight = 900;
    private final int numFrames = 6;
    private int frameNum = 0;

    private void drawAnimation(Group group) {
        // load the image relative to the classpath
        Image sprites = new Image(getClass().getClassLoader().getResource("images/sprites.png").toString());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gc.setFill(Color.BLACK);
                gc.fillRect(650, 450, frameWidth, frameHeight);

                // draw the current frame
                gc.drawImage(sprites, frameOffsetX, frameOffsetY, frameWidth, frameHeight, 650, 450, frameWidth, frameHeight);

                // proceed to the next frame of the animation
                frameNum = (frameNum + 1) % numFrames;

                // x offset remains static, since we have no other columns
                // update y offset to the next frame, or loop back
                frameOffsetY += frameHeight;
                if (frameOffsetY >= totalHeight) {
                    frameOffsetY = 0;
                }
            }
        }));
        timeline.playFromStart();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
