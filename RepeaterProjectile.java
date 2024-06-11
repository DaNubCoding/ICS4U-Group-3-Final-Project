import java.util.List;
import greenfoot.GreenfootImage;

/**
 * Write a description of class RepeaterProjectile here.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class RepeaterProjectile extends Projectile {
    public RepeaterProjectile(Entity owner, Vector3 direction, Vector3 startpos) {
        super(owner, direction, startpos, 100);
        setOriginalImage(new GreenfootImage("repeater_projectile.png"));
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 10);
        if(l.size() > 0 && l.contains(getOwner())) return false;
        return l.size() > 0;
    }

    @Override
    public void movingUpdate() {
        setWorldRotation(physics.getVelocity().xz.angle());
    }

    @Override
    public void hit() {
        // create damage
        Damage dmg = new Damage(getOwner(), this, 35, getWorldPos(), 10);
        getWorld().getDamages().add(dmg);
        disappear();
    }

}