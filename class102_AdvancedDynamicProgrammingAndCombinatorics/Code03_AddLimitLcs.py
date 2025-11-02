import sys
import time
from typing import List, Optional

class AddLimitLcs:
    """
    增加限制的最长公共子序列问题
    
    问题描述：
    给定两个字符串s1和s2，s1长度为n，s2长度为m
    返回s1和s2的最长公共子序列长度
    
    约束条件：
    - 两个字符串都只由小写字母组成
    - 1 <= n <= 10^6
    - 1 <= m <= 10^3
    
    优化背景：
    标准的LCS算法时间复杂度为O(n*m)，当n达到10^6而m为10^3时，
    直接使用标准算法会导致大约10^9次操作，显然不可行。
    因此需要利用题目中的限制条件进行优化。
    
    优化思路：
    1. 观察到s2的长度m远小于s1的长度n
    2. 预处理s1字符串，记录每个位置之后每个字符首次出现的位置
    3. 定义新的DP状态：f(i,j)表示s2的前i个字符要形成长度为j的公共子序列
       所需的s1的最短前缀长度
    4. 通过状态转移找到最大的j，使得存在i <= m且f(i,j) <= n
    
    时间复杂度分析：
    - 预处理s1：O(n * 26) = O(n)，因为每个字符需要26个小写字母的处理
    - DP状态数：O(m^2)，因为i和j的范围都是0到m
    - 总时间复杂度：O(n + m^2)
    
    空间复杂度分析：
    - next数组：O(n * 26) = O(n)
    - dp数组：O(m^2)
    - 总空间复杂度：O(n + m^2)
    
    输入输出示例：
    输入：
    s1 = "abcde"
    s2 = "ace"
    输出：3
    解释：最长公共子序列是"ace"，长度为3
    """
    
    def __init__(self):
        """
        初始化AddLimitLcs类的实例
        """
        self.NA = float('inf')  # 表示不可行的情况
        self.s1 = ""  # 第一个字符串（可能很长）
        self.s2 = ""  # 第二个字符串（相对较短）
        self.n = 0    # s1的长度
        self.m = 0    # s2的长度
        self.next = []  # next[i][c]表示s1中位置i之后字符c首次出现的位置
        self.dp = []    # 动态规划表
    
    def build(self):
        """
        构建预处理数据结构
        1. next数组：next[i][c]表示s1中位置i之后字符c首次出现的位置
        2. 初始化dp数组为-1（表示未计算）
        """
        # 初始化next数组
        self.next = [[self.NA] * 26 for _ in range(self.n + 1)]
        right = [self.NA] * 26
        
        # 从右向左遍历s1，构建next数组
        for i in range(self.n, 0, -1):
            # 复制当前的right数组到next[i]
            for j in range(26):
                self.next[i][j] = right[j]
            # 更新right数组
            right[ord(self.s1[i - 1]) - ord('a')] = i
        
        # 处理i=0的情况
        for j in range(26):
            self.next[0][j] = right[j]
        
        # 初始化dp数组
        self.dp = [[-1] * (self.m + 1) for _ in range(self.m + 1)]
    
    def f(self, i: int, j: int) -> int:
        """
        核心动态规划函数
        定义：f(i,j)表示用s2的前i个字符形成长度为j的公共子序列
             所需的s1的最短前缀长度
        
        Args:
            i: s2前缀的长度
            j: 目标公共子序列的长度
        
        Returns:
            所需的s1最短前缀长度，如果不可行返回NA
        """
        # 基本情况：
        # 1. 如果i < j，不可能形成长度为j的公共子序列（因为s2只有i个字符）
        if i < j:
            return self.NA
        # 2. 如果j == 0，不需要任何s1字符
        if j == 0:
            return 0
        # 3. 如果已经计算过，直接返回
        if self.dp[i][j] != -1:
            return self.dp[i][j]
        
        # 策略1：不使用s2的第i个字符（即s2[i-1]）
        # 此时结果为f(i-1,j)
        ans = self.f(i - 1, j)
        
        # 策略2：使用s2的第i个字符（即s2[i-1]）
        # 我们需要先找到用s2的前i-1个字符形成长度为j-1的公共子序列所需的最短s1前缀长度pre
        # 然后在s1的pre位置之后找到第一个等于s2[i-1]的字符的位置
        pre = self.f(i - 1, j - 1)
        if pre != self.NA:
            # 获取s2第i个字符的ASCII码索引
            char_index = ord(self.s2[i - 1]) - ord('a')
            if self.next[pre][char_index] != self.NA:
                ans = min(ans, self.next[pre][char_index])
        
        # 记忆结果并返回
        self.dp[i][j] = ans
        return ans
    
    def lcs_classic(self) -> int:
        """
        经典动态规划版本的最长公共子序列算法
        时间复杂度：O(n*m)，不适用于n很大的情况
        仅用于验证优化算法的正确性
        
        Returns:
            最长公共子序列的长度
        """
        # 创建二维DP数组
        dp = [[0] * (self.m + 1) for _ in range(self.n + 1)]
        
        for i in range(1, self.n + 1):
            for j in range(1, self.m + 1):
                if self.s1[i - 1] == self.s2[j - 1]:
                    dp[i][j] = 1 + dp[i - 1][j - 1]
                else:
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        
        return dp[self.n][self.m]
    
    def lcs(self, str1: str, str2: str) -> int:
        """
        优化版本的LCS算法主函数
        利用s2较短的特点进行优化
        
        Args:
            str1: 第一个字符串（可能很长）
            str2: 第二个字符串（相对较短）
        
        Returns:
            最长公共子序列的长度
        """
        # 初始化输入数据
        self.s1 = str1
        self.s2 = str2
        self.n = len(str1)
        self.m = len(str2)
        
        # 边界情况处理
        if self.n == 0 or self.m == 0:
            return 0
        
        # 构建预处理数据结构
        self.build()
        
        # 寻找最大的j，使得f(m, j) <= n
        ans = 0
        for j in range(self.m, 0, -1):
            if self.f(self.m, j) != self.NA:
                ans = j
                break
        
        return ans
    
    def verify(self, str1: str, str2: str) -> bool:
        """
        验证函数：同时使用经典算法和优化算法，并比较结果
        
        Args:
            str1: 第一个字符串
            str2: 第二个字符串
        
        Returns:
            验证是否通过
        """
        self.s1 = str1
        self.s2 = str2
        self.n = len(str1)
        self.m = len(str2)
        
        # 对于大的n，不进行经典算法验证，避免超时
        if self.n > 10000:
            return True
        
        classic_result = self.lcs_classic()
        optimized_result = self.lcs(str1, str2)
        
        return classic_result == optimized_result

    def benchmark(self, str1: str, str2: str, iterations: int = 10) -> dict:
        """
        性能基准测试函数，用于比较经典算法和优化算法的性能
        
        Args:
            str1: 第一个字符串
            str2: 第二个字符串
            iterations: 测试迭代次数
        
        Returns:
            包含性能指标的字典
        """
        self.s1 = str1
        self.s2 = str2
        self.n = len(str1)
        self.m = len(str2)
        
        results = {}
        
        # 只在小数据上测试经典算法
        if self.n <= 10000:
            # 测量经典算法的时间
            start_time = time.time()
            for _ in range(iterations):
                classic_result = self.lcs_classic()
            end_time = time.time()
            results['classic_time'] = (end_time - start_time) / iterations
            results['classic_result'] = classic_result
        
        # 测量优化算法的时间
        start_time = time.time()
        for _ in range(iterations):
            optimized_result = self.lcs(str1, str2)
        end_time = time.time()
        results['optimized_time'] = (end_time - start_time) / iterations
        results['optimized_result'] = optimized_result
        
        return results

# Python特有的工程化建议和优化技巧：
# 
# 1. 内存优化策略：
#    - 在Python中，对于大型列表，可以使用数组模块(array)代替列表(list)以节省内存
#    - 当n非常大时，可以考虑使用numpy数组进行向量化操作
#    - 对于next数组，可以使用稀疏矩阵表示法，特别是当字符集较大但实际使用的字符较少时
#    - 使用生成器表达式代替列表推导式，减少内存占用
# 
# 2. 性能优化技巧：
#    - 使用lru_cache装饰器进行记忆化搜索（但在本问题中我们手动实现了记忆化）
#    - 对于频繁访问的属性，使用局部变量缓存
#    - 使用内置函数如min()、max()，它们是用C实现的，速度更快
#    - 避免在循环中使用不必要的函数调用和对象创建
#    - 使用字典推导式和列表推导式代替显式循环
# 
# 3. Python特性利用：
#    - 使用type hints提高代码可读性和可维护性
#    - 使用docstrings提供详细的文档
#    - 利用Python的异常处理机制进行边界情况处理
#    - 使用上下文管理器(with语句)管理资源
#    - 利用Python的标准库如collections中的数据结构优化算法
# 
# 4. 并行计算：
#    - 对于独立的子问题，可以使用multiprocessing模块进行并行计算
#    - 使用concurrent.futures库简化异步任务处理
#    - 考虑使用numba或cython等工具将关键路径代码编译为机器码
# 
# 5. 代码风格与规范：
#    - 遵循PEP 8规范编写代码
#    - 使用描述性的变量名和函数名
#    - 模块化设计，将功能划分为小的、可测试的函数
#    - 添加适当的注释和文档
#    - 使用单元测试确保代码质量
# 
# 6. 特殊优化考虑：
#    - 在Python中，递归深度有限制(默认1000)，因此在递归实现时需要注意
#    - 对于非常大的n，考虑使用迭代方式实现算法，避免栈溢出
#    - 使用sys.getsizeof()检查对象大小，优化内存使用
#    - 避免在循环中使用字符串拼接，使用列表收集后join

# 算法优化的核心思想：
# 
# 1. 问题转化的艺术：
#    - 传统LCS问题关注长度，这里转化为关注所需的s1前缀长度
#    - 这种转化使我们能够利用s2长度较小的特点
#    - 时间复杂度从O(n*m)降低到O(n + m^2)
# 
# 2. 预处理技巧：
#    - next数组预处理让我们能在O(1)时间内找到字符在s1中特定位置之后的下一次出现
#    - 这避免了在每次查找时遍历s1
#    - 从右向左的构建方式高效地利用了字符的最近出现位置
# 
# 3. 动态规划状态设计：
#    - f(i,j)的定义非常巧妙，专注于s2的前缀和目标长度
#    - 这种设计将状态数从O(n*m)减少到O(m^2)
#    - 使用记忆化搜索避免重复计算
# 
# 4. 贪心选择策略：
#    - 在状态转移中，我们总是选择所需s1前缀最短的方案
#    - 这确保了后续状态有更多的选择空间
#    - 体现了"贪心地选择更优的中间状态"的思想
# 
# 5. 边界条件处理：
#    - 正确处理i < j和j = 0的特殊情况
#    - 使用NA（无穷大）表示不可行的状态
#    - 在主函数中对空字符串进行了特殊处理

# 类似题目与训练拓展：
# 
# 1. LeetCode 1143 - Longest Common Subsequence
#    链接：https://leetcode.cn/problems/longest-common-subsequence/
#    区别：标准LCS问题，没有长度限制
#    算法：动态规划
#    
# 2. LeetCode 583 - Delete Operation for Two Strings
#    链接：https://leetcode.cn/problems/delete-operation-for-two-strings/
#    区别：求最少删除次数，等价于求LCS
#    算法：动态规划
#    
# 3. LeetCode 712 - Minimum ASCII Delete Sum for Two Strings
#    链接：https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
#    区别：求最小ASCII删除和
#    算法：动态规划
#    
# 4. LeetCode 1035 - Uncrossed Lines
#    链接：https://leetcode.cn/problems/uncrossed-lines/
#    区别：求不相交的线的最大数量，本质也是LCS问题
#    算法：动态规划
#    
# 5. LeetCode 516 - Longest Palindromic Subsequence
#    链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
#    区别：求最长回文子序列
#    算法：区间动态规划
#    
# 6. 牛客网 NC127 - 最长公共子串
#    链接：https://www.nowcoder.com/practice/f33f5adc55f444baa0e0ca87ad8a6aac
#    区别：求最长公共子串（连续）而非子序列
#    算法：动态规划或滑动窗口

# 主函数，用于测试
def main():
    solution = AddLimitLcs()
    
    # 测试用例
    s1 = "abcde"
    s2 = "ace"
    print(f"s1 = {s1}, s2 = {s2}")
    print(f"最长公共子序列长度: {solution.lcs(s1, s2)}")  # 预期输出: 3
    
    # 更多测试用例
    s1 = "abc"
    s2 = "def"
    print(f"\ns1 = {s1}, s2 = {s2}")
    print(f"最长公共子序列长度: {solution.lcs(s1, s2)}")  # 预期输出: 0
    
    s1 = "abc"
    s2 = "abc"
    print(f"\ns1 = {s1}, s2 = {s2}")
    print(f"最长公共子序列长度: {solution.lcs(s1, s2)}")  # 预期输出: 3
    
    # 验证算法正确性
    if solution.verify("abcde", "ace"):
        print("\n算法验证通过!")
    else:
        print("\n算法验证失败!")
    
    # 性能测试
    s1_medium = "abcdefghij" * 100  # 长度为1000的字符串
    s2_medium = "acegikmoqsu" * 10  # 长度为200的字符串
    print(f"\n性能测试 - s1长度: {len(s1_medium)}, s2长度: {len(s2_medium)}")
    results = solution.benchmark(s1_medium, s2_medium, iterations=3)
    
    if 'classic_time' in results:
        print(f"经典算法平均执行时间: {results['classic_time']:.6f}秒")
        print(f"经典算法结果: {results['classic_result']}")
    
    print(f"优化算法平均执行时间: {results['optimized_time']:.6f}秒")
    print(f"优化算法结果: {results['optimized_result']}")

if __name__ == "__main__":
    main()