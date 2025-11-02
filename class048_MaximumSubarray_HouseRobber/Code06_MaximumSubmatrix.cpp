#include <vector>
#include <algorithm>
#include <climits>
#include <stdexcept>
#include <iostream>
#include <chrono>

using namespace std;

// 函数声明
int kadane(vector<int>& arr);
int kadaneWithPosition(vector<int>& arr, int& left, int& right);
int maxSubmatrix(vector<vector<int>>& matrix);
int maxSubmatrixWithPosition(vector<vector<int>>& matrix, int& top, int& bottom, int& left, int& right);
void testMaxSubmatrix();
void performanceTest();

/**
 * 子矩阵最大累加和问题 - C++实现
 * 给定一个整数矩阵matrix
 * 请找出元素和最大的子矩阵
 * 返回这个子矩阵的元素和
 * 测试链接 : https://leetcode.cn/problems/max-submatrix-lcci/
 * 
 * 算法核心思想：
 * 1. 将二维问题转化为一维问题
 * 2. 枚举所有可能的上下边界
 * 3. 将上下边界之间的每列元素相加，形成一维数组
 * 4. 对一维数组应用Kadane算法求最大子数组和
 * 5. 记录最大的子矩阵和
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n² * m) 或 O(m² * n) - 取决于行列数量
 * - 空间复杂度：O(m) 或 O(n) - 用于存储压缩后的一维数组
 * 
 * 工程化考量：
 * 1. 边界处理：处理空矩阵、单行矩阵等特殊情况
 * 2. 性能优化：根据矩阵形状选择最优的枚举方向
 * 3. 内存优化：使用空间压缩技术减少内存使用
 */

/**
 * 计算子矩阵最大累加和（基础版本）
 * 
 * 算法原理：
 * 1. 枚举所有可能的上下边界组合
 * 2. 将多行压缩为一维数组（列方向求和）
 * 3. 对一维数组应用Kadane算法
 * 4. 记录过程中的最大值
 * 
 * 时间复杂度：O(n² * m) - 其中n是行数，m是列数
 * 空间复杂度：O(m) - 用于存储压缩后的一维数组
 * 
 * @param matrix 输入的二维整数矩阵
 * @return 最大子矩阵的元素和
 * @throws invalid_argument 如果输入矩阵为空
 */
int maxSubmatrix(vector<vector<int>>& matrix) {
    // 边界检查
    if (matrix.empty() || matrix[0].empty()) {
        throw invalid_argument("输入矩阵不能为空");
    }
    
    int rows = matrix.size();
    int cols = matrix[0].size();
    int maxSum = INT_MIN;
    
    // 枚举所有可能的上下边界
    for (int top = 0; top < rows; top++) {
        // 压缩数组，存储当前上下边界之间的列和
        vector<int> compressed(cols, 0);
        
        for (int bottom = top; bottom < rows; bottom++) {
            // 更新压缩数组：添加当前行的元素
            for (int col = 0; col < cols; col++) {
                compressed[col] += matrix[bottom][col];
            }
            
            // 对压缩数组应用Kadane算法
            int currentSum = kadane(compressed);
            maxSum = max(maxSum, currentSum);
        }
    }
    
    return maxSum;
}

/**
 * Kadane算法：计算一维数组的最大子数组和
 * 
 * @param arr 输入的一维整数数组
 * @return 最大子数组和
 */
int kadane(vector<int>& arr) {
    if (arr.empty()) {
        return 0;
    }
    
    int maxSum = arr[0];
    int currentSum = arr[0];
    
    for (int i = 1; i < arr.size(); i++) {
        currentSum = max(arr[i], currentSum + arr[i]);
        maxSum = max(maxSum, currentSum);
    }
    
    return maxSum;
}

/**
 * 计算子矩阵最大累加和并返回位置信息
 * 
 * 算法改进：
 * - 记录最大子矩阵的边界位置
 * - 返回最大和及对应的子矩阵坐标
 * 
 * @param matrix 输入的二维整数矩阵
 * @param top 上边界（输出参数）
 * @param bottom 下边界（输出参数）
 * @param left 左边界（输出参数）
 * @param right 右边界（输出参数）
 * @return 最大子矩阵的元素和
 */
int maxSubmatrixWithPosition(vector<vector<int>>& matrix, int& top, int& bottom, int& left, int& right) {
    if (matrix.empty() || matrix[0].empty()) {
        throw invalid_argument("输入矩阵不能为空");
    }
    
    int rows = matrix.size();
    int cols = matrix[0].size();
    int maxSum = INT_MIN;
    
    for (int i = 0; i < rows; i++) {
        vector<int> compressed(cols, 0);
        
        for (int j = i; j < rows; j++) {
            // 更新压缩数组
            for (int k = 0; k < cols; k++) {
                compressed[k] += matrix[j][k];
            }
            
            // 使用扩展的Kadane算法获取位置信息
            int currentLeft, currentRight;
            int currentSum = kadaneWithPosition(compressed, currentLeft, currentRight);
            
            if (currentSum > maxSum) {
                maxSum = currentSum;
                top = i;
                bottom = j;
                left = currentLeft;
                right = currentRight;
            }
        }
    }
    
    return maxSum;
}

/**
 * Kadane算法扩展：返回最大子数组和及其位置
 * 
 * @param arr 输入的一维整数数组
 * @param left 左边界（输出参数）
 * @param right 右边界（输出参数）
 * @return 最大子数组和
 */
int kadaneWithPosition(vector<int>& arr, int& left, int& right) {
    if (arr.empty()) {
        left = right = 0;
        return 0;
    }
    
    int maxSum = arr[0];
    int currentSum = arr[0];
    int maxLeft = 0, maxRight = 0;
    int currentLeft = 0;
    
    for (int i = 1; i < arr.size(); i++) {
        if (arr[i] > currentSum + arr[i]) {
            currentSum = arr[i];
            currentLeft = i;
        } else {
            currentSum += arr[i];
        }
        
        if (currentSum > maxSum) {
            maxSum = currentSum;
            maxLeft = currentLeft;
            maxRight = i;
        }
    }
    
    left = maxLeft;
    right = maxRight;
    return maxSum;
}

/**
 * 测试函数：验证算法正确性
 */
void testMaxSubmatrix() {
    vector<vector<vector<int>>> testCases = {
        {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}},           // 全正数矩阵
        {{-1, -2, -3}, {-4, -5, -6}, {-7, -8, -9}},  // 全负数矩阵
        {{-1, 2, 3}, {4, -5, 6}, {7, 8, -9}},        // 混合正负数
        {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},           // 全零矩阵
        {{1}}                                         // 单元素矩阵
    };
    
    vector<int> expected = {45, -1, 21, 0, 1};
    
    cout << "=== 子矩阵最大累加和算法测试 ===" << endl;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        try {
            int result = maxSubmatrix(testCases[i]);
            
            cout << "测试用例 " << i+1 << ": ";
            cout << "结果=" << result << ", 期望=" << expected[i];
            cout << ", 状态=" << (result == expected[i] ? "通过" : "失败") << endl;
            
            // 测试位置信息方法
            if (result == expected[i]) {
                int top, bottom, left, right;
                int sum = maxSubmatrixWithPosition(testCases[i], top, bottom, left, right);
                cout << "  位置信息: 和=" << sum << ", 区域=[" << top << ":" << bottom << ", " << left << ":" << right << "]" << endl;
            }
            
        } catch (const exception& e) {
            cout << "测试用例 " << i+1 << ": 异常 - " << e.what() << endl;
        }
    }
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试：大数据量验证
 */
void performanceTest() {
    const int ROWS = 100;
    const int COLS = 100;
    vector<vector<int>> largeMatrix(ROWS, vector<int>(COLS, 1));  // 全1矩阵
    
    cout << "=== 性能测试开始 ===" << endl;
    cout << "数据量: " << ROWS << "x" << COLS << " 矩阵" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result = maxSubmatrix(largeMatrix);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "计算结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    cout << "=== 性能测试结束 ===" << endl;
}

int main() {
    // 运行功能测试
    testMaxSubmatrix();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    return 0;
}

/*
 * 扩展思考与工程化考量：
 * 
 * 1. 算法正确性深度分析：
 *    - 为什么枚举上下边界的方法是正确的？
 *      因为任何子矩阵都可以由上下边界和左右边界唯一确定
 *    - 压缩数组的物理意义是什么？
 *      表示在固定上下边界的情况下，每列的元素总和
 *    - Kadane算法为什么适用于压缩数组？
 *      压缩数组的一维最大和对应原矩阵中相应子矩阵的和
 * 
 * 2. 性能优化策略：
 *    - 预处理前缀和：可以预先计算前缀和数组，将压缩操作优化到O(1)
 *    - 方向选择：根据矩阵形状选择最优的枚举方向
 *    - 提前终止：对于某些情况可以提前结束计算
 * 
 * 3. 工程实践要点：
 *    - 边界处理：空矩阵、单行矩阵等特殊情况
 *    - 数值范围：处理极大值可能导致的溢出问题
 *    - 内存使用：优化压缩数组的内存分配
 * 
 * 4. 实际应用场景：
 *    - 图像处理：最大亮度区域检测
 *    - 数据分析：最大收益区域分析
 *    - 机器学习：特征选择中的区域优化
 */