"""
Morris遍历求二叉树最小高度 - Python实现

题目来源：
- 二叉树最小深度：LeetCode 111. Minimum Depth of Binary Tree
  链接：https://leetcode.cn/problems/minimum-depth-of-binary-tree/

Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。

本实现包含：
1. Python语言的Morris遍历计算最小深度
2. 递归版本的计算最小深度
3. 迭代版本的计算最小深度（BFS）
4. 详细的注释和算法解析
5. 完整的测试用例

算法详解：
利用Morris中序遍历计算二叉树的最小深度，通过记录遍历过程中的层数来确定叶子节点的深度
1. 在Morris遍历过程中维护当前节点所在的层数
2. 当第二次访问节点时，检查其左子树的最右节点是否为叶子节点
3. 最后检查整棵树的最右节点是否为叶子节点
4. 返回所有叶子节点深度中的最小值

时间复杂度：O(n)，空间复杂度：O(1)
适用场景：内存受限环境中计算大规模二叉树的最小深度

工程化考量：
1. 异常处理：处理空树、单节点树等边界情况
2. 代码可读性：清晰的变量命名和详细注释
3. 类型注解：使用类型注解提高代码可读性
4. 模块化：将不同方法封装为独立函数

语言特性差异：
- Python：动态类型系统，代码更简洁
- 列表推导式便于结果收集
- 无显式指针操作，通过属性访问实现
- 垃圾回收机制无需手动管理内存
"""

from typing import Optional, List
from collections import deque
import sys


class TreeNode:
    """二叉树节点定义"""
    
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
    
    def __repr__(self):
        return f"TreeNode({self.val})"


def morris_min_depth(root: Optional[TreeNode]) -> int:
    """
    Morris遍历求二叉树最小深度
    
    算法思路：
    1. 使用Morris中序遍历遍历二叉树
    2. 在遍历过程中维护当前节点的层数
    3. 当第二次访问节点时，检查其左子树的最右节点是否为叶子节点
    4. 最后检查整棵树的最右节点是否为叶子节点
    5. 返回所有叶子节点深度中的最小值
    
    时间复杂度：O(n) - 每个节点最多被访问3次
    空间复杂度：O(1) - 仅使用常数额外空间
    
    Args:
        root: 二叉树根节点
        
    Returns:
        int: 最小深度
    """
    if not root:
        return 0
    
    cur = root
    min_depth = sys.maxsize
    cur_level = 1  # 当前节点所在层数
    
    while cur:
        most_right = cur.left
        
        if most_right:
            # 计算左子树最右节点的层数
            right_level = 1
            temp = cur.left
            while temp != cur and temp.right != cur:
                temp = temp.right
                right_level += 1
            
            if most_right.right is None:
                # 第一次到达当前节点，建立线索
                most_right.right = cur
                cur = cur.left
                cur_level += 1
                continue
            else:
                # 第二次到达当前节点，断开线索
                most_right.right = None
                
                # 检查左子树的最右节点是否为叶子节点
                if most_right.left is None:
                    min_depth = min(min_depth, cur_level - 1)
        else:
            # 没有左子树，检查当前节点是否为叶子节点
            if cur.left is None and cur.right is None:
                min_depth = min(min_depth, cur_level)
        
        cur = cur.right
        cur_level += 1
    
    # 检查整棵树的最右节点是否为叶子节点
    right_most = root
    right_most_level = 1
    while right_most:
        if right_most.left is None and right_most.right is None:
            min_depth = min(min_depth, right_most_level)
            break
        right_most = right_most.right
        right_most_level += 1
    
    return min_depth


def recursive_min_depth(root: Optional[TreeNode]) -> int:
    """
    递归版本求二叉树最小深度
    
    算法思路：
    1. 如果根节点为空，返回0
    2. 如果左右子树都为空，返回1
    3. 如果左子树为空，返回右子树的最小深度+1
    4. 如果右子树为空，返回左子树的最小深度+1
    5. 否则返回左右子树最小深度的较小值+1
    
    时间复杂度：O(n) - 每个节点被访问一次
    空间复杂度：O(h) - h为树高，最坏情况下为O(n)
    
    Args:
        root: 二叉树根节点
        
    Returns:
        int: 最小深度
    """
    if not root:
        return 0
    
    # 叶子节点
    if not root.left and not root.right:
        return 1
    
    # 只有右子树
    if not root.left:
        return recursive_min_depth(root.right) + 1
    
    # 只有左子树
    if not root.right:
        return recursive_min_depth(root.left) + 1
    
    # 左右子树都存在
    return min(recursive_min_depth(root.left), recursive_min_depth(root.right)) + 1


def iterative_min_depth(root: Optional[TreeNode]) -> int:
    """
    迭代版本（BFS）求二叉树最小深度
    
    算法思路：
    1. 使用队列进行层次遍历
    2. 记录每个节点所在的层数
    3. 遇到第一个叶子节点时返回其层数
    
    时间复杂度：O(n) - 每个节点被访问一次
    空间复杂度：O(w) - w为树的最大宽度
    
    Args:
        root: 二叉树根节点
        
    Returns:
        int: 最小深度
    """
    if not root:
        return 0
    
    queue = deque([(root, 1)])
    
    while queue:
        node, depth = queue.popleft()
        
        # 找到第一个叶子节点
        if not node.left and not node.right:
            return depth
        
        if node.left:
            queue.append((node.left, depth + 1))
        if node.right:
            queue.append((node.right, depth + 1))
    
    return 0  # 不会执行到这里


def create_test_tree1() -> TreeNode:
    """创建测试用例1：平衡二叉树"""
    # [3,9,20,null,null,15,7]
    #       3
    #      / \
    #     9  20
    #       /  \
    #      15   7
    root = TreeNode(3)
    root.left = TreeNode(9)
    root.right = TreeNode(20)
    root.right.left = TreeNode(15)
    root.right.right = TreeNode(7)
    return root


def create_test_tree2() -> TreeNode:
    """创建测试用例2：右斜树"""
    # [2,null,3,null,4,null,5,null,6]
    # 2
    #  \
    #   3
    #    \
    #     4
    #      \
    #       5
    #        \
    #         6
    root = TreeNode(2)
    root.right = TreeNode(3)
    root.right.right = TreeNode(4)
    root.right.right.right = TreeNode(5)
    root.right.right.right.right = TreeNode(6)
    return root


def create_test_tree3() -> TreeNode:
    """创建测试用例3：完全二叉树"""
    # [1,2,3,4,5]
    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.left.right = TreeNode(5)
    return root


def delete_tree(root: Optional[TreeNode]) -> None:
    """释放二叉树内存"""
    if not root:
        return
    delete_tree(root.left)
    delete_tree(root.right)
    # Python自动垃圾回收，这里主要是为了逻辑完整性


def test_morris_min_depth():
    """测试函数"""
    print("=== Morris遍历求二叉树最小深度测试 ===")
    
    # 测试用例1：平衡二叉树
    print("测试用例1 ([3,9,20,null,null,15,7]):")
    root1 = create_test_tree1()
    print(f"Morris方法结果: {morris_min_depth(root1)}")
    print(f"递归方法结果: {recursive_min_depth(root1)}")
    print(f"迭代方法结果: {iterative_min_depth(root1)}")
    delete_tree(root1)
    print()
    
    # 测试用例2：右斜树
    print("测试用例2 ([2,null,3,null,4,null,5,null,6]):")
    root2 = create_test_tree2()
    print(f"Morris方法结果: {morris_min_depth(root2)}")
    print(f"递归方法结果: {recursive_min_depth(root2)}")
    print(f"迭代方法结果: {iterative_min_depth(root2)}")
    delete_tree(root2)
    print()
    
    # 测试用例3：完全二叉树
    print("测试用例3 ([1,2,3,4,5]):")
    root3 = create_test_tree3()
    print(f"Morris方法结果: {morris_min_depth(root3)}")
    print(f"递归方法结果: {recursive_min_depth(root3)}")
    print(f"迭代方法结果: {iterative_min_depth(root3)}")
    delete_tree(root3)
    print()
    
    # 测试用例4：空树
    print("测试用例4 (空树):")
    root4 = None
    print(f"Morris方法结果: {morris_min_depth(root4)}")
    print(f"递归方法结果: {recursive_min_depth(root4)}")
    print(f"迭代方法结果: {iterative_min_depth(root4)}")
    print()
    
    # 测试用例5：单节点树
    print("测试用例5 ([1]):")
    root5 = TreeNode(1)
    print(f"Morris方法结果: {morris_min_depth(root5)}")
    print(f"递归方法结果: {recursive_min_depth(root5)}")
    print(f"迭代方法结果: {iterative_min_depth(root5)}")
    delete_tree(root5)
    print()
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    test_morris_min_depth()


"""
算法复杂度分析：

Morris方法：
- 时间复杂度：O(n) - 每个节点最多被访问3次
- 空间复杂度：O(1) - 仅使用常数额外空间
- 是否为最优解：是，从空间复杂度角度最优

递归方法：
- 时间复杂度：O(n) - 每个节点被访问一次
- 空间复杂度：O(h) - h为树高，最坏情况下为O(n)
- 是否为最优解：否，空间复杂度不是最优

迭代方法（BFS）：
- 时间复杂度：O(n) - 每个节点被访问一次
- 空间复杂度：O(w) - w为树的最大宽度
- 是否为最优解：在大多数情况下是实际最优解

工程化建议：
1. 对于内存受限环境，优先选择Morris方法
2. 对于一般应用场景，选择迭代方法（BFS）更实用
3. 递归方法代码简洁，适合教学和快速验证
4. 在实际工程中，根据具体需求选择合适的方法

Python特有优化：
1. 使用类型注解提高代码可读性
2. 利用Python的动态特性简化代码实现
3. 使用collections.deque提高队列操作效率
4. 利用Python的垃圾回收机制简化内存管理
"""