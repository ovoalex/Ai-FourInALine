package fourInALineGame;
import java.util.ArrayList;

public class Board {
  private ArrayList<Move> moves = new ArrayList<>();
  public State state = new State();
  private boolean playerFirst;

  public Board(boolean playerFirst) {
    this.playerFirst = playerFirst;
  }

  public void addMove(Move move) {
    moves.add(move);
  }

  public void print() {
    System.out.println(toString());
  }

  @Override
  public String toString() {
    return sideBySideString(boardString(), moveList(playerFirst));
  }

  private String boardString() {
    StringBuilder result = new StringBuilder();
    result.append("  ");

    for (int i = 1; i <= 8; i++)
      result.append(i + " ");

    result.append("\n");

    int letter = 65; // ascii for A
    for (int i = 0; i < 8; i++) {
      result.append((char) letter + " ");

      for (int j = 0; j < 8; j++) {
        char marker = intToSymbol(state.board[i][j]);
        result.append(marker + " ");
      }

      result.append("\n");
      letter++;
    }

    result.append("\n");

    return result.toString();
  }

  private String moveList(boolean playerFirst) {
    StringBuilder result = new StringBuilder();

    if (playerFirst)
      result.append("Opponent vs. Player");
    else
      result.append("Player vs. Opponent");

    for (int i = 0; i < moves.size(); i++) {
      String moveString = moves.get(i).toString();
      if (i % 2 == 0)
        result.append("\n  " + (i / 2 + 1) + ". " + moveString);
      else
        result.append(" " + moveString);
    }

    return result.toString();
  }

  private static char intToSymbol(int input) {
    char result;
    switch (input) {
      case 0:
        result = '-';
        break;
      case 1:
        result = 'X';
        break;
      case 2:
        result = 'O';
        break;
      default:
        result = '?';
    }

    return result;
  }

  // Modified version of StackOverflow answer:
  // https://stackoverflow.com/a/29322564/1177200
  private String sideBySideString(String s1, String s2) {
    String returnPattern = "\n";
    String between = "   ";

    // split the data into individual parts
    String[] s1a = s1.split(returnPattern);
    String[] s2a = s2.split(returnPattern);

    // find out the longest String in data set one
    int longestString = 0;
    for (String s : s1a) {
      if (longestString < s.length())
        longestString = s.length();
    }

    // loop through parts and build new string
    StringBuilder b = new StringBuilder();
    int largerLength = Math.max(s1a.length, s2a.length);
    for (int i = 0; i < largerLength; i++) {
      if (i < s1a.length) // string 1
        b.append(s1a[i]);

      if (i < s2a.length) { // string 2 with padding
        if (i >= s1a.length)
          b.append("                  ");
        b.append(between).append(s2a[i]);
      }

      b.append(returnPattern); // newline
    }

    return b.toString();
  }
}
