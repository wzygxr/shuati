// Lomsat gelral (Codeforces 600E) - 统计子树中出现次数最多的颜色值之和
// 题目来源: Codeforces 600E
// 链接: https://codeforces.com/problemset/problem/600/E

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

// 第一题：Lomsat gelral (Codeforces 600E)实现
#include <iostream>
using namespace std;

const int MAXN = 100005;

// Lomsat gelral - 变量定义
int n_lomsat;
int color_lomsat[MAXN];
int head_lomsat[MAXN], next_lomsat[MAXN << 1], to_lomsat[MAXN << 1], cnt_lomsat = 0;
int size_lomsat[MAXN];
int son_lomsat[MAXN];
int colorCount_lomsat[MAXN];
long long ans_lomsat[MAXN];
int maxFreq_lomsat = 0;
long long sumFreq_lomsat = 0;

// Lomsat gelral - 添加边
void addEdge_lomsat(int u, int v) {
    next_lomsat[++cnt_lomsat] = head_lomsat[u];
    to_lomsat[cnt_lomsat] = v;
    head_lomsat[u] = cnt_lomsat;
}

// Lomsat gelral - 第一次DFS
void dfs1_lomsat(int u, int fa) {
    size_lomsat[u] = 1;
    son_lomsat[u] = 0;
    
    for (int e = head_lomsat[u], v; e; e = next_lomsat[e]) {
        v = to_lomsat[e];
        if (v != fa) {
            dfs1_lomsat(v, u);
            size_lomsat[u] += size_lomsat[v];
            if (son_lomsat[u] == 0 || size_lomsat[son_lomsat[u]] < size_lomsat[v]) {
                son_lomsat[u] = v;
            }
        }
    }
}

// Lomsat gelral - 添加节点贡献
void addNode_lomsat(int u) {
    int c = color_lomsat[u];
    if (colorCount_lomsat[c] == maxFreq_lomsat) {
        sumFreq_lomsat += c;
    } else if (colorCount_lomsat[c] == maxFreq_lomsat + 1) {
        maxFreq_lomsat++;
        sumFreq_lomsat = c;
    }
    colorCount_lomsat[c]++;
}

// Lomsat gelral - 删除节点贡献
void removeNode_lomsat(int u) {
    int c = color_lomsat[u];
    if (colorCount_lomsat[c] == maxFreq_lomsat) {
        sumFreq_lomsat -= c;
        if (sumFreq_lomsat == 0) {
            maxFreq_lomsat--;
            sumFreq_lomsat = 0;
            for (int i = 1; i < MAXN; i++) {
                if (colorCount_lomsat[i] == maxFreq_lomsat) {
                    sumFreq_lomsat += i;
                }
            }
        }
    }
    colorCount_lomsat[c]--;
}

// Lomsat gelral - 添加子树贡献
void addSubtree_lomsat(int u, int fa) {
    addNode_lomsat(u);
    for (int e = head_lomsat[u], v; e; e = next_lomsat[e]) {
        v = to_lomsat[e];
        if (v != fa) {
            addSubtree_lomsat(v, u);
        }
    }
}

// Lomsat gelral - 删除子树贡献
void removeSubtree_lomsat(int u, int fa) {
    removeNode_lomsat(u);
    for (int e = head_lomsat[u], v; e; e = next_lomsat[e]) {
        v = to_lomsat[e];
        if (v != fa) {
            removeSubtree_lomsat(v, u);
        }
    }
}

// Lomsat gelral - DSU on Tree主过程
void dsuOnTree_lomsat(int u, int fa, int keep) {
    for (int e = head_lomsat[u], v; e; e = next_lomsat[e]) {
        v = to_lomsat[e];
        if (v != fa && v != son_lomsat[u]) {
            dsuOnTree_lomsat(v, u, 0);
        }
    }
    
    if (son_lomsat[u] != 0) {
        dsuOnTree_lomsat(son_lomsat[u], u, 1);
    }
    
    addNode_lomsat(u);
    
    for (int e = head_lomsat[u], v; e; e = next_lomsat[e]) {
        v = to_lomsat[e];
        if (v != fa && v != son_lomsat[u]) {
            addSubtree_lomsat(v, u);
        }
    }
    
    ans_lomsat[u] = sumFreq_lomsat;
    
    if (keep == 0) {
        removeSubtree_lomsat(u, fa);
        maxFreq_lomsat = 0;
        sumFreq_lomsat = 0;
    }
}

// Lomsat gelral - 测试函数
void testLomsat() {
    // 简单测试用例
    n_lomsat = 4;
    color_lomsat[1] = 1;
    color_lomsat[2] = 2;
    color_lomsat[3] = 3;
    color_lomsat[4] = 1;
    
    addEdge_lomsat(1, 2);
    addEdge_lomsat(2, 1);
    addEdge_lomsat(1, 3);
    addEdge_lomsat(3, 1);
    addEdge_lomsat(2, 4);
    addEdge_lomsat(4, 2);
    
    dfs1_lomsat(1, 0);
    dsuOnTree_lomsat(1, 0, 0);
    
    // 实际使用时应输出结果
}

// 第三题：Dominant Indices (Codeforces 1009F) - 统计子树中出现次数最多的深度值
// 题目来源: Codeforces 1009F
// 链接: https://codeforces.com/problemset/problem/1009/F

// Dominant Indices - 变量定义
int n_dominant;
int head_dominant[MAXN], next_dominant[MAXN << 1], to_dominant[MAXN << 1], cnt_dominant = 0;
int size_dominant[MAXN];
int son_dominant[MAXN];
int depth_dominant[MAXN];
int cntDepth_dominant[MAXN];
int maxFreq_dominant = 0;
int ans_dominant[MAXN];
int maxDepth_dominant = 0;

// Dominant Indices - 添加边
void addEdge_dominant(int u, int v) {
    next_dominant[++cnt_dominant] = head_dominant[u];
    to_dominant[cnt_dominant] = v;
    head_dominant[u] = cnt_dominant;
}

// Dominant Indices - 第一次DFS计算子树大小和重儿子
void dfs1_dominant(int u, int fa) {
    size_dominant[u] = 1;
    son_dominant[u] = 0;
    depth_dominant[u] = depth_dominant[fa] + 1;
    maxDepth_dominant = max(maxDepth_dominant, depth_dominant[u]);
    
    for (int e = head_dominant[u], v; e; e = next_dominant[e]) {
        v = to_dominant[e];
        if (v != fa) {
            dfs1_dominant(v, u);
            size_dominant[u] += size_dominant[v];
            if (son_dominant[u] == 0 || size_dominant[son_dominant[u]] < size_dominant[v]) {
                son_dominant[u] = v;
            }
        }
    }
}

// Dominant Indices - 添加节点贡献
void addNode_dominant(int u) {
    int d = depth_dominant[u];
    cntDepth_dominant[d]++;
    if (cntDepth_dominant[d] > maxFreq_dominant) {
        maxFreq_dominant = cntDepth_dominant[d];
    }
}

// Dominant Indices - 删除节点贡献
void removeNode_dominant(int u) {
    int d = depth_dominant[u];
    cntDepth_dominant[d]--;
}

// Dominant Indices - 添加子树贡献
void addSubtree_dominant(int u, int fa) {
    addNode_dominant(u);
    for (int e = head_dominant[u], v; e; e = next_dominant[e]) {
        v = to_dominant[e];
        if (v != fa) {
            addSubtree_dominant(v, u);
        }
    }
}

// Dominant Indices - 删除子树贡献
void removeSubtree_dominant(int u, int fa) {
    removeNode_dominant(u);
    for (int e = head_dominant[u], v; e; e = next_dominant[e]) {
        v = to_dominant[e];
        if (v != fa) {
            removeSubtree_dominant(v, u);
        }
    }
}

// Dominant Indices - DSU on Tree主过程
void dsuOnTree_dominant(int u, int fa, int keep) {
    for (int e = head_dominant[u], v; e; e = next_dominant[e]) {
        v = to_dominant[e];
        if (v != fa && v != son_dominant[u]) {
            dsuOnTree_dominant(v, u, 0);
        }
    }
    
    if (son_dominant[u] != 0) {
        dsuOnTree_dominant(son_dominant[u], u, 1);
    }
    
    addNode_dominant(u);
    
    for (int e = head_dominant[u], v; e; e = next_dominant[e]) {
        v = to_dominant[e];
        if (v != fa && v != son_dominant[u]) {
            addSubtree_dominant(v, u);
        }
    }
    
    // 找到出现次数最多的最小深度
    for (int d = depth_dominant[u]; d <= maxDepth_dominant; d++) {
        if (cntDepth_dominant[d] == maxFreq_dominant) {
            ans_dominant[u] = d - depth_dominant[u];
            break;
        }
    }
    
    if (keep == 0) {
        removeSubtree_dominant(u, fa);
        maxFreq_dominant = 0;
    }
}

// Dominant Indices - 测试函数
void testDominant() {
    // 简单测试用例
    n_dominant = 3;
    addEdge_dominant(1, 2);
    addEdge_dominant(2, 1);
    addEdge_dominant(1, 3);
    addEdge_dominant(3, 1);
    
    depth_dominant[0] = -1; // 根节点深度为0
    dfs1_dominant(1, 0);
    dsuOnTree_dominant(1, 0, 0);
    
    // 实际使用时应输出结果
}
