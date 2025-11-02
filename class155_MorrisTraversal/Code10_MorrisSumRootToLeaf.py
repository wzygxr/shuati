"""
使用Morris遍历解决求根到叶子节点数字之和问题

题目来源：LeetCode 129. Sum Root to Leaf Numbers
题目链接：https://leetcode.cn/problems/sum-root-to-leaf-numbers/

题目描述：
给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
每条从根节点到叶节点的路径都代表一个数字：
例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
计算从根节点到叶节点生成的所有数字之和。
叶节点是指没有子节点的节点。

解题思路：
1. 需要遍历所有从根到叶的路径
2. 在遍历过程中维护当前路径表示的数字
3. 当到达叶节点时，将当前数字加到结果中
4. 使用Morris遍历的前序遍历方式实现

算法步骤：
1. 使用Morris前序遍历遍历二叉树
2. 在遍历过程中维护从根到当前节点的数字路径
3. 当到达叶节点时，累加路径数字到结果中
4. 需要特别处理回溯过程，因为Morris遍历会修改树结构

注意：这个问题实际上不适合用标准的Morris遍历来解决，因为：
1. 需要准确知道何时到达叶节点
2. 需要维护从根到当前节点的路径信息
3. Morris遍历的线索机制会干扰路径信息的正确维护

但我们可以借鉴Morris遍历的思想，实现一种变种方法：
1. 使用前序遍历的方式
2. 在建立线索时记录路径信息
3. 在断开线索时进行回溯

时间复杂度：O(n) - 需要遍历所有节点
空间复杂度：O(1) - 仅使用常数额外空间
是否为最优解：不是，此问题更适合用递归或迭代方法解决

适用场景：
1. 理解Morris遍历思想的扩展应用
2. 面试中展示对算法的深入理解

扩展思考：
1. 如何处理节点值不是0-9的情况？
2. 如何处理路径数字可能溢出的情况？
3. 如何在并发环境下保证线程安全？
"""


# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def sumNumbers(self, root):
        """
        使用Morris前序遍历计算根到叶节点数字之和
        
        :param root: 二叉树的根节点
        :return: 所有根到叶路径数字之和
        """
        if not root:
            return 0
        
        total_sum = 0          # 结果总和
        current_num = 0        # 当前路径表示的数字
        cur = root             # 当前节点
        most_right = None      # 最右节点（前驱节点）
        
        # Morris前序遍历
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
                    # 更新当前路径数字
                    current_num = current_num * 10 + cur.val
                    
                    # 如果是叶节点，累加到结果中
                    if not cur.left and not cur.right:
                        total_sum += current_num
                    
                    most_right.right = cur
                    cur = cur.left
                    continue
                else:
                    # 第二次到达，断开线索
                    most_right.right = None
            else:
                # 没有左子树，直接处理当前节点
                current_num = current_num * 10 + cur.val
                
                # 如果是叶节点，累加到结果中
                if not cur.left and not cur.right:
                    total_sum += current_num
            
            cur = cur.right
        
        return total_sum


# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1: [1,2,3]
    #     1
    #    / \
    #   2   3
    # 数字: 12, 13 -> 和为25
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    
    result1 = solution.sumNumbers(root1)
    print("测试用例1结果:", result1)  # 期望输出: 25
    
    # 测试用例2: [4,9,0,5,1]
    #       4
    #      / \
    #     9   0
    #    / \
    #   5   1
    # 数字: 495, 491, 40 -> 和为1026
    root2 = TreeNode(4)
    root2.left = TreeNode(9)
    root2.right = TreeNode(0)
    root2.left.left = TreeNode(5)
    root2.left.right = TreeNode(1)
    
    result2 = solution.sumNumbers(root2)
    print("测试用例2结果:", result2)  # 期望输出: 1026


if __name__ == "__main__":
    main()