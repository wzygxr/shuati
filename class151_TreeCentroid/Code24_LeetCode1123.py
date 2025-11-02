# LeetCode 1123. 最深叶节点的最近公共祖先
# 题目描述：给定一个二叉树，返回其最深叶节点的最近公共祖先。
# 算法思想：1. 首先计算树的最大深度；2. 然后找到深度等于最大深度的所有叶节点；3. 最后找到这些叶节点的最近公共祖先
# 测试链接：https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/
# 时间复杂度：O(n)
# 空间复杂度：O(h)，h为树高

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Code24_LeetCode1123:
    def __init__(self):
        self.max_depth = 0
        self.lca = None
    
    def compute_depth(self, node, depth):
        """计算树的最大深度"""
        if node is None:
            return
        
        self.max_depth = max(self.max_depth, depth)
        self.compute_depth(node.left, depth + 1)
        self.compute_depth(node.right, depth + 1)
    
    def find_lca(self, node, depth):
        """找到最深叶节点的最近公共祖先"""
        if node is None:
            return depth - 1  # 返回上一层的深度
        
        # 递归计算左右子树中最深节点的深度
        left_depth = self.find_lca(node.left, depth + 1)
        right_depth = self.find_lca(node.right, depth + 1)
        
        # 如果左右子树都包含最深节点，那么当前节点就是这些最深节点的最近公共祖先
        if left_depth == self.max_depth and right_depth == self.max_depth:
            self.lca = node
        
        # 返回以当前节点为根的子树中最深节点的深度
        return max(left_depth, right_depth)
    
    def lca_deepest_leaves(self, root):
        """
        找到最深叶节点的最近公共祖先
        :param root: 二叉树的根节点
        :return: 最深叶节点的最近公共祖先
        """
        self.max_depth = 0
        self.lca = None
        
        # 首先计算树的最大深度
        self.compute_depth(root, 0)
        
        # 然后找到最深叶节点的最近公共祖先
        self.find_lca(root, 0)
        
        return self.lca
    
    def dfs(self, node):
        """优化版本的深度优先搜索"""
        if node is None:
            return None, 0
        
        left_node, left_depth = self.dfs(node.left)
        right_node, right_depth = self.dfs(node.right)
        
        # 如果左右子树深度相同，当前节点就是最近公共祖先
        if left_depth == right_depth:
            return node, left_depth + 1
        # 否则，选择深度较大的子树中的结果
        elif left_depth > right_depth:
            return left_node, left_depth + 1
        else:
            return right_node, right_depth + 1
    
    def lca_deepest_leaves_optimized(self, root):
        """
        优化版本：一次性递归完成最大深度计算和最近公共祖先查找
        :param root: 二叉树的根节点
        :return: 最深叶节点的最近公共祖先
        """
        return self.dfs(root)[0]
    
    def build_tree(self, nums, index=0):
        """根据数组构建二叉树"""
        if index >= len(nums) or nums[index] is None:
            return None
        
        node = TreeNode(nums[index])
        node.left = self.build_tree(nums, 2 * index + 1)
        node.right = self.build_tree(nums, 2 * index + 2)
        
        return node
    
    def print_tree(self, node):
        """打印树的节点值（用于调试）"""
        if node is None:
            print("null", end=" ")
            return
        print(node.val, end=" ")
        self.print_tree(node.left)
        self.print_tree(node.right)
    
    def test(self):
        """测试方法"""
        # 测试用例1
        nums1 = [3, 5, 1, 6, 2, 0, 8, None, None, 7, 4]
        root1 = self.build_tree(nums1)
        result1 = self.lca_deepest_leaves(root1)
        result1_optimized = self.lca_deepest_leaves_optimized(root1)
        print("测试用例1结果:", end=" ")
        self.print_tree(result1)
        print()
        print("优化版本结果:", end=" ")
        self.print_tree(result1_optimized)
        print()
        # 期望输出: 2 7 4 null null null null
        
        # 测试用例2
        nums2 = [1]
        root2 = self.build_tree(nums2)
        result2 = self.lca_deepest_leaves(root2)
        print("测试用例2结果:", end=" ")
        self.print_tree(result2)
        print()
        # 期望输出: 1 null null
        
        # 测试用例3
        nums3 = [0, 1, 3, None, 2]
        root3 = self.build_tree(nums3)
        result3 = self.lca_deepest_leaves(root3)
        print("测试用例3结果:", end=" ")
        self.print_tree(result3)
        print()
        # 期望输出: 2 null null

# 主函数
def main():
    solution = Code24_LeetCode1123()
    solution.test()

if __name__ == "__main__":
    main()

# 注意：
# 1. 这道题虽然不是直接找树的重心，但可以应用类似的思想：寻找一个节点，使得它到最深叶节点的距离尽可能小
# 2. 树的重心是使得最大子树的大小最小的节点，而本题是寻找最深叶节点的最近公共祖先
# 3. 两种算法都利用了树形结构的特性，通过深度优先搜索来计算子树的属性
# 4. 优化版本的算法更加高效，只需要一次深度优先搜索就能同时获取深度和最近公共祖先信息
# 5. 在Python中不需要手动释放内存，垃圾回收机制会自动处理