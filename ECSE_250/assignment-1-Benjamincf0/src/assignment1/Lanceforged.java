package assignment1;

public class Lanceforged extends Warrior {
    public static double BASE_HEALTH;
    public static int BASE_COST;
    public static int WEAPON_TYPE = 3;
    public static int BASE_ATTACK_DAMAGE;
    private int piercingPower;
    private int actionRange;

    public Lanceforged(Tile position, int piercingPower, int actionRange) {
        super(position, BASE_HEALTH, WEAPON_TYPE, BASE_COST, BASE_ATTACK_DAMAGE);
        this.piercingPower = piercingPower;
        this.actionRange = actionRange;
    }

    public int takeAction() {
        Tile toCamp = this.getPosition();
        int totalSkillPoints = 0;
        int monstersHit = 0;

        for (int j = 0; j < this.actionRange; j++) {
            if (toCamp == null) {
                break;
            }
            if (toCamp.getNumOfMonsters() > 0) {
                int n = Math.min(toCamp.getNumOfMonsters(), piercingPower);
                Monster[] monsters = toCamp.getMonsters();

                for (int i = 0; i < n; i++) {
                    double damageDealt = monsters[i].takeDamage(Lanceforged.BASE_ATTACK_DAMAGE, Lanceforged.WEAPON_TYPE);
                    totalSkillPoints += (int) Math.floor((double) Lanceforged.BASE_ATTACK_DAMAGE / damageDealt + 1);
                    monstersHit++;
                }
                return (int) Math.floor((double) totalSkillPoints / monstersHit);
            }
            toCamp = toCamp.towardTheCamp();
        }

        return 0;
    }
}
