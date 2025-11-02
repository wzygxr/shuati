// LeetCode 474. 一和零
// 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
// 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
// 链接：https://leetcode.cn/problems/ones-and-zeroes/
// 
// 解题思路：
// 这是一个二维费用的0-1背包问题。每个字符串可以看作是一个物品，它有两个费用：0的数量和1的数量。
// 我们的背包有两个容量限制：最多可以使用m个0和n个1。我们的目标是选择尽可能多的物品（字符串）。
// 
// 状态定义：dp[i][j] 表示使用i个0和j个1时，最多可以选择的字符串数量
// 状态转移方程：dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)，其中zeros和ones是当前字符串的0和1的数量
// 初始状态：dp[0][0] = 0，其他初始化为0
// 
// 时间复杂度：O(l * m * n)，其中l是字符串数组的长度
// 空间复杂度：O(m * n)，使用二维DP数组

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 统计字符串中0和1的数量
 * @param s 二进制字符串
 * @return 一个vector，第一个元素是0的数量，第二个元素是1的数量
 */
vector<int> countZeroesOnes(const string& s) {
    vector<int> counts(2, 0);
    for (char c : s) {
        counts[c - '0']++;
    }
    return counts;
}

/**
 * 计算最大子集的长度
 * @param strs 二进制字符串数组
 * @param m 最多可以使用的0的数量
 * @param n 最多可以使用的1的数量
 * @return 最大子集的长度
 */
int findMaxForm(vector<string>& strs, int m, int n) {
    if (strs.empty()) {
        return 0;
    }
    
    // 创建二维DP数组，dp[i][j]表示使用i个0和j个1时，最多可以选择的字符串数量
    vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
    
    // 遍历每个字符串
    for (const string& str : strs) {
        // 统计当前字符串中0和1的数量
        vector<int> counts = countZeroesOnes(str);
        int zeros = counts[0];
        int ones = counts[1];
        
        // 逆序遍历，避免重复使用同一个字符串
        for (int i = m; i >= zeros; i--) {
            for (int j = n; j >= ones; j--) {
                // 更新状态：不选当前字符串 或 选当前字符串（如果可以的话）
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1);
            }
        }
    }
    
    return dp[m][n];
}

/**
 * 使用三维DP数组的版本（更直观但空间效率较低）
 * @param strs 二进制字符串数组
 * @param m 最多可以使用的0的数量
 * @param n 最多可以使用的1的数量
 * @return 最大子集的长度
 */
int findMaxForm3D(vector<string>& strs, int m, int n) {
    if (strs.empty()) {
        return 0;
    }
    
    int l = strs.size();
    // 创建三维DP数组，dp[k][i][j]表示前k个字符串中，使用i个0和j个1时，最多可以选择的字符串数量
    vector<vector<vector<int>>> dp(l + 1, vector<vector<int>>(m + 1, vector<int>(n + 1, 0)));
    
    // 遍历每个字符串
    for (int k = 1; k <= l; k++) {
        string str = strs[k - 1];
        vector<int> counts = countZeroesOnes(str);
        int zeros = counts[0];
        int ones = counts[1];
        
        // 遍历0的数量
        for (int i = 0; i <= m; i++) {
            // 遍历1的数量
            for (int j = 0; j <= n; j++) {
                // 不选第k个字符串
                dp[k][i][j] = dp[k - 1][i][j];
                
                // 选第k个字符串（如果可以的话）
                if (i >= zeros && j >= ones) {
                    dp[k][i][j] = max(dp[k][i][j], dp[k - 1][i - zeros][j - ones] + 1);
                }
            }
        }
    }
    
    return dp[l][m][n];
}

/**
 * 优化的二维DP版本，将统计0和1的过程提前
 * @param strs 二进制字符串数组
 * @param m 最多可以使用的0的数量
 * @param n 最多可以使用的1的数量
 * @return 最大子集的长度
 */
int findMaxFormOptimized(vector<string>& strs, int m, int n) {
    if (strs.empty()) {
        return 0;
    }
    
    // 预先统计所有字符串中0和1的数量
    vector<pair<int, int>> counts;
    for (const string& str : strs) {
        vector<int> cnt = countZeroesOnes(str);
        counts.push_back({cnt[0], cnt[1]});
    }
    
    // 创建二维DP数组
    vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
    
    // 遍历每个字符串
    for (const auto& count : counts) {
        int zeros = count.first;
        int ones = count.second;
        
        // 逆序遍历
        for (int i = m; i >= zeros; i--) {
            for (int j = n; j >= ones; j--) {
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1);
            }
        }
    }
    
    return dp[m][n];
}

// 记忆化搜索所需的辅助数组
template<typename T>
void printVector(const vector<T>& v) {
    for (const auto& item : v) {
        cout << item << " ";
    }
    cout << endl;
}

void printMatrix(const vector<vector<int>>& mat) {
    for (const auto& row : mat) {
        printVector(row);
    }
    cout << endl;
}

// 主函数，用于测试
int main() {
    // 测试用例1
    vector<string> strs1 = {"10", "0001", "111001", "1", "0"};
    int m1 = 5, n1 = 3;
    cout << "测试用例1结果: " << findMaxForm(strs1, m1, n1) << endl; // 预期输出: 4
    cout << "三维DP版本: " << findMaxForm3D(strs1, m1, n1) << endl;
    cout << "优化版本: " << findMaxFormOptimized(strs1, m1, n1) << endl;
    
    // 测试用例2
    vector<string> strs2 = {"10", "0", "1"};
    int m2 = 1, n2 = 1;
    cout << "测试用例2结果: " << findMaxForm(strs2, m2, n2) << endl; // 预期输出: 2
    cout << "三维DP版本: " << findMaxForm3D(strs2, m2, n2) << endl;
    cout << "优化版本: " << findMaxFormOptimized(strs2, m2, n2) << endl;
    
    // 测试用例3
    vector<string> strs3 = {"001", "110", "0000", "0000"};
    int m3 = 9, n3 = 3;
    cout << "测试用例3结果: " << findMaxForm(strs3, m3, n3) << endl; // 预期输出: 4
    
    return 0;
}