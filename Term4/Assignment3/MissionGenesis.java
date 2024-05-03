// Class representing the mission of Genesis

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales
    // fields once called
    public void readXML(String filename) {
        try {
            Document doc = readXMLAsDoc(filename);

            Node humanMolecularData = doc.getElementsByTagName("HumanMolecularData").item(0);
            Node vitalesMolecularData = doc.getElementsByTagName("VitalesMolecularData").item(0);

            molecularDataHuman = parseMolecularData(humanMolecularData);
            molecularDataVitales = parseMolecularData(vitalesMolecularData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document readXMLAsDoc(String filename) throws Exception {
        File file = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        return doc;
    }

    private MolecularData parseMolecularData(Node molecularDataNode) {
        if (molecularDataNode.getNodeType() != Node.ELEMENT_NODE)
            return null;

        ArrayList<Molecule> molecules = new ArrayList<>();

        Element molecularDataElement = (Element) molecularDataNode;
        NodeList moleculeNodes = molecularDataElement.getElementsByTagName("Molecule");

        for (int i = 0; i < moleculeNodes.getLength(); i++) {
            Node moleculeNode = moleculeNodes.item(i);

            if (moleculeNode.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element moleculeElement = (Element) moleculeNode;

            String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
            int bondStrength = Integer
                    .parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());
            NodeList bondNodes = moleculeElement.getElementsByTagName("MoleculeID");

            ArrayList<String> bonds = new ArrayList<>();
            for (int j = 0; j < bondNodes.getLength(); j++) {
                bonds.add(bondNodes.item(j).getTextContent());
            }

            Molecule molecule = new Molecule(id, bondStrength, bonds);
            molecules.add(molecule);
        }

        return new MolecularData(molecules);
    }
}
