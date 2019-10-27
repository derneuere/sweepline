package sweepLine.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import sweepLine.algorithm.areaOfTheUnionOfTwoRectangles.BoxRectangle;

public class Designer {
	
	private Designer() {}
	
	
	
	// Circle
	public static final void design(Circle e) {
		e.setRadius(5);
		e.setStrokeWidth(2);
		e.setFill(null);
		normal(e);
	}
	public static final void normal(Circle e) {
		e.setStroke(Color.DODGERBLUE);
	}
	public static final void highlight(Circle e) {
		e.setStroke(Color.RED);
	}
	
	
	
	
	// Line
	public static final void design(Line e) {
		e.setStrokeWidth(2);
		e.setFill(null);
		normal(e);
	}
	public static final void normal(Line e) {
		e.setStroke(Color.GREY);
	}
	public static final void highlight(Line e) {
		e.setStroke(Color.RED);
	}
	
	
	
	// Sweep Line
	public static final void designSweepLine(Line e) {
		e.setStrokeWidth(2);
		e.setFill(null);
		normalSweepLine(e);
	}
	public static final void normalSweepLine(Line e) { e.setStroke(Color.CHOCOLATE); }
	
	// Convex hull lines
	public static final void designConvexHullLineActiveUpper(Line line) {
		line.setStroke(Color.DARKGREEN);
		line.setStrokeWidth(2);
		line.setOpacity(0.9);
	}

	public static final void designConvexHullLineInactiveUpper(Line line) {
		line.setStroke(Color.DARKGREEN);
		line.setStrokeWidth(1);
		line.setOpacity(0.3);
	}
	public static final void designConvexHullLineActiveLower(Line line) {
		line.setStroke(Color.DARKRED);
		line.setStrokeWidth(2);
		line.setOpacity(0.9);
	}
	public static final void designConvexHullLineInactiveLower(Line line) {
		line.setStroke(Color.DARKRED);
		line.setStrokeWidth(1);
		line.setOpacity(0.3);
	}


	// Rectangle
	
	public static void highlight(BoxRectangle r) {
		r.setStroke(Color.RED);
		r.setOpacity(0.3);
		r.setFill(Color.RED);
		
	}
	
}
