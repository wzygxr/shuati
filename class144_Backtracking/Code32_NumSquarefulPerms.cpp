/*
LeetCode 996. 正方形数组的数目

给定一个非负整数数组 A，如果该数组任意两个相邻元素的和都可以表示为某个完全平方数，
那么这个数组就称为正方形数组。返回 A 的所有可能的排列中，正方形数组的数目。

算法思路：
使用回溯算法生成所有可能的排列，并在生成过程中检查相邻元素之和是否为完全平方数。
需要注意去重，因为数组中可能有重复元素。

时间复杂度：O(n! * n)
空间复杂度：O(n)
*/

#include <vector>
#include <algorithm>
#include <cmath>
#include <iostream>
using namespace std;

class Solution {
private:
    int count = 0;
    
    /**
     * 回溯函数
     * @param nums 输入数组
     * @param used 标记数组元素是否已使用
     * @param prevIndex 前一个元素的索引
     * @param index 当前处理的位置
     */
    void backtrack(vector<int>& nums, vector<bool>& used, int prevIndex, int index) {
        // 终止条件：处理完所有元素
        if (index == nums.size()) {
            count++;
            return;
        }
        
        for (int i = 0; i < nums.size(); i++) {
            // 去重：如果当前元素与前一个元素相同，且前一个元素未使用，则跳过
            if (used[i] || (i > 0 && nums[i] == nums[i-1] && !used[i-1])) {
                continue;
            }
            
            // 检查相邻元素之和是否为完全平方数
            if (prevIndex != -1 && !isPerfectSquare(nums[prevIndex] + nums[i])) {
                continue;
            }
            
            used[i] = true;
            backtrack(nums, used, i, index + 1);
            used[i] = false;
        }
    }
    
    /**
     * 判断一个数是否为完全平方数
     * @param num 待判断的数
     * @return 是否为完全平方数
     */
    bool isPerfectSquare(int num) {
        int sqrtNum = (int)sqrt(num);
        return sqrtNum * sqrtNum == num;
    }
    
public:
    /**
     * 返回正方形数组的数目
     * @param nums 输入数组
     * @return 正方形数组的数目
     */
    int numSquarefulPerms(vector<int>& nums) {
        count = 0;
        sort(nums.begin(), nums.end()); // 排序便于去重
        vector<bool> used(nums.size(), false);
        backtrack(nums, used, -1, 0);
        return count;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 17, 8};
    cout << "Input: [1, 17, 8]" << endl;
    cout << "Output: " << solution.numSquarefulPerms(nums1) << endl;
    
    // 测试用例2
    vector<int> nums2 = {2, 2, 2};
    cout << "\nInput: [2, 2, 2]" << endl;
    cout << "Output: " << solution.numSquarefulPerms(nums2) << endl;
    
    return 0;
}