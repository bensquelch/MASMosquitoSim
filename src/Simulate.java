import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;

public class Simulate extends PApplet{
	private ArrayList<Mosquito> mosquitos = new ArrayList<Mosquito>();
	private Field field;
	private View view;
	private PApplet main;
	
	public int mosquitoCount = 0;
	public int eggCount = 0;
	public int totalCount = 0;
	
	private int oscillatorStep = 0;
	double WATER_MIN_VALUE = Constants.WATER_LEVEL - 0.04;
	double WATER_MAX_VALUE = Constants.WATER_LEVEL + 0.04;
	
	float[][] noiseMap = new float[Constants.WIDTH][Constants.HEIGHT];
	private ArrayList<Mosquito> mosquitosToAdd = new ArrayList<Mosquito>();
		    
	public Simulate(int width, int height, PApplet main) {
		this.main = main;
		field = new Field(Constants.WIDTH,Constants.HEIGHT);
		view = new View(width,height,this,main);
		CSVWriter.createFile();
	}
	
	public void step() {
			//add the new mosquitos 
			for(Mosquito m : mosquitosToAdd) {
				mosquitos.add(m);
				field.place(m, m.getLocation());
			}

			
			Constants.WATER_LEVEL = calculateWaterChange();
			
			mosquitosToAdd.clear();
		
		   //Loop through mosquitos
		   mosquitoCount = 0;
		   eggCount = 0;
		   totalCount = 0;
		   
		   for(Iterator<Mosquito> it = mosquitos.iterator(); it.hasNext(); ) {
			   Mosquito m = it.next();
			  
			   if(m.isAdult()) {
				   mosquitoCount ++;   
			   } else {
				   eggCount ++;
			   }

			   
			   //Handle mosquito death
		       if (Constants.random.nextDouble() < m.calculateDeathChance() || m.kill) {
		           killMosquito(m, it);
		       } else {
		    	   m.act(field);
		       }  
	       }
		   totalCount = eggCount + mosquitoCount;
		   
		  if(main.frameCount % Constants.simSpeed == 0) {
			  main.background(173, 255, 173);
			  view.displayEntities(field, main);
		  }

		  //Control the percentage of frames that will result in a new food being spawned
		  if (Constants.random.nextFloat() < Constants.FOOD_SPAWN_RATE) {
			  field.spawnFood();
		  }
		  
		  if(main.frameCount % 10 == 0) {
			 CSVWriter.print(mosquitoCount, eggCount, totalCount);
		  }

	}
	
	public void populate() {
		int maxX = 300;
		int maxY = 300;
		
		//Generate and add mosquitos to field
		for (int i = 0; i < Constants.mosquitoCount; i ++) {
			int randX= Constants.random.nextInt((maxX));
			int randY = Constants.random.nextInt((maxY));
			
			Location loc = new Location(randX,randY);
			
			Mosquito m = new Mosquito(loc, Constants.random.nextInt(2), i, this);
			mosquitos.add(m);
			field.place(m, loc);	
			mosquitoCount++;
		}	
		field.setRandomFood();
		generateNoiseMap();
	}
	

	
	private void killMosquito(Mosquito m, Iterator<Mosquito> it) {
        field.clearLocation(m.getLocation());
        it.remove();
	}
	
	public void birthMosquito(int x, int y, Field field) {
		for (int i = 0; i < 30; i ++) {
			boolean validLocation = false;
			int newX = x;
			int newY = y;
			int c = 0;
			
			//While no valid location has been found
				while(!validLocation) {
					
					if(c > 5) {
						break;
					}
					
					newX += Constants.random.nextInt(11) - 5;
					newY += Constants.random.nextInt(11) - 5;
					
					if (field.inBounds(newX,newY)){
						validLocation = true;
						Location newLoc = new Location(newX, newY);
						Mosquito m = new Mosquito(newLoc, Constants.random.nextInt(2), 9999, this);
						mosquitosToAdd.add(m);
					} else {
						c++;
					}
				}
		}
	}
	
	private void generateNoiseMap() {
		noiseDetail(8);
		noiseSeed(Constants.NOISESEED);
		
		float xoff = 0;
		float increment = 0.01f;
		
		  for (int x = 0; x < Constants.WIDTH; x++) {
			    xoff += increment;   // Increment xoff 
			    float yoff = 0.0f;   // For every xoff, start yoff at 0
			    for (int y = 0; y < Constants.HEIGHT; y++) {
			      yoff += increment; // Increment yoff
			      
			      // Calculate noise and scale by 255
			      float val = noise(xoff, yoff);

			      
			      // Set each pixel onscreen to a grayscale value
			      noiseMap[x][y] = val;
			    }
		   }		  
		
	}
	
	private double calculateWaterChange() {
	    int PERIOD = Constants.DAY_LENGTH * 365;
	    oscillatorStep++;
	    
	    if (oscillatorStep >= PERIOD) {
	    	oscillatorStep = 0;
        }
	    
        // Calculate the angle for the sine function
        double angle = (double) oscillatorStep / PERIOD * 2 * Math.PI;
        return (Math.sin(angle) +1 ) / 2 * (WATER_MAX_VALUE - WATER_MIN_VALUE) + WATER_MIN_VALUE;
	}
	
	public double getNoiseValue(int x, int y) {
		return noiseMap[x][y];
	}
	

}
