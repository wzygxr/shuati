// Kay and Snowflake (雪花与凯)
// 题目来源: Codeforces 686D https://codeforces.com/contest/686/problem/D
// 问题描述: 给定一棵有根树，求出每一棵子树的重心是哪一个节点
// 树的重心定义：找到一个点，其所有的子树中最大的子树节点数最少
// 算法思路:
// 1. 首先通过DFS计算每个子树的大小
// 2. 对于每个节点，利用其最大子树的重心信息来快速找到当前子树的重心
// 3. 利用性质：子树的重心要么是最大子树的重心，要么在从最大子树重心到根节点的路径上
// 时间复杂度：O(n)，每个节点最多被访问常数次
// 空间复杂度：O(n)，用于存储树结构和递归栈

// 由于编译环境限制，使用基础C++语法实现

// 最大节点数，根据题目限制设置
const int MAXN = 300001;

// 节点数量和查询数量
int n, q;

// 链式前向星存储树结构
// head[i]表示节点i的第一条边的索引
int head[MAXN];
// next[i]表示第i条边的下一条边的索引
int next[MAXN << 1];
// to[i]表示第i条边指向的节点
int to[MAXN << 1];
// 边的计数器，从1开始编号
int cnt;

// 父节点数组，parent[i]表示节点i的父节点
int parent[MAXN];

// 子树大小数组，size[i]表示以节点i为根的子树的节点数量
int size[MAXN];

// 每个子树的重心数组，centroid[i]表示以节点i为根的子树的重心
int centroid[MAXN];

// 初始化函数，重置邻接表
void init() {
    cnt = 1;  // 边的索引从1开始
    // 初始化邻接表
    for (int i = 0; i <= n; i++) {
        head[i] = 0;
    }
}

// 添加边的函数
// u和v之间添加一条有向边（从u指向v）
void addEdge(int u, int v) {
    // 将新边添加到邻接表中
    next[cnt] = head[u];  // 新边的下一条边指向原来u节点的第一条边
    to[cnt] = v;          // 新边指向节点v
    head[u] = cnt++;      // u节点的第一条边更新为新边，然后cnt自增
}

// 计算每个子树的大小
// 使用DFS递归计算以节点u为根的子树大小
void computeSize(int u) {
    // 初始化当前节点u的子树大小为0
    size[u] = 0;
    
    // 递归计算每个子节点的子树大小
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        computeSize(v);
        size[u] += size[v];
    }
    
    // 加上节点u本身
    size[u]++;
}

// 计算每个子树的重心
// 利用已知的子树重心信息来快速计算当前子树的重心
void computeCentroid(int u) {
    // 如果子树只有一个节点，重心就是它本身
    if (size[u] == 1) {
        centroid[u] = u;
        return;
    }

    // 找到最大的子树
    // 初始化最大子树为第一个子节点
    int largest = -1;
    int largestSize = 0;
    
    // 遍历所有子节点，找到子树大小最大的子节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        // 递归计算子节点v的重心
        computeCentroid(v);
        
        // 更新最大子树
        if (largestSize < size[v]) {
            largest = v;
            largestSize = size[v];
        }
    }

    // 子树大小的一半（向上取整）
    // 这是判断一个节点是否为重心的关键阈值
    int half = (size[u] + 1) / 2;
    
    // 从最大子树的重心开始向上查找
    // 利用性质：子树的重心要么是最大子树的重心，要么在从最大子树重心到根节点的路径上
    int cur = centroid[largest];
    
    // 沿着从最大子树重心到当前节点u的路径向上查找
    while (cur != u) {
        // 如果当前节点的子树大小小于half，说明它不可能是重心，需要继续向上查找
        if (size[cur] < half) {
            cur = parent[cur];
        } else {
            // 如果当前节点的子树大小大于等于half，且父节点方向的子树大小也小于half，则找到重心
            // 父节点方向的子树大小 = 整棵子树大小 - 当前节点子树大小
            if (size[u] - size[cur] < half) {
                break;
            } else {
                // 否则继续向上查找
                cur = parent[cur];
            }
        }
    }
    centroid[u] = cur;
}

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}