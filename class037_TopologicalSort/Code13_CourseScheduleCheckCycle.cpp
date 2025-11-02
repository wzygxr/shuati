#include <iostream>
#include <vector>
#include <queue>

using namespace std;

/**
 * 课程表 - 拓扑排序判环 - C++实现
 * 题目解析：判断课程安排图中是否存在环
 * 
 * 算法思路：
 * 1. 使用邻接表存储课程依赖关系
 * 2. 计算每个课程的入度
 * 3. 使用队列进行BFS拓扑排序
 * 4. 如果完成的课程数等于总课程数，说明无环
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 * 
 * 工程化考虑：
 * 1. 使用vector存储邻接表，提高内存效率
 * 2. 输入验证：验证课程数和依赖关系的有效性
 * 3. 边界处理：空课程、单课程等情况
 * 4. 性能优化：使用队列进行BFS
 */
class Solution {
public:
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        // 特殊情况处理
        if (numCourses <= 0) return true;
        if (prerequisites.empty()) return true;
        
        // 构建邻接表
        vector<vector<int>> graph(numCourses);
        vector<int> indegree(numCourses, 0);
        
        // 构建图并计算入度
        for (const auto& pre : prerequisites) {
            int course = pre[0];
            int prerequisite = pre[1];
            graph[prerequisite].push_back(course);
            indegree[course]++;
        }
        
        // 使用队列进行拓扑排序
        queue<int> q;
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                q.push(i);
            }
        }
        
        int count = 0; // 已完成的课程数
        while (!q.empty()) {
            int course = q.front();
            q.pop();
            count++;
            
            // 更新依赖该课程的课程的入度
            for (int nextCourse : graph[course]) {
                if (--indegree[nextCourse] == 0) {
                    q.push(nextCourse);
                }
            }
        }
        
        return count == numCourses;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1：无环，可以完成
    int numCourses1 = 2;
    vector<vector<int>> prerequisites1 = {{1, 0}};
    cout << "测试用例1: " << solution.canFinish(numCourses1, prerequisites1) << endl; // 输出: 1 (true)
    
    // 测试用例2：有环，无法完成
    int numCourses2 = 2;
    vector<vector<int>> prerequisites2 = {{1, 0}, {0, 1}};
    cout << "测试用例2: " << solution.canFinish(numCourses2, prerequisites2) << endl; // 输出: 0 (false)
    
    // 测试用例3：空课程
    int numCourses3 = 0;
    vector<vector<int>> prerequisites3 = {};
    cout << "测试用例3: " << solution.canFinish(numCourses3, prerequisites3) << endl; // 输出: 1 (true)
    
    return 0;
}