package com.uvg.gt;

import java.util.PriorityQueue;

public class JCFPriorityQueue<T> implements IPriorityQueue<T> {
    private PriorityQueue<QueueItem<T>> internal = new PriorityQueue<>();

    @Override
    public T dequeue() {
        QueueItem<T> result = internal.poll();
        if (result == null) {
            return null;
        }
        return result.getValue();
    }

    @Override
    public void insert(T value, int priority) {
        internal.add(new QueueItem<>(value, priority));
    }

    @Override
    public T peek() {
        var result = internal.peek();
        if (result == null) {
            return null;
        }

        return result.getValue();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

}
