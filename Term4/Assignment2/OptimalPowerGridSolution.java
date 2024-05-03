import java.util.ArrayList;

/**
 * A class that represents the optimal solution for the Power Grid optimization scenario (Dynamic Programming)
 */

public class OptimalPowerGridSolution {
    private int maxNumberOfSatisfiedDemands;
    private ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency;
    private int totalDemandCount;

    public OptimalPowerGridSolution(int totalDemandSum,int maxNumberOfSatisfiedDemands, ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency) {
        this.totalDemandCount = totalDemandSum;
        this.maxNumberOfSatisfiedDemands = maxNumberOfSatisfiedDemands;
        this.hoursToDischargeBatteriesForMaxEfficiency = hoursToDischargeBatteriesForMaxEfficiency;
    }

    public OptimalPowerGridSolution() {

    }

    public int getmaxNumberOfSatisfiedDemands() {
        return maxNumberOfSatisfiedDemands;
    }

    public ArrayList<Integer> getHoursToDischargeBatteriesForMaxEfficiency() {
        return hoursToDischargeBatteriesForMaxEfficiency;
    }

    public void printSolution() {
        System.out.println("The total number of demanded gigawatts: " + (totalDemandCount));
        System.out.println("Maximum number of satisfied gigawatts: " + maxNumberOfSatisfiedDemands);
        System.out.print("Hours at which the battery bank should be discharged: ");
        for (int i = 0; i < hoursToDischargeBatteriesForMaxEfficiency.size(); i++) {
            System.out.print(hoursToDischargeBatteriesForMaxEfficiency.get(i));
            if (i != hoursToDischargeBatteriesForMaxEfficiency.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
        System.out.println("The number of unsatisfied gigawatts: " + (totalDemandCount - maxNumberOfSatisfiedDemands));
    }

}
