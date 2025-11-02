# LeetCode 124. 二叉树中的最大路径和
# 题目描述：路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。路径和是路径中各节点值的总和。
# 算法思想：利用深度优先搜索计算每个节点的最大贡献值，同时更新全局最大路径和
# 测试链接：https://leetcode.com/problems/binary-tree-maximum-path-sum/
# 时间复杂度：O(n)
# 空间复杂度：O(h)，h为树高，最坏情况下为O(n)

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Code26_LeetCode124:
    def __init__(self):
        self.max_sum = float('-inf')
    
    def max_path_sum_helper(self, node):
        """
        计算从当前节点开始的最大路径和，并更新全局最大路径和
        :param node: 当前节点
        :return: 以当前节点为起点的最大路径和
        """
        if node is None:
            return 0
        
        # 递归计算左右子树的最大贡献值（如果贡献值为负，则取0，即不选择该子树）
        left_gain = max(self.max_path_sum_helper(node.left), 0)
        right_gain = max(self.max_path_sum_helper(node.right), 0)
        
        # 更新全局最大路径和：当前节点值 + 左子树最大贡献值 + 右子树最大贡献值
        self.max_sum = max(self.max_sum, node.val + left_gain + right_gain)
        
        # 返回当前节点的最大贡献值：节点值 + 左右子树中较大的贡献值
        return node.val + max(left_gain, right_gain)
    
    def max_path_sum(self, root):
        """
        计算二叉树中的最大路径和
        :param root: 二叉树的根节点
        :return: 最大路径和
        """
        # 初始化最大路径和为最小整数值，考虑到可能有负数的情况
        self.max_sum = float('-inf')
        self.max_path_sum_helper(root)
        return self.max_sum
    
    def build_tree(self, nums, index=0):
        """
        根据数组构建二叉树
        :param nums: 数组，None表示空节点
        :param index: 当前索引
        :return: 构建好的树节点
        """
        if index >= len(nums) or nums[index] is None:
            return None
        
        node = TreeNode(nums[index])
        node.left = self.build_tree(nums, 2 * index + 1)
        node.right = self.build_tree(nums, 2 * index + 2)
        
        return node
    
    def print_tree(self, node):
        """
        打印树的结构（用于调试）
        :param node: 二叉树的根节点
        """
        if node is None:
            print("null", end=" ")
            return
        
        print(node.val, end=" ")
        self.print_tree(node.left)
        self.print_tree(node.right)
    
    def test(self):
        """
        测试方法
        """
        # 测试用例1
        nums1 = [1, 2, 3]
        root1 = self.build_tree(nums1)
        result1 = self.max_path_sum(root1)
        print("测试用例1结果:", result1)  # 期望输出: 6
        
        # 测试用例2
        nums2 = [-10, 9, 20, None, None, 15, 7]
        root2 = self.build_tree(nums2)
        result2 = self.max_path_sum(root2)
        print("测试用例2结果:", result2)  # 期望输出: 42
        
        # 测试用例3 - 单节点树
        nums3 = [1]
        root3 = self.build_tree(nums3)
        result3 = self.max_path_sum(root3)
        print("测试用例3结果:", result3)  # 期望输出: 1
        
        # 测试用例4 - 全负数节点
        nums4 = [-3]
        root4 = self.build_tree(nums4)
        result4 = self.max_path_sum(root4)
        print("测试用例4结果:", result4)  # 期望输出: -3
        
        # 测试用例5 - 混合正负数节点
        nums5 = [2, -1, -2]
        root5 = self.build_tree(nums5)
        result5 = self.max_path_sum(root5)
        print("测试用例5结果:", result5)  # 期望输出: 2

# 主函数
def main():
    solution = Code26_LeetCode124()
    solution.test()

if __name__ == "__main__":
    main()

# 注意：
# 1. 树的最大路径和问题与树重心的思想有相似之处，都是通过深度优先搜索来计算子树的属性
# 2. 树重心寻找的是使最大子树大小最小的节点，而最大路径和寻找的是路径和最大的路径
# 3. 两种算法都需要在递归过程中维护全局最优解
# 4. 最大路径和问题中，我们需要考虑每个节点作为路径转折点的情况，即当前节点的值加上左右子树的最大贡献值
# 5. 时间复杂度分析：每个节点只被访问一次，因此时间复杂度为O(n)
# 6. 空间复杂度分析：递归调用栈的深度为树的高度，最坏情况下为O(n)
# 7. 异常情况处理：代码处理了空树和只有负数节点的情况
# 8. 算法优化：当子树的贡献值为负时，我们选择不包含该子树，以获得更大的路径和
# 9. 边界情况处理：初始最大路径和设置为负无穷，避免了全负数情况的错误
# 10. 在Python中不需要手动释放内存，垃圾回收机制会自动处理