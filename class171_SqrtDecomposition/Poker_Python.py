"""
由乃打扑克 - Python实现

题目来源：洛谷 P5356
题目描述：
给定一个长度为n的数组arr，接下来有m条操作，操作类型如下
操作 1 l r v : 查询arr[l..r]范围上，第v小的数
操作 2 l r v : arr[l..r]范围上每个数加v，v可能是负数

数据范围：
1 <= n、m <= 10^5
-2 * 10^4 <= 数组中的值 <= +2 * 10^4

解题思路：
使用分块算法解决此问题。将数组分成大小约为√(n/2)的块，对每个块维护以下信息：
1. 原数组arr：存储实际值
2. 排序数组sortv：存储块内元素排序后的结果
3. 懒惰标记lazy：记录块内所有元素需要增加的值

对于操作2（区间加法）：
- 对于完整块，直接更新懒惰标记
- 对于不完整块，暴力更新元素值并重新排序块内元素

对于操作1（查询第k小）：
- 使用二分答案的方法，通过统计小于等于某值的元素个数来确定第k小的值
- 统计时利用分块结构优化计算

时间复杂度分析：
- 区间加法操作：O(√n)
  - 完整块：O(1)更新标记
  - 不完整块：O(√n)暴力更新并排序
- 查询第k小：O(√n * log(max_val - min_val))
  - 二分答案：O(log(max_val - min_val))
  - 每次统计：O(√n)

空间复杂度：O(n)

工程化考量：
1. 异常处理：
   - 检查查询参数k的有效性（1 <= k <= 区间长度）
   - 处理空区间等边界情况
2. 性能优化：
   - 使用懒惰标记避免重复计算
   - 合理设置块大小为√(n/2)以平衡完整块和不完整块的处理时间
3. 鲁棒性：
   - 处理负数加法操作
   - 保证在各种数据分布下的稳定性能

测试链接：https://www.luogu.com.cn/problem/P5356
"""

import math
import sys

class PokerSolution:
    def __init__(self, size):
        """
        初始化分块结构
        
        :param size: 数组大小
        """
        self.n = size
        # 设置块大小为sqrt(n/2)，这是一个经验性的优化
        self.block_size = int(math.sqrt(size / 2))
        # 计算块数量
        self.block_num = (size + self.block_size - 1) // self.block_size
        
        # 初始化数组
        self.arr = [0] * (size + 1)
        self.sortv = [0] * (size + 1)
        
        # 分块相关变量
        self.block_index = [0] * (size + 1)  # 每个元素所属的块
        self.block_left = [0] * (self.block_num + 1)   # 每个块的左边界
        self.block_right = [0] * (self.block_num + 1)  # 每个块的右边界
        self.lazy = [0] * (self.block_num + 1)        # 每个块的懒惰标记
        
        # 初始化每个元素所属的块
        for i in range(1, size + 1):
            self.block_index[i] = (i - 1) // self.block_size + 1
            
        # 初始化每个块的边界
        for i in range(1, self.block_num + 1):
            self.block_left[i] = (i - 1) * self.block_size + 1
            self.block_right[i] = min(i * self.block_size, size)
    
    def inner_add(self, l, r, v):
        """
        对指定区间进行加法操作并维护排序数组
        
        :param l: 区间左端点
        :param r: 区间右端点
        :param v: 要增加的值
        """
        # 对区间内每个元素加上v
        for i in range(l, r + 1):
            self.arr[i] += v
            
        # 更新该块的排序数组
        block_id = self.block_index[l]
        for i in range(self.block_left[block_id], self.block_right[block_id] + 1):
            self.sortv[i] = self.arr[i]
            
        # 对块内元素重新排序
        temp = []
        for i in range(self.block_left[block_id], self.block_right[block_id] + 1):
            temp.append(self.sortv[i])
        temp.sort()
        for i in range(len(temp)):
            self.sortv[self.block_left[block_id] + i] = temp[i]
    
    def add(self, l, r, v):
        """
        区间加法操作
        
        :param l: 区间左端点
        :param r: 区间右端点
        :param v: 要增加的值
        """
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
    
    def get_min(self, l, r):
        """
        获取区间最小值
        
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 区间最小值
        """
        left_block = self.block_index[l]
        right_block = self.block_index[r]
        ans = float('inf')
        
        # 如果区间在同一个块内
        if left_block == right_block:
            for i in range(l, r + 1):
                ans = min(ans, self.arr[i] + self.lazy[left_block])
        else:
            # 处理左边不完整块
            for i in range(l, self.block_right[left_block] + 1):
                ans = min(ans, self.arr[i] + self.lazy[left_block])
            # 处理右边不完整块
            for i in range(self.block_left[right_block], r + 1):
                ans = min(ans, self.arr[i] + self.lazy[right_block])
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                ans = min(ans, self.sortv[self.block_left[i]] + self.lazy[i])
                
        return ans
    
    def get_max(self, l, r):
        """
        获取区间最大值
        
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 区间最大值
        """
        left_block = self.block_index[l]
        right_block = self.block_index[r]
        ans = float('-inf')
        
        # 如果区间在同一个块内
        if left_block == right_block:
            for i in range(l, r + 1):
                ans = max(ans, self.arr[i] + self.lazy[left_block])
        else:
            # 处理左边不完整块
            for i in range(l, self.block_right[left_block] + 1):
                ans = max(ans, self.arr[i] + self.lazy[left_block])
            # 处理右边不完整块
            for i in range(self.block_left[right_block], r + 1):
                ans = max(ans, self.arr[i] + self.lazy[right_block])
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                ans = max(ans, self.sortv[self.block_right[i]] + self.lazy[i])
                
        return ans
    
    def block_count(self, block_id, v):
        """
        返回第block_id块内<= v的数字个数
        
        :param block_id: 块编号
        :param v: 比较值
        :return: 第block_id块内<= v的数字个数
        """
        v -= self.lazy[block_id]
        left = self.block_left[block_id]
        right = self.block_right[block_id]
        
        if self.sortv[left] > v:
            return 0
        if self.sortv[right] <= v:
            return right - left + 1
            
        pos = left
        while left <= right:
            mid = (left + right) // 2
            if self.sortv[mid] <= v:
                pos = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return pos - self.block_left[block_id] + 1
    
    def get_count(self, l, r, v):
        """
        返回arr[l..r]范围上<= v的数字个数
        
        :param l: 区间左端点
        :param r: 区间右端点
        :param v: 比较值
        :return: arr[l..r]范围上<= v的数字个数
        """
        left_block = self.block_index[l]
        right_block = self.block_index[r]
        ans = 0
        
        # 如果区间在同一个块内
        if left_block == right_block:
            for i in range(l, r + 1):
                if self.arr[i] + self.lazy[left_block] <= v:
                    ans += 1
        else:
            # 处理左边不完整块
            for i in range(l, self.block_right[left_block] + 1):
                if self.arr[i] + self.lazy[left_block] <= v:
                    ans += 1
            # 处理右边不完整块
            for i in range(self.block_left[right_block], r + 1):
                if self.arr[i] + self.lazy[right_block] <= v:
                    ans += 1
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                ans += self.block_count(i, v)
                
        return ans
    
    def query(self, l, r, k):
        """
        查询区间第k小的数
        
        :param l: 区间左端点
        :param r: 区间右端点
        :param k: 第k小
        :return: 第k小的数，如果k无效则返回-1
        """
        # 检查k的有效性
        if k < 1 or k > r - l + 1:
            return -1
            
        # 获取区间最小值和最大值作为二分的边界
        min_val = self.get_min(l, r)
        max_val = self.get_max(l, r)
        answer = -1
        
        # 二分答案
        while min_val <= max_val:
            mid_val = min_val + (max_val - min_val) // 2
            # 如果小于等于mid_val的元素个数>=k，说明第k小的数<=mid_val
            if self.get_count(l, r, mid_val) >= k:
                answer = mid_val
                max_val = mid_val - 1
            else:
                min_val = mid_val + 1
                
        return answer

def main():
    """
    主函数，用于测试
    """
    # 读取输入
    line = input().split()
    n, m = int(line[0]), int(line[1])
    
    # 初始化解决方案
    solution = PokerSolution(n)
    
    # 读取数组元素
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        solution.arr[i] = elements[i - 1]
        
    # 初始化排序数组
    for i in range(1, n + 1):
        solution.sortv[i] = solution.arr[i]
    # 对每个块内的元素进行排序
    for i in range(1, solution.block_num + 1):
        temp = []
        for j in range(solution.block_left[i], solution.block_right[i] + 1):
            temp.append(solution.sortv[j])
        temp.sort()
        for j in range(len(temp)):
            solution.sortv[solution.block_left[i] + j] = temp[j]
    
    # 处理操作
    for _ in range(m):
        operation = list(map(int, input().split()))
        op, l, r, v = operation[0], operation[1], operation[2], operation[3]
        
        if op == 1:
            print(solution.query(l, r, v))
        else:
            solution.add(l, r, v)

if __name__ == "__main__":
    main()