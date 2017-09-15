package Maze;

/** Questa classe definisce le costanti utilizzate per le quattro
  * direzioni.
  */

public enum Direction { 
	NORTH(0), EAST(1), SOUTH(2), WEST(3);

    public static Direction fromInt(int i) {
    	if (i == NORTH.toInt()) return NORTH;
    	else if (i == EAST.toInt()) return EAST;
    	else if (i == SOUTH.toInt()) return SOUTH;
    	else if (i == WEST.toInt()) return WEST;
    	else return null;
    }
    
    private final int value;
	
    private Direction(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }
        
    public Direction left() {
    	int newValue = (toInt() + 3) % 4;
    	return fromInt(newValue);
    }
    
    public Direction right() {
    	int newValue = (toInt() + 1) % 4;
    	return fromInt(newValue);
    }
    
    public Direction behind() {
    	int newValue = (toInt() + 2) % 4;
    	return fromInt(newValue);
    }
    
}
