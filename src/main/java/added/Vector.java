package added;

public class Vector {
    public double x;
    public double y;

    private double mag;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public static Vector fromAngle(double r) {
        return new Vector(Math.cos(r), Math.sin(r));
    }

    public void add(Vector other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void mult(double v) {
        this.x *= v;
        this.y *= v;
    }

    public void limit(double maxSpeed) {
        this.x = Math.min(this.x, maxSpeed);
        this.y = Math.min(this.y, maxSpeed);
    }

    public void setMag(double v) {
        this.mag = v;
    }

    public double mag() {
        return mag;
    }

    public void normalize() {

    }

    public double dist(Vector bulletPos) {
        double xDistance = Math.abs(bulletPos.x - x);
        double yDistance = Math.abs(bulletPos.y - y);

        return Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
    }

    public void rotate(double v) {

    }


    public double dot(Vector towardsPlayer) {
        return 0;
    }
}
