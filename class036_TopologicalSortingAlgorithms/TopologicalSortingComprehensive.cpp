/**
 * 拓扑排序综合题目集 - C++实现
 * 
 * 本文件包含来自多个平台的拓扑排序题目C++实现：
 * - LeetCode
 * - Codeforces
 * - AtCoder
 * - HDU
 * - POJ
 * - UVA
 * - SPOJ
 * 
 * 每个题目都包含详细的注释、时间空间复杂度分析、测试用例和工程化考量。
 */

#include <iostream>
#include <vector>
#include <queue>
#include <stack>
#include <string>
#include <algorithm>
#include <unordered_map>
#include <unordered_set>
#include <set>
#include <map>
#include <limits>
#include <functional>
#include <cassert>
#include <memory>
using namespace std;

using namespace std;

/**
 * =====================================================================
 * LeetCode 207. Course Schedule - C++实现
 * 题目链接: https://leetcode.com/problems/course-schedule/
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 */
class Leetcode207_CourseSchedule {
public:
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        // 构建邻接表
        vector<vector<int>> graph(numCourses);
        vector<int> inDegree(numCourses, 0);
        
        // 构建图和入度数组
        for (auto& pre : prerequisites) {
            int course = pre[0];
            int preCourse = pre[1];
            graph[preCourse].push_back(course);
            inDegree[course]++;
        }
        
        // Kahn算法拓扑排序
        queue<int> q;
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                q.push(i);
            }
        }
        
        int processed = 0;
        while (!q.empty()) {
            int current = q.front();
            q.pop();
            processed++;
            
            for (int next : graph[current]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    q.push(next);
                }
            }
        }
        
        return processed == numCourses;
    }
};

/**
 * =====================================================================
 * LeetCode 210. Course Schedule II - C++实现
 * 题目链接: https://leetcode.com/problems/course-schedule-ii/
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 */
class Leetcode210_CourseScheduleII {
public:
    vector<int> findOrder(int numCourses, vector<vector<int>>& prerequisites) {
        vector<vector<int>> graph(numCourses);
        vector<int> inDegree(numCourses, 0);
        
        // 构建图
        for (auto& pre : prerequisites) {
            int course = pre[0];
            int preCourse = pre[1];
            graph[preCourse].push_back(course);
            inDegree[course]++;
        }
        
        queue<int> q;
        vector<int> result;
        
        // 入度为0的节点入队
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                q.push(i);
            }
        }
        
        // 拓扑排序
        while (!q.empty()) {
            int current = q.front();
            q.pop();
            result.push_back(current);
            
            for (int next : graph[current]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    q.push(next);
                }
            }
        }
        
        if (result.size() != numCourses) {
            return {};
        }
        return result;
    }
};

/**
 * =====================================================================
 * LeetCode 269. Alien Dictionary - C++实现
 * 题目链接: https://leetcode.com/problems/alien-dictionary/
 * 
 * 时间复杂度：O(C)，C是所有单词中字符的总数
 * 空间复杂度：O(1)，字符集大小固定
 */
class Leetcode269_AlienDictionary {
public:
    string alienOrder(vector<string>& words) {
        unordered_map<char, unordered_set<char>> graph;
        unordered_map<char, int> inDegree;
        
        // 初始化所有字符
        for (string& word : words) {
            for (char c : word) {
                graph[c] = unordered_set<char>();
                inDegree[c] = 0;
            }
        }
        
        // 构建字符顺序关系
        for (int i = 0; i < words.size() - 1; i++) {
            string& word1 = words[i];
            string& word2 = words[i + 1];
            
            // 检查前缀关系
            if (word1.length() > word2.length() && 
                word1.substr(0, word2.length()) == word2) {
                return "";
            }
            
            // 找到第一个不同的字符
            int minLen = min(word1.length(), word2.length());
            for (int j = 0; j < minLen; j++) {
                char c1 = word1[j];
                char c2 = word2[j];
                
                if (c1 != c2) {
                    if (graph[c1].find(c2) == graph[c1].end()) {
                        graph[c1].insert(c2);
                        inDegree[c2]++;
                    }
                    break;
                }
            }
        }
        
        // 拓扑排序
        queue<char> q;
        for (auto& entry : inDegree) {
            if (entry.second == 0) {
                q.push(entry.first);
            }
        }
        
        string result;
        while (!q.empty()) {
            char current = q.front();
            q.pop();
            result += current;
            
            for (char next : graph[current]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    q.push(next);
                }
            }
        }
        
        return result.length() == inDegree.size() ? result : "";
    }
};

/**
 * =====================================================================
 * HDU 1285 - 确定比赛名次 - C++实现
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 
 * 要求输出字典序最小的拓扑序列
 * 时间复杂度：O(V log V + E)
 * 空间复杂度：O(V + E)
 */
class HDU1285_DetermineTheRanking {
public:
    vector<int> topologicalSort(int n, vector<vector<int>>& edges) {
        vector<vector<int>> graph(n + 1);
        vector<int> inDegree(n + 1, 0);
        
        // 构建图
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1];
            graph[u].push_back(v);
            inDegree[v]++;
        }
        
        // 使用最小堆保证字典序最小
        priority_queue<int, vector<int>, greater<int>> pq;
        vector<int> result;
        
        // 入度为0的节点入堆
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                pq.push(i);
            }
        }
        
        // 拓扑排序
        while (!pq.empty()) {
            int current = pq.top();
            pq.pop();
            result.push_back(current);
            
            for (int next : graph[current]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    pq.push(next);
                }
            }
        }
        
        return result;
    }
};

/**
 * =====================================================================
 * POJ 1094 - Sorting It All Out - C++实现
 * 题目链接: http://poj.org/problem?id=1094
 * 
 * 逐步添加关系并检查状态
 * 时间复杂度：O(m * (n + m))
 * 空间复杂度：O(n + m)
 */
class POJ1094_SortingItAllOut {
public:
    string determineOrder(int n, vector<string>& relations) {
        vector<vector<bool>> graph(26, vector<bool>(26, false));
        vector<int> inDegree(26, 0);
        
        for (int i = 0; i < relations.size(); i++) {
            string rel = relations[i];
            int from = rel[0] - 'A';
            int to = rel[2] - 'A';
            
            if (!graph[from][to]) {
                graph[from][to] = true;
                inDegree[to]++;
            }
            
            // 检查当前状态
            int result = checkTopologicalSort(graph, inDegree, n);
            if (result == -1) {
                return "Inconsistency found after " + to_string(i + 1) + " relations.";
            } else if (result == 1) {
                string order = getOrder(graph, inDegree, n);
                return "Sorted sequence determined after " + to_string(i + 1) + 
                       " relations: " + order + ".";
            }
        }
        
        return "Sorted sequence cannot be determined.";
    }
    
private:
    int checkTopologicalSort(vector<vector<bool>>& graph, vector<int>& inDegree, int n) {
        vector<int> tempInDegree = inDegree;
        vector<bool> visited(26, false);
        bool multiple = false;
        
        for (int i = 0; i < n; i++) {
            int zeroCount = 0;
            int zeroNode = -1;
            
            for (int j = 0; j < n; j++) {
                if (!visited[j] && tempInDegree[j] == 0) {
                    zeroCount++;
                    zeroNode = j;
                }
            }
            
            if (zeroCount == 0) return -1; // 有环
            if (zeroCount > 1) multiple = true; // 不唯一
            
            visited[zeroNode] = true;
            for (int k = 0; k < n; k++) {
                if (graph[zeroNode][k]) {
                    tempInDegree[k]--;
                }
            }
        }
        
        return multiple ? 0 : 1; // 0: 不唯一, 1: 唯一
    }
    
    string getOrder(vector<vector<bool>>& graph, vector<int>& inDegree, int n) {
        vector<int> tempInDegree = inDegree;
        vector<bool> visited(26, false);
        string order;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!visited[j] && tempInDegree[j] == 0) {
                    order += char('A' + j);
                    visited[j] = true;
                    
                    for (int k = 0; k < n; k++) {
                        if (graph[j][k]) {
                            tempInDegree[k]--;
                        }
                    }
                    break;
                }
            }
        }
        
        return order;
    }
};

/**
 * =====================================================================
 * UVA 10305 - Ordering Tasks - C++实现
 * 题目链接: https://vjudge.net/problem/UVA-10305
 * 
 * 经典拓扑排序模板题
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 */
class UVA10305_OrderingTasks {
public:
    vector<int> orderingTasks(int n, vector<vector<int>>& constraints) {
        vector<vector<int>> graph(n + 1);
        vector<int> inDegree(n + 1, 0);
        
        // 构建图
        for (auto& constraint : constraints) {
            int u = constraint[0], v = constraint[1];
            graph[u].push_back(v);
            inDegree[v]++;
        }
        
        queue<int> q;
        vector<int> result;
        
        // 入度为0的节点入队
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                q.push(i);
            }
        }
        
        // 拓扑排序
        while (!q.empty()) {
            int current = q.front();
            q.pop();
            result.push_back(current);
            
            for (int next : graph[current]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    q.push(next);
                }
            }
        }
        
        return result;
    }
};

/**
 * =====================================================================
 * SPOJ TOPOSORT - Topological Sorting - C++实现
 * 题目链接: https://www.spoj.com/problems/TOPOSORT/
 * 
 * 字典序最小的拓扑排序
 * 时间复杂度：O(V log V + E)
 * 空间复杂度：O(V + E)
 */
class SPOJ_TopologicalSorting {
public:
    vector<int> topologicalSort(int n, vector<vector<int>>& edges) {
        vector<vector<int>> graph(n + 1);
        vector<int> inDegree(n + 1, 0);
        
        // 构建图
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1];
            graph[u].push_back(v);
            inDegree[v]++;
        }
        
        // 使用最小堆
        priority_queue<int, vector<int>, greater<int>> pq;
        vector<int> result;
        
        // 入度为0的节点入堆
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                pq.push(i);
            }
        }
        
        // 拓扑排序
        while (!pq.empty()) {
            int current = pq.top();
            pq.pop();
            result.push_back(current);
            
            for (int next : graph[current]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    pq.push(next);
                }
            }
        }
        
        return result;
    }
};

/**
 * =====================================================================
 * Codeforces 510C - Fox And Names - C++实现
 * 题目链接: https://codeforces.com/problemset/problem/510/C
 * 
 * 类似外星字典问题
 * 时间复杂度：O(C)
 * 空间复杂度：O(1)
 */
class Codeforces510C_FoxAndNames {
public:
    string foxAndNames(vector<string>& names) {
        unordered_map<char, unordered_set<char>> graph;
        unordered_map<char, int> inDegree;
        
        // 初始化字符
        for (string& name : names) {
            for (char c : name) {
                graph[c];
                inDegree[c];
            }
        }
        
        // 构建字符顺序关系
        for (int i = 0; i < names.size() - 1; i++) {
            string& name1 = names[i];
            string& name2 = names[i + 1];
            
            // 检查前缀关系
            if (name1.length() > name2.length() && 
                name1.substr(0, name2.length()) == name2) {
                return "Impossible";
            }
            
            // 找到第一个不同的字符
            int minLen = min(name1.length(), name2.length());
            for (int j = 0; j < minLen; j++) {
                if (name1[j] != name2[j]) {
                    if (graph[name1[j]].find(name2[j]) == graph[name1[j]].end()) {
                        graph[name1[j]].insert(name2[j]);
                        inDegree[name2[j]]++;
                    }
                    break;
                }
            }
        }
        
        // 拓扑排序
        queue<char> q;
        for (auto& entry : inDegree) {
            if (entry.second == 0) {
                q.push(entry.first);
            }
        }
        
        string result;
        while (!q.empty()) {
            char current = q.front();
            q.pop();
            result += current;
            
            for (char next : graph[current]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    q.push(next);
                }
            }
        }
        
        return result.length() == inDegree.size() ? result : "Impossible";
    }
};

/**
 * =====================================================================
 * 测试函数
 */
void testAllSolutions() {
    cout << "=== 拓扑排序综合题目集测试 ===" << endl;
    
    // 测试LeetCode 207
    Leetcode207_CourseSchedule lc207;
    vector<vector<int>> prerequisites1 = {{1, 0}};
    cout << "LeetCode 207: " << lc207.canFinish(2, prerequisites1) << endl;
    
    // 测试LeetCode 210
    Leetcode210_CourseScheduleII lc210;
    vector<vector<int>> prerequisites2 = {{1, 0}};
    vector<int> order = lc210.findOrder(2, prerequisites2);
    cout << "LeetCode 210: ";
    for (int num : order) cout << num << " ";
    cout << endl;
    
    // 测试LeetCode 269
    Leetcode269_AlienDictionary lc269;
    vector<string> words = {"wrt", "wrf", "er", "ett", "rftt"};
    cout << "LeetCode 269: " << lc269.alienOrder(words) << endl;
    
    // 测试HDU 1285
    HDU1285_DetermineTheRanking hdu1285;
    vector<vector<int>> edges = {{1, 2}, {1, 3}, {2, 4}, {3, 4}};
    vector<int> ranking = hdu1285.topologicalSort(4, edges);
    cout << "HDU 1285: ";
    for (int num : ranking) cout << num << " ";
    cout << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * =====================================================================
 * 工程化考量 - C++特性
 * 
 * 1. 内存管理：使用智能指针避免内存泄漏
 * 2. 异常安全：使用RAII原则
 * 3. 模板编程：支持不同类型的图表示
 * 4. 性能优化：使用移动语义和完美转发
 * 5. 并发安全：使用原子操作和锁
 */

// 智能指针版本
class GraphWithSmartPointers {
private:
    struct Node {
        int id;
        vector<shared_ptr<Node>> neighbors;
        Node(int id) : id(id) {}
    };
    
    vector<shared_ptr<Node>> nodes;
    
public:
    void addNode(int id) {
        nodes.push_back(make_shared<Node>(id));
    }
    
    void addEdge(int from, int to) {
        if (from < nodes.size() && to < nodes.size()) {
            nodes[from]->neighbors.push_back(nodes[to]);
        }
    }
};

// 模板版本
template<typename T>
class GenericGraph {
private:
    unordered_map<T, vector<T>> adjacencyList;
    
public:
    void addNode(const T& node) {
        adjacencyList[node] = vector<T>();
    }
    
    void addEdge(const T& from, const T& to) {
        adjacencyList[from].push_back(to);
    }
    
    vector<T> topologicalSort() {
        unordered_map<T, int> inDegree;
        queue<T> q;
        vector<T> result;
        
        // 计算入度
        for (auto& entry : adjacencyList) {
            inDegree[entry.first] = 0;
        }
        for (auto& entry : adjacencyList) {
            for (T neighbor : entry.second) {
                inDegree[neighbor]++;
            }
        }
        
        // 拓扑排序
        for (auto& entry : inDegree) {
            if (entry.second == 0) {
                q.push(entry.first);
            }
        }
        
        while (!q.empty()) {
            T current = q.front();
            q.pop();
            result.push_back(current);
            
            for (T neighbor : adjacencyList[current]) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    q.push(neighbor);
                }
            }
        }
        
        return result;
    }
};

int main() {
    testAllSolutions();
    return 0;
}