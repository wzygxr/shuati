// D-query问题 - Mo算法实现 (C++版本)
// 题目来源: https://www.spoj.com/problems/DQUERY/
// 题目大意: 给定一个长度为n的数组arr，有q次查询，每次查询[l,r]区间内不同数字的个数
// 约束条件: 1 <= n, q <= 2*10^5, 1 <= arr[i] <= 10^6
// 解法: Mo算法（离线分块）
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n + V), 其中V为值域大小(10^6)
// 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

#include <cstdio>
#include <algorithm>
#include <cmath>
using namespace std;

const int MAXN = 200001;
const int MAXV = 1000001;
int n, q, blen;
int arr[MAXN];
int ans[MAXN];
int count[MAXV]; // 计数数组
int curAns = 0; // 当前答案

// 查询结构
struct Query {
    int l, r, id;
    
    bool operator<(const Query& other) const {
        int blockA = (l - 1) / blen;
        int blockB = (other.l - 1) / blen;
        if (blockA != blockB) {
            return blockA < blockB;
        }
        return r < other.r;
    }
};

Query queries[MAXN];

/**
 * 添加元素到当前窗口
 * 时间复杂度: O(1)
 * 设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和不同数字的总数
 * 如果该元素之前在窗口中出现次数为0，说明是新数字，不同数字总数加1
 * @param pos 位置
 */
void add(int pos) {
    // 如果之前该数字出现次数为0，说明是新数字
    if (count[arr[pos]] == 0) {
        curAns++;
    }
    count[arr[pos]]++;
}

/**
 * 从当前窗口移除元素
 * 时间复杂度: O(1)
 * 设计思路: 当从窗口中移除一个元素时，需要更新该元素的计数和不同数字的总数
 * 如果移除后该元素在窗口中出现次数为0，说明少了一个不同数字，不同数字总数减1
 * @param pos 位置
 */
void remove(int pos) {
    count[arr[pos]]--;
    // 如果移除后该数字出现次数为0，说明少了一个不同数字
    if (count[arr[pos]] == 0) {
        curAns--;
    }
}

int main() {
    // 读取数组长度n
    scanf("%d", &n);
    
    // 读取初始数组
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }

    // 读取查询次数q
    scanf("%d", &q);
    
    // 读取所有查询
    for (int i = 1; i <= q; i++) {
        scanf("%d%d", &queries[i].l, &queries[i].r);
        queries[i].id = i;
    }

    // 使用Mo算法进行排序
    // 块大小选择: sqrt(n)，这是经过理论分析得出的最优块大小
    // 时间复杂度: O(q * log(q))，主要是排序的复杂度
    // 排序策略: 按照左端点所在的块编号排序，块内按右端点排序
    // 这样可以最小化指针移动的次数，从而优化整体时间复杂度
    blen = (int)sqrt((double)n);
    sort(queries + 1, queries + q + 1);

    // Mo算法处理
    int curL = 1, curR = 0;
    for (int i = 1; i <= q; i++) {
        int l = queries[i].l;
        int r = queries[i].r;
        int id = queries[i].id;

        // 扩展右边界
        while (curR < r) {
            curR++;
            add(curR);
        }

        // 收缩左边界
        while (curL > l) {
            curL--;
            add(curL);
        }

        // 收缩右边界
        while (curR > r) {
            remove(curR);
            curR--;
        }

        // 扩展左边界
        while (curL < l) {
            remove(curL);
            curL++;
        }

        // 记录当前查询的答案
        ans[id] = curAns;
    }

    // 输出所有查询的结果
    for (int i = 1; i <= q; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}

// 算法说明：
// 1. 使用Mo算法（离线分块）解决区间不同数字计数问题
// 2. 时间复杂度：O((n + q) * sqrt(n))
// 3. 空间复杂度：O(n + V)，其中V为值域大小
// 4. 核心思想：通过分块排序查询，减少元素添加/删除操作次数
//
// 详细分析：
// Mo算法是一种离线算法，通过巧妙地排序查询来减少重复计算
// 块大小选择sqrt(n)是经过理论分析的最优选择
// 排序策略是先按左端点所在块排序，再按右端点排序
// 这样可以保证相邻查询之间的指针移动次数最少