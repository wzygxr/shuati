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

class PersistentLiteraryTree2:
    def __init__(self, maxn=200001, maxm=200001*100):
        self.MAXN = maxn
        self.MAXM = maxm
        self.cnt = 0
        self.head = [0] * (self.MAXN + 1)
        self.key = [0] * (self.MAXM + 1)
        self.ls = [0] * (self.MAXM + 1)
        self.rs = [0] * (self.MAXM + 1)
        self.siz = [0] * (self.MAXM + 1)
        self.rev = [False] * (self.MAXM + 1)
        self.sum = [0] * (self.MAXM + 1)
        self.priority = [0.0] * (self.MAXM + 1)
        
    def copy_node(self, i):
        if i == 0:
            return 0
        self.cnt += 1
        self.key[self.cnt] = self.key[i]
        self.ls[self.cnt] = self.ls[i]
        self.rs[self.cnt] = self.rs[i]
        self.siz[self.cnt] = self.siz[i]
        self.rev[self.cnt] = self.rev[i]
        self.sum[self.cnt] = self.sum[i]
        self.priority[self.cnt] = self.priority[i]
        return self.cnt
    
    def update_size(self, i):
        if i == 0:
            return
        self.siz[i] = self.siz[self.ls[i]] + self.siz[self.rs[i]] + 1
        self.sum[i] = self.sum[self.ls[i]] + self.sum[self.rs[i]] + self.key[i]
    
    def push_down(self, i):
        if self.rev[i]:
            if self.ls[i] != 0:
                self.ls[i] = self.copy_node(self.ls[i])
                self.rev[self.ls[i]] = not self.rev[self.ls[i]]
            if self.rs[i] != 0:
                self.rs[i] = self.copy_node(self.rs[i])
                self.rev[self.rs[i]] = not self.rev[self.rs[i]]
            self.ls[i], self.rs[i] = self.rs[i], self.ls[i]
            self.rev[i] = False
    
    def split(self, i, rank):
        if i == 0:
            return 0, 0
        
        new_i = self.copy_node(i)
        self.push_down(new_i)
        
        left_size = self.siz[self.ls[new_i]]
        
        if left_size + 1 <= rank:
            l = new_i
            r1, r2 = self.split(self.rs[new_i], rank - left_size - 1)
            self.rs[new_i] = r1
            self.update_size(new_i)
            return new_i, r2
        else:
            r = new_i
            l1, l2 = self.split(self.ls[new_i], rank)
            self.ls[new_i] = l2
            self.update_size(new_i)
            return l1, new_i
    
    def merge(self, l, r):
        if l == 0 or r == 0:
            return l + r
        
        if self.priority[l] >= self.priority[r]:
            new_l = self.copy_node(l)
            self.push_down(new_l)
            self.rs[new_l] = self.merge(self.rs[new_l], r)
            self.update_size(new_l)
            return new_l
        else:
            new_r = self.copy_node(r)
            self.push_down(new_r)
            self.ls[new_r] = self.merge(l, self.ls[new_r])
            self.update_size(new_r)
            return new_r

def main():
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    ptr = 1
    
    tree = PersistentLiteraryTree2()
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
            tree.siz[tree.cnt] = 1
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
            
            tree.rev[m] = not tree.rev[m]
            
            tree.head[i] = tree.merge(tree.merge(l, m), r)
        else:  # 查询和
            lm, r = tree.split(tree.head[v], y)
            l, m = tree.split(lm, x - 1)
            
            last_ans = tree.sum[m]
            print(last_ans)
            
            tree.head[i] = tree.merge(tree.merge(l, m), r)

if __name__ == "__main__":
    main()