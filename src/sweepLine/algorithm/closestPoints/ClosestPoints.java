package sweepLine.algorithm.closestPoints;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import sweepLine.algorithm.areaOfTheUnionOfTwoRectangles.BoxUnion;
import sweepLine.framework.AbstractFrameworkAlgorithm;
import sweepLine.gui.Designer;

//https://baptiste-wicht.com/posts/2010/04/closest-pair-of-point-plane-sweep-algorithm.html

public class ClosestPoints extends AbstractFrameworkAlgorithm<PointEvent,PointEvent> {
	
	private final static Logger log = Logger.getLogger(ClosestPoints.class.getName());
	
	PointEvent[] closestPair = new PointEvent[2];
	double currentMininmalDistance;
	Line shortestPair;
	
	public ClosestPoints(Pane pane, ListView<String> output, ListView<String> treeSetView) {
		// initialize superclass with two comparators of which the first is for the prio queue and the latter for the binary search tree (sweep status structure))
		super(pane,output,treeSetView,new HorizontalComparator(),new VerticalComparator());
		currentMininmalDistance = Double.POSITIVE_INFINITY;
	}
	
	public void computeNext(PointEvent point1) {
			//Delete points which are too far away
			PointEvent temppoint = point1;
			while (temppoint != null){
				if(temppoint.getX() < point1.getX() - currentMininmalDistance){
					removeSssObject(temppoint);
				}
				temppoint = getSssLower(temppoint);
				
			}
						
			//Compute the y head and the y tail of the candidates set
	        PointEvent head = new PointEvent(point1.getX(), (point1.getY() - currentMininmalDistance));
	        PointEvent tail = new PointEvent(point1.getX(), (point1.getY() + currentMininmalDistance));

	        //We take only the interesting candidates in the y axis
	        for (PointEvent point2 : getBetween(head, tail)) {
				double distance = point1.distance(point2);	
	            //Simple min computation
	            if (distance < currentMininmalDistance) {
	            	currentMininmalDistance = distance;
	                closestPair[0] = point2;
	                closestPair[1] = point1;
	                
	                removeShape(shortestPair); 
	                shortestPair = new Line(closestPair[0].getX(), closestPair[0].getY() , closestPair[1].getX(), closestPair[1].getY());
	                addShape(shortestPair);
	                Designer.highlight(shortestPair);
	            }
	        }

	        //The current point is now a candidate
	        addSssObject(point1);
		}

	@Override
	public void end() {
	    setMsg("The two closest Points are:" + " Point 1 " + "x : " + closestPair[0].getCenterX() + " " + "y : " + closestPair[0].getCenterY() + " Point 2: x : " + closestPair[1].getCenterX() + " y : " + closestPair[1].getCenterY());

	}

	@Override
	protected void draw() {
		//doesn't get used
	}
	
	public void addPoint(double x1, double y1) {
		addEvent(new PointEvent(x1,y1));	
	}

	@Override
	protected ArrayList<PointEvent> getInitialEvents() {
		ArrayList<PointEvent> points = new ArrayList<>();
		points.add(new PointEvent(30, 55));
		points.add(new PointEvent(80, 350));
		points.add(new PointEvent(100, 250));
		points.add(new PointEvent(120, 300));
		points.add(new PointEvent(150, 150));
		points.add(new PointEvent(200, 200));
		points.add(new PointEvent(250, 300));
		points.add(new PointEvent(300, 150));
		points.add(new PointEvent(350, 50));
		points.add(new PointEvent(400, 150));
		return points;
	}

	
	@Override
	protected ArrayList<PointEvent> getRandomEvents() {
		this.clear();
		ArrayList<PointEvent> points = new ArrayList<>();
		int count = 8;
		Random rand = new Random();	
		
		for(int i=0; i<count; i++) {
			double e1x = rand.nextInt((int)getPaneWidth());
			double e1y = rand.nextInt((int)getPaneHeight());
			points.add(new PointEvent(e1x,e1y));
		}
		
		return points;
	}

	@Override
	protected void clear() {
		closestPair = new PointEvent[2];
		currentMininmalDistance = Double.POSITIVE_INFINITY;
	}
}
	

