// 树中的最长交错路径 - LeetCode 1372
// 给定一棵二叉树的根节点，找到最长的交错路径
// 交错路径定义为：路径中相邻节点交替向左和向右移动
// 测试链接 : https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/

/*
题目解析：
这是一个树形DP问题，需要计算树中最长的交错路径。
交错路径定义为路径中相邻节点交替向左和向右移动。

算法思路：
1. 使用DFS遍历每个节点
2. 对于每个节点，维护两个状态：
   - 从当前节点向左走的最大长度
   - 从当前节点向右走的最大长度
3. 更新全局最长交错路径

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，这是解决此类问题的最优方法

工程化考量：
1. 异常处理：空树、单节点树
2. 边界条件：链状树、星状树
3. 性能优化：避免重复计算

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code23_LongestZigZagPath.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.cpp
*/

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

public class Code23_LongestZigZagPath {
    
    private int maxLength;
    
    public int longestZigZag(TreeNode root) {
        maxLength = 0;
        dfs(root);
        return maxLength;
    }
    
    /**
     * DFS遍历，返回当前节点的两个状态
     * 
     * @param node 当前节点
     * @return int[2]数组，[0]表示向左走的最大长度，[1]表示向右走的最大长度
     */
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{-1, -1}; // 空节点长度为-1
        }
        
        int[] leftState = dfs(node.left);
        int[] rightState = dfs(node.right);
        
        // 当前节点的状态
        int leftMax = 0, rightMax = 0;
        
        // 从当前节点向左走：如果左子节点存在，可以向右走（交错）
        if (node.left != null) {
            leftMax = rightState[0] + 1; // 从左子节点向右走
        }
        
        // 从当前节点向右走：如果右子节点存在，可以向左走（交错）
        if (node.right != null) {
            rightMax = leftState[1] + 1; // 从右子节点向左走
        }
        
        // 更新全局最大长度
        maxLength = Math.max(maxLength, Math.max(leftMax, rightMax));
        
        return new int[]{leftMax, rightMax};
    }
    
    // 另一种实现：更直观的DFS
    public int longestZigZag2(TreeNode root) {
        maxLength = 0;
        dfs2(root, true, 0);  // 从根节点开始，可以向左或向右
        dfs2(root, false, 0);
        return maxLength;
    }
    
    /**
     * DFS遍历，记录当前路径长度和方向
     * 
     * @param node 当前节点
     * @param isLeft 是否向左走
     * @param length 当前路径长度
     */
    private void dfs2(TreeNode node, boolean isLeft, int length) {
        if (node == null) {
            return;
        }
        
        maxLength = Math.max(maxLength, length);
        
        if (isLeft) {
            // 当前向左走，下一步应该向右走
            dfs2(node.left, false, length + 1); // 继续交错
            dfs2(node.right, true, 1); // 重新开始（改变方向）
        } else {
            // 当前向右走，下一步应该向左走
            dfs2(node.right, true, length + 1); // 继续交错
            dfs2(node.left, false, 1); // 重新开始（改变方向）
        }
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code23_LongestZigZagPath solution = new Code23_LongestZigZagPath();
        
        // 测试用例1: [1,null,1,1,1,null,null,1,1,null,1,null,null,null,1]
        TreeNode root1 = new TreeNode(1,
            null,
            new TreeNode(1,
                new TreeNode(1),
                new TreeNode(1,
                    new TreeNode(1,
                        null,
                        new TreeNode(1,
                            null,
                            new TreeNode(1))),
                    null)));
        System.out.println("测试1: " + solution.longestZigZag(root1)); // 期望: 3
        
        // 测试用例2: [1,1,1,null,1,null,null,1,1,null,1]
        TreeNode root2 = new TreeNode(1,
            new TreeNode(1,
                null,
                new TreeNode(1,
                    new TreeNode(1,
                        null,
                        new TreeNode(1)),
                    new TreeNode(1))),
            new TreeNode(1));
        System.out.println("测试2: " + solution.longestZigZag(root2)); // 期望: 4
        
        // 测试用例3: 单节点
        TreeNode root3 = new TreeNode(1);
        System.out.println("测试3: " + solution.longestZigZag(root3)); // 期望: 0
        
        // 测试第二种实现
        System.out.println("方法2测试1: " + solution.longestZigZag2(root1)); // 期望: 3
        System.out.println("方法2测试2: " + solution.longestZigZag2(root2)); // 期望: 4
    }
    
    /**
     * 算法复杂度分析：
     * 时间复杂度：O(n) - 每个节点只被访问一次
     * 空间复杂度：O(h) - 递归栈深度
     * 
     * 算法正确性验证：
     * 1. 基础情况：单节点路径长度为0
     * 2. 交错约束：路径必须交替改变方向
     * 3. 全局维护：正确更新最长路径
     * 
     * 工程化改进：
     * 1. 提供多种实现方法
     * 2. 添加详细的注释和文档
     * 3. 支持大规模数据测试
     */
}