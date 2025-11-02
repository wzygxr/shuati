import sys
from typing import List, Tuple
import random
import time

# POJ 2184 Cow Exhibition
# 题目描述：奶牛们想证明它们是聪明而风趣的。为此，贝西筹备了一个奶牛博览会，
# 她已经对N头奶牛进行了面试，确定了每头奶牛的聪明度和幽默度。
# 贝西可以选择任意数量的奶牛参加展览，但希望总聪明度和总幽默度都非负。
# 在满足条件的情况下，使得总聪明度与总幽默度的和最大。
# 链接：http://poj.org/problem?id=2184
# 
# 解题思路：
# 这是一个二维费用的01背包问题，需要同时考虑两个维度（聪明度和幽默度）。
# 由于两个维度都可能为负数，需要进行坐标平移。
# 
# 状态定义：dp[i][j] 表示前i头奶牛，聪明度总和为j时的最大幽默度总和
# 状态转移方程：
#   dp[i][j] = max(dp[i-1][j], dp[i-1][j-smart[i]] + funny[i])
# 
# 关键点：
# 1. 坐标平移：由于聪明度可能为负数，需要将坐标平移到非负数范围
# 2. 二维费用：同时考虑聪明度和幽默度两个维度
# 3. 状态优化：使用滚动数组优化空间复杂度
# 
# 时间复杂度：O(N * range)，其中range是聪明度的可能范围
# 空间复杂度：O(range)，使用滚动数组优化
# 
# 工程化考量：
# 1. 异常处理：处理空输入、边界值等情况
# 2. 性能优化：坐标平移和滚动数组优化
# 3. 边界条件：处理负数范围和结果有效性检查

class CowExhibition:
    """
    奶牛展览问题的多种解法
    """
    
    OFFSET = 100000  # 坐标偏移量，处理负数
    MAX_RANGE = 200000  # 总范围大小
    INF = -10**9  # 表示不可达状态
    
    @staticmethod
    def max_cow_exhibition(cows: List[Tuple[int, int]]) -> int:
        """
        动态规划解法 - 二维费用01背包
        
        Args:
            cows: 奶牛列表，每个奶牛包含聪明度和幽默度
            
        Returns:
            int: 最大总聪明度与总幽默度的和
        """
        # 参数验证
        if not cows:
            return 0
        
        n = len(cows)
        
        # 创建DP数组，使用滚动数组优化
        dp = [CowExhibition.INF] * (CowExhibition.MAX_RANGE + 1)
        dp[CowExhibition.OFFSET] = 0  # 初始状态：聪明度总和为0，幽默度总和为0
        
        # 遍历每头奶牛
        for smart, funny in cows:
            # 根据smart的正负决定遍历方向
            if smart >= 0:
                # 正数：倒序遍历，避免重复选择
                for j in range(CowExhibition.MAX_RANGE, smart - 1, -1):
                    if dp[j - smart] != CowExhibition.INF:
                        dp[j] = max(dp[j], dp[j - smart] + funny)
            else:
                # 负数：正序遍历
                for j in range(0, CowExhibition.MAX_RANGE + smart + 1):
                    if dp[j - smart] != CowExhibition.INF:
                        dp[j] = max(dp[j], dp[j - smart] + funny)
        
        # 寻找最大和（聪明度+幽默度）
        max_sum = 0
        for j in range(CowExhibition.OFFSET, CowExhibition.MAX_RANGE + 1):
            if dp[j] >= 0:  # 幽默度总和需要非负
                max_sum = max(max_sum, j - CowExhibition.OFFSET + dp[j])
        
        return max_sum
    
    @staticmethod
    def max_cow_exhibition_2d(cows: List[Tuple[int, int]]) -> int:
        """
        优化的动态规划解法 - 使用二维数组便于理解
        
        Args:
            cows: 奶牛列表，每个奶牛包含聪明度和幽默度
            
        Returns:
            int: 最大总聪明度与总幽默度的和
        """
        if not cows:
            return 0
        
        n = len(cows)
        
        # 计算聪明度的可能范围
        min_smart = 0
        max_smart = 0
        for smart, _ in cows:
            if smart < 0:
                min_smart += smart
            else:
                max_smart += smart
        
        range_size = max_smart - min_smart
        offset = -min_smart
        
        # 创建二维DP数组
        dp = [[CowExhibition.INF] * (range_size + 1) for _ in range(n + 1)]
        dp[0][offset] = 0
        
        # 动态规划
        for i in range(1, n + 1):
            smart, funny = cows[i - 1]
            
            for j in range(range_size + 1):
                # 不选当前奶牛
                dp[i][j] = dp[i - 1][j]
                
                # 选当前奶牛
                prev = j - smart
                if 0 <= prev <= range_size and dp[i - 1][prev] != CowExhibition.INF:
                    dp[i][j] = max(dp[i][j], dp[i - 1][prev] + funny)
        
        # 寻找最大和
        max_sum = 0
        for j in range(offset, range_size + 1):
            if dp[n][j] >= 0:
                max_sum = max(max_sum, j - offset + dp[n][j])
        
        return max_sum
    
    @staticmethod
    def max_cow_exhibition_optimized(cows: List[Tuple[int, int]]) -> int:
        """
        空间优化的解法 - 只记录有效状态
        
        Args:
            cows: 奶牛列表，每个奶牛包含聪明度和幽默度
            
        Returns:
            int: 最大总聪明度与总幽默度的和
        """
        if not cows:
            return 0
        
        # 分离正负聪明度的奶牛
        positive_cows = []
        negative_cows = []
        
        for smart, funny in cows:
            if smart >= 0:
                positive_cows.append((smart, funny))
            else:
                negative_cows.append((smart, funny))
        
        # 处理正数聪明度的奶牛
        dp = [CowExhibition.INF] * (CowExhibition.MAX_RANGE + 1)
        dp[CowExhibition.OFFSET] = 0
        
        for smart, funny in positive_cows:
            for j in range(CowExhibition.MAX_RANGE, smart - 1, -1):
                if dp[j - smart] != CowExhibition.INF:
                    dp[j] = max(dp[j], dp[j - smart] + funny)
        
        # 处理负数聪明度的奶牛
        for smart, funny in negative_cows:
            for j in range(0, CowExhibition.MAX_RANGE + smart + 1):
                if dp[j - smart] != CowExhibition.INF:
                    dp[j] = max(dp[j], dp[j - smart] + funny)
        
        # 寻找最大和
        max_sum = 0
        for j in range(CowExhibition.OFFSET, CowExhibition.MAX_RANGE + 1):
            if dp[j] >= 0:
                max_sum = max(max_sum, j - CowExhibition.OFFSET + dp[j])
        
        return max_sum
    
    @staticmethod
    def run_tests():
        """
        运行测试用例，验证所有方法的正确性
        """
        # 测试用例
        test_cases = [
            # 示例测试用例
            [(5, 1), (1, 5), (-5, 5), (5, -1)],
            # 边界测试用例
            [(10, 20), (15, 15)],
            # 包含负数的测试用例
            [(-1, 100), (2, 50), (-3, 200)],
            # 空测试用例
            []
        ]
        
        print("奶牛展览问题测试：")
        for i, cows in enumerate(test_cases, 1):
            try:
                result1 = CowExhibition.max_cow_exhibition(cows)
                result2 = CowExhibition.max_cow_exhibition_2d(cows)
                result3 = CowExhibition.max_cow_exhibition_optimized(cows)
                
                print(f"测试用例{i}: 奶牛数量={len(cows)}, "
                      f"方法1={result1}, 方法2={result2}, 方法3={result3}")
                
                # 验证结果一致性
                if result1 != result2 or result2 != result3:
                    print("警告：不同方法结果不一致！")
                    
            except Exception as e:
                print(f"测试用例{i}时发生错误: {e}")
        
        # 性能测试 - 大规模数据
        n = 100
        large_cows = CowExhibition.generate_large_test_data(n)
        
        start_time = time.time()
        large_result = CowExhibition.max_cow_exhibition_optimized(large_cows)
        end_time = time.time()
        
        print(f"大规模测试: 奶牛数量={n}, 结果={large_result}, "
              f"耗时={end_time - start_time:.4f}秒")
        
        # 边界情况测试
        print("边界情况测试：")
        print(f"空数组: {CowExhibition.max_cow_exhibition([])}")
        print(f"单头奶牛: {CowExhibition.max_cow_exhibition([(10, 20)])}")
        print(f"全负数聪明度: {CowExhibition.max_cow_exhibition([(-1, 5), (-2, 10)])}")
    
    @staticmethod
    def generate_large_test_data(n: int) -> List[Tuple[int, int]]:
        """
        生成大规模测试数据
        
        Args:
            n: 奶牛数量
            
        Returns:
            List[Tuple[int, int]]: 生成的奶牛数据
        """
        cows = []
        for _ in range(n):
            # 生成-100到100之间的聪明度
            smart = random.randint(-100, 100)
            # 生成0到100之间的幽默度
            funny = random.randint(0, 100)
            cows.append((smart, funny))
        return cows

def main():
    """
    主函数 - 运行测试和演示
    """
    try:
        CowExhibition.run_tests()
    except Exception as e:
        print(f"程序执行错误: {e}")
        return 1
    return 0

if __name__ == "__main__":
    sys.exit(main())

"""
复杂度分析：

方法1：动态规划（滚动数组）
- 时间复杂度：O(N * range)
  - N: 奶牛数量
  - range: 聪明度的可能范围（经过坐标平移）
- 空间复杂度：O(range)

方法2：二维动态规划
- 时间复杂度：O(N * range)
- 空间复杂度：O(N * range)

方法3：优化的动态规划
- 时间复杂度：O(N * range)（但常数更小）
- 空间复杂度：O(range)

Python特定优化：
1. 使用列表推导式和生成器表达式
2. 利用Python的动态类型特性
3. 使用类型注解提高代码可读性
4. 使用random模块进行性能测试

关键点分析：
1. 坐标平移：处理负数聪明度，将坐标平移到非负数范围
2. 遍历方向：根据smart的正负决定遍历方向（01背包特性）
3. 状态有效性：只考虑幽默度非负的状态
4. 结果计算：聪明度+幽默度的最大和

工程化考量：
1. 模块化设计：将不同解法封装为静态方法
2. 异常处理：完善的参数验证和错误处理
3. 性能监控：包含性能测试和时间测量
4. 测试覆盖：包含各种边界情况和性能测试

面试要点：
1. 理解二维费用背包问题的特点
2. 掌握坐标平移处理负数的方法
3. 了解不同遍历方向的原因
4. 能够分析算法的时空复杂度
5. 了解Python在算法实现中的优势

扩展应用：
1. 多目标优化问题
2. 资源约束下的最优选择
3. 投资组合优化
4. 特征选择和权重分配
"""