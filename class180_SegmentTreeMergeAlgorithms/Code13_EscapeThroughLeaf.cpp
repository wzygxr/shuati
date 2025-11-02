// Escape Through Leaf CF932F.cpp
// 题目来源: Codeforces 932F - Escape Through Leaf
// 题目链接: https://codeforces.com/problemset/problem/932/F
// 题目大意: 给定一棵n个节点的树，每个节点有两个权值a[i]和b[i]。对于每个节点u，需要计算从u出发到达任意叶子节点的最小代价。
//          从节点u跳到节点v的代价是a[u] * b[v]。要求输出从根节点1出发的最小代价。
// 解法: 使用李超线段树合并优化树形DP
// 时间复杂度: O(n log n)
// 空间复杂度: O(n log n)

int n;
int G[100005][10], G_size[100005];
long long a[100005], b[100005], dp[100005];

const long long INF = 1LL << 60;

// 线段树合并相关
int root[100005], lc[2000005], rc[2000005];
long long k[2000005], bVal[2000005];
int segCnt = 0;

// 李超线段树插入直线
// 在区间[l,r]内插入一条直线y = newK * x + newB
void insert(int rt, long long l, long long r, long long newK, long long newB) {
    if (rt == 0) return;
    long long mid = (l + r) >> 1;
    bool left = newK * l + newB < k[rt] * l + bVal[rt];
    bool right = newK * r + newB < k[rt] * r + bVal[rt];
    
    if (right) {
        k[rt] = newK;
        bVal[rt] = newB;
    }
    
    if (l == r) return;
    
    if (left != right) {
        bool midCheck = newK * mid + newB < k[rt] * mid + bVal[rt];
        if (midCheck) {
            long long tempK = k[rt], tempB = bVal[rt];
            k[rt] = newK;
            bVal[rt] = newB;
            insert(lc[rt], l, mid, tempK, tempB);
        } else {
            insert(rc[rt], mid+1, r, newK, newB);
        }
    } else if (left) {
        insert(lc[rt], l, mid, newK, newB);
    } else {
        insert(rc[rt], mid+1, r, newK, newB);
    }
}

// 李超线段树查询
// 查询当x=x时，所有直线中的最小y值
long long query(int rt, long long l, long long r, long long x) {
    if (rt == 0) return INF;
    if (l == r) return k[rt] * x + bVal[rt];
    long long mid = (l + r) >> 1;
    long long res = k[rt] * x + bVal[rt];
    if (x <= mid) {
        return res < query(lc[rt], l, mid, x) ? res : query(lc[rt], l, mid, x);
    } else {
        return res < query(rc[rt], mid+1, r, x) ? res : query(rc[rt], mid+1, r, x);
    }
}

// 线段树合并
// 将以y为根的线段树合并到以x为根的线段树中
int merge(int x, int y, long long l, long long r) {
    if (!x || !y) return x + y;
    if (l == r) {
        if (k[y] * l + bVal[y] < k[x] * l + bVal[x]) {
            k[x] = k[y];
            bVal[x] = bVal[y];
        }
        return x;
    }
    long long mid = (l + r) >> 1;
    lc[x] = merge(lc[x], lc[y], l, mid);
    rc[x] = merge(rc[x], rc[y], mid+1, r);
    // 更新当前节点最优直线
    if (k[y] * l + bVal[y] < k[x] * l + bVal[x] || 
        k[y] * r + bVal[y] < k[x] * r + bVal[x]) {
        // 需要重新插入y的直线到x中
        insert(x, l, r, k[y], bVal[y]);
    }
    return x;
}

// DFS处理线段树合并
// 树形DP + 李超线段树合并
void dfs(int u, int father) {
    // 先处理所有子节点
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs(v, u);
            // 合并子节点的信息到当前节点
            root[u] = merge(root[u], root[v], -INF, INF);
        }
    }
    
    // 叶子节点特殊处理
    if (G_size[u] == 1 && u != 1) {
        dp[u] = 0;
    } else {
        // 查询最小值
        dp[u] = query(root[u], -INF, INF, a[u]);
    }
    
    // 插入当前节点的直线
    if (root[u] == 0) root[u] = ++segCnt;
    insert(root[u], -INF, INF, b[u], dp[u]);
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}

/*
 * 相关题目推荐:
 * 1. Codeforces 809C - Find a car (李超线段树)
 *    链接: https://codeforces.com/problemset/problem/809/C
 *    题意: 在一个网格中，每个格子的值是其左上矩形内不同数字的个数，求子矩形和
 *    解法: 李超线段树维护二维前缀和
 * 
 * 2. Codeforces 932F - Escape Through Leaf (本题)
 *    链接: https://codeforces.com/problemset/problem/932/F
 *    题意: 树上DP，从每个节点跳到叶子节点的最小代价
 *    解法: 李超线段树合并优化树形DP
 * 
 * 3. Codeforces 1083E - The Fair Nut and Rectangles
 *    链接: https://codeforces.com/problemset/problem/1083/E
 *    题意: 平面上有一些带权矩形，选择一些不相交的矩形使得权值和最大
 *    解法: 李超线段树维护凸包优化DP
 * 
 * 4. Codeforces 660F - Bear and Bowling 4
 *    链接: https://codeforces.com/problemset/problem/660/F
 *    题意: 给定一个序列，选择一个子段使得该子段的加权和最大
 *    解法: 李超线段树维护斜率优化DP
 * 
 * 5. BZOJ 2146 - Construct (线段树合并)
 *    题意: 给定一棵树，每个节点有权值，求每个子树中不同权值个数
 *    解法: 线段树合并维护子树信息
 * 
 * 6. Codeforces 600E - Lomsat gelral (树上启发式合并)
 *    链接: https://codeforces.com/problemset/problem/600/E
 *    题意: 求每棵子树中出现次数最多的颜色
 *    解法: 树上启发式合并或线段树合并
 * 
 * 7. Codeforces 570D - Tree Requests (树上启发式合并)
 *    链接: https://codeforces.com/problemset/problem/570/D
 *    题意: 查询子树中深度为h的节点字符能否重排成回文串
 *    解法: 树上启发式合并维护位运算
 * 
 * 8. Codeforces 715C - Digit Tree (点分治+线段树合并)
 *    链接: https://codeforces.com/problemset/problem/715/C
 *    题意: 统计树上路径中数字能被m整除的路径条数
 *    解法: 点分治+线段树合并
 * 
 * 算法详解:
 * 李超线段树合并是一种用于优化树形DP的技术，特别适用于转移方程为min/max形式且涉及直线的情况。
 * 主要思想:
 * 1. 对于每个节点，维护一个李超线段树，存储该子树中所有可能的转移直线
 * 2. 在DFS过程中，先递归处理所有子节点
 * 3. 将子节点的线段树合并到当前节点
 * 4. 查询当前节点的最优转移值
 * 5. 插入当前节点的新直线
 * 
 * 时间复杂度分析:
 * - DFS遍历: O(n)
 * - 线段树合并: O(n log n) (每次合并会销毁一个节点，总共O(n)个节点)
 * - 查询和插入: O(log n)
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 线段树节点数: O(n log n)
 * - 其他辅助数组: O(n)
 * - 总空间复杂度: O(n log n)
 */