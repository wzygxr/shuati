#include <iostream>
#include <vector>
#include <stdexcept>
using namespace std;

/**
 * 二维区域和检索 - 不可变 (Range Sum Query 2D - Immutable)
 * 
 * 题目描述:
 * 给定一个二维矩阵 matrix，计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2) 。
 * 实现 NumMatrix 类：
 * NumMatrix(int[][] matrix) 给定整数矩阵 matrix 进行初始化
 * int sumRegion(int row1, int col1, int row2, int col2) 返回左上角 (row1, col1) 、右下角 (row2, col2) 的子矩阵的元素总和。
 * 
 * 示例:
 * 输入:
 * ["NumMatrix", "sumRegion", "sumRegion", "sumRegion"]
 * [[[[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]], [2, 1, 4, 3], [1, 1, 2, 2], [1, 2, 2, 4]]
 * 输出:
 * [null, 8, 11, 12]
 * 解释:
 * NumMatrix numMatrix = new NumMatrix([[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]);
 * numMatrix.sumRegion(2, 1, 4, 3); // return 8 (红色矩形框的元素总和)
 * numMatrix.sumRegion(1, 1, 2, 2); // return 11 (绿色矩形框的元素总和)
 * numMatrix.sumRegion(1, 2, 2, 4); // return 12 (蓝色矩形框的元素总和)
 * 
 * 提示:
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 200
 * -10^5 <= matrix[i][j] <= 10^5
 * 0 <= row1 <= row2 < m
 * 0 <= col1 <= col2 < n
 * 最多调用 10^4 次 sumRegion 方法
 * 
 * 题目链接: https://leetcode.com/problems/range-sum-query-2d-immutable/
 * 
 * 解题思路:
 * 使用二维前缀和数组预处理原矩阵，使得每次查询子矩阵和的操作时间复杂度为O(1)。
 * 1. 计算二维前缀和数组 prefixSum，其中 prefixSum[i][j] 表示从左上角 (0,0) 到右下角 (i-1,j-1) 的子矩阵元素和
 * 2. 对于子矩阵查询 (row1, col1) 到 (row2, col2)，可以利用容斥原理计算：
 *    prefixSum[row2+1][col2+1] - prefixSum[row1][col2+1] - prefixSum[row2+1][col1] + prefixSum[row1][col1]
 * 
 * 时间复杂度:
 * - 初始化: O(m*n) - 预处理二维前缀和数组
 * - 查询: O(1) - 直接利用二维前缀和数组计算子矩阵和
 * 空间复杂度: O(m*n) - 存储二维前缀和数组
 */

class NumMatrix {
private:
    // 二维前缀和数组
    vector<vector<long long>> prefixSum;  // 使用long long避免整数溢出

public:
    /**
     * 使用二维矩阵 matrix 初始化对象，预处理计算二维前缀和数组
     * 
     * @param matrix 输入的二维矩阵
     */
    NumMatrix(vector<vector<int>>& matrix) {
        // 边界检查
        if (matrix.empty() || matrix[0].empty()) {
            return;
        }
        
        int m = matrix.size();      // 矩阵的行数
        int n = matrix[0].size();   // 矩阵的列数
        
        // 初始化前缀和数组，大小为 (m+1) × (n+1)
        // prefixSum[0][*] 和 prefixSum[*][0] 都是边界，值为0
        // prefixSum[i][j] 表示从 (0,0) 到 (i-1,j-1) 的子矩阵元素和
        prefixSum.resize(m + 1, vector<long long>(n + 1, 0));
        
        // 计算二维前缀和
        for (int i = 0; i < m; i++) {
            long long rowSum = 0;  // 用于优化计算，记录当前行的累加和
            for (int j = 0; j < n; j++) {
                // 当前行的累加和
                rowSum += matrix[i][j];
                // 前缀和计算：当前行累加和 + 上方子矩阵和
                prefixSum[i + 1][j + 1] = rowSum + prefixSum[i][j + 1];
            }
        }
    }

    /**
     * 返回左上角 (row1, col1) 、右下角 (row2, col2) 的子矩阵的元素总和
     * 
     * @param row1 左上角行索引
     * @param col1 左上角列索引
     * @param row2 右下角行索引
     * @param col2 右下角列索引
     * @return 子矩阵 [row1, row2] × [col1, col2] 的元素和
     * @throws invalid_argument 如果索引参数无效
     */
    int sumRegion(int row1, int col1, int row2, int col2) {
        // 参数合法性检查
        if (prefixSum.empty() || prefixSum[0].empty()) {
            throw invalid_argument("矩阵为空");
        }
        
        int m = prefixSum.size() - 1;
        int n = prefixSum[0].size() - 1;
        
        // 检查行索引是否有效
        if (row1 < 0 || row1 >= m || row2 < 0 || row2 >= m) {
            throw invalid_argument("行索引无效");
        }
        // 检查列索引是否有效
        if (col1 < 0 || col1 >= n || col2 < 0 || col2 >= n) {
            throw invalid_argument("列索引无效");
        }
        // 检查索引范围是否有效
        if (row1 > row2 || col1 > col2) {
            throw invalid_argument("无效的索引范围");
        }
        
        // 利用容斥原理计算子矩阵和
        // 区域和 = 右下角前缀和 - 左侧前缀和 - 上方前缀和 + 左上角重叠区域前缀和
        return static_cast<int>(
            prefixSum[row2 + 1][col2 + 1] - 
            prefixSum[row1][col2 + 1] - 
            prefixSum[row2 + 1][col1] + 
            prefixSum[row1][col1]
        );
    }
};

int main() {
    try {
        // 创建测试用例矩阵
        vector<vector<int>> matrix = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        // 初始化 NumMatrix 对象
        NumMatrix numMatrix(matrix);
        
        // 测试子矩阵和查询
        // 测试用例1: (2, 1, 4, 3) 预期输出: 8
        cout << "子矩阵 [2,1] 到 [4,3] 的和: " << numMatrix.sumRegion(2, 1, 4, 3) << endl;
        
        // 测试用例2: (1, 1, 2, 2) 预期输出: 11
        cout << "子矩阵 [1,1] 到 [2,2] 的和: " << numMatrix.sumRegion(1, 1, 2, 2) << endl;
        
        // 测试用例3: (1, 2, 2, 4) 预期输出: 12
        cout << "子矩阵 [1,2] 到 [2,4] 的和: " << numMatrix.sumRegion(1, 2, 2, 4) << endl;
        
        // 测试边界情况
        // 测试用例4: (0, 0, 0, 0) 预期输出: 3
        cout << "子矩阵 [0,0] 到 [0,0] 的和: " << numMatrix.sumRegion(0, 0, 0, 0) << endl;
        
        // 测试用例5: (0, 0, 4, 4) 预期输出: 所有元素的和 48
        cout << "子矩阵 [0,0] 到 [4,4] 的和: " << numMatrix.sumRegion(0, 0, 4, 4) << endl;
        
        // 测试空矩阵
        vector<vector<int>> emptyMatrix;
        NumMatrix emptyNumMatrix(emptyMatrix);
        // 这行应该抛出异常
        emptyNumMatrix.sumRegion(0, 0, 0, 0);
    } catch (const invalid_argument& e) {
        cout << "异常捕获: " << e.what() << endl;
    }
    
    return 0;
}