import org.jfree.fx.ResizableCanvas;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Point {
    private Point2D position;
    private double xDirection = 2;
    private double yDirection = 2;

    public Point(Point2D position) {
        this.position = position;
    }

    public void move(ResizableCanvas canvas, int pointNumber) {
        checkBorders(canvas);
        double x = getX();
        double y = getY();
        switch (pointNumber) {
            case 0:
                this.position = new Point2D.Double(x - xDirection, y - yDirection);
                break;
            case 1:
                this.position = new Point2D.Double(x + xDirection, y + yDirection);
                break;
            case 2:
                this.position = new Point2D.Double(x + xDirection, y - yDirection);
                break;
            case 3:
                this.position = new Point2D.Double(x - xDirection, y + yDirection);
                break;
        }
    }

    public void checkBorders(ResizableCanvas canvas) {
        if (this.position.getX() < 0 || position.getX() >= canvas.getWidth()) {
            xDirection = -xDirection;
        }
        if (this.position.getY() < 0 || this.position.getY() >= canvas.getHeight()) {
            yDirection = -yDirection;
        }
    }

    public void addPointHistory(ArrayList<Point> points, int index) {
        Point oldLocation = new Point(this.position);
        points.add(index, oldLocation);
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }
}