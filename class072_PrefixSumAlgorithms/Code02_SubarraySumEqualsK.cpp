/**
 * 和为K的子数组 (Subarray Sum Equals K)
 * 
 * 题目描述:
 * 给你一个整数数组 nums 和一个整数 k，请你统计并返回该数组中和为 k 的子数组的个数。
 * 子数组是数组中元素的连续非空序列。
 * 
 * 示例:
 * 输入：nums = [1,1,1], k = 2
 * 输出：2
 * 
 * 输入：nums = [1,2,3], k = 3
 * 输出：2
 * 
 * 提示:
 * 1 <= nums.length <= 2 * 10^4
 * -1000 <= nums[i] <= 1000
 * -10^7 <= k <= 10^7
 * 
 * 题目链接: https://leetcode.com/problems/subarray-sum-equals-k/
 * 
 * 解题思路:
 * 使用前缀和 + 哈希表的方法。
 * 1. 遍历数组，计算前缀和
 * 2. 对于当前位置的前缀和sum，查找是否存在前缀和为(sum - k)的历史记录
 * 3. 如果存在，则说明存在子数组和为k
 * 4. 使用哈希表记录每个前缀和出现的次数
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、k值极端情况
 * 2. 哈希表选择：unordered_map提供O(1)的平均查找时间
 * 3. 整数溢出：使用long long避免大数溢出
 * 4. 负数处理：k可能为负数，但算法本身支持负数
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能统计所有子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1] = k
 * 即prefix[j] - k = prefix[i-1]，因此统计prefix[j] - k出现的次数即可。
 */

#include <vector>
#include <unordered_map>
#include <iostream>

using namespace std;

class Solution {
public:
    /**
     * 计算和为k的子数组个数
     * 
     * @param nums 输入数组
     * @param k 目标和
     * @return 和为k的子数组个数
     */
    int subarraySum(vector<int>& nums, int k) {
        // 边界情况处理
        if (nums.empty()) {
            return 0;
        }
        
        // 使用unordered_map记录前缀和及其出现次数
        // 初始化：前缀和为0出现1次（表示空数组）
        unordered_map<long long, int> prefixSumCount;
        prefixSumCount[0] = 1;
        
        int count = 0;          // 结果计数
        long long prefixSum = 0; // 当前前缀和，使用long long避免溢出
        
        // 遍历数组
        for (int num : nums) {
            // 更新前缀和
            prefixSum += num;
            
            // 查找是否存在前缀和为(prefixSum - k)的历史记录
            // 如果存在，说明存在子数组和为k
            if (prefixSumCount.find(prefixSum - k) != prefixSumCount.end()) {
                count += prefixSumCount[prefixSum - k];
            }
            
            // 更新当前前缀和的出现次数
            prefixSumCount[prefixSum]++;
        }
        
        return count;
    }
};

/**
 * 测试函数
 */
void testSubarraySum() {
    Solution solution;
    
    // 测试用例1：经典情况
    vector<int> nums1 = {1, 1, 1};
    int k1 = 2;
    int result1 = solution.subarraySum(nums1, k1);
    cout << "测试用例1 [1,1,1] k=2: " << result1 << " (预期: 2)" << endl;
    
    // 测试用例2：多个子数组
    vector<int> nums2 = {1, 2, 3};
    int k2 = 3;
    int result2 = solution.subarraySum(nums2, k2);
    cout << "测试用例2 [1,2,3] k=3: " << result2 << " (预期: 2)" << endl;
    
    // 测试用例3：包含0和负数
    vector<int> nums3 = {1, -1, 0};
    int k3 = 0;
    int result3 = solution.subarraySum(nums3, k3);
    cout << "测试用例3 [1,-1,0] k=0: " << result3 << " (预期: 3)" << endl;
    
    // 测试用例4：单个元素
    vector<int> nums4 = {5};
    int k4 = 5;
    int result4 = solution.subarraySum(nums4, k4);
    cout << "测试用例4 [5] k=5: " << result4 << " (预期: 1)" << endl;
    
    // 测试用例5：空数组
    vector<int> nums5 = {};
    int k5 = 1;
    int result5 = solution.subarraySum(nums5, k5);
    cout << "测试用例5 [] k=1: " << result5 << " (预期: 0)" << endl;
    
    // 测试用例6：大k值
    vector<int> nums6 = {1, 2, 3};
    int k6 = 100;
    int result6 = solution.subarraySum(nums6, k6);
    cout << "测试用例6 [1,2,3] k=100: " << result6 << " (预期: 0)" << endl;
}

/**
 * 主函数
 */
int main() {
    cout << "=== 和为K的子数组测试 ===" << endl;
    testSubarraySum();
    return 0;
}