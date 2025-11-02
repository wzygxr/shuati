#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 239. 滑动窗口最大值 - Sparse Table应用
题目链接：https://leetcode.com/problems/sliding-window-maximum/

【题目描述】
给定一个整数数组nums和一个整数k，有一个大小为k的滑动窗口从数组的最左侧移动到数组的最右侧。
你只可以看到在滑动窗口内的k个数字。滑动窗口每次只向右移动一位。
返回滑动窗口中的最大值。

【算法核心思想】
使用Sparse Table预处理区间最大值，然后对每个滑动窗口进行O(1)查询。
这种方法特别适合k值较大且需要高效查询的场景。

【核心原理】
Sparse Table基于倍增思想，通过预处理所有长度为2的幂次的区间最大值，
实现O(n log n)预处理，O(1)查询的高效区间最值查询。

【位运算常用技巧】
1. 位移运算：1 << k 等价于 2^k，比pow(2,k)更高效
2. 整数除法：i >> 1 等价于 i // 2，用于快速计算log2值
3. 位掩码：使用位运算快速判断和计算区间覆盖

【时间复杂度分析】
- 预处理：O(n log n) - 构建Sparse Table
- 查询：O(n) - 每个窗口一次O(1)查询，共n-k+1个窗口
- 总时间复杂度：O(n log n)

【空间复杂度分析】
- Sparse Table：O(n log n)
- 结果数组：O(n)
- 总空间复杂度：O(n log n)

【算法优势】
1. 查询时间复杂度为O(1)，非常高效
2. 实现相对简单，代码可读性好
3. 适用于静态数据（不需要修改）
4. 支持多种可重复贡献操作（最大值、最小值、GCD等）

【算法劣势】
1. 不支持在线修改操作
2. 预处理时间较长O(n log n)
3. 空间复杂度较高O(n log n)
4. 仅适用于可重复贡献的问题

【应用场景】
1. 大数据分析中的滑动窗口统计
2. 实时数据流分析
3. 股票价格监控
4. 网络流量峰值检测
5. 传感器数据质量监控

【相关题目】
1. LeetCode 239 - 滑动窗口最大值（本题）
2. Codeforces 514D - R2D2 and Droid Army（区间最大值查询的扩展应用）
3. POJ 3264 - Balanced Lineup（区间最大值与最小值之差）
4. SPOJ RMQSQ - Range Minimum Query（标准区间最小值查询）
5. SPOJ FREQUENT - 区间频繁值查询
6. CodeChef MSTICK - 区间最值查询
7. UVA 11235 - Frequent values（区间频繁值查询）
8. HackerRank Maximum Element in a Subarray（使用ST表高效查询）
9. AtCoder ABC189 C - Mandarin Orange（结合ST表和单调栈的题目）
10. Codeforces 1311E - Concatenation with Beautiful Strings（可使用ST表预处理最值）
"""

import math
import time
from typing import List

class SparseTable:
    """
    Sparse Table实现类，支持区间最大值查询
    
    【设计原理】
    Sparse Table是一种基于倍增思想的数据结构，通过预处理所有长度为2的幂次的区间答案，
    实现O(1)时间复杂度的区间查询。
    
    【核心数据结构】
    1. st[i][j]: 表示从位置i开始，长度为2^j的区间的最大值
    2. log_table[i]: 表示不超过i的最大2的幂次的指数
    
    【时间复杂度】
    - 构建: O(n log n)
    - 查询: O(1)
    
    【空间复杂度】
    - O(n log n)
    """
    
    def __init__(self, arr: List[int]):
        """
        初始化Sparse Table
        
        【实现原理】
        1. 预处理log_table数组，用于快速计算区间长度对应的最大2的幂次
        2. 初始化ST表的第0层（长度为1的区间）
        3. 动态规划构建更高层的ST表，每层依赖于前一层的结果
        
        Args:
            arr: 输入数组
            
        【时间复杂度】
        O(n log n)
        
        【空间复杂度】
        O(n log n)
        """
        self.n = len(arr)
        if self.n == 0:
            return
            
        # 计算最大层数
        self.max_log = math.floor(math.log2(self.n)) + 1
        self.st = [[0] * self.max_log for _ in range(self.n)]
        self.log_table = [0] * (self.n + 1)
        
        # 预处理log2值
        self._preprocess_log()
        
        # 初始化第一层
        # 长度为1的区间，最大值就是元素本身
        for i in range(self.n):
            self.st[i][0] = arr[i]
        
        # 动态规划构建ST表
        # j表示区间长度为2^j
        for j in range(1, self.max_log):
            step = 1 << j  # 2^j
            # 遍历所有可能的起始位置，确保区间不越界
            for i in range(self.n - step + 1):
                # 状态转移：当前区间的最大值由两个子区间的最大值合并而来
                # 子区间1: [i, i+2^(j-1)-1]
                # 子区间2: [i+2^(j-1), i+2^j-1]
                self.st[i][j] = max(self.st[i][j-1], self.st[i + (1 << (j-1))][j-1])
    
    def _preprocess_log(self):
        """
        预处理log2值
        
        【实现原理】
        使用动态规划方法预处理log_table数组，log_table[i]表示不超过i的最大2的幂次的指数
        
        【时间复杂度】
        O(n)
        """
        self.log_table[1] = 0
        for i in range(2, self.n + 1):
            self.log_table[i] = self.log_table[i // 2] + 1
    
    def query(self, l: int, r: int) -> int:
        """
        查询区间[l, r]的最大值
        
        【实现原理】
        1. 计算查询区间的长度len = r - l + 1
        2. 找到最大的k，使得 2^k ≤ len
        3. 构造两个覆盖整个查询区间的预处理区间：
           - 第一个区间：[l, l + 2^k - 1]
           - 第二个区间：[r - 2^k + 1, r]
        4. 这两个区间的最大值即为整个查询区间的最大值
        
        Args:
            l: 区间左端点（包含，0-based）
            r: 区间右端点（包含，0-based）
            
        Returns:
            区间最大值
            
        【时间复杂度】
        O(1)
        
        【异常处理】
        检查区间边界有效性
        """
        if l > r:
            raise ValueError("Invalid range: left boundary greater than right boundary")
            
        length = r - l + 1
        k = self.log_table[length]
        
        # 区间查询：取两个重叠子区间的最大值
        return max(self.st[l][k], self.st[r - (1 << k) + 1][k])

class Solution:
    """
    滑动窗口最大值解决方案类
    
    【设计思想】
    将滑动窗口最大值问题转化为区间最大值查询问题，利用Sparse Table的O(1)查询特性，
    实现高效的滑动窗口最大值计算。
    """
    
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        """
        使用Sparse Table解决滑动窗口最大值问题
        
        【实现原理】
        1. 使用Sparse Table预处理数组，构建区间最大值查询结构
        2. 对每个长度为k的滑动窗口，使用O(1)时间查询最大值
        3. 将所有查询结果收集到结果列表中
        
        Args:
            nums: 输入数组
            k: 滑动窗口大小
            
        Returns:
            每个滑动窗口的最大值列表
            
        【时间复杂度】
        O(n log n) - 预处理O(n log n) + 查询O(n)
        
        【空间复杂度】
        O(n log n) - Sparse Table的空间占用
        
        【边界情况处理】
        1. 空数组或k<=0：返回空列表
        2. k大于数组长度：将k调整为数组长度
        """
        # 边界情况处理
        if not nums or k <= 0:
            return []
            
        n = len(nums)
        if k > n:
            k = n  # 处理k大于n的情况
            
        # 构建Sparse Table
        st = SparseTable(nums)
        
        # 查询每个滑动窗口的最大值
        result = []
        # 滑动窗口的起始位置范围：[0, n-k]
        for i in range(n - k + 1):
            result.append(st.query(i, i + k - 1))
            
        return result

def test_sliding_window_maximum():
    """
    单元测试函数
    
    【测试覆盖】
    1. 常规测试：正常输入数据
    2. 边界测试：k=1和k等于数组长度的情况
    3. 极端测试：大数组性能测试
    4. 性能测试：大规模数据处理时间
    
    【测试设计原则】
    1. 覆盖各种边界情况
    2. 包含典型和极端输入
    3. 验证算法正确性和性能
    4. 提供清晰的测试结果输出
    """
    solution = Solution()
    
    # 测试用例1：常规测试
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = solution.maxSlidingWindow(nums1, k1)
    print(f"测试用例1结果: {result1}")
    print("期望结果: [3, 3, 5, 5, 6, 7]")
    
    # 测试用例2：边界测试 - k=1
    nums2 = [1, 2, 3, 4, 5]
    k2 = 1
    result2 = solution.maxSlidingWindow(nums2, k2)
    print(f"测试用例2结果: {result2}")
    
    # 测试用例3：边界测试 - k等于数组长度
    nums3 = [5, 4, 3, 2, 1]
    k3 = 5
    result3 = solution.maxSlidingWindow(nums3, k3)
    print(f"测试用例3结果: {result3}")
    
    # 测试用例4：极端测试 - 大数组
    nums4 = [1] * 1000
    k4 = 100
    result4 = solution.maxSlidingWindow(nums4, k4)
    print(f"测试用例4结果长度: {len(result4)}")
    
    # 性能测试
    large_nums = [1] * 100000
    large_k = 1000
    
    start_time = time.time()
    large_result = solution.maxSlidingWindow(large_nums, large_k)
    end_time = time.time()
    
    print(f"性能测试耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    print("所有测试用例执行完成！")

"""
【算法优化技巧】
1. 预处理log数组避免重复计算，提高查询效率
2. 使用位移运算提高效率（1 << p 代替 pow(2, p)），避免浮点数运算
3. 采用列表推导式优化内存使用
4. 对于大数据量，可以考虑使用生成器减少内存占用
5. 在预处理ST表时，可以按位运算预计算所有可能的区间长度
6. 使用局部变量存储中间结果，减少数组访问次数

【常见错误点】
1. 数组索引越界：构建ST表时未正确检查边界条件
2. 整数溢出：对于较大的数，位移运算可能导致溢出
3. log数组初始化错误：特别是log_table[1]的处理
4. 位运算优先级问题：位移运算符优先级低于算术运算符，需要注意括号使用
5. 内存分配不足：对于非常大的数组，ST表可能需要过多内存
6. 查询区间长度计算错误：导致选择了错误的k值

【工程化考量】
1. 异常处理：添加输入参数校验，处理无效查询
2. 内存优化：对于特别大的数组，可以考虑动态调整ST表大小
3. 并发处理：对于多线程环境，考虑添加同步机制
4. 测试覆盖：编写全面的测试用例，覆盖各种边界情况
5. 代码复用：将ST表封装为通用类，支持不同的数据类型和操作
6. 性能监控：添加性能指标收集，监控查询效率
7. 文档完善：提供详细的API文档和使用示例
8. 并行预处理：对于非常大的数据集，可以考虑并行构建ST表

【实际应用注意事项】
1. 数据规模评估：对于特别大的数组，需要评估内存占用是否在允许范围内
2. 查询频率分析：ST表适用于查询密集型应用，预处理一次性完成
3. 数据特性利用：如果数据有特定规律，可以进一步优化计算
4. 混合策略：在某些情况下，结合不同数据结构可能更优
5. 语言特性：利用Python的列表推导式可以实现更简洁的代码
6. 维护成本：确保代码的可读性和可维护性，便于后续优化
7. 硬件环境：考虑目标运行环境的内存限制和缓存大小
8. 数据动态性：ST表不支持动态更新，如果数据需要频繁修改，考虑使用线段树
9. 精度问题：处理大整数时注意溢出问题
10. 性能测试：在实际数据集上进行性能测试，验证算法效率
"""

if __name__ == "__main__":
    """
    程序入口点
    
    【工程化考量】
    1. 模块化设计：将算法封装为类，便于复用和测试
    2. 类型提示：使用typing模块提供类型提示，提高代码可读性
    3. 异常处理：在实际应用中应添加try-except块处理运行时异常
    4. 性能优化：对于大数据量，可以考虑使用更高效的输入输出方式
    5. 可扩展性：设计时考虑未来可能的功能扩展
    """
    test_sliding_window_maximum()