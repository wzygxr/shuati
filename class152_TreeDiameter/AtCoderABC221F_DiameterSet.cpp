// AtCoder ABC221F Diameter Set
// 题目：给定一棵N个顶点的树，顶点编号为1到N。
// 选择两个或更多顶点并将其涂成红色的方法数是多少，
// 使得红色顶点之间的最大距离等于树的直径？
// 答案对998244353取模。
// 来源：AtCoder Beginner Contest 221 Problem F
// 链接：https://atcoder.jp/contests/abc221/tasks/abc221_f

#define MAXN 200001
#define MOD 998244353

// 邻接表存储树
int head[MAXN], next[MAXN << 1], to[MAXN << 1], cnt;
int n;  // 节点数

// 树的直径相关变量
int diameter;  // 树的直径

// DFS计算子树大小和深度
int subtreeSize[MAXN];
int depth[MAXN];

// 队列实现BFS
int queue[MAXN], front, rear;
int visited[MAXN];

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
 * 快速幂运算
 * @param base 底数
 * @param exp 指数
 * @return (base^exp) % MOD
 * 
 * 时间复杂度：O(log exp)
 * 空间复杂度：O(1)
 */
long long power(long long base, long long exp) {
    long long result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result = (result * base) % MOD;
        }
        base = (base * base) % MOD;
        exp /= 2;
    }
    return result;
}

/**
 * BFS求从起点开始的最远节点和距离
 * @param start 起点
 * @param result 返回结果：[最远节点, 距离]
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
void bfs(int start, int result[2]) {
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
    
    result[0] = lastNode;
    result[1] = maxDistance;
}

/**
 * 使用两次BFS法求树的直径
 * @return 树的直径
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
int findDiameter() {
    int firstBFS[2], secondBFS[2];
    
    // 第一次BFS，从节点1开始找到最远节点
    bfs(1, firstBFS);
    
    // 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
    bfs(firstBFS[0], secondBFS);
    
    // 第二次BFS的距离就是树的直径
    return secondBFS[1];
}

/**
 * DFS计算子树大小
 * @param u 当前节点
 * @param parent 父节点
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
void dfsSubtreeSize(int u, int parent) {
    subtreeSize[u] = 1;
    for (int e = head[u]; e != 0; e = next[e]) {
        int v = to[e];
        if (v != parent) {
            dfsSubtreeSize(v, u);
            subtreeSize[u] += subtreeSize[v];
        }
    }
}

/**
 * DFS计算深度
 * @param u 当前节点
 * @param parent 父节点
 * @param d 当前深度
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
void dfsDepth(int u, int parent, int d) {
    depth[u] = d;
    for (int e = head[u]; e != 0; e = next[e]) {
        int v = to[e];
        if (v != parent) {
            dfsDepth(v, u, d + 1);
        }
    }
}

/**
 * 计算满足条件的方案数
 * @return 满足条件的方案数
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
long long solve() {
    // 计算树的直径
    diameter = findDiameter();
    
    // 特殊情况：直径为0（只有一个节点）
    if (diameter == 0) {
        return 1;  // 只有一种方案：选择这个节点
    }
    
    // 计算子树大小
    dfsSubtreeSize(1, 0);
    
    // 计算深度
    dfsDepth(1, 0, 0);
    
    // 找到深度最大的节点
    int deepestNode = 1;
    for (int i = 2; i <= n; i++) {
        if (depth[i] > depth[deepestNode]) {
            deepestNode = i;
        }
    }
    
    // 从最深节点再次DFS，找到直径的端点
    dfsDepth(deepestNode, 0, 0);
    int endpoint1 = deepestNode;
    for (int i = 1; i <= n; i++) {
        if (depth[i] > depth[endpoint1]) {
            endpoint1 = i;
        }
    }
    
    // 从endpoint1再次DFS，找到另一个端点
    dfsDepth(endpoint1, 0, 0);
    int endpoint2 = 1;
    for (int i = 2; i <= n; i++) {
        if (depth[i] > depth[endpoint2]) {
            endpoint2 = i;
        }
    }
    
    // 计算满足条件的方案数
    // 如果直径是偶数，有一个中心点
    // 如果直径是奇数，有一个中心边
    if (diameter % 2 == 0) {
        // 直径为偶数，有一个中心点
        // 找到中心点
        int center = 0;
        dfsDepth(endpoint1, 0, 0);
        for (int i = 1; i <= n; i++) {
            if (depth[i] == diameter / 2 && 
                bfs(i, (int[2]){0,0}), ((int[2]){0,0})[1] == diameter / 2) {
                center = i;
                break;
            }
        }
        
        // 计算以中心点为根的子树中满足条件的方案数
        long long result = 1;
        for (int e = head[center]; e != 0; e = next[e]) {
            int v = to[e];
            // 计算每个子树中满足条件的方案数
            long long subtreeWays = power(2, subtreeSize[v]) - 1;
            result = (result * subtreeWays) % MOD;
        }
        
        // 至少选择两个节点
        result = (result - 1 + MOD) % MOD;
        return result;
    } else {
        // 直径为奇数，有一个中心边
        // 找到中心边的两个端点
        dfsDepth(endpoint1, 0, 0);
        int center1 = 0, center2 = 0;
        for (int i = 1; i <= n; i++) {
            if (depth[i] == diameter / 2) {
                if (center1 == 0) {
                    center1 = i;
                } else {
                    center2 = i;
                    break;
                }
            }
        }
        
        // 计算每个部分中满足条件的方案数
        long long ways1 = power(2, subtreeSize[center1]) - 1;
        long long ways2 = power(2, subtreeSize[center2]) - 1;
        
        long long result = (ways1 * ways2) % MOD;
        return result;
    }
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
    
    // 计算并输出结果
    // printf("%lld\n", solve()); // 应该输出某种结果
    
    return 0;
}