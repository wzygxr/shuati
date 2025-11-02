#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

/**
 * 参加会议的最多员工数 - 基环树问题 - C++实现
 * 题目解析：处理基环树，分类讨论环和链的情况
 * 
 * 算法思路：
 * 1. 使用拓扑排序找出所有不在环上的节点
 * 2. 计算链的深度
 * 3. 分类处理不同大小的环
 * 4. 取所有情况的最大值
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考虑：
 * 1. 图结构分析：识别基环树特性
 * 2. 分类讨论：处理不同大小的环
 * 3. 链长度计算：使用拓扑排序
 * 4. 边界处理：单节点、自环等情况
 */
class Solution {
public:
    int maximumInvitations(vector<int>& favorite) {
        int n = favorite.size();
        
        // 计算入度
        vector<int> indegree(n, 0);
        for (int i = 0; i < n; i++) {
            indegree[favorite[i]]++;
        }
        
        // 拓扑排序：找出所有不在环上的节点
        vector<bool> visited(n, false);
        vector<int> depth(n, 0); // 链的深度
        queue<int> q;
        
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                q.push(i);
                visited[i] = true;
            }
        }
        
        // 计算链的深度
        while (!q.empty()) {
            int curr = q.front();
            q.pop();
            int next = favorite[curr];
            depth[next] = max(depth[next], depth[curr] + 1);
            
            if (--indegree[next] == 0) {
                q.push(next);
                visited[next] = true;
            }
        }
        
        int maxCircle = 0; // 最大环的大小
        int sumPairs = 0;   // 所有大小为2的环加上链的长度之和
        
        // 处理环
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                // 找到环
                int circleSize = 0;
                int curr = i;
                
                while (!visited[curr]) {
                    visited[curr] = true;
                    circleSize++;
                    curr = favorite[curr];
                }
                
                if (circleSize == 2) {
                    // 大小为2的环：可以加上两条链的长度
                    sumPairs += 2 + depth[i] + depth[favorite[i]];
                } else {
                    // 大小大于2的环：只能选择环的大小
                    maxCircle = max(maxCircle, circleSize);
                }
            }
        }
        
        return max(maxCircle, sumPairs);
    }
};

int main() {
    Solution solution;
    
    // 测试用例1：大小为2的环加上链
    vector<int> favorite1 = {2, 2, 1, 2};
    cout << "测试用例1: " << solution.maximumInvitations(favorite1) << endl; // 输出: 3
    
    // 测试用例2：大小为3的环
    vector<int> favorite2 = {1, 2, 0};
    cout << "测试用例2: " << solution.maximumInvitations(favorite2) << endl; // 输出: 3
    
    // 测试用例3：多个环
    vector<int> favorite3 = {3, 0, 1, 4, 1};
    cout << "测试用例3: " << solution.maximumInvitations(favorite3) << endl; // 输出: 4
    
    // 测试用例4：自环
    vector<int> favorite4 = {0};
    cout << "测试用例4: " << solution.maximumInvitations(favorite4) << endl; // 输出: 1
    
    return 0;
}