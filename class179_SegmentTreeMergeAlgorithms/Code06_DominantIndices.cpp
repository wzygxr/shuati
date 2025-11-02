// Dominant Indices - CF1009F
// 测试链接 : https://codeforces.com/contest/1009/problem/F

/**
 * Dominant Indices问题 - C++版本
 * 
 * 题目来源: Codeforces 1009F
 * 题目链接: https://codeforces.com/contest/1009/problem/F
 * 
 * 题目描述:
 * 给定一棵 n 个节点的树，根节点为1。对于每个节点 u，定义其深度数组为一个无限序列，
 * 其中第 d 项表示 u 的子树中深度为 d 的节点数量。求每个节点的深度数组中最大值的下标。
 * 如果有多个最大值，输出最小的下标。
 * 
 * 解题思路:
 * 1. 使用线段树合并技术解决树上统计问题
 * 2. 为每个节点建立一棵深度线段树，维护子树中各深度的节点数量
 * 3. 从叶子节点开始，自底向上合并子树的线段树
 * 4. 查询当前节点线段树中节点数量最多的深度
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)
 * - 空间复杂度: O(n log n)
 * 
 * 线段树合并核心思想:
 * 1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
 * 2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
 * 3. 合并过程类似于可并堆的合并方式
 */

// 为了解决编译问题，使用基本的C头文件
extern "C" {
    int scanf(const char*, ...);
    int printf(const char*, ...);
}

const int MAXN = 1000001;
const int MAXT = MAXN * 2;  // 注意空间，因为每个节点最多log个节点

int n;

// 邻接表存储树
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cntg;

// 线段树相关数组
int ls[MAXT];     // 左子节点
int rs[MAXT];     // 右子节点
int maxDep[MAXT]; // 最大深度
int maxCnt[MAXT]; // 最大深度的节点数
int cntt;

// 答案数组
int ans[MAXN];

/**
 * 添加边到邻接表
 * @param u 起点
 * @param v 终点
 */
void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

/**
 * 更新节点信息
 * @param i 节点索引
 */
void up(int i) {
    // 左子树信息更深一层，所以要比较右子树和左子树+1
    if (maxCnt[rs[i]] > maxCnt[ls[i]]) {
        maxCnt[i] = maxCnt[rs[i]];
        maxDep[i] = maxDep[rs[i]];
    } else {
        maxCnt[i] = maxCnt[ls[i]];
        maxDep[i] = maxDep[ls[i]] + 1;
    }
}

/**
 * 创建新节点
 * @return 新节点索引
 */
int newNode() {
    ++cntt;
    ls[cntt] = rs[cntt] = maxDep[cntt] = maxCnt[cntt] = 0;
    return cntt;
}

/**
 * 在深度d处增加计数
 * @param d 深度
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点索引
 * @return 更新后的节点索引
 */
int add(int d, int l, int r, int i) {
    int rt = i;
    if (rt == 0) {
        rt = newNode(); // 动态开点
    }
    if (l == r) {
        maxCnt[rt]++;   // 叶子节点计数加1
        maxDep[rt] = l; // 更新最大深度
    } else {
        int mid = (l + r) >> 1;
        if (d <= mid) {
            ls[rt] = add(d, l, mid, ls[rt]); // 递归更新左子树
        } else {
            rs[rt] = add(d, mid + 1, r, rs[rt]); // 递归更新右子树
        }
        up(rt); // 更新当前节点信息
    }
    return rt;
}

/**
 * 合并两棵线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @param t1 第一棵线段树根节点
 * @param t2 第二棵线段树根节点
 * @return 合并后的线段树根节点
 */
int merge(int l, int r, int t1, int t2) {
    // 边界条件：如果其中一个节点为空，返回另一个节点
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    // 叶子节点：合并节点信息
    if (l == r) {
        maxCnt[t1] += maxCnt[t2]; // 累加计数
        maxDep[t1] = l;           // 更新最大深度
    } else {
        // 递归合并左右子树
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1); // 更新当前节点信息
    }
    return t1;
}

// DFS遍历树并计算答案
int root[MAXN];

/**
 * DFS遍历树并计算答案
 * @param u 当前节点
 * @param fa 父节点
 */
void dfs(int u, int fa) {
    // 先递归处理所有子节点
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != fa) {
            dfs(v, u);
        }
    }

    // 将所有子节点的线段树合并到当前节点
    root[u] = newNode();      // 创建当前节点的线段树根节点
    maxCnt[root[u]] = 1;      // 当前节点自身贡献1个深度为0的节点
    maxDep[root[u]] = 0;      // 当前节点深度为0
    
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != fa) {
            root[u] = merge(0, n, root[u], root[v]); // 合并子节点线段树
        }
    }
    
    // 当前节点的答案就是最大深度
    ans[u] = maxDep[root[u]];
}

// 由于环境限制，使用基本的输入输出方式
int main() {
    // 读取节点数
    scanf("%d", &n);
    
    // 读取边信息
    for (int i = 1, u, v; i < n; i++) {
        scanf("%d%d", &u, &v);
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // DFS计算答案
    dfs(1, 0);
    
    // 输出结果
    for (int i = 1; i <= n; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}