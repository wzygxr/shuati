import sys

"""
带权并查集解决区间和问题 (Python版本)

问题分析：
给定一些区间的和，查询其他区间的和

核心思想：
1. 将区间和问题转化为前缀和问题：区间[l,r]的和等于sum[r] - sum[l-1]
2. 使用带权并查集维护前缀和之间的关系
3. dist[i] 表示 sum[i] - sum[find(i)]，即节点i到其根节点的"距离"

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- query: O(α(n)) 近似O(1)
- 总体: O((m+q) * α(n))

空间复杂度: O(n) 用于存储father和dist数组

应用场景：
- 区间和查询与更新
- 差分约束系统
- 数据一致性验证
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化带权并查集
        :param n: 节点数量
        """
        self.father = list(range(n + 1))  # father[i] 表示节点i的父节点
        self.dist = [0] * (n + 1)         # dist[i] 表示节点i到根节点的距离
        
    def find(self, i):
        """
        查找节点i的根节点，并进行路径压缩
        同时更新dist[i]为节点i到根节点的距离
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的节点
        :return: 节点i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 保存父节点
            tmp = self.father[i]
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(tmp)
            # 更新距离：当前节点到根节点的距离 = 当前节点到父节点的距离 + 父节点到根节点的距离
            self.dist[i] += self.dist[tmp]
        return self.father[i]
    
    def union(self, l, r, v):
        """
        合并两个集合，建立l和r之间的关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左边界
        :param r: 右边界+1（转换为前缀和表示）
        :param v: 区间和值
        """
        # 查找两个节点的根节点
        lf = self.find(l)
        rf = self.find(r)
        # 如果不在同一集合中
        if lf != rf:
            # 合并两个集合
            self.father[lf] = rf
            # 更新距离关系：
            # sum[lf] - sum[rf] = v + (sum[r] - sum[rf]) - (sum[l] - sum[lf])
            # 整理得：dist[lf] = v + dist[r] - dist[l]
            self.dist[lf] = v + self.dist[r] - self.dist[l]
            
    def query(self, l, r):
        """
        查询区间[l,r]的和
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 查询区间左边界
        :param r: 查询区间右边界+1（转换为前缀和表示）
        :return: 区间和，如果无法确定则返回None
        """
        # 如果两个节点不在同一集合中，无法确定关系
        if self.find(l) != self.find(r):
            return None
        # 区间[l,r]的和 = sum[r] - sum[l-1] = (sum[r] - sum[find(r)]) - (sum[l-1] - sum[find(l-1)])
        # 由于find(l) == find(r)，所以结果为 dist[l-1] - dist[r]
        return self.dist[l] - self.dist[r]

def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n = int(line[0]) + 1  # n+1是为了处理前缀和，将区间[l,r]转换为sum[r] - sum[l-1]
    m = int(line[1])
    q = int(line[2])
    
    # 初始化带权并查集
    wuf = WeightedUnionFind(n)
    
    # 处理m个给定条件
    for _ in range(m):
        line = sys.stdin.readline().split()
        l = int(line[0])
        r = int(line[1]) + 1  # 转换为前缀和表示
        v = int(line[2])
        wuf.union(l, r, v)
        
    # 处理q个查询
    for _ in range(q):
        line = sys.stdin.readline().split()
        l = int(line[0])
        r = int(line[1]) + 1  # 转换为前缀和表示
        result = wuf.query(l, r)
        if result is None:
            print("UNKNOWN")
        else:
            print(result)

if __name__ == "__main__":
    main()