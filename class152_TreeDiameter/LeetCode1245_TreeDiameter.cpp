// LeetCode 1245. 树的直径（非二叉树版本）
// 题目：给你一棵树，树中包含 n 个节点，节点编号从 0 到 n-1。
// 树用一个边列表来表示，其中 edges[i] = [u, v] 表示节点 u 和 v 之间有一条无向边。
// 返回这棵树的直径长度。
// 树的直径是树中任意两个节点之间最长路径的长度。
// 这条路径可能不经过根节点。
// 来源：LeetCode
// 链接：https://leetcode.cn/problems/tree-diameter/

#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <cstring>

using namespace std;

class LeetCode1245_TreeDiameter {
public:
    /**
     * 计算树的直径（两次BFS法）
     * @param edges 边列表，表示树结构
     * @return 树的直径长度
     * 
     * 时间复杂度：O(n)，其中n是节点数
     * 空间复杂度：O(n)，用于存储邻接表和BFS队列
     */
    int treeDiameter(vector<vector<int>>& edges) {
        int n = edges.size() + 1; // 节点数 = 边数 + 1
        
        // 特殊情况处理
        if (n == 0) return 0;
        if (n == 1) return 0;
        
        // 构建邻接表
        vector<vector<int>> graph(n);
        
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 第一次BFS：从任意节点（如0）出发，找到最远的节点A
        auto firstBFS = bfs(0, graph, n);
        int nodeA = firstBFS.first;
        
        // 第二次BFS：从节点A出发，找到最远的节点B，距离就是直径
        auto secondBFS = bfs(nodeA, graph, n);
        
        return secondBFS.second; // 返回直径长度
    }
    
    /**
     * BFS方法，返回最远节点和距离
     * @param start 起始节点
     * @param graph 邻接表
     * @param n 节点总数
     * @return pair，第一个元素是最远节点，第二个元素是距离
     */
    pair<int, int> bfs(int start, vector<vector<int>>& graph, int n) {
        vector<int> distance(n, -1);
        distance[start] = 0;
        
        queue<int> q;
        q.push(start);
        
        int farthestNode = start;
        int maxDistance = 0;
        
        while (!q.empty()) {
            int current = q.front();
            q.pop();
            
            // 遍历当前节点的所有邻居
            for (int neighbor : graph[current]) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[current] + 1;
                    q.push(neighbor);
                    
                    // 更新最远节点和最大距离
                    if (distance[neighbor] > maxDistance) {
                        maxDistance = distance[neighbor];
                        farthestNode = neighbor;
                    }
                }
            }
        }
        
        return {farthestNode, maxDistance};
    }
    
    /**
     * 树形DP方法计算树的直径（可以处理负权边）
     * @param edges 边列表
     * @return 树的直径长度
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    int treeDiameterDP(vector<vector<int>>& edges) {
        int n = edges.size() + 1;
        
        // 特殊情况处理
        if (n == 0) return 0;
        if (n == 1) return 0;
        
        // 构建邻接表
        vector<vector<int>> graph(n);
        
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 全局变量记录最大直径
        int maxDiameter = 0;
        
        // DFS计算每个节点的最大深度
        dfs(0, -1, graph, maxDiameter);
        
        return maxDiameter;
    }
    
    /**
     * DFS计算节点深度并更新最大直径
     * @param node 当前节点
     * @param parent 父节点
     * @param graph 邻接表
     * @param maxDiameter 全局最大直径
     * @return 当前节点的最大深度
     */
    int dfs(int node, int parent, vector<vector<int>>& graph, int& maxDiameter) {
        int maxDepth1 = 0; // 最大深度
        int maxDepth2 = 0; // 次大深度
        
        for (int neighbor : graph[node]) {
            if (neighbor != parent) {
                int depth = dfs(neighbor, node, graph, maxDiameter);
                
                if (depth > maxDepth1) {
                    maxDepth2 = maxDepth1;
                    maxDepth1 = depth;
                } else if (depth > maxDepth2) {
                    maxDepth2 = depth;
                }
            }
        }
        
        // 更新最大直径：经过当前节点的最长路径 = maxDepth1 + maxDepth2
        maxDiameter = max(maxDiameter, maxDepth1 + maxDepth2);
        
        // 返回当前节点的最大深度
        return maxDepth1 + 1;
    }
    
    // 测试方法
    void test() {
        LeetCode1245_TreeDiameter solution;
        
        // 测试用例1: [[0,1],[0,2]]
        // 树结构：
        //   0
        //  / \
        // 1   2
        // 预期输出：2（路径 1-0-2）
        vector<vector<int>> edges1 = {{0,1},{0,2}};
        cout << "测试用例1结果: " << solution.treeDiameter(edges1) << endl; // 应该输出2
        cout << "测试用例1(DP)结果: " << solution.treeDiameterDP(edges1) << endl; // 应该输出2
        
        // 测试用例2: [[0,1],[1,2],[2,3],[1,4],[4,5]]
        // 树结构：
        //     0
        //     |
        //     1
        //    / \
        //   2   4
        //  /     \
        // 3       5
        // 预期输出：4（路径 3-2-1-4-5）
        vector<vector<int>> edges2 = {{0,1},{1,2},{2,3},{1,4},{4,5}};
        cout << "测试用例2结果: " << solution.treeDiameter(edges2) << endl; // 应该输出4
        cout << "测试用例2(DP)结果: " << solution.treeDiameterDP(edges2) << endl; // 应该输出4
        
        // 测试用例3: 单节点树
        vector<vector<int>> edges3 = {};
        cout << "测试用例3结果: " << solution.treeDiameter(edges3) << endl; // 应该输出0
        cout << "测试用例3(DP)结果: " << solution.treeDiameterDP(edges3) << endl; // 应该输出0
    }
};

int main() {
    LeetCode1245_TreeDiameter solution;
    solution.test();
    return 0;
}