import time

"""
LeetCode 853. 车队 (Car Fleet)

题目描述:
N 辆车沿着一条车道驶向位于 target 英里之外的共同目的地。
每辆车 i 以恒定的速度 speed[i] 英里/小时，从初始位置 position[i] 英里处出发。
一辆车永远不会超过前面的另一辆车，但它可以追上去，并与前车以相同的速度紧接着行驶。
此时，我们会忽略这两辆车之间的距离，也就是说，它们被假定处于同一位置。
车队是一些由一辆或多辆车组成的非空集合，这些车以相同的速度行驶，并且彼此之间没有间隔。
注意，一辆车也可以是一个车队。
即便一辆车在到达目的地后不会再移动，它仍然可能是车队的一部分。

返回最终车队的数量。

示例1:
输入: target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
输出: 3
解释:
从初始位置开始，车辆按以下方式移动：
- 10号位置的车以2的速度移动，到达时间为(12-10)/2=1小时
- 8号位置的车以4的速度移动，到达时间为(12-8)/4=1小时
- 0号位置的车以1的速度移动，到达时间为12/1=12小时
- 5号位置的车以1的速度移动，到达时间为(12-5)/1=7小时
- 3号位置的车以3的速度移动，到达时间为(12-3)/3=3小时

0号车会在12小时到达，而前面的车已经到达。
3号车和5号车在到达之前都不会被前面的车阻挡。
10号车和8号车会在1小时同时到达，并且形成一个车队。
因此，最终车队的数量是3。

示例2:
输入: target = 10, position = [3], speed = [3]
输出: 1

示例3:
输入: target = 100, position = [0,2,4], speed = [4,2,1]
输出: 1
解释: 0号车会在(100-0)/4=25小时到达，2号车会在(100-2)/2=49小时到达，4号车会在(100-4)/1=96小时到达。
但0号车会被2号车和4号车阻挡，最终这三辆车会形成一个车队。

提示:
1. 0 <= N <= 10^4
2. 0 < target <= 10^6
3. 0 <= position[i] < target
4. 0 < speed[i] <= 10^6
5. 所有车的初始位置各不相同。

题目链接: https://leetcode.com/problems/car-fleet/

解题思路:
这个问题可以通过以下步骤解决：
1. 首先，我们需要将每辆车的位置和速度组合成一个对象，并按照位置从大到小（离终点近到远）排序
2. 然后，计算每辆车到达终点所需的时间
3. 从离终点最近的车开始，如果后面的车到达终点的时间不大于前面的车，那么后面的车会与前面的车组成一个车队
4. 否则，后面的车会形成一个新的车队

时间复杂度: O(n log n)，其中 n 是车的数量。排序的时间复杂度为 O(n log n)。
空间复杂度: O(n)，用于存储车的信息和到达时间。

这是最优解，因为我们需要至少对车辆进行一次排序，排序的时间复杂度无法低于 O(n log n)。
"""

def car_fleet(target, position, speed):
    """
    计算最终车队的数量
    
    Args:
        target: 目标位置
        position: 每辆车的初始位置数组
        speed: 每辆车的速度数组
    
    Returns:
        最终车队的数量
    """
    # 参数校验
    if not position or not speed or len(position) != len(speed):
        return 0
    
    # 创建车辆列表，存储位置和速度
    cars = list(zip(position, speed))
    
    # 按照位置从大到小排序（离终点近到远）
    cars.sort(key=lambda x: x[0], reverse=True)
    
    fleet_count = 1  # 至少有一个车队
    current_time = (target - cars[0][0]) / cars[0][1]  # 第一辆车到达终点的时间
    
    # 从第二辆车开始，检查是否会与前面的车形成车队
    for i in range(1, len(cars)):
        arrival_time = (target - cars[i][0]) / cars[i][1]
        
        # 如果当前车的到达时间大于前面车队的到达时间，那么它会形成一个新的车队
        if arrival_time > current_time:
            fleet_count += 1
            current_time = arrival_time
        # 否则，当前车会与前面的车形成一个车队
    
    return fleet_count

def car_fleet_alternative(target, position, speed):
    """
    另一种实现方式，使用列表推导式和zip函数
    
    Args:
        target: 目标位置
        position: 每辆车的初始位置数组
        speed: 每辆车的速度数组
    
    Returns:
        最终车队的数量
    """
    # 参数校验
    if not position or not speed or len(position) != len(speed):
        return 0
    
    # 计算每辆车到达终点所需的时间，并按照位置从大到小排序
    # 使用列表存储 (position, time_to_reach)
    time_and_positions = [(pos, (target - pos) / spd) for pos, spd in zip(position, speed)]
    
    # 按照位置从大到小排序
    time_and_positions.sort(key=lambda x: x[0], reverse=True)
    
    fleet_count = 0
    current_max_time = 0
    
    # 从离终点最近的车开始遍历
    for _, arrival_time in time_and_positions:
        # 如果当前车的到达时间大于之前所有车的到达时间，它将形成一个新的车队
        if arrival_time > current_max_time:
            current_max_time = arrival_time
            fleet_count += 1
    
    return fleet_count

def print_array(arr):
    """
    打印数组
    
    Args:
        arr: 要打印的数组
    """
    print(f"[{' '.join(map(str, arr))}]")

# 测试代码
def main():
    # 测试用例1
    target1 = 12
    position1 = [10, 8, 0, 5, 3]
    speed1 = [2, 4, 1, 1, 3]
    
    print("测试用例1:")
    print(f"target = {target1}")
    print("position = ")
    print_array(position1)
    print("speed = ")
    print_array(speed1)
    print(f"car_fleet 结果: {car_fleet(target1, position1, speed1)}")  # 预期输出: 3
    print(f"car_fleet_alternative 结果: {car_fleet_alternative(target1, position1, speed1)}")  # 预期输出: 3
    print()
    
    # 测试用例2
    target2 = 10
    position2 = [3]
    speed2 = [3]
    
    print("测试用例2:")
    print(f"target = {target2}")
    print("position = ")
    print_array(position2)
    print("speed = ")
    print_array(speed2)
    print(f"car_fleet 结果: {car_fleet(target2, position2, speed2)}")  # 预期输出: 1
    print(f"car_fleet_alternative 结果: {car_fleet_alternative(target2, position2, speed2)}")  # 预期输出: 1
    print()
    
    # 测试用例3
    target3 = 100
    position3 = [0, 2, 4]
    speed3 = [4, 2, 1]
    
    print("测试用例3:")
    print(f"target = {target3}")
    print("position = ")
    print_array(position3)
    print("speed = ")
    print_array(speed3)
    print(f"car_fleet 结果: {car_fleet(target3, position3, speed3)}")  # 预期输出: 1
    print(f"car_fleet_alternative 结果: {car_fleet_alternative(target3, position3, speed3)}")  # 预期输出: 1
    print()
    
    # 测试用例4 - 边界情况：所有车都形成一个车队
    target4 = 100
    position4 = [90, 80, 70, 60]
    speed4 = [1, 2, 3, 4]
    
    print("测试用例4:")
    print(f"target = {target4}")
    print("position = ")
    print_array(position4)
    print("speed = ")
    print_array(speed4)
    print(f"car_fleet 结果: {car_fleet(target4, position4, speed4)}")  # 预期输出: 1
    print(f"car_fleet_alternative 结果: {car_fleet_alternative(target4, position4, speed4)}")  # 预期输出: 1
    print()
    
    # 测试用例5 - 边界情况：所有车都各自形成一个车队
    target5 = 100
    position5 = [90, 80, 70, 60]
    speed5 = [1, 1, 1, 1]
    
    print("测试用例5:")
    print(f"target = {target5}")
    print("position = ")
    print_array(position5)
    print("speed = ")
    print_array(speed5)
    print(f"car_fleet 结果: {car_fleet(target5, position5, speed5)}")  # 预期输出: 4
    print(f"car_fleet_alternative 结果: {car_fleet_alternative(target5, position5, speed5)}")  # 预期输出: 4
    print()
    
    # 性能测试
    print("性能测试:")
    target6 = 1000000
    n = 10000
    position6 = [n - i - 1 for i in range(n)]  # 从近到远排列
    speed6 = [1 + (i % 10) for i in range(n)]  # 速度在1-10之间
    
    start_time = time.time()
    result1 = car_fleet(target6, position6, speed6)
    end_time = time.time()
    print(f"大数组 - car_fleet 结果: {result1}")
    print(f"大数组 - car_fleet 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    start_time = time.time()
    result2 = car_fleet_alternative(target6, position6, speed6)
    end_time = time.time()
    print(f"大数组 - car_fleet_alternative 结果: {result2}")
    print(f"大数组 - car_fleet_alternative 耗时: {(end_time - start_time) * 1000:.2f}ms")

if __name__ == "__main__":
    main()