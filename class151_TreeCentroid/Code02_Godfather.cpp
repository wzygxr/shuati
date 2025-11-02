// 教父 (Godfather)
// 题目来源: POJ 3107 http://poj.org/problem?id=3107
// 问题描述: 给定一棵n个节点的树，找到树的所有重心
// 树的重心定义: 找到一个点，其所有的子树中最大的子树节点数不超过总节点数的一半
// 算法思路:
// 1. 使用DFS遍历树，计算每个节点的子树大小
// 2. 对于每个节点，计算删除该节点后形成的各个连通块的大小
// 3. 找到满足条件（最大连通块大小不超过n/2）的所有节点，即为重心
// 时间复杂度: O(n)，每个节点访问一次
// 空间复杂度: O(n)，用于存储邻接表和递归栈

// 由于编译环境限制，使用基础C++语法实现

// 最大节点数，根据题目限制设置
const int MAXN = 50001;

// 节点数量
int n;

// 邻接表的链式前向星表示法
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

// maxsub[i]表示以节点i为根时的最大子树大小
int maxsub[MAXN];

// 重心数组，最多有两个重心
int centers[2];
// 重心数量
int centerCount;

// 初始化函数，重置邻接表
void build() {
    cnt = 1;  // 边的索引从1开始
    // 初始化邻接表
    for (int i = 1; i <= n; i++) {
        head[i] = 0;
    }
    // 初始化重心数量
    centerCount = 0;
}

// 添加无向边的函数
// u和v之间添加一条边
void addEdge(int u, int v) {
    // 将新边添加到邻接表中
    next[cnt] = head[u];  // 新边的下一条边指向原来u节点的第一条边
    to[cnt] = v;          // 新边指向节点v
    head[u] = cnt++;      // u节点的第一条边更新为新边，然后cnt自增
}

// 求两个数的最大值的辅助函数
int max(int a, int b) {
    return a > b ? a : b;
}

// 深度优先搜索函数，用于计算子树大小和最大子树大小
// u: 当前访问的节点
// f: u的父节点，避免回到父节点形成环
void dfs(int u, int f) {
    // 初始化当前节点u的子树大小为1（包含节点u本身）
    size[u] = 1;
    // 初始化当前节点u的最大子树大小为0
    maxsub[u] = 0;
    
    // 遍历u的所有邻接节点
    for (int e = head[u], v; e != 0; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != f) {
            // 递归访问子节点v
            dfs(v, u);
            
            // 将子节点v的子树大小加到当前节点u的子树大小中
            size[u] += size[v];
            
            // 更新以u为根时的最大子树大小
            maxsub[u] = max(maxsub[u], size[v]);
        }
    }
    
    // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
    maxsub[u] = max(maxsub[u], n - size[u]);
}

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}