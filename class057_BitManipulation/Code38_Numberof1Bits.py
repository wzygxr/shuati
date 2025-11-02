"""
位1的个数（多种解法）
测试链接：https://leetcode.cn/problems/number-of-1-bits/

题目描述：
编写一个函数，输入是一个无符号整数（以二进制串的形式），
返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。

解题思路：
1. 逐位检查法：检查每一位是否为1
2. Brian Kernighan算法：n & (n-1) 消除最低位的1
3. 查表法：预计算所有可能值的1的个数
4. 分治法：使用位运算技巧分组统计
5. 内置函数法：使用语言内置函数

时间复杂度分析：
- 逐位检查法：O(32) = O(1)
- Brian Kernighan算法：O(k)，k为1的个数
- 查表法：O(1)
- 分治法：O(1)
- 内置函数法：O(1)

空间复杂度分析：
- 逐位检查法：O(1)
- Brian Kernighan算法：O(1)
- 查表法：O(256) = O(1)
- 分治法：O(1)
- 内置函数法：O(1)
"""

class Solution:
    def hammingWeight1(self, n: int) -> int:
        """
        方法1：逐位检查法
        时间复杂度：O(32) = O(1)
        空间复杂度：O(1)
        """
        count = 0
        
        # 检查32位中的每一位
        for i in range(32):
            # 检查第i位是否为1
            if (n & (1 << i)) != 0:
                count += 1
        
        return count
    
    def hammingWeight2(self, n: int) -> int:
        """
        方法2：Brian Kernighan算法（推荐）
        核心思想：n & (n-1) 可以消除n的二进制表示中最右边的1
        时间复杂度：O(k)，k为1的个数
        空间复杂度：O(1)
        """
        count = 0
        
        # Python中整数是动态大小的，需要限制为32位
        n = n & 0xFFFFFFFF
        
        while n != 0:
            n = n & (n - 1)  # 消除最低位的1
            count += 1
        
        return count
    
    def hammingWeight3(self, n: int) -> int:
        """
        方法3：查表法
        预计算0-255所有数字的1的个数
        时间复杂度：O(1)
        空间复杂度：O(256) = O(1)
        """
        # 预计算表：0-255每个数字的1的个数
        table = [0] * 256
        for i in range(256):
            table[i] = table[i >> 1] + (i & 1)
        
        # 将32位整数分成4个8位部分
        n = n & 0xFFFFFFFF
        return table[n & 0xFF] + \
               table[(n >> 8) & 0xFF] + \
               table[(n >> 16) & 0xFF] + \
               table[(n >> 24) & 0xFF]
    
    def hammingWeight4(self, n: int) -> int:
        """
        方法4：分治法（位运算技巧）
        使用分治思想，先计算每2位的1的个数，然后合并
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        n = n & 0xFFFFFFFF  # 限制为32位
        
        # 第一步：每2位统计1的个数
        n = (n & 0x55555555) + ((n >> 1) & 0x55555555)
        
        # 第二步：每4位统计1的个数
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333)
        
        # 第三步：每8位统计1的个数
        n = (n & 0x0F0F0F0F) + ((n >> 4) & 0x0F0F0F0F)
        
        # 第四步：每16位统计1的个数
        n = (n & 0x00FF00FF) + ((n >> 8) & 0x00FF00FF)
        
        # 第五步：每32位统计1的个数
        n = (n & 0x0000FFFF) + ((n >> 16) & 0x0000FFFF)
        
        return n
    
    def hammingWeight5(self, n: int) -> int:
        """
        方法5：内置函数法
        使用Python内置的bin()函数
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        return bin(n & 0xFFFFFFFF).count('1')
    
    def hammingWeight6(self, n: int) -> int:
        """
        方法6：移位统计法
        时间复杂度：O(32) = O(1)
        空间复杂度：O(1)
        """
        count = 0
        n = n & 0xFFFFFFFF  # 限制为32位
        
        while n != 0:
            count += n & 1
            n = n >> 1
        
        return count
    
    def hammingWeight7(self, n: int) -> int:
        """
        方法7：并行计算法（另一种分治）
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        n = n & 0xFFFFFFFF  # 限制为32位
        
        # 并行计算1的个数
        n = n - ((n >> 1) & 0x55555555)
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333)
        n = (n + (n >> 4)) & 0x0F0F0F0F
        n = n + (n >> 8)
        n = n + (n >> 16)
        return n & 0x3F  # 最多32个1，所以取低6位

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：正常情况
    n1 = 11  # 二进制: 1011
    result1 = solution.hammingWeight2(n1)
    print(f"测试用例1 - 输入: {n1} (二进制: {bin(n1)})")
    print(f"结果: {result1} (预期: 3)")
    
    # 测试用例2：2的幂
    n2 = 16  # 二进制: 10000
    result2 = solution.hammingWeight2(n2)
    print(f"测试用例2 - 输入: {n2} (二进制: {bin(n2)})")
    print(f"结果: {result2} (预期: 1)")
    
    # 测试用例3：全1
    n3 = 0xFFFFFFFF  # 二进制: 11111111111111111111111111111111
    result3 = solution.hammingWeight2(n3)
    print(f"测试用例3 - 输入: {n3} (二进制: {bin(n3)})")
    print(f"结果: {result3} (预期: 32)")
    
    # 测试用例4：0
    n4 = 0  # 二进制: 0
    result4 = solution.hammingWeight2(n4)
    print(f"测试用例4 - 输入: {n4} (二进制: {bin(n4)})")
    print(f"结果: {result4} (预期: 0)")
    
    # 性能测试
    import time
    large_num = 0x7FFFFFFF  # 最大正数
    
    start_time = time.time()
    result5 = solution.hammingWeight2(large_num)
    end_time = time.time()
    print(f"性能测试 - 输入: {large_num}")
    print(f"结果: {result5}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 所有方法结果对比
    print("\n=== 所有方法结果对比 ===")
    test_num = 123456789
    print(f"测试数字: {test_num} (二进制: {bin(test_num)})")
    print(f"方法1 (逐位检查): {solution.hammingWeight1(test_num)}")
    print(f"方法2 (Brian Kernighan): {solution.hammingWeight2(test_num)}")
    print(f"方法3 (查表法): {solution.hammingWeight3(test_num)}")
    print(f"方法4 (分治法): {solution.hammingWeight4(test_num)}")
    print(f"方法5 (内置函数): {solution.hammingWeight5(test_num)}")
    print(f"方法6 (移位统计): {solution.hammingWeight6(test_num)}")
    print(f"方法7 (并行计算): {solution.hammingWeight7(test_num)}")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("方法1 - 逐位检查法:")
    print("  时间复杂度: O(32) = O(1)")
    print("  空间复杂度: O(1)")
    
    print("方法2 - Brian Kernighan算法:")
    print("  时间复杂度: O(k)，k为1的个数")
    print("  空间复杂度: O(1)")
    
    print("方法3 - 查表法:")
    print("  时间复杂度: O(1)")
    print("  空间复杂度: O(256) = O(1)")
    
    print("方法4 - 分治法:")
    print("  时间复杂度: O(1)")
    print("  空间复杂度: O(1)")
    
    print("方法5 - 内置函数法:")
    print("  时间复杂度: O(1)")
    print("  空间复杂度: O(1)")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 算法选择：方法2 (Brian Kernighan) 最优")
    print("2. 性能优化：避免不必要的循环")
    print("3. 可读性：方法2逻辑清晰")
    print("4. 实际应用：Python内置函数最简洁")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. Brian Kernighan算法：n & (n-1) 消除最低位1")
    print("2. 分治法：使用位运算并行计算")
    print("3. 查表法：空间换时间")
    print("4. 内置函数：利用语言特性")
    
    # Python特殊处理说明
    print("\n=== Python特殊处理说明 ===")
    print("Python整数是动态大小的，需要手动限制为32位：")
    print("  使用 n & 0xFFFFFFFF 确保32位操作")
    print("  避免负数的影响")
    print("  确保位运算的正确性")

if __name__ == "__main__":
    test_solution()