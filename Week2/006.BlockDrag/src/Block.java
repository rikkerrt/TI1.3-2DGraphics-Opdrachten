import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Block {
    private Shape shape;
    private Point2D position;

    private Color color;


    public Block(Shape shape, Point2D position, Color color)
    {
        this.shape = shape;
        this.position = position;
        this.color = color;
    }

    public void draw(FXGraphics2D graphics){
        Shape shapeToDraw = getFinalShape();
        graphics.draw(shapeToDraw);
        graphics.setColor(color);
        graphics.fill(shapeToDraw);
    }
    public Shape getFinalShape(){
        return getTransform().createTransformedShape(shape);
    }

    public AffineTransform getTransform(){
        AffineTransform transform = new AffineTransform();
        transform.translate(position.getX(),position.getY());
        return transform;
    }

    public Point2D getPosition() {
        return position;
    }
    public void setPosition(Point2D position){
        this.position = position;
    }
    public Shape getShape(){
        return shape;
    }

}
