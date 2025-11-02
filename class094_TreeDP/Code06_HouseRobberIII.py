# 337. 打家劫舍 III
# 测试链接 : https://leetcode.cn/problems/house-robber-iii/

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    # 提交如下的方法
    # 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    # 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    # 是否为最优解: 是，这是解决树形DP问题的标准方法
    def rob(self, root: TreeNode) -> int:
        # 调用递归函数，返回包含两个值的元组
        # 第一个值表示不抢劫当前节点时的最大收益
        # 第二个值表示抢劫当前节点时的最大收益
        result = self.robHelper(root)
        # 返回两种情况的最大值
        return max(result[0], result[1])

    # 递归函数返回一个长度为2的元组
    # result[0] 表示不抢劫当前节点时的最大收益
    # result[1] 表示抢劫当前节点时的最大收益
    def robHelper(self, node: TreeNode) -> tuple:
        # 基础情况：如果节点为空，返回(0, 0)
        if not node:
            return (0, 0)

        # 递归计算左右子树的结果
        left = self.robHelper(node.left) if node.left else (0, 0)
        right = self.robHelper(node.right) if node.right else (0, 0)

        # 计算当前节点的两种情况
        # 1. 不抢劫当前节点：左右子树可以自由选择是否抢劫
        not_rob = max(left[0], left[1]) + max(right[0], right[1])
        
        # 2. 抢劫当前节点：左右子节点都不能抢劫
        do_rob = node.val + left[0] + right[0]

        # 返回结果
        return (not_rob, do_rob)

# 补充题目1: 1372. 二叉树中的最长交错路径
# 题目链接: https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
# 题目描述: 给定一棵二叉树，找到最长的交错路径的长度。
# 交错路径定义为：从根节点到任意叶子节点，路径上的节点交替经过左子节点和右子节点。
def longest_zigzag(root: TreeNode) -> int:
    if not root:
        return 0
    
    max_length = [0]  # 使用列表作为可变对象存储最大值
    
    def dfs(node, length, is_left):
        if not node:
            return
        
        max_length[0] = max(max_length[0], length)
        
        if is_left:
            # 如果当前是左子节点，下一步应该走右子节点
            dfs(node.right, length + 1, False)
            # 也可以重新开始计算
            dfs(node.left, 1, True)
        else:
            # 如果当前是右子节点，下一步应该走左子节点
            dfs(node.left, length + 1, True)
            # 也可以重新开始计算
            dfs(node.right, 1, False)
    
    dfs(root.left, 1, True)  # 从左子节点开始，方向为左
    dfs(root.right, 1, False)  # 从右子节点开始，方向为右
    
    return max_length[0]

# 补充题目2: 549. 二叉树中最长的连续序列
# 题目链接: https://leetcode.cn/problems/binary-tree-longest-consecutive-sequence-ii/
# 题目描述: 给定一棵二叉树，找出最长连续序列路径的长度。这个路径可以是升序也可以是降序。
def longest_consecutive2(root: TreeNode) -> int:
    max_length = [0]  # 使用列表作为可变对象存储最大值
    
    def dfs(node):
        if not node:
            return (0, 0)  # (递增序列长度, 递减序列长度)
        
        inc = 1  # 递增序列长度，初始为1（包含自己）
        dec = 1  # 递减序列长度，初始为1（包含自己）
        
        if node.left:
            left_inc, left_dec = dfs(node.left)
            if node.val == node.left.val + 1:
                # 当前节点比左子节点大1，递减序列
                dec = left_dec + 1
            elif node.val == node.left.val - 1:
                # 当前节点比左子节点小1，递增序列
                inc = left_inc + 1
        
        if node.right:
            right_inc, right_dec = dfs(node.right)
            if node.val == node.right.val + 1:
                # 当前节点比右子节点大1，递减序列
                dec = max(dec, right_dec + 1)
            elif node.val == node.right.val - 1:
                # 当前节点比右子节点小1，递增序列
                inc = max(inc, right_inc + 1)
        
        # 更新全局最长长度：可以是从该节点开始的递增或递减序列，或者经过该节点的序列（inc + dec - 1）
        max_length[0] = max(max_length[0], inc + dec - 1)
        
        return (inc, dec)
    
    dfs(root)
    return max_length[0]

# 补充题目3: 1457. 二叉树中的伪回文路径
# 题目链接: https://leetcode.cn/problems/pseudo-palindromic-paths-in-a-binary-tree/
# 题目描述: 给一棵二叉树，统计从根到叶子节点的所有路径中，伪回文路径的数量。
# 伪回文路径定义为：路径上的节点值可以重新排列形成一个回文串。
def pseudo_palindromic_paths(root: TreeNode) -> int:
    # 初始化计数数组，存储每个数字出现的次数
    count = [0] * 10
    
    def is_pseudo_palindrome():
        odd_count = 0
        for c in count:
            if c % 2 != 0:
                odd_count += 1
                # 伪回文最多只能有一个奇数次数
                if odd_count > 1:
                    return False
        return True
    
    def dfs(node):
        if not node:
            return 0
        
        # 增加当前节点值的计数
        count[node.val] += 1
        
        result = 0
        if not node.left and not node.right:
            # 叶子节点，检查是否是伪回文路径
            result = 1 if is_pseudo_palindrome() else 0
        else:
            # 非叶子节点，继续递归
            result = dfs(node.left) + dfs(node.right)
        
        # 回溯，减少当前节点值的计数
        count[node.val] -= 1
        
        return result
    
    return dfs(root)

# 补充题目4: 2246. 相邻字符不同的最长路径
# 题目链接: https://leetcode.cn/problems/longest-path-with-different-adjacent-characters/
# 题目描述: 给一棵树，每个节点有一个字符，找到最长的路径，使得路径上相邻节点的字符不同。
def longest_path(parent, s: str) -> int:
    n = len(parent)
    # 构建邻接表
    adj = [[] for _ in range(n)]
    for i in range(1, n):
        adj[parent[i]].append(i)
        adj[i].append(parent[i])  # 无向树
    
    max_length = [0]  # 使用列表作为可变对象存储最大值
    
    def dfs(node, parent_node):
        first_max = 0
        second_max = 0
        
        for neighbor in adj[node]:
            if neighbor == parent_node:
                continue
            
            current_length = dfs(neighbor, node)
            
            # 如果相邻节点字符不同，才能继续路径
            if s[neighbor] != s[node]:
                if current_length > first_max:
                    second_max = first_max
                    first_max = current_length
                elif current_length > second_max:
                    second_max = current_length
        
        # 更新全局最长路径：可能是通过当前节点的两条最长路径之和
        max_length[0] = max(max_length[0], first_max + second_max + 1)
        
        # 返回从当前节点开始的最长路径长度
        return first_max + 1
    
    dfs(0, -1)
    return max_length[0]