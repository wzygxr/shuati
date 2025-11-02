# 文艺平衡树，FHQ-Treap实现范围翻转，python版本
# 长度为n的序列，下标从1开始，一开始序列为1, 2, ..., n
# 接下来会有k个操作，每个操作给定l，r，表示从l到r范围上的所有数字翻转
# 做完k次操作后，从左到右打印所有数字
# 1 <= n, k <= 10^5
# 测试链接 : https://www.luogu.com.cn/problem/P3391
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import sys
import random
from io import StringIO

class LiteraryTree:
    def __init__(self, max_n=100001):
        """
        初始化文艺平衡树
        
        Args:
            max_n: 最大节点数
        """
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号
        self.cnt = 0   # 空间使用计数
        
        # 节点信息数组
        self.key = [0] * self.MAXN      # 节点的key值
        self.left = [0] * self.MAXN     # 左孩子
        self.right = [0] * self.MAXN    # 右孩子
        self.size = [0] * self.MAXN     # 节点总数
        self.priority = [0.0] * self.MAXN  # 节点优先级
        self.reverse = [False] * self.MAXN  # 翻转标记
        
        # 中序遍历结果数组
        self.ans = [0] * self.MAXN
        self.ansi = 0
    
    def up(self, i):
        """
        更新节点信息
        
        Args:
            i: 节点编号
        """
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + 1
    
    def down(self, i):
        """
        下传翻转标记
        
        Args:
            i: 节点编号
        """
        if self.reverse[i]:
            # 交换左右子树
            self.left[i], self.right[i] = self.right[i], self.left[i]
            
            # 下传翻转标记
            if self.left[i] != 0:
                self.reverse[self.left[i]] = not self.reverse[self.left[i]]
            if self.right[i] != 0:
                self.reverse[self.right[i]] = not self.reverse[self.right[i]]
                
            # 清除当前节点的翻转标记
            self.reverse[i] = False
    
    def split(self, l, r, i, rank):
        """
        按排名分裂操作，将树i按照排名rank分裂为两棵树
        
        Args:
            l: 左树根节点编号（结果）
            r: 右树根节点编号（结果）
            i: 待分裂的树根节点编号
            rank: 分裂排名
        """
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            self.down(i)
            if self.size[self.left[i]] + 1 <= rank:
                self.right[l] = i
                self.split(i, r, self.right[i], rank - self.size[self.left[i]] - 1)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], rank)
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
    
    def inorder(self, i):
        """
        中序遍历，用于输出结果
        
        Args:
            i: 树根节点编号
        """
        if i != 0:
            self.down(i)
            self.inorder(self.left[i])
            self.ansi += 1
            self.ans[self.ansi] = self.key[i]
            self.inorder(self.right[i])
    
    def build(self, n):
        """
        构建初始序列1, 2, ..., n
        
        Args:
            n: 序列长度
        """
        for i in range(1, n + 1):
            self.cnt += 1
            self.key[self.cnt] = i
            self.size[self.cnt] = 1
            self.priority[self.cnt] = random.random()
            self.head = self.merge(self.head, self.cnt)
    
    def reverse_range(self, x, y):
        """
        翻转区间[x, y]
        
        Args:
            x: 区间左端点
            y: 区间右端点
        """
        # 先分裂出[1, y]和(y, n]
        self.split(0, 0, self.head, y)
        lm = self.right[0]
        r = self.left[0]
        
        # 再分裂出[1, x-1]和[x, y]
        self.split(0, 0, lm, x - 1)
        l = self.right[0]
        m = self.left[0]
        
        # 翻转区间[x, y]
        self.reverse[m] = not self.reverse[m]
        
        # 合并所有区间
        self.head = self.merge(self.merge(l, m), r)


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """5 3
1 3
1 3
1 4"""
    
    sys.stdin = StringIO(input_text)
    
    tree = LiteraryTree()
    
    n, k = map(int, input().split())
    
    # 构建初始序列
    tree.build(n)
    
    # 处理k个操作
    for _ in range(k):
        x, y = map(int, input().split())
        tree.reverse_range(x, y)
    
    # 中序遍历输出结果
    tree.ansi = 0
    tree.inorder(tree.head)
    
    # 打印结果
    result = []
    for i in range(1, tree.ansi + 1):
        result.append(str(tree.ans[i]))
    
    print(" ".join(result))


if __name__ == "__main__":
    main()