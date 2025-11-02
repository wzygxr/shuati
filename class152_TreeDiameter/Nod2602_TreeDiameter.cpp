#include <iostream>
#include <vector>
#include <queue>
using namespace std;

/**
 * 51Nod-2602 - 树的直径
 * 题目描述：一棵树的直径就是这棵树上存在的最长路径。现在有一棵n个节点的树，现在想知道这棵树的直径包含的边的个数是多少？
 * 输入：第1行一个整数n表示节点个数，接下来n-1行每行两个整数u,v表示边
 * 输出：树的直径包含的边的个数
 * 
 * 解题思路：使用两次BFS法求树的直径
 * 第一次BFS从任意节点出发找到最远节点u，第二次BFS从u出发找到最远节点v，u到v的距离即为树的直径
 * 
 * 时间复杂度：O(n)，空间复杂度：O(n)
 */

int n; // 节点数量
vector<vector<int>> graph; // 邻接表存储树结构

/**
 * BFS函数，从指定节点开始，找到距离最远的节点和最远距离
 * @param start 起始节点
 * @return 包含最远节点和最远距离的pair
 */
pair<int, int> bfs(int start) {
    vector<bool> visited(n + 1, false); // 标记节点是否被访问过
    vector<int> distance(n + 1, 0); // 存储每个节点到起始节点的距离
    queue<int> q;
    
    q.push(start);
    visited[start] = true;
    distance[start] = 0;
    
    int maxDistance = 0;
    int farthestNode = start;
    
    while (!q.empty()) {
        int current = q.front();
        q.pop();
        
        for (int neighbor : graph[current]) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                distance[neighbor] = distance[current] + 1;
                q.push(neighbor);
                
                // 更新最远距离和最远节点
                if (distance[neighbor] > maxDistance) {
                    maxDistance = distance[neighbor];
                    farthestNode = neighbor;
                }
            }
        }
    }
    
    return {farthestNode, maxDistance};
}

int main() {
    cin >> n;
    
    // 初始化邻接表
    graph.resize(n + 1);
    
    // 读取边
    for (int i = 0; i < n - 1; i++) {
        int u, v;
        cin >> u >> v;
        // 无向树，添加双向边
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 第一次BFS，找到距离任意节点(这里选择1号节点)最远的节点u
    auto result1 = bfs(1);
    int u = result1.first;
    
    // 第二次BFS，找到距离u最远的节点v，此时的距离即为树的直径
    auto result2 = bfs(u);
    int diameter = result2.second;
    
    // 输出树的直径包含的边的个数
    cout << diameter << endl;
    
    return 0;
}