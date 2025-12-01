package assignment1;

public abstract class Warrior extends Fighter{

    private int requiredSkillPoints;

    public static double CASTLE_DMG_REDUCTION;

    public Warrior(Tile position, double health, int weaponType, int attackDamage, int requiredSkillPoints) {
        super(position, health, weaponType, attackDamage);
        this.requiredSkillPoints = requiredSkillPoints;
    }

    public int getTrainingCost() {
        return this.requiredSkillPoints;
    }

    @Override
    public double takeDamage(double rawDamage, int oppWeaponType) {
        double damage;
        // If position is castle, reduce damage
        if (this.getPosition() != null && this.getPosition().isCastle()) {
            damage = super.takeDamage(rawDamage*(1-CASTLE_DMG_REDUCTION), oppWeaponType);
        }
        else {
            damage = super.takeDamage(rawDamage*(1-CASTLE_DMG_REDUCTION), oppWeaponType);
        }

        return damage;
    }
}
