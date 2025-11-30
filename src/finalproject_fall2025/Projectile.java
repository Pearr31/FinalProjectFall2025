package finalproject_fall2025;

import javafx.scene.shape.Circle;

/**
 *
 * @author massi
 */
public class Projectile {

    // User Input Values 
    private double initialVelocity;
    private double launchAngle;
    private double initialHeight;
    private Circle circle;

    private double v0x;
    private double v0y;

    //Output Values
    private double flightTime;
    private double maxHeight;
    private double range;
    private double finalVelocity;

    //Gavitational constant
    private final double g = 9.81;

    /**
     * Creates a new Projectile instance with the specified initial velocity,
     * launch angle, and height.
     * <p>
     * Converts the launch angle to radians and automatically computes all
     * derived values.
     * </p>
     *
     * @param initialVelocity Initial speed in meters per second (must be ≥ 0)
     * @param launchAngle Launch angle in degrees (0° ≤ angle ≤ 90°)
     * @param initialHeight Initial height in meters (≥ 0)
     */
    public Projectile(double initialVelocity, double launchAngle, double initialHeight) {
        this.initialVelocity = initialVelocity;
        this.launchAngle = Math.toRadians(launchAngle); //Convert to radians for easier calculations
        this.initialHeight = initialHeight;

        computeValues();
    }

    /**
     * Computes the horizontal and vertical velocity components, flight time,
     * maximum height, range, and final velocity.
     * <p>
     * Uses standard projectile motion equations:
     * <ul>
     * Horizontal velocity: v0x = v0 * cos(θ) Vertical velocity: v0y = v0 *
     * sin(θ) Flight time: solved from y(t) = h0 + v0y * t - 0.5 * g * t² = 0
     * Maximum height: h_max = h0 + (v0y²) / (2g) Range: x = v0x * flightTime
     * Final velocity: magnitude of (v0x, vy_final)
     * </ul>
     * </p>
     */
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

    public void projectileLaunch() {
        //TODO
    }

    /**
     * Returns the horizontal position of the projectile at time t.
     *
     * @param t Time in seconds since launch
     * @return Horizontal position in meters
     */
    public double getX(double t) {
        return v0x * t;
    }

    /**
     * Returns the vertical position of the projectile at time t.
     *
     * @param t Time in seconds since launch
     * @return Vertical position in meters
     */
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
