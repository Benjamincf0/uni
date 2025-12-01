package assignment1;

public class Tile {
    private boolean isCastle;
    private boolean isCamp;
    private boolean onThePath;
    private Tile towardTheCastle; // null if not on the path or at the end of it
    private Tile towardTheCamp; // null if not on the path or at the end of it
    private Warrior warrior;
    private MonsterTroop troop;

    public Tile() {
        this.isCastle = this.isCamp = this.onThePath = false;
        this.towardTheCamp = this.towardTheCastle = null;
        this.warrior = null; //TODO
        this.troop = new MonsterTroop();
    }

    public Tile(boolean isCastle, boolean isCamp, boolean onThePath, Tile towardTheCastle, Tile towardTheCamp, Warrior warrior, MonsterTroop troop) {
        this.isCastle = isCastle;
        this.isCamp = isCamp;
        this.onThePath = onThePath;
        this.towardTheCastle = towardTheCastle;
        this.towardTheCamp = towardTheCamp;
        this.warrior = warrior;
        this.troop = troop;
    }

    public boolean isCastle() {
        return isCastle;
    }

    public boolean isCamp() {
        return isCamp;
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
        if (!this.onThePath || isCastle) {
            return null;
        }
        return this.towardTheCastle;
    }

    public Tile towardTheCamp() {
        if (!this.onThePath || isCamp) {
            return null;
        }
        return this.towardTheCamp;
    }

    // WHICH parameters should be defined?????
    public void createPath(Tile toCastle, Tile toCamp) {
        if (toCastle == null && toCamp == null) {
            throw new IllegalArgumentException("Both input Tiles cannot be null");
        }
        // } else if (toCastle == null) {
        //     this.isCastle = true;
        //     this.isCamp = false;
        //     this.onThePath = true;
        //     this.towardTheCastle = toCastle;
        //     this.towardTheCamp = toCamp;

        // } else if (toCamp == null) {
        //     this.isCastle = false;
        //     this.isCamp = true;
        //     this.onThePath = true;
        //     this.towardTheCastle = toCastle;
        //     this.towardTheCamp = toCamp;

        // } else {
        //     this.isCastle = false;
        //     this.isCamp = false;
        //     this.onThePath = true;
        //     this.towardTheCastle = toCastle;
        //     this.towardTheCamp = toCamp;
        // }
            this.onThePath = true;
            this.towardTheCastle = toCastle;
            this.towardTheCamp = toCamp;
    }

    public int getNumOfMonsters() {
        if (troop == null) {
            return 0;
        }
        return troop.sizeOfTroop();
    }

    public Warrior getWarrior() {
        return this.warrior;
    }

    public Monster getMonster() {
        if (troop == null) {
            return null;
        }

        return troop.getFirstMonster();
    }

    public Monster[] getMonsters() {
        return troop.getMonsters();
    }

    public boolean addFighter(Fighter fighter) {
        if (fighter instanceof Monster && (this.onThePath || this.isCamp || this.isCastle)) {
            this.troop.addMonster((Monster) fighter);
            fighter.setPosition(this);
            return true;
        } else if (fighter instanceof Warrior && this.warrior == null && !this.isCamp) {
            this.warrior = (Warrior) fighter;
            fighter.setPosition(this);
            return true;
        }
        return false;
    }

    public boolean removeFighter(Fighter fighter) {
        if (fighter instanceof Monster) {
            boolean removed = this.troop.removeMonster((Monster) fighter);
            if (removed) {
                fighter.setPosition(null);
            }
            return removed;
        } else if (fighter instanceof Warrior && this.warrior == fighter) {
            this.warrior = null;
            fighter.setPosition(null);
            return true;
        }
        return false;
    }
}
