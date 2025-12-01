package assignment1;

public class MonsterTroop {
    private Monster[] monsters;
    private int numOfMonsters;

    public MonsterTroop() {
        this.monsters = new Monster[0];
        this.numOfMonsters = 0;
    }

    public int sizeOfTroop() {
        return this.numOfMonsters;
    }

    public Monster[] getMonsters() {
        // Shallow copy of monsters array
        Monster[] out = new Monster[numOfMonsters];
        for (int i = 0; i < numOfMonsters; i++) {
            out[i] = monsters[i];
        }

        return out;
    }

    public Monster getFirstMonster() {
        if (numOfMonsters > 0) {
            return monsters[0];
        }
        return null;
    }

    public void addMonster(Monster monster) {
        Monster[] newMonsters = new Monster[numOfMonsters + 1];
        for (int i = 0; i < numOfMonsters; i++) {
            newMonsters[i] = monsters[i];
        }
        newMonsters[numOfMonsters] = monster;
        this.monsters = newMonsters;
        this.numOfMonsters++;
    }

    public boolean removeMonster(Monster monster) {
        
        if (numOfMonsters == 0) {
            return false;
        }

        boolean found = false;
        for (int i = 0; i < numOfMonsters; i++) {
            if (this.monsters[i] == monster) {
                found = true;
                for (int j = i; j < numOfMonsters - 1; j++) {
                    this.monsters[j] = this.monsters[j + 1];
                }
                this.monsters[numOfMonsters - 1] = null;
                this.numOfMonsters--;
                break;
            }
        }

        return found;
    }
}