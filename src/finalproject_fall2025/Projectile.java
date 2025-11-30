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

    private double v0x;
    private double v0y;

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
            return;
        }

        // store into fields, not new local variables
        this.v0x = initialVelocity * Math.cos(launchAngle);
        this.v0y = initialVelocity * Math.sin(launchAngle);

        // Solve: y(t) = h0 + v0y t - 1/2 g t^2 = 0
        double a = -0.5 * g;
        double b = v0y;
        double c = initialHeight;

        double discriminant = (b * b) - (4 * a * c);
        if (discriminant < 0) {
            System.out.println("Simulation unable to run. No real flight time.");
            return;
        }

        // larger positive root: time when it hits the ground
        flightTime = (-b - Math.sqrt(discriminant)) / (2 * a);  // (b + sqrt(D)) / g

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
        //TODO
    }

    public double getX(double t) {
        return v0x * t;
    }

    public double getY(double t) {
        return initialHeight + v0y * t - 0.5 * g * t * t;
    }

    public double getV0x() {
        return v0x;
    }

    public double getV0y() {
        return v0y;
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
