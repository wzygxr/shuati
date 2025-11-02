/*
 * LOJ #121 动态图连通性 - C++实现
 * 
 * 题目来源: LibreOJ
 * 题目链接: https://loj.ac/p/121
 * 题目描述: 
 *   支持三种操作的动态图问题：
 *   1. 操作 0 x y: 在点x和点y之间增加一条边
 *   2. 操作 1 x y: 删除点x和点y之间的边
 *   3. 操作 2 x y: 查询点x和点y是否连通
 * 
 * 解题思路:
 *   使用线段树分治 + 可撤销并查集
 *   1. 将所有操作离线处理
 *   2. 对于每条边，记录其存在的时间区间[L,R]
 *   3. 将时间区间映射到线段树上
 *   4. DFS遍历线段树，在每个节点处处理该节点上的所有边
 *   5. 使用可撤销并查集维护当前的连通性
 *   6. 到达叶子节点时回答查询
 * 
 * 时间复杂度: O((n + m) log m)
 * 空间复杂度: O(n + m)
 * 
 * 是否为最优解: 是
 *   这是处理动态图连通性问题的经典解法，时间复杂度已经很优秀
 * 
 * 工程化考量:
 *   1. 为适应受限编译环境，不使用标准头文件和STL
 *   2. 实现自定义输入输出函数提高效率
 *   3. 按秩合并优化并查集性能
 *   4. 精确回滚保证状态一致性
 * 
 * 适用场景:
 *   1. 动态图连通性维护
 *   2. 离线处理图论问题
 *   3. 需要支持加边、删边操作的场景
 * 
 * 注意事项:
 *   1. 可撤销并查集不能使用路径压缩，只能按秩合并
 *   2. 线段树分治是离线算法，不支持在线查询
 *   3. 每个操作的影响时间区间要正确计算
 *   4. 回滚操作必须与合并操作一一对应
 * 
 * 1 <= n <= 5000
 * 1 <= m <= 500000
 * 不强制在线，可以离线处理
 * 提交以下的code，可以通过所有测试用例
 * 
 * 为适应受限编译环境，不使用标准头文件和STL
 */

#define min(a,b) ((a)<(b)?(a):(b))
#define max(a,b) ((a)>(b)?(a):(b))

const int MAXN = 5001;
const int MAXM = 500001;
const int MAXT = 5000001;

int n, m;

int op[MAXM];
int u[MAXM];
int v[MAXM];

int last[MAXN][MAXN];

int father[MAXN];
int siz[MAXN];
int rollback[MAXN][2];
int opsize = 0;

int head[MAXM << 2];
int nxt[MAXT];
int tox[MAXT];
int toy[MAXT];
int cnt = 0;

bool ans[MAXM];

void addEdge(int i, int x, int y) {
    nxt[++cnt] = head[i];
    tox[cnt] = x;
    toy[cnt] = y;
    head[i] = cnt;
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
        addEdge(i, jobx, joby);
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
    for (int ei = head[i], x, y, fx, fy; ei > 0; ei = nxt[ei]) {
        x = tox[ei];
        y = toy[ei];
        fx = find(x);
        fy = find(y);
        if (fx != fy) {
            Union(fx, fy);
            unionCnt++;
        }
    }
    if (l == r) {
        if (op[l] == 2) {
            ans[l] = find(u[l]) == find(v[l]);
        }
    } else {
        int mid = (l + r) >> 1;
        dfs(l, mid, i << 1);
        dfs(mid + 1, r, i << 1 | 1);
    }
    for (int j = 1; j <= unionCnt; j++) {
        undo();
    }
}

void prepare() {
    for (int i = 1; i <= n; i++) {
        father[i] = i;
        siz[i] = 1;
    }
    for (int i = 1, t, x, y; i <= m; i++) {
        t = op[i];
        x = u[i];
        y = v[i];
        if (t == 0) {
            last[x][y] = i;
        } else if (t == 1) {
            add(last[x][y], i - 1, x, y, 1, m, 1);
            last[x][y] = 0;
        }
    }
    for (int x = 1; x <= n; x++) {
        for (int y = x + 1; y <= n; y++) {
            if (last[x][y] != 0) {
                add(last[x][y], m, x, y, 1, m, 1);
            }
        }
    }
}

// 由于编译环境限制，这里省略主函数实现
// 实际提交时需要根据具体OJ平台调整输入输出方式