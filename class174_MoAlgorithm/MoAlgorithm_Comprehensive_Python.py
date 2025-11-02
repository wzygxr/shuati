# -*- coding: utf-8 -*-
import math
from collections import defaultdict

# ======================= 普通莫队算法 =======================
class ClassicMoAlgorithm:
    
    @staticmethod
    def solve(a, queries):
        n = len(a)
        q = len(queries)
        block_size = int(math.sqrt(n)) + 1
        
        # 构建查询对象
        qs = []
        for i in range(q):
            l, r = queries[i]
            l -= 1  # 转换为0-based
            r -= 1
            block = l // block_size
            qs.append( (block, l, r, i) )
        
        # 排序：按左端点所在块排序，同一块内右端点升序，奇偶优化
        qs.sort(key=lambda x: (x[0], x[2] if x[0] % 2 == 0 else -x[2]))
        
        ans = [0] * q
        count = defaultdict(int)
        current_ans = 0
        current_l = 0
        current_r = -1
        
        # 添加元素函数
        def add(pos):
            nonlocal current_ans
            num = a[pos]
            if count[num] == 0:
                current_ans += 1
            count[num] += 1
        
        # 删除元素函数
        def remove(pos):
            nonlocal current_ans
            num = a[pos]
            count[num] -= 1
            if count[num] == 0:
                current_ans -= 1
        
        # 处理每个查询
        for block, l, r, idx in qs:
            while current_l > l: add(--current_l)
            while current_r < r: add(++current_r)
            while current_l < l: remove(current_l++)
            while current_r > r: remove(current_r--)
            ans[idx] = current_ans
        
        return ans

# ======================= 带修改莫队算法 =======================
class MoWithModifications:
    
    @staticmethod
    def solve(a, queries, modifications):
        n = len(a)
        q = len(queries)
        m = len(modifications)
        block_size = int(n ** (2/3)) + 1
        
        # 构建查询对象
        qs = []
        for i in range(q):
            l, r = queries[i]
            l -= 1  # 转换为0-based
            r -= 1
            t = 0  # 初始时间戳
            block = l // block_size
            qs.append( (block, r // block_size, t, l, r, i) )
        
        # 构建修改对象
        mods = []
        arr = a.copy()  # 复制原数组
        for i in range(m):
            pos, new_val = modifications[i]
            pos -= 1  # 转换为0-based
            old_val = arr[pos]
            mods.append( (pos, old_val, new_val) )
            arr[pos] = new_val  # 更新数组用于下一次修改
        
        # 恢复原数组
        for i in range(m-1, -1, -1):
            pos, old_val, _ = mods[i]
            a[pos] = old_val
        
        # 排序查询
        qs.sort()
        
        ans = [0] * q
        count = defaultdict(int)
        current_ans = 0
        current_l = 0
        current_r = -1
        current_t = 0
        arr = a.copy()  # 复制原数组用于修改
        
        # 添加元素函数
        def add(pos):
            nonlocal current_ans
            num = arr[pos]
            if count[num] == 0:
                current_ans += 1
            count[num] += 1
        
        # 删除元素函数
        def remove(pos):
            nonlocal current_ans
            num = arr[pos]
            count[num] -= 1
            if count[num] == 0:
                current_ans -= 1
        
        # 应用修改函数
        def apply_modification(t):
            pos, old_val, new_val = mods[t]
            # 保存当前值作为新的旧值
            mods[t] = (pos, new_val, old_val)
            
            # 如果修改位置在当前区间内，需要先删除旧值再添加新值
            if current_l <= pos <= current_r:
                remove(pos)
                arr[pos] = new_val
                add(pos)
            else:
                arr[pos] = new_val
        
        # 处理每个查询
        for block, r_block, t, l, r, idx in qs:
            # 调整时间戳
            while current_t < t:
                apply_modification(current_t)
                current_t += 1
            while current_t > t:
                current_t -= 1
                apply_modification(current_t)
            
            # 调整左右指针
            while current_l > l: add(--current_l)
            while current_r < r: add(++current_r)
            while current_l < l: remove(current_l++)
            while current_r > r: remove(current_r--)
            
            ans[idx] = current_ans
        
        return ans

# ======================= 回滚莫队算法 =======================
class RollbackMoAlgorithm:
    
    @staticmethod
    def solve(a, queries):
        n = len(a)
        q = len(queries)
        block_size = int(math.sqrt(n)) + 1
        
        # 构建查询对象
        qs = []
        for i in range(q):
            l, r = queries[i]
            l -= 1  # 转换为0-based
            r -= 1
            block = l // block_size
            qs.append( (block, r, l, i) )
        
        # 排序：按块号升序，右端点升序
        qs.sort(key=lambda x: (x[0], x[1]))
        
        ans = [0] * q
        count = defaultdict(int)
        current_ans = 0
        current_block = -1
        r = -1
        
        for block, query_r, query_l, idx in qs:
            # 如果进入新块，重置状态
            if block != current_block:
                count.clear()
                current_ans = 0
                current_block = block
                r = min( (current_block + 1) * block_size - 1, n - 1 )
            
            # 如果查询完全在同一块内，暴力处理
            if query_r // block_size == block:
                local_count = defaultdict(int)
                local_ans = 0
                for i in range(query_l, query_r + 1):
                    num = a[i]
                    local_count[num] += 1
                    local_ans = max(local_ans, local_count[num])
                ans[idx] = local_ans
                continue
            
            # 右端点逐步扩展（只添加不删除）
            while r < query_r:
                r += 1
                num = a[r]
                count[num] += 1
                current_ans = max(current_ans, count[num])
            
            # 左端点使用临时字典回滚
            temp_count = count.copy()
            temp_ans = current_ans
            
            # 处理左半部分
            for i in range(query_l, (current_block + 1) * block_size):
                num = a[i]
                temp_count[num] += 1
                temp_ans = max(temp_ans, temp_count[num])
            
            ans[idx] = temp_ans
        
        return ans

# ======================= 树上莫队算法 =======================
class TreeMoAlgorithm:
    
    @staticmethod
    def solve(values, edges, queries):
        n = len(values)
        q = len(queries)
        
        # 构建邻接表
        adj = [[] for _ in range(n)]
        for u, v in edges:
            u -= 1  # 转换为0-based
            v -= 1
            adj[u].append(v)
            adj[v].append(u)
        
        # 预处理：欧拉序和LCA所需信息
        in_time = [0] * n
        out_time = [0] * n
        depth = [0] * n
        parent = [-1] * n
        logn = math.floor(math.log2(n)) + 1 if n > 0 else 0
        up = [[-1] * logn for _ in range(n)]
        euler = []
        time_stamp = 0
        
        # DFS预处理
        def dfs(u, p):
            nonlocal time_stamp
            in_time[u] = time_stamp
            euler.append(u)
            time_stamp += 1
            
            parent[u] = p
            if p != -1:
                depth[u] = depth[p] + 1
            else:
                depth[u] = 0
            
            up[u][0] = p
            for k in range(1, logn):
                if up[u][k-1] != -1:
                    up[u][k] = up[up[u][k-1]][k-1]
            
            for v in adj[u]:
                if v != p:
                    dfs(v, u)
            
            out_time[u] = time_stamp
            euler.append(u)
            time_stamp += 1
        
        dfs(0, -1)  # 假设根节点为0
        
        # LCA函数
        def get_lca(u, v):
            if depth[u] < depth[v]:
                u, v = v, u
            
            # 将u提升到与v同一深度
            for k in range(logn-1, -1, -1):
                if depth[u] - (1 << k) >= depth[v]:
                    u = up[u][k]
            
            if u == v:
                return u
            
            for k in range(logn-1, -1, -1):
                if up[u][k] != -1 and up[u][k] != up[v][k]:
                    u = up[u][k]
                    v = up[v][k]
            
            return up[u][0]
        
        # 转换树查询为区间查询
        qs = []
        for i in range(q):
            u, v = queries[i]
            u -= 1  # 转换为0-based
            v -= 1
            
            if in_time[u] > in_time[v]:
                u, v = v, u
            
            ancestor = get_lca(u, v)
            
            if ancestor == u:
                # 路径u->v在欧拉序中是连续的
                l = in_time[u]
                r = in_time[v]
                lca_node = -1
            else:
                # 路径u->v需要考虑out[u]和in[v]，并额外处理LCA
                l = out_time[u]
                r = in_time[v]
                lca_node = ancestor
            
            qs.append( (l, r, lca_node, i) )
        
        # 莫队算法参数
        m = len(euler)
        block_size = int(math.sqrt(m)) + 1
        
        # 为查询添加块信息并排序
        for i in range(q):
            l, r, lca_node, idx = qs[i]
            block = l // block_size
            qs[i] = (block, r if block % 2 == 0 else -r, l, r, lca_node, idx)
        
        qs.sort()
        
        ans = [0] * q
        count = defaultdict(int)
        in_range = [False] * n
        current_ans = 0
        current_l = 0
        current_r = -1
        
        # 切换节点状态函数
        def toggle(u):
            nonlocal current_ans
            num = values[u]
            if in_range[u]:
                # 移除节点
                count[num] -= 1
                if count[num] == 0:
                    current_ans -= 1
            else:
                # 添加节点
                if count[num] == 0:
                    current_ans += 1
                count[num] += 1
            in_range[u] = not in_range[u]
        
        # 处理每个查询
        for block, _, l, r, lca_node, idx in qs:
            # 调整左右指针
            while current_l > l: toggle(euler[--current_l])
            while current_r < r: toggle(euler[++current_r])
            while current_l < l: toggle(euler[current_l++])
            while current_r > r: toggle(euler[current_r--])
            
            # 处理LCA节点
            if lca_node != -1:
                toggle(lca_node)
            
            ans[idx] = current_ans
            
            # 撤销LCA节点的处理
            if lca_node != -1:
                toggle(lca_node)
        
        return ans

# 测试代码
def test_classic_mo():
    print("===== 测试普通莫队算法 ======")
    a = [1, 2, 1, 3, 2, 4, 1, 5]
    queries = [(1, 4), (2, 6), (3, 8), (1, 8)]
    ans = ClassicMoAlgorithm.solve(a, queries)
    print("区间不同数个数结果:", ans)

def test_mo_with_modifications():
    print("\n===== 测试带修改莫队算法 ======")
    a = [1, 2, 1, 3, 2]
    queries = [(1, 3), (2, 5), (1, 5)]
    modifications = [(2, 3), (4, 4)]  # (位置, 新值)
    ans = MoWithModifications.solve(a, queries, modifications)
    print("带修改莫队结果:", ans)

def test_rollback_mo():
    print("\n===== 测试回滚莫队算法 ======")
    a = [1, 2, 1, 3, 2, 1, 4]
    queries = [(1, 4), (2, 6), (1, 7)]
    ans = RollbackMoAlgorithm.solve(a, queries)
    print("区间众数出现次数结果:", ans)

def test_tree_mo():
    print("\n===== 测试树上莫队算法 ======")
    values = [1, 2, 3, 1, 2, 4]  # 每个节点的值
    edges = [(1, 2), (1, 3), (2, 4), (2, 5), (3, 6)]  # 边
    queries = [(1, 5), (4, 6), (2, 3)]  # 路径查询
    ans = TreeMoAlgorithm.solve(values, edges, queries)
    print("树上路径不同值个数结果:", ans)

if __name__ == "__main__":
    test_classic_mo()
    test_mo_with_modifications()
    test_rollback_mo()
    test_tree_mo()