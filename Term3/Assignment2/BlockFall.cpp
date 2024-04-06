#include "BlockFall.h"
#include <iostream>
#include <fstream>
#include <algorithm>
#include <ctime>


BlockFall::BlockFall(string grid_file_name, string blocks_file_name, bool gravity_mode_on, const string& leaderboard_file_name, const string& player_name) : gravity_mode_on(
	gravity_mode_on), leaderboard_file_name(leaderboard_file_name), player_name(player_name)
{
	initialize_grid(grid_file_name);
	read_blocks(blocks_file_name);
	leaderboard.read_from_file(leaderboard_file_name);
}

// TODO: Read the blocks from the input file and initialize "initial_block" and "active_rotation" member variables
// TODO: For every block, generate its rotations and properly implement the multilevel linked list structure
//       that represents the game blocks, as explained in the PA instructions.
// TODO: Initialize the "power_up" member variable as the last block from the input file (do not add it to the linked list!)
void BlockFall::read_blocks(const string& input_file)
{
	auto lines = read_lines(input_file, true);
	Block* last_block = nullptr;
	Block* current_block = nullptr;
	initial_block = nullptr;
	vector<bool> shape_row;
	for (int i = 0; i < lines.size(); i++)
	{
		string line = lines[i];
		for (int j = 0; j < line.size(); j++)
		{
			char c = line[j];

			if (c == '[')
			{
				Block* new_block = new Block();
				if (current_block == nullptr)
					initial_block = new_block;
				else
					current_block->next_block = new_block;
				last_block = current_block;
				current_block = new_block;
			}
			else if (c == '\n' || c == ']')
			{
				if (shape_row.size() != 0)
					current_block->shape.push_back(shape_row);
				shape_row.clear();
			}
			else if (c == '0')
			{
				shape_row.push_back(false);
			}
			else if (c == '1')
			{
				shape_row.push_back(true);
			}
		}
	}

	Block* current = initial_block;
	while (current != nullptr)
	{
		current->create_rotations(current);
		current = current->next_block;
	}

	// Assign power_up
	if (current_block != nullptr)
		power_up = current_block->shape;

	// Remove last block from the list
	if (last_block != nullptr)
		last_block->next_block = nullptr;

	// Assign active_rotation
	active_rotation = initial_block;
}

// TODO: Initialize "rows" and "cols" member variables
// TODO: Initialize "grid" member variable using the command-line argument 1 in main
void BlockFall::initialize_grid(const string& input_file)
{
	vector<string> lines = read_lines(input_file, false);

	if (lines.size() == 0 || lines[0].size() == 0)
	{
		cout << "Grid file is empty" << endl;
	}

	for (int i = 0; i < lines.size(); i++)
	{
		string line = lines[i];
		vector<int> row;
		for (int j = 0; j < lines[0].size(); j++)
		{
			if (line[j] == '0')
				row.push_back(0);
			else if (line[j] == '1')
				row.push_back(1);
		}
		grid.push_back(row);
	}
	rows = grid.size();
	cols = grid[0].size();
}

void BlockFall::print_grid()
{
	cout << "Score: " << current_score << endl;
	cout << "High Score: " << endl; // TODO: high score

	vector<vector<int>> screen = grid;
	for (int i = 0; i < active_rotation->get_height(); i++)
	{
		for (int j = 0; j < active_rotation->get_width(); j++)
		{
			screen[i + pos_y][j + pos_x] = active_rotation->shape[i][j];
		}
	}

	print_board(screen);
}

void BlockFall::rotate_right()
{
	if (can_place(active_rotation->right_rotation, pos_x, pos_y, grid))
		active_rotation = active_rotation->right_rotation;
}

void BlockFall::rotate_left()
{
	if (can_place(active_rotation->left_rotation, pos_x, pos_y, grid))
		active_rotation = active_rotation->left_rotation;
}

void BlockFall::move_right()
{
	if (can_place(active_rotation, pos_x + 1, pos_y, grid))
		pos_x++;
}

void BlockFall::move_left()
{
	if (can_place(active_rotation, pos_x - 1, pos_y, grid))
		pos_x--;
}

bool BlockFall::drop()
{
	int initial_pos_y = pos_y;
	while (can_place(active_rotation, pos_x, pos_y + 1, grid))
		pos_y++;
	// Gain points
	current_score += (pos_y - initial_pos_y) * active_rotation->get_cell_count();

	if (gravity_mode_on)
	{
		for (int i = active_rotation->get_height() - 1; i >= 0; i--)
		{
			for (int j = 0; j < active_rotation->get_width(); j++)
			{
				if (active_rotation->shape[i][j] == 1)
				{
					Block particle(vector<vector<bool>>(1, vector<bool>(1, true)));
					int particle_x = pos_x + j;
					int particle_y = pos_y + i;

					while (can_place(&particle, particle_x, particle_y + 1, grid))
						particle_y++;

					emplace_block_to(&particle, particle_x, particle_y, grid);
				}
			}
		}
	}
	else
	{
		emplace_block_to(active_rotation, pos_x, pos_y, grid);
	}

	// TODO: check for power up if found, 1000 + cleared cells
	check_for_power_ups();

	clear_full_rows(true);

	return instantiate_next_block();
}

void BlockFall::clear_full_rows(bool will_print)
{
	bool printed = false;
	for (int i = 0; i < grid.size(); i++)
	{
		vector<int> row = grid[i];

		bool is_full = std::all_of(row.begin(), row.end(), [](int i) { return i == 1; });
		if (is_full)
		{
			if (will_print && !printed) {
				cout << "Before clearing:" << endl;
				print_board(grid);
				printed = true;
			}
			for (int j = i; j > 0; j--)
			{
				grid[j] = grid[j - 1];
			}
			grid[0] = vector<int>(cols, 0);

			// Add points
			current_score += cols;
		}
	}
}

void BlockFall::emplace_block_to(Block* block, int x, int y, vector<vector<int>>& emplace_on)
{
	for (int i = 0; i < block->get_height(); i++)
	{
		for (int j = 0; j < block->get_width(); j++)
		{
			if (block->shape[i][j] == 1)
				emplace_on[i + y][j + x] = block->shape[i][j];
		}
	}
}

void BlockFall::save_to_leaderboard()
{
	auto* leaderboard_entry = new LeaderboardEntry(current_score, time(nullptr), player_name);
	leaderboard.insert_new_entry(leaderboard_entry);
	leaderboard.write_to_file(leaderboard_file_name);
}

void BlockFall::print_board(const vector<vector<int>>& board)
{
	for (int i = 0; i < rows; i++)
	{
		for (int j = 0; j < cols; j++)
		{
			if (board[i][j] == 0)
				cout << unoccupiedCellChar;
			else if (board[i][j] == 1)
				cout << occupiedCellChar;
		}
		cout << endl;
	}
	cout << endl;
}


void BlockFall::check_for_power_ups()
{
	for (int row = 0; row < grid.size() - power_up.size() + 1; ++row)
	{
		for (int col = 0; col < grid[row].size() - power_up[0].size() + 1; ++col)
		{
			if (check_pattern_at_position(row, col))
			{
				for (int i = 0; i < grid.size(); i++)
				{
					for (int j = 0; j < grid[0].size(); j++)
					{
						if (grid[i][j] == 1)
						{
							grid[i][j] = 0;
							current_score++;
						}
					}
				}

				current_score += 1000;
			}
		}
	}
}

bool BlockFall::check_pattern_at_position(int row, int col) {
	for (int i = 0; i < power_up.size(); ++i) {
		for (int j = 0; j < power_up[i].size(); ++j) {
			if ((power_up[i][j] && grid[row + i][col + j] != 1) || (!power_up[i][j] && grid[row + i][col + j] != 0)) {
				return false;
			}
		}
	}

	return true;
}

void BlockFall::gravity_switch()
{
	gravity_mode_on = !gravity_mode_on;

	if (gravity_mode_on)
	{
		vector<vector<int>> temp_grid = vector<vector<int>>(grid.size(), vector<int>(grid[0].size(), 0));
		for (int i = grid.size() - 1; i >= 0; i--)
		{
			for (int j = 0; j < grid[0].size(); j++)
			{
				if (grid[i][j] == 1)
				{
					Block particle(vector<vector<bool>>(1, vector<bool>(1, true)));
					int particle_x = j;
					int particle_y = i;

					while (can_place(&particle, particle_x, particle_y + 1, temp_grid))
						particle_y++;

					emplace_block_to(&particle, particle_x, particle_y, temp_grid);
				}
			}
		}

		grid = temp_grid;

		check_for_power_ups();

		clear_full_rows(false);
	}
}

bool BlockFall::can_place(const Block* block, int x, int y, vector<vector<int>>& place_on)
{
	if (x < 0 || x + block->get_width() > cols || y + block->get_height() > rows)
		return false;

	for (int i = (y < 0 ? -y : 0); i < block->get_height(); i++)
	{
		for (int j = 0; j < block->get_width(); j++)
		{
			if (block->shape[i][j] && place_on[i + y][j + x] == 1)
				return false;
		}
	}

	return true;
}

bool BlockFall::instantiate_next_block()
{
	Block* next_block = active_rotation->next_block;
	if (next_block == nullptr)
	{
		end_game();
		return false;
	}

	// Can we instantiate it from top of the screen to coordinates (0, 0)
	for (int i = 0; i < next_block->get_height(); i++)
	{
		if (!can_place(next_block, 0, -(next_block->get_height() - 1 - i), grid))
		{
			lose_game(next_block);
			return false;
		}
	}

	pos_x = 0;
	pos_y = 0;
	active_rotation = next_block;

	return true;
}

void BlockFall::win_game()
{
	cout << "YOU WIN!" << endl;
	cout << "No more blocks." << endl;
	cout << "Final grid and score:" << endl;
	cout << endl;
	print_grid();
	leaderboard.print_leaderboard();

	player_won = true;
}

void BlockFall::lose_game(Block* not_fitting_block)
{
	cout << "GAME OVER !" << endl;
	cout << "Next block that couldn't fit:" << endl;
	for (int i = 0; i < not_fitting_block->get_height(); i++)
	{
		for (int j = 0; j < not_fitting_block->get_width(); j++)
		{
			if (not_fitting_block->shape[i][j] == 0)
				cout << unoccupiedCellChar;
			else if (not_fitting_block->shape[i][j] == 1)
				cout << occupiedCellChar;
		}
		cout << endl;
	}
	cout << endl;
	cout << "Final grid and score:" << endl;
	cout << endl;
	print_grid();
	leaderboard.print_leaderboard();

	player_won = false;
}

void BlockFall::end_game()
{
	cout << "GAME FINISHED!" << endl;
	cout << "No more commands." << endl;
	cout << "Final grid and score:" << endl;
	cout << endl;
	print_grid();
	leaderboard.print_leaderboard();

	player_won = true;
}

vector<string> BlockFall::read_lines(const string& input_file, bool include_newlines)
{
	fstream file;
	file.open(input_file, ios::in);
	if (!file)
	{
		cout << "Error opening file" << endl;
		exit(1);
	}

	vector<string> lines;
	string line;
	while (getline(file, line))
		lines.push_back(line + (include_newlines ? "\n" : ""));
	file.close();

	return lines;
}


BlockFall::~BlockFall()
{
	Block* current = initial_block;
	while (current != nullptr)
	{
		Block* next = current->next_block;
		current->dispose_rotations();
		delete current;
		current = next;
	}
}
