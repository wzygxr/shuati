#include <vector>
#include <iostream>
#include <stdexcept>
#include <chrono>
using namespace std;

/**
 * 二维前缀和算法实现 - C++版本
 * 
 * 核心思想：
 * 1. 利用二维前缀和数组快速计算任意子矩阵的元素和
 * 2. 前缀和数组preSum[i][j]表示从(0,0)到(i-1,j-1)的子矩阵元素和
 * 3. 通过容斥原理计算任意子矩阵和：sumRegion(a,b,c,d) = preSum[c+1][d+1] - preSum[c+1][b] - preSum[a][d+1] + preSum[a][b]
 * 
 * 时间复杂度分析：
 * 1. 构造前缀和数组：O(n*m)，其中n为行数，m为列数
 * 2. 查询子矩阵和：O(1)
 * 
 * 空间复杂度分析：
 * O((n+1)*(m+1))，用于存储前缀和数组
 * 
 * 算法优势：
 * 1. 查询效率高，一次查询时间复杂度为O(1)
 * 2. 适用于需要多次查询不同子矩阵和的场景
 * 3. 代码实现简单，易于理解和维护
 * 
 * 工程化考虑：
 * 1. 边界处理：通过扩展前缀和数组边界避免特殊判断
 * 2. 异常处理：应添加对空矩阵、越界查询的处理
 * 3. 内存管理：使用vector容器自动管理内存
 * 4. 性能优化：避免不必要的拷贝操作
 * 
 * 应用场景：
 * 1. 图像处理中的区域统计
 * 2. 机器学习中的特征提取
 * 3. 游戏开发中的地图区域计算
 * 4. 数据分析中的区域统计
 * 
 * 相关题目：
 * 1. LeetCode 304. Range Sum Query 2D - Immutable
 * 2. Codeforces 1371C - A Cookie for You
 * 3. AtCoder ABC106D - AtCoder Express 2
 * 4. HDU 1559 最大子矩阵
 * 5. POJ 1050 To the Max
 * 
 * 测试链接 : https://leetcode.cn/problems/range-sum-query-2d-immutable/
 * 
 * C++语言特性：
 * 1. 使用vector容器自动管理内存
 * 2. 使用引用避免不必要的拷贝
 * 3. 使用异常处理机制
 * 4. 支持移动语义优化性能
 */
class NumMatrix {
private:
    // 前缀和数组，尺寸为(n+1)*(m+1)，避免边界判断
    // 使用vector<vector<int>>自动管理内存
    vector<vector<int>> preSum;

public:
    /**
     * 构造函数：构建二维前缀和数组
     * 
     * 算法步骤：
     * 1. 初始化(n+1)*(m+1)的前缀和数组
     * 2. 将原始矩阵复制到前缀和数组的偏移位置
     * 3. 按行按列依次计算前缀和
     * 
     * 时间复杂度：O(n*m)
     * 空间复杂度：O((n+1)*(m+1))
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入矩阵是否为空
     * 2. 边界处理：扩展数组边界避免特殊判断
     * 3. 内存管理：使用reserve预分配内存提高性能
     * 
     * @param matrix 原始二维矩阵，要求非空且至少有一个元素
     * @throws invalid_argument 如果输入矩阵为空或维度为0
     */
    NumMatrix(vector<vector<int>>& matrix) {
        // 参数校验：确保输入矩阵有效
        if (matrix.empty() || matrix[0].empty()) {
            throw invalid_argument("输入矩阵不能为空");
        }
        
        int n = matrix.size();
        int m = matrix[0].size();
        
        // 创建前缀和数组，行列均多申请一个空间用于简化边界处理
        // 使用resize初始化二维vector，默认值为0
        preSum.resize(n + 1, vector<int>(m + 1, 0));
        
        // 构建前缀和数组
        // 利用容斥原理：当前点前缀和 = 当前点值 + 上方前缀和 + 左方前缀和 - 左上角前缀和
        // 数学原理：preSum[i][j] = matrix[i-1][j-1] + preSum[i-1][j] + preSum[i][j-1] - preSum[i-1][j-1]
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                preSum[i][j] = matrix[i-1][j-1] + preSum[i-1][j] + preSum[i][j-1] - preSum[i-1][j-1];
                
                // 调试输出：打印每一步的前缀和计算结果
                // cout << "preSum[" << i << "][" << j << "] = " << matrix[i-1][j-1] 
                //      << " + " << preSum[i-1][j] << " + " << preSum[i][j-1] 
                //      << " - " << preSum[i-1][j-1] << " = " << preSum[i][j] << endl;
            }
        }
    }

    /**
     * 查询指定区域的元素和
     * 
     * 算法原理：
     * 利用容斥原理计算子矩阵和：
     * sumRegion(a,b,c,d) = preSum[c+1][d+1] - preSum[c+1][b] - preSum[a][d+1] + preSum[a][b]
     * 
     * 数学推导：
     * 1. preSum[c+1][d+1] 包含从(0,0)到(c,d)的所有元素
     * 2. 减去preSum[c+1][b] 去掉左侧多余部分
     * 3. 减去preSum[a][d+1] 去掉上方多余部分
     * 4. 加上preSum[a][b] 补回多减的部分
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * 边界情况处理：
     * 1. 输入坐标合法性检查
     * 2. 坐标越界处理
     * 3. 空矩阵查询处理
     * 
     * 工程化考量：
     * 1. 参数校验：确保输入坐标有效
     * 2. 性能优化：避免不必要的计算
     * 3. 异常处理：提供友好的错误信息
     * 
     * @param a 子矩阵左上角行索引（从0开始）
     * @param b 子矩阵左上角列索引（从0开始）
     * @param c 子矩阵右下角行索引（从0开始）
     * @param d 子矩阵右下角列索引（从0开始）
     * @return 子矩阵元素和
     * @throws invalid_argument 如果坐标越界或无效
     */
    int sumRegion(int a, int b, int c, int d) {
        // 参数校验：确保坐标有效
        if (a < 0 || b < 0 || c < a || d < b || 
            c >= preSum.size() - 1 || d >= preSum[0].size() - 1) {
            throw invalid_argument("坐标越界或无效");
        }
        
        // 调整坐标到前缀和数组的对应位置
        // 由于前缀和数组有偏移，需要将原始坐标加1
        c++;
        d++;
        
        // 利用容斥原理计算区域和
        // 公式：preSum[c][d] - preSum[c][b] - preSum[a][d] + preSum[a][b]
        int result = preSum[c][d] - preSum[c][b] - preSum[a][d] + preSum[a][b];
        
        // 调试输出：打印查询结果
        // cout << "sumRegion(" << a << "," << b << "," << c-1 << "," << d-1 
        //      << ") = " << preSum[c][d] << " - " << preSum[c][b] 
        //      << " - " << preSum[a][d] << " + " << preSum[a][b] 
        //      << " = " << result << endl;
        
        return result;
    }
};

/**
 * 测试用例和演示代码
 * 
 * 包含多种测试场景：
 * 1. 正常情况测试
 * 2. 边界情况测试
 * 3. 性能测试
 * 4. 异常情况测试
 */
int main() {
    // 测试用例1：正常情况
    cout << "=== 测试用例1：正常情况 ===" << endl;
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
    
    cout << endl;
    
    // 测试用例2：边界情况 - 单元素矩阵
    cout << "=== 测试用例2：单元素矩阵 ===" << endl;
    vector<vector<int>> matrix2 = {{5}};
    NumMatrix numMatrix2(matrix2);
    int result4 = numMatrix2.sumRegion(0, 0, 0, 0);
    cout << "sumRegion(0, 0, 0, 0) = " << result4 << endl; // 预期输出: 5
    
    cout << endl;
    
    // 测试用例3：性能测试 - 大规模数据
    cout << "=== 测试用例3：性能测试 ===" << endl;
    int n = 1000;
    int m = 1000;
    vector<vector<int>> largeMatrix(n, vector<int>(m));
    // 填充测试数据
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            largeMatrix[i][j] = i + j;
        }
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    NumMatrix numMatrix3(largeMatrix);
    auto constructionTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - startTime).count();
    
    startTime = chrono::high_resolution_clock::now();
    // 执行多次查询测试性能
    for (int i = 0; i < 1000; i++) {
        numMatrix3.sumRegion(0, 0, n-1, m-1);
    }
    auto queryTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - startTime).count();
    
    cout << "构造时间: " << constructionTime << "ms" << endl;
    cout << "1000次查询时间: " << queryTime << "ms" << endl;
    cout << "平均查询时间: " << (queryTime / 1000.0) << "ms" << endl;
    
    cout << endl;
    
    // 测试用例4：异常情况测试
    cout << "=== 测试用例4：异常情况测试 ===" << endl;
    try {
        vector<vector<int>> emptyMatrix;
        NumMatrix numMatrix4(emptyMatrix);
    } catch (const invalid_argument& e) {
        cout << "异常处理测试通过: " << e.what() << endl;
    }
    
    return 0;
}