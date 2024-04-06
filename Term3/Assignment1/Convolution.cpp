#include <iostream>

#include "Convolution.h"

// Default constructor 
Convolution::Convolution()
	: customKernel(nullptr), kernelHeight(0), kernelWidth(0), stride(0), padding(false)
{
}

// Parametrized constructor for custom kernel and other parameters
Convolution::Convolution(double** customKernel, int kernelHeight, int kernelWidth, int stride, bool padding)
{
	reset_data(customKernel, kernelHeight, kernelWidth, stride, padding);
}

// Destructor
Convolution::~Convolution()
{
	delete_data();
}

// Copy constructor
Convolution::Convolution(const Convolution& other)
{
	reset_data(other.customKernel, other.kernelHeight, other.kernelWidth, other.stride, other.padding);
}

// Copy assignment operator
Convolution& Convolution::operator=(const Convolution& other)
{
	if (this == &other)
		return *this;

	reset_data(other.customKernel, other.kernelHeight, other.kernelWidth, other.stride, other.padding);

	return *this;
}


// Convolve Function: Responsible for convolving the input image with a kernel and return the convolved image.
ImageMatrix Convolution::convolve(const ImageMatrix& input_image) const
{
	ImageMatrix image = input_image;
	
	ImageMatrix result((input_image.get_height() - kernelHeight + 2 * padding) / stride + 1, (input_image.get_width() - kernelWidth + 2 * padding) / stride + 1);

	if (padding)
		image.add_zero_padding();

	for (int i = 0; i < result.get_height(); i++)
	{
		for (int j = 0; j < result.get_width(); j++)
		{
			double kernelled = get_kernelled(image, i, j);
			result.set_data(kernelled, i, j);
		}
	}

	return result;
}

void Convolution::delete_data() const
{
	for (int i = 0; i < kernelHeight; i++)
		delete[] customKernel[i];
	delete[] customKernel;
}

void Convolution::reset_data(double** customKernel, int kernelHeight, int kernelWidth, int stride, int padding)
{
	this->kernelHeight = kernelHeight;
	this->kernelWidth = kernelWidth;
	this->stride = stride;
	this->padding = padding;

	if (customKernel == nullptr)
		delete_data();
	this->customKernel = new double* [this->kernelHeight];
	for (int i = 0; i < this->kernelHeight; i++)
	{
		this->customKernel[i] = new double[this->kernelWidth];
		for (int j = 0; j < this->kernelWidth; j++)
		{
			this->customKernel[i][j] = customKernel[i][j];
		}
	}
}



double Convolution::get_kernelled(const ImageMatrix& image, int i, int j) const
{
	double sum = 0;

	for (int m = 0; m < kernelHeight; m++)
	{
		for (int n = 0; n < kernelWidth; n++)
		{
			sum += customKernel[m][n] * image.get_data(i * stride + m, j * stride + n);
		}
	}

	return sum;
}