"""
Morris遍历实现后序遍历 - Python版本

题目来源：
- 后序遍历：LeetCode 145. Binary Tree Postorder Traversal
  链接：https://leetcode.cn/problems/binary-tree-postorder-traversal/

Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。

本实现包含：
1. Python语言的Morris后序遍历
2. 递归版本的后序遍历
3. 迭代版本的后序遍历
4. 详细的注释和算法解析
5. 完整的测试用例

算法详解：
Morris后序遍历相对复杂，因为后序遍历的顺序是左->右->根，而线索化的过程是按照中序遍历的顺序进行的
核心技巧是在第二次访问节点时，先收集其左子树的右边界，最后再收集整棵树的右边界
1. 线索化过程与中序遍历类似
2. 在第二次访问节点时，收集左子树的右边界（逆序）
3. 最后收集整棵树的右边界（逆序）
4. 通过翻转右边界链表来实现逆序收集

时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
空间复杂度：O(1) - 不考虑返回值的空间占用
适用场景：内存受限环境、需要后序遍历的大规模二叉树

优缺点分析：
- 优点：空间复杂度最优，适用于内存极度受限的环境
- 缺点：实现最为复杂，需要多次翻转链表，常数因子较大

运行命令：python Code02_MorrisPostorder.py
"""

from typing import List, Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Code02_MorrisPostorder:
    """
    Morris后序遍历算法实现类
    """
    
    @staticmethod
    def postorder_traversal(root: Optional[TreeNode]) -> List[int]:
        """
        Morris遍历实现后序遍历
        
        后序遍历顺序：左-右-根
        在Morris遍历中的实现：
        - 在第二次访问节点时，收集其左子树的右边界（逆序）
        - 最后收集整棵树的右边界（逆序）
        - 通过翻转链表来实现逆序收集
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            List[int]: 后序遍历的节点值列表
            
        时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
        空间复杂度：O(1) - 不考虑返回值的空间占用
        
        算法步骤：
        1. 初始化当前节点cur为根节点
        2. 当cur不为None时：
           a. 如果cur没有左子树，cur移动到其右子树
           b. 如果cur有左子树：
              i. 找到cur左子树的最右节点most_right
              ii. 如果most_right的right指针为None（第一次访问cur）：
                  - 将most_right的right指向cur
                  - cur移动到其左子树
              iii. 如果most_right的right指针指向cur（第二次访问cur）：
                  - 将most_right的right恢复为None
                  - 收集cur左子树的右边界（逆序）
                  - cur移动到其右子树
        3. 最后收集整棵树的右边界（逆序）
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
                    
                    # 收集cur左子树的右边界（逆序）
                    Code02_MorrisPostorder.collect_right_edge(cur.left, result)
            
            cur = cur.right  # 移动到右子树
        
        # 最后收集整棵树的右边界（逆序）
        Code02_MorrisPostorder.collect_right_edge(root, result)
        
        return result
    
    @staticmethod
    def collect_right_edge(node: Optional[TreeNode], result: List[int]) -> None:
        """
        收集右边界节点（逆序）
        
        Args:
            node: 起始节点
            result: 存储结果的列表
            
        时间复杂度：O(k) - k为右边界长度
        空间复杂度：O(1)
        """
        if node is None:
            return
        
        # 先收集右边界（正序）
        temp = []
        cur = node
        while cur is not None:
            temp.append(cur.val)
            cur = cur.right
        
        # 逆序添加到结果中
        temp.reverse()
        result.extend(temp)
    
    @staticmethod
    def postorder_recursive(root: Optional[TreeNode], result: List[int]) -> None:
        """
        递归实现后序遍历（对比参考）
        
        Args:
            root: 二叉树的根节点
            result: 存储遍历结果的列表
            
        时间复杂度：O(n) - 每个节点访问一次
        空间复杂度：O(h) - h为树高，最坏情况下为O(n)
        """
        if root is None:
            return
        Code02_MorrisPostorder.postorder_recursive(root.left, result)   # 遍历左子树
        Code02_MorrisPostorder.postorder_recursive(root.right, result)  # 遍历右子树
        result.append(root.val)              # 访问根节点
    
    @staticmethod
    def postorder_iterative(root: Optional[TreeNode]) -> List[int]:
        """
        迭代实现后序遍历（对比参考）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            List[int]: 后序遍历的节点值列表
            
        时间复杂度：O(n) - 每个节点访问一次
        空间复杂度：O(h) - h为树高，最坏情况下为O(n)
        """
        result = []
        if root is None:
            return result
        
        stack = []
        prev = None
        cur = root
        
        while cur is not None or stack:
            # 一直向左遍历，直到叶子节点
            while cur is not None:
                stack.append(cur)
                cur = cur.left
            
            cur = stack[-1]
            
            # 如果右子树为空或已经访问过
            if cur.right is None or cur.right == prev:
                result.append(cur.val)
                stack.pop()
                prev = cur
                cur = None
            else:
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
        Code02_MorrisPostorder.delete_tree(root.left)
        Code02_MorrisPostorder.delete_tree(root.right)
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
        print("=== Morris后序遍历算法测试 ===")
        
        # 测试用例1：常规二叉树
        print("\n测试用例1：常规二叉树")
        root1 = Code02_MorrisPostorder.create_test_tree()
        
        postorder_morris = Code02_MorrisPostorder.postorder_traversal(root1)
        Code02_MorrisPostorder.print_list(postorder_morris, "Morris后序遍历")
        
        # 对比测试：递归方法
        postorder_rec = []
        Code02_MorrisPostorder.postorder_recursive(root1, postorder_rec)
        Code02_MorrisPostorder.print_list(postorder_rec, "递归后序遍历")
        
        # 对比测试：迭代方法
        postorder_iter = Code02_MorrisPostorder.postorder_iterative(root1)
        Code02_MorrisPostorder.print_list(postorder_iter, "迭代后序遍历")
        
        # 验证结果一致性
        postorder_match = (postorder_morris == postorder_rec == postorder_iter)
        print(f"后序遍历结果一致性: {'✓ 通过' if postorder_match else '✗ 失败'}")
        
        Code02_MorrisPostorder.delete_tree(root1)
        
        # 测试用例2：空树
        print("\n测试用例2：空树")
        empty_postorder = Code02_MorrisPostorder.postorder_traversal(None)
        print(f"空树后序遍历结果大小: {len(empty_postorder)}")
        
        # 测试用例3：单节点树
        print("\n测试用例3：单节点树")
        single_node = TreeNode(42)
        
        single_postorder = Code02_MorrisPostorder.postorder_traversal(single_node)
        Code02_MorrisPostorder.print_list(single_postorder, "单节点后序遍历")
        
        # 测试用例4：链表结构树（只有右子树）
        print("\n测试用例4：链表结构树")
        list_tree = TreeNode(1)
        list_tree.right = TreeNode(2)
        list_tree.right.right = TreeNode(3)
        list_tree.right.right.right = TreeNode(4)
        
        list_postorder = Code02_MorrisPostorder.postorder_traversal(list_tree)
        Code02_MorrisPostorder.print_list(list_postorder, "链表树后序遍历")
        
        Code02_MorrisPostorder.delete_tree(list_tree)
        
        # 测试用例5：只有左子树的树
        print("\n测试用例5：只有左子树的树")
        left_tree = TreeNode(1)
        left_tree.left = TreeNode(2)
        left_tree.left.left = TreeNode(3)
        left_tree.left.left.left = TreeNode(4)
        
        left_postorder = Code02_MorrisPostorder.postorder_traversal(left_tree)
        Code02_MorrisPostorder.print_list(left_postorder, "左子树树后序遍历")
        
        Code02_MorrisPostorder.delete_tree(left_tree)
        
        print("\n=== 测试完成 ===")

# 主函数 - 程序入口点
if __name__ == "__main__":
    Code02_MorrisPostorder.run_tests()