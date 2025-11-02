// 数组查询问题 - 分块算法实现 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/CF797E
// 题目来源: https://codeforces.com/problemset/problem/797/E
// 题目大意: 给定一个长度为n的数组arr，支持查询操作：
// 查询 p k : 从位置p开始，每次跳跃arr[p] + k步，直到越界，返回跳跃次数
// 约束条件: 
// 1 <= n、q <= 10^5
// 1 <= arr[i] <= n
// 相关解答: 
// - C++版本: Code02_ArrayQueries.cpp
// - Java版本: Code02_ArrayQueries1.java, Code02_ArrayQueries2.java
// - Python版本: Code02_ArrayQueries.py
// 
// 分块算法分析：
// - 对于k较大的情况(k > sqrt(n))，由于每次跳跃的步长至少是sqrt(n)+1，所以最多跳跃sqrt(n)次
// - 对于k较小的情况(k <= sqrt(n))，可以预处理所有可能的情况，使查询时间为O(1)
// - 这种分块处理的策略使得总的时间复杂度为O(n*sqrt(n) + q*sqrt(n))，可以在1e5的数据规模下高效运行

#include <cstdio>
#include <cmath>
using namespace std;

const int MAXN = 100001;
const int MAXB = 401;
int n, q, blen;
int arr[MAXN];
int dp[MAXN][MAXB];

/**
 * 查询操作
 * 从位置p开始，每次跳跃arr[p] + k步，直到越界，返回跳跃次数
 * @param p 起始位置
 * @param k 跳跃增量
 * @return 跳跃次数
 * 
 * 算法策略:
 * - 对于k <= sqrt(n)的情况: O(1)时间复杂度，直接返回预处理好的dp[p][k]
 * - 对于k > sqrt(n)的情况: O(sqrt(n))时间复杂度，因为每次跳跃至少sqrt(n)+1步
 * 这种分块处理的策略使得查询操作的平均时间复杂度为O(sqrt(n))
 */
int query(int p, int k) {
    // 当k较小时(k <= blen)，直接使用预处理结果，O(1)时间
    if (k <= blen) {
        return dp[p][k];
    }
    
    // 当k较大时(k > blen)，直接模拟跳跃过程
    // 由于k > sqrt(n)，每次跳跃步长至少是arr[p] + k >= 1 + sqrt(n) + 1
    // 因此最多执行n/(1+sqrt(n)) < sqrt(n)次跳跃，总时间复杂度为O(sqrt(n))
    int ans = 0;
    while (p <= n) {
        ans++;  // 记录跳跃次数
        p += arr[p] + k;  // 计算下一个位置
    }
    return ans;
}

/**
 * 预处理函数
 * 对于所有k <= sqrt(n)的情况，预处理dp[p][k]的值
 * 使用动态规划从后往前计算，这样可以利用已计算的结果
 * 
 * 预处理策略:
 * - 计算块大小blen = sqrt(n)
 * - 从后往前遍历所有位置p (从n到1)
 * - 对每个位置p，计算所有k <= blen对应的dp[p][k]
 * - 使用动态规划的思路：dp[p][k] = 1 + (如果下一步越界则0，否则dp[p + arr[p] + k][k])
 * - 时间复杂度: O(n*sqrt(n))
 * 虽然预处理时间较高，但可以确保后续查询操作的高效性
 */
void prepare() {
    // 计算块大小，通常选择sqrt(n)
    blen = (int)sqrt((double)n);
    
    // 从后往前计算dp值
    // 这样可以确保在计算dp[p][k]时，dp[p + arr[p] + k][k]已经计算过了
    for (int p = n; p >= 1; p--) {
        // 对于每个k <= sqrt(n)的情况进行预处理
        for (int k = 1; k <= blen; k++) {
            // 计算从位置p跳一步后的新位置
            int next_pos = p + arr[p] + k;
            
            // 动态规划转移方程：
            // - 如果下一步越界(n+1或更大)，则dp[p][k] = 1（只跳转一次）
            // - 否则dp[p][k] = 1 + dp[next_pos][k]（一次跳转加上从next_pos开始的跳转次数）
            dp[p][k] = 1 + (next_pos > n ? 0 : dp[next_pos][k]);
        }
    }
}

/**
 * 主函数
 * 读取输入数据，初始化数组，预处理数据，处理所有查询
 * 
 * 主流程:
 * 1. 读取数组长度n
 * 2. 读取数组元素
 * 3. 调用prepare()函数进行预处理
 * 4. 读取查询次数q
 * 5. 处理每个查询，调用query()函数计算结果并输出
 */
int main() {
    // 读取数组长度n
    scanf("%d", &n);
    
    // 读取初始数组元素
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 进行预处理，计算所有p和k<=sqrt(n)的dp[p][k]值
    prepare();
    
    // 读取查询次数q
    scanf("%d", &q);
    
    // 处理q次查询
    for (int i = 1, p, k; i <= q; i++) {
        // 读取查询参数p(起始位置)和k(跳跃增量)
        scanf("%d%d", &p, &k);
        // 计算并输出跳跃次数
        printf("%d\n", query(p, k));
    }
    return 0;
}