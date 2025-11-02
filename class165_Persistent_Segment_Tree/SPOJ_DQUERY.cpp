/**
 * SPOJ DQUERY - D-query
 * 
 * 题目描述:
 * 给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中不同数字的个数。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决静态区间不同元素个数问题。
 * 1. 对于每个位置，记录该位置的数字上一次出现的位置
 * 2. 从前向后建立主席树，第i棵线段树表示前i个位置的信息
 * 3. 对于位置i，如果该数字之前出现过位置j，则在第i棵线段树中将位置j的计数减1，
 *    位置i的计数加1；否则只在位置i的计数加1
 * 4. 利用前缀和思想，通过第r棵和第l-1棵线段树的差得到区间[l,r]的信息
 * 5. 查询区间不同数字个数即为查询区间内计数大于0的位置个数
 * 
 * 时间复杂度: O(n log n + q log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5
 * 1 1 2 1 3
 * 3
 * 1 5
 * 2 4
 * 3 5
 * 
 * 输出:
 * 3
 * 2
 * 3
 */
const int MAXN = 30010;
const int MAXV = 1000010; // 值域范围

// 原始数组
int arr[MAXN];
// pos[v] : 数字v上次出现的位置
int pos[MAXV];

// 可持久化线段树需要
int root[MAXN];
int left[MAXN * 20];
int right[MAXN * 20];
int sum[MAXN * 20];
int cnt = 0;

/**
 * 构建空线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    int rt = ++cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 在线段树中更新一个位置的值
 * @param pos 要更新的位置
 * @param val 要增加的值
 * @param l 区间左端点
 * @param r 区间右端点
 * @param pre 前一个版本的节点编号
 * @return 新节点编号
 */
int update(int pos, int val, int l, int r, int pre) {
    int rt = ++cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + val;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = update(pos, val, l, mid, left[rt]);
        } else {
            right[rt] = update(pos, val, mid + 1, r, right[rt]);
        }
    }
    return rt;
}

/**
 * 查询区间不同数字个数
 * @param l 查询区间左端点
 * @param r 查询区间右端点
 * @param u 前一个版本的根节点
 * @param v 当前版本的根节点
 * @return 不同数字个数
 */
int query(int l, int r, int u, int v) {
    if (l == r) {
        return sum[v] - sum[u] > 0 ? 1 : 0;
    }
    int mid = (l + r) / 2;
    return query(l, mid, left[u], left[v]) + query(mid + 1, r, right[u], right[v]);
}