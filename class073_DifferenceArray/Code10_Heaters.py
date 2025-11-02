import bisect

"""
LeetCode 475. 供暖器 (Heaters)

题目描述:
冬季已经来临。你的任务是设计一个有固定加热半径的供暖器，使得所有房屋都可以被供暖。
现在，给出位于一条水平线上的房屋和供暖器的位置，找到可以覆盖所有房屋的最小加热半径。
所以，你的输入将会是房屋和供暖器的位置。你将输出供暖器的最小加热半径。

示例1:
输入: houses = [1,2,3], heaters = [2]
输出: 1
解释: 仅在位置2上有一个供暖器。如果我们将加热半径设为1，那么所有房屋就都能得到供暖。

示例2:
输入: houses = [1,2,3,4], heaters = [1,4]
输出: 1
解释: 在位置1和4上有两个供暖器。我们需要将加热半径设为1，这样房屋2和3就都能得到供暖。

示例3:
输入: houses = [1,5], heaters = [2]
输出: 3
解释: 供暖器在位置2，需要半径3才能覆盖房屋1和房屋5。

提示:
1. 给出的房屋和供暖器的数目是非负数且不会超过 25000。
2. 给出的房屋和供暖器的位置均是非负数且不会超过 10^9。
3. 只要房屋位于供暖器的半径内（包括在边缘上），它就可以得到供暖。
4. 所有供暖器都遵循你的半径标准，加热的半径也一样。

题目链接: https://leetcode.com/problems/heaters/

解题思路:
这个问题可以使用二分查找来解决：
1. 首先对供暖器的位置进行排序，以便使用二分查找
2. 对于每个房屋，找到离它最近的供暖器
3. 计算房屋到最近供暖器的距离，并更新最大距离
4. 最终的最大距离就是所需的最小加热半径

具体步骤：
1. 对供暖器数组进行排序
2. 遍历每个房屋位置
3. 对每个房屋位置，使用二分查找找到其左右两侧最近的供暖器
4. 计算房屋到这两个供暖器的距离，取较小值
5. 更新全局最大距离

时间复杂度: O(n log n + m log n) - n是供暖器数量，m是房屋数量，排序需要O(n log n)，每个房屋的二分查找需要O(log n)
空间复杂度: O(1) - 只需要常数级的额外空间

这是最优解，因为我们需要遍历每个房屋并为每个房屋进行二分查找，这已经是理论上的最优复杂度。
"""

class Solution:
    """
    计算供暖器的最小加热半径
    
    Args:
        houses: 房屋位置数组
        heaters: 供暖器位置数组
    
    Returns:
        最小加热半径
    """
    def findRadius(self, houses, heaters):
        if not houses:
            return 0
        if not heaters:
            # 没有供暖器，无法供暖，但根据题意，供暖器数量不会为0
            return -1
        
        # 对供暖器位置进行排序，以便使用二分查找
        heaters.sort()
        
        max_radius = 0
        
        # 遍历每个房屋
        for house in houses:
            # 找到离当前房屋最近的供暖器
            closest_heater_distance = self.find_closest_heater(house, heaters)
            
            # 更新最大半径
            max_radius = max(max_radius, closest_heater_distance)
        
        return max_radius
    
    """
    使用二分查找找到离指定房屋最近的供暖器，并返回距离
    
    Args:
        house: 房屋位置
        heaters: 已排序的供暖器位置数组
    
    Returns:
        房屋到最近供暖器的距离
    """
    def find_closest_heater(self, house, heaters):
        left = 0
        right = len(heaters) - 1
        
        # 处理边界情况：房屋在所有供暖器的左侧
        if house <= heaters[0]:
            return heaters[0] - house
        # 处理边界情况：房屋在所有供暖器的右侧
        if house >= heaters[right]:
            return house - heaters[right]
        
        # 二分查找
        while left < right - 1:
            mid = left + (right - left) // 2
            if heaters[mid] == house:
                return 0  # 房屋正好在供暖器位置
            elif heaters[mid] < house:
                left = mid
            else:
                right = mid
        
        # 此时，heaters[left] < house < heaters[right]，计算到两者的距离，取较小值
        return min(house - heaters[left], heaters[right] - house)
    
    """
    另一种实现方式，使用Python内置的bisect模块
    
    Args:
        houses: 房屋位置数组
        heaters: 供暖器位置数组
    
    Returns:
        最小加热半径
    """
    def findRadiusAlternative(self, houses, heaters):
        if not houses:
            return 0
        if not heaters:
            return -1
        
        heaters.sort()
        max_radius = 0
        
        for house in houses:
            # 使用bisect_left找到插入位置
            index = bisect.bisect_left(heaters, house)
            closest_distance = float('inf')
            
            # 检查左侧供暖器
            if index > 0:
                closest_distance = min(closest_distance, house - heaters[index - 1])
            
            # 检查右侧供暖器
            if index < len(heaters):
                closest_distance = min(closest_distance, heaters[index] - house)
            
            max_radius = max(max_radius, closest_distance)
        
        return max_radius

# 辅助函数：打印数组
def print_array(arr):
    print(f"[{', '.join(map(str, arr))}]")

# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1
    houses1 = [1, 2, 3]
    heaters1 = [2]
    print("测试用例1 结果:", solution.findRadius(houses1, heaters1))  # 预期输出: 1
    print("测试用例1 (替代方法) 结果:", solution.findRadiusAlternative(houses1, heaters1))  # 预期输出: 1

    # 测试用例2
    houses2 = [1, 2, 3, 4]
    heaters2 = [1, 4]
    print("测试用例2 结果:", solution.findRadius(houses2, heaters2))  # 预期输出: 1
    print("测试用例2 (替代方法) 结果:", solution.findRadiusAlternative(houses2, heaters2))  # 预期输出: 1

    # 测试用例3
    houses3 = [1, 5]
    heaters3 = [2]
    print("测试用例3 结果:", solution.findRadius(houses3, heaters3))  # 预期输出: 3
    print("测试用例3 (替代方法) 结果:", solution.findRadiusAlternative(houses3, heaters3))  # 预期输出: 3
    
    # 测试用例4 - 空输入
    houses4 = []
    heaters4 = [1, 2, 3]
    print("测试用例4 (空房屋) 结果:", solution.findRadius(houses4, heaters4))  # 预期输出: 0
    
    # 测试用例5 - 供暖器和房屋重叠
    houses5 = [1, 1, 1, 1]
    heaters5 = [1]
    print("测试用例5 (重叠位置) 结果:", solution.findRadius(houses5, heaters5))  # 预期输出: 0
    
    # 测试用例6 - 大规模数据
    houses6 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    heaters6 = [3, 7]
    print("测试用例6 (大规模数据) 结果:", solution.findRadius(houses6, heaters6))  # 预期输出: 3

if __name__ == "__main__":
    main()