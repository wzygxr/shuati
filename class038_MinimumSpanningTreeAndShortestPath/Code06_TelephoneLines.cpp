// 洛谷P1967 [NOIP2013 提高组] 电话线路 - 二分答案+最小生成树
// 题目链接: https://www.luogu.com.cn/problem/P1967
// 
// 题目描述:
// 给出一个农村，共有n个村庄，编号1到n。村庄之间有m条无向道路，每条道路有不同的长度。
// 现在需要从村庄1铺设电话线路到村庄n，其中一部分道路的电线杆需要升级才能承载光纤电缆，升级费用与道路长度成正比。
// 电信公司可以免费升级k条道路的电线杆。我们的目标是在满足条件的情况下，找到一条路径，使得路径上需要自己付费升级的最长道路的长度尽可能小。
//
// 解题思路:
// 二分答案 + BFS方法：
// 1. 二分查找可能的最长付费道路长度mid
// 2. 将每条道路分类：长度>mid的需要自己付费，长度<=mid的视为免费
// 3. 使用BFS判断：从1到n的路径中，最多使用k条付费道路（即长度>mid的边）
//
// 时间复杂度: O(m log(max_weight))，其中max_weight是最大的道路长度
// 空间复杂度: O(n + m)
// 是否为最优解: 是，这种方法是解决此类问题的有效方法

#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

const int INF = 1e9;

// 判断是否存在一条路径，其中付费道路（>mid）的数量不超过k
bool isPossible(int n, int k, const vector<vector<pair<int, int>>>& graph, int mid) {
    vector<int> dist(n + 1, INF);
    queue<int> q;
    q.push(1);
    dist[1] = 0;
    
    while (!q.empty()) {
        int u = q.front();
        q.pop();
        
        if (u == n) {
            return dist[u] <= k;
        }
        
        for (const auto& edge : graph[u]) {
            int v = edge.first;
            int w = edge.second;
            int cost = (w > mid) ? 1 : 0;
            
            if (dist[v] > dist[u] + cost) {
                dist[v] = dist[u] + cost;
                q.push(v);
            }
        }
    }
    
    return false;  // 无法到达n
}

int main() {
    int n, m, k;
    cin >> n >> m >> k;
    
    vector<vector<pair<int, int>>> graph(n + 1);
    int max_weight = 0;
    
    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        graph[u].emplace_back(v, w);
        graph[v].emplace_back(u, w);
        max_weight = max(max_weight, w);
    }
    
    // 二分查找最小的mid
    int left = 0;
    int right = max_weight;
    int answer = -1;
    
    while (left <= right) {
        int mid = (left + right) / 2;
        if (isPossible(n, k, graph, mid)) {
            answer = mid;
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    cout << answer << endl;
    
    return 0;
}