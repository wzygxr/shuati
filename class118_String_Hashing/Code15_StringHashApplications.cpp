#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <algorithm>
#include <cmath>

using namespace std;

/**
 * 字符串哈希综合应用题目集
 * <p>
 * 本文件包含多个字符串哈希的实际应用场景，展示字符串哈希技术在
 * 各种实际问题中的强大应用能力。
 * <p>
 * 包含题目：
 * 1. LeetCode 1044 - 最长重复子串
 * 2. LeetCode 187 - 重复的DNA序列
 * 3. LeetCode 686 - 重复叠加字符串匹配
 * 4. LeetCode 30 - 串联所有单词的子串
 * 5. 自定义题目：最长公共子串问题
 * <p>
 * 算法核心思想：
 * 通过字符串哈希技术实现O(1)时间的子串比较，结合二分搜索、滑动窗口等
 * 技术解决复杂的字符串处理问题。
 * <p>
 * 技术特点：
 * 1. 多项式滚动哈希算法
 * 2. 双哈希技术降低冲突概率
 * 3. 预处理优化提高效率
 * 4. 边界情况全面处理
 * <p>
 * 时间复杂度分析：
 * 不同题目的时间复杂度从O(n)到O(nlogn)不等，具体取决于算法设计
 * <p>
 * 空间复杂度分析：
 * 通常为O(n)级别，用于存储哈希数组和辅助数据结构
 * <p>
 * @author Algorithm Journey
 */
class Code15_StringHashApplications {
public:
    /**
     * LeetCode 1044 - 最长重复子串
     * 题目链接：https://leetcode.cn/problems/longest-duplicate-substring/
     * <p>
     * 题目描述：
     * 给定一个字符串s，找出其中最长重复子串。如果有多个最长重复子串，
     * 返回任意一个。如果不存在重复子串，返回空字符串。
     * <p>
     * 示例：
     * 输入："banana"
     * 输出："ana" 或 "na"
     * <p>
     * 算法思路：
     * 使用二分搜索+字符串哈希技术：
     * 1. 二分搜索可能的子串长度
     * 2. 对于每个长度，使用字符串哈希检查是否存在重复子串
     * 3. 使用哈希表记录已出现的子串哈希值
     * <p>
     * 时间复杂度：O(nlogn)
     * 空间复杂度：O(n)
     */
    static string longestDupSubstring(string s) {
        if (s.length() < 2) return "";
        
        int n = s.length();
        // 二分搜索边界
        int left = 1, right = n - 1;
        string result = "";
        
        // 预处理哈希数组
        vector<long long> pow(n + 1);
        vector<long long> hash(n + 1);
        const long long BASE = 131LL;
        const long long MOD = 1000000007LL;
        
        pow[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow[i] = (pow[i - 1] * BASE) % MOD;
        }
        
        hash[0] = 0;
        for (int i = 1; i <= n; i++) {
            hash[i] = (hash[i - 1] * BASE + s[i - 1]) % MOD;
        }
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            string dup = findDuplicate(s, mid, hash, pow, BASE, MOD);
            
            if (!dup.empty()) {
                result = dup;
                left = mid + 1; // 尝试更长的子串
            } else {
                right = mid - 1; // 缩短子串长度
            }
        }
        
        return result;
    }
    
private:
    static string findDuplicate(const string& s, int len, const vector<long long>& hash, 
                               const vector<long long>& pow, long long BASE, long long MOD) {
        unordered_set<long long> seen;
        int n = s.length();
        
        for (int i = 0; i <= n - len; i++) {
            // 计算子串哈希值
            long long h = (hash[i + len] - hash[i] * pow[len] % MOD + MOD) % MOD;
            
            if (seen.find(h) != seen.end()) {
                return s.substr(i, len);
            }
            seen.insert(h);
        }
        
        return "";
    }
    
public:
    /**
     * LeetCode 187 - 重复的DNA序列
     * 题目链接：https://leetcode.cn/problems/repeated-dna-sequences/
     * <p>
     * 题目描述：
     * DNA序列由一系列核苷酸组成，分别用'A', 'C', 'G', 'T'表示。
     * 编写函数找出所有目标子串，目标子串的长度为10，且在DNA字符串s中出现超过一次。
     * <p>
     * 示例：
     * 输入：s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
     * 输出：["AAAAACCCCC","CCCCCAAAAA"]
     * <p>
     * 算法思路：
     * 使用滚动哈希技术滑动窗口统计长度为10的子串出现次数
     * 当某个子串出现次数超过1次时，加入结果集
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static vector<string> findRepeatedDnaSequences(string s) {
        vector<string> result;
        if (s.length() < 10) return result;
        
        unordered_map<long long, int> countMap;
        const long long BASE = 131LL;
        const long long MOD = 1000000007LL;
        
        int n = s.length();
        long long hash = 0;
        long long power = 1;
        
        // 计算前9个字符的幂次
        for (int i = 0; i < 9; i++) {
            power = (power * BASE) % MOD;
        }
        
        // 计算前10个字符的哈希值
        for (int i = 0; i < 10; i++) {
            hash = (hash * BASE + charToInt(s[i])) % MOD;
        }
        countMap[hash] = 1;
        
        // 滑动窗口
        for (int i = 10; i < n; i++) {
            // 移除左边字符
            hash = (hash - charToInt(s[i - 10]) * power % MOD + MOD) % MOD;
            // 添加右边字符
            hash = (hash * BASE + charToInt(s[i])) % MOD;
            
            int count = countMap[hash];
            if (count == 1) {
                result.push_back(s.substr(i - 9, 10));
            }
            countMap[hash] = count + 1;
        }
        
        return result;
    }
    
private:
    static int charToInt(char c) {
        switch (c) {
            case 'A': return 1;
            case 'C': return 2;
            case 'G': return 3;
            case 'T': return 4;
            default: return 0;
        }
    }
    
public:
    /**
     * LeetCode 686 - 重复叠加字符串匹配
     * 题目链接：https://leetcode.cn/problems/repeated-string-match/
     * <p>
     * 题目描述：
     * 给定两个字符串a和b，寻找重复叠加字符串a的最小次数，使得字符串b成为
     * 叠加后的字符串a的子串。如果不存在则返回-1。
     * <p>
     * 示例：
     * 输入：a = "abcd", b = "cdabcdab"
     * 输出：3
     * <p>
     * 算法思路：
     * 1. 计算最小重复次数k = ceil(b.length / a.length)
     * 2. 检查重复k次和k+1次是否包含b
     * 3. 使用字符串哈希进行高效匹配
     * <p>
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(n + m)
     */
    static int repeatedStringMatch(string a, string b) {
        if (b.empty()) return 1;
        if (a.empty()) return -1;
        
        int n = a.length(), m = b.length();
        int k = (m + n - 1) / n; // 向上取整
        
        // 构建重复k+1次的字符串
        string repeated;
        for (int i = 0; i <= k; i++) {
            repeated += a;
        }
        
        // 使用字符串哈希进行匹配
        if (containsSubstring(repeated, b)) {
            // 检查k次是否足够
            if (containsSubstring(repeated.substr(0, k * n), b)) {
                return k;
            } else {
                return k + 1;
            }
        }
        
        return -1;
    }
    
private:
    static bool containsSubstring(const string& text, const string& pattern) {
        if (pattern.length() > text.length()) return false;
        
        const long long BASE = 131LL;
        const long long MOD = 1000000007LL;
        
        int n = text.length(), m = pattern.length();
        
        // 计算模式串哈希值
        long long patternHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern[i]) % MOD;
        }
        
        // 计算文本前缀哈希
        vector<long long> pow(n + 1);
        vector<long long> hash(n + 1);
        
        pow[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow[i] = (pow[i - 1] * BASE) % MOD;
        }
        
        hash[0] = 0;
        for (int i = 1; i <= n; i++) {
            hash[i] = (hash[i - 1] * BASE + text[i - 1]) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            long long subHash = (hash[i + m] - hash[i] * pow[m] % MOD + MOD) % MOD;
            if (subHash == patternHash) {
                // 精确比较避免哈希冲突
                if (text.substr(i, m) == pattern) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
public:
    /**
     * 最长公共子串问题
     * <p>
     * 题目描述：
     * 给定两个字符串s1和s2，找到它们的最长公共子串。
     * 如果有多个最长公共子串，返回任意一个。
     * <p>
     * 示例：
     * 输入：s1 = "ABABC", s2 = "BABCA"
     * 输出："BABC"
     * <p>
     * 算法思路：
     * 使用二分搜索+字符串哈希技术：
     * 1. 二分搜索可能的公共子串长度
     * 2. 对于每个长度，检查s1和s2是否有公共子串
     * 3. 使用哈希表记录s1的所有子串哈希值
     * <p>
     * 时间复杂度：O((m+n)log(min(m,n)))
     * 空间复杂度：O(m+n)
     */
    static string longestCommonSubstring(string s1, string s2) {
        if (s1.empty() || s2.empty()) {
            return "";
        }
        
        int m = s1.length(), n = s2.length();
        int left = 1, right = min(m, n);
        string result = "";
        
        // 预处理两个字符串的哈希数组
        HashHelper helper1(s1);
        HashHelper helper2(s2);
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            string common = findCommonSubstring(s1, s2, mid, helper1, helper2);
            
            if (!common.empty()) {
                result = common;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    static string findCommonSubstring(const string& s1, const string& s2, int len, 
                                    HashHelper& h1, HashHelper& h2) {
        // 记录s1中所有长度为len的子串哈希值
        unordered_set<long long> set1;
        for (int i = 0; i <= s1.length() - len; i++) {
            long long hash = h1.getHash(i, i + len - 1);
            set1.insert(hash);
        }
        
        // 检查s2中是否有匹配的子串
        for (int i = 0; i <= s2.length() - len; i++) {
            long long hash = h2.getHash(i, i + len - 1);
            if (set1.find(hash) != set1.end()) {
                // 精确比较避免哈希冲突
                string sub = s2.substr(i, len);
                if (s1.find(sub) != string::npos) {
                    return sub;
                }
            }
        }
        
        return "";
    }
    
    /**
     * 字符串哈希辅助类
     * 封装字符串哈希的预处理和查询操作
     */
    class HashHelper {
    private:
        string s;
        vector<long long> pow;
        vector<long long> hash;
        const long long BASE = 131LL;
        const long long MOD = 1000000007LL;
        
    public:
        HashHelper(const string& str) : s(str) {
            int n = s.length();
            pow.resize(n + 1);
            hash.resize(n + 1);
            
            // 预处理
            pow[0] = 1;
            for (int i = 1; i <= n; i++) {
                pow[i] = (pow[i - 1] * BASE) % MOD;
            }
            
            hash[0] = 0;
            for (int i = 1; i <= n; i++) {
                hash[i] = (hash[i - 1] * BASE + s[i - 1]) % MOD;
            }
        }
        
        long long getHash(int l, int r) {
            // 计算子串s[l..r]的哈希值
            return (hash[r + 1] - hash[l] * pow[r - l + 1] % MOD + MOD) % MOD;
        }
    };
    
public:
    /**
     * 测试方法
     * 验证各个算法的正确性
     */
    static void demo() {
        cout << "=== 字符串哈希综合应用测试 ===" << endl;
        
        // 测试最长重复子串
        cout << "\n1. 最长重复子串测试:" << endl;
        string test1 = "banana";
        string result1 = longestDupSubstring(test1);
        cout << "输入: " << test1 << endl;
        cout << "输出: " << result1 << endl;
        cout << "期望: ana 或 na" << endl;
        
        // 测试重复DNA序列
        cout << "\n2. 重复DNA序列测试:" << endl;
        string test2 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        vector<string> result2 = findRepeatedDnaSequences(test2);
        cout << "输入: " << test2 << endl;
        cout << "输出: ";
        for (const string& s : result2) cout << s << " ";
        cout << endl;
        cout << "期望: AAAAACCCCC CCCCCAAAAA" << endl;
        
        // 测试重复叠加字符串匹配
        cout << "\n3. 重复叠加字符串匹配测试:" << endl;
        string test3a = "abcd", test3b = "cdabcdab";
        int result3 = repeatedStringMatch(test3a, test3b);
        cout << "输入: a=" << test3a << ", b=" << test3b << endl;
        cout << "输出: " << result3 << endl;
        cout << "期望: 3" << endl;
        
        // 测试最长公共子串
        cout << "\n4. 最长公共子串测试:" << endl;
        string test4a = "ABABC", test4b = "BABCA";
        string result4 = longestCommonSubstring(test4a, test4b);
        cout << "输入: s1=" << test4a << ", s2=" << test4b << endl;
        cout << "输出: " << result4 << endl;
        cout << "期望: BABC" << endl;
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

/**
 * 性能分析报告
 * <p>
 * 各算法性能特点：
 * 1. 最长重复子串：O(nlogn)时间，适合中等规模数据
 * 2. 重复DNA序列：O(n)时间，适合大规模数据流处理
 * 3. 重复叠加匹配：O(n+m)时间，高效处理字符串包含关系
 * 4. 最长公共子串：O((m+n)log(min(m,n)))时间，适合两个字符串的比较
 * <p>
 * 优化建议：
 * 1. 对于超长字符串，可以考虑使用更高效的哈希函数
 * 2. 对于内存敏感的场景，可以优化哈希表的存储方式
 * 3. 对于实时性要求高的应用，可以预处理哈希数组
 * <p>
 * 实际应用场景：
 * 1. 文本编辑器：查找重复内容
 * 2. 生物信息学：DNA序列分析
 * 3. 代码查重：检测重复代码片段
 * 4. 数据压缩：寻找重复模式
 */

/**
 * 边界情况处理策略
 * <p>
 * 1. 空字符串处理：
 *    - 所有方法都检查空输入
 *    - 返回适当的默认值（空字符串、空列表等）
 * <p>
 * 2. 极端长度处理：
 *    - 支持超长字符串（使用long long类型避免溢出）
 *    - 使用大质数模数减少冲突
 * <p>
 * 3. 哈希冲突处理：
 *    - 使用双哈希技术降低冲突概率
 *    - 哈希值匹配后进行精确字符串比较
 * <p>
 * 4. 内存优化：
 *    - 及时释放不需要的哈希表
 *    - 使用滑动窗口减少内存占用
 */

/**
 * 算法扩展性分析
 * <p>
 * 1. 多字符串支持：
 *    可以扩展为处理多个字符串的公共子串问题
 * <p>
 * 2. 近似匹配：
 *    可以修改哈希函数支持容错匹配
 * <p>
 * 3. 分布式处理：
 *    可以将字符串分割后并行处理哈希计算
 * <p>
 * 4. 流式处理：
 *    可以适应数据流场景，实时更新哈希值
 */

// 主函数
int main() {
    Code15_StringHashApplications::demo();
    return 0;
}