package class079;

// 968. 二叉树摄像头
// 测试链接 : https://leetcode.cn/problems/binary-tree-cameras/
import java.util.*;

// Definition for a binary tree node.
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class Code10_BinaryTreeCameras {
    
    // 提交如下的方法
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树摄像头问题的标准方法，使用贪心策略的树形DP
    public int minCameraCover(TreeNode root) {
        // 调用递归函数，返回包含三个状态值的数组
        int[] result = dfs(root);
        // 返回根节点的最小摄像头数量
        // 三种情况：根节点安装摄像头、根节点被子节点监控、根节点未被监控需要父节点安装摄像头
        // 我们选择前两种情况的最小值（根节点不能要求父节点安装摄像头）
        return Math.min(result[0], result[1]);
    }
    
    // 递归函数返回一个长度为3的数组，表示三种状态：
    // result[0]: 当前节点安装摄像头时，监控整棵树所需的最小摄像头数量
    // result[1]: 当前节点未安装摄像头但被子节点监控时，监控整棵树所需的最小摄像头数量
    // result[2]: 当前节点未被监控时，监控整棵树所需的最小摄像头数量（需要父节点安装摄像头）
    private int[] dfs(TreeNode node) {
        // 基础情况：如果节点为空，返回对应的状态值
        if (node == null) {
            // 空节点不需要安装摄像头，也不需要被监控
            return new int[]{Integer.MAX_VALUE / 2, 0, 0};
        }
        
        // 递归计算左右子树的结果
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // 计算当前节点的三种状态
        
        // 1. 当前节点安装摄像头
        // 左右子树可以是任意状态，我们选择每种状态的最小值
        int install = 1 + Math.min(Math.min(left[0], left[1]), left[2]) + 
                         Math.min(Math.min(right[0], right[1]), right[2]);
        
        // 2. 当前节点未安装摄像头但被子节点监控
        // 至少有一个子节点安装了摄像头
        int monitored = Math.min(
            Math.min(left[0] + right[0], left[0] + right[1]),
            left[1] + right[0]
        );
        
        // 3. 当前节点未被监控
        // 左右子树都必须被监控（但不一定安装摄像头）
        int unmonitored = left[1] + right[1];
        
        return new int[]{install, monitored, unmonitored};
    }
    
    // 补充题目1: 337. 打家劫舍 III
    // 题目链接: https://leetcode.cn/problems/house-robber-iii/
    // 题目描述: 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
    // 这个地区只有一个入口，我们称之为“根”。 除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
    // 一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
    // 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    // 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决树状结构打家劫舍问题的标准树形DP方法
    public int rob(TreeNode root) {
        int[] result = robHelper(root);
        // 返回偷或不偷当前节点的最大值
        return Math.max(result[0], result[1]);
    }
    
    // 辅助函数返回一个长度为2的数组：
    // result[0]: 不偷当前节点能获得的最大金额
    // result[1]: 偷当前节点能获得的最大金额
    private int[] robHelper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        // 递归计算左右子树
        int[] left = robHelper(node.left);
        int[] right = robHelper(node.right);
        
        // 不偷当前节点，左右子树可以偷或不偷，取最大值之和
        int notRobCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        // 偷当前节点，则左右子树都不能偷
        int robCurrent = node.val + left[0] + right[0];
        
        return new int[]{notRobCurrent, robCurrent};
    }
    
    // 补充题目2: 543. 二叉树的直径
    // 题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
    // 题目描述: 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
    // 这条路径可能穿过也可能不穿过根结点。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树直径问题的高效方法
    private int maxDiameter = 0;
    
    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameter = 0; // 重置最大直径
        maxDepth(root);
        return maxDiameter;
    }
    
    // 计算以当前节点为根的子树的最大深度，并同时更新最大直径
    private int maxDepth(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的最大深度
        int leftDepth = maxDepth(node.left);
        int rightDepth = maxDepth(node.right);
        
        // 更新最大直径：左子树深度 + 右子树深度
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);
        
        // 返回当前节点为根的子树的最大深度
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    // 补充题目3: 124. 二叉树中的最大路径和
    // 题目链接: https://leetcode.cn/problems/binary-tree-maximum-path-sum/
    // 题目描述: 路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
    // 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
    // 路径和是路径中各节点值的总和。
    // 给你一个二叉树的根节点 root ，返回其最大路径和 。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树最大路径和问题的标准树形DP方法
    private int maxPathSum = Integer.MIN_VALUE;
    
    public int maxPathSum(TreeNode root) {
        maxPathSum = Integer.MIN_VALUE; // 重置最大路径和
        maxGain(root);
        return maxPathSum;
    }
    
    // 计算以当前节点为起点的最大路径和，并同时更新全局最大路径和
    private int maxGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的最大贡献值
        // 只有当贡献值大于0时，才会选择该子树
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        
        // 更新最大路径和：当前节点的值 + 左子树的最大贡献 + 右子树的最大贡献
        maxPathSum = Math.max(maxPathSum, node.val + leftGain + rightGain);
        
        // 返回当前节点为起点的最大路径和
        return node.val + Math.max(leftGain, rightGain);
    }
    
    // 补充题目4: 979. 在二叉树中分配硬币
    // 题目链接: https://leetcode.cn/problems/distribute-coins-in-binary-tree/
    // 题目描述: 给定一个有 N 个结点的二叉树的根结点 root，树中的每个结点上都对应有 node.val 枚硬币，
    // 并且总共有 N 枚硬币。在一次移动中，我们可以选择两个相邻的结点，然后将一枚硬币从其中一个结点移动到另一个结点。
    // (移动可以是从父结点到子结点，或者从子结点移动到父结点。)
    // 返回使每个结点上只有一枚硬币所需的移动次数。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树硬币分配问题的高效方法
    private int moves = 0;
    
    public int distributeCoins(TreeNode root) {
        moves = 0; // 重置移动次数
        distributeHelper(root);
        return moves;
    }
    
    // 计算当前节点需要移动的硬币数量
    // 返回值表示当前节点需要传递给父节点的硬币数量（可能为负，表示需要从父节点获取）
    private int distributeHelper(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的硬币情况
        int leftCoins = distributeHelper(node.left);
        int rightCoins = distributeHelper(node.right);
        
        // 左右子树传递硬币的过程会产生移动次数
        // 取绝对值是因为不管是移入还是移出，都需要一次移动
        moves += Math.abs(leftCoins) + Math.abs(rightCoins);
        
        // 返回当前节点需要传递给父节点的硬币数量
        // 当前节点的硬币数减去1（自己需要保留的一枚）加上左右子树传递来的硬币
        return node.val - 1 + leftCoins + rightCoins;
    }
}