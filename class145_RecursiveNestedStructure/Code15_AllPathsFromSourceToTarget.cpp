// LeetCode 797. All Paths From Source to Target
// 所有可能的路径
// 题目来源：https://leetcode.cn/problems/all-paths-from-source-to-target/

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>

/**
 * 问题描述：
 * 给你一个有 n 个节点的 有向无环图（DAG），请你找出所有从节点 0 到节点 n-1 的路径并输出（不要求按特定顺序）。
 * 二维数组的第 i 个数组中的元素表示从节点 i 可以直接到达的所有节点，空就是没有可以直接到达的节点。
 * 
 * 解题思路：
 * 1. 由于图是有向无环的（DAG），我们可以使用深度优先搜索（DFS）来遍历所有可能的路径
 * 2. 递归地探索每个节点的邻居，并记录路径
 * 3. 当到达目标节点（n-1）时，将当前路径添加到结果中
 * 
 * 时间复杂度：O(2^N * N)，其中N是节点数量，最坏情况下每个节点都可以选择是否加入路径，且路径长度最多为N
 * 空间复杂度：O(N)，递归调用栈的深度最多为N，同时存储路径的空间也是O(N)
 */

class AllPathsSourceTarget {
public:
    /**
     * 查找从源节点到目标节点的所有路径
     * @param graph 图的邻接表表示
     * @return 所有可能的路径列表
     */
    std::vector<std::vector<int>> allPathsSourceTarget(std::vector<std::vector<int>>& graph) {
        std::vector<std::vector<int>> result;
        std::vector<int> currentPath;
        currentPath.push_back(0); // 起始节点是0
        
        // 从节点0开始深度优先搜索
        dfs(graph, 0, graph.size() - 1, currentPath, result);
        
        return result;
    }
    
    /**
     * 深度优先搜索辅助方法
     * @param graph 图的邻接表表示
     * @param current 当前节点
     * @param target 目标节点（n-1）
     * @param currentPath 当前路径
     * @param result 结果列表，存储所有路径
     */
    void dfs(std::vector<std::vector<int>>& graph, int current, int target, 
             std::vector<int>& currentPath, std::vector<std::vector<int>>& result) {
        // 基础情况：到达目标节点
        if (current == target) {
            // 将当前路径的副本添加到结果中
            result.push_back(currentPath);
            return;
        }
        
        // 遍历当前节点的所有邻居
        for (int neighbor : graph[current]) {
            // 将邻居节点添加到当前路径
            currentPath.push_back(neighbor);
            // 递归探索邻居节点
            dfs(graph, neighbor, target, currentPath, result);
            // 回溯：移除最后添加的节点
            currentPath.pop_back();
        }
    }
    
    /**
     * 迭代版本的深度优先搜索实现
     * @param graph 图的邻接表表示
     * @return 所有可能的路径列表
     */
    std::vector<std::vector<int>> allPathsSourceTargetIterative(std::vector<std::vector<int>>& graph) {
        std::vector<std::vector<int>> result;
        int target = graph.size() - 1;
        
        // 使用栈来模拟递归调用
        // 栈中每个元素是一个包含当前节点和当前路径的pair
        std::stack<std::pair<int, std::vector<int>>> stack;
        std::vector<int> initialPath = {0};
        stack.push({0, initialPath});
        
        // 迭代DFS
        while (!stack.empty()) {
            auto current = stack.top();
            stack.pop();
            
            int node = current.first;
            std::vector<int> path = current.second;
            
            // 如果到达目标节点，将路径添加到结果
            if (node == target) {
                result.push_back(path);
                continue;
            }
            
            // 将当前节点的所有邻居加入栈中
            // 注意：为了保持与递归版本相似的路径顺序，我们需要反转邻居列表
            // 因为栈是后进先出的结构
            for (auto it = graph[node].rbegin(); it != graph[node].rend(); ++it) {
                std::vector<int> newPath = path;
                newPath.push_back(*it);
                stack.push({*it, newPath});
            }
        }
        
        return result;
    }
};

/**
 * 打印路径列表的辅助函数
 */
void printPaths(const std::vector<std::vector<int>>& paths) {
    for (const auto& path : paths) {
        std::cout << "[";
        for (size_t i = 0; i < path.size(); ++i) {
            std::cout << path[i];
            if (i < path.size() - 1) {
                std::cout << ", ";
            }
        }
        std::cout << "]" << std::endl;
    }
}

// 测试代码
int main() {
    AllPathsSourceTarget solution;
    
    // 测试用例1
    std::vector<std::vector<int>> graph1 = {{1, 2}, {3}, {3}, {}};
    std::cout << "递归DFS结果:" << std::endl;
    std::vector<std::vector<int>> result1 = solution.allPathsSourceTarget(graph1);
    printPaths(result1);
    
    std::cout << "\n迭代DFS结果:" << std::endl;
    std::vector<std::vector<int>> result1Iterative = solution.allPathsSourceTargetIterative(graph1);
    printPaths(result1Iterative);
    
    // 测试用例2
    std::vector<std::vector<int>> graph2 = {{4, 3, 1}, {3, 2, 4}, {3}, {4}, {}};
    std::cout << "\n递归DFS结果:" << std::endl;
    std::vector<std::vector<int>> result2 = solution.allPathsSourceTarget(graph2);
    printPaths(result2);
    
    std::cout << "\n迭代DFS结果:" << std::endl;
    std::vector<std::vector<int>> result2Iterative = solution.allPathsSourceTargetIterative(graph2);
    printPaths(result2Iterative);
    
    return 0;
}

/**
 * 性能分析：
 * - 时间复杂度：O(2^N * N)，其中N是节点数量
 *   最坏情况下，每个节点都可以选择是否加入路径，且路径长度最多为N
 *   例如，在完全二叉树形状的DAG中，路径数量可能达到2^(N-1)级别
 *   每个路径需要O(N)时间来复制
 * 
 * - 空间复杂度：O(N)，递归调用栈的深度最多为N，同时存储路径的空间也是O(N)
 *   注意：最终结果占用的空间不计入算法的空间复杂度分析
 * 
 * 工程化考量：
 * 1. 异常处理：在实际应用中，应该检查输入图是否为空，节点数量是否合法
 * 2. 对于大型图，可以考虑使用更高效的数据结构来存储路径，避免频繁复制
 * 3. 在C++中，递归深度可能会受到系统栈大小的限制，可以使用迭代版本来处理更深的图
 * 4. 当图中存在环时，这个算法会陷入死循环，因此需要确保图是DAG或添加访问标记
 * 5. 可以使用移动语义(std::move)来优化路径复制的性能
 */