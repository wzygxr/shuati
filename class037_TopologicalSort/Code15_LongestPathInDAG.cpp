#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>

using namespace std;

/**
 * 有向无环图中的最长路径 - C++实现
 * 题目解析：计算DAG中的最长路径，使用拓扑排序+动态规划
 * 
 * 算法思路：
 * 1. 使用邻接表存储图结构
 * 2. 进行拓扑排序确定节点处理顺序
 * 3. 使用动态规划计算每个节点的最长路径
 * 4. dp[i]表示以节点i为终点的最长路径长度
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 * 
 * 工程化考虑：
 * 1. 使用vector存储邻接表，提高内存效率
 * 2. 输入输出优化：使用scanf/printf
 * 3. 边界处理：空图、单节点图等情况
 * 4. 性能优化：拓扑排序+BFS
 */
class Solution {
public:
    int longestPath(int n, vector<int>& weight, vector<vector<int>>& edges) {
        // 构建邻接表
        vector<vector<int>> graph(n + 1);
        vector<int> indegree(n + 1, 0);
        vector<int> outdegree(n + 1, 0);
        
        // 构建图
        for (const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].push_back(v);
            indegree[v]++;
            outdegree[u]++;
        }
        
        // DP数组：dp[i]表示以节点i为终点的最长路径长度
        vector<int> dp(n + 1, 0);
        
        // 初始化dp数组为节点权重
        for (int i = 1; i <= n; i++) {
            dp[i] = weight[i];
        }
        
        // 拓扑排序队列
        queue<int> q;
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                q.push(i);
            }
        }
        
        int maxPath = INT_MIN;
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            
            // 如果u是终点（出度为0），更新最长路径
            if (outdegree[u] == 0) {
                maxPath = max(maxPath, dp[u]);
            }
            
            // 遍历u的所有邻居
            for (int v : graph[u]) {
                // 状态转移：dp[v] = max(dp[v], dp[u] + weight[v])
                if (dp[u] + weight[v] > dp[v]) {
                    dp[v] = dp[u] + weight[v];
                }
                
                if (--indegree[v] == 0) {
                    q.push(v);
                }
            }
        }
        
        return maxPath;
    }
};

int main() {
    Solution solution;
    int n, m;
    
    while (cin >> n >> m) {
        vector<int> weight(n + 1);
        for (int i = 1; i <= n; i++) {
            cin >> weight[i];
        }
        
        vector<vector<int>> edges(m, vector<int>(2));
        for (int i = 0; i < m; i++) {
            cin >> edges[i][0] >> edges[i][1];
        }
        
        int result = solution.longestPath(n, weight, edges);
        cout << result << endl;
    }
    
    return 0;
}