// LeetCode 743. Network Delay Time
// 题目链接: https://leetcode.cn/problems/network-delay-time/
// 
// 题目描述:
// 有n个网络节点，标记为1到n。给你一个列表times，表示信号经过有向边的传递时间。times[i] = (u_i, v_i, w_i)，其中u_i是源节点，v_i是目标节点，w_i是一个信号从源节点传递到目标节点的时间。
// 现在，从某个节点k发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回-1。
//
// 解题思路:
// 这是一个典型的单源最短路径问题，可以使用Dijkstra算法来解决，因为所有边的权值都是正数（传递时间）。
// 我们需要找到从节点k到所有其他节点的最短路径，然后取其中的最大值作为答案。
// 如果有节点无法到达，则返回-1。
//
// 时间复杂度: O(E log V)，其中E是边数，V是顶点数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，Dijkstra算法是解决带权有向图中单源最短路径问题的高效算法

#include <iostream>
#include <vector>
#include <queue>
#include <climits>
using namespace std;

// 定义无穷大
const int INF = INT_MAX;

// 定义边的结构体
typedef pair<int, int> Edge; // (目标顶点, 权重)

int networkDelayTime(vector<vector<int>>& times, int n, int k) {
    // 构建邻接表表示的图
    vector<vector<Edge>> graph(n + 1);
    for (const auto& time : times) {
        int u = time[0];
        int v = time[1];
        int w = time[2];
        graph[u].emplace_back(v, w);
    }
    
    // 使用Dijkstra算法计算从k到所有节点的最短路径
    vector<int> distances(n + 1, INF);
    vector<bool> visited(n + 1, false);
    
    // 优先队列，存储(距离, 节点)，按照距离从小到大排序
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    
    distances[k] = 0;
    pq.emplace(0, k);
    
    while (!pq.empty()) {
        int current_dist = pq.top().first;
        int current_node = pq.top().second;
        pq.pop();
        
        // 如果该节点已经处理过，跳过
        if (visited[current_node]) {
            continue;
        }
        
        visited[current_node] = true;
        
        // 遍历所有邻居
        for (const Edge& edge : graph[current_node]) {
            int neighbor = edge.first;
            int weight = edge.second;
            
            // 如果通过当前节点可以得到更短的路径
            if (!visited[neighbor] && distances[current_node] != INF && 
                distances[neighbor] > distances[current_node] + weight) {
                distances[neighbor] = distances[current_node] + weight;
                pq.emplace(distances[neighbor], neighbor);
            }
        }
    }
    
    // 找到所有节点中最大的最短距离
    int max_distance = 0;
    for (int i = 1; i <= n; i++) {
        if (distances[i] == INF) {
            return -1; // 有节点无法到达
        }
        max_distance = max(max_distance, distances[i]);
    }
    
    return max_distance;
}

// 使用SPFA算法的实现
int networkDelayTimeSPFA(vector<vector<int>>& times, int n, int k) {
    // 构建邻接表表示的图
    vector<vector<Edge>> graph(n + 1);
    for (const auto& time : times) {
        int u = time[0];
        int v = time[1];
        int w = time[2];
        graph[u].emplace_back(v, w);
    }
    
    // 初始化距离数组
    vector<int> distances(n + 1, INF);
    vector<bool> in_queue(n + 1, false);
    queue<int> q;
    
    distances[k] = 0;
    q.push(k);
    in_queue[k] = true;
    
    while (!q.empty()) {
        int current_node = q.front();
        q.pop();
        in_queue[current_node] = false;
        
        // 遍历所有邻居
        for (const Edge& edge : graph[current_node]) {
            int neighbor = edge.first;
            int weight = edge.second;
            
            // 如果通过当前节点可以得到更短的路径
            if (distances[current_node] != INF && 
                distances[neighbor] > distances[current_node] + weight) {
                distances[neighbor] = distances[current_node] + weight;
                
                if (!in_queue[neighbor]) {
                    q.push(neighbor);
                    in_queue[neighbor] = true;
                }
            }
        }
    }
    
    // 找到所有节点中最大的最短距离
    int max_distance = 0;
    for (int i = 1; i <= n; i++) {
        if (distances[i] == INF) {
            return -1; // 有节点无法到达
        }
        max_distance = max(max_distance, distances[i]);
    }
    
    return max_distance;
}

// 测试函数
void test() {
    // 测试用例1
    vector<vector<int>> times1 = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
    int n1 = 4, k1 = 2;
    cout << "Test 1 (Dijkstra): " << networkDelayTime(times1, n1, k1) << endl;  // 预期输出: 2
    cout << "Test 1 (SPFA): " << networkDelayTimeSPFA(times1, n1, k1) << endl;  // 预期输出: 2
    
    // 测试用例2
    vector<vector<int>> times2 = {{1, 2, 1}};
    int n2 = 2, k2 = 1;
    cout << "Test 2 (Dijkstra): " << networkDelayTime(times2, n2, k2) << endl;  // 预期输出: 1
    cout << "Test 2 (SPFA): " << networkDelayTimeSPFA(times2, n2, k2) << endl;  // 预期输出: 1
    
    // 测试用例3 - 有节点无法到达
    vector<vector<int>> times3 = {{1, 2, 1}};
    int n3 = 3, k3 = 1;
    cout << "Test 3 (Dijkstra): " << networkDelayTime(times3, n3, k3) << endl;  // 预期输出: -1
    cout << "Test 3 (SPFA): " << networkDelayTimeSPFA(times3, n3, k3) << endl;  // 预期输出: -1
}

int main() {
    test();
    return 0;
}