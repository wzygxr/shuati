#include <vector>
#include <stack>
#include <climits>
#include <iostream>
using namespace std;

/**
 * 132模式
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，数组中共有 n 个整数。132 模式的子序列由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
 * 并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j] 。
 * 如果 nums 中存在 132 模式的子序列，返回 true；否则，返回 false。
 * 
 * 测试链接：https://leetcode.cn/problems/132-pattern/
 * 
 * 解题思路：
 * 使用单调栈来解决这个问题。我们从右向左遍历数组，维护一个单调递减栈：
 * 1. 栈中存储可能作为"3"的元素（中间最大元素）
 * 2. 使用一个变量third记录可能作为"2"的元素（右侧较小元素）
 * 3. 当我们找到一个元素小于third时，就找到了一个132模式
 * 
 * 具体步骤：
 * 1. 从右向左遍历数组
 * 2. 如果当前元素小于third，说明找到了132模式，返回true
 * 3. 当栈不为空且栈顶元素小于当前元素时，弹出栈顶元素并更新third
 * 4. 将当前元素入栈
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈和出栈各一次，n为数组长度
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间最多为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 */
class Solution {
public:
    bool find132pattern(vector<int>& nums) {
        // 边界条件检查
        if (nums.size() < 3) {
            return false;
        }
        
        // 使用栈存储可能作为"3"的元素
        stack<int> st;
        // 记录可能作为"2"的元素（右侧较小元素）
        int third = INT_MIN;
        
        // 从右向左遍历数组
        for (int i = nums.size() - 1; i >= 0; i--) {
            // 如果当前元素小于third，说明找到了132模式
            if (nums[i] < third) {
                return true;
            }
            
            // 当栈不为空且栈顶元素小于当前元素时，弹出栈顶元素并更新third
            while (!st.empty() && st.top() < nums[i]) {
                third = st.top();  // 更新third为弹出的元素
                st.pop();
            }
            
            // 将当前元素入栈
            st.push(nums[i]);
        }
        
        return false;
    }
};

// 测试用例
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 3, 4};
    cout << "测试用例1: ";
    for (int num : nums1) cout << num << " ";
    cout << endl;
    cout << "输出: " << (solution.find132pattern(nums1) ? "true" : "false") << endl; // 期望输出: false
    
    // 测试用例2
    vector<int> nums2 = {3, 1, 4, 2};
    cout << "测试用例2: ";
    for (int num : nums2) cout << num << " ";
    cout << endl;
    cout << "输出: " << (solution.find132pattern(nums2) ? "true" : "false") << endl; // 期望输出: true
    
    // 测试用例3
    vector<int> nums3 = {-1, 3, 2, 0};
    cout << "测试用例3: ";
    for (int num : nums3) cout << num << " ";
    cout << endl;
    cout << "输出: " << (solution.find132pattern(nums3) ? "true" : "false") << endl; // 期望输出: true
    
    // 测试用例4
    vector<int> nums4 = {1, 0, 1, -4, -3};
    cout << "测试用例4: ";
    for (int num : nums4) cout << num << " ";
    cout << endl;
    cout << "输出: " << (solution.find132pattern(nums4) ? "true" : "false") << endl; // 期望输出: false
    
    return 0;
}