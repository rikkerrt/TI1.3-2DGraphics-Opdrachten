import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dyn4j.collision.Filter;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.dynamics.Force;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;
import org.jfree.fx.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class Eindopdracht extends Application implements Resizable {
    private ResizableCanvas canvas;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private GameObject mainship;
    private boolean debugSelected;
    private World world;
    private Camera camera;
    private MousePicker mousePicker;
    private FXGraphics2D g2d;
    private double timer;
    private BufferedImage background;

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
        background = ImageIO.read(Objects.requireNonNull(getClass().getResource("background1.jpg")));
        canvas = new ResizableCanvas(e -> draw(e), borderPane);
        g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        borderPane.setCenter(canvas);

        camera = new Camera(canvas, this, g2d);
        mousePicker = new MousePicker(canvas);

        stage.setScene(new Scene(borderPane, 1920, 1080));
        stage.setTitle("Space invaders");
        stage.show();

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                boolean addbullet = false;
                if (last == -1)
                    last = now;
                update((now - last) / 1.0e9, addbullet);
                draw(g2d);
            }
        }.start();
    }

    public void init() {
        world = new World();


        world.setGravity(new Vector2(0, 0));
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();

        Body mainShip = new Body();
        mainShip.addFixture(Geometry.createRectangle(1, 1));
        mainShip.getTransform().setTranslation(0, -3.5);
        mainShip.setMass(MassType.NORMAL);
        mainShip.setUserData("player");
        world.addBody(mainShip);
        this.mainship = new GameObject("mainship.png", mainShip, new Vector2(0, 0), 0.2);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 3; y++) {
                enemies.add(new Enemy(world, -5 + x * 1, 3 + y * 1));
            }
        }
    }

    public void update(double deltaTime, boolean addBullet) {
        world.update(deltaTime);

        if (timer % 50 == 0) {
            bullets.add(new Bullet(mainship, world));
        }

        ArrayList<Bullet> bulletsCopy = new ArrayList<>(bullets);

        for (Bullet bullet : bullets) {
            ArrayList<DetectResult> result = bullet.hasHit();
//                for (Enemy enemy : enemies) {
//                    System.out.println(bullet.getBody().isInContact(enemy.getBody()));
//                }
            if (!result.isEmpty()) {
                for (DetectResult result1 : result) {
                    Body resultBody = result1.getBody();
                    resultBody.setAsleep(false);
                    resultBody.setAutoSleepingEnabled(false);

                    if (resultBody.getUserData() == null)
                        continue;

                    if (resultBody.getUserData().equals("enemy") && !resultBody.equals(bullet.getBody())) {
                        bulletsCopy.remove(bullet);
                        System.out.println(result1);
                        world.removeBody(bullet.getBody());

                        //check for enemy, if found remove.
                        for (int i = 0; i < enemies.size(); i++) {
                            Enemy enemy = enemies.get(i);
                            if (enemy.getBody().equals(resultBody)) {
                                if(enemy.noHitsLeft()) {
                                    removeEnemy(enemy);
                                    break;
                                }

                            }
                        }
                    }

                }
            }
        }
        mainship.draw(g2d);
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        bullets = bulletsCopy;
        timer++;
    }

    public void draw(FXGraphics2D g2d) {
        g2d.setTransform(new AffineTransform());
        g2d.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        g2d.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        g2d.scale(1, -1);


//        g2d.drawImage(background, 0, 0, null);
        g2d.setPaint(new TexturePaint(background, new Rectangle2D.Double(0,canvas.getHeight(), background.getWidth()/2.0, background.getHeight()/2.0)));


        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
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

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        world.removeBody(enemy.getBody());
    }
}
