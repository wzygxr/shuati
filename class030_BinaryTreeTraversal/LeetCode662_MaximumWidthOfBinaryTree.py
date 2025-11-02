from collections import deque
from typing import Optional

# LeetCode 662. 二叉树最大宽度
# 题目链接: https://leetcode.cn/problems/maximum-width-of-binary-tree/
# 题目大意: 给你一棵二叉树的根节点 root ，返回树的 最大宽度 。
# 树的 最大宽度 是所有层中最大的 宽度 。
# 每一层的 宽度 被定义为该层最左和最右的非空节点（即，两个端点）之间的长度。
# 将这个二叉树视作与满二叉树结构相同，两端点间会出现一些延伸到这一层的 null 节点，这些 null 节点也计入长度。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def widthOfBinaryTree1(self, root: Optional[TreeNode]) -> int:
        """
        方法1: 使用BFS层序遍历，给每个节点分配位置索引
        思路: 在完全二叉树中，如果父节点的位置是i，那么左子节点的位置是2*i，右子节点的位置是2*i+1
        时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
        空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
        """
        if not root:
            return 0
        
        # 使用队列存储节点及其位置索引
        queue = deque([(root, 1)])
        max_width = 0
        
        while queue:
            size = len(queue)
            left_index = queue[0][1]  # 当前层最左边节点的索引
            right_index = left_index  # 初始化为最左边节点的索引
            
            for _ in range(size):
                node, index = queue.popleft()
                right_index = index  # 更新最右边节点的索引
                
                # 添加子节点到队列
                if node.left:
                    queue.append((node.left, index * 2))
                if node.right:
                    queue.append((node.right, index * 2 + 1))
            
            # 计算当前层的宽度
            max_width = max(max_width, right_index - left_index + 1)
        
        return max_width
    
    def widthOfBinaryTree2(self, root: Optional[TreeNode]) -> int:
        """
        方法2: 优化的BFS，避免索引过大导致的整数溢出
        思路: 每层重新编号，将最左边节点的索引作为基准(1)，其他节点相对编号
        时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
        空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
        """
        if not root:
            return 0
        
        # 使用队列存储节点及其位置索引
        queue = deque([(root, 1)])
        max_width = 0
        
        while queue:
            size = len(queue)
            left_index = queue[0][1]  # 当前层最左边节点的索引
            
            for _ in range(size):
                node, index = queue.popleft()
                
                # 重新编号，避免索引过大
                normalized_index = index - left_index + 1
                
                # 添加子节点到队列
                if node.left:
                    queue.append((node.left, normalized_index * 2))
                if node.right:
                    queue.append((node.right, normalized_index * 2 + 1))
            
            # 计算当前层的宽度
            right_index = queue[-1][1] if queue else 1
            max_width = max(max_width, right_index)
        
        return max_width

# 测试代码
if __name__ == "__main__":
    # 构建测试二叉树: [1,3,2,5,3,null,9]
    #       1
    #      / \
    #     3   2
    #    / \   \
    #   5   3   9
    root = TreeNode(1)
    root.left = TreeNode(3)
    root.right = TreeNode(2)
    root.left.left = TreeNode(5)
    root.left.right = TreeNode(3)
    root.right.right = TreeNode(9)
    
    solution = Solution()
    print("方法1结果:", solution.widthOfBinaryTree1(root))
    print("方法2结果:", solution.widthOfBinaryTree2(root))