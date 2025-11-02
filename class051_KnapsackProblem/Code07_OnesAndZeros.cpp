// LeetCode 474. 一和零
// 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
// 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
// 链接：https://leetcode.cn/problems/ones-and-zeroes/
// 
// 解题思路：
// 这是一个二维费用的01背包问题。
// 1. 每个字符串相当于一个物品，有两个重量限制：0的个数和1的个数
// 2. dp[i][j][k] 表示前i个字符串中，在最多使用j个0和k个1的情况下，能选出的最大子集大小
// 3. 状态转移方程：
//    dp[i][j][k] = max(dp[i-1][j][k], dp[i-1][j-zero][k-one] + 1)
//    其中zero和one分别是第i个字符串中0和1的个数
// 4. 可以使用滚动数组优化空间复杂度
//
// 时间复杂度：O(len(strs) * m * n + sum(len(str) for str in strs))
// 空间复杂度：O(m * n)

#define MAXM 101
#define MAXN 101

// 计算字符串中0和1的个数
// 参数:
//   str: 输入的字符串
//   zeros: 指向存储0个数的变量的指针
//   ones: 指向存储1个数的变量的指针
void countZerosOnes(char* str, int* zeros, int* ones) {
    *zeros = 0;
    *ones = 0;
    // 遍历字符串中的每个字符
    for (int i = 0; str[i] != '\0'; i++) {
        if (str[i] == '0') {
            (*zeros)++;  // 如果是'0'，增加zeros计数
        } else {
            (*ones)++;   // 如果是'1'，增加ones计数
        }
    }
}

/**
 * 找到最大子集的长度，该子集中最多有m个0和n个1
 * 
 * 参数:
 *   strs: 二进制字符串数组
 *   strsSize: 字符串数组的大小
 *   m: 最多允许的0的个数
 *   n: 最多允许的1的个数
 * 返回值:
 *   最大子集的长度
 */
int findMaxForm(char** strs, int strsSize, int m, int n) {
    // dp[i][j] 表示最多使用i个0和j个1时，能选出的最大子集大小
    // 这里使用了空间优化的二维DP数组，相当于dp[i][j][k]压缩为dp[j][k]
    int dp[MAXM][MAXN];
    
    // 初始化dp数组，所有值初始化为0
    for (int i = 0; i <= m; i++) {
        for (int j = 0; j <= n; j++) {
            dp[i][j] = 0;
        }
    }
    
    // 遍历每个字符串（物品）
    // 这相当于01背包中的物品遍历
    for (int i = 0; i < strsSize; i++) {
        // 统计当前字符串中0和1的个数
        // 这相当于获取当前物品的两个重量属性
        int zeros, ones;
        countZerosOnes(strs[i], &zeros, &ones);
        
        // 01背包需要倒序遍历，确保每个物品只使用一次
        // 注意边界条件：j >= zeros && k >= ones
        // 这里是二维费用01背包的核心实现
        // j表示当前可用的0的个数，k表示当前可用的1的个数
        for (int j = m; j >= zeros; j--) {
            for (int k = n; k >= ones; k--) {
                // 状态转移方程：
                // dp[j][k] = max(不选择当前字符串, 选择当前字符串)
                // 不选择当前字符串：dp[j][k]（保持原值）
                // 选择当前字符串：dp[j - zeros][k - ones] + 1（前一个状态+1）
                int newValue = dp[j - zeros][k - ones] + 1;
                if (newValue > dp[j][k]) {
                    dp[j][k] = newValue;
                }
            }
        }
    }
    
    // 返回最多使用m个0和n个1时能选出的最大子集大小
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
 * 时间复杂度: O(len(strs) * m * n + sum(len(str) for str in strs))
 *   - 外层循环遍历所有字符串：O(len(strs))
 *   - 中层循环遍历m：O(m)
 *   - 内层循环遍历n：O(n)
 *   - 统计每个字符串中0和1的个数：O(sum(len(str) for str in strs))
 * 空间复杂度: O(m * n)
 *   - 二维DP数组的空间消耗
 */