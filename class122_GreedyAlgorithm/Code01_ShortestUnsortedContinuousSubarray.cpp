#include <vector>
#include <algorithm>
#include <climits>
#include <iostream>

using namespace std;

// 最短无序连续子数组
// 给你一个整数数组nums，你需要找出一个 连续子数组
// 如果对这个子数组进行升序排序，那么整个数组都会变为升序排序
// 请你找出符合题意的最短子数组，并输出它的长度
// 测试链接 : https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/
// 相关题目链接：
// https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/ (LeetCode 581)
// https://www.lintcode.com/problem/shortest-unsorted-continuous-subarray/ (LintCode 1206)
// https://practice.geeksforgeeks.org/problems/minimum-subarray-to-sort/ (GeeksforGeeks)
// https://www.nowcoder.com/practice/2f9264b48cc24799925d48d355094c78 (牛客网)
// https://ac.nowcoder.com/acm/problem/14251 (牛客网)
// https://codeforces.com/problemset/problem/1139/C (Codeforces)
// https://atcoder.jp/contests/abc134/tasks/abc134_c (AtCoder)
// https://www.hackerrank.com/challenges/shortest-unsorted-continuous-subarray/problem (HackerRank)
// https://www.luogu.com.cn/problem/P1525 (洛谷)
// https://vjudge.net/problem/HDU-6375 (HDU)
// https://www.spoj.com/problems/ARRAYSUB/ (SPOJ)
// https://www.codechef.com/problems/SUBSPLAY (CodeChef)

class Solution {
public:
    /**
     * 找到最短无序连续子数组
     * 
     * 算法思路：
     * 使用两次遍历的贪心策略：
     * 1. 从左到右遍历，维护最大值，如果当前元素小于最大值，则更新右边界
     * 2. 从右到左遍历，维护最小值，如果当前元素大于最小值，则更新左边界
     * 
     * 时间复杂度：O(n) - 需要遍历数组两次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * @param nums 输入的整数数组
     * @return 需要排序的最短子数组长度
     */
    static int findUnsortedSubarray(vector<int>& nums) {
        int n = nums.size();
        
        // 从左往右遍历，找到最右的不达标位置
        // max > 当前数，认为不达标（即当前数应该在前面）
        int right = -1;
        int max_val = INT_MIN;
        for (int i = 0; i < n; i++) {
            if (max_val > nums[i]) {
                right = i;
            }
            max_val = max(max_val, nums[i]);
        }
        
        // 从右往左遍历，找到最左的不达标位置
        // min < 当前数，认为不达标（即当前数应该在后面）
        int min_val = INT_MAX;
        int left = n;
        for (int i = n - 1; i >= 0; i--) {
            if (min_val < nums[i]) {
                left = i;
            }
            min_val = min(min_val, nums[i]);
        }
        
        // 如果left和right没有更新，说明数组已经有序
        // 否则返回子数组长度
        return max(0, right - left + 1);
    }
};

// 测试用例
int main() {
    // 测试用例1: [2, 6, 4, 8, 10, 9, 15] -> [6, 4, 8, 10, 9] 长度为5
    vector<int> nums1 = {2, 6, 4, 8, 10, 9, 15};
    cout << "测试用例1: [";
    for (int i = 0; i < nums1.size(); i++) {
        cout << nums1[i];
        if (i < nums1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "结果: " << Solution::findUnsortedSubarray(nums1) << endl; // 期望输出: 5
    
    // 测试用例2: [1, 2, 3, 4] -> 已经有序，长度为0
    vector<int> nums2 = {1, 2, 3, 4};
    cout << "测试用例2: [";
    for (int i = 0; i < nums2.size(); i++) {
        cout << nums2[i];
        if (i < nums2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "结果: " << Solution::findUnsortedSubarray(nums2) << endl; // 期望输出: 0
    
    // 测试用例3: [1] -> 单个元素，长度为0
    vector<int> nums3 = {1};
    cout << "测试用例3: [";
    for (int i = 0; i < nums3.size(); i++) {
        cout << nums3[i];
        if (i < nums3.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "结果: " << Solution::findUnsortedSubarray(nums3) << endl; // 期望输出: 0
    
    return 0;
}