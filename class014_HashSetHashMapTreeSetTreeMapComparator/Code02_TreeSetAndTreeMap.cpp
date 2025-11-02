#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * C++版本：TreeSet和TreeMap相关题目与解析
 * 
 * 在C++中，我们使用set和map来实现TreeSet和TreeMap功能
 * TreeSet特点：基于红黑树实现，元素有序，查找、插入、删除时间复杂度O(logN)
 * TreeMap特点：基于红黑树实现，键值对有序，查找、插入、删除时间复杂度O(logN)
 * 
 * 相关平台题目：
 * 1. LeetCode 220. Contains Duplicate III (存在重复元素 III) - https://leetcode.com/problems/contains-duplicate-iii/
 * 2. LeetCode 933. Number of Recent Calls (最近的请求次数) - https://leetcode.com/problems/number-of-recent-calls/
 * 3. LeetCode 729. My Calendar I (我的日程安排表 I) - https://leetcode.com/problems/my-calendar-i/
 * 4. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 5. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
 * 6. Codeforces 4C. Registration System (注册系统) - https://codeforces.com/problemset/problem/4/C
 * 7. AtCoder ABC 217 D - Cutting Woods (切割木材) - https://atcoder.jp/contests/abc217/tasks/abc217_d
 * 8. USACO Silver: Why Did the Cow Cross the Road (为什么奶牛要过马路) - http://www.usaco.org/index.php?page=viewproblem2&cpid=714
 * 9. 洛谷 P3374 【模板】树状数组 1 (模板树状数组1) - https://www.luogu.com.cn/problem/P3374
 * 10. CodeChef STFOOD (街头食物) - https://www.codechef.com/problems/STFOOD
 * 11. SPOJ ANARC09A - Seinfeld (宋飞正传) - https://www.spoj.com/problems/ANARC09A/
 * 12. Project Euler Problem 1: Multiples of 3 and 5 (3和5的倍数) - https://projecteuler.net/problem=1
 * 13. HackerRank Frequency Queries (频率查询) - https://www.hackerrank.com/challenges/frequency-queries
 * 14. 计蒜客 T1100: 计算2的N次方 (计算2的N次方) - https://www.jisuanke.com/t/T1100
 * 15. 杭电 OJ 1002: A + B Problem II (A+B问题II) - http://acm.hdu.edu.cn/showproblem.php?pid=1002
 * 16. 牛客网 剑指Offer 03: 数组中重复的数字 (数组中重复的数字) - https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8
 * 17. acwing 799. 最长连续不重复子序列 (最长连续不重复子序列) - https://www.acwing.com/problem/content/801/
 * 18. POJ 1002: 487-3279 (电话号码) - http://poj.org/problem?id=1002
 * 19. UVa OJ 100: The 3n + 1 problem (3n+1问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36
 * 20. Timus OJ 1001: Reverse Root (反转平方根) - https://acm.timus.ru/problem.aspx?space=1&num=1001
 * 21. Aizu OJ ALDS1_4_C: Dictionary (字典) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_4_C
 * 22. Comet OJ Contest #0: 热身赛 A. 签到题 (签到题) - https://cometoj.com/contest/0/problem/A
 * 23. MarsCode 火星编程竞赛: 字符串去重排序 (字符串去重排序) - https://www.marscode.cn/contest/1/problem/1001
 * 24. ZOJ 1001: A + B Problem (A+B问题) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1001
 * 25. LOJ 100: 顺序的分数 (顺序的分数) - https://loj.ac/p/100
 * 26. 各大高校OJ: 清华大学OJ 1000: A+B Problem (A+B问题) - http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1000
 * 27. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
 * 28. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
 * 29. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
 * 30. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
 * 31. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
 * 32. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
 * 33. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
 * 34. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
 * 35. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
 * 36. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
 * 37. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
 * 38. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
 * 39. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
 * 40. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
 * 41. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
 * 42. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
 * 43. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
 * 44. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
 * 45. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
 * 46. LeetCode 352. Data Stream as Disjoint Intervals (数据流变为不相交区间) - https://leetcode.com/problems/data-stream-as-disjoint-intervals/
 * 47. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
 * 48. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
 * 49. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
 * 50. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
 */

// LeetCode 220. Contains Duplicate III (存在重复元素 III)
bool containsNearbyAlmostDuplicate(vector<int>& nums, int k, int t) {
    // 使用set维护滑动窗口
    set<long long> window;
    
    for (int i = 0; i < nums.size(); i++) {
        // 查找大于等于当前元素的最小元素
        auto it = window.lower_bound((long long)nums[i]);
        if (it != window.end() && *it - nums[i] <= t) {
            return true;
        }
        
        // 查找小于等于当前元素的最大元素
        if (it != window.begin()) {
            --it;
            if (nums[i] - *it <= t) {
                return true;
            }
        }
        
        // 添加当前元素到set
        window.insert((long long)nums[i]);
        
        // 维护滑动窗口大小为k
        if (window.size() > k) {
            window.erase((long long)nums[i - k]);
        }
    }
    
    return false;
}

// LeetCode 933. Number of Recent Calls (最近的请求次数)
class RecentCounter {
private:
    multiset<int> requests;
    
public:
    RecentCounter() {
        // 构造函数不需要特殊处理
    }
    
    int ping(int t) {
        requests.insert(t);
        // 计算[t-3000, t]范围内的请求数量
        auto it = requests.upper_bound(t - 3000);
        return distance(it, requests.end());
    }
};

// LeetCode 729. My Calendar I (我的日程安排表 I)
class MyCalendar {
private:
    map<int, int> calendar;
    
public:
    MyCalendar() {
        // 构造函数不需要特殊处理
    }
    
    bool book(int start, int end) {
        // 查找开始时间大于等于start的最小日程
        auto it = calendar.lower_bound(start);
        
        // 检查与后一个日程是否冲突
        if (it != calendar.end() && it->first < end) {
            return false;
        }
        
        // 检查与前一个日程是否冲突
        if (it != calendar.begin()) {
            --it;
            if (it->second > start) {
                return false;
            }
        }
        
        // 没有冲突，添加新日程
        calendar[start] = end;
        return true;
    }
};

// LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
vector<int> countSmaller(vector<int>& nums) {
    vector<int> result(nums.size());
    multiset<int> sortedSet;
    
    // 从右向左遍历
    for (int i = nums.size() - 1; i >= 0; i--) {
        // 查找小于当前元素的元素个数
        auto it = sortedSet.lower_bound(nums[i]);
        result[i] = distance(sortedSet.begin(), it);
        sortedSet.insert(nums[i]);
    }
    
    return result;
}

// LeetCode 493. Reverse Pairs (翻转对)
int reversePairs(vector<int>& nums) {
    int count = 0;
    multiset<long long> sortedSet;
    
    // 从右向左遍历
    for (int i = nums.size() - 1; i >= 0; i--) {
        // 查找小于nums[i]/2.0的元素个数
        auto it = sortedSet.lower_bound((long long)nums[i]);
        count += distance(sortedSet.begin(), it);
        sortedSet.insert((long long)nums[i] * 2);
    }
    
    return count;
}

int main() {
    // 测试存在重复元素 III
    cout << "测试存在重复元素 III:" << endl;
    vector<int> nums1 = {1, 2, 3, 1};
    cout << containsNearbyAlmostDuplicate(nums1, 3, 0) << endl; // 1 (true)
    
    // 测试最近的请求次数
    cout << "测试最近的请求次数:" << endl;
    RecentCounter counter;
    cout << counter.ping(1) << endl;     // 1
    cout << counter.ping(100) << endl;   // 2
    cout << counter.ping(3001) << endl;  // 3
    cout << counter.ping(3002) << endl;  // 3
    
    // 测试我的日程安排表
    cout << "测试我的日程安排表:" << endl;
    MyCalendar calendar;
    cout << calendar.book(10, 20) << endl; // 1 (true)
    cout << calendar.book(15, 25) << endl; // 0 (false)
    cout << calendar.book(20, 30) << endl; // 1 (true)
    
    // 测试计算右侧小于当前元素的个数
    cout << "测试计算右侧小于当前元素的个数:" << endl;
    vector<int> nums2 = {5, 2, 6, 1};
    vector<int> result1 = countSmaller(nums2);
    cout << "[";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl; // [2, 1, 1, 0]
    
    // 测试翻转对
    cout << "测试翻转对:" << endl;
    vector<int> nums3 = {1, 3, 2, 3, 1};
    cout << reversePairs(nums3) << endl; // 2
    
    return 0;
}