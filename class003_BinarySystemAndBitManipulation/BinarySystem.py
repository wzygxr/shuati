#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
================================================================================================
Class003: 二进制系统与位运算专题（Binary System and Bit Manipulation）- Python版本
================================================================================================

【核心知识点总结】
1. 位运算基础：
   - AND(&): 两位都为1时结果为1，常用于清零特定位、提取特定位
   - OR(|): 有一位为1时结果为1，常用于设置特定位
   - XOR(^): 两位不同时结果为1，常用于无临时变量交换、查找单独元素
   - NOT(~): 按位取反
   - 左移(<<): 相当于乘以2的幂（非负数），所有位向左移动
   - 右移(>>): 算术右移，保留符号位

2. 常见技巧与应用场景：
   ① 判断奇偶：(n & 1) == 1 为奇数，== 0 为偶数
   ② 交换变量：a ^= b; b ^= a; a ^= b; （无需临时变量）
   ③ 清除最右边的1：n &= (n - 1)
   ④ 获取最右边的1：n & (-n)
   ⑤ 判断2的幂：n > 0 and (n & (n - 1)) == 0
   ⑥ 计算二进制中1的个数：Brian Kernighan算法
   ⑦ 找唯一元素：利用 a ^ a = 0, a ^ 0 = a
   ⑧ 位掩码：用于状态压缩DP、集合表示

3. 题型分类：
   【基础操作】：位反转、位计数、进制转换
   【数学性质】：幂判断、格雷编码、斯特林数
   【查找问题】：找唯一元素、找缺失数字、找重复数字
   【XOR应用】：异或和、最大异或对、异或路径
   【位运算优化】：快速幂、乘法优化、状态压缩
   【工程应用】：位图、布隆过滤器、哈希表优化

【时间复杂度分析技巧】
- 基础位运算：O(1) 常数时间
- 遍历所有位：O(log n) 或 O(32/64) = O(1)
- Brian Kernighan算法：O(k)，k为1的个数
- Trie树优化XOR：O(n * log(max_value))

【空间复杂度优化】
- 原地操作：使用异或交换，空间O(1)
- 位压缩：用一个整数表示多个布尔值
- 滚动数组：DP优化空间

【边界场景与异常处理】
1. 负数处理：
   - Python使用任意精度整数，负数需要特殊处理
   - 对于32位操作，需要使用掩码限制位数
2. 溢出处理：
   - Python整数无溢出问题，但需要注意32位限制
   - 使用掩码 0xFFFFFFFF 限制为32位
3. 边界值：
   - 0的特殊处理（补数、幂判断等）
   - 空数组的判断
   - 单元素数组的特殊情况

【语言特性差异（Python vs Java vs C++）】
1. 整数表示：
   - Python: 任意精度整数，无固定大小
   - Java: 固定32位int/64位long，有符号
   - C++: int大小取决于平台，有signed/unsigned
2. 位运算操作符：
   - Python: 无>>>，负数需要特殊处理
   - Java: 有>>>无符号右移
   - C++: 无>>>，对unsigned自动逻辑右移
3. 位长度获取：
   - Python: bin(n).count('1'), n.bit_length()
   - Java: Integer.bitCount(), Integer.numberOfLeadingZeros()
   - C++: __builtin_popcount(), __builtin_clz()

【工程化考量】
1. 代码可读性：
   - 使用常量命名位掩码：MASK_ODD_BITS = 0x55555555
   - 添加详细注释说明位操作意图
   - 复杂位运算拆分为多步
2. 性能优化：
   - 使用位运算替代乘除法（仅限2的幂）
   - 查表法优化频繁的位计数
   - 使用内置函数优化
3. 异常处理：
   - 参数验证：if n < 0: raise ValueError("参数错误")
   - 边界检查：数组访问前检查索引
   - 类型检查：确保输入为整数
4. 单元测试：
   - 正常值测试
   - 边界值测试（0, 1, 最大值, 最小值）
   - 负数测试
   - 大规模数据性能测试
"""

class BinarySystem:
    """
    二进制系统与位运算专题 - Python实现
    """
    
    @staticmethod
    def main():
        """
        主函数：演示二进制系统的基本操作和扩展题目
        
        来源: 算法学习系统
        更新时间: 2025-10-23
        题目总数: 200+道精选题目
        平台覆盖: LeetCode (力扣)、LintCode (炼码)、HackerRank、赛码、AtCoder、USACO、洛谷 (Luogu)、CodeChef、SPOJ、Project Euler、HackerEarth、计蒜客、各大高校 OJ、zoj、MarsCode、UVa OJ、TimusOJ、AizuOJ、Comet OJ、杭电 OJ、LOJ、牛客、杭州电子科技大学、acwing、codeforces、hdu、poj、剑指Offer等
        """
        print("=== 二进制系统与位运算专题 ===")
        print("作者: 算法学习系统")
        print("日期: 2025年10月23日")
        print()
        
        # 运行基础操作测试
        BinarySystem.run_basic_operations()
        
        # 运行扩展题目测试
        BinarySystem.run_extended_problems()
        
        print("=== 所有测试完成 ===")
    
    @staticmethod
    def run_basic_operations():
        """
        运行基础操作测试
        """
        print("=== 基础操作测试 ===")
        
        # 测试位反转
        print("位反转测试:")
        test_reverse = 43261596  # 00000010100101000001111010011100
        print(f"原数字: {test_reverse} (二进制: {bin(test_reverse)})")
        reversed_num = BinarySystem.reverse_bits(test_reverse)
        print(f"颠倒后: {reversed_num} (二进制: {bin(reversed_num)})")
        print("预期结果: 964176192")
        print()
        
        # 测试汉明重量
        print("汉明重量测试:")
        test_hamming = 11  # 1011
        print(f"数字: {test_hamming} (二进制: {bin(test_hamming)})")
        print(f"1的个数: {BinarySystem.hamming_weight(test_hamming)}")
        print("预期结果: 3")
        print()
        
        # 测试2的幂判断
        print("2的幂判断测试:")
        print(f"8是2的幂: {'是' if BinarySystem.is_power_of_two(8) else '否'}")
        print(f"10是2的幂: {'是' if BinarySystem.is_power_of_two(10) else '否'}")
        print()
        
        # 测试4的幂判断
        print("4的幂判断测试:")
        print(f"16是4的幂: {'是' if BinarySystem.is_power_of_four(16) else '否'}")
        print(f"8是4的幂: {'是' if BinarySystem.is_power_of_four(8) else '否'}")
        print()
    
    @staticmethod
    def run_extended_problems():
        """
        运行扩展题目测试
        包含从各大OJ平台精选的位运算题目
        """
        print("=== 扩展题目测试 ===")
        
        # LeetCode 136 - Single Number
        BinarySystem.test_single_number()
        
        # LeetCode 137 - Single Number II
        BinarySystem.test_single_number_ii()
        
        # LeetCode 260 - Single Number III
        BinarySystem.test_single_number_iii()
        
        # LeetCode 191 - Number of 1 Bits
        BinarySystem.test_number_of_1_bits()
        
        # LeetCode 338 - Counting Bits
        BinarySystem.test_counting_bits()
        
        # LeetCode 190 - Reverse Bits
        BinarySystem.test_reverse_bits()
        
        # LeetCode 231 - Power of Two
        BinarySystem.test_power_of_two()
        
        # LeetCode 342 - Power of Four
        BinarySystem.test_power_of_four()
        
        # LeetCode 268 - Missing Number
        BinarySystem.test_missing_number()
        
        # LeetCode 371 - Sum of Two Integers
        BinarySystem.test_sum_of_two_integers()
        
        # LeetCode 201 - Bitwise AND of Numbers Range
        BinarySystem.test_bitwise_and_of_numbers_range()
        
        # LeetCode 477 - Total Hamming Distance
        BinarySystem.test_total_hamming_distance()
        
        print("=== 扩展题目测试完成 ===")
    
    @staticmethod
    def reverse_bits(n: int) -> int:
        """
        1. LeetCode 190. Reverse Bits (颠倒二进制位)
        题目链接: https://leetcode.com/problems/reverse-bits/
        题目描述: 颠倒给定的 32 位无符号整数的二进制位
        时间复杂度: O(1) - 固定32次循环
        空间复杂度: O(1)
        """
        result = 0
        for i in range(32):
            result = (result << 1) | (n & 1)
            n >>= 1
        return result
    
    @staticmethod
    def hamming_weight(n: int) -> int:
        """
        2. LeetCode 191. Number of 1 Bits (位1的个数)
        题目链接: https://leetcode.com/problems/number-of-1-bits/
        题目描述: 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数
        时间复杂度: O(k)，k为1的个数
        空间复杂度: O(1)
        """
        count = 0
        while n != 0:
            n &= (n - 1)
            count += 1
        return count
    
    @staticmethod
    def is_power_of_two(n: int) -> bool:
        """
        3. LeetCode 231. Power of Two (2的幂)
        题目链接: https://leetcode.com/problems/power-of-two/
        题目描述: 给定一个整数，编写一个函数来判断它是否是 2 的幂次方
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        return n > 0 and (n & (n - 1)) == 0
    
    @staticmethod
    def is_power_of_four(n: int) -> bool:
        """
        4. LeetCode 342. Power of Four (4的幂)
        题目链接: https://leetcode.com/problems/power-of-four/
        题目描述: 给定一个整数，写一个函数来判断它是否是 4 的幂次方
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        return n > 0 and (n & (n - 1)) == 0 and (n & 0x55555555) != 0
    
    @staticmethod
    def single_number(nums: list) -> int:
        """
        5. LeetCode 136. Single Number (只出现一次的数字)
        题目链接: https://leetcode.com/problems/single-number/
        题目描述: 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        解题思路：
        利用异或运算的性质：
        1. a ^ a = 0（任何数与自己异或为0）
        2. a ^ 0 = a（任何数与0异或为自己）
        3. 异或运算满足交换律和结合律
        因此，所有出现两次的数字异或后结果为0，最后剩下的就是只出现一次的数字。
        """
        if not nums:
            raise ValueError("数组不能为空")
        
        result = 0
        for num in nums:
            result ^= num
        return result
    
    @staticmethod
    def test_single_number():
        """测试LeetCode 136"""
        print("\n=== LeetCode 136 - Single Number 测试 ===")
        nums1 = [2, 2, 1]
        nums2 = [4, 1, 2, 1, 2]
        nums3 = [1]
        
        print(f"测试用例1: {nums1} -> {BinarySystem.single_number(nums1)}")
        print(f"测试用例2: {nums2} -> {BinarySystem.single_number(nums2)}")
        print(f"测试用例3: {nums3} -> {BinarySystem.single_number(nums3)}")
    
    @staticmethod
    def single_number_ii(nums: list) -> int:
        """
        6. LeetCode 137. Single Number II (只出现一次的数字 II)
        题目链接: https://leetcode.com/problems/single-number-ii/
        题目描述: 给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        解题思路：
        使用位运算状态机的方法。对于每个二进制位，统计所有数字在该位上1的个数，
        如果该位上1的个数对3取余不为0，则说明只出现一次的数字在该位上是1。
        """
        if not nums:
            raise ValueError("数组不能为空")
        
        result = 0
        for i in range(32):
            sum_bits = 0
            for num in nums:
                # 处理Python的负数右移问题
                if num < 0:
                    # 对于负数，使用补码处理
                    sum_bits += ((num + 0x100000000) >> i) & 1
                else:
                    sum_bits += (num >> i) & 1
            if sum_bits % 3 != 0:
                result |= (1 << i)
        return result
    
    @staticmethod
    def test_single_number_ii():
        """测试LeetCode 137"""
        print("\n=== LeetCode 137 - Single Number II 测试 ===")
        nums1 = [2, 2, 3, 2]
        nums2 = [0, 1, 0, 1, 0, 1, 99]
        
        print(f"测试用例1: {nums1} -> {BinarySystem.single_number_ii(nums1)}")
        print(f"测试用例2: {nums2} -> {BinarySystem.single_number_ii(nums2)}")
    
    @staticmethod
    def single_number_iii(nums: list) -> list:
        """
        7. LeetCode 260. Single Number III (只出现一次的数字 III)
        题目链接: https://leetcode.com/problems/single-number-iii/
        题目描述: 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        解题思路：
        1. 首先获取两个只出现一次的数的异或结果
        2. 找到xor中最右边的1位，这表示两个数在这一位上不同
        3. 根据这一位将数组分为两组，分别异或得到两个数
        """
        if len(nums) < 2:
            raise ValueError("数组长度至少为2")
        
        # 第一步：计算所有数字的异或
        xor = 0
        for num in nums:
            xor ^= num
        
        # 第二步：找到最右边的1
        rightmost_one = xor & (-xor)
        
        # 第三步：根据这个位将数组分成两组
        result = [0, 0]
        for num in nums:
            if (num & rightmost_one) == 0:
                result[0] ^= num
            else:
                result[1] ^= num
        
        return result
    
    @staticmethod
    def test_single_number_iii():
        """测试LeetCode 260"""
        print("\n=== LeetCode 260 - Single Number III 测试 ===")
        nums1 = [1, 2, 1, 3, 2, 5]
        result = BinarySystem.single_number_iii(nums1)
        print(f"测试用例: {nums1} -> {result}")
    
    @staticmethod
    def number_of_1_bits(n: int) -> int:
        """
        8. LeetCode 191. Number of 1 Bits (位1的个数)
        题目链接: https://leetcode.com/problems/number-of-1-bits/
        题目描述: 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）
        时间复杂度: O(k)，k为1的个数
        空间复杂度: O(1)
        
        解题思路：
        使用Brian Kernighan算法：
        每执行一次n = n & (n - 1)，就会将n的最后一个1变成0
        这样只需要循环k次，k为1的个数
        """
        # 处理Python的32位限制
        if n < 0:
            n = n & 0xFFFFFFFF
        count = 0
        while n != 0:
            n &= (n - 1)
            count += 1
        return count
    
    @staticmethod
    def test_number_of_1_bits():
        """测试LeetCode 191"""
        print("\n=== LeetCode 191 - Number of 1 Bits 测试 ===")
        print(f"11(1011)的1的个数: {BinarySystem.number_of_1_bits(11)}")
        print(f"128(10000000)的1的个数: {BinarySystem.number_of_1_bits(128)}")
    
    @staticmethod
    def counting_bits(n: int) -> list:
        """
        9. LeetCode 338. Counting Bits (比特位计数)
        题目链接: https://leetcode.com/problems/counting-bits/
        题目描述: 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回
        时间复杂度: O(n)
        空间复杂度: O(n)
        
        解题思路：
        利用已知结果：i的1的个数 = i/2的1的个数 + i的最低位是否为1
        即：bits[i] = bits[i >> 1] + (i & 1)
        """
        bits = [0] * (n + 1)
        for i in range(1, n + 1):
            bits[i] = bits[i >> 1] + (i & 1)
        return bits
    
    @staticmethod
    def test_counting_bits():
        """测试LeetCode 338"""
        print("\n=== LeetCode 338 - Counting Bits 测试 ===")
        result = BinarySystem.counting_bits(5)
        print(f"0到5的1的个数: {result}")
    
    @staticmethod
    def reverse_bits_leetcode(n: int) -> int:
        """
        10. LeetCode 190. Reverse Bits (颠倒二进制位)
        题目链接: https://leetcode.com/problems/reverse-bits/
        题目描述: 颠倒给定的 32 位无符号整数的二进制位
        时间复杂度: O(1) - 固定32次循环
        空间复杂度: O(1)
        
        解题思路：
        从右到左提取每一位，然后从左到右放置到结果中
        """
        # 处理32位无符号整数
        result = 0
        for i in range(32):
            result = (result << 1) | (n & 1)
            n >>= 1
        return result
    
    @staticmethod
    def test_reverse_bits():
        """测试LeetCode 190"""
        print("\n=== LeetCode 190 - Reverse Bits 测试 ===")
        n = 43261596  # 00000010100101000001111010011100
        reversed_num = BinarySystem.reverse_bits_leetcode(n)
        print(f"原数字: {n}, 颠倒后: {reversed_num}")
    
    @staticmethod
    def power_of_two(n: int) -> bool:
        """
        11. LeetCode 231. Power of Two (2的幂)
        题目链接: https://leetcode.com/problems/power-of-two/
        题目描述: 给定一个整数，编写一个函数来判断它是否是 2 的幂次方
        时间复杂度: O(1)
        空间复杂度: O(1)
        
        解题思路：
        位运算技巧：2的幂在二进制表示中只有一个位是1
        n & (n-1) 会清除 n 中最低位的 1
        如果 n 是 2 的幂，那么 n & (n-1) == 0
        """
        return n > 0 and (n & (n - 1)) == 0
    
    @staticmethod
    def test_power_of_two():
        """测试LeetCode 231"""
        print("\n=== LeetCode 231 - Power of Two 测试 ===")
        print(f"8是2的幂: {'是' if BinarySystem.power_of_two(8) else '否'}")
        print(f"10是2的幂: {'是' if BinarySystem.power_of_two(10) else '否'}")
    
    @staticmethod
    def power_of_four(n: int) -> bool:
        """
        12. LeetCode 342. Power of Four (4的幂)
        题目链接: https://leetcode.com/problems/power-of-four/
        题目描述: 给定一个整数，写一个函数来判断它是否是 4 的幂次方
        时间复杂度: O(1)
        空间复杂度: O(1)
        
        解题思路：
        4的幂首先是2的幂，而且1必须在奇数位上（从右往左数，最右边是第0位）
        0x55555555 是十六进制表示，二进制是 01010101010101010101010101010101
        这个数在所有奇数位上都是1，用于检查1是否在正确的位置上
        """
        return n > 0 and (n & (n - 1)) == 0 and (n & 0x55555555) != 0
    
    @staticmethod
    def test_power_of_four():
        """测试LeetCode 342"""
        print("\n=== LeetCode 342 - Power of Four 测试 ===")
        print(f"16是4的幂: {'是' if BinarySystem.power_of_four(16) else '否'}")
        print(f"8是4的幂: {'是' if BinarySystem.power_of_four(8) else '否'}")
    
    @staticmethod
    def missing_number(nums: list) -> int:
        """
        13. LeetCode 268. Missing Number (缺失数字)
        题目链接: https://leetcode.com/problems/missing-number/
        题目描述: 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        解题思路：
        利用异或运算的性质：
        1. a ^ a = 0
        2. a ^ 0 = a
        3. 异或运算满足交换律和结合律
        
        位运算技巧：利用异或的性质 x ^ x = 0, x ^ 0 = x
        """
        n = len(nums)
        missing = n
        for i in range(n):
            missing ^= i ^ nums[i]
        return missing
    
    @staticmethod
    def test_missing_number():
        """测试LeetCode 268"""
        print("\n=== LeetCode 268 - Missing Number 测试 ===")
        nums1 = [3, 0, 1]
        nums2 = [0, 1]
        print(f"数组{nums1}缺失的数字: {BinarySystem.missing_number(nums1)}")
        print(f"数组{nums2}缺失的数字: {BinarySystem.missing_number(nums2)}")
    
    @staticmethod
    def sum_of_two_integers(a: int, b: int) -> int:
        """
        14. LeetCode 371. Sum of Two Integers (两整数之和)
        题目链接: https://leetcode.com/problems/sum-of-two-integers/
        题目描述: 不使用运算符 + 和 -，计算两整数 a 、b 之和
        时间复杂度: O(1) - 最多32次循环
        空间复杂度: O(1)
        
        解题思路：
        使用位运算模拟加法过程：
        1. 异或运算得到无进位和
        2. 与运算左移一位得到进位
        3. 重复直到进位为0
        """
        # 处理32位整数
        MASK = 0xFFFFFFFF
        MAX_INT = 0x7FFFFFFF
        
        while b != 0:
            carry = ((a & b) << 1) & MASK
            a = (a ^ b) & MASK
            b = carry
        
        # 处理负数情况
        if a > MAX_INT:
            a = ~(a ^ MASK)
        return a
    
    @staticmethod
    def test_sum_of_two_integers():
        """测试LeetCode 371"""
        print("\n=== LeetCode 371 - Sum of Two Integers 测试 ===")
        print(f"1 + 2 = {BinarySystem.sum_of_two_integers(1, 2)}")
        print(f"15 + 7 = {BinarySystem.sum_of_two_integers(15, 7)}")
    
    @staticmethod
    def bitwise_and_of_numbers_range(m: int, n: int) -> int:
        """
        15. LeetCode 201. Bitwise AND of Numbers Range (数字范围按位与)
        题目链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
        题目描述: 给定范围 [m, n]，其中 0 <= m <= n <= 2147483647，返回此范围内所有数字的按位与（包含 m, n 两端点）
        时间复杂度: O(1) - 最多32次循环
        空间复杂度: O(1)
        
        解题思路：
        找到m和n的公共前缀，因为在这个范围内的所有数字，只有公共前缀部分在按位与后保持不变。
        """
        shift = 0
        while m < n:
            m >>= 1
            n >>= 1
            shift += 1
        return m << shift
    
    @staticmethod
    def test_bitwise_and_of_numbers_range():
        """测试LeetCode 201"""
        print("\n=== LeetCode 201 - Bitwise AND of Numbers Range 测试 ===")
        print(f"[5, 7]的按位与: {BinarySystem.bitwise_and_of_numbers_range(5, 7)}")
        print(f"[0, 1]的按位与: {BinarySystem.bitwise_and_of_numbers_range(0, 1)}")
    
    @staticmethod
    def total_hamming_distance(nums: list) -> int:
        """
        16. LeetCode 477. Total Hamming Distance (汉明距离总和)
        题目链接: https://leetcode.com/problems/total-hamming-distance/
        题目描述: 两个整数的 汉明距离 指的是这两个数字的二进制数对应位不同的数量。给你一个整数数组 nums，请你求出数组中任意两个数之间汉明距离的总和
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        解题思路：
        对于每一位分别计算汉明距离，然后求和：
        1. 对于第i位，统计有多少数字在该位上是1（设为k）
        2. 那么该位贡献的汉明距离为 k * (n - k)
        3. 将所有位的贡献相加得到总和
        """
        total = 0
        n = len(nums)
        for i in range(32):
            count_ones = 0
            for num in nums:
                count_ones += (num >> i) & 1
            total += count_ones * (n - count_ones)
        return total
    
    @staticmethod
    def test_total_hamming_distance():
        """测试LeetCode 477"""
        print("\n=== LeetCode 477 - Total Hamming Distance 测试 ===")
        nums = [4, 14, 2]
        print(f"数组{nums}的汉明距离总和: {BinarySystem.total_hamming_distance(nums)}")
    
    @staticmethod
    def xor_queries(arr: list, queries: list) -> list:
        """
        17. LeetCode 1310. XOR Queries of a Subarray (子数组异或查询)
        题目链接: https://leetcode.com/problems/xor-queries-of-a-subarray/
        题目描述: 给你一个正整数数组 arr，你需要处理以下两种类型的查询：
                 1. 计算从索引 L 到 R 的元素的异或值
        时间复杂度: O(n + q) - n为数组长度，q为查询次数
        空间复杂度: O(n) - 前缀异或数组
        
        解题思路：
        使用前缀异或数组优化多次查询：
        1. 构建前缀异或数组 prefix，其中 prefix[i] = arr[0] ^ arr[1] ^ ... ^ arr[i-1]
        2. 对于查询 [L, R]，结果为 prefix[R+1] ^ prefix[L]
        """
        n = len(arr)
        prefix = [0] * (n + 1)
        
        # 构建前缀异或数组
        for i in range(n):
            prefix[i + 1] = prefix[i] ^ arr[i]
        
        result = []
        
        # 处理每个查询
        for left, right in queries:
            result.append(prefix[right + 1] ^ prefix[left])
        
        return result
    
    @staticmethod
    def test_xor_queries():
        """测试LeetCode 1310"""
        print("\n=== LeetCode 1310 - XOR Queries of a Subarray 测试 ===")
        arr = [1, 3, 4, 8]
        queries = [[0, 1], [1, 2], [0, 3], [3, 3]]
        result = BinarySystem.xor_queries(arr, queries)
        
        print(f"数组: {arr}")
        print(f"查询结果: {result}")
    
    @staticmethod
    def min_bit_flips(start: int, goal: int) -> int:
        """
        18. LeetCode 2220. Minimum Bit Flips to Convert Number (转换数字的最少位翻转次数)
        题目链接: https://leetcode.com/problems/minimum-bit-flips-to-convert-number/
        题目描述: 一次位翻转定义为将数字 x 二进制中的一个位进行翻转操作，即将 0 变成 1 ，或者将 1 变成 0 。
                 给你两个整数 start 和 goal ，请你返回将 start 转变成 goal 的最少位翻转次数。
        时间复杂度: O(1) - 固定32位比较
        空间复杂度: O(1) - 只使用常数级额外空间
        
        解题思路：
        计算两个数字的汉明距离，即异或结果中1的个数
        """
        # 计算异或结果中1的个数
        return bin(start ^ goal).count('1')
    
    @staticmethod
    def test_min_bit_flips():
        """测试LeetCode 2220"""
        print("\n=== LeetCode 2220 - Minimum Bit Flips to Convert Number 测试 ===")
        print(f"start=10, goal=7 的最少位翻转次数: {BinarySystem.min_bit_flips(10, 7)}")
        print(f"start=3, goal=4 的最少位翻转次数: {BinarySystem.min_bit_flips(3, 4)}")
    
    @staticmethod
    def find_array(pref: list) -> list:
        """
        19. LeetCode 2433. Find The Original Array of Prefix Xor (找出前缀异或的原始数组)
        题目链接: https://leetcode.com/problems/find-the-original-array-of-prefix-xor/
        题目描述: 给你一个长度为 n 的整数数组 pref。找出并返回满足以下条件且长度为 n 的数组 arr：
                 pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i].
        时间复杂度: O(n) - 遍历数组一次
        空间复杂度: O(n) - 结果数组空间
        
        解题思路：
        根据异或的性质，如果 pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i]
        那么 arr[i] = pref[i] ^ pref[i-1] (i > 0)
        arr[0] = pref[0]
        """
        n = len(pref)
        arr = [0] * n
        
        # 第一个元素就是前缀异或的第一个元素
        arr[0] = pref[0]
        
        # 根据公式计算其他元素
        for i in range(1, n):
            arr[i] = pref[i] ^ pref[i - 1]
        
        return arr
    
    @staticmethod
    def test_find_array():
        """测试LeetCode 2433"""
        print("\n=== LeetCode 2433 - Find The Original Array of Prefix Xor 测试 ===")
        pref = [5, 2, 0, 3, 1]
        result = BinarySystem.find_array(pref)
        
        print(f"前缀异或数组: {pref}")
        print(f"原始数组: {result}")
    
    @staticmethod
    def binary_gap(n: int) -> int:
        """
        20. LeetCode 868. Binary Gap (二进制间距)
        题目链接: https://leetcode.com/problems/binary-gap/
        题目描述: 给定一个正整数 n，找到并返回 n 的二进制表示中两个相邻 1 之间的最长距离。
                 如果不存在两个相邻的 1，返回 0。
        时间复杂度: O(log n) - 遍历二进制位
        空间复杂度: O(1) - 只使用常数级额外空间
        
        解题思路：
        遍历二进制表示，记录相邻1之间的距离
        """
        max_gap = 0
        last_pos = -1
        pos = 0
        
        while n > 0:
            if (n & 1) == 1:
                if last_pos != -1:
                    max_gap = max(max_gap, pos - last_pos)
                last_pos = pos
            pos += 1
            n >>= 1
        
        return max_gap
    
    @staticmethod
    def test_binary_gap():
        """测试LeetCode 868"""
        print("\n=== LeetCode 868 - Binary Gap 测试 ===")
        print(f"n=22 的二进制间距: {BinarySystem.binary_gap(22)}")
        print(f"n=8 的二进制间距: {BinarySystem.binary_gap(8)}")
        print(f"n=5 的二进制间距: {BinarySystem.binary_gap(5)}")
    
    @staticmethod
    def bitwise_complement(n: int) -> int:
        """
        21. LeetCode 1009. Complement of Base 10 Integer (十进制整数的反码)
        题目链接: https://leetcode.com/problems/complement-of-base-10-integer/
        题目描述: 每个非负整数 N 都有其二进制表示。例如，5 可以被表示为二进制 "101"，11 可以用二进制 "1011" 表示，依此类推。
                 注意，除 N = 0 外，任何二进制表示中都不含前导零。
                 二进制的反码表示是将每个 1 改为 0 且每个 0 改为 1。例如，二进制数 "101" 的二进制反码为 "010"。
                 给你一个十进制数 N，请你返回其二进制表示的反码所对应的十进制整数。
        时间复杂度: O(log n) - 构造掩码
        空间复杂度: O(1) - 只使用常数级额外空间
        
        解题思路：
        1. 构造一个掩码，该掩码的位数与n相同，但所有位都是1
        2. 使用异或操作取反
        """
        if n == 0:
            return 1
        
        mask = 1
        # 构造一个掩码，该掩码的位数与n相同，但所有位都是1
        while mask < n:
            mask = (mask << 1) | 1
        # 使用异或操作取反
        return n ^ mask
    
    @staticmethod
    def test_bitwise_complement():
        """测试LeetCode 1009"""
        print("\n=== LeetCode 1009 - Complement of Base 10 Integer 测试 ===")
        print(f"n=5 的反码: {BinarySystem.bitwise_complement(5)}")
        print(f"n=7 的反码: {BinarySystem.bitwise_complement(7)}")
        print(f"n=10 的反码: {BinarySystem.bitwise_complement(10)}")

if __name__ == "__main__":
    BinarySystem.main()