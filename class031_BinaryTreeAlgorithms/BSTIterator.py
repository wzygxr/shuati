# LeetCode 173. Binary Search Tree Iterator
# 题目链接: https://leetcode.cn/problems/binary-search-tree-iterator/
# 题目描述: 实现一个二叉搜索树迭代器类BSTIterator ，表示一个按中序遍历二叉搜索树（BST）的迭代器：
# - BSTIterator(TreeNode root) 初始化 BSTIterator 类的一个对象。BST 的根节点 root 会作为构造函数的一部分给出。
# - boolean hasNext() 如果向指针右侧遍历存在数字，则返回 true ；否则返回 false 。
# - int next()将指针向右移动，然后返回指针处的数字。
#
# 解题思路:
# 1. 使用栈模拟中序遍历的递归过程
# 2. 初始化时将根节点及其所有左子节点入栈
# 3. next()方法弹出栈顶节点，处理其右子树
# 4. hasNext()方法检查栈是否为空
#
# 时间复杂度: 
#   - 构造函数: O(h) - h为树的高度
#   - next(): 平均O(1)，最坏O(h)
#   - hasNext(): O(1)
# 空间复杂度: O(h) - 栈中最多存储h个节点
# 是否为最优解: 是，这是BST迭代器的标准实现

from typing import Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class BSTIterator:
    def __init__(self, root: Optional[TreeNode]):
        """
        BSTIterator构造函数
        初始化时将根节点及其所有左子节点入栈
        
        Args:
            root: 二叉搜索树的根节点
        """
        self.stack = []
        self._push_all_left(root)
    
    def _push_all_left(self, node: Optional[TreeNode]) -> None:
        """
        将节点及其所有左子节点入栈
        用于模拟中序遍历的左子树递归过程
        
        Args:
            node: 当前节点
        """
        while node is not None:
            self.stack.append(node)
            node = node.left
    
    def next(self) -> int:
        """
        返回中序遍历序列的下一个元素
        
        Returns:
            下一个节点的值
        """
        # 弹出栈顶节点（当前最小的节点）
        node = self.stack.pop()
        
        # 如果该节点有右子树，将右子树及其所有左子节点入栈
        if node.right is not None:
            self._push_all_left(node.right)
        
        return node.val
    
    def hasNext(self) -> bool:
        """
        检查是否还有下一个元素
        
        Returns:
            如果还有下一个元素返回True，否则返回False
        """
        return len(self.stack) > 0

# 测试用例
def main():
    # 构造测试BST:
    #       7
    #      / \
    #     3   15
    #        /  \
    #       9    20
    root = TreeNode(7)
    root.left = TreeNode(3)
    root.right = TreeNode(15)
    root.right.left = TreeNode(9)
    root.right.right = TreeNode(20)

    # 创建BST迭代器
    iterator = BSTIterator(root)
    
    print("BST中序遍历结果:")
    # 应该按顺序输出: 3, 7, 9, 15, 20
    while iterator.hasNext():
        print(iterator.next(), end=" ")
    print()  # 换行

    # 测试用例2: 空树
    print("\n=== 测试用例2: 空树 ===")
    empty_iterator = BSTIterator(None)
    print("空树hasNext:", empty_iterator.hasNext())  # 应该输出False

    # 测试用例3: 单节点树
    print("\n=== 测试用例3: 单节点树 ===")
    single_node = TreeNode(1)
    single_iterator = BSTIterator(single_node)
    print("单节点树遍历:")
    while single_iterator.hasNext():
        print(single_iterator.next(), end=" ")  # 应该输出1
    print()

    # 补充题目测试: LeetCode 230 - BST中第K小的元素
    print("\n=== 补充题目测试: BST中第K小的元素 ===")
    
    def kth_smallest(root: Optional[TreeNode], k: int) -> int:
        """
        使用BST迭代器思路找到第K小的元素
        
        Args:
            root: 二叉搜索树的根节点
            k: 第k小的元素位置
            
        Returns:
            第k小的元素值
        """
        stack = []
        current = root
        count = 0
        
        while current is not None or stack:
            # 将当前节点及其所有左子节点入栈
            while current is not None:
                stack.append(current)
                current = current.left
            
            # 弹出栈顶节点（当前最小的节点）
            current = stack.pop()
            count += 1
            
            # 如果找到第K小的元素，返回
            if count == k:
                return current.val
            
            # 处理右子树
            current = current.right
        
        return -1  # 理论上不会执行到这里
    
    k = 3
    kth_result = kth_smallest(root, k)
    print(f"第{k}小的元素: {kth_result}")  # 应该输出9

if __name__ == "__main__":
    main()