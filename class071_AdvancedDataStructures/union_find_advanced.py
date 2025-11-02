"""
高级并查集实现
包含：
1. 回滚并查集（支持离线动态连通性问题）
2. 时间轴并查集
3. 二分图判定建模

时间复杂度：
- 回滚并查集：O(α(n) log n) 每次操作
- 时间轴并查集：O(α(n) log n) 每次操作
- 二分图判定：O(α(n)) 每次操作

空间复杂度：O(n)

设计要点：
1. 路径压缩和按秩合并优化
2. 支持撤销操作的日志记录
3. 支持时间维度的查询
4. 支持二分图的双色标记
5. 工程化考量：异常处理、边界检查、内存优化

典型应用场景：
- 回滚并查集：离线动态连通性问题、Kruskal算法中的环检测
- 时间轴并查集：带时间限制的连通性问题
- 二分图判定：图的双色问题、冲突检测
"""


class RollbackUnionFind:
    """
    回滚并查集（支持撤销操作）
    """
    
    def __init__(self, n):
        """
        构造函数
        
        Args:
            n: 节点数量
        """
        self.parent = list(range(n))  # 父节点数组，初始化每个节点的父节点是自己
        self.rank = [1] * n           # 秩（树高上界）
        self.history = []             # 操作历史记录
        self.set_count = n            # 集合数量
    
    def find(self, x):
        """
        查找根节点（带路径压缩）
        
        Args:
            x: 节点
            
        Returns:
            根节点
        """
        while self.parent[x] != x:
            x = self.parent[x]
        return x
    
    def union(self, x, y):
        """
        合并两个集合（带按秩合并，记录操作历史）
        
        Args:
            x: 节点x
            y: 节点y
            
        Returns:
            是否成功合并（如果已经在同一集合返回False）
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False
        
        # 记录操作历史
        self.history.append({
            'x': root_x,
            'px': self.parent[root_x],
            'y': root_y,
            'py': self.parent[root_y],
            'rank_y': self.rank[root_y]
        })
        
        # 按秩合并：将秩小的树合并到秩大的树上
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        
        self.set_count -= 1
        return True
    
    def rollback(self):
        """
        撤销上一次合并操作
        """
        if not self.history:
            raise RuntimeError("No operation to rollback")
        
        op = self.history.pop()
        self.parent[op['x']] = op['px']
        self.parent[op['y']] = op['py']
        self.rank[op['y']] = op['rank_y']
        self.set_count += 1
    
    def get_set_count(self):
        """
        获取集合数量
        
        Returns:
            集合数量
        """
        return self.set_count
    
    def is_connected(self, x, y):
        """
        判断两个节点是否连通
        
        Args:
            x: 节点x
            y: 节点y
            
        Returns:
            是否连通
        """
        return self.find(x) == self.find(y)


class TemporalUnionFind:
    """
    时间轴并查集（支持历史版本查询）
    注意：这里的实现是基于离线处理的，需要预先知道所有操作
    """
    
    def __init__(self, n, max_time):
        """
        构造函数
        
        Args:
            n: 节点数量
            max_time: 最大时间戳
        """
        self.parent = list(range(n))     # 父节点数组，初始化每个节点的父节点是自己
        self.rank = [1] * n              # 秩数组
        self.time = [0] * n              # 记录父节点变化的时间
        self.history = [[] for _ in range(max_time + 1)]  # 历史记录 [时间][节点]
        self.current_time = 0            # 当前时间戳
        self.n = n                       # 节点数量
        
        # 初始化历史记录
        for i in range(n):
            self.history[0].append(i)
    
    def find(self, x, t):
        """
        查找在指定时间点的根节点
        
        Args:
            x: 节点
            t: 时间点
            
        Returns:
            根节点
        """
        # 基础版本的实现，实际应用中可能需要更复杂的历史记录方式
        while self.parent[x] != x and self.time[self.parent[x]] <= t:
            x = self.parent[x]
        return x
    
    def union(self, x, y):
        """
        合并两个集合并记录时间
        
        Args:
            x: 节点x
            y: 节点y
            
        Returns:
            是否成功合并
        """
        self.current_time += 1
        
        root_x = self.find(x, self.current_time - 1)
        root_y = self.find(y, self.current_time - 1)
        
        if root_x == root_y:
            # 复制上一时间点的状态
            self.history[self.current_time] = self.history[self.current_time - 1].copy()
            return False
        
        # 复制上一时间点的状态
        self.history[self.current_time] = self.history[self.current_time - 1].copy()
        
        # 按秩合并
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
            self.time[root_x] = self.current_time
            self.history[self.current_time][root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
            self.time[root_y] = self.current_time
            self.history[self.current_time][root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.time[root_y] = self.current_time
            self.history[self.current_time][root_y] = root_x
            self.rank[root_x] += 1
        
        return True
    
    def is_connected(self, x, y, t):
        """
        查询在特定时间点两个节点是否连通
        
        Args:
            x: 节点x
            y: 节点y
            t: 时间点
            
        Returns:
            是否连通
        """
        return self.find(x, t) == self.find(y, t)
    
    def get_current_time(self):
        """
        获取当前时间
        
        Returns:
            当前时间戳
        """
        return self.current_time


class BipartiteUnionFind:
    """
    二分图判定并查集（带权并查集）
    用权值表示与父节点的关系（0表示同色，1表示异色）
    """
    
    def __init__(self, n):
        """
        构造函数
        
        Args:
            n: 节点数量
        """
        self.parent = list(range(n))  # 父节点数组，初始化每个节点的父节点是自己
        self.rank = [1] * n           # 秩数组
        self.color = [0] * n          # 与父节点的颜色关系（0同色，1异色）
        self.is_bipartite = True      # 是否是二分图
    
    def find(self, x):
        """
        查找根节点（带路径压缩和颜色关系更新）
        
        Args:
            x: 节点
            
        Returns:
            根节点
        """
        if self.parent[x] != x:
            root = self.find(self.parent[x])
            # 更新颜色关系：x到根的颜色 = x到父节点的颜色 + 父节点到根的颜色
            self.color[x] ^= self.color[self.parent[x]]
            self.parent[x] = root
        return self.parent[x]
    
    def union(self, x, y, is_same):
        """
        合并两个节点，并检查是否是二分图
        
        Args:
            x: 节点x
            y: 节点y
            is_same: 是否要求同色（False表示异色）
            
        Returns:
            是否成功合并且不破坏二分图性质
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            # 检查颜色关系是否符合要求
            check = (self.color[x] ^ self.color[y]) == (0 if is_same else 1)
            if not check:
                self.is_bipartite = False
            return check
        
        # 按秩合并
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
            # 计算color[root_x]使得颜色关系成立
            self.color[root_x] = self.color[x] ^ self.color[y] ^ (0 if is_same else 1)
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
            self.color[root_y] = self.color[x] ^ self.color[y] ^ (0 if is_same else 1)
        else:
            self.parent[root_y] = root_x
            self.color[root_y] = self.color[x] ^ self.color[y] ^ (0 if is_same else 1)
            self.rank[root_x] += 1
        
        return True
    
    def add_edge(self, x, y):
        """
        添加一条边（x和y必须异色）
        
        Args:
            x: 节点x
            y: 节点y
            
        Returns:
            是否成功添加
        """
        return self.union(x, y, False)
    
    def is_bipartite_graph(self):
        """
        判断当前图是否是二分图
        
        Returns:
            是否是二分图
        """
        return self.is_bipartite
    
    def get_color(self, x):
        """
        获取节点x的颜色（相对于根节点）
        
        Args:
            x: 节点
            
        Returns:
            颜色值
        """
        self.find(x)  # 确保路径压缩
        return self.color[x]


def test_rollback_union_find():
    """
    测试回滚并查集
    """
    print("===== 测试回滚并查集 ====")
    uf = RollbackUnionFind(5)
    
    uf.union(0, 1)
    uf.union(2, 3)
    print(f"0和1连通: {uf.is_connected(0, 1)}")  # True
    print(f"0和2连通: {uf.is_connected(0, 2)}")  # False
    print(f"集合数量: {uf.get_set_count()}")      # 3
    
    # 回滚操作
    uf.rollback()
    print(f"回滚一次后，集合数量: {uf.get_set_count()}")  # 4
    print(f"2和3连通: {uf.is_connected(2, 3)}")   # False
    
    # 再次合并
    uf.union(1, 2)
    print(f"0和2连通: {uf.is_connected(0, 2)}")   # True


def test_temporal_union_find():
    """
    测试时间轴并查集
    """
    print("\n===== 测试时间轴并查集 ====")
    tuf = TemporalUnionFind(5, 10)
    
    tuf.union(0, 1)  # 时间1
    tuf.union(2, 3)  # 时间2
    
    print(f"时间2时，0和1连通: {tuf.is_connected(0, 1, 2)}")  # True
    print(f"时间2时，0和2连通: {tuf.is_connected(0, 2, 2)}")  # False
    
    tuf.union(1, 2)  # 时间3
    print(f"时间3时，0和2连通: {tuf.is_connected(0, 2, 3)}")  # True
    print(f"时间2时，0和2连通: {tuf.is_connected(0, 2, 2)}")  # False (历史时间查询)


def test_bipartite_union_find():
    """
    测试二分图判定
    """
    print("\n===== 测试二分图判定 ====")
    
    # 测试1：二分图（无向图：0-1-2-3，0-3）
    bipartite = BipartiteUnionFind(4)
    bipartite.add_edge(0, 1)
    bipartite.add_edge(1, 2)
    bipartite.add_edge(2, 3)
    bipartite.add_edge(0, 3)
    print(f"测试1是否是二分图: {bipartite.is_bipartite_graph()}")  # True
    
    # 测试2：非二分图（无向图：0-1-2-0）
    non_bipartite = BipartiteUnionFind(3)
    non_bipartite.add_edge(0, 1)
    non_bipartite.add_edge(1, 2)
    result = non_bipartite.add_edge(2, 0)
    print(f"测试2是否是二分图: {non_bipartite.is_bipartite_graph()}")  # False
    print(f"添加边2-0是否成功: {result}")  # False


def main():
    """
    主函数用于测试
    """
    # 测试回滚并查集
    test_rollback_union_find()
    
    # 测试时间轴并查集
    test_temporal_union_find()
    
    # 测试二分图判定
    test_bipartite_union_find()


if __name__ == "__main__":
    main()