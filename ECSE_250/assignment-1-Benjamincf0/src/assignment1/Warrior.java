package assignment1;

abstract public class Warrior extends Fighter {
    private int requiredSkillPoints; // number of skill points required to train this warrior
    public static double CASTLE_DMG_REDUCTION; // % of damage reduction warriors recieve when on castle tile.


    public Warrior(Tile position, double hp, int weaponType, int attackDamage, int requiredSkillPoints) {
        super(position, hp, weaponType, attackDamage);
        this.requiredSkillPoints = requiredSkillPoints;
    }

    public int getTrainingCost() {
        return this.requiredSkillPoints;
    }

    @Override
    public double takeDamage(double damage, int oppWeaponType) {
                return super.takeDamage(this.getPosition().isCastle()
                ? damage * CASTLE_DMG_REDUCTION
                : damage,
            oppWeaponType);
    }
}
