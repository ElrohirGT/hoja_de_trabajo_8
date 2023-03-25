package com.uvg.gt;

/**
 * Representation of a PC Process.
 */
public class Process implements Comparable<Process>{

    private final int NICE_CONSTANT = 120;
    private String name;
    private String owner;
    private int nice;

    /**
     * Constructor
     * @param name name
     * @param owner owner
     * @param nice process's nice value.
     */
    public Process(String name, String owner, int nice) {
        this.name = name;
        this.owner = owner;
        this.nice = nice;
    }

    /**
     * Returns priority in Linux standards.
     * @return A number between 100 and 139.
     */
    public int getPriority(){
        return nice + NICE_CONSTANT;
    }

    @Override
    public int compareTo(Process that) {
        return Integer.compare(this.getPriority(), that.getPriority()) * -1;
    }

    @Override
    public String toString() {
        return name + "," + owner + "," + nice;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public int getNice() {
        return nice;
    }
}
