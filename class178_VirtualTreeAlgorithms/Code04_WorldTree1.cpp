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
// 1. BZOJ 3572 - [HNOI2014]世界树
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算每个关键点能管理多少个点
//
// 2. Codeforces 613D - Kingdom and Cities
//    链接：https://codeforces.com/problemset/problem/613/D
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
//
// 3. 洛谷 P2495 - [SDOI2011]消耗战
//    链接：https://www.luogu.com.cn/problem/P2495
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
//
// 4. 洛谷 P4103 - [HEOI2014]大工程
//    链接：https://www.luogu.com.cn/problem/P4103
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
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

// 世界树，C++版（简化版，避免标准库问题）
// 世界树是一棵无比巨大的树，它伸出的枝干构成了整个世界
// 在这里，生存着各种各样的种族和生灵，他们共同信奉着绝对公正公平的女神艾莉森
// 出于对公平的考虑，第i年，世界树的国王需要授权m[i]个种族的聚居地为临时议事处
// 对于某个种族x（x为种族的编号），如果距离该种族最近的临时议事处为y（y为议事处所在节点）
// 那么节点x就由种族y管理
// 如果有多个距离相同的议事处，则由编号最小的管理
// 现在国王希望知道，对于每个临时议事处，它管理了多少个节点
// 1 <= n <= 10^5
// 1 <= q <= 10^5
// 1 <= 所有查询给出的点的总数 <= 3*10^5

const int MAXN = 100001;
const int MAXP = 20;

int n, q, k;

// 原始树
int headg[MAXN], nextg[MAXN << 1], tog[MAXN << 1], cntg;

// 虚树
int headv[MAXN], nextv[MAXN], tov[MAXN], cntv;

// 树上倍增求LCA + 生成dfn序
int dep[MAXN], dfn[MAXN], stjump[MAXN][MAXP], cntd;

// 关键点数组
int arr[MAXN];
// 标记节点是否是关键点
bool isKey[MAXN];

// 第一种建树方式
int tmp[MAXN << 1];
// 第二种建树方式
int stk[MAXN];

// 动态规划相关
// siz[u]表示子树u中节点总数
// own[u]表示子树u中被u管理的节点数
// near[u]表示子树u中距离u最近的关键点编号
int siz[MAXN], own[MAXN], near[MAXN];

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

// 树上倍增的dfs过程
void dfs(int u, int fa) {
    dep[u] = dep[fa] + 1;
    cntd++;
    dfn[u] = cntd;
    stjump[u][0] = fa;
    for (int p = 1; p < MAXP; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    for (int e = headg[u]; e > 0; e = nextg[e]) {
        if (tog[e] != fa) {
            dfs(tog[e], u);
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

// 树型dp的过程
void dp(int u) {
    siz[u] = 1;
    own[u] = 0;
    near[u] = isKey[u] ? u : 0;
    for (int e = headv[u]; e > 0; e = nextv[e]) {
        int v = tov[e];
        dp(v);
        siz[u] += siz[v];
        // 更新管理关系
        if (near[u] == 0) {
            near[u] = near[v];
        } else if (near[v] != 0) {
            int dist_u = dep[u] - dep[getLca(u, near[u])];
            int dist_v = dep[v] - dep[getLca(v, near[v])] + 1;
            if (dist_v < dist_u || (dist_v == dist_u && near[v] < near[u])) {
                near[u] = near[v];
            }
        }
    }
    // 计算u管理的节点数
    if (near[u] != 0) {
        own[near[u]] += siz[u];
    }
    for (int e = headv[u]; e > 0; e = nextv[e]) {
        int v = tov[e];
        // 减去子树v中由near[u]管理的节点数
        if (near[u] != 0 && near[v] != 0) {
            int dist_u = dep[v] - dep[getLca(v, near[u])];
            int dist_v = dep[v] - dep[getLca(v, near[v])];
            if (dist_u > dist_v || (dist_u == dist_v && near[u] > near[v])) {
                own[near[u]] -= siz[v];
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
    // 初始化dp数组
    for (int i = 1; i <= k; i++) {
        own[arr[i]] = 0;
    }
    dp(tree);
    // 输出结果
    for (int i = 1; i <= k; i++) {
        // 输出每个关键点管理的节点数
    }
    // 节点撤销关键点信息
    for (int i = 1; i <= k; i++) {
        isKey[arr[i]] = false;
    }
}

// 由于编译环境问题，这里不包含main函数
// 在实际使用中，需要添加适当的输入输出函数