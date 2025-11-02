// 区间最小值查询 - 分块算法实现 (C++版本)
// 题目来源: AizuOJ 2442
// 题目链接: https://onlinejudge.u-aizu.ac.jp/problems/2442
// 题目大意: 实现一个数据结构，支持单点更新和区间最小值查询
// 约束条件: 数组长度n ≤ 10^5，操作次数q ≤ 10^5

#include <cstdio>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

typedef long long ll;
const int MAXN = 100005;
const ll INF = 1e18;

int n, q, blen; // blen为块的大小
ll arr[MAXN]; // 原始数组
ll block_min[MAXN]; // 每个块的最小值

// 初始化分块结构
void init() {
    blen = sqrt(n);
    if (blen == 0) blen = 1;
    
    int block_count = (n + blen - 1) / blen;
    
    // 初始化每个块的最小值
    for (int i = 0; i < block_count; i++) {
        ll min_val = INF;
        for (int j = 0; j < blen; j++) {
            int idx = i * blen + j;
            if (idx >= n) break;
            if (arr[idx] < min_val) {
                min_val = arr[idx];
            }
        }
        block_min[i] = min_val;
    }
}

// 单点更新操作
void update(int pos, ll val) {
    int block_idx = pos / blen;
    arr[pos] = val;
    
    // 更新对应块的最小值
    ll min_val = INF;
    int start = block_idx * blen;
    int end = min((block_idx + 1) * blen, n);
    
    for (int i = start; i < end; i++) {
        if (arr[i] < min_val) {
            min_val = arr[i];
        }
    }
    block_min[block_idx] = min_val;
}

// 区间最小值查询
ll query_min(int l, int r) {
    ll min_val = INF;
    
    // 处理左边不完整的块
    int left_block = l / blen;
    int right_block = r / blen;
    
    if (left_block == right_block) {
        // 所有元素都在同一个块内，直接暴力查询
        for (int i = l; i <= r; i++) {
            if (arr[i] < min_val) {
                min_val = arr[i];
            }
        }
        return min_val;
    }
    
    // 处理左边不完整的块
    for (int i = l; i < (left_block + 1) * blen; i++) {
        if (arr[i] < min_val) {
            min_val = arr[i];
        }
    }
    
    // 处理中间完整的块
    for (int i = left_block + 1; i < right_block; i++) {
        if (block_min[i] < min_val) {
            min_val = block_min[i];
        }
    }
    
    // 处理右边不完整的块
    for (int i = right_block * blen; i <= r; i++) {
        if (arr[i] < min_val) {
            min_val = arr[i];
        }
    }
    
    return min_val;
}

int main() {
    scanf("%d %d", &n, &q);
    
    for (int i = 0; i < n; i++) {
        scanf("%lld", &arr[i]);
    }
    
    init();
    
    for (int i = 0; i < q; i++) {
        int type;
        scanf("%d", &type);
        
        if (type == 0) { // 更新操作
            int pos;
            ll val;
            scanf("%d %lld", &pos, &val);
            update(pos, val);
        } else { // 查询操作
            int l, r;
            scanf("%d %d", &l, &r);
            printf("%lld\n", query_min(l, r));
        }
    }
    
    return 0;
}

/*
时间复杂度分析：
- 初始化：O(n)
- 更新操作：O(√n)
- 查询操作：O(√n)
- 对于q次操作，总体时间复杂度：O(n + q√n)

空间复杂度分析：
- 数组arr：O(n)
- 块最小值数组block_min：O(√n)
- 总体空间复杂度：O(n + √n) = O(n)

算法说明：
这是一个经典的区间查询问题，可以使用线段树、稀疏表或分块算法解决。
使用分块算法可以在O(√n)的时间复杂度内处理更新和查询操作，
相比于线段树的O(log n)和稀疏表的不支持动态更新，分块算法提供了一个平衡的解决方案。

优化说明：
1. 块的大小选择为√n，这是分块算法中常见的优化，使得更新和查询操作的时间复杂度均为O(√n)
2. 预处理每个块的最小值，加速区间查询
3. 对于跨越多个块的查询，分别处理不完整的块和完整的块

工程化考虑：
1. 使用long long类型避免整数溢出
2. 初始化INF为足够大的值
3. 注意处理边界条件，如最后一个块可能不完整的情况
4. 输入输出使用scanf和printf以提高效率

与线段树的对比：
- 分块算法实现更简单
- 常数因子更小
- 对于某些特定问题，分块算法可能更高效
- 线段树的理论时间复杂度更低（O(log n) vs O(√n)）

当n和q的规模在1e5级别时，√n大约为300，所以q√n大约为3e7，这在现代计算机上是可以接受的。
*/