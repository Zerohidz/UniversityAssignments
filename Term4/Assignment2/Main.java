import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {
        /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
            String line = reader.readLine();
            reader.close();

            ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour = new ArrayList<Integer>();
            String[] energyDemands = line.split(" ");
            for (String energyDemand : energyDemands) {
                amountOfEnergyDemandsArrivingPerHour.add(Integer.parseInt(energyDemand));
            }
            PowerGridOptimization powerGridOptimization = new PowerGridOptimization(
                    amountOfEnergyDemandsArrivingPerHour);
            OptimalPowerGridSolution solution = powerGridOptimization.getOptimalPowerGridSolutionDP();
            solution.printSolution();
        } catch (Exception e) {
            System.out.println("Misson Failed");
            System.out.println(e.getMessage());
        }
        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");
        
        BufferedReader reader = new BufferedReader(new FileReader(new File(args[1])));
        String line0 = reader.readLine();
        String line1 = reader.readLine();
        reader.close();

        String[] line0Parts = line0.split(" ");
        int maxNumberOfAvailableESVs = Integer.parseInt(line0Parts[0]);
        int maxESVCapacity = Integer.parseInt(line0Parts[1]);

        ArrayList<Integer> maintenanceTaskEnergyDemands = new ArrayList<Integer>();
        String[] energyDemands = line1.split(" ");
        for (String energyDemand : energyDemands) {
            maintenanceTaskEnergyDemands.add(Integer.parseInt(energyDemand));
        }

        OptimalESVDeploymentGP optimalESVDeploymentGP = new OptimalESVDeploymentGP(maintenanceTaskEnergyDemands);
        int minESVCount = optimalESVDeploymentGP.getMinNumESVsToDeploy(maxNumberOfAvailableESVs,
                maxESVCapacity);
        if (minESVCount > 0)
            optimalESVDeploymentGP.printReport();
        else
            optimalESVDeploymentGP.printMissonFailed();
        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
}
