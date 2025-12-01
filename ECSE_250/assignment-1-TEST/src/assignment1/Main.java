package assignment1;
public class Main {
    public static void main(String[] args) {
        testMonsterTroop();
    }

    public static void testMonsterTroop(){

        //I first create some monsters
        Monster monster1 = new Monster(null, 100, 1, 10);
        Monster monster2 = new Monster(null, 200, 2, 20);
        Monster monster3 = new Monster(null, 300, 3, 30);
        Monster monster4 = new Monster(null, 400, 4, 40);
        Monster monster5 = new Monster(null, 500, 5, 50);
        Monster monster6 = new Monster(null, 600, 6, 60);

        //I create an array from the monsters I made
        Monster[] monsterArr = new Monster[] {monster1, monster2, monster3, monster4, monster5, monster6};

        //I create an empty MomnsterTroop object
        MonsterTroop legion = new MonsterTroop();
        System.out.println(legion.getFirstMonster() + " " + legion.sizeOfTroop());// expected: null 0

        //I add my previously created array of monsters to my MonsterTroop object
        legion = createLegion(monsterArr);

        //I test out the characteristics of my object
        System.out.println(legion.getFirstMonster() + " " + legion.sizeOfTroop());// expected: assignment1.Monster(some address) 6
        System.out.println(legion.removeMonster(monster2) + " " + legion.removeMonster(monster1) + " " + legion.removeMonster(monster1));// expected: true true false
        System.out.println(legion.getFirstMonster() + " " + legion.sizeOfTroop());// expected: assignment1.Monster(a different address) 4

        //I add other monsters (but one that is already present in the object)
        legion.addMonster(monster4);
        legion.addMonster(monster4);

        //I test the removeMonster method to see if it truly removes the first instance of monster4 and also test
        //the fields of my monster object
        legion.removeMonster(monster4);
        for (int index = 0; index < legion.getMonsters().length; index++){
            System.out.println("Position: " + legion.getMonsters()[index].getPosition() + ", Hp: " + legion.getMonsters()[index].getHealth() +
                    ", Attack Damage: " + legion.getMonsters()[index].getAttackDamage() + ", Weapon Type: " + legion.getMonsters()[index].getWeaponType());
        }
        // expected:
        //Position: null, Hp: 300.0, Attack Damage: 30, Weapon Type: 3
        //Position: null, Hp: 500.0, Attack Damage: 50, Weapon Type: 5
        //Position: null, Hp: 600.0, Attack Damage: 60, Weapon Type: 6
        //Position: null, Hp: 400.0, Attack Damage: 40, Weapon Type: 4
        //Position: null, Hp: 400.0, Attack Damage: 40, Weapon Type: 4
    }

    private static MonsterTroop createLegion(Monster[] monsterArr){
        MonsterTroop legion = new MonsterTroop();
        for (int monster = 0; monster < monsterArr.length; monster++){
            legion.addMonster(monsterArr[monster]);
        }
        return legion;
    }
}