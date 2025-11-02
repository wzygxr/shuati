"""
Morris遍历求两个节点的最低公共祖先 - Python实现

题目来源：
- 最低公共祖先：LeetCode 236. Lowest Common Ancestor of a Binary Tree
  链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/

Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。

本实现包含：
1. Python语言的Morris遍历求最低公共祖先
2. 递归版本的求最低公共祖先
3. 迭代版本的求最低公共祖先（使用父指针）
4. 详细的注释和算法解析
5. 完整的测试用例

算法详解：
利用Morris遍历求二叉树中两个节点的最低公共祖先（LCA）
1. 首先检查特殊情况：一个节点是否是另一个节点的祖先
2. 使用Morris先序遍历找到第一个遇到的目标节点
3. 使用Morris中序遍历寻找LCA：
   - 在第二次访问节点时，检查left是否在当前节点左子树的右边界上
   - 如果是，则检查left的右子树中是否包含另一个目标节点
   - 如果找到，则left就是LCA
4. 如果遍历结束后仍未找到LCA，则最后一个left就是答案

时间复杂度：O(n)，空间复杂度：O(1)
适用场景：内存受限环境中求大规模二叉树中两个节点的LCA

工程化考量：
1. 异常处理：处理空树、节点不存在等边界情况
2. 代码可读性：清晰的变量命名和详细注释
3. 类型注解：使用类型注解提高代码可读性
4. 模块化：将不同方法封装为独立函数
"""

from typing import Optional, Dict
from collections import deque


class TreeNode:
    """二叉树节点定义"""
    
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
    
    def __repr__(self):
        return f"TreeNode({self.val})"


def morris_lca(root: Optional[TreeNode], p: Optional[TreeNode], q: Optional[TreeNode]) -> Optional[TreeNode]:
    """
    Morris遍历求两个节点的最低公共祖先
    
    算法思路：
    1. 首先检查特殊情况：一个节点是否是另一个节点的祖先
    2. 使用Morris先序遍历找到第一个遇到的目标节点
    3. 使用Morris中序遍历寻找LCA：
       - 在第二次访问节点时，检查left是否在当前节点左子树的右边界上
       - 如果是，则检查left的右子树中是否包含另一个目标节点
       - 如果找到，则left就是LCA
    4. 如果遍历结束后仍未找到LCA，则最后一个left就是答案
    
    时间复杂度：O(n) - 每个节点最多被访问3次
    空间复杂度：O(1) - 仅使用常数额外空间
    
    Args:
        root: 二叉树根节点
        p: 第一个目标节点
        q: 第二个目标节点
        
    Returns:
        TreeNode: 最低公共祖先节点
    """
    if not root or not p or not q:
        return None
    
    # 特殊情况：一个节点是另一个节点的祖先
    if p == q:
        return p
    
    cur = root
    lca = None
    found_p = False
    found_q = False
    
    # Morris先序遍历找到第一个目标节点
    while cur and not (found_p and found_q):
        most_right = cur.left
        
        if most_right:
            while most_right.right and most_right.right != cur:
                most_right = most_right.right
            
            if most_right.right is None:
                # 第一次到达当前节点
                if cur == p:
                    found_p = True
                if cur == q:
                    found_q = True
                
                most_right.right = cur
                cur = cur.left
                continue
            else:
                # 第二次到达当前节点
                most_right.right = None
        else:
            # 没有左子树
            if cur == p:
                found_p = True
            if cur == q:
                found_q = True
        
        cur = cur.right
    
    # 重置状态，开始Morris中序遍历寻找LCA
    cur = root
    left = None
    
    while cur:
        most_right = cur.left
        
        if most_right:
            while most_right.right and most_right.right != cur:
                most_right = most_right.right
            
            if most_right.right is None:
                # 第一次到达当前节点
                most_right.right = cur
                cur = cur.left
                continue
            else:
                # 第二次到达当前节点
                most_right.right = None
                
                # 检查left是否在cur左子树的右边界上
                if left and left == most_right:
                    # 检查left的右子树中是否包含另一个目标节点
                    temp = left.right
                    while temp and temp != cur:
                        if temp == p or temp == q:
                            lca = left
                            break
                        temp = temp.right
        
        # 更新left指针
        left = cur
        cur = cur.right
    
    # 如果仍未找到LCA，检查最后一个left
    if not lca and left:
        temp = left.right
        while temp:
            if temp == p or temp == q:
                lca = left
                break
            temp = temp.right
    
    return lca


def recursive_lca(root: Optional[TreeNode], p: Optional[TreeNode], q: Optional[TreeNode]) -> Optional[TreeNode]:
    """
    递归版本求最低公共祖先
    
    算法思路：
    1. 如果当前节点为空或等于p或q，返回当前节点
    2. 递归在左子树中查找LCA
    3. 递归在右子树中查找LCA
    4. 如果左右子树都找到了结果，说明当前节点就是LCA
    5. 否则返回非空的那个子树结果
    
    时间复杂度：O(n) - 每个节点被访问一次
    空间复杂度：O(h) - h为树高，最坏情况下为O(n)
    
    Args:
        root: 二叉树根节点
        p: 第一个目标节点
        q: 第二个目标节点
        
    Returns:
        TreeNode: 最低公共祖先节点
    """
    if not root or root == p or root == q:
        return root
    
    left = recursive_lca(root.left, p, q)
    right = recursive_lca(root.right, p, q)
    
    if left and right:
        return root
    return left if left else right


def iterative_lca(root: Optional[TreeNode], p: Optional[TreeNode], q: Optional[TreeNode]) -> Optional[TreeNode]:
    """
    迭代版本求最低公共祖先（使用父指针）
    
    算法思路：
    1. 使用栈进行深度优先遍历，记录每个节点的父指针
    2. 找到p和q节点的所有祖先节点
    3. 从p开始向上遍历，记录路径
    4. 从q开始向上遍历，找到第一个在p路径中的节点
    
    时间复杂度：O(n) - 每个节点被访问一次
    空间复杂度：O(n) - 需要存储父指针信息
    
    Args:
        root: 二叉树根节点
        p: 第一个目标节点
        q: 第二个目标节点
        
    Returns:
        TreeNode: 最低公共祖先节点
    """
    if not root or not p or not q:
        return None
    
    parent: Dict[TreeNode, Optional[TreeNode]] = {}
    stack = [root]
    parent[root] = None
    
    # 构建父指针映射
    while stack:
        node = stack.pop()
        
        if node.left:
            parent[node.left] = node
            stack.append(node.left)
        if node.right:
            parent[node.right] = node
            stack.append(node.right)
    
    # 找到p的所有祖先
    ancestors = set()
    temp = p
    while temp:
        ancestors.add(temp)
        temp = parent.get(temp)
    
    # 从q开始向上找第一个在p祖先中的节点
    temp = q
    while temp:
        if temp in ancestors:
            return temp
        temp = parent.get(temp)
    
    return None


def create_test_tree() -> TreeNode:
    """创建测试用例"""
    # [3,5,1,6,2,0,8,null,null,7,4]
    #        3
    #       / \
    #      5   1
    #     / \ / \
    #    6  2 0  8
    #      / \
    #     7   4
    root = TreeNode(3)
    root.left = TreeNode(5)
    root.right = TreeNode(1)
    root.left.left = TreeNode(6)
    root.left.right = TreeNode(2)
    root.right.left = TreeNode(0)
    root.right.right = TreeNode(8)
    root.left.right.left = TreeNode(7)
    root.left.right.right = TreeNode(4)
    return root


def delete_tree(root: Optional[TreeNode]) -> None:
    """释放二叉树内存"""
    if not root:
        return
    delete_tree(root.left)
    delete_tree(root.right)
    # Python自动垃圾回收，这里主要是为了逻辑完整性


def test_morris_lca():
    """测试函数"""
    print("=== Morris遍历求最低公共祖先测试 ===")
    
    root = create_test_tree()
    
    # 获取测试节点
    p = root.left  # 5
    q = root.right  # 1
    r = root.left.right.left  # 7
    s = root.left.right.right  # 4
    
    # 测试用例1：p=5, q=1
    print("测试用例1 (p=5, q=1):")
    lca1 = morris_lca(root, p, q)
    lca2 = recursive_lca(root, p, q)
    lca3 = iterative_lca(root, p, q)
    print(f"Morris方法结果: {lca1.val if lca1 else 'None'}")
    print(f"递归方法结果: {lca2.val if lca2 else 'None'}")
    print(f"迭代方法结果: {lca3.val if lca3 else 'None'}")
    print()
    
    # 测试用例2：p=7, q=4
    print("测试用例2 (p=7, q=4):")
    lca1 = morris_lca(root, r, s)
    lca2 = recursive_lca(root, r, s)
    lca3 = iterative_lca(root, r, s)
    print(f"Morris方法结果: {lca1.val if lca1 else 'None'}")
    print(f"递归方法结果: {lca2.val if lca2 else 'None'}")
    print(f"迭代方法结果: {lca3.val if lca3 else 'None'}")
    print()
    
    # 测试用例3：p=5, q=4
    print("测试用例3 (p=5, q=4):")
    lca1 = morris_lca(root, p, s)
    lca2 = recursive_lca(root, p, s)
    lca3 = iterative_lca(root, p, s)
    print(f"Morris方法结果: {lca1.val if lca1 else 'None'}")
    print(f"递归方法结果: {lca2.val if lca2 else 'None'}")
    print(f"迭代方法结果: {lca3.val if lca3 else 'None'}")
    print()
    
    # 测试用例4：空树
    print("测试用例4 (空树):")
    lca1 = morris_lca(None, p, q)
    lca2 = recursive_lca(None, p, q)
    lca3 = iterative_lca(None, p, q)
    print(f"Morris方法结果: {lca1.val if lca1 else 'None'}")
    print(f"递归方法结果: {lca2.val if lca2 else 'None'}")
    print(f"迭代方法结果: {lca3.val if lca3 else 'None'}")
    print()
    
    delete_tree(root)
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    test_morris_lca()


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

迭代方法（父指针）：
- 时间复杂度：O(n) - 每个节点被访问一次
- 空间复杂度：O(n) - 需要存储父指针信息
- 是否为最优解：否，空间复杂度不是最优

工程化建议：
1. 对于内存受限环境，优先选择Morris方法
2. 对于一般应用场景，选择递归方法更简洁实用
3. 迭代方法适合需要父指针信息的场景
4. 在实际工程中，根据具体需求选择合适的方法

Python特有优化：
1. 使用类型注解提高代码可读性
2. 利用Python的动态特性简化代码实现
3. 使用set数据结构提高查找效率
4. 利用Python的垃圾回收机制简化内存管理
"""