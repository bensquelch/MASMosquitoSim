
public class Location implements Comparable<Location> {
	int x;
	int y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
    public int compareTo(Location other) {
        if(this.x == other.x && this.y == other.y) {
        	return 1;
        }
        return 0;
    }
}
