// Dijkstra算法与SPFA算法的实现与比较
// 
// 解题思路:
// Dijkstra算法适用于所有边权为非负数的图，使用优先队列优化，时间复杂度为O(E log V)
// SPFA算法是Bellman-Ford算法的队列优化版本，可以处理负权边，时间复杂度平均为O(E)，最坏情况下为O(VE)
// 当图中存在负权边时，Dijkstra算法可能会给出错误的结果，此时应使用SPFA算法
//
// 时间复杂度:
// Dijkstra: O(E log V)，其中E是边数，V是顶点数
// SPFA: 平均O(E)，最坏O(VE)
// 空间复杂度: O(V + E)
//
// 两种算法的应用场景:
// 1. 当图中所有边的权值都是非负数时，优先使用Dijkstra算法
// 2. 当图中存在负权边时，必须使用SPFA算法
// 3. SPFA还可以用来检测图中是否存在负权环

#include <iostream>
#include <vector>
#include <queue>
#include <climits>
using namespace std;

// 定义无穷大
const int INF = INT_MAX;

// 定义边的结构体
typedef pair<int, int> Edge; // (邻居顶点, 权重)

vector<int> dijkstra(const vector<vector<Edge>>& graph, int start) {
    """
    Dijkstra算法实现 - 使用优先队列优化
    参数:
        graph: 图的邻接表表示
        start: 起始顶点
    返回:
        从起始顶点到所有顶点的最短距离
    """
    int n = graph.size();
    vector<int> distances(n, INF);
    vector<bool> visited(n, false);
    
    // 优先队列，存储(距离, 顶点)，按照距离从小到大排序
    // 使用greater<pair<int, int>>实现最小堆
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    
    distances[start] = 0;
    pq.push({0, start});
    
    while (!pq.empty()) {
        int current_dist = pq.top().first;
        int current_vertex = pq.top().second;
        pq.pop();
        
        // 如果该顶点已经处理过，跳过
        if (visited[current_vertex]) {
            continue;
        }
        
        visited[current_vertex] = true;
        
        // 遍历所有邻居
        for (const Edge& edge : graph[current_vertex]) {
            int neighbor = edge.first;
            int weight = edge.second;
            
            // 如果通过当前顶点可以得到更短的路径
            if (!visited[neighbor] && distances[current_vertex] != INF && 
                distances[neighbor] > distances[current_vertex] + weight) {
                distances[neighbor] = distances[current_vertex] + weight;
                pq.push({distances[neighbor], neighbor});
            }
        }
    }
    
    return distances;
}

vector<int> spfa(const vector<vector<Edge>>& graph, int start, bool& has_negative_cycle) {
    """
    SPFA算法实现 - Bellman-Ford算法的队列优化版本
    参数:
        graph: 图的邻接表表示
        start: 起始顶点
        has_negative_cycle: 输出参数，表示图中是否存在负权环
    返回:
        从起始顶点到所有顶点的最短距离
    """
    int n = graph.size();
    vector<int> distances(n, INF);
    vector<bool> in_queue(n, false);
    vector<int> count(n, 0); // 记录每个顶点的入队次数
    queue<int> q;
    
    distances[start] = 0;
    q.push(start);
    in_queue[start] = true;
    count[start] = 1;
    has_negative_cycle = false;
    
    while (!q.empty() && !has_negative_cycle) {
        int current_vertex = q.front();
        q.pop();
        in_queue[current_vertex] = false;
        
        // 遍历所有邻居
        for (const Edge& edge : graph[current_vertex]) {
            int neighbor = edge.first;
            int weight = edge.second;
            
            // 如果通过当前顶点可以得到更短的路径
            if (distances[current_vertex] != INF && 
                distances[neighbor] > distances[current_vertex] + weight) {
                distances[neighbor] = distances[current_vertex] + weight;
                
                if (!in_queue[neighbor]) {
                    q.push(neighbor);
                    in_queue[neighbor] = true;
                    count[neighbor]++;
                    
                    // 如果一个顶点的入队次数超过n，说明存在负权环
                    if (count[neighbor] > n) {
                        has_negative_cycle = true;
                        break;
                    }
                }
            }
        }
    }
    
    return distances;
}

// 打印距离数组
void printDistances(const vector<int>& distances) {
    cout << "[";
    for (size_t i = 0; i < distances.size(); i++) {
        if (distances[i] == INF) {
            cout << "INF";
        } else {
            cout << distances[i];
        }
        if (i < distances.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

int main() {
    // 测试用例1: 所有边权为正的图
    vector<vector<Edge>> graph1 = {
        {{1, 4}, {2, 2}},  // 顶点0的邻居
        {{3, 2}, {2, 5}},  // 顶点1的邻居
        {{3, 1}},          // 顶点2的邻居
        {}                 // 顶点3的邻居
    };
    int start1 = 0;
    cout << "Test 1 (all positive weights):" << endl;
    cout << "Dijkstra result: ";
    printDistances(dijkstra(graph1, start1));
    
    bool has_cycle1;
    vector<int> spfa_result1 = spfa(graph1, start1, has_cycle1);
    cout << "SPFA result: ";
    printDistances(spfa_result1);
    cout << "Has negative cycle: " << (has_cycle1 ? "true" : "false") << endl << endl;
    
    // 测试用例2: 包含负权边的图
    vector<vector<Edge>> graph2 = {
        {{1, 4}, {2, 2}},  // 顶点0的邻居
        {{3, 2}, {2, -5}}, // 顶点1的邻居 (注意这里有负权边)
        {{3, 1}},          // 顶点2的邻居
        {}                 // 顶点3的邻居
    };
    int start2 = 0;
    cout << "Test 2 (with negative weight):" << endl;
    // Dijkstra算法在有负权边的情况下可能会给出错误结果
    cout << "Dijkstra result: ";
    printDistances(dijkstra(graph2, start2));
    
    bool has_cycle2;
    vector<int> spfa_result2 = spfa(graph2, start2, has_cycle2);
    cout << "SPFA result: ";
    printDistances(spfa_result2);
    cout << "Has negative cycle: " << (has_cycle2 ? "true" : "false") << endl << endl;
    
    // 测试用例3: 包含负权环的图
    vector<vector<Edge>> graph3 = {
        {{1, 4}},          // 顶点0的邻居
        {{2, 2}},          // 顶点1的邻居
        {{1, -5}, {3, 1}}, // 顶点2的邻居 (1->2->1形成负权环)
        {}                 // 顶点3的邻居
    };
    int start3 = 0;
    cout << "Test 3 (with negative cycle):" << endl;
    // Dijkstra算法在有负权环的情况下会给出错误结果
    cout << "Dijkstra result: ";
    printDistances(dijkstra(graph3, start3));
    
    bool has_cycle3;
    spfa(graph3, start3, has_cycle3);
    cout << "SPFA detected negative cycle: " << (has_cycle3 ? "true" : "false") << endl;
    
    return 0;
}