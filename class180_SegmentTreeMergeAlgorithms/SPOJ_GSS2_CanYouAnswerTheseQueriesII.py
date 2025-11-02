"""
SPOJ GSS2 - Can you answer these queries II

题目描述:
给定一个整数数组，多次查询某个区间内的最大子段和，但重复元素只计算一次。

解题思路:
这是一个非常经典的线段树问题，需要使用扫描线算法和历史信息维护。

关键点:
1. 使用离线处理，将所有查询按右端点排序
2. 使用扫描线从左到右处理数组元素
3. 维护一个线段树，支持区间加法和历史最大值查询
4. 使用last数组记录每个值最后出现的位置

时间复杂度: O((n + q) * log n)
空间复杂度: O(n)
"""

class SegmentTree:
    def __init__(self, size):
        """
        构造函数 - 初始化线段树
        
        @param size: 数组大小
        """
        self.n = size
        self.sum = [0] * (4 * size)          # 当前区间和
        self.max_sum = [0] * (4 * size)      # 当前区间最大子段和
        self.history_max_sum = [0] * (4 * size)  # 历史最大子段和
        self.lazy = [0] * (4 * size)         # 懒惰标记
    
    def update_range(self, jobl, jobr, jobv, l, r, i):
        """
        区间加法更新
        
        @param jobl: 任务区间左端点
        @param jobr: 任务区间右端点
        @param jobv: 任务值
        @param l: 当前区间左端点
        @param r: 当前区间右端点
        @param i: 当前节点索引
        """
        if jobl <= l and r <= jobr:
            # 当前区间完全包含在任务区间内
            self.lazy[i] += jobv
            self.sum[i] += jobv
            self.history_max_sum[i] = max(self.history_max_sum[i], self.max_sum[i] + self.lazy[i])
            self.max_sum[i] += jobv
        else:
            # 需要继续向下递归
            self.push_down(i, l, r)
            mid = (l + r) >> 1
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)
    
    def query_range(self, jobl, jobr, l, r, i):
        """
        区间最大子段和查询
        
        @param jobl: 查询区间左端点
        @param jobr: 查询区间右端点
        @param l: 当前区间左端点
        @param r: 当前区间右端点
        @param i: 当前节点索引
        @return: 区间最大子段和
        """
        if jobl <= l and r <= jobr:
            # 当前区间完全包含在查询区间内
            return self.history_max_sum[i]
        else:
            # 需要继续向下递归
            self.push_down(i, l, r)
            mid = (l + r) >> 1
            result = -float('inf')
            if jobl <= mid:
                result = max(result, self.query_range(jobl, jobr, l, mid, i << 1))
            if jobr > mid:
                result = max(result, self.query_range(jobl, jobr, mid + 1, r, i << 1 | 1))
            return int(result)
    
    def push_down(self, i, l, r):
        """
        下推操作
        
        @param i: 当前节点索引
        @param l: 当前区间左端点
        @param r: 当前区间右端点
        """
        if self.lazy[i] != 0:
            # 下推懒惰标记
            self.lazy[i << 1] += self.lazy[i]
            self.lazy[i << 1 | 1] += self.lazy[i]
            
            # 更新子节点的sum和max_sum
            self.sum[i << 1] += self.lazy[i]
            self.sum[i << 1 | 1] += self.lazy[i]
            self.max_sum[i << 1] += self.lazy[i]
            self.max_sum[i << 1 | 1] += self.lazy[i]
            
            # 更新历史最大值
            self.history_max_sum[i << 1] = max(self.history_max_sum[i << 1], 
                                              self.max_sum[i << 1] + self.lazy[i << 1] - self.lazy[i])
            self.history_max_sum[i << 1 | 1] = max(self.history_max_sum[i << 1 | 1], 
                                                  self.max_sum[i << 1 | 1] + self.lazy[i << 1 | 1] - self.lazy[i])
            
            self.lazy[i] = 0
    
    def push_up(self, i):
        """
        上推操作
        
        @param i: 当前节点索引
        """
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]
        self.max_sum[i] = max(self.max_sum[i << 1], self.max_sum[i << 1 | 1])
        self.history_max_sum[i] = max(self.history_max_sum[i << 1], self.history_max_sum[i << 1 | 1])


class Query:
    def __init__(self, l, r, id):
        """
        查询类构造函数
        
        @param l: 查询左端点
        @param r: 查询右端点
        @param id: 查询ID
        """
        self.l = l
        self.r = r
        self.id = id


def main():
    """
    主函数
    """
    import sys
    input = sys.stdin.read
    data = input().split()
    
    # 读取数组长度
    idx = 0
    n = int(data[idx])
    idx += 1
    
    # 读取数组元素
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    # 读取查询数量
    q = int(data[idx])
    idx += 1
    
    # 读取所有查询
    queries = []
    for i in range(q):
        l = int(data[idx])
        r = int(data[idx + 1])
        idx += 2
        queries.append(Query(l, r, i))
    
    # 按右端点排序查询
    queries.sort(key=lambda x: x.r)
    
    # 初始化线段树
    seg_tree = SegmentTree(n)
    results = [0] * q
    last = [0] * 200001  # 记录每个值最后出现的位置，偏移100000处理负数
    
    # 扫描线处理
    query_index = 0
    for i in range(1, n + 1):
        val = arr[i] + 100000  # 偏移处理负数
        # 更新区间[last[val]+1, i]
        seg_tree.update_range(last[val] + 1, i, arr[i], 1, n, 1)
        last[val] = i
        
        # 处理所有右端点为i的查询
        while query_index < q and queries[query_index].r == i:
            results[queries[query_index].id] = seg_tree.query_range(
                queries[query_index].l, queries[query_index].r, 1, n, 1)
            query_index += 1
    
    # 输出结果
    for i in range(q):
        print(int(results[i]))


if __name__ == "__main__":
    main()