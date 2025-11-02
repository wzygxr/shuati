// 最大子阵列 - 分块算法实现 (C++版本)
// 题目来源: 计蒜客
// 题目链接: https://www.jisuanke.com/course/705/27296
// 题目大意: 在数组中找出和最大的连续子数组（至少包含一个元素）
// 约束条件: 数组长度n不超过1000，元素为整数

#include <cstdio>
#include <algorithm>
#include <cmath>
using namespace std;

typedef long long ll;
const int MAXN = 1005;
const ll INF = 1e18;

int n, blen; // blen为块的大小
ll arr[MAXN]; // 原始数组

// 分块预处理的结构
ll pre_sum[MAXN]; // 前缀和数组
ll block_sum[MAXN]; // 每个块的总和
ll l_max[MAXN][MAXN]; // l_max[i][j]: 从块i的第j个元素开始，向右延伸的最大子数组和
ll r_max[MAXN][MAXN]; // r_max[i][j]: 到块i的第j个元素结束，向左延伸的最大子数组和
ll max_sub[MAXN]; // 每个块内部的最大子数组和
ll total_max; // 整个数组的最大子数组和

// 初始化分块信息
void init_blocks() {
    blen = sqrt(n);
    if (blen == 0) blen = 1;
    
    int block_count = (n + blen - 1) / blen;
    
    // 计算前缀和
    pre_sum[0] = 0;
    for (int i = 1; i <= n; i++) {
        pre_sum[i] = pre_sum[i - 1] + arr[i];
    }
    
    // 预处理每个块的信息
    for (int b = 0; b < block_count; b++) {
        int start = b * blen + 1;
        int end = min((b + 1) * blen, n);
        
        // 计算块的总和
        block_sum[b] = pre_sum[end] - pre_sum[start - 1];
        
        // 计算l_max：从每个位置开始向右延伸的最大子数组和
        ll current_max = -INF;
        ll current_sum = 0;
        for (int i = end; i >= start; i--) {
            current_sum += arr[i];
            current_max = max(current_max, current_sum);
            l_max[b][i - start] = current_max;
        }
        
        // 计算r_max：到每个位置结束向左延伸的最大子数组和
        current_max = -INF;
        current_sum = 0;
        for (int i = start; i <= end; i++) {
            current_sum += arr[i];
            current_max = max(current_max, current_sum);
            r_max[b][i - start] = current_max;
        }
        
        // 计算块内的最大子数组和（Kadane算法）
        ll kadane_max = -INF;
        ll kadane_sum = 0;
        for (int i = start; i <= end; i++) {
            kadane_sum = max(arr[i], kadane_sum + arr[i]);
            kadane_max = max(kadane_max, kadane_sum);
        }
        max_sub[b] = kadane_max;
    }
    
    // 计算整个数组的最大子数组和
    total_max = -INF;
    for (int b = 0; b < block_count; b++) {
        total_max = max(total_max, max_sub[b]);
    }
    
    // 检查跨越块的情况
    for (int b = 0; b < block_count - 1; b++) {
        int end1 = min((b + 1) * blen, n);
        int start2 = (b + 1) * blen + 1;
        
        // 从块b的末尾向左延伸的最大值
        ll right_max = r_max[b][blen - 1];
        
        // 块b+1的总和累加
        ll current_sum = right_max;
        total_max = max(total_max, current_sum);
        
        for (int next_b = b + 1; next_b < block_count; next_b++) {
            current_sum += block_sum[next_b];
            total_max = max(total_max, current_sum);
            total_max = max(total_max, current_sum - block_sum[next_b] + r_max[next_b][0]);
        }
    }
}

// 使用分块预处理的方法求最大子数组和
ll max_subarray() {
    init_blocks();
    return total_max;
}

// 朴素的Kadane算法实现（用于验证）
ll kadane() {
    ll max_so_far = -INF;
    ll max_ending_here = 0;
    
    for (int i = 1; i <= n; i++) {
        max_ending_here = max(arr[i], max_ending_here + arr[i]);
        max_so_far = max(max_so_far, max_ending_here);
    }
    
    return max_so_far;
}

int main() {
    scanf("%d", &n);
    for (int i = 1; i <= n; i++) {
        scanf("%lld", &arr[i]);
    }
    
    ll result = max_subarray();
    printf("%lld\n", result);
    
    return 0;
}

/*
时间复杂度分析：
- 预处理时间：O(n)
- 分块初始化：O(n)
- 块内预处理：O(n)
- 跨越块处理：O(n) （最坏情况下）
- 总体时间复杂度：O(n)

空间复杂度分析：
- 数组arr：O(n)
- 前缀和数组pre_sum：O(n)
- 块信息数组：O(n)
- 总体空间复杂度：O(n)

算法说明：
这个问题虽然通常使用Kadane算法以O(n)时间解决，但这里展示了如何使用分块思想来处理。
分块方法特别适合需要支持动态更新和查询的场景，而不仅仅是静态数组。

优化说明：
1. 预处理每个块的信息，包括：
   - 块的总和
   - 从每个位置开始向右延伸的最大子数组和
   - 到每个位置结束向左延伸的最大子数组和
   - 块内的最大子数组和
2. 然后考虑跨越多个块的情况

工程化考虑：
1. 使用long long类型避免整数溢出
2. 初始化INF为足够大的值
3. 注意边界条件的处理
4. 提供了Kadane算法作为验证方法
*/