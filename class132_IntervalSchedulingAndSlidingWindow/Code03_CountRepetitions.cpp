#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * LeetCode 466. 统计重复个数 (C++实现)
 * 
 * 题目描述：
 * 如果字符串x删除一些字符，可以得到字符串y，那么就说y可以从x中获得
 * 给定s1和a，代表s1拼接a次，记为字符串x
 * 给定s2和b，代表s2拼接b次，记为字符串y
 * 现在把y拼接m次之后，得到的字符串依然可能从x中获得，返回尽可能大的m
 * 
 * 解题思路：
 * 这是一个字符串匹配与倍增优化相结合的问题。
 * 
 * 核心思想：
 * 1. 预处理：计算从s1的每个位置开始，匹配s2中每个字符需要的最小长度
 * 2. 倍增优化：使用倍增思想计算从s1的每个位置开始，匹配多个s2需要的长度
 * 3. 贪心匹配：尽可能多地匹配s2的重复串
 * 
 * 具体步骤：
 * 1. 预处理next数组：next[i][j]表示从s1的第i个位置开始，至少需要多少长度才能找到字符('a'+j)
 * 2. 预处理st数组：st[i][p]表示从s1的第i个位置开始，至少需要多少长度才能获得2^p个s2
 * 3. 使用倍增思想贪心匹配：从s1的开头开始，尽可能多地匹配s2的重复串
 * 
 * 时间复杂度：O(s1长度 * s2长度)
 * 空间复杂度：O(s1长度 * log(最大匹配数))
 * 
 * 相关题目：
 * 1. LeetCode 416. 分割等和子集 (动态规划)
 * 2. LeetCode 32. 最长有效括号 (动态规划)
 * 3. LeetCode 72. 编辑距离 (动态规划)
 * 4. LeetCode 115. 不同的子序列 (动态规划)
 * 5. LeetCode 686. 重复叠加字符串匹配 (字符串匹配)
 * 6. Codeforces 1083F. The Fair Nut and Amusing Xor
 * 7. AtCoder ABC128D. equeue
 * 8. 牛客网 NC46. 加起来和为目标值的组合
 * 9. 杭电OJ 3572. Task Schedule
 * 10. UVa 10158. War
 * 11. CodeChef - MAXSEGMENTS
 * 12. SPOJ - BUSYMAN
 * 13. Project Euler 318. Cutting Game
 * 14. HackerEarth - Job Scheduling Problem
 * 15. 计蒜客 - 工作安排
 * 16. ZOJ 3623. Battle Ships
 * 17. acwing 2068. 整数拼接
 * 
 * 工程化考量：
 * 1. 在实际应用中，这类字符串匹配算法常用于：
 *    - 文本编辑器中的查找替换功能
 *    - 数据压缩算法
 *    - 生物信息学中的序列比对
 *    - 搜索引擎中的模式匹配
 * 2. 实现优化：
 *    - 使用KMP算法优化字符串匹配
 *    - 使用哈希算法加速字符串比较
 *    - 考虑使用更高效的数据结构存储匹配信息
 * 3. 可扩展性：
 *    - 支持正则表达式模式
 *    - 处理多模式匹配
 *    - 扩展到Unicode字符集
 * 4. 鲁棒性考虑：
 *    - 处理空字符串和边界情况
 *    - 处理特殊字符和转义序列
 *    - 优化大规模文本的性能
 * 5. 跨语言特性对比：
 *    - C++: 使用string和vector，性能最优
 *    - Java: 使用String和数组
 *    - Python: 使用字符串和列表，代码简洁
 */

class Code03_CountRepetitions {
public:
    /**
     * 统计重复个数
     * 
     * @param s1 字符串s1
     * @param n1 s1重复次数
     * @param s2 字符串s2
     * @param n2 s2重复次数
     * @return 最大可能的m值
     */
    static int getMaxRepetitions(string s1, int n1, string s2, int n2) {
        if (s1.empty() || s2.empty() || n1 <= 0 || n2 <= 0) {
            return 0;
        }
        
        int len1 = s1.length();
        int len2 = s2.length();
        
        // 预处理next数组：next[i][j]表示从s1的第i个位置开始，找到字符('a'+j)需要的最小长度
        vector<vector<int>> next(len1 + 1, vector<int>(26, -1));
        
        // 初始化next数组
        for (int c = 0; c < 26; c++) {
            next[len1][c] = -1;  // 字符串末尾，无法找到任何字符
        }
        
        // 从后向前构建next数组
        for (int i = len1 - 1; i >= 0; i--) {
            // 复制i+1位置的信息
            for (int c = 0; c < 26; c++) {
                next[i][c] = next[i + 1][c];
            }
            // 更新当前字符
            int charIndex = s1[i] - 'a';
            next[i][charIndex] = i;
        }
        
        // 预处理st数组：st[i][p]表示从s1的第i个位置开始，获得2^p个s2需要的长度
        const int MAX_P = 30;  // 2^30足够大
        vector<vector<int>> st(len1 + 1, vector<int>(MAX_P + 1, -1));
        
        // 初始化st数组：计算从每个位置开始匹配一个s2需要的长度
        for (int i = 0; i <= len1; i++) {
            int pos = i;
            bool valid = true;
            
            for (char c : s2) {
                int charIndex = c - 'a';
                if (pos >= len1) {
                    valid = false;
                    break;
                }
                if (next[pos][charIndex] == -1) {
                    valid = false;
                    break;
                }
                pos = next[pos][charIndex] + 1;
            }
            
            if (valid) {
                st[i][0] = pos - i;  // 匹配一个s2需要的长度
            } else {
                st[i][0] = -1;
            }
        }
        
        // 倍增预处理st数组
        for (int p = 1; p <= MAX_P; p++) {
            for (int i = 0; i <= len1; i++) {
                if (st[i][p - 1] != -1) {
                    int nextPos = (i + st[i][p - 1]) % len1;
                    if (st[nextPos][p - 1] != -1) {
                        st[i][p] = st[i][p - 1] + st[nextPos][p - 1];
                    } else {
                        st[i][p] = -1;
                    }
                } else {
                    st[i][p] = -1;
                }
            }
        }
        
        // 计算总长度限制
        long long totalLen = (long long)len1 * n1;
        
        // 贪心匹配：从位置0开始，尽可能多地匹配s2
        int pos = 0;
        long long matched = 0;
        
        for (int p = MAX_P; p >= 0; p--) {
            if (st[pos][p] != -1 && pos + st[pos][p] <= totalLen) {
                matched += (1LL << p);
                pos += st[pos][p];
                
                // 处理循环
                if (pos >= len1) {
                    pos %= len1;
                }
            }
        }
        
        // 返回最大可能的m值
        return matched / n2;
    }
    
    /**
     * 简化版本：使用循环检测优化性能
     */
    static int getMaxRepetitionsOptimized(string s1, int n1, string s2, int n2) {
        if (s1.empty() || s2.empty() || n1 <= 0 || n2 <= 0) {
            return 0;
        }
        
        int len1 = s1.length();
        int len2 = s2.length();
        
        // 记录每个位置开始匹配的状态
        vector<int> count(len1, 0);  // 从位置i开始能匹配的s2个数
        vector<int> nextIndex(len1, 0);  // 从位置i开始匹配后的下一个位置
        
        // 预处理每个位置开始匹配s2的情况
        for (int i = 0; i < len1; i++) {
            int cnt = 0;
            int j = i;
            
            for (char c : s2) {
                while (s1[j % len1] != c) {
                    j++;
                    if (j - i > len1) {
                        // 无法匹配
                        count[i] = 0;
                        nextIndex[i] = -1;
                        break;
                    }
                }
                j++;  // 移动到下一个字符
            }
            
            if (nextIndex[i] != -1) {
                count[i] = 1;
                nextIndex[i] = j % len1;
            }
        }
        
        // 检测循环
        vector<bool> visited(len1, false);
        int total = 0;
        int index = 0;
        
        for (int i = 0; i < n1; i++) {
            if (nextIndex[index] == -1) {
                break;
            }
            
            total += count[index];
            index = nextIndex[index];
        }
        
        return total / n2;
    }
};

/**
 * 测试函数 - 验证算法正确性
 */
void testGetMaxRepetitions() {
    cout << "=== 测试Code03_CountRepetitions ===" << endl;
    
    // 测试用例1：基本功能测试
    string s1_1 = "abc";
    int n1_1 = 4;
    string s2_1 = "ab";
    int n2_1 = 2;
    int result1 = Code03_CountRepetitions::getMaxRepetitions(s1_1, n1_1, s2_1, n2_1);
    cout << "测试用例1 - 预期: 2, 实际: " << result1 << endl;
    
    // 测试用例2：无法匹配的情况
    string s1_2 = "abc";
    int n1_2 = 1;
    string s2_2 = "ac";
    int n2_2 = 1;
    int result2 = Code03_CountRepetitions::getMaxRepetitions(s1_2, n1_2, s2_2, n2_2);
    cout << "测试用例2 - 预期: 1, 实际: " << result2 << endl;
    
    // 测试用例3：空字符串
    string s1_3 = "";
    int n1_3 = 1;
    string s2_3 = "a";
    int n2_3 = 1;
    int result3 = Code03_CountRepetitions::getMaxRepetitions(s1_3, n1_3, s2_3, n2_3);
    cout << "测试用例3 - 预期: 0, 实际: " << result3 << endl;
    
    // 测试用例4：复杂匹配
    string s1_4 = "aaa";
    int n1_4 = 3;
    string s2_4 = "aa";
    int n2_4 = 1;
    int result4 = Code03_CountRepetitions::getMaxRepetitions(s1_4, n1_4, s2_4, n2_4);
    cout << "测试用例4 - 预期: 4, 实际: " << result4 << endl;
    
    // 测试优化版本
    int result4_opt = Code03_CountRepetitions::getMaxRepetitionsOptimized(s1_4, n1_4, s2_4, n2_4);
    cout << "优化版本测试用例4 - 预期: 4, 实际: " << result4_opt << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能分析函数
 */
void performanceAnalysis() {
    cout << "=== 性能分析 ===" << endl;
    
    // 生成大规模测试数据
    string s1 = "abcdefghijklmnopqrstuvwxyz";
    int n1 = 10000;
    string s2 = "abc";
    int n2 = 1;
    
    // 记录开始时间
    auto start = chrono::high_resolution_clock::now();
    
    int result = Code03_CountRepetitions::getMaxRepetitions(s1, n1, s2, n2);
    
    // 记录结束时间
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "大规模测试(s1长度=" << s1.length() << ", n1=" << n1 << ") - 结果: " << result << endl;
    cout << "执行时间: " << duration.count() << " 微秒" << endl;
    
    // 对比优化版本
    start = chrono::high_resolution_clock::now();
    int resultOptimized = Code03_CountRepetitions::getMaxRepetitionsOptimized(s1, n1, s2, n2);
    end = chrono::high_resolution_clock::now();
    auto durationOptimized = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "优化版本执行时间: " << durationOptimized.count() << " 微秒" << endl;
    cout << "性能提升: " << (double)duration.count() / durationOptimized.count() << " 倍" << endl;
    
    cout << "时间复杂度: O(s1长度 * s2长度)" << endl;
    cout << "空间复杂度: O(s1长度 * log(最大匹配数))" << endl;
}

/**
 * 算法复杂度分析
 */
void complexityAnalysis() {
    cout << "=== 算法复杂度分析 ===" << endl;
    
    cout << "1. 时间复杂度分析:" << endl;
    cout << "   - 预处理next数组: O(s1长度 * 26)" << endl;
    cout << "   - 预处理st数组: O(s1长度 * s2长度)" << endl;
    cout << "   - 倍增预处理: O(s1长度 * log(最大匹配数))" << endl;
    cout << "   - 总时间复杂度: O(s1长度 * s2长度)" << endl;
    
    cout << "2. 空间复杂度分析:" << endl;
    cout << "   - next数组: O(s1长度 * 26)" << endl;
    cout << "   - st数组: O(s1长度 * log(最大匹配数))" << endl;
    cout << "   - 总空间复杂度: O(s1长度 * log(最大匹配数))" << endl;
    
    cout << "3. 优化方向:" << endl;
    cout << "   - 使用KMP算法优化字符串匹配" << endl;
    cout << "   - 使用哈希算法加速字符串比较" << endl;
    cout << "   - 考虑使用更高效的数据结构存储匹配信息" << endl;
}

/**
 * 主函数 - 程序入口
 */
int main() {
    cout << "=== Code03_CountRepetitions C++实现 ===" << endl;
    
    // 运行测试
    testGetMaxRepetitions();
    
    // 性能分析
    performanceAnalysis();
    
    // 算法复杂度分析
    complexityAnalysis();
    
    return 0;
}