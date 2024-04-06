#ifndef PA2_BLOCK_H
#define PA2_BLOCK_H

#include <vector>
#include <iostream>

using namespace std;

class Block
{
public:
	vector<vector<bool>> shape; // Two-dimensional vector corresponding to the block's shape
	Block* right_rotation = nullptr; // Pointer to the block's clockwise neighbor block (its right rotation)
	Block* left_rotation = nullptr; // Pointer to the block's counter-clockwise neighbor block (its left rotation)
	Block* next_block = nullptr; // Pointer to the next block to appear in the game

	Block() {}
	Block(vector<vector<bool>> shape) : shape(shape) {}

	int get_width() const
	{
		return shape[0].size();
	}

	int get_height() const
	{
		return shape.size();
	}

	int get_cell_count() const 
	{
		int count = 0;
		for (int i = 0; i < shape.size(); i++)
		{
			for (int j = 0; j < shape[0].size(); j++)
			{
				if (shape[i][j])
					count++;
			}
		}

		return count;
	}

	void create_rotations(Block* original, Block* left = nullptr)
	{
		if (*this == *original && left != nullptr)
		{
			left->right_rotation = original;
			original->left_rotation = left;
			delete this;
			return;
		}

		right_rotation = new Block;
		right_rotation->shape = vector<vector<bool>>(shape[0].size(), vector<bool>(shape.size(), false));
		for (size_t i = 0; i < shape.size(); ++i)
		{
			for (size_t j = 0; j < shape[i].size(); ++j)
			{
				right_rotation->shape[j][shape.size() - 1 - i] = shape[i][j];
			}
		}

		next_block = original->next_block;
		right_rotation->left_rotation = this;
		right_rotation->create_rotations(original, this);
	}

	void print_block(Block* block)
	{
		for (auto row : shape)
		{
			for (bool b : row)
				std::cout << b;
			std::cout << std::endl;
		}
		std::cout << std::endl;
	}

	// Dispose all possible rotations of the block (but not itself)
	void dispose_rotations()
	{
		Block* current = right_rotation;
		for (int i = 0; i < 3; i++)
		{
			if (current == this)
				break;

			Block* next = current->right_rotation;
			delete current;
			current = next;
		}
	}

	bool operator==(const Block& other) const
	{
		return shape == other.shape;
	}

	bool operator!=(const Block& other) const
	{
		return !(*this == other);
	}
};


#endif //PA2_BLOCK_H