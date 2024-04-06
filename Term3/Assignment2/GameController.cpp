#include "GameController.h"

bool GameController::play(BlockFall& game, const string& commands_file)
{
	bool the_game_is_on_wattson = true;
	vector<string> command_lines = BlockFall::read_lines(commands_file, false);
	for (string command_line : command_lines)
	{
		if (command_line == "PRINT_GRID")
			game.print_grid();
		else if (command_line == "ROTATE_RIGHT")
			game.rotate_right();
		else if (command_line == "ROTATE_LEFT")
			game.rotate_left();
		else if (command_line == "MOVE_RIGHT")
			game.move_right();
		else if (command_line == "MOVE_LEFT")
			game.move_left();
		else if (command_line == "DROP")
			the_game_is_on_wattson = game.drop();
		else if (command_line == "GRAVITY_SWITCH")
			game.gravity_switch();

		if (!the_game_is_on_wattson)
			break;
	}

	if (the_game_is_on_wattson && game.player_won)
		game.win_game();

	game.save_to_leaderboard();
	return game.player_won;
}