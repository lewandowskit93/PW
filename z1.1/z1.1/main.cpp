#include <cstdint>
#include <chrono>
#include <iostream>
#include <thread>

void emptyFunct()
{
  
}

int main()
{
  uint32_t repeats;
  std::cin >> repeats;

  std::chrono::high_resolution_clock::time_point th_start_time;
  std::chrono::high_resolution_clock::time_point th_end_time;
  std::chrono::microseconds th_duration;
  std::chrono::high_resolution_clock::time_point loop_start_time;
  std::chrono::high_resolution_clock::time_point loop_end_time;
  std::chrono::microseconds loop_duration;

  th_start_time = std::chrono::high_resolution_clock::now();
  for (uint32_t i = 0; i < repeats; ++i)
  {
    std::thread t(&emptyFunct);
    t.join();
  }
  th_end_time = std::chrono::high_resolution_clock::now();
  th_duration = std::chrono::duration_cast<std::chrono::microseconds>(th_end_time - th_start_time);

  loop_start_time = std::chrono::high_resolution_clock::now();
  for (uint32_t i = 0; i < repeats; ++i)
  {
  }
  loop_end_time = std::chrono::high_resolution_clock::now();
  loop_duration = std::chrono::duration_cast<std::chrono::microseconds>(loop_end_time - loop_start_time);

  th_duration -= loop_duration;

  std::cout <<repeats<<" : "<< th_duration.count() << " microsecs"<< std::endl;
  std::cout << "1 : " << th_duration.count() / static_cast<double>(repeats) << " microsecs" << std::endl;
}