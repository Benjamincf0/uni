package assignment1;

public class Monster extends Fighter {
    private int rageLevel = 0;
    public static int BERSERK_THRESHOLD;

    public Monster(Tile position, double hp, int weaponType, int attackDamage) {
        super(position, hp, weaponType, attackDamage);
    }

    @Override
    public int takeAction() {
        if (this.getPosition().getWarrior() != null) {
            // Monster attacks
            this.getPosition().getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());
        } else {
            Tile currentTile = this.getPosition();
            Tile nextTile = currentTile.towardTheCastle();
            currentTile.removeFighter(this);
            this.setPosition(nextTile);
            nextTile.addFighter(this);
        }

        // 2x when rage
        if (rageLevel > BERSERK_THRESHOLD) {
            rageLevel = 0;
            this.takeAction();
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        Monster other = (Monster) obj;
        return Math.abs(this.getAttackDamage() - other.getAttackDamage()) <= 0.001;
    }

    @Override
    public double takeDamage(double damage, int oppWeaponType) {
        int weaponDiff = oppWeaponType - this.getWeaponType();
        if (weaponDiff > 0) {
            this.rageLevel += weaponDiff;
        }

        return super.takeDamage(damage, oppWeaponType);
    }

}
