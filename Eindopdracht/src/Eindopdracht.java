import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;
import org.jfree.fx.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Eindopdracht extends Application implements Resizable {
    private ResizableCanvas canvas;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private boolean debugSelected;
    private World world;
    private Camera camera;
    private MousePicker mousePicker;
    private FXGraphics2D g2d;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();

        //add debug (show hitboxes)
        CheckBox showDebug = new CheckBox("Show Debug");

        showDebug.setOnAction(e-> {
            debugSelected =  showDebug.isSelected();
        });

        borderPane.setTop(showDebug);

        canvas = new ResizableCanvas(e -> draw(e), borderPane);
        g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        borderPane.setCenter(canvas);

        camera = new Camera(canvas, this, g2d);
        mousePicker = new MousePicker(canvas);

        stage.setScene(new Scene(borderPane, 1920, 1080));
        stage.setTitle("Hello Joints");
        stage.show();

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1.0e9);
                last = now;
                draw(g2d);
            }
        }.start();
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, 0));

        Body floor = new Body();
        floor.addFixture(new Rectangle(20, 1));
        floor.getTransform().setTranslation(0, -10);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);

        Body mainShip = new Body();
        mainShip.addFixture(Geometry.createRectangle(4.75, 5.5));
        mainShip.getTransform().setTranslation(0, -4.6);
        mainShip.setMass(MassType.NORMAL);
        world.addBody(mainShip);
        gameObjects.add(new GameObject("mainship.png", mainShip, new Vector2(0, 0), 1));
    }

    public void update(double deltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g2d);
        }

        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);

    }

    public void draw(FXGraphics2D g2d) {
        g2d.setTransform(new AffineTransform());
        g2d.clearRect(0, 0, (int) canvas.getWidth() , (int) canvas.getHeight());

        g2d.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        g2d.scale(1, -1);

        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g2d);
        }

        if(debugSelected) {
            g2d.setColor(Color.blue);
            DebugDraw.draw(g2d, world, 100);
        }
    }


    public static void main(String[] args) {
        Application.launch(Eindopdracht.class);
    }
}
