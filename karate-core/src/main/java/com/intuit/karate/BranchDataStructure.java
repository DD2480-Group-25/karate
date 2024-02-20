package com.intuit.karate;
import java.util.HashMap;
/**
 * BranchDataStructure
 */
public class BranchDataStructure {

    public static final HashMap<String, BranchDataStructure> instances = new HashMap<>();

    private boolean[] flags;
    private int branchCount;

    public BranchDataStructure(int branchCount, String name) {
        this.branchCount = branchCount;
        reset();
        instances.put(name, this);
    }

    public void reset() {
        flags = new boolean[branchCount];
        for (int i = 0; i < branchCount; i++) {
            flags[i] = false;
        }
    }

    public void setFlag(int id) {
        if (id >= 0 && id < branchCount) {
            flags[id] = true;
        }
    }

    public boolean getFlag(int id) {
        if (id >= 0 && id < branchCount) {
            return flags[id];
        }

        return false;
    }

    public boolean isAllTrue() {
        boolean result = true;
        for (int i = 0; i < branchCount; i++) {
            if (!flags[i]) {
                result = false;
            }
        }

        return result;
    }

    public double getBranchTakenPercentage() {
        double count = 0;
        for (int i = 0; i < branchCount; i++) {
            if (flags[i]) {
                count++;
            }
        }
        return count / branchCount * 100;
    }

    public void logResults() {
        System.out.println("There are " + branchCount + " branches in total in the function.");
        System.out.println(String.format("%.2g", getBranchTakenPercentage()) + " % of them were taken during the testing.");

        if (!isAllTrue()) {
            System.out.println("The following branches were taken:");
            for (int i = 0; i < branchCount; i++) {
                if (flags[i]) {
                    System.out.print(i + ", ");
                }
            }
            System.out.println("");
            System.out.println("The following branches were NOT taken:");
            for (int i = 0; i < branchCount; i++) {
                if (!flags[i]) {
                        System.out.print(i + ", ");
                    }
                }
            System.out.println("");
        } else {
            System.out.println("All the branches were taken, yay");
        }
    }
}