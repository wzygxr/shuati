"""
洛谷 P2596 [ZJOI2006]书架
题目链接: https://www.luogu.com.cn/problem/P2596
题目描述: 维护一个书架，支持以下操作：
1. 将某本书置于顶部 (Top x)
2. 将某本书置于底部 (Bottom x)
3. 将某本书置于指定位置 (Insert x y)
4. 询问某本书在书架上的位置 (Ask x)
5. 从顶部取书 (Query Top)
6. 从底部取书 (Query Bottom)
"""

import random

class Node:
    def __init__(self, key):
        self.key = key          # 书的编号
        self.priority = random.randint(0, 2**31-1)  # 随机优先级
        self.size = 1           # 子树大小
        self.position = 0       # 书在书架上的位置
        self.left = None        # 左子节点
        self.right = None       # 右子节点

class Code17_Bookshelf2:
    def __init__(self):
        self.root = None        # 根节点
        self.node_cnt = 0       # 节点计数
    
    def update(self, node):
        """更新节点信息"""
        if node:
            left_size = node.left.size if node.left else 0
            right_size = node.right.size if node.right else 0
            node.size = left_size + right_size + 1
    
    def split_by_position(self, root, pos):
        """按位置分裂，将树按照位置pos分裂为两棵树"""
        if not root:
            return None, None
        
        left_size = root.left.size if root.left else 0
        if left_size + 1 <= pos:
            left_tree, right_tree = self.split_by_position(root.right, pos - left_size - 1)
            root.right = left_tree
            self.update(root)
            return root, right_tree
        else:
            left_tree, right_tree = self.split_by_position(root.left, pos)
            root.left = right_tree
            self.update(root)
            return left_tree, root
    
    def split_by_book_id(self, root, book_id):
        """按书编号分裂，将树按照书编号book_id分裂为两棵树"""
        if not root:
            return None, None
        
        if root.key <= book_id:
            left_tree, right_tree = self.split_by_book_id(root.right, book_id)
            root.right = left_tree
            self.update(root)
            return root, right_tree
        else:
            left_tree, right_tree = self.split_by_book_id(root.left, book_id)
            root.left = right_tree
            self.update(root)
            return left_tree, root
    
    def merge(self, left, right):
        """合并操作，将两棵树合并为一棵树"""
        if not left:
            return right
        if not right:
            return left
        
        if left.priority >= right.priority:
            left.right = self.merge(left.right, right)
            self.update(left)
            return left
        else:
            right.left = self.merge(left, right.left)
            self.update(right)
            return right
    
    def find_position(self, root, book_id):
        """查找书的位置"""
        if not root:
            return -1
        if root.key == book_id:
            return root.position
        elif root.key > book_id:
            return self.find_position(root.left, book_id)
        else:
            return self.find_position(root.right, book_id)
    
    def find_book_by_position(self, root, pos):
        """根据位置查找书"""
        if not root:
            return -1
        
        left_size = root.left.size if root.left else 0
        if left_size + 1 == pos:
            return root.key
        elif left_size + 1 > pos:
            return self.find_book_by_position(root.left, pos)
        else:
            return self.find_book_by_position(root.right, pos - left_size - 1)
    
    def update_position(self, node, delta):
        """更新子树中所有书的位置"""
        if not node:
            return
        node.position += delta
        self.update_position(node.left, delta)
        self.update_position(node.right, delta)
    
    def build(self, n):
        """构建初始书架"""
        for i in range(1, n + 1):
            new_node = Node(i)
            new_node.position = i
            self.root = self.merge(self.root, new_node)
    
    def top(self, book_id):
        """将书置于顶部"""
        # 查找书的位置
        pos = self.find_position(self.root, book_id)
        if pos == -1 or pos == 1:
            return  # 书不存在或已在顶部
        
        # 分裂出前pos-1本书
        left_tree, right_tree = self.split_by_position(self.root, pos - 1)
        
        # 分裂出前pos本书
        middle_tree, right_right_tree = self.split_by_position(right_tree, pos)
        
        # 取出要移动的书
        if middle_tree:  # 添加空值检查
            middle_tree.position = 1
        
        # 更新位置信息
        self.update_position(left_tree, 1)  # 前面的书位置后移
        self.update_position(right_right_tree, -1)  # 后面的书位置前移
        
        # 重新合并树
        self.root = self.merge(self.merge(middle_tree, left_tree), right_right_tree)
    
    def bottom(self, book_id):
        """将书置于底部"""
        # 查找书的位置
        pos = self.find_position(self.root, book_id)
        if pos == -1:
            return  # 书不存在
        
        total_size = self.root.size if self.root else 0
        if pos == total_size:
            return  # 书已在底部
        
        # 分裂出前pos本书
        left_tree, right_tree = self.split_by_position(self.root, pos)
        
        # 分裂出前pos+1本书
        book, right_right_tree = self.split_by_position(right_tree, 1)
        
        # 更新位置信息
        if book:  # 添加空值检查
            book.position = total_size
        self.update_position(left_tree, 1)  # 前面的书位置后移
        self.update_position(right_right_tree, -1)  # 后面的书位置前移
        
        # 重新合并树
        self.root = self.merge(self.merge(left_tree, right_right_tree), book)
    
    def ask(self, book_id):
        """查询书的位置"""
        return self.find_position(self.root, book_id)
    
    def query(self, pos):
        """查询指定位置的书"""
        return self.find_book_by_position(self.root, pos)

# 测试函数
def main():
    bookshelf = Code17_Bookshelf2()
    
    # 初始化书架，放入1到3本书
    bookshelf.build(3)
    
    # 示例操作
    bookshelf.top(2)  # 将书2移到顶部
    print("Book at position 1:", bookshelf.query(1))  # 应该输出2
    print("Book at position 2:", bookshelf.query(2))  # 应该输出1
    print("Book at position 3:", bookshelf.query(3))  # 应该输出3
    
    bookshelf.bottom(1)  # 将书1移到底部
    print("Book at position 1:", bookshelf.query(1))  # 应该输出2
    print("Book at position 2:", bookshelf.query(2))  # 应该输出3
    print("Book at position 3:", bookshelf.query(3))  # 应该输出1
    
    print("Position of book 2:", bookshelf.ask(2))  # 应该输出1
    print("Position of book 3:", bookshelf.ask(3))  # 应该输出2
    print("Position of book 1:", bookshelf.ask(1))  # 应该输出3

if __name__ == "__main__":
    main()