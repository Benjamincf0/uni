package assignment1;

public class Bladesworn extends Warrior{

    public static double BASE_HEALTH;

    public static int BASE_COST;

    public static int WEAPON_TYPE = 3;

    public static int BASE_ATTACK_DAMAGE;

    public Bladesworn(Tile position) {
        super(position, BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE, BASE_COST);
    }

    public int takeAction() {
        Tile currentPosition = this.getPosition();
        Tile nextPosition = currentPosition.towardTheCamp();

        // If current tile has monster, attack the first one of the troop
        if (currentPosition.getMonster() != null) {
            Monster monster = currentPosition.getMonster();
            double damageDealt = monster.takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
            return (int) Math.floor(BASE_ATTACK_DAMAGE/damageDealt + 1);
        }
        // If not, check if next tile towards camp is unoccupied and not camp. If so, move there
        else {
            if (nextPosition.getMonster() == null && nextPosition.getWarrior() == null && !nextPosition.isCamp()) {
                this.setPosition(nextPosition);
            }
        }
        return 0;
    }
}
