// LintCode 1489. 树中的中心点
// 题目描述：给定一棵树，找出树的中心点（重心）
// 算法思想：直接应用树的重心查找算法
// 测试链接：https://www.lintcode.com/problem/1489/
// 时间复杂度：O(n)
// 空间复杂度：O(n)

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
private:
    vector<vector<int>> graph;
    vector<int> size_;
    vector<int> maxSub;
    int n;
    
    // 计算子树大小和最大子树大小
    void dfs(int u, int parent) {
        size_[u] = 1;
        maxSub[u] = 0;
        
        // 遍历所有邻居
        for (int v : graph[u]) {
            if (v != parent) {
                dfs(v, u);
                size_[u] += size_[v];
                maxSub[u] = max(maxSub[u], size_[v]);
            }
        }
        
        // 计算父方向的子树大小
        maxSub[u] = max(maxSub[u], n - size_[u]);
    }
    
public:
    // 寻找树的中心点（重心）
    vector<int> findMinHeightTrees(int n, vector<vector<int>>& edges) {
        this->n = n;
        
        // 边界情况处理
        if (n == 1) {
            return {0};
        }
        if (n == 2) {
            return {0, 1};
        }
        
        // 初始化邻接表
        graph.resize(n);
        
        // 构建图
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 初始化size和maxSub数组
        size_.resize(n, 0);
        maxSub.resize(n, 0);
        
        // 第一次DFS计算子树信息
        dfs(0, -1);
        
        // 找到最小的最大子树大小
        int minMaxSub = INT_MAX;
        for (int i = 0; i < n; i++) {
            if (maxSub[i] < minMaxSub) {
                minMaxSub = maxSub[i];
            }
        }
        
        // 收集所有重心
        vector<int> result;
        for (int i = 0; i < n; i++) {
            if (maxSub[i] == minMaxSub) {
                result.push_back(i);
            }
        }
        
        return result;
    }
};

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 4;
    vector<vector<int>> edges1 = {{1, 0}, {1, 2}, {1, 3}};
    vector<int> result1 = solution.findMinHeightTrees(n1, edges1);
    cout << "Test Case 1: ";
    for (int node : result1) {
        cout << node << " ";
    }
    cout << endl;  // Expected: 1
    
    // 测试用例2
    int n2 = 6;
    vector<vector<int>> edges2 = {{0, 3}, {1, 3}, {2, 3}, {4, 3}, {5, 4}};
    vector<int> result2 = solution.findMinHeightTrees(n2, edges2);
    cout << "Test Case 2: ";
    for (int node : result2) {
        cout << node << " ";
    }
    cout << endl;  // Expected: 3 4
    
    return 0;
}

// 注意：在LintCode上提交时，需要将代码适配为LintCode的格式