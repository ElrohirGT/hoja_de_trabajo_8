package com.uvg.gt;

/**
 * Container class for storing items of a Priority Queue.
 * @param <T> Type of the object to store.
 */
public class QueueItem <T> implements Comparable<QueueItem<T>> {
    private final int priority;
    T value;

    public QueueItem(T value, int priority){
        this.value = value;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public T getValue(){
        return this.value;
    }

    @Override
    public int compareTo(QueueItem<T> that) {
        return Integer.compare(this.getPriority(), that.getPriority());
    }
}
