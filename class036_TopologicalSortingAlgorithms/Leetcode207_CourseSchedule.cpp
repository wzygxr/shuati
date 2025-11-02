#include <iostream>
#include <vector>
#include <queue>
using namespace std;

/**
 * LeetCode 207. Course Schedule
 * 
 * 题目链接: https://leetcode.com/problems/course-schedule/
 * 
 * 题目描述：
 * 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1。
 * 在选修某些课程之前需要一些先修课程。先修课程按数组 prerequisites 给出，
 * 其中 prerequisites[i] = [ai, bi]，表示如果要学习课程 ai 则必须先学习课程 bi。
 * 请你判断是否可能完成所有课程的学习？如果可以，返回 true；否则，返回 false。
 * 
 * 解题思路：
 * 这是一个典型的拓扑排序问题。我们需要判断有向图中是否存在环。
 * 如果存在环，则无法完成所有课程；如果不存在环，则可以完成。
 * 我们可以使用Kahn算法来解决：
 * 1. 计算每个节点的入度
 * 2. 将所有入度为0的节点加入队列
 * 3. 不断从队列中取出节点，并将其所有邻居节点的入度减1
 * 4. 如果邻居节点的入度变为0，则将其加入队列
 * 5. 重复步骤3-4直到队列为空
 * 6. 最后检查处理的节点数是否等于总节点数
 * 
 * 时间复杂度：O(V + E)，其中V是课程数，E是先修关系数
 * 空间复杂度：O(V + E)，用于存储图和入度数组
 * 
 * 示例：
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：true
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0。这是可能的。
 * 
 * 输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
 * 输出：false
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要先完成课程 0；
 *       并且学习课程 0 之前，你还应先完成课程 1。这是不可能的。
 */

class Solution {
public:
    /**
     * 判断是否可以完成所有课程
     * @param numCourses 课程总数
     * @param prerequisites 先修课程关系数组
     * @return 如果可以完成所有课程返回true，否则返回false
     */
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        // 构建邻接表表示的图
        vector<vector<int>> graph(numCourses);
        
        // 初始化入度数组
        vector<int> inDegree(numCourses, 0);
        
        // 构建图和入度数组
        for (const auto& prerequisite : prerequisites) {
            int course = prerequisite[0];      // 当前课程
            int preCourse = prerequisite[1];   // 先修课程
            
            // 添加边：先修课程 -> 当前课程
            graph[preCourse].push_back(course);
            
            // 当前课程入度加1
            inDegree[course]++;
        }
        
        // 使用Kahn算法进行拓扑排序
        return topologicalSort(graph, inDegree, numCourses);
    }
    
private:
    /**
     * 使用Kahn算法进行拓扑排序，判断是否存在环
     * @param graph 邻接表表示的图
     * @param inDegree 入度数组
     * @param numCourses 课程总数
     * @return 如果不存在环返回true，否则返回false
     */
    bool topologicalSort(const vector<vector<int>>& graph, vector<int>& inDegree, int numCourses) {
        queue<int> q;
        
        // 将所有入度为0的节点加入队列
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                q.push(i);
            }
        }
        
        int processedCourses = 0; // 记录已处理的课程数
        
        // Kahn算法进行拓扑排序
        while (!q.empty()) {
            int currentCourse = q.front();
            q.pop();
            processedCourses++;
            
            // 遍历当前课程的所有后续课程
            for (int nextCourse : graph[currentCourse]) {
                // 将后续课程的入度减1
                inDegree[nextCourse]--;
                
                // 如果后续课程的入度变为0，则加入队列
                if (inDegree[nextCourse] == 0) {
                    q.push(nextCourse);
                }
            }
        }
        
        // 如果处理的课程数等于总课程数，说明不存在环，可以完成所有课程
        return processedCourses == numCourses;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    int numCourses1 = 2;
    vector<vector<int>> prerequisites1 = {{1, 0}};
    cout << "Test Case 1: " << (solution.canFinish(numCourses1, prerequisites1) ? "true" : "false") << endl; // 应该输出 true
    
    // 测试用例2
    int numCourses2 = 2;
    vector<vector<int>> prerequisites2 = {{1, 0}, {0, 1}};
    cout << "Test Case 2: " << (solution.canFinish(numCourses2, prerequisites2) ? "true" : "false") << endl; // 应该输出 false
    
    // 测试用例3
    int numCourses3 = 3;
    vector<vector<int>> prerequisites3 = {{1, 0}, {2, 1}};
    cout << "Test Case 3: " << (solution.canFinish(numCourses3, prerequisites3) ? "true" : "false") << endl; // 应该输出 true
    
    return 0;
}

// 补充头文件
#include <unordered_map>
#include <unordered_set>
#include <string>
#include <algorithm>
#include <stack>

/**
 * LeetCode 210. Course Schedule II
 * 题目链接: https://leetcode.com/problems/course-schedule-ii/
 * 
 * 题目描述：
 * 返回完成所有课程的学习顺序。如果有多个可能的答案，返回任意一个。
 * 如果不可能完成所有课程，返回一个空数组。
 */
class CourseScheduleII {
public:
    vector<int> findOrder(int numCourses, vector<vector<int>>& prerequisites) {
        // 构建邻接表表示的图
        vector<vector<int>> graph(numCourses);
        
        // 初始化入度数组
        vector<int> inDegree(numCourses, 0);
        
        // 构建图和入度数组
        for (const auto& prerequisite : prerequisites) {
            int course = prerequisite[0];      // 当前课程
            int preCourse = prerequisite[1];   // 先修课程
            
            graph[preCourse].push_back(course);
            inDegree[course]++;
        }
        
        // 使用Kahn算法进行拓扑排序
        return topologicalSort(graph, inDegree, numCourses);
    }
    
private:
    /**
     * 使用Kahn算法进行拓扑排序，返回拓扑排序的结果
     */
    vector<int> topologicalSort(const vector<vector<int>>& graph, vector<int> inDegree, int numCourses) {
        queue<int> q;
        vector<int> result;
        result.reserve(numCourses);
        
        // 将所有入度为0的节点加入队列
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                q.push(i);
            }
        }
        
        // Kahn算法进行拓扑排序
        while (!q.empty()) {
            int currentCourse = q.front();
            q.pop();
            result.push_back(currentCourse);
            
            // 遍历当前课程的所有后续课程
            for (int nextCourse : graph[currentCourse]) {
                inDegree[nextCourse]--;
                
                if (inDegree[nextCourse] == 0) {
                    q.push(nextCourse);
                }
            }
        }
        
        // 如果处理的课程数等于总课程数，返回排序结果，否则返回空数组
        if (result.size() != numCourses) {
            return {};
        }
        return result;
    }
};

/**
 * LeetCode 269. Alien Dictionary
 * 题目链接: https://leetcode.com/problems/alien-dictionary/
 * 
 * 题目描述：
 * 给定一个按字典序排序的外星文字典中的单词列表，推断其中字母的顺序。
 */
class AlienDictionary {
public:
    string alienOrder(vector<string>& words) {
        // 构建图
        unordered_map<char, vector<char>> graph;
        unordered_map<char, int> inDegree;
        
        // 初始化所有出现的字符
        for (const string& word : words) {
            for (char c : word) {
                if (graph.find(c) == graph.end()) {
                    graph[c] = vector<char>();
                    inDegree[c] = 0;
                }
            }
        }
        
        // 构建字符之间的约束关系
        for (int i = 0; i < words.size() - 1; i++) {
            const string& word1 = words[i];
            const string& word2 = words[i + 1];
            
            // 检查是否是前缀关系
            if (word1.size() > word2.size() && word1.substr(0, word2.size()) == word2) {
                return "";
            }
            
            // 找出第一个不同的字符
            int minLength = min(word1.size(), word2.size());
            for (int j = 0; j < minLength; j++) {
                char c1 = word1[j];
                char c2 = word2[j];
                
                if (c1 != c2) {
                    // 避免重复添加边
                    bool exists = false;
                    for (char neighbor : graph[c1]) {
                        if (neighbor == c2) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        graph[c1].push_back(c2);
                        inDegree[c2]++;
                    }
                    break;
                }
            }
        }
        
        // 使用Kahn算法进行拓扑排序
        queue<char> q;
        for (const auto& entry : inDegree) {
            if (entry.second == 0) {
                q.push(entry.first);
            }
        }
        
        string result;
        while (!q.empty()) {
            char current = q.front();
            q.pop();
            result += current;
            
            for (char neighbor : graph[current]) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    q.push(neighbor);
                }
            }
        }
        
        // 检查是否有环
        if (result.size() != inDegree.size()) {
            return "";
        }
        
        return result;
    }
};

/**
 * LeetCode 936. Stamping The Sequence
 * 题目链接: https://leetcode.com/problems/stamping-the-sequence/
 * 
 * 题目描述：
 * 给定一个目标字符串 target 和一个印章字符串 stamp，返回一个操作序列。
 */
class StampingTheSequence {
public:
    vector<int> movesToStamp(string stamp, string target) {
        int m = stamp.size();
        int n = target.size();
        
        vector<int> coverCount(n - m + 1, 0);
        vector<int> matched(n, 0);
        
        queue<int> q;
        vector<int> resultList;
        
        // 初始时，找出所有可以完全匹配的子串
        for (int i = 0; i <= n - m; i++) {
            for (int j = 0; j < m; j++) {
                if (stamp[j] == target[i + j]) {
                    coverCount[i]++;
                }
            }
            
            if (coverCount[i] == m) {
                q.push(i);
                resultList.push_back(i);
                for (int j = 0; j < m; j++) {
                    if (matched[i + j] == 0) {
                        matched[i + j] = 1;
                    }
                }
            }
        }
        
        // 进行拓扑排序
        while (!q.empty()) {
            int pos = q.front();
            q.pop();
            
            // 检查pos周围的位置
            int start = max(0, pos - m + 1);
            int end = min(n - m, pos + m - 1);
            for (int i = start; i <= end; i++) {
                if (i == pos) continue;
                
                bool overlap = false;
                for (int j = 0; j < m; j++) {
                    if (i + j >= pos && i + j < pos + m) {
                        overlap = true;
                        if (matched[pos + j - i] == 1 && stamp[j] == target[i + j]) {
                            if (coverCount[i] < m) {
                                coverCount[i]++;
                            }
                        }
                    }
                }
                
                if (overlap && coverCount[i] == m) {
                    q.push(i);
                    resultList.push_back(i);
                    for (int j = 0; j < m; j++) {
                        if (matched[i + j] == 0) {
                            matched[i + j] = 1;
                        }
                    }
                }
            }
        }
        
        // 检查是否所有字符都被覆盖
        for (int i = 0; i < n; i++) {
            if (matched[i] == 0) {
                return {};
            }
        }
        
        // 反转结果
        reverse(resultList.begin(), resultList.end());
        return resultList;
    }
};

/**
 * 使用DFS算法实现拓扑排序
 */
class TopologicalSortDFS {
private:
    bool hasCycle = false;
    vector<bool> visited;
    vector<bool> onPath;
    vector<int> postorder;
    
    void traverse(const vector<vector<int>>& graph, int node) {
        if (onPath[node]) {
            hasCycle = true;
            return;
        }
        
        if (visited[node]) {
            return;
        }
        
        visited[node] = true;
        onPath[node] = true;
        
        for (int neighbor : graph[node]) {
            traverse(graph, neighbor);
            if (hasCycle) {
                return;
            }
        }
        
        postorder.push_back(node);
        onPath[node] = false;
    }
    
public:
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        vector<vector<int>> graph(numCourses);
        for (const auto& prerequisite : prerequisites) {
            int from = prerequisite[1];
            int to = prerequisite[0];
            graph[from].push_back(to);
        }
        
        visited.assign(numCourses, false);
        onPath.assign(numCourses, false);
        hasCycle = false;
        postorder.clear();
        
        for (int i = 0; i < numCourses; i++) {
            if (!visited[i]) {
                traverse(graph, i);
            }
        }
        
        return !hasCycle;
    }
    
    vector<int> findOrderDFS(int numCourses, vector<vector<int>>& prerequisites) {
        vector<vector<int>> graph(numCourses);
        for (const auto& prerequisite : prerequisites) {
            int from = prerequisite[1];
            int to = prerequisite[0];
            graph[from].push_back(to);
        }
        
        visited.assign(numCourses, false);
        onPath.assign(numCourses, false);
        hasCycle = false;
        postorder.clear();
        
        for (int i = 0; i < numCourses; i++) {
            if (!visited[i]) {
                traverse(graph, i);
            }
        }
        
        if (hasCycle) {
            return {};
        }
        
        reverse(postorder.begin(), postorder.end());
        return postorder;
    }
};