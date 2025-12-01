package assignment1;

public class Bladesworn extends Warrior {
    public static double BASE_HEALTH;
    public static int BASE_COST;
    public static int WEAPON_TYPE = 3;
    public static int BASE_ATTACK_DAMAGE;

    public Bladesworn(Tile position) {
        super(position, BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE,  BASE_COST);
    }

    public int takeAction() {
        Tile currentTile = this.getPosition();
        Tile nextTile = currentTile.towardTheCamp();
        int skillPoints = 0;

        if (currentTile.getNumOfMonsters() > 0) {
            Monster monster = currentTile.getMonster();
            double damageDealt = monster.takeDamage(Bladesworn.BASE_ATTACK_DAMAGE, Bladesworn.WEAPON_TYPE);
            skillPoints = (int) Math.floor((double) Bladesworn.BASE_ATTACK_DAMAGE / damageDealt + 1);
        } else if (nextTile != null && nextTile.getWarrior() == null && !nextTile.isCamp()) {
            currentTile.removeFighter(this);
            this.setPosition(nextTile);
            nextTile.addFighter(this);
        }

        return skillPoints;
    }
}
