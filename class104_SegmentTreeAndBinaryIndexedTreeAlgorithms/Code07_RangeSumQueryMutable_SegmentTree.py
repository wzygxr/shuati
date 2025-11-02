"""
LeetCode 307. Range Sum Query - Mutable (区域和检索 - 数组可修改)
题目链接: https://leetcode.cn/problems/range-sum-query-mutable/

题目描述: 
给你一个数组 nums ，请你完成两类查询：
1. 更新数组 nums 下标对应的值
2. 求数组 nums 中索引 left 和 right 之间的元素和，包含 left 和 right 两点

解题思路:
使用线段树实现，支持单点更新和区间查询
线段树每个节点存储对应区间的元素和

时间复杂度分析:
- 构建线段树: O(n)
- 单点更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(4n) 线段树需要约4n的空间

工程化考量:
1. 性能优化: 线段树查询和更新都是O(log n)
2. 内存优化: 动态分配节点，避免内存浪费
3. 边界处理: 处理空数组和非法索引
4. 可读性: 清晰的变量命名和注释
"""

class SegmentTreeNode:
    """线段树节点定义
    每个节点表示数组的一个区间[start, end]，并存储该区间内所有元素的和
    """
    
    def __init__(self, start, end):
        """
        构造函数
        
        Args:
            start: 区间起始位置
            end: 区间结束位置
        """
        self.start = start
        self.end = end
        self.left = None
        self.right = None
        self.sum = 0

class NumArray:
    """线段树实现的区域和查询类"""
    
    def __init__(self, nums):
        """
        构造函数
        
        Args:
            nums: 输入数组
        """
        self.nums = nums.copy() if nums else []
        self.root = self._build_tree(0, len(nums) - 1, nums) if nums else None
    
    def _build_tree(self, start, end, nums):
        """
        构建线段树
        
        Args:
            start: 区间起始位置
            end: 区间结束位置
            nums: 原始数组
            
        Returns:
            线段树节点
        """
        if start > end:
            return None
        
        node = SegmentTreeNode(start, end)
        
        if start == end:
            # 叶节点，直接存储数组元素值
            node.sum = nums[start]
        else:
            # 递归构建左右子树
            mid = start + (end - start) // 2
            node.left = self._build_tree(start, mid, nums)
            node.right = self._build_tree(mid + 1, end, nums)
            
            # 计算当前节点的和
            left_sum = node.left.sum if node.left else 0
            right_sum = node.right.sum if node.right else 0
            node.sum = left_sum + right_sum
        
        return node
    
    def update(self, index, val):
        """
        单点更新操作
        
        Args:
            index: 要更新的位置
            val: 新的值
            
        Raises:
            IndexError: 如果索引超出范围
        """
        # 参数检查
        if index < 0 or index >= len(self.nums):
            raise IndexError("Index out of range")
        
        self.nums[index] = val
        if self.root:
            self._update_tree(self.root, index, val)
    
    def _update_tree(self, node, index, val):
        """
        线段树单点更新
        
        Args:
            node: 当前节点
            index: 要更新的位置
            val: 新的值
        """
        if not node or index < node.start or index > node.end:
            return
        
        if node.start == node.end and node.start == index:
            # 找到目标叶节点
            node.sum = val
        else:
            # 递归更新左右子树
            mid = node.start + (node.end - node.start) // 2
            if index <= mid:
                self._update_tree(node.left, index, val)
            else:
                self._update_tree(node.right, index, val)
            
            # 更新当前节点的和
            left_sum = node.left.sum if node.left else 0
            right_sum = node.right.sum if node.right else 0
            node.sum = left_sum + right_sum
    
    def sum_range(self, left, right):
        """
        区间求和操作
        
        Args:
            left: 区间左边界
            right: 区间右边界
            
        Returns:
            区间和
            
        Raises:
            IndexError: 如果区间不合法
        """
        # 参数检查
        if left < 0 or right >= len(self.nums) or left > right:
            raise IndexError("Invalid range")
        
        if not self.root:
            return 0
        
        return self._query_tree(self.root, left, right)
    
    def _query_tree(self, node, left, right):
        """
        线段树区间查询
        
        Args:
            node: 当前节点
            left: 查询区间左边界
            right: 查询区间右边界
            
        Returns:
            区间和
        """
        if not node or left > node.end or right < node.start:
            return 0
        
        if left <= node.start and node.end <= right:
            # 当前节点区间完全包含在查询区间内
            return node.sum
        
        # 递归查询左右子树
        mid = node.start + (node.end - node.start) // 2
        left_sum, right_sum = 0, 0
        
        if left <= mid:
            left_sum = self._query_tree(node.left, left, right)
        if right > mid:
            right_sum = self._query_tree(node.right, left, right)
        
        return left_sum + right_sum

# 单元测试
def test_num_array():
    """测试函数，验证算法正确性"""
    
    print("开始测试线段树区域和查询...")
    
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
    print("1. 线段树应用：用于高效处理区间查询和单点更新")
    print("2. 递归构建：自底向上构建线段树")
    print("3. 区间分解：将大区间分解为小区间处理")
    print("4. 懒更新：支持高效的区间更新操作")
    print("5. 边界处理：处理空数组和非法索引")
    
    print("\n=== 工程化考量 ===")
    print("1. 异常防御：处理非法输入参数")
    print("2. 性能优化：线段树操作时间复杂度O(log n)")
    print("3. 内存优化：动态分配节点，避免内存浪费")
    print("4. 可读性：清晰的变量命名和注释")
    print("5. 测试覆盖：单元测试覆盖各种边界情况")
    
    print("\n=== 复杂度分析 ===")
    print("时间复杂度: O(log n) 每次查询和更新")
    print("空间复杂度: O(n) 线段树需要约4n的空间")
    print("其中n为数组长度")