// 晋升者计数问题 (Promotion Counting) - C++版本
// 测试链接 : https://www.luogu.com.cn/problem/P3605

/**
 * 题目来源: USACO 2017 January Contest, Platinum Problem 1. Promotion Counting
 * 题目链接: https://www.luogu.com.cn/problem/P3605
 * 
 * 题目描述:
 * 给定一棵 n 个节点的树，每个节点有一个能力值。对于每个节点，统计其子树中有多少个节点的能力值
 * 严格大于该节点的能力值。
 * 
 * 解题思路:
 * 1. 使用线段树合并技术解决树上统计问题
 * 2. 为每个节点建立一棵权值线段树，维护子树中各能力值的出现次数
 * 3. 从叶子节点开始，自底向上合并子树的线段树
 * 4. 查询当前节点线段树中大于该节点能力值的节点数量
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)，其中 n 是节点数量
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

const int MAXN = 100001;
const int MAXT = MAXN * 40;

int n;

// 邻接表存储树结构
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cntg;

// 节点能力值数组和排序后的数组
int arr[MAXN];
int sorted[MAXN];
int cntv;

// 每个节点对应的线段树根节点及相关数组
int root[MAXN];
int ls[MAXT];
int rs[MAXT];
int siz[MAXT];
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
 * 二分查找数字在排序数组中的位置
 * @param num 要查找的数字
 * @return 位置索引
 */
int kth(int num) {
    int left = 1, right = cntv, mid, ret = 0;
    while (left <= right) {
        mid = (left + right) >> 1;
        if (sorted[mid] <= num) {
            ret = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return ret;
}

/**
 * 更新线段树节点信息（父节点信息由子节点信息推导）
 * @param i 节点索引
 */
void up(int i) {
    siz[i] = siz[ls[i]] + siz[rs[i]];
}

/**
 * 在线段树中添加一个值
 * @param jobi 要添加的值（离散化后的索引）
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点索引
 * @return 更新后的节点索引
 */
int add(int jobi, int l, int r, int i) {
    int rt = i;
    if (rt == 0) {
        rt = ++cntt; // 动态开点
    }
    if (l == r) {
        siz[rt]++; // 叶子节点计数加1
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            ls[rt] = add(jobi, l, mid, ls[rt]); // 递归更新左子树
        } else {
            rs[rt] = add(jobi, mid + 1, r, rs[rt]); // 递归更新右子树
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
        siz[t1] += siz[t2]; // 累加计数
    } else {
        // 递归合并左右子树
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1); // 更新当前节点信息
    }
    return t1;
}

/**
 * 查询区间[jobl, jobr]内的节点数量
 * @param jobl 查询区间左端点
 * @param jobr 查询区间右端点
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点索引
 * @return 区间内节点数量
 */
int query(int jobl, int jobr, int l, int r, int i) {
    // 边界条件：查询区间无效或节点为空
    if (jobl > jobr || i == 0) {
        return 0;
    }
    // 完全覆盖：当前区间完全在查询区间内
    if (jobl <= l && r <= jobr) {
        return siz[i];
    }
    int mid = (l + r) >> 1;
    int ret = 0;
    // 递归查询左右子树
    if (jobl <= mid) {
        ret += query(jobl, jobr, l, mid, ls[i]);
    }
    if (jobr > mid) {
        ret += query(jobl, jobr, mid + 1, r, rs[i]);
    }
    return ret;
}

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
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != fa) {
            root[u] = merge(1, cntv, root[u], root[v]);
        }
    }
    
    // 查询大于当前节点能力值的节点数量
    ans[u] = query(arr[u] + 1, cntv, 1, cntv, root[u]);
}

/**
 * 预处理函数
 */
void compute() {
    // 复制能力值数组用于排序
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    // 排序并去重实现离散化
    // 简单实现排序（冒泡排序）
    for (int i = 1; i <= n; i++) {
        for (int j = i + 1; j <= n; j++) {
            if (sorted[i] > sorted[j]) {
                int temp = sorted[i];
                sorted[i] = sorted[j];
                sorted[j] = temp;
            }
        }
    }
    cntv = 1;
    for (int i = 2; i <= n; i++) {
        if (sorted[cntv] != sorted[i]) {
            sorted[++cntv] = sorted[i];
        }
    }
    // 将原数组转换为离散化后的索引
    for (int i = 1; i <= n; i++) {
        arr[i] = kth(arr[i]);
    }
    // 为每个节点建立初始线段树节点
    for (int i = 1; i <= n; i++) {
        root[i] = add(arr[i], 1, cntv, root[i]);
    }
}

int main() {
    // 读取节点数
    scanf("%d", &n);
    
    // 读取节点能力值
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 读取边信息
    for (int i = 2, fa; i <= n; i++) {
        scanf("%d", &fa);
        addEdge(fa, i);
        addEdge(i, fa);
    }
    
    // 预处理
    compute();
    
    // DFS计算答案
    dfs(1, 0);
    
    // 输出结果
    for (int i = 1; i <= n; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}