package sweepLine.algorithm.areaOfTheUnionOfTwoRectangles;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sweepLine.algorithm.areaOfTheUnionOfTwoRectangles.BoxUnionEvent.EventType;

public class BoxRectangle extends Rectangle{
	
	private BoxUnionEvent e1;
	private BoxUnionEvent e2;
	
	public BoxRectangle(double x1, double y1, double x2, double y2){
		super(x1,y1,x2-x1,y2-y1);
		
		e1 = new BoxUnionEvent(this, EventType.LOWERLEFT, x1, y1);
		e2 = new BoxUnionEvent(this, EventType.UPPERRIGHT, x2, y2);
		
		this.setStrokeWidth(2);
		this.setStroke(Color.BLACK);
		this.setFill(Color.TRANSPARENT);
	}

	
	public BoxUnionEvent getE1(){
		return e1;
			}
	
	public BoxUnionEvent getE2(){
		return e2;
	}
		

	
}
