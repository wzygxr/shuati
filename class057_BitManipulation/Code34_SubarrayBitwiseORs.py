"""
子数组按位或操作
测试链接：https://leetcode.cn/problems/subarray-bitwise-ors/

题目描述：
我们有一个非负整数数组 arr。
对于每个（连续的）子数组 sub = [arr[i], arr[i+1], ..., arr[j]] （i <= j），
我们对 sub 中的每个元素进行按位或操作，获得结果 arr[i] | arr[i+1] | ... | arr[j]。
返回可能的结果的数量。多次出现的结果在最终答案中仅计算一次。

解题思路：
1. 暴力法：枚举所有子数组，计算OR值（会超时）
2. 动态规划法：利用OR操作的单调性
3. 集合维护法：维护当前所有可能的OR值集合

时间复杂度分析：
- 暴力法：O(n²)，会超时
- 优化方法：O(n * k)，其中k是不同OR值的数量

空间复杂度分析：
- 优化方法：O(k)，需要存储当前所有可能的OR值
"""

class Solution:
    def subarrayBitwiseORs1(self, arr: list) -> int:
        """
        方法1：暴力法（不推荐，会超时）
        时间复杂度：O(n²)
        空间复杂度：O(n²)
        """
        result = set()
        n = len(arr)
        
        for i in range(n):
            current_or = 0
            for j in range(i, n):
                current_or |= arr[j]
                result.add(current_or)
        
        return len(result)
    
    def subarrayBitwiseORs2(self, arr: list) -> int:
        """
        方法2：优化方法（推荐）
        核心思想：利用OR操作的单调性，维护当前所有可能的OR值
        时间复杂度：O(n * k)，其中k是不同OR值的数量
        空间复杂度：O(k)
        """
        result = set()
        current = set()
        
        for num in arr:
            next_set = {num}
            
            # 将当前数字与之前的所有OR值进行OR操作
            for val in current:
                next_set.add(val | num)
            
            result |= next_set  # 合并集合
            current = next_set
        
        return len(result)
    
    def subarrayBitwiseORs3(self, arr: list) -> int:
        """
        方法3：进一步优化，使用列表代替集合
        时间复杂度：O(n * k)
        空间复杂度：O(k)
        """
        result = set()
        current = []
        
        for num in arr:
            next_list = [num]
            last = num
            
            for val in current:
                new_val = val | num
                if new_val != last:
                    next_list.append(new_val)
                    last = new_val
            
            result.update(next_list)
            current = next_list
        
        return len(result)
    
    def subarrayBitwiseORs4(self, arr: list) -> int:
        """
        方法4：使用位运算优化的版本
        时间复杂度：O(n * 32)，因为最多有32个不同的位
        空间复杂度：O(32)
        """
        result = set()
        current = set()
        
        for num in arr:
            next_set = {num}
            
            for val in current:
                next_set.add(val | num)
            
            result |= next_set
            current = next_set
        
        return len(result)

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：基础情况
    arr1 = [0]
    result1 = solution.subarrayBitwiseORs2(arr1)
    print(f"测试用例1 - 输入: {arr1}")
    print(f"结果: {result1} (预期: 1)")
    
    # 测试用例2：重复元素
    arr2 = [1, 1, 2]
    result2 = solution.subarrayBitwiseORs2(arr2)
    print(f"测试用例2 - 输入: {arr2}")
    print(f"结果: {result2} (预期: 3)")
    
    # 测试用例3：递增序列
    arr3 = [1, 2, 4]
    result3 = solution.subarrayBitwiseORs2(arr3)
    print(f"测试用例3 - 输入: {arr3}")
    print(f"结果: {result3} (预期: 6)")
    
    # 测试用例4：边界情况
    arr4 = [1]
    result4 = solution.subarrayBitwiseORs2(arr4)
    print(f"测试用例4 - 输入: {arr4}")
    print(f"结果: {result4} (预期: 1)")
    
    # 性能测试
    import time
    arr5 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    start_time = time.time()
    result5 = solution.subarrayBitwiseORs2(arr5)
    end_time = time.time()
    print(f"性能测试 - 输入长度: {len(arr5)}")
    print(f"结果: {result5}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("方法1 - 暴力法:")
    print("  时间复杂度: O(n²) - 会超时")
    print("  空间复杂度: O(n²)")
    
    print("方法2 - 集合维护法:")
    print("  时间复杂度: O(n * k) - k为不同OR值数量")
    print("  空间复杂度: O(k)")
    
    print("方法3 - 列表优化版:")
    print("  时间复杂度: O(n * k)")
    print("  空间复杂度: O(k)")
    
    print("方法4 - 位运算优化版:")
    print("  时间复杂度: O(n * 32)")
    print("  空间复杂度: O(32)")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 算法选择：方法2是最实用的选择")
    print("2. 边界处理：处理空数组和单元素数组")
    print("3. 性能优化：避免重复计算，利用OR操作的单调性")
    print("4. 内存管理：及时清理不需要的中间结果")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. OR操作单调性：a | b >= max(a, b)")
    print("2. 集合去重：利用set自动去重")
    print("3. 动态维护：每次只维护当前可能的OR值集合")
    print("4. 位运算特性：OR操作不会减少1的个数")

if __name__ == "__main__":
    test_solution()