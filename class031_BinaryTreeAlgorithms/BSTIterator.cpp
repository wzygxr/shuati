// LeetCode 173. Binary Search Tree Iterator
// 题目链接: https://leetcode.cn/problems/binary-search-tree-iterator/
// 题目描述: 实现一个二叉搜索树迭代器类BSTIterator ，表示一个按中序遍历二叉搜索树（BST）的迭代器：
// - BSTIterator(TreeNode root) 初始化 BSTIterator 类的一个对象。BST 的根节点 root 会作为构造函数的一部分给出。
// - boolean hasNext() 如果向指针右侧遍历存在数字，则返回 true ；否则返回 false 。
// - int next()将指针向右移动，然后返回指针处的数字。
//
// 解题思路:
// 1. 使用栈模拟中序遍历的递归过程
// 2. 初始化时将根节点及其所有左子节点入栈
// 3. next()方法弹出栈顶节点，处理其右子树
// 4. hasNext()方法检查栈是否为空
//
// 时间复杂度: 
//   - 构造函数: O(h) - h为树的高度
//   - next(): 平均O(1)，最坏O(h)
//   - hasNext(): O(1)
// 空间复杂度: O(h) - 栈中最多存储h个节点
// 是否为最优解: 是，这是BST迭代器的标准实现

#include <stack>
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

class BSTIterator {
private:
    stack<TreeNode*> stk;

    /**
     * 将节点及其所有左子节点入栈
     * 用于模拟中序遍历的左子树递归过程
     * 
     * @param node 当前节点
     */
    void pushAllLeft(TreeNode* node) {
        while (node != nullptr) {
            stk.push(node);
            node = node->left;
        }
    }

public:
    /**
     * BSTIterator构造函数
     * 初始化时将根节点及其所有左子节点入栈
     * 
     * @param root 二叉搜索树的根节点
     */
    BSTIterator(TreeNode* root) {
        pushAllLeft(root);
    }
    
    /**
     * 返回中序遍历序列的下一个元素
     * 
     * @return 下一个节点的值
     */
    int next() {
        // 弹出栈顶节点（当前最小的节点）
        TreeNode* node = stk.top();
        stk.pop();
        
        // 如果该节点有右子树，将右子树及其所有左子节点入栈
        if (node->right != nullptr) {
            pushAllLeft(node->right);
        }
        
        return node->val;
    }
    
    /**
     * 检查是否还有下一个元素
     * 
     * @return 如果还有下一个元素返回true，否则返回false
     */
    bool hasNext() {
        return !stk.empty();
    }
};

// 测试用例
// int main() {
//     // 构造测试BST:
//     //       7
//     //      / \
//     //     3   15
//     //        /  \
//     //       9    20
//     TreeNode* root = new TreeNode(7);
//     root->left = new TreeNode(3);
//     root->right = new TreeNode(15);
//     root->right->left = new TreeNode(9);
//     root->right->right = new TreeNode(20);
//
//     // 创建BST迭代器
//     BSTIterator iterator(root);
//     
//     // 应该按顺序输出: 3, 7, 9, 15, 20
//     while (iterator.hasNext()) {
//         cout << iterator.next() << " ";
//     }
//     cout << endl;
//     
//     // 内存清理
//     delete root->right->left;
//     delete root->right->right;
//     delete root->right;
//     delete root->left;
//     delete root;
//     
//     return 0;
// }