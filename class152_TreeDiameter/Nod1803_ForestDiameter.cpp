#include <iostream>
#include <vector>
#include <queue>
#include <map>
#include <algorithm>
using namespace std;

/**
 * 51Nod-1803 - 森林的直径
 * 题目描述：有一个由n个节点组成的森林，每个节点属于一棵树。
 * 支持两种操作：
 * 1. 连接两棵树：输入格式为"1 u v"，表示将节点u所在的树与节点v所在的树合并
 * 2. 查询操作：输入格式为"2 u"，表示询问节点u所在的树的直径
 * 
 * 解题思路：
 * 1. 使用并查集来管理各个树的合并
 * 2. 对于每个树，维护其直径的两个端点
 * 3. 当合并两棵树时，新树的直径只可能是原两棵树的直径之一，或者通过连接边形成的新路径（即u树的两个端点和v树的两个端点之间的最长路径）
 * 
 * 时间复杂度分析：
 * - 并查集操作的均摊时间复杂度接近O(1)
 * - 合并操作需要计算4种可能的路径长度，需要额外的BFS/DFS操作，单次时间复杂度为O(n)，但实际应用中树的大小通常不大
 * 
 * 空间复杂度：O(n)
 */

vector<int> parent;        // 并查集父节点数组
vector<pair<int, int>> treeDiameter; // 存储每个树的直径的两个端点
vector<vector<int>> graph; // 邻接表存储森林结构
int n, m;                  // n是节点数，m是操作数

/**
 * 并查集的查找操作，带路径压缩
 * @param x 要查找的节点
 * @return 节点x所在树的根节点
 */
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);
    }
    return parent[x];
}

/**
 * BFS函数，从指定节点开始，找到距离最远的节点和最远距离
 * @param start 起始节点
 * @param visited 标记数组，用于在合并过程中避免越界
 * @return 包含最远节点和最远距离的pair
 */
pair<int, int> bfs(int start, vector<bool>& visited) {
    queue<int> q;
    map<int, int> distance;
    
    q.push(start);
    distance[start] = 0;
    visited[start] = true;
    
    int maxDistance = 0;
    int farthestNode = start;
    
    while (!q.empty()) {
        int current = q.front();
        q.pop();
        
        for (int neighbor : graph[current]) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                int newDistance = distance[current] + 1;
                distance[neighbor] = newDistance;
                q.push(neighbor);
                
                if (newDistance > maxDistance) {
                    maxDistance = newDistance;
                    farthestNode = neighbor;
                }
            }
        }
    }
    
    return {farthestNode, maxDistance};
}

/**
 * 计算两个节点之间的距离
 * @param u 起始节点
 * @param v 目标节点
 * @return u和v之间的距离
 */
int getDistance(int u, int v) {
    vector<bool> visited(n + 1, false);
    queue<int> q;
    map<int, int> distance;
    
    q.push(u);
    distance[u] = 0;
    visited[u] = true;
    
    while (!q.empty()) {
        int current = q.front();
        q.pop();
        
        if (current == v) {
            return distance[current];
        }
        
        for (int neighbor : graph[current]) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                distance[neighbor] = distance[current] + 1;
                q.push(neighbor);
            }
        }
    }
    
    return -1; // 应该不会到达这里，因为u和v在同一棵树中
}

/**
 * 计算一棵树的直径
 * @param root 树的根节点
 * @return 树的直径的两个端点
 */
pair<int, int> computeDiameter(int root) {
    vector<bool> visited(n + 1, false);
    // 第一次BFS找到距离root最远的节点u
    auto result1 = bfs(root, visited);
    int u = result1.first;
    
    // 重置visited数组
    fill(visited.begin(), visited.end(), false);
    // 第二次BFS找到距离u最远的节点v
    auto result2 = bfs(u, visited);
    int v = result2.first;
    
    return {u, v};
}

int main() {
    cin >> n >> m;
    
    // 初始化并查集
    parent.resize(n + 1);
    for (int i = 1; i <= n; i++) {
        parent[i] = i;
    }
    
    // 初始化邻接表
    graph.resize(n + 1);
    
    // 初始化每个树的直径（初始时每个节点自身就是一棵树）
    treeDiameter.resize(n + 1);
    for (int i = 1; i <= n; i++) {
        treeDiameter[i] = {i, i};
    }
    
    // 处理每个操作
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            // 连接操作
            int u, v;
            cin >> u >> v;
            
            // 添加边
            graph[u].push_back(v);
            graph[v].push_back(u);
            
            // 合并两个集合
            int rootU = find(u);
            int rootV = find(v);
            if (rootU != rootV) {
                parent[rootV] = rootU;
                
                // 计算新树的直径
                int a1 = treeDiameter[rootU].first;
                int a2 = treeDiameter[rootU].second;
                int b1 = treeDiameter[rootV].first;
                int b2 = treeDiameter[rootV].second;
                
                // 可能的四种路径
                int d1 = getDistance(a1, a2); // 原u树的直径
                int d2 = getDistance(b1, b2); // 原v树的直径
                int d3 = getDistance(a1, b1); // a1到b1
                int d4 = getDistance(a1, b2); // a1到b2
                int d5 = getDistance(a2, b1); // a2到b1
                int d6 = getDistance(a2, b2); // a2到b2
                
                // 找出最长的路径
                int maxDistance = d1;
                pair<int, int> newDiameter = {a1, a2};
                
                if (d2 > maxDistance) {
                    maxDistance = d2;
                    newDiameter = {b1, b2};
                }
                if (d3 > maxDistance) {
                    maxDistance = d3;
                    newDiameter = {a1, b1};
                }
                if (d4 > maxDistance) {
                    maxDistance = d4;
                    newDiameter = {a1, b2};
                }
                if (d5 > maxDistance) {
                    maxDistance = d5;
                    newDiameter = {a2, b1};
                }
                if (d6 > maxDistance) {
                    maxDistance = d6;
                    newDiameter = {a2, b2};
                }
                
                // 更新新树的直径
                treeDiameter[rootU] = newDiameter;
            }
        } else if (op == 2) {
            // 查询操作
            int u;
            cin >> u;
            int root = find(u);
            int a = treeDiameter[root].first;
            int b = treeDiameter[root].second;
            // 计算直径长度并输出
            cout << getDistance(a, b) << endl;
        }
    }
    
    return 0;
}