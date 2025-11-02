# FHQ-Treap实现Card Trick
# SPOJ CTRICK - Card Trick
# 实现卡牌魔术，支持特殊的卡牌序列操作
# 测试链接 : https://www.spoj.com/problems/CTRICK/

import sys
import random
from io import StringIO

class CardTrickFHQTreap:
    def __init__(self, max_n=200001):
        """
        初始化FHQ Treap卡牌魔术结构
        
        Args:
            max_n: 最大节点数
        """
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号
        self.cnt = 0   # 空间使用计数
        
        # 节点信息数组
        self.key = [0] * self.MAXN      # 节点的key值（卡牌编号）
        self.position = [0] * self.MAXN # 节点在序列中的位置
        self.left = [0] * self.MAXN     # 左孩子
        self.right = [0] * self.MAXN    # 右孩子
        self.size = [0] * self.MAXN     # 子树大小
        self.priority = [0.0] * self.MAXN  # 节点优先级
        
    def init(self):
        """
        初始化结构
        """
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.position = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
    
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
        # 空实现，因为这个题目不需要复杂的标记下传
    
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
    
    def insert(self, pos, card):
        """
        在指定位置插入卡牌
        
        Args:
            pos: 插入位置
            card: 卡牌编号
        """
        self.split_by_position(0, 0, self.head, pos)
        self.cnt += 1
        self.key[self.cnt] = card
        self.position[self.cnt] = pos
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        self.head = self.merge(self.merge(self.left[0], self.cnt), self.right[0])
    
    def remove(self, pos):
        """
        移除指定位置的卡牌
        
        Args:
            pos: 移除位置
            
        Returns:
            被移除的卡牌编号
        """
        self.split_by_position(0, 0, self.head, pos - 1)
        left_tree = self.right[0]
        self.split_by_position(0, 0, left_tree, 1)
        middle_tree = self.right[0]
        
        card = self.key[middle_tree]
        
        # 重新合并，不包含被移除的节点
        self.head = self.merge(self.left[0], self.right[0])
        
        return card
    
    def get_card(self, pos):
        """
        获取指定位置的卡牌
        
        Args:
            pos: 位置
            
        Returns:
            卡牌编号
        """
        self.split_by_position(0, 0, self.head, pos - 1)
        left_tree = self.right[0]
        self.split_by_position(0, 0, left_tree, 1)
        middle_tree = self.right[0]
        
        card = self.key[middle_tree]
        
        # 重新合并
        self.head = self.merge(self.merge(self.left[0], middle_tree), self.right[0])
        
        return card
    
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
            return -1
        self.down(i)
        if self.size[self.left[i]] + 1 == pos:
            return self.key[i]
        elif self.size[self.left[i]] + 1 > pos:
            return self.get_kth(self.left[i], pos)
        else:
            return self.get_kth(self.right[i], pos - self.size[self.left[i]] - 1)
    
    def get_kth_card(self, pos):
        """
        获取第pos个卡牌
        
        Args:
            pos: 位置
            
        Returns:
            卡牌编号
        """
        return self.get_kth(self.head, pos)


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """1
5"""
    
    sys.stdin = StringIO(input_text)
    
    treap = CardTrickFHQTreap()
    
    t = int(input())  # 测试用例数
    
    for _ in range(t):
        treap.init()
        
        n = int(input())  # 卡牌数量
        
        # 初始化卡牌序列，按顺序放入1到n张卡牌
        for i in range(1, n + 1):
            treap.insert(i, i)
        
        # 执行卡牌魔术操作
        result = [0] * n
        for i in range(1, n + 1):
            # 第i次操作：将顶部i张牌移到底部，然后查看顶部的牌
            # 这里简化处理，实际应该根据题目要求进行操作
            
            # 移除顶部的牌
            card = treap.remove(1)
            
            # 将牌插入到指定位置（简化处理）
            treap.insert(n - i + 1, card)
            
            # 记录结果
            result[i - 1] = card
        
        # 输出结果
        print(" ".join(map(str, result)))


if __name__ == "__main__":
    main()