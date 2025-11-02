/**
 * 线段树合并专题 - 雨天的尾巴问题
 * 
 * 本题来源于Vani有约会，是线段树合并技术的经典应用
 * 测试链接: https://www.luogu.com.cn/problem/P4556
 * 
 * 题目大意：
 * - 在树上进行路径加操作，每次给路径上所有节点添加某种类型的救济粮
 * - 最后查询每个节点最多的救济粮类型
 * 
 * 解题思路：
 * 1. 树上差分：将路径操作转化为端点差分
 * 2. 树链剖分：高效处理树上路径问题，求LCA
 * 3. 线段树合并：动态维护每个节点的信息，通过合并得到子树信息
 * 
 * 时间复杂度分析：
 * - 树链剖分预处理：O(n)
 * - 线段树更新操作：O(log n) 每操作
 * - 线段树合并操作：O(n log n) 总体
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点数：O(n log n)
 * - 其他辅助数组：O(n)
 * - 总空间复杂度：O(n log n)
 */

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

#include <iostream>
#include <vector>
#include <functional>
using namespace std;

const int MAXN = 100001;     // 最大节点数
const int MAXT = MAXN * 100; // 线段树节点数，需要足够大

// 树结构相关变量
int n, m;                    // 节点数和操作数
int fa[MAXN];                // 父亲节点
int dep[MAXN];               // 节点深度
int sz[MAXN];                // 子树大小
int hs[MAXN];                // 重儿子
int top[MAXN];               // 所在重链的顶端
int id[MAXN];                // dfs序编号
int orig[MAXN];              // dfs序对应的原节点编号
int cnt;                     // dfs序计数器

// 链式前向星存图
int head[MAXN];              // 邻接表头节点
int nxt[MAXN * 2];           // 下一条边
int to[MAXN * 2];            // 边指向的节点
int eCnt;                    // 边计数器

// 线段树相关变量
int root[MAXN];              // 每个节点的线段树根节点
int ls[MAXT];                // 左子节点
int rs[MAXT];                // 右子节点
int max_val[MAXT];           // 区间最大值（出现次数）
int ans_type[MAXT];          // 最大值对应的救济粮类型
int segCnt;                  // 线段树节点计数器

/**
 * 添加无向边到邻接表
 * @param u 边的一个端点
 * @param v 边的另一个端点
 */
void addEdge(int u, int v) {
    nxt[++eCnt] = head[u];   // 当前边的下一条边是原来的头节点
    to[eCnt] = v;            // 边指向的节点是v
    head[u] = eCnt;          // 更新头节点为当前边
}

/**
 * 第一次DFS：计算父节点、深度、子树大小、重儿子
 * @param u 当前节点
 * @param father 父节点
 * @param depth 当前深度
 */
void dfs1(int u, int father, int depth) {
    fa[u] = father;          // 设置父节点
    dep[u] = depth;          // 设置深度
    sz[u] = 1;               // 初始化子树大小为1
    hs[u] = 0;               // 初始化重儿子为0
    
    // 遍历所有子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != father) {   // 确保不回到父节点
            dfs1(v, u, depth + 1);
            sz[u] += sz[v];  // 更新子树大小
            // 更新重儿子（子树最大的节点）
            if (sz[v] > sz[hs[u]]) {
                hs[u] = v;
            }
        }
    }
}

/**
 * 第二次DFS：处理链剖分，分配DFS序
 * @param u 当前节点
 * @param tp 当前链的顶端节点
 */
void dfs2(int u, int tp) {
    id[u] = ++cnt;           // 分配DFS序
    orig[cnt] = u;           // 记录DFS序对应的原节点
    top[u] = tp;             // 设置当前节点所在链的顶端
    
    // 优先处理重儿子，保证重链连续
    if (hs[u]) {
        dfs2(hs[u], tp);     // 重儿子与当前节点在同一链
    }
    
    // 处理所有轻儿子
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != fa[u] && v != hs[u]) {
            dfs2(v, v);      // 轻儿子作为新链的顶端
        }
    }
}

/**
 * 向上更新线段树节点信息
 * @param p 当前节点编号
 */
void pushUp(int p) {
    // 比较左右子树，选择最大值较大的一方
    if (max_val[ls[p]] >= max_val[rs[p]]) {
        max_val[p] = max_val[ls[p]];
        ans_type[p] = ans_type[ls[p]];
    } else {
        max_val[p] = max_val[rs[p]];
        ans_type[p] = ans_type[rs[p]];
    }
}

/**
 * 线段树单点修改
 * @param pos 修改的位置（救济粮类型）
 * @param val 修改的值（+1或-1）
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param rootId 当前子树根节点编号（引用传递）
 */
void update(int pos, int val, int l, int r, int& rootId) {
    // 如果根节点不存在，创建新节点
    if (!rootId) rootId = ++segCnt;
    
    // 叶子节点，直接修改值
    if (l == r) {
        max_val[rootId] += val;
        ans_type[rootId] = l;  // 叶子节点对应的类型就是当前区间
        return;
    }
    
    // 计算中点，递归更新左子树或右子树
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        update(pos, val, l, mid, ls[rootId]);
    } else {
        update(pos, val, mid + 1, r, rs[rootId]);
    }
    
    // 更新当前节点的最大值和对应类型
    pushUp(rootId);
}

/**
 * 线段树合并
 * @param p1 第一个线段树的根节点
 * @param p2 第二个线段树的根节点
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @return 合并后的线段树根节点
 */
int merge(int p1, int p2, int l, int r) {
    // 如果其中一个为空，返回另一个
    if (!p1 || !p2) {
        return p1 + p2;
    }
    
    // 叶子节点，直接合并计数值
    if (l == r) {
        max_val[p1] += max_val[p2];
        ans_type[p1] = l;
        return p1;  // 返回p1，不再使用p2
    }
    
    // 递归合并左右子树
    int mid = (l + r) >> 1;
    ls[p1] = merge(ls[p1], ls[p2], l, mid);
    rs[p1] = merge(rs[p1], rs[p2], mid + 1, r);
    
    // 更新当前节点的信息
    pushUp(p1);
    return p1;
}

/**
 * 使用树链剖分求两个节点的最近公共祖先（LCA）
 * @param x 第一个节点
 * @param y 第二个节点
 * @return x和y的LCA
 */
int lca(int x, int y) {
    // 当x和y不在同一链时，不断将较深的节点向上跳
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            swap(x, y);  // 保证x所在链的顶端较深
        }
        x = fa[top[x]];  // 跳到链顶端的父节点
    }
    // 最终在同一链时，返回较浅的节点
    return dep[x] < dep[y] ? x : y;
}

/**
 * 使用树上差分进行路径更新
 * @param x 路径的一个端点
 * @param y 路径的另一个端点
 * @param z 救济粮类型
 */
void updatePath(int x, int y, int z) {
    int l = lca(x, y);  // 计算LCA
    // 使用差分思想，在x和y处+1，在l和l的父节点处-1
    update(z, 1, 1, MAXN, root[x]);
    update(z, 1, 1, MAXN, root[y]);
    update(z, -1, 1, MAXN, root[l]);
    if (fa[l]) {  // 确保l有父节点
        update(z, -1, 1, MAXN, root[fa[l]]);
    }
}

/**
 * 第三次DFS：后序遍历合并子节点的线段树
 * @param u 当前节点
 */
void dfs3(int u) {
    // 递归处理所有子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != fa[u]) {
            dfs3(v);
            // 将子节点的线段树合并到当前节点
            root[u] = merge(root[u], root[v], 1, MAXN);
        }
    }
}

/**
 * 主函数
 * 处理输入、初始化和输出
 */
int main() {
    // 输入处理
    cin >> n >> m;
    
    // 初始化邻接表
    eCnt = 0;
    for (int i = 1; i <= n; ++i) {
        head[i] = 0;
    }
    
    // 读取树的边
    for (int i = 1; i < n; ++i) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 初始化链剖分相关变量
    cnt = 0;
    dfs1(1, 0, 1);  // 第一次DFS
    dfs2(1, 1);     // 第二次DFS
    
    // 初始化线段树相关变量
    segCnt = 0;
    for (int i = 1; i <= n; ++i) {
        root[i] = 0;
    }
    
    // 处理m个路径更新操作
    for (int i = 1; i <= m; ++i) {
        int x, y, z;
        cin >> x >> y >> z;
        updatePath(x, y, z);
    }
    
    // 后序遍历合并线段树
    dfs3(1);
    
    // 输出每个节点的答案
    for (int i = 1; i <= n; ++i) {
        // 如果max_val[root[i]]为0，表示没有救济粮，输出0
        // 否则输出对应的类型ans_type[root[i]]
        if (max_val[root[i]] == 0) {
            cout << 0 << endl;
        } else {
            cout << ans_type[root[i]] << endl;
        }
    }
    
    return 0;
}

/*
=============================================================
补充题目：Codeforces 600E - Lomsat gelral
题目链接：https://codeforces.com/contest/600/problem/E

题目大意：
给定一棵树，每个节点有一个颜色，求每个节点的子树中，出现次数最多的所有颜色的编号和。

解题思路：
与雨天的尾巴问题类似，同样可以使用线段树合并解决。
主要区别在于线段树维护的信息不同：
1. 需要维护每种颜色的出现次数
2. 维护出现次数最多的颜色的编号和

线段树节点需要记录：
- 最大出现次数（max_cnt）
- 出现次数最多的颜色的编号和（sum）

合并时，如果左右子树的最大出现次数相同，则将两个子树的sum相加；
否则，选择出现次数较大的子树的sum。
=============================================================
*/

/**
 * Codeforces 600E - Lomsat gelral 的完整实现
 * 时间复杂度：O(n log c)，其中c为颜色数量
 * 空间复杂度：O(n log c)
 */
namespace Codeforces600E {
    const int MAXN = 100005;
    const int MAXT = MAXN * 20; // 动态开点线段树节点数
    
    int n;                     // 节点数
    int color[MAXN];           // 每个节点的颜色
    
    // 链式前向星存图
    int head[MAXN], nxt[MAXN * 2], to[MAXN * 2], eCnt;
    
    // 线段树相关变量
    int root[MAXN];            // 每个节点的线段树根节点
    int ls[MAXT], rs[MAXT];    // 左右子节点
    long long sum[MAXT];       // 出现次数最多的颜色的和
    int max_cnt[MAXT];         // 最大出现次数
    int cnt[MAXT];             // 当前位置的出现次数
    int segCnt;                // 线段树节点计数器
    
    // 添加边
    void addEdge(int u, int v) {
        nxt[++eCnt] = head[u];
        to[eCnt] = v;
        head[u] = eCnt;
    }
    
    // 向上更新线段树节点信息
    void pushUp(int p) {
        if (max_cnt[ls[p]] > max_cnt[rs[p]]) {
            max_cnt[p] = max_cnt[ls[p]];
            sum[p] = sum[ls[p]];
        } else if (max_cnt[rs[p]] > max_cnt[ls[p]]) {
            max_cnt[p] = max_cnt[rs[p]];
            sum[p] = sum[rs[p]];
        } else {
            max_cnt[p] = max_cnt[ls[p]];
            sum[p] = sum[ls[p]] + sum[rs[p]];
        }
    }
    
    // 线段树单点更新
    void update(int pos, int val, int l, int r, int& p) {
        if (!p) p = ++segCnt;
        
        if (l == r) {
            cnt[p] += val;
            max_cnt[p] = cnt[p];
            sum[p] = pos;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            update(pos, val, l, mid, ls[p]);
        } else {
            update(pos, val, mid + 1, r, rs[p]);
        }
        
        pushUp(p);
    }
    
    // 线段树合并
    int merge(int p1, int p2, int l, int r) {
        if (!p1 || !p2) return p1 + p2;
        
        if (l == r) {
            cnt[p1] += cnt[p2];
            max_cnt[p1] = cnt[p1];
            sum[p1] = l;
            return p1;
        }
        
        int mid = (l + r) >> 1;
        ls[p1] = merge(ls[p1], ls[p2], l, mid);
        rs[p1] = merge(rs[p1], rs[p2], mid + 1, r);
        
        pushUp(p1);
        return p1;
    }
    
    long long ans[MAXN]; // 存储每个节点的答案
    
    // 后序遍历树，合并线段树
    void dfs(int u, int parent) {
        // 初始化当前节点的颜色
        update(color[u], 1, 1, n, root[u]);
        
        // 遍历所有子节点
        for (int i = head[u]; i; i = nxt[i]) {
            int v = to[i];
            if (v != parent) {
                dfs(v, u);
                // 合并子节点的线段树
                root[u] = merge(root[u], root[v], 1, n);
            }
        }
        
        // 保存当前节点的答案
        ans[u] = sum[root[u]];
    }
    
    // 主函数，处理输入输出和初始化
    void solve() {
        cin >> n;
        for (int i = 1; i <= n; ++i) {
            cin >> color[i];
        }
        
        // 初始化邻接表
        eCnt = 0;
        for (int i = 1; i <= n; ++i) {
            head[i] = 0;
        }
        
        // 读取边信息
        for (int i = 1; i < n; ++i) {
            int u, v;
            cin >> u >> v;
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 初始化线段树相关变量
        segCnt = 0;
        for (int i = 1; i <= n; ++i) {
            root[i] = 0;
        }
        
        // 开始DFS
        dfs(1, 0);
        
        // 输出结果
        for (int i = 1; i <= n; ++i) {
            cout << ans[i] << " ";
        }
        cout << endl;
    }
}

/**
 * LeetCode 1519. 子树中标签相同的节点数
 * 题目链接：https://leetcode.cn/problems/number-of-nodes-in-the-sub-tree-with-the-same-label/
 * 
 * 由于标签只有26种，可以使用数组优化空间复杂度
 * 时间复杂度：O(n * 26)
 * 空间复杂度：O(n)
 */
namespace LeetCode1519 {
    vector<int> countSubTrees(int n, vector<vector<int>>& edges, string labels) {
        // 构建邻接表
        vector<vector<int>> graph(n);
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        vector<int> result(n);
        
        // DFS函数，返回每个子树中各标签的出现次数
        function<vector<int>(int, int)> dfs = [&](int node, int parent) -> vector<int> {
            vector<int> counts(26, 0);
            char label = labels[node];
            counts[label - 'a'] = 1; // 当前节点自身计入一次
            
            for (int neighbor : graph[node]) {
                if (neighbor != parent) {
                    vector<int> child_counts = dfs(neighbor, node);
                    // 合并子节点的计数
                    for (int i = 0; i < 26; ++i) {
                        counts[i] += child_counts[i];
                    }
                }
            }
            
            result[node] = counts[label - 'a'];
            return counts;
        };
        
        dfs(0, -1);
        return result;
    }
}

// 总结：
// 1. 线段树合并是一种高效处理树上子树信息统计的算法
// 2. 核心思想是通过后序遍历，自底向上合并子节点的线段树
// 3. 适用于需要频繁合并区间信息的场景
// 4. 动态开点技术可以有效节省空间
// 5. 性能优化技巧：
//    - 预分配足够的空间以减少动态内存分配
//    - 避免不必要的递归调用
//    - 合理使用位运算优化
// 6. 工程应用考虑：
//    - 注意栈溢出问题，对于大规模数据可能需要非递归实现
//    - 内存管理，避免内存泄漏
//    - 输入输出效率，使用快速IO方法
//    - 错误处理，包括边界情况和异常输入
//    - 测试覆盖，确保算法在各种场景下的正确性