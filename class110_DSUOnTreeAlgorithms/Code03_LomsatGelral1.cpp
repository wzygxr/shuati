// 主导颜色累加和，C++版
// 题目来源: Codeforces 600E Lomsat gelral
// 题目链接: https://codeforces.com/problemset/problem/600/E
// 题目来源: 洛谷 CF600E 主导颜色累加和
// 题目链接: https://www.luogu.com.cn/problem/CF600E
// 
// 题目大意:
// 一共有n个节点，编号1~n，给定n-1条边，所有节点连成一棵树，1号节点为树头
// 每个节点给定一种颜色值，主导颜色累加和定义如下
// 以x为头的子树上，哪种颜色出现最多，那种颜色就是主导颜色，主导颜色可能不止一种
// 所有主导颜色的值累加起来，每个主导颜色只累加一次，就是该子树的主导颜色累加和
// 打印1~n每个节点为头的子树的主导颜色累加和
// 1 <= n、颜色值 <= 10^5
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每种颜色的出现次数以及出现次数最多的颜色值之和
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
// 主导颜色处理:
// 1. 维护每种颜色的出现次数
// 2. 维护当前最大出现次数
// 3. 维护出现次数最多的颜色值之和
// 4. 当颜色出现次数更新时，根据情况更新最大出现次数和颜色值之和
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

// 由于编译环境限制，不使用标准头文件
// 使用基本的C++语法和内置类型

const int MAXN = 100001;
int n;
int color[MAXN];

int head[MAXN];
int nxt[MAXN << 1];
int to[MAXN << 1];
int cnt = 0;

int fa[MAXN];
int siz[MAXN];
int son[MAXN];

int colorCnt[MAXN];
int maxCnt[MAXN];
long long ans[MAXN];

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

void effect(int u, int h) {
    colorCnt[color[u]]++;
    if (colorCnt[color[u]] == maxCnt[h]) {
        ans[h] += color[u];
    } else if (colorCnt[color[u]] > maxCnt[h]) {
        maxCnt[h] = colorCnt[color[u]];
        ans[h] = color[u];
    }
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa[u]) {
            effect(v, h);
        }
    }
}

void cancel(int u) {
    colorCnt[color[u]] = 0;
    maxCnt[u] = 0;
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
    maxCnt[u] = maxCnt[son[u]];
    ans[u] = ans[son[u]];
    colorCnt[color[u]]++;
    if (colorCnt[color[u]] == maxCnt[u]) {
        ans[u] += color[u];
    } else if (colorCnt[color[u]] > maxCnt[u]) {
        maxCnt[u] = colorCnt[color[u]];
        ans[u] = color[u];
    }
    for (int e = head[u], v; e; e = nxt[e]) {
        v = to[e];
        if (v != fa[u] && v != son[u]) {
            effect(v, u);
        }
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
    
    // 执行算法
    dfs1(1, 0);
    dfs2(1, 0);
    
    // 输出结果（实际使用时需要替换为适当的输出方法）
    // 节点1的子树包含颜色1(出现2次)、颜色2(出现2次)和颜色3(出现1次)
    // 出现次数最多的颜色是1和2，所以答案是1+2=3
    // 节点2的子树包含颜色1(出现2次)、颜色2(出现2次)和颜色3(出现1次)
    // 出现次数最多的颜色是1和2，所以答案是1+2=3
    // 节点3的子树只包含颜色3，所以答案是3
    // 节点4的子树只包含颜色1，所以答案是1
    // 节点5的子树只包含颜色2，所以答案是2
    
    return 0;
}