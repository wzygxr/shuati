# 种花问题（Can Place Flowers）
# 题目来源：LeetCode 605
# 题目链接：https://leetcode.cn/problems/can-place-flowers/

"""
问题描述：
假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，
其中 0 表示没种植花，1 表示种植了花。
另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
能则返回 true ，不能则返回 false 。

算法思路：
使用贪心策略，尽可能多地种花，只要当前位置可以种花（当前位置是0，且左右都是0或边界），就种花。
具体步骤：
1. 遍历花坛数组
2. 对于每个位置，检查是否满足种花条件：
   - 当前位置是0
   - 前一个位置是0或当前是第一个位置
   - 后一个位置是0或当前是最后一个位置
3. 如果满足条件，就在当前位置种花（将0改为1），并减少需要种的花数量
4. 如果需要种的花数量减为0，返回true
5. 遍历结束后，如果需要种的花数量已减为0，返回true，否则返回false

时间复杂度：O(n)，其中n是花坛数组的长度，只需遍历数组一次
空间复杂度：O(1)，只使用了常数额外空间

是否最优解：是。贪心策略在此问题中能得到最优解。

适用场景：
1. 间隔种植问题
2. 资源分配问题，需要满足相邻资源不能同时使用

异常处理：
1. 处理空数组情况
2. 处理n为0的边界情况（不需要种花，直接返回true）

工程化考量：
1. 输入验证：检查数组是否为空，检查n是否为非负数
2. 边界条件：处理边界位置的种植判断
3. 性能优化：一旦确认可以种植n朵花，立即返回结果
"""

class Solution:
    def canPlaceFlowers(self, flowerbed, n):
        """
        判断是否能在不打破种植规则的情况下种入n朵花
        
        Args:
            flowerbed: List[int] - 表示花坛的数组，0表示没种植花，1表示种植了花
            n: int - 需要种入的花数量
            
        Returns:
            bool - 如果能种入n朵花返回True，否则返回False
        """
        # 边界条件检查
        if not flowerbed:
            return n == 0  # 空花坛只能种0朵花
        
        if n <= 0:
            return True  # 不需要种花，直接返回True
        
        length = len(flowerbed)
        
        # 遍历花坛数组
        for i in range(length):
            # 检查当前位置是否可以种花
            if flowerbed[i] == 0:
                # 检查左侧是否为空或边界
                left_empty = (i == 0) or (flowerbed[i - 1] == 0)
                # 检查右侧是否为空或边界
                right_empty = (i == length - 1) or (flowerbed[i + 1] == 0)
                
                if left_empty and right_empty:
                    # 可以种花
                    flowerbed[i] = 1  # 标记为已种花
                    n -= 1  # 减少需要种的花数量
                    
                    # 如果已经种完所有需要的花，返回True
                    if n == 0:
                        return True
        
        # 遍历结束后，检查是否种完了所有需要的花
        return n == 0

# 测试函数，验证算法正确性
def test_can_place_flowers():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 可以种花
    flowerbed1 = [1, 0, 0, 0, 1]
    n1 = 1
    # 创建副本以避免修改原始数组
    flowerbed1_copy = flowerbed1.copy()
    result1 = solution.canPlaceFlowers(flowerbed1_copy, n1)
    print("测试用例1:")
    print(f"花坛: {flowerbed1}")
    print(f"需要种花数量: {n1}")
    print(f"能否种植: {result1}")
    print(f"期望输出: True")
    print()
    
    # 测试用例2: 基本情况 - 不能种花
    flowerbed2 = [1, 0, 0, 0, 1]
    n2 = 2
    flowerbed2_copy = flowerbed2.copy()
    result2 = solution.canPlaceFlowers(flowerbed2_copy, n2)
    print("测试用例2:")
    print(f"花坛: {flowerbed2}")
    print(f"需要种花数量: {n2}")
    print(f"能否种植: {result2}")
    print(f"期望输出: False")
    print()
    
    # 测试用例3: 边界情况 - n为0
    flowerbed3 = [1, 0, 0, 0, 1]
    n3 = 0
    flowerbed3_copy = flowerbed3.copy()
    result3 = solution.canPlaceFlowers(flowerbed3_copy, n3)
    print("测试用例3:")
    print(f"花坛: {flowerbed3}")
    print(f"需要种花数量: {n3}")
    print(f"能否种植: {result3}")
    print(f"期望输出: True")
    print()
    
    # 测试用例4: 边界情况 - 全为0的花坛
    flowerbed4 = [0, 0, 0, 0]
    n4 = 2
    flowerbed4_copy = flowerbed4.copy()
    result4 = solution.canPlaceFlowers(flowerbed4_copy, n4)
    print("测试用例4:")
    print(f"花坛: {flowerbed4}")
    print(f"需要种花数量: {n4}")
    print(f"能否种植: {result4}")
    print(f"期望输出: True")
    print()
    
    # 测试用例5: 边界情况 - 单元素花坛
    flowerbed5 = [0]
    n5 = 1
    flowerbed5_copy = flowerbed5.copy()
    result5 = solution.canPlaceFlowers(flowerbed5_copy, n5)
    print("测试用例5:")
    print(f"花坛: {flowerbed5}")
    print(f"需要种花数量: {n5}")
    print(f"能否种植: {result5}")
    print(f"期望输出: True")

# 运行测试
if __name__ == "__main__":
    test_can_place_flowers()