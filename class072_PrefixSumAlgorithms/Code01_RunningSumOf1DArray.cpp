/**
 * 一维数组的动态和 (Running Sum of 1d Array)
 * 
 * 题目描述:
 * 给你一个数组 nums 。数组「动态和」的计算公式为：runningSum[i] = sum(nums[0]…nums[i]) 。
 * 请返回 nums 的动态和。
 * 
 * 示例:
 * 输入：nums = [1,2,3,4]
 * 输出：[1,3,6,10]
 * 解释：动态和计算过程为 [1, 1+2, 1+2+3, 1+2+3+4] 。
 * 
 * 输入：nums = [1,1,1,1,1]
 * 输出：[1,2,3,4,5]
 * 解释：动态和计算过程为 [1, 1+1, 1+1+1, 1+1+1+1, 1+1+1+1+1] 。
 * 
 * 输入：nums = [3,1,2,10,1]
 * 输出：[3,4,6,16,17]
 * 
 * 提示:
 * 1 <= nums.length <= 1000
 * -10^6 <= nums[i] <= 10^6
 * 
 * 题目链接: https://leetcode.com/problems/running-sum-of-1d-array/
 * 
 * 解题思路:
 * 使用前缀和的思想，从前向后累加即可。
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(1) - 不考虑输出数组，只使用常数额外空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 原地修改：节省空间，避免创建新数组
 * 3. 整数溢出：虽然题目保证在32位整数范围内，但实际工程中需要考虑
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能计算前缀和，时间复杂度O(n)无法优化。
 * 空间复杂度O(1)也是最优的（不考虑输出数组）。
 */

#include <vector>
#include <iostream>

using namespace std;

class Solution {
public:
    /**
     * 计算数组的动态和
     * 
     * @param nums 输入数组
     * @return 动态和数组
     */
    vector<int> runningSum(vector<int>& nums) {
        // 边界情况处理：空数组或单元素数组直接返回
        if (nums.empty()) {
            return nums;
        }
        
        // 直接在原数组上进行修改，节省空间
        // 从第二个元素开始，每个位置的值等于前一个位置的前缀和加上当前位置的原始值
        for (int i = 1; i < nums.size(); i++) {
            nums[i] += nums[i - 1];
        }
        
        return nums;
    }
};

/**
 * 测试函数
 */
void testRunningSum() {
    Solution solution;
    
    // 测试用例1：正常情况
    vector<int> nums1 = {1, 2, 3, 4};
    vector<int> result1 = solution.runningSum(nums1);
    cout << "测试用例1: ";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << " (预期: 1 3 6 10)" << endl;
    
    // 测试用例2：全1数组
    vector<int> nums2 = {1, 1, 1, 1, 1};
    vector<int> result2 = solution.runningSum(nums2);
    cout << "测试用例2: ";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << " (预期: 1 2 3 4 5)" << endl;
    
    // 测试用例3：混合数值
    vector<int> nums3 = {3, 1, 2, 10, 1};
    vector<int> result3 = solution.runningSum(nums3);
    cout << "测试用例3: ";
    for (int num : result3) {
        cout << num << " ";
    }
    cout << " (预期: 3 4 6 16 17)" << endl;
    
    // 测试用例4：空数组
    vector<int> nums4 = {};
    vector<int> result4 = solution.runningSum(nums4);
    cout << "测试用例4: ";
    for (int num : result4) {
        cout << num << " ";
    }
    cout << " (预期: 空数组)" << endl;
    
    // 测试用例5：单元素数组
    vector<int> nums5 = {5};
    vector<int> result5 = solution.runningSum(nums5);
    cout << "测试用例5: ";
    for (int num : result5) {
        cout << num << " ";
    }
    cout << " (预期: 5)" << endl;
}

/**
 * 主函数
 */
int main() {
    cout << "=== 一维数组的动态和测试 ===" << endl;
    testRunningSum();
    return 0;
}