#include "Sector.h"
#include <cmath>
#include <string>

using namespace std;


Sector::Sector(int x, int y, int z) : x(x), y(y), z(z), left(nullptr), right(nullptr), parent(nullptr), color(RED)
{
	distance_from_earth = sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));

	sector_code = to_string((int)floor(distance_from_earth));
	sector_code += x == 0 ? 'S' : (x < 0 ? 'L' : 'R');
	sector_code += y == 0 ? 'S' : (y < 0 ? 'D' : 'U');
	sector_code += z == 0 ? 'S' : (z < 0 ? 'B' : 'F');
}

Sector::~Sector()
{
}

bool Sector::isRed()
{
	return color == RED;
}

Sector& Sector::operator=(const Sector& other)
{
	distance_from_earth = other.distance_from_earth;
	sector_code = other.sector_code;
	x = other.x;
	y = other.y;
	z = other.z;

	return *this;
}

bool Sector::operator==(const Sector& other) const
{
	return (x == other.x && y == other.y && z == other.z);
}

bool Sector::operator!=(const Sector& other) const
{
	return !(*this == other);
}