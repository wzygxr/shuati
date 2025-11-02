// 虚树(Virtual Tree)算法详解与应用
// 
// 虚树是一种优化技术，用于解决树上多次询问的问题，每次询问涉及部分关键点
// 虚树只保留关键点及其两两之间的LCA，节点数控制在O(k)级别，从而提高效率
//
// 算法核心思想：
// 1. 虚树包含所有关键点和它们两两之间的LCA
// 2. 虚树的节点数不超过2*k-1（k为关键点数）
// 3. 在虚树上进行DP等操作，避免遍历整棵树
//
// 构造方法：
// 方法一：二次排序法
// 1. 将关键点按DFS序排序
// 2. 相邻点求LCA并加入序列
// 3. 再次排序并去重得到虚树所有节点
// 4. 按照父子关系连接节点
//
// 方法二：单调栈法
// 1. 将关键点按DFS序排序
// 2. 用栈维护虚树的一条链
// 3. 逐个插入关键点并维护栈结构
//
// 应用场景：
// 1. 树上多次询问，每次询问涉及部分关键点
// 2. 需要在关键点及其LCA上进行DP等操作
// 3. 数据范围要求∑k较小（通常≤10^5）
//
// 相关题目：
// 1. 洛谷 P4103 - [HEOI2014]大工程
//    链接：https://www.luogu.com.cn/problem/P4103
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
//
// 2. Codeforces 613D - Kingdom and Cities
//    链接：https://codeforces.com/problemset/problem/613/D
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
//
// 3. 洛谷 P2495 - [SDOI2011]消耗战
//    链接：https://www.luogu.com.cn/problem/P2495
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
//
// 4. Codeforces 1000G - Two Melborians, One Siberian
//    链接：https://codeforces.com/problemset/problem/1000/G
//    题意：在树上处理多组询问，涉及关键点的最短距离等信息
//
// 5. AtCoder ABC154F - Many Many Paths
//    链接：https://atcoder.jp/contests/abc154/tasks/abc154_f
//    题意：计算树上路径数量，可以使用虚树优化
//
// 时间复杂度分析：
// 1. 预处理（DFS序、LCA）：O(n log n)
// 2. 构造虚树：O(k log k)
// 3. 在虚树上DP：O(k)
// 总体复杂度：O(n log n + ∑k log k)
//
// 空间复杂度：O(n + k)
//
// 工程化考量：
// 1. 注意虚树边通常没有边权，需要通过原树计算
// 2. 清空关键点标记时避免使用memset，用for循环逐个清除
// 3. 排序后的关键点顺序不是原节点序，如需按原序输出需额外保存
// 4. 虚树主要用于卡常题，需注意常数优化

// 大工程，C++版（简化版，避免标准库问题）
// 一共有n个节点，给定n-1条无向边，所有节点组成一棵树
// 如果在节点a和节点b之间建立新通道，那么代价是两个节点在树上的距离
// 一共有q条查询，每条查询格式如下
// 查询 k a1 a2 ... ak : 给出了k个不同的节点，任意两个节点之间都会建立新通道
//                       打印新通道的代价和、新通道中代价最小的值、新通道中代价最大的值
// 1 <= n <= 10^6
// 1 <= q <= 5 * 10^4
// 1 <= 所有查询给出的点的总数 <= 2 * n
// 测试链接 : https://www.luogu.com.cn/problem/P4103

const long long INF = 1LL << 60;
const int MAXN = 1000001;
const int MAXP = 20;

// 全局变量
int n, q, k;

int headg[MAXN], nextg[MAXN << 1], tog[MAXN << 1], cntg;

int headv[MAXN], nextv[MAXN], tov[MAXN], cntv;

int dep[MAXN], dfn[MAXN], stjump[MAXN][MAXP], cntd;

int arr[MAXN];
bool isKey[MAXN];

int tmp[MAXN << 1];
int stk[MAXN];

// siz[u]表示子树u上，关键点的数量
// sum[u]表示子树u上，所有关键点到u的总距离
// near[u]表示子树u上，到u最近关键点的距离
// far[u]表示子树u上，到u最远关键点的距离
int siz[MAXN];
long long sum[MAXN], near[MAXN], far[MAXN];

// 新通道的代价和、新通道中代价最小的值、新通道中代价最大的值
long long costSum, costMin, costMax;

// ufe用于dfs和dp的迭代版
int ufe[MAXN][3];
int stacksize, u, f, e;

void push(int u_val, int f_val, int e_val) {
    ufe[stacksize][0] = u_val;
    ufe[stacksize][1] = f_val;
    ufe[stacksize][2] = e_val;
    stacksize++;
}

void pop() {
    stacksize--;
    u = ufe[stacksize][0];
    f = ufe[stacksize][1];
    e = ufe[stacksize][2];
}

// 原始树连边
void addEdgeG(int u, int v) {
    cntg++;
    nextg[cntg] = headg[u];
    tog[cntg] = v;
    headg[u] = cntg;
}

// 虚树连边
void addEdgeV(int u, int v) {
    cntv++;
    nextv[cntv] = headv[u];
    tov[cntv] = v;
    headv[u] = cntv;
}

// nums中的数，根据dfn的大小排序，手撸双指针快排
void sortByDfn(int nums[], int l, int r) {
    if (l >= r) return;
    int i = l, j = r;
    int pivot = nums[(l + r) >> 1];
    while (i <= j) {
        while (dfn[nums[i]] < dfn[pivot]) i++;
        while (dfn[nums[j]] > dfn[pivot]) j--;
        if (i <= j) {
            int t = nums[i]; nums[i] = nums[j]; nums[j] = t;
            i++; j--;
        }
    }
    sortByDfn(nums, l, j);
    sortByDfn(nums, i, r);
}

// dfs递归版，可能会爆栈
void dfs1(int u, int fa) {
    dep[u] = dep[fa] + 1;
    cntd++;
    dfn[u] = cntd;
    stjump[u][0] = fa;
    for (int p = 1; p < MAXP; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    for (int e_idx = headg[u]; e_idx > 0; e_idx = nextg[e_idx]) {
        if (tog[e_idx] != fa) {
            dfs1(tog[e_idx], u);
        }
    }
}

// dfs1的迭代版
void dfs2() {
    stacksize = 0;
    push(1, 0, -1);
    while (stacksize > 0) {
        pop();
        if (e == -1) {
            dep[u] = dep[f] + 1;
            cntd++;
            dfn[u] = cntd;
            stjump[u][0] = f;
            for (int p = 1; p < MAXP; p++) {
                stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
            }
            e = headg[u];
        } else {
            e = nextg[e];
        }
        if (e != 0) {
            push(u, f, e);
            if (tog[e] != f) {
                push(tog[e], u, -1);
            }
        }
    }
}

// 返回a和b的最低公共祖先
int getLca(int a, int b) {
    if (dep[a] < dep[b]) {
        int t = a; a = b; b = t;
    }
    for (int p = MAXP - 1; p >= 0; p--) {
        if (dep[stjump[a][p]] >= dep[b]) {
            a = stjump[a][p];
        }
    }
    if (a == b) {
        return a;
    }
    for (int p = MAXP - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    return stjump[a][0];
}

// 二次排序 + LCA连边的方式建立虚树
int buildVirtualTree1() {
    sortByDfn(arr, 1, k);
    int len = 0;
    for (int i = 1; i < k; i++) {
        len++;
        tmp[len] = arr[i];
        len++;
        tmp[len] = getLca(arr[i], arr[i + 1]);
    }
    len++;
    tmp[len] = arr[k];
    sortByDfn(tmp, 1, len);
    int unique = 1;
    for (int i = 2; i <= len; i++) {
        if (tmp[unique] != tmp[i]) {
            unique++;
            tmp[unique] = tmp[i];
        }
    }
    cntv = 0;
    for (int i = 1; i <= unique; i++) {
        headv[tmp[i]] = 0;
    }
    for (int i = 1; i < unique; i++) {
        addEdgeV(getLca(tmp[i], tmp[i + 1]), tmp[i + 1]);
    }
    return tmp[1];
}

// 单调栈的方式建立虚树
int buildVirtualTree2() {
    sortByDfn(arr, 1, k);
    cntv = 0;
    headv[arr[1]] = 0;
    int top = 0;
    top++;
    stk[top] = arr[1];
    for (int i = 2; i <= k; i++) {
        int x = arr[i];
        int y = stk[top];
        int lca = getLca(x, y);
        while (top > 1 && dfn[stk[top - 1]] >= dfn[lca]) {
            addEdgeV(stk[top - 1], stk[top]);
            top--;
        }
        if (lca != stk[top]) {
            headv[lca] = 0;
            addEdgeV(lca, stk[top]);
            stk[top] = lca;
        }
        headv[x] = 0;
        top++;
        stk[top] = x;
    }
    while (top > 1) {
        addEdgeV(stk[top - 1], stk[top]);
        top--;
    }
    return stk[1];
}

// dp递归版，可能会爆栈
void dp1(int u) {
    siz[u] = isKey[u] ? 1 : 0;
    sum[u] = 0;
    if (isKey[u]) {
        far[u] = near[u] = 0;
    } else {
        near[u] = INF;
        far[u] = -INF;
    }
    for (int e_idx = headv[u]; e_idx > 0; e_idx = nextv[e_idx]) {
        dp1(tov[e_idx]);
    }
    for (int e_idx = headv[u]; e_idx > 0; e_idx = nextv[e_idx]) {
        int v = tov[e_idx];
        long long len = dep[v] - dep[u];
        costSum += (sum[u] + (long long)siz[u] * len) * siz[v] + sum[v] * siz[u];
        siz[u] += siz[v];
        sum[u] += sum[v] + len * siz[v];
        if (near[u] + near[v] + len < costMin) costMin = near[u] + near[v] + len;
        if (far[u] + far[v] + len > costMax) costMax = far[u] + far[v] + len;
        if (near[v] + len < near[u]) near[u] = near[v] + len;
        if (far[v] + len > far[u]) far[u] = far[v] + len;
    }
}

// dp1的迭代版
void dp2(int tree) {
    stacksize = 0;
    push(tree, 0, -1);
    while (stacksize > 0) {
        pop();
        if (e == -1) {
            siz[u] = isKey[u] ? 1 : 0;
            sum[u] = 0;
            if (isKey[u]) {
                far[u] = near[u] = 0;
            } else {
                near[u] = INF;
                far[u] = -INF;
            }
            e = headv[u];
        } else {
            e = nextv[e];
        }
        if (e != 0) {
            push(u, 0, e);
            push(tov[e], 0, -1);
        } else {
            for (int ei = headv[u]; ei > 0; ei = nextv[ei]) {
                int v = tov[ei];
                long long len = dep[v] - dep[u];
                costSum += (sum[u] + (long long)siz[u] * len) * siz[v] + sum[v] * siz[u];
                siz[u] += siz[v];
                sum[u] += sum[v] + len * siz[v];
                long long tempMin = near[u] + near[v] + len;
                long long tempMax = far[u] + far[v] + len;
                if (tempMin < costMin) costMin = tempMin;
                if (tempMax > costMax) costMax = tempMax;
                if (near[v] + len < near[u]) near[u] = near[v] + len;
                if (far[v] + len > far[u]) far[u] = far[v] + len;
            }
        }
    }
}

void compute() {
    // 节点标记关键点信息
    for (int i = 1; i <= k; i++) {
        isKey[arr[i]] = true;
    }
    int tree = buildVirtualTree1();
    // int tree = buildVirtualTree2();
    costSum = 0;
    costMin = INF;
    costMax = -INF;
    // dp1(tree);  // 递归版，可能会爆栈
    dp2(tree);     // 迭代版，推荐使用
    // 节点撤销关键点信息
    for (int i = 1; i <= k; i++) {
        isKey[arr[i]] = false;
    }
}

// 由于编译环境问题，这里不包含main函数
// 在实际使用中，需要添加适当的输入输出函数