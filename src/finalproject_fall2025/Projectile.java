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

    //despacito
    private void computeValues() {
        if (initialVelocity <= 0 || launchAngle < 0 || initialHeight < 0) {
            System.out.println("Simulation unable to run. Invalid Input");
        }

        double v0x = initialVelocity * Math.cos(launchAngle);
        double v0y = initialVelocity * Math.sin(launchAngle);

        // Solve: y(t) = h0 + v0y t - 1/2 g t^2 = 0
        double a = -0.5 * g;
        double b = v0y;
        double c = initialHeight;

        double discriminant = (b * b) - (4 * a * c);
        flightTime = (-b - Math.sqrt(discriminant)) / (2 * a);  // positive root

        // h_max = h0 + (v0y^2)/(2g)
        maxHeight = initialHeight + (v0y * v0y) / (2 * g);

        // range = v0x * flightTime
        range = v0x * flightTime;

        // vertical velocity at impact
        double vy_final = v0y - (g * flightTime);

        // final speed vector magnitude
        finalVelocity = Math.sqrt((v0x * v0x) + (vy_final * vy_final));
    }

    public void projectileLaunch(double maxHieght, double range, double launchAngle, double initialHeight, double flightTime) {
       
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(double initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public double getLaunchAngle() {
        return launchAngle;
    }

    public void setLaunchAngle(double launchAngle) {
        this.launchAngle = launchAngle;
    }

    public double getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(double initialHeight) {
        this.initialHeight = initialHeight;
    }

    public double getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(double flightTime) {
        this.flightTime = flightTime;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getFinalVelocity() {
        return finalVelocity;
    }

    public void setFinalVelocity(double finalVelocity) {
        this.finalVelocity = finalVelocity;
    }

}
