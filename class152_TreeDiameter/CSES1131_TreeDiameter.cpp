// CSES 1131 Tree Diameter
// 题目：给定一棵树，求树的直径（树中任意两点间最长的简单路径）
// 来源：CSES Problem Set - Tree Algorithms
// 链接：https://cses.fi/problemset/task/1131

#define MAXN 200001
#define MAXM 400001

// 邻接表存储树
int head[MAXN], next[MAXM], to[MAXM], cnt;
int n;  // 节点数

// 队列实现BFS
int queue[MAXN], front, rear;
int visited[MAXN];

// Pair结构体，用于存储节点和距离
struct Pair {
    int node;
    int distance;
};

// 添加边
void addEdge(int u, int v) {
    next[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
}

// 初始化队列
void initQueue() {
    front = rear = 0;
}

// 入队
void enqueue(int x) {
    queue[rear++] = x;
}

// 出队
int dequeue() {
    return queue[front++];
}

// 判断队列是否为空
int isEmpty() {
    return front == rear;
}

// 获取队列大小
int queueSize() {
    return rear - front;
}

/**
 * BFS求从起点开始的最远节点
 * @param start 起点
 * @return Pair对象，包含最远节点和距离
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
struct Pair bfs(int start) {
    struct Pair result;
    
    // 初始化访问数组
    for (int i = 1; i <= n; i++) {
        visited[i] = 0;
    }
    
    initQueue();
    
    visited[start] = 1;
    enqueue(start);
    
    int lastNode = start;
    int maxDistance = 0;
    
    while (!isEmpty()) {
        int size = queueSize();
        for (int i = 0; i < size; i++) {
            int current = dequeue();
            lastNode = current;
            
            // 遍历当前节点的所有邻居
            for (int e = head[current]; e != 0; e = next[e]) {
                int neighbor = to[e];
                if (!visited[neighbor]) {
                    visited[neighbor] = 1;
                    enqueue(neighbor);
                }
            }
        }
        if (!isEmpty()) {
            maxDistance++;
        }
    }
    
    result.node = lastNode;
    result.distance = maxDistance;
    return result;
}

/**
 * 使用两次BFS法求树的直径
 * @return 树的直径
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
int findDiameter() {
    // 第一次BFS，从节点1开始找到最远节点
    struct Pair firstBFS = bfs(1);
    
    // 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
    struct Pair secondBFS = bfs(firstBFS.node);
    
    // 第二次BFS的距离就是树的直径
    return secondBFS.distance;
}

/**
 * 主方法
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
int main() {
    // 由于编译环境限制，这里只展示算法实现
    // 实际使用时需要根据具体环境添加输入输出代码
    
    // 示例：n = 4, 边为 1-2, 2-3, 3-4
    n = 4;
    cnt = 1;
    for (int i = 1; i <= n; i++) {
        head[i] = 0;
    }
    
    addEdge(1, 2);
    addEdge(2, 1);
    addEdge(2, 3);
    addEdge(3, 2);
    addEdge(3, 4);
    addEdge(4, 3);
    
    // 计算并输出树的直径
    // printf("%d\n", findDiameter()); // 应该输出3
    
    return 0;
}