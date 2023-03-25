package com.uvg.gt;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class VectorHeap<T> implements IPriorityQueue<T> {

    private ArrayList<QueueItem<T>> data = new ArrayList<>();

    /**
     * Removes the element with most priority in the queue
     * and return it.
     * 
     * @return Most important element.
     */
    @Override
    public T dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        // Getting item with most priority.
        T value = data.get(0).getValue();

        // Resigning the new root value.
        data.set(0, data.get(data.size() - 1));
        data.remove(data.size() - 1);

        // Rearranging tree to make sure it still a heap.
        if (data.size() > 1)
            pushDownRoot(0);

        return value;
    }

    /**
     * Adds an element to the queue.
     * 
     * @param value    the value to insert.
     * @param priority the priority level. Lower means more priority.
     */
    @Override
    public void insert(T value, int priority) {
        // Inserting new item to queue.
        data.add(new QueueItem<T>(value, priority));
        // Rearranging the new item in the queue to maker sure it still ordered by
        // priority.
        traverseUp(data.size() - 1);
    }

    /**
     * Returns the element with most priority at the moment.
     * 
     * @return Most important element.
     */
    @Override
    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException();
        return this.data.get(0).getValue();
    }

    /**
     * Checks if queue is empty.
     * 
     * @return True = empty | False = not empty.
     */
    @Override
    public boolean isEmpty() {
        return this.data.size() == 0;
    }

    /**
     * Returns queue size.
     * 
     * @return queue's size.
     */
    public int size() {
        return this.data.size();
    }

    /**
     * Moves and element up the tree, until it finds the
     * right spot within the heap.
     * 
     * @param leaf Index of the element to move.
     */
    private void traverseUp(int leaf) {
        int parent = parent(leaf);
        QueueItem<T> value = data.get(leaf);
        while (leaf > 0 &&
                (value.compareTo(data.get(parent))) < 0) {
            data.set(leaf, data.get(parent));
            leaf = parent;
            parent = parent(leaf);
        }
        data.set(leaf, value);
    }

    /**
     * Moves and element down the tree, until it finds the
     * right spot within the heap.
     * 
     * @param root Index of the element to move.
     */
    private void pushDownRoot(int root) {
        int heapSize = data.size();
        QueueItem<T> value = data.get(root);
        while (root < heapSize) {
            int childpos = leftChild(root);
            if (childpos < heapSize) {
                if (rightChild(root) < heapSize &&
                        (data.get(rightChild(root))).compareTo(data.get(childpos)) < 0)
                    childpos++;
                if ((data.get(childpos)).compareTo(value) < 0) {
                    data.set(root, data.get(childpos));
                    root = childpos;
                } else {
                    data.set(root, value);
                    return;
                }
            } else {
                data.set(root, value);
                return;
            }
        }
    }

    /**
     * Finds the parent of a node.
     * 
     * @param i index of the current node.
     * @return Index of the parent node.
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Finds the left child of a node.
     * 
     * @param i index of the current node.
     * @return Index of it's left child node.
     */
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Finds the right child of a node.
     * 
     * @param i index of the current node.
     * @return Index of it's right child node.
     */
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    @Override
    public String toString() {
        return data.stream()
                .map(QueueItem::getValue)
                .map(Objects::toString)
                .collect(Collectors.joining(",", "{", "}"));
    }
}
