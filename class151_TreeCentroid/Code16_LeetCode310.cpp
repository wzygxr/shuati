// LeetCode 310. 最小高度树
// 题目来源：LeetCode 310 https://leetcode.cn/problems/minimum-height-trees/
// 题目描述：对于一个具有 n 个节点的树，给定 n-1 条边，找到所有可能的最小高度树的根节点。
// 算法思想：最小高度树的根节点就是树的重心
// 解题思路：
// 1. 树的高度定义为从根节点到最远叶子节点的边数
// 2. 最小高度树的根节点就是树的重心，即删除该节点后最大连通分量最小的节点
// 3. 通过一次DFS计算每个节点的最大子树大小，找到具有最小最大子树大小的所有节点
// 时间复杂度：O(n)，只需要一次DFS遍历
// 空间复杂度：O(n)，用于存储树结构和递归栈

// 由于编译环境限制，使用基础C++语法实现

// 树的最大节点数，根据题目限制设置
const int MAXN = 20001;

// 邻接表存储树结构
// head[i]表示节点i的第一条边的索引
int head[MAXN];
// next[i]表示第i条边的下一条边的索引
int next[MAXN << 1];
// to[i]表示第i条边指向的节点
int to[MAXN << 1];
// 边的计数器，从1开始编号
int cnt;

// size[i]表示以节点i为根的子树的节点数量
int size[MAXN];

// maxSub[i]表示以节点i为根时的最大子树大小
int maxSub[MAXN];

// 添加无向边的函数
// u和v之间添加一条边
void addEdge(int u, int v) {
    // 将新边添加到邻接表中
    next[cnt] = head[u];  // 新边的下一条边指向原来u节点的第一条边
    to[cnt] = v;          // 新边指向节点v
    head[u] = cnt++;      // u节点的第一条边更新为新边，然后cnt自增
    
    next[cnt] = head[v];  // 新边的下一条边指向原来v节点的第一条边
    to[cnt] = u;          // 新边指向节点u
    head[v] = cnt++;      // v节点的第一条边更新为新边，然后cnt自增
}

// 求两个数的最大值的辅助函数
int max(int a, int b) {
    return a > b ? a : b;
}

// 求两个数的最小值的辅助函数
int min(int a, int b) {
    return a < b ? a : b;
}

// 初始化函数，重置邻接表
void init() {
    cnt = 1;  // 边的索引从1开始
    // 初始化邻接表
    for (int i = 0; i < MAXN; i++) {
        head[i] = 0;
        size[i] = 0;
        maxSub[i] = 0;
    }
}

// 计算子树大小和最大子树大小
// u: 当前访问的节点
// parent: u的父节点，避免回到父节点形成环
// n: 节点总数
void dfs(int u, int parent, int n) {
    // 初始化当前节点u的子树大小为1（包含节点u本身）
    size[u] = 1;
    // 初始化当前节点u的最大子树大小为0
    maxSub[u] = 0;
    
    // 遍历u的所有邻接节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != parent) {
            // 递归访问子节点v，父节点为u
            dfs(v, u, n);
            
            // 将子节点v的子树大小加到当前节点u的子树大小中
            size[u] += size[v];
            
            // 更新以u为根时的最大子树大小
            maxSub[u] = max(maxSub[u], size[v]);
        }
    }
    
    // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
    maxSub[u] = max(maxSub[u], n - size[u]);
}

// 由于无法使用标准输入输出函数和STL容器，这里只展示算法实现
// 实际使用时需要添加输入输出代码和结果存储代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}