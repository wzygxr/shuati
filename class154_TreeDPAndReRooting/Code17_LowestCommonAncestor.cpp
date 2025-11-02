// 二叉树的最近公共祖先
// 题目链接：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
// 给定一个二叉树，找到该树中两个指定节点的最近公共祖先（LCA）。
// 最近公共祖先的定义为："对于有根树 T 的两个节点 p 和 q，最近公共祖先是后代节点中同时包含 p 和 q 的最深节点（这里的一个节点也可以是它自己的后代）。"

/*
题目解析：
这是一个经典的树遍历问题。我们需要找到二叉树中两个节点的最近公共祖先，即同时是p和q的祖先且深度最深的节点。

算法思路：
1. 递归解法（后序遍历）：
   - 如果当前节点为空，返回nullptr
   - 如果当前节点是p或q中的一个，返回当前节点
   - 递归搜索左右子树
   - 如果左右子树搜索结果都不为空，说明p和q分别在当前节点的两侧，因此当前节点就是LCA
   - 如果只有一侧不为空，返回不为空的一侧结果
   - 如果两侧都为空，返回nullptr

时间复杂度：O(n) - 每个节点最多被访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)（递归调用栈的开销）
是否为最优解：是，这是解决二叉树最近公共祖先问题的最优方法

边界情况：
- 空树：返回nullptr
- p或q不存在于树中：题目假设p和q都存在于树中
- p是q的祖先或q是p的祖先：递归会正确返回祖先节点
- 树中只有两个节点：返回根节点

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 最近公共祖先问题与树形数据结构的层次关系分析相关
- 在生物信息学中，LCA问题与进化树分析有联系

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.cpp
*/

#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <stack>
#include <vector>

// 二叉树节点的定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

// 标准递归解法
class Solution {
public:
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        // 边界条件处理：空树
        if (root == nullptr) {
            return nullptr;
        }
        
        // 如果当前节点是p或q中的一个，直接返回当前节点
        if (root == p || root == q) {
            return root;
        }
        
        // 递归搜索左子树
        TreeNode* left = lowestCommonAncestor(root->left, p, q);
        
        // 递归搜索右子树
        TreeNode* right = lowestCommonAncestor(root->right, p, q);
        
        // 情况1：如果左右子树都找到了结果，说明p和q分别在当前节点的两侧，当前节点就是LCA
        if (left != nullptr && right != nullptr) {
            return root;
        }
        
        // 情况2：如果只有一侧找到结果，返回那一侧的结果
        // 情况3：如果两侧都没找到结果，返回nullptr
        return (left != nullptr) ? left : right;
    }
};

// 使用引用传递发现状态的优化递归解法
class SolutionOptimized {
public:
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        TreeNode* lca = nullptr;
        bool found_p = false;
        bool found_q = false;
        
        dfs(root, p, q, lca, found_p, found_q);
        return lca;
    }

private:
    void dfs(TreeNode* node, TreeNode* p, TreeNode* q, TreeNode*& lca,
             bool& found_p, bool& found_q) {
        // 如果节点为空或已经找到LCA，提前返回
        if (node == nullptr || lca != nullptr) {
            return;
        }
        
        // 保存当前状态（用于回溯）
        bool prev_found_p = found_p;
        bool prev_found_q = found_q;
        
        // 检查当前节点是否是目标节点
        if (node == p) {
            found_p = true;
        }
        if (node == q) {
            found_q = true;
        }
        
        // 递归搜索左子树
        dfs(node->left, p, q, lca, found_p, found_q);
        
        // 如果左子树已经找到LCA，直接返回
        if (lca != nullptr) {
            return;
        }
        
        // 递归搜索右子树
        dfs(node->right, p, q, lca, found_q, found_p);
        
        // 如果右子树已经找到LCA，直接返回
        if (lca != nullptr) {
            return;
        }
        
        // 判断当前节点是否是LCA
        if (found_p && found_q) {
            lca = node;
        }
        
        // 回溯：恢复之前的状态（只在当前节点不是目标节点时）
        if (node != p) {
            found_p = prev_found_p;
        }
        if (node != q) {
            found_q = prev_found_q;
        }
    }
};

// 使用结构体返回多个值的优化递归解法
class SolutionWithStructReturn {
public:
    struct Result {
        bool found_lca;
        TreeNode* lca_node;
        bool found_p;
        bool found_q;
        
        Result(bool fl = false, TreeNode* ln = nullptr, bool fp = false, bool fq = false)
            : found_lca(fl), lca_node(ln), found_p(fp), found_q(fq) {}
    };
    
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        Result result = dfs(root, p, q);
        return result.lca_node;
    }

private:
    Result dfs(TreeNode* node, TreeNode* p, TreeNode* q) {
        // 边界条件
        if (node == nullptr) {
            return Result();
        }
        
        // 检查当前节点是否是目标节点
        bool is_p = (node == p);
        bool is_q = (node == q);
        
        // 递归搜索左子树
        Result left = dfs(node->left, p, q);
        if (left.found_lca) {
            return left;  // 左子树已经找到LCA
        }
        
        // 递归搜索右子树
        Result right = dfs(node->right, p, q);
        if (right.found_lca) {
            return right;  // 右子树已经找到LCA
        }
        
        // 合并左右子树的搜索状态
        bool found_p = is_p || left.found_p || right.found_p;
        bool found_q = is_q || left.found_q || right.found_q;
        
        // 判断是否找到LCA
        if (found_p && found_q) {
            return Result(true, node, true, true);
        }
        
        // 返回当前子树的搜索状态
        return Result(false, nullptr, found_p, found_q);
    }
};

// 迭代实现版本（使用后序遍历的变体）
class SolutionIterative {
public:
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        // 边界条件处理
        if (root == nullptr || root == p || root == q) {
            return root;
        }
        
        // 存储每个节点的父节点映射
        std::unordered_map<TreeNode*, TreeNode*> parent_map;
        
        // 使用栈进行迭代后序遍历
        std::stack<TreeNode*> stack;
        stack.push(root);
        parent_map[root] = nullptr;  // 根节点没有父节点
        
        // 遍历树，直到找到p和q
        while (parent_map.find(p) == parent_map.end() || 
               parent_map.find(q) == parent_map.end()) {
            TreeNode* current = stack.top();
            stack.pop();
            
            // 先处理右子节点
            if (current->right != nullptr) {
                parent_map[current->right] = current;
                stack.push(current->right);
            }
            
            // 再处理左子节点
            if (current->left != nullptr) {
                parent_map[current->left] = current;
                stack.push(current->left);
            }
        }
        
        // 收集p的所有祖先
        std::unordered_set<TreeNode*> ancestors;
        TreeNode* current = p;
        while (current != nullptr) {
            ancestors.insert(current);
            current = parent_map[current];
        }
        
        // 向上查找q的祖先，直到找到在p的祖先集合中的节点
        current = q;
        while (ancestors.find(current) == ancestors.end()) {
            current = parent_map[current];
        }
        
        return current;
    }
};

// 辅助函数：构建树（根据数组）
TreeNode* buildTree(const std::vector<int*>& nodes, int index) {
    if (index >= nodes.size() || nodes[index] == nullptr) {
        return nullptr;
    }
    
    TreeNode* root = new TreeNode(*nodes[index]);
    root->left = buildTree(nodes, 2 * index + 1);
    root->right = buildTree(nodes, 2 * index + 2);
    
    return root;
}

// 辅助函数：释放树内存
void deleteTree(TreeNode* root) {
    if (root == nullptr) {
        return;
    }
    
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 主函数，用于测试
int main() {
    // 构建测试树
    //       3
    //      / \
    //     5   1
    //    / \ / \
    //   6  2 0  8
    //     / \
    //    7   4
    TreeNode* root = new TreeNode(3);
    TreeNode* node5 = new TreeNode(5);
    TreeNode* node1 = new TreeNode(1);
    TreeNode* node6 = new TreeNode(6);
    TreeNode* node2 = new TreeNode(2);
    TreeNode* node0 = new TreeNode(0);
    TreeNode* node8 = new TreeNode(8);
    TreeNode* node7 = new TreeNode(7);
    TreeNode* node4 = new TreeNode(4);
    
    root->left = node5;
    root->right = node1;
    node5->left = node6;
    node5->right = node2;
    node1->left = node0;
    node1->right = node8;
    node2->left = node7;
    node2->right = node4;
    
    // 测试标准递归解法
    Solution solution;
    std::cout << "=== 标准递归解法 ===" << std::endl;
    
    // 测试用例1: p = 5, q = 1，预期输出: 3
    TreeNode* result1 = solution.lowestCommonAncestor(root, node5, node1);
    std::cout << "测试用例1结果: " << result1->val << std::endl;  // 预期输出: 3
    
    // 测试用例2: p = 5, q = 4，预期输出: 5
    TreeNode* result2 = solution.lowestCommonAncestor(root, node5, node4);
    std::cout << "测试用例2结果: " << result2->val << std::endl;  // 预期输出: 5
    
    // 测试用例3: p = 6, q = 4，预期输出: 5
    TreeNode* result3 = solution.lowestCommonAncestor(root, node6, node4);
    std::cout << "测试用例3结果: " << result3->val << std::endl;  // 预期输出: 5
    
    // 测试优化递归解法
    SolutionOptimized optimizedSolution;
    std::cout << "\n=== 优化递归解法 ===" << std::endl;
    TreeNode* optResult = optimizedSolution.lowestCommonAncestor(root, node5, node1);
    std::cout << "测试用例1结果: " << optResult->val << std::endl;  // 预期输出: 3
    
    // 测试结构体返回值优化解法
    SolutionWithStructReturn structSolution;
    std::cout << "\n=== 结构体返回值优化解法 ===" << std::endl;
    TreeNode* structResult = structSolution.lowestCommonAncestor(root, node5, node1);
    std::cout << "测试用例1结果: " << structResult->val << std::endl;  // 预期输出: 3
    
    // 测试迭代解法
    SolutionIterative iterativeSolution;
    std::cout << "\n=== 迭代解法 ===" << std::endl;
    TreeNode* iterResult = iterativeSolution.lowestCommonAncestor(root, node5, node1);
    std::cout << "测试用例1结果: " << iterResult->val << std::endl;  // 预期输出: 3
    
    // 释放树内存
    deleteTree(root);
    
    return 0;
}

/*
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 使用了nullptr替代NULL，更加安全
   - 代码可以处理p或q是另一个节点的祖先的情况

2. 内存管理：
   - 添加了deleteTree辅助函数用于释放树内存，避免内存泄漏
   - 在C++中需要特别注意动态分配内存的管理

3. 性能优化：
   - 递归版本中添加了提前终止条件，避免不必要的搜索
   - 优化版本使用引用传递状态，减少值拷贝开销
   - 迭代版本避免了深层递归可能导致的栈溢出问题

4. 代码质量：
   - 提供了多种实现方式，包括标准递归、优化递归和迭代版本
   - 使用了C++的特性，如结构体、引用传递等
   - 添加了详细的注释说明算法思路和各种情况的处理

5. 可扩展性：
   - 代码结构清晰，易于扩展到其他类似问题
   - 可以轻松修改为处理N叉树的情况

6. 调试技巧：
   - 可以在递归函数中添加打印语句输出当前节点的值和搜索状态
   - 使用调试器设置断点进行调试
   - 对于复杂树结构，可以添加可视化辅助函数

7. C++特有优化：
   - 使用引用传递减少拷贝开销
   - 使用自定义结构体返回多个值
   - 使用unordered_map和unordered_set进行高效的查找操作

8. 算法安全与业务适配：
   - 对于大型树结构，迭代版本更适合C++环境，避免递归深度限制
   - 代码中添加了适当的边界检查，确保程序不会崩溃
   - 内存管理得当，避免内存泄漏

9. 跨语言实现对比：
   - C++版本相比Java和Python版本更加注重内存管理
   - C++的引用传递比Java的值传递效率更高
   - C++的结构体比Python的元组更灵活
*/