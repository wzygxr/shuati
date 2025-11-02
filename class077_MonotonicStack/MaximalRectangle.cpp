/**
 * 85. 最大矩形 (Maximal Rectangle)
 * 
 * 题目描述:
 * 给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，
 * 找出只包含 1 的最大矩形，并返回其面积。
 * 
 * 解题思路:
 * 将二维问题转化为一维问题。逐行构建高度数组，然后对每一行应用柱状图中最大矩形的解法。
 * 使用单调栈来计算每个位置的最大矩形面积。
 * 
 * 时间复杂度: O(rows * cols)
 * 空间复杂度: O(cols)
 * 
 * 测试链接: https://leetcode.cn/problems/maximal-rectangle/
 * 
 * 工程化考量:
 * 1. 异常处理：空矩阵、边界情况处理
 * 2. 性能优化：使用vector预分配空间，避免动态内存分配
 * 3. 内存管理：使用RAII原则管理资源
 * 4. 代码可读性：详细注释和模块化设计
 */

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <chrono>

using namespace std;

/**
 * @brief 计算柱状图中最大矩形的面积（单调栈解法）
 * 
 * @param heights 高度数组
 * @return int 最大矩形面积
 */
int largestRectangleArea(vector<int>& heights) {
    int n = heights.size();
    if (n == 0) return 0;
    
    stack<int> st;
    int maxArea = 0;
    
    // 遍历每个柱子
    for (int i = 0; i < n; i++) {
        // 当栈不为空且当前高度小于栈顶索引对应的高度时
        while (!st.empty() && heights[st.top()] > heights[i]) {
            int height = heights[st.top()]; // 弹出栈顶元素作为高度
            st.pop();
            // 计算宽度
            int width = st.empty() ? i : i - st.top() - 1;
            maxArea = max(maxArea, height * width);
        }
        st.push(i); // 将当前索引入栈
    }
    
    // 处理栈中剩余元素
    while (!st.empty()) {
        int height = heights[st.top()];
        st.pop();
        int width = st.empty() ? n : n - st.top() - 1;
        maxArea = max(maxArea, height * width);
    }
    
    return maxArea;
}

/**
 * @brief 计算二维矩阵中最大矩形的面积
 * 
 * @param matrix 输入二维矩阵
 * @return int 最大矩形面积
 */
int maximalRectangle(vector<vector<char>>& matrix) {
    // 边界条件检查
    if (matrix.empty() || matrix[0].empty()) {
        return 0;
    }
    
    int rows = matrix.size();
    int cols = matrix[0].size();
    int maxArea = 0;
    
    // 高度数组，记录每一列连续1的高度
    vector<int> heights(cols, 0);
    
    // 逐行处理矩阵
    for (int i = 0; i < rows; i++) {
        // 更新高度数组
        for (int j = 0; j < cols; j++) {
            if (matrix[i][j] == '1') {
                heights[j] += 1;
            } else {
                heights[j] = 0;
            }
        }
        
        // 对当前行的高度数组计算最大矩形面积
        maxArea = max(maxArea, largestRectangleArea(heights));
    }
    
    return maxArea;
}

/**
 * @brief 优化版本：使用动态规划预处理左右边界
 */
int maximalRectangleOptimized(vector<vector<char>>& matrix) {
    if (matrix.empty() || matrix[0].empty()) {
        return 0;
    }
    
    int rows = matrix.size();
    int cols = matrix[0].size();
    int maxArea = 0;
    
    vector<int> heights(cols, 0);
    vector<int> left(cols, 0);   // 左边第一个比当前高度小的位置
    vector<int> right(cols, cols); // 右边第一个比当前高度小的位置
    
    for (int i = 0; i < rows; i++) {
        // 更新高度数组
        for (int j = 0; j < cols; j++) {
            if (matrix[i][j] == '1') {
                heights[j] += 1;
            } else {
                heights[j] = 0;
            }
        }
        
        // 更新left边界
        int currentLeft = 0;
        for (int j = 0; j < cols; j++) {
            if (matrix[i][j] == '1') {
                left[j] = max(left[j], currentLeft);
            } else {
                left[j] = 0;
                currentLeft = j + 1;
            }
        }
        
        // 更新right边界
        int currentRight = cols;
        for (int j = cols - 1; j >= 0; j--) {
            if (matrix[i][j] == '1') {
                right[j] = min(right[j], currentRight);
            } else {
                right[j] = cols;
                currentRight = j;
            }
        }
        
        // 计算当前行的最大矩形面积
        for (int j = 0; j < cols; j++) {
            int area = heights[j] * (right[j] - left[j]);
            maxArea = max(maxArea, area);
        }
    }
    
    return maxArea;
}

/**
 * @brief 测试方法 - 验证算法正确性
 */
void testMaximalRectangle() {
    cout << "=== 最大矩形算法测试 ===" << endl;
    
    // 测试用例1
    vector<vector<char>> matrix1 = {
        {'1','0','1','0','0'},
        {'1','0','1','1','1'},
        {'1','1','1','1','1'},
        {'1','0','0','1','0'}
    };
    int result1 = maximalRectangle(matrix1);
    int result1Opt = maximalRectangleOptimized(matrix1);
    cout << "测试用例1: " << result1 << " (优化版: " << result1Opt << ", 预期: 6)" << endl;
    
    // 测试用例2: 全1矩阵
    vector<vector<char>> matrix2 = {
        {'1','1'},
        {'1','1'}
    };
    int result2 = maximalRectangle(matrix2);
    int result2Opt = maximalRectangleOptimized(matrix2);
    cout << "测试用例2: " << result2 << " (优化版: " << result2Opt << ", 预期: 4)" << endl;
    
    // 测试用例3: 空矩阵
    vector<vector<char>> matrix3 = {};
    int result3 = maximalRectangle(matrix3);
    int result3Opt = maximalRectangleOptimized(matrix3);
    cout << "测试用例3: " << result3 << " (优化版: " << result3Opt << ", 预期: 0)" << endl;
    
    // 测试用例4: 单行矩阵
    vector<vector<char>> matrix4 = {{'1','0','1','1','0'}};
    int result4 = maximalRectangle(matrix4);
    int result4Opt = maximalRectangleOptimized(matrix4);
    cout << "测试用例4: " << result4 << " (优化版: " << result4Opt << ", 预期: 2)" << endl;
    
    // 测试用例5: 全0矩阵
    vector<vector<char>> matrix5 = {
        {'0','0'},
        {'0','0'}
    };
    int result5 = maximalRectangle(matrix5);
    int result5Opt = maximalRectangleOptimized(matrix5);
    cout << "测试用例5: " << result5 << " (优化版: " << result5Opt << ", 预期: 0)" << endl;
    
    cout << "=== 功能测试完成！ ===" << endl;
}

/**
 * @brief 性能测试方法
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 性能测试：大规模数据
    const int SIZE = 100;
    vector<vector<char>> matrix(SIZE, vector<char>(SIZE));
    
    // 初始化矩阵
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            matrix[i][j] = (i + j) % 2 == 0 ? '1' : '0';
        }
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result = maximalRectangleOptimized(matrix);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [" << SIZE << "x" << SIZE << "矩阵]: 结果=" << result 
         << ", 耗时: " << duration.count() << "ms" << endl;
    
    cout << "=== 性能测试完成！ ===" << endl;
}

/**
 * @brief 主函数
 */
int main() {
    // 运行功能测试
    testMaximalRectangle();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/**
 * 算法复杂度分析:
 * 
 * 时间复杂度: O(rows * cols)
 * - 外层循环遍历rows行
 * - 内层循环遍历cols列，并调用O(cols)的单调栈算法
 * - 总时间复杂度为O(rows * cols)
 * 
 * 空间复杂度: O(cols)
 * - 高度数组大小为cols
 * - 单调栈大小为cols
 * - 优化版本需要额外的left和right数组
 * 
 * 最优解分析:
 * - 这是最大矩形问题的最优解之一
 * - 无法获得更好的时间复杂度
 * - 空间复杂度也是最优的
 * 
 * C++特性利用:
 * - 使用vector和stack提供高效的数据结构
 * - 使用algorithm库的max函数
 * - 使用chrono库进行精确性能测量
 * - 使用RAII原则自动管理内存
 * 
 * 问题转化技巧:
 * - 将二维矩阵问题转化为多个一维柱状图问题
 * - 逐行构建高度数组，记录连续1的累积高度
 * - 对每一行应用柱状图中最大矩形的单调栈解法
 */