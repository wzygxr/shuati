#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * 找出到每个位置为止最长的非递减子序列
 * 
 * 题目来源：LeetCode 1964. 找出到每个位置为止最长的非递减子序列
 * 题目链接：https://leetcode.cn/problems/find-the-longest-valid-obstacle-course-at-each-position/
 * 题目描述：你打算构建一些障碍赛跑路线。给你一个下标从 0 开始的整数数组 obstacles，数组长度为 n，
 * 其中 obstacles[i] 表示第 i 个障碍的高度。对于每个介于 0 和 n - 1 之间（包含 0 和 n - 1）的下标 i，
 * 在满足下述条件的前提下，请你找出 obstacles 能构成的最长障碍路线的长度。
 * 
 * 条件：对于路线中的每个下标 j（0 <= j <= i），必须满足 obstacles[j] <= obstacles[i]。
 * 路线必须包含下标 i。
 * 
 * 算法思路：
 * 1. 这实际上是求以每个位置结尾的最长非递减子序列长度
 * 2. 使用贪心+二分查找优化，维护ends数组
 * 3. ends[i]表示长度为i+1的非递减子序列的最小结尾元素
 * 4. 对于每个障碍高度，在ends中查找>obstacles[i]的最左位置
 * 
 * 时间复杂度：O(n*logn) - 每个位置二分查找O(logn)
 * 空间复杂度：O(n) - 需要ends数组存储状态
 * 是否最优解：是，这是最优解法
 * 
 * 示例：
 * 输入: obstacles = [1,2,3,2]
 * 输出: [1,2,3,3]
 * 解释：
 * - i=0: [1] -> 长度1
 * - i=1: [1,2] -> 长度2  
 * - i=2: [1,2,3] -> 长度3
 * - i=3: [1,2,3] 或 [1,2,2] -> 长度3
 * 
 * 输入: obstacles = [2,2,1]
 * 输出: [1,2,1]
 * 解释：
 * - i=0: [2] -> 长度1
 * - i=1: [2,2] -> 长度2
 * - i=2: [1] -> 长度1
 */

class Solution {
public:
    /**
     * 计算每个位置的最长非递减子序列长度
     * 
     * @param obstacles 障碍高度数组
     * @return 每个位置的最长非递减子序列长度
     */
    vector<int> longestObstacleCourseAtEachPosition(vector<int>& obstacles) {
        int n = obstacles.size();
        vector<int> result(n);
        vector<int> ends(n, INT_MAX); // ends[i]表示长度为i+1的非递减子序列的最小结尾元素
        int len = 0;
        
        for (int i = 0; i < n; i++) {
            // 在ends数组中查找>obstacles[i]的最左位置
            int find = binarySearch(ends, len, obstacles[i]);
            
            if (find == -1) {
                // 没有找到，说明obstacles[i]可以延长当前最长非递减子序列
                ends[len] = obstacles[i];
                result[i] = len + 1;
                len++;
            } else {
                // 找到了位置，更新该位置的值为obstacles[i]
                ends[find] = obstacles[i];
                result[i] = find + 1;
            }
        }
        
        return result;
    }

    /**
     * 在不降序数组ends中查找>num的最左位置
     * 
     * 算法思路：
     * 1. 使用二分查找在有序数组中查找目标值
     * 2. 维护左边界l和右边界r
     * 3. 计算中间位置m，比较ends[m]与num的大小关系
     * 4. 如果ends[m] > num，说明目标位置在左半部分（包括m），更新ans和r
     * 5. 否则目标位置在右半部分，更新l
     * 
     * 时间复杂度：O(logn) - 标准二分查找
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是，这是标准的二分查找实现
     * 
     * @param ends 不降序数组
     * @param len 有效长度
     * @param num 目标值
     * @return >num的最左位置，如果不存在返回-1
     */
    int binarySearch(vector<int>& ends, int len, int num) {
        int left = 0, right = len - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (ends[mid] > num) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }

    /**
     * 使用动态规划解法（时间复杂度较高，用于对比）
     * 
     * 算法思路：
     * 1. 使用动态规划计算每个位置的最长非递减子序列长度
     * 2. dp[i]表示以obstacles[i]结尾的最长非递减子序列长度
     * 3. 对于每个位置i，遍历前面所有位置j，如果obstacles[j] <= obstacles[i]，则更新dp[i]
     * 
     * 时间复杂度：O(n²) - 外层循环n次，内层循环最多n次
     * 空间复杂度：O(n) - 需要dp数组存储状态
     * 是否最优解：否，存在O(n*logn)的优化解法
     * 
     * @param obstacles 障碍高度数组
     * @return 每个位置的最长非递减子序列长度
     */
    vector<int> longestObstacleCourseAtEachPositionDP(vector<int>& obstacles) {
        int n = obstacles.size();
        vector<int> dp(n, 1); // 每个位置至少可以单独构成长度为1的子序列
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (obstacles[j] <= obstacles[i]) {
                    dp[i] = max(dp[i], dp[j] + 1);
                }
            }
        }
        
        return dp;
    }
};

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1
    vector<int> obstacles1 = {1, 2, 3, 2};
    cout << "输入: [1,2,3,2]" << endl;
    vector<int> result1 = solution.longestObstacleCourseAtEachPosition(obstacles1);
    cout << "优化方法输出: ";
    for (int num : result1) cout << num << " ";
    cout << endl;
    cout << "期望: [1,2,3,3]" << endl;
    
    vector<int> result1_dp = solution.longestObstacleCourseAtEachPositionDP(obstacles1);
    cout << "DP方法输出: ";
    for (int num : result1_dp) cout << num << " ";
    cout << endl << endl;
    
    // 测试用例2
    vector<int> obstacles2 = {2, 2, 1};
    cout << "输入: [2,2,1]" << endl;
    vector<int> result2 = solution.longestObstacleCourseAtEachPosition(obstacles2);
    cout << "优化方法输出: ";
    for (int num : result2) cout << num << " ";
    cout << endl;
    cout << "期望: [1,2,1]" << endl;
    
    vector<int> result2_dp = solution.longestObstacleCourseAtEachPositionDP(obstacles2);
    cout << "DP方法输出: ";
    for (int num : result2_dp) cout << num << " ";
    cout << endl << endl;
    
    // 测试用例3
    vector<int> obstacles3 = {3, 1, 5, 6, 4, 2};
    cout << "输入: [3,1,5,6,4,2]" << endl;
    vector<int> result3 = solution.longestObstacleCourseAtEachPosition(obstacles3);
    cout << "优化方法输出: ";
    for (int num : result3) cout << num << " ";
    cout << endl;
    
    vector<int> result3_dp = solution.longestObstacleCourseAtEachPositionDP(obstacles3);
    cout << "DP方法输出: ";
    for (int num : result3_dp) cout << num << " ";
    cout << endl << endl;
}

int main() {
    test();
    return 0;
}