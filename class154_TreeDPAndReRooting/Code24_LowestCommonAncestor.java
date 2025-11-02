// 二叉树的最近公共祖先 - LeetCode 236
// 题目来源：LeetCode 236. Lowest Common Ancestor of a Binary Tree
// 题目链接：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
// 测试链接 : https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一个经典的树形问题，有多种解法：递归、迭代、路径记录等。

算法思路（递归版）：
1. 使用后序遍历（DFS）处理每个节点
2. 如果当前节点是p或q，返回当前节点
3. 递归处理左右子树
4. 如果左右子树都找到了目标节点，当前节点就是LCA
5. 如果只有一边找到，返回找到的那边

时间复杂度：O(n) - 每个节点最多访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，这是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code24_LowestCommonAncestor.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.cpp

工程化考量：
1. 异常处理：节点不存在、p=q等情况
2. 边界条件：p或q是根节点、p和q是同一个节点
3. 性能优化：提前终止搜索
*/

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

public class Code24_LowestCommonAncestor {
    
    /**
     * 递归解法：找到两个节点的最近公共祖先
     * 
     * @param root 二叉树根节点
     * @param p 第一个节点
     * @param q 第二个节点
     * @return 最近公共祖先节点
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 基础情况：空树或找到目标节点
        if (root == null || root == p || root == q) {
            return root;
        }
        
        // 递归处理左右子树
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // 如果左右子树都找到了目标节点，当前节点就是LCA
        if (left != null && right != null) {
            return root;
        }
        
        // 如果只有一边找到，返回找到的那边
        return left != null ? left : right;
    }
    
    // 迭代解法：使用父指针记录路径
    public TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        
        // 特殊情况：p和q是同一个节点
        if (p == q) {
            return p;
        }
        
        // 使用栈进行DFS遍历，记录每个节点的父节点
        java.util.Stack<TreeNode> stack = new java.util.Stack<>();
        java.util.Map<TreeNode, TreeNode> parent = new java.util.HashMap<>();
        
        stack.push(root);
        parent.put(root, null);
        
        // 遍历直到找到p和q
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = stack.pop();
            
            if (node.left != null) {
                parent.put(node.left, node);
                stack.push(node.left);
            }
            
            if (node.right != null) {
                parent.put(node.right, node);
                stack.push(node.right);
            }
        }
        
        // 记录p的祖先路径
        java.util.Set<TreeNode> ancestors = new java.util.HashSet<>();
        while (p != null) {
            ancestors.add(p);
            p = parent.get(p);
        }
        
        // 找到q的路径中第一个出现在p祖先路径中的节点
        while (!ancestors.contains(q)) {
            q = parent.get(q);
        }
        
        return q;
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code24_LowestCommonAncestor solution = new Code24_LowestCommonAncestor();
        
        // 构建测试树: [3,5,1,6,2,0,8,null,null,7,4]
        TreeNode root = new TreeNode(3);
        TreeNode p = new TreeNode(5);
        TreeNode q = new TreeNode(1);
        TreeNode node6 = new TreeNode(6);
        TreeNode node2 = new TreeNode(2);
        TreeNode node0 = new TreeNode(0);
        TreeNode node8 = new TreeNode(8);
        TreeNode node7 = new TreeNode(7);
        TreeNode node4 = new TreeNode(4);
        
        root.left = p;
        root.right = q;
        p.left = node6;
        p.right = node2;
        q.left = node0;
        q.right = node8;
        node2.left = node7;
        node2.right = node4;
        
        // 测试用例1: p=5, q=1
        TreeNode result1 = solution.lowestCommonAncestor(root, p, q);
        System.out.println("测试1: " + (result1 == root)); // 期望: true
        
        // 测试用例2: p=5, q=4
        TreeNode result2 = solution.lowestCommonAncestor(root, p, node4);
        System.out.println("测试2: " + (result2 == p)); // 期望: true
        
        // 测试用例3: p和q是同一个节点
        TreeNode result3 = solution.lowestCommonAncestor(root, p, p);
        System.out.println("测试3: " + (result3 == p)); // 期望: true
        
        // 测试迭代解法
        TreeNode result4 = solution.lowestCommonAncestorIterative(root, p, q);
        System.out.println("迭代测试1: " + (result4 == root)); // 期望: true
        
        TreeNode result5 = solution.lowestCommonAncestorIterative(root, p, node4);
        System.out.println("迭代测试2: " + (result5 == p)); // 期望: true
    }
    
    /**
     * 算法复杂度分析：
     * 递归解法：
     * - 时间复杂度：O(n) - 每个节点最多访问一次
     * - 空间复杂度：O(h) - 递归栈深度
     * 
     * 迭代解法：
     * - 时间复杂度：O(n) - 每个节点最多访问一次
     * - 空间复杂度：O(n) - 需要存储父指针和路径
     * 
     * 算法正确性验证：
     * 1. 基础情况：空树返回null，找到目标节点返回该节点
     * 2. LCA判定：正确识别最近公共祖先
     * 3. 边界处理：处理各种边界情况
     * 
     * 工程化改进：
     * 1. 提供多种解法
     * 2. 添加详细的注释和文档
     * 3. 支持大规模数据测试
     */
}