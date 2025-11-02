/**
 * ============================================================================
 * 多源最短路径问题 - Dijkstra算法扩展（C++实现）
 * ============================================================================
 * 
 * 题目：多源最短路径（Multi-Source Shortest Path）
 * 来源：各大算法平台通用问题，如LeetCode、Codeforces、洛谷等
 * 
 * 题目描述：
 * 给定一个带权有向图，包含n个节点和m条边，同时给定k个源点，需要计算从每个源点到
 * 所有其他节点的最短距离。
 * 
 * 输入格式：
 * - n: 节点数量，编号从1到n
 * - edges: 边列表，每条边格式为{u, v, w}，表示从u到v的有向边，权重为w
 * - sources: 源点列表，包含k个源点编号
 * 
 * 输出格式：
 * - 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
 * - 如果节点不可达，则距离为INT_MAX
 * 
 * 编译要求：
 * - C++11及以上标准
 * - 包含必要的标准库头文件
 */

#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <functional>
using namespace std;

/**
 * 方法1：对每个源点单独运行Dijkstra算法
 * 
 * 算法步骤：
 * 1. 对于每个源点，运行标准的Dijkstra算法
 * 2. 记录从该源点到所有其他节点的最短距离
 * 3. 返回所有源点的最短距离矩阵
 * 
 * 时间复杂度：O(K*(V+E)logV)
 * 空间复杂度：O(K*V)
 * 
 * @param n 节点总数
 * @param edges 边列表，格式为 {u, v, w}
 * @param sources 源点列表
 * @return 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
 */
vector<vector<int>> multiSourceDijkstra1(int n, vector<vector<int>>& edges, vector<int>& sources) {
    // 构建邻接表表示的图
    vector<vector<pair<int, int>>> graph(n + 1);
    
    for (auto& edge : edges) {
        int u = edge[0];
        int v = edge[1];
        int w = edge[2];
        graph[u].push_back({v, w});
    }
    
    // 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
    vector<vector<int>> dist(sources.size(), vector<int>(n + 1, INT_MAX));
    
    // 对每个源点运行Dijkstra算法
    for (int idx = 0; idx < sources.size(); idx++) {
        int source = sources[idx];
        vector<int> distance(n + 1, INT_MAX);
        distance[source] = 0;
        
        vector<bool> visited(n + 1, false);
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
        heap.push({0, source});
        
        while (!heap.empty()) {
            auto top = heap.top();
            int dist_val = top.first;
            int u = top.second;
            heap.pop();
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (auto& edge : graph[u]) {
                int v = edge.first;
                int w = edge.second;
                if (!visited[v] && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    heap.push({distance[v], v});
                }
            }
        }
        
        // 存储当前源点的距离数组
        dist[idx] = distance;
    }
    
    return dist;
}

/**
 * 方法2：虚拟超级源点法
 * 
 * 算法步骤：
 * 1. 创建一个虚拟源点0，连接到所有实际源点，边权为0
 * 2. 从虚拟源点0运行Dijkstra算法
 * 3. 得到的距离数组就是从虚拟源点到各点的最短距离
 * 4. 由于虚拟源点到实际源点的距离为0，所以这等价于多源最短路径
 * 
 * 时间复杂度：O((V+E)logV)
 * 空间复杂度：O(V+E)
 * 
 * @param n 节点总数
 * @param edges 边列表，格式为 {u, v, w}
 * @param sources 源点列表
 * @return 距离数组，dist[i]表示从最近的源点到节点i的最短距离
 */
vector<int> multiSourceDijkstra2(int n, vector<vector<int>>& edges, vector<int>& sources) {
    // 构建扩展图（包含虚拟源点0）
    vector<vector<pair<int, int>>> graph(n + 1);
    
    // 添加原始边
    for (auto& edge : edges) {
        int u = edge[0];
        int v = edge[1];
        int w = edge[2];
        graph[u].push_back({v, w});
    }
    
    // 添加虚拟源点到所有实际源点的边（权重为0）
    for (int source : sources) {
        graph[0].push_back({source, 0});
    }
    
    // 从虚拟源点0运行Dijkstra算法
    vector<int> distance(n + 1, INT_MAX);
    distance[0] = 0;
    
    vector<bool> visited(n + 1, false);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    heap.push({0, 0});
    
    while (!heap.empty()) {
        auto top = heap.top();
        int dist_val = top.first;
        int u = top.second;
        heap.pop();
        
        if (visited[u]) continue;
        visited[u] = true;
        
        for (auto& edge : graph[u]) {
            int v = edge.first;
            int w = edge.second;
            if (!visited[v] && distance[u] + w < distance[v]) {
                distance[v] = distance[u] + w;
                heap.push({distance[v], v});
            }
        }
    }
    
    return distance;
}

/**
 * 方法3：多源最短路径的优化版本（使用链式前向星）
 * 
 * 算法优化点：
 * 1. 使用链式前向星存储图，节省空间
 * 2. 支持大规模图的快速计算
 * 
 * 时间复杂度：O((V+E)logV)
 * 空间复杂度：O(V+E)
 */
class OptimizedMultiSourceDijkstra {
private:
    static const int MAXN = 100005;
    static const int MAXM = 200005;
    
    // 链式前向星数据结构
    int head[MAXN];
    int next[MAXM];
    int to[MAXM];
    int weight[MAXM];
    int cnt;
    
    // 距离数组
    int distance[MAXN];
    
public:
    /**
     * 初始化数据结构
     */
    void build(int n) {
        cnt = 1;
        fill(head, head + n + 2, 0); // n+2因为包含虚拟源点0
        fill(distance, distance + n + 2, INT_MAX);
    }
    
    /**
     * 添加边
     */
    void addEdge(int u, int v, int w) {
        next[cnt] = head[u];
        to[cnt] = v;
        weight[cnt] = w;
        head[u] = cnt++;
    }
    
    /**
     * 多源最短路径计算
     */
    vector<int> calculate(int n, vector<vector<int>>& edges, vector<int>& sources) {
        build(n);
        
        // 添加原始边
        for (auto& edge : edges) {
            addEdge(edge[0], edge[1], edge[2]);
        }
        
        // 添加虚拟源点到实际源点的边
        for (int source : sources) {
            addEdge(0, source, 0);
        }
        
        // 从虚拟源点开始Dijkstra算法
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
        distance[0] = 0;
        heap.push({0, 0});
        
        vector<bool> visited(n + 2, false);
        
        while (!heap.empty()) {
            auto top = heap.top();
            int dist_val = top.first;
            int u = top.second;
            heap.pop();
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int ei = head[u]; ei > 0; ei = next[ei]) {
                int v = to[ei];
                int w = weight[ei];
                
                if (!visited[v] && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    heap.push({distance[v], v});
                }
            }
        }
        
        return vector<int>(distance + 1, distance + n + 1);
    }
};

// 测试用例
int main() {
    // 测试用例1：简单多源最短路径
    int n1 = 4;
    vector<vector<int>> edges1 = {
        {1, 2, 1}, {2, 3, 2}, {3, 4, 3},
        {1, 3, 4}, {2, 4, 5}
    };
    vector<int> sources1 = {1, 3}; // 两个源点：1和3
    
    cout << "=== 测试用例1 ===" << endl;
    cout << "方法1结果（单独Dijkstra）：" << endl;
    auto result1 = multiSourceDijkstra1(n1, edges1, sources1);
    for (int i = 0; i < sources1.size(); i++) {
        cout << "从源点" << sources1[i] << "到各点的距离: ";
        for (int j = 1; j <= n1; j++) {
            cout << result1[i][j] << " ";
        }
        cout << endl;
    }
    
    cout << "方法2结果（虚拟源点法）：" << endl;
    auto result2 = multiSourceDijkstra2(n1, edges1, sources1);
    cout << "从最近源点到各点的距离: ";
    for (int j = 1; j <= n1; j++) {
        cout << result2[j] << " ";
    }
    cout << endl;
    
    cout << "方法3结果（优化版本）：" << endl;
    OptimizedMultiSourceDijkstra optimizer;
    auto result3 = optimizer.calculate(n1, edges1, sources1);
    cout << "从最近源点到各点的距离: ";
    for (int dist : result3) {
        cout << dist << " ";
    }
    cout << endl;
    
    // 性能对比分析
    cout << "\n=== 性能分析 ===" << endl;
    cout << "方法1：适合源点数量较少的情况，实现简单" << endl;
    cout << "方法2：适合源点数量较多的情况，效率更高" << endl;
    cout << "方法3：适合大规模图，内存使用更优" << endl;
    
    return 0;
}