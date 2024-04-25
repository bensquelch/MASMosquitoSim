import java.util.ArrayList;
import java.util.List;

public class Mosquito extends Entity{
	public MosquitoState currentState;
	private boolean gender;
	public float foodLevel;
	private int incubationCounter = 0;
	public int id;
	private Simulate s;
	private double age; //age in frames
	private int lifespan;
	
	private boolean adult = false;
	
	//Amount of time the mosquito hasn't eaten for
	private int starveTime = 0;
	
	//Flag to kill the mosquito
	public boolean kill = false;
	
	public Mosquito(Location l, int gender, int i, Simulate sim) {
		x = l.x;
		y = l.y;
		s = sim;
		id = i;
		
		//Spawn with different food levels
		foodLevel = Constants.random.nextInt(Constants.MAX_FOOD);
		
		//set age and life span
		if(gender == 1) {
			lifespan = Constants.FEMALE_LIFESPAN;
		} else {
			lifespan = Constants.MALE_LIFESPAN;
		}
		
		//Change the gender (0 or 1) into a boolean
		if(gender ==1) {
			this.gender = true;
		} else {
			this.gender = false;
		}
		
		//Set state to either hungry or full at the start
		int randomState = Constants.random.nextInt(1);
		switch(randomState) {
			case 0:
				this.currentState = MosquitoState.HUNGRY;
				break;
			case 1: 
				this.currentState = MosquitoState.FULL;
		}
		
		//7 days until eggs mature to adults
		if(id == 9999) {
			age = -7 * Constants.DAY_LENGTH;
		} else {
			age = 0;
		}
	}
	
    public double calculateDeathChance() {
        double k = 0.2; //control the slope of the curve
        return 1 / (1 + Math.exp(-k * (age - lifespan)));
    }
    
	public void act(Field field) {
		age ++;
		
		//Ignore act method for mosquito eggs
		if(age < 0) {
			return;
		}
		
		//Egg has now matured to an adult
		adult = true;
		
		//Store the previous location of the mosquito 
		Location prevLoc = new Location(x,y);
		
		//Mosquito becomes hungry, takes priority 
		if(foodLevel < 1) {
			currentState = MosquitoState.HUNGRY;
		}
		
		switch(currentState) {
			case HUNGRY:
				starveTime++;
				eatFood(field);
				moveToFood(findFood(field), field);
				break;
			case FULL:
				//Females need to wait an incubation period, males rest until hungry
				if(gender) {
					incubationCounter ++;
				}
				break;
			case ATTEMPTMATE:
				moveToMate(findMate(field),field);
				break;
			case GRAVID:
				moveToWater(findWater(field),field);
				break;
			case FOUNDSITE:
				s.birthMosquito(x,y, field);
				currentState = MosquitoState.HUNGRY;
				break;
		}
		
		//Once the Mosquito has rested
		if(incubationCounter >= Constants.INCUBATION_PERIOD) {
			if(gender) {
				currentState = MosquitoState.ATTEMPTMATE;
			}
			incubationCounter = 0;
		}
		
		//Decrement the food level
		if(foodLevel > 0) {
			foodLevel -= Constants.FOOD_DECREMENT_VAL;
		}
		
		//If the mosquito has starved
		if(starveTime > Constants.STARVE_TIME) {
			kill = true;
		}
		
		//Remove the previous location from the field if the mosquito has moved
		if(prevLoc.compareTo(this.getLocation()) < 1) {
			field.clearLocation(prevLoc);	
		}	
		
		//Place the mosquito in the entity array
        field.place(this, x, y);
	}
	
	
	
	//Search a matrix and return a location of food within matrix
	public Location findFood(Field field) { 
		List<Location> locationList = new ArrayList<>();
		
		// Search in a matrix around the entity's current position
        for (int i = x - 6; i <= x + 6; i++) {
            for (int j = y - 6; j <= y + 6; j++) {

            	// Check if the indices are within the grid bounds
                if (field.inBounds(i,j)) {
                   
                	// Check if there is food at this position
                    if (field.getFood(i, j) >= 1) {
                        locationList.add(new Location(i,j));
                    }
                }
            }
        }
        
        //If a food location has been found
        if(locationList.size() > 0) {
        	int randomLocation = Constants.random.nextInt(locationList.size());
        	return locationList.get(randomLocation);
        }
        return new Location(-1,-1);
	}
	
	private void moveToFood(Location Loc, Field field) {
		//if food has been found
        if (Loc.x != -1 && Loc.y != -1) {
            // Move towards the food
        	if (Loc.x < x) {
        		x--;
            } else if (Loc.x > x) {
                x++;
            }
            if (Loc.y < y) {
                y--;
            } else if (Loc.y > y) {
                y++;
            }
        } else {
        	moveRandom(field);
        }
	}
	
	private void eatFood(Field field) {
		//check if on food
		if(field.getFood(x, y) >=1) {
			
			starveTime = 0;
			field.eatFood(x,y);
			foodLevel += 1;
			
			//Set status to full
			if(foodLevel >= Constants.MAX_FOOD) {
				currentState = MosquitoState.FULL;
			}
			return;
		}
}
	
	private Location findMate(Field field) {
		// Search in a matrix around the entity's current position
        for (int i = x - 15; i <= x + 15; i++) {
            for (int j = y - 15; j <= y + 15; j++) {

            	// Check if the indices are within the grid bounds
                if (i >= 0 && i < field.getWidth() && j >= 0 && j < field.getHeight()) {
                   
                	// Check if there is a mate at this position
                    if (field.getObjectAt(i,j) instanceof Mosquito) {
                    	Mosquito o = (Mosquito) field.getObjectAt(i, j);
                    	  	
                    	//If the mate is a male and adult
                    	if(!o.gender && o.adult) {
                        	return new Location(i,j);
                    	}
                    }
                }
            }
        }
        return new Location(-1,-1);
	}
	
	private void moveToMate(Location mateLoc, Field field) {
		int x_ = mateLoc.getX();
		int y_ = mateLoc.getY();
		
		//If within a 5 pixel grid of a mate become gravid
		if((Math.abs(x-x_) < 5) && (Math.abs(y-y_) < 5)) {
			this.currentState = MosquitoState.GRAVID;
			return;
		}
		
		//if mate has been found
        if (mateLoc.x != -1 && mateLoc.y != -1) {
            // Move towards the food
        	if (mateLoc.x < x) {
        		x--;
            } else if (mateLoc.x > x) {
                x++;
            }
            if (mateLoc.y < y) {
                y--;
            } else if (mateLoc.y > y) {
                y++;
            }
        } else {
        	moveRandom(field);
        }
	}
	
	
	private Location findWater(Field field) {
		// Search in a matrix around the entity's current position
        for (int i = x - 10; i <= x + 10; i++) {
            for (int j = y - 10; j <= y + 10; j++) {
            	if (field.inBounds(i,j)) {
                	if(s.getNoiseValue(i, j) < Constants.WATER_LEVEL) {
                    	currentState = MosquitoState.FOUNDSITE;
                		return new Location(i,j);
                	}
            	}
            }
        }
        return new Location(-1,-1);
	}
	
	private void moveToWater(Location Loc, Field field) {
		//if water has been found
        if (Loc.x != -1 && Loc.y != -1) {
            // Move towards the food
        	if (Loc.x < x) {
        		x--;
            } else if (Loc.x > x) {
                x++;
            }
            if (Loc.y < y) {
                y--;
            } else if (Loc.y > y) {
                y++;
            }
        } else {
        	moveRandom(field);
        }
	}
	
	public void moveRandom(Field field) {
	
		//Generate random number between -1 and 1
		int n = Constants.random.nextInt(3) -1;
		int c = Constants.random.nextInt(3) -1;
	
		if(field.inBounds(x+n, y+c)) {
			x+=n;
			y+=c;
		}	
	}
	
	public boolean getGender() {
		return gender;
	}
	
	
	public Location getLocation() {
		return new Location(x,y);
	}
	
	public boolean isAdult() {
		return adult;
	}
	
	
	
	
	
}
