/**
 * 和可被K整除的子数组 (Subarray Sums Divisible by K)
 * 
 * 题目描述:
 * 给定一个整数数组 A，返回其中元素之和可被 K 整除的（连续、非空）子数组的数目。
 * 
 * 示例:
 * 输入: A = [4,5,0,-2,-3,1], K = 5
 * 输出: 7
 * 解释: 有 7 个子数组满足其元素之和可被 K = 5 整除。
 * 
 * 输入: A = [5], K = 9
 * 输出: 0
 * 
 * 提示:
 * 1 <= A.length <= 3 * 10^4
 * -10^4 <= A[i] <= 10^4
 * 2 <= K <= 10^4
 * 
 * 题目链接: https://leetcode.com/problems/subarray-sums-divisible-by-k/
 * 
 * 解题思路:
 * 1. 利用前缀和的性质：如果两个前缀和除以K的余数相同，那么这两个位置之间的子数组和可被K整除
 * 2. 使用前缀和 + 哈希表的方法
 * 3. 遍历数组，计算前缀和并对K取余
 * 4. 使用哈希表记录每个余数出现的次数
 * 5. 对于相同的余数，任意两个位置之间的子数组都满足条件
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(min(n, K)) - 哈希表最多存储K个不同的余数或n个前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、K=0、K=1等特殊情况
 * 2. 负数取模处理：C++中负数取模结果为负，需要转换为正数
 * 3. 哈希表选择：unordered_map提供O(1)的平均查找时间
 * 4. 整数溢出：使用long long避免大数溢出
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能统计所有子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1]。
 * 当(prefix[j] - prefix[i-1]) % K = 0时，即prefix[j] % K = prefix[i-1] % K。
 * 因此统计相同余数的前缀和对数即可。
 */

#include <vector>
#include <unordered_map>
#include <iostream>
#include <chrono>

using namespace std;

class Solution {
public:
    /**
     * 计算和可被K整除的子数组数目
     * 
     * @param A 输入数组
     * @param K 除数
     * @return 和可被K整除的子数组数目
     * 
     * 异常场景处理:
     * - 空数组：返回0
     * - K=0：返回0（除数不能为0）
     * - K=1：所有子数组都满足条件
     */
    int subarraysDivByK(vector<int>& A, int K) {
        // 边界情况处理
        if (A.empty() || K == 0) {
            return 0;
        }
        
        // 如果K=1，所有子数组都满足条件
        if (K == 1) {
            int n = A.size();
            return n * (n + 1) / 2;
        }
        
        // 使用unordered_map记录每个余数出现的次数
        unordered_map<int, int> map;
        // 初始化：余数为0出现1次（表示空前缀）
        map[0] = 1;
        
        int count = 0;              // 结果计数
        long long prefixSum = 0;    // 当前前缀和，使用long long避免溢出
        
        // 遍历数组
        for (int num : A) {
            // 更新前缀和
            prefixSum += num;
            
            // 计算前缀和对K的余数（处理负数情况）
            // C++中负数取模结果为负，需要转换为[0, K-1]范围内的正数
            int remainder = prefixSum % K;
            if (remainder < 0) {
                remainder += K;
            }
            
            // 如果该余数之前出现过，说明存在满足条件的子数组
            if (map.find(remainder) != map.end()) {
                count += map[remainder];
            }
            
            // 更新该余数的出现次数
            map[remainder]++;
        }
        
        return count;
    }
};

/**
 * 测试函数
 */
void testSubarraysDivByK() {
    Solution solution;
    
    // 测试用例1：经典情况
    vector<int> A1 = {4, 5, 0, -2, -3, 1};
    int K1 = 5;
    int result1 = solution.subarraysDivByK(A1, K1);
    cout << "测试用例1 [4,5,0,-2,-3,1] K=5: " << result1 << " (预期: 7)" << endl;
    
    // 测试用例2：单个元素
    vector<int> A2 = {5};
    int K2 = 9;
    int result2 = solution.subarraysDivByK(A2, K2);
    cout << "测试用例2 [5] K=9: " << result2 << " (预期: 0)" << endl;
    
    // 测试用例3：K=1的情况
    vector<int> A3 = {1, 2, 3};
    int K3 = 1;
    int result3 = solution.subarraysDivByK(A3, K3);
    cout << "测试用例3 [1,2,3] K=1: " << result3 << " (预期: 6)" << endl;
    
    // 测试用例4：空数组
    vector<int> A4 = {};
    int K4 = 5;
    int result4 = solution.subarraysDivByK(A4, K4);
    cout << "测试用例4 [] K=5: " << result4 << " (预期: 0)" << endl;
    
    // 测试用例5：K=0的情况
    vector<int> A5 = {1, 2, 3};
    int K5 = 0;
    int result5 = solution.subarraysDivByK(A5, K5);
    cout << "测试用例5 [1,2,3] K=0: " << result5 << " (预期: 0)" << endl;
    
    // 测试用例6：全0数组
    vector<int> A6 = {0, 0, 0};
    int K6 = 2;
    int result6 = solution.subarraysDivByK(A6, K6);
    cout << "测试用例6 [0,0,0] K=2: " << result6 << " (预期: 6)" << endl;
    
    // 测试用例7：包含负数
    vector<int> A7 = {-1, 2, 9};
    int K7 = 2;
    int result7 = solution.subarraysDivByK(A7, K7);
    cout << "测试用例7 [-1,2,9] K=2: " << result7 << " (预期: 2)" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===" << endl;
    Solution solution;
    int size = 30000; // 3万元素（题目上限）
    int K = 1000;      // K的上限
    vector<int> largeArray(size);
    
    // 初始化大数组
    for (int i = 0; i < size; i++) {
        largeArray[i] = (i % 200) - 100; // 包含正负数
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int result = solution.subarraysDivByK(largeArray, K);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "处理 " << size << " 个元素，结果: " << result << ", 耗时: " << duration.count() << "ms" << endl;
}

/**
 * 主函数
 */
int main() {
    cout << "=== 和可被K整除的子数组测试 ===" << endl;
    testSubarraysDivByK();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    cout << "\n=== 测试完成 ===" << endl;
    return 0;
}