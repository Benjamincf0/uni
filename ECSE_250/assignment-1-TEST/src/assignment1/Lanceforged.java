package assignment1;

public class Lanceforged extends Warrior{

    public static double BASE_HEALTH;

    public static int BASE_COST;

    public static int WEAPON_TYPE = 3;

    public static int BASE_ATTACK_DAMAGE;

    private int piercingPower;

    private int actionRange;

    public Lanceforged(Tile position, int piercingPower, int actionRange) {
        super(position, BASE_HEALTH, WEAPON_TYPE, BASE_ATTACK_DAMAGE, BASE_COST);
        this.piercingPower = piercingPower;
        this.actionRange = actionRange;
    }

    public int takeAction() {
        Tile checkPosition = this.getPosition();
        if (checkPosition == null) {
            return 0; // Edge case: no position
        }
        double totalDamage = 0.0;
        int countAttacks = 0;

        for (int i = 0; i <= this.actionRange; i++) {
            // Check if tile is valid, on the path, not camp, and has monsters
            if (checkPosition.isOnThePath() && !checkPosition.isCamp() && checkPosition.getNumOfMonsters() > 0) {
                // Get a copy of monsters on this tile
                Monster[] monsters = checkPosition.getMonsters();
                int reach = Math.min(monsters.length, this.piercingPower);

                // Attack the first `reach` monsters in the copy
                for (int j = 0; j < reach; j++) {
                    Monster monster = monsters[j];
                    // Check if the monster is still alive and on the tile
                    if (monster.getHealth() > 0 && monster.getPosition() == checkPosition) {
                        double damageDealt = monster.takeDamage(this.getAttackDamage(), WEAPON_TYPE);
                        totalDamage += damageDealt;
                        countAttacks++;
                    }
                }
                break; // Attack nearest troop only
            }

            // Move toward the camp for the next iteration
            checkPosition = checkPosition.towardTheCamp();
            if (checkPosition == null) break; // No more tiles to check
        }

        if (countAttacks > 0) {
            double averageDamage = totalDamage / countAttacks;
            int skillPointsGained = (int) Math.floor((this.getAttackDamage() / averageDamage) + 1);
            return skillPointsGained;
        }

        return 0;
    }

}
