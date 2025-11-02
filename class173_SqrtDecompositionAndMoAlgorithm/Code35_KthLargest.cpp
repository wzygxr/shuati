// 区间第k大 - 分块算法实现 (C++版本)
// 题目来源: POJ 2104
// 题目链接: http://poj.org/problem?id=2104
// 题目大意: 多次查询区间[l,r]内第k小的数字
// 约束条件: 数组长度n ≤ 1e5，查询次数m ≤ 1e4

#include <cstdio>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

const int MAXN = 100005;
const int BLOCK_SIZE = 317; // 约等于sqrt(1e5)

int n, m, blen; // blen为块的大小
int arr[MAXN]; // 原始数组
int sorted_arr[MAXN]; // 排序后的数组，用于二分查找
vector<int> blocks[BLOCK_SIZE]; // 每个块排序后的数组

// 初始化分块结构
void init() {
    blen = sqrt(n);
    if (blen == 0) blen = 1;
    
    // 复制数组并排序，用于二分查找
    for (int i = 0; i < n; i++) {
        sorted_arr[i] = arr[i];
    }
    sort(sorted_arr, sorted_arr + n);
    
    // 分块并对每个块进行排序
    int block_count = (n + blen - 1) / blen;
    for (int i = 0; i < block_count; i++) {
        int start = i * blen;
        int end = min((i + 1) * blen, n);
        
        blocks[i].clear();
        for (int j = start; j < end; j++) {
            blocks[i].push_back(arr[j]);
        }
        sort(blocks[i].begin(), blocks[i].end());
    }
}

// 查询区间[l,r]内小于等于x的元素个数
int query_count(int l, int r, int x) {
    int count = 0;
    int left_block = l / blen;
    int right_block = r / blen;
    
    if (left_block == right_block) {
        // 所有元素都在同一个块内
        for (int i = l; i <= r; i++) {
            if (arr[i] <= x) {
                count++;
            }
        }
    } else {
        // 处理左边不完整的块
        for (int i = l; i < (left_block + 1) * blen; i++) {
            if (arr[i] <= x) {
                count++;
            }
        }
        
        // 处理中间完整的块
        for (int i = left_block + 1; i < right_block; i++) {
            // 利用块的有序性进行二分查找
            count += upper_bound(blocks[i].begin(), blocks[i].end(), x) - blocks[i].begin();
        }
        
        // 处理右边不完整的块
        for (int i = right_block * blen; i <= r; i++) {
            if (arr[i] <= x) {
                count++;
            }
        }
    }
    
    return count;
}

// 二分查找第k小的元素
int find_kth_smallest(int l, int r, int k) {
    int left = 0, right = n - 1;
    while (left < right) {
        int mid = (left + right) / 2;
        int x = sorted_arr[mid];
        int cnt = query_count(l, r, x);
        
        if (cnt >= k) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return sorted_arr[left];
}

int main() {
    scanf("%d %d", &n, &m);
    
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }
    
    init();
    
    for (int i = 0; i < m; i++) {
        int l, r, k;
        scanf("%d %d %d", &l, &r, &k);
        // 注意POJ的输入可能是1-based索引
        l--; r--;
        int result = find_kth_smallest(l, r, k);
        printf("%d\n", result);
    }
    
    return 0;
}

/*
时间复杂度分析：
- 初始化：O(n log n)
- 单次查询：
  - query_count函数：O(√n + √n log √n) = O(√n log n)
  - find_kth_smallest函数：O(log n)次query_count调用
  - 总体单次查询时间复杂度：O(√n (log n)^2)
- 对于m次查询，总体时间复杂度：O(n log n + m√n (log n)^2)

空间复杂度分析：
- 数组arr：O(n)
- 数组sorted_arr：O(n)
- 块排序数组blocks：O(n)
- 总体空间复杂度：O(n)

算法说明：
这是一个经典的区间第k小查询问题，分块算法是一种有效的解决方案。
算法的核心思想是：
1. 将数组分成大小为√n的块，并对每个块进行排序
2. 使用二分查找确定可能的答案值
3. 对于每个候选值，统计区间内小于等于它的元素个数
4. 根据统计结果调整二分范围

优化说明：
1. 块的大小选择为√n，这是分块算法的最佳实践
2. 预处理整个数组的排序版本，用于二分查找候选值
3. 对每个块进行排序，利用二分查找快速统计块内元素
4. 对于不完整的块，直接暴力统计

与其他方法的对比：
- 线段树+归并树：时间复杂度O(n log n + m log^2 n)，但实现复杂
- 主席树（可持久化线段树）：时间复杂度O(n log n + m log n)，实现也较复杂
- 分块算法：实现相对简单，时间复杂度适中

工程化考虑：
1. 注意索引的处理，POJ的输入可能使用1-based索引
2. 块的大小可以调整为固定值（如317）以避免动态计算
3. 使用vector存储排序后的块，方便进行二分查找
4. 对于非常大的数据集，可以考虑使用更高效的输入方法
*/