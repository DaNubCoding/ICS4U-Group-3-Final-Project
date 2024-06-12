import greenfoot.GreenfootImage;

public class RickBoombox extends Magic {
    private Timer shockwaveTimer;

    public RickBoombox(Vector3 startpos, int inaccuracy) {
        super(startpos.add(new Vector3(0, 5, 0)), inaccuracy, -1, 600, 0);
        setOriginalImage(new GreenfootImage("rpg_explosion.png"));
        shockwaveTimer = new Timer(32);
    }

    @Override
    public void actionUpdate() {
        if (shockwaveTimer.ended()) {
            shockwaveTimer.restart();
            Damage dmg = new Damage(getWorld().getPlayer(), this, 10, getWorldPos(), 60);
            getWorld().getDamages().add(dmg);

            for (Entity e : getWorld().getEntitiesInRange(getWorldPos(), 60)) {
                e.physics.applyForce(e.getWorldPos().subtract(getWorldPos()).normalize().multiply(2));
            }

            for (int theta = 0; theta < 360; theta += 15) {
                for (int i = 0; i < 60; i += 15) {
                    double x = Math.cos(Math.toRadians(theta)) * i;
                    double y = Math.sin(Math.toRadians(theta)) * i;
                    Vector3 particleLocation = getWorldPos().add(Vector3.fromXZ(new Vector2(x, y)));
                    getWorld().addWorldObject(new ArmorParticle(), particleLocation);
                }
            }
        }
    }
}
