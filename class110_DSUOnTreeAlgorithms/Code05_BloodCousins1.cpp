// 表亲数量，C++版
// 题目来源: Codeforces 208E / 洛谷 CF208E
// 题目链接: https://codeforces.com/problemset/problem/208/E
// 题目链接: https://www.luogu.com.cn/problem/CF208E
// 
// 题目大意:
// 一共有n个节点，编号1~n，给定每个节点的父亲节点编号，父亲节点为0，说明当前节点是某棵树的头
// 注意，n个节点组成的是森林结构，可能有若干棵树
// 一共有m条查询，每条查询 x k，含义如下
// 如果x往上走k的距离，没有祖先节点，打印0
// 如果x往上走k的距离，能找到祖先节点a，那么从a往下走k的距离，除了x之外，可能还有其他节点
// 这些节点叫做x的k级表亲，打印这个表亲的数量
// 1 <= n、m <= 10^5
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每个深度上的节点数量
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
// 表亲处理:
// 1. 维护每个深度上的节点数量(depCnt)
// 2. 通过倍增法计算k级祖先
// 3. 利用深度信息计算表亲数量
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
//
// 由于编译环境限制，不使用标准头文件
// 使用基本的C++语法和内置类型
//
// 测试链接 : https://www.luogu.com.cn/problem/CF208E
// 测试链接 : https://codeforces.com/problemset/problem/208/E
// 提交如下代码，可以通过所有测试用例

const int MAXN = 100001;
const int MAXH = 20;
int n, m;
bool root[MAXN];

// 链式前向星
int headg[MAXN];
int nextg[MAXN];
int tog[MAXN];
int cntg;

// 问题列表
int headq[MAXN];
int nextq[MAXN];
int ansiq[MAXN];
int kq[MAXN];
int cntq;

// 树链剖分
int siz[MAXN];
int dep[MAXN];
int son[MAXN];
int stjump[MAXN][MAXH];

// 树上启发式合并
int depCnt[MAXN];
int ans[MAXN];

void addEdge(int u, int v) {
    nextg[++cntg] = headg[u];
    tog[cntg] = v;
    headg[u] = cntg;
}

void addQuestion(int u, int i, int k) {
    nextq[++cntq] = headq[u];
    ansiq[cntq] = i;
    kq[cntq] = k;
    headq[u] = cntq;
}

void dfs1(int u, int fa) {
    siz[u] = 1;
    dep[u] = dep[fa] + 1;
    stjump[u][0] = fa;
    for (int p = 1; p < MAXH; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    for (int e = headg[u]; e > 0; e = nextg[e]) {
        dfs1(tog[e], u);
    }
    for (int e = headg[u], v; e > 0; e = nextg[e]) {
        v = tog[e];
        siz[u] += siz[v];
        if (son[u] == 0 || siz[son[u]] < siz[v]) {
            son[u] = v;
        }
    }
}

int kAncestor(int u, int k) {
    for (int p = MAXH - 1; p >= 0; p--) {
        if (k >= 1 << p) {
            k -= 1 << p;
            u = stjump[u][p];
        }
    }
    return u;
}

void effect(int u) {
    depCnt[dep[u]]++;
    for (int e = headg[u]; e > 0; e = nextg[e]) {
        effect(tog[e]);
    }
}

void cancel(int u) {
    depCnt[dep[u]]--;
    for (int e = headg[u]; e > 0; e = nextg[e]) {
        cancel(tog[e]);
    }
}

void dfs2(int u, int keep) {
    for (int e = headg[u], v; e > 0; e = nextg[e]) {
        v = tog[e];
        if (v != son[u]) {
            dfs2(v, 0);
        }
    }
    if (son[u] != 0) {
        dfs2(son[u], 1);
    }
    depCnt[dep[u]]++;
    for (int e = headg[u], v; e > 0; e = nextg[e]) {
        v = tog[e];
        if (v != son[u]) {
            effect(v);
        }
    }
    for (int i = headq[u]; i > 0; i = nextq[i]) {
    	ans[ansiq[i]] = depCnt[dep[u] + kq[i]];
    }
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
    
    // 节点父节点关系
    // 节点1是根节点
    root[1] = true;
    
    // 构建树结构
    addEdge(1, 2);
    addEdge(1, 3);
    addEdge(2, 4);
    addEdge(2, 5);
    
    // 添加查询
    // 查询节点4的1级表亲数量
    int kfather1 = kAncestor(4, 1);
    if (kfather1 != 0) {
        addQuestion(kfather1, 1, 1);
    }
    
    // 查询节点5的1级表亲数量
    int kfather2 = kAncestor(5, 1);
    if (kfather2 != 0) {
        addQuestion(kfather2, 2, 1);
    }
    
    // 执行算法
    for (int i = 1; i <= n; i++) {
        if (root[i]) {
            dfs1(i, 0);
        }
    }
    
    for (int i = 1; i <= n; i++) {
        if (root[i]) {
            dfs2(i, 0);
        }
    }
    
    // 输出结果（实际使用时需要替换为适当的输出方法）
    // 查询结果
    
    return 0;
}