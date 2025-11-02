"""
LeetCode 307. Range Sum Query - Mutable (区域和检索 - 数组可修改) - 树状数组解法
题目链接: https://leetcode.cn/problems/range-sum-query-mutable/

题目描述: 
给你一个数组 nums ，请你完成两类查询：
1. 更新数组 nums 下标对应的值
2. 求数组 nums 中索引 left 和 right 之间的元素和，包含 left 和 right 两点

解题思路:
使用树状数组（Binary Indexed Tree/Fenwick Tree）实现
树状数组支持单点更新和前缀和查询，通过前缀和差值计算区间和

时间复杂度分析:
- 构建树状数组: O(n log n)
- 单点更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(n) 树状数组只需要n+1的空间

工程化考量:
1. 性能优化: 树状数组查询和更新都是O(log n)
2. 内存优化: 树状数组空间复杂度O(n)
3. 边界处理: 处理空数组和非法索引
4. 可读性: 清晰的变量命名和注释
"""

class NumArray:
    """树状数组实现的区域和查询类"""
    
    def __init__(self, nums):
        """
        构造函数，根据给定数组构建树状数组
        
        Args:
            nums: 初始数组
        """
        self.n = len(nums)
        self.nums = nums.copy() if nums else []
        # 树状数组索引从1开始，所以需要n+1的长度
        self.tree = [0] * (self.n + 1)
        
        # 初始化树状数组，将每个元素添加到树状数组中
        for i in range(self.n):
            self._add(i + 1, nums[i])
    
    def _lowbit(self, x):
        """
        计算x的最低位1所代表的值
        这是树状数组的核心操作，用于确定节点的父节点和子节点关系
        
        Args:
            x: 输入值
            
        Returns:
            最低位1的值
        """
        return x & -x
    
    def _add(self, index, delta):
        """
        树状数组单点更新操作
        将位置index的值增加delta
        
        Args:
            index: 要更新的位置（树状数组索引从1开始）
            delta: 增量值
        """
        while index <= self.n:
            self.tree[index] += delta
            index += self._lowbit(index)
    
    def _prefix_sum(self, index):
        """
        树状数组前缀和查询
        查询前index个元素的和
        
        Args:
            index: 查询结束位置（树状数组索引从1开始）
            
        Returns:
            前缀和
        """
        sum_val = 0
        while index > 0:
            sum_val += self.tree[index]
            index -= self._lowbit(index)
        return sum_val
    
    def update(self, index, val):
        """
        单点更新操作
        将位置index的值更新为val
        
        Args:
            index: 要更新的位置（数组索引从0开始）
            val: 新的值
            
        Raises:
            IndexError: 如果索引超出范围
        """
        # 参数检查
        if index < 0 or index >= self.n:
            raise IndexError("Index out of range")
        
        # 计算增量值
        delta = val - self.nums[index]
        self.nums[index] = val
        
        # 更新树状数组
        self._add(index + 1, delta)
    
    def sum_range(self, left, right):
        """
        区间求和操作
        计算区间[left, right]内元素的和
        
        Args:
            left: 区间左边界
            right: 区间右边界
            
        Returns:
            区间和
            
        Raises:
            IndexError: 如果区间不合法
        """
        # 参数检查
        if left < 0 or right >= self.n or left > right:
            raise IndexError("Invalid range")
        
        # 使用前缀和差值计算区间和
        # sum_range(left, right) = prefix_sum(right+1) - prefix_sum(left)
        return self._prefix_sum(right + 1) - self._prefix_sum(left)
    
    def get_tree(self):
        """
        获取树状数组状态（用于调试）
        
        Returns:
            树状数组内容
        """
        return self.tree
    
    def get_nums(self):
        """
        获取原始数组状态（用于调试）
        
        Returns:
            原始数组内容
        """
        return self.nums

# 单元测试
def test_num_array():
    """测试函数，验证算法正确性"""
    
    print("开始测试树状数组区域和查询...")
    
    # 测试用例1: 正常情况
    nums1 = [1, 3, 5]
    num_array1 = NumArray(nums1)
    
    print("测试用例1: 初始数组 [1, 3, 5]")
    result1 = num_array1.sum_range(0, 2)
    print(f"sum_range(0, 2) = {result1} (期望: 9)")
    assert result1 == 9, f"预期9，实际{result1}"
    
    num_array1.update(1, 2)
    result1_updated = num_array1.sum_range(0, 2)
    print(f"更新index=1为2后，sum_range(0, 2) = {result1_updated} (期望: 8)")
    assert result1_updated == 8, f"预期8，实际{result1_updated}"
    
    # 测试用例2: 空数组
    nums2 = []
    num_array2 = NumArray(nums2)
    
    try:
        num_array2.sum_range(0, 0)
        assert False, "应该抛出异常"
    except IndexError as e:
        print(f"测试用例2: 空数组异常处理通过 - {e}")
    
    # 测试用例3: 单元素数组
    nums3 = [7]
    num_array3 = NumArray(nums3)
    
    print("测试用例3: 单元素数组 [7]")
    result3 = num_array3.sum_range(0, 0)
    print(f"sum_range(0, 0) = {result3} (期望: 7)")
    assert result3 == 7, f"预期7，实际{result3}"
    
    num_array3.update(0, 10)
    result3_updated = num_array3.sum_range(0, 0)
    print(f"更新index=0为10后，sum_range(0, 0) = {result3_updated} (期望: 10)")
    assert result3_updated == 10, f"预期10，实际{result3_updated}"
    
    # 测试用例4: 边界情况
    nums4 = [1, 2, 3, 4, 5]
    num_array4 = NumArray(nums4)
    
    result4 = num_array4.sum_range(1, 3)
    print(f"测试用例4: [1, 2, 3, 4, 5], sum_range(1, 3) = {result4} (期望: 9)")
    assert result4 == 9, f"预期9，实际{result4}"
    
    print("所有测试用例通过！")

# 性能测试
def performance_test():
    """性能测试函数"""
    
    print("开始性能测试...")
    
    import time
    
    # 大规模数据测试
    large_nums = list(range(10000))
    num_array = NumArray(large_nums)
    
    # 测试查询性能
    start_time = time.time()
    for _ in range(1000):
        num_array.sum_range(0, 9999)
    query_time = time.time() - start_time
    
    # 测试更新性能
    start_time = time.time()
    for i in range(1000):
        num_array.update(i % 10000, i)
    update_time = time.time() - start_time
    
    print(f"大规模测试: 数组长度{len(large_nums)}")
    print(f"1000次查询耗时: {query_time:.4f}秒")
    print(f"1000次更新耗时: {update_time:.4f}秒")

if __name__ == "__main__":
    # 运行测试
    test_num_array()
    
    # 性能测试
    performance_test()
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 树状数组应用：用于高效处理前缀和查询和单点更新")
    print("2. 二进制索引：利用二进制位运算实现高效更新和查询")
    print("3. 前缀和差值：通过前缀和差值计算任意区间和")
    print("4. 空间优化：相比线段树，树状数组空间复杂度更低")
    print("5. 边界处理：处理空数组和非法索引")
    
    print("\n=== 工程化考量 ===")
    print("1. 异常防御：处理非法输入参数")
    print("2. 性能优化：树状数组操作时间复杂度O(log n)")
    print("3. 内存优化：树状数组空间复杂度O(n)")
    print("4. 可读性：清晰的变量命名和注释")
    print("5. 测试覆盖：单元测试覆盖各种边界情况")
    
    print("\n=== 复杂度分析 ===")
    print("时间复杂度: O(log n) 每次查询和更新")
    print("空间复杂度: O(n) 树状数组需要n+1的空间")
    print("其中n为数组长度")
    
    print("\n=== 与线段树对比 ===")
    print("优势：")
    print("1. 代码更简洁，实现更简单")
    print("2. 空间复杂度更低（O(n) vs O(4n))")
    print("3. 常数因子更小，实际运行更快")
    print("劣势：")
    print("1. 不支持区间更新操作")
    print("2. 不支持复杂的区间查询（如区间最大值）")
    print("3. 只能处理前缀和相关的查询")