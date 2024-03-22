import java.awt.*;
import java.awt.geom.*;
import java.util.Random;


import javafx.animation.AnimationTimer;
import javafx.application.Application;


import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    private double spotlightX;
    private double spotlightY;


    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);

        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        canvas.setOnMouseMoved(event -> {
            spotlightX = event.getX() - 100;
            spotlightY = event.getY() - 100;
            draw(g2d);
        });

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Spotlight");
        stage.show();
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.setColor(Color.blue);
        Shape shape = new Rectangle2D.Double(spotlightX, spotlightY, 200, 200);
        graphics.draw(shape);
        graphics.setClip(shape);

        Random r = new Random();
        for(int i = 0; i < 1000; i++) {
            graphics.setPaint(Color.getHSBColor(r.nextFloat(),1,1));
            graphics.drawLine((int)(r.nextInt() % canvas.getWidth()), (int)(r.nextInt() % canvas.getHeight()), (int)(r.nextInt() % canvas.getWidth()), (int)(r.nextInt() % canvas.getHeight()));
        }

        graphics.setClip(null);
    }

    public void init() {

    }

    public static void main(String[] args) {
        launch(Spotlight.class);
    }

}
