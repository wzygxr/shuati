// LeetCode 474. 一和零
// 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
// 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
// 链接：https://leetcode.cn/problems/ones-and-zeroes/
// 
// 解题思路：
// 这是一个二维费用的01背包问题。
// 每个字符串可以看作一个物品，它有两个「费用」：0的数量和1的数量
// 背包的容量是两个维度的：最多可以装m个0和n个1
// 每个物品的「价值」都是1（因为我们要最大化子集的长度）
// 
// 状态定义：dp[i][j] 表示最多使用i个0和j个1时能组成的最大子集长度
// 状态转移方程：dp[i][j] = max(dp[i][j], dp[i-zeroCount][j-oneCount] + 1)
// 其中zeroCount是当前字符串中0的数量，oneCount是当前字符串中1的数量
// 
// 时间复杂度：O(l * m * n)，其中l是字符串数组的长度
// 空间复杂度：O(m * n)，使用二维DP数组

#define MAX_M 101
#define MAX_N 101

// 获取两个数中的较大值
int max(int a, int b) {
    return a > b ? a : b;
}

/**
 * 计算最多使用m个0和n个1时能组成的最大子集长度
 * 
 * 解题思路：
 * 这是一个二维费用的01背包问题。
 * 每个字符串可以看作一个物品，它有两个「费用」：0的数量和1的数量
 * 背包的容量是两个维度的：最多可以装m个0和n个1
 * 每个物品的「价值」都是1（因为我们要最大化子集的长度）
 * 
 * 参数:
 *   strs: 二进制字符串数组
 *   strsSize: 字符串数组的大小
 *   m: 最多可以使用的0的个数
 *   n: 最多可以使用的1的个数
 * 返回值:
 *   最大子集长度
 */
int findMaxForm(char** strs, int strsSize, int m, int n) {
    // 参数验证
    if (strsSize == 0) {
        return 0;
    }
    
    // 创建二维DP数组，dp[i][j]表示最多使用i个0和j个1时能组成的最大子集长度
    // 这是一个二维费用的01背包问题
    int dp[MAX_M][MAX_N];
    
    // 初始化DP数组
    for (int i = 0; i <= m; i++) {
        for (int j = 0; j <= n; j++) {
            dp[i][j] = 0;
        }
    }
    
    // 遍历每个字符串（物品）
    for (int idx = 0; idx < strsSize; idx++) {
        // 计算当前字符串中0和1的数量
        // 这相当于获取当前物品的两个费用属性
        int zeroCount = 0, oneCount = 0;
        for (int i = 0; strs[idx][i] != '\0'; i++) {
            if (strs[idx][i] == '0') {
                zeroCount++;
            } else {
                oneCount++;
            }
        }
        
        // 二维01背包，需要倒序遍历两个维度，避免重复计算
        // i表示当前可用的0的个数，j表示当前可用的1的个数
        // 倒序遍历确保每个物品只使用一次
        for (int i = m; i >= zeroCount; i--) {
            for (int j = n; j >= oneCount; j--) {
                // 状态转移：选择当前字符串或不选择当前字符串
                // dp[i][j] = max(不选择当前字符串, 选择当前字符串)
                // 不选择当前字符串：dp[i][j]（保持原值）
                // 选择当前字符串：dp[i - zeroCount][j - oneCount] + 1（前一个状态+1）
                dp[i][j] = max(dp[i][j], dp[i - zeroCount][j - oneCount] + 1);
            }
        }
    }
    
    // 返回最多使用m个0和n个1时能组成的最大子集长度
    return dp[m][n];
}

/**
 * 优化版本：预处理计算每个字符串的0和1数量，减少重复计算
 * 
 * 参数:
 *   strs: 二进制字符串数组
 *   strsSize: 字符串数组的大小
 *   m: 最多可以使用的0的个数
 *   n: 最多可以使用的1的个数
 * 返回值:
 *   最大子集长度
 */
int findMaxFormOptimized(char** strs, int strsSize, int m, int n) {
    // 参数验证
    if (strsSize == 0) {
        return 0;
    }
    
    // 预处理：计算每个字符串中0和1的数量
    // 这样可以避免在动态规划过程中重复计算
    int counts[600][2]; // 假设最多600个字符串
    for (int i = 0; i < strsSize; i++) {
        int zeros = 0, ones = 0;
        for (int j = 0; strs[i][j] != '\0'; j++) {
            if (strs[i][j] == '0') zeros++;
            else ones++;
        }
        counts[i][0] = zeros;
        counts[i][1] = ones;
    }
    
    // 创建二维DP数组
    int dp[MAX_M][MAX_N];
    for (int i = 0; i <= m; i++) {
        for (int j = 0; j <= n; j++) {
            dp[i][j] = 0;
        }
    }
    
    // 遍历每个字符串（物品）
    for (int i = 0; i < strsSize; i++) {
        int zeroCount = counts[i][0];
        int oneCount = counts[i][1];
        
        // 剪枝：如果当前字符串的0或1数量超过背包容量，直接跳过
        // 因为当前字符串本身就无法放入背包
        if (zeroCount > m || oneCount > n) {
            continue;
        }
        
        // 二维01背包，倒序遍历
        for (int j = m; j >= zeroCount; j--) {
            for (int k = n; k >= oneCount; k--) {
                dp[j][k] = max(dp[j][k], dp[j - zeroCount][k - oneCount] + 1);
            }
        }
    }
    
    return dp[m][n];
}

/*
 * 示例:
 * 输入: strs = ["10", "0001", "111001", "1", "0"], m = 5, n = 3
 * 输出: 4
 * 解释: 最多有5个0和3个1的子集是{"10","0001","1","0"}，因此答案是4。
 *
 * 输入: strs = ["10", "0", "1"], m = 1, n = 1
 * 输出: 2
 * 解释: 最多有1个0和1个1的子集是{"0", "1"}，因此答案是2。
 *
 * 时间复杂度: O(l * m * n)
 *   - 外层循环遍历所有字符串：O(l)
 *   - 中层循环遍历m：O(m)
 *   - 内层循环遍历n：O(n)
 * 空间复杂度: O(m * n)
 *   - 二维DP数组的空间消耗
 */