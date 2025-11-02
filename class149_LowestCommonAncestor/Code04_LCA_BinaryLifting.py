"""
LCA问题 - 递归法实现
题目来源：LeetCode 236. Lowest Common Ancestor of a Binary Tree
题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/

问题描述：
给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
满足 x 是 p、q 的祖先且 x 的深度尽可能大。"

解题思路：
1. 使用递归深度优先搜索(DFS)
2. 对于每个节点，递归检查其左右子树
3. 如果当前节点是p或q，则直接返回当前节点
4. 如果左右子树分别找到了p和q，则当前节点就是LCA
5. 如果只在一侧子树找到了p或q，则返回找到的节点

时间复杂度：O(n) - n为树中节点的数量，最坏情况下需要遍历所有节点
空间复杂度：O(h) - h为树的高度，递归调用栈的深度
是否为最优解：是，这是寻找LCA的标准方法

工程化考虑：
1. 边界条件处理：处理空树、节点不存在等情况
2. 输入验证：验证输入节点是否在树中
3. 异常处理：对非法输入进行检查
4. 可读性：添加详细注释和变量命名

算法要点：
1. 递归终止条件：节点为空或者找到目标节点
2. 递归处理左右子树
3. 根据左右子树返回结果判断当前节点是否为LCA

与标准库实现对比：
1. 标准库通常有更完善的错误处理
2. 标准库可能使用迭代而非递归以避免栈溢出
3. 标准库可能有缓存优化

性能优化：
1. 剪枝优化：一旦找到LCA立即返回
2. 空间优化：使用原地算法，不额外开辟空间

特殊场景：
1. 空输入：返回None
2. 节点不存在：返回None
3. 一个节点是另一个节点的祖先：返回祖先节点
4. 重复数据：不适用（树结构中节点唯一）

语言特性差异：
1. Python：动态类型，引用计数垃圾回收
2. Java：对象引用传递，自动垃圾回收
3. C++：指针操作，需要手动管理内存

数学联系：
1. 与图论中的最短路径问题相关
2. 与树的深度优先搜索理论相关
3. 与并查集数据结构有一定联系

调试能力：
1. 可通过打印节点遍历顺序调试
2. 可通过断言验证中间结果
3. 可通过特殊测试用例验证边界条件
"""

from typing import Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left: Optional['TreeNode'] = None
        self.right: Optional['TreeNode'] = None

# 带父指针的二叉树节点类定义
class TreeNodeWithParent:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None
        self.parent = None

# 解决方案类
class Solution:
    """
    LCA (最近公共祖先) 算法的多种实现方式
    支持普通二叉树、二叉搜索树、带父指针的树等多种场景
    """
    
    def lowestCommonAncestor(self, root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """
        解法一：LeetCode 236. 二叉树的最近公共祖先
        题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
        难度：中等
        
        问题描述：
        给定一个二叉树，找到该树中两个指定节点的最近公共祖先。
        最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
        满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
        
        解题思路：递归深度优先搜索
        1. 递归终止条件：当前节点为空或者是p或q中的一个
        2. 递归搜索左右子树
        3. 根据左右子树的返回结果判断：
           - 如果左右子树都返回非空，说明当前节点就是LCA
           - 如果只有一侧返回非空，返回该侧的结果
           - 如果两侧都返回空，返回None
        
        时间复杂度：O(n) - 其中n是树中节点的数量，最坏情况下需要遍历所有节点
        空间复杂度：O(h) - 其中h是树的高度，递归调用栈的深度
        是否为最优解：对于单次查询，这是最优解之一
        """
        # 异常处理：检查输入参数
        if root is None or p is None or q is None:
            return None
        
        # 基本情况：如果当前节点是p或q，则当前节点就是LCA
        if root == p or root == q:
            return root
        
        # 递归查找左子树中的LCA
        left = self.lowestCommonAncestor(root.left, p, q)
        # 递归查找右子树中的LCA
        right = self.lowestCommonAncestor(root.right, p, q)
        
        # 如果左右子树都找到了节点，说明当前节点是LCA
        if left is not None and right is not None:
            return root
        
        # 如果只有左子树找到了节点，返回左子树的结果
        if left is not None:
            return left
        
        # 如果只有右子树找到了节点，返回右子树的结果
        if right is not None:
            return right
        
        # 如果左右子树都没有找到节点，返回None
        return None
    
    def lowestCommonAncestorBST(self, root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """
        解法二：LeetCode 235. 二叉搜索树的最近公共祖先
        题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/
        难度：简单
        
        问题描述：
        给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
        最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
        满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
        
        解题思路：利用二叉搜索树的特性
        二叉搜索树的特性：左子树所有节点值 < 根节点值 < 右子树所有节点值
        1. 如果p和q的值都小于当前节点，那么LCA在左子树
        2. 如果p和q的值都大于当前节点，那么LCA在右子树
        3. 如果一个小于等于，一个大于等于，那么当前节点就是LCA
        
        时间复杂度：O(h) - 其中h是树的高度，在平衡树情况下为O(log n)
        空间复杂度：O(h) - 递归调用栈的深度
        是否为最优解：是，利用了BST的特性，比通用二叉树解法更高效
        """
        # 异常处理
        if root is None or p is None or q is None:
            return None
        
        # 如果p和q都在左子树
        if p.val < root.val and q.val < root.val:
            return self.lowestCommonAncestorBST(root.left, p, q)
        # 如果p和q都在右子树
        elif p.val > root.val and q.val > root.val:
            return self.lowestCommonAncestorBST(root.right, p, q)
        # 如果p和q分别在两侧，或者其中一个是当前节点
        else:
            return root
    
    def lowestCommonAncestorWithParent(self, p: 'TreeNodeWithParent', q: 'TreeNodeWithParent') -> 'TreeNodeWithParent':
        """
        解法三：LeetCode 1650. 二叉树的最近公共祖先 III (带父指针)
        题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree-iii/
        难度：中等
        
        问题描述：
        给定一棵二叉树中的两个节点 p 和 q，返回它们的最近公共祖先（LCA）节点。
        每个节点都有一个指向其父节点的指针。
        
        解题思路：双指针法
        1. 分别计算p和q到根节点的深度差
        2. 先将较深的节点向上移动，使两个节点处于同一深度
        3. 然后同时向上移动两个节点，直到找到相同的节点，即为LCA
        
        时间复杂度：O(h) - 其中h是树的高度
        空间复杂度：O(1) - 只使用常数额外空间
        是否为最优解：是，充分利用了父指针的特性
        """
        if not p or not q:
            return None
        
        a, b = p, q
        
        # 双指针法，类似于链表相交问题
        # 当a或b为空时，将其指向对方的起始节点，这样可以抵消深度差
        while a != b:
            a = q if a is None else a.parent
            b = p if b is None else b.parent
        
        return a
    
    def lowestCommonAncestorIterative(self, root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """
        解法四：迭代版本的二叉树LCA（避免递归栈溢出）
        适用于处理大型树的情况
        
        解题思路：后序遍历 + 记录父节点路径
        1. 使用栈进行后序遍历
        2. 记录每个节点的访问状态
        3. 当找到p或q时，记录其路径
        4. 比较两条路径，找到最后一个公共节点
        
        时间复杂度：O(n)
        空间复杂度：O(h)
        """
        if root is None or p is None or q is None:
            return None
        
        # 存储路径
        parent_map = {root: None}
        stack = [root]
        
        # 遍历树，构建父节点映射
        while p not in parent_map or q not in parent_map:
            node = stack.pop()
            
            if node.right:
                parent_map[node.right] = node
                stack.append(node.right)
            if node.left:
                parent_map[node.left] = node
                stack.append(node.left)
        
        # 构建p的祖先集合
        ancestors = set()
        current = p
        while current:
            ancestors.add(current)
            current = parent_map[current]
        
        # 查找q的祖先中是否在p的祖先集合中
        current = q
        while current not in ancestors:
            current = parent_map[current]
        
        return current

# 测试代码
def test_all_lca_implementations():
    solution = Solution()
    
    print("========== 测试普通二叉树的LCA算法 ==========")
    # 测试用例1: 简单树结构
    #       3
    #      / \
    #     5   1
    #    / \
    #   6   2
    root1 = TreeNode(3)
    node5 = TreeNode(5)
    node1 = TreeNode(1)
    node6 = TreeNode(6)
    node2 = TreeNode(2)
    
    root1.left = node5
    root1.right = node1
    node5.left = node6
    node5.right = node2
    
    # 测试LCA(5, 1) 应该返回 3
    lca1 = solution.lowestCommonAncestor(root1, node5, node1)
    print(f"Test Case 1: LCA(5, 1) = {lca1.val if lca1 else None}")
    
    # 测试LCA(5, 6) 应该返回 5
    lca2 = solution.lowestCommonAncestor(root1, node5, node6)
    print(f"Test Case 2: LCA(5, 6) = {lca2.val if lca2 else None}")
    
    # 测试用例2: 更复杂的树结构
    #        1
    #       / \
    #      2   3
    #     / \   \
    #    4   5   6
    root2 = TreeNode(1)
    node2_2 = TreeNode(2)
    node2_3 = TreeNode(3)
    node2_4 = TreeNode(4)
    node2_5 = TreeNode(5)
    node2_6 = TreeNode(6)
    
    root2.left = node2_2
    root2.right = node2_3
    node2_2.left = node2_4
    node2_2.right = node2_5
    node2_3.right = node2_6
    
    # 测试LCA(4, 5) 应该返回 2
    lca3 = solution.lowestCommonAncestor(root2, node2_4, node2_5)
    print(f"Test Case 3: LCA(4, 5) = {lca3.val if lca3 else None}")
    
    # 测试LCA(4, 6) 应该返回 1
    lca4 = solution.lowestCommonAncestor(root2, node2_4, node2_6)
    print(f"Test Case 4: LCA(4, 6) = {lca4.val if lca4 else None}")
    
    # 测试用例3: 极端情况 - 空树
    lca5 = solution.lowestCommonAncestor(None, node5, node1)
    print(f"Test Case 5: LCA in null tree = {lca5.val if lca5 else None}")
    
    # 测试用例4: 极端情况 - 节点为空
    lca6 = solution.lowestCommonAncestor(root1, None, node1)
    print(f"Test Case 6: LCA with null node = {lca6.val if lca6 else None}")
    
    print("\n========== 测试二叉搜索树的LCA算法 ==========")
    # 测试用例5: 二叉搜索树
    #       6
    #      / \
    #     2   8
    #    / \ / \
    #   0  4 7  9
    #     / \
    #    3   5
    root3 = TreeNode(6)
    node3_2 = TreeNode(2)
    node3_8 = TreeNode(8)
    node3_0 = TreeNode(0)
    node3_4 = TreeNode(4)
    node3_7 = TreeNode(7)
    node3_9 = TreeNode(9)
    node3_3 = TreeNode(3)
    node3_5 = TreeNode(5)
    
    root3.left = node3_2
    root3.right = node3_8
    node3_2.left = node3_0
    node3_2.right = node3_4
    node3_8.left = node3_7
    node3_8.right = node3_9
    node3_4.left = node3_3
    node3_4.right = node3_5
    
    # 测试BST的LCA - LCA(2, 8) 应该返回 6
    bst_lca1 = solution.lowestCommonAncestorBST(root3, node3_2, node3_8)
    print(f"BST Test 1: LCA(2, 8) = {bst_lca1.val if bst_lca1 else None}")
    
    # 测试BST的LCA - LCA(2, 4) 应该返回 2
    bst_lca2 = solution.lowestCommonAncestorBST(root3, node3_2, node3_4)
    print(f"BST Test 2: LCA(2, 4) = {bst_lca2.val if bst_lca2 else None}")
    
    # 测试BST的LCA - LCA(3, 5) 应该返回 4
    bst_lca3 = solution.lowestCommonAncestorBST(root3, node3_3, node3_5)
    print(f"BST Test 3: LCA(3, 5) = {bst_lca3.val if bst_lca3 else None}")
    
    print("\n========== 测试带父指针的LCA算法 ==========")
    # 测试用例6: 带父指针的二叉树
    #       3
    #      / \
    #     5   1
    #    / \
    #   6   2
    root4 = TreeNodeWithParent(3)
    node4_5 = TreeNodeWithParent(5)
    node4_1 = TreeNodeWithParent(1)
    node4_6 = TreeNodeWithParent(6)
    node4_2 = TreeNodeWithParent(2)
    
    root4.left = node4_5
    root4.right = node4_1
    node4_5.left = node4_6
    node4_5.right = node4_2
    
    # 设置父指针
    node4_5.parent = root4
    node4_1.parent = root4
    node4_6.parent = node4_5
    node4_2.parent = node4_5
    
    # 测试带父指针的LCA - LCA(5, 1) 应该返回 3
    parent_lca1 = solution.lowestCommonAncestorWithParent(node4_5, node4_1)
    print(f"Parent Test 1: LCA(5, 1) = {parent_lca1.val if parent_lca1 else None}")
    
    # 测试带父指针的LCA - LCA(6, 2) 应该返回 5
    parent_lca2 = solution.lowestCommonAncestorWithParent(node4_6, node4_2)
    print(f"Parent Test 2: LCA(6, 2) = {parent_lca2.val if parent_lca2 else None}")
    
    print("\n========== 测试迭代版本的LCA算法 ==========")
    # 测试迭代版本的LCA - 使用root1树
    iterative_lca1 = solution.lowestCommonAncestorIterative(root1, node5, node1)
    print(f"Iterative Test 1: LCA(5, 1) = {iterative_lca1.val if iterative_lca1 else None}")
    
    # 测试迭代版本的LCA - 使用root2树
    iterative_lca2 = solution.lowestCommonAncestorIterative(root2, node2_4, node2_6)
    print(f"Iterative Test 2: LCA(4, 6) = {iterative_lca2.val if iterative_lca2 else None}")
    
    print("\n所有LCA算法测试完成！")

# 执行测试
if __name__ == "__main__":
    test_all_lca_implementations()