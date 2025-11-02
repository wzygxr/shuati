"""
序列 - Python实现

题目来源：洛谷 P3863
题目描述：
给定一个长度为n的数组arr，初始时刻认为是第0秒
接下来发生m条操作，第i条操作发生在第i秒，操作类型如下
操作 1 l r v : arr[l..r]范围上每个数加v，v可能是负数
操作 2 x v   : 不包括当前这一秒，查询过去多少秒内，arr[x] >= v

数据范围：
2 <= n、m <= 10^5
-10^9 <= 数组中的值 <= +10^9

解题思路：
这是一个时间轴上的分块问题。我们需要处理两种操作：
1. 区间加法操作：对时间轴上的区间进行加法操作
2. 查询操作：查询在某个时间点之前，满足条件的时间点数量

关键思路是将所有事件离线处理，按位置排序后使用分块算法：
1. 将所有修改和查询事件存储下来
2. 按位置排序，相同位置时修改事件优先于查询事件
3. 使用分块维护时间轴上的信息
4. 对于每个位置，维护时间轴上该位置的值变化情况

时间复杂度分析：
- 预处理（排序）：O((m+n) * log(m+n))
- 每次区间加法操作：O(√m)
- 每次查询操作：O(√m)
- 总体时间复杂度：O((m+n) * log(m+n) + (m+n) * √m)

空间复杂度：O(m+n)

工程化考量：
1. 异常处理：
   - 处理空区间情况
   - 处理边界条件
2. 性能优化：
   - 使用分块算法优化区间操作
   - 离线处理减少重复计算
3. 鲁棒性：
   - 处理大数值运算（使用long类型）
   - 保证在各种数据分布下的稳定性能

测试链接：https://www.luogu.com.cn/problem/P3863
"""

import math
import sys

class Event:
    def __init__(self, op, x, t, v, q):
        self.op = op
        self.x = x
        self.t = t
        self.v = v
        self.q = q

class SequenceSolution:
    def __init__(self, n, m):
        """
        初始化序列解决方案
        
        :param n: 数组长度
        :param m: 操作数量
        """
        self.n = n
        self.m = m + 1  # 时间轴重新定义，1是初始时刻、2、3 ... m+1
        self.arr = [0] * (n + 1)
        
        # 事件数组，存储所有操作事件
        self.events = []
        self.event_count = 0  # 事件计数器
        self.query_count = 0  # 查询计数器
        
        # tim[i] = v，表示在i号时间点，所有数字都增加v
        self.tim = [0] * (self.m + 1)
        # 时间块内的所有值要排序，方便查询 >= v的数字个数
        self.sortv = [0] * (self.m + 1)
        
        # 时间分块相关变量
        self.block_size = int(math.sqrt(self.m))  # 块大小
        self.block_num = (self.m + self.block_size - 1) // self.block_size  # 块数量
        
        self.block_index = [0] * (self.m + 1)  # 每个时间点所属的块
        self.block_left = [0] * (self.block_num + 1)   # 每个块的左边界
        self.block_right = [0] * (self.block_num + 1)  # 每个块的右边界
        self.lazy = [0] * (self.block_num + 1)        # 每个块的懒惰标记
        
        # 初始化每个时间点所属的块
        for i in range(1, self.m + 1):
            self.block_index[i] = (i - 1) // self.block_size + 1
            
        # 初始化每个块的边界
        for i in range(1, self.block_num + 1):
            self.block_left[i] = (i - 1) * self.block_size + 1
            self.block_right[i] = min(i * self.block_size, self.m)
        
        # 每个查询的答案
        self.ans = [0] * (m + 1)
    
    def inner_add(self, l, r, v):
        """
        对指定时间区间进行加法操作并维护排序数组
        
        :param l: 时间区间左端点
        :param r: 时间区间右端点
        :param v: 要增加的值
        """
        # 对时间区间内每个时间点加上v
        for i in range(l, r + 1):
            self.tim[i] += v
            
        # 更新该块的排序数组
        block_id = self.block_index[l]
        for i in range(self.block_left[block_id], self.block_right[block_id] + 1):
            self.sortv[i] = self.tim[i]
            
        # 对块内时间点重新排序
        temp = []
        for i in range(self.block_left[block_id], self.block_right[block_id] + 1):
            temp.append(self.sortv[i])
        temp.sort()
        for i in range(len(temp)):
            self.sortv[self.block_left[block_id] + i] = temp[i]
    
    def add(self, l, r, v):
        """
        时间区间加法操作
        
        :param l: 时间区间左端点
        :param r: 时间区间右端点
        :param v: 要增加的值
        """
        # 处理空区间
        if l > r:
            return
            
        left_block = self.block_index[l]
        right_block = self.block_index[r]
        
        # 如果区间在同一个块内
        if left_block == right_block:
            self.inner_add(l, r, v)
        else:
            # 处理左边不完整块
            self.inner_add(l, self.block_right[left_block], v)
            # 处理右边不完整块
            self.inner_add(self.block_left[right_block], r, v)
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                self.lazy[i] += v
    
    def inner_query(self, l, r, v):
        """
        在指定时间区间内查询大于等于v的数字个数（暴力方法）
        
        :param l: 时间区间左端点
        :param r: 时间区间右端点
        :param v: 比较值
        :return: 大于等于v的数字个数
        """
        v -= self.lazy[self.block_index[l]]  # 考虑块的懒惰标记
        count = 0
        for i in range(l, r + 1):
            if self.tim[i] >= v:
                count += 1
        return count
    
    def block_count(self, block_id, v):
        """
        第block_id块内>= v的数字个数（使用二分查找）
        
        :param block_id: 块编号
        :param v: 比较值
        :return: 第block_id块内>= v的数字个数
        """
        v -= self.lazy[block_id]  # 考虑块的懒惰标记
        left = self.block_left[block_id]
        right = self.block_right[block_id]
        pos = self.block_right[block_id] + 1
        
        # 二分查找第一个大于等于v的位置
        while left <= right:
            mid = (left + right) >> 1
            if self.sortv[mid] >= v:
                pos = mid
                right = mid - 1
            else:
                left = mid + 1
        return self.block_right[block_id] - pos + 1
    
    def query(self, l, r, v):
        """
        查询时间区间内大于等于v的数字个数
        
        :param l: 时间区间左端点
        :param r: 时间区间右端点
        :param v: 比较值
        :return: 大于等于v的数字个数
        """
        # 处理空区间
        if l > r:
            return 0
            
        left_block = self.block_index[l]
        right_block = self.block_index[r]
        count = 0
        
        # 如果区间在同一个块内
        if left_block == right_block:
            count += self.inner_query(l, r, v)
        else:
            # 处理左边不完整块
            count += self.inner_query(l, self.block_right[left_block], v)
            # 处理右边不完整块
            count += self.inner_query(self.block_left[right_block], r, v)
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                count += self.block_count(i, v)
        return count
    
    def add_change(self, x, t, v):
        """
        添加修改事件
        
        :param x: 位置
        :param t: 时间
        :param v: 修改值
        """
        self.event_count += 1
        self.events.append(Event(1, x, t, v, 0))
    
    def add_query(self, x, t, v):
        """
        添加查询事件
        
        :param x: 位置
        :param t: 时间
        :param v: 查询标准
        """
        self.event_count += 1
        self.query_count += 1
        self.events.append(Event(2, x, t, v, self.query_count))
    
    def prepare(self):
        """
        初始化分块结构和事件排序
        """
        # 按位置排序，位置相同的按时间排序
        self.events.sort(key=lambda e: (e.x, e.t))

def main():
    """
    主函数，用于测试
    """
    # 读取输入
    line = input().split()
    n, m = int(line[0]), int(line[1])
    
    # 初始化解决方案
    solution = SequenceSolution(n, m)
    
    # 读取数组元素
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        solution.arr[i] = elements[i - 1]
    
    # 读取所有操作
    for t in range(2, solution.m + 1):
        operation = list(map(int, input().split()))
        op = operation[0]
        if op == 1:
            l, r, v = operation[1], operation[2], operation[3]
            # 使用差分数组技巧处理区间加法
            solution.add_change(l, t, v)
            solution.add_change(r + 1, t, -v)
        else:
            x, v = operation[1], operation[2]
            solution.add_query(x, t, v)
    
    solution.prepare()
    
    # 处理所有事件
    for event in solution.events:
        if event.op == 1:
            # 处理修改事件
            solution.add(event.t, solution.m, event.v)
        else:
            # 处理查询事件
            solution.ans[event.q] = solution.query(1, event.t - 1, event.v - solution.arr[event.x])
    
    # 输出所有查询结果
    for i in range(1, solution.query_count + 1):
        print(solution.ans[i])

if __name__ == "__main__":
    main()