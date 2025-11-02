#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

/**
 * 最大单词长度乘积（位掩码优化）
 * 测试链接：https://leetcode.cn/problems/maximum-product-of-word-lengths/
 * 
 * 题目描述：
 * 给定一个字符串数组 words，找到 length(word[i]) * length(word[j]) 的最大值，
 * 并且这两个单词不含有公共字母。你可以认为每个单词只包含小写字母。如果不存在这样的两个单词，返回 0。
 * 
 * 解题思路：
 * 1. 暴力法：双重循环检查所有组合（会超时）
 * 2. 位掩码法：使用位掩码表示每个单词的字符集合
 * 3. 位掩码优化：预计算位掩码和长度，优化比较过程
 * 4. 哈希表法：使用哈希表存储相同位掩码的最大长度
 * 5. 分组法：按位掩码分组，只比较不同组的单词
 * 
 * 时间复杂度分析：
 * - 暴力法：O(n² * L)，L为单词平均长度
 * - 位掩码法：O(n² + nL)
 * - 位掩码优化：O(n² + nL)
 * - 哈希表法：O(n² + nL)
 * - 分组法：O(n² + nL)
 * 
 * 空间复杂度分析：
 * - 暴力法：O(1)
 * - 位掩码法：O(n)
 * - 位掩码优化：O(n)
 * - 哈希表法：O(n)
 * - 分组法：O(n)
 */
class Solution {
public:
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n² * L)，L为单词平均长度
     * 空间复杂度：O(1)
     */
    int maxProduct1(vector<string>& words) {
        int maxProduct = 0;
        int n = words.size();
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (!hasCommonChars(words[i], words[j])) {
                    maxProduct = max(maxProduct, (int)(words[i].length() * words[j].length()));
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 检查两个单词是否有公共字符
     * 时间复杂度：O(L1 + L2)
     * 空间复杂度：O(26) = O(1)
     */
    bool hasCommonChars(const string& word1, const string& word2) {
        bool chars[26] = {false};
        
        // 记录第一个单词的字符
        for (char c : word1) {
            chars[c - 'a'] = true;
        }
        
        // 检查第二个单词是否有相同字符
        for (char c : word2) {
            if (chars[c - 'a']) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 方法2：位掩码法（推荐）
     * 核心思想：使用26位整数表示每个单词的字符集合
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    int maxProduct2(vector<string>& words) {
        int n = words.size();
        vector<int> masks(n, 0);  // 存储每个单词的位掩码
        vector<int> lengths(n, 0);  // 存储每个单词的长度
        
        // 预处理：计算每个单词的位掩码和长度
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i]) {
                mask |= 1 << (c - 'a');
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }
        
        int maxProduct = 0;
        
        // 比较所有单词对
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 使用位与运算检查是否有公共字符
                if ((masks[i] & masks[j]) == 0) {
                    maxProduct = max(maxProduct, lengths[i] * lengths[j]);
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 方法3：位掩码优化版
     * 优化比较过程，减少不必要的计算
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    int maxProduct3(vector<string>& words) {
        int n = words.size();
        vector<int> masks(n);
        vector<int> lengths(n);
        
        // 预处理
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i]) {
                mask |= 1 << (c - 'a');
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }
        
        int maxProduct = 0;
        
        // 创建索引数组用于排序
        vector<int> indices(n);
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 按长度降序排序
        sort(indices.begin(), indices.end(), [&](int a, int b) {
            return lengths[a] > lengths[b];
        });
        
        for (int i = 0; i < n; i++) {
            int idx1 = indices[i];
            
            // 如果当前最大乘积已经大于可能的最大值，提前终止
            if (lengths[idx1] * lengths[idx1] <= maxProduct) {
                break;
            }
            
            for (int j = i + 1; j < n; j++) {
                int idx2 = indices[j];
                
                if ((masks[idx1] & masks[idx2]) == 0) {
                    maxProduct = max(maxProduct, lengths[idx1] * lengths[idx2]);
                    break;  // 由于排序，后面的长度更小，乘积不会更大
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 方法4：哈希表法
     * 使用哈希表存储相同位掩码的最大长度
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    int maxProduct4(vector<string>& words) {
        unordered_map<int, int> maskToMaxLength;
        
        // 预处理：对于相同的位掩码，只保留最长的单词长度
        for (const string& word : words) {
            int mask = 0;
            for (char c : word) {
                mask |= 1 << (c - 'a');
            }
            
            // 更新相同掩码的最大长度
            if (maskToMaxLength.find(mask) == maskToMaxLength.end() || 
                word.length() > maskToMaxLength[mask]) {
                maskToMaxLength[mask] = word.length();
            }
        }
        
        int maxProduct = 0;
        vector<int> masks;
        for (const auto& pair : maskToMaxLength) {
            masks.push_back(pair.first);
        }
        int size = masks.size();
        
        // 比较所有不同的位掩码
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                int mask1 = masks[i];
                int mask2 = masks[j];
                
                if ((mask1 & mask2) == 0) {
                    int product = maskToMaxLength[mask1] * maskToMaxLength[mask2];
                    maxProduct = max(maxProduct, product);
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 方法5：分组法
     * 按位掩码分组，优化比较过程
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    int maxProduct5(vector<string>& words) {
        int n = words.size();
        
        // 预处理：计算位掩码和长度
        vector<int> masks(n);
        vector<int> lengths(n);
        
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i]) {
                mask |= 1 << (c - 'a');
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }
        
        int maxProduct = 0;
        
        // 分组比较：使用位运算优化
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 快速检查：如果长度乘积小于当前最大值，跳过
                if (lengths[i] * lengths[j] <= maxProduct) {
                    continue;
                }
                
                // 位运算检查公共字符
                if ((masks[i] & masks[j]) == 0) {
                    maxProduct = lengths[i] * lengths[j];
                }
            }
        }
        
        return maxProduct;
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：示例1
    vector<string> words1 = {"abcw", "baz", "foo", "bar", "xtfn", "abcdef"};
    int result1 = solution.maxProduct2(words1);
    cout << "测试用例1 - 输入: ";
    for (const string& word : words1) cout << word << " ";
    cout << endl;
    cout << "结果: " << result1 << " (预期: 16)" << endl;
    
    // 测试用例2：示例2
    vector<string> words2 = {"a", "ab", "abc", "d", "cd", "bcd", "abcd"};
    int result2 = solution.maxProduct2(words2);
    cout << "测试用例2 - 输入: ";
    for (const string& word : words2) cout << word << " ";
    cout << endl;
    cout << "结果: " << result2 << " (预期: 4)" << endl;
    
    // 测试用例3：无解情况
    vector<string> words3 = {"a", "aa", "aaa", "aaaa"};
    int result3 = solution.maxProduct2(words3);
    cout << "测试用例3 - 输入: ";
    for (const string& word : words3) cout << word << " ";
    cout << endl;
    cout << "结果: " << result3 << " (预期: 0)" << endl;
    
    // 测试用例4：边界情况（两个单词）
    vector<string> words4 = {"ab", "cd"};
    int result4 = solution.maxProduct2(words4);
    cout << "测试用例4 - 输入: ";
    for (const string& word : words4) cout << word << " ";
    cout << endl;
    cout << "结果: " << result4 << " (预期: 4)" << endl;
    
    // 性能测试
    vector<string> largeWords(100);
    for (int i = 0; i < 100; i++) {
        largeWords[i] = generateRandomWord(100);
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result5 = solution.maxProduct2(largeWords);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "性能测试 - 输入长度: " << largeWords.size() << endl;
    cout << "结果: " << result5 << endl;
    cout << "耗时: " << duration.count() << "ms" << endl;
    
    // 所有方法结果对比
    cout << "\n=== 所有方法结果对比 ===" << endl;
    vector<string> testWords = {"abc", "def", "ghi", "jkl"};
    cout << "测试单词数组: ";
    for (const string& word : testWords) cout << word << " ";
    cout << endl;
    cout << "方法1 (暴力法): " << solution.maxProduct1(testWords) << endl;
    cout << "方法2 (位掩码法): " << solution.maxProduct2(testWords) << endl;
    cout << "方法3 (位掩码优化): " << solution.maxProduct3(testWords) << endl;
    cout << "方法4 (哈希表法): " << solution.maxProduct4(testWords) << endl;
    cout << "方法5 (分组法): " << solution.maxProduct5(testWords) << endl;
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "方法1 - 暴力法:" << endl;
    cout << "  时间复杂度: O(n² * L) - 会超时" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法2 - 位掩码法:" << endl;
    cout << "  时间复杂度: O(n² + nL)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法3 - 位掩码优化:" << endl;
    cout << "  时间复杂度: O(n² + nL)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法4 - 哈希表法:" << endl;
    cout << "  时间复杂度: O(n² + nL)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    cout << "方法5 - 分组法:" << endl;
    cout << "  时间复杂度: O(n² + nL)" << endl;
    cout << "  空间复杂度: O(n)" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 算法选择：方法2 (位掩码法) 最优" << endl;
    cout << "2. 性能优化：避免O(n² * L)的暴力解法" << endl;
    cout << "3. 空间优化：使用位掩码压缩存储" << endl;
    cout << "4. 实际应用：适合处理大量单词数据" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. 位掩码技术：26位整数表示字符集合" << endl;
    cout << "2. 位运算优化：使用 & 运算快速检查公共字符" << endl;
    cout << "3. 预处理思想：先计算位掩码，再进行比较" << endl;
    cout << "4. 排序优化：按长度降序排序，提前终止" << endl;
    
    return 0;
}

/**
 * 生成随机单词（用于性能测试）
 */
string generateRandomWord(int length) {
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis('a', 'z');
    
    string word;
    for (int i = 0; i < length; i++) {
        word += static_cast<char>(dis(gen));
    }
    return word;
}