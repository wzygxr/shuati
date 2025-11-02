// 【模板】树的重心
// 题目来源: 洛谷 U328173 https://www.luogu.com.cn/problem/U328173
// 问题描述: 给定一棵无根树，求这棵树的重心（可能有多个）
// 树的重心定义: 计算以无根树每个点为根节点时的最大子树大小，这个值最小的点称为无根树的重心
// 算法思路:
// 1. 通过一次DFS计算每个节点作为根时的最大子树大小
// 2. 找到具有最小最大子树大小的所有节点，即为重心
// 时间复杂度：O(n)，只需要一次DFS遍历
// 空间复杂度：O(n)，用于存储树结构和递归栈

// 由于编译环境限制，使用基础C++语法实现

// 最大节点数，根据题目限制设置
const int MAXN = 1000001;

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

// 子树大小数组，size[i]表示以节点i为根的子树的节点数量
int size[MAXN];

// 每个节点的最大子树大小数组，maxSub[i]表示以节点i为根时的最大子树大小
int maxSub[MAXN];

// 重心列表，centroids[i]存储第i个重心节点
int centroids[MAXN];
// 重心数量
int centroidCount;

// 初始化函数，重置相关变量
void init() {
    cnt = 1;  // 边的索引从1开始
    for (int i = 0; i <= n; i++) {
        head[i] = 0;      // 初始化邻接表
        size[i] = 0;      // 初始化子树大小
        maxSub[i] = 0;    // 初始化最大子树大小
    }
    centroidCount = 0;    // 初始化重心数量
}

// 添加无向边的函数
// u和v之间添加一条边
void addEdge(int u, int v) {
    // 将新边添加到邻接表中（无向图需要添加两条边）
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

// 第一次DFS，计算每个节点的子树大小和最大子树大小
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void dfs1(int u, int father) {
    // 初始化当前节点u的子树大小为1（包含节点u本身）
    size[u] = 1;
    // 初始化当前节点u的最大子树大小为0
    maxSub[u] = 0;
    
    // 遍历u的所有邻接节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != father) {
            // 递归访问子节点v，父节点为u
            dfs1(v, u);
            
            // 将子节点v的子树大小加到当前节点u的子树大小中
            size[u] += size[v];
            
            // 更新以u为根时的最大子树大小
            maxSub[u] = max(maxSub[u], size[v]);
        }
    }
    
    // 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
    // 并更新最大子树大小
    maxSub[u] = max(maxSub[u], n - size[u]);
}

// 找到所有重心
void findCentroids() {
    // 初始化最小的最大子树大小为n（最大可能值）
    int minMaxSub = n;
    
    // 找到最小的最大子树大小
    // 遍历所有节点，找到最小的maxSub值
    for (int i = 1; i <= n; i++) {
        if (maxSub[i] < minMaxSub) {
            minMaxSub = maxSub[i];
        }
    }
    
    // 收集所有具有最小最大子树大小的节点
    // 这些节点就是树的重心
    for (int i = 1; i <= n; i++) {
        if (maxSub[i] == minMaxSub) {
            centroids[centroidCount++] = i;
        }
    }
    
    // 对重心列表进行排序（使用冒泡排序）
    for (int i = 0; i < centroidCount - 1; i++) {
        for (int j = 0; j < centroidCount - 1 - i; j++) {
            if (centroids[j] > centroids[j + 1]) {
                // 交换两个重心节点
                int temp = centroids[j];
                centroids[j] = centroids[j + 1];
                centroids[j + 1] = temp;
            }
        }
    }
}

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}