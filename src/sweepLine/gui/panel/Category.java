package sweepLine.gui.panel;

public enum Category {
	
	CrossingLines(1, "Crossing lines");
	
	int i;
	String text;
	
	Category(int i, String text) {
		this.i = i;
		this.text = text;
	}

	public int getI() {
		return i;
	}

	public String getText() {
		return text;
	}
	
	
}
