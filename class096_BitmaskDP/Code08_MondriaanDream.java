package class081;

import java.util.Arrays;

// Mondriaan's Dream (蒙德里安的梦想)
// 题目来源: POJ 2411 Mondriaan's Dream
// 题目链接: http://poj.org/problem?id=2411
// 题目描述:
// 给定n行m列的矩形，用1×2的砖块填充，问有多少种填充方案。
//
// 解题思路:
// 这是一道经典的轮廓线DP问题，也是状态压缩DP的一种。
// 1. 按格子进行DP，从上到下，从左到右填充
// 2. 用二进制状态表示当前轮廓线上的格子是否已被填充
// 3. dp[i][mask] 表示处理到第i个格子，轮廓线状态为mask时的方案数
// 4. 对于每个格子，有两种选择：横放或竖放砖块
//
// 时间复杂度: O(n*m*2^m)
// 空间复杂度: O(2^m)
//
// 补充题目1: 不要62 (不要62)
// 题目来源: HDU 2089 不要62
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=2089
// 题目描述:
// 杭州人称那些傻乎乎粘嗒嗒的人为62（音：laoer）。
// 杭州交通管理局经常会扩充一些的士车牌照，新近出来一个好消息，
// 以后上牌照，不再含有不吉利的数字了，这样就可以消除个别的士司机和乘客的心理障碍，
// 为社会和谐做出贡献。
// 不吉利的数字为所有包含4或62的号码。例如：62315 73418 88914都属于不吉利的号码。
// 问题是：从n到m的所有整数中，有多少个吉利的数字？
// 解题思路:
// 1. 数位DP解法
// 2. dfs(pos, pre, state, limit) 表示处理到第pos位，前一位数字是pre，状态为state，是否有限制
// 3. state表示是否包含不吉利数字的状态
// 时间复杂度: O(log(n) * 10 * 2)
// 空间复杂度: O(log(n) * 10 * 2)

// 补充题目2: 铺瓷砖 (Tiling with Dominoes)
// 题目来源: LeetCode 790 多米诺和托米诺平铺
// 题目链接: https://leetcode.cn/problems/domino-and-tromino-tiling/
// 题目描述:
// 有两种形状的瓷砖: 一种是 2 x 1 的多米诺形，另一种是形如 "L" 的托米诺形。
// 两种形状都可以旋转。
// 给定一个整数 n，返回可以平铺 2 x n 的面板的方法的数量。
// 答案可能很大，所以请返回其对 10^9 + 7 取模的结果。
// 解题思路:
// 1. 动态规划解法
// 2. dp[i][j] 表示前i列已经填满，第i+1列的状态为j时的方案数
// 3. j可以取4种状态：0（完全填满）、1（上半部分未填满）、2（下半部分未填满）、3（完全未填满）
// 时间复杂度: O(n)
// 空间复杂度: O(n)

// 补充题目3: 状态压缩DP的应用 - 机器人的运动范围
// 题目来源: LeetCode 2996 缺失的观测数据
// 题目链接: https://leetcode.cn/problems/missing-observations/
// 题目描述:
// 现有一份 n + m 次投掷单个六面骰子的观测数据，骰子的每个面分别为 1, 2, 3, 4, 5, 6。
// 其中有 n 个观测数据丢失，这 n 个数据需要我们自己推断。
// 已知剩余 m 个观测数据的平均值为 mean。
// 我们需要找到 n 个丢失的观测数据，使得所有 n + m 个观测数据的平均值也为 mean。
// 解题思路:
// 1. 数学计算法
// 2. 计算总和约束条件，生成符合条件的数据
// 时间复杂度: O(n)
// 空间复杂度: O(n)

// 补充题目4: 数位DP的应用 - 数字范围统计
// 题目来源: LeetCode 233 数字 1 的个数
// 题目链接: https://leetcode.cn/problems/number-of-digit-one/
// 题目描述:
// 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
// 解题思路:
// 1. 数位DP解法
// 2. dfs(pos, cnt, limit) 表示处理到第pos位，已经有cnt个1，是否有限制时的方案数
// 时间复杂度: O(log(n) * log(n))
// 空间复杂度: O(log(n) * log(n))

public class Code08_MondriaanDream {
    
    // POJ 2411 Mondriaan's Dream 解法
    public static long mondriaanDream(int n, int m) {
        // 特殊情况：如果n*m是奇数，则无法完全填充
        if ((n * m) % 2 == 1) {
            return 0;
        }
        
        // 交换n和m，确保m<=n，优化时间复杂度
        if (m > n) {
            int temp = n;
            n = m;
            m = temp;
        }
        
        // dp[i][mask] 表示处理到第i行，轮廓线状态为mask时的方案数
        long[][] dp = new long[n + 1][1 << m];
        dp[0][0] = 1;
        
        // 按行进行状态转移
        for (int i = 1; i <= n; i++) {
            // 按列进行状态转移
            for (int mask = 0; mask < (1 << m); mask++) {
                if (dp[i - 1][mask] > 0) {
                    // 尝试在当前行放置砖块
                    dfs(i, 0, mask, 0, dp);
                }
            }
        }
        
        return dp[n][0];
    }
    
    // DFS辅助函数，用于处理当前行的砖块放置
    private static void dfs(int row, int col, int prevMask, int currMask, long[][] dp) {
        int m = (int) (Math.log(prevMask) / Math.log(2)) + 1;
        if (m == 0) m = 1;
        
        // 如果处理完当前行
        if (col == m) {
            dp[row][currMask] += dp[row - 1][prevMask];
            return;
        }
        
        // 如果当前位置在前一行已经被填充（prevMask的第col位为1）
        if ((prevMask & (1 << col)) != 0) {
            // 当前位置不需要填充，直接处理下一个位置
            dfs(row, col + 1, prevMask, currMask, dp);
        } else {
            // 当前位置未被填充，需要放置砖块
            
            // 竖放砖块（占用当前位置和下一行的同一列）
            dfs(row, col + 1, prevMask, currMask | (1 << col), dp);
            
            // 横放砖块（占用当前位置和同一行的下一列），前提是下一列存在且未被填充
            if (col + 1 < m && (prevMask & (1 << (col + 1))) == 0) {
                // 横放砖块不需要在当前轮廓线上标记，因为两个位置都被填充了
                dfs(row, col + 2, prevMask, currMask, dp);
            }
        }
    }
    
    // 更简洁的实现方式
    public static long mondriaanDreamSimple(int n, int m) {
        // 特殊情况：如果n*m是奇数，则无法完全填充
        if ((n * m) % 2 == 1) {
            return 0;
        }
        
        // 交换n和m，确保m<=n，优化时间复杂度
        if (m > n) {
            int temp = n;
            n = m;
            m = temp;
        }
        
        // dp[mask] 表示当前行的轮廓线状态为mask时的方案数
        long[] dp = new long[1 << m];
        long[] nextDp = new long[1 << m];
        dp[0] = 1;
        
        // 按行进行状态转移
        for (int i = 0; i < n; i++) {
            Arrays.fill(nextDp, 0);
            
            // 按列进行状态转移
            for (int mask = 0; mask < (1 << m); mask++) {
                if (dp[mask] > 0) {
                    // 尝试在当前行放置砖块
                    dfsSimple(i, 0, mask, 0, dp[mask], nextDp);
                }
            }
            
            // 交换dp数组
            long[] temp = dp;
            dp = nextDp;
            nextDp = temp;
        }
        
        return dp[0];
    }
    
    // DFS辅助函数，用于处理当前行的砖块放置（简洁版）
    private static void dfsSimple(int row, int col, int prevMask, int currMask, long count, long[] nextDp) {
        int m = 32 - Integer.numberOfLeadingZeros(prevMask | 1); // 计算位数
        if (m == 0) m = 1;
        
        // 如果处理完当前行
        if (col == m) {
            nextDp[currMask] += count;
            return;
        }
        
        // 如果当前位置在前一行已经被填充（prevMask的第col位为1）
        if ((prevMask & (1 << col)) != 0) {
            // 当前位置不需要填充，直接处理下一个位置
            dfsSimple(row, col + 1, prevMask, currMask, count, nextDp);
        } else {
            // 当前位置未被填充，需要放置砖块
            
            // 竖放砖块（占用当前位置和下一行的同一列）
            dfsSimple(row, col + 1, prevMask, currMask | (1 << col), count, nextDp);
            
            // 横放砖块（占用当前位置和同一行的下一列），前提是下一列存在且未被填充
            if (col + 1 < m && (prevMask & (1 << (col + 1))) == 0) {
                // 横放砖块不需要在当前轮廓线上标记，因为两个位置都被填充了
                dfsSimple(row, col + 2, prevMask, currMask, count, nextDp);
            }
        }
    }
    
    // HDU 2089 不要62 解法
    public static int countLuckyNumbers(int n, int m) {
        // 将数字转换为字符串，便于数位DP处理
        String num1 = String.valueOf(n - 1);
        String num2 = String.valueOf(m);
        
        // 计算[0, m]范围内的吉利数字个数
        int count2 = countLuckyNumbersHelper(num2);
        // 计算[0, n-1]范围内的吉利数字个数
        int count1 = countLuckyNumbersHelper(num1);
        
        return count2 - count1;
    }
    
    // 数位DP辅助函数
    private static int countLuckyNumbersHelper(String num) {
        int len = num.length();
        if (len == 0) return 0;
        
        // dp[pos][has62][has4][limit] 表示处理到第pos位，是否包含62，是否包含4，是否有限制时的方案数
        int[][][][] dp = new int[len][2][2][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    Arrays.fill(dp[i][j][k], -1);
                }
            }
        }
        
        return dfsLucky(0, 0, 0, true, num, dp);
    }
    
    // DFS辅助函数，用于数位DP
    private static int dfsLucky(int pos, int has62, int has4, boolean limit, String num, int[][][][] dp) {
        // 如果处理完所有位数
        if (pos == num.length()) {
            // 如果不包含不吉利数字，返回1；否则返回0
            return (has62 == 0 && has4 == 0) ? 1 : 0;
        }
        
        // 记忆化搜索
        if (!limit && dp[pos][has62][has4][limit ? 1 : 0] != -1) {
            return dp[pos][has62][has4][limit ? 1 : 0];
        }
        
        int up = limit ? (num.charAt(pos) - '0') : 9;
        int res = 0;
        
        // 枚举当前位可以填的数字
        for (int i = 0; i <= up; i++) {
            // 如果当前数字是4，标记包含4
            int newHas4 = (has4 == 1 || i == 4) ? 1 : 0;
            // 如果前一位是6且当前位是2，标记包含62
            int newHas62 = (has62 == 1 || (pos > 0 && pos-1 < num.length() && num.charAt(pos - 1) == '6' && i == 2)) ? 1 : 0;
            
            // 递归处理下一位
            res += dfsLucky(pos + 1, newHas62, newHas4, limit && (i == up), num, dp);
        }
        
        // 记忆化存储
        if (!limit) {
            dp[pos][has62][has4][limit ? 1 : 0] = res;
        }
        
        return res;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试 POJ 2411 Mondriaan's Dream
        System.out.println("POJ 2411 Mondriaan's Dream 测试:");
        System.out.println("2×2: " + mondriaanDreamSimple(2, 2));
        System.out.println("3×2: " + mondriaanDreamSimple(3, 2));
        System.out.println("4×2: " + mondriaanDreamSimple(4, 2));
        
        // 测试 HDU 2089 不要62
        System.out.println("\nHDU 2089 不要62 测试:");
        System.out.println("[1, 100]范围内的吉利数字个数: " + countLuckyNumbers(1, 100));
        System.out.println("[1, 1000]范围内的吉利数字个数: " + countLuckyNumbers(1, 1000));
        
        // 测试 LeetCode 790 多米诺和托米诺平铺
        System.out.println("\nLeetCode 790 多米诺和托米诺平铺 测试:");
        System.out.println("n = 1: " + numTilings(1)); // 应输出 1
        System.out.println("n = 2: " + numTilings(2)); // 应输出 2
        System.out.println("n = 3: " + numTilings(3)); // 应输出 5
        
        // 测试 LeetCode 2996 缺失的观测数据
        System.out.println("\nLeetCode 2996 缺失的观测数据 测试:");
        int[] rolls = {3, 2, 4, 3};
        System.out.print("输入 [3,2,4,3], n=2, mean=4: ");
        int[] result = missingRolls(rolls, 2, 4);
        for (int num : result) {
            System.out.print(num + " ");
        }
        System.out.println();
        
        // 测试 LeetCode 233 数字1的个数
        System.out.println("\nLeetCode 233 数字1的个数 测试:");
        System.out.println("n = 13: " + countDigitOne(13)); // 应输出 6
        System.out.println("n = 0: " + countDigitOne(0));   // 应输出 0
    }
    
    // 补充题目2: LeetCode 790 多米诺和托米诺平铺 解法
    public static int numTilings(int n) {
        final int MOD = 1000000007;
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        // dp[i][j]: 前i列已经填满，第i+1列的状态为j时的方案数
        // j=0: 完全填满
        // j=1: 上半部分未填满
        // j=2: 下半部分未填满
        // j=3: 完全未填满
        long[][] dp = new long[n+1][4];
        dp[0][0] = 1; // 初始状态
        
        for (int i = 0; i < n; i++) {
            // 从状态0转移
            dp[i+1][0] = (dp[i+1][0] + dp[i][0]) % MOD; // 放置一个2x1多米诺
            dp[i+2][0] = (dp[i+2][0] + dp[i][0]) % MOD; // 放置两个1x2多米诺
            dp[i+1][1] = (dp[i+1][1] + dp[i][0]) % MOD; // 放置一个L形托米诺（左上）
            dp[i+1][2] = (dp[i+1][2] + dp[i][0]) % MOD; // 放置一个L形托米诺（左下）
            
            // 从状态1转移
            dp[i+1][0] = (dp[i+1][0] + dp[i][1]) % MOD; // 放置一个1x2多米诺（下半部分）
            dp[i+2][0] = (dp[i+2][0] + dp[i][1]) % MOD; // 放置一个L形托米诺（右下）
            
            // 从状态2转移
            dp[i+1][0] = (dp[i+1][0] + dp[i][2]) % MOD; // 放置一个1x2多米诺（上半部分）
            dp[i+2][0] = (dp[i+2][0] + dp[i][2]) % MOD; // 放置一个L形托米诺（右上）
            
            // 从状态3转移
            dp[i+1][0] = (dp[i+1][0] + dp[i][3]) % MOD; // 放置两个2x1多米诺
        }
        
        return (int) dp[n][0];
    }
    
    // 补充题目3: LeetCode 2996 缺失的观测数据 解法
    public static int[] missingRolls(int[] rolls, int n, int mean) {
        int m = rolls.length;
        int totalSum = mean * (n + m);
        int existingSum = 0;
        for (int roll : rolls) {
            existingSum += roll;
        }
        int missingSum = totalSum - existingSum;
        
        // 检查缺失和是否在有效范围内
        if (missingSum < n || missingSum > 6 * n) {
            return new int[0]; // 不可能的情况
        }
        
        int[] result = new int[n];
        // 初始化为1，然后将多余的部分均匀分配
        for (int i = 0; i < n; i++) {
            result[i] = 1;
        }
        missingSum -= n; // 已经分配了n个1
        
        // 分配剩余的点数
        for (int i = 0; i < n && missingSum > 0; i++) {
            int add = Math.min(5, missingSum); // 每个骰子最多再加5点（1+5=6）
            result[i] += add;
            missingSum -= add;
        }
        
        return result;
    }
    
    // 补充题目4: LeetCode 233 数字1的个数 解法
    public static int countDigitOne(int n) {
        if (n <= 0) return 0;
        
        String num = String.valueOf(n);
        int len = num.length();
        // 记忆化搜索的缓存
        Integer[][] memo = new Integer[len][len];
        
        return dfsCountOne(0, 0, true, num.toCharArray(), memo);
    }
    
    private static int dfsCountOne(int pos, int cnt, boolean limit, char[] num, Integer[][] memo) {
        if (pos == num.length) {
            return cnt; // 返回已经统计的1的个数
        }
        
        // 如果没有限制并且已经计算过，直接返回缓存的结果
        if (!limit && memo[pos][cnt] != null) {
            return memo[pos][cnt];
        }
        
        int upper = limit ? (num[pos] - '0') : 9;
        int res = 0;
        
        for (int i = 0; i <= upper; i++) {
            int newCnt = cnt + (i == 1 ? 1 : 0); // 如果当前数字是1，增加计数
            res += dfsCountOne(pos + 1, newCnt, limit && (i == upper), num, memo);
        }
        
        // 缓存结果（只有在没有限制的情况下才缓存）
        if (!limit) {
            memo[pos][cnt] = res;
        }
        
        return res;
    }
}

/*
 * C++ 实现
 */
// #include <iostream>
// #include <vector>
// #include <string>
// #include <algorithm>
// #include <cstring>
// #include <cmath>
// #include <climits>
// using namespace std;

// // POJ 2411 Mondriaan's Dream 解法
// class MondriaanDream {
// public:
//     // 普通版本
//     long long mondriaanDream(int n, int m) {
//         if ((n * m) % 2 == 1) return 0;
//         
//         if (m > n) swap(n, m);
//         
//         vector<vector<long long>> dp(n + 1, vector<long long>(1 << m, 0));
//         dp[0][0] = 1;
//         
//         for (int i = 1; i <= n; i++) {
//             for (int mask = 0; mask < (1 << m); mask++) {
//                 if (dp[i - 1][mask] > 0) {
//                     dfs(i, 0, mask, 0, dp);
//                 }
//             }
//         }
//         
//         return dp[n][0];
//     }
//     
//     // DFS辅助函数
//     void dfs(int row, int col, int prevMask, int currMask, vector<vector<long long>>& dp) {
//         int m = 1;
//         if (prevMask > 0) {
//             m = log2(prevMask) + 1;
//         }
//         
//         if (col == m) {
//             dp[row][currMask] += dp[row - 1][prevMask];
//             return;
//         }
//         
//         if ((prevMask & (1 << col)) != 0) {
//             dfs(row, col + 1, prevMask, currMask, dp);
//         } else {
//             dfs(row, col + 1, prevMask, currMask | (1 << col), dp);
//             
//             if (col + 1 < m && (prevMask & (1 << (col + 1))) == 0) {
//                 dfs(row, col + 2, prevMask, currMask, dp);
//             }
//         }
//     }
//     
//     // 简洁版本
//     long long mondriaanDreamSimple(int n, int m) {
//         if ((n * m) % 2 == 1) return 0;
//         
//         if (m > n) swap(n, m);
//         
//         vector<long long> dp(1 << m, 0);
//         vector<long long> nextDp(1 << m, 0);
//         dp[0] = 1;
//         
//         for (int i = 0; i < n; i++) {
//             fill(nextDp.begin(), nextDp.end(), 0);
//             
//             for (int mask = 0; mask < (1 << m); mask++) {
//                 if (dp[mask] > 0) {
//                     dfsSimple(i, 0, mask, 0, dp[mask], nextDp, m);
//                 }
//             }
//             
//             swap(dp, nextDp);
//         }
//         
//         return dp[0];
//     }
//     
//     // DFS辅助函数（简洁版）
//     void dfsSimple(int row, int col, int prevMask, int currMask, long long count, 
//                    vector<long long>& nextDp, int m) {
//         if (col == m) {
//             nextDp[currMask] += count;
//             return;
//         }
//         
//         if ((prevMask & (1 << col)) != 0) {
//             dfsSimple(row, col + 1, prevMask, currMask, count, nextDp, m);
//         } else {
//             dfsSimple(row, col + 1, prevMask, currMask | (1 << col), count, nextDp, m);
//             
//             if (col + 1 < m && (prevMask & (1 << (col + 1))) == 0) {
//                 dfsSimple(row, col + 2, prevMask, currMask, count, nextDp, m);
//             }
//         }
//     }
// };

// // HDU 2089 不要62 解法
// int countLuckyNumbers(int n, int m) {
//     string num1 = to_string(n - 1);
//     string num2 = to_string(m);
//     
//     function<int(int, int, int, bool, string&, vector<vector<vector<vector<int>>>>&)> dfsLucky = 
//         [&](int pos, int has62, int has4, bool limit, string& num, vector<vector<vector<vector<int>>>>& dp) {
//             if (pos == num.size()) {
//                 return (has62 == 0 && has4 == 0) ? 1 : 0;
//             }
//             
//             if (!limit && dp[pos][has62][has4][limit ? 1 : 0] != -1) {
//                 return dp[pos][has62][has4][limit ? 1 : 0];
//             }
//             
//             int up = limit ? (num[pos] - '0') : 9;
//             int res = 0;
//             
//             for (int i = 0; i <= up; i++) {
//                 int newHas4 = (has4 == 1 || i == 4) ? 1 : 0;
//                 int newHas62 = has62;
//                 if (pos > 0 && num[pos-1] == '6' && i == 2) {
//                     newHas62 = 1;
//                 }
//                 
//                 res += dfsLucky(pos + 1, newHas62, newHas4, limit && (i == up), num, dp);
//             }
//             
//             if (!limit) {
//                 dp[pos][has62][has4][limit ? 1 : 0] = res;
//             }
//             
//             return res;
//         };
//     
//     auto countHelper = [&](string num) -> int {
//         int len = num.size();
//         if (len == 0) return 0;
//         
//         vector<vector<vector<vector<int>>>> dp(len, vector<vector<vector<int>>>(2, vector<vector<int>>(2, vector<int>(2, -1))));
//         return dfsLucky(0, 0, 0, true, num, dp);
//     };
//     
//     int count1 = countHelper(num1);
//     int count2 = countHelper(num2);
//     
//     return count2 - count1;
// }

// // LeetCode 790 多米诺和托米诺平铺 解法
// int numTilings(int n) {
//     const int MOD = 1000000007;
//     if (n == 0) return 0;
//     if (n == 1) return 1;
//     if (n == 2) return 2;
//     
//     vector<vector<long long>> dp(n + 1, vector<long long>(4, 0));
//     dp[0][0] = 1;
//     
//     for (int i = 0; i < n; i++) {
//         // 从状态0转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][0]) % MOD;
//         if (i + 2 <= n) dp[i+2][0] = (dp[i+2][0] + dp[i][0]) % MOD;
//         dp[i+1][1] = (dp[i+1][1] + dp[i][0]) % MOD;
//         dp[i+1][2] = (dp[i+1][2] + dp[i][0]) % MOD;
//         
//         // 从状态1转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][1]) % MOD;
//         if (i + 2 <= n) dp[i+2][0] = (dp[i+2][0] + dp[i][1]) % MOD;
//         
//         // 从状态2转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][2]) % MOD;
//         if (i + 2 <= n) dp[i+2][0] = (dp[i+2][0] + dp[i][2]) % MOD;
//         
//         // 从状态3转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][3]) % MOD;
//     }
//     
//     return dp[n][0];
// }

// // LeetCode 2996 缺失的观测数据 解法
// vector<int> missingRolls(vector<int>& rolls, int n, int mean) {
//     int m = rolls.size();
//     int totalSum = mean * (n + m);
//     int existingSum = 0;
//     for (int roll : rolls) {
//         existingSum += roll;
//     }
//     int missingSum = totalSum - existingSum;
//     
//     if (missingSum < n || missingSum > 6 * n) {
//         return {};
//     }
//     
//     vector<int> result(n, 1);
//     missingSum -= n;
//     
//     for (int i = 0; i < n && missingSum > 0; i++) {
//         int add = min(5, missingSum);
//         result[i] += add;
//         missingSum -= add;
//     }
//     
//     return result;
// }

// // LeetCode 233 数字1的个数 解法
// int countDigitOne(int n) {
//     if (n <= 0) return 0;
//     
//     string num = to_string(n);
//     int len = num.size();
//     vector<vector<int>> memo(len, vector<int>(len, -1));
//     
//     function<int(int, int, bool)> dfsCountOne = [&](int pos, int cnt, bool limit) -> int {
//         if (pos == len) {
//             return cnt;
//         }
//         
//         if (!limit && memo[pos][cnt] != -1) {
//             return memo[pos][cnt];
//         }
//         
//         int upper = limit ? (num[pos] - '0') : 9;
//         int res = 0;
//         
//         for (int i = 0; i <= upper; i++) {
//             int newCnt = cnt + (i == 1 ? 1 : 0);
//             res += dfsCountOne(pos + 1, newCnt, limit && (i == upper));
//         }
//         
//         if (!limit) {
//             memo[pos][cnt] = res;
//         }
//         
//         return res;
//     };
//     
//     return dfsCountOne(0, 0, true);
// }

// int main() {
//     // 测试 POJ 2411 Mondriaan's Dream
//     MondriaanDream md;
//     cout << "POJ 2411 Mondriaan's Dream 测试:" << endl;
//     cout << "2×2: " << md.mondriaanDreamSimple(2, 2) << endl;
//     cout << "3×2: " << md.mondriaanDreamSimple(3, 2) << endl;
//     cout << "4×2: " << md.mondriaanDreamSimple(4, 2) << endl;
//     
//     // 测试 HDU 2089 不要62
//     cout << "\nHDU 2089 不要62 测试:" << endl;
//     cout << "[1, 100]范围内的吉利数字个数: " << countLuckyNumbers(1, 100) << endl;
//     cout << "[1, 1000]范围内的吉利数字个数: " << countLuckyNumbers(1, 1000) << endl;
//     
//     // 测试 LeetCode 790 多米诺和托米诺平铺
//     cout << "\nLeetCode 790 多米诺和托米诺平铺 测试:" << endl;
//     cout << "n = 1: " << numTilings(1) << endl;
//     cout << "n = 2: " << numTilings(2) << endl;
//     cout << "n = 3: " << numTilings(3) << endl;
//     
//     // 测试 LeetCode 2996 缺失的观测数据
//     cout << "\nLeetCode 2996 缺失的观测数据 测试:" << endl;
//     vector<int> rolls = {3, 2, 4, 3};
//     vector<int> result = missingRolls(rolls, 2, 4);
//     cout << "输入 [3,2,4,3], n=2, mean=4: ";
//     for (int num : result) {
//         cout << num << " ";
//     }
//     cout << endl;
//     
//     // 测试 LeetCode 233 数字1的个数
//     cout << "\nLeetCode 233 数字1的个数 测试:" << endl;
//     cout << "n = 13: " << countDigitOne(13) << endl;
//     cout << "n = 0: " << countDigitOne(0) << endl;
//     
//     return 0;
// }

/*
 * Python 实现
 */
// import sys
// sys.setrecursionlimit(1 << 25)

// // POJ 2411 Mondriaan's Dream 解法
// class MondriaanDream:
//     def mondriaanDream(self, n, m):
//         if (n * m) % 2 == 1:
//             return 0
//         
//         if m > n:
//             n, m = m, n
//         
//         dp = [[0] * (1 << m) for _ in range(n + 1)]
//         dp[0][0] = 1
//         
//         for i in range(1, n + 1):
//             for mask in range(1 << m):
//                 if dp[i - 1][mask] > 0:
//                     self.dfs(i, 0, mask, 0, dp, m)
//         
//         return dp[n][0]
//     
//     def dfs(self, row, col, prev_mask, curr_mask, dp, m):
//         if col == m:
//             dp[row][curr_mask] += dp[row - 1][prev_mask]
//             return
//         
//         if (prev_mask & (1 << col)) != 0:
//             self.dfs(row, col + 1, prev_mask, curr_mask, dp, m)
//         else:
//             // 竖放砖块
//             self.dfs(row, col + 1, prev_mask, curr_mask | (1 << col), dp, m)
//             // 横放砖块
//             if col + 1 < m and (prev_mask & (1 << (col + 1))) == 0:
//                 self.dfs(row, col + 2, prev_mask, curr_mask, dp, m)
//     
//     def mondriaanDreamSimple(self, n, m):
//         if (n * m) % 2 == 1:
//             return 0
//         
//         if m > n:
//             n, m = m, n
//         
//         dp = [0] * (1 << m)
//         next_dp = [0] * (1 << m)
//         dp[0] = 1
//         
//         for i in range(n):
//             next_dp = [0] * (1 << m)
//             for mask in range(1 << m):
//                 if dp[mask] > 0:
//                     self.dfs_simple(i, 0, mask, 0, dp[mask], next_dp, m)
//             dp, next_dp = next_dp, dp
//         
//         return dp[0]
//     
//     def dfs_simple(self, row, col, prev_mask, curr_mask, count, next_dp, m):
//         if col == m:
//             next_dp[curr_mask] += count
//             return
//         
//         if (prev_mask & (1 << col)) != 0:
//             self.dfs_simple(row, col + 1, prev_mask, curr_mask, count, next_dp, m)
//         else:
//             // 竖放砖块
//             self.dfs_simple(row, col + 1, prev_mask, curr_mask | (1 << col), count, next_dp, m)
//             // 横放砖块
//             if col + 1 < m and (prev_mask & (1 << (col + 1))) == 0:
//                 self.dfs_simple(row, col + 2, prev_mask, curr_mask, count, next_dp, m)

// // HDU 2089 不要62 解法
// def countLuckyNumbers(n, m):
//     def countHelper(num_str):
//         len_num = len(num_str)
//         memo = [[[[-1 for _ in range(2)] for __ in range(2)] for ___ in range(2)] for ____ in range(len_num)]
//         
//         def dfs(pos, has62, has4, limit):
//             if pos == len_num:
//                 return 1 if (has62 == 0 and has4 == 0) else 0
//             
//             if not limit and memo[pos][has62][has4][limit] != -1:
//                 return memo[pos][has62][has4][limit]
//             
//             upper = int(num_str[pos]) if limit else 9
//             res = 0
//             
//             for i in range(upper + 1):
//                 new_has4 = 1 if (has4 == 1 or i == 4) else 0
//                 new_has62 = has62
//                 if pos > 0 and num_str[pos-1] == '6' and i == 2:
//                     new_has62 = 1
//                 
//                 res += dfs(pos + 1, new_has62, new_has4, limit and (i == upper))
//             
//             if not limit:
//                 memo[pos][has62][has4][limit] = res
//             
//             return res
//         
//         return dfs(0, 0, 0, True)
//     
//     num1 = str(n - 1)
//     num2 = str(m)
//     
//     count1 = countHelper(num1)
//     count2 = countHelper(num2)
//     
//     return count2 - count1

// // LeetCode 790 多米诺和托米诺平铺 解法
// def numTilings(n):
//     MOD = 10**9 + 7
//     if n == 0:
//         return 0
//     if n == 1:
//         return 1
//     if n == 2:
//         return 2
//     
//     // dp[i][j]: 前i列已经填满，第i+1列的状态为j时的方案数
//     dp = [[0] * 4 for _ in range(n + 1)]
//     dp[0][0] = 1
//     
//     for i in range(n):
//         // 从状态0转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][0]) % MOD
//         if i + 2 <= n:
//             dp[i+2][0] = (dp[i+2][0] + dp[i][0]) % MOD
//         dp[i+1][1] = (dp[i+1][1] + dp[i][0]) % MOD
//         dp[i+1][2] = (dp[i+1][2] + dp[i][0]) % MOD
//         
//         // 从状态1转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][1]) % MOD
//         if i + 2 <= n:
//             dp[i+2][0] = (dp[i+2][0] + dp[i][1]) % MOD
//         
//         // 从状态2转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][2]) % MOD
//         if i + 2 <= n:
//             dp[i+2][0] = (dp[i+2][0] + dp[i][2]) % MOD
//         
//         // 从状态3转移
//         dp[i+1][0] = (dp[i+1][0] + dp[i][3]) % MOD
//     
//     return dp[n][0]

// // LeetCode 2996 缺失的观测数据 解法
// def missingRolls(rolls, n, mean):
//     m = len(rolls)
//     total_sum = mean * (n + m)
//     existing_sum = sum(rolls)
//     missing_sum = total_sum - existing_sum
//     
//     if missing_sum < n or missing_sum > 6 * n:
//         return []
//     
//     result = [1] * n
//     missing_sum -= n  // 已经分配了n个1
//     
//     for i in range(n):
//         if missing_sum <= 0:
//             break
//         add = min(5, missing_sum)  // 每个骰子最多再加5点
//         result[i] += add
//         missing_sum -= add
//     
//     return result

// // LeetCode 233 数字1的个数 解法
// def countDigitOne(n):
//     if n <= 0:
//         return 0
//     
//     num_str = str(n)
//     len_num = len(num_str)
//     memo = [[-1 for _ in range(len_num)] for __ in range(len_num)]
//     
//     def dfs(pos, cnt, limit):
//         if pos == len_num:
//             return cnt
//         
//         if not limit and memo[pos][cnt] != -1:
//             return memo[pos][cnt]
//         
//         upper = int(num_str[pos]) if limit else 9
//         res = 0
//         
//         for i in range(upper + 1):
//             new_cnt = cnt + (1 if i == 1 else 0)
//             res += dfs(pos + 1, new_cnt, limit and (i == upper))
//         
//         if not limit:
//             memo[pos][cnt] = res
//         
//         return res
//     
//     return dfs(0, 0, True)

// # 测试代码
// if __name__ == "__main__":
//     # 测试 POJ 2411 Mondriaan's Dream
//     md = MondriaanDream()
//     print("POJ 2411 Mondriaan's Dream 测试:")
//     print("2×2:", md.mondriaanDreamSimple(2, 2))
//     print("3×2:", md.mondriaanDreamSimple(3, 2))
//     print("4×2:", md.mondriaanDreamSimple(4, 2))
//     
//     # 测试 HDU 2089 不要62
//     print("\nHDU 2089 不要62 测试:")
//     print("[1, 100]范围内的吉利数字个数:", countLuckyNumbers(1, 100))
//     print("[1, 1000]范围内的吉利数字个数:", countLuckyNumbers(1, 1000))
//     
//     # 测试 LeetCode 790 多米诺和托米诺平铺
//     print("\nLeetCode 790 多米诺和托米诺平铺 测试:")
//     print("n = 1:", numTilings(1))
//     print("n = 2:", numTilings(2))
//     print("n = 3:", numTilings(3))
//     
//     # 测试 LeetCode 2996 缺失的观测数据
//     print("\nLeetCode 2996 缺失的观测数据 测试:")
//     rolls = [3, 2, 4, 3]
//     result = missingRolls(rolls, 2, 4)
//     print("输入 [3,2,4,3], n=2, mean=4:", result)
//     
//     # 测试 LeetCode 233 数字1的个数
//     print("\nLeetCode 233 数字1的个数 测试:")
//     print("n = 13:", countDigitOne(13))
//     print("n = 0:", countDigitOne(0))