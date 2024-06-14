/**
 * Saint's barrior
 * 
 * @author Stanley 
 * @version June 2024
 */
public class SaintShield extends Enemy 
{
    private static final Animation shield = new Animation(-1, "saint_shield");
    private Saint owner;
    private double angle;
    
    public SaintShield(double direction, Saint owner)
    {
        super("saint_shield");
        setHealth(200);
        setNoticeRange(0);
        setForgetRange(0);
        physics.setMaxSpeed(2);
        physics.setMaxAccelMag(1);
        setWorldRotation(direction);
        this.owner = owner;
        this.angle = direction;
        physics.setAlwaysTurnTowardsMovement(false);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        super.addedToWorld(world);
        getWorld().addCollisionController(new CollisionController(this, 0, 0.8, 0.5));
    }

    @Override
    public void idle(Player player) {
        setHealth(super.getHealth()-0.05);
        physics.moveToTarget(owner.getWorldPos().xz.add(new Vector2(angle).multiply(20)));
    } // Takes damage over time, follows castor around

    @Override
    public void notice(Player player) {}

    @Override
    public void forget(Player player) {}

    @Override
    public void engage(Player player) {
    }

    @Override
    public void damage(Damage damage) {
        //Redirects a portion of all melee damage back
        Entity o = damage.getOwner();
        if (!(damage.getSource() instanceof Projectile)) {
            super.damage(damage.multiply(0.6));
            if (!(damage.getSource() instanceof Statue)) {
                Vector3 ownerPos = o.getWorldPos();
                Vector3 shieldPos = getWorldPos();
                final Vector3 dist = shieldPos.subtract(ownerPos);
                o.physics.applyForce(dist.normalize().multiply(-2));
            }
            o.damage(damage.multiply(0.4));
        } else {
            super.damage(damage);
        }
    }
}
