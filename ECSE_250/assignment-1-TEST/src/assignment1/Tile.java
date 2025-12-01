package assignment1;

public class Tile {

    private boolean isCastle;

    private boolean isCamp;

    private boolean onThePath;

    private Tile towardTheCastle;

    private Tile towardTheCamp;

    private Warrior warrior;

    private MonsterTroop troop;

    public Tile() {
        this.isCastle = false;
        this.isCamp = false;
        this.onThePath = false;
        this.towardTheCastle = null;
        this.towardTheCamp = null;
        this.warrior = null;
        this.troop = new MonsterTroop();
    }

    public Tile(boolean isCastle, boolean isCamp, boolean onThePath, Tile towardTheCastle, Tile towardTheCamp) {
        this.isCastle = isCastle;
        this.isCamp = isCamp;
        this.onThePath = onThePath;
        this.towardTheCastle = towardTheCastle;
        this.towardTheCamp = towardTheCamp;
        this.warrior = null;
        this.troop = new MonsterTroop();
    }

    public boolean isCastle() {
        return this.isCastle;
    }

    public boolean isCamp() {
        return this.isCamp;
    }

    public void buildCastle() {
        this.isCastle = true;
    }

    public void buildCamp() {
        this.isCamp = true;
    }

    public boolean isOnThePath() {
        return this.onThePath;
    }

    public Tile towardTheCastle() {
        if (!this.onThePath || this.isCastle) {
            return null;
        }
        else {
            return this.towardTheCastle;
        }
    }

    public Tile towardTheCamp() {
        if (!this.onThePath || this.isCamp) {
            return null;
        }
        else {
            return this.towardTheCamp;
        }
    }

    public void createPath(Tile towardTheCastle, Tile towardTheCamp) {
        if (!isCastle && !isCamp) {
            if (towardTheCastle == null || towardTheCamp == null) {
                throw new IllegalArgumentException("Only extreme path tiles (Castle or Camp) can have a null next tile.");
            }
        }
        this.towardTheCastle = towardTheCastle;
        this.towardTheCamp = towardTheCamp;
        this.onThePath = true;
    }

    public int getNumOfMonsters() {
        return this.troop.sizeOfTroop();
    }

    public Warrior getWarrior() {
        return this.warrior;
    }

    public Monster getMonster() {
        return this.troop.getFirstMonster();
    }

    public Monster[] getMonsters() {
        return this.troop.getMonsters();
    }

    public boolean addFighter(Fighter fighter) {
        if (fighter instanceof Warrior) {
            if (getWarrior() != null || this.isCamp) {
                return false;
            }
            this.warrior = (Warrior) fighter;
        }

        else if (fighter instanceof Monster) {
            if (!this.onThePath) {
                return false;
            }
            this.troop.addMonster((Monster) fighter);
        }
        fighter.setPosition(this);
        return true;
    }

    public boolean removeFighter(Fighter fighter) {
        if (fighter instanceof Warrior) {
            if (fighter == this.warrior) {
                this.warrior = null;
            }
            else {
                return false;
            }
        }
        else if (fighter instanceof Monster) {
            if (!(this.troop.removeMonster((Monster) fighter))) {
                return false;
            }
        }
        fighter.setPosition(null);
        return true;
    }
}
