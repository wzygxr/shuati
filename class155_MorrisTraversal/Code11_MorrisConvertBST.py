"""
使用Morris遍历解决把二叉搜索树转换为累加树问题

题目来源：LeetCode 538. Convert BST to Greater Tree
题目链接：https://leetcode.cn/problems/convert-bst-to-greater-tree/

题目描述：
给出二叉搜索树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），
使每个节点 node 的新值等于原树中大于或等于 node.val 的值之和。

解题思路：
1. 利用BST的性质：中序遍历得到递增序列
2. 累加树需要的是大于等于当前节点值的所有节点值之和
3. 可以通过反向中序遍历（右-根-左）来实现
4. 在反向中序遍历过程中维护累加和
5. 使用Morris反向中序遍历实现

算法步骤：
1. 使用Morris反向中序遍历遍历BST（右-根-左）
2. 在遍历过程中维护累加和sum
3. 每个节点的值更新为累加和

Morris反向中序遍历的实现要点：
1. 与标准中序遍历相反，先处理右子树
2. 找前驱节点时，是在右子树中找最左节点
3. 线索建立和断开的逻辑与标准中序遍历对称

时间复杂度：O(n) - 需要遍历所有节点
空间复杂度：O(1) - 仅使用常数额外空间
是否为最优解：是，Morris遍历是解决此问题的最优方法

适用场景：
1. 需要节省内存空间的环境
2. BST反向遍历的应用场景
3. 面试中展示对Morris遍历的深入理解

扩展思考：
1. 如何处理节点值重复的情况？
2. 如何在并发环境下保证线程安全？
3. 如何处理节点值为负数的情况？
"""


# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def convertBST(self, root):
        """
        使用Morris反向中序遍历将BST转换为累加树
        
        :param root: BST的根节点
        :return: 转换后的累加树的根节点
        """
        total_sum = 0          # 累加和
        cur = root             # 当前节点
        most_left = None       # 最左节点（前驱节点）
        
        # Morris反向中序遍历（右-根-左）
        while cur:
            most_left = cur.right
            
            # 如果当前节点有右子树
            if most_left:
                # 找到右子树中的最左节点（前驱节点）
                while most_left.left and most_left.left != cur:
                    most_left = most_left.left
                
                # 判断前驱节点的左指针状态
                if most_left.left is None:
                    # 第一次到达，建立线索
                    most_left.left = cur
                    cur = cur.right
                    continue
                else:
                    # 第二次到达，断开线索
                    most_left.left = None
            
            # 处理当前节点（反向中序遍历的核心处理逻辑）
            total_sum += cur.val
            cur.val = total_sum
            
            cur = cur.left
        
        return root


# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1: [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
    # 原始BST:
    #         4
    #       /   \
    #      1     6
    #     / \   / \
    #    0   2 5   7
    #         \     \
    #          3     8
    # 转换后的累加树:
    #         30
    #       /    \
    #      36     21
    #     /  \   /  \
    #    36  35 26  15
    #          \      \
    #          33      8
    root1 = TreeNode(4)
    root1.left = TreeNode(1)
    root1.right = TreeNode(6)
    root1.left.left = TreeNode(0)
    root1.left.right = TreeNode(2)
    root1.right.left = TreeNode(5)
    root1.right.right = TreeNode(7)
    root1.left.right.right = TreeNode(3)
    root1.right.right.right = TreeNode(8)
    
    result1 = solution.convertBST(root1)
    print("测试用例1完成，结果已存储在result1中")
    
    # 测试用例2: [0,null,1]
    root2 = TreeNode(0)
    root2.right = TreeNode(1)
    
    result2 = solution.convertBST(root2)
    print("测试用例2完成，结果已存储在result2中")


if __name__ == "__main__":
    main()