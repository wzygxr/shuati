from collections import deque
import random
import time
from typing import List

class Code16_SlidingWindowMaximum:
    """
    滑动窗口最大值 - Python实现
    
    题目描述：
    给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
    你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
    返回滑动窗口中的最大值。
    
    测试链接：https://leetcode.cn/problems/sliding-window-maximum/
    题目来源：LeetCode
    难度：困难
    
    核心算法：单调队列（双端队列实现）
    
    解题思路：
    使用单调递减双端队列来维护当前窗口中的最大值候选者。
    队列中存储的是数组元素的索引，而不是元素值本身，这样可以方便判断元素是否在窗口内。
    
    具体步骤：
    1. 初始化一个双端队列用于存储索引
    2. 遍历数组中的每个元素：
        a. 移除队列中不在当前窗口范围内的索引（从队首移除）
        b. 从队尾开始移除所有小于当前元素的索引，保持队列单调递减
        c. 将当前元素索引入队
        d. 当窗口形成时（i >= k-1），记录当前窗口的最大值（队首元素）
    
    时间复杂度分析：
    O(n) - 每个元素最多入队和出队各一次，n为数组长度
    
    空间复杂度分析：
    O(k) - 队列最多存储k个元素
    
    是否为最优解：
    是，这是解决该问题的最优解之一
    
    Python语言特性：
    - 使用collections.deque实现双端队列
    - 使用类型注解提高代码可读性
    - 使用列表推导式简化代码
    - 使用time模块进行性能测试
    """
    
    @staticmethod
    def max_sliding_window(nums: List[int], k: int) -> List[int]:
        """
        滑动窗口最大值主函数
        
        Args:
            nums: 整数数组
            k: 窗口大小
            
        Returns:
            滑动窗口最大值数组
            
        Raises:
            无显式异常抛出，但会处理边界情况
        """
        # 边界条件检查
        if not nums or k <= 0 or k > len(nums):
            return []
        
        n = len(nums)
        result = []
        
        # 使用双端队列存储索引，维护单调递减队列
        dq = deque()
        
        for i in range(n):
            # 步骤1：移除队列中不在当前窗口范围内的索引
            while dq and dq[0] < i - k + 1:
                dq.popleft()
            
            # 步骤2：从队尾开始移除所有小于当前元素的索引
            while dq and nums[dq[-1]] < nums[i]:
                dq.pop()
            
            # 步骤3：将当前索引入队
            dq.append(i)
            
            # 步骤4：当窗口形成时，记录当前窗口的最大值
            if i >= k - 1:
                result.append(nums[dq[0]])
        
        return result
    
    @staticmethod
    def test_max_sliding_window():
        """
        单元测试方法
        包含多种测试场景验证算法正确性
        """
        print("=== 滑动窗口最大值单元测试 ===")
        
        # 测试用例1：常规情况
        nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
        k1 = 3
        result1 = Code16_SlidingWindowMaximum.max_sliding_window(nums1, k1)
        print(f"测试用例1: {nums1}, k={k1}")
        print(f"输出: {result1}")
        print("期望: [3, 3, 5, 5, 6, 7]")
        
        # 测试用例2：k=1的情况
        nums2 = [1, 2, 3, 4, 5]
        k2 = 1
        result2 = Code16_SlidingWindowMaximum.max_sliding_window(nums2, k2)
        print(f"测试用例2: {nums2}, k={k2}")
        print(f"输出: {result2}")
        print("期望: [1, 2, 3, 4, 5]")
        
        # 测试用例3：k等于数组长度
        nums3 = [9, 8, 7, 6, 5]
        k3 = 5
        result3 = Code16_SlidingWindowMaximum.max_sliding_window(nums3, k3)
        print(f"测试用例3: {nums3}, k={k3}")
        print(f"输出: {result3}")
        print("期望: [9]")
        
        # 测试用例4：单调递增数组
        nums4 = [1, 2, 3, 4, 5, 6]
        k4 = 3
        result4 = Code16_SlidingWindowMaximum.max_sliding_window(nums4, k4)
        print(f"测试用例4: {nums4}, k={k4}")
        print(f"输出: {result4}")
        print("期望: [3, 4, 5, 6]")
        
        # 测试用例5：单调递减数组
        nums5 = [6, 5, 4, 3, 2, 1]
        k5 = 3
        result5 = Code16_SlidingWindowMaximum.max_sliding_window(nums5, k5)
        print(f"测试用例5: {nums5}, k={k5}")
        print(f"输出: {result5}")
        print("期望: [6, 5, 4, 3]")
        
        # 测试用例6：边界情况 - 空数组
        nums6 = []
        k6 = 3
        result6 = Code16_SlidingWindowMaximum.max_sliding_window(nums6, k6)
        print(f"测试用例6: 空数组, k={k6}")
        print(f"输出: {result6}")
        print("期望: []")
        
        # 测试用例7：包含重复元素
        nums7 = [1, 3, 3, 2, 5, 5, 4]
        k7 = 3
        result7 = Code16_SlidingWindowMaximum.max_sliding_window(nums7, k7)
        print(f"测试用例7: {nums7}, k={k7}")
        print(f"输出: {result7}")
        print("期望: [3, 3, 3, 5, 5]")
    
    @staticmethod
    def performance_test():
        """
        性能测试方法
        测试算法在大规模数据下的性能表现
        """
        print("\n=== 性能测试 ===")
        
        # 生成大规模测试数据
        n = 100000
        large_nums = [random.randint(0, 10000) for _ in range(n)]
        k = 1000
        
        start_time = time.time()
        result = Code16_SlidingWindowMaximum.max_sliding_window(large_nums, k)
        end_time = time.time()
        
        print(f"数据规模: {n}, 窗口大小: {k}")
        print(f"执行时间: {(end_time - start_time) * 1000:.2f}ms")
        print(f"结果数组长度: {len(result)}")
    
    @staticmethod
    def run():
        """
        主运行函数
        """
        # 运行单元测试
        Code16_SlidingWindowMaximum.test_max_sliding_window()
        
        # 运行性能测试
        Code16_SlidingWindowMaximum.performance_test()
        
        print("\n=== 算法验证完成 ===")

# 程序入口点
if __name__ == "__main__":
    Code16_SlidingWindowMaximum.run()