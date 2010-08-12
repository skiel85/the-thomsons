package vectorization;

public class Point extends java.awt.Point {

	private static final long serialVersionUID = 2430782886536003874L;
	private double x;
    private double y;


    public static Point getPoint(int radio, int angle){
        double angle_in_radians = angle * 2 * Math.PI / 360;
        int internalX = (int) Math.round(radio * Math.cos(angle_in_radians));
        int internalY = (int) Math.round(radio * Math.sin(angle_in_radians));
        return new Point(internalX,internalY);
    }

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString(){
        return "(" + this.getX() + "," + this.getY() + ")";
    }

    public double distanceTo(Point point) {
        return Math.sqrt(Math.pow(this.getX() - point.getX(), 2) + Math.pow(this.getY() - point.getY(), 2));
    }

    public Point substract(Point otherPoint){
        return new Point( this.getX() - otherPoint.getX(), this.getY() - otherPoint.getY());
    }

    public Point divideByScalar(double value){
        return new Point(this.getX() / value,this.getY() / value);
    }

	public int getIntY() {
		return (int)this.y;
	}

	public int getIntX() {
		return (int)this.x;
	}

    
}