package com.company;
import java.io.*;
import java.util.ArrayList;


public class Parser {
    private File fileToParse;
    private FileReader fileReader;
    private BufferedReader buffReader;
    private ArrayList<logLine> logLines;

    public Parser (String fileName) {
        fileToParse = new File(fileName);
        logLines = getLines(fileToParse);
    }

    private ArrayList<logLine> getLines (File fileToRead) {
        String lineToRead;
        ArrayList<logLine> lines = new ArrayList<logLine>();
        try {
            if (fileToRead.length() != 0) {
                fileReader = new FileReader(fileToRead);
                buffReader = new BufferedReader(fileReader);
                buffReader.readLine();
                while ((lineToRead = buffReader.readLine()) != null) {
                    lines.add(parseLine(lineToRead));
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Specified file does not exist!");
        }
        catch (IOException e) {
            System.out.println("Could not read from the file!");
        }
        return lines;
    }

    public int getEntityNo () {
        int firstEntity = logLines.get(0).getAgentNo();
        int count = 1;
        for (int i = 1; i < logLines.size(); i++) {
            int currentAgent = logLines.get(i).getAgentNo();
            if (currentAgent == firstEntity) {
                return count;
            }
            else {
                count++;
            }
        }
        return count;
    }

    private logLine parseLine (String lineToParse) {
        logLine line;
        String[] splitString;
        splitString = lineToParse.split(",");
        for (int i = 0; i < splitString.length; i++) {
            splitString[i] = splitString[i].trim(); //Remove whitespace
        }
        //Here I insert the necessary quantities from the logs into a separate Class for later use
        line = new logLine(splitString);
        return line;
    }

    public ArrayList<logLine> getLogLines () {
        return logLines;
    }
}
