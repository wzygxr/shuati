"""
汉明距离总和
测试链接：https://leetcode.cn/problems/total-hamming-distance/

题目描述：
两个整数的 汉明距离 指的是这两个数字的二进制数对应位不同的数量。
给你一个整数数组 nums，请你计算并返回 nums 中任意两个数之间汉明距离的总和。

解题思路：
1. 暴力法：双重循环计算所有组合（会超时）
2. 位运算法：逐位计算贡献值
3. 数学优化：利用组合数学优化计算

时间复杂度分析：
- 暴力法：O(n²)，会超时
- 位运算法：O(n * 32)，32位整数
- 数学优化：O(n * 32)

空间复杂度分析：
- 所有方法：O(1)，只使用常数空间
"""

class Solution:
    def totalHammingDistance1(self, nums: list[int]) -> int:
        """
        方法1：暴力法（不推荐，会超时）
        时间复杂度：O(n²)
        空间复杂度：O(1)
        """
        total = 0
        n = len(nums)
        
        for i in range(n):
            for j in range(i + 1, n):
                # 计算两个数的汉明距离
                total += bin(nums[i] ^ nums[j]).count('1')
        
        return total
    
    def totalHammingDistance2(self, nums: list[int]) -> int:
        """
        方法2：位运算法（推荐）
        核心思想：逐位计算每个位的贡献
        对于每个位，统计有多少个数的该位是1（设为count）
        那么该位的贡献就是 count * (n - count)
        时间复杂度：O(n * 32)
        空间复杂度：O(1)
        """
        total = 0
        n = len(nums)
        
        # 遍历32位（整数最多32位）
        for i in range(32):
            count_ones = 0
            
            # 统计当前位为1的数的个数
            for num in nums:
                count_ones += (num >> i) & 1
            
            # 当前位的贡献：count_ones * (n - count_ones)
            total += count_ones * (n - count_ones)
        
        return total
    
    def totalHammingDistance3(self, nums: list[int]) -> int:
        """
        方法3：数学优化版
        使用更高效的位运算技巧
        时间复杂度：O(n * 32)
        空间复杂度：O(1)
        """
        total = 0
        n = len(nums)
        
        for i in range(32):
            mask = 1 << i
            count = 0
            
            for num in nums:
                if (num & mask) != 0:
                    count += 1
            
            total += count * (n - count)
        
        return total
    
    def totalHammingDistance4(self, nums: list[int]) -> int:
        """
        方法4：使用位运算优化
        时间复杂度：O(n * 32)
        空间复杂度：O(1)
        """
        total = 0
        n = len(nums)
        
        for bit_pos in range(32):
            ones = 0
            
            for num in nums:
                # 检查特定位是否为1
                if ((num >> bit_pos) & 1) == 1:
                    ones += 1
            
            # 当前位的汉明距离贡献
            total += ones * (n - ones)
        
        return total
    
    def totalHammingDistance5(self, nums: list[int]) -> int:
        """
        方法5：分组统计法
        将数字按位分组统计
        时间复杂度：O(n * 32)
        空间复杂度：O(32)
        """
        if not nums:
            return 0
        
        total = 0
        n = len(nums)
        
        # 创建32个桶，每个桶统计对应位的1的个数
        bit_counts = [0] * 32
        
        for num in nums:
            for i in range(32):
                if (num & (1 << i)) != 0:
                    bit_counts[i] += 1
        
        # 计算总汉明距离
        for count in bit_counts:
            total += count * (n - count)
        
        return total

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：基础情况
    nums1 = [4, 14, 2]
    result1 = solution.totalHammingDistance2(nums1)
    print(f"测试用例1 - 输入: {nums1}")
    print(f"结果: {result1} (预期: 6)")
    
    # 测试用例2：两个相同数字
    nums2 = [4, 4]
    result2 = solution.totalHammingDistance2(nums2)
    print(f"测试用例2 - 输入: {nums2}")
    print(f"结果: {result2} (预期: 0)")
    
    # 测试用例3：三个不同数字
    nums3 = [1, 2, 3]
    result3 = solution.totalHammingDistance2(nums3)
    print(f"测试用例3 - 输入: {nums3}")
    print(f"结果: {result3} (预期: 4)")
    
    # 测试用例4：边界情况（单个元素）
    nums4 = [5]
    result4 = solution.totalHammingDistance2(nums4)
    print(f"测试用例4 - 输入: {nums4}")
    print(f"结果: {result4} (预期: 0)")
    
    # 性能测试
    import time
    large_nums = list(range(1000))  # 0到999的序列
    
    start_time = time.time()
    result5 = solution.totalHammingDistance2(large_nums)
    end_time = time.time()
    print(f"性能测试 - 输入长度: {len(large_nums)}")
    print(f"结果: {result5}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("方法1 - 暴力法:")
    print("  时间复杂度: O(n²) - 会超时")
    print("  空间复杂度: O(1)")
    
    print("方法2 - 位运算法:")
    print("  时间复杂度: O(n * 32)")
    print("  空间复杂度: O(1)")
    
    print("方法3 - 数学优化版:")
    print("  时间复杂度: O(n * 32)")
    print("  空间复杂度: O(1)")
    
    print("方法4 - 位运算优化版:")
    print("  时间复杂度: O(n * 32)")
    print("  空间复杂度: O(1)")
    
    print("方法5 - 分组统计法:")
    print("  时间复杂度: O(n * 32)")
    print("  空间复杂度: O(32)")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 算法选择：方法2是最优选择")
    print("2. 性能优化：避免O(n²)的暴力解法")
    print("3. 边界处理：处理空数组和单元素数组")
    print("4. 可读性：清晰的数学公式解释")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 组合数学：C(k,2) = k*(k-1)/2 的变形应用")
    print("2. 位运算：逐位统计1的个数")
    print("3. 贡献值计算：每个位的独立贡献可以分开计算")
    print("4. 数学优化：利用对称性减少计算量")
    
    # 数学原理说明
    print("\n=== 数学原理说明 ===")
    print("对于每个位位置：")
    print("  设该位为1的数字有k个，为0的数字有m个")
    print("  那么该位产生的汉明距离贡献为：k * m")
    print("  因为每个1和每个0的组合都会产生1的贡献")
    print("  总贡献 = Σ(每个位的k * m)")
    
    # 示例演示
    print("\n=== 示例演示 ===")
    demo_nums = [4, 14, 2]  # 二进制: 0100, 1110, 0010
    print(f"示例数组: {demo_nums}")
    print("二进制表示:")
    for i, num in enumerate(demo_nums):
        print(f"  {num}: {bin(num)[2:]:>4}")
    
    print("逐位分析:")
    for bit_pos in range(4):  # 只看前4位
        ones = 0
        for num in demo_nums:
            if ((num >> bit_pos) & 1) == 1:
                ones += 1
        zeros = len(demo_nums) - ones
        contribution = ones * zeros
        print(f"  第{bit_pos}位: 1的个数={ones}, 0的个数={zeros}, 贡献={contribution}")

if __name__ == "__main__":
    test_solution()