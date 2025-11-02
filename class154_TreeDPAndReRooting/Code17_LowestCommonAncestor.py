# 二叉树的最近公共祖先
# 题目链接：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
# 给定一个二叉树，找到该树中两个指定节点的最近公共祖先（LCA）。
# 最近公共祖先的定义为："对于有根树 T 的两个节点 p 和 q，最近公共祖先是后代节点中同时包含 p 和 q 的最深节点（这里的一个节点也可以是它自己的后代）。"

'''
题目解析：
这是一个经典的树遍历问题。我们需要找到二叉树中两个节点的最近公共祖先，即同时是p和q的祖先且深度最深的节点。

算法思路：
1. 递归解法（后序遍历）：
   - 如果当前节点为空，返回None
   - 如果当前节点是p或q中的一个，返回当前节点
   - 递归搜索左右子树
   - 如果左右子树搜索结果都不为None，说明p和q分别在当前节点的两侧，因此当前节点就是LCA
   - 如果只有一侧不为None，返回不为None的一侧结果
   - 如果两侧都为None，返回None

时间复杂度：O(n) - 每个节点最多被访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)（递归调用栈的开销）
是否为最优解：是，这是解决二叉树最近公共祖先问题的最优方法

边界情况：
- 空树：返回None
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
'''

# 二叉树节点的定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution:
    def lowestCommonAncestor(self, root, p, q):
        # 边界条件处理：空树
        if root is None:
            return None
        
        # 如果当前节点是p或q中的一个，直接返回当前节点
        if root == p or root == q:
            return root
        
        # 递归搜索左子树
        left = self.lowestCommonAncestor(root.left, p, q)
        
        # 递归搜索右子树
        right = self.lowestCommonAncestor(root.right, p, q)
        
        # 情况1：如果左右子树都找到了结果，说明p和q分别在当前节点的两侧，当前节点就是LCA
        if left is not None and right is not None:
            return root
        
        # 情况2：如果只有一侧找到结果，返回那一侧的结果
        # 情况3：如果两侧都没找到结果，返回None
        return left if left is not None else right

# 使用列表传递引用的版本（避免全局变量）
class SolutionWithListRef:
    def lowestCommonAncestor(self, root, p, q):
        # 使用列表作为引用容器来存储结果
        result = [None]
        self._dfs(root, p, q, result)
        return result[0]
    
    def _dfs(self, node, p, q, result):
        # 如果已经找到结果或者节点为空，提前返回
        if result[0] is not None or node is None:
            return False
        
        # 当前节点是否是目标节点之一
        is_self = (node == p) or (node == q)
        
        # 左子树中是否包含目标节点
        is_left = self._dfs(node.left, p, q, result)
        
        # 右子树中是否包含目标节点
        is_right = self._dfs(node.right, p, q, result)
        
        # 发现LCA的三种情况：
        # 1. 当前节点是其中一个目标节点，且另一目标节点在其子树中
        # 2. 左右子树各包含一个目标节点
        if (is_self and (is_left or is_right)) or (is_left and is_right):
            result[0] = node
            return True
        
        # 返回当前子树是否包含至少一个目标节点
        return is_self or is_left or is_right

# 迭代实现版本（使用后序遍历的变体）
class SolutionIterative:
    def lowestCommonAncestor(self, root, p, q):
        # 边界条件处理
        if root is None or root == p or root == q:
            return root
        
        # 存储每个节点的父节点映射
        parent_map = {}
        
        # 使用栈进行迭代后序遍历
        stack = [root]
        parent_map[root] = None  # 根节点没有父节点
        
        # 遍历树，直到找到p和q
        while p not in parent_map or q not in parent_map:
            current = stack.pop()
            
            # 先处理右子节点
            if current.right is not None:
                parent_map[current.right] = current
                stack.append(current.right)
            
            # 再处理左子节点
            if current.left is not None:
                parent_map[current.left] = current
                stack.append(current.left)
        
        # 收集p的所有祖先
        ancestors = set()
        current = p
        while current is not None:
            ancestors.add(current)
            current = parent_map[current]
        
        # 向上查找q的祖先，直到找到在p的祖先集合中的节点
        current = q
        while current not in ancestors:
            current = parent_map[current]
        
        return current

# 优化的递归实现，使用额外的函数参数传递发现状态
class SolutionOptimized:
    def lowestCommonAncestor(self, root, p, q):
        def dfs(node):
            # 返回值：(是否找到LCA, LCA节点, 是否找到p, 是否找到q)
            if node is None:
                return False, None, False, False
            
            # 检查当前节点是否是p或q
            found_p = (node == p)
            found_q = (node == q)
            
            # 递归搜索左子树
            left_has_lca, left_lca, left_p, left_q = dfs(node.left)
            if left_has_lca:
                return True, left_lca, True, True  # 左子树已经找到LCA
            
            # 更新p和q的发现状态
            found_p = found_p or left_p
            found_q = found_q or left_q
            
            # 如果已经同时找到了p和q，可以提前返回
            if found_p and found_q:
                return True, node, True, True  # 当前节点就是LCA
            
            # 递归搜索右子树
            right_has_lca, right_lca, right_p, right_q = dfs(node.right)
            if right_has_lca:
                return True, right_lca, True, True  # 右子树已经找到LCA
            
            # 更新p和q的发现状态
            found_p = found_p or right_p
            found_q = found_q or right_q
            
            # 判断是否找到LCA
            if found_p and found_q:
                return True, node, True, True  # 当前节点是LCA
            
            # 返回当前子树的搜索状态
            return False, None, found_p, found_q
        
        # 调用辅助函数并返回结果
        _, lca, _, _ = dfs(root)
        return lca

# 测试代码
if __name__ == "__main__":
    # 构建测试树
    #       3
    #      / \
    #     5   1
    #    / \ / \
    #   6  2 0  8
    #     / \
    #    7   4
    root = TreeNode(3)
    node5 = TreeNode(5)
    node1 = TreeNode(1)
    node6 = TreeNode(6)
    node2 = TreeNode(2)
    node0 = TreeNode(0)
    node8 = TreeNode(8)
    node7 = TreeNode(7)
    node4 = TreeNode(4)
    
    root.left = node5
    root.right = node1
    node5.left = node6
    node5.right = node2
    node1.left = node0
    node1.right = node8
    node2.left = node7
    node2.right = node4
    
    # 测试各种解法
    solution = Solution()
    print("=== 标准递归解法 ===")
    
    # 测试用例1: p = 5, q = 1，预期输出: 3
    result1 = solution.lowestCommonAncestor(root, node5, node1)
    print(f"测试用例1结果: {result1.val}")  # 预期输出: 3
    
    # 测试用例2: p = 5, q = 4，预期输出: 5
    result2 = solution.lowestCommonAncestor(root, node5, node4)
    print(f"测试用例2结果: {result2.val}")  # 预期输出: 5
    
    # 测试用例3: p = 6, q = 4，预期输出: 5
    result3 = solution.lowestCommonAncestor(root, node6, node4)
    print(f"测试用例3结果: {result3.val}")  # 预期输出: 5
    
    # 测试列表引用版本
    solution_list_ref = SolutionWithListRef()
    print("\n=== 列表引用解法 ===")
    result_list1 = solution_list_ref.lowestCommonAncestor(root, node5, node1)
    print(f"测试用例1结果: {result_list1.val}")  # 预期输出: 3
    
    # 测试迭代版本
    solution_iterative = SolutionIterative()
    print("\n=== 迭代解法 ===")
    result_iter1 = solution_iterative.lowestCommonAncestor(root, node5, node1)
    print(f"测试用例1结果: {result_iter1.val}")  # 预期输出: 3
    
    # 测试优化版本
    solution_optimized = SolutionOptimized()
    print("\n=== 优化递归解法 ===")
    result_opt1 = solution_optimized.lowestCommonAncestor(root, node5, node1)
    print(f"测试用例1结果: {result_opt1.val}")  # 预期输出: 3

'''
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 代码可以处理p或q是另一个节点的祖先的情况
   - Python语言特性使得None的处理更加自然

2. 性能优化：
   - 递归版本在各个实现中都添加了提前终止条件
   - 优化版本使用更丰富的返回值来传递状态，避免不必要的搜索
   - 迭代版本避免了深层递归可能导致的栈溢出问题

3. 代码质量：
   - 提供了多种实现方式，适应不同场景
   - 使用Python特有的语言特性（如列表作为引用容器）
   - 添加了详细的注释说明算法思路和各种情况的处理

4. 可扩展性：
   - 代码结构清晰，易于扩展到其他类似问题
   - 可以轻松修改为处理N叉树的情况

5. 调试技巧：
   - 可以在递归函数中添加print语句输出当前节点的值和搜索状态
   - 使用Python的调试器（如pdb）设置断点进行调试
   - 对于复杂树结构，可以添加可视化辅助函数

6. Python特有优化：
   - 利用Python的多重返回值特性，在优化版本中传递更多状态信息
   - 使用集合（set）进行高效的查找操作
   - 列表作为可变对象可以用于在函数调用中传递引用

7. 算法安全与业务适配：
   - 对于大型树结构，迭代版本更适合Python环境，避免递归深度限制
   - Python的垃圾回收机制自动处理节点对象，无需手动管理内存
   - 代码中添加了适当的边界检查，确保程序不会崩溃

8. 跨语言实现对比：
   - Python版本相比Java版本更简洁，语法更灵活
   - Python的多重返回值特性比Java的单返回值更适合复杂状态传递
   - Python的集合操作比Java的Set接口使用更简洁
'''