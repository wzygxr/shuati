// 块颜色标记 - 分块算法实现 (C++版本)
// 题目来源: 洛谷 P3203
// 题目链接: https://www.luogu.com.cn/problem/P3203
// 题目大意: 维护一个序列，支持区间颜色修改和单点查询颜色
// 约束条件: 数组长度n ≤ 1e5，操作次数m ≤ 1e5

#include <cstdio>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

const int MAXN = 100005;
const int MAX_COLOR = 1000005;

int n, m, blen; // n是数组长度，m是操作次数，blen是块的大小
int arr[MAXN]; // 原始数组
int color_tag[MAXN]; // 块的颜色标记（-1表示没有标记）
int block[MAXN]; // 每个元素所属的块
bool has_tag[MAXN]; // 标记块是否有颜色标记

// 初始化分块结构
void init() {
    blen = sqrt(n);
    if (blen == 0) blen = 1;
    
    // 为每个元素分配块，并初始化颜色标记
    for (int i = 0; i < n; i++) {
        block[i] = i / blen;
    }
    
    // 初始化块颜色标记
    int block_count = (n + blen - 1) / blen;
    for (int i = 0; i < block_count; i++) {
        has_tag[i] = false;
        color_tag[i] = -1;
    }
}

// 将颜色标记下推到块中的所有元素
void push_down(int block_id) {
    if (!has_tag[block_id]) return;
    
    int start = block_id * blen;
    int end = min((block_id + 1) * blen, n);
    
    // 将标记应用到块中的每个元素
    for (int i = start; i < end; i++) {
        arr[i] = color_tag[block_id];
    }
    
    // 清除标记
    has_tag[block_id] = false;
    color_tag[block_id] = -1;
}

// 区间颜色修改
void update_range(int l, int r, int c) {
    int left_block = block[l];
    int right_block = block[r];
    
    if (left_block == right_block) {
        // 所有元素都在同一个块内
        // 先下推该块的标记
        push_down(left_block);
        
        // 直接修改每个元素
        for (int i = l; i <= r; i++) {
            arr[i] = c;
        }
    } else {
        // 处理左边不完整的块
        push_down(left_block);
        for (int i = l; i < (left_block + 1) * blen; i++) {
            arr[i] = c;
        }
        
        // 处理中间完整的块（使用块标记）
        for (int i = left_block + 1; i < right_block; i++) {
            has_tag[i] = true;
            color_tag[i] = c;
        }
        
        // 处理右边不完整的块
        push_down(right_block);
        for (int i = right_block * blen; i <= r; i++) {
            arr[i] = c;
        }
    }
}

// 单点查询颜色
int query_point(int pos) {
    int block_id = block[pos];
    
    if (has_tag[block_id]) {
        // 如果块有标记，直接返回标记的颜色
        return color_tag[block_id];
    } else {
        // 否则返回原始数组中的颜色
        return arr[pos];
    }
}

int main() {
    scanf("%d", &n);
    
    // 读取初始数组（假设是0-based索引）
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 初始化分块结构
    init();
    
    scanf("%d", &m);
    
    // 处理每个操作
    for (int i = 0; i < m; i++) {
        int op, a, b, c;
        scanf("%d", &op);
        
        if (op == 1) {
            // 区间修改操作
            scanf("%d %d %d", &a, &b, &c);
            // 注意题目中的输入可能是1-based索引
            a--; b--;
            update_range(a, b, c);
        } else if (op == 2) {
            // 单点查询操作
            scanf("%d", &a);
            a--; // 转换为0-based索引
            int result = query_point(a);
            printf("%d\n", result);
        }
    }
    
    return 0;
}

/*
时间复杂度分析：
- 初始化：O(n)
- 单点查询：O(1)
- 区间修改：
  - 对于完整的块：O(1)（只需要设置标记）
  - 对于不完整的块：O(√n)（需要下推标记并暴力修改）
  - 总体时间复杂度：O(√n)
- 对于m次操作，总体时间复杂度：O(m√n)

空间复杂度分析：
- 数组arr：O(n)
- 标记数组color_tag和has_tag：O(√n)
- 块分配数组block：O(n)
- 总体空间复杂度：O(n + √n) = O(n)

算法说明：
块颜色标记是分块算法的一个典型应用，主要用于处理区间颜色修改和单点查询问题。

算法步骤：
1. 将数组分成大小为√n的块
2. 对于区间颜色修改操作：
   - 对于不完整的块，先下推可能存在的标记，然后暴力修改每个元素
   - 对于完整的块，只需要设置块的颜色标记，不需要立即修改每个元素
3. 对于单点查询操作：
   - 如果该元素所在的块有颜色标记，直接返回标记的颜色
   - 否则返回原始数组中的颜色

优化说明：
1. 使用延迟标记技术，避免不必要的元素修改操作
2. 块的大小选择为√n，平衡了查询和修改的时间复杂度
3. 只有在需要访问块中的元素时才下推标记，减少操作次数

与其他方法的对比：
- 暴力法：每次修改O(n)，每次查询O(1)，总时间复杂度O(mn)
- 线段树：每次修改和查询都是O(log n)，但实现复杂
- 块颜色标记：实现简单，时间复杂度适中

工程化考虑：
1. 注意数组的索引方式（0-based或1-based）
2. 块的大小可以根据具体数据规模调整
3. 延迟标记的管理需要特别注意，避免错误
4. 对于大规模数据，可以考虑使用更高效的输入方法
*/