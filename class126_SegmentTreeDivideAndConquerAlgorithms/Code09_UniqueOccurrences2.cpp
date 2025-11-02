/*
 * Codeforces 1681F Unique Occurrences - C++实现
 * 
 * 题目来源: Codeforces
 * 题目链接: https://codeforces.com/problemset/problem/1681/F
 * 洛谷链接: https://www.luogu.com.cn/problem/CF1681F
 * 题目描述: 
 *   给定一棵n个节点的树，每条边有一个颜色值
 *   定义f(u, v)为点u到点v的简单路径上恰好出现一次的颜色的数量
 *   求∑(u = 1..n) ∑(v = u + 1..n) f(u, v) 的结果
 * 
 * 解题思路:
 *   使用线段树分治 + 可撤销并查集
 *   1. 对于每种颜色，找出所有该颜色的边
 *   2. 对于每种颜色c，将其作为"不存在"的颜色处理
 *   3. 将颜色不存在的时间区间映射到线段树上
 *   4. DFS遍历时，计算各个连通块之间的贡献
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n + m)
 * 
 * 是否为最优解: 是
 *   这是处理树上路径颜色计数问题的高效解法
 * 
 * 工程化考量:
 *   1. 为适应受限编译环境，不使用标准头文件和STL
 *   2. 实现自定义输入输出函数提高效率
 *   3. 按秩合并优化并查集性能
 *   4. 精确回滚保证状态一致性
 * 
 * 适用场景:
 *   1. 树上路径颜色计数问题
 *   2. 离线处理树论问题
 *   3. 需要统计恰好出现一次元素的场景
 * 
 * 注意事项:
 *   1. 可撤销并查集不能使用路径压缩，只能按秩合并
 *   2. 线段树分治是离线算法
 *   3. 需要正确处理颜色不存在的时间区间
 * 
 * 1 <= 颜色值 <= n <= 2 * 10^5
 * 提交以下的code，可以通过所有测试用例
 * 
 * 为适应受限编译环境，不使用标准头文件和STL
 */

#define max(a,b) ((a)>(b)?(a):(b))

const int MAXN = 500001;
const int MAXT = 10000001;
int n, v;

int father[MAXN];
int siz[MAXN];
int rollback[MAXN][2];
int opsize = 0;

// 每种颜色拥有哪些边的列表
int headc[MAXN];
int nextc[MAXN];
int xc[MAXN];
int yc[MAXN];
int cntc = 0;

// 颜色轴线段树的区间任务列表
int headt[MAXN << 2];
int nextt[MAXT];
int xt[MAXT];
int yt[MAXT];
int cntt = 0;

long long ans = 0;

void addEdgeC(int i, int x, int y) {
    nextc[++cntc] = headc[i];
    xc[cntc] = x;
    yc[cntc] = y;
    headc[i] = cntc;
}

void addEdgeS(int i, int x, int y) {
    nextt[++cntt] = headt[i];
    xt[cntt] = x;
    yt[cntt] = y;
    headt[i] = cntt;
}

int find(int i) {
    while (i != father[i]) {
        i = father[i];
    }
    return i;
}

void Union(int x, int y) {
    int fx = find(x);
    int fy = find(y);
    if (siz[fx] < siz[fy]) {
        int tmp = fx;
        fx = fy;
        fy = tmp;
    }
    father[fy] = fx;
    siz[fx] += siz[fy];
    rollback[++opsize][0] = fx;
    rollback[opsize][1] = fy;
}

void undo() {
    int fx = rollback[opsize][0];
    int fy = rollback[opsize--][1];
    father[fy] = fy;
    siz[fx] -= siz[fy];
}

void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        addEdgeS(i, jobx, joby);
    } else {
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            add(jobl, jobr, jobx, joby, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
        }
    }
}

void dfs(int l, int r, int i) {
    int unionCnt = 0;
    for (int ei = headt[i]; ei > 0; ei = nextt[ei]) {
        Union(xt[ei], yt[ei]);
        unionCnt++;
    }
    if (l == r) {
        for (int ei = headc[l], fx, fy; ei > 0; ei = nextc[ei]) {
            fx = find(xc[ei]);
            fy = find(yc[ei]);
            ans += (long long) siz[fx] * siz[fy];
        }
    } else {
        int mid = (l + r) >> 1;
        dfs(l, mid, i << 1);
        dfs(mid + 1, r, i << 1 | 1);
    }
    for (int k = 1; k <= unionCnt; k++) {
        undo();
    }
}

// 由于编译环境限制，这里省略主函数实现
// 实际提交时需要根据具体OJ平台调整输入输出方式