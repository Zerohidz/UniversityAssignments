#define _CRT_SECURE_NO_WARNINGS
#include "Network.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <iomanip>
#include <chrono>
#include <ctime>
#include <cmath>


string operator*(string str, int multiplier)
{
	if (multiplier < 1)
		return string("");

	string new_str(str);

	for (size_t i = 0; i < multiplier - 1; i++)
		new_str += str;

	return new_str;
}

Network::Network()
{

}

void Network::process_commands(vector<Client>& clients, vector<string>& commands, int message_limit,
	const string& sender_port, const string& receiver_port)
{
	for (string command : commands)
	{
		print_command_header(command);

		if (command.rfind("MESSAGE", 0) == 0)
			message_cmd(command, clients, message_limit, sender_port, receiver_port);
		else if (command.rfind("SHOW_FRAME_INFO", 0) == 0)
			show_frame_info_cmd(command, clients);
		else if (command.rfind("SHOW_Q_INFO", 0) == 0)
			show_q_info_cmd(command, clients);
		else if (command.rfind("SEND", 0) == 0)
			send_cmd(clients);
		else if (command.rfind("RECEIVE", 0) == 0)
			receive_cmd(clients);
		else if (command.rfind("PRINT_LOG", 0) == 0)
			print_log_cmd(command, clients);
		else
			invalid_command_cmd(command);
	}
}

void Network::message_cmd(string command, vector<Client>& clients, int message_limit,
	const string& sender_port, const string& receiver_port)
{
	// Parse the command and extract sender and receiver IDs and the message
	std::istringstream iss(command);
	std::string dummy, sender_id, receiver_id, text;
	iss >> dummy >> sender_id >> receiver_id;

	string message;
	while (!iss.eof())
	{
		string word;
		iss >> word;
		message += word;
		if (!iss.eof())
			message += " ";
	}
	message = message.substr(1, message.size() - 2);

	// Find the sender and receiver clients
	Client* sender = find_client_by_id(clients, sender_id);
	Client* receiver = find_client_by_id(clients, receiver_id);
	if (sender == nullptr || receiver == nullptr)
	{
		cerr << "No sender or receiver found with the given IDs." << endl;
		return;
	}

	string next_hop_client_id = sender->routing_table[receiver_id];
	Client* next_hop_client = find_client_by_id(clients, next_hop_client_id);

	// Create the frames and push them to the outgoing queue of the sender
	cout << "Message to be sent: \"" << message << "\"\n";
	cout << endl;
	int chunk_count = ceil((float)message.size() / message_limit);
	for (size_t i = 0; i < chunk_count; i++)
	{
		stack<Packet*> frame;
		frame.push(new ApplicationLayerPacket(0, sender_id, receiver_id, message.substr(i * message_limit, message_limit)));
		frame.push(new TransportLayerPacket(1, sender_port, receiver_port));
		frame.push(new NetworkLayerPacket(2, sender->client_ip, receiver->client_ip));
		frame.push(new PhysicalLayerPacket(3, sender->client_mac, next_hop_client->client_mac));

		sender->outgoing_queue.push(frame);
		frames_to_dispose.push_back(frame);

		cout << "Frame #" << i + 1 << endl;
		sender->print_frame_info(frame);
		cout << "--------" << endl;
	}

	sender->log(Log(get_current_timestamp(), message, chunk_count, 0, sender_id, receiver_id, true, ActivityType::MESSAGE_SENT));
}

void Network::show_frame_info_cmd(string command, vector<Client>& clients)
{
	istringstream iss(command);
	string dummy, client_id, queue_selection, frame_number;
	iss >> dummy >> client_id >> queue_selection >> frame_number;

	Client* client = find_client_by_id(clients, client_id);

	stack<Packet*> frame;
	bool frame_found = false;
	if (client != nullptr)
	{
		if (queue_selection == "in")
			frame_found = client->get_incoming_frame(frame_number, frame);
		else if (queue_selection == "out")
			frame_found = client->get_outgoing_frame(frame_number, frame);
	}

	if (!frame_found)
	{
		cout << "No such frame." << endl;
		return;
	}

	cout << "Current Frame #" << frame_number << " on the " << (queue_selection == "in" ? "incoming" : "outgoing") << " queue of client " << client_id << endl;
	print_frame_info(frame);
}

void Network::show_q_info_cmd(string command, vector<Client>& clients)
{
	// parse: SHOW_Q_INFO<space>client_ID<space>queue_selection
	istringstream iss(command);
	string dummy, client_id, queue_selection;
	iss >> dummy >> client_id >> queue_selection;

	Client* client = find_client_by_id(clients, client_id);

	if (client == nullptr)
	{
		cout << "No such client." << endl;
		return;
	}

	cout << "Client " << client_id << " " << (queue_selection == "in" ? "Incoming" : "Outgoing") << " Queue Status" << endl;
	cout << "Current total number of frames: " << (queue_selection == "in" ? client->incoming_queue.size() : client->outgoing_queue.size()) << endl;
}

void Network::send_cmd(vector<Client>& clients)
{
	for (size_t i = 0; i < clients.size(); i++)
	{
		Client& client = clients[i];
		int count = 0;
		while (!client.outgoing_queue.empty())
		{
			stack<Packet*> frame = client.outgoing_queue.front();
			client.outgoing_queue.pop();
			count++;

			PhysicalLayerPacket* physical_layer = (PhysicalLayerPacket*)frame.top();
			ApplicationLayerPacket* app_layer = get_app_layer(frame);

			string receiver_mac = physical_layer->receiver_MAC_address;
			Client* receiver = find_client_by_mac(clients, receiver_mac);
			if (receiver == nullptr)
			{
				cerr << "No receiver found with the given MAC address." << endl;
				return;
			}
			receiver->incoming_queue.push(frame);

			physical_layer->hop_count++;

			cout << "Client " << physical_layer->sender_MAC_address[0] << " sending frame #" << count << " to client " << physical_layer->receiver_MAC_address[0] << endl;
			client.print_frame_info(frame);
			cout << "--------" << endl;

			if (is_end_of_a_message(app_layer->message_data))
				count = 0;
		}
	}
}

void Network::receive_cmd(vector<Client>& clients)
{
	for (auto& client : clients)
	{
		while (!client.incoming_queue.empty())
		{
			int count = 0;
			bool receiving = false;
			bool forwarding = false;
			bool dropping = false;
			string sender_id = "";
			string sender_mac_id = "";
			string receiver_id = "";
			string receiver_mac_id = "";
			int hop_count = 0;

			if (client.is_going_to_forward(client.incoming_queue.front(), clients))
				cout << "Client " << client.client_id << " receiving a message from client " << get_app_layer(client.incoming_queue.front())->sender_ID << ", but intended for client " << get_app_layer(client.incoming_queue.front())->receiver_ID << ". Forwarding... " << endl;

			string assembled_message = "";
			while (!client.incoming_queue.empty() && !is_end_of_a_message(assembled_message))
			{
				auto current_frame = client.incoming_queue.front();
				client.incoming_queue.pop();

				const auto* current_app_layer = get_app_layer(current_frame);
				auto* current_physical_layer = ((PhysicalLayerPacket*)current_frame.top());
				assembled_message += current_app_layer->message_data;
				hop_count = current_physical_layer->hop_count;
				sender_id = current_app_layer->sender_ID;
				receiver_id = current_app_layer->receiver_ID;
				sender_mac_id = current_physical_layer->sender_MAC_address[0];
				receiver_mac_id = current_physical_layer->receiver_MAC_address[0];
				count++;

				if (current_app_layer->receiver_ID == client.client_id)
				{
					receiving = true;
					sender_mac_id = current_app_layer->sender_ID;

					client.print_received(current_frame, count);
				}
				else
				{
					// Forward the message frame to the next hop using routing table and app_layer->reciever_ID
					string next_hop_client_id = client.routing_table[current_app_layer->receiver_ID];
					Client* next_hop_client = find_client_by_id(clients, next_hop_client_id);
					if (next_hop_client == nullptr)
					{
						cout << "Client " << client.client_id << " receiving frame #" << count << " from client " << current_app_layer->sender_ID << ", but intended for client " << current_app_layer->receiver_ID << ". Forwarding... " << endl;
						cout << "Error: Unreachable destination. Packets are dropped after " << current_physical_layer->hop_count << " hops!" << endl;
						dropping = true;
					}
					else
					{
						cout << "Frame #" << count << " MAC address change: New sender MAC " << client.client_mac << ", new receiver MAC " << next_hop_client->client_mac << endl;

						current_physical_layer->sender_MAC_address = client.client_mac;
						current_physical_layer->receiver_MAC_address = next_hop_client->client_mac;
						client.outgoing_queue.push(current_frame);

						forwarding = true;
					}
				}
			}

			if (receiving)
			{
				cout << "Client " << client.client_id << " received the message \"" << assembled_message << "\" from client " << sender_mac_id << ".\n";
				client.log(Log(get_current_timestamp(), assembled_message, count, hop_count, sender_mac_id, receiver_mac_id, true, ActivityType::MESSAGE_RECEIVED));
			}
			if (forwarding)
			{
				client.log(Log(get_current_timestamp(), assembled_message, count, hop_count, sender_id, receiver_id, true, ActivityType::MESSAGE_FORWARDED));
			}
			if (dropping)
			{
				client.log(Log(get_current_timestamp(), assembled_message, count, hop_count, sender_id, receiver_id, false, ActivityType::MESSAGE_DROPPED));
			}

			cout << "--------" << endl;
		}
	}
}

void Network::print_log_cmd(string command, vector<Client>& clients)
{
	string client_id = command.substr(10, command.size() - 10);

	Client* client = find_client_by_id(clients, client_id);
	if (client == nullptr || client->log_entries.empty())
		return;

	cout << "Client " << client->client_id << " Logs:" << endl;
	cout << "--------------" << endl;

	for (size_t i = 0; i < client->log_entries.size(); i++)
	{
		client->log_entries[i].print(i + 1);
		if (i != client->log_entries.size() - 1)
			cout << "--------------" << endl;
	}
}

void Network::invalid_command_cmd(string command)
{
	cout << "Invalid command." << endl;
}

Client* Network::find_client_by_id(vector<Client>& clients, string client_id)
{
	for (auto& client : clients)
	{
		if (client.client_id == client_id)
			return &client;
	}

	return nullptr;
}

Client* Network::find_client_by_mac(vector<Client>& clients, string client_mac)
{
	for (auto& client : clients)
	{
		if (client.client_mac == client_mac)
			return &client;
	}

	return nullptr;
}

ApplicationLayerPacket* Network::get_app_layer(stack<Packet*> frame)
{
	for (size_t _ = 0; _ < 3; _++)
		frame.pop();
	return (ApplicationLayerPacket*)frame.top();
}

void Network::print_frame_info(stack<Packet*> frame)
{
	auto* physical_layer = (PhysicalLayerPacket*)frame.top();
	frame.pop();
	auto* network_layer = (NetworkLayerPacket*)frame.top();
	frame.pop();
	auto* transport_layer = (TransportLayerPacket*)frame.top();
	frame.pop();
	auto* app_layer = (ApplicationLayerPacket*)frame.top();
	frame.pop();

	cout << "Carried Message: \"" << app_layer->message_data << "\"" << endl;
	cout << "Layer 0 info: Sender ID: " << app_layer->sender_ID << ", Receiver ID: " << app_layer->receiver_ID << endl;
	cout << "Layer 1 info: Sender port number: " << transport_layer->sender_port_number << ", Receiver port number: " << transport_layer->receiver_port_number << endl;
	cout << "Layer 2 info: Sender IP address: " << network_layer->sender_IP_address << ", Receiver IP address: " << network_layer->receiver_IP_address << endl;
	cout << "Layer 3 info: Sender MAC address: " << physical_layer->sender_MAC_address << ", Receiver MAC address: " << physical_layer->receiver_MAC_address << endl;
	cout << "Number of hops so far: " << physical_layer->hop_count << endl;
}

void Network::print_command_header(string command)
{
	string command_header = "Command: " + command;
	print_seperator_line(command_header);
	cout << command_header << endl;
	print_seperator_line(command_header);
}

void Network::print_seperator_line(string& length_ref)
{
	cout << string("-") * (int)length_ref.size() << endl;
}

bool Network::is_end_of_a_message(string str)
{
	if (str == "")
		return false;
	return MESSAGE_ENDERS.find(str.back()) != string::npos;
}

void Network::dispose_frame_packets(stack<Packet*>& frame)
{
	while (!frame.empty())
	{
		delete frame.top();
		frame.pop();
	}
}

string Network::get_current_timestamp()
{
	auto now = chrono::system_clock::now();
	time_t currentTime = chrono::system_clock::to_time_t(now);

	tm* tmStruct = localtime(&currentTime);
	ostringstream oss;
	oss << put_time(tmStruct, "%Y-%m-%d %H:%M:%S");

	return oss.str();
}

vector<Client> Network::read_clients(const string& filename)
{
	vector<Client> clients;
	fstream file;
	file.open(filename);
	if (!file.is_open())
	{
		cerr << "File could not be opened" << endl;
		return vector<Client>();
	}

	int client_count = 0;
	if (!file.eof())
		file >> client_count;
	for (size_t i = 0; i < client_count; i++)
	{
		string client_id, client_ip, client_mac;
		file >> client_id >> client_ip >> client_mac;
		clients.push_back(Client(client_id, client_ip, client_mac));
	}

	return clients;
}

void Network::read_routing_tables(vector<Client>& clients, const string& filename)
{
	fstream file;
	file.open(filename);
	if (!file.is_open())
	{
		cerr << "File could not be opened" << endl;
		return;
	}

	for (size_t i = 0; i < clients.size(); i++)
	{
		auto& routing_table = clients[i].routing_table;
		for (size_t j = 0; j < clients.size() - 1; j++)
		{
			string receiver_id, next_hop_id;
			file >> receiver_id >> next_hop_id;
			routing_table[receiver_id] = next_hop_id;
		}
		string dummy;
		file >> dummy;
	}

	file.close();
}

vector<string> Network::read_commands(const string& filename)
{
	vector<string> commands;

	fstream file;
	file.open(filename);
	if (!file.is_open())
	{
		cerr << "File could not be opened" << endl;
		return vector<string>();
	}

	bool first = true;
	while (!file.eof())
	{
		string command;
		getline(file, command);
		if (!first)
			commands.push_back(command);
		else
		{
			first = false;
			continue;
		}
	}

	return commands;
}

Network::~Network()
{
	for (auto& frame : frames_to_dispose)
		dispose_frame_packets(frame);
}
