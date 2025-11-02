/**
 * Rabin-Karp滚动哈希字符串匹配算法实现
 * 时间复杂度：
 *   - 最好情况: O(n+m)，其中n是文本长度，m是模式长度
 *   - 最坏情况: O(n*m)，当有很多哈希冲突时
 *   - 平均情况: O(n+m)
 * 空间复杂度：O(1) - 基本操作，O(n) - 存储所有匹配
 */

#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
using namespace std;

class RabinKarpAlgorithm {
private:
    static const long long BASE = 256;  // 字符集大小
    static const long long MOD = 1000000007;  // 大素数，防止溢出

    /**
     * 计算base^exponent % mod的值，使用快速幂算法
     */
    static long long power(long long base, long long exponent, long long mod) {
        long long result = 1;
        base = base % mod;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent /= 2;
        }
        return result;
    }

public:
    /**
     * Rabin-Karp字符串匹配算法
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     * @throws invalid_argument 如果输入参数为nullptr
     */
    static int search(const string& text, const string& pattern) {
        // 边界条件检查
        if (pattern.empty()) {
            return 0;  // 空模式串匹配任何位置的开始
        }
        if (text.empty()) {
            return -1;  // 空文本串不可能匹配非空模式串
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (n < m) {
            return -1;  // 文本串比模式串短，不可能匹配
        }
        
        // 计算pattern的哈希值和text前m个字符的哈希值
        long long patternHash = 0;
        long long textHash = 0;
        long long highestPow = power(BASE, m - 1, MOD);  // BASE^(m-1) % MOD
        
        // 计算初始哈希值
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern[i]) % MOD;
            textHash = (textHash * BASE + text[i]) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            // 如果哈希值相同，进行精确比较以避免哈希冲突
            if (patternHash == textHash) {
                bool match = true;
                for (int j = 0; j < m; j++) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;  // 找到匹配
                }
            }
            
            // 更新滑动窗口的哈希值
            if (i < n - m) {
                // 移除最左边的字符
                textHash = (textHash - highestPow * text[i] % MOD + MOD) % MOD;
                // 添加新的右边字符
                textHash = (textHash * BASE + text[i + m]) % MOD;
            }
        }
        
        return -1;  // 未找到匹配
    }
    
    /**
     * 查找模式串在文本串中所有出现的位置
     * @param text 文本串
     * @param pattern 模式串
     * @return 包含所有匹配位置的vector
     */
    static vector<int> searchAll(const string& text, const string& pattern) {
        vector<int> matches;
        
        // 边界条件检查
        if (pattern.empty()) {
            // 空模式串匹配每个位置的开始
            for (int i = 0; i <= (int)text.length(); i++) {
                matches.push_back(i);
            }
            return matches;
        }
        if (text.empty()) {
            return matches;  // 空文本串不可能匹配非空模式串
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (n < m) {
            return matches;  // 无匹配
        }
        
        // 计算pattern的哈希值和text前m个字符的哈希值
        long long patternHash = 0;
        long long textHash = 0;
        long long highestPow = power(BASE, m - 1, MOD);  // BASE^(m-1) % MOD
        
        // 计算初始哈希值
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern[i]) % MOD;
            textHash = (textHash * BASE + text[i]) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            // 如果哈希值相同，进行精确比较以避免哈希冲突
            if (patternHash == textHash) {
                bool match = true;
                for (int j = 0; j < m; j++) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    matches.push_back(i);  // 记录匹配位置
                }
            }
            
            // 更新滑动窗口的哈希值
            if (i < n - m) {
                // 移除最左边的字符
                textHash = (textHash - highestPow * text[i] % MOD + MOD) % MOD;
                // 添加新的右边字符
                textHash = (textHash * BASE + text[i + m]) % MOD;
            }
        }
        
        return matches;
    }
    
    /**
     * 双哈希版本的Rabin-Karp算法，用于减少哈希冲突
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     */
    static int searchDoubleHash(const string& text, const string& pattern) {
        // 边界条件检查
        if (pattern.empty()) {
            return 0;  // 空模式串匹配任何位置的开始
        }
        if (text.empty()) {
            return -1;  // 空文本串不可能匹配非空模式串
        }
        
        int n = text.length();
        int m = pattern.length();
        
        if (n < m) {
            return -1;  // 文本串比模式串短，不可能匹配
        }
        
        // 使用两个不同的哈希参数
        long long BASE1 = 256, MOD1 = 1000000007;
        long long BASE2 = 263, MOD2 = 1000000009;
        
        long long patternHash1 = 0, textHash1 = 0;
        long long patternHash2 = 0, textHash2 = 0;
        long long highestPow1 = power(BASE1, m - 1, MOD1);
        long long highestPow2 = power(BASE2, m - 1, MOD2);
        
        // 计算初始哈希值
        for (int i = 0; i < m; i++) {
            patternHash1 = (patternHash1 * BASE1 + pattern[i]) % MOD1;
            textHash1 = (textHash1 * BASE1 + text[i]) % MOD1;
            
            patternHash2 = (patternHash2 * BASE2 + pattern[i]) % MOD2;
            textHash2 = (textHash2 * BASE2 + text[i]) % MOD2;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            // 双重哈希都相等时才进行精确比较
            if (patternHash1 == textHash1 && patternHash2 == textHash2) {
                bool match = true;
                for (int j = 0; j < m; j++) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;
                }
            }
            
            // 更新哈希值
            if (i < n - m) {
                // 哈希1更新
                textHash1 = (textHash1 - highestPow1 * text[i] % MOD1 + MOD1) % MOD1;
                textHash1 = (textHash1 * BASE1 + text[i + m]) % MOD1;
                
                // 哈希2更新
                textHash2 = (textHash2 - highestPow2 * text[i] % MOD2 + MOD2) % MOD2;
                textHash2 = (textHash2 * BASE2 + text[i + m]) % MOD2;
            }
        }
        
        return -1;
    }
};

int main() {
    // 测试用例1：基本匹配
    string text1 = "hello world";
    string pattern1 = "world";
    cout << "测试1 - 查找'world'在'hello world'中的位置: " << RabinKarpAlgorithm::search(text1, pattern1) << endl;  // 应该是6
    
    // 测试用例2：多次匹配
    string text2 = "abababa";
    string pattern2 = "aba";
    vector<int> results2 = RabinKarpAlgorithm::searchAll(text2, pattern2);
    cout << "测试2 - 查找所有'aba'在'abababa'中的位置: ";
    for (int pos : results2) {
        cout << pos << " ";  // 应该是0 2 4
    }
    cout << endl;
    
    // 测试用例3：无匹配
    string text3 = "hello";
    string pattern3 = "world";
    cout << "测试3 - 查找'world'在'hello'中的位置: " << RabinKarpAlgorithm::search(text3, pattern3) << endl;  // 应该是-1
    
    // 测试用例4：边界情况
    string text4 = "test";
    string pattern4 = "";
    vector<int> results4 = RabinKarpAlgorithm::searchAll(text4, pattern4);
    cout << "测试4 - 查找空串在'test'中的位置: ";
    for (int pos : results4) {
        cout << pos << " ";  // 应该是0 1 2 3 4
    }
    cout << endl;
    
    // 测试用例5：双哈希版本
    string text5 = "a!b@c#d$e%";
    string pattern5 = "c#d";
    cout << "测试5 - 双哈希版本查找'c#d'的位置: " << RabinKarpAlgorithm::searchDoubleHash(text5, pattern5) << endl;  // 应该是4
    
    return 0;
}