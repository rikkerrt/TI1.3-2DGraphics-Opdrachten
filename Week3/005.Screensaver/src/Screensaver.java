import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application {
    private ResizableCanvas canvas;
    private ArrayList<Point> points = new ArrayList<>();

    private ArrayList<ArrayList<Point>> lines = new ArrayList<>();
    private int linesIndex = 1;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                update();
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.setColor(new Color(176, 38, 255));


        for (ArrayList<Point> line : lines) {
            graphics.drawLine((int) line.get(0).getX(), (int) line.get(0).getY(), (int) line.get(1).getX(), (int) line.get(1).getY());
            graphics.drawLine((int) line.get(1).getX(), (int) line.get(1).getY(), (int) line.get(3).getX(), (int) line.get(3).getY());
            graphics.drawLine((int) line.get(3).getX(), (int) line.get(3).getY(), (int) line.get(2).getX(), (int) line.get(2).getY());
            graphics.drawLine((int) line.get(2).getX(), (int) line.get(2).getY(), (int) line.get(0).getX(), (int) line.get(0).getY());
        }
    }

    public void init() {
        createPoints();
    }

    public void update() {
        ArrayList<Point> temporaryList = new ArrayList<>();

        int pointIndex = 0;
        for (Point point : points) {
            point.addPointHistory(temporaryList, pointIndex);
            point.move(canvas, pointIndex);
            pointIndex++;
        }
        addToLinesArrayList(temporaryList);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    private void addToLinesArrayList(ArrayList<Point> points) {
        if (lines.size() == 25) {
            if (linesIndex >= lines.size()) {
                linesIndex = 1;
            }
            lines.remove(linesIndex);
        }
        lines.add(linesIndex, points);
        linesIndex++;
    }

    public void addLines() {
        lines.add(points);
    }

    private void createPoints() {
        Random random = new Random();
        Point2D point1 = new Point2D.Double(random.nextInt(300), random.nextInt(300));
        Point2D point2 = new Point2D.Double(random.nextInt(300), random.nextInt(300));
        Point2D point3 = new Point2D.Double(random.nextInt(300), random.nextInt(300));
        Point2D point4 = new Point2D.Double(random.nextInt(300), random.nextInt(300));
        points.add(new Point(point1));
        points.add(new Point(point2));
        points.add(new Point(point3));
        points.add(new Point(point4));
        addLines();
    }

    public static void main(String[] args) {
        launch(Screensaver.class);
    }

}