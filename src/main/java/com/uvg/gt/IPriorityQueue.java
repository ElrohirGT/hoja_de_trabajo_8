package com.uvg.gt;

public interface IPriorityQueue<T> {
    /**
     * @return the item with the most priority.
     */
    public T get();

    /**
     * Inserts a value into the priority queue with the given priority.
     * 
     * @param value    the value to insert.
     * @param priority the priority level. Lower means more priority.
     */
    public void insert(T value, int priority);

    /**
     * @return the element that get would return.
     */
    public T peek();
}
