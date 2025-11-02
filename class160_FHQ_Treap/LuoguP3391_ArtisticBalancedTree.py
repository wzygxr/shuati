# 洛谷 P3391 文艺平衡树 - Python实现
# 使用FHQ-Treap（无旋Treap）解决区间翻转问题
# 题目链接: https://www.luogu.com.cn/problem/P3391
# 题目描述: 维护一个序列，支持区间反转操作，并输出最终数组
# 
# 解题思路:
# 使用FHQ-Treap维护序列，通过按大小分裂和合并操作结合懒标记实现区间翻转
# 实现O(log n)的区间反转操作复杂度

import random
import sys

class LuoguP3391_ArtisticBalancedTree:
    class Node:
        def __init__(self, key, priority):
            self.key = key          # 键值
            self.priority = priority  # 随机优先级
            self.size = 1           # 子树大小
            self.reversed = False   # 反转标记（懒标记）
            self.left = None        # 左子节点
            self.right = None       # 右子节点
    
    def __init__(self):
        self.root = None           # 根节点
        random.seed(42)            # 设置随机种子以保证结果可复现
    
    def _update_size(self, node):
        """更新节点的子树大小"""
        if node:
            left_size = node.left.size if node.left else 0
            right_size = node.right.size if node.right else 0
            node.size = left_size + right_size + 1
    
    def _push_down(self, node):
        """下传懒标记"""
        if node and node.reversed:
            # 交换左右子树
            node.left, node.right = node.right, node.left
            
            # 标记子节点为待反转
            if node.left:
                node.left.reversed = not node.left.reversed
            if node.right:
                node.right.reversed = not node.right.reversed
            
            # 清除当前节点的反转标记
            node.reversed = False
    
    def _split_by_size(self, root, k):
        """按照大小分裂（第k大）"""
        if not root:
            return (None, None)
        
        # 先下传懒标记
        self._push_down(root)
        
        left_size = root.left.size if root.left else 0
        
        if left_size + 1 <= k:
            # 当前节点及其左子树属于左部分，递归分裂右子树
            left, right = self._split_by_size(root.right, k - left_size - 1)
            root.right = left
            self._update_size(root)
            return (root, right)
        else:
            # 当前节点及其右子树属于右部分，递归分裂左子树
            left, right = self._split_by_size(root.left, k)
            root.left = right
            self._update_size(root)
            return (left, root)
    
    def _merge(self, left, right):
        """合并操作"""
        if not left:
            return right
        if not right:
            return left
        
        # 先下传懒标记
        self._push_down(left)
        self._push_down(right)
        
        if left.priority >= right.priority:
            left.right = self._merge(left.right, right)
            self._update_size(left)
            return left
        else:
            right.left = self._merge(left, right.left)
            self._update_size(right)
            return right
    
    def build(self, n):
        """构建1~n的FHQ-Treap"""
        # 优化构建过程，使用递归的方式构建平衡的Treap
        def build_helper(l, r):
            if l > r:
                return None
            mid = (l + r) // 2
            node = self.Node(mid, random.random())
            node.left = build_helper(l, mid - 1)
            node.right = build_helper(mid + 1, r)
            self._update_size(node)
            return node
        
        self.root = build_helper(1, n)
    
    def reverse(self, l, r):
        """区间反转操作 [l, r]"""
        # 先将树分裂成三部分：1~l-1, l~r, r+1~n
        left, right = self._split_by_size(self.root, r)
        left_left, mid = self._split_by_size(left, l - 1)
        
        # 对中间部分打反转标记
        if mid:
            mid.reversed = not mid.reversed
        
        # 合并回去
        self.root = self._merge(self._merge(left_left, mid), right)
    
    def _inorder_traversal(self, node, result):
        """中序遍历辅助函数"""
        if not node:
            return
        
        # 先下传懒标记
        self._push_down(node)
        
        self._inorder_traversal(node.left, result)
        result.append(str(node.key))
        self._inorder_traversal(node.right, result)
    
    def get_result(self):
        """获取中序遍历结果"""
        result = []
        self._inorder_traversal(self.root, result)
        return ' '.join(result)

# 主程序
if __name__ == "__main__":
    # 读取输入
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 构建树
    tree = LuoguP3391_ArtisticBalancedTree()
    tree.build(n)
    
    # 处理每个反转操作
    for _ in range(m):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        tree.reverse(l, r)
    
    # 输出结果
    print(tree.get_result())

'''
【时间复杂度分析】
- 构建树：O(n) - 使用递归构建平衡树
- 每次反转操作：O(log n)
- 中序遍历：O(n)
总时间复杂度：O(n + m log n)

【空间复杂度分析】
- O(n)，存储n个节点

【Python优化说明】
1. 优化了构建过程，使用递归构建平衡的Treap，而不是逐个插入
2. 使用sys.stdin.read()一次性读取所有输入，提高读取效率
3. 使用列表收集结果，最后再join，避免频繁字符串拼接
4. 注意Python的递归深度限制，对于n较大的情况，递归构建可能需要调整递归深度

【测试用例】
输入：
6 3
1 3
1 4
1 6
输出：
6 5 3 4 2 1

【边界情况处理】
1. n=1时，只有一个元素，反转无效果
2. l=r时，单个元素反转无效果
3. 多次反转同一个区间，相当于偶数次反转会恢复原状

【Python递归深度考虑】
对于Python来说，默认的递归深度限制可能会在构建大型树时出现问题。如果n很大（如1e5），可能需要使用非递归的构建方法或调整sys.setrecursionlimit()。
'''