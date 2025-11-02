# 第k小的异或和问题
# 题目来源：LOJ #114. 第k小异或和
# 题目链接：https://loj.ac/p/114
# 题目描述：给定一个长度为n的数组arr，arr中都是long类型的非负数，可能有重复值
# 在这些数中选取任意个，至少要选一个数字
# 可以得到很多异或和，假设异或和的结果去重
# 返回第k小的异或和
# 算法：线性基（高斯消元法）
# 时间复杂度：构建线性基O(n * log(max_value))，单次查询O(log(max_value))
# 空间复杂度：O(log(max_value))
# 测试链接 : https://loj.ac/p/114
# 提交以下的code，可以通过所有测试用例

import time
import random

class LinearBasisKthXor:
    """
    线性基类，用于处理异或问题，特别是求第k小的异或和
    
    属性:
        MAXN: 最大数组长度
        BIT: 最大位数，因为arr[i] <= 2^50
        arr: 存储输入数组
        len_basis: 线性基的大小
        zero: 是否能异或出0
        n: 数组长度
    """
    
    def __init__(self):
        """初始化线性基对象"""
        self.MAXN = 100001  # 最大数组长度
        self.BIT = 50       # 最大位数，因为arr[i] <= 2^50
        self.arr = [0] * (self.MAXN + 1)  # 索引从1开始
        self.len_basis = 0  # 线性基的大小
        self.zero = False   # 是否能异或出0
        self.n = 0          # 数组长度
    
    def swap(self, a, b):
        """
        交换数组中的两个元素
        
        参数:
            a: 第一个元素的索引
            b: 第二个元素的索引
        """
        self.arr[a], self.arr[b] = self.arr[b], self.arr[a]
    
    def compute(self):
        """
        高斯消元法构建线性基
        
        算法思路：
        1. 从最高位到最低位进行高斯消元
        2. 对于每一位，寻找当前位为1的元素
        3. 将找到的元素交换到当前处理位置
        4. 用该元素将其他元素的当前位消为0
        5. 线性基大小增加
        
        时间复杂度: O(n * BIT)，其中n是数组长度，BIT是最大位数
        空间复杂度: O(BIT)，用于存储线性基
        """
        self.len_basis = 1  # 线性基从索引1开始
        # 从最高位到最低位进行高斯消元
        for i in range(self.BIT, -1, -1):
            # 寻找当前位为1的元素
            for j in range(self.len_basis, self.n + 1):
                # 检查第i位是否为1
                if (self.arr[j] & (1 << i)) != 0:
                    # 将找到的元素交换到当前处理位置
                    self.swap(j, self.len_basis)
                    break
            # 如果找到了当前位为1的元素
            if (self.arr[self.len_basis] & (1 << i)) != 0:
                # 用该元素将其他元素的当前位消为0
                for j in range(1, self.n + 1):
                    if j != self.len_basis and (self.arr[j] & (1 << i)) != 0:
                        self.arr[j] ^= self.arr[self.len_basis]
                # 线性基大小增加
                self.len_basis += 1
        
        self.len_basis -= 1  # 修正线性基的实际大小
        # 判断是否能异或出0：当线性基大小小于数组大小时，存在线性相关的情况
        self.zero = self.len_basis != self.n
    
    def query(self, k):
        """
        返回第k小的异或和
        
        算法思路：
        1. 特殊情况处理：如果能异或出0，0是第1小的结果
        2. 如果能异或出0，实际查询的是第k-1小的非0值
        3. 检查k是否超出可能的异或和个数
        4. 根据k的二进制表示选择线性基中的元素进行异或
        
        参数:
            k: 要查询的第k小的异或和的位置
            
        返回值:
            第k小的异或和，如果不存在则返回-1
            
        异常:
            当k < 1时抛出ValueError异常
        """
        # 异常处理：k超出合理范围
        if k < 1:
            raise ValueError("k must be at least 1")
        
        # 特殊情况处理：如果能异或出0，0是第1小的结果
        if self.zero and k == 1:
            return 0
        
        # 如果能异或出0，实际查询的是第k-1小的非0值
        if self.zero:
            k -= 1
        
        # 检查k是否超出可能的异或和个数
        max_possible = 1 << self.len_basis
        if k >= max_possible:
            return -1  # 无法找到第k小的异或和
        
        # 根据k的二进制表示选择线性基中的元素进行异或
        ans = 0
        for i in range(self.len_basis, 0, -1):
            j = self.len_basis - i
            if (k & (1 << j)) != 0:
                ans ^= self.arr[i]
        
        return ans
    
    def get_gaussian_basis(self, input_arr):
        """
        辅助方法：创建高斯消元后的线性基（用于测试和调试）
        
        参数:
            input_arr: 输入数组
            
        返回:
            高斯消元后的线性基数组
        """
        # 复制输入数组以避免修改原数组
        copy = input_arr.copy()
        basis = [0] * (self.BIT + 1)
        basis_len = 0
        
        for i in range(self.BIT, -1, -1):
            # 寻找当前位为1的数
            pivot = 0
            pivot_index = -1
            for j in range(len(copy)):
                if (copy[j] & (1 << i)) != 0:
                    pivot = copy[j]
                    pivot_index = j
                    break
            
            if pivot_index == -1:
                continue
            
            # 交换到当前位置
            copy[basis_len], copy[pivot_index] = copy[pivot_index], copy[basis_len]
            pivot = copy[basis_len]
            basis[i] = pivot
            basis_len += 1
            
            # 消去其他数的当前位
            for j in range(len(copy)):
                if j != basis_len - 1 and (copy[j] & (1 << i)) != 0:
                    copy[j] ^= pivot
        
        return basis
    
    def load_data(self, arr):
        """
        加载数据到线性基对象中
        
        参数:
            arr: 输入数组
        """
        self.n = len(arr)
        for i in range(1, self.n + 1):
            self.arr[i] = arr[i - 1]
    
    def run_unit_tests(self):
        """
        运行单元测试
        """
        print("Running unit tests...")
        
        # 测试用例1: 基础测试
        # 测试数据: [3, 1, 5]
        test1 = [3, 1, 5]
        self.load_data(test1)
        self.compute()
        
        print(f"Test 1: Expected 0, Got {self.query(1)}")
        print(f"Test 1: Expected 1, Got {self.query(2)}")
        print(f"Test 1: Expected 3, Got {self.query(3)}")
        print(f"Test 1: Expected 2, Got {self.query(4)}")  # 3^1=2
        print(f"Test 1: Expected 4, Got {self.query(5)}")  # 5
        print(f"Test 1: Expected 5, Got {self.query(6)}")  # 5^1=4
        print(f"Test 1: Expected 6, Got {self.query(7)}")  # 5^3=6
        print(f"Test 1: Expected 7, Got {self.query(8)}")  # 5^3^1=7
        
        # 测试用例2: 无法异或出0的情况
        # 测试数据: [1, 2]
        test2 = [1, 2]
        self.load_data(test2)
        self.compute()
        
        print(f"Test 2: Expected 1, Got {self.query(1)}")
        print(f"Test 2: Expected 2, Got {self.query(2)}")
        print(f"Test 2: Expected 3, Got {self.query(3)}")
        print(f"Test 2: Expected -1, Got {self.query(4)}")  # 超出范围
        
        # 测试用例3: 包含重复元素
        # 测试数据: [1, 1, 2]
        test3 = [1, 1, 2]
        self.load_data(test3)
        self.compute()
        
        print(f"Test 3: Expected 0, Got {self.query(1)}")
        print(f"Test 3: Expected 1, Got {self.query(2)}")
        print(f"Test 3: Expected 2, Got {self.query(3)}")
        print(f"Test 3: Expected 3, Got {self.query(4)}")
        print(f"Test 3: Expected -1, Got {self.query(5)}")
        
        # 测试用例4: 异常处理
        try:
            self.query(0)
            print("Test 4: Failed - Expected ValueError")
        except ValueError as e:
            print(f"Test 4: Passed - Caught expected error: {e}")
        
        print("Unit tests completed.")
    
    def benchmark(self):
        """
        运行性能测试
        """
        print("Running benchmark...")
        
        # 生成大规模测试数据
        test_size = 100000
        test_data = [random.randint(0, 2**30) for _ in range(test_size)]
        self.load_data(test_data)
        
        start_time = time.time()
        self.compute()
        end_time = time.time()
        
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"Benchmark: Processed {test_size} elements in {duration:.2f}ms")
        print(f"Linear basis size: {self.len_basis}")
        
        # 测试查询性能
        start_time = time.time()
        for i in range(1, 1001):
            self.query(i)
        end_time = time.time()
        
        query_duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"Query performance: 1000 queries in {query_duration:.2f}ms")
        
        print("Benchmark completed.")

# 主函数，用于处理输入输出
def main():
    """
    主函数，处理输入输出
    """
    import sys
    
    # 创建线性基对象
    lb = LinearBasisKthXor()
    
    # 读取输入
    n = int(sys.stdin.readline())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 加载数据并构建线性基
    lb.load_data(arr)
    lb.compute()
    
    # 处理查询
    q = int(sys.stdin.readline())
    for _ in range(q):
        k = int(sys.stdin.readline())
        try:
            result = lb.query(k)
            print(result)
        except ValueError as e:
            # 异常处理：输出错误信息并继续
            print(-1)

# 如果直接运行此脚本
if __name__ == "__main__":
    # 注意：如果要运行单元测试或性能测试，请取消下面的注释
    # lb = LinearBasisKthXor()
    # lb.run_unit_tests()
    # lb.benchmark()
    
    # 运行主函数
    main()

'''
线性基求第k小异或和详解

在线性基的应用中，求第k小异或和是一个经典问题。与求最大异或和不同，
求第k小异或和需要使用高斯消元法构建线性基，而非普通消元法。

为什么需要高斯消元法？

普通消元法构建的线性基虽然可以表示所有可能的异或和，但其元素顺序是不确定的，
无法直接用于求第k小值。而高斯消元法构建的线性基具有"阶梯状"结构，即：
basis[1]的最高位是所有元素中最高的
basis[2]的最高位是除去basis[1]外所有元素中最高的
...
这种结构使得我们可以通过二进制表示来快速计算第k小异或和。

算法思路：

1. 使用高斯消元法构建线性基
2. 判断是否能异或出0（即是否存在线性相关的元素）
3. 对于查询k：
   - 如果能异或出0，那么0是第1小的，实际第k小对应的是第(k-1)小的非0值
   - 将k的二进制表示用于选择线性基中的元素进行异或

时间复杂度分析：
- 构建线性基：O(n * log(max_value))
- 单次查询：O(log(max_value))

空间复杂度分析：
- O(log(max_value))，用于存储线性基

相关题目：
1. https://loj.ac/p/114 - 第k小异或和（模板题）
2. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
3. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
4. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
5. https://www.luogu.com.cn/problem/P4151 - 最大XOR和路径
6. https://www.luogu.com.cn/problem/P4301 - 最大异或和（可持久化线性基）
7. https://www.luogu.com.cn/problem/P3292 - 幸运数字（线性基+倍增）
'''