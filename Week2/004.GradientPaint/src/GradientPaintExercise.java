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

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    private Point2D middlePoint;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        middlePoint = new Point2D.Double(canvas.getWidth()/2, canvas.getHeight()/2);
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));


        canvas.setOnMouseDragged(event -> {
            middlePoint = new Point2D.Double(event.getX(), event.getY());
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        float[] fractions = {0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
        Color[] colors = {Color.RED, Color.orange, Color.yellow, Color.green, Color.blue};

        RadialGradientPaint gradientPaint = new RadialGradientPaint(middlePoint, 300, fractions, colors, MultipleGradientPaint.CycleMethod.REFLECT);

        graphics.setPaint(gradientPaint);
        graphics.fill(new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight()));
    }

    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
