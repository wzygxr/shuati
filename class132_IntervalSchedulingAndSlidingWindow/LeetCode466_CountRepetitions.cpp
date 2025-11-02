#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>
#include <limits>

using namespace std;

/**
 * LeetCode 466. 统计重复个数 - C++实现
 * 
 * 题目描述：
 * 定义 str = [str, n] 表示重复字符串，由 n 个连续的字符串 str 组成。
 * 例如 ["abc", 3] = "abcabcabc"。
 * 如果我们可以从 s2 中删除某些字符使其变为 s1，则称字符串 s1 可以从字符串 s2 获得。
 * 现在给你两个非空字符串 s1 和 s2（每个最多 100 个字符长）和两个整数 0 <= n1 <= 10^6 和 1 <= n2 <= 10^6。
 * 现在考虑字符串 S1 和 S2，其中 S1=[s1,n1] 、S2=[s2,n2] 。
 * 请你找出一个可以满足使 [S2, M] 从 S1 获得的最大整数 M 。
 * 
 * 解题思路：
 * 这是一道需要寻找循环节的字符串匹配问题。
 * 
 * 核心思想：
 * 1. 预处理：计算从s1的每个位置开始，匹配s2中每个字符需要的最小长度
 * 2. 倍增优化：预处理从每个位置开始匹配一个s2需要的长度，然后使用倍增思想计算匹配多个s2
 * 3. 循环节：寻找循环节，利用循环节快速计算结果
 * 
 * 具体步骤：
 * 1. 预处理 next 数组：next[i][j] 表示从 s1 的位置 i 开始，至少需要多少长度才能找到字符 'a'+j
 * 2. 预处理 st 数组：st[i][p] 表示从 s1 的位置 i 开始，至少需要多少长度才能匹配 2^p 个 s2
 * 3. 倍增计算：使用 st 数组快速计算能匹配多少个 s2
 * 4. 结果计算：总匹配数除以 n2 得到最终结果
 * 
 * 时间复杂度：O(len1 * len2 + log(n1 * len1))
 * 空间复杂度：O(len1 * log(n1 * len1))
 * 
 * 工程化考量：
 * - 使用vector存储数据，避免内存泄漏
 * - 添加边界条件检查
 * - 使用倍增思想优化计算效率
 * - 提供完整的测试用例
 */

class CountRepetitions {
public:
    /**
     * 计算最大重复数M
     * 
     * @param s1 字符串s1
     * @param n1 s1重复次数
     * @param s2 字符串s2
     * @param n2 s2重复次数
     * @return 最大整数M，使得[S2,M]可以从S1获得
     */
    static int getMaxRepetitions(string s1, int n1, string s2, int n2) {
        // 边界条件检查
        if (s1.empty() || s2.empty() || n1 <= 0 || n2 <= 0) {
            return 0;
        }
        
        int len1 = s1.length();
        int len2 = s2.length();
        
        // 预处理next数组：next[i][j]表示从位置i开始找到字符j的最小长度
        vector<vector<int>> next(len1, vector<int>(26, -1));
        
        // 从后往前预处理next数组
        for (int i = len1 - 1; i >= 0; i--) {
            // 复制下一行的值
            if (i < len1 - 1) {
                next[i] = next[i + 1];
            }
            // 设置当前字符的位置
            next[i][s1[i] - 'a'] = i;
        }
        
        // 预处理第一个字符的next数组（处理循环情况）
        vector<int> firstNext(26, -1);
        for (int i = 0; i < len1; i++) {
            if (firstNext[s1[i] - 'a'] == -1) {
                firstNext[s1[i] - 'a'] = i;
            }
        }
        
        // 计算匹配一个s2需要的最小长度
        vector<int> matchLen(len1, 0);
        for (int start = 0; start < len1; start++) {
            int pos = start;
            int matched = 0;
            
            for (char c : s2) {
                int charIndex = c - 'a';
                
                // 在当前s1中查找字符
                if (next[pos][charIndex] != -1) {
                    matched += next[pos][charIndex] - pos + 1;
                    pos = (next[pos][charIndex] + 1) % len1;
                } else {
                    // 如果当前s1中找不到，需要到下一个s1中查找
                    if (firstNext[charIndex] == -1) {
                        // s1中根本不存在该字符
                        matchLen[start] = -1;
                        break;
                    }
                    matched += (len1 - pos) + firstNext[charIndex] + 1;
                    pos = (firstNext[charIndex] + 1) % len1;
                }
            }
            
            if (matchLen[start] != -1) {
                matchLen[start] = matched;
            }
        }
        
        // 检查是否存在无法匹配的情况
        if (matchLen[0] == -1) {
            return 0;
        }
        
        // 计算倍增表的层数
        int maxPower = 0;
        long long totalLen = (long long)n1 * len1;
        while ((1LL << maxPower) <= totalLen) {
            maxPower++;
        }
        
        // 初始化倍增表
        vector<vector<long long>> st(len1, vector<long long>(maxPower, 0));
        vector<vector<int>> nextStart(len1, vector<int>(maxPower, 0));
        
        // 初始化第一层
        for (int i = 0; i < len1; i++) {
            if (matchLen[i] != -1) {
                st[i][0] = matchLen[i];
                nextStart[i][0] = (i + matchLen[i]) % len1;
            } else {
                st[i][0] = -1;
                nextStart[i][0] = -1;
            }
        }
        
        // 构建倍增表
        for (int p = 1; p < maxPower; p++) {
            for (int i = 0; i < len1; i++) {
                if (st[i][p-1] != -1 && st[nextStart[i][p-1]][p-1] != -1) {
                    st[i][p] = st[i][p-1] + st[nextStart[i][p-1]][p-1];
                    nextStart[i][p] = nextStart[nextStart[i][p-1]][p-1];
                } else {
                    st[i][p] = -1;
                    nextStart[i][p] = -1;
                }
            }
        }
        
        // 使用倍增表计算最大匹配数
        long long currentLen = 0;
        int currentStart = 0;
        long long matchCount = 0;
        
        for (int p = maxPower - 1; p >= 0; p--) {
            if (st[currentStart][p] != -1 && currentLen + st[currentStart][p] <= totalLen) {
                currentLen += st[currentStart][p];
                matchCount += (1LL << p);
                currentStart = nextStart[currentStart][p];
            }
        }
        
        // 返回结果：匹配的s2数量除以n2
        return matchCount / n2;
    }
    
    /**
     * 简化版本：使用循环节检测
     * 当n1很大时，寻找循环节可以优化性能
     */
    static int getMaxRepetitionsSimple(string s1, int n1, string s2, int n2) {
        if (s1.empty() || s2.empty() || n1 <= 0 || n2 <= 0) {
            return 0;
        }
        
        int len1 = s1.length();
        int len2 = s2.length();
        
        // 检查s1是否包含s2的所有字符
        vector<bool> charExists(26, false);
        for (char c : s1) {
            charExists[c - 'a'] = true;
        }
        for (char c : s2) {
            if (!charExists[c - 'a']) {
                return 0;
            }
        }
        
        // 使用循环节检测
        vector<int> indexMap(len1, -1);
        vector<int> countMap(len1, 0);
        
        int index = 0;
        int count = 0;
        int s2Index = 0;
        
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < len1; j++) {
                if (s1[j] == s2[s2Index]) {
                    s2Index++;
                    if (s2Index == len2) {
                        count++;
                        s2Index = 0;
                    }
                }
            }
            
            // 检查是否出现循环节
            if (indexMap[index] != -1) {
                // 找到循环节
                int prevIndex = indexMap[index];
                int prevCount = countMap[index];
                
                int cycleLength = i - prevIndex;
                int cycleCount = count - prevCount;
                
                int remaining = n1 - i - 1;
                int fullCycles = remaining / cycleLength;
                
                count += fullCycles * cycleCount;
                i += fullCycles * cycleLength;
                
                // 处理剩余部分
                for (int k = 0; k < remaining % cycleLength; k++) {
                    for (int j = 0; j < len1; j++) {
                        if (s1[j] == s2[s2Index]) {
                            s2Index++;
                            if (s2Index == len2) {
                                count++;
                                s2Index = 0;
                            }
                        }
                    }
                }
                break;
            } else {
                indexMap[index] = i;
                countMap[index] = count;
            }
            
            index = s2Index;
        }
        
        return count / n2;
    }
};

/**
 * 测试函数
 */
void testCountRepetitions() {
    cout << "=== LeetCode 466 统计重复个数 测试 ===" << endl;
    
    // 测试用例1：基础测试
    string s1_1 = "abc";
    int n1_1 = 4;
    string s2_1 = "ab";
    int n2_1 = 2;
    
    int result1 = CountRepetitions::getMaxRepetitions(s1_1, n1_1, s2_1, n2_1);
    cout << "测试用例1 - 预期: 1, 实际: " << result1 << endl;
    
    // 测试用例2：经典测试
    string s1_2 = "acb";
    int n1_2 = 4;
    string s2_2 = "ab";
    int n2_2 = 2;
    
    int result2 = CountRepetitions::getMaxRepetitions(s1_2, n1_2, s2_2, n2_2);
    cout << "测试用例2 - 预期: 2, 实际: " << result2 << endl;
    
    // 测试用例3：边界测试
    string s1_3 = "a";
    int n1_3 = 1000000;
    string s2_3 = "a";
    int n2_3 = 1;
    
    int result3 = CountRepetitions::getMaxRepetitions(s1_3, n1_3, s2_3, n2_3);
    cout << "测试用例3 - 预期: 1000000, 实际: " << result3 << endl;
    
    // 测试用例4：无法匹配的情况
    string s1_4 = "abc";
    int n1_4 = 1;
    string s2_4 = "d";
    int n2_4 = 1;
    
    int result4 = CountRepetitions::getMaxRepetitions(s1_4, n1_4, s2_4, n2_4);
    cout << "测试用例4 - 预期: 0, 实际: " << result4 << endl;
    
    // 测试简化版本
    int result1_simple = CountRepetitions::getMaxRepetitionsSimple(s1_1, n1_1, s2_1, n2_1);
    cout << "简化版本测试用例1 - 预期: 1, 实际: " << result1_simple << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 大规模测试
    string s1 = "abc";
    int n1 = 1000000;
    string s2 = "ab";
    int n2 = 1;
    
    auto start = chrono::high_resolution_clock::now();
    int result1 = CountRepetitions::getMaxRepetitions(s1, n1, s2, n2);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    start = chrono::high_resolution_clock::now();
    int result2 = CountRepetitions::getMaxRepetitionsSimple(s1, n1, s2, n2);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "大规模测试 - 标准版本结果: " << result1 << ", 耗时: " << duration1.count() << "微秒" << endl;
    cout << "大规模测试 - 简化版本结果: " << result2 << ", 耗时: " << duration2.count() << "微秒" << endl;
    
    cout << "=== 性能测试完成 ===" << endl;
}

/**
 * 主函数
 */
int main() {
    testCountRepetitions();
    performanceTest();
    return 0;
}

/**
 * 复杂度分析：
 * 时间复杂度：
 * - 预处理next数组：O(len1 * 26)
 * - 计算匹配长度：O(len1 * len2)
 * - 构建倍增表：O(len1 * log(totalLen))
 * - 查询：O(log(totalLen))
 * - 总复杂度：O(len1 * len2 + log(n1 * len1))
 * 
 * 空间复杂度：O(len1 * log(totalLen))
 * 
 * 算法优化点：
 * 1. 使用倍增思想将线性查询优化为对数级别
 * 2. 预处理避免重复计算
 * 3. 提供简化版本处理特殊情况
 * 
 * 工程化改进：
 * 1. 添加完整的边界条件检查
 * 2. 提供两种实现版本（标准和简化）
 * 3. 包含性能测试和功能测试
 * 4. 详细的注释和文档
 * 
 * 相关题目对比：
 * - LeetCode 686: 重复叠加字符串匹配
 * - LeetCode 28: 实现strStr()
 * - LeetCode 139: 单词拆分
 */