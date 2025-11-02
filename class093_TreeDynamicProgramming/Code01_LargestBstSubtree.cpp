// 最大BST子树 (Largest BST Subtree)
// 题目描述:
// 给你一个二叉树的根节点 root，返回任意二叉搜索子树的最大键值数
// 二叉搜索树的定义如下：
// 任意节点的左子树中的键值都 小于 此节点的键值
// 任意节点的右子树中的键值都 大于 此节点的键值
// 任意节点的左子树和右子树都是二叉搜索树
// 测试链接 : https://leetcode.cn/problems/largest-bst-subtree/

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

// 用于存储递归过程中的信息
struct Info {
    long maxVal;        // 以当前节点为根的子树中的最大值
    long minVal;        // 以当前节点为根的子树中的最小值
    bool isBst;         // 该子树是否为BST
    int maxBstSize;     // 以该节点为根的子树中BST的最大节点数
    
    // 默认构造函数
    Info() : maxVal(0), minVal(0), isBst(false), maxBstSize(0) {}
    
    Info(long max_val, long min_val, bool is_bst, int max_bst_size)
        : maxVal(max_val), minVal(min_val), isBst(is_bst), maxBstSize(max_bst_size) {}
};

class Solution {
public:
    // 主函数：计算最大BST子树的节点数
    int largestBSTSubtree(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        Info result = dfs(root);
        return result.maxBstSize;
    }

private:
    // 深度优先搜索，递归处理每个节点
    Info dfs(TreeNode* node) {
        // 基本情况：空节点
        if (node == nullptr) {
            // 空树也是BST，节点数为0
            // 最大值设为LONG_MIN，最小值设为LONG_MAX
            // 这样在比较时不会影响父节点的判断
            return Info(-9223372036854775807L-1, 9223372036854775807L, true, 0);
        }
        
        // 递归处理左右子树
        Info leftInfo = dfs(node->left);
        Info rightInfo = dfs(node->right);
        
        // 计算当前子树的信息
        // 当前子树的最大值 = max(当前节点值, 左子树最大值, 右子树最大值)
        long currentMax = custom_max((long)node->val, custom_max(leftInfo.maxVal, rightInfo.maxVal));
        // 当前子树的最小值 = min(当前节点值, 左子树最小值, 右子树最小值)
        long currentMin = custom_min((long)node->val, custom_min(leftInfo.minVal, rightInfo.minVal));
        
        // 判断当前子树是否为BST
        // 条件：左右子树都是BST，且左子树最大值 < 当前节点值 < 右子树最小值
        bool isCurrentBst = leftInfo.isBst && rightInfo.isBst && 
                           leftInfo.maxVal < node->val && node->val < rightInfo.minVal;
        
        // 计算当前子树中BST的最大节点数
        int currentMaxBstSize = custom_max(leftInfo.maxBstSize, rightInfo.maxBstSize);
        if (isCurrentBst) {
            // 如果当前子树是BST，则更新最大节点数
            currentMaxBstSize = custom_max(currentMaxBstSize, 
                                          (node->left ? leftInfo.maxBstSize : 0) + 
                                          (node->right ? rightInfo.maxBstSize : 0) + 1);
        }
        
        // 返回当前节点的信息
        return Info(currentMax, currentMin, isCurrentBst, currentMaxBstSize);
    }
    
    // 自定义max和min函数，避免使用标准库
    long custom_max(long a, long b) {
        return (a > b) ? a : b;
    }
    
    long custom_min(long a, long b) {
        return (a < b) ? a : b;
    }
    
    int custom_max(int a, int b) {
        return (a > b) ? a : b;
    }
    
    int custom_min(int a, int b) {
        return (a < b) ? a : b;
    }
};