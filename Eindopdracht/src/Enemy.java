import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

public class Enemy {

    private int hitsLeft;
    private Body body;
    private World world;

    public Body getBody() {
        return body;
    }
    private GameObject gameObject;

    public Enemy(World world, int x, int y) {
        this.world = world;
        hitsLeft = 3;

        Body enemySip = new Body();
        enemySip.addFixture(Geometry.createRectangle(0.68, 0.5));
        enemySip.getTransform().setTranslation(x, y);
        enemySip.setMass(MassType.NORMAL);
        world.addBody(enemySip);
        gameObject = new GameObject("/enemyShip.png", enemySip, new Vector2(0,40), 0.1);
    }

    public boolean noHitsLeft() {
        if(hitsLeft == 1) {
            return true;
        }
        hitsLeft--;
        return true;
    }

    public void draw(FXGraphics2D g2d) {
        gameObject.draw(g2d);
    }
}
