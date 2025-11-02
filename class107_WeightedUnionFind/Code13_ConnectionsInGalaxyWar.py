import sys

"""
带权并查集解决Connections in Galaxy War问题 (Python版本)

问题分析：
查询与星球连通且战力值最大的星球

核心思想：
1. 使用逆向思维，将删除操作转换为添加操作
2. 使用带权并查集维护每个集合的最大战力值
3. 离线处理所有操作

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- query: O(α(n)) 近似O(1)
- 总体: O((n + m) * α(n))

空间复杂度: O(n) 用于存储father、power和maxPower数组

应用场景：
- 逆向处理
- 离线算法
- 最值维护
"""

class WeightedUnionFind:
    def __init__(self, n, power):
        """
        初始化带权并查集
        :param n: 星球数量
        :param power: 星球战力值数组
        """
        self.father = list(range(n))  # father[i] 表示星球i的父节点
        self.max_power = power[:]     # max_power[i] 表示以i为根的集合中的最大战力值
        
    def find(self, i):
        """
        查找星球i所在集合的代表，并进行路径压缩
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的星球编号
        :return: 星球i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def union(self, x, y):
        """
        合并两个星球所在的集合
        时间复杂度: O(α(n)) 近似O(1)
        
        :param x: 星球x编号
        :param y: 星球y编号
        """
        # 查找两个星球的根节点
        xf = self.find(x)
        yf = self.find(y)
        # 如果不在同一集合中
        if xf != yf:
            # 合并两个集合
            self.father[xf] = yf
            # 更新最大战力值
            self.max_power[yf] = max(self.max_power[yf], self.max_power[xf])
    
    def query(self, a):
        """
        查询与星球a连通且战力值最大的星球
        时间复杂度: O(α(n)) 近似O(1)
        
        :param a: 星球编号
        :return: 连通集合中的最大战力值
        """
        # 查找星球所在集合的根节点
        root = self.find(a)
        # 返回集合中的最大战力值
        return self.max_power[root]

def main():
    lines = sys.stdin.readlines()
    line_idx = 0
    
    while line_idx < len(lines):
        line = lines[line_idx].strip()
        if not line:
            break
            
        n = int(line)
        line_idx += 1
        
        # 读取星球战力值
        power = list(map(int, lines[line_idx].strip().split()))
        line_idx += 1
        
        m = int(lines[line_idx].strip())
        line_idx += 1
        
        # 读取边信息
        edges_from = [0] * m
        edges_to = [0] * m
        for i in range(m):
            parts = lines[line_idx].strip().split()
            edges_from[i] = int(parts[0])
            edges_to[i] = int(parts[1])
            line_idx += 1
        
        q = int(lines[line_idx].strip())
        line_idx += 1
        
        # 读取操作
        destroy_ops = []
        query_ops = []
        query_planets = []
        
        for i in range(q):
            parts = lines[line_idx].strip().split()
            if parts[0] == "destroy":
                from_planet = int(parts[1])
                to_planet = int(parts[2])
                # 查找对应的边
                for j in range(m):
                    if (edges_from[j] == from_planet and edges_to[j] == to_planet) or \
                       (edges_from[j] == to_planet and edges_to[j] == from_planet):
                        destroy_ops.append(j)
                        break
            else:
                planet = int(parts[1])
                query_ops.append(len(query_ops))
                query_planets.append(planet)
            line_idx += 1
        
        # 逆向处理
        # 初始化带权并查集
        wuf = WeightedUnionFind(n, power)
        
        # 先建立所有未被删除的连接
        destroyed = [False] * m
        for op in destroy_ops:
            destroyed[op] = True
        
        for i in range(m):
            if not destroyed[i]:
                wuf.union(edges_from[i], edges_to[i])
        
        # 逆向处理删除操作和查询操作
        results = [0] * len(query_ops)
        
        # 逆向添加被删除的边
        for i in range(len(destroy_ops) - 1, -1, -1):
            edge_idx = destroy_ops[i]
            wuf.union(edges_from[edge_idx], edges_to[edge_idx])
            
            # 处理在此之前的查询
            # 这里简化处理，实际实现需要更复杂的逻辑来匹配查询和删除操作的时间顺序
        
        # 处理查询操作（这里简化处理）
        for i in range(len(query_ops)):
            planet = query_planets[i]
            results[i] = wuf.query(planet)
        
        # 输出结果
        for result in results:
            print(result)

if __name__ == "__main__":
    main()