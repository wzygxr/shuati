// 绝对差不超过限制的最长连续子数组
// 给你一个整数数组 nums ，和一个表示限制的整数 limit
// 请你返回最长连续子数组的长度
// 该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit
// 如果不存在满足条件的子数组，则返回 0
// 测试链接 : https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
//
// 题目解析：
// 这是一道结合滑动窗口和单调队列的题目。我们需要找到最长的连续子数组，
// 使得子数组中任意两个元素的绝对差不超过limit。
//
// 算法思路：
// 1. 使用滑动窗口技术，维护一个变长窗口
// 2. 使用两个单调队列分别维护窗口内的最大值和最小值：
//    - maxDeque：单调递减队列，队首为窗口最大值
//    - minDeque：单调递增队列，队首为窗口最小值
// 3. 当窗口内最大值与最小值的差超过limit时，收缩窗口左边界
// 4. 记录满足条件的最长窗口长度
//
// 时间复杂度：O(n) - 每个元素最多入队和出队各两次（最大值队列和最小值队列各一次）
// 空间复杂度：O(n) - 两个单调队列最多存储n个元素

#include <vector>
#include <deque>
#include <algorithm>
using namespace std;

class Solution {
public:
    // 计算绝对差不超过限制的最长连续子数组长度
    // nums: 输入数组
    // limit: 限制值
    // 返回: 满足条件的最长子数组长度
    int longestSubarray(vector<int>& nums, int limit) {
        // 窗口内最大值的更新结构（单调队列）
        deque<int> maxDeque;
        // 窗口内最小值的更新结构（单调队列）
        deque<int> minDeque;
        
        int n = nums.size();
        int ans = 0;
        
        // 滑动窗口，l为左边界，r为右边界
        for (int l = 0, r = 0; l < n; l++) {
            // [l,r)，r永远是没有进入窗口的、下一个数所在的位置
            // 扩展窗口右边界，直到不满足条件
            while (r < n && ok(maxDeque, minDeque, nums[r], limit)) {
                push(maxDeque, minDeque, nums, r++);
            }
            
            // 从while出来的时候，[l,r)是l开头的子数组能向右延伸的最大范围
            // 更新最长子数组长度
            ans = max(ans, r - l);
            
            // 收缩窗口左边界
            pop(maxDeque, minDeque, l);
        }
        
        return ans;
    }

private:
    // 判断如果加入数字number，窗口最大值 - 窗口最小值是否依然 <= limit
    // 依然 <= limit，返回true
    // 不再 <= limit，返回false
    bool ok(deque<int>& maxDeque, deque<int>& minDeque, int number, int limit) {
        // max : 如果number进来，新窗口的最大值
        int maxVal = !maxDeque.empty() ? max(number, maxDeque.front()) : number;
        // min : 如果number进来，新窗口的最小值
        int minVal = !minDeque.empty() ? min(number, minDeque.front()) : number;
        return maxVal - minVal <= limit;
    }

    // r位置的数字进入窗口，修改窗口内最大值的更新结构、修改窗口内最小值的更新结构
    // 维护两个单调队列的单调性
    void push(deque<int>& maxDeque, deque<int>& minDeque, vector<int>& nums, int r) {
        // 维护最大值队列的单调递减性质
        while (!maxDeque.empty() && nums[maxDeque.back()] <= nums[r]) {
            maxDeque.pop_back();
        }
        maxDeque.push_back(r);
        
        // 维护最小值队列的单调递增性质
        while (!minDeque.empty() && nums[minDeque.back()] >= nums[r]) {
            minDeque.pop_back();
        }
        minDeque.push_back(r);
    }

    // 窗口要吐出l位置的数了！检查过期！
    // 检查队首元素是否需要出队（即是否超出窗口范围）
    void pop(deque<int>& maxDeque, deque<int>& minDeque, int l) {
        // 如果最大值队列的队首元素是l位置的元素，则出队
        if (!maxDeque.empty() && maxDeque.front() == l) {
            maxDeque.pop_front();
        }
        // 如果最小值队列的队首元素是l位置的元素，则出队
        if (!minDeque.empty() && minDeque.front() == l) {
            minDeque.pop_front();
        }
    }
};