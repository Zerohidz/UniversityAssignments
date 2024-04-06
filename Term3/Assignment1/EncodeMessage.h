#ifndef ENCODE_MESSAGE_H
#define ENCODE_MESSAGE_H

#include <string>
#include <vector>
#include "ImageMatrix.h"

class EncodeMessage
{
public:
	EncodeMessage();
	~EncodeMessage();

	ImageMatrix encodeMessageToImage(const ImageMatrix& img, const std::string& message, const std::vector<std::pair<int, int>>& positions);

	bool is_prime(int n);
	int get_fibonacci(int i);

	std::vector<int> to_binary(std::vector<int> message);

private:
	// Any private helper functions or variables if necessary


};

#endif // ENCODE_MESSAGE_H
