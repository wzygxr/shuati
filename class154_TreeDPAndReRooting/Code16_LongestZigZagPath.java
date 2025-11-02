// 二叉树中的最长交错路径
// 题目链接：https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/
// 给定一个二叉树，找到最长的路径，这个路径中的每个相邻节点在二叉树中都处于不同的父-子关系中。
// 例如，路径是父节点的右子节点，然后是左子节点，接着是右子节点等。

/*
题目解析：
这是一个树形动态规划问题。我们需要找到二叉树中最长的交错路径，即路径中相邻节点交替左右子节点关系。

算法思路：
对于每个节点，我们可以从两个方向到达它：
1. 从父节点的左侧到达（left direction）
2. 从父节点的右侧到达（right direction）

对于每个节点，我们可以计算两个值：
- 如果从左侧到达该节点，那么它的最长交错路径长度
- 如果从右侧到达该节点，那么它的最长交错路径长度

状态转移方程：
- 如果当前节点有左子节点，那么从左侧到达左子节点的路径长度 = 从右侧到达当前节点的路径长度 + 1
- 如果当前节点有右子节点，那么从右侧到达右子节点的路径长度 = 从左侧到达当前节点的路径长度 + 1

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决二叉树最长交错路径问题的最优方法

边界情况：
- 空树：路径长度为0
- 单节点树：路径长度为0（因为没有相邻节点）
- 只有一条直线的树：最长路径长度为1（因为只能交替一次）

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 路径寻找问题与图神经网络（GNN）中的路径分析类似
- 最长路径问题与自然语言处理中的最长依赖路径相关

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.cpp
*/

// 导入必要的类
import java.util.*;

// 二叉树节点的定义
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

class Solution {
    // 全局变量，用于存储最长交错路径的长度
    private int maxLength = 0;
    
    public int longestZigZag(TreeNode root) {
        // 边界条件处理：空树
        if (root == null) {
            return 0;
        }
        
        // 初始化最长长度
        maxLength = 0;
        
        // 从根节点开始，尝试向左和向右的路径
        // 由于根节点没有父节点，初始方向可以任意，这里用-1表示没有方向
        dfs(root, -1, 0);
        
        return maxLength;
    }
    
    /**
     * 深度优先搜索函数
     * @param node 当前节点
     * @param direction 当前方向：0表示从父节点左侧来，1表示从父节点右侧来，-1表示没有方向（根节点）
     * @param length 当前路径长度
     */
    private void dfs(TreeNode node, int direction, int length) {
        // 更新全局最大长度
        maxLength = Math.max(maxLength, length);
        
        // 如果还有左子节点
        if (node.left != null) {
            // 如果当前是从右侧来的，或者是根节点，那么向左走可以形成交错路径
            if (direction != 0) {  // 不是从左侧来的
                dfs(node.left, 0, length + 1);
            } else {
                // 如果当前是从左侧来的，那么向左走不能形成交错路径，需要重新开始
                dfs(node.left, 0, 1);
            }
        }
        
        // 如果还有右子节点
        if (node.right != null) {
            // 如果当前是从左侧来的，或者是根节点，那么向右走可以形成交错路径
            if (direction != 1) {  // 不是从右侧来的
                dfs(node.right, 1, length + 1);
            } else {
                // 如果当前是从右侧来的，那么向右走不能形成交错路径，需要重新开始
                dfs(node.right, 1, 1);
            }
        }
    }
}

// 优化版本：使用返回值而不是全局变量
class Solution2 {
    public int longestZigZag(TreeNode root) {
        // 边界条件处理：空树
        if (root == null) {
            return 0;
        }
        
        // 创建一个数组来存储最长长度（使用数组是为了在递归中能够修改它）
        int[] maxLength = {0};
        
        // 从根节点开始，尝试向左和向右的路径
        dfs(root, -1, 0, maxLength);
        
        return maxLength[0];
    }
    
    private void dfs(TreeNode node, int direction, int length, int[] maxLength) {
        // 更新全局最大长度
        maxLength[0] = Math.max(maxLength[0], length);
        
        // 如果还有左子节点
        if (node.left != null) {
            // 如果当前是从右侧来的，或者是根节点，那么向左走可以形成交错路径
            if (direction != 0) {  // 不是从左侧来的
                dfs(node.left, 0, length + 1, maxLength);
            } else {
                // 如果当前是从左侧来的，那么向左走不能形成交错路径，需要重新开始
                dfs(node.left, 0, 1, maxLength);
            }
        }
        
        // 如果还有右子节点
        if (node.right != null) {
            // 如果当前是从左侧来的，或者是根节点，那么向右走可以形成交错路径
            if (direction != 1) {  // 不是从右侧来的
                dfs(node.right, 1, length + 1, maxLength);
            } else {
                // 如果当前是从右侧来的，那么向右走不能形成交错路径，需要重新开始
                dfs(node.right, 1, 1, maxLength);
            }
        }
    }
}

// 更简洁的实现：使用返回值表示从左和从右的最长路径
class Solution3 {
    private int maxLength = 0;
    
    public int longestZigZag(TreeNode root) {
        // 边界条件处理：空树
        if (root == null) {
            return 0;
        }
        
        maxLength = 0;
        dfs(root);
        return maxLength;
    }
    
    /**
     * 返回一个数组，其中：
     * result[0] 表示从当前节点向左走的最长交错路径长度
     * result[1] 表示从当前节点向右走的最长交错路径长度
     */
    private int[] dfs(TreeNode node) {
        if (node == null) {
            // 空节点返回-1，表示无法继续延伸路径
            return new int[]{-1, -1};
        }
        
        // 递归计算左右子节点的最长交错路径
        int[] leftResult = dfs(node.left);
        int[] rightResult = dfs(node.right);
        
        // 计算从当前节点向左走的最长路径：当前节点 -> 左子节点 -> 右子节点...
        int leftPath = leftResult[1] + 1;
        
        // 计算从当前节点向右走的最长路径：当前节点 -> 右子节点 -> 左子节点...
        int rightPath = rightResult[0] + 1;
        
        // 更新全局最大长度
        maxLength = Math.max(maxLength, Math.max(leftPath, rightPath));
        
        // 返回从当前节点向左和向右走的最长路径长度
        return new int[]{leftPath, rightPath};
    }
}

// 主类，用于测试
public class Code16_LongestZigZagPath {
    public static void main(String[] args) {
        // 测试用例1: [1,null,1,1,1,null,null,1,1,null,1,null,null,null,1]
        // 最长路径是 [1,1,1,1,1]，长度为3
        TreeNode root1 = new TreeNode(1);
        root1.right = new TreeNode(1);
        root1.right.left = new TreeNode(1);
        root1.right.right = new TreeNode(1);
        root1.right.left.left = new TreeNode(1);
        root1.right.left.right = new TreeNode(1);
        root1.right.left.left.right = new TreeNode(1);
        
        Solution solution = new Solution();
        System.out.println("测试用例1结果: " + solution.longestZigZag(root1));  // 预期输出: 3
        
        // 测试用例2: [1,1,1,null,1,null,null,1,1,null,1]
        // 最长路径是 [1,1,1,1]，长度为3
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(1);
        root2.left.right = new TreeNode(1);
        root2.left.right.left = new TreeNode(1);
        root2.left.right.right = new TreeNode(1);
        root2.left.right.left.right = new TreeNode(1);
        
        System.out.println("测试用例2结果: " + solution.longestZigZag(root2));  // 预期输出: 3
        
        // 测试用例3: [1]
        // 单节点树，最长路径长度为0
        TreeNode root3 = new TreeNode(1);
        
        System.out.println("测试用例3结果: " + solution.longestZigZag(root3));  // 预期输出: 0
        
        // 测试Solution2和Solution3
        Solution2 solution2 = new Solution2();
        Solution3 solution3 = new Solution3();
        
        System.out.println("Solution2 测试用例1结果: " + solution2.longestZigZag(root1));  // 预期输出: 3
        System.out.println("Solution3 测试用例1结果: " + solution3.longestZigZag(root1));  // 预期输出: 3
    }
}

/*
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 处理了单节点树的特殊情况
   - 注意整数溢出问题（在这个问题中不太可能，但保持警惕）

2. 性能优化：
   - 避免重复计算：每个节点只访问一次
   - 使用递归DFS遍历树，时间复杂度为O(n)
   - 空间上使用递归调用栈，最坏情况下为O(n)

3. 代码质量：
   - 提供了三种实现方式：使用全局变量、使用数组引用和使用返回值
   - 添加了详细的注释说明算法思路和参数含义
   - 包含多个测试用例验证正确性

4. 可扩展性：
   - 如果需要返回具体的最长路径而不仅仅是长度，可以在递归中记录路径
   - 如果需要处理N叉树，可以修改算法以考虑多个子节点的情况

5. 调试技巧：
   - 可以在递归函数中添加打印语句，输出当前节点的值、方向和路径长度
   - 对于复杂树结构，可以使用图形化工具可视化树的结构

6. Java特有优化：
   - 使用数组作为引用传递来修改递归过程中的变量
   - 避免使用不必要的对象创建
   - 注意空指针检查

7. 算法安全与业务适配：
   - 对于非常深的树，可能会导致栈溢出，可以考虑使用迭代版本
   - 对于大规模数据，可以使用非递归DFS或BFS实现
*/