#define _CRT_SECURE_NO_WARNINGS
#include "Leaderboard.h"
#include <fstream>
#include <iostream>
#include <vector>
#include <ctime>


void Leaderboard::insert_new_entry(LeaderboardEntry* new_entry)
{
	if (head_leaderboard_entry == nullptr)
	{
		head_leaderboard_entry = new_entry;
		return;
	}

	LeaderboardEntry* current = head_leaderboard_entry;
	LeaderboardEntry* previous = nullptr;

	while (current != nullptr)
	{
		if (new_entry->score > current->score)
		{
			if (previous == nullptr)
			{
				new_entry->next_leaderboard_entry = head_leaderboard_entry;
				head_leaderboard_entry = new_entry;
			}
			else
			{
				previous->next_leaderboard_entry = new_entry;
				new_entry->next_leaderboard_entry = current;
			}

			trim_leaderboard();

			return;
		}

		previous = current;
		current = current->next_leaderboard_entry;
	}

	previous->next_leaderboard_entry = new_entry;

	trim_leaderboard();
}

void Leaderboard::trim_leaderboard() {
	int count = 0;
	LeaderboardEntry* current = head_leaderboard_entry;
	while (current != nullptr) {
		count++;
		current = current->next_leaderboard_entry;
	}

	while (count > 10) {
		LeaderboardEntry* last = head_leaderboard_entry;
		LeaderboardEntry* second_last = nullptr;

		while (last->next_leaderboard_entry != nullptr) {
			second_last = last;
			last = last->next_leaderboard_entry;
		}

		delete last;
		second_last->next_leaderboard_entry = nullptr;

		count--;
	}
}

void Leaderboard::write_to_file(const string& filename)
{
	ofstream file(filename);
	if (!file.is_open())
	{
		cerr << "Error: cannot open file '" << filename << "'\n";
		return;
	}

	int count = 0;
	LeaderboardEntry* current = head_leaderboard_entry;
	while (current != nullptr && count < MAX_LEADERBOARD_SIZE)
	{
		file << current->score << " " << current->last_played << " " << current->player_name;
		if (current->next_leaderboard_entry != nullptr)
			file << '\n';
		current = current->next_leaderboard_entry;
		count++;
	}

	file.close();
}


void Leaderboard::read_from_file(const string& filename)
{
	ifstream file(filename);
	if (!file.is_open())
	{
		cerr << "Error: Can not open file'" << filename << "'\n";
	}

	unsigned long score;
	time_t last_played;
	string player_name;
	LeaderboardEntry* last_entry = nullptr;
	while (!file.eof())
	{
		file >> score >> last_played >> player_name;
		if (last_entry == nullptr)
		{
			last_entry = new LeaderboardEntry(score, last_played, player_name);
			head_leaderboard_entry = last_entry;
		}
		else
		{
			last_entry->next_leaderboard_entry = new LeaderboardEntry(score, last_played, player_name);
			last_entry = last_entry->next_leaderboard_entry;
		}
	}

	file.close();

	trim_leaderboard();
}


void Leaderboard::print_leaderboard()
{
	cout << "Leaderboard\n";
	cout << "-----------\n";


	int count = 0;
	LeaderboardEntry* current = head_leaderboard_entry;
	while (current != nullptr && count < MAX_LEADERBOARD_SIZE)
	{
		char last_played[20];
		std::strftime(last_played, sizeof(last_played), "%H:%M:%S/%d.%m.%Y", localtime(&current->last_played));
		cout << count + 1 << ". " << current->player_name << " " << current->score << " " << last_played << '\n';
		current = current->next_leaderboard_entry;
		count++;
	}
}

Leaderboard::~Leaderboard()
{
	LeaderboardEntry* current = head_leaderboard_entry;
	while (current != nullptr)
	{
		LeaderboardEntry* next = current->next_leaderboard_entry;
		delete current;
		current = next;
	}
}
