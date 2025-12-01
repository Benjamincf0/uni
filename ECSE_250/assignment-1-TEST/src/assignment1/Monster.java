package assignment1;

public class Monster extends Fighter {

    private int rageLevel = 0;

    public static int BERSERK_THRESHOLD;

    public boolean berserkMode = false;


    public Monster(Tile position, double health, int weaponType, int attackDamage) {
        super(position, health, weaponType, attackDamage);
    }

    @Override
    public int takeAction() {
        Tile currentPosition = this.getPosition();
        boolean attacked = false;

        for (int i = 0; i < (this.berserkMode ? 2 : 1); i++) {

            // If there is warrior on that tile, attack it
            if (currentPosition.getWarrior() != null) {
                currentPosition.getWarrior().takeDamage(this.getAttackDamage(), this.getWeaponType());
                attacked = true;

            }
            // If no warrior, go to the next tile towards the castle
            else {
                Tile nextPosition = currentPosition.towardTheCastle();
                if (nextPosition != null) {
                    currentPosition.removeFighter(this);
                    if (nextPosition.addFighter(this)) {
                        this.setPosition(nextPosition);
                        currentPosition = nextPosition;
                    }
                }

            }
        }

        // Resetting rage levels after berserk mode
        if (this.berserkMode) {
            this.berserkMode = false;
            this.rageLevel = 0;
        }

        // If attack was done, move monster to back of troop
        if (attacked) {
            currentPosition.removeFighter(this);
            currentPosition.addFighter(this);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        // Check if they have same reference and make sure not null
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        // Make object a Monster
        Monster monster = (Monster) o;

        // Compare fighter's position, health and attack damage
        if (this.getPosition() == ((Monster) monster).getPosition() &&
                Math.abs(this.getHealth()-((Monster) monster).getHealth()) <= 0.001 &&
                this.getAttackDamage() == ((Monster) monster).getAttackDamage()) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public double takeDamage(double rawDamage, int oppWeaponType) {
        double damage =  super.takeDamage(rawDamage, oppWeaponType);
        // Add rage gain if opponent weapon more powerful

        if (this.getHealth() > 0) {
            if (oppWeaponType > this.getWeaponType()) {
                int rageGain = oppWeaponType - this.getWeaponType();
                this.rageLevel += rageGain;
                if (this.rageLevel >= BERSERK_THRESHOLD) {
                    this.berserkMode = true;
                }
            }
        }

        return damage;
    }
}
