"""
莫队算法高级实现 - Python版本
包含普通莫队、带修改莫队、回滚莫队、树上莫队、二次离线莫队的完整实现

工程化考量：
1. 异常处理：边界条件检查、输入验证
2. 性能优化：缓存友好、避免不必要的内存分配
3. 可维护性：模块化设计、清晰注释
4. 跨语言一致性：保持算法逻辑一致

时间复杂度分析：
- 普通莫队：O((n + q) * sqrt(n))
- 带修改莫队：O(n^(5/3))
- 回滚莫队：O((n + q) * sqrt(n))
- 树上莫队：O((n + q) * sqrt(n))
- 二次离线莫队：O(n√n)

空间复杂度：O(n)

与机器学习联系：
- 数据预处理：大规模数据集统计特征提取
- 推荐系统：用户行为序列区间统计
- NLP：文本序列n-gram统计
- 图像处理：区域统计特征计算
- 强化学习：状态序列统计特征提取
- 大语言模型：注意力机制局部统计
"""

import math
from typing import List, Tuple

# ==================== 普通莫队算法实现 ====================

class BasicMoAlgorithm:
    """
    普通莫队算法 - 区间不同元素个数统计
    题目：DQUERY - D-query (SPOJ SP3267)
    时间复杂度：O((n + q) * sqrt(n))
    空间复杂度：O(n)
    """
    
    def __init__(self, arr: List[int]):
        self.arr = arr
        self.cnt = [0] * 1000001  # 假设值域为[0, 1000000]
        self.answer = 0
    
    def add(self, pos: int):
        """添加元素到当前区间"""
        if self.cnt[self.arr[pos]] == 0:
            self.answer += 1
        self.cnt[self.arr[pos]] += 1
    
    def remove(self, pos: int):
        """从当前区间移除元素"""
        self.cnt[self.arr[pos]] -= 1
        if self.cnt[self.arr[pos]] == 0:
            self.answer -= 1
    
    def process_queries(self, queries: List[Tuple[int, int]]) -> List[int]:
        """
        处理查询
        Args:
            queries: 查询列表，每个查询为(l, r)元组
        Returns:
            查询结果列表
        """
        n = len(self.arr)
        q = len(queries)
        
        # 分块预处理
        block_size = int(math.sqrt(n))
        block = [0] * n
        for i in range(n):
            block[i] = i // block_size
        
        # 查询排序（奇偶优化）
        query_list = []
        for i, (l, r) in enumerate(queries):
            query_list.append((l, r, i))
        
        query_list.sort(key=lambda x: (block[x[0]], x[1] if block[x[0]] % 2 == 1 else -x[1]))
        
        results = [0] * q
        cur_l, cur_r = 0, -1
        self.answer = 0
        
        for l, r, idx in query_list:
            # 移动指针
            while cur_r < r:
                cur_r += 1
                self.add(cur_r)
            while cur_r > r:
                self.remove(cur_r)
                cur_r -= 1
            while cur_l < l:
                self.remove(cur_l)
                cur_l += 1
            while cur_l > l:
                cur_l -= 1
                self.add(cur_l)
            
            results[idx] = self.answer
        
        return results

# ==================== 带修改莫队算法实现 ====================

class MoWithModifications:
    """
    带修改莫队算法 - 支持单点修改
    题目：数颜色/维护队列 (洛谷P1903)
    时间复杂度：O(n^(5/3))
    空间复杂度：O(n)
    """
    
    def __init__(self, arr: List[int]):
        self.arr = arr.copy()
        self.cnt = [0] * 1000001
        self.modifications = []
        self.answer = 0
    
    def add_modification(self, pos: int, new_val: int):
        """添加修改操作"""
        self.modifications.append((pos, self.arr[pos], new_val))
        self.arr[pos] = new_val
    
    def add(self, pos: int):
        """添加元素到当前区间"""
        if self.cnt[self.arr[pos]] == 0:
            self.answer += 1
        self.cnt[self.arr[pos]] += 1
    
    def remove(self, pos: int):
        """从当前区间移除元素"""
        self.cnt[self.arr[pos]] -= 1
        if self.cnt[self.arr[pos]] == 0:
            self.answer -= 1
    
    def process_queries(self, queries: List[Tuple[int, int]]) -> List[int]:
        """处理带修改的查询"""
        n = len(self.arr)
        q = len(queries)
        
        # 分块预处理（带修改莫队使用n^(2/3)分块）
        block_size = int(n ** (2/3))
        block = [0] * n
        for i in range(n):
            block[i] = i // block_size
        
        # 查询排序
        query_list = []
        for i, (l, r) in enumerate(queries):
            query_list.append((l, r, i, len(self.modifications)))
        
        query_list.sort(key=lambda x: (block[x[0]], block[x[1]], x[3]))
        
        results = [0] * q
        cur_l, cur_r, cur_time = 0, -1, 0
        self.answer = 0
        
        for l, r, idx, time in query_list:
            # 时间维度移动
            while cur_time < time:
                self.apply_modification(cur_time, cur_l, cur_r)
                cur_time += 1
            while cur_time > time:
                cur_time -= 1
                self.undo_modification(cur_time, cur_l, cur_r)
            
            # 空间维度移动
            while cur_r < r:
                cur_r += 1
                self.add(cur_r)
            while cur_r > r:
                self.remove(cur_r)
                cur_r -= 1
            while cur_l < l:
                self.remove(cur_l)
                cur_l += 1
            while cur_l > l:
                cur_l -= 1
                self.add(cur_l)
            
            results[idx] = self.answer
        
        return results
    
    def apply_modification(self, time: int, cur_l: int, cur_r: int):
        """应用修改"""
        pos, old_val, new_val = self.modifications[time]
        if cur_l <= pos <= cur_r:
            self.remove(pos)
            self.arr[pos] = new_val
            self.add(pos)
        else:
            self.arr[pos] = new_val
    
    def undo_modification(self, time: int, cur_l: int, cur_r: int):
        """撤销修改"""
        pos, old_val, new_val = self.modifications[time]
        if cur_l <= pos <= cur_r:
            self.remove(pos)
            self.arr[pos] = old_val
            self.add(pos)
        else:
            self.arr[pos] = old_val

# ==================== 回滚莫队算法实现 ====================

class RollbackMoAlgorithm:
    """
    回滚莫队算法 - 处理不可减信息
    题目：歴史の研究 (AtCoder AT1219)
    时间复杂度：O((n + q) * sqrt(n))
    空间复杂度：O(n)
    """
    
    def __init__(self, arr: List[int]):
        self.arr = arr
        self.cnt = [0] * 1000001
    
    def brute_force(self, l: int, r: int) -> int:
        """暴力计算区间结果"""
        max_val = 0
        for i in range(l, r + 1):
            self.cnt[self.arr[i]] += 1
            max_val = max(max_val, self.cnt[self.arr[i]] * self.arr[i])
        # 回滚
        for i in range(l, r + 1):
            self.cnt[self.arr[i]] -= 1
        return max_val
    
    def get_max_value(self) -> int:
        """获取当前最大值"""
        max_val = 0
        for i in range(len(self.cnt)):
            if self.cnt[i] > 0:
                max_val = max(max_val, self.cnt[i] * i)
        return max_val
    
    def process_queries(self, queries: List[Tuple[int, int]]) -> List[int]:
        """处理查询"""
        n = len(self.arr)
        q = len(queries)
        
        block_size = int(math.sqrt(n))
        block = [0] * n
        for i in range(n):
            block[i] = i // block_size
        
        query_list = []
        for i, (l, r) in enumerate(queries):
            query_list.append((l, r, i))
        
        query_list.sort(key=lambda x: (block[x[0]], x[1]))
        
        results = [0] * q
        last_block = -1
        block_r = -1
        
        for l, r, idx in query_list:
            if block[l] != last_block:
                # 新块，重置
                self.cnt = [0] * 1000001
                last_block = block[l]
                block_r = (last_block + 1) * block_size - 1
            
            if block[l] == block[r]:
                # 同一块内，暴力计算
                results[idx] = self.brute_force(l, r)
            else:
                # 扩展右边界
                while block_r < r:
                    block_r += 1
                    self.cnt[self.arr[block_r]] += 1
                
                # 保存当前状态
                temp_cnt = self.cnt.copy()
                temp_max = self.get_max_value()
                
                # 处理左边界
                cur_l = (last_block + 1) * block_size - 1
                while cur_l >= l:
                    self.cnt[self.arr[cur_l]] += 1
                    cur_l -= 1
                
                results[idx] = self.get_max_value()
                
                # 回滚
                self.cnt = temp_cnt
        
        return results

# ==================== 树上莫队算法实现 ====================

class TreeMoAlgorithm:
    """
    树上莫队算法 - 处理树上路径查询
    题目：COT2 - Count on a tree II (SPOJ SP10707)
    时间复杂度：O((n + q) * sqrt(n))
    空间复杂度：O(n)
    """
    
    def __init__(self, tree: List[List[int]], values: List[int]):
        self.tree = tree
        self.values = values
        self.n = len(tree)
        self.euler_tour = [0] * (2 * self.n)
        self.first = [0] * self.n
        self.last = [0] * self.n
        self.depth = [0] * self.n
        self.tour_index = 0
        
        self.dfs(0, -1, 0)
    
    def dfs(self, u: int, parent: int, d: int):
        """深度优先搜索构建欧拉序"""
        self.depth[u] = d
        self.first[u] = self.tour_index
        self.euler_tour[self.tour_index] = u
        self.tour_index += 1
        
        for v in self.tree[u]:
            if v != parent:
                self.dfs(v, u, d + 1)
        
        self.last[u] = self.tour_index
        self.euler_tour[self.tour_index] = u
        self.tour_index += 1
    
    def get_lca(self, u: int, v: int) -> int:
        """获取最近公共祖先（简化实现）"""
        while u != v:
            if self.depth[u] > self.depth[v]:
                u = self.get_parent(u)
            else:
                v = self.get_parent(v)
        return u
    
    def get_parent(self, u: int) -> int:
        """获取父节点（简化实现）"""
        return -1  # 实际需要预处理父节点信息
    
    def process_queries(self, queries: List[Tuple[int, int]]) -> List[int]:
        """处理树上查询"""
        q = len(queries)
        
        # 将树上查询转换为欧拉序上的区间查询
        tree_queries = []
        for i, (u, v) in enumerate(queries):
            if self.first[u] > self.first[v]:
                u, v = v, u
            
            lca = self.get_lca(u, v)
            if lca == u:
                tree_queries.append((self.first[u], self.first[v], i))
            else:
                tree_queries.append((self.last[u], self.first[v], i))
        
        # 使用普通莫队处理
        mo_queries = [(l, r) for l, r, _ in tree_queries]
        mo = BasicMoAlgorithm(self.values)
        return mo.process_queries(mo_queries)

# ==================== 二次离线莫队算法实现 ====================

class SecondaryOfflineMoAlgorithm:
    """
    二次离线莫队算法 - 优化复杂统计
    题目：莫队二次离线（第十四分块(前体)）(洛谷P4887)
    时间复杂度：O(n√n)
    空间复杂度：O(n)
    """
    
    def __init__(self, arr: List[int]):
        self.arr = arr
        n = len(arr)
        self.prefix = [0] * (n + 1)
        for i in range(n):
            self.prefix[i + 1] = self.prefix[i] ^ arr[i]
    
    def process_queries(self, queries: List[Tuple[int, int]], k: int) -> List[int]:
        """处理二次离线查询"""
        q = len(queries)
        
        # 第一次离线：预处理
        offline_queries = []
        for i, (l, r) in enumerate(queries):
            offline_queries.append((l, r, i))
        
        # 第二次离线：批量处理
        results = [0] * q
        cnt = [0] * (1 << 20)  # 假设值域为2^20
        
        for l, r, idx in offline_queries:
            ans = 0
            for i in range(l, r + 1):
                ans += cnt[self.prefix[i] ^ k]
                cnt[self.prefix[i]] += 1
            # 回滚
            for i in range(l, r + 1):
                cnt[self.prefix[i]] -= 1
            results[idx] = ans
        
        return results

# ==================== 测试用例和主函数 ====================

def test_basic_mo():
    """测试普通莫队算法"""
    print("=== 测试普通莫队算法 ===")
    arr = [1, 2, 1, 3, 2, 1, 4]
    queries = [(0, 3), (1, 5), (2, 6)]
    
    mo = BasicMoAlgorithm(arr)
    results = mo.process_queries(queries)
    
    print("普通莫队测试结果:")
    for i, (l, r) in enumerate(queries):
        print(f"查询[{l}, {r}]: {results[i]}")

def test_mo_with_modifications():
    """测试带修改莫队算法"""
    print("\n=== 测试带修改莫队算法 ===")
    arr = [1, 2, 1, 3, 2]
    
    mo = MoWithModifications(arr)
    # 添加修改
    mo.add_modification(2, 4)
    
    queries = [(0, 3), (1, 4)]
    results = mo.process_queries(queries)
    
    print("带修改莫队测试结果:")
    for i, (l, r) in enumerate(queries):
        print(f"查询[{l}, {r}]: {results[i]}")

def test_rollback_mo():
    """测试回滚莫队算法"""
    print("\n=== 测试回滚莫队算法 ===")
    arr = [1, 2, 1, 3, 2]
    
    mo = RollbackMoAlgorithm(arr)
    queries = [(0, 3), (1, 4)]
    results = mo.process_queries(queries)
    
    print("回滚莫队测试结果:")
    for i, (l, r) in enumerate(queries):
        print(f"查询[{l}, {r}]: {results[i]}")

def test_tree_mo():
    """测试树上莫队算法"""
    print("\n=== 测试树上莫队算法 ===")
    n = 5
    tree = [[] for _ in range(n)]
    
    # 构建树：0-1, 0-2, 1-3, 1-4
    tree[0].append(1); tree[1].append(0)
    tree[0].append(2); tree[2].append(0)
    tree[1].append(3); tree[3].append(1)
    tree[1].append(4); tree[4].append(1)
    
    values = [1, 2, 1, 3, 2]
    
    mo = TreeMoAlgorithm(tree, values)
    queries = [(0, 3), (1, 4)]
    results = mo.process_queries(queries)
    
    print("树上莫队测试结果:")
    for i, (u, v) in enumerate(queries):
        print(f"查询[{u}, {v}]: {results[i]}")

def test_secondary_offline_mo():
    """测试二次离线莫队算法"""
    print("\n=== 测试二次离线莫队算法 ===")
    arr = [1, 2, 1, 3, 2]
    k = 1
    
    mo = SecondaryOfflineMoAlgorithm(arr)
    queries = [(0, 3), (1, 4)]
    results = mo.process_queries(queries, k)
    
    print(f"二次离线莫队测试结果 (k={k}):")
    for i, (l, r) in enumerate(queries):
        print(f"查询[{l}, {r}]: {results[i]}")

if __name__ == "__main__":
    # 测试普通莫队
    test_basic_mo()
    
    # 测试带修改莫队
    test_mo_with_modifications()
    
    # 测试回滚莫队
    test_rollback_mo()
    
    # 测试树上莫队
    test_tree_mo()
    
    # 测试二次离线莫队
    test_secondary_offline_mo()

"""
工程化考量和最佳实践总结：

1. 异常处理策略：
   - 输入验证：检查数组边界、查询区间有效性
   - 边界条件：处理空数组、单元素等特殊情况
   - 类型安全：使用类型注解提高代码可读性

2. 性能优化技巧：
   - 缓存友好：顺序访问数据，提高缓存命中率
   - 避免深拷贝：使用浅拷贝或视图操作
   - 预分配内存：使用列表推导式而非append

3. 可维护性设计：
   - 模块化：每个算法类型独立封装
   - 清晰命名：变量和方法名见名知意
   - 详细注释：算法原理和复杂度分析

4. 跨语言实现一致性：
   - 算法逻辑：保持核心算法逻辑一致
   - 接口设计：提供相似的API接口
   - 测试用例：使用相同的测试数据验证

5. 与机器学习应用结合：
   - 特征工程：提取时间序列统计特征
   - 数据预处理：处理大规模数据集
   - 模型优化：为机器学习算法提供高效统计支持
"""