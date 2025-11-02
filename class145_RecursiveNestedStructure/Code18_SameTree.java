// LeetCode 100. Same Tree
// 相同的树
// 题目来源：https://leetcode.cn/problems/same-tree/

/**
 * 问题描述：
 * 给你两棵二叉树的根节点 p 和 q，编写一个函数来检验这两棵树是否相同。
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 * 
 * 解题思路：
 * 1. 递归方法：深度优先搜索，同时遍历两棵树的每个节点
 * 2. 迭代方法：使用队列或栈同时处理两棵树的节点
 * 
 * 时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
 * 空间复杂度：
 *   - 递归：最坏情况下O(N)（树为链状），平均O(log N)（平衡树）
 *   - 迭代：O(N)，栈或队列最多存储树的最宽层的所有节点
 */

// 二叉树节点定义
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

public class Code18_SameTree {
    /**
     * 递归方法判断两棵二叉树是否相同
     * @param p 第一棵二叉树的根节点
     * @param q 第二棵二叉树的根节点
     * @return 如果两棵树相同返回true，否则返回false
     */
    public boolean isSameTreeRecursive(TreeNode p, TreeNode q) {
        // 情况1：两个节点都为空，它们是相同的
        if (p == null && q == null) {
            return true;
        }
        
        // 情况2：一个节点为空，另一个不为空，它们不相同
        if (p == null || q == null) {
            return false;
        }
        
        // 情况3：两个节点都不为空，比较它们的值和子树
        // 1. 比较当前节点的值
        // 2. 递归比较左子树
        // 3. 递归比较右子树
        // 只有当这三个条件都满足时，两棵树才相同
        return (p.val == q.val) && 
               isSameTreeRecursive(p.left, q.left) && 
               isSameTreeRecursive(p.right, q.right);
    }
    
    /**
     * 迭代方法判断两棵二叉树是否相同（使用队列，BFS）
     * @param p 第一棵二叉树的根节点
     * @param q 第二棵二叉树的根节点
     * @return 如果两棵树相同返回true，否则返回false
     */
    public boolean isSameTreeIterativeBFS(TreeNode p, TreeNode q) {
        // 使用队列同时存储两棵树的对应节点
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        
        // 初始时将两棵树的根节点加入队列
        queue.offer(p);
        queue.offer(q);
        
        // 当队列不为空时，继续处理
        while (!queue.isEmpty()) {
            // 从队列中取出两棵树的对应节点
            TreeNode nodeP = queue.poll();
            TreeNode nodeQ = queue.poll();
            
            // 如果两个节点都为空，继续处理下一对节点
            if (nodeP == null && nodeQ == null) {
                continue;
            }
            
            // 如果一个节点为空另一个不为空，或者节点值不相同，返回false
            if (nodeP == null || nodeQ == null || nodeP.val != nodeQ.val) {
                return false;
            }
            
            // 将两个节点的左子节点加入队列
            queue.offer(nodeP.left);
            queue.offer(nodeQ.left);
            
            // 将两个节点的右子节点加入队列
            queue.offer(nodeP.right);
            queue.offer(nodeQ.right);
        }
        
        // 所有节点都比较完成，两棵树相同
        return true;
    }
    
    /**
     * 迭代方法判断两棵二叉树是否相同（使用栈，DFS）
     * @param p 第一棵二叉树的根节点
     * @param q 第二棵二叉树的根节点
     * @return 如果两棵树相同返回true，否则返回false
     */
    public boolean isSameTreeIterativeDFS(TreeNode p, TreeNode q) {
        // 使用栈同时存储两棵树的对应节点
        java.util.Stack<TreeNode> stack = new java.util.Stack<>();
        
        // 初始时将两棵树的根节点加入栈
        stack.push(p);
        stack.push(q);
        
        // 当栈不为空时，继续处理
        while (!stack.isEmpty()) {
            // 从栈中取出两棵树的对应节点
            TreeNode nodeQ = stack.pop();
            TreeNode nodeP = stack.pop();
            
            // 如果两个节点都为空，继续处理下一对节点
            if (nodeP == null && nodeQ == null) {
                continue;
            }
            
            // 如果一个节点为空另一个不为空，或者节点值不相同，返回false
            if (nodeP == null || nodeQ == null || nodeP.val != nodeQ.val) {
                return false;
            }
            
            // 将两个节点的右子节点加入栈
            stack.push(nodeP.right);
            stack.push(nodeQ.right);
            
            // 将两个节点的左子节点加入栈（注意顺序，先右后左，这样出栈时先处理左子节点）
            stack.push(nodeP.left);
            stack.push(nodeQ.left);
        }
        
        // 所有节点都比较完成，两棵树相同
        return true;
    }
    
    public static void main(String[] args) {
        Code18_SameTree solution = new Code18_SameTree();
        
        // 测试用例1：两棵相同的树
        // 树1:    1           树2:    1
        //        / \                 / \
        //       2   3               2   3
        TreeNode p1 = new TreeNode(1);
        p1.left = new TreeNode(2);
        p1.right = new TreeNode(3);
        
        TreeNode q1 = new TreeNode(1);
        q1.left = new TreeNode(2);
        q1.right = new TreeNode(3);
        
        System.out.println("测试用例1 - 递归方法: " + solution.isSameTreeRecursive(p1, q1));
        System.out.println("测试用例1 - 迭代BFS方法: " + solution.isSameTreeIterativeBFS(p1, q1));
        System.out.println("测试用例1 - 迭代DFS方法: " + solution.isSameTreeIterativeDFS(p1, q1));
        
        // 测试用例2：两棵不同的树
        // 树1:    1           树2:    1
        //        /                     \
        //       2                       2
        TreeNode p2 = new TreeNode(1);
        p2.left = new TreeNode(2);
        
        TreeNode q2 = new TreeNode(1);
        q2.right = new TreeNode(2);
        
        System.out.println("测试用例2 - 递归方法: " + solution.isSameTreeRecursive(p2, q2));
        System.out.println("测试用例2 - 迭代BFS方法: " + solution.isSameTreeIterativeBFS(p2, q2));
        System.out.println("测试用例2 - 迭代DFS方法: " + solution.isSameTreeIterativeDFS(p2, q2));
        
        // 测试用例3：两棵不同的树
        // 树1:    1           树2:    1
        //        / \                 / \
        //       2   1               1   2
        TreeNode p3 = new TreeNode(1);
        p3.left = new TreeNode(2);
        p3.right = new TreeNode(1);
        
        TreeNode q3 = new TreeNode(1);
        q3.left = new TreeNode(1);
        q3.right = new TreeNode(2);
        
        System.out.println("测试用例3 - 递归方法: " + solution.isSameTreeRecursive(p3, q3));
        System.out.println("测试用例3 - 迭代BFS方法: " + solution.isSameTreeIterativeBFS(p3, q3));
        System.out.println("测试用例3 - 迭代DFS方法: " + solution.isSameTreeIterativeDFS(p3, q3));
        
        // 测试用例4：两棵空树
        System.out.println("测试用例4 - 递归方法: " + solution.isSameTreeRecursive(null, null));
        System.out.println("测试用例4 - 迭代BFS方法: " + solution.isSameTreeIterativeBFS(null, null));
        System.out.println("测试用例4 - 迭代DFS方法: " + solution.isSameTreeIterativeDFS(null, null));
        
        // 测试用例5：一棵树为空，另一棵不为空
        System.out.println("测试用例5 - 递归方法: " + solution.isSameTreeRecursive(new TreeNode(1), null));
        System.out.println("测试用例5 - 迭代BFS方法: " + solution.isSameTreeIterativeBFS(new TreeNode(1), null));
        System.out.println("测试用例5 - 迭代DFS方法: " + solution.isSameTreeIterativeDFS(new TreeNode(1), null));
    }
}

/**
 * 性能分析：
 * 
 * 1. 递归实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：
 *      - 最好情况：O(log N)，对于完全平衡的二叉树
 *      - 最坏情况：O(N)，对于链状树（每个节点只有一个子节点）
 *      - 平均情况：O(log N)
 *    - 优点：代码简洁，逻辑清晰，容易实现和理解
 *    - 缺点：对于非常深的树可能导致栈溢出
 * 
 * 2. 迭代BFS实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：O(W)，其中W是树中最宽层的节点数
 *    - 优点：避免了递归调用栈溢出的风险
 *    - 缺点：代码相对复杂，需要额外的数据结构（队列）
 * 
 * 3. 迭代DFS实现：
 *    - 时间复杂度：O(N)，每个节点都会被访问一次
 *    - 空间复杂度：O(H)，其中H是树的高度
 *    - 优点：对于不平衡的树，可能比BFS更节省空间
 *    - 缺点：需要手动维护栈，实现相对复杂
 * 
 * 工程化考量：
 * 1. 对于小型树或平衡树，递归实现是最简单和最高效的选择
 * 2. 对于可能有很深深度的树，应优先选择迭代实现以避免栈溢出
 * 3. 在Java中，递归深度默认限制约为1000，超过这个深度会抛出StackOverflowError
 * 4. 可以根据具体应用场景选择合适的实现方式：
 *    - 如果树的宽度较小，DFS可能更节省内存
 *    - 如果树的高度较大，BFS可能更合适
 * 5. 实际应用中，可以添加日志记录和异常处理，以增强代码的健壮性
 * 6. 对于频繁调用的场景，可以考虑缓存中间结果或使用记忆化搜索
 * 
 * 扩展思考：
 * 1. 这个问题可以扩展到比较N叉树是否相同
 * 2. 可以修改算法来判断一棵树是否是另一棵树的子树
 * 3. 对于非常大的树，可以考虑使用并行处理或流式处理来提高效率
 */