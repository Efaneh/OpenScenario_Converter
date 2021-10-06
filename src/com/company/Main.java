package com.company;

public class Main {

    public static void main(String[] args) {
        try {
            Parser parseLog = new Parser(args[0]);
            Writer openSWriter = new Writer(parseLog.getLogLines(), args[1], args[2], parseLog, 2);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Incorrect command line parameters - Should be Log file (txt), name of produced file and finally the CARLA map");
        }
    }
}
