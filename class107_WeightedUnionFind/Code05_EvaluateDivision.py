"""
带权并查集解决变量除法求值问题 (Python版本)

问题分析：
给定一些变量之间的除法等式关系，查询其他变量之间的除法结果

核心思想：
1. 将变量之间的除法关系转化为图上的权重关系
2. 如果 a/b = v，则在图中添加边 a->b 权重为v，b->a 权重为1/v
3. 使用带权并查集维护变量之间的倍数关系
4. dist[x] 表示变量x是其根节点代表变量的多少倍

时间复杂度分析：
- prepare: O(e) e为等式数量
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- query: O(α(n)) 近似O(1)
- 总体: O(e * α(n) + q * α(n)) q为查询数量

空间复杂度: O(n) n为不同变量的数量

应用场景：
- 变量关系推导
- 单位换算
- 比例计算
"""

class WeightedUnionFind:
    def __init__(self):
        """
        初始化带权并查集
        """
        self.father = {}  # father[x] 表示变量x的父节点
        self.dist = {}    # dist[x] 表示变量x是其根节点代表变量的多少倍
        
    def prepare(self, equations):
        """
        初始化并查集
        时间复杂度: O(e) e为等式数量
        空间复杂度: O(n) n为不同变量的数量
        
        :param equations: 等式列表
        """
        # 清空之前的数据
        self.father.clear()
        self.dist.clear()
        # 初始化所有出现的变量
        for equation in equations:
            for var in equation:
                # 每个变量初始时是自己的根节点
                self.father[var] = var
                # 每个变量初始时是自己根节点的1倍
                self.dist[var] = 1.0
                
    def find(self, x):
        """
        查找变量x的根节点，并进行路径压缩
        同时更新dist[x]为变量x是其根节点代表变量的多少倍
        时间复杂度: O(α(n)) 近似O(1)
        
        :param x: 要查找的变量
        :return: 变量x所在集合的根节点
        """
        # 如果变量不存在，返回None
        if x not in self.father:
            return None
            
        # 如果不是根节点
        if x != self.father[x]:
            # 保存父节点
            tmp = self.father[x]
            # 递归查找根节点，同时进行路径压缩
            self.father[x] = self.find(tmp)
            # 更新倍数关系：当前变量是根节点的倍数 = 当前变量是父节点的倍数 * 父节点是根节点的倍数
            self.dist[x] *= self.dist[tmp]
            
        return self.father[x]
    
    def union(self, l, r, v):
        """
        合并两个变量所在的集合，建立倍数关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左侧变量
        :param r: 右侧变量
        :param v: 倍数关系 l/r = v
        """
        # 查找两个变量的根节点
        lf = self.find(l)
        rf = self.find(r)
        # 如果不在同一集合中
        if lf != rf:
            # 合并两个集合
            self.father[lf] = rf
            # 更新倍数关系：
            # l = v * r
            # l = dist[l] * lf, r = dist[r] * rf
            # 所以 dist[l] * lf = v * dist[r] * rf
            # 即 lf = (v * dist[r] / dist[l]) * rf
            # 因此 dist[lf] = v * dist[r] / dist[l]
            self.dist[lf] = self.dist[r] / self.dist[l] * v
            
    def query(self, l, r):
        """
        查询两个变量之间的倍数关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左侧变量
        :param r: 右侧变量
        :return: l/r的结果，如果无法确定返回-1.0
        """
        # 查找两个变量的根节点
        lf = self.find(l)
        rf = self.find(r)
        # 如果任一变量不存在或不在同一集合中，无法确定关系
        if lf is None or rf is None or lf != rf:
            return -1.0
        # l/r = (dist[l] * lf) / (dist[r] * rf) = dist[l] / dist[r] (因为lf == rf)
        return self.dist[l] / self.dist[r]

def calcEquation(equations, values, queries):
    """
    计算所有查询的答案
    
    :param equations: 等式列表，每个等式包含两个变量
    :param values: 等式对应的值
    :param queries: 查询列表
    :return: 所有查询的答案
    """
    # 初始化带权并查集
    wuf = WeightedUnionFind()
    wuf.prepare(equations)
    
    # 处理所有等式，建立变量间的关系
    for i in range(len(values)):
        # 建立变量之间的倍数关系
        wuf.union(equations[i][0], equations[i][1], values[i])
        
    # 处理所有查询
    ans = []
    for query in queries:
        ans.append(wuf.query(query[0], query[1]))
        
    return ans