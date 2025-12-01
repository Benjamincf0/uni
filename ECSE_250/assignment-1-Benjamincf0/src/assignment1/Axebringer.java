package assignment1;

public class Axebringer extends Warrior{
    public static double BASE_HEALTH;
    public static int BASE_COST;
    public static int WEAPON_TYPE = 2;
    public static int BASE_ATTACK_DAMAGE;
    private boolean isIdle;

    public Axebringer(Tile position) {
        super(position, BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE, BASE_COST);
    }

    public int takeAction() {
        if (isIdle) {
            isIdle = false;
            return 0; // Skip this turn
        }

        Tile currentTile = this.getPosition();
        Tile nextTile = currentTile.towardTheCamp();
        int skillPoints = 0;

        if (currentTile.getNumOfMonsters() > 0) {
            Monster monster = currentTile.getMonster();
            double damageDealt = monster.takeDamage(Axebringer.BASE_ATTACK_DAMAGE, Axebringer.WEAPON_TYPE);
            skillPoints = (int) Math.floor((double) Axebringer.BASE_ATTACK_DAMAGE / damageDealt + 1);
        } else if (nextTile.getNumOfMonsters() > 0) {
            Monster monster = nextTile.getMonster();
            double damageDealt = monster.takeDamage(Axebringer.BASE_ATTACK_DAMAGE, Axebringer.WEAPON_TYPE);
            skillPoints = (int) Math.floor((double) Axebringer.BASE_ATTACK_DAMAGE / damageDealt + 1);
            isIdle = true;
        }
        return skillPoints;
    }
}
