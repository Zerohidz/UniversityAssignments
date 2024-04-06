#ifndef SPACESECTORLLRBT_H
#define SPACESECTORLLRBT_H

#include "Sector.h"
#include <iostream>
#include <fstream>  
#include <sstream>
#include <vector>

class SpaceSectorLLRBT {
public:
    Sector* root;
    SpaceSectorLLRBT();
    ~SpaceSectorLLRBT();
    void deleteSubTree(Sector* current_root);
    void readSectorsFromFile(const std::string& filename);
    void insertSectorByCoordinates(int x, int y, int z);
    void insertNewSector(Sector* current_root, Sector* new_sector);
    Sector* balance(Sector* sector);
    void flipColors(Sector* sector);
    Sector* rotateLeft(Sector* old_root);
    Sector* rotateRight(Sector* old_root);
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

#endif // SPACESECTORLLRBT_H
