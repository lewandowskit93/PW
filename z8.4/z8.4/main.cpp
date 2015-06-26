#include <iostream>
#include <cstdint>
#include <iomanip>

int main()
{
  uint32_t steps;
  std::cin >> steps;
  double f = 1;
  double e = 0;
  for (uint32_t i = 0; i < steps;++i)
  {
    if (i != 0)f = f*i;
    e += 1 / f;
  }
  std::cout << std::setprecision(10) << e << std::endl;
}