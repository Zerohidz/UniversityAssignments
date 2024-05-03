
// Class representing a bond between two molecules
public class Bond {
    
    // Private fields
    private Molecule to; // Molecule to which the bond is directed
    private Molecule from; // Molecule from which the bond originates
    private Double weight; // Weight or strength of the bond

    // Constructor
    public Bond(Molecule to, Molecule from, Double weight) {
        this.to = to;
        this.from = from;
        this.weight = weight;
    }

    // Getter for 'to' molecule
    public Molecule getTo() {
        return to;
    }

    // Getter for 'from' molecule
    public Molecule getFrom() {
        return from;
    }

    // Getter for bond weight
    public Double getWeight() {
        return weight;
    }
}
