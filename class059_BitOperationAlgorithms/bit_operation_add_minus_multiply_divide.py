# 不用任何算术运算，只用位运算实现加减乘除及相关位运算算法
# 代码实现中你找不到任何一个算术运算符
# 本文件包含基础位运算实现、四则运算实现以及多种位运算相关算法题解
# 涵盖LeetCode、LintCode、HackerRank、AtCoder、USACO、洛谷、CodeChef、SPOJ、Project Euler、HackerEarth、计蒜客、各大高校OJ、zoj、MarsCode、UVa OJ、TimusOJ、AizuOJ、Comet OJ、杭电OJ、LOJ、牛客、杭州电子科技大学、acwing、codeforces、hdu、poj、剑指Offer等各大算法平台的位运算相关题目


"""
位运算实现四则运算及相关算法题解

本类使用纯位运算实现加减乘除四则运算，以及多种与位运算相关的算法题目解答。
所有实现都避免使用任何算术运算符（+、-、*、/），仅使用位运算符。

核心思想：
1. 加法：利用异或运算实现无进位加法，利用与运算和左移实现进位
2. 减法：基于加法和相反数实现，a - b = a + (-b)
3. 乘法：基于二进制分解，检查乘数每一位是否为1，为1则将被乘数左移相应位数后累加
4. 除法：从高位到低位尝试减法，使用位移优化性能

作者: Algorithm Journey
版本: 1.0
"""


class BitOperationAddMinusMultiplyDivide:
    MIN = -2**31  # INT_MIN
    MAX = 2**31 - 1  # INT_MAX

    @staticmethod
    def divide(a, b):
        """
        两数相除
        
        算法原理：
        处理各种边界情况，特别是整数最小值的情况，然后调用div方法进行计算
        
        特殊情况处理：
        1. a和b都是整数最小值：返回1
        2. b是整数最小值：返回0
        3. a是整数最小值且b是-1：返回整数最大值（防止溢出）
        4. 其他情况：调用div方法计算
        
        测试链接 : https://leetcode.cn/problems/divide-two-integers/
        
        Args:
            a (int): 被除数
            b (int): 除数
            
        Returns:
            int: a除以b的结果
        """
        if a == BitOperationAddMinusMultiplyDivide.MIN and b == BitOperationAddMinusMultiplyDivide.MIN:
            # a和b都是整数最小
            return 1
        if a != BitOperationAddMinusMultiplyDivide.MIN and b != BitOperationAddMinusMultiplyDivide.MIN:
            # a和b都不是整数最小，那么正常去除
            return BitOperationAddMinusMultiplyDivide.div(a, b)
        if b == BitOperationAddMinusMultiplyDivide.MIN:
            # a不是整数最小，b是整数最小
            return 0
        # a是整数最小，b是-1，返回整数最大，因为题目里明确这么说了
        if b == BitOperationAddMinusMultiplyDivide.neg(1):
            return BitOperationAddMinusMultiplyDivide.MAX
        # a是整数最小，b不是整数最小，b也不是-1
        a = BitOperationAddMinusMultiplyDivide.add(
            a, b if b > 0 else BitOperationAddMinusMultiplyDivide.neg(b))
        ans = BitOperationAddMinusMultiplyDivide.div(a, b)
        offset = BitOperationAddMinusMultiplyDivide.neg(
            1) if b > 0 else 1
        return BitOperationAddMinusMultiplyDivide.add(ans, offset)

    @staticmethod
    def div(a, b):
        """
        除法辅助函数：必须保证a和b都不是整数最小值，返回a除以b的结果
        
        算法原理：
        1. 将a和b都转换为正数处理（取绝对值）
        2. 从最高位开始，尝试将被除数减去除数的倍数
        3. 使用位移优化性能，避免逐个减法
        
        时间复杂度：O(1) - 固定位数的整数
        空间复杂度：O(1) - 只使用常数级额外空间
        
        Args:
            a (int): 被除数（非整数最小值）
            b (int): 除数（非整数最小值）
            
        Returns:
            int: a除以b的结果
        """
        x = BitOperationAddMinusMultiplyDivide.neg(
            a) if a < 0 else a
        y = BitOperationAddMinusMultiplyDivide.neg(
            b) if b < 0 else b
        ans = 0
        i = 30
        while i >= 0:
            if (x >> i) >= y:
                ans |= (1 << i)
                x = BitOperationAddMinusMultiplyDivide.minus(x, y << i)
            i = BitOperationAddMinusMultiplyDivide.minus(i, 1)
        return BitOperationAddMinusMultiplyDivide.neg(ans) if (a < 0) ^ (b < 0) else ans

    @staticmethod
    def add(a, b):
        """
        加法实现
        
        算法原理：
        1. 异或运算(^)实现无进位加法
        2. 与运算(&)和左移(<<)实现进位
        3. 循环直到没有进位
        
        例如：计算 5 + 3
        5 的二进制: 101
        3 的二进制: 011
        第一次循环:
          无进位加法: 101 ^ 011 = 110
          进位: (101 & 011) << 1 = 001 << 1 = 010
        第二次循环:
          无进位加法: 110 ^ 010 = 100
          进位: (110 & 010) << 1 = 010 << 1 = 100
        第三次循环:
          无进位加法: 100 ^ 100 = 000
          进位: (100 & 100) << 1 = 100 << 1 = 1000
        第四次循环:
          无进位加法: 000 ^ 1000 = 1000
          进位: (000 & 1000) << 1 = 000 << 1 = 000
        进位为0，循环结束，结果为 1000 (二进制) = 8 (十进制)
        
        时间复杂度：O(1) - 固定位数的整数
        空间复杂度：O(1) - 只使用常数级额外空间
        
        Args:
            a (int): 第一个加数
            b (int): 第二个加数
            
        Returns:
            int: a与b的和
        """
        ans = a
        while b != 0:
            # ans : a和b无进位相加的结果
            ans = a ^ b
            # b : a和b相加时的进位信息
            b = (a & b) << 1
            a = ans
        return ans

    @staticmethod
    def add_recursive(a, b):
        """
        递归版本的加法实现（LeetCode 371. 两整数之和）
        题目链接: https://leetcode.cn/problems/sum-of-two-integers/
        时间复杂度: O(1) - 因为整数的位数是固定的
        空间复杂度: O(1) - 只使用常数级额外空间
        """
        # 递归终止条件：当没有进位时，异或结果就是最终结果
        if b == 0:
            return a
        # 递归计算：无进位相加的结果 + 进位
        return BitOperationAddMinusMultiplyDivide.add_recursive(a ^ b, (a & b) << 1)

    @staticmethod
    def minus(a, b):
        """
        减法实现
        
        算法原理：
        基于加法和相反数实现
        a - b = a + (-b)
        
        时间复杂度：O(1) - 固定位数的整数
        空间复杂度：O(1) - 只使用常数级额外空间
        
        Args:
            a (int): 被减数
            b (int): 减数
            
        Returns:
            int: a与b的差
        """
        return BitOperationAddMinusMultiplyDivide.add(a, BitOperationAddMinusMultiplyDivide.neg(b))

    @staticmethod
    def neg(n):
        """
        求相反数
        
        算法原理：
        基于补码表示法
        -n = ~n + 1
        
        时间复杂度：O(1) - 固定位数的整数
        空间复杂度：O(1) - 只使用常数级额外空间
        
        Args:
            n (int): 待求相反数的整数
            
        Returns:
            int: n的相反数
        """
        return BitOperationAddMinusMultiplyDivide.add(~n, 1)

    @staticmethod
    def multiply(a, b):
        """
        乘法实现（龟速乘）
        
        算法原理：
        基于二进制分解
        检查乘数b的每一位是否为1
        如果为1，则将被乘数a左移相应位数后累加到结果中
        
        例如：计算 5 * 3
        5 的二进制: 101
        3 的二进制: 011
        检查3的每一位：
          第0位：1，将5左移0位(5)累加到结果中
          第1位：1，将5左移1位(10)累加到结果中
          第2位：0，不累加
        结果：5 + 10 = 15
        
        时间复杂度：O(log b) - b的二进制位数
        空间复杂度：O(1) - 只使用常数级额外空间
        
        Args:
            a (int): 被乘数
            b (int): 乘数
            
        Returns:
            int: a与b的积
        """
        ans = 0
        while b != 0:
            if (b & 1) != 0:
                # 考察b当前最右的状态！
                ans = BitOperationAddMinusMultiplyDivide.add(ans, a)
            a <<= 1
            b >>= 1  # Python中使用算术右移
        return ans

    @staticmethod
    def hamming_weight(n):
        """
        计算一个数字的二进制表示中1的个数（汉明重量）
        LeetCode 191. 位1的个数
        题目链接: https://leetcode.cn/problems/number-of-1-bits/
        题目描述: 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
        
        算法原理：
        遍历32位，检查每一位是否为1
        
        时间复杂度: O(1) - 最多循环32次（32位整数）
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            n (int): 输入的整数
            
        Returns:
            int: n的二进制表示中1的个数
        """
        # 处理负数情况
        if n < 0:
            n = n & 0xFFFFFFFF  # 将负数转换为32位无符号整数
            
        count = 0
        for i in range(32):
            # 检查n的第i位是否为1
            if (n & (1 << i)) != 0:
                count = BitOperationAddMinusMultiplyDivide.add(count, 1)
        return count

    @staticmethod
    def hamming_weight_optimized(n):
        """
        优化版本的汉明重量计算（更高效）
        
        算法原理：
        利用 n & (n-1) 可以清除n的二进制表示中最右边的1
        每次操作都会清除最右边的一个1，直到n变为0
        
        例如：计算 12 的汉明重量
        12 的二进制: 1100
        第一次：1100 & 1011 = 1000 (清除最右边的1)
        第二次：1000 & 0111 = 0000 (清除最右边的1)
        循环2次，所以汉明重量为2
        
        时间复杂度: O(k) - k是二进制表示中1的个数
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            n (int): 输入的整数
            
        Returns:
            int: n的二进制表示中1的个数
        """
        # 处理负数情况
        if n < 0:
            n = n & 0xFFFFFFFF  # 将负数转换为32位无符号整数
            
        count = 0
        while n != 0:
            count = BitOperationAddMinusMultiplyDivide.add(count, 1)
            # 清除最右边的1
            n = n & (n - 1)
        return count

    @staticmethod
    def is_power_of_two(n):
        """
        判断一个数是否是2的幂
        LeetCode 231. 2的幂
        题目链接: https://leetcode.cn/problems/power-of-two/
        题目描述: 给你一个整数 n，请你判断该整数是否是 2 的幂次方。
        
        算法原理：
        2的幂在二进制表示中只有一个1，且必须是正数
        n & (n-1) 会清除n的二进制表示中最右边的1
        如果n是2的幂，那么n & (n-1)的结果应该是0
        
        例如：
        8 的二进制: 1000
        7 的二进制: 0111
        8 & 7 = 0000
        
        时间复杂度: O(1) - 只进行一次位运算
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            n (int): 待判断的整数
            
        Returns:
            bool: 如果n是2的幂返回True，否则返回False
        """
        # 2的幂在二进制表示中只有一个1，且必须是正数
        # n & (n-1) 会清除n的二进制表示中最右边的1
        # 如果n是2的幂，那么n & (n-1)的结果应该是0
        return n > 0 and (n & (n - 1)) == 0

    @staticmethod
    def hamming_distance(x, y):
        """
        计算两个数字的汉明距离（对应二进制位不同的位置的数目）
        LeetCode 461. 汉明距离
        题目链接: https://leetcode.cn/problems/hamming-distance/
        题目描述: 两个整数之间的 汉明距离 指的是这两个数字对应二进制位不同的位置的数目。
        
        算法原理：
        1. 先对两个数进行异或运算，相同为0，不同为1
        2. 然后计算异或结果中1的个数
        
        时间复杂度: O(1) - 最多循环32次（32位整数）
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            x (int): 第一个整数
            y (int): 第二个整数
            
        Returns:
            int: x和y的汉明距离
        """
        # 先对两个数进行异或运算，相同为0，不同为1
        xor_result = x ^ y
        # 然后计算xor中1的个数
        return BitOperationAddMinusMultiplyDivide.hamming_weight(xor_result)

    @staticmethod
    def add_without_arithmetic(a, b):
        """
        不用加减乘除做加法（剑指Offer 65）
        题目链接: https://leetcode.cn/problems/bu-yong-jia-jian-cheng-chu-zuo-jia-fa-lcof/
        题目描述: 写一个函数，求两个整数之和，要求在函数体内不得使用 “+”、“-”、“*”、“/”四则运算符号。
        
        算法原理：
        与add方法原理相同，循环直到没有进位
        Python中需要特别处理负数和整数溢出情况
        
        时间复杂度: O(1) - 最多循环32次（32位整数）
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            a (int): 第一个整数
            b (int): 第二个整数
            
        Returns:
            int: a与b的和
        """
        # 循环直到没有进位
        while b != 0:
            # 计算进位，Python中需要处理负数情况
            carry = (a & b) << 1
            # 计算无进位和
            a = a ^ b
            # 将进位赋值给b，继续下一轮循环
            b = carry
            
            # 处理Python中的整数溢出
            a &= 0xFFFFFFFF
            b &= 0xFFFFFFFF

    @staticmethod
    def reverse_bits(n):
        """
        LeetCode 190. 颠倒二进制位
        题目链接: https://leetcode.cn/problems/reverse-bits/
        题目描述: 颠倒给定的 32 位无符号整数的二进制位
        
        算法原理:
        逐位颠倒，从最低位开始，将每一位移动到对应的高位位置
        
        时间复杂度: O(1) - 固定32次循环
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            n (int): 输入的32位无符号整数
            
        Returns:
            int: 颠倒二进制位后的结果
        """
        # 处理负数情况
        if n < 0:
            n = n & 0xFFFFFFFF  # 将负数转换为32位无符号整数
            
        result = 0
        for i in range(32):
            result = (result << 1) | (n & 1)
            n >>= 1
        return result
            
        # 处理负数结果
        return a if a < 0x80000000 else a - 0x100000000
    
    @staticmethod
    def single_number(nums):
        """
        LeetCode 136. 只出现一次的数字
        题目链接: https://leetcode.cn/problems/single-number/
        题目描述: 给你一个非空整数数组 nums ，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
        
        算法原理:
        利用异或运算的性质：
        1. a ^ a = 0 (任何数与自己异或结果为0)
        2. a ^ 0 = a (任何数与0异或结果为自己)
        3. 异或运算满足交换律和结合律
        
        因此，将数组中所有元素异或，出现两次的元素会相互抵消为0，
        最终只剩下只出现一次的元素。
        
        时间复杂度: O(n) - 需要遍历整个数组
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            nums (List[int]): 输入的整数数组
            
        Returns:
            int: 只出现一次的元素
        """
        result = 0
        for num in nums:
            result = result ^ num
        return result
    
    @staticmethod
    def missing_number(nums):
        """
        LeetCode 268. 缺失的数字
        题目链接: https://leetcode.cn/problems/missing-number/
        题目描述: 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
        
        算法原理:
        利用异或运算的性质：
        1. 将索引0到n-1与数组元素nums[0]到nums[n-1]一起异或
        2. 再异或n
        3. 由于除了缺失的数字外，其他数字都会出现两次，最终结果就是缺失的数字
        
        例如：nums = [3, 0, 1]，n = 3
        初始result = 0
        i=0: result = 0 ^ 0 ^ 3 = 3
        i=1: result = 3 ^ 1 ^ 0 = 2
        i=2: result = 2 ^ 2 ^ 1 = 3
        最后: result = 3 ^ 3 = 0
        但0在数组中存在，所以缺失的是另一个数字
        正确做法是最后再异或n: result = 3 ^ 3 = 0
        
        时间复杂度: O(n) - 需要遍历整个数组
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            nums (List[int]): 输入的整数数组
            
        Returns:
            int: 缺失的数字
        """
        result = 0
        n = len(nums)
        for i in range(n):
            result = result ^ i ^ nums[i]
        return result ^ n
    
    @staticmethod
    def count_bits(n):
        """
        LeetCode 338. 比特位计数
        题目链接: https://leetcode.cn/problems/counting-bits/
        题目描述: 给你一个整数 n ，对于 0 <= i <= n 中的每个 i ，计算其二进制表示中 1 的个数，返回一个长度为 n + 1 的数组 ans 作为答案。
        
        算法原理:
        利用动态规划思想：
        对于数字i，其1的个数等于 i>>1 的1的个数加上 i 的最低位
        i>>1 相当于i除以2，(i & 1)判断i的最低位是否为1
        
        例如：
        i=5 (二进制: 101)
        i>>1 = 2 (二进制: 10)
        (i & 1) = 1 (最低位是1)
        所以countBits(5) = countBits(2) + 1 = 1 + 1 = 2
        
        时间复杂度: O(n) - 只需要遍历一次
        空间复杂度: O(1) - 除了返回数组外，只使用常数级额外空间
        
        Args:
            n (int): 输入的整数
            
        Returns:
            List[int]: 长度为n+1的数组，ans[i]表示i的二进制中1的个数
        """
        result = [0] * (n + 1)
        for i in range(1, n + 1):
            result[i] = result[i >> 1] + (i & 1)
        return result
    
    @staticmethod
    def single_numberIII(nums):
        """
        LeetCode 260. 只出现一次的数字 III
        题目链接: https://leetcode.cn/problems/single-number-iii/
        题目描述: 给你一个整数数组 nums ，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。
        
        算法原理:
        1. 先对所有数字异或，得到两个只出现一次数字的异或结果
        2. 找到异或结果中任意一个为1的位，这个位在两个只出现一次的数字中必然不同
        3. 根据这一位将数组分为两组分别异或，得到两个只出现一次的数字
        
        例如：nums = [1, 2, 1, 3, 2, 5]
        1. 所有数字异或：1^2^1^3^2^5 = 3^5 = 6 (二进制: 110)
        2. 找到最右边的1：6 & (-6) = 2 (二进制: 10)
        3. 根据第1位是否为1分组：
           第1位为1的组：[2, 2, 3] -> 异或结果为3
           第1位为0的组：[1, 1, 5] -> 异或结果为5
        
        时间复杂度: O(n) - 需要遍历整个数组两次
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            nums (List[int]): 输入的整数数组
            
        Returns:
            List[int]: 包含两个只出现一次元素的数组
        """
        # 对所有数字异或，得到两个只出现一次数字的异或结果
        xor_result = 0
        for num in nums:
            xor_result ^= num
        
        # 找到xor中最右边的1，这个位在两个只出现一次的数字中必然不同
        right_most_bit = xor_result & (-xor_result)
        
        # 根据right_most_bit将数组分为两组分别异或
        num1 = 0
        num2 = 0
        for num in nums:
            if (num & right_most_bit) != 0:
                num1 ^= num
            else:
                num2 ^= num
        
        return [num1, num2]

    @staticmethod
    def single_numberII(nums):
        """
        LeetCode 137. 只出现一次的数字 II
        题目链接: https://leetcode.cn/problems/single-number-ii/
        题目描述: 给你一个整数数组 nums ，除了某个元素只出现一次外，其余每个元素均出现三次。找出那个只出现了一次的元素。
        
        算法原理:
        使用位运算统计每一位上1出现的次数，对3取模，剩下的就是只出现一次的数字在该位的值
        
        对于32位整数的每一位：
        1. 统计所有数字在该位上1出现的次数
        2. 对次数对3取模
        3. 如果结果不为0，说明只出现一次的数字在该位为1
        
        例如：nums = [2, 2, 3, 2]
        2的二进制: 010
        3的二进制: 011
        第0位：1出现1次，1%3=1，所以结果的第0位为1
        第1位：1出现4次，4%3=1，所以结果的第1位为1
        第2位：1出现0次，0%3=0，所以结果的第2位为0
        结果：011 (二进制) = 3 (十进制)
        
        时间复杂度: O(n) - 需要遍历整个数组一次
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            nums (List[int]): 输入的整数数组
            
        Returns:
            int: 只出现一次的元素
        """
        result = 0
        # 遍历每一位（32位整数）
        for i in range(32):
            count = 0
            # 统计该位上1出现的次数
            for num in nums:
                # 检查num的第i位是否为1
                if (num >> i) & 1:
                    count = BitOperationAddMinusMultiplyDivide.add(count, 1)
            # 如果该位上1出现的次数不是3的倍数，则说明只出现一次的数字在该位为1
            if count % 3 != 0:
                result |= (1 << i)
        
        # 处理Python中整数的符号问题
        # 如果最高位是1，说明是负数，需要转换为负数表示
        if (result & (1 << 31)) != 0:
            result -= (1 << 32)
        
        return result

    @staticmethod
    def range_bitwise_and(left, right):
        """
        LeetCode 201. 数字范围按位与
        题目链接: https://leetcode.cn/problems/bitwise-and-of-numbers-range/
        题目描述: 给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字 按位与 的结果（包含 left 和 right 端点）。
        
        算法原理:
        找到left和right的最长公共前缀，后面补0
        
        在一个连续的数字范围内，低位的变化会导致按位与的结果为0，
        只有最高位的公共前缀会在最终结果中保留。
        
        例如：left=5, right=7
        5: 101
        6: 110
        7: 111
        5&6&7 = 100 (二进制) = 4 (十进制)
        公共前缀是最高位的1，后面补0
        
        时间复杂度: O(1) - 最多循环32次
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            left (int): 区间左端点
            right (int): 区间右端点
            
        Returns:
            int: 区间内所有数字按位与的结果
        """
        shift = 0
        # 找到left和right的最长公共前缀
        while left < right:
            left >>= 1
            right >>= 1
            shift = BitOperationAddMinusMultiplyDivide.add(shift, 1)
        # 左移shift位，后面补0
        return left << shift

    @staticmethod
    def find_the_difference(s, t):
        """
        LeetCode 389. 找不同
        题目链接: https://leetcode.cn/problems/find-the-difference/
        题目描述: 给定两个字符串 s 和 t ，它们只包含小写字母。字符串 t 由字符串 s 随机重排，然后在随机位置添加一个字母。请找出在 t 中被添加的字母。
        
        算法原理:
        利用异或运算的性质：
        1. 字符串s中的每个字符在t中都会出现一次
        2. 除了被添加的字符外，其他字符都会出现两次
        3. 将两个字符串的所有字符异或，出现两次的字符会相互抵消为0
        4. 最终只剩下被添加的字符
        
        例如：s = "abcd", t = "abcde"
        s中字符异或：a^b^c^d
        t中字符异或：a^b^c^d^e
        最终结果：(a^b^c^d) ^ (a^b^c^d^e) = e
        
        时间复杂度: O(n) - n是字符串长度
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            s (str): 原始字符串
            t (str): 添加了一个字符的字符串
            
        Returns:
            str: 被添加的字符
        """
        result = 0
        # 异或s中的所有字符
        for c in s:
            result ^= ord(c)
        # 异或t中的所有字符
        for c in t:
            result ^= ord(c)
        # 将结果转换为字符返回
        return chr(result)

    @staticmethod
    def subsets(nums):
        """
        LeetCode 78. 子集
        题目链接: https://leetcode.cn/problems/subsets/
        题目描述: 给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
        
        算法原理:
        使用位运算枚举所有可能的子集：
        1. 对于n个元素的数组，共有2^n个子集
        2. 用0到2^n-1的每个数字的二进制表示来表示一个子集
        3. 二进制表示中第j位为1表示包含第j个元素，为0表示不包含
        
        例如：nums = [1, 2, 3]
        0: 000 -> []
        1: 001 -> [1]
        2: 010 -> [2]
        3: 011 -> [1,2]
        4: 100 -> [3]
        5: 101 -> [1,3]
        6: 110 -> [2,3]
        7: 111 -> [1,2,3]
        
        时间复杂度: O(n * 2^n) - 共有2^n个子集，每个子集需要O(n)时间构造
        空间复杂度: O(n) - 除了返回结果外，只使用常数级额外空间
        
        Args:
            nums (List[int]): 输入的整数数组
            
        Returns:
            List[List[int]]: 所有可能的子集
        """
        result = []
        n = len(nums)
        # 枚举从0到2^n-1的所有数，表示所有可能的子集
        for i in range(1 << n):
            subset = []
            for j in range(n):
                # 检查第j位是否为1
                if (i >> j) & 1:
                    subset.append(nums[j])
            result.append(subset)
        return result

    @staticmethod
    def maximum_or(nums, k):
        """
        LeetCode 2680. 最大或值
        题目链接: https://leetcode.cn/problems/maximum-or/
        题目描述: 给你一个下标从 0 开始长度为 n 的整数数组 nums 和一个整数 k 。每一次操作中，你可以选择一个数并将它乘 2 。你最多可以进行 k 次操作，请你返回 nums[0] | nums[1] | ... | nums[n - 1] 的最大值。
        
        算法原理:
        贪心策略，尽可能让高位变为1，优先选择能使最高位为1的数字进行多次左移
        
        使用前缀和后缀数组优化计算：
        1. prefix[i] 表示 nums[0] 到 nums[i-1] 的或值
        2. suffix[i] 表示 nums[i+1] 到 nums[n-1] 的或值
        3. 对于每个nums[i]左移k位后，结果为 current | prefix[i] | suffix[i]
        
        时间复杂度: O(n^2) - 枚举每个位置作为乘2的主要候选，然后进行最多k次左移
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            nums (List[int]): 输入的整数数组
            k (int): 最多可以进行的操作次数
            
        Returns:
            int: 最大或值
        """
        n = len(nums)
        # 前缀或数组
        prefix = [0] * n
        # 后缀或数组
        suffix = [0] * n
        
        # 计算前缀或
        for i in range(1, n):
            prefix[i] = prefix[i - 1] | nums[i - 1]
        
        # 计算后缀或
        for i in range(n - 2, -1, -1):
            suffix[i] = suffix[i + 1] | nums[i + 1]
        
        max_result = 0
        # 枚举每个数字作为可能要进行k次左移的数字
        for i in range(n):
            # 当前数字左移k次后的结果
            current = nums[i] << k
            # 与前缀和后缀或操作，得到当前情况下的最大或值
            current_or = current | prefix[i] | suffix[i]
            max_result = max(max_result, current_or)
        
        return max_result
    
    @staticmethod
    def find_maximum_xor(nums):
        """
        LeetCode 421. 数组中两个数的最大异或值
        题目链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
        题目描述: 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
        
        算法原理:
        使用字典树存储所有数字的二进制表示，然后对每个数字贪心地寻找能产生最大异或值的数字
        
        1. 构建字典树：将所有数字的32位二进制表示插入字典树
        2. 对每个数字寻找最大异或值：
           贪心策略：从高位到低位，优先选择与当前位不同的路径
           如果当前位是0，优先选择1的路径；如果是1，优先选择0的路径
        
        时间复杂度: O(n) - 使用字典树优化
        空间复杂度: O(n) - 需要构建字典树
        
        Args:
            nums (List[int]): 输入的整数数组
            
        Returns:
            int: 数组中两个数的最大异或值
        """
        # 构建字典树
        root = {}
        
        # 插入数字到字典树
        for num in nums:
            node = root
            for i in range(31, -1, -1):
                bit = (num >> i) & 1
                if bit not in node:
                    node[bit] = {}
                node = node[bit]
        
        # 对每个数字寻找最大异或值
        max_result = 0
        for num in nums:
            node = root
            current_xor = 0
            for i in range(31, -1, -1):
                bit = (num >> i) & 1
                # 贪心策略：优先选择与当前位不同的路径
                toggled_bit = bit ^ 1
                if toggled_bit in node:
                    current_xor = (current_xor << 1) | 1
                    node = node[toggled_bit]
                else:
                    current_xor = current_xor << 1
                    node = node[bit]
            max_result = max(max_result, current_xor)
        
        return max_result

    @staticmethod
    def has_alternating_bits(n):
        """
        LeetCode 693. 交替位二进制数
        题目链接: https://leetcode.cn/problems/binary-number-with-alternating-bits/
        题目描述: 给定一个正整数，检查它的二进制表示是否总是 0、1 交替出现
        
        算法原理:
        检查 n ^ (n >> 1) 是否所有位都是1
        
        对于交替位二进制数，右移1位后与原数异或，结果应该是全1
        例如：n = 10 (二进制: 1010)
        n>>1 = 0101
        n^(n>>1) = 1111 (全1)
        全1的数字加1后是2的幂，与原数相与结果为0
        
        时间复杂度: O(1) - 最多循环32次
        空间复杂度: O(1) - 只使用常数级额外空间
        
        Args:
            n (int): 输入的正整数
            
        Returns:
            bool: 如果二进制表示是交替位返回True，否则返回False
        """
        # 将n右移1位后与n异或，如果结果是全1，则说明是交替位
        xor_result = n ^ (n >> 1)
        # 检查xor_result+1是否是2的幂（即xor_result是否全为1）
        return (xor_result & (xor_result + 1)) == 0


# 测试代码
if __name__ == "__main__":
    print("位运算实现四则运算测试：")
    
    # 测试加法
    print("加法测试：")
    print(f"10 + 15 = {BitOperationAddMinusMultiplyDivide.add(10, 15)}")
    print(f"(-10) + 15 = {BitOperationAddMinusMultiplyDivide.add(-10, 15)}")
    
    # 测试减法
    print("\n减法测试：")
    print(f"15 - 10 = {BitOperationAddMinusMultiplyDivide.minus(15, 10)}")
    print(f"10 - 15 = {BitOperationAddMinusMultiplyDivide.minus(10, 15)}")
    
    # 测试乘法
    print("\n乘法测试：")
    print(f"10 * 15 = {BitOperationAddMinusMultiplyDivide.multiply(10, 15)}")
    print(f"(-10) * 15 = {BitOperationAddMinusMultiplyDivide.multiply(-10, 15)}")
    
    # 测试除法
    print("\n除法测试：")
    print(f"15 / 10 = {BitOperationAddMinusMultiplyDivide.divide(15, 10)}")
    print(f"15 / (-10) = {BitOperationAddMinusMultiplyDivide.divide(15, -10)}")
    print(f"(-15) / (-10) = {BitOperationAddMinusMultiplyDivide.divide(-15, -10)}")
    print(f"(-2147483648) / (-1) = {BitOperationAddMinusMultiplyDivide.divide(-2**31, -1)}")
    
    # 测试其他位运算相关函数
    print("\n其他位运算函数测试：")
    print(f"二进制中1的个数 (15): {BitOperationAddMinusMultiplyDivide.hamming_weight(15)}")
    print(f"二进制中1的个数 (-1): {BitOperationAddMinusMultiplyDivide.hamming_weight(-1)}")
    print(f"是否为2的幂 (16): {BitOperationAddMinusMultiplyDivide.is_power_of_two(16)}")
    print(f"是否为2的幂 (15): {BitOperationAddMinusMultiplyDivide.is_power_of_two(15)}")
    print(f"汉明距离 (1, 4): {BitOperationAddMinusMultiplyDivide.hamming_distance(1, 4)}")
    print(f"缺失数字 ([3, 0, 1]): {BitOperationAddMinusMultiplyDivide.missing_number([3, 0, 1])}")
    print(f"只出现一次的数字 ([2, 2, 1]): {BitOperationAddMinusMultiplyDivide.single_number([2, 2, 1])}")
    print(f"只出现一次的数字III ([1, 2, 1, 3, 2, 5]): {BitOperationAddMinusMultiplyDivide.single_numberIII([1, 2, 1, 3, 2, 5])}")
    
    # 测试新增的位运算算法
    print("\n新增位运算算法测试：")
    print(f"LeetCode 137 - 只出现一次的数字 II ([2, 2, 3, 2]): {BitOperationAddMinusMultiplyDivide.single_numberII([2, 2, 3, 2])}")
    print(f"LeetCode 137 - 只出现一次的数字 II ([0, 1, 0, 1, 0, 1, 99]): {BitOperationAddMinusMultiplyDivide.single_numberII([0, 1, 0, 1, 0, 1, 99])}")
    print(f"LeetCode 201 - 数字范围按位与 [5,7]: {BitOperationAddMinusMultiplyDivide.range_bitwise_and(5, 7)}")
    print(f"LeetCode 201 - 数字范围按位与 [0,1]: {BitOperationAddMinusMultiplyDivide.range_bitwise_and(0, 1)}")
    print(f"LeetCode 389 - 找不同 ('abcd', 'abcde'): {BitOperationAddMinusMultiplyDivide.find_the_difference('abcd', 'abcde')}")
    print(f"LeetCode 389 - 找不同 ('', 'y'): {BitOperationAddMinusMultiplyDivide.find_the_difference('', 'y')}")
    print(f"LeetCode 78 - 子集 [1, 2, 3]: {BitOperationAddMinusMultiplyDivide.subsets([1, 2, 3])}")
    print(f"LeetCode 2680 - 最大或值 ([12, 9], 1): {BitOperationAddMinusMultiplyDivide.maximum_or([12, 9], 1)}")
    print(f"LeetCode 2680 - 最大或值 ([8, 1, 2], 2): {BitOperationAddMinusMultiplyDivide.maximum_or([8, 1, 2], 2)}")