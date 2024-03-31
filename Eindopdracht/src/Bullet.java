import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;
import org.jfree.fx.FXGraphics2D;

import javax.print.attribute.standard.PresentationDirection;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Bullet {
    private Body body;
    private GameObject gameObject;

    public Body getBody() {
        return body;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public World world;

    public Bullet(GameObject mainship, World world) {
        body = new Body();
        body.addFixture(Geometry.createRectangle(0.1, 0.25));
        body.getTransform().setTranslation(mainship.getBody().getTransform().getTranslationX(), mainship.getBody().getTransform().getTranslationY() + 0.6);
        body.setMass(MassType.NORMAL);
        body.applyForce(new Force(new Vector2(0, 2)));
        body.setUserData("Bullet");
        world.addBody(body);

        this.world = world;
        gameObject = new GameObject("/bullet.png", body, new Vector2(0, 0), .05);
    }

    public ArrayList<DetectResult> hasHit() {
        Point2D pos = new Point2D.Double(getBody().getTransform().getTranslationX(), getBody().getTransform().getTranslationY());
        Convex convex = Geometry.createRectangle(0.1, 0.35);
        Transform tx = new Transform();
        tx.translate(pos.getX(), pos.getY());

        Body testBody = new Body();
        testBody.addFixture(new BodyFixture(convex));
        testBody.setUserData("Bullet");

        //uncomment for debug draw
        testBody.getTransform().setTranslation(pos.getX(), pos.getY());
//        world.addBody(testBody);

        ArrayList<DetectResult>  results = new ArrayList<>();

        world.detect(convex, tx, null, false, false, true, results);

        return results;
    }

    public void draw(FXGraphics2D g2d) {
        gameObject.draw(g2d);
    }
}