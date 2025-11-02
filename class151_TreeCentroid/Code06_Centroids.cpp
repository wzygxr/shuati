// Centroids (重心)
// 题目来源: Codeforces 708C https://codeforces.com/contest/708/problem/C
// 问题描述: 给定一棵树，对于每个点，我们删掉任意一条边，再连上任意一条边，
// 求这样操作后可以使这个点为重心的点数
// 树的重心定义：删除这个点后最大连通块的结点数最小
// 算法思路:
// 1. 对于每个节点，首先计算其作为重心时的最大连通块大小
// 2. 如果最大连通块大小不超过n/2，则该节点本身就是重心
// 3. 否则，检查是否可以通过调整一条边使其成为重心
//    调整策略：将最大的子树移动到其他位置，使得调整后最大连通块大小不超过n/2
// 时间复杂度：O(n)，需要两次DFS遍历
// 空间复杂度：O(n)，用于存储树结构和递归栈

// 由于编译环境限制，使用基础C++语法实现

// 最大节点数，根据题目限制设置
const int MAXN = 400001;

// 节点数量
int n;

// 链式前向星存储树结构
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

// secondMaxSub[i]表示以节点i为根时的次大子树大小
int secondMaxSub[MAXN];

// upSub[i]表示节点i向上（父节点方向）的子树大小，即整棵树去掉以i为根的子树后剩余的部分
int upSub[MAXN];

// 答案数组，ans[i]=1表示节点i可以通过调整一条边成为重心，ans[i]=0表示不可以
int ans[MAXN];

// 初始化函数，重置邻接表
void init() {
    cnt = 1;  // 边的索引从1开始
    // 初始化邻接表
    for (int i = 0; i < MAXN; i++) {
        head[i] = 0;
        size[i] = 0;
        maxSub[i] = 0;
        secondMaxSub[i] = 0;
        upSub[i] = 0;
        ans[i] = 0;
    }
}

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

// 第一次DFS，计算每个节点的子树大小、最大子树大小和次大子树大小
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void dfs1(int u, int father) {
    // 初始化当前节点u的子树大小为1（包含节点u本身）
    size[u] = 1;
    // 初始化当前节点u的最大子树大小为0
    maxSub[u] = 0;
    // 初始化当前节点u的次大子树大小为0
    secondMaxSub[u] = 0;

    // 遍历u的所有邻接节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != father) {
            // 递归访问子节点v，父节点为u
            dfs1(v, u);
            
            // 将子节点v的子树大小加到当前节点u的子树大小中
            size[u] += size[v];

            // 更新最大和次大子树大小
            if (size[v] > maxSub[u]) {
                // 如果当前子树大小大于原最大子树大小
                // 原最大子树大小变为次大子树大小
                secondMaxSub[u] = maxSub[u];
                // 当前子树大小变为最大子树大小
                maxSub[u] = size[v];
            } else if (size[v] > secondMaxSub[u]) {
                // 如果当前子树大小大于原次大子树大小但不大于最大子树大小
                // 当前子树大小变为次大子树大小
                secondMaxSub[u] = size[v];
            }
        }
    }
}

// 第二次DFS，计算向上子树的大小并判断每个节点是否可以成为重心
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void dfs2(int u, int father) {
    // 计算向上子树的大小，即整棵树去掉以u为根的子树后剩余的部分
    upSub[u] = n - size[u];

    // 判断当前节点是否可以成为重心
    // 当前节点作为根时的最大连通块大小
    int maxComponent = max(upSub[u], maxSub[u]);
    
    if (maxComponent <= n / 2) {
        // 如果最大连通块大小不超过总节点数的一半，则当前节点本身就是重心
        ans[u] = 1;
    } else {
        // 否则，需要通过调整边使当前节点成为重心
        // 调整策略：将最大的子树移动到其他位置
        
        // 标记是否可以通过调整使当前节点成为重心
        bool canMakeCentroid = false;

        // 检查向上子树（父节点方向的子树）
        if (upSub[u] <= n / 2) {
            // 如果向上子树大小不超过n/2，则可以通过调整使当前节点成为重心
            canMakeCentroid = true;
        }

        // 检查各个子树
        for (int e = head[u], v; e; e = next[e]) {
            v = to[e];  // 获取当前边指向的节点
            
            if (v != father) {
                // 如果v是最大子树
                if (size[v] == maxSub[u]) {
                    // 使用次大子树进行调整
                    // 调整后的最大连通块大小为n - maxSub[u]（即去掉最大子树后剩余的部分）
                    if (n - maxSub[u] <= n / 2) {
                        canMakeCentroid = true;
                        break;
                    }
                } else {
                    // 使用最大子树进行调整
                    // 调整后的最大连通块大小为n - size[v]（即去掉子树v后剩余的部分）
                    if (n - size[v] <= n / 2) {
                        canMakeCentroid = true;
                        break;
                    }
                }
            }
        }

        if (canMakeCentroid) {
            // 如果可以通过调整使当前节点成为重心，则标记为1
            ans[u] = 1;
        }
    }

    // 递归处理子节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        if (v != father) {
            dfs2(v, u);
        }
    }
}

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}