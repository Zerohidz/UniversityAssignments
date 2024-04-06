// DecodeMessage.cpp

#include "DecodeMessage.h"
#include <iostream>

// Default constructor
DecodeMessage::DecodeMessage()
{
	// Nothing specific to initialize here
}

// Destructor
DecodeMessage::~DecodeMessage()
{
	// Nothing specific to clean up
}


std::string DecodeMessage::decodeFromImage(const ImageMatrix& image, const std::vector<std::pair<int, int>>& edgePixels)
{
	std::string code;
	for (std::pair<int, int> pair : edgePixels)
	{
		int value = (int)image.get_data(pair.first, pair.second);
		if (value % 2 == 1)
			code.push_back('1');
		else
			code.push_back('0');
	}

	int zero_padding_count = 7 - (code.size() % 7);
	for (int i = 0; i < zero_padding_count; i++)
	{
		code.insert(code.begin(), '0');
	}

	std::string message;
	for (size_t i = 0; i < code.size() / 7; i++)
	{
		std::string c = code.substr(i * 7, 7);
		int decimal = binaryToDecimal(c);
		if (decimal <= 32) decimal += 33;
		if (decimal == 127) decimal = 126;
		message += char(decimal);
	}

	return message;
}

int DecodeMessage::binaryToDecimal(std::string n)
{
	std::string num = n;

	int dec_value = 0;

	int base = 1;

	int len = num.length();
	for (int i = len - 1; i >= 0; i--)
	{
		if (num[i] == '1')
			dec_value += base;
		base = base * 2;
	}

	return dec_value;
}

