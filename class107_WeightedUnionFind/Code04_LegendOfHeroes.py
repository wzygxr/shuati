import sys

"""
带权并查集解决队列合并与查询问题 (Python版本)

问题分析：
维护战舰队列的合并和查询操作，需要支持：
1. 将一个队列整体合并到另一个队列末尾
2. 查询同一队列中两艘战舰之间间隔的战舰数量

核心思想：
1. 使用带权并查集维护战舰之间的相对位置关系
2. dist[i] 表示战舰i到其所在队列队首的距离（以战舰数量为单位）
3. size[i] 表示以战舰i为根的队列中战舰的数量

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- query: O(α(n)) 近似O(1)
- 总体: O(n + t * α(n))

空间复杂度: O(n) 用于存储father、dist和size数组

应用场景：
- 队列合并与查询
- 动态维护序列位置关系
- 游戏中的编队系统
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化带权并查集
        :param n: 战舰数量
        """
        self.father = list(range(n + 1))  # father[i] 表示战舰i的父节点
        self.dist = [0] * (n + 1)         # dist[i] 表示战舰i到其所在队列队首的距离
        self.size = [1] * (n + 1)         # size[i] 表示以战舰i为根的队列中战舰的数量
        
    def find(self, i):
        """
        查找战舰i所在队列的代表（队首），并进行路径压缩
        同时更新dist[i]为战舰i到队首的距离
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的战舰编号
        :return: 战舰i所在队列的代表（队首）
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 保存父节点
            tmp = self.father[i]
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(tmp)
            # 更新距离：当前战舰到队首的距离 = 当前战舰到父节点的距离 + 父节点到队首的距离
            self.dist[i] += self.dist[tmp]
        return self.father[i]
    
    def union(self, l, r):
        """
        合并两个队列，将l号战舰所在队列整体移动到r号战舰所在队列末尾
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 战舰l的编号
        :param r: 战舰r的编号
        """
        # 查找两个战舰所在队列的代表
        lf = self.find(l)
        rf = self.find(r)
        # 如果不在同一队列中
        if lf != rf:
            # 将l所在队列合并到r所在队列末尾
            self.father[lf] = rf
            # 更新l所在队列队首到r所在队列队首的距离
            # 距离 = r所在队列的战舰数量（即r所在队列末尾到新队首的距离）
            self.dist[lf] += self.size[rf]
            # 更新新队列的战舰数量
            self.size[rf] += self.size[lf]
            
    def query(self, l, r):
        """
        查询两艘战舰之间间隔的战舰数量
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 战舰l的编号
        :param r: 战舰r的编号
        :return: 间隔的战舰数量，如果不在同一队列中返回-1
        """
        # 如果两艘战舰不在同一队列中
        if self.find(l) != self.find(r):
            return -1
        # 间隔数量 = 两艘战舰到队首距离的差值的绝对值 - 1
        # 减1是因为不计算两艘战舰本身
        return abs(self.dist[l] - self.dist[r]) - 1

def main():
    # 读取操作数量
    t = int(sys.stdin.readline())
    
    # 初始化带权并查集，30000艘战舰
    wuf = WeightedUnionFind(30000)
    
    # 处理t个操作
    for _ in range(t):
        line = sys.stdin.readline().split()
        op = line[0]
        l = int(line[1])
        r = int(line[2])
        
        # 根据操作类型执行相应操作
        if op == "M":
            # 合并队列
            wuf.union(l, r)
        else:
            # 查询间隔
            print(wuf.query(l, r))

if __name__ == "__main__":
    main()