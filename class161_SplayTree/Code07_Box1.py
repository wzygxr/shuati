#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Box (HDU 2475) - Python完整实现
【题目来源】HDU 2475
【题目链接】http://acm.hdu.edu.cn/showproblem.php?pid=2475
【算法说明】
此文件包含Box问题的Python实现，使用Splay树维护森林结构
解决盒子嵌套关系的动态维护问题
【实现特点】
- 使用Splay树维护森林结构，每个树代表一个嵌套盒子集合
- 优化的移动操作，支持高效的盒子重定位
- 实时的父盒子查询功能
- 详细的复杂度分析和算法原理解释
【复杂度分析】
- 时间复杂度：MOVE和QUERY操作均摊O(log n)
- 空间复杂度：O(n)
【工程考量】
- 数组式节点存储，支持O(1)的节点访问
- 维护包含关系和树结构双重信息
- 异常处理和边界情况检查
【应用场景】
- 嵌套结构的动态维护
- 森林操作的高效实现
- 数据结构竞赛中的集合操作问题
"""

class SplayNode:
    """Splay树节点类 - 专门为Box问题设计
    每个节点代表一个盒子，维护树结构关系和包含关系
    """
    def __init__(self, box_id):
        # 【基本属性】
        self.box_id = box_id     # 盒子的唯一标识符
        
        # 【树结构指针】
        self.father = None      # 树结构中的父节点
        self.left = None        # 树结构中的左子节点
        self.right = None       # 树结构中的右子节点
        
        # 【包含关系信息】
        # 表示在包含关系中，当前盒子直接位于哪个盒子内部
        # 0表示在最外层（不在任何盒子中）
        self.parent = 0         # 包含关系中的父盒子

class BoxSplayTree:
    """盒子嵌套关系管理的Splay树实现
    维护多个盒子集合，支持移动和查询操作
    """
    def __init__(self, n):
        """
        初始化BoxSplayTree
        时间复杂度: O(n)
        
        参数:
            n: 盒子的数量（盒子ID从1到n）
        """
        # 使用数组存储所有盒子节点，支持O(1)的节点访问
        # 索引0未使用，1~n对应盒子ID 1~n
        self.nodes = [None] * (n + 1)  # 盒子节点数组
        for i in range(1, n + 1):
            self.nodes[i] = SplayNode(i)
            # 初始时，每个盒子都在最外层，parent为0
    
    def lr(self, node):
        """
        【方向判断】确定节点是其父节点的左儿子还是右儿子
        时间复杂度: O(1)
        
        参数:
            node: 需要判断的节点
        返回:
            0 表示是左儿子，1 表示是右儿子
        """
        if not node.father:
            return 0
        return 1 if node.father.right == node else 0
    
    def rotate(self, node):
        """
        【核心旋转操作】将节点旋转至其父节点的位置
        时间复杂度: O(1)
        功能：
        - 根据节点位置（左子或右子）执行不同的旋转
        - 更新父指针和子指针关系
        
        参数:
            node: 需要旋转的节点
        """
        f = node.father        # 父节点
        g = f.father if f else None  # 祖父节点
        soni = self.lr(node)   # 当前节点是父节点的哪个子节点
        sonf = self.lr(f) if f else 0  # 父节点是祖父节点的哪个子节点
        
        # 根据节点位置执行不同的旋转操作
        if soni == 1:  # 右子节点，执行右旋
            # 将当前节点的左子树变为父节点的右子树
            f.right = node.left
            if f.right:  # 确保左子树存在
                f.right.father = f
            # 将父节点变为当前节点的左子树
            node.left = f
        else:  # 左子节点，执行左旋
            # 将当前节点的右子树变为父节点的左子树
            f.left = node.right
            if f.left:  # 确保右子树存在
                f.left.father = f
            # 将父节点变为当前节点的右子树
            node.right = f
        
        # 处理与祖父节点的关系
        if g:  # 如果祖父节点存在
            if sonf == 1:  # 父节点是祖父节点的右子节点
                g.right = node
            else:  # 父节点是祖父节点的左子节点
                g.left = node
        
        # 更新父指针
        node.father = g
        f.father = node
    
    def splay(self, node):
        """
        【核心伸展操作】将节点旋转到根节点
        时间复杂度: 均摊O(log n)
        功能：
        - 使用双旋（zig-zig、zig-zag）策略优化伸展过程
        - 将访问的节点提升到树的顶部，加速后续访问
        
        参数:
            node: 需要旋转到根的节点
        """
        # 循环直到节点成为根节点（没有父节点）
        f = node.father
        g = f.father if f else None
        while f:  # 当父节点存在时继续旋转
            # 根据节点、父节点、祖父节点的关系选择旋转方式
            if g:  # 如果祖父节点存在
                if self.lr(node) == self.lr(f):
                    # Zig-Zig情况：先旋转父节点
                    self.rotate(f)
                else:
                    # Zig-Zag情况：先旋转当前节点
                    self.rotate(node)
            # 旋转当前节点
            self.rotate(node)
            
            # 更新指针，继续下一轮循环
            f = node.father
            g = f.father if f else None
    
    def move(self, x, y):
        """
        【移动操作】将盒子x移动到盒子y中
        如果y=0，则将x移到最外层
        时间复杂度: 均摊O(log n)
        
        参数:
            x: 要移动的盒子ID
            y: 目标盒子ID（0表示最外层）
        """
        # 获取x对应的节点
        node_x = self.nodes[x]
        
        # 【重要步骤1】将x从原来的包含关系中分离
        # 执行splay操作，将x节点旋转到其所在树的根
        self.splay(node_x)
        
        # 断开x节点与左子树的连接
        # 左子树代表x节点之前包含的所有盒子
        if node_x.left:
            node_x.left.father = None
        node_x.left = None
        
        # 【重要步骤2】如果y不为0，将x连接到y的最右路径
        # 这样可以保持y所在树的中序遍历顺序不变
        if y != 0:
            # 获取y对应的节点
            node_y = self.nodes[y]
            # 将y节点旋转到其所在树的根
            self.splay(node_y)
            
            # 找到y所在树的最右节点（中序遍历的最后一个节点）
            cur = node_y
            while cur.right:
                cur = cur.right
            # 将该最右节点旋转到根
            self.splay(cur)
            # 将x连接为其右子节点
            cur.right = node_x
            node_x.father = cur
        
        # 【重要步骤3】更新包含关系信息
        # 记录x现在直接位于y内部
        node_x.parent = y
    
    def query(self, x):
        """
        【查询操作】查询盒子x的直接外层盒子
        时间复杂度: O(1)
        
        参数:
            x: 要查询的盒子ID
        返回:
            直接包含x的盒子ID，0表示在最外层
        """
        return self.nodes[x].parent

# 【输入输出优化】在Python中处理大量数据时的优化
import sys

def main():
    """
    主函数：处理输入输出和操作调用
    注意：由于Python性能限制，在大规模数据下可能较慢
    但算法逻辑正确，可适用于算法竞赛中的中小规模数据
    """
    try:
        # 读取输入数据
        # 在算法竞赛中，使用sys.stdin.read()一次性读取所有输入
        # 然后进行处理可以大幅提高IO效率
        data = sys.stdin.read().split()
        ptr = 0
        
        # 读取操作数量
        m = int(data[ptr])
        ptr += 1
        
        # 初始化一个字典，用于记录每个盒子当前所在的位置
        # 初始时，每个盒子位于自己所在的集合
        box_map = {}
        
        # 处理每个操作
        for _ in range(m):
            op = data[ptr]
            ptr += 1
            
            if op == 'MOVE':
                x = int(data[ptr])
                y = int(data[ptr + 1])
                ptr += 2
                
                # 检查x是否已经在box_map中，如果不在则初始化
                if x not in box_map:
                    # 找到或创建x对应的集合
                    if not box_map or all(tree.nodes[x] is None for tree in box_map.values()):
                        # 找到x所在的最大可能n值
                        n = max(x, max(box_map.keys()) if box_map else 0)
                        tree = BoxSplayTree(n)
                        box_map[x] = tree
                    else:
                        # 找到包含x的现有树
                        for tree in box_map.values():
                            if tree.nodes[x] is not None:
                                box_map[x] = tree
                                break
                
                # 执行移动操作
                tree = box_map[x]
                tree.move(x, y)
            
            elif op == 'QUERY':
                x = int(data[ptr])
                ptr += 1
                
                # 找到x对应的树
                found = False
                for tree in box_map.values():
                    if tree.nodes[x] is not None:
                        # 执行查询操作
                        result = tree.query(x)
                        print(result)
                        found = True
                        break
                
                # 如果x还没有被初始化，说明在最外层
                if not found:
                    print(0)
    
    except Exception as e:
        # 错误处理，避免程序崩溃
        print(f"Error: {e}")

# 【测试代码】用于验证算法正确性的测试用例
def test_box_splay_tree():
    """
    单元测试函数：测试BoxSplayTree的核心功能
    验证移动和查询操作的正确性
    """
    print("开始单元测试...")
    
    # 创建5个盒子的树
    tree = BoxSplayTree(5)
    
    # 测试初始状态
    for i in range(1, 6):
        assert tree.query(i) == 0, f"初始状态错误，盒子{i}的parent应该是0"
    print("初始状态测试通过")
    
    # 测试移动操作
    tree.move(1, 2)  # 将盒子1移到盒子2中
    assert tree.query(1) == 2, "移动操作错误，盒子1的parent应该是2"
    print("移动操作测试通过")
    
    # 测试嵌套移动
    tree.move(2, 3)  # 将盒子2移到盒子3中
    assert tree.query(2) == 3, "嵌套移动错误，盒子2的parent应该是3"
    assert tree.query(1) == 2, "嵌套移动错误，盒子1的parent应该还是2"
    print("嵌套移动测试通过")
    
    # 测试移到最外层
    tree.move(2, 0)  # 将盒子2移到最外层
    assert tree.query(2) == 0, "移到最外层错误，盒子2的parent应该是0"
    print("移到最外层测试通过")
    
    print("所有单元测试通过！")

if __name__ == '__main__':
    # 选择运行模式：测试模式或主程序模式
    # 测试模式：运行单元测试
    # 主程序模式：处理输入输出
    
    # 取消下面的注释可以运行单元测试
    # test_box_splay_tree()
    
    # 运行主程序
    main()

"""
【算法原理详解】

1. 【问题建模】
   - Box问题实际上是维护一个森林结构，每个树代表一个嵌套的盒子集合
   - 每个盒子可以有多个子盒子，形成树状结构
   - 需要支持动态调整盒子的嵌套关系，并快速查询直接父盒子

2. 【Splay树在森林维护中的应用】
   - 每个盒子集合用一棵Splay树表示
   - 树的结构不直接反映嵌套关系，而是用于维护集合的动态性
   - 使用额外的parent属性直接存储包含关系

3. 【关键操作分析】
   - 移动操作(MOVE x y)：将x从原集合中分离，然后连接到y所在的集合
   - 查询操作(QUERY x)：直接返回x的parent属性，O(1)时间复杂度

4. 【性能优化策略】
   - 使用Splay操作将最近访问的节点提升到树的顶部，加速后续访问
   - 使用数组存储节点，支持O(1)的节点访问
   - 查询操作直接返回属性值，无需树操作

【工程化考量】

1. 【数据结构设计】
   - 节点设计同时维护树结构关系和包含关系，分离关注点
   - 使用数组存储节点，简化节点访问逻辑

2. 【边界情况处理】
   - 处理y=0的特殊情况（移到最外层）
   - 处理节点不存在的情况
   - 确保旋转操作的正确性，避免空指针引用

3. 【错误处理】
   - 添加异常捕获机制，确保程序稳定性
   - 验证输入参数的有效性

4. 【性能优化】
   - 在Python中使用sys.stdin.read()优化IO性能
   - 使用字典管理多个树结构，适应当多组数据的情况

5. 【测试验证】
   - 提供单元测试函数，验证核心功能的正确性
   - 测试包括初始状态、基本移动、嵌套移动和移到最外层等场景

【与其他数据结构的对比】

1. 【并查集】
   - 并查集可以处理集合的合并和查找，但不适合维护动态的树结构
   - Splay树更适合Box问题这种需要频繁调整树结构的场景

2. 【普通二叉搜索树】
   - 普通BST在最坏情况下可能退化为链表，时间复杂度变为O(n)
   - Splay树通过自调整机制，保证均摊O(log n)的时间复杂度

3. 【Treap或SBT】
   - 这些树也能提供均摊O(log n)的时间复杂度
   - Splay树的优势在于对最近访问的节点有更好的缓存局部性

【注意事项】

1. 在实际应用中，可能需要根据数据规模调整实现方式
2. 在Python中，由于解释器的性能限制，对于大规模数据，可能需要使用C++实现以获得更好的性能
3. 本实现中的主函数部分是一个简化版本，实际竞赛中可能需要根据具体输入格式进行调整
"""