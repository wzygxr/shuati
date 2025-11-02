#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;

/**
 * LeetCode 474. 一和零
 * 给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
 * 请你找出并返回 strs 的最大子集的大小，该子集中最多有 m 个 0 和 n 个 1 。
 * 测试链接：https://leetcode.cn/problems/ones-and-zeroes/
 * 
 * 算法详解：
 * 将问题转化为二维费用的0-1背包问题，使用动态规划求解。
 * 
 * 时间复杂度：O(len * m * n + L)，其中L是所有字符串总长度
 * 空间复杂度：O(m * n)（优化版本）
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数有效性
 * 2. 边界处理：m或n为0的情况
 * 3. 性能优化：使用空间优化技术
 * 4. 代码质量：清晰的变量命名和状态转移逻辑
 */

class Solution {
public:
    /**
     * 空间优化版本（二维DP）
     * 时间复杂度：O(len * m * n + L)
     * 空间复杂度：O(m * n)
     */
    static int findMaxForm(vector<string>& strs, int m, int n) {
        if (strs.empty() || m < 0 || n < 0) {
            return 0;
        }
        
        // dp[i][j]表示使用i个0和j个1时的最大子集大小
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        
        for (const string& str : strs) {
            // 统计当前字符串的0和1数量
            auto counts = countZeroesAndOnes(str);
            int zeroes = counts.first;
            int ones = counts.second;
            
            // 从后往前更新，避免重复使用
            for (int i = m; i >= zeroes; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = max(dp[i][j], dp[i - zeroes][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 带剪枝的优化版本
     * 时间复杂度：O(len * m * n + L)
     * 空间复杂度：O(m * n)
     */
    static int findMaxFormWithPruning(vector<string>& strs, int m, int n) {
        if (strs.empty() || m < 0 || n < 0) {
            return 0;
        }
        
        // 按字符串长度排序（短字符串优先）
        sort(strs.begin(), strs.end(), [](const string& a, const string& b) {
            return a.length() < b.length();
        });
        
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        
        for (const string& str : strs) {
            auto counts = countZeroesAndOnes(str);
            int zeroes = counts.first;
            int ones = counts.second;
            
            // 剪枝：如果0或1数量超过限制，跳过该字符串
            if (zeroes > m || ones > n) {
                continue;
            }
            
            for (int i = m; i >= zeroes; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = max(dp[i][j], dp[i - zeroes][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }

private:
    /**
     * 统计字符串中0和1的数量
     * 时间复杂度：O(L)，其中L是字符串长度
     * 空间复杂度：O(1)
     */
    static pair<int, int> countZeroesAndOnes(const string& str) {
        int zeroes = 0, ones = 0;
        for (char c : str) {
            if (c == '0') zeroes++;
            else ones++;
        }
        return {zeroes, ones};
    }
};

/**
 * 测试辅助函数
 */
void runTest(const string& description, vector<string> strs, int m, int n, int expected) {
    cout << description << endl;
    cout << "输入字符串: [";
    for (size_t i = 0; i < strs.size(); i++) {
        cout << "\"" << strs[i] << "\"";
        if (i < strs.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "m = " << m << ", n = " << n << endl;
    cout << "期望结果: " << expected << endl;
    
    vector<string> strs1 = strs;
    vector<string> strs2 = strs;
    
    int result1 = Solution::findMaxForm(strs1, m, n);
    int result2 = Solution::findMaxFormWithPruning(strs2, m, n);
    
    cout << "优化DP: " << result1 << " " << (result1 == expected ? "✓" : "✗") << endl;
    cout << "剪枝优化: " << result2 << " " << (result2 == expected ? "✓" : "✗") << endl;
    
    if (result1 == result2 && result1 == expected) {
        cout << "测试通过 ✓" << endl;
    } else {
        cout << "测试失败 ✗" << endl;
    }
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成测试数据：大规模字符串数组
    const int len = 100;
    const int strLen = 10;
    vector<string> strs;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(0, 1);
    
    for (int i = 0; i < len; i++) {
        string str;
        for (int j = 0; j < strLen; j++) {
            str += (dis(gen) == 0 ? '0' : '1');
        }
        strs.push_back(str);
    }
    
    int m = 50, n = 50;
    
    cout << "测试数据规模: " << len << "个字符串" << endl;
    cout << "每个字符串长度: " << strLen << endl;
    cout << "m = " << m << ", n = " << n << endl;
    
    // 测试优化版本
    auto start = chrono::high_resolution_clock::now();
    int result1 = Solution::findMaxForm(strs, m, n);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "优化DP版本:" << endl;
    cout << "  结果: " << result1 << endl;
    cout << "  耗时: " << duration1.count() << " 毫秒" << endl;
    
    // 测试剪枝优化版本
    start = chrono::high_resolution_clock::now();
    int result2 = Solution::findMaxFormWithPruning(strs, m, n);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "剪枝优化版本:" << endl;
    cout << "  结果: " << result2 << endl;
    cout << "  耗时: " << duration2.count() << " 毫秒" << endl;
    
    // 验证结果一致性
    if (result1 == result2) {
        cout << "结果一致性验证: 通过 ✓" << endl;
    } else {
        cout << "结果一致性验证: 失败 ✗" << endl;
    }
    
    cout << endl;
}

int main() {
    cout << "=== LeetCode 474 一和零问题测试 ===" << endl << endl;
    
    // 测试用例1：基本功能测试
    runTest("测试用例1 - 基本功能", 
            {"10", "0001", "111001", "1", "0"}, 5, 3, 4);
    
    // 测试用例2：官方示例
    runTest("测试用例2 - 官方示例", 
            {"10", "0", "1"}, 1, 1, 2);
    
    // 测试用例3：m或n为0
    runTest("测试用例3 - m为0", 
            {"10", "0", "1"}, 0, 1, 1);
    
    // 测试用例4：空数组
    runTest("测试用例4 - 空数组", 
            {}, 5, 3, 0);
    
    // 测试用例5：所有字符串都相同
    runTest("测试用例5 - 全相同", 
            {"1", "1", "1", "1"}, 3, 3, 3);
    
    // 性能测试
    performanceTest();
    
    cout << "所有测试完成！" << endl;
    return 0;
}

/**
 * 复杂度分析详细计算：
 * 
 * 优化DP版本：
 * - 时间：预处理O(L) + DP计算O(len * m * n) → O(len * m * n + L)
 * - 空间：二维vector大小m×n → O(m * n)
 * 
 * 剪枝优化版本：
 * - 时间：O(len * m * n + L)，但实际运行可能更快
 * - 空间：O(m * n)
 * 
 * C++特性说明：
 * 1. 使用STL容器简化代码
 * 2. lambda表达式用于排序
 * 3. pair类型用于返回多个值
 * 4. chrono库进行精确性能测试
 * 
 * 工程化建议：
 * 1. 对于生产环境使用优化DP版本
 * 2. 添加异常处理确保程序健壮性
 * 3. 编写单元测试覆盖各种边界情况
 * 4. 使用性能分析工具优化关键路径
 */