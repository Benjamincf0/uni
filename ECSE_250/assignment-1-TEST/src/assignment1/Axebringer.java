package assignment1;

public class Axebringer extends Warrior{

    public static double BASE_HEALTH;

    public static int BASE_COST;

    public static int WEAPON_TYPE = 2;

    public static int BASE_ATTACK_DAMAGE;

    public boolean axeThrown = false;

    public Axebringer(Tile position) {
        super(position, BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE, BASE_COST);
    }

    public int takeAction() {
        Tile currentPosition = this.getPosition();
        Tile nextPosition = currentPosition.towardTheCamp();

        // Skip turn if axe is thrown
        if (this.axeThrown) {
            this.axeThrown = false;
            return 0;
        }
        // If current tile has monster, attack the first one of the troop
        if (currentPosition.getMonster() != null) {
            Monster monster = currentPosition.getMonster();
            monster.takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
            double damageDealt = monster.takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
            return (int) Math.floor(BASE_ATTACK_DAMAGE/damageDealt + 1);
        }
        // If not, check if next tile towards camp has monster and is not camp. If so, attack it
        else {
            if (nextPosition.getMonster() != null) {
                if (!nextPosition.isCamp()) {
                    Monster nextMonster = nextPosition.getMonster();
                    nextMonster.takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
                    this.axeThrown = true;
                    double damageDealt = nextMonster.takeDamage(BASE_ATTACK_DAMAGE, WEAPON_TYPE);
                    return (int) Math.floor(BASE_ATTACK_DAMAGE/damageDealt + 1);

                }
            }
        }
        return 0;

    }

}
