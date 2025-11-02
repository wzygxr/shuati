#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * 题目名称：LeetCode 862. 和至少为K的最短子数组
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
 * 题目难度：困难
 * 
 * 题目描述：
 * 给定一个数组arr，其中的值有可能正、负、0
 * 给定一个正数k
 * 返回累加和>=k的所有子数组中，最短的子数组长度
 * 
 * 解题思路：
 * 使用单调队列解决该问题。核心思想是利用前缀和将问题转化为寻找满足条件的两个前缀和之差。
 * 对于前缀和数组，我们需要找到最小的 j-i，使得 sum[j] - sum[i] >= k。
 * 为了高效查找，我们维护一个单调递增队列，队列中存储前缀和的索引。
 *
 * 算法步骤：
 * 1. 计算前缀和数组
 * 2. 遍历前缀和数组，维护单调递增队列
 * 3. 对于每个前缀和，检查是否能与队首元素构成满足条件的子数组
 * 4. 维护队列的单调性
 *
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 *
 * 空间复杂度分析：
 * O(n) - 存储前缀和和单调队列
 *
 * 是否最优解：
 * ✅ 是，这是处理此类问题的最优解法
 */

class Solution {
public:
    int shortestSubarray(vector<int>& nums, int k) {
        if (nums.empty() || k <= 0) {
            return -1;
        }
        
        int n = nums.size();
        vector<long long> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        deque<int> dq;
        int minLength = INT_MAX;
        
        for (int i = 0; i <= n; i++) {
            while (!dq.empty() && prefixSum[i] - prefixSum[dq.front()] >= k) {
                minLength = min(minLength, i - dq.front());
                dq.pop_front();
            }
            
            while (!dq.empty() && prefixSum[dq.back()] >= prefixSum[i]) {
                dq.pop_back();
            }
            
            dq.push_back(i);
        }
        
        return minLength != INT_MAX ? minLength : -1;
    }
};

void testShortestSubarray() {
    Solution solution;
    cout << "=== LeetCode 862 测试用例 ===" << endl;
    
    vector<int> nums1 = {2, -1, 2};
    int k1 = 3;
    int result1 = solution.shortestSubarray(nums1, k1);
    cout << "测试用例1 - 输入: [2,-1,2], k=3" << endl;
    cout << "预期输出: 3, 实际输出: " << result1 << endl;
    cout << "测试结果: " << (result1 == 3 ? "✓ 通过" : "✗ 失败") << endl;
    
    vector<int> nums2 = {1, 2, -3, 4, 5};
    int k2 = 7;
    int result2 = solution.shortestSubarray(nums2, k2);
    cout << "\n测试用例2 - 输入: [1,2,-3,4,5], k=7" << endl;
    cout << "预期输出: 2, 实际输出: " << result2 << endl;
    cout << "测试结果: " << (result2 == 2 ? "✓ 通过" : "✗ 失败") << endl;
    
    cout << "\n=== 算法分析 ===" << endl;
    cout << "时间复杂度: O(n) - 每个元素最多入队出队一次" << endl;
    cout << "空间复杂度: O(n) - 前缀和数组和单调队列" << endl;
    cout << "最优解: ✅ 是" << endl;
}

int main() {
    testShortestSubarray();
    return 0;
}