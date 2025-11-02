// LeetCode 1192. Critical Connections in a Network
// 题目链接: https://leetcode.cn/problems/critical-connections-in-a-network/
// 
// 题目描述:
// 力扣数据中心有n台服务器，编号为0到n-1。服务器之间形成一个无向拓扑图，其中connections[i] = [a, b]表示服务器a和b之间的连接。
// 连接是无向的，也就是说connections[i] = [a, b]和connections[i] = [b, a]表示的是同一个连接。
// 请你找出所有的关键连接，即删除这些连接后，服务器之间的连通性会受到影响的连接。请以任意顺序返回这些连接。
//
// 解题思路:
// 这是一个典型的寻找无向图中桥（Bridge）的问题。桥是指在图中，如果删除该边后，图会分成两个或更多的连通分量。
// 我们可以使用Tarjan算法来高效地找出所有的桥。Tarjan算法基于深度优先搜索（DFS），通过记录每个节点的发现时间（discovery time）和能够回溯到的最早的节点（low value）来判断一条边是否为桥。
//
// 时间复杂度: O(V + E)，其中V是顶点数，E是边数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，Tarjan算法是寻找图中桥的线性时间算法

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
private:
    // 深度优先搜索函数，用于寻找桥
    void dfs(int node, int parent, vector<int>& disc, vector<int>& low, 
             vector<bool>& visited, int& time, const vector<vector<int>>& graph, 
             vector<vector<int>>& result) {
        // 标记当前节点为已访问
        visited[node] = true;
        
        // 设置当前节点的发现时间和low值
        disc[node] = low[node] = ++time;
        
        // 遍历当前节点的所有邻居
        for (int neighbor : graph[node]) {
            // 如果邻居是父节点，跳过
            if (neighbor == parent) {
                continue;
            }
            
            // 如果邻居还没有被访问过
            if (!visited[neighbor]) {
                dfs(neighbor, node, disc, low, visited, time, graph, result);
                
                // 更新当前节点的low值
                low[node] = min(low[node], low[neighbor]);
                
                // 检查边(node, neighbor)是否为桥
                if (low[neighbor] > disc[node]) {
                    result.push_back({node, neighbor});
                }
            } else {
                // 如果邻居已经被访问过，且不是父节点，说明找到一条回边
                // 更新当前节点的low值
                low[node] = min(low[node], disc[neighbor]);
            }
        }
    }
    
public:
    vector<vector<int>> criticalConnections(int n, vector<vector<int>>& connections) {
        // 构建邻接表表示的图
        vector<vector<int>> graph(n);
        for (const auto& connection : connections) {
            int u = connection[0];
            int v = connection[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 初始化发现时间、low值和访问标记数组
        vector<int> disc(n, -1);
        vector<int> low(n, -1);
        vector<bool> visited(n, false);
        vector<vector<int>> result;
        int time = 0;
        
        // 对每个未访问的节点进行DFS
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, -1, disc, low, visited, time, graph, result);
            }
        }
        
        return result;
    }
};

// 打印结果函数
void printResult(const vector<vector<int>>& result) {
    cout << "[";
    for (size_t i = 0; i < result.size(); i++) {
        cout << "[" << result[i][0] << ", " << result[i][1] << "]";
        if (i < result.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1
    int n1 = 4;
    vector<vector<int>> connections1 = {{0, 1}, {1, 2}, {2, 0}, {1, 3}};
    cout << "Test 1: ";
    printResult(solution.criticalConnections(n1, connections1));
    // 预期输出: [[1, 3]]
    
    // 测试用例2
    int n2 = 2;
    vector<vector<int>> connections2 = {{0, 1}};
    cout << "Test 2: ";
    printResult(solution.criticalConnections(n2, connections2));
    // 预期输出: [[0, 1]]
    
    // 测试用例3 - 多个桥
    int n3 = 5;
    vector<vector<int>> connections3 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {2, 4}};
    cout << "Test 3: ";
    printResult(solution.criticalConnections(n3, connections3));
    // 预期输出: [[0, 1], [1, 2]]
}

int main() {
    test();
    return 0;
}