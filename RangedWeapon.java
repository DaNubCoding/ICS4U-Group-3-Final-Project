/**
 * Weapon that fires projectiles.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public abstract class RangedWeapon extends Weapon {

    @FunctionalInterface
    public static interface ProjectileFactory {
        public Projectile create(Entity owner, Vector3 initialVel, Vector3 pos);
    }

    private ProjectileFactory projFactory;
    private int inaccuracy;
    private double speed;
    private int shotCount;

    public RangedWeapon(Player player, String image, int inaccuracy,
                        double speed, int shotCount, int windup, int cooldown,
                        ProjectileFactory projectileFactory) {
        super(player, image, windup, cooldown);
        projFactory = projectileFactory;
        this.inaccuracy = inaccuracy;
        this.speed = speed;
        this.shotCount = shotCount;
    }

    @Override
    public void update() {
        super.update();
    }

    /**
     * Get the player's shooting direction from the mouse
     *
     * @return the direction of the mouse as a 3d vector with no vertical component
     */
    private Vector3 getProjectileDirection() {
        Vector2 targetPos = MouseManager.getMouseWorldPos();
        Vector3 gunPos = getWorldPos();
        Vector3 targetPos3 = new Vector3(targetPos.x, 0, targetPos.y);
        Vector3 direction3 = (targetPos3.subtract(gunPos)).normalize();
        return direction3;
    }

    @Override
    public void attack() {
        Vector3 spawnPos = getWorldPos();
        Vector3 direction = getProjectileDirection().multiply(speed);
        for(int i = 0; i < shotCount; i++) {
            Vector3 newDir = adjustForInaccuracy(direction, inaccuracy);
            Projectile proj = projFactory.create(getPlayer(), newDir, spawnPos);
            getPlayer().getWorld().addSprite(proj, 0, 0);
        }
    }

    private static Vector3 adjustForInaccuracy(Vector3 initialVel, int inaccuracy) {
        double dAngle = Math.random() * inaccuracy - inaccuracy / 2.0;
        double adjustedAngle = initialVel.xz.angle() + dAngle;
        Vector2 adjVector = new Vector2(adjustedAngle);
        return initialVel.add(Vector3.fromXZ(adjVector));
    }
}
