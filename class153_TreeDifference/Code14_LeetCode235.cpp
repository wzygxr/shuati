/**
 * LeetCode 235. 二叉搜索树的最近公共祖先（LCA）
 * 题目链接：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
 * 题目描述：给定一个二叉搜索树，找到两个指定节点的最近公共祖先
 * 解法：利用二叉搜索树的性质
 * 
 * 算法思路：
 * 1. 利用二叉搜索树的性质：左子树所有节点值 < 根节点值 < 右子树所有节点值
 * 2. 如果p和q的值都小于当前节点值，则LCA在左子树
 * 3. 如果p和q的值都大于当前节点值，则LCA在右子树
 * 4. 否则当前节点就是LCA
 * 
 * 时间复杂度：O(h)，h为树的高度
 * 空间复杂度：O(1)
 */

#include <iostream>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

class Solution {
public:
    /**
     * 二叉搜索树的最近公共祖先（迭代版本）
     * @param root 根节点
     * @param p 节点p
     * @param q 节点q
     * @return 最近公共祖先
     */
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        TreeNode* current = root;
        
        while (current != nullptr) {
            // 如果p和q的值都小于当前节点值，LCA在左子树
            if (p->val < current->val && q->val < current->val) {
                current = current->left;
            } 
            // 如果p和q的值都大于当前节点值，LCA在右子树
            else if (p->val > current->val && q->val > current->val) {
                current = current->right;
            } 
            // 否则当前节点就是LCA
            else {
                return current;
            }
        }
        
        return nullptr; // 理论上不会执行到这里
    }
    
    /**
     * 二叉搜索树的最近公共祖先（递归版本）
     * @param root 根节点
     * @param p 节点p
     * @param q 节点q
     * @return 最近公共祖先
     */
    TreeNode* lowestCommonAncestorRecursive(TreeNode* root, TreeNode* p, TreeNode* q) {
        // 如果p和q的值都小于当前节点值，LCA在左子树
        if (p->val < root->val && q->val < root->val) {
            return lowestCommonAncestorRecursive(root->left, p, q);
        }
        
        // 如果p和q的值都大于当前节点值，LCA在右子树
        if (p->val > root->val && q->val > root->val) {
            return lowestCommonAncestorRecursive(root->right, p, q);
        }
        
        // 否则当前节点就是LCA
        return root;
    }
};

/**
 * 测试用例
 */
int main() {
    // 构建测试二叉搜索树
    //       6
    //      / \
    //     2   8
    //    / \ / \
    //   0  4 7  9
    //     / \
    //    3   5
    
    TreeNode* root = new TreeNode(6);
    root->left = new TreeNode(2);
    root->right = new TreeNode(8);
    root->left->left = new TreeNode(0);
    root->left->right = new TreeNode(4);
    root->right->left = new TreeNode(7);
    root->right->right = new TreeNode(9);
    root->left->right->left = new TreeNode(3);
    root->left->right->right = new TreeNode(5);
    
    Solution solution;
    
    // 测试用例1：节点2和8的LCA
    TreeNode* p1 = root->left;  // 节点2
    TreeNode* q1 = root->right;  // 节点8
    TreeNode* lca1 = solution.lowestCommonAncestor(root, p1, q1);
    cout << "节点2和8的LCA: " << lca1->val << endl; // 期望: 6
    
    // 测试用例2：节点2和4的LCA
    TreeNode* p2 = root->left;      // 节点2
    TreeNode* q2 = root->left->right; // 节点4
    TreeNode* lca2 = solution.lowestCommonAncestor(root, p2, q2);
    cout << "节点2和4的LCA: " << lca2->val << endl; // 期望: 2
    
    // 测试用例3：节点3和5的LCA
    TreeNode* p3 = root->left->right->left;  // 节点3
    TreeNode* q3 = root->left->right->right;  // 节点5
    TreeNode* lca3 = solution.lowestCommonAncestor(root, p3, q3);
    cout << "节点3和5的LCA: " << lca3->val << endl; // 期望: 4
    
    // 测试用例4：节点0和5的LCA
    TreeNode* p4 = root->left->left;        // 节点0
    TreeNode* q4 = root->left->right->right; // 节点5
    TreeNode* lca4 = solution.lowestCommonAncestor(root, p4, q4);
    cout << "节点0和5的LCA: " << lca4->val << endl; // 期望: 2
    
    // 清理内存
    delete root->left->right->right;
    delete root->left->right->left;
    delete root->right->right;
    delete root->right->left;
    delete root->left->right;
    delete root->left->left;
    delete root->right;
    delete root->left;
    delete root;
    
    return 0;
}