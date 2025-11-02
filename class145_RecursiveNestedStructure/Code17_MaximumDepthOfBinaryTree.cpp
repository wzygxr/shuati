// LeetCode 104. Maximum Depth of Binary Tree
// 二叉树的最大深度
// 题目来源：https://leetcode.cn/problems/maximum-depth-of-binary-tree/

#include <iostream>
#include <queue>
#include <stack>
#include <algorithm>

/**
 * 问题描述：
 * 给定一个二叉树，找出其最大深度。
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 * 
 * 解题思路：
 * 1. 递归方法：使用深度优先搜索（DFS），计算左右子树的最大深度，取较大值加1
 * 2. 迭代BFS方法：使用广度优先搜索，逐层处理节点，记录层数
 * 3. 迭代DFS方法：使用显式栈模拟递归过程
 * 
 * 时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
 * 空间复杂度：
 *   - 递归：最坏情况下O(N)（树为链状），平均O(log N)（平衡树）
 *   - 迭代BFS：O(W)，W是树中最宽层的节点数
 *   - 迭代DFS：O(H)，H是树的高度
 */

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class MaximumDepthOfBinaryTree {
public:
    /**
     * 递归方法计算二叉树的最大深度
     * @param root 二叉树的根节点
     * @return 二叉树的最大深度
     */
    int maxDepthRecursive(TreeNode* root) {
        // 基本情况：空节点深度为0
        if (root == nullptr) {
            return 0;
        }
        
        // 递归计算左子树的最大深度
        int leftDepth = maxDepthRecursive(root->left);
        // 递归计算右子树的最大深度
        int rightDepth = maxDepthRecursive(root->right);
        
        // 当前树的最大深度 = max(左子树最大深度, 右子树最大深度) + 1
        return std::max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 迭代方法计算二叉树的最大深度（使用BFS）
     * @param root 二叉树的根节点
     * @return 二叉树的最大深度
     */
    int maxDepthIterativeBFS(TreeNode* root) {
        // 基本情况：空树深度为0
        if (root == nullptr) {
            return 0;
        }
        
        // 使用队列进行广度优先搜索
        std::queue<TreeNode*> queue;
        queue.push(root);
        int depth = 0;
        
        // 逐层处理节点
        while (!queue.empty()) {
            // 当前层的节点数量
            int levelSize = queue.size();
            
            // 处理当前层的所有节点
            for (int i = 0; i < levelSize; i++) {
                TreeNode* current = queue.front();
                queue.pop();
                
                // 将下一层的节点加入队列
                if (current->left != nullptr) {
                    queue.push(current->left);
                }
                if (current->right != nullptr) {
                    queue.push(current->right);
                }
            }
            
            // 处理完一层，深度加1
            depth++;
        }
        
        return depth;
    }
    
    /**
     * 迭代方法计算二叉树的最大深度（使用DFS）
     * @param root 二叉树的根节点
     * @return 二叉树的最大深度
     */
    int maxDepthIterativeDFS(TreeNode* root) {
        // 基本情况：空树深度为0
        if (root == nullptr) {
            return 0;
        }
        
        // 使用栈存储节点和对应的深度
        std::stack<std::pair<TreeNode*, int>> stack;
        stack.push({root, 1});
        int maxDepth = 0;
        
        while (!stack.empty()) {
            auto [current, currentDepth] = stack.top();
            stack.pop();
            
            // 更新最大深度
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
            
            // 将子节点加入栈中，深度加1
            // 注意先压入右子节点，再压入左子节点，这样弹出时先处理左子节点
            if (current->right != nullptr) {
                stack.push({current->right, currentDepth + 1});
            }
            if (current->left != nullptr) {
                stack.push({current->left, currentDepth + 1});
            }
        }
        
        return maxDepth;
    }
};

/**
 * 辅助函数：释放树内存
 */
void deleteTree(TreeNode* root) {
    if (root == nullptr) {
        return;
    }
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 测试代码
int main() {
    MaximumDepthOfBinaryTree solution;
    
    // 构建测试用例1：[3,9,20,null,null,15,7]
    //      3
    //     / \
    //    9  20
    //      /  \
    //     15   7
    TreeNode* root1 = new TreeNode(3);
    root1->left = new TreeNode(9);
    root1->right = new TreeNode(20);
    root1->right->left = new TreeNode(15);
    root1->right->right = new TreeNode(7);
    
    // 测试递归方法
    std::cout << "递归方法 - 测试用例1的最大深度: " 
              << solution.maxDepthRecursive(root1) << std::endl;
    // 测试迭代BFS方法
    std::cout << "迭代BFS方法 - 测试用例1的最大深度: " 
              << solution.maxDepthIterativeBFS(root1) << std::endl;
    // 测试迭代DFS方法
    std::cout << "迭代DFS方法 - 测试用例1的最大深度: " 
              << solution.maxDepthIterativeDFS(root1) << std::endl;
    
    // 释放内存
    deleteTree(root1);
    
    // 构建测试用例2：[1,null,2]
    //    1
    //     \
    //      2
    TreeNode* root2 = new TreeNode(1);
    root2->right = new TreeNode(2);
    
    std::cout << "递归方法 - 测试用例2的最大深度: " 
              << solution.maxDepthRecursive(root2) << std::endl;
    std::cout << "迭代BFS方法 - 测试用例2的最大深度: " 
              << solution.maxDepthIterativeBFS(root2) << std::endl;
    std::cout << "迭代DFS方法 - 测试用例2的最大深度: " 
              << solution.maxDepthIterativeDFS(root2) << std::endl;
    
    // 释放内存
    deleteTree(root2);
    
    // 测试空树
    TreeNode* root3 = nullptr;
    std::cout << "递归方法 - 空树的最大深度: " 
              << solution.maxDepthRecursive(root3) << std::endl;
    std::cout << "迭代BFS方法 - 空树的最大深度: " 
              << solution.maxDepthIterativeBFS(root3) << std::endl;
    std::cout << "迭代DFS方法 - 空树的最大深度: " 
              << solution.maxDepthIterativeDFS(root3) << std::endl;
    
    // 测试单节点树
    TreeNode* root4 = new TreeNode(1);
    std::cout << "递归方法 - 单节点树的最大深度: " 
              << solution.maxDepthRecursive(root4) << std::endl;
    std::cout << "迭代BFS方法 - 单节点树的最大深度: " 
              << solution.maxDepthIterativeBFS(root4) << std::endl;
    std::cout << "迭代DFS方法 - 单节点树的最大深度: " 
              << solution.maxDepthIterativeDFS(root4) << std::endl;
    
    // 释放内存
    deleteTree(root4);
    
    return 0;
}

/**
 * 性能分析：
 * 
 * 1. 递归实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：
 *      - 最好情况：O(log N)，对于完全平衡的二叉树
 *      - 最坏情况：O(N)，对于链状树（每个节点只有一个子节点）
 *      - 平均情况：O(log N)
 *    - 优点：代码简洁，易于理解
 *    - 缺点：对于非常深的树可能导致栈溢出
 * 
 * 2. 迭代BFS实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：O(W)，其中W是树中最宽层的节点数
 *    - 优点：避免了递归调用栈溢出的风险，对于寻找最近的节点更高效
 *    - 缺点：代码相对复杂，需要队列存储当前层的所有节点
 * 
 * 3. 迭代DFS实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：O(H)，其中H是树的高度
 *    - 优点：对于不平衡的树，可能比BFS更节省空间，且能更快地找到深路径
 *    - 缺点：需要手动维护栈和深度信息
 * 
 * 工程化考量：
 * 1. 内存管理：在C++中，需要注意手动释放动态分配的内存，避免内存泄漏
 * 2. 递归深度：对于非常深的树，递归实现可能导致栈溢出，应优先考虑迭代实现
 * 3. 性能优化：
 *    - 可以使用std::max而不是自己实现最大值比较
 *    - 在迭代实现中，预先计算levelSize可以避免在循环中重复调用queue.size()
 * 4. 异常处理：可以添加输入验证，确保传入的树指针有效
 * 5. 多线程安全：如果在多线程环境中使用，需要考虑线程安全问题
 * 6. 测试覆盖：应该覆盖各种边界情况，如空树、单节点树、不平衡树等
 */