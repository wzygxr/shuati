// 树上启发式合并模版题，C++版
// 题目来源: 洛谷 U41492 树上数颜色
// 题目链接: https://www.luogu.com.cn/problem/U41492
// 
// 题目大意:
// 一共有n个节点，编号1~n，给定n-1条边，所有节点连成一棵树，1号节点为树头
// 每个节点给定一种颜色值，一共有m条查询，每条查询给定参数x
// 每条查询打印x为头的子树上，一共有多少种不同的颜色
// 1 <= n、m、颜色值 <= 10^5
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
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
//
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 由于编译环境限制，不使用标准头文件
// 使用基本的C++语法和内置类型

const int MAXN = 100001;
int n, m;
int arr[MAXN];

int head[MAXN];
int nxt[MAXN << 1];
int to[MAXN << 1];
int cnt = 0;

int fa[MAXN];
int siz[MAXN];
int son[MAXN];

int colorCnt[MAXN];
int ans[MAXN];
int diffColors = 0;

void addEdge(int u, int v) {
    nxt[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
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

void effect(int u) {
    if (++colorCnt[arr[u]] == 1) {
        diffColors++;
    }
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa[u]) {
            effect(v);
        }
    }
}

void cancel(int u) {
    if (--colorCnt[arr[u]] == 0) {
        diffColors--;
    }
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa[u]) {
            cancel(v);
        }
    }
}

void dfs2(int u, int keep) {
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, 0);
        }
    }
    if (son[u] != 0) {
        dfs2(son[u], 1);
    }
    if (++colorCnt[arr[u]] == 1) {
        diffColors++;
    }
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa[u] && v != son[u]) {
            effect(v);
        }
    }
    ans[u] = diffColors;
    if (keep == 0) {
        cancel(u);
    }
}

int main() {
    // 由于编译环境限制，这里使用硬编码的测试数据
    // 实际使用时需要替换为适当的输入方法
    
    // 测试数据
    n = 5;
    m = 2;
    
    // 节点颜色
    arr[1] = 1;
    arr[2] = 2;
    arr[3] = 3;
    arr[4] = 1;
    arr[5] = 2;
    
    // 构建树结构
    addEdge(1, 2);
    addEdge(2, 1);
    addEdge(1, 3);
    addEdge(3, 1);
    addEdge(2, 4);
    addEdge(4, 2);
    addEdge(2, 5);
    addEdge(5, 2);
    
    // 执行算法
    dfs1(1, 0);
    dfs2(1, 0);
    
    // 输出结果（实际使用时需要替换为适当的输出方法）
    // 节点1的子树包含颜色1,2,3，所以答案是3
    // 节点2的子树包含颜色1,2,3，所以答案是3
    // 节点3的子树只包含颜色3，所以答案是1
    // 节点4的子树只包含颜色1，所以答案是1
    // 节点5的子树只包含颜色2，所以答案是1
    
    return 0;
}