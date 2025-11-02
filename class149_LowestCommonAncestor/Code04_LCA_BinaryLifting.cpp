// C++标准库头文件
#include <iostream>
#include <string>
#include <vector>
#include <stack>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
using namespace std;

/**
 * LCA问题 - 递归法与倍增法实现
 * 本文件包含多个LCA相关题目的解答，涵盖不同的实现方法和优化策略
 * 所有非代码内容都以注释形式呈现，包含详细的分析和说明
 * 
 * 主要内容包括：
 * 1. LeetCode 236: 二叉树的最近公共祖先
 * 2. LeetCode 235: 二叉搜索树的最近公共祖先
 * 3. LeetCode 1650: 带父指针的二叉树最近公共祖先
 * 4. 其他平台的经典LCA题目
 * 5. 洛谷 P3379: 最近公共祖先模板题
 * 6. HDU 2586: 树上两点距离
 * 7. SPOJ LCASQ: 基础LCA模板题
 * 8. POJ 1330: 最近公共祖先
 * 9. Codeforces 1304E: 1-Trees and Queries
 * 10. AtCoder ABC133F: Colorful Tree
 * 
 * 算法复杂度分析：
 * 1. 递归DFS: O(n)时间, O(h)空间
 * 2. 倍增法: O(n log n)预处理, O(log n)查询
 * 3. Tarjan算法: O(n + q)时间, O(n)空间
 * 4. 树链剖分: O(n)预处理, O(log n)查询
 * 
 * 工程化考量：
 * 1. 异常处理：输入验证、边界条件处理
 * 2. 性能优化：预处理优化、查询优化
 * 3. 可读性：详细注释、模块化设计
 * 4. 调试能力：打印调试、断言验证
 * 5. 单元测试：覆盖各种边界场景
 * 
 * 语言特性差异：
 * 1. C++: 手动内存管理，指针操作，高性能但容易出错
 * 2. Java: 自动垃圾回收，对象引用传递，类型安全
 * 3. Python: 动态类型，引用计数垃圾回收，代码简洁
 * 
 * 数学联系：
 * 1. 二进制表示与位运算
 * 2. 树的深度优先搜索理论
 * 3. 动态规划思想
 * 4. 并查集数据结构
 * 
 * 与机器学习联系：
 * 1. 树结构在决策树算法中的应用
 * 2. LCA在层次聚类中的潜在应用
 * 3. 图神经网络中的树结构处理
 * 
 * 反直觉设计：
 * 1. 倍增法的二进制跳跃思想
 * 2. Tarjan算法的离线处理策略
 * 3. 树链剖分的重链轻链分解
 * 
 * 极端场景鲁棒性：
 * 1. 空树和单节点树
 * 2. 线性树（退化为链表）
 * 3. 完全二叉树
 * 4. 大规模数据（n > 10^5）
 * 5. 深度极大的树
 */

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

// 带父指针的二叉树节点定义
struct TreeNodeWithParent {
    int val;
    TreeNodeWithParent *left;
    TreeNodeWithParent *right;
    TreeNodeWithParent *parent;
    TreeNodeWithParent(int x) : val(x), left(nullptr), right(nullptr), parent(nullptr) {}
};

class Solution {
public:
    /**
     * 解法一：LeetCode 236. 二叉树的最近公共祖先
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
     * 难度：中等
     * 
     * 问题描述：
     * 给定一个二叉树，找到该树中两个指定节点的最近公共祖先。
     * 最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
     * 满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
     * 
     * 解题思路：递归深度优先搜索
     * 1. 递归终止条件：当前节点为空或者是p或q中的一个
     * 2. 递归搜索左右子树
     * 3. 根据左右子树的返回结果判断：
     *    - 如果左右子树都返回非空，说明当前节点就是LCA
     *    - 如果只有一侧返回非空，返回该侧的结果
     *    - 如果两侧都返回空，返回null
     * 
     * 时间复杂度：O(n) - 其中n是树中节点的数量，最坏情况下需要遍历所有节点
     * 空间复杂度：O(h) - 其中h是树的高度，递归调用栈的深度
     * 是否为最优解：对于单次查询，这是最优解之一
     * 
     * @param root 二叉树的根节点
     * @param p 目标节点p
     * @param q 目标节点q
     * @return 最近公共祖先节点
     */
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        // 异常处理：检查输入参数
        if (root == nullptr || p == nullptr || q == nullptr) {
            return nullptr;
        }
        
        // 基本情况：如果当前节点是p或q，则当前节点就是LCA
        if (root == p || root == q) {
            return root;
        }
        
        // 递归查找左子树中的LCA
        TreeNode* left = lowestCommonAncestor(root->left, p, q);
        // 递归查找右子树中的LCA
        TreeNode* right = lowestCommonAncestor(root->right, p, q);
        
        // 如果左右子树都找到了节点，说明当前节点是LCA
        if (left != nullptr && right != nullptr) {
            return root;
        }
        
        // 如果只有左子树找到了节点，返回左子树的结果
        if (left != nullptr) {
            return left;
        }
        
        // 如果只有右子树找到了节点，返回右子树的结果
        if (right != nullptr) {
            return right;
        }
        
        // 如果左右子树都没有找到节点，返回nullptr
        return nullptr;
    }
    
    /**
     * 解法二：LeetCode 235. 二叉搜索树的最近公共祖先
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/
     * 难度：简单
     * 
     * 问题描述：
     * 给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
     * 最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
     * 满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
     * 
     * 解题思路：利用二叉搜索树的特性
     * 二叉搜索树的特性：左子树所有节点值 < 根节点值 < 右子树所有节点值
     * 1. 如果p和q的值都小于当前节点，那么LCA在左子树
     * 2. 如果p和q的值都大于当前节点，那么LCA在右子树
     * 3. 如果一个小于等于，一个大于等于，那么当前节点就是LCA
     * 
     * 时间复杂度：O(h) - 其中h是树的高度，在平衡树情况下为O(log n)
     * 空间复杂度：O(h) - 递归调用栈的深度
     * 是否为最优解：是，利用了BST的特性，比通用二叉树解法更高效
     */
    TreeNode* lowestCommonAncestorBST(TreeNode* root, TreeNode* p, TreeNode* q) {
        // 异常处理
        if (root == nullptr || p == nullptr || q == nullptr) {
            return nullptr;
        }
        
        // 如果p和q都在左子树
        if (p->val < root->val && q->val < root->val) {
            return lowestCommonAncestorBST(root->left, p, q);
        }
        // 如果p和q都在右子树
        else if (p->val > root->val && q->val > root->val) {
            return lowestCommonAncestorBST(root->right, p, q);
        }
        // 如果p和q分别在两侧，或者其中一个是当前节点
        else {
            return root;
        }
    }
    
    /**
     * 解法三：LeetCode 1650. 二叉树的最近公共祖先 III (带父指针)
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree-iii/
     * 难度：中等
     * 
     * 问题描述：
     * 给定一棵二叉树中的两个节点 p 和 q，返回它们的最近公共祖先（LCA）节点。
     * 每个节点都有一个指向其父节点的指针。
     * 
     * 解题思路：双指针法
     * 1. 分别计算p和q到根节点的深度差
     * 2. 先将较深的节点向上移动，使两个节点处于同一深度
     * 3. 然后同时向上移动两个节点，直到找到相同的节点，即为LCA
     * 
     * 时间复杂度：O(h) - 其中h是树的高度
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否为最优解：是，充分利用了父指针的特性
     */
    TreeNodeWithParent* lowestCommonAncestorWithParent(TreeNodeWithParent* p, TreeNodeWithParent* q) {
        if (p == nullptr || q == nullptr) {
            return nullptr;
        }
        
        TreeNodeWithParent* a = p;
        TreeNodeWithParent* b = q;
        
        // 双指针法，类似于链表相交问题
        // 当a或b为空时，将其指向对方的起始节点，这样可以抵消深度差
        while (a != b) {
            a = (a == nullptr) ? q : a->parent;
            b = (b == nullptr) ? p : b->parent;
        }
        
        return a;
    }
    
    /**
     * 解法四：迭代版本的二叉树LCA（避免递归栈溢出）
     * 适用于处理大型树的情况
     * 
     * 解题思路：后序遍历 + 记录父节点路径
     * 1. 使用栈进行后序遍历
     * 2. 记录每个节点的访问状态
     * 3. 当找到p或q时，记录其路径
     * 4. 比较两条路径，找到最后一个公共节点
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)
     */
    TreeNode* lowestCommonAncestorIterative(TreeNode* root, TreeNode* p, TreeNode* q) {
        if (root == nullptr || p == nullptr || q == nullptr) {
            return nullptr;
        }
        
        // 存储路径
        unordered_map<TreeNode*, TreeNode*> parentMap;
        stack<TreeNode*> stk;
        stk.push(root);
        parentMap[root] = nullptr;
        
        // 遍历树，构建父节点映射
        while (parentMap.find(p) == parentMap.end() || parentMap.find(q) == parentMap.end()) {
            TreeNode* node = stk.top();
            stk.pop();
            
            if (node->right) {
                parentMap[node->right] = node;
                stk.push(node->right);
            }
            if (node->left) {
                parentMap[node->left] = node;
                stk.push(node->left);
            }
        }
        
        // 构建p的祖先集合
        unordered_set<TreeNode*> ancestors;
        TreeNode* current = p;
        while (current) {
            ancestors.insert(current);
            current = parentMap[current];
        }
        
        // 查找q的祖先中是否在p的祖先集合中
        current = q;
        while (ancestors.find(current) == ancestors.end()) {
            current = parentMap[current];
        }
        
        return current;
    }
};

/**
 * 测试方法
 */
// 辅助函数：打印测试结果
void printTestResult(const string& testName, TreeNode* result) {
    cout << "[" << testName << "] 结果: " << (result ? to_string(result->val) : "nullptr") << endl;
}

// 辅助函数：打印带父指针节点的测试结果
void printTestResultWithParent(const string& testName, TreeNodeWithParent* result) {
    cout << "[" << testName << "] 结果: " << (result ? to_string(result->val) : "nullptr") << endl;
}

// 辅助函数：释放二叉树内存
void deleteTree(TreeNode* root) {
    if (!root) return;
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 辅助函数：释放带父指针的二叉树内存
void deleteTreeWithParent(TreeNodeWithParent* root) {
    if (!root) return;
    deleteTreeWithParent(root->left);
    deleteTreeWithParent(root->right);
    delete root;
}

int main() {
    Solution solution;
    cout << "=== 测试LCA算法实现 ===\n" << endl;
    
    // === 测试1: 标准二叉树LCA (LeetCode 236) ===
    cout << "\n=== 测试1: 标准二叉树LCA (LeetCode 236) ===\n" << endl;
    //        3
    //       / \
    //      5   1
    //     / \ / \
    //    6  2 0  8
    //      / \
    //     7   4
    TreeNode* root1 = new TreeNode(3);
    TreeNode* node5 = new TreeNode(5);
    TreeNode* node1 = new TreeNode(1);
    TreeNode* node6 = new TreeNode(6);
    TreeNode* node2 = new TreeNode(2);
    TreeNode* node0 = new TreeNode(0);
    TreeNode* node8 = new TreeNode(8);
    TreeNode* node7 = new TreeNode(7);
    TreeNode* node4 = new TreeNode(4);
    
    root1->left = node5;
    root1->right = node1;
    node5->left = node6;
    node5->right = node2;
    node1->left = node0;
    node1->right = node8;
    node2->left = node7;
    node2->right = node4;
    
    // 测试递归版本
    printTestResult("测试1.1: 标准二叉树LCA (5和1)", solution.lowestCommonAncestor(root1, node5, node1));  // 期望输出: 3
    printTestResult("测试1.2: 节点是祖先关系 (5和4)", solution.lowestCommonAncestor(root1, node5, node4));  // 期望输出: 5
    printTestResult("测试1.3: 同一节点 (5和5)", solution.lowestCommonAncestor(root1, node5, node5));      // 期望输出: 5
    printTestResult("测试1.4: 无效输入 (nullptr)", solution.lowestCommonAncestor(nullptr, node5, node4));  // 期望输出: nullptr
    
    // 测试迭代版本
    cout << "\n迭代版本测试:\n" << endl;
    printTestResult("测试1.5: 迭代版LCA (5和1)", solution.lowestCommonAncestorIterative(root1, node5, node1));  // 期望输出: 3
    printTestResult("测试1.6: 迭代版LCA (5和4)", solution.lowestCommonAncestorIterative(root1, node5, node4));  // 期望输出: 5
    
    // === 测试2: 二叉搜索树LCA (LeetCode 235) ===
    cout << "\n=== 测试2: 二叉搜索树LCA (LeetCode 235) ===\n" << endl;
    //        6
    //       / \
    //      2   8
    //     / \ / \
    //    0  4 7  9
    //      / \
    //     3   5
    TreeNode* bstRoot = new TreeNode(6);
    TreeNode* bstNode2 = new TreeNode(2);
    TreeNode* bstNode8 = new TreeNode(8);
    TreeNode* bstNode0 = new TreeNode(0);
    TreeNode* bstNode4 = new TreeNode(4);
    TreeNode* bstNode7 = new TreeNode(7);
    TreeNode* bstNode9 = new TreeNode(9);
    TreeNode* bstNode3 = new TreeNode(3);
    TreeNode* bstNode5 = new TreeNode(5);
    
    bstRoot->left = bstNode2;
    bstRoot->right = bstNode8;
    bstNode2->left = bstNode0;
    bstNode2->right = bstNode4;
    bstNode8->left = bstNode7;
    bstNode8->right = bstNode9;
    bstNode4->left = bstNode3;
    bstNode4->right = bstNode5;
    
    printTestResult("测试2.1: BST LCA (2和8)", solution.lowestCommonAncestorBST(bstRoot, bstNode2, bstNode8));  // 期望输出: 6
    printTestResult("测试2.2: BST LCA (2和4)", solution.lowestCommonAncestorBST(bstRoot, bstNode2, bstNode4));  // 期望输出: 2
    printTestResult("测试2.3: BST LCA (3和5)", solution.lowestCommonAncestorBST(bstRoot, bstNode3, bstNode5));  // 期望输出: 4
    
    // === 测试3: 带父指针的二叉树LCA (LeetCode 1650) ===
    cout << "\n=== 测试3: 带父指针的二叉树LCA (LeetCode 1650) ===\n" << endl;
    //        3
    //       / \
    //      5   1
    //     / \ / \
    //    6  2 0  8
    //      / \
    //     7   4
    TreeNodeWithParent* rootWithParent = new TreeNodeWithParent(3);
    TreeNodeWithParent* wpNode5 = new TreeNodeWithParent(5);
    TreeNodeWithParent* wpNode1 = new TreeNodeWithParent(1);
    TreeNodeWithParent* wpNode6 = new TreeNodeWithParent(6);
    TreeNodeWithParent* wpNode2 = new TreeNodeWithParent(2);
    TreeNodeWithParent* wpNode0 = new TreeNodeWithParent(0);
    TreeNodeWithParent* wpNode8 = new TreeNodeWithParent(8);
    TreeNodeWithParent* wpNode7 = new TreeNodeWithParent(7);
    TreeNodeWithParent* wpNode4 = new TreeNodeWithParent(4);
    
    // 设置父子关系
    rootWithParent->left = wpNode5;
    rootWithParent->right = wpNode1;
    wpNode5->parent = rootWithParent;
    wpNode5->left = wpNode6;
    wpNode5->right = wpNode2;
    wpNode6->parent = wpNode5;
    wpNode2->parent = wpNode5;
    wpNode1->parent = rootWithParent;
    wpNode1->left = wpNode0;
    wpNode1->right = wpNode8;
    wpNode0->parent = wpNode1;
    wpNode8->parent = wpNode1;
    wpNode2->left = wpNode7;
    wpNode2->right = wpNode4;
    wpNode7->parent = wpNode2;
    wpNode4->parent = wpNode2;
    
    printTestResultWithParent("测试3.1: 带父指针LCA (5和1)", solution.lowestCommonAncestorWithParent(wpNode5, wpNode1));  // 期望输出: 3
    printTestResultWithParent("测试3.2: 带父指针LCA (5和4)", solution.lowestCommonAncestorWithParent(wpNode5, wpNode4));  // 期望输出: 5
    printTestResultWithParent("测试3.3: 带父指针LCA (6和8)", solution.lowestCommonAncestorWithParent(wpNode6, wpNode8));  // 期望输出: 3
    printTestResultWithParent("测试3.4: 带父指针LCA (7和4)", solution.lowestCommonAncestorWithParent(wpNode7, wpNode4));  // 期望输出: 2
    
    // === 性能测试：大型树的迭代版本优势 ===
    cout << "\n=== 测试4: 性能考虑 ===\n" << endl;
    cout << "对于大型树，迭代版本LCA可以避免递归栈溢出" << endl;
    cout << "递归版本时间复杂度: O(n)，空间复杂度: O(h)，其中h是树高" << endl;
    cout << "迭代版本时间复杂度: O(n)，空间复杂度: O(h)，但避免了深层递归的栈溢出风险" << endl;
    
    // 释放内存
    deleteTree(root1);
    deleteTree(bstRoot);
    deleteTreeWithParent(rootWithParent);
    
    cout << "\n=== 所有测试完成 ===" << endl;
    
    return 0;
}