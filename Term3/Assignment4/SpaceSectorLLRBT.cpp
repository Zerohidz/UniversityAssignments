#include "SpaceSectorLLRBT.h"
#include "SpaceSectorBST.h"
#include <algorithm>

using namespace std;

SpaceSectorLLRBT::SpaceSectorLLRBT() : root(nullptr) {}

void SpaceSectorLLRBT::readSectorsFromFile(const std::string& filename)
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

SpaceSectorLLRBT::~SpaceSectorLLRBT()
{
	deleteSubTree(root);
}

void SpaceSectorLLRBT::deleteSubTree(Sector* current_root)
{
	if (current_root == nullptr)
		return;

	deleteSubTree(current_root->left);
	deleteSubTree(current_root->right);
	delete current_root;
}

void SpaceSectorLLRBT::insertSectorByCoordinates(int x, int y, int z)
{
	Sector* new_sector = new Sector(x, y, z);
	if (root == nullptr)
		root = new_sector;
	else
		insertNewSector(root, new_sector);

	root = balance(root);
	root->color = BLACK;
}

void SpaceSectorLLRBT::insertNewSector(Sector* current_root, Sector* new_sector)
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
		new_sector->parent = current_root;
		current_root->right = new_sector;
	}
}

Sector* SpaceSectorLLRBT::balance(Sector* sector)
{
	if (sector == nullptr)
		return nullptr;

	sector->left = balance(sector->left);
	sector->right = balance(sector->right);

	if (sector->right != nullptr && sector->right->color == RED)
		sector = rotateLeft(sector);
	if (sector->left != nullptr && sector->left->color == RED && sector->left->left != nullptr && sector->left->left->color == RED)
		sector = rotateRight(sector);
	if (sector->left != nullptr && sector->left->color == RED && sector->right != nullptr && sector->right->color == RED)
		flipColors(sector);

	return sector;
}

void SpaceSectorLLRBT::flipColors(Sector* sector)
{
	sector->color = RED;
	sector->left->color = BLACK;
	sector->right->color = BLACK;
}

Sector* SpaceSectorLLRBT::rotateLeft(Sector* old_root)
{
	Sector* outer_parent = old_root->parent;
	Sector* new_root = old_root->right;
	old_root->right = new_root->left;
	if (new_root->left != nullptr) new_root->left->parent = old_root;

	new_root->left = old_root;
	old_root->parent = new_root;

	new_root->parent = outer_parent;
	if (outer_parent != nullptr)
	{
		if (outer_parent->left == old_root)
			outer_parent->left = new_root;
		else
			outer_parent->right = new_root;
	}

	new_root->color = old_root->color;
	old_root->color = RED;

	return new_root;
}

Sector* SpaceSectorLLRBT::rotateRight(Sector* old_root)
{
	Sector* outer_parent = old_root->parent;
	Sector* new_root = old_root->left;
	old_root->left = new_root->right;
	if (new_root->right != nullptr) new_root->right->parent = old_root;

	new_root->right = old_root;
	old_root->parent = new_root;

	new_root->parent = outer_parent;
	if (outer_parent != nullptr)
	{
		if (outer_parent->left == old_root)
			outer_parent->left = new_root;
		else
			outer_parent->right = new_root;
	}

	new_root->color = old_root->color;
	old_root->color = RED;

	return new_root;
}

void SpaceSectorLLRBT::displaySectorsInOrder()
{
	cout << "Space sectors inorder traversal:" << endl;

	displaySectorsInOrderRecursive(root);
	cout << endl;
}

void SpaceSectorLLRBT::displaySectorsInOrderRecursive(Sector* sector)
{
	if (sector == nullptr)
		return;

	displaySectorsInOrderRecursive(sector->left);
	cout << (sector->color == RED ? "RED" : "BLACK") << " sector: " << sector->sector_code << endl;
	displaySectorsInOrderRecursive(sector->right);
}

void SpaceSectorLLRBT::displaySectorsPreOrder()
{
	cout << "Space sectors preorder traversal:" << endl;

	displaySectorsPreOrderRecursive(root);
	cout << endl;
}

void SpaceSectorLLRBT::displaySectorsPreOrderRecursive(Sector* sector)
{
	if (sector == nullptr)
		return;

	cout << (sector->color == RED ? "RED" : "BLACK") << " sector: " << sector->sector_code << endl;
	displaySectorsPreOrderRecursive(sector->left);
	displaySectorsPreOrderRecursive(sector->right);
}

void SpaceSectorLLRBT::displaySectorsPostOrder()
{
	cout << "Space sectors postorder traversal:" << endl;

	displaySectorsPostOrderRecursive(root);
	cout << endl;
}

void SpaceSectorLLRBT::displaySectorsPostOrderRecursive(Sector* sector)
{
	if (sector == nullptr)
		return;

	displaySectorsPostOrderRecursive(sector->left);
	displaySectorsPostOrderRecursive(sector->right);
	cout << (sector->color == RED ? "RED" : "BLACK") << " sector: " << sector->sector_code << endl;
}

std::vector<Sector*> SpaceSectorLLRBT::getStellarPath(const std::string& sector_code)
{
	std::vector<Sector*> path;

	auto path_to_destination = getStellarPath(root, sector_code);
	if (path_to_destination.empty())
		return path;
	auto path_to_earth = getStellarPath(root, "0SSS");

	std::reverse(path_to_destination.begin(), path_to_destination.end());
	std::reverse(path_to_earth.begin(), path_to_earth.end());

	while (path_to_earth.size() > 0)
	{
		path.push_back(path_to_earth.back());

		auto it = std::find(path_to_destination.begin(), path_to_destination.end(), path_to_earth.back());
		if (it == path_to_destination.end())
		{
			path_to_earth.pop_back();
			continue;
		}

		while (++it != path_to_destination.end())
		{
			path.push_back(*it);
		}

		break;
	}

	return path;
}

std::vector<Sector*> SpaceSectorLLRBT::getStellarPath(Sector* sector, const std::string& sector_code)
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

void SpaceSectorLLRBT::printStellarPath(const std::vector<Sector*>& path)
{
	if (path.size() < 1)
	{
		cout << "A path to Dr. Elara could not be found." << endl;
		cout << endl << endl << endl;
		return;
	}

	std::cout << "The stellar path to Dr. Elara: ";

	for (const auto sector : path)
	{
		std::cout << sector->sector_code;
		if (sector != path.back())
			std::cout << "->";
	}

	cout << endl << endl << endl << endl;
}