# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def getDirections(self, root: TreeNode, startValue: int, destValue: int) -> str:
        # 找到起点和目标节点的LCA
        lca = self.findLCA(root, startValue, destValue)
        
        # 从起点到LCA的路径（全部是U）
        startToLCA = []
        self.findPath(lca, startValue, startToLCA)
        
        # 从LCA到目标的路径
        lcaToDest = []
        self.findPath(lca, destValue, lcaToDest)
        
        # 起点到LCA的路径全部替换为U
        upPath = 'U' * len(startToLCA)
        
        return upPath + ''.join(lcaToDest)
    
    def findLCA(self, root: TreeNode, p: int, q: int) -> TreeNode:
        if root is None or root.val == p or root.val == q:
            return root
        
        left = self.findLCA(root.left, p, q)
        right = self.findLCA(root.right, p, q)
        
        if left is not None and right is not None:
            return root
        
        return left if left is not None else right
    
    def findPath(self, node: TreeNode, target: int, path: list) -> bool:
        if node is None:
            return False
        
        if node.val == target:
            return True
        
        # 尝试左子树
        path.append('L')
        if self.findPath(node.left, target, path):
            return True
        path.pop()
        
        # 尝试右子树
        path.append('R')
        if self.findPath(node.right, target, path):
            return True
        path.pop()
        
        return False

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    root1 = TreeNode(5)
    root1.left = TreeNode(1)
    root1.right = TreeNode(2)
    root1.left.left = TreeNode(3)
    root1.right.left = TreeNode(6)
    root1.right.right = TreeNode(4)
    
    print(solution.getDirections(root1, 3, 6))  # 输出: "UURL"
    
    # 测试用例2
    root2 = TreeNode(2)
    root2.left = TreeNode(1)
    
    print(solution.getDirections(root2, 2, 1))  # 输出: "L"