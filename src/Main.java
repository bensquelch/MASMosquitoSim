import processing.core.PApplet;
import processing.core.PFont;

import java.lang.reflect.Method;

import controlP5.*;

public class Main extends PApplet {
	ControlP5 cp5;
	String mosquitoCount, url2;
	boolean startSim = false;
	public Simulate s;
	private int dayOffset = 0;
	
	//Main menu font
	PFont font;
	//Main simulation font
	PFont font2;
	
	//Input fields
	Textfield t1;
	Textfield t2;
	Textfield t3;
	Textfield t4;
	Textfield t5;
	Textfield t6;

	Button submit;
	Button food;
	
	public static void main(String[] args) {
	    PApplet.main("Main");
	}
	
	public void settings() {
		size(600, 400);
	}
	
	public void setup() {
		surface.setTitle("Mosquito Simulation");
		surface.setLocation(500, 100);
		font = createFont("Arial", 18); // Adjust the size as needed
		font2 = createFont("Arial", 13); // Adjust the size as needed
		cp5 = new ControlP5(this);
		// Set the larger font for all ControlP5 elements
		cp5.setFont(font);
		
		//Mosquito Count
		t1 = cp5.addTextfield("mcount")
		  .setPosition(200, 10)
		  .setSize(200, 40)
		  .setAutoClear(false)
		  .setColorBackground(color(173, 255, 173))
		  .setColorForeground(color(0,25,200))
		  .setColorActive(color(255,25,200))
		  .setText(" " + String.valueOf(Constants.mosquitoCount))
		  .setFont(font);
		Label mosquitoCount = t1.getCaptionLabel();
		mosquitoCount.toUpperCase(false);
		// in order to update the upperCase state, you need to invoke setText()
		mosquitoCount.setText("Mosquito Count:"); 
		mosquitoCount.align(ControlP5.LEFT_OUTSIDE, CENTER);
		mosquitoCount._myPaddingX = 10;
		
		//Random Seed
		t2 = cp5.addTextfield("rseed")
		  .setPosition(200, 60)
		  .setSize(200, 40)
		  .setAutoClear(false)
		  .setColorBackground(color(173, 255, 173))
		  .setColorForeground(color(0,25,200))
		  .setColorActive(color(255,25,200))
		  .setText(" " + String.valueOf(Constants.SEED))
		  .setFont(font);
		Label seed = t2.getCaptionLabel();
		seed.toUpperCase(false);
		// in order to update the upperCase state, you need to invoke setText()
		seed.setText("Random Seed:"); 
		seed.align(ControlP5.LEFT_OUTSIDE, CENTER);
		seed._myPaddingX = 10;
		
		//Noise Seed
		t3 = cp5.addTextfield("nseed")
		  .setPosition(200, 110)
		  .setSize(200, 40)
		  .setAutoClear(false)
		  .setColorBackground(color(173, 255, 173))
		  .setColorForeground(color(0,25,200))
		  .setColorActive(color(255,25,200))
		  .setText(" " + String.valueOf(Constants.NOISESEED))
		  .setFont(font);
		Label nseed = t3.getCaptionLabel();
		nseed.toUpperCase(false);
		// in order to update the upperCase state, you need to invoke setText()
		nseed.setText("Noise Seed:"); 
		nseed.align(ControlP5.LEFT_OUTSIDE, CENTER);
		nseed._myPaddingX = 10;
		
		//Water Level
		t4 = cp5.addTextfield("wlevel")
		  .setPosition(200, 160)
		  .setSize(200, 40)
		  .setAutoClear(false)
		  .setColorBackground(color(173, 255, 173))
		  .setColorForeground(color(0,25,200))
		  .setColorActive(color(255,25,200))
		  .setText(" " + String.valueOf(Constants.WATER_LEVEL))
		  .setFont(font);
		Label wlevel = t4.getCaptionLabel();
		wlevel.toUpperCase(false);
		// in order to update the upperCase state, you need to invoke setText()
		wlevel.setText("Water Level:"); 
		wlevel.align(ControlP5.LEFT_OUTSIDE, CENTER);
		wlevel._myPaddingX = 10;
		
		//Width
		t5 = cp5.addTextfield("width")
		  .setPosition(200, 210)
		  .setSize(200, 40)
		  .setAutoClear(false)
		  .setColorBackground(color(173, 255, 173))
		  .setColorForeground(color(0,25,200))
		  .setColorActive(color(255,25,200))
		  .setText(" " + String.valueOf(Constants.WIDTH))
		  .setFont(font);
		Label width = t5.getCaptionLabel();
		width.toUpperCase(false);
		// in order to update the upperCase state, you need to invoke setText()
		width.setText("Width:"); 
		width.align(ControlP5.LEFT_OUTSIDE, CENTER);
		width._myPaddingX = 10;
		
		//Height
		t6 = cp5.addTextfield("height")
		  .setPosition(200, 260)
		  .setSize(200, 40)
		  .setAutoClear(false)
		  .setColorBackground(color(173, 255, 173))
		  .setColorForeground(color(0,25,200))
		  .setColorActive(color(255,25,200))
		  .setText(" " + String.valueOf(Constants.HEIGHT))
		  .setFont(font);
		Label height = t6.getCaptionLabel();
		height.toUpperCase(false);
		// in order to update the upperCase state, you need to invoke setText()
		height.setText("Height:"); 
		height.align(ControlP5.LEFT_OUTSIDE, CENTER);
		height._myPaddingX = 10;
		
		
		//Submit button
		submit = cp5.addButton("Submit").setPosition(10, 350)
		 .setSize(80, 40)
		 .setColorActive(color(255,25,200))
		 .setColorBackground(color(0,25,200))
		 .setColorForeground(color(255,25,200)); 
		
		
		Constants.setRandom();
		frameRate(600);
		stroke(155);
		background(173, 255, 173);
	}
	
	public void mouseWheel() {
		Constants.showFood = !Constants.showFood;
	}
	
	public void mousePressed() {
		  if (mouseButton == LEFT) {
			    Constants.simSpeed ++;
			  } else if (mouseButton == RIGHT) {
			    // Right mouse button was pressed
				if(Constants.simSpeed >1) {
					  Constants.simSpeed --;
			  }				  
		  }
	}

	public void draw() {
		textFont(font2);
		
		if(startSim) {
			s.step();
			if(frameCount % Constants.simSpeed ==0) {
				text(" Day: " + (frameCount - dayOffset) / Constants.DAY_LENGTH,0,13);
				text(" Mosquito Count: " + s.mosquitoCount,0,27);
				text(" Egg Count: " + s.eggCount,0,41);
				text(" Total Count: " + s.totalCount,0,55);
				text(" Sim Speed: " + Constants.simSpeed,0,71);
			}

		}
	}
	
	public void keyPressed() {
		  if (key==ESC) {
		    CSVWriter.close();
		  }
	}
	
	public void foodPressed() {
		food.hide();
	}
	
	public void Submit() {
		
		Constants.mosquitoCount = Integer.valueOf(t1.getText().replaceAll("\\s", ""));
		Constants.SEED = Integer.valueOf(t2.getText().replaceAll("\\s", ""));
		Constants.NOISESEED = Integer.valueOf(t3.getText().replaceAll("\\s", ""));
		Constants.WATER_LEVEL = Double.valueOf(t4.getText().replaceAll("\\s", ""));
		Constants.WIDTH = Integer.valueOf(t5.getText().replaceAll("\\s", ""));
		Constants.HEIGHT = Integer.valueOf(t6.getText().replaceAll("\\s", ""));
		
		t1.hide();
		t2.hide();
		t3.hide();
		t4.hide();
		t5.hide();		
		t6.hide();
		submit.hide();
		
		//Unlock resize and change to entered values
		surface.setResizable(true);
		surface.setSize(Constants.WIDTH, Constants.HEIGHT);
		surface.setLocation(-8,0);
		surface.setResizable(false);
		
		//Generate the simulate class
		s = new Simulate(Constants.WIDTH,Constants.HEIGHT,this);
		s.populate();
		startSim= true;

		dayOffset = frameCount;
	}
}

