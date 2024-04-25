import java.util.Random;

public class Constants {
	public static int mosquitoCount = 300;
	public static int SEED = 776;
    public static int NOISESEED = 4;
    
    public static int DAY_LENGTH = 125;
    public static int simSpeed = 1;
    
    public static boolean showFood = true;
    
    public static double FOOD_SPAWN_RATE = 1;
    public static double FOOD_DECREMENT_VAL = 0.025;
    public static int MAX_FOOD = 20;
    public static int FOOD_SPAWN_AMOUNT = 20;
    
    public static int STARVE_TIME = DAY_LENGTH * 4;
    
    public static int INCUBATION_PERIOD = DAY_LENGTH * 3;
	public static int FEMALE_LIFESPAN = DAY_LENGTH * 30;
	public static int MALE_LIFESPAN = DAY_LENGTH * 10;
	public static int TIME_TO_MATURITY = DAY_LENGTH * 2;
    
    public static int WIDTH =800;
    public static int HEIGHT = 600;
    
    public static double WATER_LEVEL = 0.325;    
    
    public static Random random=null;
    public static void setRandom()
    {
    	random = new Random(SEED);
    }

}
