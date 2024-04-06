#include "SpaceSectorBST.h"
#include <fstream>
#include <istream>
#include <algorithm>

using namespace std;

SpaceSectorBST::SpaceSectorBST() : root(nullptr) {}

SpaceSectorBST::~SpaceSectorBST()
{
	deleteSubTree(root);
}

void SpaceSectorBST::deleteSubTree(Sector* current_root)
{
	if (current_root == nullptr)
		return;

	deleteSubTree(current_root->left);
	deleteSubTree(current_root->right);
	delete current_root;
}

void SpaceSectorBST::readSectorsFromFile(const std::string& filename)
{
	fstream file;
	file.open(filename);
	if (!file.is_open())
		throw std::runtime_error("Could not open file");

	string header;
	getline(file, header);
	while (!file.eof())
	{
		string x, y, z;
		getline(file, x, ',');
		getline(file, y, ',');
		getline(file, z);

		if (x.empty() || y.empty() || z.empty())
			break;

		insertSectorByCoordinates(stoi(x), stoi(y), stoi(z));
	}
}

void SpaceSectorBST::insertSectorByCoordinates(int x, int y, int z)
{
	Sector* new_sector = new Sector(x, y, z);
	if (root == nullptr)
	{
		root = new_sector;
		return;
	}
	insertNewSector(root, new_sector);
}

void SpaceSectorBST::insertNewSector(Sector* current_root, Sector* new_sector)
{
	if (new_sector->x < current_root->x)
	{
		if (current_root->left != nullptr)
			insertNewSector(current_root->left, new_sector);
		else
		{
			new_sector->parent = current_root;
			current_root->left = new_sector;
		}
	}
	else if (new_sector->x > current_root->x)
	{
		if (current_root->right != nullptr)
			insertNewSector(current_root->right, new_sector);
		else
		{
			new_sector->parent = current_root;
			current_root->right = new_sector;
		}
	}
	else if (new_sector->y < current_root->y)
	{
		if (current_root->left != nullptr)
			insertNewSector(current_root->left, new_sector);
		else
		{
			new_sector->parent = current_root;
			current_root->left = new_sector;
		}
	}
	else if (new_sector->y > current_root->y)
	{
		if (current_root->right != nullptr)
			insertNewSector(current_root->right, new_sector);
		else
		{
			new_sector->parent = current_root;
			current_root->right = new_sector;
		}
	}
	else if (new_sector->z < current_root->z)
	{
		if (current_root->left != nullptr)
			(current_root->left, new_sector);
		else
		{
			new_sector->parent = current_root;
			current_root->left = new_sector;
		}
	}
	else if (new_sector->z > current_root->z)
	{
		if (current_root->right != nullptr)
			insertNewSector(current_root->right, new_sector);
		else
		{
			new_sector->parent = current_root;
			current_root->right = new_sector;
		}
	}
	else
	{
		cout << "Sector already exists" << endl;
	}
}

void SpaceSectorBST::deleteSector(const std::string& sector_code)
{
	root = deleteSector(root, sector_code);
}

Sector* SpaceSectorBST::deleteSector(Sector* current_sector, const std::string& sector_code)
{
	// Preorder traversel check
	if (current_sector == nullptr)
		return nullptr;

	if (current_sector->sector_code == sector_code)
	{
		if (current_sector->left == nullptr && current_sector->right == nullptr)
		{
			delete current_sector;
			current_sector = nullptr;
		}
		else if (current_sector->right == nullptr)
		{
			Sector* left = current_sector->left;
			delete current_sector;
			current_sector = left;
		}
		else if (current_sector->left == nullptr)
		{
			Sector* right = current_sector->right;
			delete current_sector;
			current_sector = right;
		}
		else
		{
			Sector* temp = current_sector->right;

			while (temp->left != nullptr)
				temp = temp->left;

			current_sector->operator=(*temp);

			current_sector->right = deleteSector(current_sector->right, temp->sector_code);
		}
	}
	else
	{
		current_sector->left = deleteSector(current_sector->left, sector_code);
		current_sector->right = deleteSector(current_sector->right, sector_code);
	}

	return current_sector;
}

void SpaceSectorBST::displaySectorsInOrder()
{
	cout << "Space sectors inorder traversal:" << endl;

	displaySectorsInOrderRecursive(root);
	cout << endl;
}

void SpaceSectorBST::displaySectorsInOrderRecursive(Sector* sector)
{
	if (sector == nullptr)
		return;

	displaySectorsInOrderRecursive(sector->left);
	cout << sector->sector_code << endl;
	displaySectorsInOrderRecursive(sector->right);
}

void SpaceSectorBST::displaySectorsPreOrder()
{
	cout << "Space sectors preorder traversal:" << endl;

	displaySectorsPreOrderRecursive(root);
	cout << endl;
}

void SpaceSectorBST::displaySectorsPreOrderRecursive(Sector* sector)
{
	if (sector == nullptr)
		return;

	cout << sector->sector_code << endl;
	displaySectorsPreOrderRecursive(sector->left);
	displaySectorsPreOrderRecursive(sector->right);
}

void SpaceSectorBST::displaySectorsPostOrder()
{
	cout << "Space sectors postorder traversal:" << endl;

	displaySectorsPostOrderRecursive(root);
	cout << endl;
}

void SpaceSectorBST::displaySectorsPostOrderRecursive(Sector* sector)
{
	if (sector == nullptr)
		return;

	displaySectorsPostOrderRecursive(sector->left);
	displaySectorsPostOrderRecursive(sector->right);
	cout << sector->sector_code << endl;
}

std::vector<Sector*> SpaceSectorBST::getStellarPath(const std::string& sector_code)
{
	std::vector<Sector*> path = getStellarPath(root, sector_code);
	std::reverse(path.begin(), path.end());

	return path;
}

std::vector<Sector*> SpaceSectorBST::getStellarPath(Sector* sector, const std::string& sector_code)
{
	if (sector == nullptr)
		return vector<Sector*>();
	if (sector->sector_code == sector_code)
		return vector<Sector*>(1, sector);

	auto left = getStellarPath(sector->left, sector_code);
	auto right = getStellarPath(sector->right, sector_code);

	if (left.size() > 0)
	{
		left.push_back(sector);
		return left;
	}
	else if (right.size() > 0)
	{
		right.push_back(sector);
		return right;
	}
	else
	{
		return vector<Sector*>();
	}
}

void SpaceSectorBST::printStellarPath(const std::vector<Sector*>& path)
{
	if (path.size() < 1)
	{
		cout << "A path to Dr. Elara could not be found." << endl;
		return;
	}

	cout << "The stellar path to Dr. Elara: ";

	vector<Sector*> temp_path = path;
	std::reverse(temp_path.begin(), temp_path.end());

	while (!temp_path.empty())
	{
		Sector* sector = temp_path.back();
		temp_path.pop_back();

		cout << sector->sector_code;

		if (!temp_path.empty())
			cout << "->";
	}
	cout << endl;
}