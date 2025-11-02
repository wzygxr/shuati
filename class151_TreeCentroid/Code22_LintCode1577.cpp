// LintCode 1577. 子树计数
// 题目描述：给定一棵树，计算以每个节点为根的子树的重心。
// 算法思想：对于每个子树，找到其重心。利用树的重心的性质：子树的重心一定在原树重心到该子树根的路径上。
// 测试链接：https://www.lintcode.com/problem/1577/
// 时间复杂度：O(n^2)，对于每个节点都要重新计算子树的重心
// 空间复杂度：O(n)

#include <iostream>
#include <vector>
#include <climits>
using namespace std;

class Code22_LintCode1577 {
private:
    int n; // 节点数
    vector<vector<int>> graph; // 邻接表
    vector<int> res; // 结果数组
    vector<bool> visited; // 标记数组
    vector<int> size; // 子树大小
    vector<int> maxSubtree; // 最大子树大小
    int minMaxSubtree; // 当前子树的最小最大子树大小
    int centroid; // 当前子树的重心
    
    /**
     * 计算子树大小
     */
    void dfs(int u, int parent) {
        visited[u] = true;
        size[u] = 1;
        maxSubtree[u] = 0;
        for (int v : graph[u]) {
            if (!visited[v] && v != parent) {
                dfs(v, u);
                size[u] += size[v];
                maxSubtree[u] = max(maxSubtree[u], size[v]);
            }
        }
    }
    
    /**
     * 寻找子树的重心
     */
    void findCentroid(int u, int parent, int totalSize) {
        // 计算父方向的子树大小
        int maxSize = max(maxSubtree[u], totalSize - size[u]);
        
        // 更新重心
        if (maxSize < minMaxSubtree || (maxSize == minMaxSubtree && u < centroid)) {
            minMaxSubtree = maxSize;
            centroid = u;
        }
        
        for (int v : graph[u]) {
            if (v != parent && visited[v]) {
                findCentroid(v, u, totalSize);
            }
        }
    }
    
public:
    /**
     * 计算以每个节点为根的子树的重心
     */
    vector<int> getSubtreeCentroid(int n, vector<vector<int>>& edges) {
        this->n = n;
        // 构建邻接表
        graph.resize(n);
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        res.resize(n);
        // 对每个节点作为根，计算其子树的重心
        for (int i = 0; i < n; i++) {
            visited.assign(n, false);
            size.assign(n, 0);
            maxSubtree.assign(n, 0);
            minMaxSubtree = INT_MAX;
            centroid = -1;
            
            // 计算子树大小
            dfs(i, -1);
            
            // 找到重心
            findCentroid(i, -1, size[i]);
            
            res[i] = centroid;
        }
        
        return res;
    }
    
    /**
     * 打印数组
     */
    void printArray(vector<int>& arr) {
        cout << "[";
        for (int i = 0; i < arr.size(); i++) {
            cout << arr[i];
            if (i < arr.size() - 1) {
                cout << ", ";
            }
        }
        cout << "]" << endl;
    }
};

// 测试代码
int main() {
    Code22_LintCode1577 solution;
    
    // 测试用例1
    int n1 = 3;
    vector<vector<int>> edges1 = {{0, 1}, {0, 2}};
    vector<int> res1 = solution.getSubtreeCentroid(n1, edges1);
    cout << "测试用例1结果: ";
    solution.printArray(res1);
    // 期望输出: [0, 0, 0]
    
    // 测试用例2
    int n2 = 4;
    vector<vector<int>> edges2 = {{0, 1}, {1, 2}, {1, 3}};
    vector<int> res2 = solution.getSubtreeCentroid(n2, edges2);
    cout << "测试用例2结果: ";
    solution.printArray(res2);
    // 期望输出: [1, 1, 1, 1]
    
    return 0;
}

// 注意：
// 1. 树的重心是指：对于节点u，删除u后剩余的各个连通块的大小不超过原树大小的一半
// 2. 本算法对于每个节点都重新计算子树的重心，时间复杂度为O(n^2)
// 3. 对于更大的数据规模，可以利用树的重心的性质进行优化，如利用点分治的思想