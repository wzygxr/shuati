#include <iostream>
#include <vector>
#include <unordered_set>
#include <unordered_map>
#include <algorithm>

using namespace std;

/**
 * 1695. 删除子数组的最大得分
 * 给你一个正整数数组 nums ，请你从中删除一个含有 若干不同元素 的子数组。删除子数组的 得分 就是子数组各元素之 和 。
 * 返回 只删除一个 子数组可获得的 最大得分 。
 * 如果数组为空，返回 0 。
 * 
 * 解题思路：
 * 使用滑动窗口维护一个不含重复元素的子数组
 * 当遇到重复元素时，收缩左边界直到没有重复元素
 * 在滑动过程中记录最大和
 * 
 * 时间复杂度：O(n)，其中n是数组长度
 * 空间复杂度：O(k)，k是不同元素的数量
 * 
 * 是否最优解：是
 * 
 * 测试链接：https://leetcode.cn/problems/maximum-erasure-value/
 */
class Solution {
public:
    /**
     * 计算删除子数组的最大得分
     * 
     * @param nums 正整数数组
     * @return 最大得分
     */
    int maximumUniqueSubarray(vector<int>& nums) {
        int n = nums.size();
        int maxScore = 0; // 最大得分
        int currentSum = 0; // 当前窗口的和
        int left = 0; // 窗口左边界
        unordered_set<int> window; // 记录窗口内的元素
        
        // 滑动窗口右边界
        for (int right = 0; right < n; right++) {
            // 如果当前元素已经在窗口中，收缩左边界
            while (window.find(nums[right]) != window.end()) {
                currentSum -= nums[left];
                window.erase(nums[left]);
                left++;
            }
            
            // 添加当前元素到窗口
            window.insert(nums[right]);
            currentSum += nums[right];
            
            // 更新最大得分
            maxScore = max(maxScore, currentSum);
        }
        
        return maxScore;
    }
    
    /**
     * 优化版本：使用哈希表记录元素最后一次出现的位置
     * 时间复杂度：O(n)，空间复杂度：O(k)
     */
    int maximumUniqueSubarrayOptimized(vector<int>& nums) {
        int n = nums.size();
        int maxScore = 0;
        int currentSum = 0;
        int left = 0;
        unordered_map<int, int> lastSeen; // 记录元素最后一次出现的位置
        
        for (int right = 0; right < n; right++) {
            int num = nums[right];
            
            // 如果当前元素已经在窗口中，并且位置在left之后
            if (lastSeen.find(num) != lastSeen.end() && lastSeen[num] >= left) {
                // 移动左边界到重复元素的下一个位置
                int duplicateIndex = lastSeen[num];
                for (int i = left; i <= duplicateIndex; i++) {
                    currentSum -= nums[i];
                }
                left = duplicateIndex + 1;
            }
            
            // 更新当前元素的位置
            lastSeen[num] = right;
            currentSum += num;
            
            // 更新最大得分
            maxScore = max(maxScore, currentSum);
        }
        
        return maxScore;
    }
    
    /**
     * 使用前缀和数组优化版本
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    int maximumUniqueSubarrayWithPrefixSum(vector<int>& nums) {
        int n = nums.size();
        if (n == 0) return 0;
        
        // 计算前缀和数组
        vector<int> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        int maxScore = 0;
        int left = 0;
        unordered_map<int, int> lastSeen;
        
        for (int right = 0; right < n; right++) {
            int num = nums[right];
            
            // 如果当前元素已经在窗口中，并且位置在left之后
            if (lastSeen.find(num) != lastSeen.end() && lastSeen[num] >= left) {
                left = lastSeen[num] + 1;
            }
            
            // 更新当前元素的位置
            lastSeen[num] = right;
            
            // 计算当前窗口的和
            int currentSum = prefixSum[right + 1] - prefixSum[left];
            maxScore = max(maxScore, currentSum);
        }
        
        return maxScore;
    }
    
    /**
     * 使用数组代替哈希表（当数字范围有限时）
     * 时间复杂度：O(n)，空间复杂度：O(max_value)
     */
    int maximumUniqueSubarrayWithArray(vector<int>& nums) {
        int n = nums.size();
        if (n == 0) return 0;
        
        // 找到数组中的最大值
        int maxVal = 0;
        for (int num : nums) {
            maxVal = max(maxVal, num);
        }
        
        int maxScore = 0;
        int currentSum = 0;
        int left = 0;
        vector<bool> inWindow(maxVal + 1, false); // 记录元素是否在窗口中
        
        for (int right = 0; right < n; right++) {
            int num = nums[right];
            
            // 如果当前元素已经在窗口中，收缩左边界
            while (inWindow[num]) {
                currentSum -= nums[left];
                inWindow[nums[left]] = false;
                left++;
            }
            
            // 添加当前元素到窗口
            inWindow[num] = true;
            currentSum += num;
            
            // 更新最大得分
            maxScore = max(maxScore, currentSum);
        }
        
        return maxScore;
    }
};

// 测试函数
void testMaximumUniqueSubarray() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {4, 2, 4, 5, 6};
    int result1 = solution.maximumUniqueSubarray(nums1);
    cout << "输入数组: ";
    for (int num : nums1) cout << num << " ";
    cout << endl;
    cout << "最大得分: " << result1 << endl;
    cout << "预期: 17" << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {5, 2, 1, 2, 5, 2, 1, 2, 5};
    int result2 = solution.maximumUniqueSubarray(nums2);
    cout << "输入数组: ";
    for (int num : nums2) cout << num << " ";
    cout << endl;
    cout << "最大得分: " << result2 << endl;
    cout << "预期: 8" << endl;
    cout << endl;
    
    // 测试用例3：所有元素都不同
    vector<int> nums3 = {1, 2, 3, 4, 5};
    int result3 = solution.maximumUniqueSubarray(nums3);
    cout << "输入数组: ";
    for (int num : nums3) cout << num << " ";
    cout << endl;
    cout << "最大得分: " << result3 << endl;
    cout << "预期: 15" << endl;
    cout << endl;
    
    // 测试用例4：所有元素都相同
    vector<int> nums4 = {1, 1, 1, 1, 1};
    int result4 = solution.maximumUniqueSubarray(nums4);
    cout << "输入数组: ";
    for (int num : nums4) cout << num << " ";
    cout << endl;
    cout << "最大得分: " << result4 << endl;
    cout << "预期: 1" << endl;
    cout << endl;
    
    // 测试用例5：边界情况，单个元素
    vector<int> nums5 = {5};
    int result5 = solution.maximumUniqueSubarray(nums5);
    cout << "输入数组: ";
    for (int num : nums5) cout << num << " ";
    cout << endl;
    cout << "最大得分: " << result5 << endl;
    cout << "预期: 5" << endl;
    cout << endl;
    
    // 测试用例6：空数组
    vector<int> nums6 = {};
    int result6 = solution.maximumUniqueSubarray(nums6);
    cout << "输入数组: ";
    for (int num : nums6) cout << num << " ";
    cout << endl;
    cout << "最大得分: " << result6 << endl;
    cout << "预期: 0" << endl;
}

int main() {
    testMaximumUniqueSubarray();
    return 0;
}