// LeetCode 787. Cheapest Flights Within K Stops
// 题目链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
// 
// 题目描述:
// 有n个城市通过一些航班连接。给你一个数组flights，其中flights[i] = [from_i, to_i, price_i]，表示从from_i到to_i的航班，价格为price_i。
// 现在给定所有的城市和航班，以及出发城市src和目的地dst，你的任务是找到从src到dst最多经过k站中转的最便宜的价格。如果没有这样的路线，则返回-1。
//
// 解题思路:
// 这是一个带限制条件的单源最短路径问题，可以使用以下方法解决：
// 1. 广度优先搜索(BFS) + 动态规划：维护一个距离数组，记录到达每个城市的最短距离，同时记录中转次数
// 2. Bellman-Ford算法：对图进行k+1次松弛操作
//
// 这里我们实现两种方法
//
// 时间复杂度:
// BFS + 动态规划: O(k * E)，其中E是边数，k是最大中转次数
// Bellman-Ford: O(k * E)
// 空间复杂度: O(V)，其中V是顶点数
// 是否为最优解: 是，当有中转次数限制时，这些方法效率较高

#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <unordered_map>
using namespace std;

// 定义无穷大
const int INF = INT_MAX;

// 定义边的结构体
typedef pair<int, int> Edge; // (目标顶点, 价格)

int findCheapestPriceBFS(int n, vector<vector<int>>& flights, int src, int dst, int k) {
    // 构建邻接表表示的图
    unordered_map<int, vector<Edge>> graph;
    for (const auto& flight : flights) {
        int u = flight[0];
        int v = flight[1];
        int price = flight[2];
        graph[u].emplace_back(v, price);
    }
    
    // 初始化距离数组，记录到达每个城市的最低价格
    vector<int> prices(n, INF);
    prices[src] = 0;
    
    // 使用队列进行BFS，每个元素是(城市, 当前价格, 已中转次数)
    queue<vector<int>> q;
    q.push({src, 0, 0});
    
    while (!q.empty()) {
        auto current = q.front();
        q.pop();
        
        int city = current[0];
        int current_price = current[1];
        int stops = current[2];
        
        // 如果已经到达目的地或者中转次数超过k，跳过
        if (city == dst || stops > k) {
            continue;
        }
        
        // 遍历所有邻居
        if (graph.find(city) != graph.end()) {
            for (const Edge& edge : graph[city]) {
                int neighbor = edge.first;
                int price = edge.second;
                
                // 只有当新的价格更便宜时，才更新并加入队列
                if (prices[neighbor] > current_price + price) {
                    prices[neighbor] = current_price + price;
                    q.push({neighbor, prices[neighbor], stops + 1});
                }
            }
        }
    }
    
    // 如果目的地无法到达，返回-1
    return prices[dst] == INF ? -1 : prices[dst];
}

// 使用Bellman-Ford算法的实现
int findCheapestPriceBellmanFord(int n, vector<vector<int>>& flights, int src, int dst, int k) {
    // 初始化距离数组
    vector<int> prices(n, INF);
    prices[src] = 0;
    
    // 执行k+1次松弛操作（最多可以有k次中转，所以最多可以乘坐k+1次航班）
    for (int i = 0; i <= k; i++) {
        // 创建临时数组，避免在一次迭代中多次更新
        vector<int> temp_prices = prices;
        
        for (const auto& flight : flights) {
            int u = flight[0];
            int v = flight[1];
            int price = flight[2];
            
            if (prices[u] != INF && temp_prices[v] > prices[u] + price) {
                temp_prices[v] = prices[u] + price;
            }
        }
        
        prices = temp_prices;
    }
    
    return prices[dst] == INF ? -1 : prices[dst];
}

// 测试函数
void test() {
    // 测试用例1
    int n1 = 3;
    vector<vector<int>> flights1 = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
    int src1 = 0, dst1 = 2, k1 = 1;
    cout << "Test 1 (BFS): " << findCheapestPriceBFS(n1, flights1, src1, dst1, k1) << endl;  // 预期输出: 200
    cout << "Test 1 (Bellman-Ford): " << findCheapestPriceBellmanFord(n1, flights1, src1, dst1, k1) << endl;  // 预期输出: 200
    
    // 测试用例2
    int n2 = 3;
    vector<vector<int>> flights2 = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
    int src2 = 0, dst2 = 2, k2 = 0;
    cout << "Test 2 (BFS): " << findCheapestPriceBFS(n2, flights2, src2, dst2, k2) << endl;  // 预期输出: 500
    cout << "Test 2 (Bellman-Ford): " << findCheapestPriceBellmanFord(n2, flights2, src2, dst2, k2) << endl;  // 预期输出: 500
    
    // 测试用例3
    int n3 = 4;
    vector<vector<int>> flights3 = {{0, 1, 1}, {0, 2, 5}, {1, 2, 1}, {2, 3, 1}};
    int src3 = 0, dst3 = 3, k3 = 1;
    cout << "Test 3 (BFS): " << findCheapestPriceBFS(n3, flights3, src3, dst3, k3) << endl;  // 预期输出: 6
    cout << "Test 3 (Bellman-Ford): " << findCheapestPriceBellmanFord(n3, flights3, src3, dst3, k3) << endl;  // 预期输出: 6
}

int main() {
    test();
    return 0;
}