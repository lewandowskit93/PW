
#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include "device_atomic_functions.h"

#include <iostream>

__device__ int getVertexEdgesCountDevice(int *edges_matrix,const int n,bool symetric, int x)
{
  int edges_count = 0;
  for (int i = (symetric ? x + 1 : 0); i < n; ++i)
  {
    if (x == i)continue;
    if (edges_matrix[x*n+i])++edges_count;
  }
  return edges_count;
}

__global__ void countEdgesKernel(int *edges_matrix, bool symetric,int* edges_count,const int n)
{
    int sub_count = 0;
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    int c_units = gridDim.x*blockDim.x;
    for (int i = id; i < n; i += c_units)
    {
      sub_count += getVertexEdgesCountDevice(edges_matrix, n, symetric, i);
    }
    atomicAdd(edges_count, sub_count);
}

int countEdgesCuda(int blocks, int threads, int *edges_matrix, bool symetric, const int n)
{
  int *dev_edges_matrix = 0;
  int *dev_edges_count = 0;
  cudaSetDevice(0);

  cudaMalloc((void**)&dev_edges_count, sizeof(int));
  int edges_count = 0;
  cudaMemcpy(dev_edges_count, &edges_count, sizeof(int), cudaMemcpyHostToDevice);

  cudaMalloc((void**)&dev_edges_matrix, sizeof(int)*n*n);
  cudaMemcpy(dev_edges_matrix, edges_matrix, sizeof(int)*n*n, cudaMemcpyHostToDevice);

  countEdgesKernel << <blocks, threads >> >(dev_edges_matrix, true, dev_edges_count, n);

  cudaMemcpy(&edges_count, dev_edges_count, sizeof(int), cudaMemcpyDeviceToHost);
  cudaFree(dev_edges_count);

  cudaFree(dev_edges_matrix);

  return edges_count;
}

int main()
{
  int blocks;
  int threads;
    int n;
    bool symetric;
    int nr_of_edges;
    std::cin >> blocks;
    std::cin >> threads;
    std::cin >> n;
    std::cin >> symetric;
    std::cin >> nr_of_edges;
    int *edges_matrix = new int[n*n];
    for (int i = 0; i < n; ++i)
    {
      for (int j = 0; j < n; ++j)
      {
        edges_matrix[i*n+j] = 0;
      }
    }
    for (int i = 0; i < nr_of_edges; ++i)
    {
      int x, y;
      std::cin >> x >> y;
      edges_matrix[x*n+y] = 1;
      if (symetric)edges_matrix[y*n+x] = 1;
    }

    std::cout << countEdgesCuda(blocks,threads,edges_matrix, symetric, n);

    return 0;
}
