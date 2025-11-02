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
// 1. Codeforces 613D - Kingdom and Cities
//    链接：https://codeforces.com/problemset/problem/613/D
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
//
// 2. 洛谷 P2495 - [SDOI2011]消耗战
//    链接：https://www.luogu.com.cn/problem/P2495
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
//
// 3. 洛谷 P4103 - [HEOI2014]大工程
//    链接：https://www.luogu.com.cn/problem/P4103
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
//
// 4. Codeforces 1000G - Two Melborians, One Siberian
//    链接：https://codeforces.com/problemset/problem/1000/G
//    题意：在树上处理多组询问，涉及关键点的最短距离等信息
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

// 战争，C++版（简化版，避免标准库问题）
// 一共有n个据点，给定n-1条无向边，所有据点组成一棵树
// 一共有q条查询，每条查询格式如下
// 查询 k a1 a2 ... ak : 给出了k个不同的据点，这些据点被敌军占领
//                       我军需要切断一些边，使得敌军无法从一个据点到达另一个据点
//                       打印至少需要切断几条边
// 1 <= n、q <= 10^5
// 1 <= 所有查询给出的点的总数 <= 10^5
// 测试链接 : 类似于Codeforces 613D - Kingdom and Cities

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
// siz[u]，还有几个重要点没和u断开，值为0或者1
// cost[u]，表示节点u的子树中，做到不违规，至少需要攻占几个非重要点
int siz[MAXN], cost[MAXN];

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
    cost[u] = siz[u] = 0;
    for (int e = headv[u]; e > 0; e = nextv[e]) {
        int v = tov[e];
        dp(v);
        cost[u] += cost[v];
        siz[u] += siz[v];
    }
    if (isKey[u]) {
        cost[u] += siz[u];
        siz[u] = 1;
    } else if (siz[u] > 1) {
        cost[u]++;
        siz[u] = 0;
    }
}

int compute() {
    // 节点标记关键点信息
    for (int i = 1; i <= k; i++) {
        isKey[arr[i]] = true;
    }
    bool check = true;
    for (int i = 1; i <= k; i++) {
        // 只能通过攻占非关键点的方式，来隔开关键点
        // 所以如果 a 和 a的父节点 都是关键点，这是怎么也隔不开的
        // 直接返回-1即可
        if (isKey[stjump[arr[i]][0]]) {
            check = false;
            break;
        }
    }
    int ans = -1;
    if (check) {
        int tree = buildVirtualTree1();
        // int tree = buildVirtualTree2();
        dp(tree);
        ans = cost[tree];
    }
    // 节点撤销关键点信息
    for (int i = 1; i <= k; i++) {
        isKey[arr[i]] = false;
    }
    return ans;
}

// 由于编译环境问题，这里不包含main函数
// 在实际使用中，需要添加适当的输入输出函数