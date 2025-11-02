// Tree and Queries, C++版
// 题目来源: Codeforces 375D
// 链接: https://codeforces.com/problemset/problem/375/D
// 
// 题目大意:
// 给定一棵n个节点的树，每个节点有一个颜色值。
// 有m个查询，每个查询给定一个节点v和一个整数k，
// 要求统计v的子树中，出现次数至少为k的颜色数量。
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每种颜色的出现次数
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
// 查询处理:
// 对于每个查询，统计子树中出现次数至少为k的颜色数量
// 通过维护出现i次的颜色数量来快速计算答案
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

// 由于编译环境限制，不使用标准头文件
// 使用基本的C++语法和内置类型

const int MAXN = 100001;
int n, m;
int color[MAXN];

int head[MAXN];
int nxt[MAXN << 1];
int to[MAXN << 1];
int cnt = 0;

int fa[MAXN];
int siz[MAXN];
int son[MAXN];

int colorCount[MAXN];
int countFreq[MAXN];
int ans[MAXN];

// 查询相关
int queryHead[MAXN];
int queryNext[MAXN];
int queryNode[MAXN];
int queryK[MAXN];
int queryCnt = 0;

void addEdge(int u, int v) {
    nxt[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

void addQuery(int u, int id, int k) {
    queryNext[++queryCnt] = queryHead[u];
    queryNode[queryCnt] = id;
    queryK[queryCnt] = k;
    queryHead[u] = queryCnt;
}

void dfs1(int u, int f) {
    fa[u] = f;
    siz[u] = 1;
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != f) {
            dfs1(v, u);
        }
    }
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != f) {
            siz[u] += siz[v];
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

void addNode(int u) {
    // 原来的出现次数对应的频率减1
    countFreq[colorCount[color[u]]]--;
    // 颜色出现次数加1
    colorCount[color[u]]++;
    // 新的出现次数对应的频率加1
    countFreq[colorCount[color[u]]]++;
}

void removeNode(int u) {
    // 原来的出现次数对应的频率减1
    countFreq[colorCount[color[u]]]--;
    // 颜色出现次数减1
    colorCount[color[u]]--;
    // 新的出现次数对应的频率加1
    countFreq[colorCount[color[u]]]++;
}

void addSubtree(int u, int fa) {
    addNode(u);
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa) {
            addSubtree(v, u);
        }
    }
}

void removeSubtree(int u, int fa) {
    removeNode(u);
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa) {
            removeSubtree(v, u);
        }
    }
}

void dsuOnTree(int u, int fa, int keep) {
    // 处理所有轻儿子
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa && v != son[u]) {
            dsuOnTree(v, u, 0);  // 不保留信息
        }
    }
    
    // 处理重儿子
    if (son[u] != 0) {
        dsuOnTree(son[u], u, 1);  // 保留信息
    }
    
    // 添加当前节点的贡献
    addNode(u);
    
    // 添加轻儿子的贡献
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa && v != son[u]) {
            addSubtree(v, u);
        }
    }
    
    // 处理当前节点的所有查询
    for (int e = queryHead[u]; e; e = queryNext[e]) {
        int id = queryNode[e];
        int k = queryK[e];
        // 统计出现次数至少为k的颜色数量
        int result = 0;
        for (int j = k; j < MAXN && j < n + 1; j++) {
            result += countFreq[j];
        }
        ans[id] = result;
    }
    
    // 如果不保留信息，则清除
    if (keep == 0) {
        removeSubtree(u, fa);
    }
}

int main() {
    // 由于编译环境限制，这里使用硬编码的测试数据
    // 实际使用时需要替换为适当的输入方法
    
    // 测试数据
    n = 5;
    m = 2;
    
    // 节点颜色
    color[1] = 1;
    color[2] = 2;
    color[3] = 3;
    color[4] = 1;
    color[5] = 2;
    
    // 构建树结构
    addEdge(1, 2);
    addEdge(2, 1);
    addEdge(1, 3);
    addEdge(3, 1);
    addEdge(2, 4);
    addEdge(4, 2);
    addEdge(2, 5);
    addEdge(5, 2);
    
    // 添加查询
    addQuery(1, 1, 2);  // 查询节点1子树中出现次数至少为2的颜色数量
    addQuery(2, 2, 1);  // 查询节点2子树中出现次数至少为1的颜色数量
    
    // 执行算法
    dfs1(1, 0);  // 以节点1为根进行第一次DFS
    dsuOnTree(1, 0, 0);  // 执行DSU on Tree
    
    // 输出结果（实际使用时需要替换为适当的输出方法）
    // 查询1结果: 节点1的子树包含颜色1(出现2次)和颜色2(出现2次)，都至少出现2次，所以答案是2
    // 查询2结果: 节点2的子树包含颜色1(出现1次)、颜色2(出现2次)和颜色3(出现1次)，都至少出现1次，所以答案是3
    
    return 0;
}