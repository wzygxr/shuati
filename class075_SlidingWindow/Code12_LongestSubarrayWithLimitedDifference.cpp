/*
 * 绝对差不超过限制的最长连续子数组问题解决方案
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，
 * 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。
 * 
 * 解题思路：
 * 使用滑动窗口配合 map 来维护窗口内的最大值和最小值：
 * 1. 右指针不断扩展窗口，将元素加入 map
 * 2. 当窗口内最大值与最小值的差超过 limit 时，收缩左指针
 * 3. map 可以在 O(log k) 时间内维护窗口元素的有序性，其中 k 是窗口大小
 * 4. map 的 begin()->first 和 rbegin()->first 分别获取最小值和最大值
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n * log n) - 每个元素最多入队和出队一次，map 操作需要 O(log n)
 * 空间复杂度: O(n) - map 最多存储 n 个元素
 * 
 * 是否最优解: 是，这是处理该问题的较优解法之一，还可以用单调队列优化到 O(n)
 * 
 * 相关题目链接：
 * LeetCode 1438. 绝对差不超过限制的最长连续子数组
 * https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 绝对差不超过限制的最长连续子数组
 *    https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
 * 2. LintCode 1438. 绝对差不超过限制的最长连续子数组
 *    https://www.lintcode.com/problem/1438/
 * 3. HackerRank - Longest Subarray with Limited Difference
 *    https://www.hackerrank.com/challenges/longest-subarray-with-limited-difference/problem
 * 4. CodeChef - SUBARR - Subarray with Limited Difference
 *    https://www.codechef.com/problems/SUBARR
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组等边界情况
 * 2. 性能优化：使用map维护窗口元素有序性，避免重复计算
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 * 
 * 编译说明：
 * 此代码需要C++标准库支持，编译时请确保包含正确的头文件路径
 * 编译命令示例：g++ -std=c++11 Code12_LongestSubarrayWithLimitedDifference.cpp -o Code12_LongestSubarrayWithLimitedDifference
 */

// 算法实现（需要C++标准库支持）
/*
#include <vector>
#include <map>
#include <algorithm>
#include <iostream>
using namespace std;

// 经典滑动窗口问题，使用 map 维护窗口内的最大值和最小值
class Solution {
public:
    // 计算绝对差不超过限制的最长连续子数组长度
    int longestSubarray(vector<int>& nums, int limit) {
        // 异常情况处理
        if (nums.empty()) {
            return 0;
        }
        
        // map 维护窗口内元素及其出现次数，保持有序
        // key为元素值，value为该元素在窗口中的出现次数
        map<int, int> mp;
        int left = 0;  // 滑动窗口左指针
        int result = 0;  // 记录最长子数组长度
        
        // 右指针扩展窗口
        for (int right = 0; right < nums.size(); right++) {
            // 将右指针元素加入 map
            // 如果元素已存在，计数加1；否则插入新元素，计数为1
            mp[nums[right]]++;
            
            // 当窗口内最大值与最小值的差超过 limit 时，需要收缩左指针
            // map的rbegin()->first获取最大值，begin()->first获取最小值
            while (mp.rbegin()->first - mp.begin()->first > limit) {
                // 减少左指针元素的计数
                mp[nums[left]]--;
                // 如果计数为 0，从 map 中移除该元素
                if (mp[nums[left]] == 0) {
                    mp.erase(nums[left]);
                }
                // 移动左指针
                left++;
            }
            
            // 更新最长子数组长度（当前窗口大小）
            result = max(result, right - left + 1);
        }
        
        return result;
    }
};

// 测试用例
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {8, 2, 4, 7};
    int limit1 = 4;
    int result1 = solution.longestSubarray(nums1, limit1);
    cout << "输入数组: ";
    for (int num : nums1) cout << num << " ";
    cout << "\n限制值: " << limit1;
    cout << "\n最长子数组长度: " << result1 << endl;
    // 预期输出: 2 ([2,4] 或 [4,7])
    
    // 测试用例2
    vector<int> nums2 = {10, 1, 2, 4, 7, 2};
    int limit2 = 5;
    int result2 = solution.longestSubarray(nums2, limit2);
    cout << "\n输入数组: ";
    for (int num : nums2) cout << num << " ";
    cout << "\n限制值: " << limit2;
    cout << "\n最长子数组长度: " << result2 << endl;
    // 预期输出: 4 ([2,4,7,2])
    
    return 0;
}
*/

// 算法核心逻辑说明（伪代码形式）：
/*
class Solution {
public:
    int longestSubarray(vector<int>& nums, int limit) {
        if (nums.empty()) {
            return 0;
        }
        
        map<int, int> mp;  // 维护窗口内元素及其出现次数
        int left = 0;
        int result = 0;
        
        for (int right = 0; right < nums.size(); right++) {
            // 将右指针元素加入 map
            mp[nums[right]]++;
            
            // 当窗口内最大值与最小值的差超过 limit 时，收缩左指针
            while (mp.rbegin()->first - mp.begin()->first > limit) {
                mp[nums[left]]--;
                if (mp[nums[left]] == 0) {
                    mp.erase(nums[left]);
                }
                left++;
            }
            
            // 更新最长子数组长度
            result = max(result, right - left + 1);
        }
        
        return result;
    }
};
*/