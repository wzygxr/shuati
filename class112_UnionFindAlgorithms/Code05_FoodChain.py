# 食物链，Python版
# 动物王国中有三类动物A,B,C，这三类动物的食物链构成了有趣的环形。
# A吃B，B吃C，C吃A。
# 现有N个动物，以1－N编号。
# 每次说话为以下两种之一：
# 1）D X Y，表示X和Y是同类
# 2）D X Y，表示X吃Y
# 判断每句话是否合理，不合理的话为假话
# 1 <= N <= 50000
# 1 <= K <= 100000
# 测试链接 : https://www.luogu.com.cn/problem/P2024
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

# 补充题目：
# 1. 洛谷 P2024 - 食物链（经典种类并查集）
#    链接：https://www.luogu.com.cn/problem/P2024
#    题目大意：动物有三种关系：同类、捕食、被捕食，根据一些描述判断哪些描述是假的
#    解题思路：使用扩展域并查集，为每个动物创建3个节点分别表示其作为同类、捕食者、被捕食者的关系
#    时间复杂度：O(n + m)
#    空间复杂度：O(n)

import sys
from typing import List

class FoodChainUnionFind:
    def __init__(self, n: int):
        self.MAXN = n + 1
        # 扩展域并查集，每个动物有3个节点：
        # i 表示同类
        # i + n 表示捕食者
        # i + 2 * n 表示被捕食者
        self.father = [0] * (self.MAXN * 3)
        self.siz = [0] * (self.MAXN * 3)
    
    def find(self, i: int) -> int:
        while i != self.father[i]:
            i = self.father[i]
        return i
    
    def union(self, x: int, y: int) -> None:
        fx = self.find(x)
        fy = self.find(y)
        if self.siz[fx] < self.siz[fy]:
            fx, fy = fy, fx
        self.father[fy] = fx
        self.siz[fx] += self.siz[fy]

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    k = int(data[1])
    
    # 初始化并查集
    uf = FoodChainUnionFind(n)
    for i in range(1, 3 * n + 1):
        uf.father[i] = i
        uf.siz[i] = 1
    
    falseCount = 0
    idx = 2
    
    for i in range(1, k + 1):
        d = int(data[idx])
        idx += 1
        x = int(data[idx])
        idx += 1
        y = int(data[idx])
        idx += 1
        
        # 判断是否越界
        if x > n or y > n:
            falseCount += 1
            continue
        
        if d == 1:
            # x和y是同类
            # 如果x吃y或y吃x，则为假话
            if (uf.find(x) == uf.find(y + n) or uf.find(x) == uf.find(y + 2 * n) or
                uf.find(y) == uf.find(x + n) or uf.find(y) == uf.find(x + 2 * n)):
                falseCount += 1
            else:
                # 合并同类关系
                uf.union(x, y)
                uf.union(x + n, y + n)
                uf.union(x + 2 * n, y + 2 * n)
        else:
            # x吃y
            # 如果y吃x或x和y是同类，则为假话
            if (uf.find(x) == uf.find(y) or uf.find(x) == uf.find(y + 2 * n) or
                uf.find(y) == uf.find(x + n)):
                falseCount += 1
            else:
                # 建立捕食关系
                uf.union(x, y + n)      # x和y的捕食者是同类
                uf.union(x + n, y + 2 * n)  # x的捕食者和y的被捕食者是同类
                uf.union(x + 2 * n, y)      # x的被捕食者和y是同类
    
    print(falseCount)

if __name__ == "__main__":
    main()