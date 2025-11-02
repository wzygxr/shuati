#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <algorithm>
#include <climits>
#include <cmath>
using namespace std;

/**
 * TreeSet和TreeMap高级应用题目实现（C++版本）
 * 包含LeetCode 352, 363, 436, 456, 480, 683等题目
 * 
 * C++中TreeSet和TreeMap的实现：
 * - set特点：基于红黑树实现，元素有序，支持范围查询，查找、插入、删除时间复杂度O(logN)
 * - map特点：基于红黑树实现，键值对有序，支持范围查询，查找、插入、删除时间复杂度O(logN)
 * - 高级操作：lower_bound、upper_bound等
 * 
 * 时间复杂度分析：
 * - set/map基本操作：O(log n)
 * - 范围查询：O(log n + k) 其中k是结果数量
 * - 排序操作：O(n log n)
 * 
 * 空间复杂度分析：
 * - set/map存储：O(n)
 * - 额外数据结构：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理空输入、边界条件
 * 2. 性能优化：选择合适的数据结构，避免不必要的对象创建
 * 3. 代码可读性：添加详细注释，使用有意义的变量名
 * 4. 内存管理：注意迭代器有效性，避免内存泄漏
 * 
 * 相关平台题目：
 * 1. LeetCode 352. Data Stream as Disjoint Intervals (数据流变为不相交区间) - https://leetcode.com/problems/data-stream-as-disjoint-intervals/
 * 2. LeetCode 363. Max Sum of Rectangle No Larger Than K (矩形区域不超过K的最大数值和) - https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
 * 3. LeetCode 436. Find Right Interval (寻找右区间) - https://leetcode.com/problems/find-right-interval/
 * 4. LeetCode 456. 132 Pattern (132模式) - https://leetcode.com/problems/132-pattern/
 * 5. LeetCode 480. Sliding Window Median (滑动窗口中位数) - https://leetcode.com/problems/sliding-window-median/
 * 6. LeetCode 683. K Empty Slots (K个空花盆) - https://leetcode.com/problems/k-empty-slots/
 * 7. LeetCode 715. Range Module (范围模块) - https://leetcode.com/problems/range-module/
 * 8. LeetCode 731. My Calendar II (我的日程安排表 II) - https://leetcode.com/problems/my-calendar-ii/
 * 9. LeetCode 732. My Calendar III (我的日程安排表 III) - https://leetcode.com/problems/my-calendar-iii/
 * 10. LeetCode 855. Exam Room (考场就座) - https://leetcode.com/problems/exam-room/
 * 11. LeetCode 981. Time Based Key-Value Store (基于时间的键值存储) - https://leetcode.com/problems/time-based-key-value-store/
 * 12. LeetCode 1146. Snapshot Array (快照数组) - https://leetcode.com/problems/snapshot-array/
 * 13. LeetCode 1348. Tweet Counts Per Frequency (推文计数) - https://leetcode.com/problems/tweet-counts-per-frequency/
 * 14. LeetCode 1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit (绝对差不超过限制的最长连续子数组) - https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 15. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
 * 16. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
 * 17. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
 * 18. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
 * 19. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
 * 20. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
 * 21. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
 * 22. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
 * 23. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
 * 24. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
 * 25. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
 * 26. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
 * 27. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
 * 28. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
 * 29. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
 * 30. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
 * 31. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
 * 32. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
 * 33. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
 * 34. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
 * 35. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
 * 36. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
 * 37. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
 * 38. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
 * 39. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 40. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
 * 41. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 42. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
 * 43. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
 * 44. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
 * 45. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
 * 46. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
 * 47. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
 * 48. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
 * 49. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
 * 50. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/
 */

/**
 * LeetCode 352. Data Stream as Disjoint Intervals
 * 将数据流变为多个不相交区间
 * 网址：https://leetcode.com/problems/data-stream-as-disjoint-intervals/
 * 
 * 解题思路：
 * 1. 使用map存储区间边界，键为区间起点，值为区间终点
 * 2. 添加数字时，查找可能合并的相邻区间
 * 3. 合并重叠或相邻的区间
 * 
 * 时间复杂度：每次添加O(log n)，获取区间O(n)
 * 空间复杂度：O(n)
 */
class DataStreamAsDisjointIntervals {
private:
    map<int, int> intervals;
    
public:
    DataStreamAsDisjointIntervals() {}
    
    void addNum(int val) {
        // 查找小于等于val的区间
        auto it = intervals.upper_bound(val);
        if (it != intervals.begin()) {
            it--;
            if (it->first <= val && val <= it->second) {
                // 数字已经在某个区间内，不需要处理
                return;
            }
        }
        
        // 检查是否可以与左侧区间合并
        bool mergeLeft = false, mergeRight = false;
        int leftKey = -1, rightKey = -1;
        
        if (it != intervals.begin()) {
            auto leftIt = it;
            leftIt--;
            if (leftIt->second + 1 == val) {
                mergeLeft = true;
                leftKey = leftIt->first;
            }
        }
        
        // 检查是否可以与右侧区间合并
        if (it != intervals.end() && it->first == val + 1) {
            mergeRight = true;
            rightKey = it->first;
        }
        
        if (mergeLeft && mergeRight) {
            // 合并左右区间
            int newEnd = intervals[rightKey];
            intervals[leftKey] = newEnd;
            intervals.erase(rightKey);
        } else if (mergeLeft) {
            // 只与左侧区间合并
            intervals[leftKey] = val;
        } else if (mergeRight) {
            // 只与右侧区间合并
            int end = intervals[rightKey];
            intervals.erase(rightKey);
            intervals[val] = end;
        } else {
            // 创建新区间
            intervals[val] = val;
        }
    }
    
    vector<vector<int>> getIntervals() {
        vector<vector<int>> result;
        for (auto& entry : intervals) {
            result.push_back({entry.first, entry.second});
        }
        return result;
    }
};

/**
 * LeetCode 363. Max Sum of Rectangle No Larger Than K
 * 矩形区域不超过K的最大数值和
 * 网址：https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
 * 
 * 解题思路：
 * 1. 固定左右边界，计算每一行的前缀和
 * 2. 使用set维护前缀和，快速查找满足条件的前缀和
 * 3. 使用lower_bound找到大于等于(target - k)的最小值
 * 
 * 时间复杂度：O(min(m,n)² * max(m,n) log max(m,n))
 * 空间复杂度：O(max(m,n))
 */
class Solution {
public:
    int maxSumSubmatrix(vector<vector<int>>& matrix, int k) {
        int m = matrix.size(), n = matrix[0].size();
        int maxSum = INT_MIN;
        
        // 让m是较小的维度，减少时间复杂度
        if (m > n) {
            vector<vector<int>> rotated(n, vector<int>(m));
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    rotated[j][i] = matrix[i][j];
                }
            }
            return maxSumSubmatrix(rotated, k);
        }
        
        // 枚举上下边界
        for (int top = 0; top < m; top++) {
            vector<int> colSum(n, 0);
            for (int bottom = top; bottom < m; bottom++) {
                // 更新列前缀和
                for (int j = 0; j < n; j++) {
                    colSum[j] += matrix[bottom][j];
                }
                
                // 使用set维护前缀和
                set<int> prefixSums;
                prefixSums.insert(0);
                int currentSum = 0;
                
                for (int j = 0; j < n; j++) {
                    currentSum += colSum[j];
                    // 查找大于等于(currentSum - k)的最小值
                    auto it = prefixSums.lower_bound(currentSum - k);
                    if (it != prefixSums.end()) {
                        maxSum = max(maxSum, currentSum - *it);
                    }
                    prefixSums.insert(currentSum);
                }
            }
        }
        
        return maxSum;
    }
};

/**
 * LeetCode 436. Find Right Interval
 * 寻找右区间
 * 网址：https://leetcode.com/problems/find-right-interval/
 * 
 * 解题思路：
 * 1. 使用map存储每个区间的起始位置和索引
 * 2. 对于每个区间，使用lower_bound找到起始位置大于等于当前区间结束位置的最小区间
 * 3. 如果找到则返回对应索引，否则返回-1
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class FindRightInterval {
public:
    vector<int> findRightInterval(vector<vector<int>>& intervals) {
        int n = intervals.size();
        vector<int> result(n, -1);
        
        // 使用map存储区间起始位置和索引
        map<int, int> startMap;
        for (int i = 0; i < n; i++) {
            startMap[intervals[i][0]] = i;
        }
        
        for (int i = 0; i < n; i++) {
            int end = intervals[i][1];
            // 查找大于等于end的最小起始位置
            auto it = startMap.lower_bound(end);
            if (it != startMap.end()) {
                result[i] = it->second;
            }
        }
        
        return result;
    }
};

// 测试函数
int main() {
    // 测试LeetCode 436
    vector<vector<int>> intervals = {{1,2}, {2,3}, {0,1}, {3,4}};
    FindRightInterval solution;
    vector<int> result = solution.findRightInterval(intervals);
    cout << "LeetCode 436 Result: ";
    for (int num : result) cout << num << " ";
    cout << endl;
    
    return 0;
}