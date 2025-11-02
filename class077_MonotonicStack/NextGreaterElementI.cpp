#include <iostream>
#include <vector>
#include <stack>
#include <unordered_map>
using namespace std;

/**
 * Next Greater Element I（下一个更大元素 I）
 * 
 * 题目描述:
 * nums1 中数字 x 的 下一个更大元素 是指 x 在 nums2 中对应位置右侧的第一个比 x 大的元素。
 * 给你两个没有重复元素的数组 nums1 和 nums2，下标从 0 开始计数，其中 nums1 是 nums2 的子集。
 * 对于每个 0 <= i < nums1.length，找出满足 nums1[i] == nums2[j] 的下标 j，并且在 nums2 确定 nums2[j] 的下一个更大元素。
 * 如果不存在下一个更大元素，那么本次查询的答案是 -1。
 * 返回一个长度为 nums1.length 的数组 ans 作为答案，满足 ans[i] 是如上所述的下一个更大元素。
 * 
 * 解题思路:
 * 使用单调栈预处理 nums2 数组，得到每个元素的下一个更大元素，然后通过哈希表快速查询。
 * 维护一个单调递减栈，栈中存储元素值。
 * 当遇到一个比栈顶元素大的元素时，说明栈顶元素的下一个更大元素就是当前元素。
 * 
 * 时间复杂度: O(nums1.length + nums2.length)，需要遍历两个数组各一次
 * 空间复杂度: O(nums2.length)，单调栈和哈希表的空间
 * 
 * 测试链接: https://leetcode.cn/problems/next-greater-element-i/
 */
class Solution {
public:
    vector<int> nextGreaterElement(vector<int>& nums1, vector<int>& nums2) {
        int n1 = nums1.size();
        int n2 = nums2.size();
        vector<int> result(n1);
        
        // 使用单调栈预处理nums2，得到每个元素的下一个更大元素
        stack<int> stk; // 单调递减栈
        unordered_map<int, int> nextGreaterMap;
        
        // 遍历nums2
        for (int i = 0; i < n2; i++) {
            // 当栈不为空且当前元素大于栈顶元素时
            while (!stk.empty() && stk.top() < nums2[i]) {
                int num = stk.top();
                stk.pop();
                nextGreaterMap[num] = nums2[i]; // 记录下一个更大元素
            }
            stk.push(nums2[i]); // 将当前元素压入栈
        }
        
        // 查询nums1中每个元素的下一个更大元素
        for (int i = 0; i < n1; i++) {
            if (nextGreaterMap.find(nums1[i]) != nextGreaterMap.end()) {
                result[i] = nextGreaterMap[nums1[i]];
            } else {
                result[i] = -1;
            }
        }
        
        return result;
    }
};

// 测试函数
void printVector(const vector<int>& vec) {
    for (int val : vec) {
        cout << val << " ";
    }
    cout << endl;
}

void test() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1_1 = {4, 1, 2};
    vector<int> nums2_1 = {1, 3, 4, 2};
    vector<int> result1 = solution.nextGreaterElement(nums1_1, nums2_1);
    // 预期输出: [-1, 3, -1]
    cout << "测试用例1输出: ";
    printVector(result1);
    
    // 测试用例2
    vector<int> nums1_2 = {2, 4};
    vector<int> nums2_2 = {1, 2, 3, 4};
    vector<int> result2 = solution.nextGreaterElement(nums1_2, nums2_2);
    // 预期输出: [3, -1]
    cout << "测试用例2输出: ";
    printVector(result2);
}

int main() {
    test();
    return 0;
}