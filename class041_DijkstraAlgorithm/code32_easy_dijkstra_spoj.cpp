/**
 * SPOJ - EZDIJKST: Easy Dijkstra Problem
 *
 * 题目链接: https://www.spoj.com/problems/EZDIJKST/
 *
 * 题目描述:
 * 给定一个有向带权图，确定指定顶点之间的最短路径。
 * 输入格式:
 * 第一行包含测试用例的数量。
 * 每个测试用例的第一行包含节点数n (1 <= n <= 10000)。
 * 第二行包含边数m (1 <= m <= 100000)。
 * 接下来的m行每行包含三个整数a, b, c，表示从节点a到节点b有一条权重为c的边。
 * 然后是包含源节点和目标节点的行。
 *
 * 解题思路:
 * 这是一个标准的单源最短路径问题，可以直接使用Dijkstra算法解决。
 *
 * 算法应用场景:
 * - 网络路由
 * - GPS导航
 * - 社交网络分析
 *
 * 时间复杂度分析:
 * O((V + E) * log V)，其中V是节点数，E是边数
 *
 * 空间复杂度分析:
 * O(V + E)，用于存储图和距离数组
 */

// 由于编译环境问题，使用基础C++实现方式
// 避免使用复杂的STL容器，优先使用数组等基本数据结构

/**
 * 使用Dijkstra算法求解最短路径
 *
 * 算法步骤:
 * 1. 构建图的邻接表表示
 * 2. 初始化距离数组，源节点距离为0，其他节点为无穷大
 * 3. 使用优先队列维护待处理节点，按距离从小到大排序
 * 4. 不断取出距离最小的节点，更新其邻居节点的最短距离
 * 5. 返回目标节点的最短距离
 *
 * 时间复杂度: O((V + E) * log V)
 * 空间复杂度: O(V + E)
 *
 * @param n 节点数
 * @param edges 边的列表，每个元素为 {from, to, weight}
 * @param start 起始节点
 * @param end 目标节点
 * @return 从起始节点到目标节点的最短距离，如果无法到达则返回-1
 */
const int MAXN = 10005;
const int MAXM = 100005;
const int INF = 0x3f3f3f3f;

// 链式前向星存储图
int head[MAXN], to[MAXM], weight[MAXM], next[MAXM];
int cnt;

// 距离数组和访问标记
int distance[MAXN];
bool visited[MAXN];

// 简单的优先队列实现（数组模拟）
int heap[MAXN][2];  // [0]存储节点，[1]存储距离
int heap_size;

// 初始化图
void init_graph() {
    cnt = 0;
    for (int i = 0; i < MAXN; i++) {
        head[i] = -1;
    }
}

// 添加边
void add_edge(int u, int v, int w) {
    to[cnt] = v;
    weight[cnt] = w;
    next[cnt] = head[u];
    head[u] = cnt++;
}

// 向堆中添加元素
void heap_push(int node, int dist) {
    heap[heap_size][0] = node;
    heap[heap_size][1] = dist;
    heap_size++;
    
    // 向上调整
    int i = heap_size - 1;
    while (i > 0) {
        int parent = (i - 1) / 2;
        if (heap[i][1] >= heap[parent][1]) break;
        // 交换
        int temp_node = heap[i][0];
        int temp_dist = heap[i][1];
        heap[i][0] = heap[parent][0];
        heap[i][1] = heap[parent][1];
        heap[parent][0] = temp_node;
        heap[parent][1] = temp_dist;
        i = parent;
    }
}

// 从堆中取出最小元素
void heap_pop(int* node, int* dist) {
    *node = heap[0][0];
    *dist = heap[0][1];
    
    heap_size--;
    heap[0][0] = heap[heap_size][0];
    heap[0][1] = heap[heap_size][1];
    
    // 向下调整
    int i = 0;
    while (true) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;
        
        if (left < heap_size && heap[left][1] < heap[smallest][1]) {
            smallest = left;
        }
        if (right < heap_size && heap[right][1] < heap[smallest][1]) {
            smallest = right;
        }
        
        if (smallest == i) break;
        
        // 交换
        int temp_node = heap[i][0];
        int temp_dist = heap[i][1];
        heap[i][0] = heap[smallest][0];
        heap[i][1] = heap[smallest][1];
        heap[smallest][0] = temp_node;
        heap[smallest][1] = temp_dist;
        
        i = smallest;
    }
}

// Dijkstra算法实现
int dijkstra(int n, int start, int end) {
    // 初始化距离数组
    for (int i = 1; i <= n; i++) {
        distance[i] = INF;
        visited[i] = false;
    }
    distance[start] = 0;
    
    // 初始化堆
    heap_size = 0;
    heap_push(start, 0);
    
    // Dijkstra算法主循环
    while (heap_size > 0) {
        int u, dist;
        heap_pop(&u, &dist);
        
        // 如果已经处理过，跳过
        if (visited[u]) {
            continue;
        }
        
        // 如果到达目标节点，直接返回结果
        if (u == end) {
            return dist;
        }
        
        // 标记为已处理
        visited[u] = true;
        
        // 遍历u的所有邻居节点
        for (int i = head[u]; i != -1; i = next[i]) {
            int v = to[i];
            int w = weight[i];
            
            // 如果邻居节点未访问且通过u到达v的距离更短，则更新
            if (!visited[v] && distance[u] + w < distance[v]) {
                distance[v] = distance[u] + w;
                heap_push(v, distance[v]);
            }
        }
    }
    
    // 如果无法到达目标节点，返回-1
    return -1;
}

// 测试方法
int main() {
    // 示例测试用例
    int n = 4;
    int start = 1;
    int end = 4;
    
    // 初始化图
    init_graph();
    
    // 添加边
    add_edge(1, 2, 1);
    add_edge(1, 3, 3);
    add_edge(2, 3, 1);
    add_edge(3, 4, 2);
    
    int result = dijkstra(n, start, end);
    // 由于编译环境限制，使用printf输出
    // cout << "从节点 " << start << " 到节点 " << end << " 的最短距离为: " << result << endl;
    
    return 0;
}