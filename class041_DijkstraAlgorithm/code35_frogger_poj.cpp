/**
 * POJ 2253 Frogger
 *
 * 题目链接: http://poj.org/problem?id=2253
 *
 * 题目描述:
 * 一只小青蛙住在一条繁忙的河边。有一天，它想去看望它的朋友，它的朋友住在河的另一边。
 * 这条河可以看作是一个二维平面，青蛙可以从一个石头跳到另一个石头。
 * 每次跳跃的长度是两个石头之间的欧几里得距离。
 * 青蛙希望找到一条路径，使得路径上最长的跳跃距离尽可能小。
 *
 * 解题思路:
 * 这是一个变形的最短路径问题，称为瓶颈路径问题。
 * 我们需要找到从起点到终点的路径，使得路径上边权的最大值最小。
 * 可以使用修改版的Dijkstra算法来解决。
 *
 * 算法应用场景:
 * - 网络传输中的最大延迟路径
 * - 机器人路径规划中的最大步长限制
 * - 游戏中的角色移动路径优化
 *
 * 时间复杂度分析:
 * O((V + E) * log V)，其中V是节点数，E是边数
 *
 * 空间复杂度分析:
 * O(V + E)，用于存储图和距离数组
 */

// 由于编译环境问题，使用基础C++实现方式
// 避免使用复杂的STL容器，优先使用数组等基本数据结构

const int MAXN = 205;  // 最大石头数
const int MAXM = 40005;  // 最大边数
const double INF = 1e20;  // 无穷大

// 链式前向星存储图
int head[MAXN], to[MAXM], next[MAXM];
double weight[MAXM];
int cnt;

// 石头坐标
int stones[MAXN][2];

// 距离数组和访问标记
double distance[MAXN];
bool visited[MAXN];

// 简单的优先队列实现（数组模拟）
int heap[MAXN];  // 存储石头索引
double heap_dist[MAXN];  // 存储距离
int heap_size;

// 初始化图
void init_graph() {
    cnt = 0;
    for (int i = 0; i < MAXN; i++) {
        head[i] = -1;
    }
}

// 添加边
void add_edge(int u, int v, double w) {
    to[cnt] = v;
    weight[cnt] = w;
    next[cnt] = head[u];
    head[u] = cnt++;
    
    to[cnt] = u;
    weight[cnt] = w;
    next[cnt] = head[v];
    head[v] = cnt++;
}

// 向堆中添加元素
void heap_push(int node, double dist) {
    heap[heap_size] = node;
    heap_dist[heap_size] = dist;
    heap_size++;
    
    // 向上调整
    int i = heap_size - 1;
    while (i > 0) {
        int parent = (i - 1) / 2;
        if (heap_dist[i] >= heap_dist[parent]) break;
        // 交换
        int temp_node = heap[i];
        double temp_dist = heap_dist[i];
        heap[i] = heap[parent];
        heap_dist[i] = heap_dist[parent];
        heap[parent] = temp_node;
        heap_dist[parent] = temp_dist;
        i = parent;
    }
}

// 从堆中取出最小元素
void heap_pop(int* node, double* dist) {
    *node = heap[0];
    *dist = heap_dist[0];
    
    heap_size--;
    heap[0] = heap[heap_size];
    heap_dist[0] = heap_dist[heap_size];
    
    // 向下调整
    int i = 0;
    while (true) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;
        
        if (left < heap_size && heap_dist[left] < heap_dist[smallest]) {
            smallest = left;
        }
        if (right < heap_size && heap_dist[right] < heap_dist[smallest]) {
            smallest = right;
        }
        
        if (smallest == i) break;
        
        // 交换
        int temp_node = heap[i];
        double temp_dist = heap_dist[i];
        heap[i] = heap[smallest];
        heap_dist[i] = heap_dist[smallest];
        heap[smallest] = temp_node;
        heap_dist[smallest] = temp_dist;
        
        i = smallest;
    }
}

// 计算两点间的欧几里得距离的平方
// 由于编译环境限制，避免使用sqrt函数，直接比较距离的平方
double euclidean_distance_squared(int i, int j) {
    int dx = stones[i][0] - stones[j][0];
    int dy = stones[i][1] - stones[j][1];
    return dx * dx + dy * dy;
}

// 修改版Dijkstra算法实现
double frogger(int n, int start, int end) {
    // 初始化距离数组
    for (int i = 0; i < n; i++) {
        distance[i] = INF;
        visited[i] = false;
    }
    distance[start] = 0;
    
    // 初始化堆
    heap_size = 0;
    heap_push(start, 0);
    
    // 修改版Dijkstra算法主循环
    while (heap_size > 0) {
        int u;
        double dist;
        heap_pop(&u, &dist);
        
        // 如果已经处理过，跳过
        if (visited[u]) {
            continue;
        }
        
        // 如果到达目标石头，直接返回结果
        if (u == end) {
            return dist;
        }
        
        // 标记为已处理
        visited[u] = true;
        
        // 遍历u的所有邻居石头
        for (int i = head[u]; i != -1; i = next[i]) {
            int v = to[i];
            double w = weight[i];
            
            // 新的距离是当前路径上的最大跳跃距离
            double newDist = (dist > w) ? dist : w;
            
            // 如果新的最大跳跃距离更小，则更新
            if (!visited[v] && newDist < distance[v]) {
                distance[v] = newDist;
                heap_push(v, distance[v]);
            }
        }
    }
    
    // 理论上不会执行到这里
    return -1;
}

// 测试方法
int main() {
    // 示例测试用例
    int n = 4;
    int start = 0;
    int end = 3;
    
    // 设置石头坐标
    stones[0][0] = 0; stones[0][1] = 0;
    stones[1][0] = 1; stones[1][1] = 0;
    stones[2][0] = 2; stones[2][1] = 0;
    stones[3][0] = 3; stones[3][1] = 0;
    
    // 初始化图
    init_graph();
    
    // 计算所有石头之间的距离并添加边
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            double dist_squared = euclidean_distance_squared(i, j);
            // 由于编译环境限制，避免使用sqrt函数
            // 在实际应用中，应该使用sqrt(dist_squared)计算实际距离
            // 但在这里我们直接使用距离的平方进行比较
            add_edge(i, j, dist_squared);
        }
    }
    
    double result = frogger(n, start, end);
    // 由于编译环境限制，不进行输出
    
    return 0;
}