import time
import random
from typing import List

class Code37_Candy:
    """
    分发糖果
    
    题目描述：
    老师想给孩子们分发糖果，有 N 个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分。
    你需要按照以下要求，给这些孩子分发糖果：
    1. 每个孩子至少分配到 1 个糖果。
    2. 相邻的孩子中，评分高的孩子必须获得更多的糖果。
    
    来源：LeetCode 135
    链接：https://leetcode.cn/problems/candy/
    
    算法思路：
    使用贪心算法：
    1. 从左到右遍历，保证右边评分高的孩子糖果更多
    2. 从右到左遍历，保证左边评分高的孩子糖果更多
    3. 取两次遍历的最大值
    
    时间复杂度：O(n) - 两次遍历数组
    空间复杂度：O(n) - 存储糖果分配
    
    关键点分析：
    - 贪心策略：分别处理左右关系
    - 两次遍历：确保两个方向的条件都满足
    - 边界处理：处理数组边界情况
    
    工程化考量：
    - 输入验证：检查数组是否为空
    - 性能优化：避免不必要的计算
    - 可读性：清晰的变量命名和注释
    """
    
    @staticmethod
    def candy(ratings: List[int]) -> int:
        """
        分发糖果的最小数量
        
        Args:
            ratings: 孩子的评分数组
            
        Returns:
            int: 最少需要的糖果数量
        """
        # 输入验证
        if not ratings:
            return 0
        if len(ratings) == 1:
            return 1
        
        n = len(ratings)
        candies = [1] * n  # 每个孩子至少1个糖果
        
        # 从左到右遍历：保证右边评分高的孩子糖果更多
        for i in range(1, n):
            if ratings[i] > ratings[i - 1]:
                candies[i] = candies[i - 1] + 1
        
        # 从右到左遍历：保证左边评分高的孩子糖果更多
        for i in range(n - 2, -1, -1):
            if ratings[i] > ratings[i + 1]:
                candies[i] = max(candies[i], candies[i + 1] + 1)
        
        # 计算总糖果数
        return sum(candies)
    
    @staticmethod
    def candy_one_pass(ratings: List[int]) -> int:
        """
        另一种实现：一次遍历法
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not ratings:
            return 0
        if len(ratings) == 1:
            return 1
        
        n = len(ratings)
        total = 1  # 第一个孩子至少1个糖果
        up = 0     # 上升序列长度
        down = 0   # 下降序列长度
        peak = 0   # 峰值位置
        
        for i in range(1, n):
            if ratings[i] > ratings[i - 1]:
                up += 1
                down = 0
                peak = up
                total += up + 1
            elif ratings[i] == ratings[i - 1]:
                up = 0
                down = 0
                peak = 0
                total += 1
            else:
                up = 0
                down += 1
                total += down + (1 if down > peak else 0)
        
        return total
    
    @staticmethod
    def candy_brute_force(ratings: List[int]) -> int:
        """
        暴力解法：模拟分配过程
        时间复杂度：O(n^2)
        空间复杂度：O(n)
        """
        if not ratings:
            return 0
        
        n = len(ratings)
        candies = [1] * n
        
        changed = True
        while changed:
            changed = False
            for i in range(n):
                # 检查左边邻居
                if i > 0 and ratings[i] > ratings[i - 1] and candies[i] <= candies[i - 1]:
                    candies[i] = candies[i - 1] + 1
                    changed = True
                # 检查右边邻居
                if i < n - 1 and ratings[i] > ratings[i + 1] and candies[i] <= candies[i + 1]:
                    candies[i] = candies[i + 1] + 1
                    changed = True
        
        return sum(candies)
    
    @staticmethod
    def validate_candy(ratings: List[int], result: int) -> bool:
        """
        验证函数：检查糖果分配是否满足条件
        
        Args:
            ratings: 评分数组
            result: 糖果总数
            
        Returns:
            bool: 分配是否有效
        """
        if not ratings:
            return result == 0
        
        # 重新计算糖果分配进行验证
        n = len(ratings)
        candies = [1] * n
        
        # 从左到右
        for i in range(1, n):
            if ratings[i] > ratings[i - 1]:
                candies[i] = candies[i - 1] + 1
        
        # 从右到左
        for i in range(n - 2, -1, -1):
            if ratings[i] > ratings[i + 1]:
                candies[i] = max(candies[i], candies[i + 1] + 1)
        
        total = sum(candies)
        
        # 验证分配是否满足条件
        for i in range(n):
            if i > 0 and ratings[i] > ratings[i - 1] and candies[i] <= candies[i - 1]:
                return False
            if i < n - 1 and ratings[i] > ratings[i + 1] and candies[i] <= candies[i + 1]:
                return False
        
        return total == result
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 分发糖果测试 ===")
        
        # 测试用例1: [1,0,2] -> 5
        ratings1 = [1, 0, 2]
        print(f"测试用例1: {ratings1}")
        result1 = Code37_Candy.candy(ratings1)
        result2 = Code37_Candy.candy_one_pass(ratings1)
        print(f"方法1结果: {result1}")  # 5
        print(f"方法2结果: {result2}")  # 5
        print(f"验证: {Code37_Candy.validate_candy(ratings1, result1)}")
        
        # 测试用例2: [1,2,2] -> 4
        ratings2 = [1, 2, 2]
        print(f"\n测试用例2: {ratings2}")
        result1 = Code37_Candy.candy(ratings2)
        result2 = Code37_Candy.candy_one_pass(ratings2)
        print(f"方法1结果: {result1}")  # 4
        print(f"方法2结果: {result2}")  # 4
        print(f"验证: {Code37_Candy.validate_candy(ratings2, result1)}")
        
        # 测试用例3: [1,3,2,2,1] -> 7
        ratings3 = [1, 3, 2, 2, 1]
        print(f"\n测试用例3: {ratings3}")
        result1 = Code37_Candy.candy(ratings3)
        result2 = Code37_Candy.candy_one_pass(ratings3)
        print(f"方法1结果: {result1}")  # 7
        print(f"方法2结果: {result2}")  # 7
        print(f"验证: {Code37_Candy.validate_candy(ratings3, result1)}")
        
        # 测试用例4: [1] -> 1
        ratings4 = [1]
        print(f"\n测试用例4: {ratings4}")
        result1 = Code37_Candy.candy(ratings4)
        result2 = Code37_Candy.candy_one_pass(ratings4)
        print(f"方法1结果: {result1}")  # 1
        print(f"方法2结果: {result2}")  # 1
        print(f"验证: {Code37_Candy.validate_candy(ratings4, result1)}")
        
        # 边界测试：空数组
        ratings5 = []
        print(f"\n测试用例5: {ratings5}")
        result1 = Code37_Candy.candy(ratings5)
        result2 = Code37_Candy.candy_one_pass(ratings5)
        print(f"方法1结果: {result1}")  # 0
        print(f"方法2结果: {result2}")  # 0
        print(f"验证: {Code37_Candy.validate_candy(ratings5, result1)}")
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        n = 10000
        ratings = [random.randint(0, 9) for _ in range(n)]
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code37_Candy.candy(ratings)
        end_time1 = time.time()
        print(f"方法1执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"结果: {result1}")
        print(f"验证: {Code37_Candy.validate_candy(ratings, result1)}")
        
        start_time2 = time.time()
        result2 = Code37_Candy.candy_one_pass(ratings)
        end_time2 = time.time()
        print(f"方法2执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"结果: {result2}")
        print(f"验证: {Code37_Candy.validate_candy(ratings, result2)}")
        
        # 暴力解法太慢，只测试小规模数据
        small_ratings = ratings[:100]
        start_time3 = time.time()
        result3 = Code37_Candy.candy_brute_force(small_ratings)
        end_time3 = time.time()
        print(f"方法3执行时间（小规模）: {(end_time3 - start_time3) * 1000:.2f} 毫秒")
        print(f"结果: {result3}")
        print(f"验证: {Code37_Candy.validate_candy(small_ratings, result3)}")
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("方法1（两次遍历）:")
        print("- 时间复杂度: O(n)")
        print("  - 两次遍历数组")
        print("  - 总体线性时间复杂度")
        print("- 空间复杂度: O(n)")
        print("  - 需要存储糖果分配数组")
        
        print("\n方法2（一次遍历）:")
        print("- 时间复杂度: O(n)")
        print("  - 一次遍历数组")
        print("  - 维护上升下降状态")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n方法3（暴力解法）:")
        print("- 时间复杂度: O(n^2)")
        print("  - 最坏情况下需要多次遍历")
        print("  - 每次调整可能影响相邻元素")
        print("- 空间复杂度: O(n)")
        print("  - 需要存储糖果分配")
        
        print("\n贪心策略证明:")
        print("1. 分别处理左右两个方向的约束")
        print("2. 两次遍历确保两个条件都满足")
        print("3. 取最大值保证两个方向的最优性")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理空数组和边界情况")
        print("2. 性能优化：避免暴力解法")
        print("3. 可读性：清晰的算法逻辑")
        print("4. 测试覆盖：各种评分模式")

def main():
    """主函数"""
    Code37_Candy.run_tests()
    Code37_Candy.performance_test()
    Code37_Candy.analyze_complexity()

if __name__ == "__main__":
    main()