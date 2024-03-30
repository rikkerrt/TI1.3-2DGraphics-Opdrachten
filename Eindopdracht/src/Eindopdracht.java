import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dyn4j.collision.Filter;
import org.dyn4j.dynamics.Force;
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
    private ArrayList<Enemy> enemies;
    private ArrayList<GameObject> bullets;
    private GameObject mainship;
    private boolean debugSelected;
    private World world;
    private Camera camera;
    private MousePicker mousePicker;
    private FXGraphics2D g2d;
    private Filter filter;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();

        //add debug (show hitboxes)
        CheckBox showDebug = new CheckBox("Show Debug");

        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            init();
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(showDebug, resetButton);

        borderPane.setTop(hBox);

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
                boolean addbullet = false;
                if (last == -1)
                    last = now;
                if ((now - last) / 1.0e9 >= 1) {
                    addbullet = true;
                    last = now;
                }
                update((now - last) / 1.0e9, addbullet);
//                last = now;
                draw(g2d);
            }
        }.start();
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, 0));
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();

        Body floor = new Body();
        floor.addFixture(new Rectangle(20, 1));
        floor.getTransform().setTranslation(0, -10);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);

        Body mainShip = new Body();
        mainShip.addFixture(Geometry.createRectangle(1, 1));
        mainShip.getTransform().setTranslation(0, -3.5);
        mainShip.setMass(MassType.NORMAL);
        world.addBody(mainShip);
        this.mainship = new GameObject("mainship.png", mainShip, new Vector2(0, 0), 0.2);

        for (int x = 0; x < 1; x++) {
            for (int y = 0; y < 1; y++) {
                enemies.add(new Enemy(world ,0, 0));
            }
        }
    }

    public void update(double deltaTime, boolean addBullet) {
        if (deltaTime % 2 == 0) {
            Body bullet = new Body();
            bullet.addFixture(Geometry.createRectangle(0.1, 0.25));
            bullet.getTransform().setTranslation(mainship.getBody().getTransform().getTranslationX(), mainship.getBody().getTransform().getTranslationY() + 0.6);
            bullet.setMass(MassType.NORMAL);
            bullet.applyForce(new Force(new Vector2(0, 2)));
            world.addBody(bullet);

            bullets.add(new GameObject("/bullet.png", bullet, new Vector2(0, 0), .05));
        }

        for (GameObject bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.getBody().getContacts(true) == null) {
                    if (enemy.noHitsLeft()) {
                        enemies.remove(enemy);
                        bullets.remove(bullet);
                    }
                }
            }
        }

        mainship.draw(g2d);
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);

    }

    public void draw(FXGraphics2D g2d) {
        g2d.setTransform(new AffineTransform());
        g2d.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        g2d.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        g2d.scale(1, -1);

        for (GameObject gameObject : bullets) {
            gameObject.draw(g2d);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }

        mainship.draw(g2d);


        if (debugSelected) {
            g2d.setColor(Color.blue);
            DebugDraw.draw(g2d, world, 100);
        }
    }


    public static void main(String[] args) {
        Application.launch(Eindopdracht.class);
    }
}
