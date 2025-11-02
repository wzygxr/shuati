"""
LeetCode 1094. 拼车 (Car Pooling)

题目描述:
假设你是一位顺风车司机，车上最初有 capacity 个空座位可以用来载客。
由于道路拥堵，你只能向一个方向行驶。
给定一个数组 trips，其中 trips[i] = [num_passengers, start, end]
表示第 i 次旅行有 num_passengers 位乘客，接他们和放他们的位置分别是 start 和 end。
这些位置是从你的初始位置向东的公里数。
当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则返回 false。

示例:
输入: trips = [[2,1,5],[3,3,7]], capacity = 4
输出: false

输入: trips = [[2,1,5],[3,3,7]], capacity = 5
输出: true

提示:
1 <= trips.length <= 1000
trips[i].length == 3
1 <= num_passengersi <= 100
0 <= fromi < toi <= 1000
1 <= capacity <= 10^5

题目链接: https://leetcode.com/problems/car-pooling/

解题思路:
使用差分数组技巧来处理区间更新操作。
1. 创建一个差分数组diff，大小为1001（题目提示toi <= 1000）
2. 对于每次旅行[passengers, start, end]，在差分数组中执行diff[start] += passengers和diff[end] -= passengers
3. 对差分数组计算前缀和，得到每个位置的实际乘客数
4. 检查是否有任何位置的乘客数超过capacity

时间复杂度: O(n + m) - n是trips长度，m是位置范围(1001)
空间复杂度: O(m) - 需要额外的差分数组空间

这是最优解，因为需要处理所有行程，而且位置范围固定。
"""


def car_pooling(trips, capacity):
    """
    判断是否可以完成所有行程

    :param trips: 行程数组，每个行程包含[乘客数, 起点, 终点]
    :param capacity: 车辆容量
    :return: 是否可以完成所有行程
    """
    # 边界情况处理
    if not trips:
        return True
    
    # 位置范围是0-1000，共1001个位置
    MAX_POSITION = 1001
    
    # 创建差分数组
    diff = [0] * MAX_POSITION
    
    # 处理每次行程
    for trip in trips:
        passengers = trip[0]  # 乘客数
        start = trip[1]       # 起点
        end = trip[2]         # 终点
        
        # 在起点增加乘客
        diff[start] += passengers
        
        # 在终点减少乘客（乘客在此下车）
        diff[end] -= passengers
    
    # 通过计算差分数组的前缀和得到每个位置的实际乘客数
    current_passengers = 0
    for i in range(MAX_POSITION):
        current_passengers += diff[i]
        
        # 如果任何位置的乘客数超过容量，返回False
        if current_passengers > capacity:
            return False
    
    return True


def main():
    """测试用例"""
    # 测试用例1
    trips1 = [[2, 1, 5], [3, 3, 7]]
    capacity1 = 4
    result1 = car_pooling(trips1, capacity1)
    # 预期输出: False
    print("测试用例1:", result1)

    # 测试用例2
    trips2 = [[2, 1, 5], [3, 3, 7]]
    capacity2 = 5
    result2 = car_pooling(trips2, capacity2)
    # 预期输出: True
    print("测试用例2:", result2)
    
    # 测试用例3
    trips3 = [[2, 1, 5], [3, 5, 7]]
    capacity3 = 3
    result3 = car_pooling(trips3, capacity3)
    # 预期输出: True
    print("测试用例3:", result3)
    
    # 测试用例4
    trips4 = [[3, 2, 7], [3, 7, 9], [8, 3, 9]]
    capacity4 = 11
    result4 = car_pooling(trips4, capacity4)
    # 预期输出: True
    print("测试用例4:", result4)


if __name__ == "__main__":
    main()