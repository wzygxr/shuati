#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <map>

using namespace std;

/**
 * C++版本：Comparator比较器相关题目与解析
 * 
 * 在C++中，我们使用sort函数和自定义比较函数来实现自定义排序规则
 * C++ Comparator特点：使用lambda表达式或函数对象实现自定义排序
 * 时间复杂度：取决于具体实现，通常为O(logN)到O(NlogN)
 * 空间复杂度：取决于具体实现
 * 
 * 相关平台题目：
 * 1. LeetCode 973. K Closest Points to Origin (最接近原点的 K 个点) - https://leetcode.com/problems/k-closest-points-to-origin/
 * 2. LeetCode 179. Largest Number (最大数) - https://leetcode.com/problems/largest-number/
 * 3. LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序) - https://leetcode.com/problems/sort-integers-by-the-number-of-1-bits/
 * 4. LeetCode 56. Merge Intervals (合并区间) - https://leetcode.com/problems/merge-intervals/
 * 5. LeetCode 1122. Relative Sort Array (数组的相对排序) - https://leetcode.com/problems/relative-sort-array/
 * 6. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
 * 7. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
 * 8. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
 * 9. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
 * 10. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
 * 11. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
 * 12. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
 * 13. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
 * 14. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
 * 15. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
 * 16. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
 * 17. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
 * 18. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
 * 19. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
 * 20. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
 * 21. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
 * 22. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
 * 23. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
 * 24. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
 * 25. LeetCode 352. Data Stream as Disjoint Intervals (数据流变为不相交区间) - https://leetcode.com/problems/data-stream-as-disjoint-intervals/
 * 26. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
 * 27. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
 * 28. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
 * 29. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
 * 30. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
 * 31. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 32. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
 * 33. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 34. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
 * 35. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
 * 36. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
 * 37. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
 * 38. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
 * 39. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
 * 40. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
 * 41. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
 * 42. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/
 * 43. LeetCode 937. Reorder Data in Log Files (重新排列日志文件) - https://leetcode.com/problems/reorder-data-in-log-files/
 * 44. LeetCode 1030. Matrix Cells in Distance Order (距离顺序排列矩阵单元格) - https://leetcode.com/problems/matrix-cells-in-distance-order/
 * 45. LeetCode 1636. Sort Array by Increasing Frequency (按照频率将数组升序排序) - https://leetcode.com/problems/sort-array-by-increasing-frequency/
 * 46. LeetCode 1710. Maximum Units on a Truck (卡车上的最大单元数) - https://leetcode.com/problems/maximum-units-on-a-truck/
 * 47. HackerRank Java Comparator (Java比较器) - https://www.hackerrank.com/challenges/java-comparator/problem
 * 48. LeetCode 147. Insertion Sort List (对链表进行插入排序) - https://leetcode.com/problems/insertion-sort-list/
 * 49. LeetCode 252. Meeting Rooms (会议室) - https://leetcode.com/problems/meeting-rooms/
 * 50. LeetCode 253. Meeting Rooms II (会议室 II) - https://leetcode.com/problems/meeting-rooms-ii/
 */

// LeetCode 973. K Closest Points to Origin (最接近原点的 K 个点)
vector<vector<int>> kClosest(vector<vector<int>>& points, int k) {
    // 使用自定义比较函数按距离排序
    sort(points.begin(), points.end(), [](const vector<int>& a, const vector<int>& b) {
        // 计算到原点距离的平方
        int distA = a[0] * a[0] + a[1] * a[1];
        int distB = b[0] * b[0] + b[1] * b[1];
        return distA < distB;
    });
    
    // 返回前k个点
    return vector<vector<int>>(points.begin(), points.begin() + k);
}

// LeetCode 179. Largest Number (最大数)
string largestNumber(vector<int>& nums) {
    // 转换为字符串数组
    vector<string> strs(nums.size());
    for (int i = 0; i < nums.size(); i++) {
        strs[i] = to_string(nums[i]);
    }
    
    // 使用自定义比较函数排序
    sort(strs.begin(), strs.end(), [](const string& a, const string& b) {
        // 比较b+a和a+b的大小，注意是降序排列所以顺序颠倒
        return a + b > b + a;
    });
    
    // 处理全为0的特殊情况
    if (strs[0] == "0") {
        return "0";
    }
    
    // 拼接结果
    string result;
    for (const string& str : strs) {
        result += str;
    }
    
    return result;
}

// LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序)
vector<int> sortByBits(vector<int>& arr) {
    // 使用自定义比较函数排序
    sort(arr.begin(), arr.end(), [](int a, int b) {
        // 计算二进制中1的位数
        int bitCountA = __builtin_popcount(a);
        int bitCountB = __builtin_popcount(b);
        
        // 如果1的位数相同，按数值大小排序；否则按1的位数排序
        if (bitCountA == bitCountB) {
            return a < b;
        } else {
            return bitCountA < bitCountB;
        }
    });
    
    return arr;
}

// Player类用于HackerRank Java Comparator示例
class Player {
public:
    string name;
    int score;
    
    Player(string n, int s) : name(n), score(s) {}
};

// Player比较函数
bool playerComparator(const Player& a, const Player& b) {
    // 首先按分数降序排列
    if (a.score != b.score) {
        return a.score > b.score;
    }
    // 如果分数相同，按名字升序排列
    return a.name < b.name;
}

// LeetCode 56. Merge Intervals (合并区间)
vector<vector<int>> merge(vector<vector<int>>& intervals) {
    if (intervals.size() <= 1) {
        return intervals;
    }

    // 按区间的起始位置排序
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[0] < b[0];
    });

    vector<vector<int>> result;
    vector<int> current = intervals[0];
    result.push_back(current);

    for (int i = 1; i < intervals.size(); i++) {
        vector<int> interval = intervals[i];
        // 如果当前区间与下一个区间重叠，合并它们
        if (interval[0] <= current[1]) {
            current[1] = max(current[1], interval[1]);
        } else {
            // 否则，将下一个区间添加到结果中
            current = interval;
            result.push_back(current);
        }
    }

    return result;
}

// LeetCode 1122. Relative Sort Array (数组的相对排序)
vector<int> relativeSortArray(vector<int>& arr1, vector<int>& arr2) {
    // 创建arr2中元素到索引的映射
    map<int, int> indexMap;
    for (int i = 0; i < arr2.size(); i++) {
        indexMap[arr2[i]] = i;
    }

    // 使用自定义比较函数排序
    sort(arr1.begin(), arr1.end(), [&indexMap](int a, int b) {
        // 如果两个元素都在arr2中，按它们在arr2中的索引排序
        if (indexMap.find(a) != indexMap.end() && indexMap.find(b) != indexMap.end()) {
            return indexMap[a] < indexMap[b];
        }
        // 如果只有a在arr2中，a排在前面
        if (indexMap.find(a) != indexMap.end()) {
            return true;
        }
        // 如果只有b在arr2中，b排在前面
        if (indexMap.find(b) != indexMap.end()) {
            return false;
        }
        // 如果两个元素都不在arr2中，按数值大小排序
        return a < b;
    });

    return arr1;
}

int main() {
    // 测试最接近原点的 K 个点
    cout << "测试最接近原点的 K 个点:" << endl;
    vector<vector<int>> points = {{1,1},{3,3},{2,2}};
    vector<vector<int>> result1 = kClosest(points, 2);
    for (const auto& point : result1) {
        cout << "[" << point[0] << ", " << point[1] << "]" << endl;
    }
    
    // 测试最大数
    cout << "测试最大数:" << endl;
    vector<int> nums = {3,30,34,5,9};
    cout << largestNumber(nums) << endl;
    
    // 测试根据数字二进制下 1 的数目排序
    cout << "测试根据数字二进制下 1 的数目排序:" << endl;
    vector<int> arr = {0,1,2,3,4,5,6,7,8};
    vector<int> result2 = sortByBits(arr);
    cout << "[";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i];
        if (i < result2.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
    
    // 测试HackerRank Java Comparator
    cout << "测试HackerRank Java Comparator:" << endl;
    vector<Player> players = {
        Player("amy", 100),
        Player("david", 100),
        Player("heraldo", 50),
        Player("aakansha", 75),
        Player("aleksa", 150)
    };
    sort(players.begin(), players.end(), playerComparator);
    for (const Player& player : players) {
        cout << player.name << " " << player.score << endl;
    }
    
    // 测试合并区间
    cout << "测试合并区间:" << endl;
    vector<vector<int>> intervals = {{1,3},{2,6},{8,10},{15,18}};
    vector<vector<int>> merged = merge(intervals);
    for (const auto& interval : merged) {
        cout << "[" << interval[0] << ", " << interval[1] << "]" << endl;
    }
    
    // 测试数组的相对排序
    cout << "测试数组的相对排序:" << endl;
    vector<int> arr1 = {2,3,1,3,2,4,6,7,9,2,19};
    vector<int> arr2 = {2,1,4,3,9,6};
    vector<int> result3 = relativeSortArray(arr1, arr2);
    cout << "[";
    for (int i = 0; i < result3.size(); i++) {
        cout << result3[i];
        if (i < result3.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
    
    return 0;
}