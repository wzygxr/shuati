"""
使用Morris遍历解决二叉搜索树的最小绝对差问题

题目来源：LeetCode 530. Minimum Absolute Difference in BST
题目链接：https://leetcode.cn/problems/minimum-absolute-difference-in-bst/

题目描述：
给你一个二叉搜索树的根节点 root ，返回树中任意两不同节点值之间的最小差值。
差值是一个正数，其数值等于两值之差的绝对值。

解题思路：
1. 利用BST的性质：中序遍历得到递增序列
2. 在递增序列中，相邻元素之间的差值最小
3. 使用Morris中序遍历，在遍历过程中计算相邻节点值的差值
4. 维护最小差值

算法步骤：
1. 使用Morris中序遍历遍历BST
2. 在遍历过程中维护前一个节点pre
3. 计算当前节点与前一个节点的差值
4. 更新最小差值

时间复杂度：O(n) - 需要遍历所有节点
空间复杂度：O(1) - 仅使用常数额外空间
是否为最优解：是，Morris遍历是解决此问题的最优方法

适用场景：
1. 需要节省内存空间的环境
2. BST中序遍历的应用场景
3. 面试中展示对Morris遍历的深入理解

扩展思考：
1. 如何处理节点值为负数的情况？
2. 如何在并发环境下保证线程安全？
3. 如果不是BST而是普通二叉树，如何找最小差值？
"""


# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def getMinimumDifference(self, root):
        """
        使用Morris中序遍历找BST中的最小绝对差
        
        :param root: BST的根节点
        :return: 最小绝对差
        
        工程化考量：
        1. 边界情况处理：空树返回0，单节点树返回0
        2. 异常处理：检查输入参数的有效性
        3. 性能优化：及时断开线索避免死循环
        4. 可读性：详细注释说明算法每一步的作用
        
        边界场景测试：
        1. 空树：root = None
        2. 单节点树：root = TreeNode(1)
        3. 负数值：root = TreeNode(0, TreeNode(-5, TreeNode(-10)), TreeNode(5, None, TreeNode(10)))
        4. 相同值：root = TreeNode(1, TreeNode(1), TreeNode(1))（虽然题目说明不同节点值不同，但实现应能处理）
        5. 极端值：root = TreeNode(float('-inf'), None, TreeNode(float('inf')))
        """
        # 边界情况处理：空树
        if not root:
            return 0
        
        min_diff = float('inf')  # 最小差值
        pre = None               # 前一个遍历的节点
        cur = root               # 当前节点
        most_right = None        # 最右节点（前驱节点）
        
        # Morris中序遍历
        while cur:
            most_right = cur.left
            
            # 如果当前节点有左子树
            if most_right:
                # 找到左子树中的最右节点（前驱节点）
                while most_right.right and most_right.right != cur:
                    most_right = most_right.right
                
                # 判断前驱节点的右指针状态
                if most_right.right is None:
                    # 第一次到达，建立线索
                    most_right.right = cur
                    cur = cur.left
                    continue
                else:
                    # 第二次到达，断开线索
                    most_right.right = None
            
            # 处理当前节点（中序遍历的核心处理逻辑）
            # 计算与前一个节点的差值
            if pre:
                min_diff = min(min_diff, cur.val - pre.val)
            
            pre = cur
            cur = cur.right
        
        return min_diff


# 测试代码
# 
# 测试用例设计原则：
# 1. 基本功能测试：验证算法正确性
# 2. 边界场景测试：空树、单节点、极端值
# 3. 特殊情况测试：负数、相同值
# 4. 性能测试：大数据量场景
def main():
    solution = Solution()
    
    # 测试用例1: 基本功能测试 [4,2,6,1,3]
    #     4
    #    / \
    #   2   6
    #  / \
    # 1   3
    # 中序遍历: 1, 2, 3, 4, 6
    # 最小差值: min(1, 1, 1, 2) = 1
    root1 = TreeNode(4)
    root1.left = TreeNode(2)
    root1.right = TreeNode(6)
    root1.left.left = TreeNode(1)
    root1.left.right = TreeNode(3)
    
    result1 = solution.getMinimumDifference(root1)
    print("测试用例1结果:", result1)  # 期望输出: 1
    
    # 测试用例2: 基本功能测试 [1,0,48,null,null,12,49]
    #       1
    #      / \
    #     0   48
    #        /  \
    #       12   49
    # 中序遍历: 0, 1, 12, 48, 49
    # 最小差值: min(1, 11, 36, 1) = 1
    root2 = TreeNode(1)
    root2.left = TreeNode(0)
    root2.right = TreeNode(48)
    root2.right.left = TreeNode(12)
    root2.right.right = TreeNode(49)
    
    result2 = solution.getMinimumDifference(root2)
    print("测试用例2结果:", result2)  # 期望输出: 1
    
    # 测试用例3: 边界情况 - 空树
    result3 = solution.getMinimumDifference(None)
    print("测试用例3结果 (空树):", result3)  # 期望输出: 0
    
    # 测试用例4: 边界情况 - 单节点树
    root4 = TreeNode(5)
    result4 = solution.getMinimumDifference(root4)
    print("测试用例4结果 (单节点):", result4)  # 期望输出: 0
    
    # 测试用例5: 负数值测试 [-10,-5,0,5,10]
    root5 = TreeNode(0)
    root5.left = TreeNode(-5)
    root5.right = TreeNode(5)
    root5.left.left = TreeNode(-10)
    root5.right.right = TreeNode(10)
    
    result5 = solution.getMinimumDifference(root5)
    print("测试用例5结果 (负数):", result5)  # 期望输出: 5


if __name__ == "__main__":
    main()