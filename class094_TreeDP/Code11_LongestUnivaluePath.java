package class079;

// 687. 最长同值路径
// 测试链接 : https://leetcode.cn/problems/longest-univalue-path/
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

public class Code11_LongestUnivaluePath {
    
    // 全局变量，记录最长同值路径的长度
    private int maxLength;
    
    // 提交如下的方法
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决最长同值路径问题的标准方法
    public int longestUnivaluePath(TreeNode root) {
        maxLength = 0;
        dfs(root);
        return maxLength;
    }
    
    // 递归函数返回从当前节点向下延伸的最长同值路径长度
    private int dfs(TreeNode node) {
        // 基础情况：如果节点为空，返回0
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的结果
        int leftPath = dfs(node.left);
        int rightPath = dfs(node.right);
        
        // 计算经过当前节点的最长同值路径长度
        int leftLength = 0, rightLength = 0;
        
        // 如果左子节点存在且值与当前节点相同，则可以延伸左路径
        if (node.left != null && node.left.val == node.val) {
            leftLength = leftPath + 1;
        }
        
        // 如果右子节点存在且值与当前节点相同，则可以延伸右路径
        if (node.right != null && node.right.val == node.val) {
            rightLength = rightPath + 1;
        }
        
        // 更新全局最长路径：经过当前节点的路径长度为左路径长度+右路径长度
        maxLength = Math.max(maxLength, leftLength + rightLength);
        
        // 返回从当前节点向下延伸的最长同值路径长度（只能选择左右路径中的一条）
        return Math.max(leftLength, rightLength);
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
    public int rob(TreeNode root) {
        int[] result = _robHelper(root);
        // 返回偷或不偷当前节点的最大值
        return Math.max(result[0], result[1]);
    }
    
    // 辅助函数返回一个数组：
    // result[0]: 不偷当前节点能获得的最大金额
    // result[1]: 偷当前节点能获得的最大金额
    private int[] _robHelper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        // 递归计算左右子树
        int[] left = _robHelper(node.left);
        int[] right = _robHelper(node.right);
        
        // 不偷当前节点，左右子树可以偷或不偷，取最大值之和
        int notRobCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        // 偷当前节点，则左右子树都不能偷
        int robCurrent = node.val + left[0] + right[0];
        
        return new int[]{notRobCurrent, robCurrent};
    }
    
    // 补充题目2: 979. 在二叉树中分配硬币
    // 题目链接: https://leetcode.cn/problems/distribute-coins-in-binary-tree/
    // 题目描述: 给定一个有 N 个结点的二叉树的根结点 root，树中的每个结点上都对应有 node.val 枚硬币，
    // 并且总共有 N 枚硬币。在一次移动中，我们可以选择两个相邻的结点，然后将一枚硬币从其中一个结点移动到另一个结点。
    // (移动可以是从父结点到子结点，或者从子结点移动到父结点。)
    // 返回使每个结点上只有一枚硬币所需的移动次数。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树硬币分配问题的高效方法
    private int moves;
    
    public int distributeCoins(TreeNode root) {
        moves = 0;  // 重置移动次数
        _distributeHelper(root);
        return moves;
    }
    
    // 计算当前节点需要移动的硬币数量
    // 返回值表示当前节点需要传递给父节点的硬币数量（可能为负，表示需要从父节点获取）
    private int _distributeHelper(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的硬币情况
        int leftCoins = _distributeHelper(node.left);
        int rightCoins = _distributeHelper(node.right);
        
        // 左右子树传递硬币的过程会产生移动次数
        // 取绝对值是因为不管是移入还是移出，都需要一次移动
        moves += Math.abs(leftCoins) + Math.abs(rightCoins);
        
        // 返回当前节点需要传递给父节点的硬币数量
        // 当前节点的硬币数减去1（自己需要保留的一枚）加上左右子树传递来的硬币
        return node.val - 1 + leftCoins + rightCoins;
    }
    
    // 补充题目3: 549. 二叉树中最长的连续序列
    // 题目链接: https://leetcode.cn/problems/binary-tree-longest-consecutive-sequence-ii/
    // 题目描述: 给定一个二叉树，你需要找出二叉树中最长的连续序列路径的长度。
    // 请注意，该路径可以是递增或递减的。例如，[1,2,3,4] 和 [4,3,2,1] 都被认为是有效的，但路径 [1,3,2,4] 不是有效的。
    // 另外，路径可以是子树中的任意节点开始，任意节点结束。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树最长连续序列问题的高效方法
    private int maxConsecutive;
    
    public int longestConsecutive(TreeNode root) {
        maxConsecutive = 0;
        _consecutiveHelper(root);
        return maxConsecutive;
    }
    
    // 返回一个数组：[递增序列长度, 递减序列长度]
    private int[] _consecutiveHelper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int inc = 1, dec = 1;
        
        if (node.left != null) {
            int[] left = _consecutiveHelper(node.left);
            if (node.val == node.left.val + 1) { // 当前节点比左子节点大1，递减
                dec = left[1] + 1;
            } else if (node.val == node.left.val - 1) { // 当前节点比左子节点小1，递增
                inc = left[0] + 1;
            }
        }
        
        if (node.right != null) {
            int[] right = _consecutiveHelper(node.right);
            if (node.val == node.right.val + 1) { // 当前节点比右子节点大1，递减
                dec = Math.max(dec, right[1] + 1);
            } else if (node.val == node.right.val - 1) { // 当前节点比右子节点小1，递增
                inc = Math.max(inc, right[0] + 1);
            }
        }
        
        // 更新最大长度，包括当前节点作为连接点的情况
        maxConsecutive = Math.max(maxConsecutive, inc + dec - 1);
        
        return new int[]{inc, dec};
    }
    
    // 补充题目4: 1372. 二叉树中的最长交错路径
    // 题目链接: https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
    // 题目描述: 给你一棵以 root 为根的二叉树，二叉树中的交错路径定义如下：
    // 选择二叉树中 任意 节点和一个方向（左或者右）。
    // 如果前进方向为右，那么移动到当前节点的的右子节点，然后前进方向变为左；反之亦然。
    // 不断重复这一过程，直到你在树中无法继续移动。
    // 交错路径的长度定义为：访问过的节点数目 - 1（单个节点的路径长度为 0 ）。
    // 返回给定树中最长交错路径的长度。
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是解决二叉树最长交错路径问题的高效方法
    private int maxZigzag;
    
    public int longestZigZag(TreeNode root) {
        maxZigzag = 0;
        _zigzagHelper(root, 0, 0); // 0表示从父节点的左子节点来，1表示从父节点的右子节点来，0表示初始状态
        return maxZigzag;
    }
    
    // direction: 0表示从父节点的左子节点来，1表示从父节点的右子节点来
    // length: 当前路径的长度
    private void _zigzagHelper(TreeNode node, int direction, int length) {
        if (node == null) {
            return;
        }
        
        maxZigzag = Math.max(maxZigzag, length);
        
        if (direction == 0) { // 从父节点的左子节点来，接下来可以向左或向右
            // 向左：重置路径长度为0，因为方向相同
            _zigzagHelper(node.left, 0, 1);
            // 向右：路径长度+1，方向变为1
            _zigzagHelper(node.right, 1, length + 1);
        } else { // 从父节点的右子节点来，接下来可以向左或向右
            // 向左：路径长度+1，方向变为0
            _zigzagHelper(node.left, 0, length + 1);
            // 向右：重置路径长度为0，因为方向相同
            _zigzagHelper(node.right, 1, 1);
        }
    }
}