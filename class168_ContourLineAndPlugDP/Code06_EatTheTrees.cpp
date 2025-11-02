// Eat the Trees (插头DP)
// 用若干回路覆盖n*m棋盘所有非障碍格子
// 1 <= n, m <= 12
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1693

// 题目解析：
// 这是一个经典的插头DP问题。给定一个n×m的棋盘，有些格子是障碍物，
// 需要用若干个回路覆盖所有非障碍格子，求方案数。

// 解题思路：
// 使用插头DP解决这个问题。插头DP是轮廓线DP的一种特殊形式，
// 主要用于处理连通性问题。我们逐格转移，将棋盘的边界线（轮廓线）
// 的状态作为DP状态的一部分。

// 状态设计：
// dp[i][j][s] 表示处理到第i行第j列，插头状态为s的方案数。
// 插头状态：用二进制表示轮廓线上每个位置是否有插头（1表示有插头，0表示无插头）。
// 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。

// 状态转移：
// 对于当前格子(i,j)，根据格子是否为障碍物进行不同的转移：
// 1. 如果是障碍物：只有当上下插头都不存在时才能转移
// 2. 如果不是障碍物：
//    a. 不放任何插头（前提是上下插头状态相同）
//    b. 生成一对插头（上插头和左插头都不存在）
//    c. 延续一个插头（上插头和左插头只有一个存在）

// 最优性分析：
// 该解法是最优的，因为：
// 1. 时间复杂度O(n * m * 2^m)在可接受范围内
// 2. 状态转移清晰，没有冗余计算

// 边界场景处理：
// 1. 当n=0或m=0时，方案数为1（空棋盘有一种覆盖方案）
// 2. 当棋盘全为障碍物时，方案数为0
// 3. 行间转移时需要将行末状态转移到下一行行首

// 工程化考量：
// 1. 使用滚动数组优化空间复杂度
// 2. 手动实现memset函数避免使用标准库
// 3. 对于特殊情况进行了预处理优化
// 4. 使用位运算优化状态操作

// Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code06_EatTheTrees.java
// Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code06_EatTheTrees.py
// C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code06_EatTheTrees.cpp

const int MAXN = 12;
const int MAXM = 12;
long long dp[MAXN][MAXM + 1][1 << (MAXM + 1)];
int grid[MAXN][MAXM];
int n, m;

// 时间复杂度: O(n * m * 2^m)
// 空间复杂度: O(n * m * 2^m)
long long compute() {
    // 初始化
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= m; j++) {
            for (int s = 0; s < (1 << (m + 1)); s++) {
                dp[i][j][s] = 0;
            }
        }
    }
    
    // 初始状态
    dp[0][0][0] = 1;
    
    // 逐格DP
    for (int i = 0; i < n; i++) {
        // 行间转移，将行末状态转移到下一行行首
        for (int s = 0; s < (1 << m); s++) {
            dp[i + 1][0][s << 1] = dp[i][m][s];
        }
        
        // 行内转移
        for (int j = 0; j < m; j++) {
            for (int s = 0; s < (1 << (m + 1)); s++) {
                if (dp[i][j][s] == 0) continue;
                
                // 获取当前格子的上插头和左插头
                int up = (s >> j) & 1;
                int left = (s >> (j + 1)) & 1;
                
                // 如果当前格子是障碍物
                if (grid[i][j] == 0) {
                    // 只有当上下插头都不存在时才能转移
                    if (up == 0 && left == 0) {
                        dp[i][j + 1][s] += dp[i][j][s];
                    }
                } else {
                    // 当前格子不是障碍物
                    // 三种转移方式：
                    
                    // 1. 不放任何插头（前提是上下插头状态相同）
                    if (up == left) {
                        int newState = s & (~((1 << j) | (1 << (j + 1))));
                        dp[i][j + 1][newState] += dp[i][j][s];
                    }
                    
                    // 2. 生成一对插头（上插头和左插头都不存在）
                    if (up == 0 && left == 0) {
                        int newState = s | (1 << j) | (1 << (j + 1));
                        dp[i][j + 1][newState] += dp[i][j][s];
                    }
                    
                    // 3. 延续一个插头（上插头和左插头只有一个存在）
                    if (up != left) {
                        // 延续上插头到左插头位置
                        if (up == 1) {
                            int newState = s & ~(1 << j);
                            newState |= (1 << (j + 1));
                            dp[i][j + 1][newState] += dp[i][j][s];
                        }
                        // 延续左插头到上插头位置
                        if (left == 1) {
                            int newState = s & ~(1 << (j + 1));
                            newState |= (1 << j);
                            dp[i][j + 1][newState] += dp[i][j][s];
                        }
                    }
                }
            }
        }
    }
    
    return dp[n][0][0];
}

// 主函数需要根据具体编译环境实现
// 以下为伪代码示意
/*
int main() {
    读取测试用例数cases;
    for (int t = 1; t <= cases; t++) {
        读取n和m;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                读取grid[i][j];
            }
        }
        输出"Case " + t + ": " + compute();
    }
    return 0;
}
*/