package assignment1;

public abstract class Fighter {
    private Tile position = new Tile();
    private double health;
    private int weaponType;
    private int attackDamage;

    public Fighter(Tile position, double health, int weaponType, int attackDamage) {
        if (!(position.addFighter(this))) {
            throw new IllegalArgumentException("Fighter already exists at that position");
        }
        this.position = position;
        this.health = health;
        this.weaponType = weaponType;
        this.attackDamage = attackDamage;
    }

    public final Tile getPosition() {
        return this.position;
    }

    public final double getHealth() {
        return this.health;
    }

    public final int getWeaponType() {
        return this.weaponType;
    }

    public final int getAttackDamage() {
        return this.attackDamage;
    }

    public void setPosition(Tile position) {
        this.position = position;
    }

    public double takeDamage(double rawDamage, int oppWeaponType) {
        double multiplier;
        if (oppWeaponType > this.weaponType) {
            multiplier = 1.5;
        }
        else if (oppWeaponType < this.weaponType) {
            multiplier = 0.5;
        }
        else {
            multiplier = 1;
        }
        double damage = rawDamage * multiplier;

        // If fighter's health drops to 0 or below, remove them from the game
        if (this.health <= 0) {
            if (this instanceof Monster) {
                // If the fighter is a monster, remove it from the troop
                Tile tile = this.getPosition();
                if (tile != null) {
                    tile.removeFighter(this);
                }
            }
            else {
                // For warriors, just remove them from the tile
                this.position.removeFighter(this);
            }
            this.position = null; // Fighter is no longer on any tile
        }
        return damage;
    }

    public abstract int takeAction();

    @Override
    public boolean equals(Object o) {
        // Check if they have same reference and make sure not null
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        // Make object a Fighter
        Fighter fighter = (Fighter) o;

        // Compare fighter's type, position and health
        if (fighter.getClass() == this.getClass() &&
                this.position == ((Fighter) fighter).position &&
                Math.abs(this.health-((Fighter) fighter).health) <= 0.001) {
            return true;
        }
        else {
            return false;
        }
    }
}
