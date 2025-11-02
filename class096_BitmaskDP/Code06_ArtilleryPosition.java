package class081;

import java.util.Arrays;

// 炮兵阵地 (Artillery Position)
// 题目来源: POJ 1185 炮兵阵地
// 题目链接: http://poj.org/problem?id=1185
// 题目描述:
// 司令部的将军们打算在N*M的网格地图上部署他们的炮兵部队。一个N*M的地图由N行M列组成，
// 地图的每一格可能是山地（用"H"表示），也可能是平原（用"P"表示）。
// 在每一格平原上可以布置一支炮兵部队，山地上则不可以。
// 一支炮兵部队在地图上的攻击范围是它所在位置的四个方向（上下左右）各两格内的区域，
// 但不包括该炮兵部队自身所在的格子。
// 任何一支炮兵部队的攻击范围内的格子（包括攻击范围的边界）不能再布置其他炮兵部队。
// 一支炮兵部队的攻击范围与其部署位置有关，不同位置的炮兵部队的攻击范围各不相同。
// 问题要求计算在给定的地图上最多能部署多少支炮兵部队。
//
// 解题思路:
// 这是一道经典的状态压缩DP问题。由于炮兵的攻击范围是上下左右各两格，
// 所以我们需要考虑当前行、前一行和前两行的状态。
// 我们可以按行进行状态压缩，用二进制位表示每一行的炮兵部署状态。
// 对于每一行，我们需要考虑：
// 1. 当前行的地形是否允许在某个位置部署炮兵（平原为P，山地为H）
// 2. 当前行的炮兵部署状态是否合法（同一行内炮兵不能互相攻击）
// 3. 当前行与前一行、前两行的炮兵部署状态是否冲突
//
// 状态定义:
// dp[i][mask1][mask2] 表示处理到第i行，第i-1行的部署状态为mask1，第i-2行的部署状态为mask2时的最大炮兵数
//
// 状态转移:
// 对于每一行，我们枚举所有可能的合法状态，然后检查与前两行是否冲突
//
// 时间复杂度: O(n * 2^(3*m)) 其中n是行数，m是列数
// 空间复杂度: O(2^(2*m))
//
// 补充题目1: 最大兼容数对 (Compatible Numbers)
// 题目来源: CodeForces 165E
// 题目链接: https://codeforces.com/problemset/problem/165/E
// 题目描述:
// 给定一个数组，对于每个数字，找到另一个数字，使得它们的按位与结果为0。
// 如果不存在这样的数字，输出-1。
// 解题思路:
// 1. 使用状态压缩DP或SOS DP
// 2. 对于每个数字，我们需要找到另一个数字，使得它们的按位与为0
// 3. 这等价于找到一个数字，使得它的二进制表示中为1的位在原数字中都为0
// 4. 可以使用子集枚举或预处理来解决
// 时间复杂度: O(n * 2^k) 其中k是位数
// 空间复杂度: O(2^k)

// 补充题目2: 覆盖所有点的最小矩形数目
// 题目来源: LeetCode 1240
// 题目链接: https://leetcode.com/problems/tiling-a-rectangle-with-the-fewest-squares/
// 题目描述:
// 给你一个n x m的矩形，你需要用最少的正方形来完全覆盖这个矩形，正方形的边长必须是整数。
// 解题思路:
// 1. 使用回溯+剪枝
// 2. 或者使用状态压缩DP
// 3. 我们可以按行和列进行动态规划，记录覆盖状态
// 时间复杂度: O((n*m)^2)
// 空间复杂度: O(n*m)

// 补充题目3: 数字1的个数
// 题目来源: LeetCode 233
// 题目链接: https://leetcode.com/problems/number-of-digit-one/
// 题目描述:
// 给定一个整数n，计算所有小于等于n的非负整数中数字1出现的个数。
// 解题思路:
// 1. 逐位计算每个位置上1出现的次数
// 2. 使用位运算和数学规律来优化计算
// 时间复杂度: O(log n)
// 空间复杂度: O(1)

// 补充题目4: 打家劫舍II
// 题目来源: LeetCode 213
// 题目链接: https://leetcode.com/problems/house-robber-ii/
// 题目描述:
// 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
// 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
// 这个地区的房屋排列成一个环形，这意味着第一个房屋和最后一个房屋是相邻的。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
// 解题思路:
// 1. 由于房屋是环形的，我们可以将问题拆分为两种情况：
//    - 不偷第一个房屋
//    - 不偷最后一个房屋
// 2. 对这两种情况分别使用动态规划求解，取较大值
// 时间复杂度: O(n)
// 空间复杂度: O(1)

public class Code06_ArtilleryPosition {
    public static final int MAXN = 105;
    public static final int MAXM = 15;
    public static final int MAX_STATES = 1 << 10; // 2^10 = 1024

    // POJ 1185 炮兵阵地 解法
    public static int artilleryPosition(int n, int m, char[][] grid) {
        // 预处理每一行的合法地形状态
        int[] validStates = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 'P') {
                    validStates[i] |= (1 << j);
                }
            }
        }

        // 预处理所有可能的行状态（同一行内炮兵不互相攻击）
        int[] allStates = new int[MAX_STATES];
        int[] stateCount = new int[MAX_STATES];
        int totalStates = 0;
        for (int i = 0; i < (1 << m); i++) {
            // 检查同一行内炮兵是否互相攻击（距离小于等于2）
            if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0 && 
                (i & (i >> 1)) == 0 && (i & (i >> 2)) == 0) {
                allStates[totalStates] = i;
                stateCount[totalStates] = Integer.bitCount(i); // 计算该状态下的炮兵数量
                totalStates++;
            }
        }

        // dp[i][mask1][mask2] 表示处理到第i行，第i-1行的部署状态为mask1，第i-2行的部署状态为mask2时的最大炮兵数
        int[][][] dp = new int[n + 1][MAX_STATES][MAX_STATES];
        
        // 初始化
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < MAX_STATES; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        dp[0][0][0] = 0;

        // 状态转移
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < totalStates; j++) {
                int state = allStates[j];
                int count = stateCount[j];
                
                // 检查当前状态是否在当前行的合法地形内
                if ((state & validStates[i - 1]) != state) {
                    continue;
                }

                // 枚举前两行的状态
                for (int mask1 = 0; mask1 < (1 << m); mask1++) {
                    if (dp[i - 1][mask1][0] == -1) continue;
                    
                    for (int mask2 = 0; mask2 < (1 << m); mask2++) {
                        if (dp[i - 1][mask1][mask2] == -1) continue;
                        
                        // 检查当前行与前一行、前两行是否冲突
                        if ((state & mask1) == 0 && (state & mask2) == 0) {
                            int newValue = dp[i - 1][mask1][mask2] + count;
                            if (dp[i][state][mask1] < newValue) {
                                dp[i][state][mask1] = newValue;
                            }
                        }
                    }
                }
            }
        }

        // 计算最终结果
        int result = 0;
        for (int mask1 = 0; mask1 < (1 << m); mask1++) {
            for (int mask2 = 0; mask2 < (1 << m); mask2++) {
                if (dp[n][mask1][mask2] > result) {
                    result = dp[n][mask1][mask2];
                }
            }
        }
        return result;
    }

    // 空间优化版本
    public static int artilleryPositionOptimized(int n, int m, char[][] grid) {
        // 预处理每一行的合法地形状态
        int[] validStates = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 'P') {
                    validStates[i] |= (1 << j);
                }
            }
        }

        // 预处理所有可能的行状态（同一行内炮兵不互相攻击）
        int[] allStates = new int[MAX_STATES];
        int[] stateCount = new int[MAX_STATES];
        int totalStates = 0;
        for (int i = 0; i < (1 << m); i++) {
            // 检查同一行内炮兵是否互相攻击（距离小于等于2）
            if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0 && 
                (i & (i >> 1)) == 0 && (i & (i >> 2)) == 0) {
                allStates[totalStates] = i;
                stateCount[totalStates] = Integer.bitCount(i); // 计算该状态下的炮兵数量
                totalStates++;
            }
        }

        // 空间优化的DP数组
        int[][] prev2 = new int[MAX_STATES][MAX_STATES];
        int[][] prev1 = new int[MAX_STATES][MAX_STATES];
        int[][] current = new int[MAX_STATES][MAX_STATES];
        
        // 初始化
        for (int i = 0; i < MAX_STATES; i++) {
            Arrays.fill(prev2[i], -1);
            Arrays.fill(prev1[i], -1);
            Arrays.fill(current[i], -1);
        }
        prev2[0][0] = 0;

        // 状态转移
        for (int i = 1; i <= n; i++) {
            // 初始化当前状态数组
            for (int x = 0; x < MAX_STATES; x++) {
                Arrays.fill(current[x], -1);
            }
            
            for (int j = 0; j < totalStates; j++) {
                int state = allStates[j];
                int count = stateCount[j];
                
                // 检查当前状态是否在当前行的合法地形内
                if ((state & validStates[i - 1]) != state) {
                    continue;
                }

                // 枚举前两行的状态
                for (int mask1 = 0; mask1 < (1 << m); mask1++) {
                    if (prev1[mask1][0] == -1) continue;
                    
                    for (int mask2 = 0; mask2 < (1 << m); mask2++) {
                        if (prev1[mask1][mask2] == -1) continue;
                        
                        // 检查当前行与前一行、前两行是否冲突
                        if ((state & mask1) == 0 && (state & mask2) == 0) {
                            int newValue = prev1[mask1][mask2] + count;
                            if (current[state][mask1] < newValue) {
                                current[state][mask1] = newValue;
                            }
                        }
                    }
                }
            }
            
            // 交换数组
            int[][] temp = prev2;
            prev2 = prev1;
            prev1 = current;
            current = temp;
        }

        // 计算最终结果
        int result = 0;
        for (int mask1 = 0; mask1 < (1 << m); mask1++) {
            for (int mask2 = 0; mask2 < (1 << m); mask2++) {
                if (prev1[mask1][mask2] > result) {
                    result = prev1[mask1][mask2];
                }
            }
        }
        return result;
    }

    // CodeForces 165E 最大兼容数对解法
    public static int[] compatibleNumbers(int[] nums) {
        int n = nums.length;
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        // 找到最大值的位数
        int bits = 0;
        while ((1 << bits) <= maxVal) {
            bits++;
        }
        
        // 预处理每个数字的补集
        int[] complement = new int[1 << bits];
        Arrays.fill(complement, -1);
        
        // 将数组中的数字存入complement数组
        for (int i = 0; i < n; i++) {
            complement[nums[i]] = i;
        }
        
        // 结果数组
        int[] result = new int[n];
        Arrays.fill(result, -1);
        
        // 对每个数字寻找兼容数
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            // 枚举num的补集的所有子集
            int mask = ((1 << bits) - 1) ^ num;
            for (int subMask = mask; subMask > 0; subMask = (subMask - 1) & mask) {
                if (complement[subMask] != -1) {
                    result[i] = complement[subMask];
                    break;
                }
            }
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试 POJ 1185 炮兵阵地
        char[][] grid1 = {
            {'P', 'H', 'P', 'P', 'P'},
            {'P', 'P', 'P', 'H', 'P'},
            {'P', 'H', 'P', 'P', 'P'},
            {'P', 'P', 'P', 'P', 'P'},
            {'P', 'H', 'P', 'P', 'P'}
        };
        System.out.println("POJ 1185 炮兵阵地 测试:");
        System.out.println("结果: " + artilleryPosition(5, 5, grid1));
        System.out.println("优化版结果: " + artilleryPositionOptimized(5, 5, grid1));

        // 测试 CodeForces 165E 最大兼容数对
        int[] nums1 = {3, 1, 4, 2};
        System.out.println("\nCodeForces 165E 最大兼容数对 测试:");
        int[] result1 = compatibleNumbers(nums1);
        System.out.print("数组: ");
        for (int num : nums1) {
            System.out.print(num + " ");
        }
        System.out.println();
        System.out.print("结果: ");
        for (int idx : result1) {
            System.out.print(idx + " ");
        }
        System.out.println();
    }
    
    // 补充题目2: LeetCode 1240 - 覆盖所有点的最小矩形数目
    // 使用回溯+剪枝的方法
    private static int minSquares = Integer.MAX_VALUE;
    
    public static int tilingRectangle(int n, int m) {
        // 确保n <= m，优化搜索空间
        if (n > m) {
            int temp = n;
            n = m;
            m = temp;
        }
        
        minSquares = Integer.MAX_VALUE;
        int[][] grid = new int[n][m]; // 0表示未覆盖，1表示已覆盖
        backtrack(grid, n, m, 0, 0, 0);
        return minSquares;
    }
    
    private static void backtrack(int[][] grid, int n, int m, int x, int y, int count) {
        // 剪枝：如果当前计数已经大于等于已知的最小值，直接返回
        if (count >= minSquares) {
            return;
        }
        
        // 如果已经处理完所有行，更新最小值并返回
        if (x >= n) {
            minSquares = Math.min(minSquares, count);
            return;
        }
        
        // 如果已经处理完当前行，处理下一行
        if (y >= m) {
            backtrack(grid, n, m, x + 1, 0, count);
            return;
        }
        
        // 如果当前位置已经被覆盖，处理下一个位置
        if (grid[x][y] == 1) {
            backtrack(grid, n, m, x, y + 1, count);
            return;
        }
        
        // 尝试放置不同大小的正方形
        int maxSize = Math.min(n - x, m - y);
        for (int size = maxSize; size >= 1; size--) {
            // 检查这个大小的正方形是否可以放置
            boolean canPlace = true;
            for (int i = 0; i < size && canPlace; i++) {
                for (int j = 0; j < size; j++) {
                    if (grid[x + i][y + j] == 1) {
                        canPlace = false;
                        break;
                    }
                }
            }
            
            if (!canPlace) {
                continue;
            }
            
            // 放置正方形
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    grid[x + i][y + j] = 1;
                }
            }
            
            // 递归处理下一个位置
            backtrack(grid, n, m, x, y + size, count + 1);
            
            // 回溯，移除正方形
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    grid[x + i][y + j] = 0;
                }
            }
        }
    }
    
    // 补充题目3: LeetCode 233 - 数字1的个数
    public static int countDigitOne(int n) {
        if (n <= 0) {
            return 0;
        }
        
        int count = 0;
        long divisor = 1; // 使用long避免溢出
        
        while (divisor <= n) {
            long higher = n / (divisor * 10);
            long current = (n / divisor) % 10;
            long lower = n % divisor;
            
            if (current == 0) {
                count += higher * divisor;
            } else if (current == 1) {
                count += higher * divisor + lower + 1;
            } else {
                count += (higher + 1) * divisor;
            }
            
            divisor *= 10;
        }
        
        return count;
    }
    
    // 补充题目4: LeetCode 213 - 打家劫舍II
    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        
        // 情况1：不偷第一个房屋
        int max1 = robRange(nums, 1, nums.length - 1);
        
        // 情况2：不偷最后一个房屋
        int max2 = robRange(nums, 0, nums.length - 2);
        
        return Math.max(max1, max2);
    }
    
    // 计算从start到end范围内能偷到的最大金额
    private static int robRange(int[] nums, int start, int end) {
        int prev = 0; // dp[i-2]
        int curr = nums[start]; // dp[i-1]
        
        for (int i = start + 1; i <= end; i++) {
            int temp = curr;
            curr = Math.max(curr, prev + nums[i]);
            prev = temp;
        }
        
        return curr;
    }
    

}

/*
 * C++ 实现
 */
// #include <iostream>
// #include <vector>
// #include <string>
// #include <algorithm>
// #include <climits>
// #include <cstring>
// using namespace std;

// // POJ 1185 炮兵阵地 解法
// class ArtilleryPosition {
// public:
//     static const int MAXN = 105;
//     static const int MAXM = 15;
//     static const int MAX_STATES = 1 << 10;
// 
//     int artilleryPosition(int n, int m, vector<vector<char>>& grid) {
//         // 预处理每一行的合法地形状态
//         vector<int> validStates(n, 0);
//         for (int i = 0; i < n; i++) {
//             for (int j = 0; j < m; j++) {
//                 if (grid[i][j] == 'P') {
//                     validStates[i] |= (1 << j);
//                 }
//             }
//         }

//         // 预处理所有可能的行状态
//         vector<int> allStates;
//         vector<int> stateCount;
//         for (int i = 0; i < (1 << m); i++) {
//             if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0 &&
//                 (i & (i >> 1)) == 0 && (i & (i >> 2)) == 0) {
//                 allStates.push_back(i);
//                 stateCount.push_back(__builtin_popcount(i));
//             }
//         }

//         // DP数组初始化
//         vector<vector<vector<int>>> dp(n + 1, vector<vector<int>>(MAX_STATES, vector<int>(MAX_STATES, -1)));
//         dp[0][0][0] = 0;

//         // 状态转移
//         for (int i = 1; i <= n; i++) {
//             for (int j = 0; j < allStates.size(); j++) {
//                 int state = allStates[j];
//                 int count = stateCount[j];

//                 if ((state & validStates[i - 1]) != state) continue;

//                 for (int mask1 = 0; mask1 < (1 << m); mask1++) {
//                     if (dp[i - 1][mask1][0] == -1) continue;

//                     for (int mask2 = 0; mask2 < (1 << m); mask2++) {
//                         if (dp[i - 1][mask1][mask2] == -1) continue;

//                         if ((state & mask1) == 0 && (state & mask2) == 0) {
//                             int newValue = dp[i - 1][mask1][mask2] + count;
//                             if (dp[i][state][mask1] < newValue) {
//                                 dp[i][state][mask1] = newValue;
//                             }
//                         }
//                     }
//                 }
//             }
//         }

//         // 计算结果
//         int result = 0;
//         for (int mask1 = 0; mask1 < (1 << m); mask1++) {
//             for (int mask2 = 0; mask2 < (1 << m); mask2++) {
//                 result = max(result, dp[n][mask1][mask2]);
//             }
//         }
//         return result;
//     }
// 
//     // 空间优化版本
//     int artilleryPositionOptimized(int n, int m, vector<vector<char>>& grid) {
//         vector<int> validStates(n, 0);
//         for (int i = 0; i < n; i++) {
//             for (int j = 0; j < m; j++) {
//                 if (grid[i][j] == 'P') {
//                     validStates[i] |= (1 << j);
//                 }
//             }
//         }

//         vector<int> allStates;
//         vector<int> stateCount;
//         for (int i = 0; i < (1 << m); i++) {
//             if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0 &&
//                 (i & (i >> 1)) == 0 && (i & (i >> 2)) == 0) {
//                 allStates.push_back(i);
//                 stateCount.push_back(__builtin_popcount(i));
//             }
//         }

//         vector<vector<int>> prev2(MAX_STATES, vector<int>(MAX_STATES, -1));
//         vector<vector<int>> prev1(MAX_STATES, vector<int>(MAX_STATES, -1));
//         vector<vector<int>> current(MAX_STATES, vector<int>(MAX_STATES, -1));
//         prev2[0][0] = 0;

//         for (int i = 1; i <= n; i++) {
//             // 重置当前状态
//             for (auto& row : current) fill(row.begin(), row.end(), -1);

//             for (int j = 0; j < allStates.size(); j++) {
//                 int state = allStates[j];
//                 int count = stateCount[j];

//                 if ((state & validStates[i - 1]) != state) continue;

//                 for (int mask1 = 0; mask1 < (1 << m); mask1++) {
//                     if (prev1[mask1][0] == -1) continue;

//                     for (int mask2 = 0; mask2 < (1 << m); mask2++) {
//                         if (prev1[mask1][mask2] == -1) continue;

//                         if ((state & mask1) == 0 && (state & mask2) == 0) {
//                             int newValue = prev1[mask1][mask2] + count;
//                             if (current[state][mask1] < newValue) {
//                                 current[state][mask1] = newValue;
//                             }
//                         }
//                     }
//                 }
//             }

//             // 交换数组
//             prev2.swap(prev1);
//             prev1.swap(current);
//         }

//         int result = 0;
//         for (int mask1 = 0; mask1 < (1 << m); mask1++) {
//             for (int mask2 = 0; mask2 < (1 << m); mask2++) {
//                 result = max(result, prev1[mask1][mask2]);
//             }
//         }
//         return result;
//     }
// };

// // CodeForces 165E 最大兼容数对解法
// vector<int> compatibleNumbers(vector<int>& nums) {
//     int n = nums.size();
//     int maxVal = 0;
//     for (int num : nums) maxVal = max(maxVal, num);

//     int bits = 0;
//     while ((1 << bits) <= maxVal) bits++;

//     vector<int> complement(1 << bits, -1);
//     for (int i = 0; i < n; i++) {
//         complement[nums[i]] = i;
//     }

//     vector<int> result(n, -1);
//     for (int i = 0; i < n; i++) {
//         int num = nums[i];
//         int mask = ((1 << bits) - 1) ^ num;
//         for (int subMask = mask; subMask > 0; subMask = (subMask - 1) & mask) {
//             if (complement[subMask] != -1) {
//                 result[i] = complement[subMask];
//                 break;
//             }
//         }
//     }

//     return result;
// }

// // LeetCode 1240 覆盖所有点的最小矩形数目
// int tilingRectangle(int n, int m) {
//     if (n > m) swap(n, m);
//     int minSquares = INT_MAX;
//     vector<vector<int>> grid(n, vector<int>(m, 0));

//     function<void(int, int, int)> backtrack = [&](int x, int y, int count) {
//         if (count >= minSquares) return;
//         if (x >= n) {
//             minSquares = min(minSquares, count);
//             return;
//         }
//         if (y >= m) {
//             backtrack(x + 1, 0, count);
//             return;
//         }
//         if (grid[x][y]) {
//             backtrack(x, y + 1, count);
//             return;
//         }

//         int maxSize = min(n - x, m - y);
//         for (int size = maxSize; size >= 1; size--) {
//             bool canPlace = true;
//             for (int i = 0; i < size && canPlace; i++) {
//                 for (int j = 0; j < size; j++) {
//                     if (grid[x + i][y + j]) {
//                         canPlace = false;
//                         break;
//                     }
//                 }
//             }

//             if (!canPlace) continue;

//             // 放置正方形
//             for (int i = 0; i < size; i++) {
//                 for (int j = 0; j < size; j++) {
//                     grid[x + i][y + j] = 1;
//                 }
//             }

//             backtrack(x, y + size, count + 1);

//             // 回溯
//             for (int i = 0; i < size; i++) {
//                 for (int j = 0; j < size; j++) {
//                     grid[x + i][y + j] = 0;
//                 }
//             }
//         }
//     };

//     backtrack(0, 0, 0);
//     return minSquares;
// }

// // LeetCode 233 数字1的个数
// int countDigitOne(int n) {
//     if (n <= 0) return 0;

//     int count = 0;
//     long divisor = 1;

//     while (divisor <= n) {
//         long higher = n / (divisor * 10);
//         long current = (n / divisor) % 10;
//         long lower = n % divisor;

//         if (current == 0) {
//             count += higher * divisor;
//         } else if (current == 1) {
//             count += higher * divisor + lower + 1;
//         } else {
//             count += (higher + 1) * divisor;
//         }

//         divisor *= 10;
//     }

//     return count;
// }

// // LeetCode 213 打家劫舍II
// int robRange(vector<int>& nums, int start, int end) {
//     int prev = 0;
//     int curr = nums[start];

//     for (int i = start + 1; i <= end; i++) {
//         int temp = curr;
//         curr = max(curr, prev + nums[i]);
//         prev = temp;
//     }

//     return curr;
// }

// int rob(vector<int>& nums) {
//     int n = nums.size();
//     if (n == 0) return 0;
//     if (n == 1) return nums[0];
//     if (n == 2) return max(nums[0], nums[1]);

//     int max1 = robRange(nums, 1, n - 1);
//     int max2 = robRange(nums, 0, n - 2);

//     return max(max1, max2);
// }

// int main() {
//     // 测试 POJ 1185 炮兵阵地
//     vector<vector<char>> grid1 = {
//         {'P', 'H', 'P', 'P', 'P'},
//         {'P', 'P', 'P', 'H', 'P'},
//         {'P', 'H', 'P', 'P', 'P'},
//         {'P', 'P', 'P', 'P', 'P'},
//         {'P', 'H', 'P', 'P', 'P'}
//     };
//     ArtilleryPosition ap;
//     cout << "POJ 1185 炮兵阵地 测试:" << endl;
//     cout << "结果: " << ap.artilleryPosition(5, 5, grid1) << endl;
//     cout << "优化版结果: " << ap.artilleryPositionOptimized(5, 5, grid1) << endl;

//     // 测试 CodeForces 165E 最大兼容数对
//     vector<int> nums1 = {3, 1, 4, 2};
//     cout << "\nCodeForces 165E 最大兼容数对 测试:" << endl;
//     vector<int> result1 = compatibleNumbers(nums1);
//     cout << "数组: ";
//     for (int num : nums1) cout << num << " ";
//     cout << endl;
//     cout << "结果: ";
//     for (int idx : result1) cout << idx << " ";
//     cout << endl;

//     // 测试 LeetCode 1240 覆盖所有点的最小矩形数目
//     cout << "\nLeetCode 1240 覆盖所有点的最小矩形数目 测试:" << endl;
//     cout << "3x3: " << tilingRectangle(3, 3) << endl;
//     cout << "2x3: " << tilingRectangle(2, 3) << endl;

//     // 测试 LeetCode 233 数字1的个数
//     cout << "\nLeetCode 233 数字1的个数 测试:" << endl;
//     cout << "13: " << countDigitOne(13) << endl;
//     cout << "0: " << countDigitOne(0) << endl;

//     // 测试 LeetCode 213 打家劫舍II
//     cout << "\nLeetCode 213 打家劫舍II 测试:" << endl;
//     vector<int> nums2 = {2, 3, 2};
//     cout << "[2,3,2]: " << rob(nums2) << endl;
//     vector<int> nums3 = {1, 2, 3, 1};
//     cout << "[1,2,3,1]: " << rob(nums3) << endl;

//     return 0;
// }

/*
 * Python 实现（注释版本）
 */
// import sys
// sys.setrecursionlimit(1 << 25)

// // POJ 1185 炮兵阵地 解法
// class ArtilleryPosition:
//     MAX_STATES = 1 << 10

//     def artilleryPosition(self, n, m, grid):
//         // 预处理每一行的合法地形状态
//         valid_states = [0] * n
//         for i in range(n):
//             for j in range(m):
//                 if grid[i][j] == 'P':
//                     valid_states[i] |= (1 << j)

//         // 预处理所有可能的行状态
//         all_states = []
//         state_count = []
//         for i in range(1 << m):
//             if (i & (i << 1)) == 0 and (i & (i << 2)) == 0 and \
//                (i & (i >> 1)) == 0 and (i & (i >> 2)) == 0:
//                 all_states.append(i)
//                 state_count.append(bin(i).count('1'))

//         // DP数组初始化
//         dp = [[[-1] * self.MAX_STATES for _ in range(self.MAX_STATES)] for __ in range(n + 1)]
//         dp[0][0][0] = 0

//         // 状态转移
//         for i in range(1, n + 1):
//             for j in range(len(all_states)):
//                 state = all_states[j]
//                 count = state_count[j]

//                 if (state & valid_states[i - 1]) != state:
//                     continue

//                 for mask1 in range(1 << m):
//                     if dp[i - 1][mask1][0] == -1:
//                         continue

//                     for mask2 in range(1 << m):
//                         if dp[i - 1][mask1][mask2] == -1:
//                             continue

//                         if (state & mask1) == 0 and (state & mask2) == 0:
//                             new_value = dp[i - 1][mask1][mask2] + count
//                             if dp[i][state][mask1] < new_value:
//                                 dp[i][state][mask1] = new_value

//         // 计算结果
//         result = 0
//         for mask1 in range(1 << m):
//             for mask2 in range(1 << m):
//                 if dp[n][mask1][mask2] > result:
//                     result = dp[n][mask1][mask2]
//         return result

//     // 空间优化版本
//     def artilleryPositionOptimized(self, n, m, grid):
//         valid_states = [0] * n
//         for i in range(n):
//             for j in range(m):
//                 if grid[i][j] == 'P':
//                     valid_states[i] |= (1 << j)

//         all_states = []
//         state_count = []
//         for i in range(1 << m):
//             if (i & (i << 1)) == 0 and (i & (i << 2)) == 0 and \
//                (i & (i >> 1)) == 0 and (i & (i >> 2)) == 0:
//                 all_states.append(i)
//                 state_count.append(bin(i).count('1'))

//         // 初始化三个二维数组
//         prev2 = [[-1] * self.MAX_STATES for _ in range(self.MAX_STATES)]
//         prev1 = [[-1] * self.MAX_STATES for _ in range(self.MAX_STATES)]
//         current = [[-1] * self.MAX_STATES for _ in range(self.MAX_STATES)]
//         prev2[0][0] = 0

//         for i in range(1, n + 1):
//             // 重置当前状态
//             for row in current:
//                 row[:] = [-1] * self.MAX_STATES

//             for j in range(len(all_states)):
//                 state = all_states[j]
//                 count = state_count[j]

//                 if (state & valid_states[i - 1]) != state:
//                     continue

//                 for mask1 in range(1 << m):
//                     if prev1[mask1][0] == -1:
//                         continue

//                     for mask2 in range(1 << m):
//                         if prev1[mask1][mask2] == -1:
//                             continue

//                         if (state & mask1) == 0 and (state & mask2) == 0:
//                             new_value = prev1[mask1][mask2] + count
//                             if current[state][mask1] < new_value:
//                                 current[state][mask1] = new_value

//             // 交换数组
//             prev2, prev1, current = prev1, current, prev2

//         // 计算结果
//         result = 0
//         for mask1 in range(1 << m):
//             for mask2 in range(1 << m):
//                 if prev1[mask1][mask2] > result:
//                     result = prev1[mask1][mask2]
//         return result

// // CodeForces 165E 最大兼容数对解法
// def compatibleNumbers(nums):
//     n = len(nums)
//     if n == 0:
//         return []
//     
//     max_val = max(nums)
//     bits = 0
//     while (1 << bits) <= max_val:
//         bits += 1
//     
//     complement = [-1] * (1 << bits)
//     for i in range(n):
//         complement[nums[i]] = i
//     
//     result = [-1] * n
//     for i in range(n):
//         num = nums[i]
//         mask = ((1 << bits) - 1) ^ num
//         sub_mask = mask
//         while sub_mask > 0:
//             if complement[sub_mask] != -1:
//                 result[i] = complement[sub_mask]
//                 break
//             sub_mask = (sub_mask - 1) & mask
//     
//     return result

// // LeetCode 1240 覆盖所有点的最小矩形数目
// def tilingRectangle(n, m):
//     if n > m:
//         n, m = m, n
//     
//     min_squares = float('inf')
//     grid = [[0] * m for _ in range(n)]
//     
//     def backtrack(x, y, count):
//         nonlocal min_squares
//         if count >= min_squares:
//             return
//         if x >= n:
//             min_squares = min(min_squares, count)
//             return
//         if y >= m:
//             backtrack(x + 1, 0, count)
//             return
//         if grid[x][y]:
//             backtrack(x, y + 1, count)
//             return
//         
//         max_size = min(n - x, m - y)
//         for size in range(max_size, 0, -1):
//             // 检查是否可以放置这个大小的正方形
//             can_place = True
//             for i in range(size):
//                 for j in range(size):
//                     if grid[x + i][y + j]:
//                         can_place = False
//                         break
//                 if not can_place:
//                     break
//             
//             if not can_place:
//                 continue
//             
//             // 放置正方形
//             for i in range(size):
//                 for j in range(size):
//                     grid[x + i][y + j] = 1
//             
//             backtrack(x, y + size, count + 1)
//             
//             // 回溯
//             for i in range(size):
//                 for j in range(size):
//                     grid[x + i][y + j] = 0
//     
//     backtrack(0, 0, 0)

// // 测试代码
// if __name__ == "__main__":
//     // 测试 POJ 1185 炮兵阵地
//     grid1 = [
//         ['P', 'H', 'P', 'P', 'P'],
//         ['P', 'P', 'P', 'H', 'P'],
//         ['P', 'H', 'P', 'P', 'P'],
//         ['P', 'P', 'P', 'P', 'P'],
//         ['P', 'H', 'P', 'P', 'P']
//     ]
//     ap = ArtilleryPosition()
//     print("POJ 1185 炮兵阵地 测试:")
//     print("结果:", ap.artilleryPosition(5, 5, grid1))
//     print("优化版结果:", ap.artilleryPositionOptimized(5, 5, grid1))
//     
//     // 测试 CodeForces 165E 最大兼容数对
//     nums1 = [3, 1, 4, 2]
//     print("\nCodeForces 165E 最大兼容数对 测试:")
//     result1 = compatibleNumbers(nums1)
//     print("数组:", nums1)
//     print("结果:", result1)
//     
//     // 测试 LeetCode 1240 覆盖所有点的最小矩形数目
//     print("\nLeetCode 1240 覆盖所有点的最小矩形数目 测试:")
//     print("3x3:", tilingRectangle(3, 3))
//     print("2x3:", tilingRectangle(2, 3))
//     
//     // 测试 LeetCode 233 数字1的个数
//     print("\nLeetCode 233 数字1的个数 测试:")
//     print("13:", countDigitOne(13))
//     print("0:", countDigitOne(0))
//     
//     // 测试 LeetCode 213 打家劫舍II
//     print("\nLeetCode 213 打家劫舍II 测试:")
//     print("[2,3,2]:", rob([2, 3, 2]))
//     print("[1,2,3,1]:", rob([1, 2, 3, 1]))
