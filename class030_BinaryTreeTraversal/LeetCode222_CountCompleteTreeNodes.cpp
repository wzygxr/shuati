// LeetCode 222. 完全二叉树的节点个数
// 题目链接: https://leetcode.cn/problems/count-complete-tree-nodes/
// 题目大意: 给你一棵完全二叉树的根节点 root ，求出该树的节点个数。
// 完全二叉树的定义：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，
// 并且最下面一层的节点都集中在该层最左边的若干位置。

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
    /**
     * 方法1: 普通递归方法
     * 思路: 直接递归计算左子树和右子树的节点数，然后加1
     * 时间复杂度: O(n) - n是节点数量
     * 空间复杂度: O(h) - h是树的高度，递归调用栈的深度
     */
    int countNodes1(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        return 1 + countNodes1(root->left) + countNodes1(root->right);
    }
    
    /**
     * 方法2: 利用完全二叉树性质优化的方法
     * 思路: 
     * 1. 计算树的高度
     * 2. 利用完全二叉树的性质，如果左右子树高度相等，说明左子树是满二叉树
     * 3. 如果左右子树高度不等，说明右子树是满二叉树
     * 4. 满二叉树的节点数可以直接计算，不需要遍历
     * 时间复杂度: O(log²n) - 每次递归减少一半节点，每次计算高度需要O(logn)
     * 空间复杂度: O(logn) - 递归调用栈的深度
     */
    int countNodes2(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        // 计算左子树的高度
        int leftHeight = getHeight(root->left);
        // 计算右子树的高度
        int rightHeight = getHeight(root->right);
        
        if (leftHeight == rightHeight) {
            // 左右子树高度相等，说明左子树是满二叉树
            // 左子树节点数 = 2^leftHeight - 1
            // 总节点数 = 左子树节点数 + 根节点 + 右子树节点数
            return (1 << leftHeight) + countNodes2(root->right);
        } else {
            // 左右子树高度不等，说明右子树是满二叉树
            // 右子树节点数 = 2^rightHeight - 1
            // 总节点数 = 右子树节点数 + 根节点 + 左子树节点数
            return (1 << rightHeight) + countNodes2(root->left);
        }
    }
    
private:
    /**
     * 计算树的高度
     * @param root 树的根节点
     * @return 树的高度
     */
    int getHeight(TreeNode* root) {
        int height = 0;
        while (root != nullptr) {
            height++;
            root = root->left;
        }
        return height;
    }
    
public:
    /**
     * 方法3: 二分查找法
     * 思路: 
     * 1. 计算完全二叉树的高度
     * 2. 最后一层的节点数在1到2^h之间
     * 3. 使用二分查找确定最后一层有多少个节点
     * 时间复杂度: O(log²n) - 二分查找需要O(logn)，每次检查需要O(logn)
     * 空间复杂度: O(1) - 只使用常数额外空间
     */
    int countNodes3(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        // 计算树的高度
        int height = getHeight(root);
        
        // 如果只有根节点
        if (height == 1) {
            return 1;
        }
        
        // 计算除最后一层外的节点数
        int upperCount = (1 << (height - 1)) - 1;
        
        // 二分查找最后一层的节点数
        int left = 1, right = 1 << (height - 1);
        int lastLevelCount = 0;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nodeExists(root, mid, height)) {
                lastLevelCount = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return upperCount + lastLevelCount;
    }
    
private:
    /**
     * 检查最后一层第index个节点是否存在
     * @param root 树的根节点
     * @param index 节点在最后一层的索引(从1开始)
     * @param height 树的高度
     * @return 节点是否存在
     */
    bool nodeExists(TreeNode* root, int index, int height) {
        int left = 1, right = 1 << (height - 1);
        
        for (int i = 0; i < height - 1; i++) {
            int mid = left + (right - left) / 2;
            if (index <= mid) {
                root = root->left;
                right = mid;
            } else {
                root = root->right;
                left = mid + 1;
            }
        }
        
        return root != nullptr;
    }
};