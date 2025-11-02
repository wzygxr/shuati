# 动态图连通性问题实现（线段树分治 + 可撤销并查集）

import sys
import os

class RollbackDSU:
    """
    可撤销并查集（Rollback Disjoint Set Union）
    
    用于处理需要回滚操作的并查集问题，是线段树分治算法的核心数据结构之一。
    
    注意：为了支持回滚操作，这里不使用路径压缩优化，只使用按秩合并。
    
    时间复杂度分析：
    - find: O(log n) - 由于没有路径压缩，每次查询需要O(log n)时间
    - union: O(log n) - 按秩合并保证了树的高度为O(log n)
    - rollback: O(1) - 只需要弹出最后一次操作并恢复状态
    
    空间复杂度分析：
    - O(n + m)，其中n是节点数，m是合并操作次数
    """
    def __init__(self, size):
        """
        初始化可撤销并查集
        
        参数：
            size: 节点数量
        """
        self.father = list(range(size))  # 父节点数组，初始时每个节点的父节点是自身
        self.rank = [1] * size          # 秩数组（树高上界）
        self.history = []               # 操作历史记录，用于回滚
        self.version = 0                # 当前版本号
    
    def find(self, x):
        """
        查找节点x的根节点
        
        参数：
            x: 要查找的节点
        
        返回值：
            节点x的根节点
        """
        # 不使用路径压缩，以支持回滚操作
        while x != self.father[x]:
            x = self.father[x]
        return x
    
    def union(self, x, y):
        """
        合并包含节点x和节点y的集合
        
        参数：
            x, y: 要合并的两个节点
        
        返回值：
            bool: 如果x和y原来不在同一个集合中，则返回True；否则返回False
        """
        # 查找x和y的根节点
        fx = self.find(x)
        fy = self.find(y)
        
        # 如果已经在同一个集合中，不需要合并，直接返回False
        if fx == fy:
            return False
        
        # 按秩合并：将秩较小的树合并到秩较大的树上
        # 这样可以保证树的高度较小，提高查询效率
        if self.rank[fx] < self.rank[fy]:
            fx, fy = fy, fx
        
        # 记录操作前的状态，用于回滚
        self.history.append((fy, self.father[fy], fx, self.rank[fx]))
        self.version += 1
        
        # 执行合并操作
        self.father[fy] = fx
        
        # 如果两棵树的秩相等，则合并后树的秩加1
        if self.rank[fx] == self.rank[fy]:
            self.rank[fx] += 1
        
        return True
    
    def rollback(self, version):
        """
        回滚到指定版本
        
        参数：
            version: 要回滚到的版本号
        """
        # 当当前版本号大于目标版本时，不断回滚操作
        while self.version > version:
            # 获取最后一次操作的状态
            fy, father_fy, fx, rank_fx = self.history.pop()
            
            # 恢复父节点和秩的状态
            self.father[fy] = father_fy
            self.rank[fx] = rank_fx
            
            # 版本号减1
            self.version -= 1

class SegmentTreeDivideConquer:
    """
    线段树分治算法模板
    
    用于处理离线的动态问题，将时间段上的操作拆分成多个区间，在线段树的节点上进行处理。
    
    时间复杂度分析：
    - 初始化：O(Q)，其中Q是最大时间范围
    - 添加操作：O(log Q)，每个操作需要拆分到O(log Q)个线段树节点
    - 求解：O((n + m) log Q)，其中n是节点数，m是操作数，Q是时间范围
    
    空间复杂度分析：
    - O(m log Q)，主要用于存储线段树节点上的操作
    """
    def __init__(self, max_time):
        """
        初始化线段树分治结构
        
        参数：
            max_time: 最大时间范围
        """
        self.max_time = max_time
        # 为线段树分配足够的空间，4倍大小通常足够
        self.operations = [[] for _ in range(4 * (max_time + 1))]
    
    def add_operation(self, l, r, op):
        """
        添加在时间区间[l, r]内有效的操作
        
        参数：
            l: 操作的起始时间
            r: 操作的结束时间
            op: 操作的具体内容（这里是边的两个节点）
        """
        self._update(1, 1, self.max_time, l, r, op)
    
    def _update(self, node, node_l, node_r, l, r, op):
        """
        线段树更新操作：将操作op添加到所有覆盖时间区间[l, r]的节点中
        
        参数：
            node: 当前节点编号
            node_l: 当前节点对应的左边界
            node_r: 当前节点对应的右边界
            l: 操作的起始时间
            r: 操作的结束时间
            op: 操作的具体内容
        """
        # 如果当前节点的区间与操作的区间不相交，则直接返回
        if node_r < l or node_l > r:
            return
        
        # 如果当前节点的区间完全包含在操作的区间内，则将操作添加到当前节点
        if l <= node_l and node_r <= r:
            self.operations[node].append(op)
            return
        
        # 否则，递归处理左右子节点
        mid = (node_l + node_r) // 2
        self._update(2 * node, node_l, mid, l, r, op)
        self._update(2 * node + 1, mid + 1, node_r, l, r, op)
    
    def solve(self, process_func, rollback_func):
        """
        执行线段树分治
        
        参数：
            process_func: 处理当前节点操作的函数
            rollback_func: 回滚操作的函数
        """
        self._dfs(1, 1, self.max_time, process_func, rollback_func)
    
    def _dfs(self, node, node_l, node_r, process_func, rollback_func):
        """
        DFS遍历线段树，处理每个时间区间的操作
        
        参数：
            node: 当前节点编号
            node_l: 当前节点对应的左边界
            node_r: 当前节点对应的右边界
            process_func: 处理当前节点操作的函数
            rollback_func: 回滚操作的函数
        """
        # 记录当前版本，用于回滚
        current_version = rollback_func()
        
        # 处理当前节点的所有操作
        for op in self.operations[node]:
            process_func(op)
        
        # 如果当前节点是叶子节点，执行查询或其他操作
        if node_l == node_r:
            # 这里可以添加查询处理逻辑
            # 例如，对于动态图连通性问题，可以处理查询两个节点是否连通
            pass
        else:
            # 否则，递归处理左右子节点
            mid = (node_l + node_r) // 2
            self._dfs(2 * node, node_l, mid, process_func, rollback_func)
            self._dfs(2 * node + 1, mid + 1, node_r, process_func, rollback_func)
        
        # 回滚到进入当前节点前的状态
        rollback_func(current_version)

# 动态图连通性问题的实现示例

def dynamic_graph_connectivity():
    """
    解决动态图连通性问题
    
    问题描述：
    给定一个动态图，图中的边会在某些时间点出现，在其他时间点消失。
    需要处理多个查询，每个查询询问在某个时间点两个节点是否连通。
    
    解决方案：
    使用线段树分治结合可撤销并查集来离线处理所有的边和查询。
    
    时间复杂度分析：
    - O(m log n log Q + q α(n))，其中m是边数，q是查询数，Q是时间范围
    - 对于每条边，需要O(log Q)次线段树操作，每次操作需要O(log n)时间
    - 对于每个查询，需要O(α(n))时间，其中α是阿克曼函数的反函数，可以视为常数
    
    空间复杂度分析：
    - O(m log Q + n + q)，主要用于存储线段树节点上的边、可撤销并查集和查询
    """
    # 读取输入
    n, m, q = map(int, sys.stdin.readline().split())
    
    # 存储所有边及其时间区间
    edges = []
    
    # 读取所有边
    for _ in range(m):
        u, v, l, r = map(int, sys.stdin.readline().split())
        u -= 1  # 转换为0-based索引
        v -= 1
        edges.append((u, v, l, r))
    
    # 存储所有查询
    queries = []
    for i in range(q):
        u, v, t = map(int, sys.stdin.readline().split())
        u -= 1
        v -= 1
        queries.append((u, v, t, i))
    
    # 按时间排序查询
    queries.sort(key=lambda x: x[2])
    
    # 初始化线段树分治结构
    max_time = max(r for _, _, _, r in edges)
    stdc = SegmentTreeDivideConquer(max_time)
    
    # 添加边到线段树分治结构中
    for u, v, l, r in edges:
        stdc.add_operation(l, r, (u, v))
    
    # 初始化可撤销并查集
    dsu = RollbackDSU(n)
    
    # 结果数组，按查询顺序存储答案
    results = [False] * q
    
    # 当前处理的查询索引
    current_query = 0
    
    # 处理函数：合并两个节点
    def process_op(op):
        u, v = op
        dsu.union(u, v)
    
    # 回滚函数：回滚到指定版本
    def rollback_op(version):
        if version is None:
            return dsu.version
        dsu.rollback(version)
        return None
    
    # 自定义DFS函数，用于处理查询
    def dfs_with_queries(node, node_l, node_r):
        nonlocal current_query
        
        # 记录当前版本
        current_version = dsu.version
        
        # 处理当前节点的所有边
        for u, v in stdc.operations[node]:
            dsu.union(u, v)
        
        # 处理当前时间点的所有查询
        if node_l == node_r:
            # 处理所有时间为node_l的查询
            while current_query < q and queries[current_query][2] == node_l:
                u, v, t, idx = queries[current_query]
                # 检查u和v是否连通
                results[idx] = (dsu.find(u) == dsu.find(v))
                current_query += 1
        else:
            # 递归处理左右子节点
            mid = (node_l + node_r) // 2
            dfs_with_queries(2 * node, node_l, mid)
            dfs_with_queries(2 * node + 1, mid + 1, node_r)
        
        # 回滚到进入当前节点前的状态
        dsu.rollback(current_version)
    
    # 执行自定义的DFS，处理所有查询
    dfs_with_queries(1, 1, max_time)
    
    # 输出结果
    for res in results:
        print("YES" if res else "NO")

# 二分图判定问题的实现示例

def bipartite_checking():
    """
    解决动态二分图判定问题
    
    问题描述：
    给定一个动态图，判断在每个时间点图是否是二分图。
    
    解决方案：
    使用线段树分治结合扩展域可撤销并查集来离线处理所有的边，并检测是否存在奇环。
    
    时间复杂度分析：
    - O(m log n log Q)，其中m是边数，Q是时间范围
    - 对于每条边，需要O(log Q)次线段树操作，每次操作需要O(log n)时间
    
    空间复杂度分析：
    - O(m log Q + n)，主要用于存储线段树节点上的边和扩展域可撤销并查集
    """
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    
    # 存储所有边及其时间区间
    edges = []
    for _ in range(m):
        u, v, l, r = map(int, sys.stdin.readline().split())
        u -= 1  # 转换为0-based索引
        v -= 1
        edges.append((u, v, l, r))
    
    # 初始化线段树分治结构
    max_time = max(r for _, _, _, r in edges)
    stdc = SegmentTreeDivideConquer(max_time)
    
    # 添加边到线段树分治结构中
    for u, v, l, r in edges:
        stdc.add_operation(l, r, (u, v))
    
    # 初始化扩展域可撤销并查集
    # 扩展域并查集：每个节点u有两个表示，u表示与集合根节点同色，u+n表示与集合根节点异色
    dsu = RollbackDSU(2 * n)
    
    # 结果数组，记录每个时间点是否是二分图
    is_bipartite = [True] * (max_time + 2)  # 时间从1到max_time
    
    # 处理二分图问题的DFS函数
    def dfs_bipartite(node, node_l, node_r):
        # 记录当前版本
        current_version = dsu.version
        
        # 标记当前区间是否发生冲突
        conflict_in_this_node = False
        
        # 处理当前节点的所有边
        for u, v in stdc.operations[node]:
            # 检查添加这条边是否会导致矛盾
            # 如果u和v已经在同一个集合中，或者u+n和v+n在同一个集合中，则存在奇环
            if dsu.find(u) == dsu.find(v) or dsu.find(u + n) == dsu.find(v + n):
                conflict_in_this_node = True
                # 标记该区间内所有时间点都不是二分图
                for t in range(node_l, node_r + 1):
                    if 1 <= t <= max_time:
                        is_bipartite[t] = False
                # 由于已经发现冲突，可以跳过继续添加边
                break
            
            # 正常添加边：u和v必须异色，u+n和v必须同色，u和v+n必须同色
            dsu.union(u, v + n)
            dsu.union(u + n, v)
        
        # 如果当前节点没有冲突，且不是叶子节点，则继续递归
        if not conflict_in_this_node and node_l < node_r:
            mid = (node_l + node_r) // 2
            dfs_bipartite(2 * node, node_l, mid)
            dfs_bipartite(2 * node + 1, mid + 1, node_r)
        
        # 回滚到进入当前节点前的状态
        dsu.rollback(current_version)
    
    # 执行二分图检测的DFS
    dfs_bipartite(1, 1, max_time)
    
    # 输出每个时间点的结果
    for t in range(1, max_time + 1):
        print("Yes" if is_bipartite[t] else "No")

# 主函数，用于测试和演示
def main():
    # 这里可以添加测试用例和调用上述函数的代码
    # 例如：
    # dynamic_graph_connectivity()
    # bipartite_checking()
    pass

if __name__ == "__main__":
    main()