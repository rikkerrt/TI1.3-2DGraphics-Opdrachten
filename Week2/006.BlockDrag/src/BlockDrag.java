import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application {
    ResizableCanvas canvas;
    Block selectedBlock;
    ArrayList<Block> blocks = new ArrayList<>();
    private double deltaX;
    private double deltaY;
    private double beginX;
    private double beginY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();
        Random random = new Random();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        double width = 50;
        double height = 50;
        double x = 0;
        for (int i = 0; i < 100; i += 10) {
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);
            blocks.add(new Block(new Rectangle2D.Double(r * 1.5, g * 1.5, width, height), new Point2D.Double(x, i), new Color(r, g, b)));
        }
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Block renderable : blocks) {
            renderable.draw(graphics);
        }
    }

    private void mousePressed(MouseEvent e) {
        Point2D position = new Point2D.Double(e.getX(), e.getY());
        for (Block block : blocks) {
            if (block.getFinalShape().contains(position)) {
                selectedBlock = block;
                beginX = e.getX();
                beginY = e.getY();
            }
        }
    }

    private void mouseReleased(MouseEvent e) {
        selectedBlock = null;
    }

    private void mouseDragged(MouseEvent e) {
        if (selectedBlock == null) {
            return;
        }
        deltaX = beginX - e.getX();
        beginX = e.getX();
        deltaY = beginY - e.getY();
        beginY = e.getY();

        Point2D position = new Point2D.Double(selectedBlock.getPosition().getX() - deltaX, selectedBlock.getPosition().getY() - deltaY);
        selectedBlock.setPosition(position);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));

    }
}