// ACboy needs your help
// ACboy现在有N门课程，他计划最多花费M天时间学习。
// 对于每门课程，学习不同的天数会获得不同的收益。
// 问题是：在M天内，ACboy能获得的最大收益是多少？
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1712

/*
 * 算法详解：
 * 这是一个标准的分组背包问题。每门课程可以看作一个组，学习不同的天数获得不同的收益，
 * 且每门课程最多只能选择一个学习天数方案。我们需要在总天数限制下获得最大收益。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示前i门课程，在j天内能获得的最大收益
 * 2. 状态转移方程：
 *    dp[i][j] = max(dp[i-1][j], dp[i-1][j-k] + value[i][k]) 
 *    其中k是第i门课程学习的天数（1到该课程的天数上限）
 * 3. 初始化：dp[0][j] = 0，表示没有课程时收益为0
 * 
 * 时间复杂度分析：
 * 设有n门课程，总天数为m，每门课程平均有t个学习方案
 * 1. 动态规划计算：O(n * m * t)
 * 总时间复杂度：O(n * m * t)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(n * m)
 * 2. 空间优化后：O(m)
 * 
 * 相关题目扩展：
 * 1. HDU 1712 ACboy needs your help（本题）
 * 2. 洛谷 P1757 通天之分组背包
 * 3. LeetCode 1155. 掷骰子的N种方法（分组背包思想）
 * 4. 洛谷 P1064 金明的预算方案（依赖背包）
 * 5. 洛谷 P1941 飞扬的小鸟（多重背包+分组背包）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空输入、非法输入等边界情况
 * 3. 可配置性：可以将MAXN和MAXM作为配置参数传入
 * 4. 单元测试：为solve1和solve2方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用滚动数组优化空间
 * 
 * 语言特性差异：
 * 1. Java：使用静态数组提高访问速度，StreamTokenizer优化输入
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
 * 2. 剪枝优化：当背包容量不足以容纳某组任何物品时跳过
 * 3. 预处理优化：提前对物品按组号排序
 * 
 * 与标准背包的区别：
 * 1. 分组限制：每组最多选一个物品，而不是每个物品选或不选
 * 2. 物品组织：物品按组组织，而不是线性排列
 * 3. 状态转移：需要遍历组内所有物品进行比较
 */

const int MAXN = 101;
const int MAXM = 101;

// value[i][j]表示第i门课程学习j天能获得的收益
int value[MAXN][MAXM];

// dp[i][j]表示前i门课程，在j天内能获得的最大收益
int dp[MAXN][MAXM];

// 空间压缩版本
int dp2[MAXM];

int n, m;

// 简单的最大值函数
int max(int a, int b) {
    return a > b ? a : b;
}

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

int solve1() {
    // 初始化
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= m; j++) {
            dp[i][j] = 0;
        }
    }
    
    // 动态规划填表
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j <= m; j++) {
            // 不选择第i门课程的任何学习方案
            dp[i][j] = dp[i-1][j];
            
            // 遍历第i门课程的所有学习方案
            for (int k = 1; k <= min(j, m); k++) {
                if (j >= k) {
                    dp[i][j] = max(dp[i][j], dp[i-1][j-k] + value[i][k]);
                }
            }
        }
    }
    
    return dp[n][m];
}

// 空间压缩版本
int solve2() {
    // 初始化
    for (int j = 0; j <= m; j++) {
        dp2[j] = 0;
    }
    
    // 动态规划填表
    for (int i = 1; i <= n; i++) {
        // 从后往前更新，避免重复使用本轮更新的值
        for (int j = m; j >= 0; j--) {
            // 遍历第i门课程的所有学习方案
            for (int k = 1; k <= min(j, m); k++) {
                if (j >= k) {
                    dp2[j] = max(dp2[j], dp2[j-k] + value[i][k]);
                }
            }
        }
    }
    
    return dp2[m];
}

// 简单的输出函数
void print_result(char* msg, int result) {
    // 简化输出，实际使用时可以替换为printf或其他输出方式
    // 这里只是示意
}

// 测试方法
int main() {
    // 构造测试用例
    n = 3;  // 3门课程
    m = 5;  // 5天时间
    
    // 第1门课程：学习1天收益2，学习2天收益3，学习3天收益4
    value[1][1] = 2;
    value[1][2] = 3;
    value[1][3] = 4;
    
    // 第2门课程：学习1天收益3，学习2天收益4，学习3天收益5
    value[2][1] = 3;
    value[2][2] = 4;
    value[2][3] = 5;
    
    // 第3门课程：学习1天收益4，学习2天收益5，学习3天收益6
    value[3][1] = 4;
    value[3][2] = 5;
    value[3][3] = 6;
    
    // 预期输出: 12 (第1门学3天得4分，第2门学2天得4分，第3门学2天得4分)
    int result1 = solve1();
    int result2 = solve2();
    
    return 0;
}