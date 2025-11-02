#include <iostream>
#include <vector>
#include <cmath>
#include <unordered_set>
#include <unordered_map>
using namespace std;

/*
 * 连续正整数和判断 - C++实现
 * 
 * 题目描述：
 * 判断一个正整数是否可以表示为连续正整数的和
 * 
 * 解题思路：
 * 这是一个数论问题，可以使用数学公式、滑动窗口或数学规律来解决
 * 1. 数学公式解法：利用等差数列求和公式
 * 2. 滑动窗口解法：双指针遍历可能的连续序列
 * 3. 数学规律解法：通过数学推导发现规律
 * 
 * 相关题目：
 * 1. LeetCode 829. Consecutive Numbers Sum：https://leetcode.com/problems/consecutive-numbers-sum/
 * 2. LeetCode 53. Maximum Subarray：https://leetcode.com/problems/maximum-subarray/
 * 3. LeetCode 128. Longest Consecutive Sequence：https://leetcode.com/problems/longest-consecutive-sequence/
 * 4. LeetCode 560. Subarray Sum Equals K：https://leetcode.com/problems/subarray-sum-equals-k/
 * 
 * 工程化考量：
 * 1. 异常处理：处理负数和零输入
 * 2. 边界条件：处理小规模数据
 * 3. 性能优化：使用数学规律O(1)解法
 * 4. 可读性：清晰的变量命名和注释
 */

class ConsecutiveNumbers {
public:
    // 方法1：数学公式解法
    static bool isSumOfConsecutiveMath(int n) {
        if (n <= 2) return false;
        
        // n = m*k + m*(m-1)/2
        // 其中m是连续数的个数，k是起始数字
        for (int m = 2; m * (m - 1) / 2 < n; m++) {
            int numerator = n - m * (m - 1) / 2;
            if (numerator % m == 0 && numerator / m > 0) {
                return true;
            }
        }
        return false;
    }
    
    // 方法2：滑动窗口解法
    static bool isSumOfConsecutiveSliding(int n) {
        if (n <= 2) return false;
        
        int left = 1, right = 1;
        int sum = 0;
        
        while (left <= n / 2 + 1) {
            if (sum < n) {
                sum += right;
                right++;
            } else if (sum > n) {
                sum -= left;
                left++;
            } else {
                return true;
            }
        }
        return false;
    }
    
    // 方法3：数学规律解法（最优解）
    static bool isSumOfConsecutiveOptimal(int n) {
        // 数学规律：一个数可以表示为连续正整数和当且仅当它不是2的幂
        // 因为2的幂只能表示为自身，不能拆分为多个连续正整数
        if (n <= 2) return false;
        
        // 检查是否是2的幂
        return (n & (n - 1)) != 0;
    }
    
    // ==================== 扩展题目1: 连续正整数和的个数 ====================
    /*
     * LeetCode 829. Consecutive Numbers Sum
     * 题目：计算一个数可以表示为连续正整数和的方案数
     * 网址：https://leetcode.com/problems/consecutive-numbers-sum/
     * 
     * 数学解法：
     * n = k + (k+1) + ... + (k+m-1) = m*k + m*(m-1)/2
     * 时间复杂度：O(sqrt(n))
     * 空间复杂度：O(1)
     */
    static int consecutiveNumbersSum(int n) {
        int count = 0;
        
        for (int m = 1; m * (m - 1) / 2 < n; m++) {
            int numerator = n - m * (m - 1) / 2;
            if (numerator % m == 0 && numerator / m > 0) {
                count++;
            }
        }
        
        return count;
    }
    
    // ==================== 扩展题目2: 最大连续子数组和 ====================
    /*
     * LeetCode 53. Maximum Subarray
     * 题目：求最大连续子数组和
     * 网址：https://leetcode.com/problems/maximum-subarray/
     * 
     * Kadane算法：
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    static int maxSubArray(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.size(); i++) {
            currentSum = max(nums[i], currentSum + nums[i]);
            maxSum = max(maxSum, currentSum);
        }
        
        return maxSum;
    }
    
    // ==================== 扩展题目3: 最长连续序列 ====================
    /*
     * LeetCode 128. Longest Consecutive Sequence
     * 题目：求最长连续数字序列的长度
     * 网址：https://leetcode.com/problems/longest-consecutive-sequence/
     * 
     * 哈希集合解法：
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static int longestConsecutive(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        unordered_set<int> numSet(nums.begin(), nums.end());
        int longest = 0;
        
        for (int num : numSet) {
            // 只从序列的起点开始计算
            if (numSet.find(num - 1) == numSet.end()) {
                int currentNum = num;
                int currentLength = 1;
                
                while (numSet.find(currentNum + 1) != numSet.end()) {
                    currentNum++;
                    currentLength++;
                }
                
                longest = max(longest, currentLength);
            }
        }
        
        return longest;
    }
    
    // ==================== 扩展题目4: 和为K的子数组 ====================
    /*
     * LeetCode 560. Subarray Sum Equals K
     * 题目：计算和为K的子数组个数
     * 网址：https://leetcode.com/problems/subarray-sum-equals-k/
     * 
     * 前缀和+哈希表解法：
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static int subarraySum(vector<int>& nums, int k) {
        unordered_map<int, int> prefixSumCount;
        prefixSumCount[0] = 1;
        
        int count = 0;
        int prefixSum = 0;
        
        for (int num : nums) {
            prefixSum += num;
            if (prefixSumCount.find(prefixSum - k) != prefixSumCount.end()) {
                count += prefixSumCount[prefixSum - k];
            }
            prefixSumCount[prefixSum]++;
        }
        
        return count;
    }
};

// 测试函数
int main() {
    cout << "=== 连续正整数和判断测试 ===" << endl;
    for (int i = 1; i <= 20; i++) {
        bool result1 = ConsecutiveNumbers::isSumOfConsecutiveMath(i);
        bool result2 = ConsecutiveNumbers::isSumOfConsecutiveSliding(i);
        bool result3 = ConsecutiveNumbers::isSumOfConsecutiveOptimal(i);
        cout << i << ": " << (result1 ? "true" : "false") << " / " 
             << (result2 ? "true" : "false") << " / " 
             << (result3 ? "true" : "false") << endl;
    }
    
    cout << "\n=== 扩展题目测试 ===" << endl;
    
    // 测试连续正整数和的个数
    cout << "Consecutive Numbers Sum (15): " << ConsecutiveNumbers::consecutiveNumbersSum(15) << endl;
    
    // 测试最大子数组和
    vector<int> nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    cout << "Maximum Subarray: " << ConsecutiveNumbers::maxSubArray(nums1) << endl;
    
    // 测试最长连续序列
    vector<int> nums2 = {100, 4, 200, 1, 3, 2};
    cout << "Longest Consecutive: " << ConsecutiveNumbers::longestConsecutive(nums2) << endl;
    
    // 测试和为K的子数组
    vector<int> nums3 = {1, 1, 1};
    cout << "Subarray Sum Equals K (2): " << ConsecutiveNumbers::subarraySum(nums3, 2) << endl;
    
    return 0;
}