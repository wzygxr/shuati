"""
LeetCode 887. 鸡蛋掉落
题目链接: https://leetcode.com/problems/super-egg-drop/

题目描述:
你将获得 k 个鸡蛋，并可以使用一栋从 1 到 n 共有 n 层楼的建筑。
已知存在楼层 f ，满足 0 <= f <= n ，任何从高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。
每次操作，你可以取一枚没有碎的鸡蛋并把它从任一楼层 x 扔下（满足 1 <= x <= n）。
如果鸡蛋碎了，你就不能再使用它。如果鸡蛋没碎，你可以再次使用它。
请你计算并返回要确定 f 确切的值的最小操作次数是多少？

解题思路:
本题可以使用动态规划结合高斯消元法进行优化。
定义 dp[k][m] 表示有 k 个鸡蛋，最多允许 m 次操作时，最多能测试的楼层数。
状态转移方程: dp[k][m] = dp[k][m-1] + dp[k-1][m-1] + 1

时间复杂度: O(k * log n)
空间复杂度: O(k)

工程化考虑:
1. 使用动态规划优化，避免直接高斯消元的高时间复杂度
2. 处理边界情况：当 k=1 或 n=1 时的特殊情况
3. 使用二分查找优化搜索过程
"""

import time
from typing import List

class Solution:
    """
    鸡蛋掉落问题解决方案类
    """
    
    def superEggDrop(self, k: int, n: int) -> int:
        """
        使用动态规划求解鸡蛋掉落问题
        
        Args:
            k: 鸡蛋数量
            n: 楼层数
            
        Returns:
            int: 最小操作次数
            
        时间复杂度: O(k * log n)
        空间复杂度: O(k)
        """
        # 边界情况处理
        if n == 1:
            return 1
        if k == 1:
            return n
        
        # dp[k] 表示有k个鸡蛋，最多允许m次操作时，最多能测试的楼层数
        # 使用一维数组进行优化
        dp = [0] * (k + 1)
        
        m = 0
        while dp[k] < n:
            m += 1
            # 从后往前更新，避免覆盖
            for i in range(k, 0, -1):
                dp[i] = dp[i] + dp[i - 1] + 1
        
        return m
    
    def superEggDropBinarySearch(self, k: int, n: int) -> int:
        """
        使用二分查找优化的动态规划解法
        
        Args:
            k: 鸡蛋数量
            n: 楼层数
            
        Returns:
            int: 最小操作次数
            
        时间复杂度: O(k * n * log n)
        空间复杂度: O(k * n)
        """
        # dp[k][n] 表示有k个鸡蛋，n层楼时的最小操作次数
        dp = [[0] * (n + 1) for _ in range(k + 1)]
        
        # 初始化边界条件
        for j in range(1, n + 1):
            dp[1][j] = j  # 只有一个鸡蛋时，需要逐层测试
        
        for i in range(1, k + 1):
            dp[i][1] = 1  # 只有一层楼时，只需要一次测试
        
        for i in range(2, k + 1):
            for j in range(2, n + 1):
                # 使用二分查找优化
                left, right = 1, j
                while left < right:
                    mid = left + (right - left + 1) // 2
                    # 鸡蛋碎了，测试下面 mid-1 层
                    broken = dp[i - 1][mid - 1]
                    # 鸡蛋没碎，测试上面 j-mid 层
                    not_broken = dp[i][j - mid]
                    
                    if broken > not_broken:
                        right = mid - 1
                    else:
                        left = mid
                
                dp[i][j] = max(dp[i - 1][left - 1], dp[i][j - left]) + 1
        
        return dp[k][n]
    
    def test(self):
        """
        测试方法
        """
        print("=== LeetCode 887. 鸡蛋掉落测试 ===")
        
        # 测试用例1: k=1, n=2
        result1 = self.superEggDrop(1, 2)
        print(f"测试用例1 - k=1, n=2: {result1}")
        
        # 测试用例2: k=2, n=6
        result2 = self.superEggDrop(2, 6)
        print(f"测试用例2 - k=2, n=6: {result2}")
        
        # 测试用例3: k=3, n=14
        result3 = self.superEggDrop(3, 14)
        print(f"测试用例3 - k=3, n=14: {result3}")
        
        # 性能测试: k=10, n=10000
        start_time = time.time()
        result4 = self.superEggDrop(10, 10000)
        end_time = time.time()
        print(f"性能测试 - k=10, n=10000: {result4}, 耗时: {(end_time - start_time) * 1000:.2f}ms")
        
        # 对比两种解法的结果
        print("\n对比两种解法:")
        result_opt = self.superEggDrop(2, 6)
        result_binary = self.superEggDropBinarySearch(2, 6)
        print(f"k=2, n=6 - 优化解法: {result_opt}")
        print(f"k=2, n=6 - 二分查找解法: {result_binary}")
        
        print("=== 测试完成 ===")


def complexity_analysis():
    """
    复杂度分析函数
    """
    print("\n=== 复杂度分析 ===")
    print("1. 优化解法 (superEggDrop):")
    print("   - 时间复杂度: O(k * log n)")
    print("   - 空间复杂度: O(k)")
    print("   - 优点: 效率高，适合大规模数据")
    print("   - 缺点: 状态转移理解较复杂")
    print()
    print("2. 二分查找解法 (superEggDropBinarySearch):")
    print("   - 时间复杂度: O(k * n * log n)")
    print("   - 空间复杂度: O(k * n)")
    print("   - 优点: 思路直观，易于理解")
    print("   - 缺点: 时间和空间复杂度较高")
    print()
    print("工程化建议:")
    print("- 对于小规模数据，可以使用二分查找解法，便于调试和理解")
    print("- 对于大规模数据，推荐使用优化解法")
    print("- 在实际应用中，可以根据数据规模动态选择解法")


if __name__ == "__main__":
    solution = Solution()
    solution.test()
    complexity_analysis()
    
    # 额外测试：验证算法正确性
    print("\n=== 算法正确性验证 ===")
    test_cases = [
        (1, 1, 1),   # 1个鸡蛋，1层楼
        (1, 2, 2),   # 1个鸡蛋，2层楼
        (2, 1, 1),   # 2个鸡蛋，1层楼
        (2, 6, 3),   # 2个鸡蛋，6层楼
        (3, 14, 4),  # 3个鸡蛋，14层楼
    ]
    
    all_passed = True
    for k, n, expected in test_cases:
        result = solution.superEggDrop(k, n)
        status = "通过" if result == expected else "失败"
        print(f"k={k}, n={n}: 期望={expected}, 实际={result}, 状态={status}")
        if result != expected:
            all_passed = False
    
    print(f"\n测试结果: {'所有测试通过' if all_passed else '存在测试失败'}")