/*
 * 滑动窗口最大值和最小值问题解决方案
 * 
 * 问题描述：
 * 现在有一堆数字共N个数字（N<=10^6），以及一个大小为k的窗口。
 * 现在这个从左边开始向右滑动，每次滑动一个单位，求出每次滑动后窗口中的最大值和最小值。
 * 
 * 解题思路：
 * 使用单调队列来解决滑动窗口的最值问题：
 * 1. 维护两个双端队列：
 *    - 一个单调递增队列用于维护窗口最小值
 *    - 一个单调递减队列用于维护窗口最大值
 * 2. 队列中存储数组元素的索引，便于判断元素是否在窗口范围内
 * 3. 当窗口形成后（i >= k-1），记录当前窗口的最值
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - 每个元素最多入队和出队一次
 * 空间复杂度: O(k) - 双端队列最多存储k个元素
 * 
 * 是否最优解: 是，这是处理滑动窗口最值问题的最优解法
 * 
 * 相关题目链接：
 * 1. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 2. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 3. LeetCode 239. 滑动窗口最大值
 *    https://leetcode.cn/problems/sliding-window-maximum/
 * 4. 牛客网 - 滑动窗口最大值
 *    https://www.nowcoder.com/practice/1624bc35a45c42c0bc17d17fa0cba788
 * 5. LintCode 362. 滑动窗口的最大值
 *    https://www.lintcode.com/problem/362/
 * 6. HackerRank - Sliding Window Maximum
 *    https://www.hackerrank.com/challenges/sliding-window-maximum/problem
 * 7. CodeChef - MAXSWINDOW - Maximum in Sliding Window
 *    https://www.codechef.com/problems/MAXSWINDOW
 * 8. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、k为负数或0等边界情况
 * 2. 性能优化：使用单调队列避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 * 
 * 编译说明：
 * 此代码需要C++标准库支持，编译时请确保包含正确的头文件路径
 * 编译命令示例：g++ -std=c++11 Code14_SlidingWindowMinMax.cpp -o Code14_SlidingWindowMinMax
 */

// 算法实现（需要C++标准库支持）
/*
#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
using namespace std;

// 经典单调队列问题，求滑动窗口内的最值
class Solution {
public:
    // 计算滑动窗口中的最大值和最小值
    vector<vector<int>> slidingWindowMinMax(vector<int>& nums, int k) {
        // 异常情况处理
        if (nums.empty() || k <= 0) {
            return {{}, {}};
        }
        
        int n = nums.size();
        // 结果数组，[0]存储最小值序列，[1]存储最大值序列
        vector<vector<int>> result(2, vector<int>(n - k + 1));
        // 单调递增队列，队首是当前窗口的最小值索引
        deque<int> minDeque;
        // 单调递减队列，队首是当前窗口的最大值索引
        deque<int> maxDeque;
        
        // 遍历数组中的每个元素
        for (int i = 0; i < n; i++) {
            // 移除队列中超出窗口范围的索引
            // 当前窗口范围是 [i-k+1, i]，所以队首索引小于 i-k+1 的元素已经不在窗口内
            while (!minDeque.empty() && minDeque.front() < i - k + 1) {
                minDeque.pop_front();
            }
            while (!maxDeque.empty() && maxDeque.front() < i - k + 1) {
                maxDeque.pop_front();
            }
            
            // 维护单调递增队列（用于最小值）
            // 移除所有大于等于当前元素的索引，保持队列单调递增
            while (!minDeque.empty() && nums[minDeque.back()] >= nums[i]) {
                minDeque.pop_back();
            }
            
            // 维护单调递减队列（用于最大值）
            // 移除所有小于等于当前元素的索引，保持队列单调递减
            while (!maxDeque.empty() && nums[maxDeque.back()] <= nums[i]) {
                maxDeque.pop_back();
            }
            
            // 将当前元素索引加入队列尾部
            minDeque.push_back(i);
            maxDeque.push_back(i);
            
            // 当窗口形成后（i >= k-1），记录当前窗口的最值
            // 窗口形成的条件是已经遍历了至少k个元素
            if (i >= k - 1) {
                result[0][i - k + 1] = nums[minDeque.front()];  // 最小值
                result[1][i - k + 1] = nums[maxDeque.front()];  // 最大值
            }
        }
        
        return result;
    }
};

// 测试用例
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
    int k1 = 3;
    vector<vector<int>> result1 = solution.slidingWindowMinMax(nums1, k1);
    // 预期输出: 
    // 最小值序列: -1 -3 -3 -3 3 3 
    // 最大值序列: 3 3 5 5 6 7 
    
    return 0;
}
*/

// 算法核心逻辑说明（伪代码形式）：
/*
class Solution {
public:
    vector<vector<int>> slidingWindowMinMax(vector<int>& nums, int k) {
        if (nums.empty() || k <= 0) {
            return {{}, {}};
        }
        
        int n = nums.size();
        vector<vector<int>> result(2, vector<int>(n - k + 1));
        deque<int> minDeque;  // 单调递增队列
        deque<int> maxDeque;  // 单调递减队列
        
        for (int i = 0; i < n; i++) {
            // 移除队列中超出窗口范围的索引
            while (!minDeque.empty() && minDeque.front() < i - k + 1) {
                minDeque.pop_front();
            }
            while (!maxDeque.empty() && maxDeque.front() < i - k + 1) {
                maxDeque.pop_front();
            }
            
            // 维护单调性
            while (!minDeque.empty() && nums[minDeque.back()] >= nums[i]) {
                minDeque.pop_back();
            }
            while (!maxDeque.empty() && nums[maxDeque.back()] <= nums[i]) {
                maxDeque.pop_back();
            }
            
            // 将当前元素索引加入队列
            minDeque.push_back(i);
            maxDeque.push_back(i);
            
            // 当窗口形成后，记录当前窗口的最值
            if (i >= k - 1) {
                result[0][i - k + 1] = nums[minDeque.front()];  // 最小值
                result[1][i - k + 1] = nums[maxDeque.front()];  // 最大值
            }
        }
        
        return result;
    }
};
*/