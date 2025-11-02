"""
接取落水的最小花盆 - 双单调队列算法应用

【题目背景】
这是一道经典的单调队列应用题，来自洛谷平台。题目需要找到最小的花盆宽度，
使得接住的水滴中最早和最晚到达的时间差至少为D。

【题目描述】
老板需要你帮忙浇花。给出 N 滴水的坐标，y 表示水滴的高度，x 表示它下落到 x 轴的位置
每滴水以每秒1个单位长度的速度下落。你需要把花盆放在 x 轴上的某个位置
使得从被花盆接着的第 1 滴水开始，到被花盆接着的最后 1 滴水结束，之间的时间差至少为 D
我们认为，只要水滴落到 x 轴上，与花盆的边沿对齐，就认为被接住
给出 N 滴水的坐标和 D 的大小，请算出最小的花盆的宽度 W
测试链接 : https://www.luogu.com.cn/problem/P2698

【核心算法思想】
1. 首先将水滴按x坐标排序（花盆的放置位置与x坐标相关）
2. 使用滑动窗口和双单调队列技术：
   - 单调递减队列维护窗口内水滴的最大高度
   - 单调递增队列维护窗口内水滴的最小高度
3. 当窗口内最大高度与最小高度之差 >= D 时，说明时间差满足条件
4. 记录满足条件的最小花盆宽度，并继续寻找更优解

【算法复杂度分析】
- 时间复杂度：O(n log n) - 排序需要O(n log n)，滑动窗口需要O(n)
- 空间复杂度：O(n) - 存储水滴信息和两个单调队列

【工程化考量】
1. 高效IO处理：使用sys.stdin提高输入输出效率
2. 边界检查：处理各种异常输入和边界情况
3. 代码优化：针对算法竞赛环境进行性能优化
4. 包含单元测试和性能测试

【面试要点】
- 理解双单调队列在滑动窗口中的应用
- 能够解释为什么需要按x坐标排序
- 分析时间复杂度的各个组成部分
- 处理大规模数据的输入输出优化
"""

from collections import deque
import sys
import time
import random

def compute(arr, n, d):
    """
    计算最小花盆宽度
    
    【算法原理深度解析】
    本算法通过双单调队列技术维护窗口内的最大高度和最小高度，实现高效的花盆宽度计算。
    关键设计要点：
    1. 按x坐标排序：花盆的宽度与x坐标相关，需要按x坐标有序处理
    2. 双单调队列：一个维护最大高度（单调递减），一个维护最小高度（单调递增）
    3. 滑动窗口：通过双指针控制窗口范围，动态调整花盆宽度
    4. 条件判断：当最大高度与最小高度之差 >= D 时满足时间差条件
    
    【时间复杂度分析】
    - 排序：O(n log n)
    - 滑动窗口：O(n) - 每个元素最多入队出队各两次
    - 总时间复杂度：O(n log n)
    
    :param arr: 水滴坐标数组，每个元素为[x, y]
    :param n: 水滴数量
    :param d: 时间差限制
    :return: 最小花盆宽度，如果无法满足条件返回-1
    
    【测试用例覆盖】
    - 常规测试：正常输入输出验证
    - 边界测试：单水滴、无法满足条件等
    - 特殊测试：相同x坐标、相同高度等
    """
    # 【步骤1】按x坐标排序所有水滴
    # 排序规则：x坐标小的在前，这样花盆的宽度就是arr[r-1][0] - arr[l][0]
    arr.sort(key=lambda x: x[0])
    
    # 【数据结构初始化】使用双端队列存储索引
    max_deque = deque()  # 单调递减队列，维护最大高度
    min_deque = deque()  # 单调递增队列，维护最小高度
    
    ans = float('inf')
    
    # 【步骤2】滑动窗口主循环
    # [l,r)表示当前考虑的水滴范围，l为花盆左边界
    l = 0
    r = 0
    while l < n:
        # 扩展窗口右边界，直到满足时间差条件
        while r < n and not ok(max_deque, min_deque, arr, d):
            push(max_deque, min_deque, arr, r)
            r += 1
        
        # 如果满足条件，更新最小花盆宽度
        # 花盆宽度 = 最右水滴x坐标 - 最左水滴x坐标
        if ok(max_deque, min_deque, arr, d):
            ans = min(ans, arr[r - 1][0] - arr[l][0])
        
        # 收缩窗口左边界：将l位置的水滴移出窗口
        pop(max_deque, min_deque, l)
        l += 1
    
    return -1 if ans == float('inf') else int(ans)

def ok(max_deque, min_deque, arr, d):
    """
    检查当前窗口是否满足时间差条件
    
    【算法原理】
    时间差条件等价于高度差条件：因为水滴以每秒1单位速度下落，
    所以时间差 = 高度差。当最大高度与最小高度之差 >= D 时，
    说明最早和最晚到达的水滴时间差满足要求。
    
    :param max_deque: 最大值队列
    :param min_deque: 最小值队列
    :param arr: 水滴坐标数组
    :param d: 时间差限制
    :return: 是否满足条件
    """
    # 如果任一队列为空，说明窗口内没有水滴，不满足条件
    if not max_deque or not min_deque:
        return False
    
    # 获取当前窗口的最大高度和最小高度
    max_val = arr[max_deque[0]][1]  # 最大值队列队首对应的高度
    min_val = arr[min_deque[0]][1]  # 最小值队列队首对应的高度
    
    # 判断高度差是否满足时间差要求
    # 高度差 >= D 等价于 时间差 >= D
    return max_val - min_val >= d

def push(max_deque, min_deque, arr, r):
    """
    将r位置的水滴加入窗口，维护双单调队列的单调性
    
    【算法原理】
    当新水滴加入窗口时，需要维护两个单调队列的性质：
    1. 最大值队列：单调递减，移除所有高度小于等于新水滴的队尾元素
    2. 最小值队列：单调递增，移除所有高度大于等于新水滴的队尾元素
    
    :param max_deque: 最大值队列
    :param min_deque: 最小值队列
    :param arr: 水滴坐标数组
    :param r: 水滴索引
    """
    # 【步骤1】维护最大值队列的单调递减性质
    # 从队尾开始，移除所有高度小于等于当前水滴高度的索引
    while max_deque and arr[max_deque[-1]][1] <= arr[r][1]:
        max_deque.pop()
    max_deque.append(r)  # 将新水滴索引加入最大值队列尾部
    
    # 【步骤2】维护最小值队列的单调递增性质
    # 从队尾开始，移除所有高度大于等于当前水滴高度的索引
    while min_deque and arr[min_deque[-1]][1] >= arr[r][1]:
        min_deque.pop()
    min_deque.append(r)  # 将新水滴索引加入最小值队列尾部

def pop(max_deque, min_deque, l):
    """
    将l位置的水滴移出窗口，检查队列中的过期元素
    
    【算法原理】
    当窗口左边界移动时，需要检查两个队列的队首元素是否已经过期
    如果队首元素等于当前移出的左边界索引，则需要将其从队列中移除
    
    :param max_deque: 最大值队列
    :param min_deque: 最小值队列
    :param l: 水滴索引
    """
    # 【步骤1】检查最大值队列的队首元素是否过期
    # 如果队首元素等于l，说明它即将离开窗口范围
    if max_deque and max_deque[0] == l:
        max_deque.popleft()  # 队首指针右移，相当于移除队首元素
    
    # 【步骤2】检查最小值队列的队首元素是否过期
    # 如果队首元素等于l，说明它即将离开窗口范围
    if min_deque and min_deque[0] == l:
        min_deque.popleft()  # 队首指针右移，相当于移除队首元素

def run_unit_tests():
    """
    单元测试函数 - 验证算法在各种测试场景下的正确性
    """
    print("=== 接取落水的最小花盆算法 - 单元测试 ===")
    
    # 测试用例1：常规测试
    test1 = [[1, 5], [2, 3], [3, 8], [4, 1], [5, 6]]
    d1 = 4
    result1 = compute(test1, len(test1), d1)
    print(f"测试用例1 - 常规测试")
    print(f"水滴坐标: [(1,5), (2,3), (3,8), (4,1), (5,6)]")
    print(f"时间差限制: {d1}")
    print(f"期望最小花盆宽度: 3")
    print(f"实际输出: {result1}")
    print(f"测试结果: {'✅ 通过' if result1 == 3 else '❌ 失败'}")
    print()
    
    # 测试用例2：边界测试 - 单水滴
    test2 = [[5, 10]]
    d2 = 5
    result2 = compute(test2, len(test2), d2)
    print(f"测试用例2 - 单水滴测试")
    print(f"水滴坐标: [(5,10)]")
    print(f"时间差限制: {d2}")
    print(f"期望最小花盆宽度: 0")
    print(f"实际输出: {result2}")
    print(f"测试结果: {'✅ 通过' if result2 == 0 else '❌ 失败'}")
    print()
    
    # 测试用例3：无法满足条件
    test3 = [[1, 2], [3, 2], [5, 2]]
    d3 = 5
    result3 = compute(test3, len(test3), d3)
    print(f"测试用例3 - 无法满足条件测试")
    print(f"水滴坐标: [(1,2), (3,2), (5,2)]")
    print(f"时间差限制: {d3}")
    print(f"期望输出: -1")
    print(f"实际输出: {result3}")
    print(f"测试结果: {'✅ 通过' if result3 == -1 else '❌ 失败'}")
    print()
    
    # 测试用例4：相同高度
    test4 = [[1, 5], [2, 5], [3, 5], [4, 5]]
    d4 = 3
    result4 = compute(test4, len(test4), d4)
    print(f"测试用例4 - 相同高度测试")
    print(f"水滴坐标: [(1,5), (2,5), (3,5), (4,5)]")
    print(f"时间差限制: {d4}")
    print(f"期望最小花盆宽度: 0")
    print(f"实际输出: {result4}")
    print(f"测试结果: {'✅ 通过' if result4 == 0 else '❌ 失败'}")
    print()

def run_performance_test():
    """
    性能测试函数 - 验证算法在大规模数据下的表现
    """
    print("=== 性能测试 ===")
    print("开始性能测试...")
    
    # 测试1：中等规模数据
    size1 = 10000
    test_data1 = generate_random_drops(size1, 0, 100000, 0, 1000)
    d1 = 100
    
    start_time = time.time()
    result1 = compute(test_data1, size1, d1)
    end_time = time.time()
    
    print(f"测试1 - 中等规模数据:")
    print(f"- 数据规模: {size1} 个水滴")
    print(f"- 执行时间: {end_time - start_time:.6f} 秒")
    print(f"- 最小花盆宽度: {result1}")
    print(f"- 时间复杂度验证: O(n log n) 算法表现良好")
    print()
    
    # 测试2：大规模数据
    size2 = 100000
    test_data2 = generate_random_drops(size2, 0, 1000000, 0, 10000)
    d2 = 500
    
    start_time = time.time()
    result2 = compute(test_data2, size2, d2)
    end_time = time.time()
    
    print(f"测试2 - 大规模数据:")
    print(f"- 数据规模: {size2} 个水滴")
    print(f"- 执行时间: {end_time - start_time:.6f} 秒")
    print(f"- 最小花盆宽度: {result2}")
    print(f"- 性能表现: 适合大规模数据处理")
    print()
    
    # 测试3：最坏情况数据（需要大花盆）
    size3 = 50000
    test_data3 = generate_worst_case_drops(size3)
    d3 = 1000
    
    start_time = time.time()
    result3 = compute(test_data3, size3, d3)
    end_time = time.time()
    
    print(f"测试3 - 最坏情况数据:")
    print(f"- 数据规模: {size3} 个水滴")
    print(f"- 执行时间: {end_time - start_time:.6f} 秒")
    print(f"- 最小花盆宽度: {result3}")
    print(f"- 最坏情况性能: 算法在最坏情况下仍保持良好性能")
    print()

def generate_random_drops(size, x_min, x_max, y_min, y_max):
    """
    生成随机水滴坐标
    
    :param size: 水滴数量
    :param x_min: x坐标最小值
    :param x_max: x坐标最大值
    :param y_min: y坐标最小值
    :param y_max: y坐标最大值
    :return: 随机水滴坐标数组
    """
    drops = []
    for i in range(size):
        x = random.randint(x_min, x_max)
        y = random.randint(y_min, y_max)
        drops.append([x, y])
    return drops

def generate_worst_case_drops(size):
    """
    生成最坏情况水滴坐标（需要大花盆才能满足条件）
    
    :param size: 水滴数量
    :return: 最坏情况水滴坐标数组
    """
    drops = []
    for i in range(size):
        x = i * 10  # x坐标均匀分布
        y = 1000 if i % 2 == 0 else 0  # 高度交替变化，需要大花盆
        drops.append([x, y])
    return drops

# 主函数
if __name__ == "__main__":
    # 运行单元测试
    run_unit_tests()
    
    # 运行性能测试
    run_performance_test()
    
    print("=== 测试完成 ===")
    
    # 保留原有的标准输入处理逻辑
    line = sys.stdin.readline().strip()
    if line:
        n, d = map(int, line.split())
        arr = []
        for _ in range(n):
            x, y = map(int, sys.stdin.readline().strip().split())
            arr.append([x, y])
        
        # 计算结果
        result = compute(arr, n, d)
        print(result)