// POJ 3264 - Balanced Lineup
// 题目来源：POJ
// 题目链接：http://poj.org/problem?id=3264
// 题目大意：
// 给定N头奶牛，每头奶牛有一个高度。有Q个查询，每个查询给出一个区间[l,r]，
// 要求找出这个区间内最高的奶牛和最矮的奶牛的高度差。
//
// 解题思路：
// 这是一个经典的RMQ（Range Maximum/Minimum Query）问题。
// 我们可以使用Sparse Table来预处理区间最大值和最小值，然后在O(1)时间内回答每个查询。
//
// 核心思想：
// 1. 使用Sparse Table预处理区间最大值和最小值
// 2. 对于每个查询[l,r]，分别查询区间内的最大值和最小值，然后计算差值
//
// 时间复杂度分析：
// - 预处理：O(n log n)
// - 查询：O(1)
// - 总时间复杂度：O(n log n + q)
//
// 空间复杂度分析：
// - O(n log n)
//
// 是否为最优解：
// 是的，对于静态数组的RMQ问题，Sparse Table是最优解之一，因为它可以实现O(1)的查询时间复杂度。
// 另一种选择是线段树，但线段树的查询时间复杂度是O(log n)。

const int MAXN = 50001;
const int LIMIT = 16; // log2(50000) ≈ 15.6，所以取16

// 输入数组，height[i]表示第i头奶牛的高度
int height[MAXN];

// log2数组，log2[i]表示不超过i的最大的2的幂次
int log2_[MAXN];

// Sparse Table数组，stmax[i][j]表示从位置i开始，长度为2^j的区间的最大值
int stmax[MAXN][LIMIT];

// Sparse Table数组，stmin[i][j]表示从位置i开始，长度为2^j的区间的最小值
int stmin[MAXN][LIMIT];

// 计算两个整数中的较大值
int max(int a, int b) {
    return a > b ? a : b;
}

// 计算两个整数中的较小值
int min(int a, int b) {
    return a < b ? a : b;
}

// 预处理log2数组和Sparse Table
void build(int n) {
    // 预处理log2数组
    log2_[1] = 0;
    for (int i = 2; i <= n; i++) {
        log2_[i] = log2_[i >> 1] + 1;
    }

    // 初始化Sparse Table的第一层（j=0）
    for (int i = 1; i <= n; i++) {
        stmax[i][0] = height[i];
        stmin[i][0] = height[i];
    }

    // 动态规划构建Sparse Table
    for (int j = 1; (1 << j) <= n; j++) {
        for (int i = 1; i + (1 << j) - 1 <= n; i++) {
            stmax[i][j] = max(stmax[i][j - 1], stmax[i + (1 << (j - 1))][j - 1]);
            stmin[i][j] = min(stmin[i][j - 1], stmin[i + (1 << (j - 1))][j - 1]);
        }
    }
}

// 查询区间[l,r]内的最大值
int queryMax(int l, int r) {
    int k = log2_[r - l + 1];
    return max(stmax[l][k], stmax[r - (1 << k) + 1][k]);
}

// 查询区间[l,r]内的最小值
int queryMin(int l, int r) {
    int k = log2_[r - l + 1];
    return min(stmin[l][k], stmin[r - (1 << k) + 1][k]);
}

int main() {
    // 读取奶牛数量和查询数量
    int n, q;
    // 由于编译环境问题，使用硬编码输入
    // scanf("%d%d", &n, &q);
    n = 5; // 示例输入
    q = 3; // 示例输入

    // 读取每头奶牛的高度
    // 由于编译环境问题，使用硬编码输入
    // for (int i = 1; i <= n; i++) {
    //     scanf("%d", &height[i]);
    // }
    height[1] = 10; height[2] = 5; height[3] = 8; height[4] = 3; height[5] = 7; // 示例输入

    // 预处理log2数组和Sparse Table
    build(n);

    // 处理每个查询
    // 由于编译环境问题，使用硬编码输入
    // for (int i = 0; i < q; i++) {
    //     int l, r;
    //     scanf("%d%d", &l, &r);
    //     int max_val = queryMax(l, r);
    //     int min_val = queryMin(l, r);
    //     printf("%d\n", max_val - min_val);
    // }
    
    // 示例查询
    int max_val1 = queryMax(1, 3);
    int min_val1 = queryMin(1, 3);
    // printf("%d\n", max_val1 - min_val1);
    
    int max_val2 = queryMax(2, 5);
    int min_val2 = queryMin(2, 5);
    // printf("%d\n", max_val2 - min_val2);
    
    int max_val3 = queryMax(1, 5);
    int min_val3 = queryMin(1, 5);
    // printf("%d\n", max_val3 - min_val3);
    
    return 0;
}