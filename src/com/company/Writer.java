package com.company;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;


public class Writer {
    private File fileToCreate;
    private FileWriter fileWriter;
    private BufferedWriter buffWriter;
    private String pathToMap;
    private ArrayList<logLine> logLines;
    private Parser parseInput;
    private int count;
    private ArrayList<Double> velocities;
    private ArrayList<logLine> wayPoints;
    private double lookahead;

    public Writer (ArrayList<logLine> log, String fileName, String mapPath, Parser parsed, double lHead) {
        count = 0;
        lookahead = lHead;
        fileToCreate = new File(fileName+(Double.toString(lookahead)).replace(".", "") + ".xosc");
        if (fileToCreate.exists()) {
            fileToCreate.delete();
        }
        createFile(fileToCreate);
        logLines = log;
        velocities = new ArrayList<Double>();
        wayPoints = new ArrayList<logLine>();
        parseInput = parsed;
        pathToMap = mapPath;
        writeTopLines();
        writeInEntities();
        initActions();
        storyInit();
    }

    private void createFile (File file) {
        boolean didCreate =  false;
        try {
            didCreate = file.createNewFile();
        }
        catch (IOException e) {
            System.out.println("Input/Output Error during creation of file!");
        }
        if (!didCreate) {
            System.out.println("Could not create file!");
            System.exit(1);
        }
    }

    private void writeTopLines () {
        try {
            fileWriter = new FileWriter(fileToCreate);
            buffWriter = new BufferedWriter((fileWriter));
            buffWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            buffWriter.newLine();
            buffWriter.write("<OpenSCENARIO xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"OpenSCENARIO.xsd\">");
            buffWriter.newLine();
            buffWriter.write(" <FileHeader revMajor=\"1\" revMinor=\"0\" date=\"" + java.time.LocalDateTime.now() + "\" description=\"CARLA:Converted from logs\" author=\"Efan Haynes\"/>");
            buffWriter.newLine();
            buffWriter.write(" <ParameterDeclarations/>");
            buffWriter.newLine();
            buffWriter.write(" <CatalogLocations/>");
            buffWriter.newLine();
            buffWriter.write(" <RoadNetwork>");
            buffWriter.newLine();
            buffWriter.write("  <LogicFile filepath=\"" + pathToMap + "\"/>");
            buffWriter.newLine();
            buffWriter.write(" </RoadNetwork>");
            buffWriter.newLine();
            buffWriter.flush();
            buffWriter.close();
        }
        catch (IOException e) {
            System.out.println("Could not write to the file!");
            System.exit(1);
        }
    }

    private void writeInEntities () {
        try {
            fileWriter = new FileWriter(fileToCreate, true);
            buffWriter = new BufferedWriter((fileWriter));
            buffWriter.write(" <Entities>");
            buffWriter.newLine();
            classifyObject();
            buffWriter.write(" </Entities>");
            buffWriter.newLine();
            buffWriter.flush();
            buffWriter.close();
        }
        catch (IOException e) {
            System.out.println("Could not write to the file!");
            System.exit(1);
        }
    }

    private void classifyObject () {
        try {
            for (int i = 0; i < parseInput.getEntityNo(); i++) {
                buffWriter.write("  <ScenarioObject name=\"" + (i+1) + "\">");
                buffWriter.newLine();
                if (logLines.get(i).getAgentType().charAt(0) == 'v') {
                    buffWriter.write("   <Vehicle name=\"" + logLines.get(i).getAgentType() + "\" vehicleCategory=\"car\">");
                    buffWriter.newLine();
                    buffWriter.write("    <ParameterDeclarations/>");
                    buffWriter.newLine();
                    buffWriter.write("    <Performance maxSpeed=\"69.444\" maxAcceleration=\"200\" maxDeceleration=\"10.0\"/>");
                    buffWriter.newLine();
                    buffWriter.write("    <BoundingBox>");
                    buffWriter.newLine();
                    buffWriter.write("     <Center x=\"1.5\" y=\"0.0\" z=\"0.9\"/>");
                    buffWriter.newLine();
                    buffWriter.write("     <Dimensions width= \"2.1\" length= \"4.5\" height=\"1.8\"/>");
                    buffWriter.newLine();
                    buffWriter.write("    </BoundingBox>");
                    buffWriter.newLine();
                    buffWriter.write("    <Axles>");
                    buffWriter.newLine();
                    buffWriter.write("     <FrontAxle maxSteering=\"0.5\" wheelDiameter=\"0.6\" trackWidth=\"1.8\" positionX=\"3.1\" positionZ=\"0.3\"/>");
                    buffWriter.newLine();
                    buffWriter.write("     <RearAxle maxSteering=\"0.0\" wheelDiameter=\"0.6\" trackWidth=\"1.8\" positionX=\"0.0\" positionZ=\"0.3\"/>");
                    buffWriter.newLine();
                    buffWriter.write("    </Axles>");
                    buffWriter.newLine();
                    buffWriter.write("    <Properties>");
                    buffWriter.newLine();
                    buffWriter.write("     <Property name=\"type\" value=\"vehicle" + i + "\"/>");
                    buffWriter.newLine();
                    buffWriter.write("    </Properties>");
                    buffWriter.newLine();
                    buffWriter.write("   </Vehicle>");
                    buffWriter.newLine();
                } else {
                    buffWriter.write("   <Pedestrian model=\"" + logLines.get(i).getAgentType() + "\" mass =\"90\" name=\"" + logLines.get(i).getAgentType() + "\" pedestrianCategory=\"pedestrian\">");
                    buffWriter.newLine();
                    buffWriter.write("    <ParameterDeclarations/>");
                    buffWriter.newLine();
                    buffWriter.write("    <BoundingBox>");
                    buffWriter.newLine();
                    buffWriter.write("     <Center x=\"1.5\" y=\"0.0\" z=\"0.9\"/>");
                    buffWriter.newLine();
                    buffWriter.write("     <Dimensions width=\"2.1\" length=\"4.5\" height=\"1.8\"/>");
                    buffWriter.newLine();
                    buffWriter.write("    </BoundingBox>");
                    buffWriter.newLine();
                    buffWriter.write("    <Properties>");
                    buffWriter.newLine();
                    buffWriter.write("     <Property name=\"type\" value=\"pedestrian" + i + "\"/>");
                    buffWriter.newLine();
                    buffWriter.write("    </Properties>");
                    buffWriter.newLine();
                    buffWriter.write("   </Pedestrian>");
                    buffWriter.newLine();
                }
                buffWriter.write("  </ScenarioObject>");
                buffWriter.newLine();
            }
        }
        catch (IOException e) {
            System.out.println("Could not write to the file!");
            System.exit(1);
        }
    }

    private void initActions () {
        try {
            fileWriter = new FileWriter(fileToCreate, true);
            buffWriter = new BufferedWriter((fileWriter));
            buffWriter.write(" <Storyboard>");
            buffWriter.newLine();
            buffWriter.write("  <Init>");
            buffWriter.newLine();
            buffWriter.write(("   <Actions>"));
            buffWriter.newLine();
            for (int i = 0; i < parseInput.getEntityNo(); i++) {
                buffWriter.write("    <Private entityRef=\"" + (i+1) + "\">");
                buffWriter.newLine();
                buffWriter.write("     <PrivateAction>");
                buffWriter.newLine();
                buffWriter.write("      <TeleportAction>");
                buffWriter.newLine();
                buffWriter.write("       <Position>");
                buffWriter.newLine();
                buffWriter.write("        <WorldPosition x=\"" + logLines.get(i).getX() + "\" y=\"" + logLines.get(i).getY() +
                        "\" z=\"" + logLines.get(i).getZ() + "\" h=\"" + logLines.get(i).getYaw() + "\"/>");
                buffWriter.newLine();
                buffWriter.write("       </Position>");
                buffWriter.newLine();
                buffWriter.write("      </TeleportAction>");
                buffWriter.newLine();
                buffWriter.write("     </PrivateAction>");
                buffWriter.newLine();
                buffWriter.write("    </Private>");
                buffWriter.newLine();
            }
            buffWriter.write("   </Actions>");
            buffWriter.newLine();
            buffWriter.write("  </Init>");
            buffWriter.newLine();
            buffWriter.flush();
            buffWriter.close();
        }
        catch (IOException e) {
            System.out.println("Could not write to the file!");
            System.exit(1);
        }
    }

    private void storyInit () {
        try {
            fileWriter = new FileWriter(fileToCreate, true);
            buffWriter = new BufferedWriter((fileWriter));
            buffWriter.write("  <Story name=\"MainStory\">");
            buffWriter.newLine();
            buffWriter.write("   <Act name =\"MainAct\">");
            buffWriter.newLine();
            storyManeuvers(buffWriter);
            buffWriter.write("    <StartTrigger>");
            buffWriter.newLine();
            buffWriter.write("     <ConditionGroup>");
            buffWriter.newLine();
            buffWriter.write("      <Condition name=\"\" delay=\"0\" conditionEdge=\"rising\">");
            buffWriter.newLine();
            buffWriter.write("       <ByValueCondition>");
            buffWriter.newLine();
            buffWriter.write("        <SimulationTimeCondition value=\"0\" rule=\"greaterThan\"/>");
            buffWriter.newLine();
            buffWriter.write("       </ByValueCondition>");
            buffWriter.newLine();
            buffWriter.write("      </Condition>");
            buffWriter.newLine();
            buffWriter.write("     </ConditionGroup>");
            buffWriter.newLine();
            buffWriter.write("    </StartTrigger>");
            buffWriter.newLine();
            buffWriter.write("   </Act>");
            buffWriter.newLine();
            buffWriter.write("  </Story>");
            buffWriter.newLine();
            buffWriter.write("  <StopTrigger>");
            buffWriter.newLine();
            buffWriter.write("   <ConditionGroup>");
            buffWriter.newLine();
            buffWriter.write("    <Condition name=\"\" delay=\"0\" conditionEdge=\"rising\">");
            buffWriter.newLine();
            buffWriter.write("     <ByValueCondition>");
            buffWriter.newLine();
            buffWriter.write("      <SimulationTimeCondition value=\"30\" rule=\"greaterThan\"/>");
            buffWriter.newLine();
            buffWriter.write("     </ByValueCondition>");
            buffWriter.newLine();
            buffWriter.write("    </Condition>");
            buffWriter.newLine();
            buffWriter.write("   </ConditionGroup>");
            buffWriter.newLine();
            buffWriter.write("  </StopTrigger>");
            buffWriter.newLine();
            buffWriter.write(" </Storyboard>");
            buffWriter.newLine();
            buffWriter.write("</OpenSCENARIO>");
            buffWriter.flush();
            buffWriter.close();
        }

        catch (IOException e) {
            System.out.println("Could not write to the file!");
            System.exit(1);
        }
    }

    private void storyManeuvers(BufferedWriter buffWriter) {
        try {
            for (int i = 0; i < parseInput.getEntityNo(); i++) {
                buffWriter.write("    <ManeuverGroup name=\"Group" + i +"\" maximumExecutionCount=\"1\">");
                buffWriter.newLine();
                buffWriter.write("     <Actors selectTriggeringEntities=\"false\">");
                buffWriter.newLine();
                buffWriter.write("      <EntityRef entityRef=\"" + logLines.get(i).getAgentNo() + "\"/>");
                buffWriter.newLine();
                buffWriter.write("     </Actors>");
                buffWriter.newLine();
                buffWriter.write("     <Maneuver name=\"FollowWaypoints" + i + "\">");
                buffWriter.newLine();
                eventActions(buffWriter, logLines.get(i).getAgentNo());
                buffWriter.write("     </Maneuver>");
                buffWriter.newLine();
                buffWriter.write("    </ManeuverGroup>");
                buffWriter.newLine();
            }
        }
        catch (IOException e) {
            System.out.println("Could not write to the file!");
            System.exit(1);
        }
    }

    private void eventActions (BufferedWriter buffWriter, int AgentNumber) {
        try {
            int entityNumber = parseInput.getEntityNo();
            double avgSpeed;
            buffWriter.write("      <Event name =\"RouteEvent" + count + "\" priority=\"overwrite\">");
            buffWriter.newLine();
            buffWriter.write("       <Action name =\"Assign Route\">");
            buffWriter.newLine();
            buffWriter.write("        <PrivateAction>");
            buffWriter.newLine();
            buffWriter.write("         <RoutingAction>");
            buffWriter.newLine();
            buffWriter.write("          <AssignRouteAction>");
            buffWriter.newLine();
            buffWriter.write("           <Route name=\"Route 1\" closed = \"false\">");
            buffWriter.newLine();
            mapWayPoints(buffWriter, AgentNumber, AgentNumber - 1);
            buffWriter.write("           </Route>");
            buffWriter.newLine();
            buffWriter.write("          </AssignRouteAction>");
            buffWriter.newLine();
            buffWriter.write("         </RoutingAction>");
            buffWriter.newLine();
            buffWriter.write("        </PrivateAction>");
            buffWriter.newLine();
            buffWriter.write("       </Action>");
            buffWriter.newLine();
            buffWriter.write("       <StartTrigger>");
            buffWriter.newLine();
            buffWriter.write("        <ConditionGroup>");
            buffWriter.newLine();
            buffWriter.write("         <Condition name=\"\" delay=\"0\" conditionEdge=\"rising\">");
            buffWriter.newLine();
            buffWriter.write("          <ByValueCondition>");
            buffWriter.newLine();
            buffWriter.write("           <SimulationTimeCondition value=\"0\" rule=\"greaterThan\"/>");
            buffWriter.newLine();
            buffWriter.write("          </ByValueCondition>");
            buffWriter.newLine();
            buffWriter.write("         </Condition>");
            buffWriter.newLine();
            buffWriter.write("        </ConditionGroup>");
            buffWriter.newLine();
            buffWriter.write("       </StartTrigger>");
            buffWriter.newLine();
            buffWriter.write("      </Event>");
            buffWriter.newLine();
            setSpeedEvents(buffWriter, AgentNumber);
        }
        catch (IOException e) {
            System.out.println("Could not write to the file!");
            System.exit(1);
        }
    }

    private void setSpeedEvents(BufferedWriter buffWriter, int AgentNumber) {
        try {
            for (int i = 0; i < velocities.size(); i++, count++) {
                buffWriter.write("      <Event name =\"RouteEvent" + count + "\" priority=\"parallel\">");
                buffWriter.newLine();
                buffWriter.write("       <Action name =\"ActionSpeed" + count + "\">");
                buffWriter.newLine();
                buffWriter.write("        <PrivateAction>");
                buffWriter.newLine();
                buffWriter.write("         <LongitudinalAction>");
                buffWriter.newLine();
                buffWriter.write("          <SpeedAction>");
                buffWriter.newLine();
                buffWriter.write("           <SpeedActionDynamics dynamicsShape=\"step\" value=\"0\" dynamicsDimension=\"time\"/>");
                buffWriter.newLine();
                buffWriter.write("           <SpeedActionTarget>");
                buffWriter.newLine();
                buffWriter.write("            <AbsoluteTargetSpeed value=\"" + velocities.get(i) + "\"/>");
                buffWriter.newLine();
                buffWriter.write("           </SpeedActionTarget>");
                buffWriter.newLine();
                buffWriter.write("          </SpeedAction>");
                buffWriter.newLine();
                buffWriter.write("         </LongitudinalAction>");
                buffWriter.newLine();
                buffWriter.write("        </PrivateAction>");
                buffWriter.newLine();
                buffWriter.write("       </Action>");

                buffWriter.newLine();
                buffWriter.write("       <StartTrigger>");
                buffWriter.newLine();
                buffWriter.write("        <ConditionGroup>");
                buffWriter.newLine();
                buffWriter.write("         <Condition name=\"\" delay=\"0\" conditionEdge=\"none\">");
                buffWriter.newLine();
                buffWriter.write("          <ByEntityCondition>");
                buffWriter.newLine();
                buffWriter.write("           <TriggeringEntities triggeringEntitiesRule=\"any\">");
                buffWriter.newLine();
                buffWriter.write("            <EntityRef entityRef=\"" + AgentNumber + "\"/>");
                buffWriter.newLine();
                buffWriter.write("           </TriggeringEntities>");
                buffWriter.newLine();
                buffWriter.write("           <EntityCondition>");
                buffWriter.newLine();
                buffWriter.write("            <ReachPositionCondition tolerance=\"2\">");
                buffWriter.newLine();
                buffWriter.write("             <Position>");
                buffWriter.newLine();
                buffWriter.write("              <WorldPosition x=\"" + wayPoints.get(i).getX() + "\" y=\"" + wayPoints.get(i).getY() + "\" z=\"" + wayPoints.get(i).getZ() + "\"/>");
                buffWriter.newLine();
                buffWriter.write("             </Position>");
                buffWriter.newLine();
                buffWriter.write("            </ReachPositionCondition>");
                buffWriter.newLine();
                buffWriter.write("           </EntityCondition>");
                buffWriter.newLine();
                buffWriter.write("          </ByEntityCondition>");
                buffWriter.newLine();
                buffWriter.write("         </Condition>");
                buffWriter.newLine();
                buffWriter.write("        </ConditionGroup>");
                buffWriter.newLine();
                buffWriter.write("       </StartTrigger>");
                buffWriter.newLine();
                buffWriter.write("      </Event>");
                buffWriter.newLine();
            }
            velocities.clear();
            wayPoints.clear();
        }
        catch (IOException e) {
            System.out.println("Cannot write velocities to the file!");
        }
    }

    private void mapWayPoints(BufferedWriter buffWriter, int AgentNumber, int sourceNumber) {
        try {
            int entityNumber = parseInput.getEntityNo();
            wayPoints.add(logLines.get(AgentNumber-1));
            for (int i = AgentNumber-1; i < logLines.size()-1 - entityNumber; i = i + entityNumber) {
                if (calculateAverage(logLines.get(sourceNumber), logLines.get(i), "Distance") >= lookahead) {
                    velocities.add(calculateAverage(logLines.get(sourceNumber),logLines.get(i), "Speed"));
                    wayPoints.add(logLines.get(i));
                    sourceNumber = i;
                    buffWriter.write("            <Waypoint routeStrategy=\"shortest\">");
                    buffWriter.newLine();
                    buffWriter.write("             <Position>");
                    buffWriter.newLine();
                    buffWriter.write("              <WorldPosition x=\"" + logLines.get(i).getX() + "\" y=\"" + logLines.get(i).getY() + "\" z=\"" + logLines.get(i).getZ() + "\" h=\"" + logLines.get(i).getYaw() + "\"/>");
                    buffWriter.newLine();
                    buffWriter.write("             </Position>");
                    buffWriter.newLine();
                    buffWriter.write("            </Waypoint>");
                    buffWriter.newLine();
                }
            }
            count++;
        }
        catch (IOException e) {
            System.out.println("Could not write waypoints to the file!");
        }
    }

    private double calculateAverage(logLine source, logLine destination, String Option) {
        double xDiff = source.getX() - destination.getX();
        double yDiff = source.getY() - destination.getY();
        double zDiff = source.getZ() - destination.getZ();
        double distanceChange = Math.sqrt((zDiff*zDiff) + (yDiff*yDiff) + (xDiff*xDiff));
        if (Option.equals("Distance")) {
            return distanceChange;
        }
        double timeDiff = destination.getSimTime() - source.getSimTime();
        double avgSpeed = distanceChange/timeDiff;
        return avgSpeed;
    }


}
