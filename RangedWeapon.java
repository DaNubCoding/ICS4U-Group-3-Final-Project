import greenfoot.Greenfoot;

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
        public Projectile create(Sprack owner, Vector3 direction, Vector3 pos, int inaccuracy);
    }

    private ProjectileFactory projFactory;
    private int inaccuracy;
    private double speed;
    private int shotCount;

    public RangedWeapon(Player player, String image, int inaccuracy,
                        double speed, int shotCount, 
                        ProjectileFactory projectileFactory) {
        super(player, image);
        projFactory = projectileFactory;
        this.inaccuracy = inaccuracy;
        this.speed = speed;
        this.shotCount = shotCount;
    }

    @Override
    public void update() {
        super.update();
        if(Greenfoot.mouseClicked(null)) {
            attack();
        }
    }

    /**
     * Get the player's shooting direction from the mouse
     * 
     * @return the direction of the mouse as a 3d vector with no vertical component
     */
    public Vector3 getProjectileDirection() {
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
            Projectile proj = projFactory.create(getPlayer(), direction, spawnPos, inaccuracy);
            getPlayer().getWorld().addSprite(proj, 0, 0);
        }
    }
}
