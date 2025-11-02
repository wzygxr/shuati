#include <iostream>
#include <string>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <chrono>

/**
 * Rabin-Karp滚动哈希字符串匹配算法的C++实现
 * 
 * Rabin-Karp算法是一种基于哈希的字符串搜索算法，由Richard M. Karp和Michael O. Rabin在1987年提出。
 * 该算法通过使用滚动哈希技术，使得可以在O(1)时间内更新滑动窗口的哈希值，从而实现高效的字符串匹配。
 * 
 * 时间复杂度：
 *   - 最好情况: O(n+m)，其中n是文本长度，m是模式长度
 *   - 最坏情况: O(n*m)，当有很多哈希冲突时
 *   - 平均情况: O(n+m)
 * 空间复杂度：O(1) - 基本操作，O(n) - 存储所有匹配
 * 
 * 应用场景：
 * - 字符串搜索
 * - 重复子串检测
 * - 多模式串匹配（通过适当扩展）
 * - 生物信息学中的DNA序列匹配
 */
class RabinKarpAlgorithm {
private:
    // 选择一个较大的素数作为模数，减少哈希冲突
    static const long long MOD = 1000000007;
    // 选择一个基数，通常使用字符集大小
    static const int BASE = 256;

public:
    /**
     * Rabin-Karp字符串匹配算法
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     */
    static int search(const std::string& text, const std::string& pattern) {
        if (text.empty() || pattern.empty()) {
            if (pattern.empty()) return 0; // 空模式串匹配任何位置的开始
            return -1; // 文本串为空但模式串不为空，无匹配
        }

        int n = text.length();
        int m = pattern.length();

        if (n < m) {
            return -1; // 文本串比模式串短，不可能匹配
        }

        // 计算BASE^(m-1) % MOD，用于滚动哈希中的高位计算
        long long highestPow = 1;
        for (int i = 0; i < m - 1; ++i) {
            highestPow = (highestPow * BASE) % MOD;
        }

        // 计算模式串的哈希值和文本串第一个窗口的哈希值
        long long patternHash = 0;
        long long textHash = 0;
        for (int i = 0; i < m; ++i) {
            patternHash = (patternHash * BASE + pattern[i]) % MOD;
            textHash = (textHash * BASE + text[i]) % MOD;
        }

        // 滑动窗口，比较哈希值
        for (int i = 0; i <= n - m; ++i) {
            // 如果哈希值匹配，进行字符比较确认
            if (patternHash == textHash) {
                bool match = true;
                for (int j = 0; j < m; ++j) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i; // 找到匹配
                }
            }

            // 如果还有下一个窗口，计算下一个窗口的哈希值
            if (i < n - m) {
                // 移除最高位字符的贡献
                textHash = (textHash - highestPow * text[i] % MOD + MOD) % MOD;
                // 添加新字符的贡献
                textHash = (textHash * BASE + text[i + m]) % MOD;
            }
        }

        return -1; // 未找到匹配
    }

    /**
     * 查找模式串在文本串中所有出现的位置
     * @param text 文本串
     * @param pattern 模式串
     * @return 包含所有匹配位置的向量
     */
    static std::vector<int> searchAll(const std::string& text, const std::string& pattern) {
        std::vector<int> positions;

        if (text.empty() || pattern.empty()) {
            if (pattern.empty()) {
                // 空模式串匹配每个位置的开始
                for (int i = 0; i <= static_cast<int>(text.length()); ++i) {
                    positions.push_back(i);
                }
            }
            return positions;
        }

        int n = text.length();
        int m = pattern.length();

        if (n < m) {
            return positions; // 无匹配
        }

        // 计算BASE^(m-1) % MOD
        long long highestPow = 1;
        for (int i = 0; i < m - 1; ++i) {
            highestPow = (highestPow * BASE) % MOD;
        }

        // 计算模式串的哈希值和文本串第一个窗口的哈希值
        long long patternHash = 0;
        long long textHash = 0;
        for (int i = 0; i < m; ++i) {
            patternHash = (patternHash * BASE + pattern[i]) % MOD;
            textHash = (textHash * BASE + text[i]) % MOD;
        }

        // 滑动窗口，比较哈希值
        for (int i = 0; i <= n - m; ++i) {
            // 如果哈希值匹配，进行字符比较确认
            if (patternHash == textHash) {
                bool match = true;
                for (int j = 0; j < m; ++j) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    positions.push_back(i); // 找到匹配，记录位置
                }
            }

            // 如果还有下一个窗口，计算下一个窗口的哈希值
            if (i < n - m) {
                // 移除最高位字符的贡献
                textHash = (textHash - highestPow * text[i] % MOD + MOD) % MOD;
                // 添加新字符的贡献
                textHash = (textHash * BASE + text[i + m]) % MOD;
            }
        }

        return positions;
    }

    /**
     * 计算模式串在文本串中出现的次数
     * @param text 文本串
     * @param pattern 模式串
     * @return 出现次数
     */
    static int countOccurrences(const std::string& text, const std::string& pattern) {
        return searchAll(text, pattern).size();
    }

    /**
     * 双哈希版本的Rabin-Karp算法，用于减少哈希冲突
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     */
    static int searchDoubleHash(const std::string& text, const std::string& pattern) {
        if (text.empty() || pattern.empty()) {
            if (pattern.empty()) return 0;
            return -1;
        }

        int n = text.length();
        int m = pattern.length();

        if (n < m) {
            return -1;
        }

        // 使用两个不同的模数和基数
        const long long MOD1 = 1000000007;
        const long long MOD2 = 1000000009;
        const int BASE1 = 256;
        const int BASE2 = 263;

        // 计算幂值
        long long highestPow1 = 1, highestPow2 = 1;
        for (int i = 0; i < m - 1; ++i) {
            highestPow1 = (highestPow1 * BASE1) % MOD1;
            highestPow2 = (highestPow2 * BASE2) % MOD2;
        }

        // 计算哈希值
        long long patternHash1 = 0, textHash1 = 0;
        long long patternHash2 = 0, textHash2 = 0;
        for (int i = 0; i < m; ++i) {
            patternHash1 = (patternHash1 * BASE1 + pattern[i]) % MOD1;
            textHash1 = (textHash1 * BASE1 + text[i]) % MOD1;

            patternHash2 = (patternHash2 * BASE2 + pattern[i]) % MOD2;
            textHash2 = (textHash2 * BASE2 + text[i]) % MOD2;
        }

        // 滑动窗口比较
        for (int i = 0; i <= n - m; ++i) {
            // 只有当两个哈希值都匹配时才进行字符比较
            if (patternHash1 == textHash1 && patternHash2 == textHash2) {
                bool match = true;
                for (int j = 0; j < m; ++j) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;
                }
            }

            if (i < n - m) {
                // 更新第一个哈希
                textHash1 = (textHash1 - highestPow1 * text[i] % MOD1 + MOD1) % MOD1;
                textHash1 = (textHash1 * BASE1 + text[i + m]) % MOD1;

                // 更新第二个哈希
                textHash2 = (textHash2 - highestPow2 * text[i] % MOD2 + MOD2) % MOD2;
                textHash2 = (textHash2 * BASE2 + text[i + m]) % MOD2;
            }
        }

        return -1;
    }

    /**
     * 查找文本中所有长度为length的重复子串（出现至少minOccurrences次）
     * @param text 文本串
     * @param length 子串长度
     * @param minOccurrences 最小出现次数
     * @return 包含重复子串及其位置列表的映射
     */
    static std::unordered_map<std::string, std::vector<int>> findRepeatedSubstrings(
            const std::string& text, int length, int minOccurrences) {
        
        std::unordered_map<std::string, std::vector<int>> result;
        
        if (text.empty() || length <= 0 || minOccurrences <= 1) {
            return result;
        }

        int n = text.length();
        if (length > n) {
            return result;
        }

        std::unordered_map<long long, std::vector<int>> hashMap;

        // 计算初始哈希值和最高位权值
        long long textHash = 0;
        long long highestPow = 1;
        for (int i = 0; i < length - 1; ++i) {
            highestPow = (highestPow * BASE) % MOD;
        }

        for (int i = 0; i < length; ++i) {
            textHash = (textHash * BASE + text[i]) % MOD;
        }

        // 滑动窗口遍历所有长度为length的子串
        for (int i = 0; i <= n - length; ++i) {
            auto& positions = hashMap[textHash];

            // 检查是否有哈希冲突
            bool found = false;
            for (int pos : positions) {
                bool match = true;
                for (int j = 0; j < length; ++j) {
                    if (text[pos + j] != text[i + j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    // 找到重复子串
                    std::string substring = text.substr(i, length);
                    auto& allPositions = result[substring];

                    // 如果是第一次添加这个子串，先加入之前的位置
                    if (allPositions.empty()) {
                        allPositions.push_back(pos);
                    }
                    allPositions.push_back(i);
                    found = true;
                    break;
                }
            }

            // 如果没有冲突或冲突未匹配，添加当前位置
            if (!found) {
                positions.push_back(i);
            }

            // 更新哈希值
            if (i < n - length) {
                textHash = (textHash - highestPow * text[i] % MOD + MOD) % MOD;
                textHash = (textHash * BASE + text[i + length]) % MOD;
            }
        }

        // 过滤出现次数少于minOccurrences的子串
        auto it = result.begin();
        while (it != result.end()) {
            if (it->second.size() < minOccurrences) {
                it = result.erase(it);
            } else {
                ++it;
            }
        }

        return result;
    }

    /**
     * 优化版本的Rabin-Karp搜索算法，使用预计算的幂值数组
     * @param text 文本串
     * @param pattern 模式串
     * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
     */
    static int searchOptimized(const std::string& text, const std::string& pattern) {
        if (text.empty() || pattern.empty()) {
            if (pattern.empty()) return 0;
            return -1;
        }

        int n = text.length();
        int m = pattern.length();

        if (n < m) {
            return -1;
        }

        // 预计算所有幂值，避免重复计算
        std::vector<long long> power(m);
        power[0] = 1;
        for (int i = 1; i < m; ++i) {
            power[i] = (power[i-1] * BASE) % MOD;
        }

        // 计算哈希值
        long long patternHash = 0;
        long long textHash = 0;
        for (int i = 0; i < m; ++i) {
            patternHash = (patternHash + pattern[i] * power[m-1-i]) % MOD;
            textHash = (textHash + text[i] * power[m-1-i]) % MOD;
        }

        // 滑动窗口匹配
        for (int i = 0; i <= n - m; ++i) {
            if (patternHash == textHash) {
                bool match = true;
                for (int j = 0; j < m; ++j) {
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
                // 移除最左边的字符贡献
                textHash = (textHash - text[i] * power[m-1] % MOD + MOD) % MOD;
                // 整体左移一位（乘以BASE）
                textHash = (textHash * BASE) % MOD;
                // 添加新的右边字符
                textHash = (textHash + text[i + m]) % MOD;
            }
        }

        return -1;
    }
};

int main() {
    // 测试用例1：基本匹配
    std::string text1 = "hello world";
    std::string pattern1 = "world";
    std::cout << "测试1 - 查找'world'在'hello world'中的位置: " 
              << RabinKarpAlgorithm::search(text1, pattern1) << std::endl; // 应该是6

    // 测试用例2：多次匹配
    std::string text2 = "abababa";
    std::string pattern2 = "aba";
    std::vector<int> results2 = RabinKarpAlgorithm::searchAll(text2, pattern2);
    std::cout << "测试2 - 查找所有'aba'在'abababa'中的位置: ";
    for (int pos : results2) {
        std::cout << pos << " "; // 应该是0 2 4
    }
    std::cout << std::endl;

    // 测试用例3：无匹配
    std::string text3 = "hello";
    std::string pattern3 = "world";
    std::cout << "测试3 - 查找'world'在'hello'中的位置: " 
              << RabinKarpAlgorithm::search(text3, pattern3) << std::endl; // 应该是-1

    // 测试用例4：边界情况
    std::string text4 = "test";
    std::string pattern4 = "";
    std::vector<int> results4 = RabinKarpAlgorithm::searchAll(text4, pattern4);
    std::cout << "测试4 - 查找空串在'test'中的位置: ";
    for (int pos : results4) {
        std::cout << pos << " "; // 应该是0 1 2 3 4
    }
    std::cout << std::endl;

    // 测试用例5：双哈希版本
    std::string text5 = "a!b@c#d$e%";
    std::string pattern5 = "c#d";
    std::cout << "测试5 - 双哈希版本查找'c#d'的位置: " 
              << RabinKarpAlgorithm::searchDoubleHash(text5, pattern5) << std::endl; // 应该是4

    // 测试用例6：计数功能
    std::string text6 = "abababa";
    std::string pattern6 = "aba";
    std::cout << "测试6 - 'aba'在'abababa'中出现的次数: " 
              << RabinKarpAlgorithm::countOccurrences(text6, pattern6) << std::endl; // 应该是3

    // 测试用例7：查找重复子串
    std::string text7 = "abcabcabcdefdefxyz";
    auto repeatedSubstrings = RabinKarpAlgorithm::findRepeatedSubstrings(text7, 3, 2);
    std::cout << "\n测试7 - 查找长度为3且至少出现2次的重复子串:" << std::endl;
    for (const auto& entry : repeatedSubstrings) {
        std::cout << "子串 '" << entry.first << "' 出现位置: ";
        for (int pos : entry.second) {
            std::cout << pos << " ";
        }
        std::cout << std::endl;
    }

    // 测试用例8：优化版本
    std::string text8 = "this is an optimized version of the algorithm";
    std::string pattern8 = "optimized";
    std::cout << "\n测试8 - 优化版本查找: " 
              << RabinKarpAlgorithm::searchOptimized(text8, pattern8) << std::endl; // 应该是10

    // 测试用例9：长文本测试
    std::string longTextBuilder;
    for (int i = 0; i < 1000; ++i) {
        longTextBuilder += "abcdefghijklmnopqrstuvwxyz";
    }
    std::string longText = longTextBuilder + "TARGET" + longTextBuilder;
    std::string targetPattern = "TARGET";

    auto startTime = std::chrono::high_resolution_clock::now();
    int pos = RabinKarpAlgorithm::search(longText, targetPattern);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    
    std::cout << "\n测试9 - 长文本匹配:" << std::endl;
    std::cout << "目标位置: " << pos << std::endl; // 应该是26000
    std::cout << "匹配耗时: " << duration.count() << "ms" << std::endl;

    return 0;
}