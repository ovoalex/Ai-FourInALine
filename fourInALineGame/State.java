package fourInALineGame;

import java.util.ArrayList;
import java.util.Arrays;

public class State {
  int[][] board;

  //board 8x8
  public State() {
    board = new int[8][8];
  }

  private State(int[][] board) {
    this.board = board;
  }

  public State(State copyState) {
    this.board = Arrays.stream(copyState.board)
            .map(r -> r.clone())
            .toArray(int[][]::new);
  }

  public int computerUtility() {
    return (utility(true) - utility(false));
  }

  //check for computer next move
  //up,down,left,right
  private int utility(boolean computer) {
    int points = 0;

    int marker;
    if (computer)
      marker = 1;
    else
      marker = 2;

    // Check the win
    if (win(computer))
      points += 99999;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        int current = board[row][col];

        if (current == 0) {
          // Check upwards
          int numEmpty = 0;
          int numOwn = 0;
          int numOpponent = 0;
          boolean blocked = false;

          for (int i = 1; i < 4; i++) { // Get next 3 spaces in direction
            if (row - i >= 0) { // check in bounds
              int next = board[row - i][col];
              if (next == 0) { // no marker
                numEmpty++;
              } else if (next != marker) { // opponent marker
                numOpponent++;
                blocked = true;
              } else { // own marker
                numOwn++;
              }
            }
          }

          points += calcPoints(blocked, numEmpty, numOwn, numOpponent);

          // Check downwards
          numEmpty = 0;
          numOwn = 0;
          numOpponent = 0;
          blocked = false;

          for (int i = 1; i < 4; i++) { // Get next 3 spaces in direction
            if (row + i < 8) { // In bounds?
              int next = board[row + i][col];
              if (next == 0) { // no marker
                numEmpty++;
              } else if (next != marker) { // opponent marker
                numOpponent++;
                blocked = true;
              } else { // own marker
                numOwn++;
              }
            }
          }

          points += calcPoints(blocked, numEmpty, numOwn, numOpponent);

          // Check leftwards
          numEmpty = 0;
          numOwn = 0;
          numOpponent = 0;
          blocked = false;

          for (int i = 1; i < 4; i++) { // Get next 3 spaces in direction
            if (col - i >= 0) { // In bounds
              int next = board[row][col - i];
              if (next == 0) { // no marker
                numEmpty++;
              } else if (next != marker) { // opponent marker
                numOpponent++;
                blocked = true;
              } else { // own marker
                numOwn++;
              }
            }
          }

          points += calcPoints(blocked, numEmpty, numOwn, numOpponent);

          // Check rightwards
          numEmpty = 0;
          numOwn = 0;
          numOpponent = 0;
          blocked = false;

          for (int i = 1; i < 4; i++) { // Get next 3 spaces in direction
            if (col + i < 8) { // In bounds?
              int next = board[row][col + i];
              if (next == 0) { // no marker
                numEmpty++;
              } else if (next != marker) { // opponent marker
                numOpponent++;
                blocked = true;
              } else { // own marker
                numOwn++;
              }
            }
          }

          points += calcPoints(blocked, numEmpty, numOwn, numOpponent);
        }
      }
    }

    return points;
  }

  private static int calcPoints(boolean blocked, int numEmpty, int numOwn, int numOpponent) {
    if (numOwn == 3) {
      return 1000000;
    } else if (numOwn == 2) {
      if (numOpponent == 0)
        return 1000;
      else
        return 50;
    } else if (numOwn == 1) {
      if (numOpponent == 0)
        return 250;
      else
        return 25;
    }

    if (numOpponent == 3) {
      return 50000;
    } else if (numOpponent == 2) {
      return 2000;
    } else if (numOpponent == 1) {
      return 50;
    }

    return 25; // all empty
  }

  public boolean win() {
    return win(true) || win(false);
  }

  public boolean win(boolean computer) {
    boolean win = false;

    int marker;
    if (computer)
      marker = 1;
    else
      marker = 2;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        int current = board[row][col];

        if (current == marker) {
          // Vertical win
          if (row <= 8 - 4 &&
                  board[row + 1][col] == marker &&
                  board[row + 2][col] == marker &&
                  board[row + 3][col] == marker) {
            win = true;
          }

          // Horizontal win
          if (col <= 8 - 4 &&
                  board[row][col + 1] == marker &&
                  board[row][col + 2] == marker &&
                  board[row][col + 3] == marker) {
            win = true;
          }
        }
      }
    }

    return win;
  }

  public Move getMove(State nextState) {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        int current = board[row][col];

        if (current == 0 && nextState.board[row][col] == 1)
          return new Move(1, row + 1, col + 1);
      }
    }

    return null;
  }

  public boolean isEmpty() {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        if (board[row][col] != 0)
          return false;
      }
    }

    return true;
  }

  public ArrayList<State> successors(boolean computer) {
    ArrayList<State> results = new ArrayList<>();

    int marker;
    if (computer)
      marker = 1;
    else
      marker = 2;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        int current = board[row][col];

        if (current == 0) {
          int[][] newBoard = Arrays.stream(board)
                  .map(r -> r.clone())
                  .toArray(int[][]::new);

          newBoard[row][col] = marker;
          results.add(new State(newBoard));
        }
      }
    }

    return results;
  }

  public boolean childOf(State state, boolean computer) {
    int differences = 0;

    int marker;
    if (computer)
      marker = 1;
    else
      marker = 2;

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        if (state.board[row][col] != board[row][col]) {
          if (state.board[row][col] == 0 && board[row][col] == marker) {
            differences += 1;
          } else {
            return false;
          }
        }
      }
    }

    if (differences == 1)
      return true;
    else
      return false;
  }
}
