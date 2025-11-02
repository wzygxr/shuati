import sys
import random

class LinkCutTree:
    """
    Link-Cut Tree (LCT) Python 实现
    支持操作：
    - 路径聚合（求和、最小值、最大值、异或）
    - 连通性检查
    - link/cut 操作
    
    时间复杂度：所有操作均为 O(log n) 均摊
    空间复杂度：O(n)
    
    设计要点：
    1. 实现在access路径上的路径聚合
    2. 支持splay操作维护平衡
    3. 工程化考量：异常处理、边界检查
    
    典型应用场景：
    - 动态树问题
    - 路径查询和修改
    - 连通性维护
    """
    
    class Node:
        def __init__(self, value=0):
            self.value = value        # 节点值
            self.sum = value          # 子树和
            self.min_val = value      # 子树最小值
            self.max_val = value      # 子树最大值
            self.xor = value          # 子树异或
            self.rev = False          # 翻转标记
            self.left = None          # 左子树（access路径上的左节点）
            self.right = None         # 右子树（access路径上的右节点）
            self.parent = None        # 父节点
        
        def is_root(self):
            """判断是否是splay树的根节点"""
            return self.parent is None or (
                self.parent.left != self and self.parent.right != self
            )
        
        def push_up(self):
            """上传信息"""
            self.sum = self.value
            self.min_val = self.value
            self.max_val = self.value
            self.xor = self.value
            
            if self.left:
                self.sum += self.left.sum
                self.min_val = min(self.min_val, self.left.min_val)
                self.max_val = max(self.max_val, self.left.max_val)
                self.xor ^= self.left.xor
            
            if self.right:
                self.sum += self.right.sum
                self.min_val = min(self.min_val, self.right.min_val)
                self.max_val = max(self.max_val, self.right.max_val)
                self.xor ^= self.right.xor
        
        def push_down(self):
            """下传翻转标记"""
            if self.rev:
                self.left, self.right = self.right, self.left
                if self.left:
                    self.left.rev ^= True
                if self.right:
                    self.right.rev ^= True
                self.rev = False
    
    def __init__(self, n=None, values=None):
        """初始化LCT
        
        Args:
            n: 节点数量
            values: 节点值列表
        """
        self.nodes = []
        if n is not None:
            if values is None:
                values = [0] * n
            elif len(values) != n:
                raise ValueError("Values length must match n")
            
            for i in range(n):
                self.nodes.append(self.Node(values[i]))
    
    def new_node(self, value=0):
        """创建新节点"""
        node = self.Node(value)
        self.nodes.append(node)
        return node
    
    def rotate(self, x):
        """旋转操作"""
        y = x.parent
        z = y.parent
        
        # 确定x是y的左孩子还是右孩子
        is_left = (y.left == x)
        
        # 更新父子关系
        if is_left:
            y.left = x.right
            if x.right:
                x.right.parent = y
            x.right = y
        else:
            y.right = x.left
            if x.left:
                x.left.parent = y
            x.left = y
        
        # 更新y的父指针
        x.parent = z
        y.parent = x
        
        # 更新z的子节点指针
        if z:
            if z.left == y:
                z.left = x
            elif z.right == y:
                z.right = x
        
        # 先更新y的信息，再更新x的信息
        y.push_up()
        x.push_up()
    
    def splay(self, x):
        """伸展操作，将x旋转到所在splay树的根"""
        # 辅助栈用于下传标记
        stack = []
        current = x
        while current:
            stack.append(current)
            current = current.parent
        
        # 从根到叶子下传标记
        while stack:
            stack.pop().push_down()
        
        # 进行伸展
        while not x.is_root():
            y = x.parent
            z = y.parent
            
            if not y.is_root():
                # 判断是zig-zig还是zig-zag
                if (y.left == x) == (z.left == y):
                    # zig-zig
                    self.rotate(y)
                else:
                    # zig-zag
                    self.rotate(x)
            # 最后一次旋转
            self.rotate(x)
    
    def access(self, x):
        """access操作，打通x到根的路径"""
        last = None
        current = x
        
        while current:
            self.splay(current)
            current.right = last
            current.push_up()
            last = current
            current = current.parent
        
        self.splay(x)
    
    def make_root(self, x):
        """将x设为原树的根"""
        self.access(x)
        x.rev ^= True
        self.splay(x)
    
    def find_root(self, x):
        """找到x所在树的根"""
        self.access(x)
        while x.left:
            x.push_down()
            x = x.left
        self.splay(x)  # 优化后续操作
        return x
    
    def split(self, x, y):
        """分割x和y所在的树，使得x和y不在同一棵树中"""
        self.make_root(x)
        self.access(y)
        y.left = None
        y.push_up()
    
    def link(self, x, y):
        """连接x和y所在的树，前提是x和y不在同一棵树中"""
        self.make_root(x)
        if self.find_root(y) != x:
            x.parent = y
    
    def cut(self, x, y):
        """切断x和y之间的边"""
        self.make_root(x)
        self.access(y)
        if y.left == x and x.right is None:
            y.left = None
            x.parent = None
    
    def is_connected(self, x, y):
        """判断x和y是否连通"""
        if x is None or y is None:
            return False
        return self.find_root(x) == self.find_root(y)
    
    def update_node(self, x, value):
        """更新节点的值"""
        self.splay(x)
        x.value = value
        x.push_up()
    
    def query_path_sum(self, x, y):
        """查询x到y路径上的节点和"""
        self.make_root(x)
        self.access(y)
        return y.sum
    
    def query_path_min(self, x, y):
        """查询x到y路径上的最小值"""
        self.make_root(x)
        self.access(y)
        return y.min_val
    
    def query_path_max(self, x, y):
        """查询x到y路径上的最大值"""
        self.make_root(x)
        self.access(y)
        return y.max_val
    
    def query_path_xor(self, x, y):
        """查询x到y路径上的异或和"""
        self.make_root(x)
        self.access(y)
        return y.xor
    
    def __getitem__(self, index):
        """根据索引获取节点"""
        if 0 <= index < len(self.nodes):
            return self.nodes[index]
        raise IndexError("Node index out of range")

# 测试LCT
def test_link_cut_tree():
    print("===== 测试Link-Cut Tree =====")
    
    # 创建LCT，节点值为0-4
    lct = LinkCutTree(5, list(range(5)))
    
    # 构建树结构：0-1-2-3-4
    lct.link(lct[0], lct[1])
    lct.link(lct[1], lct[2])
    lct.link(lct[2], lct[3])
    lct.link(lct[3], lct[4])
    
    # 测试路径查询
    print("路径0-4的和:", lct.query_path_sum(lct[0], lct[4]))  # 应该是 0+1+2+3+4 = 10
    print("路径0-4的最小值:", lct.query_path_min(lct[0], lct[4]))  # 应该是 0
    print("路径0-4的最大值:", lct.query_path_max(lct[0], lct[4]))  # 应该是 4
    print("路径0-4的异或和:", lct.query_path_xor(lct[0], lct[4]))  # 应该是 0^1^2^3^4 = 4
    
    # 测试cut操作
    lct.cut(lct[2], lct[3])
    print("切断2-3后，0和4是否连通:", lct.is_connected(lct[0], lct[4]))  # 应该是 False
    print("切断2-3后，0和2是否连通:", lct.is_connected(lct[0], lct[2]))  # 应该是 True
    print("切断2-3后，3和4是否连通:", lct.is_connected(lct[3], lct[4]))  # 应该是 True
    
    # 测试link操作
    lct.link(lct[2], lct[3])
    print("重新连接2-3后，0和4是否连通:", lct.is_connected(lct[0], lct[4]))  # 应该是 True
    
    # 测试更新节点
    lct.update_node(lct[2], 10)
    print("更新节点2的值为10后，路径0-4的和:", lct.query_path_sum(lct[0], lct[4]))  # 应该是 0+1+10+3+4 = 18
    
    # 测试split操作
    lct.split(lct[1], lct[2])
    print("split 1和2后，0和4是否连通:", lct.is_connected(lct[0], lct[4]))  # 应该是 False

if __name__ == "__main__":
    test_link_cut_tree()