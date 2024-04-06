#include "ImageMatrix.h"
#include <iostream>
#include <cmath>


// Default constructor
ImageMatrix::ImageMatrix() : height(0), width(0), data(nullptr)
{

}


// Parameterized constructor for creating a blank image of given size
ImageMatrix::ImageMatrix(int imgHeight, int imgWidth) : height(imgHeight), width(imgWidth)
{
	data = new double* [height];
	for (int i = 0; i < height; ++i)
	{
		data[i] = new double[width]();
	}
}

// Parameterized constructor for loading image from file. PROVIDED FOR YOUR CONVENIENCE
ImageMatrix::ImageMatrix(const std::string& filepath)
{
	// Create an ImageLoader object and load the image
	ImageLoader imageLoader(filepath);

	// Get the dimensions of the loaded image
	height = imageLoader.getHeight();
	width = imageLoader.getWidth();

	// Allocate memory for the matrix
	data = new double* [height];
	for (int i = 0; i < height; ++i)
	{
		data[i] = new double[width];
	}

	// Copy data from imageLoader to data
	double** imageData = imageLoader.getImageData();
	for (int i = 0; i < height; ++i)
	{
		for (int j = 0; j < width; j++)
		{
			data[i][j] = imageData[i][j];
		}
	}
}



// Destructor
ImageMatrix::~ImageMatrix()
{
	delete_data();
}

// Parameterized constructor - direct initialization with 2D matrix
ImageMatrix::ImageMatrix(const double** inputMatrix, int imgHeight, int imgWidth) : height(imgHeight), width(imgWidth)
{
	data = new double* [height];
	for (int i = 0; i < height; ++i)
	{
		data[i] = new double[width];
	}
	for (int i = 0; i < height; ++i)
	{
		for (int j = 0; j < width; j++)
		{
			data[i][j] = inputMatrix[i][j];
		}
	}
}

// Copy constructor
// If we do not assign data to nullptr first, it gets assigned to a junk value
// and then we try to reset the data we try to delete that junk value which causes a crash
ImageMatrix::ImageMatrix(const ImageMatrix& other) : data(nullptr)
{
	reset_data(other.data, other.height, other.width);
}


void ImageMatrix::delete_data() const
{
	for (int i = 0; i < height; i++)
		delete[] data[i];
	delete[] data;
}

void ImageMatrix::print_data() const
{
	std::cout << "Data Adress: " << data << "\n";

	if (data == nullptr)
		return;

	std::cout << "First Row Adress: " << data[0] << "\n";
	std::cout << "Height: " << height << "\n";
	std::cout << "Width: " << width << "\n";

	for (int i = 0; i < height; i++)
	{
		for (int j = 0; j < width; j++)
		{
			std::cout << std::ceil(data[i][j]) << " ";
		}
		std::cout << "\n";
	}

	std::cout << "\n";
}


// Copy assignment operator
ImageMatrix& ImageMatrix::operator=(const ImageMatrix& other)
{
	if (this == &other)
		return *this;

	reset_data(other.data, other.height, other.width);

	return *this;
}



// Overloaded operators

// Overloaded operator + to add two matrices
ImageMatrix ImageMatrix::operator+(const ImageMatrix& other) const
{
	ImageMatrix result(height, width);

	for (int i = 0; i < height; i++)
	{
		for (int j = 0; j < width; j++)
		{
			result.data[i][j] = data[i][j] + other.data[i][j];
		}
	}

	return result;
}

// Overloaded operator - to subtract two matrices
ImageMatrix ImageMatrix::operator-(const ImageMatrix& other) const
{
	ImageMatrix result(height, width);

	for (int i = 0; i < height; i++)
	{
		for (int j = 0; j < width; j++)
		{
			result.data[i][j] = data[i][j] - other.data[i][j];
		}
	}

	return result;
}

ImageMatrix ImageMatrix::operator*(const ImageMatrix& other) const
{
	ImageMatrix result(height, width);

	for (int i = 0; i < height; i++)
	{
		for (int j = 0; j < width; j++)
		{
			result.data[i][j] = data[i][j] * other.data[i][j];
		}
	}

	return result;
}

// Overloaded operator * to multiply a matrix with a scalar
ImageMatrix ImageMatrix::operator*(const double& scalar) const
{
	ImageMatrix result(height, width);

	for (int i = 0; i < height; i++)
	{
		for (int j = 0; j < width; j++)
		{
			result.data[i][j] = data[i][j] * scalar;
		}
	}

	return result;
}


ImageMatrix ImageMatrix::sqrt()
{
	ImageMatrix result(height, width);

	for (int i = 0; i < height; i++)
	{
		for (int j = 0; j < width; j++)
		{
			result.data[i][j] = std::sqrt(data[i][j]);
		}
	}

	return result;
}

// Getter function to access the data in the matrix
double** ImageMatrix::get_data() const
{
	return data;
}

// Getter function to access the data at the index (i, j)
double ImageMatrix::get_data(int i, int j) const
{
	return data[i][j];
}

void ImageMatrix::reset_data(double** inputData, int dataHeight, int dataWidth)
{
	height = dataHeight;
	width = dataWidth;

	if (data != nullptr)
		delete_data();
	data = new double* [height];
	for (int i = 0; i < height; i++)
	{
		data[i] = new double[width];
		for (int j = 0; j < width; j++)
		{
			data[i][j] = inputData[i][j];
		}
	}
}

void ImageMatrix::set_data(double value, int i, int j)
{
	data[i][j] = value;
}

void ImageMatrix::add_zero_padding()
{
	double** newData = new double* [height + 2];
	for (int i = 0; i < height + 2; ++i)
	{
		newData[i] = new double[width + 2];
	}

	for (int i = 0; i < height + 2; ++i)
	{
		for (int j = 0; j < width + 2; j++)
		{
			if (i == 0 || i == height + 1 || j == 0 || j == width + 1)
				newData[i][j] = 0;
			else
				newData[i][j] = data[i - 1][j - 1];
		}
	}

	delete_data();
	data = newData;
	height += 2;
	width += 2;
}

void ImageMatrix::threshold(double thresholdValue)
{
	for (int i = 0; i < height; ++i)
	{
		for (int j = 0; j < width; j++)
		{
			if (data[i][j] < thresholdValue)
				data[i][j] = 0;
			else
				data[i][j] = 255;
		}
	}
}

int ImageMatrix::get_height() const
{
	return height;
}

int ImageMatrix::get_width() const
{
	return width;
}

double ImageMatrix::get_average()
{
	double result = 0;

	for (int i = 0; i < height; i++)
	{
		for (int j = 0; j < width; j++)
		{
			result += data[i][j];
		}
	}

	result /= (height * width);
	return result;
}
