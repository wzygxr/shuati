// LeetCode 101. Symmetric Tree
// 题目链接: https://leetcode.cn/problems/symmetric-tree/
// 题目描述: 给定一个二叉树，检查它是否是镜像对称的。
// 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
//
// 解题思路:
// 1. 递归方法：同时遍历左右子树，检查是否镜像对称
// 2. 迭代方法：使用队列进行层序遍历，检查对称性
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: 
//   - 递归: O(h) - h为树的高度，递归调用栈的深度
//   - 迭代: O(w) - w为树的最大宽度，队列中最多存储一层的节点
// 是否为最优解: 是，这是检查对称二叉树的标准方法

#include <queue>
#include <algorithm>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    // 方法1: 递归解法
    // 核心思想: 同时遍历左右子树，检查左子树的左节点是否等于右子树的右节点，
    //          左子树的右节点是否等于右子树的左节点
    bool isSymmetricRecursive(TreeNode* root) {
        if (root == nullptr) {
            return true;
        }
        return isMirror(root->left, root->right);
    }
    
private:
    bool isMirror(TreeNode* left, TreeNode* right) {
        // 两个节点都为空，对称
        if (left == nullptr && right == nullptr) {
            return true;
        }
        // 一个为空一个不为空，不对称
        if (left == nullptr || right == nullptr) {
            return false;
        }
        // 节点值不相等，不对称
        if (left->val != right->val) {
            return false;
        }
        // 递归检查左子树的左节点和右子树的右节点，
        // 以及左子树的右节点和右子树的左节点
        return isMirror(left->left, right->right) && isMirror(left->right, right->left);
    }

public:
    // 方法2: 迭代解法（BFS）
    // 核心思想: 使用队列进行层序遍历，每次入队两个节点进行比较
    bool isSymmetricIterative(TreeNode* root) {
        if (root == nullptr) {
            return true;
        }
        
        queue<TreeNode*> q;
        q.push(root->left);
        q.push(root->right);
        
        while (!q.empty()) {
            TreeNode* left = q.front();
            q.pop();
            TreeNode* right = q.front();
            q.pop();
            
            // 两个节点都为空，继续检查
            if (left == nullptr && right == nullptr) {
                continue;
            }
            // 一个为空一个不为空，不对称
            if (left == nullptr || right == nullptr) {
                return false;
            }
            // 节点值不相等，不对称
            if (left->val != right->val) {
                return false;
            }
            
            // 按对称顺序入队子节点
            q.push(left->left);
            q.push(right->right);
            q.push(left->right);
            q.push(right->left);
        }
        
        return true;
    }

    // 提交如下的方法（使用递归版本）
    bool isSymmetric(TreeNode* root) {
        return isSymmetricRecursive(root);
    }
};

// 测试用例
int main() {
    Solution solution;

    // 测试用例1: 对称二叉树
    //       1
    //      / \
    //     2   2
    //    / \ / \
    //   3  4 4  3
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(2);
    root1->left->left = new TreeNode(3);
    root1->left->right = new TreeNode(4);
    root1->right->left = new TreeNode(4);
    root1->right->right = new TreeNode(3);
    
    bool result1 = solution.isSymmetric(root1);
    cout << "测试用例1结果: " << (result1 ? "true" : "false") << endl; // 应该输出true

    // 测试用例2: 不对称二叉树
    //       1
    //      / \
    //     2   2
    //      \   \
    //       3   3
    TreeNode* root2 = new TreeNode(1);
    root2->left = new TreeNode(2);
    root2->right = new TreeNode(2);
    root2->left->right = new TreeNode(3);
    root2->right->right = new TreeNode(3);
    
    bool result2 = solution.isSymmetric(root2);
    cout << "测试用例2结果: " << (result2 ? "true" : "false") << endl; // 应该输出false

    // 测试用例3: 空树
    TreeNode* root3 = nullptr;
    bool result3 = solution.isSymmetric(root3);
    cout << "测试用例3结果: " << (result3 ? "true" : "false") << endl; // 应该输出true

    // 测试用例4: 单节点树
    TreeNode* root4 = new TreeNode(1);
    bool result4 = solution.isSymmetric(root4);
    cout << "测试用例4结果: " << (result4 ? "true" : "false") << endl; // 应该输出true

    // 补充题目测试: LeetCode 100 - 相同的树
    cout << "\n=== 补充题目测试: 相同的树 ===" << endl;
    // 构造两棵相同的树
    TreeNode* tree1 = new TreeNode(1);
    tree1->left = new TreeNode(2);
    tree1->right = new TreeNode(3);
    
    TreeNode* tree2 = new TreeNode(1);
    tree2->left = new TreeNode(2);
    tree2->right = new TreeNode(3);
    
    // 相同的树函数实现
    auto isSameTree = [](TreeNode* p, TreeNode* q) -> bool {
        if (p == nullptr && q == nullptr) return true;
        if (p == nullptr || q == nullptr) return false;
        if (p->val != q->val) return false;
        return isSameTree(p->left, q->left) && isSameTree(p->right, q->right);
    };
    
    bool sameResult = isSameTree(tree1, tree2);
    cout << "相同的树测试: " << (sameResult ? "true" : "false") << endl; // 应该输出true

    // 内存清理
    delete root1->left->left;
    delete root1->left->right;
    delete root1->right->left;
    delete root1->right->right;
    delete root1->left;
    delete root1->right;
    delete root1;
    
    delete root2->left->right;
    delete root2->right->right;
    delete root2->left;
    delete root2->right;
    delete root2;
    
    delete root4;
    
    delete tree1->left;
    delete tree1->right;
    delete tree1;
    
    delete tree2->left;
    delete tree2->right;
    delete tree2;

    return 0;
}