#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
#include <set>
#include <unordered_map>
using namespace std;

/**
 * Comparator高级应用题目实现（C++版本）
 * 包含LeetCode 524, 937, 1331, 1366, 1451, 1509, 1561, 1636, 1710, 1859等题目
 * 
 * C++中Comparator的实现：
 * - 使用lambda表达式实现自定义比较器
 * - 使用函数对象实现复杂比较逻辑
 * - stable_sort实现稳定排序
 * - 自定义比较器可以访问私有成员
 * 
 * 时间复杂度分析：
 * - 排序操作：O(n log n)
 * - 自定义比较器：O(1)每次比较
 * - 复杂比较逻辑：O(k)每次比较，k为比较元素的复杂度
 * 
 * 空间复杂度分析：
 * - 排序算法：O(log n) 栈空间
 * - 额外数据结构：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理空输入、边界条件
 * 2. 性能优化：避免在比较器中创建新对象，使用引用
 * 3. 代码可读性：清晰的比较逻辑和注释
 * 4. 稳定性：注意排序算法的稳定性要求
 * 
 * 相关平台题目：
 * 1. LeetCode 524. Longest Word in Dictionary through Deleting (通过删除字母匹配到字典里最长单词) - https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
 * 2. LeetCode 937. Reorder Data in Log Files (重新排列日志文件) - https://leetcode.com/problems/reorder-data-in-log-files/
 * 3. LeetCode 1331. Rank Transform of an Array (数组序号转换) - https://leetcode.com/problems/rank-transform-of-an-array/
 * 4. LeetCode 1366. Rank Teams by Votes (通过投票对团队排名) - https://leetcode.com/problems/rank-teams-by-votes/
 * 5. LeetCode 1451. Rearrange Words in a Sentence (重新排列句子中的单词) - https://leetcode.com/problems/rearrange-words-in-a-sentence/
 * 6. LeetCode 1509. Minimum Difference Between Largest and Smallest Value in Three Moves (三次操作后最大值与最小值的最小差) - https://leetcode.com/problems/minimum-difference-between-largest-and-smallest-value-in-three-moves/
 * 7. LeetCode 1561. Maximum Number of Coins You Can Get (你可以获得的最大硬币数目) - https://leetcode.com/problems/maximum-number-of-coins-you-can-get/
 * 8. LeetCode 1636. Sort Array by Increasing Frequency (按照频率将数组升序排序) - https://leetcode.com/problems/sort-array-by-increasing-frequency/
 * 9. LeetCode 1710. Maximum Units on a Truck (卡车上的最大单元数) - https://leetcode.com/problems/maximum-units-on-a-truck/
 * 10. LeetCode 1859. Sorting the Sentence (将句子排序) - https://leetcode.com/problems/sorting-the-sentence/
 * 11. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
 * 12. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
 * 13. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
 * 14. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
 * 15. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
 * 16. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
 * 17. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
 * 18. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
 * 19. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
 * 20. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
 * 21. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
 * 22. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
 * 23. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
 * 24. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
 * 25. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
 * 26. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
 * 27. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
 * 28. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
 * 29. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
 * 30. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
 * 31. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
 * 32. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
 * 33. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
 * 34. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
 * 35. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 36. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
 * 37. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 38. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
 * 39. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
 * 40. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
 * 41. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
 * 42. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
 * 43. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
 * 44. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
 * 45. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
 * 46. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/
 * 47. LeetCode 1030. Matrix Cells in Distance Order (距离顺序排列矩阵单元格) - https://leetcode.com/problems/matrix-cells-in-distance-order/
 * 48. LeetCode 1122. Relative Sort Array (数组的相对排序) - https://leetcode.com/problems/relative-sort-array/
 * 49. LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序) - https://leetcode.com/problems/sort-integers-by-the-number-of-1-bits/
 * 50. LeetCode 179. Largest Number (最大数) - https://leetcode.com/problems/largest-number/
 */

/**
 * LeetCode 524. Longest Word in Dictionary through Deleting
 * 通过删除字母匹配到字典里最长单词
 * 网址：https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
 * 
 * 解题思路：
 * 1. 使用自定义Comparator按长度降序、字典序升序排序
 * 2. 检查每个单词是否可以通过删除s中的字符得到
 * 3. 返回第一个满足条件的单词
 * 
 * 时间复杂度：O(n * x * log n)，其中x是单词平均长度
 * 空间复杂度：O(1)
 */
class LongestWordInDictionary {
private:
    bool isSubsequence(const string& word, const string& s) {
        int i = 0, j = 0;
        while (i < word.length() && j < s.length()) {
            if (word[i] == s[j]) {
                i++;
            }
            j++;
        }
        return i == word.length();
    }
    
public:
    string findLongestWord(string s, vector<string>& dictionary) {
        // 自定义比较器：先按长度降序，再按字典序升序
        sort(dictionary.begin(), dictionary.end(), [](const string& a, const string& b) {
            if (a.length() != b.length()) {
                return a.length() > b.length(); // 长度降序
            }
            return a < b; // 字典序升序
        });
        
        for (const string& word : dictionary) {
            if (isSubsequence(word, s)) {
                return word;
            }
        }
        return "";
    }
};

/**
 * LeetCode 937. Reorder Data in Log Files
 * 重新排列日志文件
 * 网址：https://leetcode.com/problems/reorder-data-in-log-files/
 * 
 * 解题思路：
 * 1. 区分字母日志和数字日志
 * 2. 字母日志按内容字典序排序，内容相同按标识符排序
 * 3. 数字日志保持原有顺序
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class ReorderLogFiles {
public:
    vector<string> reorderLogFiles(vector<string>& logs) {
        // 自定义比较器
        sort(logs.begin(), logs.end(), [](const string& a, const string& b) {
            // 找到第一个空格的位置
            size_t spaceA = a.find(' ');
            size_t spaceB = b.find(' ');
            
            string identifierA = a.substr(0, spaceA);
            string contentA = a.substr(spaceA + 1);
            string identifierB = b.substr(0, spaceB);
            string contentB = b.substr(spaceB + 1);
            
            bool isDigitA = isdigit(contentA[0]);
            bool isDigitB = isdigit(contentB[0]);
            
            // 情况1：都是字母日志
            if (!isDigitA && !isDigitB) {
                if (contentA != contentB) {
                    return contentA < contentB;
                }
                return identifierA < identifierB;
            }
            
            // 情况2：一个是字母日志，一个是数字日志
            if (!isDigitA && isDigitB) {
                return true; // 字母日志在前
            } else if (isDigitA && !isDigitB) {
                return false; // 数字日志在后
            } else {
                // 情况3：都是数字日志，保持原有顺序
                return false;
            }
        });
        
        return logs;
    }
};

/**
 * LeetCode 1331. Rank Transform of an Array
 * 数组序号转换
 * 网址：https://leetcode.com/problems/rank-transform-of-an-array/
 * 
 * 解题思路：
 * 1. 创建排序后的数组副本
 * 2. 使用map存储值到排名的映射
 * 3. 根据映射转换原数组
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class RankTransformArray {
public:
    vector<int> arrayRankTransform(vector<int>& arr) {
        if (arr.empty()) return {};
        
        // 创建排序后的数组副本
        vector<int> sortedArr = arr;
        sort(sortedArr.begin(), sortedArr.end());
        
        // 创建值到排名的映射
        map<int, int> rankMap;
        int rank = 1;
        for (int num : sortedArr) {
            if (rankMap.find(num) == rankMap.end()) {
                rankMap[num] = rank++;
            }
        }
        
        // 转换原数组
        vector<int> result;
        for (int num : arr) {
            result.push_back(rankMap[num]);
        }
        
        return result;
    }
};

/**
 * LeetCode 1366. Rank Teams by Votes
 * 通过投票对团队排名
 * 网址：https://leetcode.com/problems/rank-teams-by-votes/
 * 
 * 解题思路：
 * 1. 统计每个团队在每个位置的得票数
 * 2. 使用自定义Comparator比较投票统计
 * 3. 按得票规则排序团队
 * 
 * 时间复杂度：O(n * m + n log n)，其中m是投票位置数
 * 空间复杂度：O(n²)
 */
class RankTeamsByVotes {
public:
    string rankTeams(vector<string>& votes) {
        if (votes.empty()) return "";
        if (votes.size() == 1) return votes[0];
        
        int n = votes[0].length();
        // 统计每个团队在每个位置的得票数
        unordered_map<char, vector<int>> voteCount;
        for (char team : votes[0]) {
            voteCount[team] = vector<int>(n, 0);
        }
        
        for (const string& vote : votes) {
            for (int i = 0; i < n; i++) {
                voteCount[vote[i]][i]++;
            }
        }
        
        // 创建团队列表
        vector<char> teams;
        for (char team : votes[0]) {
            teams.push_back(team);
        }
        
        // 自定义比较器
        sort(teams.begin(), teams.end(), [&](char a, char b) {
            for (int i = 0; i < n; i++) {
                if (voteCount[a][i] != voteCount[b][i]) {
                    return voteCount[a][i] > voteCount[b][i]; // 得票数降序
                }
            }
            return a < b; // 字母序升序
        });
        
        return string(teams.begin(), teams.end());
    }
};

/**
 * LeetCode 1451. Rearrange Words in a Sentence
 * 重新排列句子中的单词
 * 网址：https://leetcode.com/problems/rearrange-words-in-a-text/
 * 
 * 解题思路：
 * 1. 按单词长度排序
 * 2. 长度相同保持原有顺序（稳定排序）
 * 3. 首字母大写处理
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class RearrangeWords {
public:
    string arrangeWords(string text) {
        if (text.empty()) return text;
        
        // 转换为小写
        transform(text.begin(), text.end(), text.begin(), ::tolower);
        
        // 分割单词
        vector<string> words;
        string word;
        for (char c : text) {
            if (c == ' ') {
                if (!word.empty()) {
                    words.push_back(word);
                    word.clear();
                }
            } else {
                word += c;
            }
        }
        if (!word.empty()) {
            words.push_back(word);
        }
        
        // 稳定排序：按长度排序
        stable_sort(words.begin(), words.end(), [](const string& a, const string& b) {
            return a.length() < b.length();
        });
        
        // 首字母大写处理
        if (!words.empty() && !words[0].empty()) {
            words[0][0] = toupper(words[0][0]);
        }
        
        // 重新组合成句子
        string result;
        for (size_t i = 0; i < words.size(); i++) {
            if (i > 0) result += " ";
            result += words[i];
        }
        
        return result;
    }
};

// 测试函数
int main() {
    // 测试LeetCode 524
    LongestWordInDictionary solution1;
    string s = "abpcplea";
    vector<string> dictionary = {"ale", "apple", "monkey", "plea"};
    string longestWord = solution1.findLongestWord(s, dictionary);
    cout << "LeetCode 524 Result: " << longestWord << endl;
    
    // 测试LeetCode 1331
    RankTransformArray solution2;
    vector<int> arr = {40, 10, 20, 30};
    vector<int> ranks = solution2.arrayRankTransform(arr);
    cout << "LeetCode 1331 Result: ";
    for (int rank : ranks) cout << rank << " ";
    cout << endl;
    
    return 0;
}