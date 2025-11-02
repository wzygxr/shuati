#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <algorithm>
#include <cmath>

using namespace std;

/**
 * 高级字符串哈希应用 - 包含更多复杂场景和优化技术
 * <p>
 * 本文件包含字符串哈希的高级应用场景，展示字符串哈希在复杂问题中的
 * 强大能力和各种优化技术。
 * <p>
 * 包含题目：
 * 1. LeetCode 214 - 最短回文串
 * 2. LeetCode 336 - 回文对
 * 3. LeetCode 1316 - 不同的循环子字符串
 * 4. 自定义题目：字符串循环同构检测
 * 5. 自定义题目：多模式字符串匹配
 * <p>
 * 高级技术特点：
 * 1. 回文哈希技术
 * 2. 循环字符串处理
 * 3. 多模式匹配优化
 * 4. 双哈希+滚动哈希组合
 * 5. 内存优化策略
 * <p>
 * 时间复杂度分析：
 * 不同题目从O(n)到O(n^2)不等，但通过哈希优化显著提高效率
 * <p>
 * 空间复杂度分析：
 * 通常为O(n)级别，针对大规模数据有特殊优化
 * <p>
 * @author Algorithm Journey
 */
class Code16_AdvancedStringHash {
public:
    /**
     * LeetCode 214 - 最短回文串
     * 题目链接：https://leetcode.cn/problems/shortest-palindrome/
     * <p>
     * 题目描述：
     * 给定一个字符串s，你可以通过在字符串前面添加字符将其转换为回文串。
     * 找到并返回可以用这种方式转换的最短回文串。
     * <p>
     * 示例：
     * 输入："aacecaaa"
     * 输出："aaacecaaa"
     * <p>
     * 算法思路：
     * 1. 找到字符串s的最长回文前缀
     * 2. 将剩余部分反转后添加到字符串前面
     * 3. 使用字符串哈希技术高效判断回文性
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static string shortestPalindrome(string s) {
        if (s.length() <= 1) return s;
        
        int n = s.length();
        // 使用字符串哈希技术寻找最长回文前缀
        string reversed = s;
        reverse(reversed.begin(), reversed.end());
        
        // 计算原字符串和反转字符串的哈希值
        HashHelper original(s);
        HashHelper reversedHelper(reversed);
        
        // 寻找最长回文前缀
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            // 检查s[0..i]是否是回文
            if (original.isPalindrome(0, i)) {
                maxLen = i + 1;
            }
        }
        
        // 如果整个字符串已经是回文，直接返回
        if (maxLen == n) return s;
        
        // 将剩余部分反转后添加到前面
        string toAdd = s.substr(maxLen);
        reverse(toAdd.begin(), toAdd.end());
        return toAdd + s;
    }
    
    /**
     * LeetCode 336 - 回文对
     * 题目链接：https://leetcode.cn/problems/palindrome-pairs/
     * <p>
     * 题目描述：
     * 给定一组互不相同的单词，找出所有不同的索引对(i, j)，
     * 使得连接两个单词words[i] + words[j]是回文串。
     * <p>
     * 示例：
     * 输入：{"abcd","dcba","lls","s","sssll"}
     * 输出：{{0,1},{1,0},{3,2},{2,4}}
     * <p>
     * 算法思路：
     * 使用字符串哈希技术高效判断回文性，结合哈希表存储单词信息
     * 1. 预处理所有单词的正向和反向哈希
     * 2. 对于每个单词，检查其前缀或后缀是否是回文
     * 3. 使用哈希表快速查找匹配的单词
     * <p>
     * 时间复杂度：O(n * k^2)，其中n是单词数量，k是单词平均长度
     * 空间复杂度：O(n)
     */
    static vector<vector<int>> palindromePairs(vector<string>& words) {
        vector<vector<int>> result;
        if (words.empty()) return result;
        
        int n = words.size();
        // 存储单词到索引的映射
        unordered_map<string, int> wordMap;
        for (int i = 0; i < n; i++) {
            wordMap[words[i]] = i;
        }
        
        // 预处理所有单词的哈希信息
        vector<HashHelper> helpers;
        vector<HashHelper> reverseHelpers;
        for (int i = 0; i < n; i++) {
            helpers.push_back(HashHelper(words[i]));
            string reversed = words[i];
            reverse(reversed.begin(), reversed.end());
            reverseHelpers.push_back(HashHelper(reversed));
        }
        
        for (int i = 0; i < n; i++) {
            string word = words[i];
            int len = word.length();
            
            // 情况1：空字符串可以与任何回文单词配对
            if (word.empty()) {
                for (int j = 0; j < n; j++) {
                    if (i != j && helpers[j].isPalindrome(0, words[j].length() - 1)) {
                        result.push_back({i, j});
                        result.push_back({j, i});
                    }
                }
                continue;
            }
            
            // 情况2：检查word + otherWord是否是回文
            string reversed = word;
            reverse(reversed.begin(), reversed.end());
            if (wordMap.find(reversed) != wordMap.end() && wordMap[reversed] != i) {
                result.push_back({i, wordMap[reversed]});
            }
            
            // 情况3：检查word的前缀回文部分
            for (int k = 1; k < len; k++) {
                // 如果word[0..k-1]是回文，那么检查reversed[0..len-k-1]是否存在
                if (helpers[i].isPalindrome(0, k - 1)) {
                    string toFind = reversed.substr(0, len - k);
                    if (wordMap.find(toFind) != wordMap.end() && wordMap[toFind] != i) {
                        result.push_back({wordMap[toFind], i});
                    }
                }
                
                // 如果word[k..len-1]是回文，那么检查reversed[len-k..len-1]是否存在
                if (helpers[i].isPalindrome(k, len - 1)) {
                    string toFind = reversed.substr(len - k);
                    if (wordMap.find(toFind) != wordMap.end() && wordMap[toFind] != i) {
                        result.push_back({i, wordMap[toFind]});
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 1316 - 不同的循环子字符串
     * 题目链接：https://leetcode.cn/problems/distinct-echo-substrings/
     * <p>
     * 题目描述：
     * 给你一个字符串text，请你返回满足下述条件的不同非空子字符串的数目：
     * 可以写成某个字符串与其自身相连接的形式（即可以写成a + a，其中a是非空字符串）。
     * <p>
     * 示例：
     * 输入："abcabcabc"
     * 输出：3
     * 解释：3个不同的循环子字符串："abcabc", "bcabca", "cabcab"
     * <p>
     * 算法思路：
     * 使用滚动哈希技术高效检测循环子字符串
     * 1. 遍历所有可能的子串长度（偶数长度）
     * 2. 对于每个位置，检查text[i..i+len-1]和text[i+len..i+2*len-1]是否相等
     * 3. 使用哈希表记录已经找到的循环子字符串
     * <p>
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    static int distinctEchoSubstrings(string text) {
        if (text.length() < 2) return 0;
        
        int n = text.length();
        unordered_set<string> result;
        HashHelper helper(text);
        
        // 遍历所有可能的子串长度（从1到n/2）
        for (int len = 1; len <= n / 2; len++) {
            for (int i = 0; i <= n - 2 * len; i++) {
                // 检查text[i..i+len-1]和text[i+len..i+2*len-1]是否相等
                if (helper.getHash(i, i + len - 1) == helper.getHash(i + len, i + 2 * len - 1)) {
                    // 精确比较避免哈希冲突
                    string sub1 = text.substr(i, len);
                    string sub2 = text.substr(i + len, len);
                    if (sub1 == sub2) {
                        result.insert(text.substr(i, 2 * len));
                    }
                }
            }
        }
        
        return result.size();
    }
    
    /**
     * 字符串循环同构检测
     * <p>
     * 题目描述：
     * 给定两个字符串s1和s2，判断它们是否是循环同构的。
     * 循环同构定义：如果可以通过循环移位使s1变成s2，则称s1和s2循环同构。
     * <p>
     * 示例：
     * 输入：s1 = "abcde", s2 = "cdeab"
     * 输出：true
     * <p>
     * 算法思路：
     * 1. 将s1复制一份拼接成s1+s1
     * 2. 在s1+s1中查找s2
     * 3. 使用字符串哈希技术高效匹配
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static bool isCyclicIsomorphic(string s1, string s2) {
        if (s1.length() != s2.length()) return false;
        
        int n = s1.length();
        // 特殊情况处理
        if (n == 0) return true;
        if (s1 == s2) return true;
        
        // 将s1复制一份拼接
        string doubled = s1 + s1;
        HashHelper doubledHelper(doubled);
        HashHelper s2Helper(s2);
        
        long long targetHash = s2Helper.getHash(0, n - 1);
        
        // 在doubled中查找与s2哈希值匹配的子串
        for (int i = 0; i < n; i++) {
            if (doubledHelper.getHash(i, i + n - 1) == targetHash) {
                // 精确比较避免哈希冲突
                if (doubled.substr(i, n) == s2) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 多模式字符串匹配算法
     * <p>
     * 题目描述：
     * 给定一个文本text和一组模式串patterns，找出所有模式串在文本中出现的位置。
     * <p>
     * 算法思路：
     * 使用Rabin-Karp算法的多模式扩展版本
     * 1. 预处理所有模式串的哈希值
     * 2. 使用滚动哈希技术遍历文本
     * 3. 对于每个窗口，检查哈希值是否匹配任何模式串
     * 4. 使用哈希表存储模式串信息提高查找效率
     * <p>
     * 时间复杂度：O(n + m1 + m2 + ... + mk)
     * 空间复杂度：O(k)，其中k是模式串数量
     */
    static unordered_map<string, vector<int>> multiPatternSearch(string text, vector<string> patterns) {
        unordered_map<string, vector<int>> result;
        if (text.empty() || patterns.empty()) return result;
        
        // 初始化结果映射
        for (string pattern : patterns) {
            result[pattern] = vector<int>();
        }
        
        int n = text.length();
        HashHelper textHelper(text);
        
        // 预处理模式串信息
        unordered_map<long long, vector<PatternInfo>> patternMap;
        for (string pattern : patterns) {
            if (pattern.empty()) continue;
            
            HashHelper patternHelper(pattern);
            long long patternHash = patternHelper.getHash(0, pattern.length() - 1);
            
            PatternInfo info(pattern, pattern.length());
            patternMap[patternHash].push_back(info);
        }
        
        // 滑动窗口匹配
        for (int i = 0; i < n; i++) {
            for (auto& entry : patternMap) {
                long long patternHash = entry.first;
                for (PatternInfo info : entry.second) {
                    int len = info.length;
                    if (i + len > n) continue;
                    
                    long long textHash = textHelper.getHash(i, i + len - 1);
                    if (textHash == patternHash) {
                        // 精确比较避免哈希冲突
                        if (text.substr(i, len) == info.pattern) {
                            result[info.pattern].push_back(i);
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * 模式串信息类
     * 存储模式串的基本信息
     */
    struct PatternInfo {
        string pattern;
        int length;
        
        PatternInfo(string p, int l) : pattern(p), length(l) {}
    };
    
    /**
     * 字符串哈希辅助类（增强版）
     * 支持回文检测和更高效的哈希操作
     */
    class HashHelper {
    private:
        string s;
        vector<long long> pow;
        vector<long long> hash;
        vector<long long> reverseHash;
        const long long BASE = 131LL;
        const long long MOD = 1000000007LL;
        
    public:
        HashHelper(string str) : s(str) {
            int n = s.length();
            pow.resize(n + 1);
            hash.resize(n + 1);
            reverseHash.resize(n + 1);
            
            // 预处理幂次数组
            pow[0] = 1;
            for (int i = 1; i <= n; i++) {
                pow[i] = (pow[i - 1] * BASE) % MOD;
            }
            
            // 预处理正向哈希
            hash[0] = 0;
            for (int i = 1; i <= n; i++) {
                hash[i] = (hash[i - 1] * BASE + s[i - 1]) % MOD;
            }
            
            // 预处理反向哈希（用于回文检测）
            reverseHash[0] = 0;
            for (int i = 1; i <= n; i++) {
                reverseHash[i] = (reverseHash[i - 1] * BASE + s[n - i]) % MOD;
            }
        }
        
        /**
         * 获取子串s[l..r]的哈希值
         */
        long long getHash(int l, int r) {
            if (l < 0 || r >= s.length() || l > r) {
                throw invalid_argument("Invalid range");
            }
            return (hash[r + 1] - hash[l] * pow[r - l + 1] % MOD + MOD) % MOD;
        }
        
        /**
         * 判断子串s[l..r]是否是回文
         */
        bool isPalindrome(int l, int r) {
            if (l < 0 || r >= s.length() || l > r) {
                return false;
            }
            
            int n = s.length();
            // 计算正向哈希
            long long forwardHash = getHash(l, r);
            
            // 计算反向哈希（对应原字符串中的位置）
            int reverseL = n - 1 - r;
            int reverseR = n - 1 - l;
            long long backwardHash = (reverseHash[reverseR + 1] - reverseHash[reverseL] * pow[reverseR - reverseL + 1] % MOD + MOD) % MOD;
            
            return forwardHash == backwardHash;
        }
        
        /**
         * 获取字符串长度
         */
        int length() {
            return s.length();
        }
    };
    
    /**
     * 测试方法
     * 验证各个算法的正确性
     */
    static void demo() {
        cout << "=== 高级字符串哈希应用测试 ===" << endl;
        
        // 测试最短回文串
        cout << "\n1. 最短回文串测试:" << endl;
        string test1 = "aacecaaa";
        string result1 = shortestPalindrome(test1);
        cout << "输入: " << test1 << endl;
        cout << "输出: " << result1 << endl;
        cout << "期望: aaacecaaa" << endl;
        
        // 测试回文对
        cout << "\n2. 回文对测试:" << endl;
        vector<string> test2 = {"abcd", "dcba", "lls", "s", "sssll"};
        vector<vector<int>> result2 = palindromePairs(test2);
        cout << "输入: ";
        for (string s : test2) cout << s << " ";
        cout << endl;
        cout << "输出: ";
        for (auto pair : result2) cout << "[" << pair[0] << "," << pair[1] << "] ";
        cout << endl;
        cout << "期望: [0,1] [1,0] [3,2] [2,4]" << endl;
        
        // 测试不同的循环子字符串
        cout << "\n3. 不同的循环子字符串测试:" << endl;
        string test3 = "abcabcabc";
        int result3 = distinctEchoSubstrings(test3);
        cout << "输入: " << test3 << endl;
        cout << "输出: " << result3 << endl;
        cout << "期望: 3" << endl;
        
        // 测试循环同构检测
        cout << "\n4. 循环同构检测测试:" << endl;
        string test4a = "abcde", test4b = "cdeab";
        bool result4 = isCyclicIsomorphic(test4a, test4b);
        cout << "输入: s1=" << test4a << ", s2=" << test4b << endl;
        cout << "输出: " << (result4 ? "true" : "false") << endl;
        cout << "期望: true" << endl;
        
        // 测试多模式匹配
        cout << "\n5. 多模式匹配测试:" << endl;
        string test5text = "ABABDABACDABABCABAB";
        vector<string> test5patterns = {"AB", "ABC", "BAB"};
        unordered_map<string, vector<int>> result5 = multiPatternSearch(test5text, test5patterns);
        cout << "文本: " << test5text << endl;
        cout << "模式: ";
        for (string p : test5patterns) cout << p << " ";
        cout << endl;
        cout << "匹配位置: ";
        for (auto& entry : result5) {
            cout << entry.first << ": [";
            for (int pos : entry.second) cout << pos << " ";
            cout << "] ";
        }
        cout << endl;
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

/**
 * 性能优化策略
 * <p>
 * 1. 内存优化：
 *    - 使用基本类型而非包装类
 *    - 及时释放不需要的数据结构
 *    - 使用对象池技术重用对象
 * <p>
 * 2. 计算优化：
 *    - 预计算幂次数组避免重复计算
 *    - 使用位运算替代模运算（如果MOD是2的幂次）
 *    - 缓存常用计算结果
 * <p>
 * 3. 算法优化：
 *    - 使用双哈希技术降低冲突概率
 *    - 针对特定数据分布优化参数选择
 *    - 使用分治策略处理超大规模数据
 * <p>
 * 4. 并行优化：
 *    - 将字符串分割后并行处理哈希计算
 *    - 使用多线程处理不同的模式串
 *    - 利用GPU加速哈希计算
 */

/**
 * 工程实践建议
 * <p>
 * 1. 错误处理：
 *    - 检查输入参数的合法性
 *    - 处理边界情况和异常输入
 *    - 提供有意义的错误信息
 * <p>
 * 2. 测试策略：
 *    - 单元测试覆盖各种边界情况
 *    - 性能测试使用真实数据规模
 *    - 回归测试确保算法稳定性
 * <p>
 * 3. 文档化：
 *    - 提供清晰的API文档
 *    - 说明算法的时间空间复杂度
 *    - 提供使用示例和最佳实践
 * <p>
 * 4. 可维护性：
 *    - 模块化设计便于扩展
 *    - 遵循编码规范提高可读性
 *    - 使用设计模式提高代码质量
 */

// 主函数
int main() {
    Code16_AdvancedStringHash::demo();
    return 0;
}