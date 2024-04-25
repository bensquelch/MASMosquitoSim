public class Field{
	
	private Entity[][] field;
	private int[][] food;
	private int height, width;

	public Field(int width, int height) {
		this.height = height;
		this.width = width;
		
		//Stores entities on the field
		field = new Entity[width][height];
		food = new int[width][height];
	}
	
	public void place(Entity e, int x, int y) {
		place(e, new Location(x,y));
	}
	
	public void place(Entity e, Location location) {
		field[location.getX()][location.getY()] = e;
	}
	
	public void clearLocation(Location location)
	{
		field[location.getX()][location.getY()] = null;
	}
	
	public Entity getObjectAt(Location location)
	{
		return getObjectAt(location.getX(), location.getY());
	}
	
	public void setRandomFood() {
		for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double probability = Constants.random.nextDouble(); // Generate a random probability between 0 and 1

                if (probability < 0.99) {
                    food[x][y] = 0;
                } else {
                   //food[x][y] = Constants.FOOD_SPAWN_AMOUNT;
                    //food[x][y] = 1;
                }
            }
        }
	}
		
	public int getFood(int x, int y) {	
		if(x > 0 && y > 0 && x < width && y < height) {
			return food[x][y];
		}
		return 0;
	}
	
	public void eatFood(int x, int y) {
		food[x][y] -=1;
	}
	
	public Entity getObjectAt(int x, int y)
	{
		return field[x][y];
	}
	
	public boolean inBounds(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
        	return true;
        }
        return false;
	}
	
	//Spawn a set amount of food on the field
	public void spawnFood() {
		for( int i = 0; i < Constants.FOOD_SPAWN_RATE; i ++) {
		int x = Constants.random.nextInt(Constants.WIDTH);
		int y = Constants.random.nextInt(Constants.HEIGHT);
		
		food[x][y] = Constants.FOOD_SPAWN_AMOUNT;
		}
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

}
