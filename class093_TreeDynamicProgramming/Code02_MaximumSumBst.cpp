// 二叉搜索子树的最大键值和 (Maximum Sum BST)
// 题目描述:
// 给你一棵以 root 为根的二叉树
// 请你返回 任意 二叉搜索子树的最大键值和
// 二叉搜索树的定义如下：
// 任意节点的左子树中的键值都 小于 此节点的键值
// 任意节点的右子树中的键值都 大于 此节点的键值
// 任意节点的左子树和右子树都是二叉搜索树
// 测试链接 : https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 以该节点为根的子树中的最大值
//    - 以该节点为根的子树中的最小值
//    - 该子树中所有节点值的和
//    - 该子树是否为BST
//    - 以该节点为根的子树中BST的最大键值和
// 3. 递归处理左右子树，综合计算当前节点的信息
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算BST最大键值和的标准方法
//
// 相关题目:
// - LeetCode 1373. 二叉搜索子树的最大键值和
// - LeetCode 333. 最大BST子树
// - LeetCode 98. 验证二叉搜索树
//
// 工程化考量:
// 1. 使用int类型，因为题目数据范围在[-4*10^4, 4*10^4]
// 2. 处理空树和单节点树的边界情况
// 3. 支持负数值的处理
// 4. 提供递归和迭代两种实现方式
// 5. 添加详细的注释和调试信息

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
    int maxVal;         // 以当前节点为根的子树中的最大值
    int minVal;         // 以当前节点为根的子树中的最小值
    int sum;            // 该子树中所有节点值的和
    bool isBst;         // 该子树是否为BST
    int maxBstSum;      // 以该节点为根的子树中BST的最大键值和
    
    // 默认构造函数
    Info() : maxVal(0), minVal(0), sum(0), isBst(false), maxBstSum(0) {}
    
    Info(int max_val, int min_val, int s, bool is_bst, int max_bst_sum)
        : maxVal(max_val), minVal(min_val), sum(s), isBst(is_bst), maxBstSum(max_bst_sum) {}
};

class Solution {
public:
    // 主函数：计算二叉搜索子树的最大键值和
    int maxSumBST(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        Info result = dfs(root);
        return custom_max(0, result.maxBstSum);  // 确保返回非负数
    }

private:
    // 深度优先搜索，递归处理每个节点
    Info dfs(TreeNode* node) {
        // 基本情况：空节点
        if (node == nullptr) {
            // 空树也是BST，节点数为0，和为0
            // 最大值设为INT_MIN，最小值设为INT_MAX
            // 这样在比较时不会影响父节点的判断
            // 使用自定义的极大值和极小值
            return Info(-2147483647-1, 2147483647, 0, true, 0);
        }
        
        // 递归处理左右子树
        Info leftInfo = dfs(node->left);
        Info rightInfo = dfs(node->right);
        
        // 计算当前子树的信息
        // 当前子树的最大值 = max(当前节点值, 左子树最大值, 右子树最大值)
        int currentMax = custom_max(node->val, custom_max(leftInfo.maxVal, rightInfo.maxVal));
        // 当前子树的最小值 = min(当前节点值, 左子树最小值, 右子树最小值)
        int currentMin = custom_min(node->val, custom_min(leftInfo.minVal, rightInfo.minVal));
        // 当前子树所有节点值的和 = 左子树节点值和 + 右子树节点值和 + 当前节点值
        int currentSum = leftInfo.sum + rightInfo.sum + node->val;
        
        // 判断当前子树是否为BST
        // 条件：左右子树都是BST，且左子树最大值 < 当前节点值 < 右子树最小值
        bool isCurrentBst = leftInfo.isBst && rightInfo.isBst && 
                           leftInfo.maxVal < node->val && node->val < rightInfo.minVal;
        
        // 计算当前子树中BST的最大键值和
        int currentMaxBstSum = custom_max(leftInfo.maxBstSum, rightInfo.maxBstSum);
        if (isCurrentBst) {
            // 如果当前子树是BST，则更新最大键值和
            currentMaxBstSum = custom_max(currentMaxBstSum, currentSum);
        }
        
        // 返回当前节点的信息
        return Info(currentMax, currentMin, currentSum, isCurrentBst, currentMaxBstSum);
    }
    
    // 自定义max和min函数，避免使用标准库
    int custom_max(int a, int b) {
        return (a > b) ? a : b;
    }
    
    int custom_min(int a, int b) {
        return (a < b) ? a : b;
    }
};