# LeetCode 654. 最大二叉树
# 题目描述：给定一个不含重复元素的整数数组nums。一个以此数组构建的最大二叉树定义如下：
# 1. 二叉树的根是数组中的最大元素
# 2. 左子树是通过数组中最大值左边部分构造出的最大二叉树
# 3. 右子树是通过数组中最大值右边部分构造出的最大二叉树
# 算法思想：递归地在数组中找到最大值作为根节点，然后分别构建左右子树
# 测试链接：https://leetcode.com/problems/maximum-binary-tree/
# 时间复杂度：O(n²)，最坏情况下数组有序
# 空间复杂度：O(n)

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Code27_LeetCode654:
    def __init__(self):
        pass
    
    def build_tree(self, nums, left, right):
        """
        递归地构建最大二叉树
        :param nums: 整数数组
        :param left: 当前区间的左边界
        :param right: 当前区间的右边界
        :return: 构建好的子树的根节点
        """
        if left > right:
            return None
        
        # 找到当前区间内的最大值及其索引（作为树的重心）
        max_index = left
        for i in range(left + 1, right + 1):
            if nums[i] > nums[max_index]:
                max_index = i
        
        # 创建根节点（最大值节点）
        root = TreeNode(nums[max_index])
        
        # 递归构建左右子树
        root.left = self.build_tree(nums, left, max_index - 1)
        root.right = self.build_tree(nums, max_index + 1, right)
        
        return root
    
    def construct_maximum_binary_tree(self, nums):
        """
        根据数组构造最大二叉树
        :param nums: 整数数组，不含重复元素
        :return: 构造好的最大二叉树的根节点
        """
        if not nums:
            return None
        return self.build_tree(nums, 0, len(nums) - 1)
    
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
        nums1 = [3, 2, 1, 6, 0, 5]
        root1 = self.construct_maximum_binary_tree(nums1)
        print("测试用例1结果:", end=" ")
        self.print_tree(root1)
        print()
        # 期望输出: 6 3 null 2 null 1 null null 5 0 null null
        
        # 测试用例2
        nums2 = [3, 2, 1]
        root2 = self.construct_maximum_binary_tree(nums2)
        print("测试用例2结果:", end=" ")
        self.print_tree(root2)
        print()
        # 期望输出: 3 null 2 null 1 null null
        
        # 测试用例3 - 单元素数组
        nums3 = [5]
        root3 = self.construct_maximum_binary_tree(nums3)
        print("测试用例3结果:", end=" ")
        self.print_tree(root3)
        print()
        # 期望输出: 5 null null
        
        # 测试用例4 - 递减数组
        nums4 = [5, 4, 3, 2, 1]
        root4 = self.construct_maximum_binary_tree(nums4)
        print("测试用例4结果:", end=" ")
        self.print_tree(root4)
        print()
        # 期望输出: 5 null 4 null 3 null 2 null 1 null null
        
        # 测试用例5 - 递增数组
        nums5 = [1, 2, 3, 4, 5]
        root5 = self.construct_maximum_binary_tree(nums5)
        print("测试用例5结果:", end=" ")
        self.print_tree(root5)
        print()
        # 期望输出: 5 4 3 2 1 null null null null null null

# 主函数
def main():
    solution = Code27_LeetCode654()
    solution.test()

if __name__ == "__main__":
    main()

# 注意：
# 1. 最大二叉树的构建过程与树重心的选择有相似之处：都需要找到一个节点作为根，使得其左子树和右子树满足某种特性
# 2. 树重心是使最大子树大小最小的节点，而最大二叉树是选择当前区间的最大值作为根节点
# 3. 两种算法都采用了分治法的思想，将问题分解为子问题并递归求解
# 4. 时间复杂度分析：在最坏情况下（如递增或递减数组），每次都要遍历整个区间，因此时间复杂度为O(n²)
# 5. 空间复杂度分析：递归调用栈的深度为O(n)，因此空间复杂度为O(n)
# 6. 算法优化：可以使用单调栈将时间复杂度优化到O(n)，但会增加实现的复杂度
# 7. 异常情况处理：代码处理了空数组和单元素数组的情况
# 8. 该问题的核心思想是选择当前区间的最大值作为根节点，这与树重心思想中的"平衡"概念有关
# 9. 在树的构建过程中，我们每次都选择一个节点（最大值）作为根，然后递归构建左右子树，这与树重心分解树的过程类似
# 10. 在Python中不需要手动释放内存，垃圾回收机制会自动处理