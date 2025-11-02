# LeetCode 742. 二叉树中最近的叶节点
# 题目描述：给定一个二叉树，其中每个节点都含有一个整数键，给定一个键 k，找出距离给定节点最近的叶节点
# 算法思想：将二叉树转换为无向图，然后进行广度优先搜索。对于大型树，可以先找到重心以优化搜索
# 测试链接：https://leetcode.cn/problems/closest-leaf-in-a-binary-tree/
# 时间复杂度：O(n)
# 空间复杂度：O(n)

from collections import defaultdict, deque

# 二叉树节点定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution:
    def findClosestLeaf(self, root, k):
        """
        寻找距离给定节点最近的叶节点
        
        参数:
            root: 二叉树的根节点
            k: 给定的节点键值
        返回:
            最近叶节点的键值
        """
        # 构建图和标记叶节点
        graph = defaultdict(list)
        is_leaf = {}
        
        def build_graph(node, parent):
            """
            递归构建图结构
            
            参数:
                node: 当前节点
                parent: 父节点
            """
            if not node:
                return
            
            # 初始化邻接表
            graph[node.val] = []
            
            # 检查是否为叶节点
            if not node.left and not node.right:
                is_leaf[node.val] = True
            else:
                is_leaf[node.val] = False
            
            # 添加与父节点的连接
            if parent:
                graph[node.val].append(parent.val)
                graph[parent.val].append(node.val)
            
            # 递归处理左右子树
            build_graph(node.left, node)
            build_graph(node.right, node)
        
        # 构建图
        build_graph(root, None)
        
        # 广度优先搜索
        queue = deque([k])
        visited = set([k])
        
        while queue:
            current = queue.popleft()
            
            # 如果是叶节点，返回
            if is_leaf[current]:
                return current
            
            # 遍历所有邻居
            for neighbor in graph[current]:
                if neighbor not in visited:
                    visited.add(neighbor)
                    queue.append(neighbor)
        
        # 不应该到达这里
        return -1

# 测试函数
def test():
    solution = Solution()
    
    # 示例1: [1, 3, 2]
    root1 = TreeNode(1)
    root1.left = TreeNode(3)
    root1.right = TreeNode(2)
    print("Example 1:", solution.findClosestLeaf(root1, 1))  # Expected: 3
    
    # 示例2: [1]
    root2 = TreeNode(1)
    print("Example 2:", solution.findClosestLeaf(root2, 1))  # Expected: 1
    
    # 示例3: [1,2,3,4,null,null,null,5,null,6]
    root3 = TreeNode(1)
    root3.left = TreeNode(2)
    root3.right = TreeNode(3)
    root3.left.left = TreeNode(4)
    root3.left.left.left = TreeNode(5)
    root3.left.left.left.left = TreeNode(6)
    print("Example 3:", solution.findClosestLeaf(root3, 2))  # Expected: 3

# 运行测试
if __name__ == "__main__":
    test()

# 注意：在LeetCode上提交时，直接提交Solution类即可