// LOJ 分块一 - 分块算法实现 (C++版本)
// 题目来源: https://loj.ac/problem/6277
// 题目大意: 给定一个长度为n的数组，支持两种操作：
// 1. 区间[l, r]每个数加k
// 2. 查询位置p的值
// 约束条件: 1 <= n, m <= 1e5

#include <cstdio>
#include <cmath>
using namespace std;

typedef long long ll;
const int MAXN = 100005;

int n, m, blen; // blen为块的大小
ll arr[MAXN]; // 原始数组
ll tag[MAXN]; // 块的懒标记

// 获取元素p的值（考虑懒标记）
ll get(int p) {
    int block = (p - 1) / blen + 1; // 计算p所在的块号
    return arr[p] + tag[block];
}

// 区间更新：将[l, r]区间内的每个数加上k
void update_range(int l, int r, ll k) {
    int L = (l - 1) / blen + 1; // l所在的块号
    int R = (r - 1) / blen + 1; // r所在的块号
    
    // 如果l和r在同一个块内，直接暴力更新
    if (L == R) {
        for (int i = l; i <= r; i++) {
            arr[i] += k;
        }
        return;
    }
    
    // 暴力更新左边不完整的块
    for (int i = l; i <= L * blen; i++) {
        arr[i] += k;
    }
    
    // 对中间完整的块打标记
    for (int i = L + 1; i <= R - 1; i++) {
        tag[i] += k;
    }
    
    // 暴力更新右边不完整的块
    for (int i = (R - 1) * blen + 1; i <= r; i++) {
        arr[i] += k;
    }
}

// 单点查询：查询位置p的值
ll query_point(int p) {
    return get(p);
}

int main() {
    scanf("%d", &n);
    blen = sqrt(n);
    if (blen == 0) blen = 1; // 避免除零错误
    
    // 读取数组元素
    for (int i = 1; i <= n; i++) {
        scanf("%lld", &arr[i]);
    }
    
    // 处理m次操作
    scanf("%d", &m);
    while (m--) {
        int op, l, r, p;
        ll k;
        scanf("%d", &op);
        if (op == 1) {
            // 区间更新操作
            scanf("%d%d%lld", &l, &r, &k);
            update_range(l, r, k);
        } else {
            // 单点查询操作
            scanf("%d", &p);
            printf("%lld\n", query_point(p));
        }
    }
    
    return 0;
}

/*
时间复杂度分析：
- 预处理：O(n)
- 区间更新操作：O(√n) 每个完整块O(1)，不完整块O(√n)
- 单点查询操作：O(1) 直接计算
- 总体时间复杂度：O(m√n)，其中m为操作次数

空间复杂度分析：
- 数组arr：O(n)
- 数组tag：O(√n)
- 总体空间复杂度：O(n)

优化说明：
- 这是分块算法的最基本应用，通过将数组分成√n大小的块
- 对于完整的块，使用懒标记记录整体的增量，避免逐个元素更新
- 对于不完整的块，直接暴力更新每个元素
- 这种实现方式在处理大量区间更新和单点查询时效率较高

工程化考虑：
1. 异常处理：对于输入的范围检查可以添加更多的边界条件判断
2. 数据类型：使用long long避免整数溢出
3. 性能优化：块的大小可以根据实际数据情况进行调整，不一定是严格的sqrt(n)
*/