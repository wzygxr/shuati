#include <vector>
#include <iostream>
#include <chrono>

/**
 * LeetCode 304. 二维区域和检索 - 矩阵不可变 (Range Sum Query 2D - Immutable)
 * 
 * 题目描述:
 * 给定一个二维矩阵 matrix，计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2) 。
 * 你可以假设矩阵不可变。
 * 会多次调用 sumRegion 方法。
 * 
 * 示例1:
 * 输入:
 * matrix = [
 *   [3, 0, 1, 4, 2],
 *   [5, 6, 3, 2, 1],
 *   [1, 2, 0, 1, 5],
 *   [4, 1, 0, 1, 7],
 *   [1, 0, 3, 0, 5]
 * ]
 * sumRegion(2, 1, 4, 3) -> 8
 * sumRegion(1, 1, 2, 2) -> 11
 * sumRegion(1, 2, 2, 4) -> 12
 * 
 * 提示:
 * 1. 你可以假设矩阵的长和宽不超过 200 。
 * 2. sumRegion 函数会被调用多次。
 * 
 * 题目链接: https://leetcode.com/problems/range-sum-query-2d-immutable/
 * 
 * 解题思路:
 * 这个问题可以使用二维前缀和来解决：
 * 1. 预处理矩阵，计算每个位置 (i, j) 到 (0, 0) 的矩形区域内所有元素的和，存储在 prefixSum 数组中
 * 2. 利用前缀和数组，可以在 O(1) 时间内计算任意子矩阵的和
 * 
 * 二维前缀和的计算公式：
 * prefixSum[i][j] = matrix[i-1][j-1] + prefixSum[i-1][j] + prefixSum[i][j-1] - prefixSum[i-1][j-1]
 * 
 * 子矩阵和的计算公式：
 * sumRegion(row1, col1, row2, col2) = prefixSum[row2+1][col2+1] - prefixSum[row1][col2+1] - prefixSum[row2+1][col1] + prefixSum[row1][col1]
 * 
 * 时间复杂度:
 * - 构造函数: O(m*n)，其中 m 是矩阵的行数，n 是矩阵的列数
 * - sumRegion 方法: O(1)
 * 
 * 空间复杂度: O(m*n)，用于存储前缀和数组
 * 
 * 这是最优解，因为我们需要在 O(1) 时间内回答任意子矩阵和查询，预处理是必要的，且预处理的时间复杂度已经是最优的。
 */

class NumMatrix {
private:
    // 前缀和数组，prefixSum[i][j] 表示从 (0,0) 到 (i-1,j-1) 的矩形区域内所有元素的和
    std::vector<std::vector<int>> prefixSum;
    int rows;
    int cols;

public:
    /**
     * 初始化 NumMatrix 对象
     * 
     * @param matrix 输入的二维矩阵
     */
    NumMatrix(std::vector<std::vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            prefixSum = std::vector<std::vector<int>>();
            rows = 0;
            cols = 0;
            return;
        }
        
        rows = matrix.size();
        cols = matrix[0].size();
        
        // 创建前缀和数组，比原矩阵多一行一列，便于处理边界情况
        prefixSum.resize(rows + 1, std::vector<int>(cols + 1, 0));
        
        // 计算前缀和数组
        for (int i = 1; i <= rows; ++i) {
            for (int j = 1; j <= cols; ++j) {
                // 当前位置的值 = 原矩阵对应位置的值 + 上方区域的和 + 左方区域的和 - 左上重叠区域的和
                prefixSum[i][j] = matrix[i - 1][j - 1] + prefixSum[i - 1][j] + 
                                 prefixSum[i][j - 1] - prefixSum[i - 1][j - 1];
            }
        }
    }
    
    /**
     * 计算从 (row1, col1) 到 (row2, col2) 的矩形区域内所有元素的和
     * 
     * @param row1 左上角行索引
     * @param col1 左上角列索引
     * @param row2 右下角行索引
     * @param col2 右下角列索引
     * @return 子矩阵内所有元素的和
     */
    int sumRegion(int row1, int col1, int row2, int col2) {
        // 边界检查
        if (prefixSum.empty() || prefixSum[0].empty()) {
            return 0;
        }
        
        // 确保索引有效
        row1 = std::max(0, row1);
        col1 = std::max(0, col1);
        row2 = std::min(rows - 1, row2);
        col2 = std::min(cols - 1, col2);
        
        if (row1 > row2 || col1 > col2) {
            return 0;
        }
        
        // 使用前缀和公式计算子矩阵和
        // 子矩阵和 = 右下角前缀和 - 左上角上方前缀和 - 左上角左方前缀和 + 左上角前缀和
        return prefixSum[row2 + 1][col2 + 1] - prefixSum[row1][col2 + 1] - 
               prefixSum[row2 + 1][col1] + prefixSum[row1][col1];
    }
    
    /**
     * 获取前缀和数组，用于调试
     * 
     * @return 前缀和数组
     */
    std::vector<std::vector<int>> getPrefixSum() const {
        return prefixSum;
    }
};

/**
 * 打印矩阵，用于调试
 * 
 * @param matrix 要打印的矩阵
 */
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

// 主函数，用于测试
int main() {
    // 测试用例1
    std::vector<std::vector<int>> matrix1 = {
        {3, 0, 1, 4, 2},
        {5, 6, 3, 2, 1},
        {1, 2, 0, 1, 5},
        {4, 1, 0, 1, 7},
        {1, 0, 3, 0, 5}
    };
    
    NumMatrix numMatrix1(matrix1);
    std::cout << "测试用例1 - sumRegion(2, 1, 4, 3): " << numMatrix1.sumRegion(2, 1, 4, 3) << std::endl; // 预期输出: 8
    std::cout << "测试用例1 - sumRegion(1, 1, 2, 2): " << numMatrix1.sumRegion(1, 1, 2, 2) << std::endl; // 预期输出: 11
    std::cout << "测试用例1 - sumRegion(1, 2, 2, 4): " << numMatrix1.sumRegion(1, 2, 2, 4) << std::endl; // 预期输出: 12
    
    // 测试用例2 - 空矩阵
    std::vector<std::vector<int>> matrix2 = {};
    NumMatrix numMatrix2(matrix2);
    std::cout << "测试用例2 - 空矩阵: " << numMatrix2.sumRegion(0, 0, 0, 0) << std::endl; // 预期输出: 0
    
    // 测试用例3 - 只有一个元素的矩阵
    std::vector<std::vector<int>> matrix3 = {{5}};
    NumMatrix numMatrix3(matrix3);
    std::cout << "测试用例3 - 单元素矩阵: " << numMatrix3.sumRegion(0, 0, 0, 0) << std::endl; // 预期输出: 5
    
    // 测试用例4 - 边界情况
    std::cout << "测试用例4 - 越界索引: " << numMatrix1.sumRegion(-1, -1, 10, 10) << std::endl; // 预期输出: 应该正确处理越界
    
    // 测试用例5 - 多次调用
    auto startTime = std::chrono::high_resolution_clock::now();
    int total = 0;
    for (int i = 0; i < 1000; ++i) {
        total += numMatrix1.sumRegion(0, 0, 4, 4);
    }
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    
    std::cout << "测试用例5 - 多次调用结果: " << total << std::endl;
    std::cout << "测试用例5 - 多次调用耗时: " << duration.count() << "ms" << std::endl;
    
    return 0;
}