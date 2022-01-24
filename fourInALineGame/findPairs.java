package fourInALineGame;

//getter and setter for pairs
public class findPairs {
	  int value;
	  State state;

	  public findPairs(int value, State state) {
	    this.value = value;
	    this.state = state;
	  }

	  public findPairs(findPairs copyPair) {
	    this.value = copyPair.value;
	    this.state = new State(copyPair.state);
	  }
	}