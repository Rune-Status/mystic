package org.mystic.utility;

import java.util.LinkedList;

/**
 * An implementation of a linked list where you have a pointer to one list
 * element's index and that can be increased and decreased with the methods
 * {@code next} and {@code previous}, respectively.
 * 
 * @author Joshua Barry
 * 
 * @param <E>
 *            The type of items to hold in the list.
 */
public class TraversableList<E> extends LinkedList<E> {

	/**
	 * The pointer to the current element.
	 */
	private int pointer = 0;

	/**
	 * The serialization version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Returns the next element.
	 * 
	 * @return The next element.
	 */
	public E next() {
		if ((pointer + 1) >= size()) {
			return null;
		}
		pointer++;
		return get(pointer);
	}

	/**
	 * Returns the previous element.
	 * 
	 * @return The previous element.
	 */
	public E previous() {
		if ((pointer - 1) < 0) {
			return null;
		}
		pointer--;
		return get(pointer);
	}

	/**
	 * Returns the current element.
	 * 
	 * @return The current element.
	 */
	public E current() {
		return get(pointer);
	}
}