# 斐波那契数
# 斐波那契数 （通常用 F(n) 表示）形成的序列称为 斐波那契数列
# 该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。
# 也就是：F(0) = 0，F(1) = 1
# F(n) = F(n - 1) + F(n - 2)，其中 n > 1
# 给定 n ，请计算 F(n)
# 测试链接 : https://leetcode.cn/problems/fibonacci-number/
# 注意：最优解来自矩阵快速幂，时间复杂度可以做到O(log n)
# 后续课程一定会讲述！本节课不涉及！

class Solution:
    # 方法1：暴力递归解法
    # 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    # 空间复杂度：O(n) - 递归调用栈的深度
    # 问题：存在大量重复计算，效率低下
    def fib1(self, n: int) -> int:
        return self.f1(n)
    
    def f1(self, i: int) -> int:
        if i == 0:
            return 0
        if i == 1:
            return 1
        return self.f1(i - 1) + self.f1(i - 2)
    
    # 方法2：记忆化搜索（自顶向下动态规划）
    # 时间复杂度：O(n) - 每个状态只计算一次
    # 空间复杂度：O(n) - dp字典和递归调用栈
    # 优化：通过缓存已经计算的结果避免重复计算
    def fib2(self, n: int) -> int:
        dp = {}
        return self.f2(n, dp)
    
    def f2(self, i: int, dp: dict) -> int:
        if i == 0:
            return 0
        if i == 1:
            return 1
        if i in dp:
            return dp[i]
        ans = self.f2(i - 1, dp) + self.f2(i - 2, dp)
        dp[i] = ans
        return ans
    
    # 方法3：动态规划（自底向上）
    # 时间复杂度：O(n) - 从底向上计算每个状态
    # 空间复杂度：O(n) - dp数组存储所有状态
    # 优化：避免了递归调用的开销
    def fib3(self, n: int) -> int:
        if n == 0:
            return 0
        if n == 1:
            return 1
        dp = [0] * (n + 1)
        dp[1] = 1
        for i in range(2, n + 1):
            dp[i] = dp[i - 1] + dp[i - 2]
        return dp[n]
    
    # 方法4：空间优化的动态规划
    # 时间复杂度：O(n) - 仍然需要计算所有状态
    # 空间复杂度：O(1) - 只保存必要的前两个状态值
    # 优化：只保存必要的状态，大幅减少空间使用
    def fib4(self, n: int) -> int:
        if n == 0:
            return 0
        if n == 1:
            return 1
        last_last, last = 0, 1
        for i in range(2, n + 1):
            cur = last_last + last
            last_last = last
            last = cur
        return last

# 测试用例和性能对比
if __name__ == "__main__":
    solution = Solution()
    print("测试斐波那契数列实现：")
    
    # 测试小数值
    n = 10
    print(f"n = {n}")
    print(f"方法1 (暴力递归): {solution.fib1(n)}")
    print(f"方法2 (记忆化搜索): {solution.fib2(n)}")
    print(f"方法3 (动态规划): {solution.fib3(n)}")
    print(f"方法4 (空间优化): {solution.fib4(n)}")
    
    import time
    
    # 性能测试（只测试高效方法）
    n = 30
    
    start = time.time()
    result3 = solution.fib3(n)
    end = time.time()
    print(f"\n动态规划方法计算 fib({n}) = {result3}，耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result4 = solution.fib4(n)
    end = time.time()
    print(f"空间优化方法计算 fib({n}) = {result4}，耗时: {(end - start) * 1000:.2f}ms")