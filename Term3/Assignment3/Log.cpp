//
// Created by alperen on 2.10.2023.
//

#include "Log.h"

Log::Log(const string& _timestamp, const string& _message, int _number_of_frames, int _number_of_hops, const string& _sender_id,
	const string& _receiver_id, bool _success, ActivityType _type)
{
	timestamp = _timestamp;
	message_content = _message;
	number_of_frames = _number_of_frames;
	number_of_hops = _number_of_hops;
	sender_id = _sender_id;
	receiver_id = _receiver_id;
	success_status = _success;
	activity_type = _type;
}

Log::~Log()
{
	// TODO: Free any dynamically allocated memory if necessary.
}

void Log::print(int entry_number)
{
	cout << "Log Entry #" << entry_number << ":" << endl;
	cout << "Activity: ";

	switch (activity_type)
	{
	case ActivityType::MESSAGE_RECEIVED:
		cout << "Message Received" << endl;
		break;
	case ActivityType::MESSAGE_FORWARDED:
		cout << "Message Forwarded" << endl;
		break;
	case ActivityType::MESSAGE_SENT:
		cout << "Message Sent" << endl;
		break;
	case ActivityType::MESSAGE_DROPPED:
		cout << "Message Dropped" << endl;
		break;
	default:
		cout << "Unknown Activity" << endl;
	}

	cout << "Timestamp: " << timestamp << endl;
	cout << "Number of frames: " << number_of_frames << endl;
	cout << "Number of hops: " << number_of_hops << endl;
	cout << "Sender ID: " << sender_id << endl;
	cout << "Receiver ID: " << receiver_id << endl;
	cout << "Success: " << (success_status ? "Yes" : "No") << endl;
	if (activity_type == ActivityType::MESSAGE_SENT || activity_type == ActivityType::MESSAGE_RECEIVED)
		cout << "Message: \"" << message_content << "\"" << endl;
}
