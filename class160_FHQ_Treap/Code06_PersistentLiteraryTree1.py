# 可持久化文艺平衡树，FHQ-Treap实现，Python版
# 一开始序列为空，实现如下操作，操作一共发生n次
# v 1 x y : 基于v版本的序列，在第x个数后插入y，生成新版本的序列
# v 2 x   : 基于v版本的序列，删除第x个数，生成新版本的序列
# v 3 x y : 基于v版本的序列，范围[x,y]所有数字翻转，生成新版本的序列
# v 4 x y : 基于v版本的序列，查询范围[x,y]所有数字的和，生成新版本的序列状况=v版本状况
# 不管什么操作，都基于某个v版本，操作完成后得到新版本的序列，但v版本不会变化
# 每种操作给定的参数都是有效的，插入数字的范围[-10^6, +10^6]
# 1 <= n <= 2 * 10^5
# 本题目要求强制在线，具体规则可以打开测试链接查看
# 测试链接 : https://www.luogu.com.cn/problem/P5055
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import random
import sys

class PersistentLiteraryTree:
    def __init__(self, maxn=200001, maxm=200001*100):
        self.MAXN = maxn
        self.MAXM = maxm
        self.cnt = 0
        self.head = [0] * (self.MAXN + 1)
        self.key = [0] * (self.MAXM + 1)
        self.left = [0] * (self.MAXM + 1)
        self.right = [0] * (self.MAXM + 1)
        self.size = [0] * (self.MAXM + 1)
        self.reverse = [False] * (self.MAXM + 1)
        self.sum = [0] * (self.MAXM + 1)
        self.priority = [0.0] * (self.MAXM + 1)
        
    def copy_node(self, i):
        if i == 0:
            return 0
        self.cnt += 1
        self.key[self.cnt] = self.key[i]
        self.left[self.cnt] = self.left[i]
        self.right[self.cnt] = self.right[i]
        self.size[self.cnt] = self.size[i]
        self.reverse[self.cnt] = self.reverse[i]
        self.sum[self.cnt] = self.sum[i]
        self.priority[self.cnt] = self.priority[i]
        return self.cnt
    
    def update_size(self, i):
        if i == 0:
            return
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + 1
        self.sum[i] = self.sum[self.left[i]] + self.sum[self.right[i]] + self.key[i]
    
    def push_down(self, i):
        if self.reverse[i]:
            if self.left[i] != 0:
                self.left[i] = self.copy_node(self.left[i])
                self.reverse[self.left[i]] = not self.reverse[self.left[i]]
            if self.right[i] != 0:
                self.right[i] = self.copy_node(self.right[i])
                self.reverse[self.right[i]] = not self.reverse[self.right[i]]
            self.left[i], self.right[i] = self.right[i], self.left[i]
            self.reverse[i] = False
    
    def split(self, i, rank):
        if i == 0:
            return 0, 0
        
        new_i = self.copy_node(i)
        self.push_down(new_i)
        
        left_size = self.size[self.left[new_i]]
        
        if left_size + 1 <= rank:
            l = new_i
            r1, r2 = self.split(self.right[new_i], rank - left_size - 1)
            self.right[new_i] = r1
            self.update_size(new_i)
            return new_i, r2
        else:
            r = new_i
            l1, l2 = self.split(self.left[new_i], rank)
            self.left[new_i] = l2
            self.update_size(new_i)
            return l1, new_i
    
    def merge(self, l, r):
        if l == 0 or r == 0:
            return l + r
        
        if self.priority[l] >= self.priority[r]:
            new_l = self.copy_node(l)
            self.push_down(new_l)
            self.right[new_l] = self.merge(self.right[new_l], r)
            self.update_size(new_l)
            return new_l
        else:
            new_r = self.copy_node(r)
            self.push_down(new_r)
            self.left[new_r] = self.merge(l, self.left[new_r])
            self.update_size(new_r)
            return new_r

def main():
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    ptr = 1
    
    tree = PersistentLiteraryTree()
    tree.head[0] = 0  # 初始版本为空树
    last_ans = 0
    
    for i in range(1, n + 1):
        v = int(data[ptr]); ptr += 1
        op = int(data[ptr]); ptr += 1
        x = int(data[ptr]); ptr += 1
        x ^= last_ans
        
        y = 0
        if op != 2:
            y = int(data[ptr]); ptr += 1
            y ^= last_ans
        
        if op == 1:  # 插入
            l, r = tree.split(tree.head[v], x)
            
            tree.cnt += 1
            tree.key[tree.cnt] = y
            tree.size[tree.cnt] = 1
            tree.sum[tree.cnt] = y
            tree.priority[tree.cnt] = random.random()
            
            tree.head[i] = tree.merge(tree.merge(l, tree.cnt), r)
        elif op == 2:  # 删除
            lm, r = tree.split(tree.head[v], x)
            l, m = tree.split(lm, x - 1)
            
            tree.head[i] = tree.merge(l, r)
        elif op == 3:  # 翻转
            lm, r = tree.split(tree.head[v], y)
            l, m = tree.split(lm, x - 1)
            
            tree.reverse[m] = not tree.reverse[m]
            
            tree.head[i] = tree.merge(tree.merge(l, m), r)
        else:  # 查询和
            lm, r = tree.split(tree.head[v], y)
            l, m = tree.split(lm, x - 1)
            
            last_ans = tree.sum[m]
            print(last_ans)
            
            tree.head[i] = tree.merge(tree.merge(l, m), r)

if __name__ == "__main__":
    main()