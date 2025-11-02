#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

/**
 * 1004. 最大连续1的个数 III
 * 给定一个二进制数组 nums 和一个整数 k，如果可以翻转最多 k 个 0 ，则返回 数组中连续 1 的最大个数 。
 * 
 * 解题思路：
 * 使用滑动窗口维护一个最多包含k个0的窗口
 * 当窗口内0的个数超过k时，收缩左边界
 * 在滑动过程中记录最大窗口大小
 * 
 * 时间复杂度：O(n)，其中n是数组长度
 * 空间复杂度：O(1)
 * 
 * 是否最优解：是
 * 
 * 测试链接：https://leetcode.cn/problems/max-consecutive-ones-iii/
 */
class Solution {
public:
    /**
     * 计算最大连续1的个数（最多翻转k个0）
     * 
     * @param nums 二进制数组
     * @param k 最多可以翻转的0的个数
     * @return 最大连续1的个数
     */
    int longestOnes(vector<int>& nums, int k) {
        int n = nums.size();
        int maxLength = 0; // 最大长度
        int left = 0; // 窗口左边界
        int zeroCount = 0; // 窗口内0的个数
        
        // 滑动窗口右边界
        for (int right = 0; right < n; right++) {
            // 如果当前元素是0，增加0的计数
            if (nums[right] == 0) {
                zeroCount++;
            }
            
            // 如果窗口内0的个数超过k，收缩左边界
            while (zeroCount > k) {
                if (nums[left] == 0) {
                    zeroCount--;
                }
                left++;
            }
            
            // 更新最大长度
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 优化版本：使用更简洁的写法
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    int longestOnesOptimized(vector<int>& nums, int k) {
        int n = nums.size();
        int maxLength = 0;
        int left = 0;
        int zeroCount = 0;
        
        for (int right = 0; right < n; right++) {
            zeroCount += 1 - nums[right]; // 如果nums[right]是0，则zeroCount加1
            
            if (zeroCount > k) {
                zeroCount -= 1 - nums[left]; // 如果nums[left]是0，则zeroCount减1
                left++;
            }
            
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 另一种思路：使用双指针，不显式维护zeroCount
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    int longestOnesAlternative(vector<int>& nums, int k) {
        int n = nums.size();
        int maxLength = 0;
        int left = 0;
        int right = 0;
        int zeros = 0;
        
        while (right < n) {
            // 扩展右边界
            if (nums[right] == 0) {
                zeros++;
            }
            right++;
            
            // 如果0的个数超过k，收缩左边界
            while (zeros > k) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }
            
            // 更新最大长度
            maxLength = max(maxLength, right - left);
        }
        
        return maxLength;
    }
    
    /**
     * 使用前缀和思想（当k较大时效率更高）
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    int longestOnesWithPrefixSum(vector<int>& nums, int k) {
        int n = nums.size();
        int maxLength = 0;
        int left = 0;
        int zeroCount = 0;
        
        for (int right = 0; right < n; right++) {
            if (nums[right] == 0) {
                zeroCount++;
            }
            
            // 如果0的个数超过k，移动左边界
            if (zeroCount > k) {
                if (nums[left] == 0) {
                    zeroCount--;
                }
                left++;
            }
            
            // 更新最大长度
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
};

// 测试函数
void testLongestOnes() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0};
    int k1 = 2;
    int result1 = solution.longestOnes(nums1, k1);
    cout << "输入数组: ";
    for (int num : nums1) cout << num << " ";
    cout << endl;
    cout << "k = " << k1 << endl;
    cout << "最大连续1的个数: " << result1 << endl;
    cout << "预期: 6" << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1};
    int k2 = 3;
    int result2 = solution.longestOnes(nums2, k2);
    cout << "输入数组: ";
    for (int num : nums2) cout << num << " ";
    cout << endl;
    cout << "k = " << k2 << endl;
    cout << "最大连续1的个数: " << result2 << endl;
    cout << "预期: 10" << endl;
    cout << endl;
    
    // 测试用例3：k=0
    vector<int> nums3 = {1, 1, 0, 1, 1, 1};
    int k3 = 0;
    int result3 = solution.longestOnes(nums3, k3);
    cout << "输入数组: ";
    for (int num : nums3) cout << num << " ";
    cout << endl;
    cout << "k = " << k3 << endl;
    cout << "最大连续1的个数: " << result3 << endl;
    cout << "预期: 3" << endl;
    cout << endl;
    
    // 测试用例4：k大于0的个数
    vector<int> nums4 = {0, 0, 0, 0};
    int k4 = 2;
    int result4 = solution.longestOnes(nums4, k4);
    cout << "输入数组: ";
    for (int num : nums4) cout << num << " ";
    cout << endl;
    cout << "k = " << k4 << endl;
    cout << "最大连续1的个数: " << result4 << endl;
    cout << "预期: 2" << endl;
    cout << endl;
    
    // 测试用例5：全是1
    vector<int> nums5 = {1, 1, 1, 1, 1};
    int k5 = 2;
    int result5 = solution.longestOnes(nums5, k5);
    cout << "输入数组: ";
    for (int num : nums5) cout << num << " ";
    cout << endl;
    cout << "k = " << k5 << endl;
    cout << "最大连续1的个数: " << result5 << endl;
    cout << "预期: 5" << endl;
    cout << endl;
    
    // 测试用例6：边界情况，单个元素
    vector<int> nums6 = {0};
    int k6 = 1;
    int result6 = solution.longestOnes(nums6, k6);
    cout << "输入数组: ";
    for (int num : nums6) cout << num << " ";
    cout << endl;
    cout << "k = " << k6 << endl;
    cout << "最大连续1的个数: " << result6 << endl;
    cout << "预期: 1" << endl;
}

int main() {
    testLongestOnes();
    return 0;
}