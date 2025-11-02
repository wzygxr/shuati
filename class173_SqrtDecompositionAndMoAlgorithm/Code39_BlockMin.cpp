// 块内最小值查询与更新 - 分块算法实现 (C++版本)
// 题目来源: LeetCode 307. Range Minimum Query (修改版)
// 题目链接: https://leetcode.com/problems/range-sum-query-mutable/
// 题目大意: 维护一个数组，支持单点更新和区间最小值查询
// 约束条件: 数组长度n ≤ 1e5，操作次数m ≤ 1e5

#include <cstdio>
#include <vector>
#include <algorithm>
#include <cmath>
#include <climits>
using namespace std;

const int MAXN = 100005;

int n, m, blen; // n是数组长度，m是操作次数，blen是块的大小
int arr[MAXN]; // 原始数组
int block_min[MAXN]; // 每个块的最小值
int block[MAXN]; // 每个元素所属的块

// 初始化分块结构
void init() {
    blen = sqrt(n);
    if (blen == 0) blen = 1;
    
    // 为每个元素分配块
    for (int i = 0; i < n; i++) {
        block[i] = i / blen;
    }
    
    // 计算每个块的最小值
    int block_count = (n + blen - 1) / blen;
    for (int i = 0; i < block_count; i++) {
        int start = i * blen;
        int end = min((i + 1) * blen, n);
        int min_val = INT_MAX;
        for (int j = start; j < end; j++) {
            if (arr[j] < min_val) {
                min_val = arr[j];
            }
        }
        block_min[i] = min_val;
    }
}

// 单点更新
void update_point(int pos, int val) {
    int old_val = arr[pos];
    arr[pos] = val;
    
    // 更新对应块的最小值
    int block_id = block[pos];
    int start = block_id * blen;
    int end = min((block_id + 1) * blen, n);
    
    // 重新计算该块的最小值
    int min_val = INT_MAX;
    for (int i = start; i < end; i++) {
        if (arr[i] < min_val) {
            min_val = arr[i];
        }
    }
    block_min[block_id] = min_val;
}

// 区间最小值查询
int query_min(int l, int r) {
    int left_block = block[l];
    int right_block = block[r];
    int min_val = INT_MAX;
    
    if (left_block == right_block) {
        // 所有元素都在同一个块内，直接暴力查询
        for (int i = l; i <= r; i++) {
            if (arr[i] < min_val) {
                min_val = arr[i];
            }
        }
    } else {
        // 处理左边不完整的块
        for (int i = l; i < (left_block + 1) * blen; i++) {
            if (arr[i] < min_val) {
                min_val = arr[i];
            }
        }
        
        // 处理中间完整的块，使用块的最小值
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
    }
    
    return min_val;
}

// 优化版本的单点更新（只在需要时重新计算块最小值）
void update_point_optimized(int pos, int val) {
    int old_val = arr[pos];
    arr[pos] = val;
    
    // 只有当新值小于块最小值或者旧值等于块最小值时，才需要重新计算
    int block_id = block[pos];
    if (val < block_min[block_id] || old_val == block_min[block_id]) {
        int start = block_id * blen;
        int end = min((block_id + 1) * blen, n);
        int min_val = INT_MAX;
        for (int i = start; i < end; i++) {
            if (arr[i] < min_val) {
                min_val = arr[i];
            }
        }
        block_min[block_id] = min_val;
    }
}

int main() {
    scanf("%d", &n);
    
    // 读取初始数组（0-based索引）
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 初始化分块结构
    init();
    
    scanf("%d", &m);
    
    // 处理每个操作
    for (int i = 0; i < m; i++) {
        int op, a, b;
        scanf("%d", &op);
        
        if (op == 0) {
            // 单点更新操作
            scanf("%d %d", &a, &b);
            a--; // 转换为0-based索引
            update_point_optimized(a, b);
        } else if (op == 1) {
            // 区间查询操作
            scanf("%d %d", &a, &b);
            a--; b--; // 转换为0-based索引
            int result = query_min(a, b);
            printf("%d\n", result);
        }
    }
    
    return 0;
}

/*
时间复杂度分析：
- 初始化：O(n)
- 单点更新（优化版本）：
  - 最好情况：O(1)（当新值不影响块最小值时）
  - 最坏情况：O(√n)（当需要重新计算块最小值时）
  - 平均时间复杂度：O(√n)
- 区间查询：O(√n)
- 对于m次操作，总体时间复杂度：O(m√n)

空间复杂度分析：
- 数组arr：O(n)
- 块最小值数组block_min：O(√n)
- 块分配数组block：O(n)
- 总体空间复杂度：O(n + √n) = O(n)

算法说明：
块内最小值查询与更新是分块算法的一个典型应用，主要用于处理单点更新和区间最小值查询问题。

算法步骤：
1. 将数组分成大小为√n的块
2. 预处理每个块的最小值，存储在block_min数组中
3. 对于单点更新操作：
   - 更新原始数组中的值
   - 检查是否需要重新计算对应块的最小值
4. 对于区间最小值查询操作：
   - 对于不完整的块，直接遍历块中的元素，找到最小值
   - 对于完整的块，直接使用预处理好的块最小值
   - 综合所有部分的结果，得到最终的最小值

优化说明：
1. 在单点更新时，只有当新值小于块最小值或者旧值等于块最小值时，才需要重新计算块最小值
2. 块的大小选择为√n，平衡了查询和更新的时间复杂度
3. 对于区间查询，利用预处理的块最小值，避免重复计算

与其他方法的对比：
- 暴力法：每次更新O(1)，每次查询O(n)，总时间复杂度O(mn)
- 线段树：每次更新和查询都是O(log n)，但实现复杂
- ST表：查询O(1)，但不支持更新
- 块内最小值：实现简单，时间复杂度适中，支持更新和查询

工程化考虑：
1. 注意数组的索引方式（0-based或1-based）
2. 对于大规模数据，可以考虑使用更高效的输入方法
3. 可以根据具体数据调整块的大小，以获得最佳性能
4. 在C++中，可以使用vector代替数组，提高代码的可读性和安全性
*/