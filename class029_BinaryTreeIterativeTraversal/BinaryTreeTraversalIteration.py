# -*- coding: utf-8 -*-
"""
二叉树遍历的迭代实现
包括前序、中序、后序和层序遍历的非递归实现
"""

from typing import List, Optional
from collections import deque


class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


def preorder_traversal(root: Optional[TreeNode]) -> List[int]:
    """
    前序遍历 - 非递归实现
    
    算法思路：
    1. 使用栈来模拟递归过程
    2. 先访问根节点，然后依次访问右子树和左子树
    3. 由于栈是后进先出，所以先压入右子树，再压入左子树
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(h) - h为树的高度，最坏情况下为O(n)
    """
    if not root:
        return []
    
    result = []
    stack = [root]
    
    while stack:
        node = stack.pop()
        result.append(node.val)
        
        # 先压入右子树，再压入左子树
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)
    
    return result

# ==================== 以下是新增的补充题目 ====================

"""
题目：LeetCode 112 - 路径总和
题目来源：https://leetcode.cn/problems/path-sum/
题目描述：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
如果存在，返回 true ；否则，返回 false 。

解题思路：
1. 使用深度优先搜索（DFS）遍历二叉树
2. 从根节点开始，每次遍历到一个节点时，将当前累计和减去节点值
3. 如果到达叶子节点且累计和为0，则返回true
4. 否则继续递归遍历左右子树

时间复杂度：O(n) - 需要遍历树中的所有节点
空间复杂度：O(h) - 递归调用栈的深度，h为树的高度
是否为最优解：是，DFS是解决此类路径问题的最优方法
"""
def has_path_sum(root, target_sum):
    if not root:
        return False
    
    # 非递归DFS实现 - 使用栈同时存储节点和当前路径和
    stack = [(root, target_sum - root.val)]
    
    while stack:
        node, remaining_sum = stack.pop()
        
        # 如果是叶子节点且剩余和为0，找到符合条件的路径
        if not node.left and not node.right and remaining_sum == 0:
            return True
        
        # 先压入右子节点，这样保证左子节点先被处理（DFS顺序）
        if node.right:
            stack.append((node.right, remaining_sum - node.right.val))
        if node.left:
            stack.append((node.left, remaining_sum - node.left.val))
    
    return False

"""
题目：LeetCode 113 - 路径总和 II
题目来源：https://leetcode.cn/problems/path-sum-ii/
题目描述：给你二叉树的根节点 root 和一个整数目标和 targetSum ，
找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。

解题思路：
1. 使用回溯算法（深度优先搜索）
2. 维护一个当前路径列表，记录已经走过的节点值
3. 当到达叶子节点且路径和等于目标和时，将当前路径加入结果集
4. 否则继续递归搜索左右子树

时间复杂度：O(n²) - 每个节点访问一次，最坏情况下需要将路径复制n次
空间复杂度：O(h) - 递归调用栈和路径列表的空间，h为树的高度
是否为最优解：是，回溯是寻找所有路径的标准方法
"""
def path_sum(root, target_sum):
    result = []
    if not root:
        return result
    
    # 非递归DFS实现
    node_stack = [(root, root.val)]
    path_stack = [[root.val]]
    
    while node_stack:
        node, current_sum = node_stack.pop()
        current_path = path_stack.pop()
        
        # 如果是叶子节点且和等于目标值，加入结果集
        if not node.left and not node.right and current_sum == target_sum:
            result.append(current_path)
        
        # 先处理右子树（栈是后进先出，所以右子树先入栈）
        if node.right:
            right_sum = current_sum + node.right.val
            node_stack.append((node.right, right_sum))
            
            right_path = current_path.copy()
            right_path.append(node.right.val)
            path_stack.append(right_path)
        
        # 再处理左子树
        if node.left:
            left_sum = current_sum + node.left.val
            node_stack.append((node.left, left_sum))
            
            left_path = current_path.copy()
            left_path.append(node.left.val)
            path_stack.append(left_path)
    
    return result

"""
题目：LeetCode 129 - 求根节点到叶节点数字之和
题目来源：https://leetcode.cn/problems/sum-root-to-leaf-numbers/
题目描述：给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
每条从根节点到叶节点的路径都代表一个数字。
例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
计算从根节点到叶节点生成的 所有数字之和 。

解题思路：
1. 使用深度优先搜索遍历二叉树
2. 维护一个当前路径代表的数字
3. 当到达叶子节点时，将当前数字加入总和

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - 递归栈的深度
是否为最优解：是，DFS是解决此类路径问题的高效方法
"""
def sum_numbers(root):
    if not root:
        return 0
    
    # 非递归DFS实现
    stack = [(root, root.val)]
    total_sum = 0
    
    while stack:
        node, current_number = stack.pop()
        
        # 如果是叶子节点，将当前数字加入总和
        if not node.left and not node.right:
            total_sum += current_number
        else:
            # 非叶子节点，继续向下遍历
            if node.right:
                stack.append((node.right, current_number * 10 + node.right.val))
            if node.left:
                stack.append((node.left, current_number * 10 + node.left.val))
    
    return total_sum

"""
题目：LeetCode 257 - 二叉树的所有路径
题目来源：https://leetcode.cn/problems/binary-tree-paths/
题目描述：给你一个二叉树的根节点 root ，按 任意顺序 ，
返回所有从根节点到叶子节点的路径。

解题思路：
1. 使用回溯算法（DFS）
2. 维护当前路径字符串
3. 当到达叶子节点时，将完整路径加入结果集
4. 继续递归处理左右子树

时间复杂度：O(n) - 每个节点访问一次，路径字符串拼接可能需要O(n)时间
空间复杂度：O(h) - 递归栈的深度
是否为最优解：是，DFS是生成所有路径的标准方法
"""
def binary_tree_paths(root):
    result = []
    if not root:
        return result
    
    # 非递归DFS实现
    stack = [(root, str(root.val))]
    
    while stack:
        node, path = stack.pop()
        
        # 如果是叶子节点，将路径加入结果集
        if not node.left and not node.right:
            result.append(path)
        else:
            # 非叶子节点，继续向下遍历
            if node.right:
                stack.append((node.right, path + "->" + str(node.right.val)))
            if node.left:
                stack.append((node.left, path + "->" + str(node.left.val)))
    
    return result

"""
题目：LeetCode 1448 - 统计二叉树中好节点的数目
题目来源：https://leetcode.cn/problems/count-good-nodes-in-binary-tree/
题目描述：给你一棵根为 root 的二叉树，请你返回二叉树中好节点的数目。
「好节点」X 定义为：从根到该节点 X 所经过的节点中，没有任何节点的值大于 X 的值。

解题思路：
1. 使用深度优先搜索遍历二叉树
2. 维护从根到当前节点路径上的最大值
3. 如果当前节点的值大于等于该最大值，则为好节点，更新最大值
4. 继续递归处理左右子树

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - 递归栈的深度
是否为最优解：是，DFS是解决此类路径最大值问题的最优方法
"""
def good_nodes(root):
    if not root:
        return 0
    
    # 非递归DFS实现
    stack = [(root, root.val)]  # (节点, 路径最大值)
    good_count = 0
    
    while stack:
        node, max_so_far = stack.pop()
        
        # 判断是否为好节点
        if node.val >= max_so_far:
            good_count += 1
            max_so_far = node.val  # 更新路径最大值
        
        # 继续处理左右子树
        if node.right:
            stack.append((node.right, max_so_far))
        if node.left:
            stack.append((node.left, max_so_far))
    
    return good_count

"""
题目：剑指Offer 26 - 树的子结构
题目来源：https://leetcode.cn/problems/shu-de-zi-jie-gou-lcof/
题目描述：输入两棵二叉树A和B，判断B是不是A的子结构。
约定空树不是任意一个树的子结构。

解题思路：
1. 先序遍历树A中的每个节点nA
2. 对于每个节点nA，检查以nA为根节点的子树是否包含树B
3. 检查是否包含的逻辑：递归比较节点值是否相等，左子树和右子树是否也满足条件

时间复杂度：O(m*n) - m和n分别是两棵树的节点数
空间复杂度：O(h) - 递归栈的深度，h为树A的高度
是否为最优解：是，需要遍历树A的每个节点并进行匹配
"""
def is_sub_structure(A, B):
    # 空树不是任意一个树的子结构
    if not A or not B:
        return False
    
    # 非递归DFS实现，遍历树A的每个节点
    stack = [A]
    
    while stack:
        node = stack.pop()
        
        # 检查以当前节点为根的子树是否包含树B
        if is_match(node, B):
            return True
        
        # 继续遍历其他节点
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)
    
    return False

# 辅助方法：检查以A为根的子树是否包含以B为根的子树
def is_match(A, B):
    # 递归实现更清晰
    if not B:
        return True  # B已经匹配完
    if not A or A.val != B.val:
        return False  # A为空或值不匹配
    # 继续匹配左右子树
    return is_match(A.left, B.left) and is_match(A.right, B.right)

"""
题目：LeetCode 1372 - 二叉树中的最长交错路径
题目来源：https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
题目描述：给你一棵以 root 为根的二叉树，返回其最长的交错路径的长度。
交错路径的定义如下：从一个节点开始，沿着父-子连接，向上或向下移动，
移动时，节点的方向必须交替变化（即从左到右，或从右到左）。

解题思路：
1. 使用深度优先搜索遍历二叉树
2. 对每个节点，记录从上一个节点来的方向（左或右）
3. 如果当前方向与上一个方向交替，则路径长度+1，否则重置为1
4. 更新全局最大路径长度

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - 递归栈的深度
是否为最优解：是，一次遍历即可找到最长交错路径
"""
def longest_zig_zag(root):
    if not root:
        return 0
    
    max_length = 0
    
    # 非递归DFS实现，栈中存储三元组：(节点, 方向, 当前长度)
    # 方向：-1表示从父节点的左子树来，1表示从父节点的右子树来，0表示根节点
    stack = [(root, 0, 0)]
    
    while stack:
        node, direction, length = stack.pop()
        
        # 更新最大值
        max_length = max(max_length, length)
        
        # 处理左子树
        if node.left:
            new_length = length + 1 if direction == 1 else 1
            stack.append((node.left, -1, new_length))
        
        # 处理右子树
        if node.right:
            new_length = length + 1 if direction == -1 else 1
            stack.append((node.right, 1, new_length))
    
    return max_length

"""
题目：LeetCode 222 - 完全二叉树的节点个数
题目来源：https://leetcode.cn/problems/count-complete-tree-nodes/
题目描述：给你一棵 完全二叉树 的根节点 root ，求出该树的节点个数。
完全二叉树 的定义是：除了最底层节点可能没填满外，其余每层节点数都达到最大值，
并且最下面一层的节点都集中在该层最左边的若干位置。

解题思路：
1. 利用完全二叉树的特性：如果左子树的高度等于右子树的高度，则左子树是满二叉树
2. 如果左子树的高度大于右子树的高度，则右子树是满二叉树
3. 满二叉树的节点数为2^h - 1，其中h是树的高度
4. 递归计算剩余部分的节点数

时间复杂度：O(log²n) - 每次计算高度需要O(logn)，递归深度为O(logn)
空间复杂度：O(logn) - 递归栈的深度
是否为最优解：是，利用完全二叉树特性进行优化
"""
def count_nodes(root):
    if not root:
        return 0
    
    # 计算树的高度（从根到最左边叶子节点的距离）
    left_height = get_height(root.left)
    right_height = get_height(root.right)
    
    if left_height == right_height:
        # 左子树是满二叉树，节点数为2^left_height - 1，加上根节点和右子树
        return (1 << left_height) + count_nodes(root.right)
    else:
        # 右子树是满二叉树，节点数为2^right_height - 1，加上根节点和左子树
        return (1 << right_height) + count_nodes(root.left)

# 辅助方法：计算完全二叉树的高度（从根到最左边叶子节点的距离）
def get_height(node):
    height = 0
    while node:
        height += 1
        node = node.left
    return height


def inorder_traversal(root: Optional[TreeNode]) -> List[int]:
    """
    中序遍历 - 非递归实现
    
    算法思路：
    1. 使用栈来模拟递归过程
    2. 一直向左走到底，将路径上的节点都压入栈中
    3. 弹出栈顶节点并访问，然后转向右子树
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(h) - h为树的高度，最坏情况下为O(n)
    """
    result = []
    stack = []
    current = root
    
    while current or stack:
        if current:
            # 一直向左走到底
            stack.append(current)
            current = current.left
        else:
            # 处理栈顶节点
            current = stack.pop()
            result.append(current.val)
            # 转向右子树
            current = current.right
    
    return result


def postorder_traversal_two_stacks(root: Optional[TreeNode]) -> List[int]:
    """
    后序遍历 - 非递归实现（双栈法）
    
    算法思路：
    1. 使用两个栈
    2. 第一个栈用于遍历，第二个栈用于收集结果
    3. 先序遍历的变种：根->右->左，然后反转结果
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(h) - h为树的高度，最坏情况下为O(n)
    """
    if not root:
        return []
    
    result = []
    stack1 = [root]
    stack2 = []
    
    # 第一个栈用于遍历
    while stack1:
        node = stack1.pop()
        stack2.append(node)
        
        # 先压入左子树，再压入右子树
        if node.left:
            stack1.append(node.left)
        if node.right:
            stack1.append(node.right)
    
    # 从第二个栈中弹出元素即为后序遍历结果
    while stack2:
        result.append(stack2.pop().val)
    
    return result


def postorder_traversal_one_stack(root: Optional[TreeNode]) -> List[int]:
    """
    后序遍历 - 非递归实现（单栈法）
    
    算法思路：
    1. 使用一个栈和一个指针记录最近访问的节点
    2. 一直向左走到底，将路径上的节点都压入栈中
    3. 查看栈顶节点，如果右子树存在且未被访问过，则转向右子树
    4. 否则访问栈顶节点，并标记为已访问
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(h) - h为树的高度，最坏情况下为O(n)
    """
    if not root:
        return []
    
    result = []
    stack = []
    last_visited = None
    current = root
    
    while current or stack:
        if current:
            # 一直向左走到底
            stack.append(current)
            current = current.left
        else:
            # 查看栈顶节点
            peek_node = stack[-1]
            # 如果右子树存在且未被访问过
            if peek_node.right and last_visited != peek_node.right:
                current = peek_node.right
            else:
                # 访问栈顶节点
                result.append(peek_node.val)
                last_visited = peek_node
                stack.pop()
    
    return result


def level_order(root: Optional[TreeNode]) -> List[List[int]]:
    """
    层序遍历（广度优先遍历）
    
    算法思路：
    1. 使用队列进行广度优先搜索
    2. 每次处理一层的所有节点
    3. 将下一层的节点加入队列
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(w) - w为树的最大宽度，最坏情况下为O(n)
    """
    if not root:
        return []
    
    result = []
    queue = deque([root])
    
    while queue:
        level_size = len(queue)  # 当前层的节点数
        level_nodes = []  # 存储当前层的节点值
        
        # 处理当前层的所有节点
        for _ in range(level_size):
            node = queue.popleft()
            level_nodes.append(node.val)
            
            # 将下一层的节点加入队列
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(level_nodes)
    
    return result


def zigzag_level_order(root: Optional[TreeNode]) -> List[List[int]]:
    """
    锯齿形层序遍历
    
    算法思路：
    1. 类似层序遍历，但需要交替改变每层的遍历方向
    2. 使用一个布尔变量控制方向
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(w) - w为树的最大宽度，最坏情况下为O(n)
    """
    if not root:
        return []
    
    result = []
    queue = deque([root])
    left_to_right = True  # 控制遍历方向
    
    while queue:
        level_size = len(queue)
        level_nodes = [0] * level_size
        
        for i in range(level_size):
            node = queue.popleft()
            
            # 根据方向决定插入位置
            index = i if left_to_right else level_size - 1 - i
            level_nodes[index] = node.val
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(level_nodes)
        left_to_right = not left_to_right  # 切换方向
    
    return result


def max_depth(root: Optional[TreeNode]) -> int:
    """
    计算二叉树的最大深度
    
    算法思路：
    1. 使用层序遍历计算层数
    2. 每处理完一层，深度加1
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(w) - w为树的最大宽度，最坏情况下为O(n)
    """
    if not root:
        return 0
    
    queue = deque([root])
    depth = 0
    
    while queue:
        level_size = len(queue)
        # 处理当前层的所有节点
        for _ in range(level_size):
            node = queue.popleft()
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        depth += 1  # 每处理完一层，深度加1
    
    return depth


def invert_tree(root: Optional[TreeNode]) -> Optional[TreeNode]:
    """
    翻转二叉树
    
    算法思路：
    1. 使用层序遍历访问每个节点
    2. 交换每个节点的左右子树
    
    时间复杂度: O(n) - 每个节点访问一次
    空间复杂度: O(w) - w为树的最大宽度，最坏情况下为O(n)
    """
    if not root:
        return None
    
    queue = deque([root])
    
    while queue:
        node = queue.popleft()
        
        # 交换左右子树
        node.left, node.right = node.right, node.left
        
        # 将非空子节点加入队列
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)
    
    return root


def main():
    """测试函数"""
    # 构建测试二叉树:
    #       1
    #      / \
    #     2   3
    #    / \ / \
    #   4  5 6  7
    
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.left.right = TreeNode(5)
    root.right.left = TreeNode(6)
    root.right.right = TreeNode(7)
    
    print("=== 二叉树遍历测试 ===")
    
    # 前序遍历
    preorder = preorder_traversal(root)
    print(f"前序遍历: {' '.join(map(str, preorder))}")
    
    # 中序遍历
    inorder = inorder_traversal(root)
    print(f"中序遍历: {' '.join(map(str, inorder))}")
    
    # 后序遍历（双栈法）
    postorder1 = postorder_traversal_two_stacks(root)
    print(f"后序遍历(双栈法): {' '.join(map(str, postorder1))}")
    
    # 后序遍历（单栈法）
    postorder2 = postorder_traversal_one_stack(root)
    print(f"后序遍历(单栈法): {' '.join(map(str, postorder2))}")
    
    # 层序遍历
    levelorder = level_order(root)
    print(f"层序遍历: {' '.join([str(level) for level in levelorder])}")
    
    # 锯齿形层序遍历
    zigzag = zigzag_level_order(root)
    print(f"锯齿形层序遍历: {' '.join([str(level) for level in zigzag])}")
    
    # 二叉树的最大深度
    depth = max_depth(root)
    print(f"二叉树的最大深度: {depth}")
    
    # 翻转二叉树
    inverted = invert_tree(root)
    inverted_level_order = level_order(inverted)
    print(f"翻转后的层序遍历: {' '.join([str(level) for level in inverted_level_order])}")


if __name__ == "__main__":
    main()


# ==================== 以下是新增的补充题目 ====================

'''
题目1: LeetCode 107 - 二叉树的层序遍历 II
题目来源: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
题目描述:
给定一个二叉树，返回其节点值自底向上的层序遍历。 

解题思路:
1. 使用队列进行正常的层序遍历
2. 将每一层的结果添加到列表中
3. 最后将列表反转即可得到自底向上的结果

时间复杂度: O(n) - 需要遍历所有n个节点一次
空间复杂度: O(n) - 队列最多存储树的最大宽度(最坏情况下为n/2)
是否为最优解: 是
'''
def level_order_bottom(root: Optional[TreeNode]) -> List[List[int]]:
    if not root:
        return []
    
    result = []
    queue = deque([root])
    
    while queue:
        level_size = len(queue)
        level_nodes = []
        
        for _ in range(level_size):
            node = queue.popleft()
            level_nodes.append(node.val)
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(level_nodes)
    
    # 反转结果
    return result[::-1]


'''
题目2: LeetCode 637 - 二叉树的层平均值
题目来源: https://leetcode.cn/problems/average-of-levels-in-binary-tree/
题目描述:
给定一个非空二叉树的根节点 root , 以数组的形式返回每一层节点的平均值。

解题思路:
1. 使用层序遍历
2. 对每一层的节点值求和，然后除以节点数

时间复杂度: O(n)
空间复杂度: O(w), w为最大宽度
是否为最优解: 是
'''
def average_of_levels(root: Optional[TreeNode]) -> List[float]:
    if not root:
        return []
    
    result = []
    queue = deque([root])
    
    while queue:
        level_size = len(queue)
        level_sum = 0
        
        for _ in range(level_size):
            node = queue.popleft()
            level_sum += node.val
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(level_sum / level_size)
    
    return result


'''
题目3: LeetCode 515 - 在每个树行中找最大值
题目来源: https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
题目描述:
给定一棵二叉树的根节点 root ，请找出存在于每一层的最大值。

解题思路:
1. 使用层序遍历
2. 对每一层记录最大值

时间复杂度: O(n)
空间复杂度: O(w)
是否为最优解: 是
'''
def largest_values(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    
    result = []
    queue = deque([root])
    
    while queue:
        level_size = len(queue)
        max_val = float('-inf')  # 初始化为最小值
        
        for _ in range(level_size):
            node = queue.popleft()
            max_val = max(max_val, node.val)
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(int(max_val))
    
    return result


'''
题目4: LeetCode 513 - 找树左下角的值
题目来源: https://leetcode.cn/problems/find-bottom-left-tree-value/
题目描述:
给定一个二叉树的根节点 root，请找出该二叉树的 最底层 最左边 节点的值。

解题思路:
1. 使用层序遍历
2. 记录每一层的第一个节点
3. 最后一层的第一个节点就是答案

时间复杂度: O(n)
空间复杂度: O(w)
是否为最优解: 是
'''
def find_bottom_left_value(root: Optional[TreeNode]) -> int:
    queue = deque([root])
    leftmost = root.val
    
    while queue:
        level_size = len(queue)
        
        for i in range(level_size):
            node = queue.popleft()
            
            if i == 0:  # 记录每一层的第一个节点
                leftmost = node.val
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
    
    return leftmost


'''
题目5: LeetCode 662 - 二叉树最大宽度
题目来源: https://leetcode.cn/problems/maximum-width-of-binary-tree/
题目描述:
给定一个二叉树，编写一个函数来获取这个树的最大宽度。
树的宽度是所有层中节点的最大数量。

解题思路:
1. 使用层序遍历，为每个节点编号
2. 每一层的宽度 = 最右边节点编号 - 最左边节点编号 + 1
3. 左子节点编号 = 父节点编号 * 2
4. 右子节点编号 = 父节点编号 * 2 + 1

时间复杂度: O(n)
空间复杂度: O(w)
是否为最优解: 是
'''
def width_of_binary_tree(root: Optional[TreeNode]) -> int:
    if not root:
        return 0
    
    queue = deque([(root, 0)])  # (node, id)
    max_width = 0
    
    while queue:
        level_size = len(queue)
        _, left = queue[0]  # 当前层最左边节点的编号
        
        for i in range(level_size):
            node, node_id = queue.popleft()
            
            if node.left:
                queue.append((node.left, node_id * 2))
            if node.right:
                queue.append((node.right, node_id * 2 + 1))
        
        # 计算当前层的宽度
        max_width = max(max_width, node_id - left + 1)
    
    return max_width