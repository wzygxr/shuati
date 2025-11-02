// Codeforces 1406C. Link Cut Centroids
// 题目描述：给定一棵树，执行一次操作：切断一条边，然后添加一条新边，使得新树只有一个重心
// 算法思想：如果树原本有两个重心，切断连接它们的路径上的一条边，然后将其中一个重心连接到另一个重心的子树中
// 测试链接：https://codeforces.com/problemset/problem/1406/C
// 时间复杂度：O(n)
// 空间复杂度：O(n)

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int n;
vector<vector<int>> graph;
vector<int> size_;
vector<int> maxSub;
vector<bool> visited;

// 计算子树大小和最大子树大小
void dfs(int u, int parent) {
    size_[u] = 1;
    maxSub[u] = 0;
    
    for (int v : graph[u]) {
        if (v != parent) {
            dfs(v, u);
            size_[u] += size_[v];
            maxSub[u] = max(maxSub[u], size_[v]);
        }
    }
    
    maxSub[u] = max(maxSub[u], n - size_[u]);
}

// 找到树的所有重心
vector<int> findCentroids() {
    int minMaxSub = INT_MAX;
    vector<int> centroids;
    
    for (int i = 1; i <= n; i++) {
        if (maxSub[i] < minMaxSub) {
            minMaxSub = maxSub[i];
            centroids.clear();
            centroids.push_back(i);
        } else if (maxSub[i] == minMaxSub) {
            centroids.push_back(i);
        }
    }
    
    return centroids;
}

// 找到一个子节点用于连接
int findChild(int u, int parent) {
    for (int v : graph[u]) {
        if (v != parent) {
            return v;
        }
    }
    return -1; // 不应该到达这里
}

int main() {
    int t; // 测试用例数量
    cin >> t;
    
    while (t--) {
        cin >> n;
        
        // 初始化数据结构
        graph.assign(n + 1, vector<int>());
        size_.assign(n + 1, 0);
        maxSub.assign(n + 1, 0);
        
        // 读取边
        vector<pair<int, int>> edges;
        for (int i = 0; i < n - 1; i++) {
            int u, v;
            cin >> u >> v;
            graph[u].push_back(v);
            graph[v].push_back(u);
            edges.push_back({u, v});
        }
        
        // 计算子树信息
        dfs(1, -1);
        
        // 找到重心
        vector<int> centroids = findCentroids();
        
        // 如果只有一个重心，无需操作
        if (centroids.size() == 1) {
            // 输出任意一条边
            auto edge = edges[0];
            cout << edge.first << " " << edge.second << endl;
            cout << edge.first << " " << edge.second << endl;
        } else {
            // 有两个重心，centroids[0]和centroids[1]
            int c1 = centroids[0];
            int c2 = centroids[1];
            
            // 找到c1在c2方向上的子节点
            int child = -1;
            for (int v : graph[c2]) {
                if (v != c1 && size_[v] > size_[c2]) {
                    child = v;
                    break;
                }
            }
            if (child == -1) {
                // 如果没找到，任选c1的一个子节点
                child = findChild(c1, c2);
            }
            
            // 切断c1和child的边，连接c2和child
            cout << c1 << " " << child << endl;
            cout << c2 << " " << child << endl;
        }
    }
    
    return 0;
}

// 注意：在Codeforces上提交时，需要将代码适配为Codeforces的格式