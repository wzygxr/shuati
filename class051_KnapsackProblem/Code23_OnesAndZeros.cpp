#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <unordered_map>
#include <tuple>

// LeetCode 474. 一和零
// 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
// 请你找出并返回 strs 的最大子集的大小，该子集中 最多 有 m 个 0 和 n 个 1 。
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
// 链接：https://leetcode.cn/problems/ones-and-zeroes/
// 
// 解题思路：
// 这是一个多维背包问题，我们需要同时考虑两种资源限制：0的数量和1的数量。
// 每个字符串相当于一个物品，占用的空间是它包含的0的数量和1的数量，价值为1（因为我们想最大化子集的大小）。
// 目标是在不超过m个0和n个1的限制下，选择尽可能多的字符串。
// 
// 状态定义：dp[i][j] 表示使用i个0和j个1时，可以选择的最大字符串数量
// 状态转移方程：对于每个字符串s，其中有zeros个0和ones个1，
//              dp[i][j] = max(dp[i][j], dp[i-zeros][j-ones] + 1)，当i >= zeros且j >= ones时
// 初始状态：dp[0][0] = 0，表示不使用任何0和1时，可以选择0个字符串
// 其他初始值也为0，表示还没有选择任何字符串
// 
// 时间复杂度：O(l * m * n)，其中l是字符串数组的长度，m和n是给定的整数
// 空间复杂度：O(m * n)，使用二维DP数组

using namespace std;

/**
 * 统计字符串中0和1的数量
 */
pair<int, int> countZerosOnes(const string& s) {
    int zeros = 0, ones = 0;
    for (char c : s) {
        if (c == '0') {
            zeros++;
        } else {
            ones++;
        }
    }
    return {zeros, ones};
}

/**
 * 找出最大子集的大小，该子集中最多有m个0和n个1
 * @param strs 二进制字符串数组
 * @param m 最大0的数量
 * @param n 最大1的数量
 * @return 最大子集的大小
 */
int findMaxForm(vector<string>& strs, int m, int n) {
    // 参数验证
    if (strs.empty()) {
        return 0;
    }
    
    // 创建二维DP数组，dp[i][j]表示使用i个0和j个1时，可以选择的最大字符串数量
    vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
    
    // 遍历每个字符串（物品）
    for (const string& s : strs) {
        // 统计当前字符串中0和1的数量
        auto [zeros, ones] = countZerosOnes(s);
        
        // 逆序遍历m和n，避免重复使用同一个字符串
        // 从大到小遍历0的数量
        for (int i = m; i >= zeros; i--) {
            // 从大到小遍历1的数量
            for (int j = n; j >= ones; j--) {
                // 状态转移：选择当前字符串或不选择当前字符串
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1);
            }
        }
    }
    
    // 返回结果：使用最多m个0和n个1时，可以选择的最大字符串数量
    return dp[m][n];
}

/**
 * 优化版本：预处理字符串的0和1数量，避免重复计算
 */
int findMaxFormOptimized(vector<string>& strs, int m, int n) {
    // 参数验证
    if (strs.empty()) {
        return 0;
    }
    
    // 预处理：统计每个字符串中0和1的数量
    vector<pair<int, int>> counts;
    for (const string& s : strs) {
        int zeros = 0, ones = 0;
        for (char c : s) {
            if (c == '0') {
                zeros++;
            } else {
                ones++;
            }
        }
        counts.push_back({zeros, ones});
    }
    
    // 创建二维DP数组
    vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
    
    // 遍历每个字符串（物品）
    for (const auto& count : counts) {
        int zeros = count.first;
        int ones = count.second;
        
        // 剪枝：如果当前字符串需要的0或1超过限制，则跳过
        if (zeros > m || ones > n) {
            continue;
        }
        
        // 逆序遍历
        for (int i = m; i >= zeros; i--) {
            for (int j = n; j >= ones; j--) {
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1);
            }
        }
    }
    
    return dp[m][n];
}

/**
 * 另一种实现方式，使用滚动数组优化空间
 */
int findMaxFormWithRollingArray(vector<string>& strs, int m, int n) {
    // 参数验证
    if (strs.empty()) {
        return 0;
    }
    
    // 创建DP数组
    vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
    
    for (const string& s : strs) {
        auto [zeros, ones] = countZerosOnes(s);
        
        // 使用临时数组保存上一状态
        vector<vector<int>> temp = dp;
        
        for (int i = zeros; i <= m; i++) {
            for (int j = ones; j <= n; j++) {
                dp[i][j] = max(dp[i][j], temp[i - zeros][j - ones] + 1);
            }
        }
    }
    
    return dp[m][n];
}

/**
 * 递归+记忆化搜索实现
 * 使用三元组作为键的哈希表来缓存结果
 */
int findMaxFormDFS(vector<string>& strs, int m, int n) {
    // 参数验证
    if (strs.empty()) {
        return 0;
    }
    
    // 预处理：统计每个字符串中0和1的数量
    vector<pair<int, int>> counts;
    for (const string& s : strs) {
        auto [zeros, ones] = countZerosOnes(s);
        // 只保留可能被选中的字符串
        if (zeros <= m && ones <= n) {
            counts.push_back({zeros, ones});
        }
    }
    
    // 使用哈希表作为缓存，键为(index, m, n)的组合，值为对应的最大子集大小
    // 为了使用自定义键，我们可以将三元组转换为字符串或使用tuple作为键
    unordered_map<string, int> memo;
    
    // 定义DFS函数
    function<int(int, int, int)> dfs = [&](int index, int remainingM, int remainingN) -> int {
        // 基础情况：已经处理完所有字符串
        if (index == counts.size()) {
            return 0;
        }
        
        // 生成缓存键
        string key = to_string(index) + "," + to_string(remainingM) + "," + to_string(remainingN);
        if (memo.find(key) != memo.end()) {
            return memo[key];
        }
        
        // 获取当前字符串的0和1数量
        int zeros = counts[index].first;
        int ones = counts[index].second;
        
        // 选择不使用当前字符串
        int notTake = dfs(index + 1, remainingM, remainingN);
        
        // 选择使用当前字符串（如果有足够的0和1）
        int take = 0;
        if (zeros <= remainingM && ones <= remainingN) {
            take = 1 + dfs(index + 1, remainingM - zeros, remainingN - ones);
        }
        
        // 记录结果
        memo[key] = max(notTake, take);
        
        return memo[key];
    };
    
    // 调用递归函数
    return dfs(0, m, n);
}

/**
 * 使用tuple作为缓存键的版本（需要C++11或更高版本）
 */
int findMaxFormDFSWithTuple(vector<string>& strs, int m, int n) {
    // 参数验证
    if (strs.empty()) {
        return 0;
    }
    
    // 预处理：统计每个字符串中0和1的数量
    vector<pair<int, int>> counts;
    for (const string& s : strs) {
        auto [zeros, ones] = countZerosOnes(s);
        if (zeros <= m && ones <= n) {
            counts.push_back({zeros, ones});
        }
    }
    
    // 为tuple<int, int, int>创建哈希函数
    struct TupleHash {
        template <typename T1, typename T2, typename T3>
        size_t operator()(const tuple<T1, T2, T3>& t) const {
            auto hash1 = hash<T1>{}(get<0>(t));
            auto hash2 = hash<T2>{}(get<1>(t));
            auto hash3 = hash<T3>{}(get<2>(t));
            // 组合哈希值
            return hash1 ^ (hash2 << 1) ^ (hash3 << 2);
        }
    };
    
    // 使用tuple作为键的哈希表
    unordered_map<tuple<int, int, int>, int, TupleHash> memo;
    
    // 定义DFS函数
    function<int(int, int, int)> dfs = [&](int index, int remainingM, int remainingN) -> int {
        if (index == counts.size()) {
            return 0;
        }
        
        auto key = make_tuple(index, remainingM, remainingN);
        if (memo.find(key) != memo.end()) {
            return memo[key];
        }
        
        int zeros = counts[index].first;
        int ones = counts[index].second;
        
        int notTake = dfs(index + 1, remainingM, remainingN);
        int take = 0;
        if (zeros <= remainingM && ones <= remainingN) {
            take = 1 + dfs(index + 1, remainingM - zeros, remainingN - ones);
        }
        
        memo[key] = max(notTake, take);
        return memo[key];
    };
    
    return dfs(0, m, n);
}

/**
 * 贪心算法（仅供参考，不适用于所有情况）
 * 贪心无法保证得到正确结果，但在某些情况下可以作为启发式方法
 */
int findMaxFormGreedy(vector<string>& strs, int m, int n) {
    // 按照字符串长度排序，优先选择较短的字符串（因为它们可能占用更少的0和1）
    sort(strs.begin(), strs.end(), [](const string& a, const string& b) {
        return a.size() < b.size();
    });
    
    int result = 0;
    int usedM = 0, usedN = 0;
    
    for (const string& s : strs) {
        auto [zeros, ones] = countZerosOnes(s);
        if (usedM + zeros <= m && usedN + ones <= n) {
            usedM += zeros;
            usedN += ones;
            result++;
        }
    }
    
    return result;
}

int main() {
    // 测试用例1
    vector<string> strs1 = {"10", "0001", "111001", "1", "0"};
    int m1 = 5, n1 = 3;
    cout << "测试用例1结果: " << findMaxForm(strs1, m1, n1) << endl; // 预期输出: 4
    
    // 测试用例2
    vector<string> strs2 = {"10", "0", "1"};
    int m2 = 1, n2 = 1;
    cout << "测试用例2结果: " << findMaxForm(strs2, m2, n2) << endl; // 预期输出: 2
    
    // 测试用例3
    vector<string> strs3 = {"00", "000"};
    int m3 = 1, n3 = 0;
    cout << "测试用例3结果: " << findMaxForm(strs3, m3, n3) << endl; // 预期输出: 0
    
    // 测试用例4
    vector<string> strs4 = {"111", "1000", "1000", "1000"};
    int m4 = 9, n4 = 3;
    cout << "测试用例4结果: " << findMaxForm(strs4, m4, n4) << endl; // 预期输出: 3
    
    return 0;
}