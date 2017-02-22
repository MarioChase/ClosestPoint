
public class Point {
	private int x;
	private int y;
	
	public Point(){
		x = 0;
		y = 0;
	}
	
	public Point(int inputX, int inputY) {
		x = inputX;
		y = inputY;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public double getDistance(Point a){
		double distance = 0;
		return distance;
	}
	
	public String toString(){
		return "{" + x + "," + y + "}";
	}

}
