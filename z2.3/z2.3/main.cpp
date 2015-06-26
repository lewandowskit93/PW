#include <cstdint>
#include <atomic>
#include <thread>
#include <vector>
#include <iostream>

using namespace std;

void sumElementsToBuffer(int32_t* array, uint32_t p, uint32_t k, atomic<int64_t> * buf)
{
  if (buf)
  {
    for (auto i = p; i < k;++i)
      buf->fetch_add(array[i]);
  }
}

int64_t sumElements(int32_t* array, uint32_t n, uint32_t nr_of_threads)
{
  atomic<int64_t> buf;
  buf.store(0);
  vector<thread> threads;
  uint32_t indexes_per_thread = (n + nr_of_threads - 1) / nr_of_threads;
  for (auto i = 0;i<nr_of_threads; ++i)
  {
    uint32_t p = i*indexes_per_thread;
    uint32_t k = (i + 1)*indexes_per_thread;
    if (k>n)k = n;
    threads.push_back(thread(sumElementsToBuffer,array, p, k,&buf));
  }
  for (auto i = 0; i < nr_of_threads;++i)
  {
    threads[i].join();
  }
  return buf.load();
}


int main()
{
  uint32_t n;
  uint32_t nr_threads;
  cin >> n >> nr_threads;
  int32_t *array = new int32_t[n];
  for (auto i = 0; i < n;++i)
  {
    cin >> array[i];
  }
  uint64_t sum;
  sum = sumElements(array, n, nr_threads);
  cout << sum;
}