#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;
using namespace std;

// 路径中最大概率问题
// 给你一个由 n 个节点（下标从 0 开始）组成的无向加权图，记为 G
// 该图由边的列表表示，其中 edges[i] = [a, b] 表示连接节点 a 和 b 的无向边，且该边遍历成功的概率为 succProb[i]
// 指定两个节点分别作为起点 start 和终点 end，要求找出从起点到终点成功的最大概率
// 如果不存在从 start 到 end 的路径，返回 0
// 测试链接：https://leetcode.cn/problems/path-with-maximum-probability

class Solution {
public:
    // 使用Dijkstra算法求解最大概率路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    double maxProbability(int n, vector<vector<int>>& edges, vector<double>& succProb, int start, int end) {
        // 构建邻接表表示的图
        vector<vector<pair<int, double>>> graph(n);
        
        // 添加边到图中
        for (int i = 0; i < edges.size(); i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            double prob = succProb[i];
            // 无向图，需要添加两条边
            graph[u].push_back({v, prob});
            graph[v].push_back({u, prob});
        }
        
        // probability[i] 表示从起点start到节点i的最大概率
        vector<double> probability(n, 0.0);
        // 起点到自己的概率为1
        probability[start] = 1.0;
        
        // visited[i] 表示节点i是否已经确定了最大概率
        vector<bool> visited(n, false);
        
        // 优先队列，按概率从大到小排序
        // first : 起点到当前点的概率
        // second : 当前节点
        priority_queue<pair<double, int>> heap;
        heap.push({1.0, start});
        
        while (!heap.empty()) {
            // 取出概率最大的节点
            pair<double, int> record = heap.top();
            heap.pop();
            int u = record.second;
            double prob = record.first;
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居节点
            for (auto& edge : graph[u]) {
                int v = edge.first;  // 邻居节点
                double edgeProb = edge.second;  // 边的概率
                
                // 如果邻居节点未访问且通过u到达v的概率更大，则更新
                if (!visited[v] && probability[u] * edgeProb > probability[v]) {
                    probability[v] = probability[u] * edgeProb;
                    heap.push({probability[v], v});
                }
            }
        }
        
        return probability[end];
    }
};

// 测试用例
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 3;
    vector<vector<int>> edges1 = {{0,1},{1,2},{0,2}};
    vector<double> succProb1 = {0.5, 0.5, 0.2};
    int start1 = 0, end1 = 2;
    cout << "测试用例1结果: " << solution.maxProbability(n1, edges1, succProb1, start1, end1) << endl; // 期望输出: 0.25
    
    // 测试用例2
    int n2 = 3;
    vector<vector<int>> edges2 = {{0,1},{1,2},{0,2}};
    vector<double> succProb2 = {0.5, 0.5, 0.3};
    int start2 = 0, end2 = 2;
    cout << "测试用例2结果: " << solution.maxProbability(n2, edges2, succProb2, start2, end2) << endl; // 期望输出: 0.3
    
    // 测试用例3
    int n3 = 3;
    vector<vector<int>> edges3 = {{0,1}};
    vector<double> succProb3 = {0.5};
    int start3 = 0, end3 = 2;
    cout << "测试用例3结果: " << solution.maxProbability(n3, edges3, succProb3, start3, end3) << endl; // 期望输出: 0.0
    
    return 0;
}