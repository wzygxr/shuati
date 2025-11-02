#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'''
LeetCode 452. 用最少数量的箭引爆气球
题目链接：https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
难度：中等

问题描述：
有一些球形气球贴在一堵用XY平面表示的墙面上。墙面上的气球记录在整数数组 points ，其中points[i] = [xstart, xend] 表示水平直径在xstart和xend之间的气球。
你不知道气球的确切y坐标。一支弓箭可以沿着x轴从不同点完全垂直地射出。在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend，且满足 xstart ≤ x ≤ xend，则该气球会被引爆。
可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。

解题思路：
贪心算法 + 区间排序
1. 按气球的右边界进行排序
2. 每次尽可能用一支箭引爆最多的气球
3. 维护当前箭的位置为当前气球的右边界
4. 遍历所有气球，如果当前气球的左边界大于箭的位置，则需要一支新箭，并更新箭的位置

时间复杂度分析：
- 排序的时间复杂度：O(n log n)，其中n是气球的数量
- 遍历的时间复杂度：O(n)
- 总的时间复杂度：O(n log n)

空间复杂度分析：
- 排序所需的额外空间：O(log n)
- 总的空间复杂度：O(log n)

最优性证明：
这是一个区间调度问题的变种。通过按右边界排序，我们能够保证每次选择的箭尽可能多地引爆气球，从而得到最优解。
因为如果有一个更优的解使用更少的箭，那么在某个区间一定存在一支箭可以同时引爆更多的气球，这与我们的贪心策略矛盾。
'''

class Solution:
    def findMinArrowShots(self, points):
        """
        计算引爆所有气球所需的最小箭数
        
        Args:
            points: 气球的坐标数组，每个元素为[xstart, xend]
            
        Returns:
            最小箭数
            
        Raises:
            TypeError: 如果输入类型不正确
            ValueError: 如果输入数据格式不正确
        """
        # 参数验证
        if not isinstance(points, list):
            raise TypeError("输入必须是列表类型")
        
        # 处理边界情况
        if not points:
            return 0
        if len(points) == 1:
            return 1
        
        # 验证数据格式
        for point in points:
            if not isinstance(point, list) or len(point) != 2:
                raise ValueError("每个气球坐标必须是包含两个元素的列表")
            if not all(isinstance(coord, int) for coord in point):
                raise ValueError("气球坐标必须是整数")
        
        # 按气球的右边界排序
        # Python的sort函数是稳定的，使用Timsort算法
        points.sort(key=lambda x: x[1])
        
        # 初始化箭数为1，第一支箭的位置为第一个气球的右边界
        arrows = 1
        current_end = points[0][1]
        
        # 遍历所有气球
        for x_start, x_end in points[1:]:
            # 如果当前气球的左边界大于箭的位置，需要一支新箭
            if x_start > current_end:
                arrows += 1
                # 更新箭的位置为当前气球的右边界
                current_end = x_end
            # 否则，当前箭可以引爆这个气球，不需要额外操作
        
        return arrows

# 测试代码
def test_findMinArrowShots():
    solution = Solution()
    
    # 测试用例1：基本用例
    points1 = [[10, 16], [2, 8], [1, 6], [7, 12]]
    print("测试用例1结果：", solution.findMinArrowShots(points1))  # 预期输出：2
    
    # 测试用例2：无重叠的气球
    points2 = [[1, 2], [3, 4], [5, 6], [7, 8]]
    print("测试用例2结果：", solution.findMinArrowShots(points2))  # 预期输出：4
    
    # 测试用例3：完全重叠的气球
    points3 = [[1, 5], [2, 3], [3, 4], [4, 5]]
    print("测试用例3结果：", solution.findMinArrowShots(points3))  # 预期输出：1
    
    # 测试用例4：边界情况 - 空数组
    points4 = []
    print("测试用例4结果：", solution.findMinArrowShots(points4))  # 预期输出：0
    
    # 测试用例5：边界情况 - 单气球
    points5 = [[1, 2]]
    print("测试用例5结果：", solution.findMinArrowShots(points5))  # 预期输出：1
    
    # 测试用例6：负数坐标
    points6 = [[-10, -5], [-8, -3], [-6, 0], [-4, 2]]
    print("测试用例6结果：", solution.findMinArrowShots(points6))  # 预期输出：2
    
    # 测试用例7：混合正负坐标
    points7 = [[-10, 10], [-5, 5], [0, 15], [10, 20]]
    print("测试用例7结果：", solution.findMinArrowShots(points7))  # 预期输出：2

if __name__ == "__main__":
    test_findMinArrowShots()
    
    # 性能测试示例
    import random
    import time
    
    # 生成大规模测试数据
    print("\n性能测试：")
    for size in [100, 1000, 10000]:
        large_points = []
        for _ in range(size):
            start = random.randint(-10000, 10000)
            end = start + random.randint(1, 100)
            large_points.append([start, end])
        
        start_time = time.time()
        result = Solution().findMinArrowShots(large_points)
        end_time = time.time()
        
        print(f"数据规模 {size}，结果：{result}，耗时：{end_time - start_time:.6f} 秒")

# 代码调试技巧示例
def debug_findMinArrowShots(points):
    """
    带调试信息的函数版本，用于理解算法执行过程
    """
    print("原始数据：", points)
    
    # 边界情况处理
    if not points:
        return 0
    
    # 排序
    points.sort(key=lambda x: x[1])
    print("排序后数据：", points)
    
    arrows = 1
    current_end = points[0][1]
    print(f"初始化：箭数={arrows}, 当前箭位置={current_end}")
    
    for i, (x_start, x_end) in enumerate(points[1:], 1):
        print(f"\n处理气球 {i}: [{x_start}, {x_end}]")
        if x_start > current_end:
            arrows += 1
            current_end = x_end
            print(f"需要新箭！更新箭数={arrows}, 当前箭位置={current_end}")
        else:
            print(f"当前箭可以引爆！箭数={arrows}, 当前箭位置={current_end}")
    
    return arrows

# 示例：运行调试版本
print("\n调试运行示例：")
debug_points = [[10, 16], [2, 8], [1, 6], [7, 12]]
print("最终结果：", debug_findMinArrowShots(debug_points))

"""
Python语言特性与优化：
1. 使用列表推导式和生成器表达式提高效率
2. 利用Python的内置排序函数，其实现是高效的Timsort算法
3. 使用元组解包提高代码可读性，如for x_start, x_end in points[1:]
4. 可以使用functools.cmp_to_key来自定义比较函数，但在这个问题中直接按元素排序即可

工程化建议：
1. 代码中包含完整的参数验证和错误处理
2. 函数有详细的文档字符串(docstring)
3. 提供了单独的测试函数和调试函数
4. 包含性能测试代码，用于评估算法在大规模数据上的表现
5. 变量命名清晰，符合Python的PEP 8规范

扩展思考：
1. 如果气球是动态添加或移除的，如何维护最优解？
2. 如果箭有一定的宽度，如何调整算法？
3. 如何将这个算法扩展到二维或三维空间？
4. 如何并行化处理大规模气球数据？
"""