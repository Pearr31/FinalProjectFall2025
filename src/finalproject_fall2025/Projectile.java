package finalproject_fall2025;

/**
 *
 * @author massi
 */
public class Projectile {
    // User Input Values 
    private double initialVelocity;   
    private double launchAngle;       
    private double initialHeight;     

    //Output Values
    private double flightTime;        
    private double maxHeight;         
    private double range;             
    private double finalVelocity;     

    private final double g = 9.81;    //Gavitational constant

    public Projectile(double initialVelocity, double launchAngle, double initialHeight) {
        this.initialVelocity = initialVelocity;
        this.launchAngle = Math.toRadians(launchAngle); //Convert to radians for easier calculations
        this.initialHeight = initialHeight;

        computeValues();
    }
    
    private void computeValues() {
        //TODO Impliment projectile motion formulas to get Output values
    }
}
