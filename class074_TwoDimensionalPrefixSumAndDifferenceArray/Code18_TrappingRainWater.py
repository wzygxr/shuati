"""
LeetCode 42. 接雨水 (Trapping Rain Water) - Python版本

题目描述:
给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

示例1:
输入: height = [0,1,0,2,1,0,1,3,2,1,2,1]
输出: 6
解释: 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。

示例2:
输入: height = [4,2,0,3,2,5]
输出: 9

提示:
n == height.length
1 <= n <= 2 * 10^4
0 <= height[i] <= 10^5

题目链接: https://leetcode.com/problems/trapping-rain-water/

解题思路:
这道题可以通过多种方法解决，包括:
1. 暴力解法：计算每个位置能接的雨水量，然后求和
2. 动态规划：预先计算每个位置左右两侧的最高柱子高度
3. 双指针法：使用两个指针从两端向中间移动
4. 单调栈：使用栈来寻找可以接水的凹槽

这里实现三种解法：双指针法（最优解）、动态规划和单调栈。

解法一: 双指针法
时间复杂度: O(n)，其中 n 是数组的长度。只需要遍历一次数组。
空间复杂度: O(1)，只使用了常数级别的额外空间。

解法二: 动态规划
时间复杂度: O(n)，需要两次遍历数组来填充左右最大高度数组。
空间复杂度: O(n)，需要两个长度为 n 的数组来存储左右最大高度。

解法三: 单调栈
时间复杂度: O(n)，每个元素最多入栈和出栈一次。
空间复杂度: O(n)，最坏情况下，栈的大小可能达到数组长度。
"""

class Solution:
    """
    接雨水问题解决方案类
    """
    
    def trapTwoPointers(self, height):
        """
        解法一: 双指针法
        使用两个指针从两端向中间移动，每次比较左右两侧的最大值，决定当前位置能接的雨水量。
        
        算法思路：
        1. 使用left和right指针分别指向数组的两端
        2. 使用leftMax和rightMax记录左右两侧的最大高度
        3. 每次移动较小高度的指针，计算当前位置能接的雨水量
        
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        :param height: 柱子高度数组
        :return: 能接的雨水总量
        """
        if len(height) < 3:
            return 0
        
        left = 0
        right = len(height) - 1
        left_max = 0
        right_max = 0
        water_trapped = 0
        
        while left < right:
            # 更新左右两侧的最大高度
            left_max = max(left_max, height[left])
            right_max = max(right_max, height[right])
            
            # 哪边的最大值较小，哪边可以接水
            if left_max < right_max:
                # 左侧最大值较小，计算左侧当前位置能接的水量
                water_trapped += left_max - height[left]
                left += 1
            else:
                # 右侧最大值较小，计算右侧当前位置能接的水量
                water_trapped += right_max - height[right]
                right -= 1
        
        return water_trapped
    
    def trapDynamicProgramming(self, height):
        """
        解法二: 动态规划
        预先计算每个位置左右两侧的最高柱子高度，然后计算每个位置能接的雨水量。
        
        算法思路：
        1. 创建left_max数组，存储每个位置左侧的最高柱子高度
        2. 创建right_max数组，存储每个位置右侧的最高柱子高度
        3. 遍历数组，计算每个位置能接的雨水量
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        
        :param height: 柱子高度数组
        :return: 能接的雨水总量
        """
        if len(height) < 3:
            return 0
        
        n = len(height)
        left_max = [0] * n  # 存储每个位置左侧的最高柱子高度
        right_max = [0] * n  # 存储每个位置右侧的最高柱子高度
        water_trapped = 0
        
        # 计算每个位置左侧的最高柱子高度
        left_max[0] = height[0]
        for i in range(1, n):
            left_max[i] = max(left_max[i-1], height[i])
        
        # 计算每个位置右侧的最高柱子高度
        right_max[n-1] = height[n-1]
        for i in range(n-2, -1, -1):
            right_max[i] = max(right_max[i+1], height[i])
        
        # 计算每个位置能接的雨水量
        for i in range(n):
            # 当前位置能接的雨水量 = min(左侧最高柱子高度, 右侧最高柱子高度) - 当前柱子高度
            water_trapped += min(left_max[i], right_max[i]) - height[i]
        
        return water_trapped
    
    def trapMonotonicStack(self, height):
        """
        解法三: 单调栈
        使用栈来寻找可以接水的凹槽，栈中存储的是索引。
        
        算法思路：
        1. 使用单调递减栈存储柱子的索引
        2. 当遇到比栈顶高的柱子时，说明找到了一个凹槽
        3. 计算凹槽的宽度和高度，累加雨水量
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        
        :param height: 柱子高度数组
        :return: 能接的雨水总量
        """
        if len(height) < 3:
            return 0
        
        n = len(height)
        water_trapped = 0
        stack = []  # 存储索引，使用列表作为栈
        
        for i in range(n):
            # 当栈不为空且当前高度大于栈顶索引对应的高度时，说明找到了一个可以接水的凹槽
            while stack and height[i] > height[stack[-1]]:
                bottom = stack.pop()  # 凹槽的底部索引
                
                if not stack:
                    break  # 没有左边界，无法接水
                
                # 计算凹槽的宽度
                width = i - stack[-1] - 1
                # 计算凹槽的高度：min(左边界高度, 右边界高度) - 底部高度
                depth = min(height[stack[-1]], height[i]) - height[bottom]
                # 累加雨水量
                water_trapped += width * depth
            
            stack.append(i)  # 将当前索引入栈
        
        return water_trapped


def print_array(arr):
    """打印数组辅助函数"""
    print("[", end="")
    for i in range(len(arr)):
        print(arr[i], end="")
        if i < len(arr) - 1:
            print(", ", end="")
    print("]")


def test_normal_case():
    """测试用例1：正常情况"""
    print("=== 测试用例1：正常情况 ===")
    solution = Solution()
    
    height1 = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
    print("height = ", end="")
    print_array(height1)
    
    result1 = solution.trapTwoPointers(height1)
    result2 = solution.trapDynamicProgramming(height1)
    result3 = solution.trapMonotonicStack(height1)
    
    print(f"双指针法结果: {result1}")  # 预期输出: 6
    print(f"动态规划结果: {result2}")  # 预期输出: 6
    print(f"单调栈结果: {result3}")  # 预期输出: 6
    print()


def test_edge_case_1():
    """测试用例2：边界情况 - 只有两根柱子"""
    print("=== 测试用例2：边界情况 - 只有两根柱子 ===")
    solution = Solution()
    
    height2 = [1, 2]
    print("height = ", end="")
    print_array(height2)
    
    result1 = solution.trapTwoPointers(height2)
    result2 = solution.trapDynamicProgramming(height2)
    result3 = solution.trapMonotonicStack(height2)
    
    print(f"双指针法结果: {result1}")  # 预期输出: 0
    print(f"动态规划结果: {result2}")  # 预期输出: 0
    print(f"单调栈结果: {result3}")  # 预期输出: 0
    print()


def test_edge_case_2():
    """测试用例3：边界情况 - 单调递增数组"""
    print("=== 测试用例3：边界情况 - 单调递增数组 ===")
    solution = Solution()
    
    height3 = [1, 2, 3, 4, 5]
    print("height = ", end="")
    print_array(height3)
    
    result1 = solution.trapTwoPointers(height3)
    result2 = solution.trapDynamicProgramming(height3)
    result3 = solution.trapMonotonicStack(height3)
    
    print(f"双指针法结果: {result1}")  # 预期输出: 0
    print(f"动态规划结果: {result2}")  # 预期输出: 0
    print(f"单调栈结果: {result3}")  # 预期输出: 0
    print()


def test_edge_case_3():
    """测试用例4：边界情况 - 单调递减数组"""
    print("=== 测试用例4：边界情况 - 单调递减数组 ===")
    solution = Solution()
    
    height4 = [5, 4, 3, 2, 1]
    print("height = ", end="")
    print_array(height4)
    
    result1 = solution.trapTwoPointers(height4)
    result2 = solution.trapDynamicProgramming(height4)
    result3 = solution.trapMonotonicStack(height4)
    
    print(f"双指针法结果: {result1}")  # 预期输出: 0
    print(f"动态规划结果: {result2}")  # 预期输出: 0
    print(f"单调栈结果: {result3}")  # 预期输出: 0
    print()


def test_performance():
    """性能测试"""
    print("=== 性能测试 ===")
    import time
    
    solution = Solution()
    n = 20000
    # 生成测试数据：波峰波谷交替
    height5 = [min(i, n - i) for i in range(n)]  # 形成一个山峰形状
    
    start_time = time.time()
    result1 = solution.trapTwoPointers(height5)
    time1 = (time.time() - start_time) * 1000  # 转换为毫秒
    
    start_time = time.time()
    result2 = solution.trapDynamicProgramming(height5)
    time2 = (time.time() - start_time) * 1000  # 转换为毫秒
    
    start_time = time.time()
    result3 = solution.trapMonotonicStack(height5)
    time3 = (time.time() - start_time) * 1000  # 转换为毫秒
    
    print(f"双指针法结果: {result1}, 耗时: {time1:.2f}ms")
    print(f"动态规划结果: {result2}, 耗时: {time2:.2f}ms")
    print(f"单调栈结果: {result3}, 耗时: {time3:.2f}ms")
    print()


def main():
    """主函数：执行所有测试用例"""
    try:
        test_normal_case()
        test_edge_case_1()
        test_edge_case_2()
        test_edge_case_3()
        test_performance()
        print("所有测试用例执行完成！")
    except Exception as e:
        print(f"测试过程中出现异常: {e}")


if __name__ == "__main__":
    main()