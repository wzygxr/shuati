/**
 * 907. 子数组的最小值之和 (Sum of Subarray Minimums)
 * 
 * 题目描述:
 * 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
 * 由于答案可能很大，因此返回答案模 10^9 + 7。
 * 
 * 解题思路:
 * 使用单调栈来解决。对于每个元素，找到它作为最小值能覆盖的左右边界。
 * 然后计算该元素对总和的贡献：arr[i] * (左边界的长度) * (右边界的长度)
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，用于存储单调栈和左右边界数组
 * 
 * 测试链接: https://leetcode.cn/problems/sum-of-subarray-minimums/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、边界情况处理
 * 2. 性能优化：使用vector预分配空间，避免动态内存分配
 * 3. 大数处理：使用long long类型避免溢出，最后取模
 * 4. 内存管理：使用RAII原则管理资源
 */

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <chrono>

using namespace std;

const int MOD = 1000000007;

/**
 * @brief 计算所有子数组的最小值之和
 * 
 * @param arr 输入整数数组
 * @return int 子数组最小值之和模 10^9 + 7
 */
int sumSubarrayMins(vector<int>& arr) {
    // 边界条件检查
    if (arr.empty()) {
        return 0;
    }
    
    int n = arr.size();
    stack<int> st;
    vector<int> left(n, -1);   // 左边第一个比当前元素小的位置
    vector<int> right(n, n);   // 右边第一个比当前元素小的位置
    
    // 第一次遍历：找到每个元素右边第一个比它小的位置
    for (int i = 0; i < n; i++) {
        while (!st.empty() && arr[st.top()] > arr[i]) {
            right[st.top()] = i;
            st.pop();
        }
        st.push(i);
    }
    
    // 清空栈
    while (!st.empty()) st.pop();
    
    // 第二次遍历：找到每个元素左边第一个比它小的位置
    for (int i = n - 1; i >= 0; i--) {
        while (!st.empty() && arr[st.top()] >= arr[i]) {
            left[st.top()] = i;
            st.pop();
        }
        st.push(i);
    }
    
    // 计算总和
    long long sum = 0;
    for (int i = 0; i < n; i++) {
        long long leftCount = i - left[i];
        long long rightCount = right[i] - i;
        long long contribution = (leftCount * rightCount) % MOD;
        contribution = (contribution * arr[i]) % MOD;
        sum = (sum + contribution) % MOD;
    }
    
    return (int)sum;
}

/**
 * @brief 优化版本：一次遍历完成左右边界计算
 * 
 * @param arr 输入整数数组
 * @return int 子数组最小值之和模 10^9 + 7
 */
int sumSubarrayMinsOptimized(vector<int>& arr) {
    if (arr.empty()) {
        return 0;
    }
    
    int n = arr.size();
    stack<int> st;
    long long sum = 0;
    
    // 添加哨兵，简化边界处理
    vector<int> newArr = arr;
    newArr.push_back(0); // 哨兵值
    
    for (int i = 0; i <= n; i++) {
        while (!st.empty() && newArr[st.top()] > newArr[i]) {
            int index = st.top();
            st.pop();
            int left = st.empty() ? -1 : st.top();
            long long leftCount = index - left;
            long long rightCount = i - index;
            long long contribution = (leftCount * rightCount) % MOD;
            contribution = (contribution * newArr[index]) % MOD;
            sum = (sum + contribution) % MOD;
        }
        st.push(i);
    }
    
    return (int)sum;
}

/**
 * @brief 测试方法 - 验证算法正确性
 */
void testSumSubarrayMins() {
    cout << "=== 子数组最小值之和算法测试 ===" << endl;
    
    // 测试用例1: [3,1,2,4] - 预期: 17
    vector<int> arr1 = {3, 1, 2, 4};
    int result1 = sumSubarrayMins(arr1);
    int result1Opt = sumSubarrayMinsOptimized(arr1);
    cout << "测试用例1 [3,1,2,4]: " << result1 << " (优化版: " << result1Opt << ", 预期: 17)" << endl;
    
    // 测试用例2: [11,81,94,43,3] - 预期: 444
    vector<int> arr2 = {11, 81, 94, 43, 3};
    int result2 = sumSubarrayMins(arr2);
    int result2Opt = sumSubarrayMinsOptimized(arr2);
    cout << "测试用例2 [11,81,94,43,3]: " << result2 << " (优化版: " << result2Opt << ", 预期: 444)" << endl;
    
    // 测试用例3: 边界情况 - 空数组
    vector<int> arr3 = {};
    int result3 = sumSubarrayMins(arr3);
    int result3Opt = sumSubarrayMinsOptimized(arr3);
    cout << "测试用例3 []: " << result3 << " (优化版: " << result3Opt << ", 预期: 0)" << endl;
    
    // 测试用例4: 单元素数组 [5] - 预期: 5
    vector<int> arr4 = {5};
    int result4 = sumSubarrayMins(arr4);
    int result4Opt = sumSubarrayMinsOptimized(arr4);
    cout << "测试用例4 [5]: " << result4 << " (优化版: " << result4Opt << ", 预期: 5)" << endl;
    
    // 测试用例5: 重复元素 [2,2,2] - 预期: 12
    vector<int> arr5 = {2, 2, 2};
    int result5 = sumSubarrayMins(arr5);
    int result5Opt = sumSubarrayMinsOptimized(arr5);
    cout << "测试用例5 [2,2,2]: " << result5 << " (优化版: " << result5Opt << ", 预期: 12)" << endl;
    
    cout << "=== 功能测试完成！ ===" << endl;
}

/**
 * @brief 性能测试方法
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 性能测试：大规模数据 - 所有元素为1
    const int SIZE = 10000;
    vector<int> arr(SIZE, 1);
    
    auto start = chrono::high_resolution_clock::now();
    int result = sumSubarrayMinsOptimized(arr);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [" << SIZE << "个1]: 结果=" << result 
         << ", 耗时: " << duration.count() << "ms" << endl;
    
    // 性能测试：最坏情况 - 严格递减
    vector<int> arrWorst(SIZE);
    for (int i = 0; i < SIZE; i++) {
        arrWorst[i] = SIZE - i;
    }
    
    start = chrono::high_resolution_clock::now();
    int resultWorst = sumSubarrayMinsOptimized(arrWorst);
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [最坏情况" << SIZE << "个元素]: 结果=" << resultWorst 
         << ", 耗时: " << duration.count() << "ms" << endl;
    
    cout << "=== 性能测试完成！ ===" << endl;
}

/**
 * @brief 主函数
 */
int main() {
    // 运行功能测试
    testSumSubarrayMins();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/**
 * 算法复杂度分析:
 * 
 * 时间复杂度: O(n)
 * - 每个元素最多入栈一次和出栈一次
 * - 两次遍历数组，但总操作次数为O(n)
 * 
 * 空间复杂度: O(n)
 * - 使用了三个大小为n的vector：left、right、stack
 * - 优化版本使用了O(n)的额外空间
 * 
 * 最优解分析:
 * - 这是子数组最小值之和问题的最优解
 * - 无法在O(n)时间内获得更好的时间复杂度
 * - 空间复杂度也是最优的，因为需要存储边界信息
 * 
 * C++特性利用:
 * - 使用vector代替原生数组，更安全
 * - 使用stack容器提供标准栈操作
 * - 使用chrono库进行精确性能测量
 * - 使用RAII原则自动管理内存
 * 
 * 数学原理:
 * - 对于每个元素arr[i]，它作为最小值的子数组数量为：(i - left[i]) * (right[i] - i)
 * - 总贡献为：arr[i] * 子数组数量
 * - 所有元素的贡献之和即为答案
 */