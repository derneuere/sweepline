package sweepLine.library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractAlgorithm<E extends EventInterface, T> {
	
	private class State<E,T> {
		E event;
		ArrayList<E> queueInsertions;
		ArrayList<E> queueDeletions;
		ArrayList<T> statusInsertions;
		ArrayList<T> statusDeletions;
		State() {
			queueInsertions = new ArrayList<>();
			queueDeletions = new ArrayList<>();
			statusInsertions = new ArrayList<>();
			statusDeletions = new ArrayList<>();
		}
	}

	protected final PriorityQueue<E> eventQueue;
	protected final TreeSet<T> sweepStatusStructure;
	protected E currentEvent;
	protected final ArrayList<State<E,T>> states;
	protected int current;

	/**
	 * The Constructor.
	 * @param prioQueueComparator is used to sort the events in the priority queue.
	 * @param treeSetComparator is used to sort the status structure.
	 */
	public AbstractAlgorithm(Comparator<E> prioQueueComparator, Comparator<T> treeSetComparator){
		this.eventQueue = new PriorityQueue<>(prioQueueComparator);
		this.sweepStatusStructure = new TreeSet<>(treeSetComparator);
		this.states = new ArrayList<>(20);
		this.current = -1;
	}

	/**
	 * Passes the current event to the algorithm.
	 */
	protected abstract void computeNext(E event);
	/**
	 * This is called when the there are no more events,
	 * so you can print a final message or similar.
	 */
	protected abstract void end();
	
	/**
	 * The starting state.
	 * The events returned by this method will be added
	 * to the priority queue before the algorithm is executed. 
	 */
	protected abstract ArrayList<E> getInitialEvents();
	
	/**
	 * Returns some random events.
	 */
	protected abstract ArrayList<E> getRandomEvents();
	
	/** 
	 * Add a message to the text widget.
	 * @param msg The text to append
	 */
	protected void addMsg(String msg) {		
		System.out.println(msg);
	}
	
	/**
	 * Adds an Event to the priority queue.
	 */
	protected void addEvent(E event) {
		eventQueue.add(event);
		states.get(current).queueInsertions.add(event);
	}
	
	/** Remove an element from the priority queue. */
	protected void removeEvent(E event) {
		eventQueue.remove(event);
		states.get(current).queueDeletions.add(event);
	}

	/** Add an object to the status structure. */
	protected void addSssObject(T treeObject) {
		this.sweepStatusStructure.add(treeObject);
		states.get(current).statusInsertions.add(treeObject);
	}

	/** Remove an object from the status structure. */
	protected void removeSssObject(T treeObject) {
		this.sweepStatusStructure.remove(treeObject);
		states.get(current).statusDeletions.add(treeObject);
	}
	
	/** Returns the element in the status structure
	 * bigger than @param near.
	 */
	protected T getSssHigher(T near) {
		return this.sweepStatusStructure.higher(near);
	}

	/**
	 * Returns the element in the status structure
	 * smaller than @param near.
	 */
	protected T getSssLower(T near) {
		return this.sweepStatusStructure.lower(near);
	}
	
	/** 
	 * Returns the elements in the status structure
	 * bigger than @param from and smaller than @param to.
	 */
	protected SortedSet<T> getBetween(T from, T to) {
		return this.sweepStatusStructure.subSet(from, to);
	}
	
	/**
	 * Returns the elements in the status structure bigger
	 * than @param from and smaller than @param to.
	 */
	protected SortedSet<T> getBetween(T from, boolean fromInclusive, T to, boolean toInclusive) {
		return this.sweepStatusStructure.subSet(from, fromInclusive, to, toInclusive);
	}

	/* This is called by the GUI when the next button is clicked. */
	public boolean next() {
		currentEvent = this.eventQueue.poll();
		if(currentEvent == null) {
			end();
			return false;
		}
		current++;
		if (current >= states.size()) {
			State<E,T> st = new State<>();
			st.event = currentEvent;
			states.add(st);
			computeNext(currentEvent);
		} else {
			State<E,T> st = states.get(current);
			for (E e : st.queueDeletions) {
				eventQueue.remove(e);
			}
			for (E e : st.queueInsertions) {
				eventQueue.add(e);
			}
			for (T t : st.statusDeletions) {
				sweepStatusStructure.remove(t);
			}
			for (T t : st.statusInsertions) {
				sweepStatusStructure.add(t);
			}
		}
		return true;
	}
	public boolean prev() {
		if (current > -1) {
			State<E,T> st = states.get(current);
			for (E e : st.queueDeletions) {
				eventQueue.add(e);
			}
			for (E e : st.queueInsertions) {
				eventQueue.remove(e);
			}
			for (T t : st.statusDeletions) {
				sweepStatusStructure.add(t);
			}
			for (T t : st.statusInsertions) {
				sweepStatusStructure.remove(t);
			}
			eventQueue.add(states.get(current).event);
			current--;
			if (current > -1) {
				currentEvent = states.get(current).event;
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public void reset() {
		while (current > -1) {
			prev();
		}
	}
}
