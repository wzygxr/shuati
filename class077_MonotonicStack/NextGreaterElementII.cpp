/**
 * 503. 下一个更大元素 II (Next Greater Element II)
 * 
 * 题目描述:
 * 给定一个循环数组 nums（最后一个元素的下一个元素是数组的第一个元素），
 * 返回每个元素的下一个更大元素。如果不存在，则输出 -1。
 * 
 * 解题思路:
 * 使用单调栈来解决。由于是循环数组，可以遍历数组两次来模拟循环效果。
 * 维护一个单调递减栈，栈中存储元素索引。
 * 当遇到比栈顶元素大的元素时，说明找到了栈顶元素的下一个更大元素。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，用于存储单调栈和结果数组
 * 
 * 测试链接: https://leetcode.cn/problems/next-greater-element-ii/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、边界情况处理
 * 2. 性能优化：使用vector预分配空间，避免动态内存分配
 * 3. 循环数组处理：遍历两次模拟循环效果
 * 4. 内存管理：使用RAII原则管理资源
 */

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <chrono>

using namespace std;

/**
 * @brief 查找循环数组中每个元素的下一个更大元素
 * 
 * @param nums 输入循环数组
 * @return vector<int> 每个元素的下一个更大元素数组
 */
vector<int> nextGreaterElements(vector<int>& nums) {
    // 边界条件检查
    if (nums.empty()) {
        return {};
    }
    
    int n = nums.size();
    vector<int> result(n, -1); // 初始化为-1
    
    stack<int> st; // 使用标准库栈
    
    // 遍历两次数组模拟循环效果
    for (int i = 0; i < 2 * n; i++) {
        int actualIndex = i % n; // 实际数组索引
        
        // 当栈不为空且当前元素大于栈顶索引对应的元素时
        while (!st.empty() && nums[actualIndex] > nums[st.top()]) {
            result[st.top()] = nums[actualIndex]; // 设置下一个更大元素
            st.pop();
        }
        
        // 只在第一次遍历时将索引入栈
        if (i < n) {
            st.push(actualIndex);
        }
    }
    
    return result;
}

/**
 * @brief 优化版本：使用数组模拟栈提高性能
 */
vector<int> nextGreaterElementsOptimized(vector<int>& nums) {
    if (nums.empty()) {
        return {};
    }
    
    int n = nums.size();
    vector<int> result(n, -1);
    
    // 使用vector模拟栈，预分配空间
    vector<int> stack;
    stack.reserve(2 * n);
    int top = -1;
    
    for (int i = 0; i < 2 * n; i++) {
        int idx = i % n;
        
        while (top >= 0 && nums[idx] > nums[stack[top]]) {
            result[stack[top--]] = nums[idx];
        }
        
        // 只在第一次遍历时入栈
        if (i < n) {
            stack[++top] = idx;
        }
    }
    
    return result;
}

/**
 * @brief 测试方法 - 验证算法正确性
 */
void testNextGreaterElements() {
    cout << "=== 下一个更大元素II算法测试 ===" << endl;
    
    // 测试用例1: [1,2,1] - 预期: [2,-1,2]
    vector<int> nums1 = {1, 2, 1};
    vector<int> result1 = nextGreaterElements(nums1);
    vector<int> result1Opt = nextGreaterElementsOptimized(nums1);
    cout << "测试用例1 [1,2,1]: ";
    for (int val : result1) cout << val << " ";
    cout << "(优化版: ";
    for (int val : result1Opt) cout << val << " ";
    cout << "预期: [2, -1, 2])" << endl;
    
    // 测试用例2: [1,2,3,4,3] - 预期: [2,3,4,-1,4]
    vector<int> nums2 = {1, 2, 3, 4, 3};
    vector<int> result2 = nextGreaterElements(nums2);
    vector<int> result2Opt = nextGreaterElementsOptimized(nums2);
    cout << "测试用例2 [1,2,3,4,3]: ";
    for (int val : result2) cout << val << " ";
    cout << "(优化版: ";
    for (int val : result2Opt) cout << val << " ";
    cout << "预期: [2, 3, 4, -1, 4])" << endl;
    
    // 测试用例3: 边界情况 - 空数组
    vector<int> nums3 = {};
    vector<int> result3 = nextGreaterElements(nums3);
    vector<int> result3Opt = nextGreaterElementsOptimized(nums3);
    cout << "测试用例3 []: ";
    for (int val : result3) cout << val << " ";
    cout << "(优化版: ";
    for (int val : result3Opt) cout << val << " ";
    cout << "预期: [])" << endl;
    
    // 测试用例4: 单元素数组 [5] - 预期: [-1]
    vector<int> nums4 = {5};
    vector<int> result4 = nextGreaterElements(nums4);
    vector<int> result4Opt = nextGreaterElementsOptimized(nums4);
    cout << "测试用例4 [5]: ";
    for (int val : result4) cout << val << " ";
    cout << "(优化版: ";
    for (int val : result4Opt) cout << val << " ";
    cout << "预期: [-1])" << endl;
    
    // 测试用例5: 所有元素相同 [2,2,2] - 预期: [-1,-1,-1]
    vector<int> nums5 = {2, 2, 2};
    vector<int> result5 = nextGreaterElements(nums5);
    vector<int> result5Opt = nextGreaterElementsOptimized(nums5);
    cout << "测试用例5 [2,2,2]: ";
    for (int val : result5) cout << val << " ";
    cout << "(优化版: ";
    for (int val : result5Opt) cout << val << " ";
    cout << "预期: [-1, -1, -1])" << endl;
    
    cout << "=== 功能测试完成！ ===" << endl;
}

/**
 * @brief 性能测试方法
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 性能测试：大规模数据
    const int SIZE = 10000;
    vector<int> nums(SIZE, 1); // 所有元素为1
    nums[5000] = 2; // 中间插入一个较大值
    
    auto start = chrono::high_resolution_clock::now();
    vector<int> result = nextGreaterElementsOptimized(nums);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [" << SIZE << "个元素]: 耗时: " << duration.count() << "ms" << endl;
    
    // 性能测试：最坏情况 - 严格递减
    vector<int> numsWorst(SIZE);
    for (int i = 0; i < SIZE; i++) {
        numsWorst[i] = SIZE - i;
    }
    
    start = chrono::high_resolution_clock::now();
    vector<int> resultWorst = nextGreaterElementsOptimized(numsWorst);
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [最坏情况" << SIZE << "个元素]: 耗时: " << duration.count() << "ms" << endl;
    
    cout << "=== 性能测试完成！ ===" << endl;
}

/**
 * @brief 主函数
 */
int main() {
    // 运行功能测试
    testNextGreaterElements();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/**
 * 算法复杂度分析:
 * 
 * 时间复杂度: O(n)
 * - 虽然遍历了2n次，但每个元素最多入栈和出栈各一次
 * - 总操作次数为O(n)
 * 
 * 空间复杂度: O(n)
 * - 使用了大小为2n的栈数组（实际最多使用n个位置）
 * - 结果数组大小为n
 * 
 * 最优解分析:
 * - 这是循环数组下一个更大元素问题的最优解
 * - 无法在O(n)时间内获得更好的时间复杂度
 * - 空间复杂度也是最优的
 * 
 * C++特性利用:
 * - 使用vector代替原生数组，更安全
 * - 使用stack容器提供标准栈操作
 * - 使用chrono库进行精确性能测量
 * - 使用RAII原则自动管理内存
 * 
 * 循环数组处理技巧:
 * - 通过取模运算实现循环访问: i % n
 * - 遍历两次数组确保覆盖所有可能的下一更大元素
 * - 只在第一次遍历时入栈，避免重复处理
 */