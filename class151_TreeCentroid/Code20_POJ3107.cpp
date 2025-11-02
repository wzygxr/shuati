// POJ 3107 Godfather
// 题目描述：给定一棵树，找出所有的重心节点
// 算法思想：直接应用树的重心查找算法
// 测试链接：http://poj.org/problem?id=3107
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
int minMaxSub; // 最小的最大子树大小

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
    
    // 计算父方向的子树大小
    maxSub[u] = max(maxSub[u], n - size_[u]);
    // 更新最小的最大子树大小
    if (maxSub[u] < minMaxSub) {
        minMaxSub = maxSub[u];
    }
}

int main() {
    cin >> n;
    
    // 初始化邻接表
    graph.assign(n + 1, vector<int>());
    
    // 读取边
    for (int i = 0; i < n - 1; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 初始化size和maxSub数组
    size_.assign(n + 1, 0);
    maxSub.assign(n + 1, 0);
    minMaxSub = INT_MAX;
    
    // 第一次DFS计算子树信息
    dfs(1, -1);
    
    // 收集所有重心
    vector<int> centroids;
    for (int i = 1; i <= n; i++) {
        if (maxSub[i] == minMaxSub) {
            centroids.push_back(i);
        }
    }
    
    // 排序输出
    sort(centroids.begin(), centroids.end());
    for (int i = 0; i < centroids.size(); i++) {
        if (i > 0) {
            cout << " ";
        }
        cout << centroids[i];
    }
    cout << endl;
    
    return 0;
}

// 注意：在POJ上提交时，需要将代码适配为POJ的格式