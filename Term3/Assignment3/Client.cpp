//
// Created by alperen on 27.09.2023.
//

#include "Client.h"
#include "Network.h"

Client::Client(string const& _id, string const& _ip, string const& _mac)
{
	client_id = _id;
	client_ip = _ip;
	client_mac = _mac;
}

ostream& operator<<(ostream& os, const Client& client)
{
	os << "client_id: " << client.client_id << " client_ip: " << client.client_ip << " client_mac: "
		<< client.client_mac << endl;
	return os;
}

Client::~Client()
{
	// TODO: Free any dynamically allocated memory if necessary.
}

bool Client::is_going_to_forward(stack<Packet*> frame, vector<Client>& clients)
{
	auto app_layer = Network::get_app_layer(frame);
	if (app_layer->receiver_ID != client_id)
	{
		string next_hop_client_id = routing_table[app_layer->receiver_ID];
		Client* next_hop_client = Network::find_client_by_id(clients, next_hop_client_id);
		if (next_hop_client != nullptr)
		{
			return true;
		}
	}

	return false;
}

void Client::print_received(stack<Packet*> frame, int frame_num)
{
	auto frame_temp = frame;
	auto* physical_layer = (PhysicalLayerPacket*)frame_temp.top();
	frame_temp.pop();
	auto* network_layer = (NetworkLayerPacket*)frame_temp.top();
	frame_temp.pop();
	auto* transport_layer = (TransportLayerPacket*)frame_temp.top();
	frame_temp.pop();
	auto* app_layer = (ApplicationLayerPacket*)frame_temp.top();
	frame_temp.pop();

	cout << "Client " << client_id << " receiving frame #" << frame_num << " from client " << physical_layer->sender_MAC_address[0] << ", originating from client " << app_layer->sender_ID << endl;
	print_frame_info(frame);
	cout << "--------" << endl;
}

void Client::print_frame_info(std::stack<Packet*> frame)
{
	auto* physical_layer = (PhysicalLayerPacket*)frame.top();
	frame.pop();
	auto* network_layer = (NetworkLayerPacket*)frame.top();
	frame.pop();
	auto* transport_layer = (TransportLayerPacket*)frame.top();
	frame.pop();
	auto* app_layer = (ApplicationLayerPacket*)frame.top();
	frame.pop();

	cout << "Sender MAC address: " << physical_layer->sender_MAC_address << ", Receiver MAC address: " << physical_layer->receiver_MAC_address << endl;
	cout << "Sender IP address: " << network_layer->sender_IP_address << ", Receiver IP address: " << network_layer->receiver_IP_address << endl;
	cout << "Sender port number: " << transport_layer->sender_port_number << ", Receiver port number: " << transport_layer->receiver_port_number << endl;
	cout << "Sender ID: " << app_layer->sender_ID << ", Receiver ID: " << app_layer->receiver_ID << endl;
	cout << "Message chunk carried: \"" << app_layer->message_data << "\"\n";
	cout << "Number of hops so far: " << physical_layer->hop_count << endl;
}

void Client::log(Log log)
{
	log_entries.push_back(log);
}

bool Client::get_incoming_frame(string frame_number, stack<Packet*>& out_frame)
{
	int frame_num = stoi(frame_number);
	if (incoming_queue.size() < frame_num)
		return false;

	auto queue = incoming_queue;
	for (size_t i = 0; i < frame_num - 1; i++)
		queue.pop();

	out_frame = queue.front();
	return true;
}

bool Client::get_outgoing_frame(string frame_number, stack<Packet*>& out_frame)
{
	int frame_num = stoi(frame_number);
	if (outgoing_queue.size() < frame_num)
		return false;

	auto queue = outgoing_queue;
	for (size_t i = 0; i < frame_num - 1; i++)
		queue.pop();

	out_frame = queue.front();
	return true;
}
