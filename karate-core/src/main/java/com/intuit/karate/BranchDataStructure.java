package com.intuit.karate;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
/**
 * BranchDataStructure
 */
public class BranchDataStructure {

    public static final HashMap<String, BranchDataStructure> instances = new HashMap<>();

    private boolean[] flags;
    private int branchCount;
    private String name = "flags.dat";


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
        double result = count / branchCount * 100;
        return result;
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

    public void saveFlags() {
        boolean[] toSave = flags;

        if (fileExists(name)) {
            toSave = elementwiseOR(toSave, readBooleanArrayFromFile(name));
        }

        readBooleanArrayFromFile(name);
        writeBooleanArrayToFile(toSave, name);
    }

    public void loadFlags() {
        if (fileExists(name)) {
            flags = readBooleanArrayFromFile(name);
        }
    }

    // Function to check if a file exists locally
    public static boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    // Method to write a boolean array to a file
    private static void writeBooleanArrayToFile(boolean[] array, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             DataOutputStream dos = new DataOutputStream(fos)) {

            dos.writeInt(array.length); // Write the length of the array first
            for (boolean value : array) {
                dos.writeBoolean(value); // Write each boolean value
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read a boolean array from a file
    private static boolean[] readBooleanArrayFromFile(String filename) {
        boolean[] array = null;
        try (FileInputStream fis = new FileInputStream(filename);
             DataInputStream dis = new DataInputStream(fis)) {

            int length = dis.readInt(); // Read the length of the array first
            array = new boolean[length];
            for (int i = 0; i < length; i++) {
                array[i] = dis.readBoolean(); // Read each boolean value
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    // Function to perform element-wise OR on two boolean arrays
    public static boolean[] elementwiseOR(boolean[] array1, boolean[] array2) {
        if (array1.length != array2.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }

        boolean[] result = new boolean[array1.length];
        for (int i = 0; i < array1.length; i++) {
            result[i] = array1[i] || array2[i]; // Perform OR operation element-wise
        }
        return result;
    }


}