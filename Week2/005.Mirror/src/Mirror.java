import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Mirror extends Application {
    ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        graphics.scale(1, -1);


        double resolution = 0.1;
        double lastY = 2.5 * -200;

        for (double x = -200; x < 200; x += resolution) {
            float y = (float) (x * 2.5);
            graphics.draw(new Line2D.Double(x, y, (x - resolution), lastY));
            lastY = y;
        }

        Rectangle rectangle = new Rectangle(0, 150, 100, 100);
        graphics.setColor(Color.green);
        graphics.draw(rectangle);

        AffineTransform transform = new AffineTransform();
        transform.setTransform((2/(1+Math.pow(2.5, 2)))-1,
                ((2*2.5)/(1+Math.pow(2.5,2))),
                ((2*2.5)/(1+Math.pow(2.5,2))),
                ((2*Math.pow(2.5,2))/(1+Math.pow(2.5,2)) -1),
                0, 0);

        Shape mirroredRectangle = transform.createTransformedShape(rectangle);
        graphics.setColor(Color.red);
        graphics.draw(mirroredRectangle);
        graphics.setColor(Color.black);

    }


    public static void main(String[] args) {
        launch(Mirror.class);
    }

}
