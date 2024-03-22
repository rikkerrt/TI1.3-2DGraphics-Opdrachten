import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage[] tiles;
    private int[] walkSprites;
    private int[] jumpSprites;
    private int x = 0;
    private int tile = 0;
    private int choice = 0;
    private int timer = 0;
    private boolean jumping = false;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        getSubImages();
        getWalkAnimationIntegers();
        getJumpAnimationIntegers();
        canvas.setOnMouseClicked(event -> mouseClick(event));
        canvas.setOnMouseReleased(event -> mouseRelease(event));
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Image image = tiles[tile];
        graphics.drawImage(image, x, (int) (canvas.getHeight()/2), null);
    }


    public void update(double deltaTime)
    {
        timer++;

        if(timer > 10) {
            if (!jumping) {
                x+=5;

                if(x > canvas.getWidth()) {
                    x = 0;
                }

                tile = walkSprites[choice];

                if (choice == 6) {
                    choice = 0;
                } else {
                    choice++;
                }
            } else {
                tile = jumpSprites[choice];

                if (choice == 8) {
                    choice = 0;
                    jumping = false;
                } else {
                    choice++;
                }
            }
            timer = 0;
        }
    }

    public static void main(String[] args)
    {
        launch(MovingCharacter.class);
    }

    public void getSubImages() {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/images/sprite.png"));
            tiles = new BufferedImage[65];
            for (int i = 0; i < 65; i++)
                tiles[i] = image.getSubimage(64 * (i % 8), 64 * (i / 8), 64, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getWalkAnimationIntegers() {
        walkSprites = new int[8];
        int j = 33;
        for (int i = 0; i < 8; i++) {
            walkSprites[i] = j;
            j++;
        }
    }
    private void getJumpAnimationIntegers(){
        jumpSprites = new int[9];
        int j = 40;
        for (int i = 0; i < 8; i++) {
            jumpSprites[i] = j;
            j++;
        }
    }
    private void mouseClick(MouseEvent event) {
        jumping = true;
    }
    private void mouseRelease(MouseEvent event) {
    }


}