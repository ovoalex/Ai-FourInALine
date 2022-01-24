package fourInALineGame;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.toLowerCase;

public class Game {
  private int maxSeconds;
  private boolean computerFirst;
  private Board theBoard;
  private long startTime;
  private boolean foundWin = false;
  private ArrayList<findPairs> pairs = new ArrayList<>();
  private ArrayList<findPairs> finalPairs = new ArrayList<>();

  public Game(int maxSeconds, boolean computerFirst) {
    this.maxSeconds = maxSeconds;
    this.computerFirst = computerFirst;
  }

  public void start() {
    theBoard = new Board(computerFirst);
    theBoard.print();

    boolean computerWon = false;
    while (!theBoard.state.win()) {
      if (computerFirst || !theBoard.state.isEmpty()) {
        Move computerMove = computerTurn();
        theBoard.state.board[computerMove.row - 1][computerMove.col - 1] = 1;
        theBoard.addMove(computerMove);

        theBoard.print();
        if (theBoard.state.win(true)) {
          computerWon = true;
          break;
        }
      }

      int[] move = promptMove(theBoard.state.board);
      theBoard.state.board[move[0]][move[1]] = 2;
      theBoard.addMove(new Move(2, move[0] + 1, move[1] + 1));
      theBoard.print();
    }

    if (computerWon)
      System.out.println("Computer wins!\nGame Over!");
    else
      System.out.println("You win!\nGame Over!");
  }

  private boolean isValidMove(int row, int col, int[][] board) {
    // Check move is in bounds
    if (row < 0 || row >= 8 || col < 0 || col >= 8)
      return false;

    // Check move is in currently empty space
    if (board[row][col] == 1 || board[row][col] == 2) {
      System.out.println("Someone already moved there.");
      return false;
    }

    return true;
  }
  
  //get best move for computer
  private Move computerTurn() {
    search(theBoard.state);
    return getBestMove();
  }

  private int[] promptMove(int[][] board) {
    int row = -1;
    int col = -1;
    
    //check for valid move
    while (!isValidMove(row, col, board)) {
      String moveIn = "";
      while(moveIn.length() != 2) {
        System.out.print("Choose your next move: ");
        moveIn = new Scanner(System.in).nextLine();
      }
      System.out.println();
      row = (int) toLowerCase(moveIn.charAt(0)) - 97;
      col = getNumericValue(moveIn.charAt(1)) - 1;
    }

    return new int[]{row, col};
  }

  //find the best move 
  //find the pairs
  private Move getBestMove() {
    State bestState = finalPairs.get(0).state;
    int bestValue = finalPairs.get(0).value;
    for (findPairs p : finalPairs) {
      if (p.value > bestValue) {
        bestValue = p.value;
        bestState = p.state;
      } else if (p.value == bestValue && p.state.computerUtility() > bestState.computerUtility()) {
        bestState = p.state;
      }
    }

    Move bestMove = theBoard.state.getMove(bestState);
    
    //add randomness to given chosen move
    if (theBoard.state.isEmpty()) {
      Random r1 = new Random();
      if (r1.nextDouble() > 0.5)
        bestMove.row += 1;

      Random r2 = new Random();
      if (r2.nextDouble() > 0.5)
        bestMove.col += 1;
    }
    return bestMove;
  }

  //search at root for next best move
  private void search(State root) {
    startTime = System.nanoTime();
    for (int i = 1; i <= 999; i++) {
      pairs.clear();

      int result = alphaBeta(root, root, i, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

      if (result == -1) {
        break; // Returns -1 if out of time, need to stop searching
      }

      finalPairs.clear();
      for (findPairs p : pairs)
        finalPairs.add(new findPairs(p));

      if (finalPairs.size() > 0)

      if (foundWin) {
        foundWin = false;
        break;
      }
    }
  }

  private int alphaBeta(State root, State state, int depth, int alpha, int beta, boolean computer) {
    if (state.win(!computer)) {
      foundWin = true;
      if (state.childOf(root, !computer))
        pairs.add(new findPairs(999999999, state));

      return state.computerUtility();
    }

    if (depth == 0) {
      if (state.childOf(root, !computer))
        pairs.add(new findPairs(state.computerUtility(), state));

      return state.computerUtility();
    }

    ArrayList<State> successors = state.successors(computer);

    int value;
    if (computer) {
      value = Integer.MIN_VALUE;
      for (State child : successors) {
        value = Math.max(value, alphaBeta(root, child, depth - 1, alpha, beta, false));
        alpha = Math.max(alpha, value);

        if (outOfTime())
          return -1;

        if (alpha >= beta)
          break;
      }
    } else {
      value = Integer.MAX_VALUE;
      for (State child : successors) {
        value = Math.min(value, alphaBeta(root, child, depth - 1, alpha, beta, true));
        beta = Math.min(beta, value);

        if (outOfTime())
          return -1;

        if (alpha >= beta)
          break;
      }
    }

    if (state.childOf(root, !computer))
      pairs.add(new findPairs(value, state));

    return value;
  }
  //calculate the user input max time for each move
  private double elapsedSeconds() {
    return ((double) (System.nanoTime() - startTime)) / 1_000_000_000.0;
  }

  private boolean outOfTime() {
    return elapsedSeconds() >= maxSeconds;
  }
}
