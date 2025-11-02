/**
 * 2281. 巫师的总力量和 (Sum of Total Strength of Wizards)
 * 
 * 题目描述:
 * 作为国王的统治者，你有一支巫师军队听你指挥。
 * 给你一个下标从 0 开始的整数数组 strength ，其中 strength[i] 表示第 i 位巫师的力量值。
 * 对于连续的一组巫师（也就是这些巫师的力量值组成了一个连续子数组），总力量为以下两个值的乘积：
 * 巫师中最弱的能力值。
 * 组中所有巫师的能力值的和。
 * 请你返回所有可能的连续巫师组的总力量之和。
 * 
 * 解题思路:
 * 使用单调栈找到每个元素作为最小值能覆盖的区间范围。
 * 结合前缀和的前缀和（二次前缀和）技术来计算子数组和之和。
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * 测试链接: https://leetcode.cn/problems/sum-of-total-strength-of-wizards/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、边界情况处理
 * 2. 性能优化：使用vector预分配空间，避免动态内存分配
 * 3. 大数处理：使用long long类型避免溢出，及时取模
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
 * @brief 计算所有连续巫师组的总力量之和
 * 
 * @param strength 巫师力量值数组
 * @return int 总力量之和模 10^9 + 7
 */
int totalStrength(vector<int>& strength) {
    // 边界条件检查
    if (strength.empty()) {
        return 0;
    }
    
    int n = strength.size();
    
    // 前缀和数组
    vector<long long> prefix(n + 1, 0);
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = (prefix[i] + strength[i]) % MOD;
    }
    
    // 前缀和的前缀和（二次前缀和）
    vector<long long> prefixPrefix(n + 2, 0);
    for (int i = 0; i <= n; i++) {
        prefixPrefix[i + 1] = (prefixPrefix[i] + prefix[i]) % MOD;
    }
    
    // 使用单调栈找到每个元素作为最小值的左右边界
    vector<int> left(n, -1);   // 左边第一个小于当前元素的位置
    vector<int> right(n, n);   // 右边第一个小于等于当前元素的位置
    
    stack<int> st;
    
    // 找到右边第一个小于等于当前元素的位置
    for (int i = 0; i < n; i++) {
        while (!st.empty() && strength[st.top()] >= strength[i]) {
            right[st.top()] = i;
            st.pop();
        }
        st.push(i);
    }
    
    // 清空栈
    while (!st.empty()) st.pop();
    
    // 找到左边第一个小于当前元素的位置
    for (int i = n - 1; i >= 0; i--) {
        while (!st.empty() && strength[st.top()] > strength[i]) {
            left[st.top()] = i;
            st.pop();
        }
        st.push(i);
    }
    
    // 计算总力量
    long long total = 0;
    for (int i = 0; i < n; i++) {
        int L = left[i] + 1;  // 左边界（包含）
        int R = right[i] - 1; // 右边界（包含）
        
        // 计算以strength[i]为最小值的所有子数组的和之和
        long long leftSum = (prefixPrefix[i + 1] - prefixPrefix[L] + MOD) % MOD;
        long long rightSum = (prefixPrefix[R + 2] - prefixPrefix[i + 1] + MOD) % MOD;
        
        // 计算贡献
        long long contribution = (rightSum * (i - L + 1) - leftSum * (R - i + 1)) % MOD;
        contribution = (contribution * strength[i]) % MOD;
        
        total = (total + contribution) % MOD;
    }
    
    // 处理负数情况
    return (int)((total + MOD) % MOD);
}

/**
 * @brief 优化版本：使用数组模拟栈提高性能
 */
int totalStrengthOptimized(vector<int>& strength) {
    if (strength.empty()) {
        return 0;
    }
    
    int n = strength.size();
    
    // 前缀和数组
    vector<long long> prefix(n + 1, 0);
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = (prefix[i] + strength[i]) % MOD;
    }
    
    // 前缀和的前缀和（二次前缀和）
    vector<long long> prefixPrefix(n + 2, 0);
    for (int i = 0; i <= n; i++) {
        prefixPrefix[i + 1] = (prefixPrefix[i] + prefix[i]) % MOD;
    }
    
    // 使用数组模拟栈
    vector<int> stack(n);
    int top = -1;
    
    vector<int> left(n, -1);
    vector<int> right(n, n);
    
    // 找到右边第一个小于等于当前元素的位置
    for (int i = 0; i < n; i++) {
        while (top >= 0 && strength[stack[top]] >= strength[i]) {
            right[stack[top--]] = i;
        }
        stack[++top] = i;
    }
    
    top = -1;
    
    // 找到左边第一个小于当前元素的位置
    for (int i = n - 1; i >= 0; i--) {
        while (top >= 0 && strength[stack[top]] > strength[i]) {
            left[stack[top--]] = i;
        }
        stack[++top] = i;
    }
    
    // 计算总力量
    long long total = 0;
    for (int i = 0; i < n; i++) {
        int L = left[i] + 1;
        int R = right[i] - 1;
        
        long long leftSum = (prefixPrefix[i + 1] - prefixPrefix[L] + MOD) % MOD;
        long long rightSum = (prefixPrefix[R + 2] - prefixPrefix[i + 1] + MOD) % MOD;
        
        long long contribution = (rightSum * (i - L + 1) - leftSum * (R - i + 1)) % MOD;
        contribution = (contribution * strength[i]) % MOD;
        
        total = (total + contribution) % MOD;
    }
    
    return (int)((total + MOD) % MOD);
}

/**
 * @brief 测试方法 - 验证算法正确性
 */
void testTotalStrength() {
    cout << "=== 巫师的总力量和算法测试 ===" << endl;
    
    // 测试用例1: [1,3,1,2] - 预期: 44
    vector<int> strength1 = {1, 3, 1, 2};
    int result1 = totalStrength(strength1);
    int result1Opt = totalStrengthOptimized(strength1);
    cout << "测试用例1 [1,3,1,2]: " << result1 << " (优化版: " << result1Opt << ", 预期: 44)" << endl;
    
    // 测试用例2: [5,4,6] - 预期: 213
    vector<int> strength2 = {5, 4, 6};
    int result2 = totalStrength(strength2);
    int result2Opt = totalStrengthOptimized(strength2);
    cout << "测试用例2 [5,4,6]: " << result2 << " (优化版: " << result2Opt << ", 预期: 213)" << endl;
    
    // 测试用例3: 边界情况 - 空数组
    vector<int> strength3 = {};
    int result3 = totalStrength(strength3);
    int result3Opt = totalStrengthOptimized(strength3);
    cout << "测试用例3 []: " << result3 << " (优化版: " << result3Opt << ", 预期: 0)" << endl;
    
    // 测试用例4: 单元素数组 [10] - 预期: 100
    vector<int> strength4 = {10};
    int result4 = totalStrength(strength4);
    int result4Opt = totalStrengthOptimized(strength4);
    cout << "测试用例4 [10]: " << result4 << " (优化版: " << result4Opt << ", 预期: 100)" << endl;
    
    // 测试用例5: 重复元素 [2,2,2] - 预期: 36
    vector<int> strength5 = {2, 2, 2};
    int result5 = totalStrength(strength5);
    int result5Opt = totalStrengthOptimized(strength5);
    cout << "测试用例5 [2,2,2]: " << result5 << " (优化版: " << result5Opt << ", 预期: 36)" << endl;
    
    cout << "=== 功能测试完成！ ===" << endl;
}

/**
 * @brief 性能测试方法
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 性能测试：大规模数据
    const int SIZE = 1000;
    vector<int> strength(SIZE, 1); // 所有元素为1
    
    auto start = chrono::high_resolution_clock::now();
    int result = totalStrengthOptimized(strength);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 [" << SIZE << "个1]: 结果=" << result 
         << ", 耗时: " << duration.count() << "ms" << endl;
    
    // 性能测试：最坏情况 - 严格递减
    vector<int> strengthWorst(SIZE);
    for (int i = 0; i < SIZE; i++) {
        strengthWorst[i] = SIZE - i;
    }
    
    start = chrono::high_resolution_clock::now();
    int resultWorst = totalStrengthOptimized(strengthWorst);
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
    testTotalStrength();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/**
 * 算法复杂度分析:
 * 
 * 时间复杂度: O(n)
 * - 构建前缀和数组: O(n)
 * - 构建二次前缀和数组: O(n)
 * - 单调栈处理: O(n)
 * - 计算总贡献: O(n)
 * 
 * 空间复杂度: O(n)
 * - 前缀和数组: O(n)
 * - 二次前缀和数组: O(n)
 * - 左右边界数组: O(n)
 * - 单调栈: O(n)
 * 
 * 最优解分析:
 * - 这是巫师的总力量和问题的最优解
 * - 无法在O(n)时间内获得更好的时间复杂度
 * - 空间复杂度也是最优的
 * 
 * C++特性利用:
 * - 使用vector代替原生数组，更安全
 * - 使用stack容器提供标准栈操作
 * - 使用chrono库进行精确性能测量
 * - 使用RAII原则自动管理内存
 * 
 * 数学原理:
 * - 使用单调栈找到每个元素作为最小值的区间
 * - 使用前缀和的前缀和（二次前缀和）技术快速计算子数组和之和
 * - 贡献 = 最小值 * (子数组和之和)
 */