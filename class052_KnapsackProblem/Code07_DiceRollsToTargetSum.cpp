// 掷骰子的N种方法
// 这里有 n 个一样的骰子，每个骰子上都有 k 个面，分别标号为 1 到 k 。
// 给定三个整数 n、k 和 target，请返回可能的方式(从总共 kn 种方式中)滚动骰子，
// 使得骰子面朝上的数字总和等于 target。
// 由于答案可能很大，你需要对 10^9 + 7 取模。
// 测试链接 : https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/

/*
 * 算法详解：
 * 这是一个典型的分组背包问题。每个骰子可以看作一个组，每个组有k个物品（骰子的面值1到k），
 * 且每个组必须选择一个物品。我们需要计算恰好装满容量为target的背包的方案数。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示使用前i个骰子，使得点数和为j的方案数
 * 2. 状态转移方程：
 *    dp[i][j] = sum(dp[i-1][j-x]) 其中x是第i个骰子的点数（1到k）
 * 3. 初始化：dp[0][0] = 1，表示不使用骰子得到和为0的方案数为1
 * 
 * 时间复杂度分析：
 * 设有n个骰子，每个骰子有k个面，目标和为target
 * 1. 动态规划计算：O(n * target * k)
 * 总时间复杂度：O(n * target * k)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(n * target)
 * 2. 空间优化后：O(target)
 * 
 * 相关题目扩展：
 * 1. LeetCode 1155. 掷骰子的N种方法（本题）
 * 2. HDU 1712 ACboy needs your help（分组背包模板题）
 * 3. 洛谷 P1757 通天之分组背包
 * 4. LeetCode 322. 零钱兑换（完全背包）
 * 5. LeetCode 279. 完全平方数（完全背包）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空输入、非法输入等边界情况
 * 3. 可配置性：可以将MOD作为配置参数传入
 * 4. 单元测试：为numRollsToTarget1和numRollsToTarget2方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用滚动数组优化空间
 * 
 * 语言特性差异：
 * 1. Java：使用静态数组提高访问速度
 * 2. C++：可以使用vector，但要注意内存分配开销
 * 3. Python：列表推导式简洁但性能较低
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从二维dp优化到一维dp
 * 2. 剪枝优化：当j小于当前骰子最小点数或大于最大点数时跳过
 * 3. 前缀和优化：使用前缀和加速状态转移计算
 * 
 * 与标准分组背包的区别：
 * 1. 选择限制：标准分组背包每组最多选一个物品，本题每组必须选一个物品
 * 2. 目标函数：标准分组背包求最大价值，本题求方案数
 * 3. 状态初始化：标准分组背包dp[0][0] = 0，本题dp[0][0] = 1
 */

const int MOD = 1000000007;
const int MAXN = 31;
const int MAXK = 31;
const int MAXT = 1001;

// dp[i][j]表示使用前i个骰子，使得点数和为j的方案数
int dp[MAXN][MAXT];

// 空间压缩版本
int dp2[MAXT];

// 简单的最小值函数
int min(int a, int b) {
    return a < b ? a : b;
}

// 简单的内存设置函数
void memset(int* arr, int value, int size) {
    for (int i = 0; i < size/sizeof(int); i++) {
        arr[i] = value;
    }
}

int numRollsToTarget1(int n, int k, int target) {
    // 初始化
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= target; j++) {
            dp[i][j] = 0;
        }
    }
    dp[0][0] = 1;
    
    // 动态规划填表
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j <= target; j++) {
            // 枚举第i个骰子的点数
            for (int x = 1; x <= min(k, j); x++) {
                dp[i][j] = (dp[i][j] + dp[i-1][j-x]) % MOD;
            }
        }
    }
    
    return dp[n][target];
}

// 空间压缩版本
int numRollsToTarget2(int n, int k, int target) {
    // 初始化
    for (int j = 0; j <= target; j++) {
        dp2[j] = 0;
    }
    dp2[0] = 1;
    
    // 动态规划填表
    for (int i = 1; i <= n; i++) {
        // 从后往前更新，避免重复使用本轮更新的值
        for (int j = target; j >= 0; j--) {
            dp2[j] = 0;
            // 枚举第i个骰子的点数
            for (int x = 1; x <= min(k, j); x++) {
                dp2[j] = (dp2[j] + dp2[j-x]) % MOD;
            }
        }
    }
    
    return dp2[target];
}

// 简单的输出函数
void print_result(char* msg, int result) {
    // 简化输出，实际使用时可以替换为printf或其他输出方式
    // 这里只是示意，实际C++环境中可以使用printf
}

// 测试方法
int main() {
    // 测试用例1: n = 1, k = 6, target = 3
    // 预期输出: 1
    int result1 = numRollsToTarget1(1, 6, 3);
    int result2 = numRollsToTarget2(1, 6, 3);
    
    // 测试用例2: n = 2, k = 6, target = 7
    // 预期输出: 6
    int result3 = numRollsToTarget1(2, 6, 7);
    int result4 = numRollsToTarget2(2, 6, 7);
    
    // 测试用例3: n = 30, k = 30, target = 500
    // 预期输出: 222616187
    int result5 = numRollsToTarget1(30, 30, 500);
    int result6 = numRollsToTarget2(30, 30, 500);
    
    return 0;
}