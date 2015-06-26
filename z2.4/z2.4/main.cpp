#include <cstdint>
#include <atomic>
#include <thread>
#include <vector>
#include <iostream>

using namespace std;

class Histogram
{
public:
  Histogram()
  {
    for (auto i = 0; i < 256; ++i)
    {
      _histogram[i].store(0);
    }
  }

  vector<uint64_t> calculate(uint8_t** array, uint32_t width, uint32_t height, uint32_t nr_of_threads)
  {
    initialize();
    vector<thread> threads;
    threads.reserve(nr_of_threads);
    for (auto i = 0; i<nr_of_threads; ++i)
    {
      threads.push_back(thread(&Histogram::calculatePart,this,array, width,height,i,nr_of_threads));
    }
    for (auto i = 0; i < nr_of_threads; ++i)
    {
      threads[i].join();
    }
    vector<uint64_t> histogram;
    histogram.reserve(256);
    for (auto i = 0; i < 256;++i)
    {
      histogram.push_back(_histogram[i].load());
    }
    return histogram;
  }

protected:
private:
  void calculatePart(uint8_t** array, uint32_t width, uint32_t height, uint32_t thread_number, uint32_t nr_of_threads)
  {
    uint32_t indexes_per_thread = (width*height + nr_of_threads - 1) / nr_of_threads;
    for (uint32_t calculated = 0; calculated<indexes_per_thread; ++calculated)
    {
      int j = (indexes_per_thread*thread_number + calculated) / width;
      if (j >= height)break;
      int i = (indexes_per_thread*thread_number+calculated) % width;
      ++_histogram[array[j][i]];
    }
  }

  void initialize()
  {
    for (auto i = 0; i < 256; ++i)
    {
      _histogram[i].store(0);
    }
  }
  atomic<uint64_t> _histogram[256];
};


int main()
{
  uint32_t width, height;
  uint32_t nr_threads;
  cin >> width >> height >> nr_threads;
  uint8_t **array = new uint8_t*[height];
  for (auto j = 0; j < height;++j)
  {
    array[j] = new uint8_t[width];
    for (auto i = 0; i < width; ++i)
    {
      uint16_t k;
      cin >> k;
      array[j][i] = k % 256;
    }
  }
  Histogram h;
  vector<uint64_t> results = h.calculate(array, width, height, nr_threads);
  cout << endl;
  for (auto i = 0; i < results.size();++i)
  {
    cout << i << ":"<<static_cast<uint16_t>(results[i])<< endl;
  }
}