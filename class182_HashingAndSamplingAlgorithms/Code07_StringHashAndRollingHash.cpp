/**
 * 字符串哈希与滚动哈希算法实现 - C++版本
 * 
 * 本文件包含字符串哈希和滚动哈希的高级实现，包括：
 * - Rabin-Karp字符串匹配算法
 * - 滚动哈希技术
 * - 字符串哈希函数设计
 * - 哈希冲突处理策略
 * - 多哈希技术
 * 
 * 这些算法在字符串匹配、文本搜索、数据去重等领域有重要应用
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <set>
#include <algorithm>
#include <chrono>
#include <cmath>
#include <functional>
#include <random>
#include <stdexcept>
#include <utility>
#include <exception>

using namespace std;

/**
 * Rabin-Karp字符串匹配算法
 * 应用场景：文本编辑器、搜索引擎、DNA序列匹配
 * 
 * 算法原理：
 * 1. 使用滚动哈希计算模式串和文本串的哈希值
 * 2. 通过比较哈希值快速排除不匹配的位置
 * 3. 当哈希值匹配时进行精确比较
 * 
 * 时间复杂度：平均O(n+m)，最坏O(nm)
 * 空间复杂度：O(1)
 */
class RabinKarp {
public:
    static const int PRIME = 101; // 大质数
    static const int BASE = 256;  // 字符集大小
    
    /**
     * 字符串匹配
     */
    static vector<int> search(const string& text, const string& pattern) {
        vector<int> result;
        int n = text.length();
        int m = pattern.length();
        
        if (m == 0 || n < m) {
            return result;
        }
        
        // 计算模式串哈希值和第一个窗口哈希值
        long long patternHash = 0;
        long long textHash = 0;
        long long h = 1;
        
        // 计算h = BASE^(m-1) % PRIME
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % PRIME;
        }
        
        // 计算模式串和第一个窗口的哈希值
        for (int i = 0; i < m; i++) {
            patternHash = (BASE * patternHash + pattern[i]) % PRIME;
            textHash = (BASE * textHash + text[i]) % PRIME;
        }
        
        // 滑动窗口
        for (int i = 0; i <= n - m; i++) {
            // 检查哈希值是否匹配
            if (patternHash == textHash) {
                // 哈希值匹配，进行精确比较
                bool match = true;
                for (int j = 0; j < m; j++) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.push_back(i);
                }
            }
            
            // 计算下一个窗口的哈希值
            if (i < n - m) {
                textHash = (BASE * (textHash - text[i] * h) + text[i + m]) % PRIME;
                
                // 处理负哈希值
                if (textHash < 0) {
                    textHash += PRIME;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 多模式匹配版本
     */
    static unordered_map<string, vector<int>> searchMultiple(const string& text, const vector<string>& patterns) {
        unordered_map<string, vector<int>> result;
        
        for (const auto& pattern : patterns) {
            result[pattern] = search(text, pattern);
        }
        
        return result;
    }
    
    /**
     * 带通配符的字符串匹配
     */
    static vector<int> searchWithWildcard(const string& text, const string& pattern, char wildcard) {
        vector<int> result;
        int n = text.length();
        int m = pattern.length();
        
        if (m == 0 || n < m) {
            return result;
        }
        
        // 计算模式串中非通配符部分的哈希值
        long long patternHash = 0;
        long long textHash = 0;
        long long h = 1;
        int wildcardCount = 0;
        
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % PRIME;
        }
        
        // 计算模式串哈希值（忽略通配符）
        for (int i = 0; i < m; i++) {
            if (pattern[i] != wildcard) {
                patternHash = (BASE * patternHash + pattern[i]) % PRIME;
            } else {
                wildcardCount++;
            }
        }
        
        // 计算第一个窗口的哈希值（忽略通配符位置）
        for (int i = 0; i < m; i++) {
            if (pattern[i] != wildcard) {
                textHash = (BASE * textHash + text[i]) % PRIME;
            }
        }
        
        // 滑动窗口
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                // 哈希值匹配，进行精确比较（只比较非通配符位置）
                bool match = true;
                for (int j = 0; j < m; j++) {
                    if (pattern[j] != wildcard && 
                        text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.push_back(i);
                }
            }
            
            // 计算下一个窗口的哈希值
            if (i < n - m) {
                // 移除前一个字符的贡献（如果是非通配符位置）
                if (pattern[0] != wildcard) {
                    textHash = (BASE * (textHash - text[i] * h) + text[i + m]) % PRIME;
                } else {
                    // 如果是通配符位置，重新计算整个窗口的哈希值
                    textHash = 0;
                    for (int j = 1; j <= m; j++) {
                        if (pattern[j] != wildcard) {
                            textHash = (BASE * textHash + text[i + j]) % PRIME;
                        }
                    }
                }
                
                if (textHash < 0) {
                    textHash += PRIME;
                }
            }
        }
        
        return result;
    }
};

/**
 * 滚动哈希技术实现
 * 应用场景：字符串去重、最长重复子串、循环检测
 * 
 * 算法原理：
 * 1. 使用多项式哈希函数
 * 2. 支持O(1)时间复杂度的窗口滑动
 * 3. 支持多哈希减少冲突概率
 * 
 * 时间复杂度：O(n) 构建所有子串哈希
 * 空间复杂度：O(n)
 */
class RollingHash {
private:
    vector<long long> hash;
    vector<long long> power;
    int base;
    long long mod;
    
public:
    RollingHash(const string& s, int b, long long m) : base(b), mod(m) {
        int n = s.length();
        hash.resize(n + 1);
        power.resize(n + 1);
        
        power[0] = 1;
        for (int i = 1; i <= n; i++) {
            hash[i] = (hash[i - 1] * base + s[i - 1]) % mod;
            power[i] = (power[i - 1] * base) % mod;
        }
    }
    
    /**
     * 获取子串[l, r]的哈希值
     */
    long long getHash(int l, int r) {
        long long result = (hash[r + 1] - hash[l] * power[r - l + 1]) % mod;
        if (result < 0) {
            result += mod;
        }
        return result;
    }
    
    /**
     * 查找最长重复子串
     */
    string longestRepeatedSubstring(const string& s) {
        int n = s.length();
        int left = 1, right = n;
        string result = "";
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            unordered_map<long long, int> map;
            bool found = false;
            
            for (int i = 0; i <= n - mid; i++) {
                long long h = getHash(i, i + mid - 1);
                if (map.find(h) != map.end()) {
                    int prev = map[h];
                    // 检查是否真的是重复子串（防止哈希冲突）
                    if (s.substr(prev, mid) == s.substr(i, mid)) {
                        found = true;
                        result = s.substr(i, mid);
                        break;
                    }
                } else {
                    map[h] = i;
                }
            }
            
            if (found) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 计算不同子串的数量
     */
    int countDistinctSubstrings(const string& s) {
        int n = s.length();
        unordered_set<long long> set;
        
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                long long h = getHash(i, i + len - 1);
                set.insert(h);
            }
        }
        
        return set.size();
    }
    
    /**
     * 查找最长回文子串
     */
    string longestPalindrome(const string& s) {
        if (s.empty()) return "";
        
        int n = s.length();
        RollingHash forward(s, base, mod);
        string reversed = s;
        reverse(reversed.begin(), reversed.end());
        RollingHash backward(reversed, base, mod);
        
        int maxLen = 0;
        int start = 0;
        
        for (int i = 0; i < n; i++) {
            // 奇数长度回文
            int len1 = expandAroundCenter(forward, backward, i, i, n);
            // 偶数长度回文
            int len2 = expandAroundCenter(forward, backward, i, i + 1, n);
            
            int len = max(len1, len2);
            if (len > maxLen) {
                maxLen = len;
                start = i - (len - 1) / 2;
            }
        }
        
        return s.substr(start, maxLen);
    }
    
private:
    int expandAroundCenter(RollingHash& forward, RollingHash& backward, 
                          int left, int right, int n) {
        while (left >= 0 && right < n) {
            // 使用哈希值检查回文
            long long forwardHash = forward.getHash(left, right);
            long long backwardHash = backward.getHash(n - right - 1, n - left - 1);
            
            if (forwardHash != backwardHash) {
                break;
            }
            
            left--;
            right++;
        }
        
        return right - left - 1;
    }
};

/**
 * 多哈希技术实现
 * 应用场景：需要高精度哈希匹配的场景
 * 
 * 算法原理：
 * 1. 使用多个不同的哈希函数
 * 2. 只有当所有哈希值都匹配时才认为匹配
 * 3. 显著降低哈希冲突概率
 * 
 * 时间复杂度：O(kn)，其中k是哈希函数数量
 * 空间复杂度：O(kn)
 */
class MultiHash {
private:
    vector<RollingHash> hashes;
    int k; // 哈希函数数量
    
public:
    MultiHash(const string& s, int k_count) : k(k_count) {
        // 使用不同的基数和模数
        vector<int> bases = {131, 13331, 131313, 1313131, 13131313};
        vector<long long> mods = {1000000007LL, 1000000009LL, 1000000021LL, 1000000033LL, 1000000087LL};
        
        for (int i = 0; i < k; i++) {
            hashes.emplace_back(s, bases[i], mods[i]);
        }
    }
    
    /**
     * 获取子串的多重哈希值
     */
    vector<long long> getMultiHash(int l, int r) {
        vector<long long> result(k);
        for (int i = 0; i < k; i++) {
            result[i] = hashes[i].getHash(l, r);
        }
        return result;
    }
    
    /**
     * 比较两个子串的多重哈希值
     */
    bool equals(int l1, int r1, int l2, int r2) {
        if (r1 - l1 != r2 - l2) return false;
        
        for (int i = 0; i < k; i++) {
            if (hashes[i].getHash(l1, r1) != hashes[i].getHash(l2, r2)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 查找所有重复子串
     */
    unordered_map<string, vector<int>> findAllRepeatedSubstrings(const string& s, int minLen) {
        int n = s.length();
        unordered_map<string, vector<int>> result;
        
        // 使用多重哈希减少冲突
        for (int len = minLen; len <= n; len++) {
            unordered_map<string, int> seen;
            
            for (int i = 0; i <= n - len; i++) {
                string substr = s.substr(i, len);
                
                if (seen.find(substr) != seen.end()) {
                    int prev = seen[substr];
                    if (result.find(substr) == result.end()) {
                        result[substr] = {prev};
                    }
                    result[substr].push_back(i);
                } else {
                    seen[substr] = i;
                }
            }
        }
        
        return result;
    }
};

/**
 * 字符串哈希的性能分析工具
 */
class HashPerformanceAnalyzer {
public:
    /**
     * 分析哈希函数的质量
     */
    static void analyzeHashFunction(const vector<string>& strings, int base, long long mod) {
        unordered_set<long long> hashes;
        int collisions = 0;
        
        for (const auto& s : strings) {
            long long hash = 0;
            for (char c : s) {
                hash = (hash * base + c) % mod;
            }
            
            if (!hashes.insert(hash).second) {
                collisions++;
            }
        }
        
        double collisionRate = static_cast<double>(collisions) / strings.size();
        printf("哈希函数分析: 基数=%d, 模数=%lld, 冲突率=%.4f%%\n", 
               base, mod, collisionRate * 100);
    }
    
    /**
     * 比较不同哈希函数的性能
     */
    static void compareHashFunctions(const string& text, const string& pattern) {
        auto startTime = chrono::high_resolution_clock::now();
        
        // Rabin-Karp算法
        vector<int> rkResult = RabinKarp::search(text, pattern);
        auto endTime = chrono::high_resolution_clock::now();
        auto rkDuration = chrono::duration_cast<chrono::nanoseconds>(endTime - startTime);
        
        printf("Rabin-Karp算法: %lld ns, 匹配位置: ", rkDuration.count());
        for (int pos : rkResult) {
            printf("%d ", pos);
        }
        printf("\n");
        
        // 暴力匹配算法
        startTime = chrono::high_resolution_clock::now();
        vector<int> bfResult = bruteForceSearch(text, pattern);
        endTime = chrono::high_resolution_clock::now();
        auto bfDuration = chrono::duration_cast<chrono::nanoseconds>(endTime - startTime);
        
        printf("暴力匹配算法: %lld ns, 匹配位置: ", bfDuration.count());
        for (int pos : bfResult) {
            printf("%d ", pos);
        }
        printf("\n");
    }
    
private:
    static vector<int> bruteForceSearch(const string& text, const string& pattern) {
        vector<int> result;
        int n = text.length();
        int m = pattern.length();
        
        for (int i = 0; i <= n - m; i++) {
            bool match = true;
            for (int j = 0; j < m; j++) {
                if (text[i + j] != pattern[j]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                result.push_back(i);
            }
        }
        
        return result;
    }
};

/**
 * 单元测试函数
 */
void testStringHashAndRollingHash() {
    cout << "=== 字符串哈希与滚动哈希算法测试 ===" << endl << endl;
    
    // 测试Rabin-Karp算法
    cout << "1. Rabin-Karp字符串匹配算法测试:" << endl;
    string text = "ABABDABACDABABCABAB";
    string pattern = "ABABCABAB";
    vector<int> positions = RabinKarp::search(text, pattern);
    cout << "文本: " << text << endl;
    cout << "模式: " << pattern << endl;
    cout << "匹配位置: ";
    for (int pos : positions) {
        cout << pos << " ";
    }
    cout << endl;
    
    // 测试滚动哈希
    cout << endl << "2. 滚动哈希技术测试:" << endl;
    RollingHash rh("banana", 131, 1000000007);
    cout << "字符串: banana" << endl;
    cout << "最长重复子串: " << rh.longestRepeatedSubstring("banana") << endl;
    cout << "不同子串数量: " << rh.countDistinctSubstrings("banana") << endl;
    
    // 测试多哈希技术
    cout << endl << "3. 多哈希技术测试:" << endl;
    MultiHash mh("mississippi", 3);
    cout << "字符串: mississippi" << endl;
    auto repeated = mh.findAllRepeatedSubstrings("mississippi", 2);
    cout << "重复子串: " << endl;
    for (const auto& pair : repeated) {
        cout << pair.first << ": ";
        for (int pos : pair.second) {
            cout << pos << " ";
        }
        cout << endl;
    }
    
    // 测试带通配符的匹配
    cout << endl << "4. 带通配符的字符串匹配测试:" << endl;
    string text2 = "AABAACAADAABAAABAA";
    string pattern2 = "A*BA";
    vector<int> wildcardPositions = RabinKarp::searchWithWildcard(text2, pattern2, '*');
    cout << "文本: " << text2 << endl;
    cout << "模式: " << pattern2 << endl;
    cout << "匹配位置: ";
    for (int pos : wildcardPositions) {
        cout << pos << " ";
    }
    cout << endl;
    
    // 性能分析
    cout << endl << "5. 哈希函数性能分析:" << endl;
    vector<string> testStrings = {"hello", "world", "test", "string", "hash"};
    HashPerformanceAnalyzer::analyzeHashFunction(testStrings, 131, 1000000007);
    HashPerformanceAnalyzer::analyzeHashFunction(testStrings, 13331, 1000000009);
    
    cout << endl << "=== 算法复杂度分析 ===" << endl;
    cout << "1. Rabin-Karp算法: 平均O(n+m)，最坏O(nm)时间，O(1)空间" << endl;
    cout << "2. 滚动哈希: O(n)构建时间，O(1)查询时间，O(n)空间" << endl;
    cout << "3. 多哈希技术: O(kn)时间，O(kn)空间，k为哈希函数数量" << endl;
    
    cout << endl << "=== C++工程化应用场景 ===" << endl;
    cout << "1. 文本编辑器: 快速查找和替换" << endl;
    cout << "2. 搜索引擎: 网页内容索引和匹配" << endl;
    cout << "3. 生物信息学: DNA序列分析和比对" << endl;
    cout << "4. 数据去重: 检测重复文件和内容" << endl;
    
    cout << endl << "=== C++性能优化策略 ===" << endl;
    cout << "1. 内存布局优化: 使用连续内存提高缓存命中率" << endl;
    cout << "2. 模板元编程: 编译时优化哈希函数选择" << endl;
    cout << "3. SIMD指令: 使用向量化指令加速哈希计算" << endl;
    cout << "4. 预计算优化: 提前计算常用哈希值减少运行时开销" << endl;
}

int main() {
    testStringHashAndRollingHash();
    return 0;
}