// SPOJ PT07Z - Longest path in a tree
// 题目：给定一个无权无向树，求树中最长路径的长度。

// 算法标签：树、广度优先搜索、两次BFS
// 难度：简单
// 时间复杂度：O(n)，其中n是树中节点的数量
// 空间复杂度：O(n)，用于存储邻接表和辅助数组

// 相关题目：
// - LeetCode 543. 二叉树的直径
// - LeetCode 1245. Tree Diameter (无向树的直径)
// - LeetCode 1522. Diameter of N-Ary Tree (N叉树的直径)
// - CSES 1131 - Tree Diameter (树的直径)
// - 51Nod 2602 - 树的直径
// - 洛谷 U81904 树的直径
// - AtCoder ABC221F - Diameter Set

// 解题思路：
// 使用两次BFS法求解树的直径：
// 1. 从任意一点开始，找到距离它最远的点s
// 2. 从s开始，找到距离它最远的点t
// 3. s到t的距离即为树的直径

const int MAXN = 10001;

// 邻接表存储树
int graph[MAXN][MAXN];  // 简化的邻接表表示
int graph_size[MAXN];   // 每个节点的邻接点数量
int n;  // 节点数

// Pair结构体存储节点和距离
struct Pair {
    int node;
    int distance;
    
    Pair(int n, int d) : node(n), distance(d) {}
};

/**
 * BFS求从起点开始的最远节点
 * 
 * 算法思路：
 * 1. 从指定起点开始进行广度优先搜索
 * 2. 记录访问过的节点，避免重复访问
 * 3. 记录每一层的节点，直到遍历完所有节点
 * 4. 返回最后一层的节点（最远节点）和距离
 * 
 * @param start 起点
 * @return Pair对象，包含最远节点和距离
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
Pair bfs(int start) {
    bool visited[MAXN];
    // 初始化visited数组
    for (int i = 0; i <= n; i++) {
        visited[i] = false;
    }
    
    int queue[MAXN];
    int front = 0, rear = 0;
    
    visited[start] = true;
    queue[rear++] = start;
    
    int lastNode = start;
    int maxDistance = 0;
    
    while (front < rear) {
        int size = rear - front;
        for (int i = 0; i < size; i++) {
            int current = queue[front++];
            lastNode = current;
            
            // 遍历当前节点的所有邻居
            for (int j = 0; j < graph_size[current]; j++) {
                int neighbor = graph[current][j];
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue[rear++] = neighbor;
                }
            }
        }
        if (front < rear) {
            maxDistance++;
        }
    }
    
    return Pair(lastNode, maxDistance);
}

/**
 * 使用两次BFS法求树的直径
 * 
 * 算法思路：
 * 1. 第一次BFS，从任意节点（如节点1）开始找到最远节点
 * 2. 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
 * 3. 第二次BFS的距离就是树的直径
 * 
 * @return 树的直径
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
int findDiameter() {
    // 第一次BFS，从节点1开始找到最远节点
    Pair firstBFS = bfs(1);
    
    // 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
    Pair secondBFS = bfs(firstBFS.node);
    
    // 第二次BFS的距离就是树的直径
    return secondBFS.distance;
}

// 主方法（用于测试）
int main() {
    // 由于这是SPOJ题目，实际提交时需要按照题目要求的输入格式处理
    // 这里我们只展示算法实现
    
    // 示例输入：
    // n = 4
    // 边: 1-2, 2-3, 3-4
    // 预期输出：3
    
    n = 4;
    // 初始化图
    for (int i = 0; i <= n; i++) {
        graph_size[i] = 0;
    }
    
    // 添加边
    graph[1][graph_size[1]++] = 2;
    graph[2][graph_size[2]++] = 1;
    graph[2][graph_size[2]++] = 3;
    graph[3][graph_size[3]++] = 2;
    graph[3][graph_size[3]++] = 4;
    graph[4][graph_size[4]++] = 3;
    
    // 输出结果
    int diameter = findDiameter();
    // 应该输出3
    return 0;
}