"""
位操作算法实现
包含LeetCode多个位操作相关题目的解决方案

题目列表:
1. LeetCode 191 - 位1的个数
2. LeetCode 231 - 2的幂
3. LeetCode 342 - 4的幂
4. LeetCode 136 - 只出现一次的数字
5. LeetCode 137 - 只出现一次的数字 II
6. LeetCode 260 - 只出现一次的数字 III
7. LeetCode 190 - 颠倒二进制位
8. LeetCode 338 - 比特位计数
9. LeetCode 201 - 数字范围按位与
10. LeetCode 371 - 两整数之和

时间复杂度分析:
- 位1的个数: O(1) - 固定32位操作
- 2的幂/4的幂: O(1) - 常数时间判断
- 只出现一次的数字: O(n) - 线性扫描
- 颠倒二进制位: O(1) - 固定操作
- 比特位计数: O(n) - 线性计算
- 数字范围按位与: O(1) - 常数时间
- 两整数之和: O(1) - 常数时间循环

空间复杂度分析:
- 大部分算法: O(1) - 常数空间
- 比特位计数: O(n) - 存储结果数组
- 只出现一次的数字III: O(1) - 常数空间

工程化考量:
1. 性能优化: 选择最优位操作算法
2. 边界处理: 处理0和负数的特殊情况
3. 异常安全: 使用异常处理机制
4. 可读性: 添加详细注释说明位操作原理
"""

class BitManipulation:
    """位操作算法类"""
    
    @staticmethod
    def hamming_weight(n: int) -> int:
        """
        LeetCode 191 - 位1的个数
        计算无符号整数二进制表示中1的个数
        
        方法1: Brian Kernighan算法
        时间复杂度: O(k) - k为1的个数
        空间复杂度: O(1)
        """
        count = 0
        while n:
            n &= n - 1  # 清除最低位的1
            count += 1
        return count
    
    @staticmethod
    def hamming_weight_parallel(n: int) -> int:
        """
        方法2: 并行计算
        使用位运算并行计算1的个数
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        n = (n & 0x55555555) + ((n >> 1) & 0x55555555)
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333)
        n = (n & 0x0F0F0F0F) + ((n >> 4) & 0x0F0F0F0F)
        n = (n & 0x00FF00FF) + ((n >> 8) & 0x00FF00FF)
        n = (n & 0x0000FFFF) + ((n >> 16) & 0x0000FFFF)
        return n
    
    @staticmethod
    def is_power_of_two(n: int) -> bool:
        """
        LeetCode 231 - 2的幂
        判断一个数是否是2的幂
        
        原理: 2的幂的二进制表示只有1个1
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        if n <= 0:
            return False
        return (n & (n - 1)) == 0
    
    @staticmethod
    def is_power_of_four(n: int) -> bool:
        """
        LeetCode 342 - 4的幂
        判断一个数是否是4的幂
        
        原理: 必须是2的幂，且1出现在奇数位
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        if n <= 0:
            return False
        # 检查是否是2的幂且1出现在奇数位
        return (n & (n - 1)) == 0 and (n & 0x55555555) != 0
    
    @staticmethod
    def single_number(nums: list) -> int:
        """
        LeetCode 136 - 只出现一次的数字
        数组中只有一个数字出现一次，其余出现两次
        
        原理: 使用异或操作，相同数字异或为0
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        result = 0
        for num in nums:
            result ^= num
        return result
    
    @staticmethod
    def single_number_ii(nums: list) -> int:
        """
        LeetCode 137 - 只出现一次的数字 II
        每个数字出现三次，只有一个出现一次
        
        原理: 使用位计数，统计每位出现1的次数
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        ones, twos = 0, 0
        for num in nums:
            # 更新ones和twos
            ones = (ones ^ num) & ~twos
            twos = (twos ^ num) & ~ones
        return ones
    
    @staticmethod
    def single_number_iii(nums: list) -> list:
        """
        LeetCode 260 - 只出现一次的数字 III
        有两个数字只出现一次，其余出现两次
        
        原理: 先异或得到两个数的异或结果，然后根据最低位1分组
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 得到两个只出现一次的数字的异或
        xor_all = 0
        for num in nums:
            xor_all ^= num
        
        # 找到最低位的1（两个数字在该位不同）
        lowest_bit = xor_all & -xor_all
        
        group1, group2 = 0, 0
        for num in nums:
            if num & lowest_bit:
                group1 ^= num
            else:
                group2 ^= num
        
        return [group1, group2]
    
    @staticmethod
    def reverse_bits(n: int) -> int:
        """
        LeetCode 190 - 颠倒二进制位
        颠倒给定的32位无符号整数的二进制位
        
        原理: 使用分治思想，先交换相邻位，再交换相邻2位，依此类推
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        # 确保是32位无符号整数
        n = n & 0xFFFFFFFF
        
        # 分治交换
        n = ((n >> 1) & 0x55555555) | ((n & 0x55555555) << 1)
        n = ((n >> 2) & 0x33333333) | ((n & 0x33333333) << 2)
        n = ((n >> 4) & 0x0F0F0F0F) | ((n & 0x0F0F0F0F) << 4)
        n = ((n >> 8) & 0x00FF00FF) | ((n & 0x00FF00FF) << 8)
        n = (n >> 16) | (n << 16)
        
        return n & 0xFFFFFFFF
    
    @staticmethod
    def count_bits(n: int) -> list:
        """
        LeetCode 338 - 比特位计数
        计算0到n每个数的二进制表示中1的个数
        
        原理: 动态规划，利用i & (i-1)清除最低位的1
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        if n < 0:
            return []
        
        result = [0] * (n + 1)
        for i in range(1, n + 1):
            result[i] = result[i & (i - 1)] + 1
        
        return result
    
    @staticmethod
    def range_bitwise_and(m: int, n: int) -> int:
        """
        LeetCode 201 - 数字范围按位与
        计算区间[m, n]内所有数字的按位与
        
        原理: 找到m和n的公共前缀
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        shift = 0
        while m < n:
            m >>= 1
            n >>= 1
            shift += 1
        return m << shift
    
    @staticmethod
    def get_sum(a: int, b: int) -> int:
        """
        LeetCode 371 - 两整数之和
        不使用+和-运算符计算两数之和
        
        原理: 使用位运算模拟加法
        时间复杂度: O(1) - 最多32次循环
        空间复杂度: O(1)
        """
        # 32位整数处理
        MASK = 0xFFFFFFFF
        MAX_INT = 0x7FFFFFFF
        
        a &= MASK
        b &= MASK
        
        while b != 0:
            carry = ((a & b) << 1) & MASK
            a = (a ^ b) & MASK
            b = carry
        
        # 处理负数
        return a if a <= MAX_INT else ~(a ^ MASK)


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_hamming_weight():
        """测试位1的个数算法性能"""
        print("=== 位1的个数性能测试 ===")
        
        test_cases = [0, 1, 15, 255, 1023, 0xFFFFFFFF, 0x55555555, 0xAAAAAAAA]
        
        for n in test_cases:
            print(f"测试数字: {n} (二进制: {bin(n)})")
            
            import time
            
            start = time.time()
            r1 = BitManipulation.hamming_weight(n)
            time1 = (time.time() - start) * 1e9
            
            start = time.time()
            r2 = BitManipulation.hamming_weight_parallel(n)
            time2 = (time.time() - start) * 1e9
            
            print(f"  Kernighan算法: {r1}, 时间: {time1:.0f} ns")
            print(f"  并行算法: {r2}, 时间: {time2:.0f} ns")
            print(f"  结果一致: {'是' if r1 == r2 else '否'}")
            print()
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 位操作单元测试 ===")
        
        # 测试位1的个数
        assert BitManipulation.hamming_weight(11) == 3
        assert BitManipulation.hamming_weight_parallel(11) == 3
        print("hamming_weight测试通过")
        
        # 测试2的幂
        assert BitManipulation.is_power_of_two(16) == True
        assert BitManipulation.is_power_of_two(15) == False
        print("is_power_of_two测试通过")
        
        # 测试4的幂
        assert BitManipulation.is_power_of_four(16) == True
        assert BitManipulation.is_power_of_four(8) == False
        print("is_power_of_four测试通过")
        
        # 测试只出现一次的数字
        nums1 = [2, 2, 1]
        assert BitManipulation.single_number(nums1) == 1
        print("single_number测试通过")
        
        # 测试只出现一次的数字II
        nums2 = [2, 2, 3, 2]
        assert BitManipulation.single_number_ii(nums2) == 3
        print("single_number_ii测试通过")
        
        # 测试只出现一次的数字III
        nums3 = [1, 2, 1, 3, 2, 5]
        result = BitManipulation.single_number_iii(nums3)
        assert sorted(result) == [3, 5]
        print("single_number_iii测试通过")
        
        # 测试颠倒二进制位
        assert BitManipulation.reverse_bits(43261596) == 964176192
        print("reverse_bits测试通过")
        
        # 测试比特位计数
        result = BitManipulation.count_bits(5)
        expected = [0, 1, 1, 2, 1, 2]
        assert result == expected
        print("count_bits测试通过")
        
        # 测试数字范围按位与
        assert BitManipulation.range_bitwise_and(5, 7) == 4
        print("range_bitwise_and测试通过")
        
        # 测试两整数之和
        assert BitManipulation.get_sum(3, 5) == 8
        print("get_sum测试通过")
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "hamming_weight": {
                "time": "O(k) - k为1的个数",
                "space": "O(1)",
                "desc": "Brian Kernighan算法"
            },
            "is_power_of_two": {
                "time": "O(1)",
                "space": "O(1)", 
                "desc": "检查二进制表示"
            },
            "single_number": {
                "time": "O(n)",
                "space": "O(1)",
                "desc": "异或操作"
            },
            "reverse_bits": {
                "time": "O(1)",
                "space": "O(1)",
                "desc": "分治交换"
            },
            "count_bits": {
                "time": "O(n)", 
                "space": "O(n)",
                "desc": "动态规划"
            }
        }
        
        for name, info in algorithms.items():
            print(f"{name}:")
            print(f"  时间复杂度: {info['time']}")
            print(f"  空间复杂度: {info['space']}")
            print(f"  描述: {info['desc']}")
            print()


def main():
    """主函数"""
    print("位操作算法实现")
    print("包含LeetCode多个位操作相关题目的解决方案")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_hamming_weight()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("=== 示例使用 ===")
    
    # 测试位1的个数
    test_num = 11
    print(f"数字 {test_num} 的二进制表示中1的个数:")
    print(f"  Kernighan算法: {BitManipulation.hamming_weight(test_num)}")
    print(f"  并行算法: {BitManipulation.hamming_weight_parallel(test_num)}")
    
    # 测试2的幂
    print(f"\n2的幂判断:")
    print(f"16是2的幂: {BitManipulation.is_power_of_two(16)}")
    print(f"15是2的幂: {BitManipulation.is_power_of_two(15)}")
    
    # 测试只出现一次的数字
    nums = [4, 1, 2, 1, 2]
    print(f"\n只出现一次的数字: {BitManipulation.single_number(nums)}")
    
    # 测试比特位计数
    bits_count = BitManipulation.count_bits(5)
    print(f"\n0到5的比特位计数: {bits_count}")
    
    # 测试两整数之和
    print(f"\n3 + 5 = {BitManipulation.get_sum(3, 5)} (不使用+运算符)")


if __name__ == "__main__":
    main()