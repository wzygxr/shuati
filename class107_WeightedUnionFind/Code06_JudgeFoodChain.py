import sys

"""
种类并查集解决食物链问题 (Python版本)

问题分析：
判断动物之间的关系描述是否一致，统计矛盾的数量

核心思想：
1. 使用种类并查集（扩展域并查集）处理多个种类之间的关系
2. 对于每个动物，维护三种状态：
   - dist[i] = 0：动物i与根节点同类
   - dist[i] = 1：动物i吃根节点
   - dist[i] = 2：动物i被根节点吃
3. 利用模运算处理环形关系：A吃B，B吃C，C吃A

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- check: O(α(n)) 近似O(1)
- 总体: O(n + k * α(n))

空间复杂度: O(n) 用于存储father和dist数组

应用场景：
- 多种类关系维护
- 环形关系处理
- 逻辑一致性验证
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化种类并查集
        :param n: 动物数量
        """
        self.father = list(range(n + 1))  # father[i] 表示动物i的父节点
        self.dist = [0] * (n + 1)         # dist[i] 表示动物i与根节点的关系
        
    def find(self, i):
        """
        查找动物i所在集合的代表，并进行路径压缩
        同时更新dist[i]为动物i与根节点的关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的动物编号
        :return: 动物i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 保存父节点
            tmp = self.father[i]
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(tmp)
            # 更新关系：当前动物与根节点的关系 = 当前动物与父节点的关系 + 父节点与根节点的关系
            # 使用模3运算处理环形关系
            self.dist[i] = (self.dist[i] + self.dist[tmp]) % 3
        return self.father[i]
    
    def union(self, op, l, r):
        """
        合并两个动物所在的集合，建立关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param op: 操作类型：1表示同类，2表示吃
        :param l: 左侧动物编号
        :param r: 右侧动物编号
        """
        # 查找两个动物的根节点
        lf = self.find(l)
        rf = self.find(r)
        v = 0 if op == 1 else 1
        # 如果不在同一集合中
        if lf != rf:
            # 合并两个集合
            self.father[lf] = rf
            # 更新关系：
            # dist[lf] = (dist[r] - dist[l] + v + 3) % 3
            # 这里v=0表示同类，v=1表示l吃r
            self.dist[lf] = (self.dist[r] - self.dist[l] + v + 3) % 3
            
    def check(self, op, l, r):
        """
        检查新的关系描述是否与已有关系一致
        时间复杂度: O(α(n)) 近似O(1)
        
        :param op: 操作类型：1表示同类，2表示吃
        :param l: 左侧动物编号
        :param r: 右侧动物编号
        :return: 如果一致返回True，否则返回False
        """
        # 检查基本合法性
        if l > len(self.father) - 1 or r > len(self.father) - 1 or (op == 2 and l == r):
            return False
        # 如果两个动物在同一集合中，可以验证一致性
        if self.find(l) == self.find(r):
            if op == 1:
                # 检查是否同类
                # l和r同类当且仅当它们与根节点的关系相同
                if self.dist[l] != self.dist[r]:
                    return False
            else:
                # 检查是否l吃r
                # l吃r当且仅当l比r高一个等级（模3意义下）
                if (self.dist[l] - self.dist[r] + 3) % 3 != 1:
                    return False
        return True

def main():
    line = sys.stdin.readline().split()
    n = int(line[0])
    k = int(line[1])
    
    # 初始化种类并查集
    wuf = WeightedUnionFind(n)
    
    # 假话计数器清零
    ans = 0
    
    # 处理k句话
    for _ in range(k):
        line = sys.stdin.readline().split()
        op = int(line[0])
        l = int(line[1])
        r = int(line[2])
        # 先检查一致性
        if not wuf.check(op, l, r):
            # 发现矛盾，假话计数器加1
            ans += 1
        else:
            # 一致则合并
            wuf.union(op, l, r)
            
    # 输出假话数量
    print(ans)

if __name__ == "__main__":
    main()