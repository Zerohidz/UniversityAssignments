#ifndef NETWORK_H
#define NETWORK_H

#include <vector>
#include <iostream>
#include "Packet.h"
#include "Client.h"

using namespace std;

class Network
{
public:
	string MESSAGE_ENDERS = "!.?";

	Network();
	~Network();

	// Executes commands given as a vector of strings while utilizing the remaining arguments.
	void process_commands(vector<Client>& clients, vector<string>& commands, int message_limit, const string& sender_port,
		const string& receiver_port);

	void message_cmd(string command, vector<Client>& clients, int message_limit, const string& sender_port, const string& receiver_port);
	void show_frame_info_cmd(string command, vector<Client>& clients);
	void show_q_info_cmd(string command, vector<Client>& clients);
	void send_cmd(vector<Client>& clients);
	void receive_cmd(vector<Client>& clients);
	void print_log_cmd(string command, vector<Client>& clients);
	void invalid_command_cmd(string command);

	static Client* find_client_by_id(vector<Client>& clients, string client_id);
	static Client* find_client_by_mac(vector<Client>& clients, string client_mac);
	static ApplicationLayerPacket* get_app_layer(stack<Packet*> frame);
	static void print_frame_info(stack<Packet*> frame);
	static void print_seperator_line(string& length_ref);
	static void print_command_header(string command);
	static string get_current_timestamp();
	bool is_end_of_a_message(string str);
	void dispose_frame_packets(stack<Packet*>& frame);

	// Initialize the network from the input files.
	vector<Client> read_clients(string const& filename);
	void read_routing_tables(vector<Client>& clients, string const& filename);
	vector<string> read_commands(const string& filename);
	
	vector<stack<Packet*>> frames_to_dispose;
};

#endif  // NETWORK_H
