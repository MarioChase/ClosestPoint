
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
		if (args.length == 4) {
			obj.runMaster(args);
		} else {
			obj.runWorker();
		}

	}

	private void runMaster(String args[]) throws Exception {
		Point find = new Point(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		int numOfPoints = Integer.parseInt(args[2]);
		int grouping = Integer.parseInt(args[3]);
		List<Process> list = new ArrayList<Process>();
		List<Point> points = generatePoints(numOfPoints);
		int numOfGroups = numOfPoints / grouping;

		for (int i = 0; i < numOfGroups; i++) {
			Process p = new ProcessBuilder("java", "ClosestPoint").start();
			list.add(p);
			List<Point> grouped = getGrouping(i, grouping, points);
			sendParameters(p, find, grouped);
		}

		for (Process p : list) {
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
			String line = br.readLine();
			while (line != null) {
				System.out.println(line);
				line = br.readLine();
			}
		}

	}

	private void runWorker() throws Exception {
		System.out.println("worker initiated");
		Scanner scanner = new Scanner(System.in);
		ArrayList<Point> localGroup = new ArrayList<Point>();
		Point find = new Point(scanner.nextInt(), scanner.nextInt());
		while (scanner.hasNext() == true) {
			localGroup.add(new Point(scanner.nextInt(), scanner.nextInt()));
		}
		Point closest = localGroup.get(0);
		for (Point p : localGroup) {
			System.out.println(p);
			System.out.println("distance: " + p.getDistance(find));
			if (isCloser(closest, p, find)) {
				closest = p;
			}
		}
		System.out.println("is closest: " + closest);
	}

	private List<Point> getGrouping(int group, int interval, List<Point> points) {
		List<Point> grouping = new ArrayList<Point>();
		int start = group * interval;
		int end = start + interval;
		for (int i = start; i < end; i++) {
			grouping.add(points.get(i));
		}

		return grouping;
	}

	private ArrayList<Point> generatePoints(int numOfPoints) {
		ArrayList<Point> randomPoints = new ArrayList<Point>();
		Random rand = new Random();
		for (int i = 0; i < numOfPoints; i++) {
			Point a = new Point(rand.nextInt(100), rand.nextInt(100));
			randomPoints.add(a);
		}
		return randomPoints;
	}

	private boolean isCloser(Point close, Point test, Point find) {
		if (test.getDistance(find) < close.getDistance(find)) {
			return true;
		} else {
			return false;
		}
	}

	private void sendParameters(Process p, Point point, List<Point> points) throws Exception {
		System.out.println("Sending paramaters");
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
