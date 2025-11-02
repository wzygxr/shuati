#include <iostream>
#include <string>
#include <vector>
#include <queue>
#include <unordered_map>
#include <algorithm>
#include <chrono>
#include <sstream>

/*
相关题目13: LeetCode 767. 重构字符串
题目链接: https://leetcode.cn/problems/reorganize-string/
题目描述: 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。
若可行，输出任意可行的结果。若不可行，返回空字符串。
解题思路: 使用最大堆按字符频率排序，然后贪心选择频率最高的字符进行放置
时间复杂度: O(n log k)，其中n是字符串长度，k是不同字符的数量（最大为26）
空间复杂度: O(k)，用于存储字符频率和堆
是否最优解: 此方法是最优解，没有更优的算法

本题属于堆的应用场景：基于频率的优先级处理问题
*/

class Solution {
public:
    /**
     * 重构字符串，使得相邻字符不同
     * 
     * @param s 输入字符串
     * @return 重构后的字符串，如果无法重构则返回空字符串
     */
    std::string reorganizeString(std::string s) {
        // 异常处理：检查输入字符串是否为空
        if (s.empty()) {
            return "";
        }
        
        // 统计每个字符的出现频率
        std::unordered_map<char, int> charCount;
        for (char c : s) {
            charCount[c]++;
        }
        
        // 检查是否可以重构：最多的字符出现次数不能超过(len(s)+1)/2
        int maxCount = 0;
        for (const auto& pair : charCount) {
            maxCount = std::max(maxCount, pair.second);
        }
        if (maxCount > (s.length() + 1) / 2) {
            return "";
        }
        
        // 创建最大堆（根据字符频率排序）
        // C++的priority_queue默认是最大堆，所以比较器需要返回true时不交换元素
        auto compare = [](const std::pair<char, int>& a, const std::pair<char, int>& b) {
            return a.second < b.second; // 最大堆
        };
        std::priority_queue<std::pair<char, int>, std::vector<std::pair<char, int>>, decltype(compare)> maxHeap(compare);
        
        for (const auto& pair : charCount) {
            maxHeap.push(pair);
        }
        
        // 用于存储重构后的字符串
        std::string result;
        
        // 当堆中有元素时
        while (!maxHeap.empty()) {
            // 获取当前频率最高的字符
            auto [char1, count1] = maxHeap.top();
            maxHeap.pop();
            
            // 如果结果为空或当前字符与结果最后一个字符不同，直接添加
            if (result.empty() || char1 != result.back()) {
                result.push_back(char1);
                count1--;
                // 如果字符还有剩余，将其放回堆中
                if (count1 > 0) {
                    maxHeap.push({char1, count1});
                }
            } else {
                // 如果当前字符与结果最后一个字符相同，需要选择下一个最高频率的字符
                // 如果堆为空，说明无法重构
                if (maxHeap.empty()) {
                    return "";
                }
                
                // 获取次高频率的字符
                auto [char2, count2] = maxHeap.top();
                maxHeap.pop();
                
                result.push_back(char2);
                count2--;
                // 如果次高频率字符还有剩余，将其放回堆中
                if (count2 > 0) {
                    maxHeap.push({char2, count2});
                }
                
                // 将最高频率字符放回堆中
                maxHeap.push({char1, count1});
            }
        }
        
        return result;
    }
};

class AlternativeSolution {
public:
    /**
     * 另一种实现方式，使用贪心算法直接构建结果字符串
     * 这种方法可能在某些情况下更直观
     */
    std::string reorganizeString(std::string s) {
        if (s.empty()) {
            return "";
        }
        
        int n = s.length();
        // 统计字符频率
        std::vector<int> count(26, 0); // 假设只有小写字母
        int maxCount = 0;
        char maxChar = ' ';
        
        for (char c : s) {
            count[c - 'a']++;
            if (count[c - 'a'] > maxCount) {
                maxCount = count[c - 'a'];
                maxChar = c;
            }
        }
        
        // 检查是否可以重构
        if (maxCount > (n + 1) / 2) {
            return "";
        }
        
        // 创建结果数组
        std::string result(n, '');
        int index = 0;
        
        // 首先放置频率最高的字符，间隔放置
        while (count[maxChar - 'a'] > 0) {
            result[index] = maxChar;
            index += 2;
            count[maxChar - 'a']--;
        }
        
        // 放置剩余的字符
        for (char c = 'a'; c <= 'z'; c++) {
            while (count[c - 'a'] > 0) {
                // 如果到达数组末尾，从索引1开始
                if (index >= n) {
                    index = 1;
                }
                result[index] = c;
                index += 2;
                count[c - 'a']--;
            }
        }
        
        return result;
    }
};

class OptimizedHeapSolution {
public:
    /**
     * 优化的堆实现，使用更简洁的逻辑
     */
    std::string reorganizeString(std::string s) {
        if (s.empty()) {
            return "";
        }
        
        // 统计字符频率
        std::unordered_map<char, int> charCount;
        for (char c : s) {
            charCount[c]++;
        }
        
        int n = s.length();
        // 检查是否可以重构
        int maxCount = 0;
        for (const auto& pair : charCount) {
            maxCount = std::max(maxCount, pair.second);
        }
        if (maxCount > (n + 1) / 2) {
            return "";
        }
        
        // 创建最大堆（根据字符频率排序）
        auto compare = [](const std::pair<char, int>& a, const std::pair<char, int>& b) {
            return a.second < b.second; // 最大堆
        };
        std::priority_queue<std::pair<char, int>, std::vector<std::pair<char, int>>, decltype(compare)> maxHeap(compare);
        
        for (const auto& pair : charCount) {
            maxHeap.push(pair);
        }
        
        std::string result;
        
        // 不断从堆中取出两个字符添加到结果中
        // 这样可以确保不会有相同字符相邻
        while (maxHeap.size() >= 2) {
            auto [char1, count1] = maxHeap.top();
            maxHeap.pop();
            auto [char2, count2] = maxHeap.top();
            maxHeap.pop();
            
            // 添加两个不同的字符
            result.push_back(char1);
            result.push_back(char2);
            
            // 如果字符还有剩余，放回堆中
            if (count1 > 1) {
                maxHeap.push({char1, count1 - 1});
            }
            if (count2 > 1) {
                maxHeap.push({char2, count2 - 1});
            }
        }
        
        // 如果堆中还有一个字符，说明字符串长度为奇数，添加最后一个字符
        if (!maxHeap.empty()) {
            result.push_back(maxHeap.top().first);
        }
        
        return result;
    }
};

/**
 * 辅助函数，验证重构后的字符串是否有效
 */
bool isValidReorganization(const std::string& original, const std::string& reorganized) {
    // 检查是否为空字符串且原字符串不为空
    if (reorganized.empty() && !original.empty()) {
        // 检查是否真的无法重构
        std::unordered_map<char, int> charCount;
        for (char c : original) {
            charCount[c]++;
        }
        int maxCount = 0;
        for (const auto& pair : charCount) {
            maxCount = std::max(maxCount, pair.second);
        }
        return maxCount > (original.length() + 1) / 2;
    }
    
    // 检查长度
    if (original.length() != reorganized.length()) {
        return false;
    }
    
    // 检查相邻字符是否不同
    for (size_t i = 1; i < reorganized.length(); i++) {
        if (reorganized[i] == reorganized[i - 1]) {
            return false;
        }
    }
    
    // 检查字符频率是否匹配
    std::unordered_map<char, int> originalCount;
    std::unordered_map<char, int> reorganizedCount;
    
    for (char c : original) {
        originalCount[c]++;
    }
    
    for (char c : reorganized) {
        reorganizedCount[c]++;
    }
    
    return originalCount == reorganizedCount;
}

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
void testReorganizeString() {
    std::cout << "=== 测试重构字符串算法 ===" << std::endl;
    Solution solution;
    AlternativeSolution alternativeSolution;
    OptimizedHeapSolution optimizedSolution;
    
    // 测试用例1：基本用例 - 可以重构
    std::cout << "\n测试用例1：基本用例 - 可以重构" << std::endl;
    std::string s1 = "aab";
    std::string result1 = solution.reorganizeString(s1);
    std::string altResult1 = alternativeSolution.reorganizeString(s1);
    std::string optResult1 = optimizedSolution.reorganizeString(s1);
    
    std::cout << "原字符串: " << s1 << std::endl;
    std::cout << "堆方法结果: " << result1 << ", 有效: " << (isValidReorganization(s1, result1) ? "true" : "false") << std::endl;
    std::cout << "贪心方法结果: " << altResult1 << ", 有效: " << (isValidReorganization(s1, altResult1) ? "true" : "false") << std::endl;
    std::cout << "优化堆方法结果: " << optResult1 << ", 有效: " << (isValidReorganization(s1, optResult1) ? "true" : "false") << std::endl;
    
    // 测试用例2：基本用例 - 可以重构
    std::cout << "\n测试用例2：基本用例 - 可以重构" << std::endl;
    std::string s2 = "aaab";
    std::string result2 = solution.reorganizeString(s2);
    std::string altResult2 = alternativeSolution.reorganizeString(s2);
    std::string optResult2 = optimizedSolution.reorganizeString(s2);
    
    std::cout << "原字符串: " << s2 << std::endl;
    std::cout << "堆方法结果: " << result2 << ", 有效: " << (isValidReorganization(s2, result2) ? "true" : "false") << std::endl;
    std::cout << "贪心方法结果: " << altResult2 << ", 有效: " << (isValidReorganization(s2, altResult2) ? "true" : "false") << std::endl;
    std::cout << "优化堆方法结果: " << optResult2 << ", 有效: " << (isValidReorganization(s2, optResult2) ? "true" : "false") << std::endl;
    
    // 测试用例3：无法重构的情况
    std::cout << "\n测试用例3：无法重构的情况" << std::endl;
    std::string s3 = "aaabbc";
    std::string result3 = solution.reorganizeString(s3);
    std::string altResult3 = alternativeSolution.reorganizeString(s3);
    std::string optResult3 = optimizedSolution.reorganizeString(s3);
    
    std::cout << "原字符串: " << s3 << std::endl;
    std::cout << "堆方法结果: " << result3 << ", 有效: " << (isValidReorganization(s3, result3) ? "true" : "false") << std::endl;
    std::cout << "贪心方法结果: " << altResult3 << ", 有效: " << (isValidReorganization(s3, altResult3) ? "true" : "false") << std::endl;
    std::cout << "优化堆方法结果: " << optResult3 << ", 有效: " << (isValidReorganization(s3, optResult3) ? "true" : "false") << std::endl;
    
    // 测试用例4：单字符
    std::cout << "\n测试用例4：单字符" << std::endl;
    std::string s4 = "a";
    std::string result4 = solution.reorganizeString(s4);
    std::string altResult4 = alternativeSolution.reorganizeString(s4);
    std::string optResult4 = optimizedSolution.reorganizeString(s4);
    
    std::cout << "原字符串: " << s4 << std::endl;
    std::cout << "堆方法结果: " << result4 << ", 有效: " << (isValidReorganization(s4, result4) ? "true" : "false") << std::endl;
    std::cout << "贪心方法结果: " << altResult4 << ", 有效: " << (isValidReorganization(s4, altResult4) ? "true" : "false") << std::endl;
    std::cout << "优化堆方法结果: " << optResult4 << ", 有效: " << (isValidReorganization(s4, optResult4) ? "true" : "false") << std::endl;
    
    // 测试用例5：所有字符相同
    std::cout << "\n测试用例5：所有字符相同" << std::endl;
    std::string s5 = "aaaaa";
    std::string result5 = solution.reorganizeString(s5);
    std::string altResult5 = alternativeSolution.reorganizeString(s5);
    std::string optResult5 = optimizedSolution.reorganizeString(s5);
    
    std::cout << "原字符串: " << s5 << std::endl;
    std::cout << "堆方法结果: " << result5 << ", 有效: " << (isValidReorganization(s5, result5) ? "true" : "false") << std::endl;
    std::cout << "贪心方法结果: " << altResult5 << ", 有效: " << (isValidReorganization(s5, altResult5) ? "true" : "false") << std::endl;
    std::cout << "优化堆方法结果: " << optResult5 << ", 有效: " << (isValidReorganization(s5, optResult5) ? "true" : "false") << std::endl;
    
    // 测试用例6：所有字符都不同
    std::cout << "\n测试用例6：所有字符都不同" << std::endl;
    std::string s6 = "abcdef";
    std::string result6 = solution.reorganizeString(s6);
    std::string altResult6 = alternativeSolution.reorganizeString(s6);
    std::string optResult6 = optimizedSolution.reorganizeString(s6);
    
    std::cout << "原字符串: " << s6 << std::endl;
    std::cout << "堆方法结果: " << result6 << ", 有效: " << (isValidReorganization(s6, result6) ? "true" : "false") << std::endl;
    std::cout << "贪心方法结果: " << altResult6 << ", 有效: " << (isValidReorganization(s6, altResult6) ? "true" : "false") << std::endl;
    std::cout << "优化堆方法结果: " << optResult6 << ", 有效: " << (isValidReorganization(s6, optResult6) ? "true" : "false") << std::endl;
    
    // 性能测试
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    // 创建一个较大的可重构的字符串
    std::string smallPattern = "aabbccddeeffgghh"; // 16个字符
    std::string largeS;
    for (int i = 0; i < 1000; i++) {
        largeS += smallPattern;
    } // 总长度 16000
    
    // 测试堆方法性能
    auto start = std::chrono::high_resolution_clock::now();
    std::string largeResult = solution.reorganizeString(largeS);
    auto end = std::chrono::high_resolution_clock::now();
    auto heapTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "堆方法处理大字符串用时: " << heapTime << "毫秒, 结果有效: " << (isValidReorganization(largeS, largeResult) ? "true" : "false") << std::endl;
    
    // 测试贪心方法性能
    start = std::chrono::high_resolution_clock::now();
    std::string largeAltResult = alternativeSolution.reorganizeString(largeS);
    end = std::chrono::high_resolution_clock::now();
    auto greedyTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "贪心方法处理大字符串用时: " << greedyTime << "毫秒, 结果有效: " << (isValidReorganization(largeS, largeAltResult) ? "true" : "false") << std::endl;
    
    // 测试优化堆方法性能
    start = std::chrono::high_resolution_clock::now();
    std::string largeOptResult = optimizedSolution.reorganizeString(largeS);
    end = std::chrono::high_resolution_clock::now();
    auto optHeapTime = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "优化堆方法处理大字符串用时: " << optHeapTime << "毫秒, 结果有效: " << (isValidReorganization(largeS, largeOptResult) ? "true" : "false") << std::endl;
    
    // 性能比较
    std::cout << "\n性能比较:" << std::endl;
    double ratio1 = static_cast<double>(std::max(heapTime, greedyTime)) / std::min(heapTime, greedyTime);
    std::cout << "堆方法 vs 贪心方法: " << (greedyTime < heapTime ? "贪心方法更快" : "堆方法更快") 
              << " 约 " << ratio1 << "倍" << std::endl;
    
    double ratio2 = static_cast<double>(std::max(heapTime, optHeapTime)) / std::min(heapTime, optHeapTime);
    std::cout << "堆方法 vs 优化堆方法: " << (optHeapTime < heapTime ? "优化堆方法更快" : "堆方法更快") 
              << " 约 " << ratio2 << "倍" << std::endl;
    
    double ratio3 = static_cast<double>(std::max(greedyTime, optHeapTime)) / std::min(greedyTime, optHeapTime);
    std::cout << "贪心方法 vs 优化堆方法: " << (optHeapTime < greedyTime ? "优化堆方法更快" : "贪心方法更快") 
              << " 约 " << ratio3 << "倍" << std::endl;
}

// 主函数
int main() {
    testReorganizeString();
    return 0;
}

/*
解题思路总结：
1. 问题分析：
   - 要重新排列字符串，使得相邻字符不同
   - 关键条件：最高频率字符的出现次数不能超过(len(s)+1)/2
   - 如果最高频率字符次数超过这个阈值，无法重构

2. 堆方法（优先队列）：
   - 统计每个字符的频率
   - 将字符及其频率放入最大堆（C++中使用priority_queue实现）
   - 每次从堆中取出频率最高的字符添加到结果中
   - 如果当前字符与结果最后一个字符相同，则取出下一个最高频率的字符
   - 将使用过的字符（如果还有剩余）重新放回堆中
   - 时间复杂度：O(n log k)，其中n是字符串长度，k是不同字符的数量
   - 空间复杂度：O(k)

3. 贪心方法：
   - 先放置频率最高的字符，间隔放置
   - 然后放置剩余的字符
   - 时间复杂度：O(n)
   - 空间复杂度：O(k)

4. 优化堆方法：
   - 每次从堆中取出两个不同的字符添加到结果中
   - 这样可以确保相邻字符不同
   - 最后如果还有一个字符（字符串长度为奇数），直接添加
   - 时间复杂度：O(n log k)
   - 空间复杂度：O(k)

5. 边界情况处理：
   - 空字符串
   - 单字符字符串
   - 所有字符相同的字符串
   - 所有字符都不同的字符串
   - 最高频率字符刚好达到阈值的情况

6. 堆方法的优势：
   - 自动维护元素的优先级
   - 适合需要频繁获取最高优先级元素的场景
   - 在这里用于贪心选择频率最高的字符进行放置

7. 应用场景：
   - 字符重排问题
   - 任务调度问题（优先处理高优先级任务）
   - 资源分配问题（基于某种优先级分配资源）
*/