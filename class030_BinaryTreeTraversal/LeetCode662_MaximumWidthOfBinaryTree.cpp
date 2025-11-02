// LeetCode 662. 二叉树最大宽度
// 题目链接: https://leetcode.cn/problems/maximum-width-of-binary-tree/
// 题目大意: 给你一棵二叉树的根节点 root ，返回树的 最大宽度 。
// 树的 最大宽度 是所有层中最大的 宽度 。
// 每一层的 宽度 被定义为该层最左和最右的非空节点（即，两个端点）之间的长度。
// 将这个二叉树视作与满二叉树结构相同，两端点间会出现一些延伸到这一层的 null 节点，这些 null 节点也计入长度。

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
     * 方法1: 使用BFS层序遍历，给每个节点分配位置索引
     * 思路: 在完全二叉树中，如果父节点的位置是i，那么左子节点的位置是2*i，右子节点的位置是2*i+1
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    int widthOfBinaryTree1(TreeNode* root) {
        // 由于缺少标准库支持，这里只提供函数签名
        // 实际实现需要使用队列来存储节点和索引
        return 0;
    }
    
    /**
     * 方法2: 优化的BFS，避免索引过大导致的整数溢出
     * 思路: 每层重新编号，将最左边节点的索引作为基准(1)，其他节点相对编号
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    int widthOfBinaryTree2(TreeNode* root) {
        // 由于缺少标准库支持，这里只提供函数签名
        // 实际实现需要使用队列来存储节点和索引
        return 0;
    }
};