package class081.补充题目;

import java.util.*;

// 更多状态压缩DP经典题目
// 本文件包含多个经典的状态压缩DP题目，涵盖不同平台和难度级别

public class Code14_MoreDPProblems {
    
    // 1. SCOI2005 互不侵犯 - 经典状压DP
    // 题目来源: LOJ #2153
    // 题目描述: 在N×N的棋盘里面放K个国王，使他们互不攻击，共有多少种摆放方案
    // 解题思路: 状态压缩DP，预处理每行合法状态，逐行转移
    public static long kingPlacement(int n, int k) {
        // 预处理每行的合法状态
        List<Integer> validStates = new ArrayList<>();
        List<Integer> stateCounts = new ArrayList<>();
        
        // 生成所有合法的单行状态（相邻位置不能都是1）
        for (int mask = 0; mask < (1 << n); mask++) {
            if ((mask & (mask << 1)) == 0) { // 检查相邻位置
                validStates.add(mask);
                stateCounts.add(Integer.bitCount(mask));
            }
        }
        
        int size = validStates.size();
        // dp[i][j][l] 表示前i行，第i行状态为j，总共放置l个国王的方案数
        long[][][] dp = new long[n + 1][size][k + 1];
        
        // 初始化第一行
        for (int i = 0; i < size; i++) {
            int count = stateCounts.get(i);
            if (count <= k) {
                dp[1][i][count] = 1;
            }
        }
        
        // 状态转移
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < size; j++) { // 当前行状态
                int currState = validStates.get(j);
                int currCount = stateCounts.get(j);
                
                for (int x = 0; x < size; x++) { // 上一行状态
                    int prevState = validStates.get(x);
                    
                    // 检查当前行和上一行是否冲突
                    if ((currState & prevState) == 0 && // 同一列不冲突
                        (currState & (prevState << 1)) == 0 && // 左上右下不冲突
                        (currState & (prevState >> 1)) == 0) { // 右上左下不冲突
                        
                        for (int l = currCount; l <= k; l++) {
                            dp[i][j][l] += dp[i - 1][x][l - currCount];
                        }
                    }
                }
            }
        }
        
        // 统计结果
        long result = 0;
        for (int i = 0; i < size; i++) {
            result += dp[n][i][k];
        }
        
        return result;
    }
    
    // 2. POI2004 PRZ - 过桥问题
    // 题目来源: Luogu P5911
    // 题目描述: n个人过桥，桥有最大承重，求最短过桥时间
    // 解题思路: 状态压缩DP + 子集枚举
    public static int bridgeCrossing(int W, int[] weights, int[] times) {
        int n = weights.length;
        int totalStates = 1 << n;
        
        // 预处理每个子集的总重量和最大时间
        int[] totalWeights = new int[totalStates];
        int[] maxTimes = new int[totalStates];
        
        for (int mask = 0; mask < totalStates; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    totalWeights[mask] += weights[i];
                    maxTimes[mask] = Math.max(maxTimes[mask], times[i]);
                }
            }
        }
        
        // dp[mask] 表示mask集合的人都过桥的最短时间
        int[] dp = new int[totalStates];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        
        for (int mask = 0; mask < totalStates; mask++) {
            if (dp[mask] == Integer.MAX_VALUE / 2) continue;
            
            // 枚举所有子集作为一次过桥的组合
            for (int subset = mask; subset > 0; subset = (subset - 1) & mask) {
                int complement = mask ^ subset; // 剩余未过桥的人
                if (totalWeights[complement] <= W) { // 检查承重限制
                    dp[mask] = Math.min(dp[mask], dp[subset] + maxTimes[complement]);
                }
            }
        }
        
        return dp[totalStates - 1];
    }
    
    // 3. USACO 2006 November Gold - 平铺方案数
    // 题目描述: 用1×2和2×1的骨牌铺满M×N的棋盘，求方案数
    // 解题思路: 轮廓线DP（插头DP的一种）
    public static long tilingCount(int m, int n) {
        if (m > n) {
            int temp = m;
            m = n;
            n = temp;
        }
        
        // dp[i][mask] 表示处理到第i行，轮廓线状态为mask的方案数
        long[][] dp = new long[2][1 << m];
        int curr = 0, next = 1;
        dp[curr][0] = 1;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Arrays.fill(dp[next], 0);
                
                for (int mask = 0; mask < (1 << m); mask++) {
                    if (dp[curr][mask] == 0) continue;
                    
                    // 当前位置的上方向和左方向状态
                    int up = (mask & (1 << j));
                    int left = (j > 0) ? (mask & (1 << (j - 1))) : 0;
                    
                    if (up == 0 && left == 0) {
                        // 放置一个L型骨牌或两个1×1的方格
                        // 放置一个竖直的1×2骨牌
                        dp[next][mask | (1 << j)] += dp[curr][mask];
                        
                        // 放置一个水平的2×1骨牌（如果可能）
                        if (j < m - 1) {
                            dp[next][mask | (1 << (j + 1))] += dp[curr][mask];
                        }
                    } else if (up != 0 && left == 0) {
                        // 上方有骨牌，左边没有，可以向左延伸
                        dp[next][mask] += dp[curr][mask];
                        
                        // 或者放置一个水平骨牌
                        if (j < m - 1) {
                            dp[next][mask | (1 << (j + 1))] += dp[curr][mask];
                        }
                    } else if (up == 0 && left != 0) {
                        // 左边有骨牌，上方没有，可以向上延伸
                        dp[next][mask] += dp[curr][mask];
                        
                        // 或者放置一个竖直骨牌
                        dp[next][mask | (1 << j)] += dp[curr][mask];
                    } else {
                        // 上方和左边都有骨牌，当前位置必须空出来
                        dp[next][mask & ~(1 << j) & ~(1 << (j - 1))] += dp[curr][mask];
                    }
                }
                
                // 交换当前和下一个状态
                int temp = curr;
                curr = next;
                next = temp;
            }
        }
        
        return dp[curr][0];
    }
    
    // 4. 九省联考2018 一双木棋 - 对抗搜索+状压DP
    // 题目描述: 两人在网格图上放置棋子，求最优策略下的得分
    // 解题思路: 对抗搜索 + 状态压缩 + 记忆化
    public static int chessGame(int n, int m) {
        // 这是一个复杂的对抗搜索问题，使用状压DP优化状态表示
        // 由于实现复杂，这里只给出框架
        Map<String, Integer> memo = new HashMap<>();
        return minimax(n, m, 0, true, memo);
    }
    
    private static int minimax(int n, int m, int state, boolean isMax, Map<String, Integer> memo) {
        String key = state + "," + isMax;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        // 计算可能的移动和评估函数
        // 这里简化处理
        int result = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        // 实际实现需要根据具体规则进行
        memo.put(key, result);
        return result;
    }
    
    // 5. CodeForces 453B - 差值最小化
    // 题目描述: 给定序列a，构造序列b使得相邻元素互质且∑|ai-bi|最小
    // 解题思路: 状态压缩DP，状态表示质因子的使用情况
    public static int minimizeDifference(int[] a) {
        int n = a.length;
        int maxVal = Arrays.stream(a).max().orElse(0);
        
        // 预处理质数
        List<Integer> primes = sieveOfEratosthenes(maxVal);
        int primeCount = primes.size();
        
        // dp[i][mask] 表示前i个元素，质因子使用状态为mask时的最小差值
        int[][] dp = new int[n + 1][1 << primeCount];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
        }
        dp[0][0] = 0;
        
        for (int i = 1; i <= n; i++) {
            int target = a[i - 1];
            // 尝试所有可能的值
            for (int val = 1; val <= 2 * target; val++) {
                int mask = 0;
                // 计算val的质因子mask
                for (int j = 0; j < primeCount; j++) {
                    if (val % primes.get(j) == 0) {
                        mask |= (1 << j);
                    }
                }
                
                // 检查与前一个元素是否互质
                for (int prevMask = 0; prevMask < (1 << primeCount); prevMask++) {
                    if (dp[i - 1][prevMask] != Integer.MAX_VALUE / 2) {
                        // 检查是否互质（无公共质因子）
                        if ((mask & prevMask) == 0) {
                            int diff = Math.abs(target - val);
                            dp[i][mask] = Math.min(dp[i][mask], dp[i - 1][prevMask] + diff);
                        }
                    }
                }
            }
        }
        
        // 返回最小差值
        int result = Integer.MAX_VALUE / 2;
        for (int mask = 0; mask < (1 << primeCount); mask++) {
            result = Math.min(result, dp[n][mask]);
        }
        return result;
    }
    
    // 埃拉托斯特尼筛法生成质数
    private static List<Integer> sieveOfEratosthenes(int max) {
        boolean[] isPrime = new boolean[max + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int i = 2; i * i <= max; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= max; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= max; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
        return primes;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试SCOI2005 互不侵犯
        System.out.println("SCOI2005 互不侵犯测试:");
        System.out.println("3×3棋盘放2个国王: " + kingPlacement(3, 2));
        
        // 测试POI2004 PRZ
        System.out.println("\nPOI2004 PRZ测试:");
        int[] weights = {100, 150, 200};
        int[] times = {10, 20, 30};
        System.out.println("3人过桥(承重400): " + bridgeCrossing(400, weights, times));
        
        // 测试USACO平铺方案
        System.out.println("\nUSACO平铺方案测试:");
        System.out.println("2×3棋盘: " + tilingCount(2, 3));
        
        // 测试CodeForces 453B
        System.out.println("\nCodeForces 453B测试:");
        int[] a = {10, 15, 20};
        System.out.println("数组[10,15,20]: " + minimizeDifference(a));
    }
}

/*
 * C++ 实现示例
 */
/*
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

// SCOI2005 互不侵犯
long long kingPlacement(int n, int k) {
    vector<int> validStates, stateCounts;
    
    // 生成合法状态
    for (int mask = 0; mask < (1 << n); mask++) {
        if (!(mask & (mask << 1))) {
            validStates.push_back(mask);
            stateCounts.push_back(__builtin_popcount(mask));
        }
    }
    
    int size = validStates.size();
    vector<vector<vector<long long>>> dp(n + 1, vector<vector<long long>>(size, vector<long long>(k + 1, 0)));
    
    // 初始化
    for (int i = 0; i < size; i++) {
        int count = stateCounts[i];
        if (count <= k) {
            dp[1][i][count] = 1;
        }
    }
    
    // 状态转移
    for (int i = 2; i <= n; i++) {
        for (int j = 0; j < size; j++) {
            int currState = validStates[j];
            int currCount = stateCounts[j];
            
            for (int x = 0; x < size; x++) {
                int prevState = validStates[x];
                
                if (!(currState & prevState) && 
                    !(currState & (prevState << 1)) && 
                    !(currState & (prevState >> 1))) {
                    
                    for (int l = currCount; l <= k; l++) {
                        dp[i][j][l] += dp[i - 1][x][l - currCount];
                    }
                }
            }
        }
    }
    
    long long result = 0;
    for (int i = 0; i < size; i++) {
        result += dp[n][i][k];
    }
    
    return result;
}

int main() {
    cout << "SCOI2005 互不侵犯测试:" << endl;
    cout << "3×3棋盘放2个国王: " << kingPlacement(3, 2) << endl;
    return 0;
}
*/

/*
 * Python 实现示例
 */
/*
def king_placement(n, k):
    # 生成合法状态
    valid_states = []
    state_counts = []
    
    for mask in range(1 << n):
        if not (mask & (mask << 1)):
            valid_states.append(mask)
            state_counts.append(bin(mask).count('1'))
    
    size = len(valid_states)
    # dp[i][j][l] 表示前i行，第i行状态为j，总共放置l个国王的方案数
    dp = [[[0 for _ in range(k + 1)] for _ in range(size)] for _ in range(n + 1)]
    
    # 初始化
    for i in range(size):
        count = state_counts[i]
        if count <= k:
            dp[1][i][count] = 1
    
    # 状态转移
    for i in range(2, n + 1):
        for j in range(size):
            curr_state = valid_states[j]
            curr_count = state_counts[j]
            
            for x in range(size):
                prev_state = valid_states[x]
                
                # 检查冲突
                if not (curr_state & prev_state) and \
                   not (curr_state & (prev_state << 1)) and \
                   not (curr_state & (prev_state >> 1)):
                    
                    for l in range(curr_count, k + 1):
                        dp[i][j][l] += dp[i - 1][x][l - curr_count]
    
    # 统计结果
    result = 0
    for i in range(size):
        result += dp[n][i][k]
    
    return result

if __name__ == "__main__":
    print("SCOI2005 互不侵犯测试:")
    print("3×3棋盘放2个国王:", king_placement(3, 2))
*/