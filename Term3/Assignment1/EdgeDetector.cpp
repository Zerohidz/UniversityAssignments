// EdgeDetector.cpp

#include "EdgeDetector.h"
#include <cmath>

#include "EdgeDetector.h"
#include <cmath>


// Default constructor
EdgeDetector::EdgeDetector()
{
	kernel_sobel_x = new double* [3] {
		new double[3] { -1, 0, 1 },
		new double[3] { -2, 0, 2 },
		new double[3] { -1, 0, 1 }
	};

	kernel_sobel_y = new double* [3] {
		new double[3] { -1, -2, -1 },
		new double[3] { 0, 0, 0 },
		new double[3] { 1, 2, 1 }
	};
}

// Destructor
EdgeDetector::~EdgeDetector()
{
	for (int i = 0; i < 3; i++)
		delete[] kernel_sobel_x[i];
	delete[] kernel_sobel_x;

	for (int i = 0; i < 3; i++)
		delete[] kernel_sobel_y[i];
	delete[] kernel_sobel_y;
}

// Detect Edges using the given algorithm
std::vector<std::pair<int, int>> EdgeDetector::detectEdges(const ImageMatrix& input_image)
{
	Convolution conv_sobel_x(kernel_sobel_x, 3, 3, 1, true);
	Convolution conv_sobel_y(kernel_sobel_y, 3, 3, 1, true);

	ImageMatrix img_x = conv_sobel_x.convolve(input_image);
	ImageMatrix img_y = conv_sobel_y.convolve(input_image);

	ImageMatrix result = (img_x * img_x + img_y * img_y).sqrt();
	result.threshold(result.get_average());

	std::vector<std::pair<int, int>> edges;
	for (int i = 0; i < result.get_height(); i++)
	{
		for (int j = 0; j < result.get_width(); j++)
		{
			if (result.get_data(i,j) == 255)
				edges.push_back(std::make_pair(i, j));
		}
	}

	return edges;
}
