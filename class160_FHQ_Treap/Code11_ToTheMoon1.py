# FHQ-Treap实现To the moon
# SPOJ TTM - To the moon
# 实现可持久化数组操作
# 测试链接 : https://www.spoj.com/problems/TTM/

import sys
import random
from io import StringIO

class ToTheMoonFHQTreap:
    def __init__(self, max_n=100001, max_v=100001):
        """
        初始化FHQ Treap可持久化数组结构
        
        Args:
            max_n: 最大节点数
            max_v: 最大版本数
        """
        self.MAXN = max_n
        self.MAXV = max_v
        self.cnt = 0   # 空间使用计数
        
        # 节点信息数组
        self.key = [0] * self.MAXN      # 节点的key值（数组元素值）
        self.add = [0] * self.MAXN      # 节点的加法标记
        self.left = [0] * self.MAXN     # 左孩子
        self.right = [0] * self.MAXN    # 右孩子
        self.size = [0] * self.MAXN     # 子树大小
        self.priority = [0.0] * self.MAXN  # 节点优先级
        
        # 版本数组，存储每个版本的根节点
        self.version = [0] * self.MAXV
        
        # 当前版本号
        self.current_version = 0
    
    def init(self):
        """
        初始化结构
        """
        self.cnt = 0
        self.current_version = 0
        self.key = [0] * self.MAXN
        self.add = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
        self.version = [0] * self.MAXV
    
    def up(self, i):
        """
        更新节点信息
        
        Args:
            i: 节点编号
        """
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + 1
    
    def down(self, i):
        """
        下传标记
        
        Args:
            i: 节点编号
        """
        if self.add[i] != 0:
            # 创建新节点以实现可持久化
            if self.left[i] != 0:
                self.cnt += 1
                self.key[self.cnt] = self.key[self.left[i]]
                self.add[self.cnt] = self.add[self.left[i]]
                self.left[self.cnt] = self.left[self.left[i]]
                self.right[self.cnt] = self.right[self.left[i]]
                self.size[self.cnt] = self.size[self.left[i]]
                self.priority[self.cnt] = self.priority[self.left[i]]
                self.key[self.cnt] += self.add[i]
                self.add[self.cnt] += self.add[i]
                self.left[i] = self.cnt
            if self.right[i] != 0:
                self.cnt += 1
                self.key[self.cnt] = self.key[self.right[i]]
                self.add[self.cnt] = self.add[self.right[i]]
                self.left[self.cnt] = self.left[self.right[i]]
                self.right[self.cnt] = self.right[self.right[i]]
                self.size[self.cnt] = self.size[self.right[i]]
                self.priority[self.cnt] = self.priority[self.right[i]]
                self.key[self.cnt] += self.add[i]
                self.add[self.cnt] += self.add[i]
                self.right[i] = self.cnt
            self.add[i] = 0
    
    def split_by_position(self, l, r, i, pos):
        """
        按位置分裂，将树i按照位置pos分裂为两棵树
        
        Args:
            l: 左树根节点编号（结果）
            r: 右树根节点编号（结果）
            i: 待分裂的树根节点编号
            pos: 分裂位置
        """
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            self.down(i)
            if self.size[self.left[i]] + 1 <= pos:
                self.right[l] = i
                self.split_by_position(i, r, self.right[i], pos - self.size[self.left[i]] - 1)
            else:
                self.left[r] = i
                self.split_by_position(l, i, self.left[i], pos)
            self.up(i)
    
    def merge(self, l, r):
        """
        合并操作，将两棵树l和r合并为一棵树
        
        Args:
            l: 左树根节点编号
            r: 右树根节点编号
            
        Returns:
            合并后树的根节点编号
        """
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.down(l)
            self.right[l] = self.merge(self.right[l], r)
            self.up(l)
            return l
        else:
            self.down(r)
            self.left[r] = self.merge(l, self.left[r])
            self.up(r)
            return r
    
    def build(self, l, r, arr):
        """
        构建初始数组
        
        Args:
            l: 左边界
            r: 右边界
            arr: 数组
            
        Returns:
            构建的树根节点编号
        """
        if l > r:
            return 0
        mid = (l + r) // 2
        self.cnt += 1
        root = self.cnt
        self.key[root] = arr[mid]
        self.size[root] = 1
        self.priority[root] = random.random()
        
        if l == r:
            return root
        
        self.left[root] = self.build(l, mid - 1, arr)
        self.right[root] = self.build(mid + 1, r, arr)
        self.up(root)
        return root
    
    def add_range(self, root, l, r, value):
        """
        区间加法（可持久化）
        
        Args:
            root: 树根节点编号
            l: 区间左端点
            r: 区间右端点
            value: 加法值
            
        Returns:
            新树的根节点编号
        """
        self.split_by_position(0, 0, root, l - 1)
        left_tree = self.right[0]
        self.split_by_position(0, 0, left_tree, r - l + 1)
        middle_tree = self.right[0]
        
        # 创建新节点以实现可持久化
        self.cnt += 1
        self.key[self.cnt] = self.key[middle_tree]
        self.add[self.cnt] = self.add[middle_tree]
        self.left[self.cnt] = self.left[middle_tree]
        self.right[self.cnt] = self.right[middle_tree]
        self.size[self.cnt] = self.size[middle_tree]
        self.priority[self.cnt] = self.priority[middle_tree]
        self.key[self.cnt] += value
        self.add[self.cnt] += value
        
        # 重新合并
        return self.merge(self.merge(self.left[0], self.cnt), self.right[0])
    
    def query(self, root, pos):
        """
        查询指定位置的值
        
        Args:
            root: 树根节点编号
            pos: 位置
            
        Returns:
            位置上的值
        """
        self.split_by_position(0, 0, root, pos - 1)
        left_tree = self.right[0]
        self.split_by_position(0, 0, left_tree, 1)
        middle_tree = self.right[0]
        
        result = self.key[middle_tree]
        
        # 重新合并
        self.merge(self.merge(self.left[0], middle_tree), self.right[0])
        
        return result
    
    def get_kth(self, i, pos):
        """
        获取树中第pos个节点的key值
        
        Args:
            i: 树根节点编号
            pos: 位置
            
        Returns:
            节点key值
        """
        if i == 0:
            return 0
        self.down(i)
        if self.size[self.left[i]] + 1 == pos:
            return self.key[i]
        elif self.size[self.left[i]] + 1 > pos:
            return self.get_kth(self.left[i], pos)
        else:
            return self.get_kth(self.right[i], pos - self.size[self.left[i]] - 1)
    
    def get_kth_element(self, root, pos):
        """
        获取第pos个元素
        
        Args:
            root: 树根节点编号
            pos: 位置
            
        Returns:
            元素值
        """
        return self.get_kth(root, pos)


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """5 5
1 2 3 4 5
Q 0 2
C 0 1 3 2
Q 1 2
H 1
Q 1 2"""
    
    sys.stdin = StringIO(input_text)
    
    treap = ToTheMoonFHQTreap()
    treap.init()
    
    n, m = map(int, input().split())  # 数组长度和操作次数
    
    # 读取初始数组
    arr = [0] + list(map(int, input().split()))  # 0索引不使用，从1开始
    
    # 构建初始版本
    treap.version[treap.current_version] = treap.build(1, n, arr)
    
    # 处理操作
    for _ in range(m):
        operation = input().strip().split()
        
        if operation[0] == "Q":
            # 查询操作
            version_id = int(operation[1])
            pos = int(operation[2])
            print(treap.get_kth_element(treap.version[version_id], pos))
        elif operation[0] == "C":
            # 修改操作
            version_id = int(operation[1])
            l = int(operation[2])
            r = int(operation[3])
            value = int(operation[4])
            treap.current_version += 1
            treap.version[treap.current_version] = treap.add_range(treap.version[version_id], l, r, value)
        else:
            # 回到历史版本
            version_id = int(operation[1])
            treap.current_version = version_id


if __name__ == "__main__":
    main()