
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ClosestPoint {
	public static void main(String args[]) throws Exception {
		ClosestPoint obj = new ClosestPoint();
		//wanted to go with an SPMD program
		//checks if the arguments sent is greater than 5
		if (args.length == 5) {
			obj.runMaster(args);
			//initializes worker
		} else {
			obj.runWorker();
		}

	}

	private void runMaster(String args[]) throws Exception {
		//splits the arguments into the proper places and created a point class to make things easier to manage
		Point find = new Point(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		int numOfPoints = Integer.parseInt(args[2]);
		int grouping = Integer.parseInt(args[3]);
		int range = Integer.parseInt(args[4]);
		List<Process> list = new ArrayList<Process>();
		List<Point> points = generatePoints(numOfPoints, range);
		int numOfGroups = numOfPoints / grouping;
		
		//starts and sends the requisite parameters for the worker class
		for (int i = 0; i < numOfGroups; i++) {
			Process p = new ProcessBuilder("java", "ClosestPoint").start();
			list.add(p);
			List<Point> grouped = getGrouping(i, grouping, points);
			sendParameters(p, find, grouped);
		}
		List<Point> ClosestPoints = new ArrayList<Point>();
		
		//Receives the output from the processes
		for (Process p : list) {
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
			String line = br.readLine();
			while (line != null) {
				ClosestPoints.add(parseToPoint(line));
				line = br.readLine();
			}
		}
		//begins checking for lowest 
		Point closest = ClosestPoints.get(0);
		System.out.println("----Closest Points Found----");
		for (Point q : ClosestPoints) {
			System.out.println(q);
			if (q.isCloser(closest, find))
				closest = q;

		}
		System.out.println("The closest point is: " + closest);

	}
	//translates the string value of a point to a proper point object
	private Point parseToPoint(String input) {
		String numbers[] = input.split(",");
		Point output = new Point(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
		return output;
	}
	//runs the worker
	private void runWorker() throws Exception {
		Scanner scanner = new Scanner(System.in);
		ArrayList<Point> localGroup = new ArrayList<Point>();
		Point find = new Point(scanner.nextInt(), scanner.nextInt());
		//scanner that adds the points
		while (scanner.hasNext() == true) {
			localGroup.add(new Point(scanner.nextInt(), scanner.nextInt()));
		}
		Point closest = localGroup.get(0);
		for (Point p : localGroup) {
			if (p.isCloser(closest, find)) {
				closest = p;
			}
		}
		System.out.println(closest);
	}
	//groups points for distributing to programs
	private List<Point> getGrouping(int group, int interval, List<Point> points) {
		List<Point> grouping = new ArrayList<Point>();
		int start = group * interval;
		int end = start + interval;
		for (int i = start; i < end; i++) {
			grouping.add(points.get(i));
		}

		return grouping;
	}
	//generates random points based off of the range given in the argument
	private ArrayList<Point> generatePoints(int numOfPoints, int range) {
		ArrayList<Point> randomPoints = new ArrayList<Point>();
		Random rand = new Random();
		for (int i = 0; i < numOfPoints; i++) {
			Point a = new Point(rand.nextInt(range), rand.nextInt(range));
			System.out.println(a);
			randomPoints.add(a);
		}
		return randomPoints;
	}
	//sends the parameters to the worker process getting the find point from the first point object
	private void sendParameters(Process p, Point point, List<Point> points) throws Exception {
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(p.getOutputStream(), "UTF-8"));
		pw.println(point.getX());
		pw.println(point.getY());
		for (Point q : points) {
			pw.println(q.getX());
			pw.println(q.getY());
		}
		pw.flush();
		pw.close();
	}
}
