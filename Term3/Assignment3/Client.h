#ifndef SRC_CLIENT_H
#define SRC_CLIENT_H

#include <string>
#include <unordered_map>
#include <ostream>
#include <stack>
#include <queue>
#include <vector>
#include <iostream>
#include <algorithm>

#include "Packet.h"
#include "ApplicationLayerPacket.h"
#include "TransportLayerPacket.h"
#include "NetworkLayerPacket.h"
#include "PhysicalLayerPacket.h"
#include "Log.h"

using namespace std;

class Client {
public:
    Client(string const& id, string const& ip, string const& mac);
    ~Client();

    string client_id;
    string client_ip;
    string client_mac;
    vector<Log> log_entries;
    unordered_map < string, string > routing_table; // <receiverID, nexthopID>
    queue<stack<Packet*>> incoming_queue;
    queue<stack<Packet*>> outgoing_queue;

    void print_received(stack<Packet*> frame, int frame_num);
    void print_frame_info(stack<Packet*> frame);
    void log(Log log);
    bool is_going_to_forward(stack<Packet*> frame, vector<Client>& clients);
    bool get_incoming_frame(string frame_number, stack<Packet*>& out_frame);
    bool get_outgoing_frame(string frame_number, stack<Packet*>& out_frame);

    friend ostream &operator<<(ostream &os, const Client &client);
};


#endif //SRC_CLIENT_H
