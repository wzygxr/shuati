#include <iostream>
#include <vector>
#include <string>
#include <queue>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;

/**
 * 重构字符串
 * 
 * 题目描述：
 * 给定一个字符串 s ，检查是否能重新排布其中的字母，使得两相邻的字符不同。
 * 若可行，输出任意可行的结果。若不可行，返回空字符串。
 * 
 * 来源：LeetCode 767
 * 链接：https://leetcode.cn/problems/reorganize-string/
 * 
 * 算法思路：
 * 使用贪心算法 + 优先队列：
 * 1. 统计每个字符的出现频率
 * 2. 如果某个字符的频率超过字符串长度的一半，则无法重构，返回空字符串
 * 3. 使用最大堆（按频率排序）存储字符
 * 4. 每次从堆中取出频率最高的两个字符，交替排列
 * 5. 如果堆中还有剩余字符，继续处理
 * 
 * 时间复杂度：O(n * logk) - n是字符串长度，k是字符种类数
 * 空间复杂度：O(k) - 优先队列和频率统计的空间
 * 
 * 关键点分析：
 * - 贪心策略：每次选择频率最高的两个字符交替排列
 * - 边界处理：检查是否有字符频率超过一半
 * - 异常场景：单字符字符串的处理
 * 
 * 工程化考量：
 * - 输入验证：检查字符串是否为空
 * - 性能优化：使用数组而非unordered_map统计频率
 * - 内存安全：避免内存泄漏
 */
class Code29_ReorganizeString {
public:
    /**
     * 重构字符串，使得相邻字符不同
     * 
     * @param s 输入字符串
     * @return 重构后的字符串，如果无法重构返回空字符串
     */
    static string reorganizeString(string s) {
        // 输入验证
        if (s.empty()) {
            return "";
        }
        
        int n = s.length();
        
        // 统计字符频率
        vector<int> freq(26, 0);
        for (char c : s) {
            freq[c - 'a']++;
        }
        
        // 检查是否有字符频率超过一半（向上取整）
        int maxFreq = 0;
        int maxCharIndex = 0;
        for (int i = 0; i < 26; i++) {
            if (freq[i] > maxFreq) {
                maxFreq = freq[i];
                maxCharIndex = i;
            }
        }
        
        // 如果最大频率超过 (n+1)/2，则无法重构
        if (maxFreq > (n + 1) / 2) {
            return "";
        }
        
        // 使用优先队列存储字符和频率（最大堆）
        auto cmp = [](const pair<int, int>& a, const pair<int, int>& b) {
            return a.second < b.second;
        };
        priority_queue<pair<int, int>, vector<pair<int, int>>, decltype(cmp)> maxHeap(cmp);
        
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                maxHeap.push({i, freq[i]});
            }
        }
        
        string result = "";
        
        while (!maxHeap.empty()) {
            // 取出频率最高的字符
            auto first = maxHeap.top();
            maxHeap.pop();
            
            if (result.empty() || result.back() != 'a' + first.first) {
                // 如果结果为空或最后一个字符不同，直接添加
                result += ('a' + first.first);
                first.second--;
                
                if (first.second > 0) {
                    maxHeap.push(first);
                }
            } else {
                // 如果需要交替，但堆为空，无法重构
                if (maxHeap.empty()) {
                    return "";
                }
                
                // 取出第二个字符
                auto second = maxHeap.top();
                maxHeap.pop();
                result += ('a' + second.first);
                second.second--;
                
                // 将两个字符重新加入堆中
                if (second.second > 0) {
                    maxHeap.push(second);
                }
                maxHeap.push(first);
            }
        }
        
        return result;
    }
    
    /**
     * 另一种实现：更简洁的交替排列方法
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static string reorganizeStringAlternate(string s) {
        if (s.empty()) {
            return "";
        }
        
        int n = s.length();
        vector<int> freq(26, 0);
        
        // 统计频率并找到最大频率字符
        int maxFreq = 0;
        int maxCharIndex = 0;
        for (char c : s) {
            int index = c - 'a';
            freq[index]++;
            if (freq[index] > maxFreq) {
                maxFreq = freq[index];
                maxCharIndex = index;
            }
        }
        
        // 检查是否可重构
        if (maxFreq > (n + 1) / 2) {
            return "";
        }
        
        // 先放置最大频率字符
        string result(n, ' ');
        int idx = 0;
        
        // 先填充偶数位置
        while (freq[maxCharIndex] > 0) {
            result[idx] = 'a' + maxCharIndex;
            idx += 2;
            freq[maxCharIndex]--;
            
            // 如果偶数位置填满，转到奇数位置
            if (idx >= n) {
                idx = 1;
            }
        }
        
        // 填充其他字符
        for (int i = 0; i < 26; i++) {
            while (freq[i] > 0) {
                if (idx >= n) {
                    idx = 1;
                }
                result[idx] = 'a' + i;
                idx += 2;
                freq[i]--;
            }
        }
        
        return result;
    }
    
    /**
     * 验证字符串是否满足相邻字符不同的条件
     * 
     * @param s 要验证的字符串
     * @return 是否满足条件
     */
    static bool isValidReorganization(const string& s) {
        if (s.length() <= 1) {
            return true;
        }
        
        for (size_t i = 1; i < s.length(); i++) {
            if (s[i] == s[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 运行测试用例
     */
    static void runTests() {
        cout << "=== 重构字符串测试 ===" << endl;
        
        // 测试用例1: "aab" -> "aba"
        string s1 = "aab";
        cout << "测试用例1: \"" << s1 << "\"" << endl;
        string result1 = reorganizeString(s1);
        string result1Alt = reorganizeStringAlternate(s1);
        cout << "结果1: \"" << result1 << "\", 有效: " << (isValidReorganization(result1) ? "true" : "false") << endl;
        cout << "结果2: \"" << result1Alt << "\", 有效: " << (isValidReorganization(result1Alt) ? "true" : "false") << endl;
        
        // 测试用例2: "aaab" -> "" (无法重构)
        string s2 = "aaab";
        cout << "\n测试用例2: \"" << s2 << "\"" << endl;
        string result2 = reorganizeString(s2);
        string result2Alt = reorganizeStringAlternate(s2);
        cout << "结果1: \"" << result2 << "\"" << endl;
        cout << "结果2: \"" << result2Alt << "\"" << endl;
        
        // 测试用例3: "abc" -> 任意有效排列
        string s3 = "abc";
        cout << "\n测试用例3: \"" << s3 << "\"" << endl;
        string result3 = reorganizeString(s3);
        string result3Alt = reorganizeStringAlternate(s3);
        cout << "结果1: \"" << result3 << "\", 有效: " << (isValidReorganization(result3) ? "true" : "false") << endl;
        cout << "结果2: \"" << result3Alt << "\", 有效: " << (isValidReorganization(result3Alt) ? "true" : "false") << endl;
        
        // 测试用例4: "a" -> "a"
        string s4 = "a";
        cout << "\n测试用例4: \"" << s4 << "\"" << endl;
        string result4 = reorganizeString(s4);
        string result4Alt = reorganizeStringAlternate(s4);
        cout << "结果1: \"" << result4 << "\", 有效: " << (isValidReorganization(result4) ? "true" : "false") << endl;
        cout << "结果2: \"" << result4Alt << "\", 有效: " << (isValidReorganization(result4Alt) ? "true" : "false") << endl;
        
        // 测试用例5: "aa" -> "" (无法重构)
        string s5 = "aa";
        cout << "\n测试用例5: \"" << s5 << "\"" << endl;
        string result5 = reorganizeString(s5);
        string result5Alt = reorganizeStringAlternate(s5);
        cout << "结果1: \"" << result5 << "\"" << endl;
        cout << "结果2: \"" << result5Alt << "\"" << endl;
        
        // 测试用例6: "aabbcc" -> 有效排列
        string s6 = "aabbcc";
        cout << "\n测试用例6: \"" << s6 << "\"" << endl;
        string result6 = reorganizeString(s6);
        string result6Alt = reorganizeStringAlternate(s6);
        cout << "结果1: \"" << result6 << "\", 有效: " << (isValidReorganization(result6) ? "true" : "false") << endl;
        cout << "结果2: \"" << result6Alt << "\", 有效: " << (isValidReorganization(result6Alt) ? "true" : "false") << endl;
    }
    
    /**
     * 性能测试方法
     */
    static void performanceTest() {
        // 生成大规模测试数据
        string largeString;
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, 25);
        
        for (int i = 0; i < 10000; i++) {
            largeString += 'a' + dis(gen);
        }
        
        cout << "\n=== 性能测试 ===" << endl;
        
        auto startTime1 = chrono::high_resolution_clock::now();
        string result1 = reorganizeString(largeString);
        auto endTime1 = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::milliseconds>(endTime1 - startTime1);
        cout << "方法1执行时间: " << duration1.count() << "ms" << endl;
        cout << "方法1结果有效: " << (isValidReorganization(result1) ? "true" : "false") << endl;
        
        auto startTime2 = chrono::high_resolution_clock::now();
        string result2 = reorganizeStringAlternate(largeString);
        auto endTime2 = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::milliseconds>(endTime2 - startTime2);
        cout << "方法2执行时间: " << duration2.count() << "ms" << endl;
        cout << "方法2结果有效: " << (isValidReorganization(result2) ? "true" : "false") << endl;
    }
    
    /**
     * 算法复杂度分析
     */
    static void analyzeComplexity() {
        cout << "\n=== 算法复杂度分析 ===" << endl;
        cout << "方法1（优先队列）:" << endl;
        cout << "- 时间复杂度: O(n * logk)" << endl;
        cout << "  - 统计频率: O(n)" << endl;
        cout << "  - 堆操作: O(n * logk)，k为字符种类数" << endl;
        cout << "- 空间复杂度: O(k)" << endl;
        cout << "  - 频率数组: O(26) ≈ O(1)" << endl;
        cout << "  - 优先队列: O(k)" << endl;
        
        cout << "\n方法2（交替填充）:" << endl;
        cout << "- 时间复杂度: O(n)" << endl;
        cout << "  - 统计频率: O(n)" << endl;
        cout << "  - 填充数组: O(n)" << endl;
        cout << "- 空间复杂度: O(n)" << endl;
        cout << "  - 结果数组: O(n)" << endl;
        
        cout << "\n贪心策略证明:" << endl;
        cout << "1. 优先处理频率最高的字符可以避免冲突" << endl;
        cout << "2. 交替排列确保相邻字符不同" << endl;
        cout << "3. 数学证明：当最大频率 ≤ (n+1)/2 时可重构" << endl;
    }
};

int main() {
    Code29_ReorganizeString::runTests();
    Code29_ReorganizeString::performanceTest();
    Code29_ReorganizeString::analyzeComplexity();
    
    return 0;
}