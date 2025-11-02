// 968. 二叉树摄像头
// 测试链接 : https://leetcode.cn/problems/binary-tree-cameras/

// Definition for a binary tree node.
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
    // 提交如下的方法
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树摄像头问题的标准方法，使用贪心策略的树形DP
    int minCameraCover(TreeNode* root) {
        // 调用递归函数，返回包含三个状态值的数组
        int result[3];
        dfs(root, result);
        // 返回根节点的最小摄像头数量
        // 三种情况：根节点安装摄像头、根节点被子节点监控、根节点未被监控需要父节点安装摄像头
        // 我们选择前两种情况的最小值（根节点不能要求父节点安装摄像头）
        return result[0] < result[1] ? result[0] : result[1];
    }
    
private:
    // 递归函数计算结果并存储在result数组中，表示三种状态：
    // result[0]: 当前节点安装摄像头时，监控整棵树所需的最小摄像头数量
    // result[1]: 当前节点未安装摄像头但被子节点监控时，监控整棵树所需的最小摄像头数量
    // result[2]: 当前节点未被监控时，监控整棵树所需的最小摄像头数量（需要父节点安装摄像头）
    void dfs(TreeNode* node, int result[3]) {
        // 基础情况：如果节点为空，返回对应的状态值
        if (node == nullptr) {
            // 空节点不需要安装摄像头，也不需要被监控
            result[0] = 1000000000;  // 使用一个大数表示无穷大
            result[1] = 0;
            result[2] = 0;
            return;
        }
        
        // 递归计算左右子树的结果
        int left[3], right[3];
        dfs(node->left, left);
        dfs(node->right, right);
        
        // 计算当前节点的三种状态
        
        // 1. 当前节点安装摄像头
        // 左右子树可以是任意状态，我们选择每种状态的最小值
        int leftMin = left[0] < left[1] ? (left[0] < left[2] ? left[0] : left[2]) : (left[1] < left[2] ? left[1] : left[2]);
        int rightMin = right[0] < right[1] ? (right[0] < right[2] ? right[0] : right[2]) : (right[1] < right[2] ? right[1] : right[2]);
        result[0] = 1 + leftMin + rightMin;
        
        // 2. 当前节点未安装摄像头但被子节点监控
        // 至少有一个子节点安装了摄像头
        int option1 = left[0] + right[0];
        int option2 = left[0] + right[1];
        int option3 = left[1] + right[0];
        int min1 = option1 < option2 ? option1 : option2;
        result[1] = min1 < option3 ? min1 : option3;
        
        // 3. 当前节点未被监控
        // 左右子树都必须被监控（但不一定安装摄像头）
        result[2] = left[1] + right[1];
    }
    
    // 补充题目1: 337. 打家劫舍 III
    // 题目链接: https://leetcode.cn/problems/house-robber-iii/
    // 题目描述: 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
    // 这个地区只有一个入口，我们称之为"根"。 除了"根"之外，每栋房子有且只有一个"父"房子与之相连。
    // 一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。
    // 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    // 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决树状结构打家劫舍问题的标准树形DP方法
    int rob(TreeNode* root) {
        int result[2];
        _rob_helper(root, result);
        // 返回偷或不偷当前节点的最大值
        return result[0] > result[1] ? result[0] : result[1];
    }
    
private:
    // 辅助函数计算结果并存储在result数组中：
    // result[0]: 不偷当前节点能获得的最大金额
    // result[1]: 偷当前节点能获得的最大金额
    void _rob_helper(TreeNode* node, int result[2]) {
        if (node == nullptr) {
            result[0] = 0;
            result[1] = 0;
            return;
        }
        
        // 递归计算左右子树
        int left[2], right[2];
        _rob_helper(node->left, left);
        _rob_helper(node->right, right);
        
        // 不偷当前节点，左右子树可以偷或不偷，取最大值之和
        int max_left = left[0] > left[1] ? left[0] : left[1];
        int max_right = right[0] > right[1] ? right[0] : right[1];
        result[0] = max_left + max_right;
        
        // 偷当前节点，则左右子树都不能偷
        result[1] = node->val + left[0] + right[0];
    }
    
public:
    // 补充题目2: 543. 二叉树的直径
    // 题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
    // 题目描述: 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
    // 这条路径可能穿过也可能不穿过根结点。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树直径问题的高效方法
    int diameterOfBinaryTree(TreeNode* root) {
        max_diameter = 0;  // 重置最大直径
        _max_depth(root);
        return max_diameter;
    }
    
private:
    int max_diameter;
    
    // 计算以当前节点为根的子树的最大深度，并同时更新最大直径
    int _max_depth(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的最大深度
        int left_depth = _max_depth(node->left);
        int right_depth = _max_depth(node->right);
        
        // 更新最大直径：左子树深度 + 右子树深度
        max_diameter = max_diameter > (left_depth + right_depth) ? max_diameter : (left_depth + right_depth);
        
        // 返回当前节点为根的子树的最大深度
        return (left_depth > right_depth ? left_depth : right_depth) + 1;
    }
    
public:
    // 补充题目3: 124. 二叉树中的最大路径和
    // 题目链接: https://leetcode.cn/problems/binary-tree-maximum-path-sum/
    // 题目描述: 路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
    // 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
    // 路径和是路径中各节点值的总和。
    // 给你一个二叉树的根节点 root ，返回其最大路径和 。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树最大路径和问题的标准树形DP方法
    int maxPathSum(TreeNode* root) {
        max_path_sum = INT_MIN;  // 重置最大路径和
        _max_gain(root);
        return max_path_sum;
    }
    
private:
    int max_path_sum;
    
    // 计算以当前节点为起点的最大路径和，并同时更新全局最大路径和
    int _max_gain(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的最大贡献值
        // 只有当贡献值大于0时，才会选择该子树
        int left_gain = max(_max_gain(node->left), 0);
        int right_gain = max(_max_gain(node->right), 0);
        
        // 更新最大路径和：当前节点的值 + 左子树的最大贡献 + 右子树的最大贡献
        max_path_sum = max(max_path_sum, node->val + left_gain + right_gain);
        
        // 返回当前节点为起点的最大路径和
        return node->val + max(left_gain, right_gain);
    }
    
public:
    // 补充题目4: 979. 在二叉树中分配硬币
    // 题目链接: https://leetcode.cn/problems/distribute-coins-in-binary-tree/
    // 题目描述: 给定一个有 N 个结点的二叉树的根结点 root，树中的每个结点上都对应有 node.val 枚硬币，
    // 并且总共有 N 枚硬币。在一次移动中，我们可以选择两个相邻的结点，然后将一枚硬币从其中一个结点移动到另一个结点。
    // (移动可以是从父结点到子结点，或者从子结点移动到父结点。)
    // 返回使每个结点上只有一枚硬币所需的移动次数。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树硬币分配问题的高效方法
    int distributeCoins(TreeNode* root) {
        moves = 0;  // 重置移动次数
        _distribute_helper(root);
        return moves;
    }
    
private:
    int moves;
    
    // 计算当前节点需要移动的硬币数量
    // 返回值表示当前节点需要传递给父节点的硬币数量（可能为负，表示需要从父节点获取）
    int _distribute_helper(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的硬币情况
        int left_coins = _distribute_helper(node->left);
        int right_coins = _distribute_helper(node->right);
        
        // 左右子树传递硬币的过程会产生移动次数
        // 取绝对值是因为不管是移入还是移出，都需要一次移动
        moves += abs(left_coins) + abs(right_coins);
        
        // 返回当前节点需要传递给父节点的硬币数量
        // 当前节点的硬币数减去1（自己需要保留的一枚）加上左右子树传递来的硬币
        return node->val - 1 + left_coins + right_coins;
    }
};