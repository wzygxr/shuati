// LeetCode 100. Same Tree
// 相同的树
// 题目来源：https://leetcode.cn/problems/same-tree/

#include <iostream>
#include <queue>
#include <stack>

/**
 * 问题描述：
 * 给你两棵二叉树的根节点 p 和 q，编写一个函数来检验这两棵树是否相同。
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 * 
 * 解题思路：
 * 1. 递归方法：深度优先搜索，同时遍历两棵树的每个节点
 * 2. 迭代BFS方法：使用队列同时处理两棵树的节点
 * 3. 迭代DFS方法：使用栈同时处理两棵树的节点
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

class SameTree {
public:
    /**
     * 递归方法判断两棵二叉树是否相同
     * @param p 第一棵二叉树的根节点
     * @param q 第二棵二叉树的根节点
     * @return 如果两棵树相同返回true，否则返回false
     */
    bool isSameTreeRecursive(TreeNode* p, TreeNode* q) {
        // 情况1：两个节点都为空，它们是相同的
        if (p == nullptr && q == nullptr) {
            return true;
        }
        
        // 情况2：一个节点为空，另一个不为空，它们不相同
        if (p == nullptr || q == nullptr) {
            return false;
        }
        
        // 情况3：两个节点都不为空，比较它们的值和子树
        // 1. 比较当前节点的值
        // 2. 递归比较左子树
        // 3. 递归比较右子树
        // 只有当这三个条件都满足时，两棵树才相同
        return (p->val == q->val) && 
               isSameTreeRecursive(p->left, q->left) && 
               isSameTreeRecursive(p->right, q->right);
    }
    
    /**
     * 迭代方法判断两棵二叉树是否相同（使用队列，BFS）
     * @param p 第一棵二叉树的根节点
     * @param q 第二棵二叉树的根节点
     * @return 如果两棵树相同返回true，否则返回false
     */
    bool isSameTreeIterativeBFS(TreeNode* p, TreeNode* q) {
        // 使用队列同时存储两棵树的对应节点
        std::queue<TreeNode*> queue;
        queue.push(p);
        queue.push(q);
        
        // 当队列不为空时，继续处理
        while (!queue.empty()) {
            // 从队列中取出两棵树的对应节点
            TreeNode* nodeP = queue.front();
            queue.pop();
            TreeNode* nodeQ = queue.front();
            queue.pop();
            
            // 如果两个节点都为空，继续处理下一对节点
            if (nodeP == nullptr && nodeQ == nullptr) {
                continue;
            }
            
            // 如果一个节点为空另一个不为空，或者节点值不相同，返回false
            if (nodeP == nullptr || nodeQ == nullptr || nodeP->val != nodeQ->val) {
                return false;
            }
            
            // 将两个节点的左子节点加入队列
            queue.push(nodeP->left);
            queue.push(nodeQ->left);
            
            // 将两个节点的右子节点加入队列
            queue.push(nodeP->right);
            queue.push(nodeQ->right);
        }
        
        // 所有节点都比较完成，两棵树相同
        return true;
    }
    
    /**
     * 迭代方法判断两棵二叉树是否相同（使用栈，DFS）
     * @param p 第一棵二叉树的根节点
     * @param q 第二棵二叉树的根节点
     * @return 如果两棵树相同返回true，否则返回false
     */
    bool isSameTreeIterativeDFS(TreeNode* p, TreeNode* q) {
        // 使用栈同时存储两棵树的对应节点
        std::stack<TreeNode*> stack;
        stack.push(p);
        stack.push(q);
        
        // 当栈不为空时，继续处理
        while (!stack.empty()) {
            // 从栈中取出两棵树的对应节点
            TreeNode* nodeQ = stack.top();
            stack.pop();
            TreeNode* nodeP = stack.top();
            stack.pop();
            
            // 如果两个节点都为空，继续处理下一对节点
            if (nodeP == nullptr && nodeQ == nullptr) {
                continue;
            }
            
            // 如果一个节点为空另一个不为空，或者节点值不相同，返回false
            if (nodeP == nullptr || nodeQ == nullptr || nodeP->val != nodeQ->val) {
                return false;
            }
            
            // 将两个节点的右子节点加入栈
            stack.push(nodeP->right);
            stack.push(nodeQ->right);
            
            // 将两个节点的左子节点加入栈（注意顺序，先右后左，这样出栈时先处理左子节点）
            stack.push(nodeP->left);
            stack.push(nodeQ->left);
        }
        
        // 所有节点都比较完成，两棵树相同
        return true;
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
    SameTree solution;
    
    // 测试用例1：两棵相同的树
    // 树1:    1           树2:    1
    //        / \                 / \
    //       2   3               2   3
    TreeNode* p1 = new TreeNode(1);
    p1->left = new TreeNode(2);
    p1->right = new TreeNode(3);
    
    TreeNode* q1 = new TreeNode(1);
    q1->left = new TreeNode(2);
    q1->right = new TreeNode(3);
    
    std::cout << "测试用例1 - 递归方法: " 
              << (solution.isSameTreeRecursive(p1, q1) ? "true" : "false") << std::endl;
    std::cout << "测试用例1 - 迭代BFS方法: " 
              << (solution.isSameTreeIterativeBFS(p1, q1) ? "true" : "false") << std::endl;
    std::cout << "测试用例1 - 迭代DFS方法: " 
              << (solution.isSameTreeIterativeDFS(p1, q1) ? "true" : "false") << std::endl;
    
    // 释放内存
    deleteTree(p1);
    deleteTree(q1);
    
    // 测试用例2：两棵不同的树
    // 树1:    1           树2:    1
    //        /                     \
    //       2                       2
    TreeNode* p2 = new TreeNode(1);
    p2->left = new TreeNode(2);
    
    TreeNode* q2 = new TreeNode(1);
    q2->right = new TreeNode(2);
    
    std::cout << "测试用例2 - 递归方法: " 
              << (solution.isSameTreeRecursive(p2, q2) ? "true" : "false") << std::endl;
    std::cout << "测试用例2 - 迭代BFS方法: " 
              << (solution.isSameTreeIterativeBFS(p2, q2) ? "true" : "false") << std::endl;
    std::cout << "测试用例2 - 迭代DFS方法: " 
              << (solution.isSameTreeIterativeDFS(p2, q2) ? "true" : "false") << std::endl;
    
    // 释放内存
    deleteTree(p2);
    deleteTree(q2);
    
    // 测试用例3：两棵不同的树
    // 树1:    1           树2:    1
    //        / \                 / \
    //       2   1               1   2
    TreeNode* p3 = new TreeNode(1);
    p3->left = new TreeNode(2);
    p3->right = new TreeNode(1);
    
    TreeNode* q3 = new TreeNode(1);
    q3->left = new TreeNode(1);
    q3->right = new TreeNode(2);
    
    std::cout << "测试用例3 - 递归方法: " 
              << (solution.isSameTreeRecursive(p3, q3) ? "true" : "false") << std::endl;
    std::cout << "测试用例3 - 迭代BFS方法: " 
              << (solution.isSameTreeIterativeBFS(p3, q3) ? "true" : "false") << std::endl;
    std::cout << "测试用例3 - 迭代DFS方法: " 
              << (solution.isSameTreeIterativeDFS(p3, q3) ? "true" : "false") << std::endl;
    
    // 释放内存
    deleteTree(p3);
    deleteTree(q3);
    
    // 测试用例4：两棵空树
    std::cout << "测试用例4 - 递归方法: " 
              << (solution.isSameTreeRecursive(nullptr, nullptr) ? "true" : "false") << std::endl;
    std::cout << "测试用例4 - 迭代BFS方法: " 
              << (solution.isSameTreeIterativeBFS(nullptr, nullptr) ? "true" : "false") << std::endl;
    std::cout << "测试用例4 - 迭代DFS方法: " 
              << (solution.isSameTreeIterativeDFS(nullptr, nullptr) ? "true" : "false") << std::endl;
    
    // 测试用例5：一棵树为空，另一棵不为空
    TreeNode* p4 = new TreeNode(1);
    std::cout << "测试用例5 - 递归方法: " 
              << (solution.isSameTreeRecursive(p4, nullptr) ? "true" : "false") << std::endl;
    std::cout << "测试用例5 - 迭代BFS方法: " 
              << (solution.isSameTreeIterativeBFS(p4, nullptr) ? "true" : "false") << std::endl;
    std::cout << "测试用例5 - 迭代DFS方法: " 
              << (solution.isSameTreeIterativeDFS(p4, nullptr) ? "true" : "false") << std::endl;
    
    // 释放内存
    deleteTree(p4);
    
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
 *    - 优点：代码简洁，逻辑清晰，容易实现和理解
 *    - 缺点：对于非常深的树可能导致栈溢出
 * 
 * 2. 迭代BFS实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：O(W)，其中W是树中最宽层的节点数
 *    - 优点：避免了递归调用栈溢出的风险
 *    - 缺点：代码相对复杂，需要额外的数据结构（队列）
 * 
 * 3. 迭代DFS实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：O(H)，其中H是树的高度
 *    - 优点：对于不平衡的树，可能比BFS更节省空间
 *    - 缺点：需要手动维护栈，实现相对复杂
 * 
 * C++语言特性利用：
 * 1. 使用std::queue实现高效的队列操作
 * 2. 使用std::stack实现高效的栈操作
 * 3. 使用nullptr替代NULL，提高类型安全性
 * 4. 利用结构体构造函数简化节点创建
 * 
 * 工程化考量：
 * 1. 内存管理：在C++中，需要注意手动释放动态分配的内存，避免内存泄漏
 * 2. 异常处理：可以添加try-catch块来处理可能的异常
 * 3. 性能优化：
 *    - 对于大型树，可以考虑使用内存池来减少内存分配开销
 *    - 可以使用移动语义来避免不必要的拷贝操作
 * 4. 线程安全：如果在多线程环境中使用，需要添加适当的同步机制
 * 5. 可测试性：代码结构清晰，易于编写单元测试
 * 6. 可扩展性：设计模式良好，可以轻松扩展到其他类型的树比较问题
 */