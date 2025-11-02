/**
 * UVa 10986 - Sending email
 *
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1927
 *
 * 题目描述:
 * 现在几乎每个人都有电子邮件地址，这使得两个不同地点的人们可以快速交流。
 * 你的任务是确定从一个城市发送电子邮件到另一个城市的最短时间。
 *
 * 解题思路:
 * 这是一个标准的单源最短路径问题，使用Dijkstra算法解决。
 *
 * 算法应用场景:
 * - 网络通信
 * - 交通路线规划
 * - 物流配送优化
 *
 * 时间复杂度分析:
 * O((V + E) * log V)，其中V是节点数，E是边数
 *
 * 空间复杂度分析:
 * O(V + E)，用于存储图和距离数组
 */

// 由于编译环境问题，使用基础C++实现方式
// 避免使用复杂的STL容器，优先使用数组等基本数据结构

const int MAXN = 20005;  // 最大城市数
const int MAXM = 100005;  // 最大道路数
const int INF = 0x3f3f3f3f;  // 无穷大

// 链式前向星存储图
int head[MAXN], to[MAXM], weight[MAXM], next[MAXM];
int cnt;

// 距离数组和访问标记
int distance[MAXN];
bool visited[MAXN];

// 简单的优先队列实现（数组模拟）
int heap[MAXN][2];  // [0]存储城市，[1]存储时间
int heap_size;

// 初始化图
void init_graph() {
    cnt = 0;
    for (int i = 0; i < MAXN; i++) {
        head[i] = -1;
    }
}

// 添加道路（无向图需要添加两条边）
void add_edge(int u, int v, int w) {
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
int sendEmail(int n, int start, int end) {
    // 初始化距离数组
    for (int i = 0; i < n; i++) {
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
        
        // 如果到达目标城市，直接返回结果
        if (u == end) {
            return dist;
        }
        
        // 标记为已处理
        visited[u] = true;
        
        // 遍历u的所有邻居城市
        for (int i = head[u]; i != -1; i = next[i]) {
            int v = to[i];
            int w = weight[i];
            
            // 如果邻居城市未访问且通过u到达v的时间更短，则更新
            if (!visited[v] && distance[u] + w < distance[v]) {
                distance[v] = distance[u] + w;
                heap_push(v, distance[v]);
            }
        }
    }
    
    // 如果无法到达目标城市，返回-1
    return -1;
}

// 测试方法
int main() {
    // 示例测试用例
    int n = 4;
    int start = 0;
    int end = 3;
    
    // 初始化图
    init_graph();
    
    // 添加道路
    add_edge(0, 1, 1);
    add_edge(0, 2, 3);
    add_edge(1, 2, 1);
    add_edge(2, 3, 2);
    
    int result = sendEmail(n, start, end);
    // 由于编译环境限制，不进行输出
    
    return 0;
}