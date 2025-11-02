// LeetCode 1425. 带限制的子序列和
// 给你一个整数数组 nums 和一个整数 k ，请你返回非空子序列的最大和，其中子序列中每两个相邻整数在原数组中的下标距离不超过 k。
// 测试链接 : https://leetcode.cn/problems/constrained-subsequence-sum/

/**
 * 解题思路:
 * 这是一个动态规划 + 单调队列优化的问题。我们需要找到满足相邻元素下标距离不超过k的最大子序列和。
 * 
 * 核心思想:
 * 1. 定义dp[i]为以nums[i]结尾的满足条件的最大子序列和
 * 2. 状态转移: dp[i] = nums[i] + max(0, max(dp[j]) for j in [i-k, i-1])
 * 3. 使用单调递减队列来维护前k个dp值的最大值
 * 4. 队列中存储的是索引，按照dp值递减排序
 * 
 * 时间复杂度: O(n) - 每个元素最多入队出队一次
 * 空间复杂度: O(n) - 需要dp数组和队列
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么使用单调递减队列？
 *    - 我们需要快速找到前k个dp值中的最大值
 *    - 单调递减队列的队首就是当前窗口内的最大值
 * 2. 队列中为什么存储索引而不是值？
 *    - 存储索引可以方便地判断元素是否在有效窗口内
 *    - 当队首索引超出窗口范围时，需要出队
 * 3. 为什么dp[i] = nums[i] + max(0, 队列最大值)？
 *    - 如果前面的最大值是负数，我们宁愿从当前元素重新开始
 *    - max(0, ...)保证了不会因为负数而降低当前子序列和
 * 
 * 工程化考量:
 * 1. 使用long类型避免整数溢出
 * 2. 处理k=0的特殊情况
 * 3. 边界情况：数组长度为1的情况
 */

#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
#include <climits>
using namespace std;

class Solution {
public:
    int constrainedSubsetSum(vector<int>& nums, int k) {
        int n = nums.size();
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        
        // 使用long避免溢出
        vector<long> dp(n);
        deque<int> dq; // 单调递减队列，存储索引
        
        int maxResult = INT_MIN;
        
        for (int i = 0; i < n; i++) {
            // 如果队列不为空，获取前k个dp值的最大值
            long maxPrev = 0;
            if (!dq.empty()) {
                maxPrev = max(0L, dp[dq.front()]);
            }
            
            // 计算当前dp值
            dp[i] = nums[i] + maxPrev;
            maxResult = max(maxResult, (int)dp[i]);
            
            // 维护队列单调性：从队尾移除比当前dp值小的元素
            while (!dq.empty() && dp[i] >= dp[dq.back()]) {
                dq.pop_back();
            }
            
            // 当前索引入队
            dq.push_back(i);
            
            // 移除超出窗口范围的元素
            while (!dq.empty() && dq.front() <= i - k) {
                dq.pop_front();
            }
        }
        
        return maxResult;
    }
};

// 测试代码
#include <iostream>
int main() {
    Solution solution;
    
    // 测试用例1：正常情况
    vector<int> nums1 = {10, 2, -10, 5, 20};
    int k1 = 2;
    cout << "测试用例1: [10, 2, -10, 5, 20], k=2" << endl;
    cout << "最大子序列和: " << solution.constrainedSubsetSum(nums1, k1) << endl; // 预期输出: 37
    
    // 测试用例2：k=1
    vector<int> nums2 = {-1, -2, -3};
    int k2 = 1;
    cout << "测试用例2: [-1, -2, -3], k=1" << endl;
    cout << "最大子序列和: " << solution.constrainedSubsetSum(nums2, k2) << endl; // 预期输出: -1
    
    // 测试用例3：全正数
    vector<int> nums3 = {10, 20, 30, 40};
    int k3 = 3;
    cout << "测试用例3: [10, 20, 30, 40], k=3" << endl;
    cout << "最大子序列和: " << solution.constrainedSubsetSum(nums3, k3) << endl; // 预期输出: 100
    
    return 0;
}

/*
 * 相关题目扩展:
 * 1. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
 * 2. LeetCode 239. 滑动窗口最大值 - https://leetcode.cn/problems/sliding-window-maximum/
 * 3. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
 * 4. LeetCode 300. 最长递增子序列 - https://leetcode.cn/problems/longest-increasing-subsequence/
 * 5. LeetCode 354. 俄罗斯套娃信封问题 - https://leetcode.cn/problems/russian-doll-envelopes/
 * 
 * 算法技巧总结:
 * 1. 动态规划 + 单调队列优化是处理带约束子序列问题的有效方法
 * 2. 单调队列可以快速获取窗口内的最大值/最小值
 * 3. 时间复杂度O(n)，空间复杂度O(n)
 * 
 * 工程化思考:
 * 1. 对于大规模数据，需要考虑内存使用和性能
 * 2. 可以封装为模板类，支持不同的数值类型和约束条件
 * 3. 在实际应用中，可能需要处理更复杂的约束条件
 */

// Java 实现
/*
import java.util.Deque;
import java.util.LinkedList;

class Solution {
    public int constrainedSubsetSum(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        
        long[] dp = new long[n];
        Deque<Integer> deque = new LinkedList<>();
        int maxResult = Integer.MIN_VALUE;
        
        for (int i = 0; i < n; i++) {
            long maxPrev = 0;
            if (!deque.isEmpty()) {
                maxPrev = Math.max(0, dp[deque.peekFirst()]);
            }
            
            dp[i] = nums[i] + maxPrev;
            maxResult = Math.max(maxResult, (int)dp[i]);
            
            while (!deque.isEmpty() && dp[i] >= dp[deque.peekLast()]) {
                deque.pollLast();
            }
            
            deque.offerLast(i);
            
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
        }
        
        return maxResult;
    }
}

// 时间复杂度: O(n)
// 空间复杂度: O(n)
// 是否最优解: 是
*/

// Python 实现
"""
from collections import deque
from typing import List

class Solution:
    def constrainedSubsetSum(self, nums: List[int], k: int) -> int:
        n = len(nums)
        if n == 0:
            return 0
        if n == 1:
            return nums[0]
            
        dp = [0] * n
        dq = deque()
        max_result = float('-inf')
        
        for i in range(n):
            max_prev = 0
            if dq:
                max_prev = max(0, dp[dq[0]])
                
            dp[i] = nums[i] + max_prev
            max_result = max(max_result, dp[i])
            
            while dq and dp[i] >= dp[dq[-1]]:
                dq.pop()
                
            dq.append(i)
            
            while dq and dq[0] <= i - k:
                dq.popleft()
                
        return max_result

# 时间复杂度: O(n)
# 空间复杂度: O(n)
# 是否最优解: 是
"""