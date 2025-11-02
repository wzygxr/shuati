/*
 * 滑动窗口最大值问题解决方案
 * 
 * 问题描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回 滑动窗口中的最大值 。
 * 
 * 解题思路：
 * 使用双端队列实现单调队列，维护窗口中的最大值：
 * 1. 双端队列中存储数组元素的下标
 * 2. 队列保持单调递减特性，队首始终是当前窗口的最大值下标
 * 3. 遍历数组时，维护队列的单调性并及时移除窗口外的元素下标
 * 4. 当窗口形成后，队列头部元素就是当前窗口的最大值
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - 每个元素最多入队和出队一次
 * 空间复杂度: O(k) - 双端队列最多存储k个元素
 * 
 * 是否最优解: 是，这是处理滑动窗口最大值的最优解法
 *
 * 相关题目链接：
 * LeetCode 239. 滑动窗口最大值
 * https://leetcode.cn/problems/sliding-window-maximum/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 滑动窗口最大值
 *    https://www.nowcoder.com/practice/1624bc35a45c42c0bc17d17fa0cba788
 * 2. LintCode 362. 滑动窗口的最大值
 *    https://www.lintcode.com/problem/362/
 * 3. HackerRank - Sliding Window Maximum
 *    https://www.hackerrank.com/challenges/sliding-window-maximum/problem
 * 4. CodeChef - MAXSWINDOW - Maximum in Sliding Window
 *    https://www.codechef.com/problems/MAXSWINDOW
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
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
 * 编译命令示例：g++ -std=c++11 Code08_SlidingWindowMaximum.cpp -o Code08_SlidingWindowMaximum
 */

// 算法实现（需要C++标准库支持）
/*
#include <vector>
#include <deque>
using namespace std;

vector<int> maxSlidingWindow(vector<int>& nums, int k) {
    // 异常情况处理
    if (nums.empty() || k <= 0) {
        return {};
    }
    
    int n = nums.size();
    // 结果数组，大小为 n-k+1
    vector<int> result(n - k + 1);
    // 双端队列，存储数组下标，队列头部是当前窗口的最大值下标
    deque<int> dq;
    
    // 遍历数组中的每个元素
    for (int i = 0; i < n; i++) {
        // 移除队列中超出窗口范围的下标
        // 当前窗口范围是 [i-k+1, i]，所以队首下标小于 i-k+1 的元素已经不在窗口内
        while (!dq.empty() && dq.front() < i - k + 1) {
            dq.pop_front();
        }
        
        // 维护队列单调性，移除所有小于当前元素的下标
        // 保持队列单调递减，队首始终是最大值
        while (!dq.empty() && nums[dq.back()] < nums[i]) {
            dq.pop_back();
        }
        
        // 将当前元素下标加入队列尾部
        dq.push_back(i);
        
        // 当窗口形成后（i >= k-1），记录当前窗口的最大值
        // 窗口形成的条件是已经遍历了至少k个元素
        if (i >= k - 1) {
            result[i - k + 1] = nums[dq.front()];
        }
    }
    
    return result;
}
*/

// 算法核心逻辑说明（伪代码形式）：
/*
function maxSlidingWindow(nums, k):
    if nums is empty or k <= 0:
        return empty array
    
    n = length of nums
    result = new array of size (n - k + 1)
    dq = new deque  // 存储数组下标
    
    for i from 0 to n-1:
        // 移除队列中超出窗口范围的下标
        while dq is not empty and dq.front < i - k + 1:
            dq.pop_front()
        
        // 维护队列单调性
        while dq is not empty and nums[dq.back()] < nums[i]:
            dq.pop_back()
        
        // 将当前元素下标加入队列
        dq.push_back(i)
        
        // 当窗口形成后，记录当前窗口的最大值
        if i >= k - 1:
            result[i - k + 1] = nums[dq.front()]
    
    return result
*/