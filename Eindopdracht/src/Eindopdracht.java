import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.geometry.*;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Force;
import org.dyn4j.dynamics.World;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;


public class Eindopdracht extends Application {
    private ResizableCanvas canvas;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private boolean debugSelected;
    private World world;
    private Camera camera;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");

        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
//            this.init();
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(showDebug, resetButton);

        mainPane.setTop(hBox);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);

        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        camera = new Camera(canvas, g -> draw(g), g2d);

        canvas.setOnMousePressed(event -> {
            MousePicker mousePicker = new MousePicker(canvas);
            AffineTransform tx = new AffineTransform();
            tx.translate(event.getX(), event.getY());

            mousePicker.update(world, tx, 1);

        });

        Stage stage = new Stage();
        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }


    public void init() {
        world = new World();
        world.setGravity(new Vector2(9.8));

        Body mainShip = new Body();
        mainShip.addFixture(Geometry.createRectangle(4.75, 5.5));
        mainShip.getTransform().setTranslation(0, -4.6);
        mainShip.setMass(MassType.INFINITE);
        world.addBody(mainShip);
        gameObjects.add(new GameObject("mainship.png", mainShip, new Vector2(0, 0), 1));
    }

    private void draw(FXGraphics2D g) {
        g.setTransform(new AffineTransform());
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = g.getTransform();

        g.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        g.scale(1, -1);

        for (GameObject go : gameObjects) {
            go.draw(g);
        }

        if (debugSelected) {
            g.setColor(Color.BLUE);
            DebugDraw.draw(g, world, 100);
        }

        g.setTransform(originalTransform);
    }
}
