import sys

"""
带权并查集+贪心解决关押罪犯问题 (Python版本)

问题分析：
将n个犯人分配到两个监狱，使得两个监狱中最大的仇恨值最小

核心思想：
1. 贪心策略：按仇恨值从大到小处理，尽量将敌人分配到不同监狱
2. 使用扩展域并查集（种类并查集）处理敌对关系：
   - 对于每个犯人i，维护两个节点：i（在监狱A）和i+n（在监狱B）
   - 如果i和j必须在不同监狱，则合并(i,j+n)和(j,i+n)
   - 如果发现冲突（i和i+n在同一集合），说明需要更小的最大仇恨值

时间复杂度分析：
- 总体: O(m * log(m) + m * α(n))

空间复杂度: O(n + m)

应用场景：
- 二分图判定
- 敌对关系处理
- 最优化问题
"""

class UnionFind:
    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        self.father = list(range(n))  # father[i] 表示节点i的父节点
        
    def find(self, i):
        """
        查找节点i所在集合的代表
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的节点
        :return: 节点i所在集合的根节点
        """
        # 路径压缩
        if self.father[i] != i:
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def union(self, l, r):
        """
        合并两个集合
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左侧节点
        :param r: 右侧节点
        """
        self.father[self.find(l)] = self.find(r)
        
    def same(self, l, r):
        """
        判断两个节点是否在同一集合中
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左侧节点
        :param r: 右侧节点
        :return: 如果在同一集合中返回True，否则返回False
        """
        return self.find(l) == self.find(r)

def main():
    line = sys.stdin.readline().split()
    n = int(line[0])
    m = int(line[1])
    
    # arr[i] = [l, r, v] 表示第i条记录：l号犯人和r号犯人的仇恨值为v
    arr = []
    
    # 读取m条记录
    for _ in range(m):
        line = sys.stdin.readline().split()
        l = int(line[0])
        r = int(line[1])
        v = int(line[2])
        arr.append([l, r, v])
        
    # 按仇恨值从大到小排序
    arr.sort(key=lambda x: x[2], reverse=True)
    
    # 初始化并查集，大小为2*n以支持扩展域
    uf = UnionFind(2 * (n + 1))
    
    # enemy[i] 表示i的敌人所在的节点
    enemy = [0] * (n + 1)
    
    ans = 0
    # 从仇恨值最大的开始处理
    for l, r, v in arr:
        # 如果两个犯人被分到同一监狱
        if uf.same(l, r):
            # 产生冲突，记录当前仇恨值作为答案
            ans = v
            break
        else:
            # 尽量将两个犯人分配到不同监狱
            # 如果l还没有分配敌人
            if enemy[l] == 0:
                # 将r作为l的敌人
                enemy[l] = r
            else:
                # 将l的敌人和r合并到同一监狱
                uf.union(enemy[l], r)
            # 如果r还没有分配敌人
            if enemy[r] == 0:
                # 将l作为r的敌人
                enemy[r] = l
            else:
                # 将r的敌人和l合并到同一监狱
                uf.union(l, enemy[r])
                
    # 输出最小冲突值
    print(ans)

if __name__ == "__main__":
    main()