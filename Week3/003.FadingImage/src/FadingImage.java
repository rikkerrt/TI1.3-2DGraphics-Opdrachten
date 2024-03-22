import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class FadingImage extends Application {
    private ResizableCanvas canvas;
    private Image[] images;
    private float opacity;
    private boolean reverse;


    @Override
    public void start(Stage stage) throws Exception {
        images = new Image[]{
                ImageIO.read(getClass().getResource("image1.jpg")),
                ImageIO.read(getClass().getResource("image2.jpg"))
        };

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        this.opacity = 0f;
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 5000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading image");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.scale(2, 2);
        graphics.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        graphics.drawImage(images[0], 0,0, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.opacity));
        graphics.drawImage(images[1], 0,0, null);
    }


    public void update(double deltaTime) {
        if(reverse) {
            if(this.opacity - deltaTime < 0f) {
                reverse = false;
                return;
            }

            this.opacity -= (float) deltaTime;
        } else {
            if(this.opacity + deltaTime > 1f) {
                reverse = true;
                return;
            }
            this.opacity += (float) deltaTime;
        }
    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

}
