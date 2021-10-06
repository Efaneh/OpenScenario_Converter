package com.company;

public class Main {

    public static void main(String[] args) {
        Parser parseLog = new Parser(args[0]);
        Writer openSWriter = new Writer(parseLog.getLogLines(), args[1], args[2], parseLog, 2);
    }
}
