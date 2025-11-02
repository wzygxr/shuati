// 等差数列求和问题 - 分块算法实现 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/CF1921F
// 题目来源: https://codeforces.com/problemset/problem/1921/F
// 题目大意: 给定一个长度为n的数组arr，支持查询操作：
// 查询 s d k : arr[s]作为第1项、arr[s + 1*d]作为第2项、arr[s + 2*d]作为第3项...
//             每项的值 * 项的编号，一共k项都累加起来，打印累加和
// 约束条件: 
// 1 <= n <= 10^5
// 1 <= q <= 2 * 10^5

// 相关解答:
// C++版本: class175/Code03_SumOfProgression.cpp
// Java版本: class175/Code03_SumOfProgression1.java
// Python版本: class175/Code03_SumOfProgression.py

// 分块算法分析:
// - 时间复杂度：预处理O(n*sqrt(n)) + 查询O(q*sqrt(n))
// - 空间复杂度：O(n*sqrt(n))
// - 分块思想：将公差d分为d<=sqrt(n)和d>sqrt(n)两种情况处理
//   - 当d<=sqrt(n)时：预处理前缀和数组f和加权前缀和数组g，查询时间O(1)
//   - 当d>sqrt(n)时：直接暴力计算，由于d>sqrt(n)，每个查询最多处理O(sqrt(n))个元素
//     因此查询时间复杂度为O(sqrt(n))

#include <cstdio>
#include <cmath>
using namespace std;

const int MAXN = 100001;
const int MAXB = 401;
int t, n, q, blen;
int arr[MAXN];
long long f[MAXB][MAXN];
long long g[MAXB][MAXN];

/**
 * 查询操作
 * arr[s]作为第1项、arr[s + 1*d]作为第2项、arr[s + 2*d]作为第3项...
 * 每项的值 * 项的编号，一共k项都累加起来，返回累加和
 * 
 * 算法策略：
 * - 当d <= sqrt(n)时：使用预处理的g数组和f数组计算，时间复杂度O(1)
 * - 当d > sqrt(n)时：直接暴力计算，由于d>sqrt(n)，每个查询最多处理O(sqrt(n))个元素
 *   因此查询时间复杂度为O(sqrt(n))
 * 
 * 预处理数组说明：
 * - f[d][i]: 从位置i开始，公差为d的等差数列所有项的普通前缀和
 * - g[d][i]: 从位置i开始，公差为d的等差数列所有项的加权前缀和（每项乘以项的编号）
 * 
 * @param s 起始位置（从1开始索引）
 * @param d 公差
 * @param k 项数
 * @return 加权和
 */
long long query(int s, int d, int k) {
    long long ans = 0;
    
    // 当公差d较小时(d <= sqrt(n))，使用预处理的结果，O(1)时间
    if (d <= blen) {
        // g[d][s] 是从位置s开始的加权前缀和
        ans = g[d][s];
        
        // 如果s + d * k没有超出数组范围，则需要减去后面的部分
        // 这里使用了差分数组的思想，只计算前k项的和
        if (s + d * k <= n) {
            // 减去从s + d * k开始的部分
            // 注意需要加上f[d][s + d*k] * k，这是因为后续项的权重需要调整
            ans -= g[d][s + d * k] + f[d][s + d * k] * k;
        }
    } else {
        // 当公差d较大时(d > sqrt(n))，直接暴力计算
        // 由于d>sqrt(n)，k项最多只会跨越O(sqrt(n))个位置
        // 因此时间复杂度为O(sqrt(n))
        for (int i = 1; i <= k; i++) {
            // 第i项的位置是s + (i-1)*d，值乘以项的编号i
            ans += 1LL * arr[s + (i - 1) * d] * i;
        }
    }
    return ans;
}

/**
 * 预处理函数
 * 预处理f和g数组，用于快速回答公差d较小的查询
 * 
 * 预处理策略：
 * - 计算块大小blen = sqrt(n)
 * - 对于每个公差d <= blen，预处理两个数组：
 *   1. f[d][i]: 从位置i开始，公差为d的等差数列所有项的普通前缀和
 *   2. g[d][i]: 从位置i开始，公差为d的等差数列所有项的加权前缀和（每项乘以项的编号）
 * - 预处理顺序是从后往前（从n到1），确保计算f[d][i]时f[d][i+d]已经计算完成
 * 
 * 数学关系：
 * - f[d][i] = arr[i] + f[d][i+d] (如果i+d <= n)
 * - g[d][i] = arr[i] + (arr[i+d] + arr[i+2d] + ...) + g[d][i+d]
 *            = arr[i] + f[d][i+d] + g[d][i+d]
 *            = f[d][i] + g[d][i+d] (因为f[d][i] = arr[i] + f[d][i+d])
 * 
 * 时间复杂度：O(n*sqrt(n))
 * 空间复杂度：O(n*sqrt(n))
 */
void prepare() {
    // 计算块大小，通常选择sqrt(n)
    blen = (int)sqrt((double)n);
    
    // 预处理f数组
    // 对于每个公差d <= sqrt(n)
    for (int d = 1; d <= blen; d++) {
        // 从后往前计算前缀和
        // 这种方式确保在计算f[d][i]时，f[d][i+d]已经计算完成
        for (int i = n; i >= 1; i--) {
            // f[d][i] = arr[i] + f[d][i+d] (如果i+d <= n)
            // 如果i+d超出数组范围，则f[d][i] = arr[i]
            f[d][i] = arr[i] + (i + d > n ? 0 : f[d][i + d]);
        }
    }
    
    // 预处理g数组
    // 对于每个公差d <= sqrt(n)
    for (int d = 1; d <= blen; d++) {
        // 从后往前计算加权前缀和
        // 同样，确保在计算g[d][i]时，g[d][i+d]和f[d][i+d]已经计算完成
        for (int i = n; i >= 1; i--) {
            // g[d][i] = f[d][i] + g[d][i+d] (如果i+d <= n)
            // 这里利用了f和g之间的数学关系
            // 如果i+d超出数组范围，则g[d][i] = f[d][i] = arr[i]
            g[d][i] = f[d][i] + (i + d > n ? 0 : g[d][i + d]);
        }
    }
}

/**
 * 主函数
 * 处理多组测试用例，每组包括输入数组、预处理、处理查询
 * 
 * 流程：
 * 1. 读取测试用例数量t
 * 2. 对于每个测试用例：
 *    a. 读取数组长度n和查询次数q
 *    b. 读取长度为n的数组arr（从索引1开始存储）
 *    c. 调用prepare()函数进行预处理
 *    d. 处理q次查询，每次读取参数s、d、k并输出结果
 * 
 * 注意事项：
 * - 使用scanf和printf进行输入输出以提高效率，适用于大数据量的情况
 * - 数组索引从1开始，这与题目描述一致，方便处理
 */
int main() {
    // 读取测试用例数t
    scanf("%d", &t);
    
    // 处理每个测试用例
    for (int c = 1; c <= t; c++) {
        // 读取数组长度n和查询次数q
        scanf("%d%d", &n, &q);
        
        // 读取初始数组（索引从1开始）
        for (int i = 1; i <= n; i++) {
            scanf("%d", &arr[i]);
        }
        
        // 进行预处理，构建f和g数组
        // 预处理时间复杂度为O(n*sqrt(n))
        prepare();
        
        // 处理q次查询
        for (int i = 1, s, d, k; i <= q; i++) {
            // 读取查询参数：起始位置s，公差d，项数k
            scanf("%d%d%d", &s, &d, &k);
            // 调用查询函数并输出结果
            printf("%lld\n", query(s, d, k));
        }
    }
    return 0;
}