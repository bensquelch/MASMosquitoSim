import java.util.ArrayList;
import java.util.Iterator;
import processing.core.PApplet;
public class View {
    private Simulate s;
	public View(int height, int width, Simulate s, PApplet main) {
		this.s = s;
	}
	
	//Loop through field and display objects
	public void displayEntities(Field field, PApplet main) {
        
		//Loop through field
		for(int x = 0; x < field.getWidth(); x++) {
            for(int y = 0; y < field.getHeight(); y++) {
            	Object o = field.getObjectAt(x, y);
                
            	//Get the water value
                if(s.getNoiseValue(x,y) < Constants.WATER_LEVEL) {
                	//For the deeper colour water
                	if(s.getNoiseValue(x,y) < Constants.WATER_LEVEL - 0.03) {
                		main.stroke(153, 180, 255);
                		main.point(x,y);
                	}
                	main.stroke(173, 200, 255);
            		main.point(x,y);
            	}
            	
                //Draw mosquitos while differentiating their gender
            	if(o instanceof Mosquito) {
            		Mosquito m = (Mosquito) o;
            		
            		if(m.getGender()) {
            			//female
            			main.stroke(255,25,200);
                        main.ellipse(x,y,4,4);
            		} else {
                		//male
            			main.stroke(0,25,200);
                		main.ellipse(x,y,4,4);
            		}

            	} else if (field.getFood(x, y) != 0) {
            		if(Constants.showFood) {
                		main.stroke(244,154,194);
                		main.ellipse(x, y,2,2);
            		}
            	}
            	

            }
        }
	}
	
	public void displayMosquitos(Field field, PApplet main, ArrayList<Mosquito> mosquitos) {
		   for(Iterator<Mosquito> it = mosquitos.iterator(); it.hasNext(); ) {
			   Mosquito m = it.next();
			   
   			main.stroke(255,25,200);
    		main.ellipse(m.x,m.y,4,4);
		   }
	}

}
