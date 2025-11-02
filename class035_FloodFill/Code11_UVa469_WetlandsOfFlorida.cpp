#include <vector>
#include <iostream>
using namespace std;

/**
 * UVa 469 - Wetlands of Florida
 * Problem Link: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=6&page=show_problem&problem=410
 * 
 * Problem Description:
 * Given a grid of 'L' and 'W', where 'L' represents land and 'W' represents water (wetlands).
 * 8-connected 'W' cells form a wetland. For each query point, calculate the size of the wetland containing that point.
 * 
 * Solution Approach:
 * This is a dynamic Flood Fill problem where we need to calculate the size of the connected component for each query point.
 * Differences from POJ 2386:
 * 1. Need to handle multiple queries for different points
 * 2. Query point may be land ('L'), return 0 in this case
 * 3. Need to preserve the original grid, cannot modify in-place
 * 
 * Solution:
 * 1. For each query point, use Flood Fill algorithm to calculate connected component size
 * 2. Use auxiliary visited array to avoid re-visiting
 * 3. Use 8-connectivity to determine adjacency
 * 
 * Time Complexity: O(Q*N*M) - Q queries, each worst case traverses entire grid
 * Space Complexity: O(N*M) - space for visited array
 * Is Optimal: Not optimal for multiple queries, can be optimized with preprocessing
 * 
 * Engineering Considerations:
 * 1. Error handling: Check for empty input, out-of-bounds coordinates
 * 2. Edge cases: Handle query point being land
 * 3. Configurability: Support 4-connectivity and 8-connectivity switching
 * 
 * Language Differences:
 * Java: Object references and garbage collection
 * C++: Pointer operations and manual memory management
 * Python: Dynamic typing and automatic memory management
 * 
 * Extreme Input Cases:
 * 1. Empty grid
 * 2. All 'W' grid
 * 3. All 'L' grid
 * 4. Query point on boundary
 * 
 * Performance Optimizations:
 * 1. Preprocess all connected components and number them, direct lookup for queries
 * 2. Use Union-Find to optimize multiple queries
 * 3. Early termination condition checks
 * 
 * Connections to Other Algorithms:
 * 1. DFS/BFS: Core algorithm
 * 2. Union-Find: Optimize multiple queries
 * 3. Graph Theory: Connected components problem
 */
class Solution {
private:
    // 8-directional offsets: up, down, left, right and 4 diagonals
    const int dx[8] = {-1, -1, -1, 0, 0, 1, 1, 1};
    const int dy[8] = {-1, 0, 1, -1, 1, -1, 0, 1};
    
public:
    /**
     * Calculate the size of wetland containing the specified point
     * 
     * @param grid Grid matrix, 'L' for land, 'W' for water
     * @param row Query point row coordinate (1-based)
     * @param col Query point column coordinate (1-based)
     * @return Size of wetland
     */
    int wetlandSize(vector<vector<char>>& grid, int row, int col) {
        // Boundary condition check
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int rows = grid.size();
        int cols = grid[0].size();
        
        // Convert to 0-based coordinates
        int x = row - 1;
        int y = col - 1;
        
        // Check if coordinates are out of bounds
        if (x < 0 || x >= rows || y < 0 || y >= cols) {
            return 0;
        }
        
        // If query point is land, return 0
        if (grid[x][y] == 'L') {
            return 0;
        }
        
        // Use auxiliary array to mark visited status
        vector<vector<bool>> visited(rows, vector<bool>(cols, false));
        
        // Use Flood Fill to calculate connected component size
        return dfs(grid, x, y, rows, cols, visited);
    }
    
    /**
     * Depth-first search to calculate connected component size
     * 
     * @param grid Grid matrix
     * @param x Current row coordinate
     * @param y Current column coordinate
     * @param rows Number of rows
     * @param cols Number of columns
     * @param visited Visited marking array
     * @return Size of connected component
     */
    int dfs(vector<vector<char>>& grid, int x, int y, int rows, int cols, 
            vector<vector<bool>>& visited) {
        // Boundary check, value check and visit check
        if (x < 0 || x >= rows || y < 0 || y >= cols || 
            grid[x][y] != 'W' || visited[x][y]) {
            return 0;
        }
        
        // Mark as visited
        visited[x][y] = true;
        
        // Calculate contribution of current cell (1) plus contributions from 8 adjacent cells
        int size = 1;
        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            size += dfs(grid, newX, newY, rows, cols, visited);
        }
        
        return size;
    }
};

// Test method
void printGrid(const vector<vector<char>>& grid) {
    for (const auto& row : grid) {
        for (char cell : row) {
            cout << cell << " ";
        }
        cout << endl;
    }
}

int main() {
    Solution solution;
    
    // Test case 1
    vector<vector<char>> grid1 = {
        {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
        {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
        {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
        {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
        {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
        {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
        {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
        {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
        {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
        {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'}
    };
    
    cout << "Test case 1:" << endl;
    cout << "Grid:" << endl;
    printGrid(grid1);
    cout << "Wetland size at (2,2): " << solution.wetlandSize(grid1, 2, 2) << endl;
    cout << "Wetland size at (5,5): " << solution.wetlandSize(grid1, 5, 5) << endl;
    cout << "Wetland size at (1,1): " << solution.wetlandSize(grid1, 1, 1) << endl;
    
    // Test case 2
    vector<vector<char>> grid2 = {
        {'W', 'W', 'W'},
        {'W', 'L', 'W'},
        {'W', 'W', 'W'}
    };
    
    cout << "\nTest case 2:" << endl;
    cout << "Grid:" << endl;
    printGrid(grid2);
    cout << "Wetland size at (2,2): " << solution.wetlandSize(grid2, 2, 2) << endl;
    cout << "Wetland size at (1,1): " << solution.wetlandSize(grid2, 1, 1) << endl;
    
    return 0;
}