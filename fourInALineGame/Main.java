package fourInALineGame;
import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    Scanner scanner1 = new Scanner(System.in);
    System.out.print("Would you like to go first? (y/n): ");
    String opponentFirstInput = scanner1.nextLine();  
    
    Scanner scanner2 = new Scanner(System.in);
    System.out.print("How long should the computer think about its moves (in seconds)? : ");
    int maxSeconds = scanner2.nextInt();
    System.out.println();
    
    boolean opponentFirst = false;
    if (opponentFirstInput.toLowerCase().equals("n"))
      opponentFirst = false;
    if (opponentFirstInput.toLowerCase().equals("y"))
        opponentFirst = true;

    Game game = new Game(maxSeconds, !opponentFirst);
    game.start();
  }
 }
