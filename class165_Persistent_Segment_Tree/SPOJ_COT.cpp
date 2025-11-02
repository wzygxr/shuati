#include <cstdio>
#include <algorithm>
#include <cstring>
#include <vector>
using namespace std;

/**
 * SPOJ COT - Count on a tree
 * 
 * 题目描述:
 * 有n个节点，编号1~n，每个节点有权值，有n-1条边，所有节点组成一棵树
 * 一共有m条查询，每条查询 u v k : 打印u号点到v号点的路径上，第k小的点权
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决树上路径第K小问题。
 * 1. 对所有点权进行离散化处理
 * 2. 以DFS序建立主席树，第i个版本表示以节点i为根的子树信息
 * 3. 利用LCA求解树上两点间路径
 * 4. 通过第u、v、lca(u,v)、fa[lca(u,v)]四个版本的线段树差值计算路径信息
 * 5. 在线段树上二分查找第k小的数
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 7 3
 * 1 2 3 4 5 6 7
 * 1 2
 * 1 3
 * 2 4
 * 2 5
 * 3 6
 * 3 7
 * 1 4 2
 * 2 6 3
 * 3 7 1
 * 
 * 输出:
 * 2
 * 4
 * 3
 */

const int MAXN = 100010;
const int MAXH = 20;

// 各个节点权值
int arr[MAXN];
// 收集权值排序并且去重做离散化
int sorted[MAXN];

// 链式前向星需要
int head[MAXN];
int to[MAXN << 1];
int next[MAXN << 1];
int cntg = 0;

// 可持久化线段树需要
int root[MAXN];
int left[MAXN * MAXH];
int right[MAXN * MAXH];
int size[MAXN * MAXH];
int cntt = 0;

// 树上倍增找lca需要
int deep[MAXN];
int stjump[MAXN][MAXH];
int n, m, s;

/**
 * 查找数字在离散化数组中的位置
 * @param num 要查找的数字
 * @return 离散化后的索引
 */
int kth(int num) {
    int l = 1, r = s, mid;
    while (l <= r) {
        mid = (l + r) / 2;
        if (sorted[mid] == num) {
            return mid;
        } else if (sorted[mid] < num) {
            l = mid + 1;
        } else {
            r = mid - 1;
        }
    }
    return -1;
}

/**
 * 构建空线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    int rt = ++cntt;
    size[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 准备阶段：离散化处理
 */
void prepare() {
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    std::sort(sorted + 1, sorted + n + 1);
    s = 1;
    for (int i = 2; i <= n; i++) {
        if (sorted[s] != sorted[i]) {
            sorted[++s] = sorted[i];
        }
    }
    root[0] = build(1, s);
}

/**
 * 添加边
 * @param u 起点
 * @param v 终点
 */
void addEdge(int u, int v) {
    next[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

/**
 * 在线段树中插入一个值
 * @param jobi 要插入的位置
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 前一个版本的节点编号
 * @return 新节点编号
 */
int insert(int jobi, int l, int r, int i) {
    int rt = ++cntt;
    left[rt] = left[i];
    right[rt] = right[i];
    size[rt] = size[i] + 1;
    if (l < r) {
        int mid = (l + r) / 2;
        if (jobi <= mid) {
            left[rt] = insert(jobi, l, mid, left[rt]);
        } else {
            right[rt] = insert(jobi, mid + 1, r, right[rt]);
        }
    }
    return rt;
}

/**
 * 查询第k小的数
 * @param jobk 第k小
 * @param l 区间左端点
 * @param r 区间右端点
 * @param u u节点的根
 * @param v v节点的根
 * @param lca lca节点的根
 * @param lcafa lca父节点的根
 * @return 第k小的数在离散化数组中的位置
 */
int query(int jobk, int l, int r, int u, int v, int lca, int lcafa) {
    if (l == r) {
        return l;
    }
    int lsize = size[left[u]] + size[left[v]] - size[left[lca]] - size[left[lcafa]];
    int mid = (l + r) / 2;
    if (lsize >= jobk) {
        return query(jobk, l, mid, left[u], left[v], left[lca], left[lcafa]);
    } else {
        return query(jobk - lsize, mid + 1, r, right[u], right[v], right[lca], right[lcafa]);
    }
}

// 迭代版DFS，防止递归爆栈
int ufe[MAXN][3];
int stackSize, u, f, e;

void push(int u, int f, int e) {
    ufe[stackSize][0] = u;
    ufe[stackSize][1] = f;
    ufe[stackSize][2] = e;
    stackSize++;
}

void pop() {
    --stackSize;
    u = ufe[stackSize][0];
    f = ufe[stackSize][1];
    e = ufe[stackSize][2];
}

/**
 * DFS构建主席树
 */
void dfs() {
    stackSize = 0;
    push(1, 0, -1);
    while (stackSize > 0) {
        pop();
        if (e == -1) {
            root[u] = insert(kth(arr[u]), 1, s, root[f]);
            deep[u] = deep[f] + 1;
            stjump[u][0] = f;
            for (int p = 1; p < MAXH; p++) {
                stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
            }
            e = head[u];
        } else {
            e = next[e];
        }
        if (e != 0) {
            push(u, f, e);
            if (to[e] != f) {
                push(to[e], u, -1);
            }
        }
    }
}

/**
 * 计算两个节点的最近公共祖先
 * @param a 节点a
 * @param b 节点b
 * @return 最近公共祖先
 */
int lca(int a, int b) {
    if (deep[a] < deep[b]) {
        std::swap(a, b);
    }
    for (int p = MAXH - 1; p >= 0; p--) {
        if (deep[stjump[a][p]] >= deep[b]) {
            a = stjump[a][p];
        }
    }
    if (a == b) {
        return a;
    }
    for (int p = MAXH - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    return stjump[a][0];
}

/**
 * 查询树上路径第k小
 * @param u 起点
 * @param v 终点
 * @param k 第k小
 * @return 第k小的值
 */
int kth_query(int u, int v, int k) {
    int lcaNode = lca(u, v);
    int i = query(k, 1, s, root[u], root[v], root[lcaNode], root[stjump[lcaNode][0]]);
    return sorted[i];
}

int main() {
    scanf("%d%d", &n, &m);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    prepare();
    
    for (int i = 1; i < n; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        addEdge(u, v);
        addEdge(v, u);
    }
    
    dfs();
    
    for (int i = 1; i <= m; i++) {
        int u, v, k;
        scanf("%d%d%d", &u, &v, &k);
        printf("%d\n", kth_query(u, v, k));
    }
    
    return 0;
}