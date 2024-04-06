#ifndef BLOCKFALL_H
#define BLOCKFALL_H

#define occupiedCellChar "██"
#define unoccupiedCellChar "▒▒"
//#define occupiedCellChar "D"
//#define unoccupiedCellChar "_"

#include <vector>
#include <string>

#include "Block.h"
#include "LeaderboardEntry.h"
#include "Leaderboard.h"

using namespace std;

class BlockFall
{
public:

	BlockFall(string grid_file_name, string blocks_file_name, bool gravity_mode_on, const string& leaderboard_file_name,
		const string& player_name);
	virtual ~BlockFall();

	int rows;  // Number of rows in the grid
	int cols;  // Number of columns in the grid
	int pos_x; // Current position of active block
	int pos_y; // Current position of active block
	bool player_won = true;
	vector<vector<int> > grid;  // 2D game grid
	vector<vector<bool>> power_up; // 2D matrix of the power-up shape
	Block* initial_block = nullptr; // Head of the list of game blocks. Must be filled up and initialized after a call to read_blocks()
	Block* active_rotation = nullptr; // Currently active rotation of the active block. Must start with the initial_block
	bool gravity_mode_on = false; // Gravity mode of the game
	unsigned long current_score = 0; // Current score of the game
	string leaderboard_file_name; // Leaderboard file name, taken from the command-line argument 5 in main
	string player_name; // Player name, taken from the command-line argument 6 in main
	Leaderboard leaderboard;

	void print_grid();
	void rotate_right();
	void rotate_left();
	void move_right();
	void move_left();
	bool drop();
	void gravity_switch();

	bool can_place(const Block* block, int x, int y, vector<vector<int>>& place_on);
	bool instantiate_next_block();
	void win_game();
	void lose_game(Block* not_fitting_block);
	void end_game();
	void clear_full_rows(bool will_print);
	void emplace_block_to(Block* block, int x, int y, vector<vector<int>>& emplace_on);
	void save_to_leaderboard();
	void print_board(const vector<vector<int>>& board);
	void check_for_power_ups();
	bool check_pattern_at_position(int row, int col);

	void initialize_grid(const string& input_file); // Initializes the grid using the command-line argument 1 in main
	static vector<string> read_lines(const string& input_file, bool include_newlines);
	void read_blocks(const string& input_file); // Reads the input file and calls the read_block() function for each block;
};



#endif // BLOCKFALL_H
