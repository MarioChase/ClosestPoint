
public class Point {
	//point class contains x and y coordinates
	private int x;
	private int y;
	
	//constructs the point with 0 arguments
	public Point() {
		x = 0;
		y = 0;
	}
	
	//constructs the point with two integers
	public Point(int inputX, int inputY) {
		x = inputX;
		y = inputY;
	}
	
	//get the x value
	public int getX() {
		return x;
	}
	
	//get the y value
	public int getY() {
		return y;
	}
	
	//check to see given a different point and the finder point which one is closer
	public boolean isCloser(Point close, Point find) {
		if (getDistance(find) < close.getDistance(find)) {
			return true;
		} else {
			return false;
		}
	}
	
	//get distance value to a specific point
	public double getDistance(Point a) {
		double distance = Math.sqrt(((a.getX() - x) * (a.getX() - x)) + (a.getY() - y) * (a.getY() - y));
		return distance;
	}
	
	//prints the string
	public String toString() {
		return x + "," + y;
	}

}
