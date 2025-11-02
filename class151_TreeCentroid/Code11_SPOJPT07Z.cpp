// SPOJ PT07Z Longest path in a tree (树中的最长路径)
// 题目来源: SPOJ PT07Z https://www.spoj.com/problems/PT07Z/
// 问题描述: 求树的直径，即树中任意两点之间最长的简单路径
// 算法思路:
// 1. 树的直径可以通过两次BFS或DFS求解
// 2. 第一次从任意节点（如节点1）开始BFS，找到距离它最远的节点
// 3. 第二次从第一步找到的最远节点开始BFS，找到距离它最远的节点
// 4. 第二次BFS中找到的最远距离就是树的直径
// 与重心的关系: 树的直径与重心密切相关，直径的中点（可能是一个节点或一条边的中点）通常与重心有关
// 时间复杂度：O(n)，需要两次BFS遍历
// 空间复杂度：O(n)，用于存储树结构和BFS队列

// 由于编译环境限制，使用基础C++语法实现

// 最大节点数，根据题目限制设置
const int MAXN = 10001;

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

// dist[i]表示从起始节点到节点i的距离
int dist[MAXN];

// 队列相关变量
int queue[MAXN];
int front, rear;

// 初始化函数，重置邻接表
void init() {
    cnt = 1;  // 边的索引从1开始
    // 初始化邻接表
    for (int i = 0; i < MAXN; i++) {
        head[i] = 0;
        dist[i] = -1;
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

// BFS求最远节点
// start: BFS的起始节点
// 返回值: 距离起始节点最远的节点
int bfs(int start) {
    // 初始化距离数组，-1表示未访问
    for (int i = 0; i < MAXN; i++) {
        dist[i] = -1;
    }
    
    // 初始化队列
    front = 0;
    rear = 0;
    
    // 将起始节点加入队列
    queue[rear++] = start;
    // 起始节点的距离为0
    dist[start] = 0;
    
    // 记录最远节点和最大距离
    int farthestNode = start;
    int maxDist = 0;
    
    // BFS遍历
    while (front < rear) {
        // 取出队首节点
        int u = queue[front++];
        
        // 更新最远节点和最大距离
        if (dist[u] > maxDist) {
            maxDist = dist[u];
            farthestNode = u;
        }
        
        // 遍历u的所有邻接节点
        for (int e = head[u]; e; e = next[e]) {
            int v = to[e];  // 获取当前边指向的节点
            
            // 如果节点v未被访问过
            if (dist[v] == -1) {
                // 设置节点v的距离为节点u的距离加1
                dist[v] = dist[u] + 1;
                // 将节点v加入队列
                queue[rear++] = v;
            }
        }
    }
    
    // 返回距离起始节点最远的节点
    return farthestNode;
}

// 计算树的直径
// 树的直径定义：树中任意两点之间最长的简单路径
int treeDiameter() {
    // 第一次BFS，从节点1开始找到距离它最远的节点
    int farthestNode = bfs(1);
    
    // 第二次BFS，从第一次找到的最远节点开始BFS，找到真正的最远节点
    // 根据树的性质，这样找到的距离就是树的直径
    int diameterNode = bfs(farthestNode);
    
    // 返回直径（最远节点的距离）
    return dist[diameterNode];
}

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}