// Blood Cousins Return, C++版本
// 题目来源: Codeforces 246E
// 链接: https://www.luogu.com.cn/problem/CF246E
// 
// 题目大意:
// 给定一棵家族树，n个人，每个人有一个名字和直接祖先(0表示没有祖先)
// 定义k级祖先和k级儿子关系
// m次查询，每次查询某个人的所有k级儿子中不同名字的个数
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的深度、子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每个深度上的不同名字集合
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 离线处理所有查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 深度处理:
// 1. 维护各深度上的名字集合
// 2. 通过相对深度计算查询结果
// 3. 使用HashSet快速统计不同名字数量
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>
#include <algorithm>
#include <vector>
#include <map>
#include <set>
#include <string>
using namespace std;

const int MAXN = 100005;

// 树相关数据结构
int n, m;
string name[MAXN];
int father[MAXN];
int head[MAXN], next[MAXN << 1], to[MAXN << 1], cnt = 0;

// 树链剖分相关
int size[MAXN];  // 子树大小
int dep[MAXN];   // 深度
int son[MAXN];   // 重儿子
int fa[MAXN];    // 父亲节点

// DSU on Tree相关
map<string, int> nameMap; // 名字离散化
int nameCnt = 0;
int nameId[MAXN]; // 每个节点的名字ID

// 每个深度上的名字集合
set<int> depthNames[MAXN];

// 查询相关
struct Query {
    int k, id;
    Query(int k, int id) : k(k), id(id) {}
};

vector<Query> queries[MAXN];
int ans[MAXN];

void addEdge(int u, int v) {
    next[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// 第一次DFS，计算子树大小、深度、重儿子
void dfs1(int u, int f, int depth) {
    fa[u] = f;
    dep[u] = depth;
    size[u] = 1;
    son[u] = 0;
    
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != f) {
            dfs1(v, u, depth + 1);
            size[u] += size[v];
            if (son[u] == 0 || size[son[u]] < size[v]) {
                son[u] = v;
            }
        }
    }
}

// 添加节点u到指定深度的集合中
void addName(int u, int baseDepth) {
    int d = dep[u] - baseDepth; // 相对于根节点的深度
    depthNames[d].insert(nameId[u]);
    
    // 递归处理子节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u]) {
            addName(v, baseDepth);
        }
    }
}

// 清除指定节点子树的信息
void clearNames(int u) {
    int d = dep[u] - dep[u]; // 相对于自身的深度，即0
    depthNames[d].erase(nameId[u]);
    
    // 递归处理子节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u]) {
            clearNames(v);
        }
    }
}

// DSU on Tree 主过程
void dsuOnTree(int u, int keep) {
    // 处理所有轻儿子
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u] && v != son[u]) {
            dsuOnTree(v, 0); // 不保留信息
        }
    }
    
    // 处理重儿子
    if (son[u] != 0) {
        dsuOnTree(son[u], 1); // 保留信息
    }
    
    // 将轻儿子的贡献加入
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u] && v != son[u]) {
            addName(v, dep[u]); // 将v子树中所有节点按相对深度加入
        }
    }
    
    // 加入当前节点
    int d = dep[u] - dep[u]; // 当前节点相对深度为0
    depthNames[d].insert(nameId[u]);
    
    // 处理当前节点的所有查询
    for (int i = 0; i < queries[u].size(); i++) {
        Query q = queries[u][i];
        int k = q.k;
        int queryDepth = k; // 查询k级儿子，即深度为k的节点
        ans[q.id] = depthNames[queryDepth].size();
    }
    
    // 如果不保留信息，则清除
    if (keep == 0) {
        clearNames(u);
        // 清空所有深度集合
        for (int i = 0; i < MAXN; i++) {
            if (!depthNames[i].empty()) {
                depthNames[i].clear();
            }
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    cin >> n;
    
    // 读取每个人的信息
    for (int i = 1; i <= n; i++) {
        cin >> name[i] >> father[i];
        
        // 名字离散化
        if (nameMap.find(name[i]) == nameMap.end()) {
            nameMap[name[i]] = ++nameCnt;
        }
        nameId[i] = nameMap[name[i]];
        
        if (father[i] != 0) {
            addEdge(father[i], i);
            addEdge(i, father[i]);
        }
    }
    
    // 找到所有根节点并处理
    for (int i = 1; i <= n; i++) {
        if (father[i] == 0) {
            dfs1(i, 0, 1);
        }
    }
    
    cin >> m;
    
    // 读取查询
    for (int i = 1; i <= m; i++) {
        int v, k;
        cin >> v >> k;
        // 将查询挂到对应的节点上
        queries[v].push_back(Query(k, i));
    }
    
    // 处理所有根节点以处理查询
    for (int i = 1; i <= n; i++) {
        if (father[i] == 0) {
            dsuOnTree(i, 0);
        }
    }
    
    // 输出结果
    for (int i = 1; i <= m; i++) {
        cout << ans[i] << " ";
    }
    cout << "\n";
    
    return 0;
}// Blood Cousins Return, C++版本
// 题目来源: Codeforces 246E
// 链接: https://www.luogu.com.cn/problem/CF246E
// 
// 题目大意:
// 给定一棵家族树，n个人，每个人有一个名字和直接祖先(0表示没有祖先)
// 定义k级祖先和k级儿子关系
// m次查询，每次查询某个人的所有k级儿子中不同名字的个数
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的深度、子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每个深度上的不同名字集合
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 离线处理所有查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 深度处理:
// 1. 维护各深度上的名字集合
// 2. 通过相对深度计算查询结果
// 3. 使用HashSet快速统计不同名字数量
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cmath>
#include <algorithm>
#include <vector>
#include <map>
#include <set>
#include <string>
using namespace std;

const int MAXN = 100005;

// 树相关数据结构
int n, m;
string name[MAXN];
int father[MAXN];
int head[MAXN], next[MAXN << 1], to[MAXN << 1], cnt = 0;

// 树链剖分相关
int size[MAXN];  // 子树大小
int dep[MAXN];   // 深度
int son[MAXN];   // 重儿子
int fa[MAXN];    // 父亲节点

// DSU on Tree相关
map<string, int> nameMap; // 名字离散化
int nameCnt = 0;
int nameId[MAXN]; // 每个节点的名字ID

// 每个深度上的名字集合
set<int> depthNames[MAXN];

// 查询相关
struct Query {
    int k, id;
    Query(int k, int id) : k(k), id(id) {}
};

vector<Query> queries[MAXN];
int ans[MAXN];

void addEdge(int u, int v) {
    next[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// 第一次DFS，计算子树大小、深度、重儿子
void dfs1(int u, int f, int depth) {
    fa[u] = f;
    dep[u] = depth;
    size[u] = 1;
    son[u] = 0;
    
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != f) {
            dfs1(v, u, depth + 1);
            size[u] += size[v];
            if (son[u] == 0 || size[son[u]] < size[v]) {
                son[u] = v;
            }
        }
    }
}

// 添加节点u到指定深度的集合中
void addName(int u, int baseDepth) {
    int d = dep[u] - baseDepth; // 相对于根节点的深度
    depthNames[d].insert(nameId[u]);
    
    // 递归处理子节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u]) {
            addName(v, baseDepth);
        }
    }
}

// 清除指定节点子树的信息
void clearNames(int u) {
    int d = dep[u] - dep[u]; // 相对于自身的深度，即0
    depthNames[d].erase(nameId[u]);
    
    // 递归处理子节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u]) {
            clearNames(v);
        }
    }
}

// DSU on Tree 主过程
void dsuOnTree(int u, int keep) {
    // 处理所有轻儿子
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u] && v != son[u]) {
            dsuOnTree(v, 0); // 不保留信息
        }
    }
    
    // 处理重儿子
    if (son[u] != 0) {
        dsuOnTree(son[u], 1); // 保留信息
    }
    
    // 将轻儿子的贡献加入
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != fa[u] && v != son[u]) {
            addName(v, dep[u]); // 将v子树中所有节点按相对深度加入
        }
    }
    
    // 加入当前节点
    int d = dep[u] - dep[u]; // 当前节点相对深度为0
    depthNames[d].insert(nameId[u]);
    
    // 处理当前节点的所有查询
    for (int i = 0; i < queries[u].size(); i++) {
        Query q = queries[u][i];
        int k = q.k;
        int queryDepth = k; // 查询k级儿子，即深度为k的节点
        ans[q.id] = depthNames[queryDepth].size();
    }
    
    // 如果不保留信息，则清除
    if (keep == 0) {
        clearNames(u);
        // 清空所有深度集合
        for (int i = 0; i < MAXN; i++) {
            if (!depthNames[i].empty()) {
                depthNames[i].clear();
            }
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    cin >> n;
    
    // 读取每个人的信息
    for (int i = 1; i <= n; i++) {
        cin >> name[i] >> father[i];
        
        // 名字离散化
        if (nameMap.find(name[i]) == nameMap.end()) {
            nameMap[name[i]] = ++nameCnt;
        }
        nameId[i] = nameMap[name[i]];
        
        if (father[i] != 0) {
            addEdge(father[i], i);
            addEdge(i, father[i]);
        }
    }
    
    // 找到所有根节点并处理
    for (int i = 1; i <= n; i++) {
        if (father[i] == 0) {
            dfs1(i, 0, 1);
        }
    }
    
    cin >> m;
    
    // 读取查询
    for (int i = 1; i <= m; i++) {
        int v, k;
        cin >> v >> k;
        // 将查询挂到对应的节点上
        queries[v].push_back(Query(k, i));
    }
    
    // 处理所有根节点以处理查询
    for (int i = 1; i <= n; i++) {
        if (father[i] == 0) {
            dsuOnTree(i, 0);
        }
    }
    
    // 输出结果
    for (int i = 1; i <= m; i++) {
        cout << ans[i] << " ";
    }
    cout << "\n";
    
    return 0;
}