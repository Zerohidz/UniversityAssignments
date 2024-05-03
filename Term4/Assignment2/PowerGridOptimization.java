import java.util.ArrayList;
import java.util.List;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour) {
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }

    /**
     * Function to implement the given dynamic programming algorithm
     * SOL(0) <- 0
     * HOURS(0) <- [ ]
     * For{j <- 1...N}
     * SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     * HOURS(j) <- [HOURS(i), j]
     * EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP() {
        ArrayList<Integer> solutions = new ArrayList<Integer>();
        solutions.add(0);
        ArrayList<ArrayList<Integer>> hours = new ArrayList<ArrayList<Integer>>();
        hours.add(new ArrayList<Integer>());

        for (int j = 1; j <= amountOfEnergyDemandsArrivingPerHour.size(); j++) {
            int optimumSolutionForJ = Integer.MIN_VALUE;
            int solutionIndex = 0;
            for (int i = 0; i < j; i++) {
                int solutionForThisChoice = solutions.get(i)
                        + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), (j - i) * (j - i));
                if (solutionForThisChoice > optimumSolutionForJ) {
                    optimumSolutionForJ = solutionForThisChoice;
                    solutionIndex = i;
                }
            }
            solutions.add(optimumSolutionForJ);

            ArrayList<Integer> temp = new ArrayList<Integer>();
            temp.addAll(hours.get(solutionIndex));
            temp.add(j);
            hours.add(temp);
        }

        return new OptimalPowerGridSolution(
                amountOfEnergyDemandsArrivingPerHour.stream().mapToInt(Integer::intValue).sum(),
                solutions.get(solutions.size() - 1),
                hours.get(hours.size() - 1));
    }
}
