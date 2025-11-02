// 测试链接 : https://www.luogu.com.cn/problem/P3224
// P3224 [HNOI2012]永无乡 - C++实现

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * P3224 [HNOI2012]永无乡
 * 
 * 题目描述：
 * 永无乡包含n座岛，编号从1到n，每座岛都有自己的重要度。
 * 初始时没有桥，支持两种操作：
 * 1. 在岛a和岛b之间建一座桥
 * 2. 询问与岛a连通的所有岛中，重要度第k小的岛编号
 * 
 * 核心算法：线段树合并 + 并查集
 * 时间复杂度：O((n+m) log n)
 * 空间复杂度：O(n log n)
 */

const int MAXN = 100010;
const int MAXM = 10000000;

// 线段树节点
struct Node {
    int l, r;
    int cnt;  // 区间内元素个数
    
    Node() : l(-1), r(-1), cnt(0) {}
};

vector<Node> tree(MAXM);
int cnt = 0;
vector<int> roots(MAXN, -1);

// 并查集
vector<int> parent(MAXN);
vector<int> size(MAXN);

// 重要度映射
vector<int> rankToId(MAXN);  // 重要度排名对应的岛编号
vector<int> idToRank(MAXN);  // 岛编号对应的重要度排名

int new_node() {
    if (cnt >= MAXM) {
        tree.resize(cnt + 1000000);
    }
    tree[cnt] = Node();
    return cnt++;
}

void push_up(int rt) {
    if (rt == -1) return;
    
    tree[rt].cnt = 0;
    if (tree[rt].l != -1) {
        tree[rt].cnt += tree[tree[rt].l].cnt;
    }
    if (tree[rt].r != -1) {
        tree[rt].cnt += tree[tree[rt].r].cnt;
    }
}

void update(int rt, int l, int r, int pos) {
    if (l == r) {
        tree[rt].cnt++;
        return;
    }
    
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        if (tree[rt].l == -1) {
            tree[rt].l = new_node();
        }
        update(tree[rt].l, l, mid, pos);
    } else {
        if (tree[rt].r == -1) {
            tree[rt].r = new_node();
        }
        update(tree[rt].r, mid + 1, r, pos);
    }
    push_up(rt);
}

int merge(int p, int q, int l, int r) {
    if (p == -1) return q;
    if (q == -1) return p;
    
    if (l == r) {
        tree[p].cnt += tree[q].cnt;
        return p;
    }
    
    int mid = (l + r) >> 1;
    
    if (tree[p].l != -1 && tree[q].l != -1) {
        tree[p].l = merge(tree[p].l, tree[q].l, l, mid);
    } else if (tree[q].l != -1) {
        tree[p].l = tree[q].l;
    }
    
    if (tree[p].r != -1 && tree[q].r != -1) {
        tree[p].r = merge(tree[p].r, tree[q].r, mid + 1, r);
    } else if (tree[q].r != -1) {
        tree[p].r = tree[q].r;
    }
    
    push_up(p);
    return p;
}

int query_kth(int rt, int l, int r, int k) {
    if (l == r) {
        return l;
    }
    
    int mid = (l + r) >> 1;
    int left_cnt = (tree[rt].l != -1) ? tree[tree[rt].l].cnt : 0;
    
    if (k <= left_cnt) {
        return query_kth(tree[rt].l, l, mid, k);
    } else {
        return query_kth(tree[rt].r, mid + 1, r, k - left_cnt);
    }
}

// 并查集操作
void init_dsu(int n) {
    for (int i = 1; i <= n; i++) {
        parent[i] = i;
        size[i] = 1;
    }
}

int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);
    }
    return parent[x];
}

void union_sets(int x, int y) {
    int px = find(x);
    int py = find(y);
    if (px == py) return;
    
    // 按秩合并：小树合并到大树
    if (size[px] < size[py]) {
        swap(px, py);
    }
    
    // 合并线段树
    roots[px] = merge(roots[px], roots[py], 1, MAXN);
    parent[py] = px;
    size[px] += size[py];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    // 初始化并查集
    init_dsu(n);
    
    // 读入重要度
    for (int i = 1; i <= n; i++) {
        int rank;
        cin >> rank;
        rankToId[rank] = i;
        idToRank[i] = rank;
    }
    
    // 初始化线段树
    for (int i = 1; i <= n; i++) {
        roots[i] = new_node();
        update(roots[i], 1, n, idToRank[i]);
    }
    
    // 读入初始桥
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        union_sets(u, v);
    }
    
    int q;
    cin >> q;
    
    vector<int> results;
    
    while (q--) {
        char op;
        cin >> op;
        
        if (op == 'B') {
            // 建桥操作
            int u, v;
            cin >> u >> v;
            union_sets(u, v);
        } else {
            // 查询操作
            int u, k;
            cin >> u >> k;
            
            int root = find(u);
            if (roots[root] == -1 || tree[roots[root]].cnt < k) {
                results.push_back(-1);
            } else {
                int rank = query_kth(roots[root], 1, n, k);
                results.push_back(rankToId[rank]);
            }
        }
    }
    
    // 输出结果
    for (int res : results) {
        cout << res << "\n";
    }
    
    return 0;
}

/*
 * 算法详解：
 * 
 * 1. 问题分析：
 *    需要维护动态连通性，并支持查询连通块内第k小的元素。
 *    由于需要动态合并连通块并查询第k小，需要高效的数据结构。
 * 
 * 2. 核心思路：
 *    - 使用并查集维护连通性
 *    - 每个连通块维护一棵线段树，记录重要度的分布
 *    - 合并连通块时合并对应的线段树
 *    - 查询时在线段树上二分查找第k小
 * 
 * 3. 算法步骤：
 *    a. 初始化：每个岛单独一个连通块，对应一棵线段树
 *    b. 建桥操作：合并两个连通块，同时合并对应的线段树
 *    c. 查询操作：在对应连通块的线段树上查询第k小
 * 
 * 4. 时间复杂度分析：
 *    - 线段树合并：O(n log n)
 *    - 查询操作：O(log n)
 *    - 总体复杂度：O((n+m) log n)
 * 
 * 5. 优化技巧：
 *    - 按秩合并：小树合并到大树，减少合并深度
 *    - 动态开点：节省空间
 *    - 线段树合并：避免重复计算
 * 
 * 6. 类似题目：
 *    - P4556 [Vani有约会]雨天的尾巴
 *    - P5298 [PKUWC2018]Minimax
 *    - CF911G Mass Change Queries
 *    - P6773 [NOI2020]命运
 * 
 * 7. 应用场景：
 *    - 动态连通性问题
 *    - 连通块内统计查询
 *    - 支持合并和查询的数据结构
 */