import java.util.*;
import java.util.stream.Collectors;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules;
    private final Dictionary<String, Molecule> moleculeDictionary;
    private Dictionary<String, Integer> structureIds;
    private HashSet<String> isMarked;
    private int currentStructureId;

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
        moleculeDictionary = new Hashtable<>();
        for (Molecule molecule : molecules) {
            moleculeDictionary.put(molecule.getId(), molecule);
        }

        for (Molecule molecule : molecules) {
            for (String bond : molecule.getBonds()) {
                if (!moleculeDictionary.get(bond).getBonds().contains(molecule.getId())) {
                    moleculeDictionary.get(bond).addBond(molecule.getId());
                }
            }
        }
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Getter for molecule by ID
    public Molecule getMoleculeById(String id) {
        return moleculeDictionary.get(id);
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input
    // data
    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();

        findConnectedComponents();

        List<String> moleculeIds = molecules.stream().map(Molecule::getId).collect(Collectors.toList());
        for (int i = 0; i < currentStructureId; i++) {
            MolecularStructure structure = new MolecularStructure();
            for (String moleculeId : moleculeIds) {
                if (structureIds.get(moleculeId) == i) {
                    structure.addMolecule(getMoleculeById(moleculeId));
                }
            }
            structures.add(structure);
        }

        return structures;
    }

    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out
                .println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        for (int i = 0; i < molecularStructures.size(); i++) {
            System.out.println("Molecules in Molecular Structure " + (i + 1) + ": " + molecularStructures.get(i));
        }
    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures,
            List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();

        for (MolecularStructure targetStructure : targetStructures) {
            boolean isAnomaly = true;
            for (MolecularStructure sourceStructure : sourceStructures) {
                if (sourceStructure.equals(targetStructure)) {
                    isAnomaly = false;
                    break;
                }
            }
            if (isAnomaly) {
                anomalyList.add(targetStructure);
            }
        }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure structure : molecularStructures) {
            System.out.println(structure);
        }

    }

    public void findConnectedComponents() {
        isMarked = new HashSet<String>();
        structureIds = new Hashtable<String, Integer>();
        for (int i = 0; i < molecules.size(); i++) {
            String moleculeId = molecules.get(i).getId();
            if (!isMarked.contains(moleculeId)) {
                dfs(moleculeId);
                currentStructureId++;
            }
        }
    }

    private void dfs(String v) {
        isMarked.add(v);
        structureIds.put(v, currentStructureId);
        for (String w : moleculeDictionary.get(v).getBonds())
            if (!isMarked.contains(w))
                dfs(w);
    }
}
