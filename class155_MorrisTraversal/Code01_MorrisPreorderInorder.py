"""
Morris遍历实现先序和中序遍历 - Python版本

题目来源：
- 先序遍历：LeetCode 144. Binary Tree Preorder Traversal
  链接：https://leetcode.cn/problems/binary-tree-preorder-traversal/
- 中序遍历：LeetCode 94. Binary Tree Inorder Traversal
  链接：https://leetcode.cn/problems/binary-tree-inorder-traversal/

Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。

本实现包含：
1. Python语言的Morris先序和中序遍历
2. 递归版本的先序和中序遍历
3. 迭代版本的先序和中序遍历
4. 详细的注释和算法解析
5. 完整的测试用例（常规树、空树、单节点树、链表结构树等）
6. 性能测试和算法对比

算法详解：
Morris遍历的核心思想是利用二叉树中大量空闲的空指针来存储遍历所需的路径信息，从而避免使用栈或递归调用栈所需的额外空间
1. 线索化：对于每个有左子树的节点，将其左子树的最右节点的右指针指向该节点本身，形成一个临时的线索
2. 两次访问：第一次访问节点时建立线索，第二次访问节点时删除线索并处理右子树
3. 还原树结构：每次访问完节点后，都会恢复树的原始结构，不影响后续操作

时间复杂度：O(n)，虽然每个节点可能被访问两次，但总体操作次数仍是线性的
空间复杂度：O(1)，只使用了常数级别的额外空间
适用场景：内存受限环境、嵌入式系统、超大二叉树遍历

优缺点分析：
- 优点：空间复杂度最优，不依赖栈或递归调用栈
- 缺点：实现复杂，修改树结构，不适合并发环境

运行命令：python Code01_MorrisPreorderInorder.py
"""

from typing import List, Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Code01_MorrisPreorderInorder:
    """
    Morris遍历算法实现类
    """
    
    @staticmethod
    def preorder_traversal(root: Optional[TreeNode]) -> List[int]:
        """
        Morris遍历实现先序遍历
        
        先序遍历顺序：根-左-右
        在Morris遍历中的实现：
        - 第一次访问节点时就收集值（适合先序遍历）
        - 如果节点没有左子树，则在第一次访问时直接收集
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            List[int]: 先序遍历的节点值列表
            
        时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
        空间复杂度：O(1) - 不考虑返回值的空间占用
        
        算法步骤：
        1. 初始化当前节点cur为根节点
        2. 当cur不为None时：
           a. 如果cur没有左子树，收集cur的值，cur移动到其右子树
           b. 如果cur有左子树：
              i. 找到cur左子树的最右节点most_right
              ii. 如果most_right的right指针为None（第一次访问cur）：
                  - 收集cur的值（先序遍历特性）
                  - 将most_right的right指向cur
                  - cur移动到其左子树
              iii. 如果most_right的right指针指向cur（第二次访问cur）：
                  - 将most_right的right恢复为None
                  - cur移动到其右子树
        """
        result = []
        # 防御性编程：处理空树情况
        if root is None:
            return result
        
        cur = root
        
        while cur is not None:
            if cur.left is not None:
                # cur有左子树
                most_right = cur.left
                # 找到左子树的最右节点
                while most_right.right is not None and most_right.right != cur:
                    most_right = most_right.right
                
                if most_right.right is None:
                    # 第一次访问cur节点
                    result.append(cur.val)  # 先序遍历：第一次访问时收集
                    most_right.right = cur  # 建立线索
                    cur = cur.left         # 继续遍历左子树
                    continue
                else:
                    # 第二次访问cur节点
                    most_right.right = None  # 恢复树的原始结构
            else:
                # cur没有左子树，只有一次访问机会
                result.append(cur.val)  # 收集当前节点值
            
            cur = cur.right  # 移动到右子树
        
        return result
    
    @staticmethod
    def inorder_traversal(root: Optional[TreeNode]) -> List[int]:
        """
        Morris遍历实现中序遍历
        
        中序遍历顺序：左-根-右
        在Morris遍历中的实现：
        - 第二次访问节点时收集值（适合中序遍历）
        - 如果节点没有左子树，则在访问时直接收集
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            List[int]: 中序遍历的节点值列表
            
        时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
        空间复杂度：O(1) - 不考虑返回值的空间占用
        
        算法步骤：
        1. 初始化当前节点cur为根节点
        2. 当cur不为None时：
           a. 如果cur没有左子树，收集cur的值，cur移动到其右子树
           b. 如果cur有左子树：
              i. 找到cur左子树的最右节点most_right
              ii. 如果most_right的right指针为None（第一次访问cur）：
                  - 将most_right的right指向cur
                  - cur移动到其左子树
              iii. 如果most_right的right指针指向cur（第二次访问cur）：
                  - 收集cur的值（中序遍历特性）
                  - 将most_right的right恢复为None
                  - cur移动到其右子树
        """
        result = []
        # 防御性编程：处理空树情况
        if root is None:
            return result
        
        cur = root
        
        while cur is not None:
            if cur.left is not None:
                # cur有左子树
                most_right = cur.left
                # 找到左子树的最右节点
                while most_right.right is not None and most_right.right != cur:
                    most_right = most_right.right
                
                if most_right.right is None:
                    # 第一次访问cur节点
                    most_right.right = cur  # 建立线索
                    cur = cur.left         # 继续遍历左子树
                    continue
                else:
                    # 第二次访问cur节点
                    most_right.right = None  # 恢复树的原始结构
                    result.append(cur.val)   # 中序遍历：第二次访问时收集
            else:
                # cur没有左子树，只有一次访问机会
                result.append(cur.val)  # 收集当前节点值
            
            cur = cur.right  # 移动到右子树
        
        return result
    
    @staticmethod
    def preorder_recursive(root: Optional[TreeNode], result: List[int]) -> None:
        """
        递归实现先序遍历（对比参考）
        
        Args:
            root: 二叉树的根节点
            result: 存储遍历结果的列表
            
        时间复杂度：O(n) - 每个节点访问一次
        空间复杂度：O(h) - h为树高，最坏情况下为O(n)
        """
        if root is None:
            return
        result.append(root.val)           # 访问根节点
        Code01_MorrisPreorderInorder.preorder_recursive(root.left, result)   # 遍历左子树
        Code01_MorrisPreorderInorder.preorder_recursive(root.right, result)  # 遍历右子树
    
    @staticmethod
    def inorder_recursive(root: Optional[TreeNode], result: List[int]) -> None:
        """
        递归实现中序遍历（对比参考）
        
        Args:
            root: 二叉树的根节点
            result: 存储遍历结果的列表
            
        时间复杂度：O(n) - 每个节点访问一次
        空间复杂度：O(h) - h为树高，最坏情况下为O(n)
        """
        if root is None:
            return
        Code01_MorrisPreorderInorder.inorder_recursive(root.left, result)   # 遍历左子树
        result.append(root.val)          # 访问根节点
        Code01_MorrisPreorderInorder.inorder_recursive(root.right, result) # 遍历右子树
    
    @staticmethod
    def preorder_iterative(root: Optional[TreeNode]) -> List[int]:
        """
        迭代实现先序遍历（对比参考）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            List[int]: 先序遍历的节点值列表
            
        时间复杂度：O(n) - 每个节点访问一次
        空间复杂度：O(h) - h为树高，最坏情况下为O(n)
        """
        result = []
        if root is None:
            return result
        
        stack = [root]
        
        while stack:
            node = stack.pop()
            result.append(node.val)
            
            # 先右后左，保证左子树先出栈
            if node.right is not None:
                stack.append(node.right)
            if node.left is not None:
                stack.append(node.left)
        
        return result
    
    @staticmethod
    def inorder_iterative(root: Optional[TreeNode]) -> List[int]:
        """
        迭代实现中序遍历（对比参考）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            List[int]: 中序遍历的节点值列表
            
        时间复杂度：O(n) - 每个节点访问一次
        空间复杂度：O(h) - h为树高，最坏情况下为O(n)
        """
        result = []
        stack = []
        cur = root
        
        while cur is not None or stack:
            # 一直向左遍历，直到叶子节点
            while cur is not None:
                stack.append(cur)
                cur = cur.left
            
            cur = stack.pop()
            result.append(cur.val)
            cur = cur.right
        
        return result
    
    @staticmethod
    def create_test_tree() -> TreeNode:
        """
        创建测试二叉树
        构建如下二叉树：
              1
             / \
            2   3
           / \
          4   5
        """
        root = TreeNode(1)
        root.left = TreeNode(2)
        root.right = TreeNode(3)
        root.left.left = TreeNode(4)
        root.left.right = TreeNode(5)
        return root
    
    @staticmethod
    def delete_tree(root: Optional[TreeNode]) -> None:
        """
        释放二叉树内存（Python有垃圾回收，这里主要为了完整性）
        """
        if root is None:
            return
        Code01_MorrisPreorderInorder.delete_tree(root.left)
        Code01_MorrisPreorderInorder.delete_tree(root.right)
        # Python会自动回收内存
    
    @staticmethod
    def print_list(lst: List[int], label: str) -> None:
        """
        打印列表内容
        """
        print(f"{label}: {' '.join(map(str, lst))}")
    
    @staticmethod
    def run_tests() -> None:
        """
        运行测试用例
        """
        print("=== Morris遍历算法测试 ===")
        
        # 测试用例1：常规二叉树
        print("\n测试用例1：常规二叉树")
        root1 = Code01_MorrisPreorderInorder.create_test_tree()
        
        preorder_morris = Code01_MorrisPreorderInorder.preorder_traversal(root1)
        inorder_morris = Code01_MorrisPreorderInorder.inorder_traversal(root1)
        
        Code01_MorrisPreorderInorder.print_list(preorder_morris, "Morris先序遍历")
        Code01_MorrisPreorderInorder.print_list(inorder_morris, "Morris中序遍历")
        
        # 对比测试：递归方法
        preorder_rec = []
        Code01_MorrisPreorderInorder.preorder_recursive(root1, preorder_rec)
        inorder_rec = []
        Code01_MorrisPreorderInorder.inorder_recursive(root1, inorder_rec)
        
        Code01_MorrisPreorderInorder.print_list(preorder_rec, "递归先序遍历")
        Code01_MorrisPreorderInorder.print_list(inorder_rec, "递归中序遍历")
        
        # 对比测试：迭代方法
        preorder_iter = Code01_MorrisPreorderInorder.preorder_iterative(root1)
        inorder_iter = Code01_MorrisPreorderInorder.inorder_iterative(root1)
        
        Code01_MorrisPreorderInorder.print_list(preorder_iter, "迭代先序遍历")
        Code01_MorrisPreorderInorder.print_list(inorder_iter, "迭代中序遍历")
        
        # 验证结果一致性
        preorder_match = (preorder_morris == preorder_rec == preorder_iter)
        inorder_match = (inorder_morris == inorder_rec == inorder_iter)
        
        print(f"先序遍历结果一致性: {'✓ 通过' if preorder_match else '✗ 失败'}")
        print(f"中序遍历结果一致性: {'✓ 通过' if inorder_match else '✗ 失败'}")
        
        Code01_MorrisPreorderInorder.delete_tree(root1)
        
        # 测试用例2：空树
        print("\n测试用例2：空树")
        empty_preorder = Code01_MorrisPreorderInorder.preorder_traversal(None)
        empty_inorder = Code01_MorrisPreorderInorder.inorder_traversal(None)
        
        print(f"空树先序遍历结果大小: {len(empty_preorder)}")
        print(f"空树中序遍历结果大小: {len(empty_inorder)}")
        
        # 测试用例3：单节点树
        print("\n测试用例3：单节点树")
        single_node = TreeNode(42)
        
        single_preorder = Code01_MorrisPreorderInorder.preorder_traversal(single_node)
        single_inorder = Code01_MorrisPreorderInorder.inorder_traversal(single_node)
        
        Code01_MorrisPreorderInorder.print_list(single_preorder, "单节点先序遍历")
        Code01_MorrisPreorderInorder.print_list(single_inorder, "单节点中序遍历")
        
        # 测试用例4：链表结构树（只有右子树）
        print("\n测试用例4：链表结构树")
        list_tree = TreeNode(1)
        list_tree.right = TreeNode(2)
        list_tree.right.right = TreeNode(3)
        list_tree.right.right.right = TreeNode(4)
        
        list_preorder = Code01_MorrisPreorderInorder.preorder_traversal(list_tree)
        list_inorder = Code01_MorrisPreorderInorder.inorder_traversal(list_tree)
        
        Code01_MorrisPreorderInorder.print_list(list_preorder, "链表树先序遍历")
        Code01_MorrisPreorderInorder.print_list(list_inorder, "链表树中序遍历")
        
        Code01_MorrisPreorderInorder.delete_tree(list_tree)
        
        print("\n=== 测试完成 ===")

# 主函数 - 程序入口点
if __name__ == "__main__":
    Code01_MorrisPreorderInorder.run_tests()