package sweepLine.algorithm.areaOfTheUnionOfTwoRectangles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Logger;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import sweepLine.framework.AbstractFrameworkAlgorithm;
import sweepLine.gui.Designer;

public class BoxUnion extends AbstractFrameworkAlgorithm<BoxUnionEvent, BoxUnionEvent> {

	//logger
	private final static Logger log = Logger.getLogger(BoxUnion.class.getName());

	//variables of the class
	private double area;
	BoxUnionEvent event,event2;
	double counter,speedcounter1, speedcounter2;
	ArrayList<BoxRectangle> rects = new ArrayList<>();
	ArrayList<BoxRectangle> rectsbf = new ArrayList<>();
	
	public BoxUnion(Pane pane, ListView<String> output, ListView<String> treeSetView) {
	// initialize superclass with two comparators of which the first is for the prio queue and the latter for the binary search tree (sweep status structure))
		super(
			pane,
			output,
			treeSetView,
				//Comparator for the Event Queue, sorts for x
				new Comparator<BoxUnionEvent>() {
					@Override
					public int compare(BoxUnionEvent a, BoxUnionEvent b) {
				        if (a.getX() < b.getX()) {
				            return -1;
				        }
				        if (a.getX() > b.getX()) {
				            return 1;
				        }
				        if (a.getY() < b.getY()) {
				            return -1;
				        }
				        if (a.getY() > b.getY()) {
				            return 1;
				        }
				        return 0;
				    }
				},
				//Comparator for the Tree, sorts for x
				new Comparator<BoxUnionEvent>() {
					@Override
					public int compare(BoxUnionEvent a, BoxUnionEvent b) {
				        if (a.getX() < b.getX()) {
				            return -1;
				        }
				        if (a.getX() > b.getX()) {
				            return 1;
				        }
				        if (a.getY() < b.getY()) {
				            return -1;
				        }
				        if (a.getY() > b.getY()) {
				            return 1;
				        }
				        return 0;
				    }
				}
			);
	}

	public void addRectangle(BoxRectangle r) {
		addEvent(r.getE1());
		addEvent(r.getE2());
		addShape(r);
		//draw rectangle and save rectangle
		
		log.info("New rectangle: " + r.getE1().getX() + "," + r.getE1().getY()  + "," + r.getE2().getX()  + "," + r.getE2().getY() );
	}
	
	public void addRectangle(double x1, double y1, double x2, double y2) {
		addRectangle(new BoxRectangle(x1,y1,x2,y2));	
	}
	
	public double bruteforce(){
		int i = 0;
		int j = 0;
		BoxRectangle rect1bf,rect2bf;
		double areabf = 0;
		System.out.println(rectsbf.size());
		while(i <= rectsbf.size()){
			rect1bf = rectsbf.get(i);
			j = 0;
			while(j <= rectsbf.size()){
				rect2bf = rectsbf.get(j);
				if(rect1bf.getE2().getX() >= rect2bf.getE1().getX() && rect1bf.getE2().getY() >= rect2bf.getE1().getY() & rect1bf != rect2bf){
					//Bestimme alle Punkte vom dem Schnittpunktdreieck.
					double upper = Math.min(rect1bf.getE2().getY(), rect2bf.getE2().getY());
					double rigth = Math.min(rect1bf.getE2().getX(), rect2bf.getE2().getX());
					double lower = Math.max(rect1bf.getE1().getY(), rect2bf.getE1().getY());
					double left = Math.max(rect1bf.getE1().getX(), rect2bf.getE1().getX());
					areabf += (rigth - left) * (upper - lower);
				}
				j =+ 1;
				speedcounter2 =+ 1;
			}
			System.out.println(j);
			System.out.println(i);
			i =+ 1;
		}
		System.out.println(speedcounter2);
		return areabf;		
	}
	@Override
	public void computeNext(BoxUnionEvent event) {
		switch (event.type) {
		case LOWERLEFT:
			// Merk dir wie viele Rechtecke momentan in dem SSS sind
			counter += 1;
			// Fuegee das Event zum SSS hinzu
			addSssObject(event);
			this.speedcounter1 = speedcounter1 + 1;
			break;

		case UPPERRIGHT:

			log.info("Event 1 rect:" + event.rect + " Event 1 x,y: " + event.getX() + "," + event.getY());

			if (counter > 1) {
				// Die anderen Rechtecke laden! Sicher stellen, dass sie nicht
				// uebereinander sind.
				// Dann die Schnittflaeche berechnen
				event2 = getSssLower(event);
				while (event2 != null) {
					log.info("Event 2 rect:" + event2.rect + " Event 2 x,y: " + event2.getX() + "," + event2.getY());
					if (event.rect.getE2().getY() >= event2.rect.getE1().getY() & event.rect.getE1().getY() <= event2.rect.getE2().getY() & event.rect != event2.rect) {
						// Bestimme alle Punkte vom dem Schnittpunktdreieck.
						double upper = Math.min(event.rect.getE2().getY(), event2.rect.getE2().getY());
						double rigth = Math.min(event.rect.getE2().getX(), event2.rect.getE2().getX());
						double lower = Math.max(event.rect.getE1().getY(), event2.rect.getE1().getY());
						double left = Math.max(event.rect.getE1().getX(), event2.rect.getE1().getX());
						BoxRectangle newr = new BoxRectangle(left, lower, rigth, upper);
						addShape(newr);
						Designer.highlight(newr);
						setArea(getArea() + (rigth - left) * (upper - lower));
						log.info("Current Area:" + getArea() + "if complete: " + upper + "," + rigth + "," + lower + "," + left);
						
					}
					this.speedcounter1 = speedcounter1 + 1;
					event2 = getSssLower(event2);
				}
				
				
			}
			// loesche das letzte Rechteck, dass mit allen verglichen wurde.
			BoxUnionEvent eventlowerleft = event.rect.getE1();
			removeSssObject(event);
			removeSssObject(eventlowerleft);
			counter -= 1;	
			break;
		}
		
	}

	public double getArea() {
		return area;
	}
	
	private void setArea(double a){
		this.area = a;
		
	}

	@Override
	protected void draw() {
		for (BoxRectangle r : rects) {
			addShape(r);
		}
	}

	@Override
	protected void end() {
		log.info("The final area of the union of the rectangles are " + getArea());
		StringBuilder sb = new StringBuilder();
		sb.append("The area of the Union of the Rectangles is: " + area);
		sb.append(" The sweepline algorithm needed: " + speedcounter1 + " steps");
		addMsg(sb.toString());

	}

	@Override
	protected ArrayList<BoxUnionEvent> getInitialEvents() {
		ArrayList<BoxUnionEvent> events = new ArrayList<>();

		rects.add(new BoxRectangle(50, 50, 200, 200));
		rectsbf.add(new BoxRectangle(50,50,200,200));
		rects.add(new BoxRectangle(100, 100, 300, 300));
		rectsbf.add(new BoxRectangle(100, 100, 300, 300));
		rects.add(new BoxRectangle(220, 250, 350, 450));
		rectsbf.add(new BoxRectangle(220, 250, 350, 450));

		for (BoxRectangle r : rects) {
			events.add(r.getE1());
			events.add(r.getE2());
		}
		return events;
	}

	@Override
	protected ArrayList<BoxUnionEvent> getRandomEvents() {
		this.clear();
		ArrayList<BoxUnionEvent> events = new ArrayList<>();
		int count = 4;
		Random rand = new Random();	
		
		for(int i=0; i<count; i++) {
			double e1x = rand.nextInt((int)getPaneWidth()/2);
			double e1y = rand.nextInt((int)getPaneHeight()/2);
			double e2x = e1x + (Math.random() * (int)getPaneWidth()/2);
			double e2y = e1y + (Math.random() * (int)getPaneHeight()/2) ;
			rects.add(new BoxRectangle(e1x,e1y,e2x,e2y));
			rectsbf.add(new BoxRectangle(e1x,e1y,e2x,e2y));
		}
		for (BoxRectangle r : rects) {
			events.add(r.getE1());
			events.add(r.getE2());
		}
		return events;
	}
	
	public void getRandomEvents(int count) {
		ArrayList<BoxUnionEvent> events = new ArrayList<>();
		Random rand = new Random();	
		
		for(int i=0; i<count; i++) {
			double e1x = Math.random() * 1000;
			double e1y = Math.random() * 1000;
			double e2x = e1x + (Math.random() * 1000);
			double e2y = e1y + (Math.random() * 1000);
			rects.add(new BoxRectangle(e1x,e1y,e2x,e2y));
			rectsbf.add(new BoxRectangle(e1x,e1y,e2x,e2y));
		}
	}
	
	

	@Override
	protected void clear() {
		area = 0;
		counter = 0;
		speedcounter1 = 0;
		speedcounter2 = 0;
		event = null;
		event2 = null;
		rects = new ArrayList<>();
		rectsbf = new ArrayList <>();
	}


}
