// LeetCode 332. Reconstruct Itinerary
// 题目链接: https://leetcode.cn/problems/reconstruct-itinerary/
// 
// 题目描述:
// 给你一份航线列表tickets，其中tickets[i] = [from_i, to_i]表示飞机出发和降落的机场地点。请你对该行程进行重新规划排序。
// 所有这些机票都属于一个从JFK（肯尼迪国际机场）出发的先生，所以该行程必须从JFK开始。假设所有机票至少存在一种合理的行程。
// 如果你有多个有效的行程，请你按字典排序返回最小的行程组合。
// 例如，行程["JFK", "LGA"]与["JFK", "LGB"]相比，按字典排序更小的行程是["JFK", "LGA"]。
// 所有机票必须都用一次且只能用一次。
//
// 解题思路:
// 这是一个经典的欧拉路径问题。我们需要找到一条路径，使得它恰好使用了每条边一次，并且路径字典序最小。
// 我们可以使用Hierholzer算法来求解欧拉路径：
// 1. 构建图，使用邻接表表示，并且对每个节点的邻接列表进行排序以确保字典序最小
// 2. 使用深度优先搜索(DFS)遍历图，递归地访问每个节点的邻居
// 3. 当一个节点没有未访问的邻居时，将其添加到结果列表的开头
// 4. 最终得到的结果列表即为欧拉路径
//
// 时间复杂度: O(E log E)，其中E是边数，排序邻接列表需要O(E log E)的时间
// 空间复杂度: O(V + E)，其中V是顶点数，E是边数
// 是否为最优解: 是，Hierholzer算法是求解欧拉路径的高效算法

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <set>
#include <algorithm>
using namespace std;

class Solution {
public:
    // 递归版本
    vector<string> findItinerary(vector<vector<string>>& tickets) {
        // 构建图，使用multiset自动排序，确保字典序
        unordered_map<string, multiset<string>> graph;
        for (const auto& ticket : tickets) {
            graph[ticket[0]].insert(ticket[1]);
        }
        
        vector<string> result;
        dfs(graph, "JFK", result);
        
        // 反转结果，因为我们是在回溯时添加节点的
        reverse(result.begin(), result.end());
        return result;
    }
    
    void dfs(unordered_map<string, multiset<string>>& graph, string current, vector<string>& result) {
        // 当当前节点还有邻居时，继续访问
        while (!graph[current].empty()) {
            // 获取字典序最小的邻居
            string next = *graph[current].begin();
            // 移除该边，表示已经使用
            graph[current].erase(graph[current].begin());
            // 递归访问下一个节点
            dfs(graph, next, result);
        }
        // 当节点没有未访问的邻居时，将其添加到结果列表
        result.push_back(current);
    }
    
    // 迭代版本
    vector<string> findItineraryIterative(vector<vector<string>>& tickets) {
        // 构建图
        unordered_map<string, multiset<string>> graph;
        for (const auto& ticket : tickets) {
            graph[ticket[0]].insert(ticket[1]);
        }
        
        vector<string> result;
        vector<string> stack = {"JFK"};
        
        while (!stack.empty()) {
            string current = stack.back();
            
            // 如果当前节点还有邻居，则继续访问
            if (!graph[current].empty()) {
                string next = *graph[current].begin();
                graph[current].erase(graph[current].begin());
                stack.push_back(next);
            } else {
                // 否则，将当前节点添加到结果列表
                result.push_back(stack.back());
                stack.pop_back();
            }
        }
        
        // 反转结果
        reverse(result.begin(), result.end());
        return result;
    }
};

// 打印结果函数
void printResult(const vector<string>& result) {
    cout << "[";
    for (size_t i = 0; i < result.size(); i++) {
        cout << "\"" << result[i] << "\"";
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
    vector<vector<string>> tickets1 = {{"MUC", "LHR"}, {"JFK", "MUC"}, {"SFO", "SJC"}, {"LHR", "SFO"}};
    cout << "Test 1 (递归): ";
    printResult(solution.findItinerary(tickets1));
    cout << "Test 1 (迭代): ";
    printResult(solution.findItineraryIterative(tickets1));
    // 预期输出: ["JFK", "MUC", "LHR", "SFO", "SJC"]
    
    // 测试用例2
    vector<vector<string>> tickets2 = {{"JFK","SFO"},{"JFK","ATL"},{"SFO","ATL"},{"ATL","JFK"},{"ATL","SFO"}};
    cout << "Test 2 (递归): ";
    printResult(solution.findItinerary(tickets2));
    cout << "Test 2 (迭代): ";
    printResult(solution.findItineraryIterative(tickets2));
    // 预期输出: ["JFK","ATL","JFK","SFO","ATL","SFO"]
}

int main() {
    test();
    return 0;
}