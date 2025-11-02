#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <unordered_map>
#include <climits>
#include <cmath>
#include <memory>
#include <functional>

using namespace std;

/**
 * 高级Manacher算法题目实现 - C++版本
 * 包含更多复杂的回文串处理问题和Z函数应用
 * 
 * 本文件实现了以下高级题目：
 * 1. LeetCode 336. 回文对
 * 2. LeetCode 131. 分割回文串
 * 3. LeetCode 132. 分割回文串 II
 * 4. 洛谷 P1659 [国家集训队]拉拉队排练
 * 5. 洛谷 P4555 [国家集训队]最长双回文串
 * 6. SPOJ PALIN - The Next Palindrome
 * 7. HackerRank Build a Palindrome
 * 8. AtCoder ABC141E - Who Says a Pun?
 * 
 * 时间复杂度分析：O(n) 到 O(n²) 不等，取决于具体问题
 * 空间复杂度分析：O(n) 到 O(n²) 不等，取决于具体问题
 */

class AdvancedManacherCPP {
private:
    // 字典树节点
    struct TrieNode {
        unordered_map<char, shared_ptr<TrieNode>> children;
        int wordIndex;
        
        TrieNode() : wordIndex(-1) {}
    };
    
    shared_ptr<TrieNode> root;
    
public:
    AdvancedManacherCPP() {
        root = make_shared<TrieNode>();
    }
    
    /**
     * LeetCode 336. 回文对
     * 给定一组互不相同的单词，找出所有不同的索引对(i, j)，使得两个单词拼接起来是回文串。
     * 
     * 解题思路：
     * 1. 使用字典树存储所有单词及其反转
     * 2. 对于每个单词，检查其前缀和后缀是否为回文
     * 3. 在字典树中查找剩余部分的匹配
     * 
     * 时间复杂度：O(n * k²)，其中n是单词数量，k是单词平均长度
     * 空间复杂度：O(n * k)
     * 
     * @param words 单词列表
     * @return 回文对列表
     */
    vector<vector<int>> palindromePairs(vector<string>& words) {
        vector<vector<int>> result;
        if (words.size() < 2) return result;
        
        // 构建字典树
        for (int i = 0; i < words.size(); i++) {
            insertWord(words[i], i);
        }
        
        // 查找回文对
        for (int i = 0; i < words.size(); i++) {
            string word = words[i];
            shared_ptr<TrieNode> node = root;
            
            // 检查整个单词是否在字典树中
            for (int j = 0; j < word.length(); j++) {
                char c = word[j];
                if (node->children.find(c) == node->children.end()) {
                    break;
                }
                node = node->children[c];
                
                // 如果当前节点是某个单词的结尾，且剩余部分是回文
                if (node->wordIndex != -1 && node->wordIndex != i && 
                    isPalindrome(word, j + 1, word.length() - 1)) {
                    result.push_back({i, node->wordIndex});
                }
            }
            
            // 检查单词的反转
            string reversedWord = word;
            reverse(reversedWord.begin(), reversedWord.end());
            shared_ptr<TrieNode> revNode = root;
            for (int j = 0; j < reversedWord.length(); j++) {
                char c = reversedWord[j];
                if (revNode->children.find(c) == revNode->children.end()) {
                    break;
                }
                revNode = revNode->children[c];
                
                // 如果当前节点是某个单词的结尾，且剩余部分是回文
                if (revNode->wordIndex != -1 && revNode->wordIndex != i && 
                    isPalindrome(reversedWord, j + 1, reversedWord.length() - 1)) {
                    result.push_back({revNode->wordIndex, i});
                }
            }
        }
        
        return result;
    }
    
private:
    /**
     * 向字典树中插入单词
     */
    void insertWord(const string& word, int index) {
        shared_ptr<TrieNode> node = root;
        for (char c : word) {
            if (node->children.find(c) == node->children.end()) {
                node->children[c] = make_shared<TrieNode>();
            }
            node = node->children[c];
        }
        node->wordIndex = index;
    }
    
    /**
     * 判断子串是否为回文
     */
    bool isPalindrome(const string& s, int left, int right) {
        while (left < right) {
            if (s[left] != s[right]) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    
public:
    /**
     * LeetCode 131. 分割回文串
     * 给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。返回所有可能的分割方案。
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理回文信息
     * 2. 使用回溯法枚举所有分割方案
     * 3. 利用预处理信息快速判断子串是否为回文
     * 
     * 时间复杂度：O(n * 2ⁿ)
     * 空间复杂度：O(n²)
     * 
     * @param s 输入字符串
     * @return 所有分割方案
     */
    vector<vector<string>> partition(string s) {
        vector<vector<string>> result;
        if (s.empty()) return result;
        
        // 预处理回文信息
        vector<vector<bool>> isPalindrome = preprocessPalindrome(s);
        
        // 回溯法枚举所有分割方案
        vector<string> current;
        backtrack(s, 0, current, result, isPalindrome);
        
        return result;
    }
    
private:
    /**
     * 预处理回文信息
     */
    vector<vector<bool>> preprocessPalindrome(const string& s) {
        int n = s.length();
        vector<vector<bool>> dp(n, vector<bool>(n, false));
        
        // 单个字符都是回文
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }
        
        // 两个字符的情况
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = (s[i] == s[i + 1]);
        }
        
        // 长度大于2的情况
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = (s[i] == s[j] && dp[i + 1][j - 1]);
            }
        }
        
        return dp;
    }
    
    /**
     * 回溯法实现
     */
    void backtrack(const string& s, int start, vector<string>& current, 
                   vector<vector<string>>& result, const vector<vector<bool>>& isPalindrome) {
        if (start == s.length()) {
            result.push_back(current);
            return;
        }
        
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome[start][end]) {
                current.push_back(s.substr(start, end - start + 1));
                backtrack(s, end + 1, current, result, isPalindrome);
                current.pop_back();
            }
        }
    }
    
public:
    /**
     * LeetCode 132. 分割回文串 II
     * 给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数。
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理回文信息
     * 2. 使用动态规划计算最少分割次数
     * 3. 优化状态转移过程
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     * 
     * @param s 输入字符串
     * @return 最少分割次数
     */
    int minCut(string s) {
        int n = s.length();
        if (n <= 1) return 0;
        
        // 预处理回文信息
        vector<vector<bool>> isPalindrome = preprocessPalindrome(s);
        
        // 动态规划计算最少分割次数
        vector<int> dp(n, INT_MAX);
        
        for (int i = 0; i < n; i++) {
            if (isPalindrome[0][i]) {
                dp[i] = 0; // 整个子串是回文，不需要分割
            } else {
                for (int j = 0; j < i; j++) {
                    if (isPalindrome[j + 1][i]) {
                        dp[i] = min(dp[i], dp[j] + 1);
                    }
                }
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 洛谷 P1659 [国家集训队]拉拉队排练
     * 求字符串中所有奇数长度回文串的长度乘积。
     * 
     * 解题思路：
     * 1. 使用Manacher算法找到所有奇数长度回文串
     * 2. 统计每个长度的回文串数量
     * 3. 计算前k大的长度乘积
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param s 输入字符串
     * @param k 前k大的长度
     * @return 长度乘积（取模）
     */
    long long longestPalindromeProduct(const string& s, int k) {
        int n = s.length();
        if (n == 0) return 0;
        
        // 使用Manacher算法计算回文半径
        vector<int> radius = manacherOdd(s);
        
        // 统计每个长度的回文串数量
        vector<int> count(n + 1, 0);
        for (int i = 0; i < n; i++) {
            int len = 2 * radius[i] + 1;
            count[len]++;
        }
        
        // 计算前k大的长度乘积
        long long product = 1;
        long long mod = 1000000007;
        int remaining = k;
        
        for (int len = n; len >= 1 && remaining > 0; len -= 2) {
            if (count[len] > 0) {
                int take = min(count[len], remaining);
                product = (product * fastPower(len, take, mod)) % mod;
                remaining -= take;
            }
        }
        
        return product;
    }
    
private:
    /**
     * Manacher算法计算奇回文串
     */
    vector<int> manacherOdd(const string& s) {
        int n = s.length();
        vector<int> radius(n, 0);
        int l = 0, r = -1;
        
        for (int i = 0; i < n; i++) {
            int k = (i > r) ? 1 : min(radius[l + r - i], r - i + 1);
            
            while (i - k >= 0 && i + k < n && s[i - k] == s[i + k]) {
                k++;
            }
            
            radius[i] = k - 1;
            
            if (i + k - 1 > r) {
                l = i - k + 1;
                r = i + k - 1;
            }
        }
        
        return radius;
    }
    
    /**
     * 快速幂计算
     */
    long long fastPower(long long base, int exponent, long long mod) {
        long long result = 1;
        while (exponent > 0) {
            if (exponent & 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent >>= 1;
        }
        return result;
    }
    
public:
    /**
     * 洛谷 P4555 [国家集训队]最长双回文串
     * 输入字符串s，求s的最长双回文子串t的长度（双回文子串就是可以分成两个回文串的字符串）
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理
     * 2. 计算每个位置作为分割点的左右最长回文
     * 3. 枚举所有分割点求最大值
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param s 输入字符串
     * @return 最长双回文串长度
     */
    int longestDoublePalindrome(const string& s) {
        int n = s.length();
        if (n < 2) return 0;
        
        // 预处理字符串
        string processed = preprocessString(s);
        int len = processed.length();
        vector<int> p(len, 0);
        
        int center = 0, right = 0;
        for (int i = 1; i < len - 1; i++) {
            int mirror = 2 * center - i;
            
            if (i < right) {
                p[i] = min(right - i, p[mirror]);
            }
            
            while (i + p[i] + 1 < len && i - p[i] - 1 >= 0 && 
                   processed[i + p[i] + 1] == processed[i - p[i] - 1]) {
                p[i]++;
            }
            
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }
        }
        
        // 计算每个位置的最长回文长度
        vector<int> leftMax(n, 0);
        vector<int> rightMax(n, 0);
        
        // 从左到右计算每个位置的最长回文前缀
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            int pos = 2 * i + 1;
            maxLen = max(maxLen, p[pos]);
            leftMax[i] = maxLen;
        }
        
        // 从右到左计算每个位置的最长回文后缀
        maxLen = 0;
        for (int i = n - 1; i >= 0; i--) {
            int pos = 2 * i + 1;
            maxLen = max(maxLen, p[pos]);
            rightMax[i] = maxLen;
        }
        
        // 枚举分割点，计算最长双回文串
        int result = 0;
        for (int i = 0; i < n - 1; i++) {
            result = max(result, leftMax[i] + rightMax[i + 1]);
        }
        
        return result;
    }
    
private:
    /**
     * 预处理字符串，插入特殊字符
     */
    string preprocessString(const string& s) {
        if (s.empty()) return "^$";
        
        string result = "^";
        for (char c : s) {
            result += "#";
            result += c;
        }
        result += "#$";
        
        return result;
    }
    
public:
    /**
     * SPOJ PALIN - The Next Palindrome
     * 给定一个整数，找到大于该数的最小回文数。
     * 
     * 解题思路：
     * 1. 将数字转换为字符串
     * 2. 构造下一个回文数
     * 3. 处理进位等特殊情况
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param numStr 数字字符串
     * @return 下一个回文数
     */
    string nextPalindrome(const string& numStr) {
        if (numStr.empty()) return "1";
        
        string num = numStr;
        int n = num.length();
        
        // 处理全9的情况
        if (all_of(num.begin(), num.end(), [](char c) { return c == '9'; })) {
            return "1" + string(n - 1, '0') + "1";
        }
        
        // 构造下一个回文数
        int mid = n / 2;
        bool leftSmaller = false;
        int i = mid - 1;
        int j = (n % 2 == 0) ? mid : mid + 1;
        
        // 跳过已经相同的部分
        while (i >= 0 && num[i] == num[j]) {
            i--;
            j++;
        }
        
        // 检查是否需要增加中间部分
        if (i < 0 || num[i] < num[j]) {
            leftSmaller = true;
        }
        
        // 复制左半部分到右半部分
        while (i >= 0) {
            num[j] = num[i];
            i--;
            j++;
        }
        
        // 如果需要增加中间部分
        if (leftSmaller) {
            int carry = 1;
            i = mid - 1;
            
            if (n % 2 == 1) {
                int midNum = (num[mid] - '0') + carry;
                carry = midNum / 10;
                num[mid] = (midNum % 10) + '0';
                j = mid + 1;
            } else {
                j = mid;
            }
            
            // 处理进位
            while (i >= 0) {
                int digit = (num[i] - '0') + carry;
                carry = digit / 10;
                num[i] = (digit % 10) + '0';
                num[j] = num[i];
                i--;
                j++;
            }
        }
        
        return num;
    }
    
    /**
     * HackerRank Build a Palindrome
     * 给定两个字符串a和b，从a中取一个非空前缀，从b中取一个非空后缀，拼接成一个回文串，求最长的回文串长度。
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理两个字符串
     * 2. 枚举所有可能的前缀后缀组合
     * 3. 检查拼接后的字符串是否为回文
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * @param a 字符串a
     * @param b 字符串b
     * @return 最长回文串长度
     */
    int buildPalindrome(const string& a, const string& b) {
        int maxLen = 0;
        int n = a.length(), m = b.length();
        
        // 枚举所有可能的前缀后缀组合
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 从a取前缀，从b取后缀
                string prefix = a.substr(0, i + 1);
                string suffix = b.substr(m - j - 1);
                string combined = prefix + suffix;
                
                // 检查是否为回文
                if (isPalindrome(combined, 0, combined.length() - 1)) {
                    maxLen = max(maxLen, (int)combined.length());
                }
            }
        }
        
        return maxLen;
    }
    
    /**
     * AtCoder ABC141E - Who Says a Pun?
     * 给定一个长度为n的字符串s，找出两个不重叠的子串，使得它们相等且长度尽可能大。
     * 
     * 解题思路：
     * 1. 使用Z函数计算每个后缀的匹配情况
     * 2. 遍历所有可能的分割点
     * 3. 找到满足条件的最长子串
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * @param s 输入字符串
     * @return 最大长度
     */
    int whoSaysPun(const string& s) {
        int n = s.length();
        int maxLen = 0;
        
        // 对于每个可能的起始位置
        for (int i = 0; i < n; i++) {
            // 计算从位置i开始的Z函数
            string substr = s.substr(i);
            vector<int> z = zFunction(substr);
            
            // 查找不重叠的相同子串
            for (int j = 1; j < z.size(); j++) {
                if (z[j] > maxLen && j >= z[j]) {
                    maxLen = max(maxLen, z[j]);
                }
            }
        }
        
        return maxLen;
    }
    
private:
    /**
     * Z函数计算
     */
    vector<int> zFunction(const string& s) {
        int n = s.length();
        vector<int> z(n, 0);
        z[0] = n;
        
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = min(r - i + 1, z[i - l]);
            }
            
            while (i + z[i] < n && s[z[i]] == s[i + z[i]]) {
                z[i]++;
            }
            
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        
        return z;
    }
    
public:
    /**
     * 运行单元测试
     */
    void runUnitTests() {
        cout << "===== C++版本高级Manacher算法题目测试 =====" << endl;
        
        // 测试回文对
        cout << "\n1. 回文对测试:" << endl;
        vector<string> words = {"abcd", "dcba", "lls", "s", "sssll"};
        vector<vector<int>> result1 = palindromePairs(words);
        cout << "回文对结果: ";
        for (auto& pair : result1) {
            cout << "[" << pair[0] << "," << pair[1] << "] ";
        }
        cout << endl;
        
        // 测试分割回文串
        cout << "\n2. 分割回文串测试:" << endl;
        string s2 = "aab";
        vector<vector<string>> result2 = partition(s2);
        cout << "分割方案数量: " << result2.size() << endl;
        
        // 测试最少分割次数
        cout << "\n3. 最少分割次数测试:" << endl;
        string s3 = "aab";
        int result3 = minCut(s3);
        cout << "最少分割次数: " << result3 << endl;
        
        // 测试最长双回文串
        cout << "\n4. 最长双回文串测试:" << endl;
        string s4 = "baacaabbacabb";
        int result4 = longestDoublePalindrome(s4);
        cout << "最长双回文串长度: " << result4 << endl;
        
        // 测试下一个回文数
        cout << "\n5. 下一个回文数测试:" << endl;
        string num = "12345";
        string result5 = nextPalindrome(num);
        cout << "下一个回文数: " << result5 << endl;
        
        cout << "\n===== 测试完成 =====" << endl;
    }
};

int main() {
    AdvancedManacherCPP solver;
    solver.runUnitTests();
    return 0;
}