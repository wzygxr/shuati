# BZOJ2212/POI2011 Tree Rotations，Python版
# 测试链接 : https://www.luogu.com.cn/problem/P3521

import sys

# 提高递归深度限制，以处理深树结构
sys.setrecursionlimit(1 << 25)

'''
BZOJ2212/POI2011 Tree Rotations

题目来源: POI 2011
题目链接: https://www.luogu.com.cn/problem/P3521 / https://www.lydsy.com/JudgeOnline/problem.php?id=2212

题目描述:
给定一棵二叉树，每个节点有一个权值。你可以交换任意节点的左右子树，
求交换后中序遍历的逆序对的最小数量。

解题思路:
1. 使用后序遍历的方式处理整棵二叉树
2. 对于每个节点，分别计算交换和不交换左右子树时的逆序对数目
3. 选择逆序对数目较小的方案
4. 使用线段树合并来高效计算左右子树合并时产生的逆序对数目

算法复杂度:
- 时间复杂度: O(n log n)，其中n是树的节点数。每个节点最多被访问一次，
  每次线段树合并操作的时间复杂度是O(log n)。
- 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。

最优解验证:
线段树合并是该问题的最优解。其他可能的解法包括归并排序的分治方法，
但线段树合并的实现更加直观，且能够高效计算子树间的逆序对数目。

线段树合并解决逆序对问题的核心思想:
1. 为每个子树维护一个权值线段树，记录子树中各个权值的出现次数
2. 当合并左右子树时，可以通过线段树快速计算交叉逆序对数目
3. 同时，我们可以选择是否交换左右子树，以最小化总逆序对数目
'''

class SegmentTree:
    def __init__(self):
        # 使用字典动态存储线段树节点
        # 每个节点用字典表示，包含left, right, sum三个键
        self.nodes = {}
        self.cnt = 0  # 节点计数器
        
    def new_node(self):
        """创建新的线段树节点"""
        self.cnt += 1
        self.nodes[self.cnt] = {'left': 0, 'right': 0, 'sum': 0}
        return self.cnt
    
    def update(self, p, l, r, x, v):
        """线段树更新操作"""
        if p == 0:
            p = self.new_node()
        
        self.nodes[p]['sum'] += v
        if l == r:
            return p
        
        mid = (l + r) >> 1
        if x <= mid:
            self.nodes[p]['left'] = self.update(self.nodes[p]['left'], l, mid, x, v)
        else:
            self.nodes[p]['right'] = self.update(self.nodes[p]['right'], mid + 1, r, x, v)
        
        return p
    
    def merge(self, x, y, l, r):
        """线段树合并操作"""
        if x == 0:
            return y
        if y == 0:
            return x
        
        if l == r:
            # 合并叶子节点的sum值
            self.nodes[x]['sum'] += self.nodes[y]['sum']
            return x
        
        mid = (l + r) >> 1
        # 递归合并左右子树
        self.nodes[x]['left'] = self.merge(self.nodes[x]['left'], self.nodes[y]['left'], l, mid)
        self.nodes[x]['right'] = self.merge(self.nodes[x]['right'], self.nodes[y]['right'], mid + 1, r)
        
        # 更新当前节点的sum值
        self.nodes[x]['sum'] = self.nodes[self.nodes[x]['left']]['sum'] + self.nodes[self.nodes[x]['right']]['sum']
        
        return x
    
    def query(self, p, l, r, ql, qr):
        """线段树区间查询"""
        if p == 0:
            return 0
        if ql <= l and r <= qr:
            return self.nodes[p]['sum']
        
        mid = (l + r) >> 1
        res = 0
        if ql <= mid:
            res += self.query(self.nodes[p]['left'], l, mid, ql, qr)
        if qr > mid:
            res += self.query(self.nodes[p]['right'], mid + 1, r, ql, qr)
        
        return res
    
    def get_size(self, p):
        """获取子树大小"""
        if p == 0:
            return 0
        return self.nodes[p]['sum']

def main():
    # 使用快速输入
    input = sys.stdin.read().split()
    ptr = 0
    ans = 0
    
    st = SegmentTree()
    
    def build():
        nonlocal ptr, ans
        w = int(input[ptr])
        ptr += 1
        root = st.new_node()
        
        if w == 0:
            # 非叶子节点，递归构建左右子树
            left = build()
            right = build()
            
            # 计算不交换时的交叉逆序对数目：左子树元素 > 右子树元素的对数
            case1 = 0
            # 计算交换时的交叉逆序对数目：右子树元素 > 左子树元素的对数
            case2 = 0
            
            left_size = st.get_size(left)
            right_size = st.get_size(right)
            
            # 辅助函数：遍历左子树，计算与右子树的逆序对数目
            def dfs_calc(p, l, r):
                if p == 0:
                    return 0
                if l == r:
                    # 查询右子树中小于l的元素数目，乘以当前节点的sum
                    return st.query(right, 1, 400000, 1, l - 1) * st.nodes[p]['sum']
                
                mid = (l + r) >> 1
                return dfs_calc(st.nodes[p]['left'], l, mid) + dfs_calc(st.nodes[p]['right'], mid + 1, r)
            
            # 计算case1：左 > 右的逆序对数目
            case1 = dfs_calc(left, 1, 400000)
            # case2 = left_size * right_size - case1
            case2 = left_size * right_size - case1
            
            # 选择逆序对数目较小的方案
            ans += min(case1, case2)
            
            # 合并左右子树的线段树
            root = st.merge(left, right, 1, 400000)
        else:
            # 叶子节点，将权值插入线段树
            root = st.update(root, 1, 400000, w, 1)
        
        return root
    
    n = int(input[ptr])
    ptr += 1
    build()
    print(ans)

if __name__ == "__main__":
    main()

'''
工程化考量：
1. 输入输出效率：使用sys.stdin.read()一次性读取所有输入，避免多次IO操作
2. 内存管理：使用字典动态存储线段树节点，节省空间
3. 递归深度：设置sys.setrecursionlimit()以处理深树结构
4. 类封装：将线段树操作封装成类，提高代码可读性和复用性

Python语言特性：
1. 动态数据结构：使用字典存储线段树节点，灵活且易于实现
2. 闭包和nonlocal：在build函数中使用闭包和nonlocal关键字访问外部变量
3. 递归限制：需要手动设置递归深度限制
4. 性能考虑：由于Python递归效率较低，对于大规模数据可能需要优化

调试技巧：
1. 可以添加中间变量打印，观察线段树合并过程和逆序对计算
2. 使用assert语句验证线段树节点的正确性
3. 对于复杂问题，可以先使用小规模测试数据验证算法正确性

优化空间：
1. 可以使用lru_cache装饰器缓存重复计算的结果
2. 对于大规模数据，可以考虑将关键操作改为迭代实现
3. 可以使用PyPy运行以提高执行效率
'''