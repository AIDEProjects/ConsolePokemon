package consolepokemon.examples;

import java.util.Scanner;

class Character {
    private String name;
    private int hp;
    private int speed;

    public Character(String name, int hp, int speed) {
        this.name = name;
        this.hp = hp;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void attack(Character target) {
        int damage = 10; // 简化攻击伤害
        target.setHp(target.getHp() - damage);
        System.out.println(name + " attacks " + target.getName() + " for " + damage + " damage.");
    }

    public void flee() {
        System.out.println(name + " flees from the battle.");
    }
}

class TurnSystem {
    private Character player1;
    private Character player2;

    public TurnSystem(Character player1, Character player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startTurn() {
        System.out.println("=== New Turn ===");
        while (player1.isAlive() && player2.isAlive()) {
            if (player1.getSpeed() >= player2.getSpeed()) {
                playerAction(player1, player2);
                if (player2.isAlive()) {
                    playerAction(player2, player1);
                }
            } else {
                playerAction(player2, player1);
                if (player1.isAlive()) {
                    playerAction(player1, player2);
                }
            }
        }

        if (!player1.isAlive()) {
            System.out.println(player1.getName() + " is defeated!");
        } else {
            System.out.println(player2.getName() + " is defeated!");
        }
    }

    private void playerAction(Character currentPlayer, Character opponent) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(currentPlayer.getName() + "'s turn. Choose action: (1) Attack (2) Flee");
        int action = scanner.nextInt();

        if (action == 1) {
            currentPlayer.attack(opponent);
        } else if (action == 2) {
            currentPlayer.flee();
            System.out.println(currentPlayer.getName() + " has escaped the battle.");
            System.exit(0);
        } else {
            System.out.println("Invalid action. Try again.");
        }
    }
}

public class BattleSystem {
	public BattleSystem(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name for Player 1: ");
        String player1Name = scanner.nextLine();
        Character player1 = new Character(player1Name, 50, 10);

        System.out.print("Enter name for Player 2: ");
        String player2Name = scanner.nextLine();
        Character player2 = new Character(player2Name, 50, 5);

        TurnSystem turnSystem = new TurnSystem(player1, player2);
        turnSystem.startTurn();
    }
}

