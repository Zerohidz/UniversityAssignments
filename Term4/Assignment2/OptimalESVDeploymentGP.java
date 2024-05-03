import java.util.ArrayList;
import java.util.Collections;

/**
 * This class accomplishes Mission Eco-Maintenance
 */
public class OptimalESVDeploymentGP {
    private ArrayList<Integer> maintenanceTaskEnergyDemands;

    /*
     * Should include tasks assigned to ESVs.
     * For the sample input:
     * 8 100
     * 20 50 40 70 10 30 80 100 10
     * 
     * The list should look like this:
     * [[100], [80, 20], [70, 30], [50, 40, 10], [10]]
     * 
     * It is expected to be filled after getMinNumESVsToDeploy() is called.
     */
    private ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = new ArrayList<>();

    ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {
        return maintenanceTasksAssignedToESVs;
    }

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
    }

    public ArrayList<Integer> getMaintenanceTaskEnergyDemands() {
        return maintenanceTaskEnergyDemands;
    }

    /**
     *
     * @param maxNumberOfAvailableESVs the maximum number of available ESVs to be
     *                                 deployed
     * @param maxESVCapacity           the maximum capacity of ESVs
     * @return the minimum number of ESVs required using first fit approach over
     *         reversely sorted items.
     *         Must return -1 if all tasks can't be satisfied by the available ESVs
     */
    public int getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) {
        maintenanceTasksAssignedToESVs.clear();
        Collections.sort(maintenanceTaskEnergyDemands, Collections.reverseOrder());
        ArrayList<Integer> usedESVs = new ArrayList<>();

        for (int i = 0; i < maintenanceTaskEnergyDemands.size(); i++) {
            int taskCost = maintenanceTaskEnergyDemands.get(i);
            if (taskCost > maxESVCapacity) {
                maintenanceTasksAssignedToESVs.clear();
                return -1;
            }

            boolean foundCapableESV = false;
            for (int j = 0; j < usedESVs.size(); j++) {
                if (usedESVs.get(j) >= taskCost) {
                    usedESVs.set(j, usedESVs.get(j) - taskCost);
                    maintenanceTasksAssignedToESVs.get(j).add(taskCost);
                    foundCapableESV = true;
                    break;
                }
            }

            if (!foundCapableESV) {
                usedESVs.add(maxESVCapacity - taskCost);
                ArrayList<Integer> tasks = new ArrayList<Integer>();
                tasks.add(taskCost);
                maintenanceTasksAssignedToESVs.add(tasks);
            }
        }

        if (0 < usedESVs.size() && usedESVs.size() <= maxNumberOfAvailableESVs) {
            return usedESVs.size();
        } else {
            maintenanceTasksAssignedToESVs.clear();
            return -1;
        }
    }

    public void printReport() {
        System.out.println("The minimum number of ESVs to deploy: " + maintenanceTasksAssignedToESVs.size());
        for (int i = 0; i < maintenanceTasksAssignedToESVs.size(); i++) {
            System.out.println("ESV " + (i + 1) + " tasks: " + maintenanceTasksAssignedToESVs.get(i));
        }
    }

    public void printMissonFailed() {
        System.out.println("Warning: Mission Eco-Maintenance Failed.");
    }

}
