package com.uvg.gt;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class VectorHeapTest {

    private boolean setUpIsDone = false;

    VectorHeap<String> JOBS = new VectorHeap<>();

    @BeforeEach
    public void setUp() {
        if (setUpIsDone) {
            return;
        }
        JOBS.insert("Cleaner", 10);
        JOBS.insert("director", 5);
        JOBS.insert("manager", 2);
        JOBS.insert("president", 0);

        setUpIsDone = true;
    }

    @Test
    void testToString() {
        Assertions.assertEquals("{president,manager,director,Cleaner}", JOBS.toString());
    }

    @Test
    void insert() {
        JOBS.insert("Client", 15);
        Assertions.assertEquals("{president,manager,director,Cleaner,Client}", JOBS.toString());
    }

    @Test
    void peek() {
        Assertions.assertEquals("president", JOBS.peek());
    }

    @Test
    void dequeue() {
        Assertions.assertEquals("president", JOBS.dequeue());
    }
}