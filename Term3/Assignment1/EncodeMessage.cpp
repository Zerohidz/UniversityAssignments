#include "EncodeMessage.h"
#include <cmath>
#include <algorithm>


// Default Constructor
EncodeMessage::EncodeMessage()
{

}

// Destructor
EncodeMessage::~EncodeMessage()
{

}

// Function to encode a message into an image matrix
ImageMatrix EncodeMessage::encodeMessageToImage(const ImageMatrix& img, const std::string& message, const std::vector<std::pair<int, int>>& positions)
{
	// Modify the message ---------------------------------------------- ok
	std::vector<int> modified_message;
	for (int i = 0; i < message.size(); i++)
		modified_message.push_back((int)message[i]);

	for (int i = 0; i < message.size(); i++)
	{
		if (is_prime(i))
		{
			int decimal = (int)message[i] + get_fibonacci(i);
			if (decimal <= 32) decimal += 33;
			if (decimal >= 127) decimal = 126;
			modified_message[i] = decimal;
		}
	}

	// Circular shifting ---------------------------------------------- ok
	int shift_amount = message.size() / 2;
	std::vector<int> tmp = modified_message;
	for (int i = 0; i < modified_message.size(); i++)
	{
		int new_index = (i + shift_amount) % (int)modified_message.size();
		modified_message[new_index] = tmp[i];
	}

	// Encoding back to the image 
	// TODO: the problem
	ImageMatrix modified_image = img;
	std::vector<int> binary_message = to_binary(modified_message);
	for (int i = 0; i < positions.size(); i++)
	{
		std::pair<int, int> position = positions[i];
		int pixel_val = img.get_data(position.first, position.second);

		if (binary_message[i] == 1 && pixel_val % 2 == 0)
			modified_image.set_data(pixel_val + 1, position.first, position.second);
	}

	return modified_image;
}

bool EncodeMessage::is_prime(int n)
{
	if (n <= 1)
		return false;
	for (int i = 2; i < n; i++)
	{
		if (n % i == 0)
			return false;
	}
	return true;
}

int EncodeMessage::get_fibonacci(int i)
{
	if (i == 0)
		return 0;
	if (i == 1)
		return 1;
	return get_fibonacci(i - 1) + get_fibonacci(i - 2);
}

std::vector<int> EncodeMessage::to_binary(std::vector<int> message)
{
	std::vector<int> binary;
	for (int i = 0; i < message.size(); i++)
	{
		int decimal = message[i];
		std::vector<int> binary_char;
		while (decimal > 0)
		{
			binary_char.push_back(decimal % 2);
			decimal /= 2;
		}
		std::reverse(binary_char.begin(), binary_char.end());
		if (binary_char.size() < 7)
			for (int i = 0; i < 7 - binary_char.size(); i++)
				binary_char.insert(binary_char.begin(), 0);

		for (int i = 0; i < binary_char.size(); i++)
			binary.push_back(binary_char[i]);
	}
	return binary;
}
