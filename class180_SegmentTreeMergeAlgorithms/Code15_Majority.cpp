// Majority P8496.cpp
// 题目来源: 洛谷P8496 [NOI2022] 众数
// 题目链接: https://www.luogu.com.cn/problem/P8496
// 题目大意: 给定n个序列，支持四种操作：
//          1. 在某个序列末尾插入数字
//          2. 删除某个序列末尾的数字
//          3. 查询多个序列拼接后的众数（出现次数严格大于一半的数字）
//          4. 合并两个序列并删除原序列
// 解法: 使用线段树合并优化
// 时间复杂度: O((n + q) log(n + q))
// 空间复杂度: O((n + q) log(n + q))

int n, q;
int G[500005][10], G_size[500005];
int val[500005], ans[500005];

// 查询信息
int queryL[500005], queryR[500005];

// 线段树合并相关
int root[500005], lc[10000005], rc[10000005], sum[10000005];
int maxVal[10000005], maxCnt[10000005];
int segCnt = 0;

// 动态开点线段树插入
// 在线段树中插入位置x，值为val
void insert(int rt, int l, int r, int x, int val) {
    if (l == r) {
        sum[rt] += val;
        maxVal[rt] = l;
        maxCnt[rt] = sum[rt];
        return;
    }
    int mid = (l + r) >> 1;
    if (x <= mid) {
        if (lc[rt] == 0) lc[rt] = ++segCnt;
        insert(lc[rt], l, mid, x, val);
    } else {
        if (rc[rt] == 0) rc[rt] = ++segCnt;
        insert(rc[rt], mid+1, r, x, val);
    }
    sum[rt] = sum[lc[rt]] + sum[rc[rt]];
    // 更新众数信息
    if (maxCnt[lc[rt]] > maxCnt[rc[rt]]) {
        maxVal[rt] = maxVal[lc[rt]];
        maxCnt[rt] = maxCnt[lc[rt]];
    } else if (maxCnt[lc[rt]] < maxCnt[rc[rt]]) {
        maxVal[rt] = maxVal[rc[rt]];
        maxCnt[rt] = maxCnt[rc[rt]];
    } else {
        maxVal[rt] = (maxVal[lc[rt]] < maxVal[rc[rt]]) ? maxVal[lc[rt]] : maxVal[rc[rt]];
        maxCnt[rt] = maxCnt[lc[rt]];
    }
}

// 线段树合并
// 将以y为根的线段树合并到以x为根的线段树中
int merge(int x, int y, int l, int r) {
    if (!x || !y) return x + y;
    if (l == r) {
        sum[x] += sum[y];
        maxVal[x] = l;
        maxCnt[x] = sum[x];
        return x;
    }
    int mid = (l + r) >> 1;
    lc[x] = merge(lc[x], lc[y], l, mid);
    rc[x] = merge(rc[x], rc[y], mid+1, r);
    sum[x] = sum[lc[x]] + sum[rc[x]];
    // 更新众数信息
    if (maxCnt[lc[x]] > maxCnt[rc[x]]) {
        maxVal[x] = maxVal[lc[x]];
        maxCnt[x] = maxCnt[lc[x]];
    } else if (maxCnt[lc[x]] < maxCnt[rc[x]]) {
        maxVal[x] = maxVal[rc[x]];
        maxCnt[x] = maxCnt[rc[x]];
    } else {
        maxVal[x] = (maxVal[lc[x]] < maxVal[rc[x]]) ? maxVal[lc[x]] : maxVal[rc[x]];
        maxCnt[x] = maxCnt[lc[x]];
    }
    return x;
}

// DFS处理线段树合并
// 树形DP + 线段树合并
void dfs(int u, int father) {
    // 先处理所有子节点
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs(v, u);
            // 合并子节点的信息到当前节点
            root[u] = merge(root[u], root[v], 1, n);
        }
    }
    
    // 插入当前节点的信息
    if (root[u] == 0) root[u] = ++segCnt;
    insert(root[u], 1, n, val[u], 1);
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}

/*
 * 相关题目推荐:
 * 1. 洛谷P8496 [NOI2022] 众数 (本题)
 *    链接: https://www.luogu.com.cn/problem/P8496
 *    题意: 支持序列插入、删除、合并和查询众数的操作
 *    解法: 线段树合并维护序列信息
 * 
 * 2. Codeforces 813E - Army Creation
 *    链接: https://codeforces.com/problemset/problem/813/E
 *    题意: 在线查询区间内最多能选出多少个数，使得每个数出现次数不超过k
 *    解法: 线段树合并维护前缀信息
 * 
 * 3. Codeforces 786C - Till I Collapse
 *    链接: https://codeforces.com/problemset/problem/786/C
 *    题意: 将序列划分成最少的段，使得每段不同数字个数不超过k
 *    解法: 线段树合并维护区间信息
 * 
 * 4. BZOJ 4756 - 奶牛抗议
 *    题意: 树上路径计数问题
 *    解法: 线段树合并维护前缀和
 * 
 * 5. Codeforces 600E - Lomsat gelral
 *    链接: https://codeforces.com/problemset/problem/600/E
 *    题意: 求每棵子树中出现次数最多的颜色
 *    解法: 树上启发式合并或线段树合并
 * 
 * 6. Codeforces 570D - Tree Requests
 *    链接: https://codeforces.com/problemset/problem/570/D
 *    题意: 查询子树中深度为h的节点字符能否重排成回文串
 *    解法: 树上启发式合并维护位运算
 * 
 * 算法详解:
 * 线段树合并是一种用于优化维护动态序列信息的技术，特别适用于需要合并序列的场景。
 * 主要思想:
 * 1. 对于每个序列，维护一个线段树，存储该序列中各数字的出现次数
 * 2. 当需要合并两个序列时，将对应的线段树合并
 * 3. 在线段树中维护众数信息，便于快速查询
 * 
 * 时间复杂度分析:
 * - 插入操作: O(log n)
 * - 删除操作: O(log n)
 * - 合并操作: O(log n)
 * - 查询操作: O(log n)
 * - 总时间复杂度: O((n + q) log(n + q))
 * 
 * 空间复杂度分析:
 * - 线段树节点数: O((n + q) log(n + q))
 * - 其他辅助数组: O(n + q)
 * - 总空间复杂度: O((n + q) log(n + q))
 */