// LeetCode 862. 和至少为 K 的最短子数组
// 给你一个整数数组 nums 和一个整数 k ，找出 nums 中和至少为 k 的最短非空子数组，并返回该子数组的长度。
// 如果不存在这样的子数组，返回 -1。
// 测试链接 : https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/

/**
 * 解题思路:
 * 这是一个比普通滑动窗口更复杂的问题，因为数组可能包含负数，所以不能使用简单的滑动窗口。
 * 我们需要使用前缀和 + 单调队列的方法来解决。
 * 
 * 核心思想:
 * 1. 计算前缀和数组 prefix，其中 prefix[i] = nums[0] + nums[1] + ... + nums[i-1]
 * 2. 问题转化为：找到一对索引 (i, j)，使得 prefix[j] - prefix[i] >= k 且 j - i 最小
 * 3. 使用单调递增队列来维护可能的最优左边界
 * 4. 对于每个右边界 j，在队列中寻找满足 prefix[j] - prefix[i] >= k 的最大的 i
 * 
 * 时间复杂度: O(n) - 每个元素最多入队出队一次
 * 空间复杂度: O(n) - 需要存储前缀和和队列
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么使用单调队列？
 *    - 我们需要快速找到满足条件的最小 j-i
 *    - 单调队列可以保证队列中的前缀和是递增的，这样我们可以快速排除不可能的解
 * 2. 为什么在队首出队？
 *    - 当 prefix[j] - prefix[queue.front()] >= k 时，说明找到了一个解
 *    - 由于队列是单调递增的，队首的索引最小，所以 j - queue.front() 就是当前最短长度
 * 3. 为什么在队尾维护单调性？
 *    - 如果 prefix[j] <= prefix[queue.back()]，那么 queue.back() 不可能是最优解
 *    - 因为对于更大的 k，j 比 queue.back() 更有可能满足条件
 * 
 * 工程化考量:
 * 1. 使用 long 类型避免整数溢出
 * 2. 处理负数的情况需要特殊考虑
 * 3. 边界情况：k=0，空数组等
 */

#include <vector>
#include <deque>
#include <climits>
#include <algorithm>
using namespace std;

class Solution {
public:
    int shortestSubarray(vector<int>& nums, int k) {
        int n = nums.size();
        
        // 异常处理
        if (n == 0) return -1;
        if (k <= 0) return 1; // 任何非空子数组的和都 >= 0
        
        // 计算前缀和，使用long避免溢出
        vector<long> prefix(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        
        // 单调递增队列，存储索引
        deque<int> dq;
        int minLength = INT_MAX;
        
        for (int j = 0; j <= n; j++) {
            // 从队首移除满足条件的解
            while (!dq.empty() && prefix[j] - prefix[dq.front()] >= k) {
                minLength = min(minLength, j - dq.front());
                dq.pop_front();
            }
            
            // 维护队列单调性：从队尾移除比当前前缀和大的索引
            while (!dq.empty() && prefix[j] <= prefix[dq.back()]) {
                dq.pop_back();
            }
            
            // 当前索引入队
            dq.push_back(j);
        }
        
        return minLength == INT_MAX ? -1 : minLength;
    }
};

// 测试代码
#include <iostream>
int main() {
    Solution solution;
    
    // 测试用例1：正常情况
    vector<int> nums1 = {2, -1, 2};
    int k1 = 3;
    cout << "测试用例1: [2, -1, 2], k=3" << endl;
    cout << "最短长度: " << solution.shortestSubarray(nums1, k1) << endl; // 预期输出: 3
    
    // 测试用例2：包含负数
    vector<int> nums2 = {1, 2, 3, -2, 5};
    int k2 = 6;
    cout << "测试用例2: [1, 2, 3, -2, 5], k=6" << endl;
    cout << "最短长度: " << solution.shortestSubarray(nums2, k2) << endl; // 预期输出: 2
    
    // 测试用例3：不存在满足条件的子数组
    vector<int> nums3 = {1, 1, 1};
    int k3 = 5;
    cout << "测试用例3: [1, 1, 1], k=5" << endl;
    cout << "最短长度: " << solution.shortestSubarray(nums3, k3) << endl; // 预期输出: -1
    
    return 0;
}

/*
 * 相关题目扩展:
 * 1. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
 * 2. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
 * 3. LeetCode 560. 和为 K 的子数组 - https://leetcode.cn/problems/subarray-sum-equals-k/
 * 4. LeetCode 325. 和等于k的最长子数组长度 - https://leetcode.cn/problems/maximum-size-subarray-sum-equals-k/
 * 
 * 算法技巧总结:
 * 1. 前缀和 + 单调队列是处理带负数子数组问题的有效方法
 * 2. 单调队列可以快速找到满足条件的最优解
 * 3. 时间复杂度O(n)，空间复杂度O(n)
 * 
 * 工程化思考:
 * 1. 对于大规模数据，需要考虑内存使用和性能
 * 2. 可以封装为模板类，支持不同的数值类型
 * 3. 在实际应用中，可能需要处理浮点数或其他数据类型
 */

// Java 实现
/*
import java.util.Deque;
import java.util.LinkedList;

class Solution {
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return -1;
        if (k <= 0) return 1;
        
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        
        Deque<Integer> deque = new LinkedList<>();
        int minLength = Integer.MAX_VALUE;
        
        for (int j = 0; j <= n; j++) {
            while (!deque.isEmpty() && prefix[j] - prefix[deque.peekFirst()] >= k) {
                minLength = Math.min(minLength, j - deque.pollFirst());
            }
            
            while (!deque.isEmpty() && prefix[j] <= prefix[deque.peekLast()]) {
                deque.pollLast();
            }
            
            deque.offerLast(j);
        }
        
        return minLength == Integer.MAX_VALUE ? -1 : minLength;
    }
}

// 时间复杂度: O(n)
// 空间复杂度: O(n)
// 是否最优解: 是
*/

// Python 实现
"""
from collections import deque

class Solution:
    def shortestSubarray(self, nums: List[int], k: int) -> int:
        n = len(nums)
        if n == 0:
            return -1
        if k <= 0:
            return 1
            
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + nums[i]
            
        dq = deque()
        min_length = float('inf')
        
        for j in range(n + 1):
            while dq and prefix[j] - prefix[dq[0]] >= k:
                min_length = min(min_length, j - dq.popleft())
                
            while dq and prefix[j] <= prefix[dq[-1]]:
                dq.pop()
                
            dq.append(j)
            
        return min_length if min_length != float('inf') else -1

# 时间复杂度: O(n)
# 空间复杂度: O(n)
# 是否最优解: 是
*/