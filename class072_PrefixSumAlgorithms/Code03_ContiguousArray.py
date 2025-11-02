"""
连续数组 (Contiguous Array)

题目描述:
给定一个二进制数组 nums，找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。

示例:
输入: nums = [0,1]
输出: 2
说明: [0, 1] 是具有相同数量0和1的最长连续子数组。

输入: nums = [0,1,0]
输出: 2
说明: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。

提示:
1 <= nums.length <= 10^5
nums[i] 不是 0 就是 1

题目链接: https://leetcode.com/problems/contiguous-array/

解题思路:
1. 将0看作-1，问题转化为求和为0的最长子数组
2. 使用前缀和 + 哈希表的方法
3. 遍历数组，计算前缀和
4. 如果某个前缀和之前出现过，说明这两个位置之间的子数组和为0
5. 使用哈希表记录每个前缀和第一次出现的位置

时间复杂度: O(n) - 需要遍历数组一次
空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和

工程化考量:
1. 边界条件处理：空数组、单元素数组
2. 映射技巧：0→-1, 1→1的数学变换
3. 哈希表初始化：空前缀和为0出现在位置-1
4. 性能优化：一次遍历完成所有计算

最优解分析:
这是最优解，因为必须遍历所有元素才能找到最长子数组。
哈希表方法将时间复杂度从O(n^2)优化到O(n)。

算法核心:
设count为前缀和（0→-1, 1→1），则子数组[i,j]满足：
count[j] - count[i-1] = 0 → count[j] = count[i-1]
因此记录每个count值第一次出现的位置，找到相同count值的最远距离。

算法调试技巧:
1. 打印中间过程：可以在循环中打印每个位置的前缀和和哈希表状态
2. 边界测试：测试空数组、全0数组、全1数组等特殊情况
3. 性能测试：测试大规模数据下的性能表现

语言特性差异:
Python是动态类型语言，无需显式声明变量类型。
Python字典自动处理哈希冲突，语法简洁。
与Java/C++相比，Python有更简洁的语法和内置的数据结构操作。
"""

class Solution:
    def findMaxLength(self, nums):
        """
        找到含有相同数量0和1的最长连续子数组的长度
        
        Args:
            nums (List[int]): 输入的二进制数组
            
        Returns:
            int: 最长连续子数组的长度
            
        异常场景处理:
        - 空数组：返回0
        - 单元素数组：返回0（不可能有相同数量的0和1）
        - 全0或全1数组：返回0
        
        边界条件:
        - 数组长度为0或1
        - 数组元素全为0或全为1
        - 数组元素包含非0非1值（题目保证只有0和1）
        """
        # 边界情况处理
        if not nums or len(nums) <= 1:
            return 0
        
        # 字典记录前缀和及其第一次出现的位置
        # 初始化：前缀和为0在位置-1出现（便于计算长度）
        # 数学原理：count[j] - count[i-1] = 0 → count[j] = count[i-1]
        prefix_sum_index = {0: -1}
        
        max_length = 0  # 最长子数组长度
        sum_val = 0     # 当前前缀和（0看作-1）
        
        # 遍历数组，时间复杂度O(n)
        for i, num in enumerate(nums):
            # 更新前缀和：0看作-1，1看作1
            # 这样相同数量的0和1对应和为0
            sum_val += -1 if num == 0 else 1
            
            # 调试打印：显示中间过程（调试时取消注释）
            # print(f"位置 {i}: 数字 = {num}, 前缀和 = {sum_val}")
            
            # 如果当前前缀和之前出现过，更新最大长度
            if sum_val in prefix_sum_index:
                # 计算当前子数组长度：i - 第一次出现的位置
                length = i - prefix_sum_index[sum_val]
                max_length = max(max_length, length)
                # 调试打印：找到子数组（调试时取消注释）
                # print(f"找到子数组 [{prefix_sum_index[sum_val]+1}, {i}], 长度 = {length}")
            else:
                # 记录当前前缀和第一次出现的位置
                prefix_sum_index[sum_val] = i
                # 调试打印：记录新前缀和（调试时取消注释）
                # print(f"记录前缀和 {sum_val} 出现在位置 {i}")
        
        return max_length


def test_contiguous_array():
    """单元测试函数"""
    print("=== 连续数组单元测试 ===")
    solution = Solution()
    
    # 测试用例1：基础情况
    nums1 = [0, 1]
    result1 = solution.findMaxLength(nums1)
    print(f"测试用例1 [0,1]: {result1} (预期: 2)")
    
    # 测试用例2：多个元素
    nums2 = [0, 1, 0]
    result2 = solution.findMaxLength(nums2)
    print(f"测试用例2 [0,1,0]: {result2} (预期: 2)")
    
    # 测试用例3：完整数组
    nums3 = [0, 1, 0, 1]
    result3 = solution.findMaxLength(nums3)
    print(f"测试用例3 [0,1,0,1]: {result3} (预期: 4)")
    
    # 测试用例4：空数组
    nums4 = []
    result4 = solution.findMaxLength(nums4)
    print(f"测试用例4 []: {result4} (预期: 0)")
    
    # 测试用例5：单元素数组
    nums5 = [0]
    result5 = solution.findMaxLength(nums5)
    print(f"测试用例5 [0]: {result5} (预期: 0)")
    
    # 测试用例6：全0数组
    nums6 = [0, 0, 0]
    result6 = solution.findMaxLength(nums6)
    print(f"测试用例6 [0,0,0]: {result6} (预期: 0)")
    
    # 测试用例7：全1数组
    nums7 = [1, 1, 1]
    result7 = solution.findMaxLength(nums7)
    print(f"测试用例7 [1,1,1]: {result7} (预期: 0)")
    
    # 测试用例8：复杂情况
    nums8 = [0, 1, 0, 0, 1, 1, 0]
    result8 = solution.findMaxLength(nums8)
    print(f"测试用例8 [0,1,0,0,1,1,0]: {result8} (预期: 6)")

def performance_test():
    """性能测试函数"""
    print("\n=== 性能测试 ===")
    solution = Solution()
    size = 100000  # 10万元素
    import random
    large_array = [random.randint(0, 1) for _ in range(size)]
    
    import time
    start_time = time.time()
    result = solution.findMaxLength(large_array)
    end_time = time.time()
    
    print(f"处理 {size} 个元素，最长子数组长度: {result}, 耗时: {end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_contiguous_array()
    
    # 运行性能测试（可选）
    # performance_test()
    
    print("\n=== 测试完成 ===")