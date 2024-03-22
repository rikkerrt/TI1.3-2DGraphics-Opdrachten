import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics) {
        GeneralPath path1 = new GeneralPath();

        graphics.setColor(Color.black);
        path1.moveTo(450 ,150 );
        graphics.draw(new Ellipse2D.Double( 250, 150,400, 400));

        path1.curveTo(600,150, 600, 350, 450, 350);
        path1.curveTo(300, 350, 300, 550, 450,550);
        path1.curveTo(720, 545, 720, 145, 450,150);
        path1.closePath();

        graphics.fill(path1);

        Ellipse2D blackCircle  = new Ellipse2D.Double(425, 225, 50, 50);
        graphics.draw(blackCircle);
        graphics.fill(blackCircle);

        graphics.draw(path1);

        graphics.setColor(Color.white);
        Ellipse2D whiteCircle = new Ellipse2D.Double(425, 425, 50, 50);
        graphics.fill(whiteCircle);

    }



    public static void main(String[] args)
    {
        launch(YingYang.class);
    }

}
