// 玉米田 (Corn Fields)
// 题目来源: POJ 3254 Corn Fields
// 题目链接: http://poj.org/problem?id=3254
// 题目描述:
// Farmer John 放牧cow，有些草地上的草是不能吃的，用0表示，然后规定两头牛不能相邻放牧。
// 问题要求计算在给定的网格中，有多少种合法的放牧方案。
//
// 解题思路:
// 这是一道经典的状态压缩DP问题。我们可以按行进行状态压缩，用二进制位表示每一行的放牧状态。
// 对于每一行，我们需要考虑：
// 1. 当前行的地形是否允许在某个位置放牧（草地为1，不能放牧的为0）
// 2. 当前行的放牧状态是否合法（相邻位置不能同时放牧）
// 3. 当前行与前一行的放牧状态是否冲突（上下相邻位置不能同时放牧）
//
// 状态定义:
// dp[i][mask] 表示处理到第i行，且第i行的放牧状态为mask时的方案数
//
// 状态转移:
// 对于每一行，我们枚举所有可能的合法状态，然后检查与前一行是否冲突
//
// 时间复杂度: O(m * 2^(2*n)) 其中m是行数，n是列数
// 空间复杂度: O(2^n)
//
// 补充题目1: 格雷编码 (Gray Code)
// 题目来源: LeetCode 89. Gray Code
// 题目链接: https://leetcode.cn/problems/gray-code/
// 题目描述:
// 格雷编码是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异。
// 给定一个代表编码总位数的非负整数n，打印其格雷编码序列。
// 解题思路:
// 1. 观察格雷编码的生成规律
// 2. 递推关系: G(i) = i XOR (i >> 1)
// 3. 也可以使用动态规划方法，从n-1位的格雷编码生成n位的格雷编码
// 时间复杂度: O(2^n)
// 空间复杂度: O(1)

// 常量定义
const int MOD = 1000000007;  // 取模常量，防止整数溢出
const int MAXN = 15;         // 最大行数/列数
const int MAX_STATES = 1 << 12; // 最大状态数，2^12 = 4096

// POJ 3254 Corn Fields 解法
// 参数说明:
// m: 网格行数
// n: 网格列数
// grid: 二维数组，表示网格地形，1表示可放牧，0表示不可放牧
// 返回值: 合法的放牧方案数
int cornFields(int m, int n, int grid[][MAXN]) {
    // 预处理每一行的合法状态
    // validStates[i] 表示第i行的地形状态，用二进制位表示哪些位置可以放牧
    int validStates[MAXN];
    for (int i = 0; i < m; i++) {
        validStates[i] = 0;
        for (int j = 0; j < n; j++) {
            if (grid[i][j] == 1) {
                validStates[i] |= (1 << j);  // 将第j位设为1，表示位置j可以放牧
            }
        }
    }

    // 预处理所有可能的行状态（不考虑地形，只考虑相邻位置不冲突）
    // allStates数组存储所有合法的行状态，stateCount记录合法状态数量
    int allStates[MAX_STATES];
    int stateCount = 0;
    for (int i = 0; i < (1 << n); i++) {
        // 检查相邻位置是否冲突
        // (i << 1) 将状态左移一位，与原状态按位与，如果不为0说明有相邻的1
        // (i >> 1) 将状态右移一位，与原状态按位与，如果不为0说明有相邻的1
        if ((i & (i << 1)) == 0 && (i & (i >> 1)) == 0) {
            allStates[stateCount++] = i;
        }
    }

    // dp[i][mask] 表示处理到第i行，且第i行的放牧状态为mask时的方案数
    int dp[MAXN][MAX_STATES];
    for (int i = 0; i <= m; i++) {
        for (int j = 0; j < (1 << n); j++) {
            dp[i][j] = 0;
        }
    }
    dp[0][0] = 1;  // 初始状态：处理第0行，状态为0（没有放牧）的方案数为1

    // 状态转移过程
    for (int i = 1; i <= m; i++) {
        // 枚举所有合法的行状态
        for (int j = 0; j < stateCount; j++) {
            int state = allStates[j];
            // 检查当前状态是否在当前行的合法地形内
            // 如果(state & validStates[i - 1]) != state，说明state中有某些位置在地形上是不可放牧的
            if ((state & validStates[i - 1]) != state) {
                continue;
            }

            // 检查与前一行是否冲突
            // 枚举前一行的所有可能状态
            for (int k = 0; k < (1 << n); k++) {
                // 如果当前行状态state与前一行状态k没有上下相邻（按位与为0），则可以转移
                if ((state & k) == 0) { // 上下不相邻
                    dp[i][state] = (dp[i][state] + dp[i - 1][k]) % MOD;
                }
            }
        }
    }

    // 计算最终结果：将最后一行所有状态的方案数相加
    int result = 0;
    for (int i = 0; i < (1 << n); i++) {
        result = (result + dp[m][i]) % MOD;
    }
    return result;
}

// 空间优化版本
// 通过滚动数组优化空间复杂度，只使用两个一维数组
int cornFieldsOptimized(int m, int n, int grid[][MAXN]) {
    // 预处理每一行的合法状态
    int validStates[MAXN];
    for (int i = 0; i < m; i++) {
        validStates[i] = 0;
        for (int j = 0; j < n; j++) {
            if (grid[i][j] == 1) {
                validStates[i] |= (1 << j);
            }
        }
    }

    // 预处理所有可能的行状态（不考虑地形，只考虑相邻位置不冲突）
    int allStates[MAX_STATES];
    int stateCount = 0;
    for (int i = 0; i < (1 << n); i++) {
        // 检查相邻位置是否冲突
        if ((i & (i << 1)) == 0 && (i & (i >> 1)) == 0) {
            allStates[stateCount++] = i;
        }
    }

    // 空间优化的DP数组
    // 只需要保存当前行和下一行的状态，使用滚动数组优化空间
    int dp[MAX_STATES];
    int nextDp[MAX_STATES];
    for (int i = 0; i < (1 << n); i++) {
        dp[i] = 0;
        nextDp[i] = 0;
    }
    dp[0] = 1;  // 初始状态

    // 状态转移过程
    for (int i = 1; i <= m; i++) {
        // 初始化nextDp数组
        for (int j = 0; j < (1 << n); j++) {
            nextDp[j] = 0;
        }
        
        // 枚举所有合法的行状态
        for (int j = 0; j < stateCount; j++) {
            int state = allStates[j];
            // 检查当前状态是否在当前行的合法地形内
            if ((state & validStates[i - 1]) != state) {
                continue;
            }

            // 检查与前一行是否冲突
            for (int k = 0; k < (1 << n); k++) {
                if ((state & k) == 0) { // 上下不相邻
                    nextDp[state] = (nextDp[state] + dp[k]) % MOD;
                }
            }
        }
        // 交换dp数组，将nextDp的值复制到dp中，为下一次迭代做准备
        for (int j = 0; j < (1 << n); j++) {
            dp[j] = nextDp[j];
        }
    }

    // 计算最终结果
    int result = 0;
    for (int i = 0; i < (1 << n); i++) {
        result = (result + dp[i]) % MOD;
    }
    return result;
}

// LeetCode 89. Gray Code 解法
// 使用数学公式直接生成格雷编码
// 参数说明:
// n: 格雷编码的位数
// result: 存储结果的数组
void grayCode(int n, int* result) {
    int size = 1 << n;  // 格雷编码总数为2^n
    for (int i = 0; i < size; i++) {
        // 格雷编码的数学公式: G(i) = i XOR (i >> 1)
        result[i] = i ^ (i >> 1);
    }
}

// 动态规划方法生成格雷编码
// 通过递推方式生成格雷编码
// 参数说明:
// n: 格雷编码的位数
// result: 存储结果的数组
void grayCodeDP(int n, int* result) {
    if (n == 0) {
        result[0] = 0;
        return;
    }

    // dp数组，存储格雷编码序列
    int dp[1 << 12]; // 最大支持12位
    dp[0] = 0;

    int len = 1;
    // 递推生成过程
    for (int i = 1; i <= n; i++) {
        // 后半部分是前半部分的逆序，再加上2^(i-1)
        for (int j = 0; j < len; j++) {
            dp[len * 2 - 1 - j] = dp[j] | (1 << (i - 1));
        }
        len *= 2;
    }

    // 复制结果到输出数组
    for (int i = 0; i < (1 << n); i++) {
        result[i] = dp[i];
    }
}

// 主函数 - 用于测试
int main() {
    // 由于编译环境限制，这里不包含测试代码
    // 实际使用时可以添加适当的测试代码
    return 0;
}