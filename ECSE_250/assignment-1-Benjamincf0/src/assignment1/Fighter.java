package assignment1;

public abstract class Fighter {
    private Tile position;
    private double health;
    private int weaponType;
    private int attackDamage;

    public Fighter(Tile position, double hp, int weaponType, int attackDamage){
        this.position = position;
        this.health = hp;
        this.weaponType = weaponType;
        this.attackDamage = attackDamage;
        if (!position.addFighter(this)) {
            throw new IllegalArgumentException("Cannot add Fighter on specified Tile");
        }
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

    public void setPosition(Tile tile) {
        this.position = tile;
    }

    public double takeDamage(double damage, int oppWeaponType) {
        double effectiveDamage;
        if (oppWeaponType > this.weaponType) {
            effectiveDamage = 1.5*damage;
        } else if (oppWeaponType < this.weaponType) {
            effectiveDamage = 0.5*damage;
        } else {
            effectiveDamage = damage;
        }

        this.health -= effectiveDamage;

        if (this.health <= 0) {
            this.position.removeFighter(this);
        }
        return effectiveDamage;
    }

    abstract public int takeAction();

    @Override
    public boolean equals(Object obj) {
        Fighter objFighter = (Fighter) obj;
        return obj.getClass() == this.getClass() && Math.abs(objFighter.health - this.health) <= 0.001 && objFighter.position == this.position;
    }
}
