package com.company;

public class logLine {
    //Class that holds the relevant values from a line of the produced log file
    private int agentNo;
    private String agentType;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double simTime;

    public logLine (String[] splitString) {
        agentNo = Integer.parseInt(splitString[2]);
        agentType = splitString[4];
        x = Double.parseDouble(splitString[6]);
        y = Double.parseDouble(splitString[7]);
        z = Double.parseDouble(splitString[8]);
        yaw = Double.parseDouble(splitString[9]);
        simTime = Double.parseDouble(splitString[14]);
    }

    public int getAgentNo (){
        return agentNo;
    }

    public String getAgentType (){
        return agentType;
    }

    public double getX () {
        return x;
    }

    public double getY () {
        return y;
    }

    public double getZ () {
        return z;
    }

    public double getYaw () {
        return yaw;
    }

    public double getSimTime () {
        return simTime;
    }
}
