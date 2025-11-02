"""
线性基模板题 - 基础线性基算法应用 (Python版本)

题目来源：洛谷 P3812 【模板】线性基
题目链接：https://www.luogu.com.cn/problem/P3812

题目描述：
给定n个整数（数字可能重复），求在这些数中选取任意个，使得他们的异或和最大。

约束条件：
1 <= n <= 50
0 <= 数字 <= 2^50

算法思路：
1. 构建线性基：将每个数字插入到线性基中
2. 贪心策略：从最高位到最低位，如果当前位能使异或和增大，则选择该位
3. 线性基性质：线性基中的元素能够表示原集合中所有数的异或组合

时间复杂度：O(n * BIT)，其中BIT=50（数字的最大位数）
空间复杂度：O(BIT)

工程化考量：
1. 使用Python的int类型处理大数
2. 异常处理：空输入、重复元素等边界情况
3. 代码简洁性和可读性
4. 类型注解和文档字符串

与机器学习联系：
该问题类似于特征选择中的最大信息增益选择，
在特征工程中用于选择最具区分度的特征组合。
"""

import sys
from typing import List, Optional

class LinearBasis:
    """
    线性基类，封装线性基的基本操作
    
    属性：
        basis: 线性基数组
        BIT: 二进制位数
    """
    
    def __init__(self, bit: int = 50):
        """
        初始化线性基
        
        参数：
            bit: 二进制位数，默认为50
        """
        self.BIT = bit
        self.basis = [0] * (bit + 1)
    
    def insert(self, num: int) -> bool:
        """
        向线性基中插入数字
        
        参数：
            num: 要插入的数字
        
        返回：
            是否插入成功（是否线性无关）
        
        算法原理：
        1. 从最高位开始扫描
        2. 如果当前位为1，检查该位是否已有基向量
        3. 如果没有，直接插入作为基向量
        4. 如果有，进行异或消元操作
        5. 继续处理下一个位
        """
        for i in range(self.BIT, -1, -1):
            # 检查当前位是否为1
            if (num >> i) & 1:
                if self.basis[i] == 0:
                    # 该位还没有基向量，直接插入
                    self.basis[i] = num
                    return True
                # 该位已有基向量，进行消元操作
                num ^= self.basis[i]
        # 所有位都被消为0，说明该数字线性相关
        return False
    
    def query_max(self) -> int:
        """
        查询最大异或和
        
        返回：
            线性基能表示的最大异或和
        
        算法原理：
        1. 从最高位到最低位遍历线性基
        2. 贪心策略：如果当前位能使异或和增大，则选择该位
        3. 利用线性基的性质：每个基向量都是线性无关的
        """
        ans = 0
        for i in range(self.BIT, -1, -1):
            # 如果当前位能使异或和增大，则选择该位
            if (ans ^ self.basis[i]) > ans:
                ans ^= self.basis[i]
        return ans
    
    def query_min(self) -> int:
        """
        查询最小异或和
        
        返回：
            线性基能表示的最小非零异或和
        
        算法原理：
        1. 线性基的最小非零异或和就是最小的基向量
        2. 如果所有基向量都为0，则最小异或和为0
        """
        for i in range(self.BIT + 1):
            if self.basis[i] != 0:
                return self.basis[i]
        return 0
    
    def query_kth(self, k: int) -> int:
        """
        查询第k小异或和
        
        参数：
            k: 第k小
        
        返回：
            第k小的异或和，如果不存在返回-1
        
        算法原理：
        1. 对线性基进行标准化处理
        2. 将k转换为二进制表示
        3. 根据二进制位选择对应的基向量
        """
        # 统计线性基中非零基向量的数量
        cnt = 0
        for i in range(self.BIT + 1):
            if self.basis[i] != 0:
                cnt += 1
        
        # 如果k大于2^cnt，说明不存在第k小的异或和
        if k > (1 << cnt):
            return -1
        
        # 对线性基进行标准化处理
        d = []
        for i in range(self.BIT, -1, -1):
            if self.basis[i] != 0:
                for j in range(i - 1, -1, -1):
                    if (self.basis[i] >> j) & 1:
                        self.basis[i] ^= self.basis[j]
                d.append(self.basis[i])
        
        # 根据k的二进制位选择基向量
        ret = 0
        for i in range(cnt):
            if (k >> i) & 1:
                ret ^= d[i]
        return ret
    
    def can_represent(self, num: int) -> bool:
        """
        判断一个数是否能由线性基表示
        
        参数：
            num: 要判断的数字
        
        返回：
            是否能表示
        
        算法原理：
        1. 尝试将数字插入线性基
        2. 如果插入失败，说明该数字能被线性基表示
        3. 如果插入成功，说明该数字不能被线性基表示
        """
        for i in range(self.BIT, -1, -1):
            if (num >> i) & 1:
                if self.basis[i] == 0:
                    return False
                num ^= self.basis[i]
        return num == 0
    
    def get_basis_size(self) -> int:
        """
        获取线性基的大小（非零基向量数量）
        
        返回：
            线性基的大小
        """
        cnt = 0
        for i in range(self.BIT + 1):
            if self.basis[i] != 0:
                cnt += 1
        return cnt
    
    def clear(self):
        """清空线性基"""
        self.basis = [0] * (self.BIT + 1)

def main():
    """
    主函数：处理输入输出
    
    异常处理：
    1. 输入格式验证
    2. 边界条件检查
    3. 文件结束处理
    """
    # 创建线性基对象
    lb = LinearBasis()
    
    # 读取输入
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    
    # 读取每个数字并插入线性基
    for i in range(1, n + 1):
        num = int(data[i])
        lb.insert(num)
    
    # 查询并输出最大异或和
    max_xor = lb.query_max()
    print(max_xor)

def test_linear_basis():
    """
    单元测试函数：验证线性基基本功能
    
    测试用例：
    1. 空输入测试
    2. 单个数字测试
    3. 重复数字测试
    4. 最大异或和测试
    5. 边界值测试
    """
    print("=== 线性基单元测试 ===")
    
    # 测试用例1：空输入
    lb1 = LinearBasis()
    result1 = lb1.query_max()
    print(f"空输入最大异或和: {result1}")  # 应为0
    
    # 测试用例2：单个数字
    lb2 = LinearBasis()
    lb2.insert(5)
    result2 = lb2.query_max()
    print(f"单个数字最大异或和: {result2}")  # 应为5
    
    # 测试用例3：重复数字
    lb3 = LinearBasis()
    lb3.insert(3)
    lb3.insert(3)
    result3 = lb3.query_max()
    print(f"重复数字最大异或和: {result3}")  # 应为3
    
    # 测试用例4：多个不同数字
    lb4 = LinearBasis()
    lb4.insert(1)
    lb4.insert(2)
    lb4.insert(3)
    result4 = lb4.query_max()
    print(f"多个数字最大异或和: {result4}")  # 应为3 (1^2=3)
    
    # 测试用例5：边界值
    lb5 = LinearBasis()
    lb5.insert(0)
    lb5.insert((1 << 50) - 1)  # 2^50 - 1
    result5 = lb5.query_max()
    print(f"边界值最大异或和: {result5}")  # 应为2^50 - 1
    
    # 测试用例6：第k小查询
    lb6 = LinearBasis()
    lb6.insert(1)
    lb6.insert(2)
    lb6.insert(3)
    kth_result = lb6.query_kth(3)
    print(f"第3小异或和: {kth_result}")
    
    # 测试用例7：判断能否表示
    lb7 = LinearBasis()
    lb7.insert(1)
    lb7.insert(2)
    can_rep = lb7.can_represent(3)
    print(f"能否表示3: {can_rep}")  # 应为True
    
    print("=== 单元测试完成 ===")

class LinearBasisOptimized(LinearBasis):
    """
    优化版本的线性基类
    
    优化特性：
    1. 缓存最大异或和结果
    2. 支持批量插入
    3. 支持序列化
    """
    
    def __init__(self, bit: int = 50):
        super().__init__(bit)
        self._max_cache = None
        self._size_cache = None
    
    def insert(self, num: int) -> bool:
        """插入数字并清除缓存"""
        result = super().insert(num)
        self._max_cache = None
        self._size_cache = None
        return result
    
    def query_max(self) -> int:
        """查询最大异或和（带缓存）"""
        if self._max_cache is None:
            self._max_cache = super().query_max()
        return self._max_cache
    
    def get_basis_size(self) -> int:
        """获取线性基大小（带缓存）"""
        if self._size_cache is None:
            self._size_cache = super().get_basis_size()
        return self._size_cache
    
    def insert_batch(self, nums: List[int]) -> int:
        """
        批量插入数字
        
        参数：
            nums: 数字列表
        
        返回：
            成功插入的数量
        """
        count = 0
        for num in nums:
            if self.insert(num):
                count += 1
        return count
    
    def to_list(self) -> List[int]:
        """将线性基转换为列表"""
        return [x for x in self.basis if x != 0]
    
    def from_list(self, basis_list: List[int]) -> None:
        """从列表恢复线性基"""
        self.clear()
        for num in basis_list:
            self.insert(num)

if __name__ == "__main__":
    # 如果直接运行，执行主函数
    if len(sys.argv) > 1 and sys.argv[1] == "test":
        # 测试模式
        test_linear_basis()
    else:
        # 正常模式
        main()