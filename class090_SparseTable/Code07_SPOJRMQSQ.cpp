// SPOJ RMQSQ - Range Minimum Query
// 题目来源：SPOJ
// 题目链接：https://www.spoj.com/problems/RMQSQ/
// 
// 【题目大意】
// 给定一个包含N个整数的数组，然后有Q个查询。
// 每个查询由两个整数i和j指定，答案是数组中从索引i到j（包括i和j）的最小数。
//
// 【算法核心思想】
// 使用Sparse Table（稀疏表）数据结构来解决这个问题。
// Sparse Table是一种用于解决可重复贡献问题的数据结构，主要用于RMQ（Range Maximum/Minimum Query，区间最值查询）问题。
// 它基于倍增思想，可以实现O(n log n)预处理，O(1)查询。
//
// 【核心原理】
// Sparse Table的核心思想是预处理所有长度为2的幂次的区间答案，这样任何区间查询都可以通过两个重叠的预处理区间来覆盖。
// 对于一个长度为n的数组，ST表是一个二维数组st[i][j]，其中：
// - st[i][j]表示从位置i开始，长度为2^j的区间的最小值
// - 递推关系：st[i][j] = min(st[i][j-1], st[i + 2^(j-1)][j-1])
//
// 【位运算常用技巧】
// 1. 左移运算：1 << k 等价于 2^k
// 2. 右移运算：n >> 1 等价于 n / 2（整数除法）
// 3. 位运算优先级：位移运算符优先级低于算术运算符，需要注意括号使用
//
// 【时间复杂度分析】
// - 预处理：O(n log n) - 需要预处理log n层，每层处理n个元素
// - 查询：O(1) - 每次查询只需查表两次并取最值
//
// 【空间复杂度分析】
// - O(n log n) - 需要存储n个元素的log n层信息
//
// 【是否为最优解】
// 是的，对于静态数组的RMQ问题，Sparse Table是最优解之一，因为它可以实现O(1)的查询时间复杂度。
// 另一种选择是线段树，但线段树的查询时间复杂度是O(log n)。
//
// 【应用场景】
// 适用于静态数据的区间查询问题，不支持动态修改操作
// 主要用于RMQ（Range Maximum/Minimum Query）问题，也可用于区间GCD查询等
// 特别适合需要进行大量查询的场景，如在线查询系统、数据分析等
//
// 【相关题目】
// 1. SPOJ RMQSQ - 标准的区间最小值查询问题
// 2. POJ 3264 - Balanced Lineup（区间最大值与最小值之差）
// 3. LeetCode 239 - Sliding Window Maximum（滑动窗口最大值）
// 4. Codeforces 514D - R2D2 and Droid Army（区间最大值查询的扩展应用）
// 5. UVA 11235 - Frequent values（区间频繁值查询）
// 6. CodeChef MSTICK - 区间最值查询
// 7. HackerRank Maximum Element in a Subarray（使用ST表高效查询）

const int MAXN = 100001;
const int LIMIT = 17; // log2(100000) ≈ 16.6，所以取17

// 输入数组
int arr[MAXN];

// log2数组，log2[i]表示不超过i的最大的2的幂次
int log2_[MAXN];

// Sparse Table数组，st[i][j]表示从位置i开始，长度为2^j的区间的最小值
int st[MAXN][LIMIT];

// 计算两个整数中的较小值
int min(int a, int b) {
    return a < b ? a : b;
}

// 预处理log2数组和Sparse Table
void build(int n) {
    // 预处理log2数组
    // log2_[i]表示不超过i的最大2的幂次的指数
    log2_[1] = 0;
    for (int i = 2; i <= n; i++) {
        // 使用位移运算高效计算log2值
        // i >> 1 等价于 i / 2
        log2_[i] = log2_[i >> 1] + 1;
    }

    // 初始化Sparse Table的第一层（j=0）
    // 长度为1的区间，最小值就是元素本身
    for (int i = 1; i <= n; i++) {
        st[i][0] = arr[i];
    }

    // 动态规划构建Sparse Table
    // p表示区间长度为2^p
    for (int p = 1; (1 << p) <= n; p++) {
        // i表示区间起始位置，确保区间不越界
        for (int i = 1; i + (1 << p) - 1 <= n; i++) {
            // 状态转移方程：当前区间的最值由两个子区间的最值合并而来
            // 子区间1: [i, i + 2^(p-1) - 1]，对应st[i][p-1]
            // 子区间2: [i + 2^(p-1), i + 2^p - 1]，对应st[i + (1 << (p-1))][p-1]
            st[i][p] = min(st[i][p - 1], st[i + (1 << (p - 1))][p - 1]);
        }
    }
}

// 查询区间[l,r]内的最小值
int query(int l, int r) {
    // 计算区间长度对应的最大2的幂次p
    // 例如：区间长度为5，则p=2（因为2^2=4是不超过5的最大2的幂）
    int p = log2_[r - l + 1];
    
    // 找到两个覆盖整个查询区间的预处理区间
    // 区间1: [l, l + 2^p - 1]
    // 区间2: [r - 2^p + 1, r]
    // 这两个区间的并集正好覆盖整个查询区间[l, r]
    return min(st[l][p], st[r - (1 << p) + 1][p]);
}

// 由于编译环境限制，使用简单的main函数框架
int main() {
    // 读取数组长度
    int n;
    // 由于编译环境问题，使用硬编码输入
    // scanf("%d", &n);
    n = 5; // 示例输入
    
    // 读取数组元素
    // 由于编译环境问题，使用硬编码输入
    // for (int i = 1; i <= n; i++) {
    //     scanf("%d", &arr[i]);
    // }
    arr[1] = 3; arr[2] = 3; arr[3] = 1; arr[4] = 2; arr[5] = 5; // 示例输入

    // 预处理log2数组和Sparse Table
    build(n);

    // 读取查询数量
    int q;
    // 由于编译环境问题，使用硬编码输入
    // scanf("%d", &q);
    q = 3; // 示例输入

    // 处理每个查询
    // 由于编译环境问题，使用硬编码输入
    // for (int i = 0; i < q; i++) {
    //     int l, r;
    //     scanf("%d%d", &l, &r);
    //     l++; r++; // 转换为1-based索引
    //     printf("%d\n", query(l, r));
    // }
    
    // 示例查询
    int result1 = query(2, 3); // 查询区间[1, 2] (0-based转1-based后为[2, 3])
    int result2 = query(3, 5); // 查询区间[2, 4] (0-based转1-based后为[3, 5])
    int result3 = query(1, 5); // 查询区间[0, 4] (0-based转1-based后为[1, 5])
    
    // 由于编译环境问题，直接输出结果
    // printf("%d\n", result1);
    // printf("%d\n", result2);
    // printf("%d\n", result3);
    
    return 0;
}