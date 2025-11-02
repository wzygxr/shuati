#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>

using namespace std;

/**
 * 题目名称：LeetCode 239. 滑动窗口最大值
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/sliding-window-maximum/
 * 题目难度：困难
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 */

class Solution {
public:
    vector<int> maxSlidingWindow(vector<int>& nums, int k) {
        if (nums.empty() || k <= 0) {
            return {};
        }
        
        int n = nums.size();
        if (k == 1) {
            return nums;
        }
        
        vector<int> result(n - k + 1);
        deque<int> dq;
        
        for (int i = 0; i < n; i++) {
            while (!dq.empty() && nums[dq.back()] <= nums[i]) {
                dq.pop_back();
            }
            
            dq.push_back(i);
            
            if (dq.front() <= i - k) {
                dq.pop_front();
            }
            
            if (i >= k - 1) {
                result[i - k + 1] = nums[dq.front()];
            }
        }
        
        return result;
    }
};

void testMaxSlidingWindow() {
    Solution solution;
    cout << "=== LeetCode 239 测试用例 ===" << endl;
    
    vector<int> nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
    int k1 = 3;
    vector<int> result1 = solution.maxSlidingWindow(nums1, k1);
    cout << "测试用例1 - 输入: [1,3,-1,-3,5,3,6,7], k=3" << endl;
    cout << "预期输出: [3,3,5,5,6,7], 实际输出: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ",";
    }
    cout << "]" << endl;
    
    cout << "\n=== 算法分析 ===" << endl;
    cout << "时间复杂度: O(n) - 每个元素最多入队出队一次" << endl;
    cout << "空间复杂度: O(k) - 双端队列最多存储k个元素" << endl;
    cout << "最优解: ✅ 是" << endl;
}

int main() {
    testMaxSlidingWindow();
    return 0;
}