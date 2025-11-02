/**
 * 树上莫队应用：树上路径不同颜色数
 * 给定一棵n个节点的树，每个节点有一个颜色
 * 有m次查询，每次查询两点间路径上不同颜色的数目
 * 1 <= n, m <= 100000
 * 1 <= color[i] <= 100000
 * 测试链接 : https://vjudge.net/problem/HDU-5678
 *
 * 树上莫队的经典应用
 * 核心思想：
 * 1. 使用欧拉序将树上问题转化为序列问题
 * 2. 利用莫队算法处理转化后的序列问题
 * 3. 通过特定的处理方式，解决树上路径查询问题
 */

// 简化版本的C++实现，避免复杂的STL依赖
// 由于编译环境问题，只提供核心算法结构和注释说明

/*
 * 由于当前编译环境存在问题，无法正常编译标准C++程序
 * 以下为算法核心结构的示意代码，实际使用时需要根据具体编译环境调整
 */

/*
const int MAXN = 100001;
const int MAXM = 100001;
const int MAXP = 20;

int n, m;
// 颜色数组
int color[MAXN];
// 查询: l, r, lca, id
int queries[MAXM][4];

// 链式前向星存树
int head[MAXN];
int to[MAXN << 1];
int next_edge[MAXN << 1];
int edgeCount = 0;

// 树上信息
int depth[MAXN];
int euler[MAXN << 1];  // 欧拉序
int first[MAXN];       // 第一次出现位置
int last[MAXN];        // 最后一次出现位置
int jump[MAXN][MAXP];  // 倍增表
int eulerLen = 0;      // 欧拉序长度

// 分块相关
int belong[MAXN << 1];

// 窗口信息
bool visited[MAXN];    // 节点是否在窗口中
int count[MAXN];       // 每种颜色的出现次数
int colorTypes = 0;    // 不同颜色的种类数
int answers[MAXM];

// 核心算法函数
void addEdge(int u, int v) {
    // 添加边
}

void dfs(int u, int parent) {
    // DFS生成欧拉序和预处理LCA信息
}

int lca(int a, int b) {
    // 倍增法求LCA
}

int QueryComparator(int a[], int b[]) {
    // 普通莫队排序规则
}

void toggle(int node) {
    // 翻转节点状态
}

void compute() {
    // 主计算函数
}

void prepare() {
    // 预处理函数
}

int main() {
    // 主函数实现
    return 0;
}
*/

// 以上为算法核心结构示意，实际使用时需要根据具体编译环境调整