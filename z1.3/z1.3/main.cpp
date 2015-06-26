#include <omp.h>
#include <stdint.h>
#include <iostream>
#include <chrono>

class EdgesMatrix
{
public:

	EdgesMatrix(uint32_t n)
		: EdgesMatrix(n,true)
	{

	}
	EdgesMatrix(uint32_t n, bool symetric)
		: _n(n), _symetric(symetric), _edges_matrix(0)
	{
		_edges_matrix = new bool*[_n];
		for (uint32_t i = 0; i < _n; ++i)
		{
			_edges_matrix[i] = new bool[_n];
			for (uint32_t j = 0; j < _n; ++j)
			{
				_edges_matrix[i][j] = false;
			}
		}
	}

	bool hasEdge(uint32_t x, uint32_t y)
	{
		return _edges_matrix[x][y];
	}

	void addEdge(uint32_t x, uint32_t y)
	{
		_edges_matrix[x][y] = true;
		if (_symetric)
		{
			_edges_matrix[y][x] = true;
		}
	}

	void removeEdge(uint32_t x, uint32_t y)
	{
		_edges_matrix[x][y] = false;
		if (_symetric)
		{
			_edges_matrix[y][x] = false;
		}
	}

	uint32_t getVertexEdgesCount(uint32_t x)
	{
		uint32_t edges_count = 0;
		for (uint32_t i = (_symetric ? x+1 : 0); i < _n; ++i)
		{
			if (x==i)continue;
			if (hasEdge(x, i))++edges_count;
		}
		return edges_count;
	}

	uint32_t countEdges(bool sequential)
	{
		return sequential ? countEdgesSequential() : countEdgesParallel();
	}

	uint32_t countEdgesSequential()
	{
		uint32_t edges_count = 0;
		for (uint32_t i = 0; i < _n; ++i)
		{
			edges_count+=getVertexEdgesCount(i);
		}
		return edges_count;
	}

	uint32_t countEdgesParallel()
	{
		omp_lock_t counter_lock;
		omp_init_lock(&counter_lock);

		uint32_t edges_count=0;
#pragma omp parallel num_threads(_n) shared(edges_count)
		{
      uint32_t sub_count = 0;
      for (uint32_t i = omp_get_thread_num(); i < _n;i+=omp_get_num_threads())
      {
        sub_count += getVertexEdgesCount(i);
      }
			omp_set_lock(&counter_lock);
			edges_count += sub_count;
			omp_unset_lock(&counter_lock);
		}
		omp_destroy_lock(&counter_lock);
		return edges_count;
	}

	~EdgesMatrix()
	{
		if (_edges_matrix)
		{
			for (uint32_t i = 0; i < _n; ++i)
			{
				if(_edges_matrix[i]) delete [] _edges_matrix[i];
			}
			delete[] _edges_matrix;
		}
	}

protected:
private:
	uint32_t _n;
	bool _symetric;
	bool **_edges_matrix;
};

void main()
{
	uint32_t repeats;
	uint32_t n;
	bool symetric;
	uint32_t nrOfEdges;
	std::cin >> repeats >> n >> symetric >> nrOfEdges;
	EdgesMatrix graph(n, symetric);
	for (uint32_t i = 0; i < nrOfEdges; ++i)
	{
		uint32_t x, y;
		std::cin >> x >> y;
		graph.addEdge(x, y);
	}

	std::chrono::high_resolution_clock::time_point seq_start_time;
	std::chrono::high_resolution_clock::time_point seq_end_time;
	std::chrono::microseconds seq_duration;
	std::chrono::high_resolution_clock::time_point par_start_time;
	std::chrono::high_resolution_clock::time_point par_end_time;
	std::chrono::microseconds par_duration;
  std::chrono::high_resolution_clock::time_point loop_start_time;
  std::chrono::high_resolution_clock::time_point loop_end_time;
  std::chrono::microseconds loop_duration;

	seq_start_time = std::chrono::high_resolution_clock::now();
  for (uint32_t i = 0; i < repeats; ++i)
  {
    graph.countEdgesSequential();
  }
	seq_end_time = std::chrono::high_resolution_clock::now();
	seq_duration = std::chrono::duration_cast<std::chrono::microseconds>(seq_end_time - seq_start_time);

	par_start_time = std::chrono::high_resolution_clock::now();
  for (uint32_t i = 0; i < repeats; ++i)
  {
    graph.countEdgesParallel();
  }
	par_end_time = std::chrono::high_resolution_clock::now();
	par_duration = std::chrono::duration_cast<std::chrono::microseconds>(par_end_time - par_start_time);

  loop_start_time = std::chrono::high_resolution_clock::now();
  for (uint32_t i = 0; i < repeats; ++i)
  {
  }
  loop_end_time = std::chrono::high_resolution_clock::now();
  loop_duration = std::chrono::duration_cast<std::chrono::microseconds>(loop_end_time - loop_start_time);

  seq_duration -= loop_duration;
  par_duration -= loop_duration;

  std::cout << repeats << std::endl << n << std::endl << symetric << std::endl;
  std::cout << " seq " << repeats << " " << seq_duration.count() << " microseconds" << std::endl;
  std::cout << " seq " << 1 << " " << seq_duration.count() / static_cast<double>(repeats) << " microseconds" << std::endl;
  std::cout << " par " << repeats << " " << par_duration.count() << " microseconds" << std::endl;
  std::cout << " par " << 1 << " " << par_duration.count() / static_cast<double>(repeats) << " microseconds" << std::endl;
}