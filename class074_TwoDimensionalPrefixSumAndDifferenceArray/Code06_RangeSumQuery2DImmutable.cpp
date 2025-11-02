#include <vector>
#include <iostream>
using namespace std;

/**
 * 二维区域和检索 - 矩阵不可变问题
 * 
 * 问题描述：
 * 给定一个二维矩阵 matrix，处理多个查询，计算其子矩形范围内元素的总和。
 * 实现 NumMatrix 类：
 * - NumMatrix(vector<vector<int>>& matrix) 给定整数矩阵 matrix 进行初始化
 * - int sumRegion(int row1, int col1, int row2, int col2) 返回左上角 (row1, col1)、
 *   右下角 (row2, col2) 的子矩阵的元素总和。
 * 
 * 核心思想：
 * 1. 利用二维前缀和数组快速计算任意子矩阵的元素和
 * 2. 前缀和数组preSum[i][j]表示从(0,0)到(i-1,j-1)的子矩阵元素和
 * 3. 通过容斥原理计算任意子矩阵和：
 *    sumRegion(row1,col1,row2,col2) = preSum[row2+1][col2+1] - preSum[row1][col2+1] 
 *                                   - preSum[row2+1][col1] + preSum[row1][col1]
 * 
 * 算法详解：
 * 1. 预处理：构建二维前缀和数组，时间复杂度O(m*n)
 * 2. 查询：利用容斥原理计算子矩阵和，时间复杂度O(1)
 * 
 * 时间复杂度分析：
 * 1. 构造前缀和数组：O(m*n)，其中m为行数，n为列数
 * 2. 查询子矩阵和：O(1)
 * 
 * 空间复杂度分析：
 * O((m+1)*(n+1))，用于存储前缀和数组
 * 
 * 算法优势：
 * 1. 查询效率高，一次查询时间复杂度为O(1)
 * 2. 适用于需要多次查询不同子矩阵和的场景
 * 
 * 工程化考虑：
 * 1. 边界处理：通过扩展前缀和数组边界避免特殊判断
 * 2. 异常处理：应添加对空矩阵、越界查询的处理
 * 3. 可配置性：支持不同数据类型的前缀和计算
 * 
 * 应用场景：
 * 1. 图像处理中的区域统计
 * 2. 机器学习中的特征提取
 * 3. 游戏开发中的地图区域计算
 * 
 * 相关题目：
 * 1. LeetCode 304. Range Sum Query 2D - Immutable
 * 2. LeetCode 303. Range Sum Query - Immutable
 * 3. Codeforces 1371C - A Cookie for You
 * 4. AtCoder ABC106D - AtCoder Express 2
 * 
 * 测试链接 : https://leetcode.cn/problems/range-sum-query-2d-immutable/
 */
class NumMatrix {
private:
    // 前缀和数组，尺寸为(m+1)*(n+1)，避免边界判断
    vector<vector<int>> preSum;

public:
    /**
     * 构造函数：构建二维前缀和数组
     * 
     * 算法步骤：
     * 1. 初始化(m+1)*(n+1)的前缀和数组
     * 2. 按行按列依次计算前缀和
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O((m+1)*(n+1))
     * 
     * @param matrix 原始二维矩阵
     */
    NumMatrix(vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            return;
        }
        
        int m = matrix.size();
        int n = matrix[0].size();
        // 创建前缀和数组，行列均多申请一个空间用于简化边界处理
        preSum.assign(m + 1, vector<int>(n + 1, 0));
        
        // 构建前缀和数组
        // 利用容斥原理：当前点前缀和 = 当前点值 + 上方前缀和 + 左方前缀和 - 左上角前缀和
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                preSum[i][j] = matrix[i - 1][j - 1] + preSum[i - 1][j] + preSum[i][j - 1] - preSum[i - 1][j - 1];
            }
        }
    }

    /**
     * 查询指定区域的元素和
     * 
     * 算法原理：
     * 利用容斥原理计算子矩阵和：
     * sumRegion(row1,col1,row2,col2) = preSum[row2+1][col2+1] - preSum[row1][col2+1] 
     *                                - preSum[row2+1][col1] + preSum[row1][col1]
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * @param row1 子矩阵左上角行索引
     * @param col1 子矩阵左上角列索引
     * @param row2 子矩阵右下角行索引
     * @param col2 子矩阵右下角列索引
     * @return 子矩阵元素和
     */
    int sumRegion(int row1, int col1, int row2, int col2) {
        // 利用容斥原理计算区域和
        return preSum[row2 + 1][col2 + 1] - preSum[row1][col2 + 1] - preSum[row2 + 1][col1] + preSum[row1][col1];
    }
};

/**
 * 测试用例
 */
int main() {
    // 测试用例1
    vector<vector<int>> matrix1 = {
        {3, 0, 1, 4, 2},
        {5, 6, 3, 2, 1},
        {1, 2, 0, 1, 5},
        {4, 1, 0, 1, 7},
        {1, 0, 3, 0, 5}
    };
    
    NumMatrix numMatrix(matrix1);
    
    // 测试sumRegion(2, 1, 4, 3)
    int result1 = numMatrix.sumRegion(2, 1, 4, 3);
    cout << "sumRegion(2, 1, 4, 3) = " << result1 << endl; // 预期输出: 8
    
    // 测试sumRegion(1, 1, 2, 2)
    int result2 = numMatrix.sumRegion(1, 1, 2, 2);
    cout << "sumRegion(1, 1, 2, 2) = " << result2 << endl; // 预期输出: 11
    
    // 测试sumRegion(1, 2, 2, 4)
    int result3 = numMatrix.sumRegion(1, 2, 2, 4);
    cout << "sumRegion(1, 2, 2, 4) = " << result3 << endl; // 预期输出: 12
    
    return 0;
}