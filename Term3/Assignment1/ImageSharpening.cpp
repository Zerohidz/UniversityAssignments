#include "ImageSharpening.h"

// Default constructor
ImageSharpening::ImageSharpening()
{
	blurring_kernel = new double* [3] {
		new double[3] { 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0 },
		new double[3] { 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0 },
		new double[3] { 1.0 / 9.0, 1.0 / 9.0, 1.0 / 9.0 }
	};
	kernel_height = 3;
	kernel_width = 3;
}

ImageSharpening::~ImageSharpening()
{
	for (int i = 0; i < 3; i++)
		delete[] blurring_kernel[i];
	delete[] blurring_kernel;
}

ImageMatrix ImageSharpening::sharpen(const ImageMatrix& input_image, double k)
{
	Convolution blurring_conv(blurring_kernel, kernel_height, kernel_width, 1, true);
	ImageMatrix blurred_img = blurring_conv.convolve(input_image);

	ImageMatrix sharpened_image = input_image + (input_image - blurred_img) * k;
	for (int i = 0; i < input_image.get_height(); i++)
	{
		for (int j = 0; j < input_image.get_width(); j++)
		{
			if (sharpened_image.get_data(i, j) < 0) sharpened_image.set_data(0, i, j);
			if (sharpened_image.get_data(i, j) > 255) sharpened_image.set_data(255, i, j);
		}
	}
	return sharpened_image;
}
