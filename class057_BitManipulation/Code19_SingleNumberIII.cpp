#include <iostream>
#include <vector>
using namespace std;

// 只出现一次的数字 III
// 测试链接 : https://leetcode.cn/problems/single-number-iii/
/*
题目描述：
给你一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。你可以按任意顺序返回答案。

示例：
输入：nums = [1,2,1,3,2,5]
输出：[3,5] 或 [5,3]

输入：nums = [-1,0]
输出：[-1,0]

输入：nums = [0,1]
输出：[1,0]

提示：
2 <= nums.length <= 3 * 10^4
-2^31 <= nums[i] <= 2^31 - 1
除两个只出现一次的整数外，nums 中的其他数字都出现两次

解题思路：
这道题是对只出现一次的数字（Single Number）的延伸，现在有两个数字只出现一次，其余数字都出现两次。

关键点是如何将这两个只出现一次的数字分开处理。

1. 首先，对所有数字进行异或运算，得到的结果是两个只出现一次的数字的异或结果（因为相同数字异或为0，0与任何数异或为该数）。
2. 在这个异或结果中，找到任意一个为1的位。这个位为1表示两个只出现一次的数字在这一位上的值不同。
3. 根据这个位是否为1，将原数组分成两组。这样，两个只出现一次的数字会被分到不同的组中。
4. 对每个组内的数字进行异或运算，最终得到两个只出现一次的数字。

时间复杂度：O(n) - 我们需要遍历数组两次
空间复杂度：O(1) - 只使用了常数级别的额外空间
*/

class Solution {
public:
    /**
     * 找出数组中只出现一次的两个元素
     * @param nums 输入的整数数组
     * @return 只出现一次的两个元素组成的数组
     */
    vector<int> singleNumber(vector<int>& nums) {
        // 步骤1: 对所有数字进行异或运算，得到两个只出现一次的数字的异或结果
        long long xorResult = 0; // 使用long long避免溢出
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // 步骤2: 找到xorResult中任意一个为1的位
        // 这里我们找到最右边的1
        // 例如，对于xorResult = 01010，rightmostSetBit = 00010
        long long rightmostSetBit = xorResult & (-xorResult);
        
        // 步骤3: 根据这个位将数组分成两组，并分别对两组进行异或运算
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & rightmostSetBit) == 0) {
                // 该位为0的组
                a ^= num;
            } else {
                // 该位为1的组
                b ^= num;
            }
        }
        
        // 返回两个只出现一次的数字
        return {a, b};
    }
    
    /**
     * 找出数组中只出现一次的两个元素（另一种实现方式）
     * @param nums 输入的整数数组
     * @return 只出现一次的两个元素组成的数组
     */
    vector<int> singleNumber2(vector<int>& nums) {
        long long xorResult = 0;
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // 找到xorResult中任意一个为1的位
        // 这里使用不同的方法来找最右边的1
        long long rightmostSetBit = 1;
        while ((xorResult & rightmostSetBit) == 0) {
            rightmostSetBit <<= 1;
        }
        
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & rightmostSetBit) == 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        
        return {a, b};
    }
};

// 辅助函数：打印数组
void printArray(const vector<int>& arr) {
    cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        cout << arr[i];
        if (i < arr.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 1, 3, 2, 5};
    vector<int> result1 = solution.singleNumber(nums1);
    cout << "Test 1: ";
    printArray(result1); // 输出: [3, 5] 或 [5, 3]
    
    // 测试用例2
    vector<int> nums2 = {-1, 0};
    vector<int> result2 = solution.singleNumber(nums2);
    cout << "Test 2: ";
    printArray(result2); // 输出: [-1, 0] 或 [0, -1]
    
    // 测试用例3
    vector<int> nums3 = {0, 1};
    vector<int> result3 = solution.singleNumber(nums3);
    cout << "Test 3: ";
    printArray(result3); // 输出: [0, 1] 或 [1, 0]
    
    // 使用第二种方法测试
    cout << "\nUsing alternative method:" << endl;
    vector<int> result1_alt = solution.singleNumber2(nums1);
    cout << "Test 1 (alt): ";
    printArray(result1_alt);
    
    return 0;
}