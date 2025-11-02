#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

/**
 * 矩阵中的最长递增路径 - LeetCode 329
 * 题目来源：https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 难度：困难
 * 题目描述：给定一个 m x n 的整数矩阵 matrix ，找出其中最长递增路径的长度。
 * 对于每个单元格，你可以往上，下，左，右四个方向移动。 你不能在对角线方向上移动或移动到边界外（即不允许环绕）。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的二维变体，我们需要在矩阵中寻找最长的递增路径
 * 2. 使用深度优先搜索(DFS) + 记忆化搜索(Memoization)来避免重复计算
 * 3. 对于每个单元格，我们从四个方向进行探索，只考虑值严格大于当前单元格的相邻单元格
 * 4. 用一个dp数组存储每个单元格的最长递增路径长度，避免重复计算
 * 
 * 复杂度分析：
 * 时间复杂度：O(m*n)，其中m和n分别是矩阵的行数和列数。每个单元格只会被访问一次
 * 空间复杂度：O(m*n)，用于存储dp数组
 */

class Solution {
private:
    // 定义四个方向的移动：上、右、下、左
    const std::vector<std::vector<int>> DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    int rows;  // 矩阵行数
    int cols;  // 矩阵列数
    std::vector<std::vector<int>> matrix;  // 输入矩阵
    std::vector<std::vector<int>> dp;  // 记忆化搜索数组

    /**
     * 深度优先搜索函数（类成员变量版本）
     */
    int dfs(int i, int j) {
        // 如果已经计算过以(i,j)为起点的最长路径长度，直接返回
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        
        int maxLength = 1;  // 路径至少包含当前单元格，长度为1
        
        // 探索四个方向
        for (const auto& dir : DIRECTIONS) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            // 检查新位置是否有效，且值严格大于当前位置
            if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matrix[ni][nj] > matrix[i][j]) {
                // 递归计算从新位置出发的最长路径长度，并更新当前位置的最长路径长度
                int length = 1 + dfs(ni, nj);
                maxLength = std::max(maxLength, length);
            }
        }
        
        // 记忆化结果
        dp[i][j] = maxLength;
        return maxLength;
    }

public:
    /**
     * 最优解法：深度优先搜索 + 记忆化搜索
     * @param matrix 输入矩阵
     * @return 最长递增路径的长度
     */
    int longestIncreasingPath(const std::vector<std::vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            return 0;
        }
        
        int rows = matrix.size();
        int cols = matrix[0].size();
        std::vector<std::vector<int>> dp(rows, std::vector<int>(cols, 0));  // dp[i][j]表示以(i,j)为起点的最长递增路径长度
        int maxLength = 0;
        
        // 遍历每个单元格，寻找最长路径
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLength = std::max(maxLength, dfsHelper(matrix, dp, i, j, rows, cols));
            }
        }
        
        return maxLength;
    }
    
    /**
     * 深度优先搜索辅助函数，计算从(i,j)出发的最长递增路径长度
     */
    int dfsHelper(const std::vector<std::vector<int>>& matrix, std::vector<std::vector<int>>& dp,
                 int i, int j, int rows, int cols) {
        // 如果已经计算过以(i,j)为起点的最长路径长度，直接返回
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        
        int maxLength = 1;  // 路径至少包含当前单元格，长度为1
        
        // 探索四个方向
        for (const auto& dir : DIRECTIONS) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            // 检查新位置是否有效，且值严格大于当前位置
            if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matrix[ni][nj] > matrix[i][j]) {
                // 递归计算从新位置出发的最长路径长度，并更新当前位置的最长路径长度
                int length = 1 + dfsHelper(matrix, dp, ni, nj, rows, cols);
                maxLength = std::max(maxLength, length);
            }
        }
        
        // 记忆化结果
        dp[i][j] = maxLength;
        return maxLength;
    }
    
    /**
     * 另一种实现方式：使用类成员变量
     */
    int longestIncreasingPathAlternative(const std::vector<std::vector<int>>& inputMatrix) {
        if (inputMatrix.empty() || inputMatrix[0].empty()) {
            return 0;
        }
        
        this->rows = inputMatrix.size();
        this->cols = inputMatrix[0].size();
        this->matrix = inputMatrix;
        this->dp = std::vector<std::vector<int>>(rows, std::vector<int>(cols, 0));
        
        int maxLength = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLength = std::max(maxLength, dfs(i, j));
            }
        }
        
        return maxLength;
    }
    
    /**
     * 解释性更强的版本，添加了更多注释和中间变量
     */
    int longestIncreasingPathExplained(const std::vector<std::vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            return 0;
        }
        
        int rows = matrix.size();
        int cols = matrix[0].size();
        // 创建记忆化数组，存储每个单元格的最长递增路径长度
        std::vector<std::vector<int>> memo(rows, std::vector<int>(cols, 0));
        int longestPath = 0;
        
        // 对每个单元格进行DFS搜索
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 计算从当前单元格出发的最长递增路径
                int currentPathLength = dfsExplained(matrix, memo, i, j, rows, cols);
                // 更新全局最长路径
                longestPath = std::max(longestPath, currentPathLength);
            }
        }
        
        return longestPath;
    }
    
    /**
     * 带详细注释的深度优先搜索函数
     */
    int dfsExplained(const std::vector<std::vector<int>>& matrix, std::vector<std::vector<int>>& memo,
                    int row, int col, int rows, int cols) {
        // 检查记忆化数组，如果已经计算过则直接返回
        if (memo[row][col] > 0) {
            return memo[row][col];
        }
        
        // 初始化为1，因为路径至少包含当前单元格
        int maxPathFromHere = 1;
        
        // 定义四个方向的偏移量：上、右、下、左
        std::vector<std::vector<int>> directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        // 遍历所有可能的移动方向
        for (const auto& direction : directions) {
            // 计算新位置的坐标
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            
            // 检查新位置是否有效：
            // 1. 不超出矩阵边界
            // 2. 新位置的值严格大于当前位置（保持递增）
            bool isValidMove = (newRow >= 0 && newRow < rows && 
                              newCol >= 0 && newCol < cols && 
                              matrix[newRow][newCol] > matrix[row][col]);
            
            if (isValidMove) {
                // 递归计算从新位置出发的最长路径长度
                // 加上1是因为当前位置也要算在路径中
                int pathLength = 1 + dfsExplained(matrix, memo, newRow, newCol, rows, cols);
                // 更新最大值
                maxPathFromHere = std::max(maxPathFromHere, pathLength);
            }
        }
        
        // 记忆化结果，避免重复计算
        memo[row][col] = maxPathFromHere;
        return maxPathFromHere;
    }
};

// 辅助函数：打印矩阵
void printMatrix(const std::vector<std::vector<int>>& matrix) {
    for (const auto& row : matrix) {
        std::cout << "[";
        for (size_t j = 0; j < row.size(); ++j) {
            std::cout << row[j];
            if (j < row.size() - 1) {
                std::cout << ", ";
            }
        }
        std::cout << "]" << std::endl;
    }
}

// 运行所有解法的对比测试
void runAllSolutionsTest(const std::vector<std::vector<int>>& matrix) {
    Solution solution;
    std::cout << "\n对比测试：" << std::endl;
    printMatrix(matrix);
    
    // 测试DFS + 记忆化解法
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.longestIncreasingPath(matrix);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "DFS + 记忆化解法结果: " << result1 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试类成员变量版本
    start = std::chrono::high_resolution_clock::now();
    int result2 = solution.longestIncreasingPathAlternative(matrix);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "类成员变量版本结果: " << result2 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试解释性版本
    start = std::chrono::high_resolution_clock::now();
    int result3 = solution.longestIncreasingPathExplained(matrix);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "解释性版本结果: " << result3 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    std::cout << "----------------------------------------" << std::endl;
}

// 性能测试函数
void performanceTest(int size) {
    Solution solution;
    // 生成随机测试数据
    std::vector<std::vector<int>> matrix(size, std::vector<int>(size));
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            matrix[i][j] = rand() % 1000;
        }
    }
    
    std::cout << "\n性能测试：矩阵大小 = " << size << "x" << size << std::endl;
    
    // 测试DFS + 记忆化解法
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.longestIncreasingPath(matrix);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "DFS + 记忆化解法耗时: " << duration << " ms, 结果: " << result1 << std::endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<std::vector<int>> matrix1 = {
        {9, 9, 4},
        {6, 6, 8},
        {2, 1, 1}
    };
    std::cout << "测试用例1：" << std::endl;
    printMatrix(matrix1);
    std::cout << "结果: " << solution.longestIncreasingPath(matrix1) << "，预期: 4" << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<std::vector<int>> matrix2 = {
        {3, 4, 5},
        {3, 2, 6},
        {2, 2, 1}
    };
    std::cout << "测试用例2：" << std::endl;
    printMatrix(matrix2);
    std::cout << "结果: " << solution.longestIncreasingPath(matrix2) << "，预期: 4" << std::endl;
    std::cout << std::endl;
    
    // 测试用例3：边界情况
    std::vector<std::vector<int>> matrix3 = {{1}};
    std::cout << "测试用例3：" << std::endl;
    printMatrix(matrix3);
    std::cout << "结果: " << solution.longestIncreasingPath(matrix3) << "，预期: 1" << std::endl;
    
    // 运行所有解法的对比测试
    runAllSolutionsTest(matrix1);
    runAllSolutionsTest(matrix2);
    runAllSolutionsTest(matrix3);
    
    // 性能测试
    std::cout << "性能测试:" << std::endl;
    std::cout << "----------------------------------------" << std::endl;
    performanceTest(50);
    performanceTest(100);
    
    // 特殊测试用例：完全相同的元素
    std::cout << "\n特殊测试用例：完全相同的元素" << std::endl;
    std::vector<std::vector<int>> matrixSame = {
        {5, 5, 5},
        {5, 5, 5},
        {5, 5, 5}
    };
    printMatrix(matrixSame);
    std::cout << "结果: " << solution.longestIncreasingPath(matrixSame) << std::endl;
    
    // 特殊测试用例：严格递增的矩阵
    std::cout << "\n特殊测试用例：严格递增的矩阵" << std::endl;
    std::vector<std::vector<int>> matrixIncreasing = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };
    printMatrix(matrixIncreasing);
    std::cout << "结果: " << solution.longestIncreasingPath(matrixIncreasing) << std::endl;
    
    return 0;
}