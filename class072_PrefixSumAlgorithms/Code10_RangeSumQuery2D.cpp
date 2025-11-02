/**
 * 二维区域和检索 - 矩阵不可变 (Range Sum Query 2D - Immutable)
 * 
 * 题目描述:
 * 给定一个二维矩阵 matrix，计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2)。
 * 
 * 实现 NumMatrix 类:
 * - NumMatrix(int[][] matrix) 用整数矩阵 matrix 初始化对象
 * - int sumRegion(int row1, int col1, int row2, int col2) 返回左上角 (row1, col1)、右下角 (row2, col2) 的子矩阵的元素总和。
 * 
 * 示例:
 * 输入:
 * ["NumMatrix","sumRegion","sumRegion","sumRegion"]
 * [[[[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]],[2,1,4,3],[1,1,2,2],[1,2,2,4]]
 * 输出:
 * [null, 8, 11, 12]
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
 * 使用二维前缀和技巧:
 * 1. 创建一个二维前缀和数组 dp，其中 dp[i][j] 表示从 (0,0) 到 (i-1,j-1) 的矩形区域和
 * 2. 初始化时计算整个矩阵的前缀和
 * 3. 查询时使用公式: sum = dp[row2+1][col2+1] - dp[row1][col2+1] - dp[row2+1][col1] + dp[row1][col1]
 * 
 * 时间复杂度: 
 * - 初始化: O(m*n) - 需要遍历整个矩阵构建前缀和数组
 * - 查询: O(1) - 每次查询只需要常数时间
 * 空间复杂度: O(m*n) - 需要额外的前缀和数组空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：空矩阵、单元素矩阵
 * 2. 性能优化：预处理前缀和，查询时O(1)时间
 * 3. 空间优化：必须存储前缀和数组，无法避免
 * 4. 大数处理：元素值可能很大，需要确保整数范围
 * 
 * 最优解分析:
 * 这是最优解，时间复杂度O(m*n)初始化，O(1)查询。
 * 对于频繁查询的场景，预处理是必要的。
 * 
 * 数学原理:
 * 二维前缀和公式：
 * dp[i][j] = dp[i-1][j] + dp[i][j-1] - dp[i-1][j-1] + matrix[i-1][j-1]
 * 
 * 区域和公式：
 * sum = dp[r2+1][c2+1] - dp[r1][c2+1] - dp[r2+1][c1] + dp[r1][c1]
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：显示前缀和数组的计算过程
 * 2. 边界测试：测试空矩阵、单元素矩阵等特殊情况
 * 3. 性能测试：测试大规模矩阵下的性能表现
 * 
 * 语言特性差异:
 * C++需要手动管理内存，使用vector容器可以简化内存管理。
 * 与Python/Java相比，C++有更好的性能但语法更复杂。
 */

#include <iostream>
#include <vector>
#include <cassert>
#include <chrono>
#include <cstdlib>
#include <algorithm>

using namespace std;

class NumMatrix {
private:
    vector<vector<int>> dp;  // 二维前缀和数组

public:
    /**
     * 初始化二维前缀和数组
     * 
     * @param matrix 输入矩阵
     * 
     * 异常场景处理:
     * - 空矩阵：创建空的前缀和数组
     * - 单元素矩阵：正常处理
     * - 非矩形矩阵：题目保证是矩形矩阵
     * 
     * 边界条件:
     * - 矩阵为空
     * - 矩阵只有一行或一列
     * - 查询范围超出矩阵边界
     */
    NumMatrix(vector<vector<int>>& matrix) {
        // 边界情况处理
        if (matrix.empty() || matrix[0].empty()) {
            return;
        }
        
        int m = matrix.size();
        int n = matrix[0].size();
        
        // 创建二维前缀和数组，大小为(m+1) x (n+1)
        // 使用m+1和n+1可以避免边界检查
        dp.resize(m + 1, vector<int>(n + 1, 0));
        
        // 计算前缀和，时间复杂度O(m*n)
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 前缀和公式：上 + 左 - 左上 + 当前元素
                dp[i][j] = dp[i-1][j] + dp[i][j-1] - dp[i-1][j-1] + matrix[i-1][j-1];
                // 调试打印：显示前缀和计算过程
                // cout << "位置 (" << i-1 << "," << j-1 << "): dp[" << i << "][" << j << "] = " << dp[i][j] << endl;
            }
        }
    }
    
    /**
     * 计算子矩阵区域和
     * 
     * @param row1, col1: 左上角坐标
     * @param row2, col2: 右下角坐标
     * @return 子矩阵元素和
     * 
     * 边界条件:
     * - 坐标超出范围（题目保证有效）
     * - 单元素查询
     * - 整个矩阵查询
     */
    int sumRegion(int row1, int col1, int row2, int col2) {
        // 使用前缀和公式计算区域和，时间复杂度O(1)
        // 公式：右下 - 右上 - 左下 + 左上
        int result = dp[row2+1][col2+1] - dp[row1][col2+1] - dp[row2+1][col1] + dp[row1][col1];
        
        // 调试打印：显示查询过程
        // cout << "查询区域 [" << row1 << "," << col1 << "]到[" << row2 << "," << col2 << "]: 结果 = " << result << endl;
        
        return result;
    }
};

// 单元测试函数
void testNumMatrix() {
    cout << "=== 二维区域和检索单元测试 ===" << endl;
    
    // 测试用例1：题目示例
    vector<vector<int>> matrix1 = {
        {3, 0, 1, 4, 2},
        {5, 6, 3, 2, 1},
        {1, 2, 0, 1, 5},
        {4, 1, 0, 1, 7},
        {1, 0, 3, 0, 5}
    };
    
    NumMatrix numMatrix1(matrix1);
    
    // 测试查询
    int result1 = numMatrix1.sumRegion(2, 1, 4, 3);  // 预期: 8
    int result2 = numMatrix1.sumRegion(1, 1, 2, 2);  // 预期: 11
    int result3 = numMatrix1.sumRegion(1, 2, 2, 4);  // 预期: 12
    
    cout << "测试用例1: " << result1 << ", " << result2 << ", " << result3 << " (预期: 8, 11, 12)" << endl;
    assert(result1 == 8);
    assert(result2 == 11);
    assert(result3 == 12);
    
    // 测试用例2：空矩阵
    vector<vector<int>> matrix2;
    NumMatrix numMatrix2(matrix2);
    // 空矩阵应该能正常构造，但查询可能有问题，这里不测试查询
    
    cout << "测试用例2 空矩阵: 构造成功" << endl;
    
    // 测试用例3：单元素矩阵
    vector<vector<int>> matrix3 = {{5}};
    NumMatrix numMatrix3(matrix3);
    int result5 = numMatrix3.sumRegion(0, 0, 0, 0);  // 预期: 5
    
    cout << "测试用例3 单元素: " << result5 << " (预期: 5)" << endl;
    assert(result5 == 5);
    
    // 测试用例4：单行矩阵
    vector<vector<int>> matrix4 = {{1, 2, 3, 4, 5}};
    NumMatrix numMatrix4(matrix4);
    int result6 = numMatrix4.sumRegion(0, 1, 0, 3);  // 预期: 9 (2+3+4)
    
    cout << "测试用例4 单行矩阵: " << result6 << " (预期: 9)" << endl;
    assert(result6 == 9);
    
    cout << "=== 单元测试通过 ===" << endl;
}

// 性能测试函数
void performanceTest() {
    cout << "\n=== 性能测试 ===" << endl;
    
    // 创建大规模矩阵
    int m = 200, n = 200;  // 最大规模
    vector<vector<int>> matrix(m, vector<int>(n, 1));  // 全1矩阵
    
    // 测试初始化性能
    auto start = chrono::high_resolution_clock::now();
    NumMatrix numMatrix(matrix);
    auto end = chrono::high_resolution_clock::now();
    auto initTime = chrono::duration_cast<chrono::microseconds>(end - start).count();
    
    // 测试多次查询性能
    int queryCount = 10000;
    start = chrono::high_resolution_clock::now();
    for (int i = 0; i < queryCount; i++) {
        int row1 = rand() % m;
        int col1 = rand() % n;
        int row2 = min(row1 + rand() % 10, m - 1);
        int col2 = min(col1 + rand() % 10, n - 1);
        numMatrix.sumRegion(row1, col1, row2, col2);
    }
    end = chrono::high_resolution_clock::now();
    auto queryTime = chrono::duration_cast<chrono::microseconds>(end - start).count();
    
    cout << "初始化 " << m << "x" << n << " 矩阵耗时: " << initTime << " 微秒" << endl;
    cout << queryCount << " 次查询耗时: " << queryTime << " 微秒" << endl;
    cout << "平均每次查询耗时: " << static_cast<double>(queryTime) / queryCount << " 微秒" << endl;
}

int main() {
    // 运行单元测试
    testNumMatrix();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    cout << "\n=== 测试完成 ===" << endl;
    return 0;
}