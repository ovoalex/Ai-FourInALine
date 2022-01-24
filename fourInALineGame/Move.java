package fourInALineGame;

//getter and setter for moves
public class Move {
	  private int marker;
	  int row;
	  int col;

	  public Move(int marker, int row, int col) {
	    this.marker = marker;
	    this.row = row;
	    this.col = col;
	  }

	  public String toString() {
	    return (char) (65 + row - 1) + Integer.toString(col);
	  }
	}