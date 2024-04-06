#include "ImageProcessor.h"

ImageProcessor::ImageProcessor()
{

}

ImageProcessor::~ImageProcessor()
{

}


std::string ImageProcessor::decodeHiddenMessage(const ImageMatrix& img)
{
	ImageMatrix sharpened_img = ImageSharpening().sharpen(img, 2);
	std::vector<std::pair<int,int>> edges = EdgeDetector().detectEdges(sharpened_img);

	return DecodeMessage().decodeFromImage(img ,edges);
}

ImageMatrix ImageProcessor::encodeHiddenMessage(const ImageMatrix& img, const std::string& message)
{
	ImageMatrix sharpened_img = ImageSharpening().sharpen(img, 2);
	std::vector<std::pair<int, int>> edges = EdgeDetector().detectEdges(sharpened_img);

	return EncodeMessage().encodeMessageToImage(img, message, edges);
}
