"""
包含每个查询的最小区间 (LeetCode 1851)
题目链接: https://leetcode.cn/problems/minimum-interval-to-include-each-query/

题目描述:
给你一个二维整数数组intervals，其中intervals[i] = [l, r]
表示第i个区间开始于l，结束于r，区间的长度是r-l+1
给你一个整数数组queries，queries[i]表示要查询的位置
答案是所有包含queries[i]的区间中，最小长度的区间是多长
返回数组对应查询的所有答案，如果不存在这样的区间那么答案是-1

解题思路:
使用扫描线算法结合自定义最小堆实现最小区间查询。
1. 将区间按起始位置排序
2. 将查询按位置排序
3. 使用自定义最小堆维护当前可能包含查询点的区间（按长度排序）
4. 对于每个查询点，将起始位置小于等于该点的区间加入堆中
5. 移除堆中结束位置小于该点的区间
6. 堆顶元素即为包含该点的最小区间

时间复杂度: O(n log n + m log m) - 排序和堆操作
空间复杂度: O(n + m) - 存储区间和查询信息

工程化考量:
1. 异常处理: 检查输入数组合法性
2. 边界条件: 处理空数组和无效区间
3. 性能优化: 使用自定义堆减少系统开销
4. 可读性: 详细注释和模块化设计
"""

def min_interval(intervals, queries):
    """
    计算每个查询点的最小区间长度
    使用自定义实现的最小堆
    
    Args:
        intervals: 区间数组，每个元素为[left, right]
        queries: 查询点数组
    
    Returns:
        每个查询点对应的最小区间长度数组
    """
    # 边界条件检查
    if not intervals or not queries:
        raise ValueError("输入数组不能为空")
    
    n = len(intervals)
    m = len(queries)
    
    # 按区间起始位置排序
    # 时间复杂度: O(n log n)
    intervals.sort(key=lambda x: x[0])
    
    # 将查询点与其原始索引配对并排序
    # 时间复杂度: O(m log m)
    ques = [(queries[i], i) for i in range(m)]
    ques.sort(key=lambda x: x[0])
    
    # 初始化自定义堆
    heap = MinHeap()
    
    # 存储结果
    ans = [0] * m
    
    # 扫描线算法处理每个查询点
    # i: 查询点索引, j: 区间索引
    j = 0
    for i in range(m):
        # 将起始位置小于等于当前查询点的所有区间加入堆中
        while j < n and intervals[j][0] <= ques[i][0]:
            length = intervals[j][1] - intervals[j][0] + 1
            heap.push(length, intervals[j][1])
            j += 1
        
        # 移除堆中结束位置小于当前查询点的区间
        while not heap.is_empty() and heap.peek_end() < ques[i][0]:
            heap.poll()
        
        # 堆顶元素即为包含当前查询点的最小区间
        if not heap.is_empty():
            ans[ques[i][1]] = heap.peek_length()
        else:
            ans[ques[i][1]] = -1
    
    return ans

class MinHeap:
    """自定义最小堆实现"""
    
    def __init__(self):
        """初始化堆"""
        self.heap = []
        self.heap_size = 0
    
    def is_empty(self):
        """检查堆是否为空"""
        return self.heap_size == 0
    
    def peek_length(self):
        """获取堆顶元素的区间长度"""
        if self.is_empty():
            raise IndexError("堆为空")
        return self.heap[0][0]
    
    def peek_end(self):
        """获取堆顶元素的区间结束位置"""
        if self.is_empty():
            raise IndexError("堆为空")
        return self.heap[0][1]
    
    def push(self, length, end):
        """向堆中添加元素"""
        self.heap.append((length, end))
        self.heap_size += 1
        self._heap_insert(self.heap_size - 1)
    
    def poll(self):
        """移除堆顶元素"""
        if self.is_empty():
            raise IndexError("堆为空")
        
        self._swap(0, self.heap_size - 1)
        self.heap_size -= 1
        self._heapify(0)
        return self.heap.pop()
    
    def _heap_insert(self, i):
        """堆插入操作（上浮）"""
        # 当前元素的长度小于父节点长度时，需要上浮
        while i > 0 and self.heap[i][0] < self.heap[(i - 1) // 2][0]:
            self._swap(i, (i - 1) // 2)
            i = (i - 1) // 2
    
    def _heapify(self, i):
        """堆化操作（下沉）"""
        left = i * 2 + 1  # 左子节点索引
        
        # 当存在子节点时继续下沉
        while left < self.heap_size:
            # 找到左右子节点中长度较小的节点索引
            if left + 1 < self.heap_size and self.heap[left + 1][0] < self.heap[left][0]:
                best = left + 1
            else:
                best = left
            
            # 比较当前节点与较小子节点，确定是否需要交换
            if self.heap[best][0] >= self.heap[i][0]:
                best = i
            
            # 如果当前节点已经是最小的，则停止下沉
            if best == i:
                break
            
            # 交换节点并继续下沉
            self._swap(best, i)
            i = best
            left = i * 2 + 1
    
    def _swap(self, i, j):
        """交换堆中两个元素"""
        self.heap[i], self.heap[j] = self.heap[j], self.heap[i]

# 测试函数
def test_min_interval():
    # 测试用例1
    intervals1 = [[1,4],[2,4],[3,6],[4,4]]
    queries1 = [2,3,4,5]
    result1 = min_interval(intervals1, queries1)
    print(f"测试用例1: {result1}")  # 预期: [3,3,1,4]
    
    # 测试用例2
    intervals2 = [[2,3],[2,5],[1,8],[20,25]]
    queries2 = [2,19,5,22]
    result2 = min_interval(intervals2, queries2)
    print(f"测试用例2: {result2}")  # 预期: [2,-1,4,6]
    
    # 测试用例3: 空数组
    try:
        intervals3 = []
        queries3 = [1, 2, 3]
        result3 = min_interval(intervals3, queries3)
        print(f"测试用例3: {result3}")
    except ValueError as e:
        print(f"测试用例3 异常: {e}")

if __name__ == "__main__":
    test_min_interval()