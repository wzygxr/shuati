"""
使用Morris遍历解决二叉搜索树中的众数问题

题目来源：LeetCode 501. Find Mode in Binary Search Tree
题目链接：https://leetcode.cn/problems/find-mode-in-binary-search-tree/

题目描述：
给你一个含重复值的二叉搜索树（BST）的根节点 root ，找出并返回 BST 中的所有众数（即，出现频率最高的元素）。
如果树中有不止一个众数，可以按任意顺序返回。

解题思路：
1. 利用BST的性质：中序遍历得到有序序列
2. 在有序序列中，相同元素会连续出现
3. 使用Morris中序遍历，边遍历边统计每个元素的出现次数
4. 维护当前元素的出现次数和最大出现次数
5. 根据出现次数更新结果集

算法步骤：
1. 使用Morris中序遍历遍历BST
2. 在遍历过程中维护前一个节点pre、当前节点的出现次数count、最大出现次数maxCount
3. 当前节点值与前一个节点值相同时，count++；否则count=1
4. 如果count == maxCount，将当前节点值加入结果集
5. 如果count > maxCount，清空结果集，将当前节点值加入结果集，并更新maxCount

时间复杂度：O(n) - 需要遍历所有节点
空间复杂度：O(1) - 仅使用常数额外空间，不考虑结果集的空间
是否为最优解：是，Morris遍历是解决此问题的最优方法

适用场景：
1. 需要节省内存空间的环境
2. BST中序遍历的应用场景
3. 面试中展示对Morris遍历的深入理解

扩展思考：
1. 如果不是BST而是普通二叉树，如何找众数？
2. 如何处理节点值为负数的情况？
3. 如何在并发环境下保证线程安全？
"""


# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def __init__(self):
        # 初始化全局变量
        self.pre = None      # 前一个遍历的节点
        self.count = 0       # 当前元素出现次数
        self.maxCount = 0    # 最大出现次数
        self.modes = []      # 结果集

    def findMode(self, root):
        """
        使用Morris中序遍历找BST中的众数
        
        :param root: BST的根节点
        :return: 包含所有众数的列表
        """
        # 重置全局变量
        self.pre = None
        self.count = 0
        self.maxCount = 0
        self.modes = []
        
        # Morris遍历相关变量
        cur = root           # 当前节点
        mostRight = None     # 最右节点（前驱节点）
        
        # Morris中序遍历
        while cur:
            mostRight = cur.left
            
            # 如果当前节点有左子树
            if mostRight:
                # 找到左子树中的最右节点（前驱节点）
                while mostRight.right and mostRight.right != cur:
                    mostRight = mostRight.right
                
                # 判断前驱节点的右指针状态
                if mostRight.right is None:
                    # 第一次到达，建立线索
                    mostRight.right = cur
                    cur = cur.left
                    continue
                else:
                    # 第二次到达，断开线索
                    mostRight.right = None
            
            # 处理当前节点（中序遍历的核心处理逻辑）
            # 统计当前节点值的出现次数
            if self.pre and self.pre.val == cur.val:
                self.count += 1
            else:
                self.count = 1
            
            # 根据出现次数更新结果集
            if self.count == self.maxCount:
                self.modes.append(cur.val)
            elif self.count > self.maxCount:
                self.modes = [cur.val]  # 清空结果集
                self.maxCount = self.count
            
            self.pre = cur
            cur = cur.right
        
        return self.modes


# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1: [1,null,2,2]
    root1 = TreeNode(1)
    root1.right = TreeNode(2)
    root1.right.left = TreeNode(2)
    
    result1 = solution.findMode(root1)
    print("测试用例1结果:", result1)
    
    # 测试用例2: [0]
    root2 = TreeNode(0)
    
    result2 = solution.findMode(root2)
    print("测试用例2结果:", result2)


if __name__ == "__main__":
    main()