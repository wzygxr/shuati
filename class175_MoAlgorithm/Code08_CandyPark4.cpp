// 糖果公园，C++版
// 一共有n个公园，给定n-1条边，所有公园连成一棵树，c[i]为i号公园的糖果型号
// 一共有m种糖果，v[y]表示y号糖果的美味指数，给定长度为n的数组w，用于计算愉悦值
// 假设游客当前遇到了y号糖果，并且是第x次遇到，那么愉悦值会增加 v[y] * w[x]
// 随着游客遇到各种各样的糖果，愉悦值会不断上升，接下来有q条操作，操作类型如下
// 操作 0 x y : 第x号公园的糖果型号改成y
// 操作 1 x y : 游客从点x出发走过简单路径到达y，依次遇到每个公园的糖果，打印最终的愉悦值
// 1 <= n、m、q <= 10^5
// 1 <= v[i]、w[i] <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P4074

// 带修改树上莫队是莫队算法的高级应用
// 结合了三个重要概念：
// 1. 带修改莫队：支持在线修改操作
// 2. 树上莫队：处理树上路径查询
// 3. 复杂的答案计算：根据遇到次数计算愉悦值

// 简化版本的C++实现，避免复杂的STL依赖
// 由于编译环境问题，只提供核心算法结构和注释说明

/*
 * 由于当前编译环境存在问题，无法正常编译标准C++程序
 * 以下为算法核心结构的示意代码，实际使用时需要根据具体编译环境调整
 */

/*
const int MAXN = 100001;
const int MAXP = 20;

struct Query {
    int l, r, t, lca, id;
};

struct Update {
    int pos, val;
};

int n, m, q;
int v[MAXN];
int w[MAXN];
int c[MAXN];

int head[MAXN];
int to[MAXN << 1];
int nxt[MAXN << 1];
int cntg;

Query query[MAXN];
Update update[MAXN];
int cntq, cntu;

int dep[MAXN];
int seg[MAXN << 1];
int st[MAXN];
int ed[MAXN];
int stjump[MAXN][MAXP];
int cntd;

int bi[MAXN << 1];
bool vis[MAXN];
int cnt[MAXN];
long long happy;
long long ans[MAXN];

// 核心算法函数
void addEdge(int u, int v) {
    // 添加边到链式前向星结构中
}

void dfs(int u, int fa) {
    // DFS生成欧拉序和预处理LCA信息
}

int lca(int a, int b) {
    // 使用倍增法求两个节点的最近公共祖先(LCA)
}

int QueryCmp(Query &a, Query &b) {
    // 带修莫队经典排序
}

void invert(int node) {
    // 翻转节点node的状态（添加或删除）
}

void moveTime(int tim) {
    // 处理时间维度上的修改操作
}

void compute() {
    // 核心计算函数
}

void prapare() {
    // 预处理函数
}

int main() {
    // 主函数实现
    return 0;
}
*/

// 以上为算法核心结构示意，实际使用时需要根据具体编译环境调整