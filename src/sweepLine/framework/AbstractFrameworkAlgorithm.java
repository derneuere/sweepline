package sweepLine.framework;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import sweepLine.gui.Designer;
import sweepLine.library.AbstractAlgorithm;

/**
 * This is the parent of all algorithms. Inherit from this class
 * and implement the abstract methods.
 * This class takes care of priority queue the tree set
 * and the graphics and animations and most of the logic.
 *
 * @param <E> - Event type
 * @param <T> - TreeSet type (binary search tree)
 */
public abstract class AbstractFrameworkAlgorithm<E extends AbstractFrameworkEvent, T> extends AbstractAlgorithm<E,T>{

	
	private class FrameworkState {
		ArrayList<Shape> shapeInsertions;
		ArrayList<Shape> shapeDeletions;
		ArrayList<String> textInsertions;
		ArrayList<String> textDeletions;
		FrameworkState() {
			shapeInsertions = new ArrayList<>();
			shapeDeletions = new ArrayList<>();
			textInsertions = new ArrayList<>();
			textDeletions = new ArrayList<>();
		}
	}

	private final Pane pane;
	private final ListView<String> output;
	private final ListView<String> treeSetView;
	private final Line sweepline;
	private ArrayList<FrameworkState> frameworkStates;

	/** The constructor. Make sure to call super.
	 * @param prioQueueComparator Used by
	 * the priority queue to sort the events.
	 * @param treeSetComparator Used by the tree set to sort
	 * whatever it contains.
	 * */
	protected AbstractFrameworkAlgorithm(Pane pane, ListView<String> output,
			ListView<String> treeSetView,
			Comparator<E> prioQueueComparator, Comparator<T> treeSetComparator) {
		super(prioQueueComparator, treeSetComparator);
		this.pane = pane;
		this.output = output;
		this.treeSetView = treeSetView;
		this.sweepline = new Line(0, 10, 0, 500);
		this.pane.getChildren().add(sweepline);
		this.frameworkStates = new ArrayList<>();
	}

	/* 
	 * This method is necessary because super constructors may not
	 * call instance methods or access instance variables.
	 */
	public void init() {
		ArrayList<E> events = getInitialEvents();
		draw();
		for(E event:events) {
			Designer.design(event);
			eventQueue.add(event);
			pane.getChildren().add(event);
		}
	}

	private void moveSweepLine(E event){
		moveSweepLine(event.getX());
	}

	/* Animates the line moving to event. */ 
	private void moveSweepLine(double x) {
		if(Platform.isFxApplicationThread()) {
			// set end position in ms
			double duration = 5 * Math.abs(sweepline.getTranslateX() - x);
		
			// move sweep line to event:
			Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
			new KeyFrame(
				Duration.ZERO, // set start position at 0
				new KeyValue(sweepline.translateXProperty(), sweepline.getTranslateX())
			),
			new KeyFrame(
				new Duration(duration),
				new KeyValue(sweepline.translateXProperty(), x)
			)
		);
	
		// play animation
		timeline.play();}
	}

	/**
	 * This is called when the algorithm is selected.
	 * This method should draw the graphics that don't
	 * change during the execution of the algorithm.
	 */
	protected abstract void draw();

	@Override
	protected void addMsg(String msg) {
		super.addMsg(msg);
		if(output != null) {
			this.output.getItems().add(msg);
			if (current != -1) {
				frameworkStates.get(current).textInsertions.add(msg);
			}
		}
	}
	
	/**
	 * Sets the message of the text widget.
	 * @param msg the text to show
	 */
	protected void setMsg(String msg) {
		if(output != null) {
			if (current != -1) {
				for (String s : output.getItems()) {
					frameworkStates.get(current).textDeletions.add(s);
				}
			}
			this.output.getItems().clear();
			this.output.getItems().add(msg);
			if (current != -1) {
				frameworkStates.get(current).textInsertions.add(msg);
			}
		}
		System.out.println(msg);
	}
	
	/**
	 * Adds an Event to the priority queue.
	 * You do not need to draw it, the framework
	 * will do it automatically.
	 */
	@Override
	protected void addEvent(E event) {
		super.addEvent(event);
		addShape(event);
	}

	/** Add a shape to the drawing area. */
	protected void addShape(Shape shape) {
		this.pane.getChildren().add(shape);
		// TODO: ugly
		if (current != -1)
			frameworkStates.get(current).shapeInsertions.add(shape);
	}
	
	/** Delete a shape from the drawing area. */
	protected void removeShape(Shape shape) {
		this.pane.getChildren().remove(shape);
		frameworkStates.get(current).shapeDeletions.add(shape);
	}
	
	private void updateTreeSetView() {
		if(treeSetView != null) {
			this.treeSetView.getItems().clear();
			for (T t : this.sweepStatusStructure) {
				this.treeSetView.getItems().add(t.toString());	
			}
		}
	}

	/* This is called by the GUI when the next button is clicked. */
	public boolean next() {
		if (this.currentEvent != null)
			Designer.normal(this.currentEvent);
		if (currentEvent == null && current != -1) {
			return super.next();
		}
		if (current + 1 >= frameworkStates.size()) {
			FrameworkState fwst = new FrameworkState();
			frameworkStates.add(fwst);
			// TODO: this is ugly
			if (super.next() == false) {
				return false;
			}
		} else {
			super.next();
			FrameworkState fwst = frameworkStates.get(current);
			for (Shape s : fwst.shapeDeletions) {
				pane.getChildren().remove(s);
			}
			for (Shape s : fwst.shapeInsertions) {
				pane.getChildren().add(s);
			}
			for (String t : fwst.textDeletions) {
				output.getItems().remove(t);
			}
			for (String t : fwst.textInsertions) {
				output.getItems().add(t);
			}
		}
		if (currentEvent != null) {
			Designer.highlight(currentEvent);
			moveSweepLine(currentEvent);
		}
		updateTreeSetView();
		return true;
	}
	
	public boolean prev() {
		if (current > -1) {
			FrameworkState fwst = frameworkStates.get(current);
			if (currentEvent != null)
				Designer.normal(currentEvent);
			for (Shape s : fwst.shapeDeletions) {
				pane.getChildren().add(s);
			}
			for (Shape s : fwst.shapeInsertions) {
				pane.getChildren().remove(s);
			}
			for (String t : fwst.textDeletions) {
				output.getItems().add(t);
			}
			for (String t : fwst.textInsertions) {
				output.getItems().remove(t);
			}
			super.prev();
			updateTreeSetView();
			if (current == -1) {
				moveSweepLine(0);
				return false;
			}
			moveSweepLine(currentEvent);
			Designer.highlight(currentEvent);
			return true;
		}
		return false;
	}

	/** The framework does not create a new object when random is clicked, so
	 * the current algorithm needs to be reset. This method is used to
	 * reset/clear all variables and data structures used by the algorithm.
	 * Data structures managed by the framework don't need to be cleared
	 * manually.
	 */
	protected abstract void clear();

	public void reset() {
		while (current > -1)
			prev();
	}

	/* This is called when the random button is clicked */
	public void random() {

		this.eventQueue.clear();
		this.sweepStatusStructure.clear();
		this.pane.getChildren().clear();
		this.frameworkStates.clear();
		states.clear();
		current = -1;
		clear();

		ArrayList<E> events = getRandomEvents();
		draw();
		for(E event:events) {
			Designer.design(event);
			eventQueue.add(event);
			pane.getChildren().add(event);
		}
		this.pane.getChildren().add(sweepline);
		moveSweepLine(0);
	}

	public double getPaneHeight() {
		return this.pane.getHeight();
	}

	public double getPaneWidth() {
		return this.pane.getWidth();
	}

	public int numberOfEvents() {
		return this.eventQueue.size();
	}
}
