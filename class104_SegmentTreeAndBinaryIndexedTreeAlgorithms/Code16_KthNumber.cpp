// class131/Code16_KthNumber.cpp
// SPOJ MKTHNUM - K-th Number
// 题目链接: https://www.spoj.com/problems/MKTHNUM/

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 100005;
const int MAX_LOG = 20;

/**
 * 主席树节点定义
 * 主席树是一种可持久化线段树，支持历史版本查询
 */
struct Node {
    int left, right; // 左右子节点索引
    int count;       // 区间内元素个数
} tree[MAXN * MAX_LOG]; // 预分配空间

int root[MAXN];  // 各个版本的根节点
int a[MAXN];     // 原始数组
vector<int> sorted; // 离散化后的数组
int cnt;         // 动态开点计数器
int n, m;        // 数组大小和查询数量

/**
 * 离散化：获取数值对应的索引
 * @param val 要查找的数值
 * @return 离散化后的索引（从1开始）
 */
int getIndex(int val) {
    return lower_bound(sorted.begin(), sorted.end(), val) - sorted.begin() + 1; // 索引从1开始
}

/**
 * 构建初始线段树
 * @param l 当前节点区间左边界
 * @param r 当前节点区间右边界
 * @return 构建好的线段树根节点索引
 */
int build(int l, int r) {
    int node = ++cnt;
    tree[node].count = 0;
    
    if (l == r) {
        return node;
    }
    
    int mid = (l + r) >> 1;
    tree[node].left = build(l, mid);
    tree[node].right = build(mid + 1, r);
    
    return node;
}

/**
 * 更新线段树，创建新版本
 * @param pre 前一个版本的节点索引
 * @param l 当前节点区间左边界
 * @param r 当前节点区间右边界
 * @param pos 要更新的位置
 * @param val 更新的值（1表示插入，-1表示删除）
 * @return 更新后的新节点索引
 */
int update(int pre, int l, int r, int pos, int val) {
    int node = ++cnt;
    tree[node] = tree[pre]; // 复制前一个版本的节点信息
    tree[node].count += val; // 更新计数
    
    if (l == r) {
        return node;
    }
    
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        tree[node].left = update(tree[pre].left, l, mid, pos, val);
    } else {
        tree[node].right = update(tree[pre].right, mid + 1, r, pos, val);
    }
    
    return node;
}

/**
 * 查询区间第k小元素
 * 利用主席树的前缀和思想，通过两个版本的差值来得到区间内的信息
 * @param u 右边界版本的根节点
 * @param v 左边界-1版本的根节点
 * @param l 当前节点区间左边界
 * @param r 当前节点区间右边界
 * @param k 要查询的第k小
 * @return 第k小元素的离散化索引
 */
int query(int u, int v, int l, int r, int k) {
    if (l == r) {
        return l;
    }
    
    int mid = (l + r) >> 1;
    int leftCount = tree[tree[u].left].count - tree[tree[v].left].count;
    
    if (leftCount >= k) {
        return query(tree[u].left, tree[v].left, l, mid, k);
    } else {
        return query(tree[u].right, tree[v].right, mid + 1, r, k - leftCount);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> m;
    
    // 读取原始数组并准备离散化
    sorted.resize(n);
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
        sorted[i - 1] = a[i];
    }
    
    // 离散化处理：排序并去重
    sort(sorted.begin(), sorted.end());
    sorted.erase(unique(sorted.begin(), sorted.end()), sorted.end());
    
    // 构建主席树
    cnt = 0;
    root[0] = build(1, sorted.size());
    
    // 创建前缀版本
    for (int i = 1; i <= n; i++) {
        int idx = getIndex(a[i]);
        root[i] = update(root[i - 1], 1, sorted.size(), idx, 1);
    }
    
    // 处理查询
    while (m--) {
        int l, r, k;
        cin >> l >> r >> k;
        int idx = query(root[r], root[l - 1], 1, sorted.size(), k);
        cout << sorted[idx - 1] << "\n"; // 注意离散化索引的转换
    }
    
    return 0;
}

/*
复杂度分析：
- 时间复杂度：
  - 构建：O(n log n)
  - 查询：O(log n)
  - 空间复杂度：O(n log n)

主席树（可持久化线段树）是一种支持历史版本查询的数据结构，每个版本都是基于前一个版本进行增量修改。
在区间第k小问题中，我们利用前缀和的思想，通过两个版本的差值来得到区间内的信息。

算法详解：
1. 离散化：将原始数值映射到连续的整数范围，减少空间消耗
2. 构建主席树：为每个前缀建立一个版本的线段树
3. 查询：通过比较两个版本的差异来获得区间信息

优化注意事项：
1. 离散化是必须的，否则权值范围太大导致空间不足
2. 预分配足够的空间以避免运行时错误
3. 对于大规模数据，可以进一步优化内存使用
4. 可以使用更高效的离散化方法和输入方式
*/