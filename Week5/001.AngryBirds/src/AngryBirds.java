
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Force;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class AngryBirds extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Point2D.Double startLocation;
    private Point2D.Double endLocation;
    private boolean inFlight;


    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");

        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            this.init();
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(showDebug, resetButton);

        mainPane.setTop(hBox);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);

        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        camera = new Camera(canvas, g -> draw(g), g2d);
        mousePicker = new MousePicker(canvas);

        canvas.setOnMousePressed(event -> {
            this.startLocation = new Point2D.Double(event.getX(), event.getY());
        });

        canvas.setOnMouseReleased(event -> {
            this.endLocation = new Point2D.Double(event.getX(), event.getY());
            this.shoot();
        });

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }


    public void init() {
        double woodPlankScale = 0.185;
        String imagePath = "/wood_block.png";
        double woodBlockWidth = 0.2;
        this.inFlight = false;

        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(25.55, 1));
        floor.getTransform().setTranslation(0, -4.6);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
        gameObjects.add(new GameObject("/background.jpg", floor, new Vector2(0, -575), 1));

        Body catapult = new Body();
        catapult.addFixture(Geometry.createRectangle(0.1, 0.1));
        catapult.getTransform().setTranslation(-6.5, -3);
        catapult.setMass(MassType.INFINITE);
        world.addBody(catapult);
        gameObjects.add(new GameObject("catapult.png", catapult, new Vector2(0, 0), 0.4));

        Body ball = new Body();
        ball.addFixture(Geometry.createCircle(0.2));
        ball.getTransform().setTranslation(-6.5, -2.5);
        ball.setMass(MassType.INFINITE);
        ball.getFixture(0).setRestitution(0.75);
        world.addBody(ball);
        gameObjects.add(new GameObject("/red_bird.png", ball, new Vector2(0, 0), 0.25));

        Body box = new Body();
        box.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        box.getTransform().setTranslation(4, -3);
        box.setMass(MassType.NORMAL);
        world.addBody(box);

        gameObjects.add(new GameObject(imagePath, box, new Vector2(0, 0), woodPlankScale));

        //layer 1

        // Plank 1
        Body plank1 = new Body();
        plank1.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank1.getTransform().setTranslation(4.5, -3);
        plank1.setMass(MassType.NORMAL);
        world.addBody(plank1);
        gameObjects.add(new GameObject(imagePath, plank1, new Vector2(0, 0), woodPlankScale));

        // Plank 2
        Body plank2 = new Body();
        plank2.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank2.getTransform().setTranslation(5, -3);
        plank2.setMass(MassType.NORMAL);
        world.addBody(plank2);
        gameObjects.add(new GameObject(imagePath, plank2, new Vector2(0, 0), woodPlankScale));

        // Plank 3
        Body plank3 = new Body();
        plank3.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank3.getTransform().setTranslation(5.5, -3);
        plank3.setMass(MassType.NORMAL);
        world.addBody(plank3);
        gameObjects.add(new GameObject(imagePath, plank3, new Vector2(0, 0), woodPlankScale));

        // Plank 4
        Body plank4 = new Body();
        plank4.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank4.getTransform().setTranslation(6, -3);
        plank4.setMass(MassType.NORMAL);
        world.addBody(plank4);
        gameObjects.add(new GameObject(imagePath, plank4, new Vector2(0, 0), woodPlankScale));

        // Plank 5
        Body plank5 = new Body();
        plank5.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank5.getTransform().setTranslation(6.5, -3);
        plank5.setMass(MassType.NORMAL);
        world.addBody(plank5);
        gameObjects.add(new GameObject(imagePath, plank5, new Vector2(0, 0), woodPlankScale));

        // Plank 6
        Body plank6 = new Body();
        plank6.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank6.getTransform().setTranslation(7, -3);
        plank6.setMass(MassType.NORMAL);
        world.addBody(plank6);
        gameObjects.add(new GameObject(imagePath, plank6, new Vector2(0, 0), woodPlankScale));

        // Plank 7
        Body plank7 = new Body();
        plank7.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank7.getTransform().setTranslation(7.5, -3);
        plank7.setMass(MassType.NORMAL);
        world.addBody(plank7);
        gameObjects.add(new GameObject(imagePath, plank7, new Vector2(0, 0), woodPlankScale));

        // Plank 8
        Body plank8 = new Body();
        plank8.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank8.getTransform().setTranslation(8, -3);
        plank8.setMass(MassType.NORMAL);
        world.addBody(plank8);
        gameObjects.add(new GameObject(imagePath, plank8, new Vector2(0, 0), woodPlankScale));

        //layer 2

        // Plank 9
        Body plank9 = new Body();
        plank9.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank9.getTransform().setTranslation(4, -2);
        plank9.setMass(MassType.NORMAL);
        world.addBody(plank9);
        gameObjects.add(new GameObject(imagePath, plank9, new Vector2(0, 0), woodPlankScale));

        // Plank 10
        Body plank10 = new Body();
        plank10.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank10.getTransform().setTranslation(4.5, -2);
        plank10.setMass(MassType.NORMAL);
        world.addBody(plank10);
        gameObjects.add(new GameObject(imagePath, plank10, new Vector2(0, 0), woodPlankScale));

        // Plank 11
        Body plank11 = new Body();
        plank11.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank11.getTransform().setTranslation(5, -2);
        plank11.setMass(MassType.NORMAL);
        world.addBody(plank11);
        gameObjects.add(new GameObject(imagePath, plank11, new Vector2(0, 0), woodPlankScale));

        // Plank 12
        Body plank12 = new Body();
        plank12.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank12.getTransform().setTranslation(6, -2);
        plank12.setMass(MassType.NORMAL);
        world.addBody(plank12);
        gameObjects.add(new GameObject(imagePath, plank12, new Vector2(0, 0), woodPlankScale));

        // Plank 13
        Body plank13 = new Body();
        plank13.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank13.getTransform().setTranslation(7, -2);
        plank13.setMass(MassType.NORMAL);
        world.addBody(plank13);
        gameObjects.add(new GameObject(imagePath, plank13, new Vector2(0, 0), woodPlankScale));

        // Plank 14
        Body plank14 = new Body();
        plank14.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank14.getTransform().setTranslation(7.5, -2);
        plank14.setMass(MassType.NORMAL);
        world.addBody(plank14);
        gameObjects.add(new GameObject(imagePath, plank14, new Vector2(0, 0), woodPlankScale));

        // Plank 15
        Body plank15 = new Body();
        plank15.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank15.getTransform().setTranslation(8, -2);
        plank15.setMass(MassType.NORMAL);
        world.addBody(plank15);
        gameObjects.add(new GameObject(imagePath, plank15, new Vector2(0, 0), woodPlankScale));


        //layer 3

        // Plank 16
        Body plank16 = new Body();
        plank16.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank16.getTransform().setTranslation(4.5, -1);
        plank16.setMass(MassType.NORMAL);
        world.addBody(plank16);
        gameObjects.add(new GameObject(imagePath, plank16, new Vector2(0, 0), woodPlankScale));

        // Plank 17
        Body plank17 = new Body();
        plank17.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank17.getTransform().setTranslation(6, -1);
        plank17.setMass(MassType.NORMAL);
        world.addBody(plank17);
        gameObjects.add(new GameObject(imagePath, plank17, new Vector2(0, 0), woodPlankScale));

        // Plank 18
        Body plank18 = new Body();
        plank18.addFixture(Geometry.createRectangle(woodBlockWidth, 0.8));
        plank18.getTransform().setTranslation(7.5, -1);
        plank18.setMass(MassType.NORMAL);
        world.addBody(plank18);
        gameObjects.add(new GameObject(imagePath, plank18, new Vector2(0, 0), woodPlankScale));

        //layer 4

        // Plank 19
        Body plank19 = new Body();
        plank19.addFixture(Geometry.createRectangle(0.75, 0.75));
        plank19.getTransform().setTranslation(6, 0);
        plank19.setMass(MassType.NORMAL);
        world.addBody(plank19);
        gameObjects.add(new GameObject("/pig.png", plank19, new Vector2(0, 0), 0.1));


    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);

        for (GameObject go : gameObjects) {
            go.draw(graphics);
        }

        if (debugSelected) {
            graphics.setColor(Color.BLUE);
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime) {
//        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);
    }

    private void shoot() {
        if (!inFlight) {
            world.getBody(2).setMass(MassType.NORMAL);
            world.getBody(2).applyForce(new Force((this.startLocation.getX() - this.endLocation.getX()) * 1,
                    (this.endLocation.getY() - this.startLocation.getY()) * 3));
            inFlight = true;
        }
    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

}
