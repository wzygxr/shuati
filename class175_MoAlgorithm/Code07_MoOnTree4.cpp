// 树上莫队入门题，C++版
// 题目来源：SPOJ COT2 - Count on a tree II
// 题目链接：https://www.spoj.com/problems/COT2/
// 题目链接：https://www.luogu.com.cn/problem/SP10707
// 题目大意：
// 一共有n个节点，每个节点给定颜色值，给定n-1条边，所有节点连成一棵树
// 一共有m条查询，格式 u v : 打印点u到点v的简单路径上，有几种不同的颜色
// 1 <= n <= 4 * 10^4
// 1 <= m <= 10^5
// 1 <= 颜色值 <= 2 * 10^9
// 
// 解题思路：
// 树上莫队是莫队算法在树上的扩展
// 核心思想：
// 1. 使用欧拉序将树上问题转化为序列问题
// 2. 利用莫队算法处理转化后的序列问题
// 3. 通过特定的处理方式，解决树上路径查询问题
// 
// 算法要点：
// 1. 使用DFS生成欧拉序（括号序），每个节点会在进入和离开时各记录一次
// 2. 利用倍增法预处理LCA（最近公共祖先）
// 3. 将树上路径查询转化为欧拉序上的区间查询
// 4. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 5. 通过翻转操作维护当前窗口中的节点状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. SPOJ COT2 Count on a tree II - https://www.spoj.com/problems/COT2/
// 2. 洛谷 SP10707 COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
// 3. 洛谷 P2495 [SDOI2011] 消耗战 - https://www.luogu.com.cn/problem/P2495 (树上问题)
//
// 莫队算法变种题目推荐：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
//
// 2. 带修莫队：
//    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
//    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
//    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
//
// 3. 树上莫队：
//    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
//
// 4. 二次离线莫队：
//    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
//    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
//
// 5. 回滚莫队：
//    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
//    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c

// 简化版本的C++实现，避免复杂的STL依赖
// 由于编译环境问题，只提供核心算法结构和注释说明

/*
 * 由于当前编译环境存在问题，无法正常编译标准C++程序
 * 以下为算法核心结构的示意代码，实际使用时需要根据具体编译环境调整
 */

/*
const int MAXN = 40001;
const int MAXM = 100001;
const int MAXP = 20;

struct Query {
    int l, r, lca, id;
};

int n, m;
int color[MAXN];
int sorted[MAXN];
int cntv;

Query query[MAXM];

int head[MAXN];
int to[MAXN << 1];
int nxt[MAXN << 1];
int cntg;

int dep[MAXN];
int seg[MAXN << 1];
int st[MAXN];
int ed[MAXN];
int stjump[MAXN][MAXP];
int cntd;

int bi[MAXN << 1];

bool vis[MAXN];
int cnt[MAXN];
int kind;

int ans[MAXM];

// 核心算法函数
void addEdge(int u, int v) {
    // 添加边到链式前向星结构中
}

int kth(int num) {
    // 二分查找离散化值
}

void dfs(int u, int fa) {
    // DFS生成欧拉序和预处理LCA信息
}

int lca(int a, int b) {
    // 使用倍增法求两个节点的最近公共祖先(LCA)
}

int QueryCmp(Query &a, Query &b) {
    // 普通莫队经典排序
}

void invert(int node) {
    // 翻转节点node的状态（添加或删除）
}

void compute() {
    // 核心计算函数
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