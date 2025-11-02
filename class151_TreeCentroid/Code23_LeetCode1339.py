# LeetCode 1339. 分裂二叉树的最大乘积
# 题目描述：给定一个二叉树，通过删除一条边将树分成两个子树，使得这两个子树的节点值之和的乘积最大。
# 算法思想：1. 先计算整棵树的节点值之和；2. 遍历树，对于每个子树计算其节点值之和，然后计算乘积；3. 找到最大乘积
# 测试链接：https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/
# 时间复杂度：O(n)
# 空间复杂度：O(h)，h为树高

MOD = 10**9 + 7

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Code23_LeetCode1339:
    def __init__(self):
        self.total_sum = 0
        self.max_product = 0
    
    def calculate_sum(self, node):
        """计算树的节点值之和"""
        if node is None:
            return 0
        return node.val + self.calculate_sum(node.left) + self.calculate_sum(node.right)
    
    def calculate_subtree_sum(self, node):
        """计算子树的节点值之和，并更新最大乘积"""
        if node is None:
            return 0
        
        subtree_sum = node.val + self.calculate_subtree_sum(node.left) + self.calculate_subtree_sum(node.right)
        
        # 计算当前子树与剩余部分的乘积
        product = subtree_sum * (self.total_sum - subtree_sum)
        
        # 更新最大乘积
        if product > self.max_product:
            self.max_product = product
        
        return subtree_sum
    
    def max_product(self, root):
        """
        计算分裂二叉树的最大乘积
        :param root: 二叉树的根节点
        :return: 最大乘积对10^9+7取模的结果
        """
        self.total_sum = 0
        self.max_product = 0
        
        # 计算整棵树的节点值之和
        self.total_sum = self.calculate_sum(root)
        
        # 再次遍历树，计算每个子树的节点值之和，并更新最大乘积
        self.calculate_subtree_sum(root)
        
        return int(self.max_product % MOD)
    
    def build_tree(self, nums, index=0):
        """根据数组构建二叉树"""
        if index >= len(nums) or nums[index] is None:
            return None
        
        node = TreeNode(nums[index])
        node.left = self.build_tree(nums, 2 * index + 1)
        node.right = self.build_tree(nums, 2 * index + 2)
        
        return node
    
    def test(self):
        """测试方法"""
        # 测试用例1
        nums1 = [1, 2, 3, 4, 5, 6]
        root1 = self.build_tree(nums1)
        result1 = self.max_product(root1)
        print(f"测试用例1结果: {result1}")
        # 期望输出: 110
        
        # 测试用例2
        nums2 = [1, None, 2, 3, 4, None, None, 5, 6]
        root2 = self.build_tree(nums2)
        result2 = self.max_product(root2)
        print(f"测试用例2结果: {result2}")
        # 期望输出: 90
        
        # 测试用例3：较大的树
        nums3 = [10, 5, 15, 2, 7, None, 20]
        root3 = self.build_tree(nums3)
        result3 = self.max_product(root3)
        print(f"测试用例3结果: {result3}")

# 主函数
def main():
    solution = Code23_LeetCode1339()
    solution.test()

if __name__ == "__main__":
    main()

# 注意：
# 1. 题目中要求结果对10^9+7取模
# 2. 在Python中整数溢出问题不像Java和C++那样严重，但为了代码一致性，仍然使用长整型计算
# 3. 这道题虽然不是直接找树的重心，但可以应用类似的思想：寻找一个分割点，使得两部分的大小尽可能接近
# 4. 树的重心的定义是：删除该节点后，最大的子树的大小不超过整棵树大小的一半
# 5. 这道题的最优分割点也是使得两部分尽可能接近，所以与树的重心有密切关系
# 6. 在Python中不需要手动释放内存，垃圾回收机制会自动处理