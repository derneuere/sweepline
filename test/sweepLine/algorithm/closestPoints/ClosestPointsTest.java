package sweepLine.algorithm.closestPoints;

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.layout.Pane;

public class ClosestPointsTest {
	
	ClosestPoints algo;
	
	private final static Logger log = Logger.getLogger(ClosestPointsTest.class.getName());
	
	/**
	 * Sets up the algortihm for the tests.
	 */
	@Before
	public void setUp(){
		Pane canvas = new Pane();
		//ListView<String> output = new ListView<String>();
		//ListView<String> outBst = new ListView<String>();
		this.algo = new ClosestPoints(canvas, null, null);	
	}
	
	/**
	 * Tests the algorithm for the standard case
	 */
	@Test
	public void test() {
		algo.init();
		
		while(algo.next() != false){
		}
		
		Double[] closestPair = new Double[4];
		closestPair[0] = 100.0;
        closestPair[1] = 250.0;
        closestPair[2] = 120.0;
        closestPair[3] = 300.0;
		
        Double[] closestPairAlgo = new Double[4];
        closestPairAlgo[0] = algo.closestPair[0].getCenterX();
        closestPairAlgo[1] = algo.closestPair[0].getCenterY();
        closestPairAlgo[2] = algo.closestPair[1].getCenterX();
        closestPairAlgo[3] = algo.closestPair[1].getCenterY();
		assertTrue(closestPairAlgo[0].equals(closestPair[0]));
	}
	
	public void testAddingEvent() {
		//algo.addPoint(1,1);
		//algo.addPoint(2,2);
	}

}
