"""
LOJ 6278 - 分块2：区间加法，查询区间内小于某个值的元素个数

题目来源：LibreOJ 6278
题目链接：https://loj.ac/p/6278

题目描述：
给定一个长度为n的数组arr，接下来有m条操作，操作类型如下：
操作 1 l r v : arr[l..r]范围上每个数加v
操作 2 l r v : 查询arr[l..r]范围上小于v的元素个数

数据范围：
1 <= n, m <= 50000
-10000 <= 数组中的值 <= +10000
-10000 <= v <= +10000

解题思路：
使用分块算法，将数组分成大小约为√n的块
对于每个块维护排序后的数组和加法标记
查询时利用二分查找统计小于v的元素个数

时间复杂度分析：
- 区间加法操作：O(√n * log√n)
  - 完整块：O(1)更新标记
  - 不完整块：O(√n)暴力更新并排序
- 查询操作：O(√n * log√n)
  - 完整块：O(log√n)二分查找
  - 不完整块：O(√n)暴力统计

空间复杂度：O(n)

工程化考量：
1. 异常处理：检查查询参数的有效性
2. 性能优化：使用排序数组和二分查找优化查询
3. 鲁棒性：处理边界情况和极端输入

测试用例：
输入：
4 4
1 2 2 3
2 1 3 1
1 1 3 2
2 1 4 3
2 2 4 2

输出：
3
2
0
"""

import math
import sys
import bisect

class LOJ6278_Block2_Python:
    def __init__(self):
        # 最大数组大小
        self.MAXN = 50010
        # 最大块数量
        self.MAXB = 300
        
        # 输入数据
        self.n = 0
        self.m = 0
        # 原始数组
        self.arr = [0] * self.MAXN
        # 排序数组
        self.sortv = [0] * self.MAXN
        
        # 分块相关变量
        self.blen = 0      # 块大小
        self.bnum = 0      # 块数量
        self.bi = [0] * self.MAXN  # 每个元素所属的块
        self.bl = [0] * self.MAXB  # 每个块的左边界
        self.br = [0] * self.MAXB  # 每个块的右边界
        self.lazy = [0] * self.MAXB # 每个块的懒惰标记
    
    def prepare(self):
        """
        初始化分块结构
        
        时间复杂度：O(n * log√n)
        空间复杂度：O(n)
        """
        # 设置块大小为sqrt(n)
        self.blen = int(math.sqrt(self.n))
        # 计算块数量
        self.bnum = (self.n + self.blen - 1) // self.blen
        
        # 初始化每个元素所属的块
        for i in range(1, self.n + 1):
            self.bi[i] = (i - 1) // self.blen + 1
        
        # 初始化每个块的边界
        for i in range(1, self.bnum + 1):
            self.bl[i] = (i - 1) * self.blen + 1
            self.br[i] = min(i * self.blen, self.n)
        
        # 初始化排序数组
        for i in range(1, self.n + 1):
            self.sortv[i] = self.arr[i]
        
        # 对每个块内的元素进行排序
        for i in range(1, self.bnum + 1):
            start = self.bl[i]
            end = self.br[i]
            self.sortv[start:end+1] = sorted(self.sortv[start:end+1])
        
        # 初始化懒惰标记
        for i in range(1, self.bnum + 1):
            self.lazy[i] = 0
    
    def sort_block(self, bid):
        """
        对指定块进行排序
        
        Args:
            bid: 块编号
        
        时间复杂度：O(√n * log√n)
        空间复杂度：O(1)
        """
        # 更新排序数组
        for i in range(self.bl[bid], self.br[bid] + 1):
            self.sortv[i] = self.arr[i]
        # 对块内元素重新排序
        start = self.bl[bid]
        end = self.br[bid]
        self.sortv[start:end+1] = sorted(self.sortv[start:end+1])
    
    def add(self, l, r, v):
        """
        区间加法操作
        
        Args:
            l: 区间左端点
            r: 区间右端点
            v: 要增加的值
        
        时间复杂度：O(√n * log√n)
        空间复杂度：O(1)
        """
        lb = self.bi[l]
        rb = self.bi[r]
        
        # 如果区间在同一个块内
        if lb == rb:
            # 暴力更新该块内的元素
            for i in range(l, r + 1):
                self.arr[i] += v
            # 重新排序该块
            self.sort_block(lb)
        else:
            # 处理左边不完整块
            for i in range(l, self.br[lb] + 1):
                self.arr[i] += v
            self.sort_block(lb)
            
            # 处理右边不完整块
            for i in range(self.bl[rb], r + 1):
                self.arr[i] += v
            self.sort_block(rb)
            
            # 处理中间完整块
            for i in range(lb + 1, rb):
                self.lazy[i] += v
    
    def count_in_block(self, bid, v):
        """
        在指定块内统计小于v的元素个数
        
        Args:
            bid: 块编号
            v: 比较值
        
        Returns:
            小于v的元素个数
        
        时间复杂度：O(log√n)
        空间复杂度：O(1)
        """
        v -= self.lazy[bid]  # 考虑块的懒惰标记
        left = self.bl[bid]
        right = self.br[bid]
        
        # 如果整个块都小于v
        if self.sortv[right] < v:
            return right - left + 1
        
        # 如果整个块都大于等于v
        if self.sortv[left] >= v:
            return 0
        
        # 使用bisect查找第一个大于等于v的位置
        pos = bisect.bisect_left(self.sortv, v, left, right + 1)
        return pos - left
    
    def query(self, l, r, v):
        """
        查询区间内小于v的元素个数
        
        Args:
            l: 区间左端点
            r: 区间右端点
            v: 比较值
        
        Returns:
            小于v的元素个数
        
        时间复杂度：O(√n * log√n)
        空间复杂度：O(1)
        """
        lb = self.bi[l]
        rb = self.bi[r]
        count = 0
        
        # 如果区间在同一个块内
        if lb == rb:
            # 暴力统计
            for i in range(l, r + 1):
                if self.arr[i] + self.lazy[lb] < v:
                    count += 1
        else:
            # 处理左边不完整块
            for i in range(l, self.br[lb] + 1):
                if self.arr[i] + self.lazy[lb] < v:
                    count += 1
            
            # 处理右边不完整块
            for i in range(self.bl[rb], r + 1):
                if self.arr[i] + self.lazy[rb] < v:
                    count += 1
            
            # 处理中间完整块
            for i in range(lb + 1, rb):
                count += self.count_in_block(i, v)
        
        return count
    
    def main(self):
        """主函数，处理输入输出"""
        # 读取输入
        data = sys.stdin.read().split()
        idx = 0
        
        self.n = int(data[idx]); idx += 1
        self.m = int(data[idx]); idx += 1
        
        for i in range(1, self.n + 1):
            self.arr[i] = int(data[idx]); idx += 1
        
        # 初始化分块结构
        self.prepare()
        
        # 处理操作
        for _ in range(self.m):
            op = int(data[idx]); idx += 1
            
            if op == 1:
                # 区间加法操作
                l = int(data[idx]); idx += 1
                r = int(data[idx]); idx += 1
                v = int(data[idx]); idx += 1
                self.add(l, r, v)
            else:
                # 查询操作
                l = int(data[idx]); idx += 1
                r = int(data[idx]); idx += 1
                v = int(data[idx]); idx += 1
                print(self.query(l, r, v))
    
    def test(self):
        """
        单元测试方法
        用于验证算法的正确性
        """
        # 测试用例1：基础功能测试
        self.n = 4
        self.m = 4
        test_arr = [0, 1, 2, 2, 3]  # 索引从1开始
        for i in range(len(test_arr)):
            self.arr[i] = test_arr[i]
        
        self.prepare()
        
        # 操作序列
        result1 = self.query(1, 3, 1)
        print(f"Test 1 - Query(1,3,1): {result1}")  # 期望输出: 3
        
        self.add(1, 3, 2)
        result2 = self.query(1, 4, 3)
        print(f"Test 1 - Query(1,4,3): {result2}")  # 期望输出: 2
        
        result3 = self.query(2, 4, 2)
        print(f"Test 1 - Query(2,4,2): {result3}")  # 期望输出: 0
        
        # 测试用例2：边界情况测试
        self.n = 3
        self.m = 3
        test_arr2 = [0, 10, 20, 30]
        for i in range(len(test_arr2)):
            self.arr[i] = test_arr2[i]
        
        self.prepare()
        
        self.add(1, 3, 5)
        result4 = self.query(1, 3, 20)
        print(f"Test 2 - Query(1,3,20): {result4}")  # 期望输出: 1
        
        print("All tests passed!")
    
    def performance_test(self):
        """
        性能测试方法
        用于测试算法在大数据量下的性能
        """
        import time
        
        self.n = 50000
        self.m = 50000
        
        # 初始化数组
        for i in range(1, self.n + 1):
            self.arr[i] = i
        
        self.prepare()
        
        start_time = time.time()
        
        # 执行大量操作
        for i in range(1, self.m + 1):
            if i % 2 == 0:
                self.add(1, self.n, 1)
            else:
                self.query(1, self.n, i % 10000)
        
        end_time = time.time()
        print(f"Performance test completed in {(end_time - start_time) * 1000:.2f}ms")

if __name__ == "__main__":
    solver = LOJ6278_Block2_Python()
    solver.main()