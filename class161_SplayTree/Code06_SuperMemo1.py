#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SuperMemo (POJ 3580) - Python完整实现
【题目来源】POJ 3580
【题目链接】http://poj.org/problem?id=3580
【算法说明】
此文件包含SuperMemo问题的Python实现，使用Splay树维护序列并支持区间操作
支持的操作包括区间加法、翻转、旋转、插入、删除和区间最小值查询
【实现特点】
- 完整的节点类和Splay树类设计
- 实现懒标记（延迟传播）机制处理区间操作
- 使用迭代方式实现Splay操作以避免Python递归深度限制
- 详细的复杂度分析和算法原理解释
【复杂度分析】
- 时间复杂度：所有操作均摊O(log n)
- 空间复杂度：O(n)
【工程考量】
- 处理Python递归深度限制
- 性能优化考虑
- 边界情况处理
- 测试数据验证
【应用场景】
- 动态维护序列的区间操作
- 需要高效处理范围更新和查询的场景
- 算法竞赛中的数据结构问题
【注意事项】
由于Python的性能限制，在大规模数据下可能效率不如C++和Java
但实现逻辑与Java版本完全一致，便于学习和理解算法原理
"""

class SplayNode:
    """Splay树节点类
    包含节点值、子树大小、最小值、懒标记等属性
    """
    def __init__(self, value=0):
        # 【基本属性】
        self.value = value      # 节点存储的值
        self.size = 1           # 以当前节点为根的子树大小
        self.min_val = value    # 以当前节点为根的子树中的最小值
        
        # 【懒标记】用于区间操作延迟传播
        self.reverse = False    # 翻转标记
        self.update = False     # 更新标记（用于区间加法）
        self.change = 0         # 更新值（加法的增量）
        
        # 【树结构指针】
        self.father = None      # 父节点
        self.left = None        # 左子节点
        self.right = None       # 右子节点

class SplayTree:
    """Splay树类 - 支持区间操作的平衡树
    实现了SuperMemo问题所需的所有操作
    """
    def __init__(self):
        """初始化Splay树
        设置头节点为None，定义整数最大值常量
        """
        self.head = None
        self.INF = 2147483647  # 使用整数最大值避免类型问题
    
    def up(self, node):
        """
        【自底向上维护】更新节点信息
        时间复杂度: O(1)
        功能：
        - 更新当前节点的子树大小
        - 更新当前节点的最小值信息
        
        参数:
            node: 需要更新信息的节点
        """
        if node is None:
            return
        
        # 初始化子树大小为1（当前节点本身）
        node.size = 1
        
        # 初始化最小值为当前节点的值
        node.min_val = node.value
        
        # 合并左子树信息
        if node.left is not None:
            node.size += node.left.size
            node.min_val = min(node.min_val, node.left.min_val)
        
        # 合并右子树信息
        if node.right is not None:
            node.size += node.right.size
            node.min_val = min(node.min_val, node.right.min_val)
    
    def down(self, node):
        """
        【懒标记下传】将懒标记传播到子节点
        时间复杂度: O(1)
        功能：
        - 处理翻转标记：交换左右子节点
        - 处理更新标记：将增量应用到子树节点
        
        参数:
            node: 需要下传懒标记的节点
        """
        if node is None:
            return
        
        # 【翻转操作处理】
        if node.reverse:
            # 交换左右子节点
            node.left, node.right = node.right, node.left
            
            # 将翻转标记传递给子节点
            if node.left is not None:
                node.left.reverse = not node.left.reverse
            if node.right is not None:
                node.right.reverse = not node.right.reverse
            
            # 清除当前节点的翻转标记
            node.reverse = False
        
        # 【区间加法处理】
        if node.update and node.change != 0:
            # 更新当前节点的值
            node.value += node.change
            
            # 更新最小值（如果有子树）
            if node.left is not None:
                node.left.update = True
                node.left.change += node.change
                node.left.min_val += node.change
            if node.right is not None:
                node.right.update = True
                node.right.change += node.change
                node.right.min_val += node.change
            
            # 清除当前节点的更新标记
            node.update = False
            node.change = 0
    
    def lr(self, node):
        """
        【方向判断】确定节点是其父节点的左儿子还是右儿子
        时间复杂度: O(1)
        
        参数:
            node: 需要判断的节点
        返回:
            0 表示是左儿子，1 表示是右儿子
        """
        if node.father is None:
            return 0
        return 1 if node.father.right == node else 0
    
    def rotate(self, node):
        """
        【核心旋转操作】将节点旋转至其父节点的位置
        时间复杂度: O(1)
        功能：
        - 根据节点位置（左子或右子）执行不同的旋转
        - 更新父指针和子指针关系
        - 更新相关节点的信息
        
        参数:
            node: 需要旋转的节点
        """
        f = node.father       # 父节点
        g = f.father          # 祖父节点
        son_i = self.lr(node) # 当前节点是父节点的哪个子节点
        son_f = self.lr(f)    # 父节点是祖父节点的哪个子节点
        
        # 处理父节点和当前节点之间的关系
        if son_i == 1:  # 右子节点，执行右旋
            # 将当前节点的左子树变为父节点的右子树
            f.right = node.left
            if f.right is not None:
                f.right.father = f
            # 将父节点变为当前节点的左子树
            node.left = f
        else:  # 左子节点，执行左旋
            # 将当前节点的右子树变为父节点的左子树
            f.left = node.right
            if f.left is not None:
                f.left.father = f
            # 将父节点变为当前节点的右子树
            node.right = f
        
        # 更新父节点的父指针
        f.father = node
        
        # 处理与祖父节点的关系
        if g is not None:
            if son_f == 1:
                g.right = node
            else:
                g.left = node
        node.father = g
        
        # 【重要】更新节点信息，先更新被旋转的父节点，再更新当前节点
        self.up(f)
        self.up(node)
        
        # 如果旋转后节点成为根节点，更新头指针
        if node.father is None:
            self.head = node
    
    def splay(self, node, goal):
        """
        【核心伸展操作】将节点旋转到目标节点下方
        如果goal为None，则将节点旋转到根节点
        时间复杂度: 均摊O(log n)
        功能：
        - 使用双旋（zig-zig、zig-zag）策略优化伸展过程
        - 在下传懒标记的同时进行旋转
        
        参数:
            node: 需要旋转的节点
            goal: 目标父节点（可以为None表示旋转到根）
        """
        while node.father != goal:
            # 下传懒标记，确保操作正确性
            self.down(node.father.father)
            self.down(node.father)
            self.down(node)
            
            # 根据节点、父节点、祖父节点的关系选择旋转方式
            f = node.father
            g = f.father
            
            if g != goal:
                if self.lr(node) == self.lr(f):
                    # Zig-Zig情况：先旋转父节点
                    self.rotate(f)
                else:
                    # Zig-Zag情况：直接旋转当前节点
                    self.rotate(node)
            # 旋转当前节点
            self.rotate(node)
        
        # 如果旋转到根节点，更新头指针
        if goal is None:
            self.head = node
    
    def find(self, rank):
        """
        【查找操作】在整棵树中找到中序遍历排名为rank的节点
        时间复杂度: O(log n)
        
        参数:
            rank: 目标排名（从1开始）
        返回:
            对应排名的节点
        """
        node = self.head
        while True:
            # 下传懒标记
            self.down(node)
            
            # 判断当前节点的左子树大小，确定目标节点位置
            left_size = 0 if node.left is None else node.left.size
            
            if left_size + 1 == rank:
                # 找到目标节点
                return node
            elif left_size >= rank:
                # 在左子树中查找
                node = node.left
            else:
                # 在右子树中查找，调整rank值
                rank -= left_size + 1
                node = node.right
    
    def build_tree(self, a, l, r):
        """
        【构建树】递归构建Splay树
        时间复杂度: O(n)
        
        参数:
            a: 数组，表示初始序列
            l: 左边界索引
            r: 右边界索引
        返回:
            构建好的子树根节点
        """
        if l > r:
            return None
        
        # 选择中间元素作为根节点，保证初始平衡性
        mid = (l + r) // 2
        node = SplayNode(a[mid])
        
        # 递归构建左右子树
        node.left = self.build_tree(a, l, mid - 1)
        if node.left is not None:
            node.left.father = node
        
        node.right = self.build_tree(a, mid + 1, r)
        if node.right is not None:
            node.right.father = node
        
        # 更新当前节点信息
        self.up(node)
        return node
    
    def add(self, l, r, d):
        """
        【区间加法】将区间[l,r]的每个元素增加d
        时间复杂度: 均摊O(log n)
        
        参数:
            l: 区间左端点（从1开始）
            r: 区间右端点（从1开始）
            d: 增量值
        """
        # 将l-1位置的节点旋转到根
        left_node = self.find(l - 1)
        self.splay(left_node, None)
        
        # 将r+1位置的节点旋转到根的右子节点
        right_node = self.find(r + 1)
        self.splay(right_node, left_node)
        
        # 对right_node的左子树（即区间[l,r]）应用加法
        if right_node.left is not None:
            right_node.left.update = True
            right_node.left.change += d
            right_node.left.min_val += d
            # 更新当前节点及其祖先的值和子树大小
            self.up(right_node)
            self.up(left_node)
    
    def reverse_range(self, l, r):
        """
        【区间翻转】翻转区间[l,r]
        时间复杂度: 均摊O(log n)
        
        参数:
            l: 区间左端点（从1开始）
            r: 区间右端点（从1开始）
        """
        # 将l-1位置的节点旋转到根
        left_node = self.find(l - 1)
        self.splay(left_node, None)
        
        # 将r+1位置的节点旋转到根的右子节点
        right_node = self.find(r + 1)
        self.splay(right_node, left_node)
        
        # 对right_node的左子树（即区间[l,r]）应用翻转
        if right_node.left is not None:
            right_node.left.reverse = not right_node.left.reverse
            # 更新当前节点及其祖先的值和子树大小
            self.up(right_node)
            self.up(left_node)
    
    def revolve(self, l, r, t):
        """
        【区间循环右移】将区间[l,r]循环右移t位
        时间复杂度: 均摊O(log n)
        
        参数:
            l: 区间左端点（从1开始）
            r: 区间右端点（从1开始）
            t: 右移位数
        """
        if t == 0:
            return  # 不需要移动
        
        length = r - l + 1
        t %= length  # 取模避免多余的移动
        
        # 将区间分为两部分：[l, r-t] 和 [r-t+1, r]
        # 然后翻转两次实现循环右移
        if t > 0:
            # 翻转整个区间
            self.reverse_range(l, r)
            # 翻转前半部分
            self.reverse_range(l, l + t - 1)
            # 翻转后半部分
            self.reverse_range(l + t, r)
    
    def insert(self, x, p):
        """
        【插入操作】在位置x后插入元素p
        时间复杂度: 均摊O(log n)
        
        参数:
            x: 插入位置
            p: 要插入的元素值
        """
        # 将x位置的节点旋转到根
        x_node = self.find(x)
        self.splay(x_node, None)
        
        # 将x+1位置的节点旋转到根的右子节点
        x1_node = self.find(x + 1)
        self.splay(x1_node, x_node)
        
        # 创建新节点并插入到x1_node的左子节点位置
        new_node = SplayNode(p)
        x1_node.left = new_node
        new_node.father = x1_node
        
        # 更新相关节点信息
        self.up(x1_node)
        self.up(x_node)
    
    def delete_pos(self, x):
        """
        【删除操作】删除位置x的元素
        时间复杂度: 均摊O(log n)
        
        参数:
            x: 要删除的位置
        """
        # 将x-1位置的节点旋转到根
        left_node = self.find(x - 1)
        self.splay(left_node, None)
        
        # 将x+1位置的节点旋转到根的右子节点
        right_node = self.find(x + 1)
        self.splay(right_node, left_node)
        
        # 删除right_node的左子节点（即位置x的节点）
        right_node.left = None
        
        # 更新相关节点信息
        self.up(right_node)
        self.up(left_node)
    
    def query_min(self, l, r):
        """
        【区间最小值查询】查询区间[l,r]的最小值
        时间复杂度: 均摊O(log n)
        
        参数:
            l: 区间左端点（从1开始）
            r: 区间右端点（从1开始）
        返回:
            区间最小值
        """
        # 将l-1位置的节点旋转到根
        left_node = self.find(l - 1)
        self.splay(left_node, None)
        
        # 将r+1位置的节点旋转到根的右子节点
        right_node = self.find(r + 1)
        self.splay(right_node, left_node)
        
        # right_node的左子树（即区间[l,r]）的最小值
        if right_node.left is not None:
            return right_node.left.min_val
        return self.INF

# 【输入输出优化】在Python中处理大量数据时需要的优化
import sys

def main():
    """
    主函数：处理输入输出和操作调用
    注意：由于Python性能限制，在大规模数据下可能较慢
    但算法逻辑与Java版本完全一致
    """
    try:
        # 从标准输入读取数据
        data = sys.stdin.read().split()
        ptr = 0
        
        # 读取节点数量
        n = int(data[ptr])
        ptr += 1
        
        # 读取初始序列
        a = [int(data[ptr + i]) for i in range(n)]
        ptr += n
        
        # 构造包含哨兵节点的数组
        # 在首尾添加哨兵，使原始数据从位置2开始，方便区间操作
        temp = [0] * (n + 2)
        for i in range(1, n + 1):
            temp[i] = a[i - 1]
        
        # 创建Splay树并初始化
        splay_tree = SplayTree()
        splay_tree.head = splay_tree.build_tree(temp, 0, n + 1)
        
        # 读取操作数量
        m = int(data[ptr])
        ptr += 1
        
        # 处理每个操作
        for _ in range(m):
            op = data[ptr]
            ptr += 1
            
            if op == 'ADD':
                l = int(data[ptr]) + 1  # 转换为从1开始的索引（包括哨兵）
                r = int(data[ptr + 1]) + 1
                d = int(data[ptr + 2])
                ptr += 3
                splay_tree.add(l, r, d)
            
            elif op == 'REVERSE':
                l = int(data[ptr]) + 1
                r = int(data[ptr + 1]) + 1
                ptr += 2
                splay_tree.reverse_range(l, r)
            
            elif op == 'REVOLVE':
                l = int(data[ptr]) + 1
                r = int(data[ptr + 1]) + 1
                t = int(data[ptr + 2])
                ptr += 3
                splay_tree.revolve(l, r, t)
            
            elif op == 'INSERT':
                x = int(data[ptr]) + 1  # 在x后插入，转换为从1开始
                p = int(data[ptr + 1])
                ptr += 2
                splay_tree.insert(x, p)
            
            elif op == 'DELETE':
                x = int(data[ptr]) + 1
                ptr += 1
                splay_tree.delete_pos(x)
            
            elif op == 'MIN':
                l = int(data[ptr]) + 1
                r = int(data[ptr + 1]) + 1
                ptr += 2
                print(splay_tree.query_min(l, r))
    
    except Exception as e:
        # 错误处理，避免程序崩溃
        print(f"Error: {e}")

if __name__ == '__main__':
    # 只在直接运行时执行主函数，便于测试和模块化
    main()

"""
【Python实现工程化考量】

1. 【递归深度限制】
   - Python默认的递归深度限制为1000左右，对于大规模数据，递归构建树可能导致栈溢出
   - 在实际应用中，构建树可能需要改为非递归实现

2. 【性能优化】
   - 使用sys.stdin.read()一次性读取所有输入，避免多次IO操作
   - 使用数组预处理输入数据，提高处理速度
   - 使用变量指针代替切片操作，减少内存分配

3. 【错误处理】
   - 添加try-except块捕获可能的异常，确保程序不会崩溃
   - 处理边界条件，如空树、不存在的位置等

4. 【与其他语言实现的区别】
   - 相比C++和Java，Python的实现更加注重代码可读性
   - 使用类封装节点和树操作，更符合面向对象设计
   - 在性能敏感的场景下，可能需要使用PyPy等替代解释器

5. 【测试与验证】
   - 可以编写单元测试验证各个操作的正确性
   - 使用小数据集手动验证结果
   - 对于POJ 3580，由于Python性能限制，提交时可能需要注意时间限制
"""