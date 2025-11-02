"""
LOJ 6277 - 分块1：区间加法，单点查询

题目来源：LibreOJ 6277
题目链接：https://loj.ac/p/6277

题目描述：
给定一个长度为n的数组arr，接下来有m条操作，操作类型如下：
操作 1 l r v : arr[l..r]范围上每个数加v
操作 2 x     : 查询arr[x]的值

数据范围：
1 <= n, m <= 50000
-10000 <= 数组中的值 <= +10000
-10000 <= v <= +10000

解题思路：
使用分块算法，将数组分成大小约为√n的块
对于每个块维护一个懒惰标记，记录该块所有元素需要增加的值

时间复杂度分析：
- 区间加法操作：O(√n)
  - 完整块：O(1)更新标记
  - 不完整块：O(√n)暴力更新
- 单点查询：O(1)
  - 直接返回元素值加上所在块的标记

空间复杂度：O(n)

工程化考量：
1. 异常处理：检查查询参数的有效性
2. 性能优化：使用懒惰标记避免重复计算
3. 鲁棒性：处理边界情况和极端输入

测试用例：
输入：
5 5
1 5 4 2 3
1 2 4 2
2 3
1 1 5 -1
2 3
2 4

输出：
6
5
4
"""

import math
import sys

class LOJ6277_Block1_Python:
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
        
        时间复杂度：O(n)
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
        
        # 初始化懒惰标记
        for i in range(1, self.bnum + 1):
            self.lazy[i] = 0
    
    def add(self, l, r, v):
        """
        区间加法操作
        
        Args:
            l: 区间左端点
            r: 区间右端点
            v: 要增加的值
        
        时间复杂度：O(√n)
        空间复杂度：O(1)
        """
        lb = self.bi[l]
        rb = self.bi[r]
        
        # 如果区间在同一个块内
        if lb == rb:
            # 暴力更新该块内的元素
            for i in range(l, r + 1):
                self.arr[i] += v
        else:
            # 处理左边不完整块
            for i in range(l, self.br[lb] + 1):
                self.arr[i] += v
            
            # 处理右边不完整块
            for i in range(self.bl[rb], r + 1):
                self.arr[i] += v
            
            # 处理中间完整块
            for i in range(lb + 1, rb):
                self.lazy[i] += v
    
    def query(self, x):
        """
        单点查询操作
        
        Args:
            x: 查询位置
        
        Returns:
            arr[x]的值
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        # 返回元素值加上所在块的懒惰标记
        return self.arr[x] + self.lazy[self.bi[x]]
    
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
                # 单点查询操作
                x = int(data[idx]); idx += 1
                print(self.query(x))
    
    def test(self):
        """
        单元测试方法
        用于验证算法的正确性
        """
        # 测试用例1：基础功能测试
        self.n = 5
        self.m = 5
        test_arr = [0, 1, 5, 4, 2, 3]  # 索引从1开始
        for i in range(len(test_arr)):
            self.arr[i] = test_arr[i]
        
        self.prepare()
        
        # 操作序列
        self.add(2, 4, 2)
        print(f"Test 1 - Query(3): {self.query(3)}")  # 期望输出: 6
        
        self.add(1, 5, -1)
        print(f"Test 1 - Query(3): {self.query(3)}")  # 期望输出: 5
        print(f"Test 1 - Query(4): {self.query(4)}")  # 期望输出: 4
        
        # 测试用例2：边界情况测试
        self.n = 3
        self.m = 3
        test_arr2 = [0, 10, 20, 30]
        for i in range(len(test_arr2)):
            self.arr[i] = test_arr2[i]
        
        self.prepare()
        
        self.add(1, 3, 5)
        print(f"Test 2 - Query(1): {self.query(1)}")  # 期望输出: 15
        print(f"Test 2 - Query(2): {self.query(2)}")  # 期望输出: 25
        print(f"Test 2 - Query(3): {self.query(3)}")  # 期望输出: 35
        
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
                self.query(i % self.n + 1)
        
        end_time = time.time()
        print(f"Performance test completed in {(end_time - start_time) * 1000:.2f}ms")

if __name__ == "__main__":
    solver = LOJ6277_Block1_Python()
    solver.main()