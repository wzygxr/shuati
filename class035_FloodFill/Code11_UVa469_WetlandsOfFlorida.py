#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
UVa 469 - Wetlands of Florida
Problem Link: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=6&page=show_problem&problem=410

Problem Description:
Given a grid of 'L' and 'W', where 'L' represents land and 'W' represents water (wetlands).
8-connected 'W' cells form a wetland. For each query point, calculate the size of the wetland containing that point.

Solution Approach:
This is a dynamic Flood Fill problem where we need to calculate the size of the connected component for each query point.
Differences from POJ 2386:
1. Need to handle multiple queries for different points
2. Query point may be land ('L'), return 0 in this case
3. Need to preserve the original grid, cannot modify in-place

Solution:
1. For each query point, use Flood Fill algorithm to calculate connected component size
2. Use auxiliary visited array to avoid re-visiting
3. Use 8-connectivity to determine adjacency

Time Complexity: O(Q*N*M) - Q queries, each worst case traverses entire grid
Space Complexity: O(N*M) - space for visited array
Is Optimal: Not optimal for multiple queries, can be optimized with preprocessing

Engineering Considerations:
1. Error handling: Check for empty input, out-of-bounds coordinates
2. Edge cases: Handle query point being land
3. Configurability: Support 4-connectivity and 8-connectivity switching

Language Differences:
Java: Object references and garbage collection
C++: Pointer operations and manual memory management
Python: Dynamic typing and automatic memory management

Extreme Input Cases:
1. Empty grid
2. All 'W' grid
3. All 'L' grid
4. Query point on boundary

Performance Optimizations:
1. Preprocess all connected components and number them, direct lookup for queries
2. Use Union-Find to optimize multiple queries
3. Early termination condition checks

Connections to Other Algorithms:
1. DFS/BFS: Core algorithm
2. Union-Find: Optimize multiple queries
3. Graph Theory: Connected components problem
"""

from typing import List


class Solution:
    def __init__(self):
        # 8-directional offsets: up, down, left, right and 4 diagonals
        self.dx = [-1, -1, -1, 0, 0, 1, 1, 1]
        self.dy = [-1, 0, 1, -1, 1, -1, 0, 1]
    
    def wetland_size(self, grid: List[List[str]], row: int, col: int) -> int:
        """
        Calculate the size of wetland containing the specified point
        
        Args:
            grid: Grid matrix, 'L' for land, 'W' for water
            row: Query point row coordinate (1-based)
            col: Query point column coordinate (1-based)
            
        Returns:
            Size of wetland
        """
        # Boundary condition check
        if not grid or not grid[0]:
            return 0
        
        rows, cols = len(grid), len(grid[0])
        
        # Convert to 0-based coordinates
        x = row - 1
        y = col - 1
        
        # Check if coordinates are out of bounds
        if x < 0 or x >= rows or y < 0 or y >= cols:
            return 0
        
        # If query point is land, return 0
        if grid[x][y] == 'L':
            return 0
        
        # Use auxiliary array to mark visited status
        visited = [[False] * cols for _ in range(rows)]
        
        # Use Flood Fill to calculate connected component size
        return self._dfs(grid, x, y, rows, cols, visited)
    
    def _dfs(self, grid: List[List[str]], x: int, y: int, rows: int, cols: int, 
             visited: List[List[bool]]) -> int:
        """
        Depth-first search to calculate connected component size
        
        Args:
            grid: Grid matrix
            x: Current row coordinate
            y: Current column coordinate
            rows: Number of rows
            cols: Number of columns
            visited: Visited marking array
            
        Returns:
            Size of connected component
        """
        # Boundary check, value check and visit check
        if (x < 0 or x >= rows or y < 0 or y >= cols or 
            grid[x][y] != 'W' or visited[x][y]):
            return 0
        
        # Mark as visited
        visited[x][y] = True
        
        # Calculate contribution of current cell (1) plus contributions from 8 adjacent cells
        size = 1
        for i in range(8):
            new_x = x + self.dx[i]
            new_y = y + self.dy[i]
            size += self._dfs(grid, new_x, new_y, rows, cols, visited)
        
        return size


# Test method
def print_grid(grid: List[List[str]]) -> None:
    """Print grid"""
    for row in grid:
        print(' '.join(row))


def main():
    solution = Solution()
    
    # Test case 1
    grid1 = [
        ['L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'],
        ['L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'],
        ['L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'],
        ['L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'],
        ['L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'],
        ['L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'],
        ['L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'],
        ['L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'],
        ['L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'],
        ['L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L']
    ]
    
    print("Test case 1:")
    print("Grid:")
    print_grid(grid1)
    print(f"Wetland size at (2,2): {solution.wetland_size(grid1, 2, 2)}")
    print(f"Wetland size at (5,5): {solution.wetland_size(grid1, 5, 5)}")
    print(f"Wetland size at (1,1): {solution.wetland_size(grid1, 1, 1)}")
    
    # Test case 2
    grid2 = [
        ['W', 'W', 'W'],
        ['W', 'L', 'W'],
        ['W', 'W', 'W']
    ]
    
    print("\nTest case 2:")
    print("Grid:")
    print_grid(grid2)
    print(f"Wetland size at (2,2): {solution.wetland_size(grid2, 2, 2)}")
    print(f"Wetland size at (1,1): {solution.wetland_size(grid2, 1, 1)}")


if __name__ == "__main__":
    main()