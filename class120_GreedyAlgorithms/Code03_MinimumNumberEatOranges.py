"""
吃掉N个橘子的最少天数 - 贪心算法 + 记忆化搜索解决方案（Python实现）

题目描述：
厨房里总共有 n 个橘子，你决定每一天选择如下方式之一吃这些橘子
1）吃掉一个橘子
2) 如果剩余橘子数 n 能被 2 整除，那么你可以吃掉 n/2 个橘子
3) 如果剩余橘子数 n 能被 3 整除，那么你可以吃掉 2*(n/3) 个橘子
每天你只能从以上 3 种方案中选择一种方案
请你返回吃掉所有 n 个橘子的最少天数

测试链接：https://leetcode.cn/problems/minimum-number-of-days-to-eat-n-oranges/

算法思想：
贪心算法 + 记忆化搜索（动态规划）
1. 优先使用按比例吃橘子的方法（方法2和3），因为这样能更快减少橘子数量
2. 对于每个n，考虑两种可能性：
   - 先吃到2的倍数，然后吃掉一半
   - 先吃到3的倍数，然后吃掉三分之二
3. 选择天数最少的方法

时间复杂度分析：
O(logn) - 由于每次递归都会将问题规模减半或减为三分之一

空间复杂度分析：
O(logn) - 递归深度为logn，记忆化存储也需要O(logn)空间

是否为最优解：
是，这是解决该问题的最优解

工程化考量：
1. 边界条件处理：处理n=0和n=1的特殊情况
2. 记忆化优化：避免重复计算
3. 递归深度：使用记忆化搜索控制递归深度
4. 可读性：使用清晰的变量命名和详细的注释

贪心策略证明：
由于按比例吃橘子（方法2和3）能更快减少橘子数量，所以应该优先考虑这两种方法
对于每个n，选择能最快减少橘子数量的方法
"""

from functools import lru_cache

class Code03_MinimumNumberEatOranges:
    
    @staticmethod
    @lru_cache(maxsize=None)
    def min_days(n: int) -> int:
        """
        计算吃掉n个橘子的最少天数
        
        Args:
            n: 橘子数量
            
        Returns:
            最少天数
            
        算法步骤：
        1. 基础情况：n=0或1时，直接返回n
        2. 使用lru_cache自动进行记忆化
        3. 考虑两种贪心策略：
           - 先吃到2的倍数，然后吃掉一半
           - 先吃到3的倍数，然后吃掉三分之二
        4. 选择天数最少的方法
        
        贪心策略解释：
        方法2和3能更快减少橘子数量，所以应该优先考虑
        但需要先通过方法1吃到合适的倍数
        """
        # 基础情况处理
        if n <= 1:
            return n
        
        # 贪心策略1：先吃到2的倍数，然后吃掉一半
        # 需要先吃掉 n % 2 个橘子（方法1），然后使用方法2吃掉一半
        option1 = n % 2 + 1 + Code03_MinimumNumberEatOranges.min_days(n // 2)
        
        # 贪心策略2：先吃到3的倍数，然后吃掉三分之二
        # 需要先吃掉 n % 3 个橘子（方法1），然后使用方法3吃掉三分之二
        option2 = n % 3 + 1 + Code03_MinimumNumberEatOranges.min_days(n // 3)
        
        # 选择天数最少的方法
        return min(option1, option2)
    
    @staticmethod
    def debug_min_days(n: int, depth: int = 0) -> int:
        """
        调试版本：打印计算过程中的中间结果
        
        Args:
            n: 橘子数量
            depth: 递归深度（用于缩进显示）
            
        Returns:
            最少天数
        """
        indent = "  " * depth
        print(f"{indent}计算吃掉 {n} 个橘子的最少天数:")
        
        if n <= 1:
            print(f"{indent}基础情况: n={n}, 返回 {n}")
            return n
        
        # 策略1分析
        remainder2 = n % 2
        half = n // 2
        print(f"{indent}策略1 - 吃到2的倍数:")
        print(f"{indent}  需要先吃掉 {remainder2} 个橘子")
        print(f"{indent}  然后吃掉一半 ({half} 个橘子)")
        option1 = remainder2 + 1 + Code03_MinimumNumberEatOranges.debug_min_days(half, depth + 1)
        print(f"{indent}  策略1总天数: {remainder2} + 1 + minDays({half}) = {option1}")
        
        # 策略2分析
        remainder3 = n % 3
        third = n // 3
        print(f"{indent}策略2 - 吃到3的倍数:")
        print(f"{indent}  需要先吃掉 {remainder3} 个橘子")
        print(f"{indent}  然后吃掉三分之二 ({third} 个橘子)")
        option2 = remainder3 + 1 + Code03_MinimumNumberEatOranges.debug_min_days(third, depth + 1)
        print(f"{indent}  策略2总天数: {remainder3} + 1 + minDays({third}) = {option2}")
        
        ans = min(option1, option2)
        print(f"{indent}选择较小值: min({option1}, {option2}) = {ans}")
        
        return ans
    
    @staticmethod
    def test_min_days():
        """
        测试函数：验证吃橘子算法的正确性
        """
        print("吃掉N个橘子的最少天数算法测试开始")
        print("=============================")
        
        # 清空缓存
        Code03_MinimumNumberEatOranges.min_days.cache_clear()
        
        # 测试用例1: n = 10
        result1 = Code03_MinimumNumberEatOranges.min_days(10)
        print("输入: n = 10")
        print("输出:", result1)
        print("预期: 4")
        print("✓ 通过" if result1 == 4 else "✗ 失败")
        print()
        
        # 清空缓存
        Code03_MinimumNumberEatOranges.min_days.cache_clear()
        
        # 测试用例2: n = 6
        result2 = Code03_MinimumNumberEatOranges.min_days(6)
        print("输入: n = 6")
        print("输出:", result2)
        print("预期: 3")
        print("✓ 通过" if result2 == 3 else "✗ 失败")
        print()
        
        # 清空缓存
        Code03_MinimumNumberEatOranges.min_days.cache_clear()
        
        # 测试用例3: n = 1
        result3 = Code03_MinimumNumberEatOranges.min_days(1)
        print("输入: n = 1")
        print("输出:", result3)
        print("预期: 1")
        print("✓ 通过" if result3 == 1 else "✗ 失败")
        print()
        
        # 清空缓存
        Code03_MinimumNumberEatOranges.min_days.cache_clear()
        
        # 测试用例4: n = 56
        result4 = Code03_MinimumNumberEatOranges.min_days(56)
        print("输入: n = 56")
        print("输出:", result4)
        print("预期: 6")
        print("✓ 通过" if result4 == 6 else "✗ 失败")
        print()
        
        print("测试结束")
    
    @staticmethod
    def performance_test():
        """
        性能测试：测试算法在大规模数据下的表现
        """
        import time
        
        print("性能测试开始")
        print("============")
        
        # 清空缓存
        Code03_MinimumNumberEatOranges.min_days.cache_clear()
        
        start_time = time.time()
        result = Code03_MinimumNumberEatOranges.min_days(1000000)
        end_time = time.time()
        
        print("输入: n = 1000000")
        print("输出:", result)
        print("执行时间:", end_time - start_time, "秒")
        print("性能测试结束")
    
    @staticmethod
    def compare_with_naive(n: int) -> None:
        """
        与暴力解法对比，展示贪心算法的优势
        
        Args:
            n: 橘子数量
        """
        print(f"对比贪心算法与暴力解法 (n={n}):")
        print("贪心算法结果:", Code03_MinimumNumberEatOranges.min_days(n))
        
        # 暴力解法（仅用于小规模对比）
        def brute_force(n: int) -> int:
            if n <= 1:
                return n
            # 暴力尝试所有三种方法
            option1 = 1 + brute_force(n - 1)  # 方法1
            option2 = 10**9  # 使用大数代替无穷大
            option3 = 10**9  # 使用大数代替无穷大
            
            if n % 2 == 0:
                option2 = 1 + brute_force(n // 2)  # 方法2
            if n % 3 == 0:
                option3 = 1 + brute_force(n // 3)  # 方法3
                
            return min(option1, option2, option3)
        
        if n <= 20:  # 只对小规模数据进行暴力计算
            print("暴力解法结果:", brute_force(n))
        else:
            print("暴力解法: 数据规模太大，无法计算")


def main():
    """
    主函数：运行测试
    """
    print("吃掉N个橘子的最少天数 - 贪心算法 + 记忆化搜索解决方案（Python实现）")
    print("===========================================================")
    
    # 运行基础测试
    Code03_MinimumNumberEatOranges.test_min_days()
    
    print("\n调试模式示例:")
    print("对 n = 10 进行调试跟踪:")
    debug_result = Code03_MinimumNumberEatOranges.debug_min_days(10)
    print("最终结果:", debug_result)
    
    print("\n算法分析:")
    print("- 时间复杂度: O(logn) - 由于每次递归都会将问题规模减半或减为三分之一")
    print("- 空间复杂度: O(logn) - 递归深度为logn，记忆化存储也需要O(logn)空间")
    print("- 贪心策略: 优先使用按比例吃橘子的方法，能更快减少橘子数量")
    print("- 最优性: 这种贪心策略能够得到全局最优解")
    print("- Python特性: 使用@lru_cache自动进行记忆化")
    
    # 可选：运行性能测试
    # print("\n性能测试:")
    # Code03_MinimumNumberEatOranges.performance_test()
    
    # 可选：与暴力解法对比
    # print("\n算法对比:")
    # Code03_MinimumNumberEatOranges.compare_with_naive(10)


if __name__ == "__main__":
    main()