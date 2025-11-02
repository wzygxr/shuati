#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <stdexcept>

using namespace std;

/**
 * LeetCode 72. 编辑距离
 * 给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数。
 * 你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
 * 测试链接：https://leetcode.cn/problems/edit-distance/
 * 
 * 算法详解：
 * 使用动态规划解决编辑距离问题，支持三种操作：插入、删除、替换。
 * 
 * 时间复杂度：O(m*n)，其中m和n分别是word1和word2的长度
 * 空间复杂度：O(min(m,n))，使用空间优化技术
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数有效性
 * 2. 边界处理：正确处理空字符串
 * 3. 性能优化：使用滚动数组减少内存使用
 * 4. 代码可读性：清晰的变量命名和注释
 */

class Solution {
public:
    /**
     * 基础动态规划解法
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    static int minDistance(const string& word1, const string& word2) {
        // 异常处理
        if (word1.empty() && word2.empty()) return 0;
        
        int m = word1.length();
        int n = word2.length();
        
        // 特殊情况处理
        if (m == 0) return n;
        if (n == 0) return m;
        
        // 创建dp表
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // 将word1前i个字符转换为空字符串需要i次删除
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // 将空字符串转换为word2前j个字符需要j次插入
        }
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1[i - 1] == word2[j - 1]) {
                    // 字符相同，不需要操作
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 字符不同，取三种操作的最小值
                    dp[i][j] = min({dp[i - 1][j],    // 删除
                                   dp[i][j - 1],    // 插入  
                                   dp[i - 1][j - 1] // 替换
                                  }) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 空间优化版本
     * 使用滚动数组将空间复杂度从O(m*n)优化到O(min(m,n))
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     */
    static int minDistanceOptimized(const string& word1, const string& word2) {
        int m = word1.length();
        int n = word2.length();
        
        // 特殊情况处理
        if (m == 0) return n;
        if (n == 0) return m;
        
        // 确保word1是较短的字符串以减少空间使用
        if (m > n) {
            return minDistanceOptimized(word2, word1);
        }
        
        // 使用两行数组存储状态
        vector<int> prev(m + 1, 0);
        vector<int> curr(m + 1, 0);
        
        // 初始化第一行
        for (int i = 0; i <= m; i++) {
            prev[i] = i;
        }
        
        // 填充dp表
        for (int j = 1; j <= n; j++) {
            curr[0] = j; // 当前行的第一个元素
            
            for (int i = 1; i <= m; i++) {
                if (word1[i - 1] == word2[j - 1]) {
                    curr[i] = prev[i - 1];
                } else {
                    curr[i] = min({prev[i],     // 删除
                                 curr[i - 1], // 插入
                                 prev[i - 1]  // 替换
                                }) + 1;
                }
            }
            
            // 交换数组，准备下一轮
            swap(prev, curr);
        }
        
        return prev[m];
    }
    
    /**
     * 进一步空间优化版本（使用一维数组）
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     */
    static int minDistanceSuperOptimized(const string& word1, const string& word2) {
        int m = word1.length();
        int n = word2.length();
        
        if (m == 0) return n;
        if (n == 0) return m;
        
        // 确保word1是较短的字符串
        if (m > n) {
            return minDistanceSuperOptimized(word2, word1);
        }
        
        vector<int> dp(m + 1, 0);
        
        // 初始化：将空字符串转换为word1的前i个字符需要i次删除
        for (int i = 0; i <= m; i++) {
            dp[i] = i;
        }
        
        for (int j = 1; j <= n; j++) {
            int prev = dp[0]; // 保存左上角的值
            dp[0] = j;       // 当前行的第一个元素
            
            for (int i = 1; i <= m; i++) {
                int temp = dp[i]; // 保存当前值
                
                if (word1[i - 1] == word2[j - 1]) {
                    dp[i] = prev;
                } else {
                    dp[i] = min({dp[i],     // 删除
                               dp[i - 1], // 插入
                               prev       // 替换
                              }) + 1;
                }
                
                prev = temp; // 更新左上角的值
            }
        }
        
        return dp[m];
    }
};

/**
 * 单元测试函数
 * 验证算法的正确性和各种边界情况
 */
void runTests() {
    cout << "=== LeetCode 72 编辑距离测试 ===" << endl;
    
    // 测试用例1：基本功能测试
    string word1 = "horse";
    string word2 = "ros";
    cout << "测试用例1 - 基础功能:" << endl;
    cout << "word1 = " << word1 << ", word2 = " << word2 << endl;
    cout << "基础版本: " << Solution::minDistance(word1, word2) << endl;
    cout << "优化版本: " << Solution::minDistanceOptimized(word1, word2) << endl;
    cout << "超级优化: " << Solution::minDistanceSuperOptimized(word1, word2) << endl;
    cout << "期望结果: 3" << endl << endl;
    
    // 测试用例2：相同单词
    word1 = "intention";
    word2 = "intention";
    cout << "测试用例2 - 相同单词:" << endl;
    cout << "word1 = " << word1 << ", word2 = " << word2 << endl;
    cout << "基础版本: " << Solution::minDistance(word1, word2) << endl;
    cout << "优化版本: " << Solution::minDistanceOptimized(word1, word2) << endl;
    cout << "超级优化: " << Solution::minDistanceSuperOptimized(word1, word2) << endl;
    cout << "期望结果: 0" << endl << endl;
    
    // 测试用例3：完全不同的单词
    word1 = "abc";
    word2 = "def";
    cout << "测试用例3 - 完全不同单词:" << endl;
    cout << "word1 = " << word1 << ", word2 = " << word2 << endl;
    cout << "基础版本: " << Solution::minDistance(word1, word2) << endl;
    cout << "优化版本: " << Solution::minDistanceOptimized(word1, word2) << endl;
    cout << "超级优化: " << Solution::minDistanceSuperOptimized(word1, word2) << endl;
    cout << "期望结果: 3" << endl << endl;
    
    // 测试用例4：空字符串
    word1 = "";
    word2 = "abc";
    cout << "测试用例4 - 空字符串:" << endl;
    cout << "word1 = " << word1 << ", word2 = " << word2 << endl;
    cout << "基础版本: " << Solution::minDistance(word1, word2) << endl;
    cout << "优化版本: " << Solution::minDistanceOptimized(word1, word2) << endl;
    cout << "超级优化: " << Solution::minDistanceSuperOptimized(word1, word2) << endl;
    cout << "期望结果: 3" << endl << endl;
    
    // 测试用例5：两个空字符串
    word1 = "";
    word2 = "";
    cout << "测试用例5 - 两个空字符串:" << endl;
    cout << "word1 = " << word1 << ", word2 = " << word2 << endl;
    cout << "基础版本: " << Solution::minDistance(word1, word2) << endl;
    cout << "优化版本: " << Solution::minDistanceOptimized(word1, word2) << endl;
    cout << "超级优化: " << Solution::minDistanceSuperOptimized(word1, word2) << endl;
    cout << "期望结果: 0" << endl << endl;
    
    // 测试用例6：LeetCode官方示例
    word1 = "intention";
    word2 = "execution";
    cout << "测试用例6 - LeetCode示例:" << endl;
    cout << "word1 = " << word1 << ", word2 = " << word2 << endl;
    cout << "基础版本: " << Solution::minDistance(word1, word2) << endl;
    cout << "优化版本: " << Solution::minDistanceOptimized(word1, word2) << endl;
    cout << "超级优化: " << Solution::minDistanceSuperOptimized(word1, word2) << endl;
    cout << "期望结果: 5" << endl << endl;
}

/**
 * 性能测试函数
 * 测试算法在大规模数据下的表现
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成测试数据
    string word1(100, 'a'); // 100个'a'
    string word2(100, 'b'); // 100个'b'
    
    auto start = chrono::high_resolution_clock::now();
    int result = Solution::minDistanceOptimized(word1, word2);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "测试数据: 100个字符" << endl;
    cout << "结果: " << result << endl;
    cout << "耗时: " << duration.count() << " 微秒" << endl;
    cout << "期望结果: 100" << endl;
}

int main() {
    try {
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        cout << "所有测试通过！" << endl;
    } catch (const exception& e) {
        cerr << "测试过程中发生错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}

/**
 * 复杂度分析详细计算：
 * 
 * 基础版本：
 * - 时间：外层循环m次，内层循环n次，每次操作O(1) → O(m*n)
 * - 空间：dp数组大小(m+1)*(n+1) → O(m*n)
 * 
 * 优化版本：
 * - 时间：同上 → O(m*n)
 * - 空间：两个vector，每个大小min(m,n)+1 → O(min(m,n))
 * 
 * 超级优化版本：
 * - 时间：同上 → O(m*n)  
 * - 空间：一个vector，大小min(m,n)+1 → O(min(m,n))
 * 
 * C++特性优势：
 * 1. STL容器提供高效的内存管理
 * 2. 模板函数支持泛型编程
 * 3. 引用传递避免不必要的拷贝
 * 4. RAII机制自动管理资源
 * 
 * 与Java/Python的差异：
 * 1. 手动内存管理更灵活但需要更小心
 * 2. 性能通常优于Java和Python
 * 3. 编译时类型检查更严格
 */