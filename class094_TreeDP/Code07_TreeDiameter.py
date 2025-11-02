# 543. 二叉树的直径
# 测试链接 : https://leetcode.cn/problems/diameter-of-binary-tree/

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
    # 是否为最优解: 是，这是计算二叉树直径的标准方法
    def diameterOfBinaryTree(self, root: TreeNode) -> int:
        self.max_diameter = 0
        self.depth(root)
        return self.max_diameter

    # 计算以node为根的子树的深度
    # 在计算过程中更新最大直径
    def depth(self, node: TreeNode) -> int:
        # 基础情况：空节点的深度为0
        if not node:
            return 0

        # 递归计算左右子树的深度
        left_depth = self.depth(node.left) if node.left else 0
        right_depth = self.depth(node.right) if node.right else 0

        # 更新最大直径：左子树深度 + 右子树深度
        self.max_diameter = max(self.max_diameter, left_depth + right_depth)

        # 返回当前节点的深度：左右子树深度的最大值 + 1
        return max(left_depth, right_depth) + 1

# 补充题目1: 1245. 树的直径（N叉树/无向树版本）
# 题目链接: https://leetcode.cn/problems/tree-diameter/
# 题目描述: 给一棵无向树，找到树中最长路径的长度。
# 注意：树中的最长路径可能不经过根节点，这与二叉树的直径定义相同。
def tree_diameter(edges):
    if not edges:
        return 0
    
    # 构建邻接表表示的图
    graph = {}
    for u, v in edges:
        if u not in graph:
            graph[u] = []
        if v not in graph:
            graph[v] = []
        graph[u].append(v)
        graph[v].append(u)
    
    # 第一次DFS找到离任意节点最远的节点
    def dfs(parent, node, max_distance):
        result = [node, 0]  # [最远节点, 距离]
        
        for neighbor in graph[node]:
            if neighbor != parent:
                max_distance[0] = 0  # 重置距离计数
                current = dfs(node, neighbor, max_distance)
                distance = max_distance[0] + 1
                
                if distance > result[1]:
                    result[0] = current[0]
                    result[1] = distance
        
        max_distance[0] = result[1]
        return result
    
    max_distance = [0]
    first = dfs(-1, 0, max_distance)
    # 第二次DFS从最远节点出发找到真正的最长路径
    dfs(-1, first[0], max_distance)
    
    return max_distance[0]

# 补充题目2: 1522. N叉树的直径
# 题目链接: https://leetcode.cn/problems/diameter-of-n-ary-tree/
# 题目描述: 给定一棵N叉树，找到树中最长路径的长度。
# 注意：这里的路径是两个节点之间的边数。
class Node:
    def __init__(self, val=None, children=None):
        self.val = val
        self.children = children if children is not None else []

class NaryTreeDiameter:
    def diameter(self, root: Node) -> int:
        self.max_diameter = 0
        if not root:
            return 0
        self.height(root)
        return self.max_diameter
    
    def height(self, node: Node) -> int:
        if not node:
            return 0
        
        first_max = 0
        second_max = 0
        for child in node.children:
            h = self.height(child)
            if h > first_max:
                second_max = first_max
                first_max = h
            elif h > second_max:
                second_max = h
        
        # 更新最大直径
        self.max_diameter = max(self.max_diameter, first_max + second_max)
        return first_max + 1

# 补充题目3: 687. 最长同值路径
# 题目链接: https://leetcode.cn/problems/longest-univalue-path/
# 题目描述: 给定一棵二叉树，找出最长的路径，该路径上的每个节点都具有相同的值。
# 注意：这条路径可以经过也可以不经过根节点。
class LongestUnivaluePath:
    def longestUnivaluePath(self, root: TreeNode) -> int:
        self.max_length = 0
        self.helper(root)
        return self.max_length
    
    def helper(self, node: TreeNode) -> int:
        if not node:
            return 0
        
        left_length = 0
        if node.left:
            left = self.helper(node.left)
            if node.left.val == node.val:
                left_length = left + 1
        
        right_length = 0
        if node.right:
            right = self.helper(node.right)
            if node.right.val == node.val:
                right_length = right + 1
        
        # 更新最长同值路径长度
        self.max_length = max(self.max_length, left_length + right_length)
        
        # 返回从当前节点出发的最长同值路径长度
        return max(left_length, right_length)

# 补充题目4: 2222. 选择建筑的方案数
# 题目链接: https://leetcode.cn/problems/number-of-ways-to-select-buildings/
# 题目描述: 给定一个二进制字符串s，找出所有满足以下条件的三元组(i, j, k)：
# i < j < k，且s[i], s[j], s[k] 构成交替序列（即 "010" 或 "101"）
def number_of_ways(s: str) -> int:
    # 0的总数，1的总数
    total0 = s.count('0')
    total1 = s.count('1')
    
    # 当前已遍历的0和1的数量
    count0 = 0
    count1 = 0
    result = 0
    
    for c in s:
        if c == '0':
            # 选择当前0作为中间节点，左边的1的数量乘以右边的1的数量
            result += count1 * (total1 - count1)
            count0 += 1
        else:
            # 选择当前1作为中间节点，左边的0的数量乘以右边的0的数量
            result += count0 * (total0 - count0)
            count1 += 1
    
    return result