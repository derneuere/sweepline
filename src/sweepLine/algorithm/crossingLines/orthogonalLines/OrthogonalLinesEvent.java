package sweepLine.algorithm.crossingLines.orthogonalLines;


import sweepLine.framework.AbstractFrameworkEvent;

public class OrthogonalLinesEvent extends AbstractFrameworkEvent {

	enum Type {
		HorizontalStart, HorizontalEnd, VerticalStart, VerticalEnd
	};

	Type t;
	static double start_y;

	OrthogonalLinesEvent(Type t, double x, double y) {
		super(x, y);
		this.t = t;
	}

	public String toString() {
		String out;
		
		out = "(" + this.getX() + " | " + this.getY() + ") ";
		switch (t) {
		case HorizontalStart:
			out += "is the start of a horizontal line.";
			break;
		case HorizontalEnd:
			out += "is the end of a horizontal line.";
			break;
		case VerticalStart:
			out += "is the start of a vertical line.";
			break;
		case VerticalEnd:
			out += "is the end of a vertical line.";
			break;
		}
		return out;
	}
}
