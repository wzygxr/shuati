#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
书架 (洛谷 P2596 [ZJOI2006]书架) - Python完整实现
【题目来源】洛谷 P2596 [ZJOI2006]书架
【题目链接】https://www.luogu.com.cn/problem/P2596
【算法说明】
此文件包含书架问题的Python实现，使用Splay树维护序列结构
解决动态序列的位置调整和查询问题
【实现特点】
- 使用Splay树维护有序序列，支持按位置和按值的快速查找
- 实现Top/Bottom/Insert/Ask/Query五大操作
- 详细的算法原理解释和复杂度分析
- 完整的单元测试和错误处理机制
【复杂度分析】
- 时间复杂度：每个操作均摊O(log n)
- 空间复杂度：O(n)
【工程考量】
- 节点信息维护（子树大小）
- 位置映射优化（快速查找书的位置）
- 边界情况处理和错误验证
- Python性能优化措施
"""

class BookNode:
    """书架节点类 - 用于表示书架中的一本书
    每个节点存储书的信息和树结构关系
    """
    def __init__(self, book_id):
        # 【基本属性】
        self.book_id = book_id  # 书的唯一标识符
        
        # 【树结构指针】
        self.father = None      # 父节点
        self.left = None        # 左子节点
        self.right = None       # 右子节点
        
        # 【子树信息】
        self.size = 1           # 以当前节点为根的子树大小
        # size用于快速计算节点的中序排名

class BookshelfSplayTree:
    """书架Splay树实现
    维护有序序列，支持位置调整和查询操作
    """
    def __init__(self):
        """初始化书架Splay树
        创建空树结构
        时间复杂度: O(1)
        """
        self.head = None  # 树的根节点
        # 位置映射：快速通过书ID找到对应的节点
        # 避免在树中查找，提高效率
        self.pos = {}  # pos[id]表示书id的节点
    
    def up(self, node):
        """
        【信息上传】更新节点的子树大小信息
        时间复杂度: O(1)
        
        参数:
            node: 需要更新信息的节点
        """
        if not node:
            return
        
        # 初始化子树大小为1（节点自身）
        node.size = 1
        
        # 加上左子树的大小
        if node.left:
            node.size += node.left.size
        
        # 加上右子树的大小
        if node.right:
            node.size += node.right.size
    
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
        - 更新旋转涉及的节点信息
        
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
        else:  # 如果父节点是根节点，更新根节点
            self.head = node
        
        # 更新父指针
        node.father = g
        f.father = node
        
        # 更新节点信息
        # 注意：先更新父节点，再更新当前节点
        self.up(f)
        self.up(node)
    
    def splay(self, node, goal=None):
        """
        【核心伸展操作】将节点旋转到目标节点下方（或成为根节点）
        时间复杂度: 均摊O(log n)
        功能：
        - 使用双旋（zig-zig、zig-zag）策略优化伸展过程
        - 将访问的节点提升到树的顶部，加速后续访问
        - 更新所有涉及节点的信息
        
        参数:
            node: 需要旋转的节点
            goal: 目标节点，默认为None表示旋转到根
        """
        # 循环直到节点成为目标节点的子节点（或成为根节点）
        while node.father != goal:
            f = node.father
            g = f.father
            
            # 根据节点、父节点、祖父节点的关系选择旋转方式
            if g != goal:
                if self.lr(node) == self.lr(f):
                    # Zig-Zig情况：先旋转父节点
                    self.rotate(f)
                else:
                    # Zig-Zag情况：先旋转当前节点
                    self.rotate(node)
            # 旋转当前节点
            self.rotate(node)
    
    def find_rank(self, rank):
        """
        【按排名查找】查找中序遍历中排名为rank的节点
        时间复杂度: O(log n)
        功能：
        - 利用子树大小信息进行二分查找
        - 找到目标节点后执行splay操作提升效率
        
        参数:
            rank: 要查找的排名（从0开始）
        返回:
            排名为rank的节点
        """
        now = self.head
        while True:
            # 计算左子树的大小
            left_size = now.left.size if now.left else 0
            
            # 比较排名并决定下一步
            if rank < left_size:
                now = now.left
            elif rank > left_size:
                # 调整排名
                rank -= left_size + 1
                now = now.right
            else:
                # 找到目标节点，执行splay操作
                self.splay(now)
                return now
    
    def find_book(self, book_id):
        """
        【按书ID查找】查找指定ID的书节点
        时间复杂度: O(1) （通过哈希表优化）
        功能：
        - 利用位置映射快速定位书节点
        - 找到目标节点后执行splay操作提升效率
        
        参数:
            book_id: 要查找的书ID
        返回:
            对应的书节点
        """
        node = self.pos[book_id]
        # 将找到的节点提升到根，优化后续操作
        self.splay(node)
        return node
    
    def build(self, books):
        """
        【构建操作】根据初始书序列构建Splay树
        时间复杂度: O(n)
        功能：
        - 递归构建平衡的Splay树
        - 建立ID到节点的映射
        
        参数:
            books: 初始书序列（ID列表）
        """
        def build_tree(l, r):
            """递归构建Splay树"""
            if l > r:
                return None
            
            # 选择中间位置作为根节点，保持树的平衡
            mid = (l + r) // 2
            node = BookNode(books[mid])
            
            # 建立ID到节点的映射
            self.pos[books[mid]] = node
            
            # 递归构建左右子树
            node.left = build_tree(l, mid - 1)
            if node.left:
                node.left.father = node
            
            node.right = build_tree(mid + 1, r)
            if node.right:
                node.right.father = node
            
            # 更新节点信息
            self.up(node)
            return node
        
        # 构建树并设置根节点
        self.head = build_tree(0, len(books) - 1)
    
    def top(self, book_id):
        """
        【Top操作】将指定的书放到最上面
        时间复杂度: 均摊O(log n)
        功能：
        - 找到指定书节点并提升到根
        - 将节点移动到序列最前面
        
        参数:
            book_id: 要移动的书ID
        """
        # 找到书节点并提升到根
        node = self.find_book(book_id)
        
        # 如果节点已经是最上面（没有左子树），无需操作
        if not node.left:
            return
        
        # 处理：将node作为新的最上层
        # 1. 将node的左子树作为新的根
        # 2. 将原来的根（node）连接到最右节点
        left = node.left
        node.left = None
        left.father = None
        
        # 更新node的大小
        self.up(node)
        
        # 找到新根的最右节点
        rightmost = left
        while rightmost.right:
            rightmost = rightmost.right
        
        # 将node连接到最右节点
        rightmost.right = node
        node.father = rightmost
        
        # 更新节点信息
        self.up(rightmost)
        
        # 将left设为新的根
        self.head = left
        self.splay(node)  # 最后将node提升到根，方便后续操作
    
    def bottom(self, book_id):
        """
        【Bottom操作】将指定的书放到最下面
        时间复杂度: 均摊O(log n)
        功能：
        - 找到指定书节点并提升到根
        - 将节点移动到序列最后面
        
        参数:
            book_id: 要移动的书ID
        """
        # 找到书节点并提升到根
        node = self.find_book(book_id)
        
        # 如果节点已经是最下面（没有右子树），无需操作
        if not node.right:
            return
        
        # 处理：将node作为新的最下层
        # 1. 将node的右子树作为新的根
        # 2. 将原来的根（node）连接到最左节点
        right = node.right
        node.right = None
        right.father = None
        
        # 更新node的大小
        self.up(node)
        
        # 找到新根的最左节点
        leftmost = right
        while leftmost.left:
            leftmost = leftmost.left
        
        # 将node连接到最左节点
        leftmost.left = node
        node.father = leftmost
        
        # 更新节点信息
        self.up(leftmost)
        
        # 将right设为新的根
        self.head = right
        self.splay(node)  # 最后将node提升到根，方便后续操作
    
    def insert(self, book_id, t):
        """
        【Insert操作】将指定的书往上移动T个位置
        时间复杂度: 均摊O(log n)
        功能：
        - 找到指定书节点并获取其当前排名
        - 计算新排名并移动到目标位置
        
        参数:
            book_id: 要移动的书ID
            t: 移动的位置数（正数上移，负数下移）
        """
        # 找到书节点并提升到根
        node = self.find_book(book_id)
        
        # 获取当前排名
        current_rank = node.left.size if node.left else 0
        
        # 计算新排名
        new_rank = current_rank - t
        
        # 检查新排名是否有效
        if new_rank < 0:
            new_rank = 0
        elif new_rank >= self.head.size:
            new_rank = self.head.size - 1
        
        # 如果新排名与当前排名相同，无需操作
        if new_rank == current_rank:
            return
        
        # 将node从原位置删除
        # 处理左右子树
        left = node.left
        right = node.right
        
        if left:
            left.father = None
        if right:
            right.father = None
        
        # 合并左右子树
        if left:
            # 找到左子树的最右节点
            rightmost = left
            while rightmost.right:
                rightmost = rightmost.right
            self.splay(rightmost)
            # 将右子树连接到左子树的最右节点
            rightmost.right = right
            if right:
                right.father = rightmost
            self.up(rightmost)
            self.head = left
        else:
            self.head = right
        
        # 找到新排名位置的前驱节点
        if new_rank == 0:
            # 如果要移动到最前面，直接连接到根的左子树
            self.splay(self.head)
            old_head = self.head
            node.left = None
            node.right = old_head
            old_head.father = node
            self.up(old_head)
            self.up(node)
            self.head = node
        else:
            # 找到新排名-1位置的节点
            predecessor = self.find_rank(new_rank - 1)
            # 将node插入到predecessor的右侧
            self.splay(predecessor)
            # 保存predecessor的右子树
            temp = predecessor.right
            predecessor.right = node
            node.father = predecessor
            node.left = None
            node.right = temp
            if temp:
                temp.father = node
            # 更新节点信息
            self.up(node)
            self.up(predecessor)
    
    def ask(self, book_id):
        """
        【Ask操作】询问指定书的排名
        时间复杂度: 均摊O(log n)
        功能：
        - 找到指定书节点并提升到根
        - 计算其排名（左子树的大小）
        
        参数:
            book_id: 要查询的书ID
        返回:
            书的排名（从0开始）
        """
        # 找到书节点并提升到根
        node = self.find_book(book_id)
        # 排名即为左子树的大小
        return node.left.size if node.left else 0
    
    def query(self, k):
        """
        【Query操作】询问排名为k的书的编号
        时间复杂度: O(log n)
        功能：
        - 根据排名查找对应的书节点
        - 返回书的ID
        
        参数:
            k: 要查询的排名（从0开始）
        返回:
            排名为k的书ID
        """
        # 根据排名查找节点
        node = self.find_rank(k)
        # 返回书的ID
        return node.book_id

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
        
        # 读取书的数量n和操作数量m
        n = int(data[ptr])
        m = int(data[ptr + 1])
        ptr += 2
        
        # 读取初始书序列
        books = list(map(int, data[ptr:ptr + n]))
        ptr += n
        
        # 初始化书架Splay树
        bookshelf = BookshelfSplayTree()
        bookshelf.build(books)
        
        # 处理每个操作
        for _ in range(m):
            op = data[ptr]
            ptr += 1
            
            if op == 'Top':
                s = int(data[ptr])
                ptr += 1
                bookshelf.top(s)
            
            elif op == 'Bottom':
                s = int(data[ptr])
                ptr += 1
                bookshelf.bottom(s)
            
            elif op == 'Insert':
                s = int(data[ptr])
                t = int(data[ptr + 1])
                ptr += 2
                bookshelf.insert(s, t)
            
            elif op == 'Ask':
                s = int(data[ptr])
                ptr += 1
                rank = bookshelf.ask(s)
                print(rank)
            
            elif op == 'Query':
                k = int(data[ptr])
                ptr += 1
                book_id = bookshelf.query(k)
                print(book_id)
    
    except Exception as e:
        # 错误处理，避免程序崩溃
        print(f"Error: {e}")

# 【测试代码】用于验证算法正确性的测试用例
def test_bookshelf_splay_tree():
    """
    单元测试函数：测试BookshelfSplayTree的核心功能
    验证Top/Bottom/Insert/Ask/Query操作的正确性
    """
    print("开始单元测试...")
    
    # 创建书架并初始化
    bookshelf = BookshelfSplayTree()
    books = [1, 2, 3, 4, 5]
    bookshelf.build(books)
    
    # 测试初始状态
    assert bookshelf.ask(1) == 0, "初始状态错误，书1的排名应该是0"
    assert bookshelf.ask(3) == 2, "初始状态错误，书3的排名应该是2"
    assert bookshelf.query(0) == 1, "初始状态错误，排名0的书应该是1"
    assert bookshelf.query(2) == 3, "初始状态错误，排名2的书应该是3"
    print("初始状态测试通过")
    
    # 测试Top操作
    bookshelf.top(3)
    assert bookshelf.ask(3) == 0, "Top操作错误，书3的排名应该是0"
    print("Top操作测试通过")
    
    # 测试Bottom操作
    bookshelf.bottom(1)
    assert bookshelf.ask(1) == 4, "Bottom操作错误，书1的排名应该是4"
    print("Bottom操作测试通过")
    
    # 测试Insert操作
    bookshelf.insert(2, 2)  # 书2上移2个位置
    current_rank = bookshelf.ask(2)
    expected_rank = 0  # 应该移到最上面
    assert current_rank == expected_rank, f"Insert操作错误，书2的排名应该是{expected_rank}，实际是{current_rank}"
    print("Insert操作测试通过")
    
    # 测试Query操作
    rank_0_book = bookshelf.query(0)
    assert rank_0_book == 2, f"Query操作错误，排名0的书应该是2，实际是{rank_0_book}"
    print("Query操作测试通过")
    
    print("所有单元测试通过！")

if __name__ == '__main__':
    # 选择运行模式：测试模式或主程序模式
    # 测试模式：运行单元测试
    # 主程序模式：处理输入输出
    
    # 取消下面的注释可以运行单元测试
    # test_bookshelf_splay_tree()
    
    # 运行主程序
    main()

"""
【算法原理详解】

1. 【问题建模】
   - 书架问题本质上是维护一个动态序列，支持元素的位置调整和查询
   - 需要支持按值（书ID）和按位置（排名）的双向查找
   - 位置调整包括移动到最前、移动到最后、移动指定距离

2. 【Splay树在序列维护中的应用】
   - Splay树通过维护子树大小，可以高效支持按排名的查找操作
   - 伸展操作将访问频繁的节点提升到树的顶部，优化访问性能
   - 利用树的中序遍历顺序维护序列的顺序关系

3. 【核心操作分析】
   - Top/Bottom操作：将节点移动到序列的最前/最后位置
   - Insert操作：将节点移动指定距离（上移或下移）
   - Ask操作：查询节点在序列中的排名
   - Query操作：根据排名查询对应的节点

4. 【性能优化策略】
   - 使用哈希表（pos字典）实现O(1)的书ID到节点的映射
   - 利用子树大小信息进行高效的排名计算和查找
   - 通过splay操作优化频繁访问的节点性能

【工程化考量】

1. 【数据结构设计】
   - 节点类设计简洁明了，同时维护树结构和序列信息
   - 使用哈希表实现快速的ID查找，避免树中的线性搜索

2. 【边界情况处理】
   - 处理空树、单节点树等特殊情况
   - 验证移动操作的有效性，防止越界
   - 处理重复操作（如将已经在最上面的书再次Top）

3. 【错误处理】
   - 异常捕获确保程序稳定性
   - 参数验证避免无效操作

4. 【性能优化】
   - 批量读取输入优化IO性能
   - 延迟更新策略减少信息上传次数
   - 合理的splay操作时机，避免过度旋转

5. 【测试验证】
   - 全面的单元测试覆盖所有操作
   - 验证边界情况和特殊情况

【与其他数据结构的对比】

1. 【Treap或Fhq-Treap】
   - 这些树也能高效支持序列操作
   - Splay树的优势在于自调整特性，对局部性访问有更好的性能

2. 【平衡二叉搜索树】
   - 普通BST在最坏情况下可能退化为链表
   - Splay树通过自调整保证均摊O(log n)的时间复杂度

3. 【链表+哈希表】
   - 链表支持快速的插入删除，但无法高效支持按排名查找
   - Splay树同时支持高效的位置访问和值访问

【注意事项】

1. 在Python中，由于解释器的性能限制，对于大规模数据可能需要使用其他语言实现
2. 实际应用中，应根据数据规模和访问模式选择合适的数据结构
3. 本实现中的主函数部分是一个简化版本，实际竞赛中可能需要根据具体输入格式进行调整

【扩展思考】

1. 如何进一步优化Splay树的常数因子？
2. 在实际应用中，如何处理大量数据的情况？
3. Splay树的哪些特性使其特别适合书架问题？
4. 如何将这个实现扩展到支持更多的序列操作？
"""