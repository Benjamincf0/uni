package assignment1;

public class MonsterTroop {

    private Monster[] monsters;

    private int numOfMonsters;

    public MonsterTroop() {
        this.numOfMonsters = 0;
        this.monsters = new Monster[0];
    }

    public int sizeOfTroop() {
        return this.numOfMonsters;
    }

    public Monster[] getMonsters() {
        Monster[] copy = new Monster[this.numOfMonsters];
        for (int i = 0; i < this.numOfMonsters; i++) {
            if (this.monsters[i] != null) {
                copy[i] = this.monsters[i];
            }
        }
        return copy;
    }

    public Monster getFirstMonster() {
        if (numOfMonsters > 0) {
            return this.monsters[0];
        }
        else {
            return null;
        }
    }

    public void addMonster(Monster monster) {
        // Double troop size if troop is full

        if (this.numOfMonsters == 0) {
            monsters = new Monster[1];
        }

        if (this.numOfMonsters == this.monsters.length) {
            doubleTroopSize();
        }

        // Add monster to first null spot in troop
        this.monsters[this.numOfMonsters] = monster;

        this.numOfMonsters++;
    }

    private void doubleTroopSize() {
        // Create troop of double size
        Monster[] new_monsters = new Monster[this.monsters.length*2];

        // Fill in new troop with monsters of old troop
        for (int i = 0; i < this.numOfMonsters; i++) {
            new_monsters[i] = this.monsters[i];
        }

        this.monsters = new_monsters;
    }

    public boolean removeMonster(Monster monster) {
        for (int i = 0; i < this.numOfMonsters; i++) {
            if (this.monsters[i] == monster) {
                shiftLeft(i);
                this.numOfMonsters--;
                return true;
            }
        }
        return false;
    }

    private void shiftLeft(int index) {
        // Shift elements of the array to the left from index
        for (int i = index; i < this.numOfMonsters - 1; i++) {
            this.monsters[i] = this.monsters[i + 1]; // Copy next element into current position
        }
        // Clear the last element to avoid duplicates
        this.monsters[this.numOfMonsters - 1] = null;

    }


}
