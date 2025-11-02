// 二叉树的最近公共祖先
// 题目链接：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
// 给定一个二叉树，找到该树中两个指定节点的最近公共祖先（LCA）。
// 最近公共祖先的定义为：“对于有根树 T 的两个节点 p 和 q，最近公共祖先是后代节点中同时包含 p 和 q 的最深节点（这里的一个节点也可以是它自己的后代）。”

/*
题目解析：
这是一个经典的树遍历问题。我们需要找到二叉树中两个节点的最近公共祖先，即同时是p和q的祖先且深度最深的节点。

算法思路：
1. 递归解法（后序遍历）：
   - 如果当前节点为空，返回null
   - 如果当前节点是p或q中的一个，返回当前节点
   - 递归搜索左右子树
   - 如果左右子树搜索结果都不为空，说明p和q分别在当前节点的两侧，因此当前节点就是LCA
   - 如果只有一侧不为空，返回不为空的一侧结果
   - 如果两侧都为空，返回null

时间复杂度：O(n) - 每个节点最多被访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决二叉树最近公共祖先问题的最优方法

边界情况：
- 空树：返回null
- p或q不存在于树中：题目假设p和q都存在于树中
- p是q的祖先或q是p的祖先：递归会正确返回祖先节点
- 树中只有两个节点：返回根节点

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 最近公共祖先问题与树形数据结构的层次关系分析相关
- 在生物信息学中，LCA问题与进化树分析有联系

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code17_LowestCommonAncestor.cpp
*/

// 导入必要的类
import java.util.*;

// 二叉树节点的定义
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 边界条件处理：空树
        if (root == null) {
            return null;
        }
        
        // 如果当前节点是p或q中的一个，直接返回当前节点
        if (root == p || root == q) {
            return root;
        }
        
        // 递归搜索左子树
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        
        // 递归搜索右子树
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // 情况1：如果左右子树都找到了结果，说明p和q分别在当前节点的两侧，当前节点就是LCA
        if (left != null && right != null) {
            return root;
        }
        
        // 情况2：如果只有一侧找到结果，返回那一侧的结果
        // 情况3：如果两侧都没找到结果，返回null
        return left != null ? left : right;
    }
}

// 迭代实现版本（使用后序遍历的变体）
class SolutionIterative {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 边界条件处理
        if (root == null || root == p || root == q) {
            return root;
        }
        
        // 存储每个节点的父节点映射
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        
        // 使用栈进行迭代后序遍历
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        parentMap.put(root, null);  // 根节点没有父节点
        
        // 遍历树，直到找到p和q
        while (!parentMap.containsKey(p) || !parentMap.containsKey(q)) {
            TreeNode current = stack.pop();
            
            // 先处理右子节点
            if (current.right != null) {
                parentMap.put(current.right, current);
                stack.push(current.right);
            }
            
            // 再处理左子节点
            if (current.left != null) {
                parentMap.put(current.left, current);
                stack.push(current.left);
            }
        }
        
        // 收集p的所有祖先
        Set<TreeNode> ancestors = new HashSet<>();
        TreeNode current = p;
        while (current != null) {
            ancestors.add(current);
            current = parentMap.get(current);
        }
        
        // 向上查找q的祖先，直到找到在p的祖先集合中的节点
        current = q;
        while (!ancestors.contains(current)) {
            current = parentMap.get(current);
        }
        
        return current;
    }
}

// 优化的递归实现，添加提前终止条件
class SolutionOptimized {
    private boolean foundP = false;
    private boolean foundQ = false;
    
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 重置标记
        foundP = false;
        foundQ = false;
        
        TreeNode result = dfs(root, p, q);
        
        // 验证p和q都在树中（根据题目假设，这一步可能不是必需的）
        return (foundP && foundQ) ? result : null;
    }
    
    private TreeNode dfs(TreeNode node, TreeNode p, TreeNode q) {
        if (node == null) {
            return null;
        }
        
        // 检查当前节点是否是p或q
        boolean isCurrentP = (node == p);
        boolean isCurrentQ = (node == q);
        
        // 更新发现标记
        if (isCurrentP) foundP = true;
        if (isCurrentQ) foundQ = true;
        
        // 提前终止：如果已经找到p和q，可以提前返回
        if (foundP && foundQ) {
            return node;  // 这个返回值不会被使用，因为我们已经找到了
        }
        
        // 递归搜索左右子树
        TreeNode left = dfs(node.left, p, q);
        
        // 如果左子树已经找到了LCA，直接返回
        if (left != null && left != p && left != q) {
            return left;
        }
        
        TreeNode right = dfs(node.right, p, q);
        
        // 情况1：当前节点是p或q，且另一侧找到了另一个目标节点
        if ((isCurrentP && right == q) || (isCurrentQ && right == p)) {
            return node;
        }
        
        // 情况2：左右子树各找到了一个目标节点
        if (left != null && right != null) {
            return node;
        }
        
        // 情况3：当前节点是目标节点之一
        if (isCurrentP || isCurrentQ) {
            return node;
        }
        
        // 情况4：只有一侧找到了结果
        return left != null ? left : right;
    }
}

// 主类，用于测试
public class Code17_LowestCommonAncestor {
    public static void main(String[] args) {
        // 构建测试树
        //       3
        //      / \
        //     5   1
        //    / \ / \
        //   6  2 0  8
        //     / \
        //    7   4
        TreeNode root = new TreeNode(3);
        TreeNode node5 = new TreeNode(5);
        TreeNode node1 = new TreeNode(1);
        TreeNode node6 = new TreeNode(6);
        TreeNode node2 = new TreeNode(2);
        TreeNode node0 = new TreeNode(0);
        TreeNode node8 = new TreeNode(8);
        TreeNode node7 = new TreeNode(7);
        TreeNode node4 = new TreeNode(4);
        
        root.left = node5;
        root.right = node1;
        node5.left = node6;
        node5.right = node2;
        node1.left = node0;
        node1.right = node8;
        node2.left = node7;
        node2.right = node4;
        
        Solution solution = new Solution();
        
        // 测试用例1: p = 5, q = 1，预期输出: 3
        TreeNode result1 = solution.lowestCommonAncestor(root, node5, node1);
        System.out.println("测试用例1结果: " + result1.val);  // 预期输出: 3
        
        // 测试用例2: p = 5, q = 4，预期输出: 5
        TreeNode result2 = solution.lowestCommonAncestor(root, node5, node4);
        System.out.println("测试用例2结果: " + result2.val);  // 预期输出: 5
        
        // 测试用例3: p = 6, q = 4，预期输出: 5
        TreeNode result3 = solution.lowestCommonAncestor(root, node6, node4);
        System.out.println("测试用例3结果: " + result3.val);  // 预期输出: 5
        
        // 测试迭代版本
        SolutionIterative iterativeSolution = new SolutionIterative();
        TreeNode iterativeResult1 = iterativeSolution.lowestCommonAncestor(root, node5, node1);
        System.out.println("迭代版本 测试用例1结果: " + iterativeResult1.val);  // 预期输出: 3
        
        // 测试优化版本
        SolutionOptimized optimizedSolution = new SolutionOptimized();
        TreeNode optimizedResult1 = optimizedSolution.lowestCommonAncestor(root, node5, node1);
        System.out.println("优化版本 测试用例1结果: " + optimizedResult1.val);  // 预期输出: 3
    }
}

/*
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 在优化版本中添加了对p和q是否在树中的验证
   - 代码可以处理p或q是另一个节点的祖先的情况

2. 性能优化：
   - 递归版本在找到结果后会提前终止不必要的搜索
   - 优化版本添加了foundP和foundQ标记，避免不必要的搜索
   - 迭代版本使用哈希表存储父节点信息，减少重复计算

3. 代码质量：
   - 提供了三种实现方式：递归版本、迭代版本和优化的递归版本
   - 添加了详细的注释说明算法思路和各种情况的处理
   - 包含多个测试用例验证正确性

4. 可扩展性：
   - 如果需要处理N叉树，只需修改搜索子节点的部分
   - 如果需要查找多个节点的LCA，可以扩展为查找两两之间的LCA

5. 调试技巧：
   - 可以在递归函数中添加打印语句，输出当前节点的值和搜索状态
   - 对于复杂树结构，可以使用图形化工具可视化树的结构
   - 使用JUnit等测试框架进行单元测试

6. Java特有优化：
   - 使用哈希表和集合进行高效的查找操作
   - 使用栈模拟递归过程，避免深层递归可能导致的栈溢出
   - 使用布尔变量提前终止递归

7. 算法安全与业务适配：
   - 对于非常深的树，迭代版本更安全，可以避免栈溢出
   - 对于大规模数据，可以使用非递归实现提高效率
   - 代码中添加了适当的边界检查，确保程序不会崩溃
*/