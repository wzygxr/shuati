#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <algorithm>

using namespace std;

/**
 * Sorting It All Out - 拓扑排序状态判断 - C++实现
 * 题目解析：逐步添加关系并判断拓扑排序状态
 * 
 * 算法思路：
 * 1. 使用邻接矩阵存储图结构
 * 2. 逐步添加边关系
 * 3. 每次添加后尝试进行拓扑排序
 * 4. 根据结果判断三种状态
 * 
 * 时间复杂度：O(n * m)
 * 空间复杂度：O(n^2)
 * 
 * 工程化考虑：
 * 1. 使用邻接矩阵简化边操作
 * 2. 增量式拓扑排序避免重复计算
 * 3. 精确判断三种可能状态
 * 4. 输入验证和边界处理
 */
class Solution {
public:
    int topologicalSortState(vector<vector<int>>& graph, vector<int>& indegree, int n) {
        vector<int> tempIndegree = indegree;
        queue<int> q;
        
        // 统计入度为0的节点
        for (int i = 0; i < n; i++) {
            if (tempIndegree[i] == 0) {
                q.push(i);
            }
        }
        
        bool determined = true;
        vector<int> result;
        
        while (!q.empty()) {
            // 如果队列中有多个节点，说明无法确定
            if (q.size() > 1) {
                determined = false;
            }
            
            int u = q.front();
            q.pop();
            result.push_back(u);
            
            for (int v = 0; v < n; v++) {
                if (graph[u][v] == 1) {
                    if (--tempIndegree[v] == 0) {
                        q.push(v);
                    }
                }
            }
        }
        
        // 检查是否有环
        if (result.size() < n) {
            return 2; // 存在矛盾（有环）
        }
        
        return determined ? 1 : 0; // 1-唯一确定, 0-无法确定
    }
    
    vector<int> getTopologicalSequence(vector<vector<int>>& graph, vector<int>& indegree, int n) {
        vector<int> tempIndegree = indegree;
        queue<int> q;
        vector<int> result;
        
        for (int i = 0; i < n; i++) {
            if (tempIndegree[i] == 0) {
                q.push(i);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            result.push_back(u);
            
            for (int v = 0; v < n; v++) {
                if (graph[u][v] == 1) {
                    if (--tempIndegree[v] == 0) {
                        q.push(v);
                    }
                }
            }
        }
        
        return result;
    }
    
    void solve(int n, int m, vector<string>& relations) {
        vector<vector<int>> graph(n, vector<int>(n, 0));
        vector<int> indegree(n, 0);
        bool foundResult = false;
        int resultStep = -1;
        int resultState = -1;
        vector<int> resultSequence;
        
        // 逐步添加关系
        for (int step = 0; step < m; step++) {
            string relation = relations[step];
            int u = relation[0] - 'A';
            int v = relation[2] - 'A';
            
            // 添加边
            graph[u][v] = 1;
            indegree[v]++;
            
            // 检查状态
            int state = topologicalSortState(graph, indegree, n);
            
            if (state == 1 || state == 2) {
                foundResult = true;
                resultStep = step + 1;
                resultState = state;
                
                // 如果是唯一确定，记录拓扑序列
                if (state == 1) {
                    resultSequence = getTopologicalSequence(graph, indegree, n);
                }
                break;
            }
        }
        
        if (foundResult) {
            if (resultState == 1) {
                cout << "Sorted sequence determined after " << resultStep << " relations: ";
                for (int node : resultSequence) {
                    cout << (char)('A' + node);
                }
                cout << "." << endl;
            } else {
                cout << "Inconsistency found after " << resultStep << " relations." << endl;
            }
        } else {
            cout << "Sorted sequence cannot be determined." << endl;
        }
    }
};

int main() {
    Solution solution;
    int n, m;
    
    while (cin >> n >> m) {
        if (n == 0 && m == 0) break;
        
        vector<string> relations(m);
        for (int i = 0; i < m; i++) {
            cin >> relations[i];
        }
        
        solution.solve(n, m, relations);
    }
    
    return 0;
}