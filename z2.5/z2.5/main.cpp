#include <future>
#include <cstdint>
#include <iostream>
#include <vector>

using namespace std;

bool containsT(int32_t *array, uint32_t n, int32_t elem, uint32_t thread_nr, uint32_t nr_threads)
{
  for (auto i = thread_nr; i < n;i+=nr_threads)
  {
    if (array[i] == elem)return true;
  }
  return false;
}

bool contains(int32_t *array, uint32_t n, int32_t elem, uint32_t nr_threads)
{
  vector<future<bool>> results;
  results.reserve(nr_threads);
  for (auto i = 0; i < nr_threads;++i)
  {
    results.push_back(async(containsT, array, n, elem, i, nr_threads));
  }
  for (auto i = 0; i < nr_threads; ++i)
  {
    if (results[i].get() == true)return true;
  }
  return false;
}

int main()
{
  uint32_t n;
  uint32_t nr_threads;
  int32_t elem;
  cin >> n >> elem >> nr_threads;
  int32_t *array = new int32_t[n];
  for (auto i = 0; i < n;++i)
  {
    cin >> array[i];
  }
  cout << contains(array, n, elem, nr_threads)<<endl;
  delete [] array;
}