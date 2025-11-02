// LeetCode 429. N-ary Tree Level Order Traversal
// N叉树的层序遍历
// 题目来源：https://leetcode.cn/problems/n-ary-tree-level-order-traversal/

#include <iostream>
#include <vector>
#include <queue>

/**
 * 问题描述：
 * 给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）。
 * 树的序列化输入是用层序遍历，每组子节点都由 null 值分隔（参见示例）。
 * 
 * 解题思路：
 * 1. 递归方法：使用深度优先搜索，记录每个节点的层级，并将节点值添加到对应层级的列表中
 * 2. 迭代方法：使用队列进行广度优先搜索，逐层处理节点
 * 
 * 时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
 * 空间复杂度：
 *   - 递归：O(H)，H是树的高度，递归调用栈的最大深度
 *   - 迭代：O(W)，W是树中最宽层的节点数，队列的最大大小
 */

// N叉树节点定义
class Node {
public:
    int val;
    std::vector<Node*> children;

    Node() {}

    Node(int _val) {
        val = _val;
    }

    Node(int _val, std::vector<Node*> _children) {
        val = _val;
        children = _children;
    }
};

class NaryTreeLevelOrderTraversal {
public:
    /**
     * 递归实现层序遍历
     * @param root N叉树的根节点
     * @return 层序遍历的结果列表
     */
    std::vector<std::vector<int>> levelOrderRecursive(Node* root) {
        std::vector<std::vector<int>> result;
        if (root == nullptr) {
            return result;
        }
        
        // 从第0层开始递归遍历
        dfs(root, 0, result);
        return result;
    }
    
    /**
     * 深度优先搜索辅助方法，按层收集节点值
     * @param node 当前节点
     * @param level 当前节点的层级
     * @param result 存储层序遍历结果的列表
     */
    void dfs(Node* node, int level, std::vector<std::vector<int>>& result) {
        // 如果当前层级的列表还不存在，创建它
        if (level >= result.size()) {
            result.push_back(std::vector<int>());
        }
        
        // 将当前节点的值添加到对应层级的列表中
        result[level].push_back(node->val);
        
        // 递归处理所有子节点，层级加1
        for (Node* child : node->children) {
            dfs(child, level + 1, result);
        }
    }
    
    /**
     * 迭代实现层序遍历（使用队列）
     * @param root N叉树的根节点
     * @return 层序遍历的结果列表
     */
    std::vector<std::vector<int>> levelOrderIterative(Node* root) {
        std::vector<std::vector<int>> result;
        if (root == nullptr) {
            return result;
        }
        
        // 使用队列进行广度优先搜索
        std::queue<Node*> queue;
        queue.push(root);
        
        // 逐层处理节点
        while (!queue.empty()) {
            int levelSize = queue.size(); // 当前层的节点数量
            std::vector<int> currentLevel;
            
            // 处理当前层的所有节点
            for (int i = 0; i < levelSize; i++) {
                Node* currentNode = queue.front();
                queue.pop();
                currentLevel.push_back(currentNode->val);
                
                // 将子节点加入队列，用于处理下一层
                for (Node* child : currentNode->children) {
                    queue.push(child);
                }
            }
            
            // 将当前层的结果添加到最终结果中
            result.push_back(currentLevel);
        }
        
        return result;
    }
};

/**
 * 打印结果的辅助函数
 */
void printResult(const std::vector<std::vector<int>>& result) {
    std::cout << "[";
    for (size_t i = 0; i < result.size(); i++) {
        std::cout << "[";
        const std::vector<int>& level = result[i];
        for (size_t j = 0; j < level.size(); j++) {
            std::cout << level[j];
            if (j < level.size() - 1) {
                std::cout << ", ";
            }
        }
        std::cout << "]";
        if (i < result.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 释放树内存的辅助函数
 */
void deleteTree(Node* root) {
    if (root == nullptr) {
        return;
    }
    for (Node* child : root->children) {
        deleteTree(child);
    }
    delete root;
}

// 测试代码
int main() {
    NaryTreeLevelOrderTraversal solution;
    
    // 构建测试用例的N叉树
    // 示例：[1,null,3,2,4,null,5,6]
    Node* root = new Node(1);
    Node* node3 = new Node(3);
    Node* node2 = new Node(2);
    Node* node4 = new Node(4);
    Node* node5 = new Node(5);
    Node* node6 = new Node(6);
    
    root->children = {node3, node2, node4};
    node3->children = {node5, node6};
    
    // 递归方法测试
    std::cout << "递归实现结果:" << std::endl;
    std::vector<std::vector<int>> result1 = solution.levelOrderRecursive(root);
    printResult(result1);
    
    // 迭代方法测试
    std::cout << "\n迭代实现结果:" << std::endl;
    std::vector<std::vector<int>> result2 = solution.levelOrderIterative(root);
    printResult(result2);
    
    // 空树测试
    std::cout << "\n空树测试:" << std::endl;
    std::vector<std::vector<int>> result3 = solution.levelOrderRecursive(nullptr);
    printResult(result3);
    
    std::vector<std::vector<int>> result4 = solution.levelOrderIterative(nullptr);
    printResult(result4);
    
    // 释放树内存
    deleteTree(root);
    
    return 0;
}

/**
 * 性能分析：
 * - 时间复杂度：两种实现都是O(N)，其中N是树中的节点数，每个节点只被访问一次
 * 
 * - 空间复杂度：
 *   - 递归：O(H)，H是树的高度，递归调用栈的最大深度
 *     最坏情况下，树是一条链，空间复杂度为O(N)
 *   - 迭代：O(W)，W是树中最宽层的节点数，队列的最大大小
 *     最坏情况下，最后一层全是叶子节点，空间复杂度为O(N)
 * 
 * 两种实现方法的对比：
 * 1. 递归实现更简洁，但对于非常深的树可能导致栈溢出
 * 2. 迭代实现更稳健，不受递归深度限制，对于大型树更安全
 * 
 * 工程化考量：
 * 1. 内存管理：在C++中，需要注意手动释放动态分配的内存，避免内存泄漏
 * 2. 异常处理：在实际应用中，应该检查输入树是否为nullptr，以及树的结构是否合法
 * 3. 对于非常大的树，应该优先考虑迭代实现，避免栈溢出风险
 * 4. 可以使用智能指针（如std::shared_ptr）来管理节点内存，简化内存管理
 * 5. 可以添加并行处理来加速遍历，但需要注意线程安全问题
 */