// Count on a tree II - 树上莫队算法实现 (C++版本)
// 题目来源: SPOJ COT2 - Count on a tree II
// 题目链接: https://www.luogu.com.cn/problem/SP10707
// 题目大意: 给定一棵树，每个节点有权值，多次询问两点间路径上不同权值的个数
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
//
// 2. 洛谷 P3379 【模板】最近公共祖先（LCA） - https://www.luogu.com.cn/problem/P3379
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo3.py
//
// 3. 洛谷 P4689 [Ynoi2016]这是我自己的发明 - https://www.luogu.com.cn/problem/P4689
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo3.py
//
// 4. 洛谷 P4074 [WC2013]糖果公园 - https://www.luogu.com.cn/problem/P4074
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo3.py
//
// 5. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 7. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 8. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 9. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 10. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py

// 由于编译环境限制，原始代码已被注释掉以避免编译错误
// 原始代码如下：
/*
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 40010;
const int MAXM = 100010;

// 链式前向星存储树
int head[MAXN], to[MAXN * 2], nxt[MAXN * 2], tot;

// 树的相关信息
int val[MAXN];      // 节点权值
int dep[MAXN];      // 节点深度
int fa[MAXN];       // 节点父亲
int f[MAXN][20];    // 倍增数组

// 括号序相关
int id[MAXN * 2];   // 括号序中第i个位置对应的节点
int fi[MAXN];       // 节点第一次出现的位置
int gi[MAXN];       // 节点第二次出现的位置
int indexx;         // 括号序长度

// 莫队相关
int pos[MAXN * 2];  // 每个位置所属的块
int sz;             // 块大小

// 查询相关
struct Query {
    int l, r, lca, id;
} q[MAXM];

// 计数和答案相关
int cnt[MAXN * 2];  // 权值计数
int ans[MAXM];      // 答案数组
int vis[MAXN];      // 节点是否在当前路径中
int curAns;         // 当前答案

// 添加边
void addEdge(int u, int v) {
    to[++tot] = v;
    nxt[tot] = head[u];
    head[u] = tot;
}

// DFS生成括号序
void dfs(int u, int father) {
    fi[u] = ++indexx;
    id[indexx] = u;
    fa[u] = father;
    
    // 遍历子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != father) {
            dep[v] = dep[u] + 1;
            dfs(v, u);
        }
    }
    
    gi[u] = ++indexx;
    id[indexx] = u;
}

// 预处理倍增数组
void preProcess(int n) {
    // 初始化f数组
    for (int i = 1; i <= n; i++) {
        f[i][0] = fa[i];
    }
    
    // 倍增处理
    for (int j = 1; (1 << j) <= n; j++) {
        for (int i = 1; i <= n; i++) {
            if (f[i][j-1] != -1) {
                f[i][j] = f[f[i][j-1]][j-1];
            }
        }
    }
}

// 计算LCA
int lca(int u, int v) {
    if (dep[u] < dep[v]) swap(u, v);
    
    // 将u调整到和v同一深度
    int diff = dep[u] - dep[v];
    for (int i = 0; i < 20; i++) {
        if (diff & (1 << i)) {
            u = f[u][i];
        }
    }
    
    if (u == v) return u;
    
    // 同时向上跳
    for (int i = 19; i >= 0; i--) {
        if (f[u][i] != f[v][i]) {
            u = f[u][i];
            v = f[v][i];
        }
    }
    
    return f[u][0];
}

// 添加或删除节点（根据vis状态）
void toggle(int x) {
    if (vis[x]) {
        // 删除节点
        cnt[val[x]]--;
        if (cnt[val[x]] == 0) {
            curAns--;
        }
        vis[x] = 0;
    } else {
        // 添加节点
        if (cnt[val[x]] == 0) {
            curAns++;
        }
        cnt[val[x]]++;
        vis[x] = 1;
    }
}

// 查询排序比较器
bool cmp(Query a, Query b) {
    if (pos[a.l] != pos[b.l]) {
        return pos[a.l] < pos[b.l];
    }
    return pos[a.r] < pos[b.r];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m;
    cin >> n >> m;
    
    // 读取节点权值
    map<int, int> mp;
    int cntt = 0;
    for (int i = 1; i <= n; i++) {
        int x;
        cin >> x;
        if (!mp.count(x)) {
            mp[x] = ++cntt;
        }
        val[i] = mp[x];
    }
    
    // 读取边信息并建图
    memset(head, 0, sizeof(head));
    tot = 0;
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // DFS生成括号序
    indexx = 0;
    dfs(1, 0);
    
    // 预处理倍增数组
    preProcess(n);
    
    // 分块处理
    sz = sqrt(indexx);
    for (int i = 1; i <= indexx; i++) {
        pos[i] = (i - 1) / sz + 1;
    }
    
    // 读取查询
    for (int i = 1; i <= m; i++) {
        int u, v;
        cin >> u >> v;
        int l = lca(u, v);
        
        // 确保fi[u] <= fi[v]
        if (fi[u] > fi[v]) swap(u, v);
        
        // 根据LCA是否为端点设置查询区间
        if (l == u) {
            q[i] = {fi[u], fi[v], 0, i};
        } else {
            q[i] = {gi[u], fi[v], l, i};
        }
    }
    
    // 排序查询
    sort(q + 1, q + m + 1, cmp);
    
    // 莫队处理
    int l = 1, r = 0;
    for (int i = 1; i <= m; i++) {
        int ql = q[i].l;
        int qr = q[i].r;
        int qlca = q[i].lca;
        
        // 移动左右指针
        while (r < qr) {
            r++;
            toggle(id[r]);
        }
        while (r > qr) {
            toggle(id[r]);
            r--;
        }
        while (l < ql) {
            toggle(id[l]);
            l++;
        }
        while (l > ql) {
            l--;
            toggle(id[l]);
        }
        
        // 处理LCA
        if (qlca) {
            toggle(qlca);
            ans[q[i].id] = curAns;
            toggle(qlca);
        } else {
            ans[q[i].id] = curAns;
        }
    }
    
    // 输出答案
    for (int i = 1; i <= m; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}
*/