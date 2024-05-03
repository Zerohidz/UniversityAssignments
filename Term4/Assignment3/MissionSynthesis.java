import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans
    private final List<String> humanMolecules;
    private final List<String> diffMolecules;

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
        this.humanMolecules = new ArrayList<>();
        this.diffMolecules = new ArrayList<>();
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        List<Molecule> moleculesToBound = new ArrayList<>();
        for (MolecularStructure structure : humanStructures) {
            Molecule weakestMolecule = structure.getMoleculeWithWeakestBondStrength();
            moleculesToBound.add(weakestMolecule);
            humanMolecules.add(weakestMolecule.getId());
        }
        for (MolecularStructure structure : diffStructures) {
            Molecule weakestMolecule = structure.getMoleculeWithWeakestBondStrength();
            moleculesToBound.add(weakestMolecule);
            diffMolecules.add(weakestMolecule.getId());
        }

        List<Bond> mst = constructMST(moleculesToBound.toArray(new Molecule[0]));
        serum.addAll(mst);

        return serum;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {
        System.out.println("Typical human molecules selected for synthesis: " + humanMolecules);
        System.out.println("Vitales molecules selected for synthesis: " + diffMolecules);
        System.out.println("Synthesizing the serum...");
        double totalWeight = 0;
        for (Bond bond : serum) {
            String id1 = bond.getFrom().getId();
            String id2 = bond.getTo().getId();

            if (Integer.parseInt(id1.substring(1)) < Integer.parseInt(id2.substring(1))) {
                System.out.println("Forming a bond between " + id1 + " - " + id2
                        + " with strength " + String.format("%.2f", bond.getWeight()));
            } else {
                System.out.println("Forming a bond between " + id2 + " - " + id1
                        + " with strength " + String.format("%.2f", bond.getWeight()));
            }

            totalWeight += bond.getWeight();
        }
        System.out.println("The total serum bond strength is " + String.format("%.2f", totalWeight));
    }

    private static List<Bond> constructMST(Molecule[] moleculesToBound) {
        List<Bond> mst = new ArrayList<>();

        int[] parent = new int[moleculesToBound.length];
        double[] weight = new double[moleculesToBound.length];
        boolean[] inMST = new boolean[moleculesToBound.length];

        final int START = 0;
        inMST[START] = true;
        for (int i = 0; i < moleculesToBound.length; i++) {
            if (i == START)
                continue;
            double w = getDistance(moleculesToBound[START], moleculesToBound[i]);
            weight[i] = w;
            parent[i] = START;
        }

        while (mst.size() < moleculesToBound.length - 1) {
            int minIndex = -1;
            double minWeight = Float.MAX_VALUE;
            for (int i = 0; i < moleculesToBound.length; i++) {
                if (!inMST[i] && weight[i] < minWeight) {
                    minIndex = i;
                    minWeight = weight[i];
                }
            }

            inMST[minIndex] = true;
            mst.add(new Bond(moleculesToBound[parent[minIndex]], moleculesToBound[minIndex], weight[minIndex]));

            for (int i = 0; i < moleculesToBound.length; i++) {
                if (!inMST[i]) {
                    double w = getDistance(moleculesToBound[minIndex], moleculesToBound[i]);
                    if (w < weight[i]) {
                        weight[i] = w;
                        parent[i] = minIndex;
                    }
                }
            }
        }

        return mst;
    }

    private static double getDistance(Molecule m1, Molecule m2) {
        return (m1.getBondStrength() + m2.getBondStrength()) / 2.0;
    }
}
