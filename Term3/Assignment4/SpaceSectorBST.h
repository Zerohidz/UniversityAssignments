#ifndef SPACESECTORBST_H
#define SPACESECTORBST_H

#include <iostream>
#include <fstream>  
#include <sstream>
#include <vector>

#include "Sector.h"

class SpaceSectorBST
{

public:
	Sector* root;
	SpaceSectorBST();
	~SpaceSectorBST();
	void deleteSubTree(Sector* current_root);
	void readSectorsFromFile(const std::string& filename);
	void insertSectorByCoordinates(int x, int y, int z);
	void insertNewSector(Sector* current_root, Sector* new_sector);
	void deleteSector(const std::string& sector_code);
	Sector* deleteSector(Sector* current_sector, const std::string& sector_code);
	void displaySectorsInOrder();
	void displaySectorsInOrderRecursive(Sector* sector);
	void displaySectorsPreOrder();
	void displaySectorsPreOrderRecursive(Sector* sector);
	void displaySectorsPostOrder();
	void displaySectorsPostOrderRecursive(Sector* sector);
	std::vector<Sector*> getStellarPath(const std::string& sector_code);
	std::vector<Sector*> getStellarPath(Sector* sector, const std::string& sector_code);
	void printStellarPath(const std::vector<Sector*>& path);
};

#endif // SPACESECTORBST_H
