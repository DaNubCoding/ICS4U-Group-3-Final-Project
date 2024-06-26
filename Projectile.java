import java.util.List;

/**
 * A projectile, shot by a ranged weapon.
 *
 * @author Lucas Fu
 * @author Martin Baldwin
 * @version June 2024
 */
public abstract class Projectile extends WorldSprite {
    private Entity owner;
    private Timer lifeTimer;
    public final PhysicsController physics = new PhysicsController(this);

    /**
     * Create a new projectile using direction, starting position, and inaccuracy
     *
     * @param owner the entity that created this projectile
     * @param initialVel the initial velocity of the projectile
     * @param startPos the initial position of the projectile
     * @param inaccuracy the inaccuracy of the projectile, measured in degrees
     * @param lifespan the number of frames this projectile can last
     */
    public Projectile(Entity owner, Vector3 initialVel, Vector3 startPos, int lifespan) {
        this.owner = owner;

        physics.applyForce(initialVel);
        physics.setAffectedByFrictionalForces(false);
        physics.setAffectedByGravity(false);
        setWorldPos(startPos);

        lifeTimer = new Timer(lifespan);
    }

    @Override
    public void update() {
        movingUpdate();
        physics.update();
        if (hitCondition()) hit();
        if (lifeTimer.ended()) disappear();
    }

    /**
     * An update that is performed while moving.
     * <p>
     * This method currently only rotates, so override it to add some features!
     */
    public void movingUpdate() {
        physics.setWorldRotation(getScreenRotation()+5);
    }

    /**
     * Test whether or not the given sprack is a solid sprack. A solid sprack
     * will be able to be hit by projectiles.
     *
     * @return true if the given sprack object is considered solid, false otherwise
     */
    public static boolean isSprackSolid(Sprack s) {
        return !(s instanceof DirtSpawner
            || s instanceof EnemySpawner
            || s instanceof GrassSpawner
            || s instanceof PondSpawner);
    }

    /**
     * The condition to check if a hit happened.
     * <p>
     * Currently checks whether a direct hit occured (enemy less than range of 10),
     * but don't forget that you can override this!
     *
     * @return whether the condition has been met
     */
    public boolean hitCondition() {
        List<Entity> l = getWorld().getEntitiesInRange(getWorldPos(), 10);
        for (Entity e : l) {
            if (e == owner) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * An update that is performed when hitting a target.
     * <p>
     * This method currently only makes the projectile disappear, so override it
     * to add some features!
     */
    public void hit() {
        disappear();
    }

    /**
     * Updates to be performed when disappearing.
     * <p>
     * This method currently only makes this disappear, so override it to add
     * some interesting features!
     */
    public void disappear() {
        getWorld().removeSprite(this);
    }

    /**
     * Get the owner of this projectile.
     *
     * @return the owner of this projectile
     */
    public Entity getOwner() {
        return owner;
    }
}
