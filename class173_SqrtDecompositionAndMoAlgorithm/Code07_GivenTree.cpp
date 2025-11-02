// 给你一棵树问题 - 分块算法优化动态规划 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/CF1039D
// 题目来源: https://codeforces.com/problemset/problem/1039/D
// 题目大意: 一共有n个节点，给定n-1条边，所有节点连成一棵树
// 树的路径是指，从端点x到端点y的简单路径，k路径是指，路径的节点数正好为k
// 整棵树希望分解成尽量多的k路径，k路径的节点不能复用，所有k路径不要求包含所有点
// 打印k = 1, 2, 3..n时，k路径有最多有几条
// 约束条件: 1 <= n <= 200000

#include <cstdio>
#include <cmath>
#include <algorithm>
using namespace std;

const int MAXN = 200001;
int n, blen;
int head[MAXN];
int nxt[MAXN << 1];
int to[MAXN << 1];
int cntg = 0;

int fa[MAXN];
int dfnOrder[MAXN];
int cntd = 0;

int len[MAXN];
int max1[MAXN];
int max2[MAXN];

int ans[MAXN];

/**
 * 添加边到邻接表
 * @param u 节点u
 * @param v 节点v
 */
void addEdge(int u, int v) {
    // 添加u->v的边
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
    
    // 添加v->u的边
    nxt[++cntg] = head[v];
    to[cntg] = u;
    head[v] = cntg;
}

/**
 * DFS遍历树，生成dfn序
 * @param u 当前节点
 * @param f 父节点
 */
void dfs(int u, int f) {
    // 记录父节点
    fa[u] = f;
    
    // 记录dfn序
    dfnOrder[++cntd] = u;
    
    // 遍历所有子节点
    for (int e = head[u]; e; e = nxt[e]) {
        // 避免回到父节点
        if (to[e] != f) {
            dfs(to[e], u);
        }
    }
}

/**
 * 查询当路径长度为k时，最多能分解成几条路径
 * @param k 路径长度
 * @return 最多路径数
 */
int query(int k) {
    int cnt = 0;
    
    // 按照dfn序的逆序处理节点
    for (int i = n, cur, father; i >= 1; i--) {
        cur = dfnOrder[i];
        father = fa[cur];
        
        // 如果当前节点的最长链和次长链之和+1 >= k
        // 说明可以形成一条长度为k的路径
        if (max1[cur] + max2[cur] + 1 >= k) {
            cnt++; // 路径数+1
            len[cur] = 0; // 当前节点的最长链长度重置为0
        } else {
            // 否则更新当前节点的最长链长度
            len[cur] = max1[cur] + 1;
        }
        
        // 更新父节点的最长链和次长链
        if (len[cur] > max1[father]) {
            max2[father] = max1[father];
            max1[father] = len[cur];
        } else if (len[cur] > max2[father]) {
            max2[father] = len[cur];
        }
    }
    
    // 重置数组
    for (int i = 1; i <= n; i++) {
        len[i] = max1[i] = max2[i] = 0;
    }
    return cnt;
}

/**
 * 跳跃函数，用于优化计算
 * @param l 左边界
 * @param r 右边界
 * @param curAns 当前答案
 * @return 下一个需要计算的位置
 */
int jump(int l, int r, int curAns) {
    int find = l;
    while (l <= r) {
        int mid = (l + r) >> 1;
        int check = query(mid);
        
        if (check < curAns) {
            r = mid - 1;
        } else if (check > curAns) {
            l = mid + 1;
        } else {
            find = mid;
            l = mid + 1;
        }
    }
    return find + 1;
}

/**
 * 计算所有答案
 */
void compute() {
    // 对于k <= sqrt(n)的情况，直接计算
    for (int i = 1; i <= blen; i++) {
        ans[i] = query(i);
    }
    
    // 对于k > sqrt(n)的情况，使用跳跃优化
    for (int i = blen + 1; i <= n; i = jump(i, n, ans[i])) {
        ans[i] = query(i);
    }
}

/**
 * 预处理函数
 */
void prepare() {
    // 计算块大小，选择sqrt(n * log2(n))以优化性能
    blen = max(1, (int)sqrt((double)(n * log2(n))));
    
    // 初始化答案数组为-1，表示未计算
    fill(ans + 1, ans + n + 1, -1);
}

int main() {
    // 读取节点数量n
    scanf("%d", &n);
    
    // 读取n-1条边
    for (int i = 1, u, v; i < n; i++) {
        scanf("%d%d", &u, &v);
        addEdge(u, v);
    }
    
    // DFS生成dfn序
    dfs(1, 0);
    
    // 进行预处理
    prepare();
    
    // 计算所有答案
    compute();
    
    // 输出所有答案
    for (int i = 1; i <= n; i++) {
        // 如果答案未计算，则继承前一个答案
        if (ans[i] == -1) {
            ans[i] = ans[i - 1];
        }
        printf("%d\n", ans[i]);
    }
    return 0;
}// 给你一棵树问题 - 分块算法优化动态规划 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/CF1039D
// 题目来源: https://codeforces.com/problemset/problem/1039/D
// 题目大意: 一共有n个节点，给定n-1条边，所有节点连成一棵树
// 树的路径是指，从端点x到端点y的简单路径，k路径是指，路径的节点数正好为k
// 整棵树希望分解成尽量多的k路径，k路径的节点不能复用，所有k路径不要求包含所有点
// 打印k = 1, 2, 3..n时，k路径有最多有几条
// 约束条件: 1 <= n <= 200000

#include <cstdio>
#include <cmath>
#include <algorithm>
using namespace std;

const int MAXN = 200001;
int n, blen;
int head[MAXN];
int nxt[MAXN << 1];
int to[MAXN << 1];
int cntg = 0;

int fa[MAXN];
int dfnOrder[MAXN];
int cntd = 0;

int len[MAXN];
int max1[MAXN];
int max2[MAXN];

int ans[MAXN];

/**
 * 添加边到邻接表
 * @param u 节点u
 * @param v 节点v
 */
void addEdge(int u, int v) {
    // 添加u->v的边
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
    
    // 添加v->u的边
    nxt[++cntg] = head[v];
    to[cntg] = u;
    head[v] = cntg;
}

/**
 * DFS遍历树，生成dfn序
 * @param u 当前节点
 * @param f 父节点
 */
void dfs(int u, int f) {
    // 记录父节点
    fa[u] = f;
    
    // 记录dfn序
    dfnOrder[++cntd] = u;
    
    // 遍历所有子节点
    for (int e = head[u]; e; e = nxt[e]) {
        // 避免回到父节点
        if (to[e] != f) {
            dfs(to[e], u);
        }
    }
}

/**
 * 查询当路径长度为k时，最多能分解成几条路径
 * @param k 路径长度
 * @return 最多路径数
 */
int query(int k) {
    int cnt = 0;
    
    // 按照dfn序的逆序处理节点
    for (int i = n, cur, father; i >= 1; i--) {
        cur = dfnOrder[i];
        father = fa[cur];
        
        // 如果当前节点的最长链和次长链之和+1 >= k
        // 说明可以形成一条长度为k的路径
        if (max1[cur] + max2[cur] + 1 >= k) {
            cnt++; // 路径数+1
            len[cur] = 0; // 当前节点的最长链长度重置为0
        } else {
            // 否则更新当前节点的最长链长度
            len[cur] = max1[cur] + 1;
        }
        
        // 更新父节点的最长链和次长链
        if (len[cur] > max1[father]) {
            max2[father] = max1[father];
            max1[father] = len[cur];
        } else if (len[cur] > max2[father]) {
            max2[father] = len[cur];
        }
    }
    
    // 重置数组
    for (int i = 1; i <= n; i++) {
        len[i] = max1[i] = max2[i] = 0;
    }
    return cnt;
}

/**
 * 跳跃函数，用于优化计算
 * @param l 左边界
 * @param r 右边界
 * @param curAns 当前答案
 * @return 下一个需要计算的位置
 */
int jump(int l, int r, int curAns) {
    int find = l;
    while (l <= r) {
        int mid = (l + r) >> 1;
        int check = query(mid);
        
        if (check < curAns) {
            r = mid - 1;
        } else if (check > curAns) {
            l = mid + 1;
        } else {
            find = mid;
            l = mid + 1;
        }
    }
    return find + 1;
}

/**
 * 计算所有答案
 */
void compute() {
    // 对于k <= sqrt(n)的情况，直接计算
    for (int i = 1; i <= blen; i++) {
        ans[i] = query(i);
    }
    
    // 对于k > sqrt(n)的情况，使用跳跃优化
    for (int i = blen + 1; i <= n; i = jump(i, n, ans[i])) {
        ans[i] = query(i);
    }
}

/**
 * 预处理函数
 */
void prepare() {
    // 计算块大小，选择sqrt(n * log2(n))以优化性能
    blen = max(1, (int)sqrt((double)(n * log2(n))));
    
    // 初始化答案数组为-1，表示未计算
    fill(ans + 1, ans + n + 1, -1);
}

int main() {
    // 读取节点数量n
    scanf("%d", &n);
    
    // 读取n-1条边
    for (int i = 1, u, v; i < n; i++) {
        scanf("%d%d", &u, &v);
        addEdge(u, v);
    }
    
    // DFS生成dfn序
    dfs(1, 0);
    
    // 进行预处理
    prepare();
    
    // 计算所有答案
    compute();
    
    // 输出所有答案
    for (int i = 1; i <= n; i++) {
        // 如果答案未计算，则继承前一个答案
        if (ans[i] == -1) {
            ans[i] = ans[i - 1];
        }
        printf("%d\n", ans[i]);
    }
    return 0;
}