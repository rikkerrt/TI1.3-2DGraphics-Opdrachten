import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    Canvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        graphics.translate(1920/2, 1000);
        graphics.scale(1,-1);
        float radiusBinnen = 700;
        float radiusBuiten = 500;
        float step = 10000f;


        for (int i = 0; i < step; i++) {
            graphics.setColor(Color.getHSBColor(i/step, 1, 1));
            double cos = Math.cos( i/step * Math.PI);
            double sin = Math.sin( i/step * Math.PI);
            double x1 = radiusBinnen * cos;
            double y1 = radiusBinnen * sin;
            double x2 = radiusBuiten * cos;
            double y2 = radiusBuiten * sin;
            graphics.draw(new Line2D.Double(x1,y1,x2,y2));
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
