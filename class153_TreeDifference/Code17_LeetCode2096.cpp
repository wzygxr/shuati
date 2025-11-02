#include <iostream>
#include <string>
#include <algorithm>
using namespace std;

/**
 * LeetCode 2096. 从二叉树一个节点到另一个节点的方向
 * 题目描述：给定一棵二叉树，找到从起点节点到目标节点的路径方向。
 * 使用LCA和路径重建解决。
 */

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

class Solution {
public:
    string getDirections(TreeNode* root, int startValue, int destValue) {
        // 找到起点和目标节点的LCA
        TreeNode* lca = findLCA(root, startValue, destValue);
        
        // 从起点到LCA的路径（全部是U）
        string startToLCA;
        findPath(lca, startValue, startToLCA);
        
        // 从LCA到目标的路径
        string lcaToDest;
        findPath(lca, destValue, lcaToDest);
        
        // 起点到LCA的路径全部替换为U
        string upPath(startToLCA.size(), 'U');
        
        return upPath + lcaToDest;
    }
    
private:
    TreeNode* findLCA(TreeNode* root, int p, int q) {
        if (root == nullptr || root->val == p || root->val == q) {
            return root;
        }
        
        TreeNode* left = findLCA(root->left, p, q);
        TreeNode* right = findLCA(root->right, p, q);
        
        if (left != nullptr && right != nullptr) {
            return root;
        }
        
        return left != nullptr ? left : right;
    }
    
    bool findPath(TreeNode* node, int target, string& path) {
        if (node == nullptr) return false;
        
        if (node->val == target) return true;
        
        // 尝试左子树
        path.push_back('L');
        if (findPath(node->left, target, path)) {
            return true;
        }
        path.pop_back();
        
        // 尝试右子树
        path.push_back('R');
        if (findPath(node->right, target, path)) {
            return true;
        }
        path.pop_back();
        
        return false;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    TreeNode* root1 = new TreeNode(5);
    root1->left = new TreeNode(1);
    root1->right = new TreeNode(2);
    root1->left->left = new TreeNode(3);
    root1->right->left = new TreeNode(6);
    root1->right->right = new TreeNode(4);
    
    cout << solution.getDirections(root1, 3, 6) << endl; // 输出: "UURL"
    
    // 测试用例2
    TreeNode* root2 = new TreeNode(2);
    root2->left = new TreeNode(1);
    
    cout << solution.getDirections(root2, 2, 1) << endl; // 输出: "L"
    
    return 0;
}