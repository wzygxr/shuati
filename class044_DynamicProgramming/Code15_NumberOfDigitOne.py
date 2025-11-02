# 数字1的个数 (Number of Digit One)
# 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
# 测试链接 : https://leetcode.cn/problems/number-of-digit-one/

class Solution:
    # 方法1：暴力解法
    # 时间复杂度：O(n*log10(n)) - 需要遍历每个数字，并计算每个数字中1的个数
    # 空间复杂度：O(1) - 只使用常数额外空间
    # 问题：当n很大时效率低下
    def countDigitOne1(self, n: int) -> int:
        count = 0
        for i in range(1, n + 1):
            count += self.count_ones_in_number(i)
        return count
    
    # 计算一个数字中1的个数
    def count_ones_in_number(self, num: int) -> int:
        count = 0
        while num > 0:
            if num % 10 == 1:
                count += 1
            num //= 10
        return count
    
    # 方法2：数位DP解法
    # 时间复杂度：O(log10(n)) - 按数位处理
    # 空间复杂度：O(log10(n)) - 递归调用栈和dp字典
    def countDigitOne2(self, n: int) -> int:
        if n <= 0:
            return 0
        # 将数字转换为字符数组，方便按位处理
        digits = str(n)
        dp = {}
        return self.f(digits, 0, 0, True, dp)
    
    # 数位DP递归函数
    # digits: 数字的字符串表示
    # pos: 当前处理的位置
    # count: 已经出现的1的个数
    # limit: 是否受到原数字的限制
    # dp: 记忆化字典
    def f(self, digits: str, pos: int, count: int, limit: bool, dp: dict) -> int:
        # base case
        if pos == len(digits):
            return count
        if (pos, count, limit) in dp and not limit:
            return dp[(pos, count, limit)]
        
        ans = 0
        # 确定当前位可以填的数字范围
        max_digit = int(digits[pos]) if limit else 9
        for digit in range(max_digit + 1):
            # 递归处理下一位
            ans += self.f(digits, pos + 1, count + (1 if digit == 1 else 0), limit and digit == max_digit, dp)
        
        if not limit:
            dp[(pos, count, limit)] = ans
        return ans
    
    # 方法3：数学规律解法
    # 时间复杂度：O(log10(n)) - 按数位处理
    # 空间复杂度：O(1) - 只使用常数额外空间
    def countDigitOne3(self, n: int) -> int:
        if n <= 0:
            return 0
        count = 0
        factor = 1  # 当前位的权重（个位、十位、百位等）
        
        while factor <= n:
            # 计算当前位上1的个数
            # high: 当前位之前的高位数字
            # cur: 当前位的数字
            # low: 当前位之后的低位数字
            high = n // (factor * 10)
            cur = (n // factor) % 10
            low = n % factor
            
            if cur == 0:
                # 当前位为0，1的个数由高位决定
                count += high * factor
            elif cur == 1:
                # 当前位为1，1的个数由高位和低位共同决定
                count += high * factor + low + 1
            else:
                # 当前位大于1，1的个数由高位决定
                count += (high + 1) * factor
            
            factor *= 10
        return count

# 测试用例和性能对比
if __name__ == "__main__":
    solution = Solution()
    print("测试数字1的个数实现：")
    
    # 测试用例1
    n1 = 13
    print(f"n = {n1}")
    # 由于方法1效率较低，只在小数据上测试
    if n1 <= 1000:
        print(f"方法1 (暴力解法): {solution.countDigitOne1(n1)}")
    print(f"方法2 (数位DP): {solution.countDigitOne2(n1)}")
    print(f"方法3 (数学规律): {solution.countDigitOne3(n1)}")
    
    # 测试用例2
    n2 = 0
    print(f"\nn = {n2}")
    print(f"方法2 (数位DP): {solution.countDigitOne2(n2)}")
    print(f"方法3 (数学规律): {solution.countDigitOne3(n2)}")
    
    # 测试用例3
    n3 = 100
    print(f"\nn = {n3}")
    if n3 <= 1000:
        print(f"方法1 (暴力解法): {solution.countDigitOne1(n3)}")
    print(f"方法2 (数位DP): {solution.countDigitOne2(n3)}")
    print(f"方法3 (数学规律): {solution.countDigitOne3(n3)}")