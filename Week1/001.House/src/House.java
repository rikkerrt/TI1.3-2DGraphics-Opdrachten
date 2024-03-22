import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class House extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1024, 768);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        //walls
        graphics.drawLine(150, 600, 150, 300);
        graphics.drawLine(150, 600, 600, 600);
        graphics.drawLine(600, 600, 600, 300);
        graphics.drawLine(375, 50, 600, 300);
        graphics.drawLine(375, 50, 150, 300);

        //door
        graphics.drawLine(200, 600, 200, 400);
        graphics.drawLine(200, 400, 300, 400);
        graphics.drawLine(300, 400, 300, 600);

        //window
        graphics.drawLine(350, 500, 350, 400);
        graphics.drawLine(350, 400, 550, 400);
        graphics.drawLine(550, 400, 550, 500);
        graphics.drawLine(550, 500, 350, 500);
    }

    public static void main(String[] args) {
        launch(House.class);
    }

}
