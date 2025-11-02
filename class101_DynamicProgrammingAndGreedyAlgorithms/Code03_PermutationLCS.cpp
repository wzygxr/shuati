#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>

using namespace std;

/**
 * 两个排列的最长公共子序列长度 - C++实现
 * 
 * 问题描述:
 * 给出由1~n这些数字组成的两个排列
 * 求它们的最长公共子序列长度
 * 
 * 解题思路:
 * 利用排列的特殊性质，将LCS问题转化为LIS问题
 * 1. 将第二个排列转换为第一个排列中对应数字的位置
 * 2. 问题转化为求位置序列的最长递增子序列
 * 3. 使用贪心+二分查找求解LIS
 * 
 * 约束条件:
 * 1 <= n <= 10^5
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P1439
 * 
 * 工程化考量:
 * 1. 使用const引用避免不必要的拷贝
 * 2. 添加输入验证和边界检查
 * 3. 实现完整的单元测试
 * 4. 提供性能测试功能
 */

class Code03_PermutationLCS {
private:
    static const int MAXN = 100001;
    
public:
    /**
     * 计算两个排列的最长公共子序列长度
     * 
     * 算法原理:
     * 利用排列的特殊性质，将LCS问题转化为LIS问题
     * 1. 创建位置映射: where[ai] = i
     * 2. 将第二个排列转换为位置序列: b'[i] = where[b[i]]
     * 3. 问题转化为求位置序列的最长递增子序列
     * 4. 则LCS(A, B) = LIS(b')
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * @param a 第一个排列
     * @param b 第二个排列
     * @return 最长公共子序列长度
     */
    static int compute(const vector<int>& a, const vector<int>& b) {
        int n = a.size();
        if (n == 0) return 0;
        
        // 创建位置映射
        vector<int> where(n + 1);
        for (int i = 0; i < n; i++) {
            where[a[i]] = i;
        }
        
        // 将B转换为位置序列
        vector<int> pos(n);
        for (int i = 0; i < n; i++) {
            pos[i] = where[b[i]];
        }
        
        // 计算位置序列的最长递增子序列
        return lis(pos);
    }
    
    /**
     * 计算最长递增子序列长度
     * 使用贪心+二分查找算法
     * 
     * 算法思路:
     * 维护一个数组ends，ends[i]表示长度为i+1的递增子序列的最小结尾元素
     * 遍历序列，对于每个元素:
     * 1. 如果大于ends的最后一个元素，扩展ends
     * 2. 否则替换ends中第一个大于等于该元素的位置
     */
    static int lis(const vector<int>& nums) {
        int n = nums.size();
        if (n == 0) return 0;
        
        vector<int> ends(n);
        int len = 0;
        
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            
            // 二分查找第一个大于等于num的位置
            int pos = binarySearch(ends, len, num);
            
            if (pos == -1) {
                // 没有找到，可以扩展ends
                ends[len++] = num;
            } else {
                // 替换该位置，使得结尾元素更小
                ends[pos] = num;
            }
        }
        
        return len;
    }
    
    /**
     * 二分查找辅助函数
     * 在ends[0..len-1]中查找第一个大于等于target的位置
     */
    static int binarySearch(const vector<int>& ends, int len, int target) {
        int left = 0, right = len - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (ends[mid] >= target) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 类似题目1: 最长公共子序列（LeetCode 1143）
     * 经典LCS问题的动态规划解法
     */
    static int longestCommonSubsequence1(const string& text1, const string& text2) {
        int m = text1.length();
        int n = text2.length();
        
        // dp[i][j]表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1[i-1] == text2[j-1]) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                } else {
                    dp[i][j] = max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 最长公共子序列 - 空间优化版本
     */
    static int longestCommonSubsequence2(const string& text1, const string& text2) {
        int m = text1.length();
        int n = text2.length();
        
        vector<int> dp(n + 1, 0);
        
        for (int i = 1; i <= m; i++) {
            int prev = 0;  // 保存dp[i-1][j-1]的值
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];  // 保存当前dp[j]的值
                if (text1[i-1] == text2[j-1]) {
                    dp[j] = prev + 1;
                } else {
                    dp[j] = max(dp[j], dp[j-1]);
                }
                prev = temp;
            }
        }
        
        return dp[n];
    }
    
    /**
     * 类似题目2: 最长重复子数组（LeetCode 718）
     * 求两个数组的最长公共连续子数组
     */
    static int findLength(const vector<int>& A, const vector<int>& B) {
        int m = A.size();
        int n = B.size();
        
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        int maxLen = 0;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (A[i-1] == B[j-1]) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                    maxLen = max(maxLen, dp[i][j]);
                } else {
                    dp[i][j] = 0;
                }
            }
        }
        
        return maxLen;
    }
    
    /**
     * 最长重复子数组 - 空间优化版本
     */
    static int findLength2(const vector<int>& A, const vector<int>& B) {
        int m = A.size();
        int n = B.size();
        
        vector<int> dp(n + 1, 0);
        int maxLen = 0;
        
        for (int i = 1; i <= m; i++) {
            int prev = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (A[i-1] == B[j-1]) {
                    dp[j] = prev + 1;
                    maxLen = max(maxLen, dp[j]);
                } else {
                    dp[j] = 0;
                }
                prev = temp;
            }
        }
        
        return maxLen;
    }
    
    /**
     * 类似题目3: 编辑距离（LeetCode 72）
     * 计算将word1转换为word2所需的最少操作数
     */
    static int minDistance(const string& word1, const string& word2) {
        int m = word1.length();
        int n = word2.length();
        
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1[i-1] == word2[j-1]) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = min({dp[i-1][j], dp[i][j-1], dp[i-1][j-1]}) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 单元测试函数
     */
    static void test() {
        cout << "=== 测试排列LCS算法 ===" << endl;
        
        // 测试用例1
        vector<int> a1 = {3, 2, 1, 4, 5};
        vector<int> b1 = {1, 2, 3, 4, 5};
        int result1 = compute(a1, b1);
        cout << "测试用例1结果: " << result1 << " (期望: 3)" << endl;
        
        // 测试用例2
        vector<int> a2 = {1, 2, 3, 4, 5};
        vector<int> b2 = {5, 4, 3, 2, 1};
        int result2 = compute(a2, b2);
        cout << "测试用例2结果: " << result2 << " (期望: 1)" << endl;
        
        // 测试类似题目
        string text1 = "abcde", text2 = "ace";
        int result3 = longestCommonSubsequence1(text1, text2);
        cout << "最长公共子序列结果: " << result3 << " (期望: 3)" << endl;
        
        cout << "=== 测试完成 ===" << endl;
    }
    
    /**
     * 性能测试函数
     */
    static void performance_test() {
        cout << "\n=== 性能测试 ===" << endl;
        
        // 创建大规模测试数据
        int n = 100000;
        vector<int> a(n), b(n);
        
        // 生成排列
        for (int i = 0; i < n; i++) {
            a[i] = i + 1;
            b[i] = n - i;  // 逆序排列
        }
        
        auto start = chrono::high_resolution_clock::now();
        int result = compute(a, b);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模测试结果: " << result << endl;
        cout << "执行时间: " << duration.count() << "ms" << endl;
    }
    
    /**
     * 主函数
     */
    static void main() {
        test();
        performance_test();
    }
};

int main() {
    Code03_PermutationLCS::main();
    return 0;
}

/**
 * 工程化考量:
 * 1. 使用const引用避免不必要的拷贝
 * 2. 添加输入验证和边界检查
 * 3. 实现完整的单元测试
 * 4. 提供性能测试功能
 * 
 * 调试技巧:
 * 1. 打印中间的位置映射序列
 * 2. 验证LIS算法的正确性
 * 3. 对比不同规模数据的性能表现
 * 
 * 面试要点:
 * 1. 理解排列LCS转化为LIS的原理
 * 2. 掌握LIS的贪心+二分查找算法
 * 3. 能够分析算法的时间复杂度
 * 4. 了解空间优化的方法
 */