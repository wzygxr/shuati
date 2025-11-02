# LeetCode 307. Range Sum Query - Mutable
# 题目描述：给定一个整数数组 nums，实现两个函数：
# 1. update(i, val)：将数组中索引 i 处的值更新为 val
# 2. sumRange(i, j)：返回数组中从索引 i 到 j 的元素之和
# 题目链接：https://leetcode.com/problems/range-sum-query-mutable/
# 解题思路：使用线段树或树状数组实现，本题同时提供两种实现方式

class Code15_RangeSumQueryMutable:
    """
    使用线段树实现区间和查询与单点更新
    
    时间复杂度：
    - 构建线段树：O(n)
    - 单点更新：O(log n)
    - 区间查询：O(log n)
    空间复杂度：O(n)
    """
    
    def __init__(self, nums):
        """
        构造函数
        :param nums: 输入数组
        """
        self.n = len(nums)
        # 线段树数组大小为4n，确保足够空间
        self.tree = [0] * (4 * self.n)
        if self.n > 0:
            # 构建线段树
            self._build_tree(nums, 0, 0, self.n - 1)
    
    def _build_tree(self, nums, node, start, end):
        """
        构建线段树
        :param nums: 原始数组
        :param node: 当前节点索引
        :param start: 当前区间左边界
        :param end: 当前区间右边界
        """
        if start == end:
            # 叶子节点，直接赋值
            self.tree[node] = nums[start]
            return
        
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        # 递归构建左右子树
        self._build_tree(nums, left_node, start, mid)
        self._build_tree(nums, right_node, mid + 1, end)
        
        # 当前节点的值为左右子节点之和
        self.tree[node] = self.tree[left_node] + self.tree[right_node]
    
    def _update_tree(self, node, start, end, index, val):
        """
        单点更新（内部方法）
        :param node: 当前节点索引
        :param start: 当前区间左边界
        :param end: 当前区间右边界
        :param index: 要更新的元素索引
        :param val: 新值
        """
        if start == end:
            # 到达叶子节点，更新值
            self.tree[node] = val
            return
        
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        # 根据index所在的区间决定递归左子树还是右子树
        if index <= mid:
            self._update_tree(left_node, start, mid, index, val)
        else:
            self._update_tree(right_node, mid + 1, end, index, val)
        
        # 更新当前节点的值
        self.tree[node] = self.tree[left_node] + self.tree[right_node]
    
    def _query_tree(self, node, start, end, left, right):
        """
        区间查询（内部方法）
        :param node: 当前节点索引
        :param start: 当前区间左边界
        :param end: 当前区间右边界
        :param left: 查询区间的左边界
        :param right: 查询区间的右边界
        :return: 查询区间的和
        """
        # 查询区间与当前区间无交集
        if right < start or left > end:
            return 0
        
        # 当前区间完全包含在查询区间内
        if left <= start and end <= right:
            return self.tree[node]
        
        # 查询区间部分重叠，递归查询左右子树
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        left_sum = self._query_tree(left_node, start, mid, left, right)
        right_sum = self._query_tree(right_node, mid + 1, end, left, right)
        
        return left_sum + right_sum
    
    def update(self, index, val):
        """
        单点更新公共接口
        :param index: 要更新的元素索引
        :param val: 新值
        :raises ValueError: 当索引超出范围时
        """
        if index < 0 or index >= self.n:
            raise ValueError("索引超出范围")
        self._update_tree(0, 0, self.n - 1, index, val)
    
    def sum_range(self, left, right):
        """
        区间和查询公共接口
        :param left: 查询区间的左边界
        :param right: 查询区间的右边界
        :return: 查询区间的和
        :raises ValueError: 当查询区间无效时
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询区间无效")
        return self._query_tree(0, 0, self.n - 1, left, right)

class NumArrayBIT:
    """
    树状数组实现类
    
    树状数组(Binary Indexed Tree 或 Fenwick Tree)是一种高效处理前缀和查询和单点更新的数据结构
    
    时间复杂度：
    - 构建树状数组：O(n log n)
    - 单点更新：O(log n)
    - 前缀和查询：O(log n)
    - 区间和查询：O(log n)
    空间复杂度：O(n)
    """
    
    def __init__(self, nums):
        """
        构造函数
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.nums = nums.copy()
        self.bit = [0] * (self.n + 1)  # 树状数组从索引1开始
        
        # 初始化树状数组
        for i in range(self.n):
            self._update_bit(i, nums[i])
    
    def _lowbit(self, x):
        """
        lowbit操作，获取x二进制表示中最低位的1所对应的值
        :param x: 输入整数
        :return: 最低位的1对应的值
        """
        return x & (-x)
    
    def _update_bit(self, index, val):
        """
        单点更新（树状数组内部方法）
        :param index: 要更新的元素索引（0-based）
        :param val: 新值
        """
        # 将原数组中的增量累加到树状数组中
        delta = val
        index += 1  # 转换为1-based索引
        
        # 更新所有受影响的节点
        while index <= self.n:
            self.bit[index] += delta
            index += self._lowbit(index)
    
    def update(self, index, val):
        """
        更新元素值（公共接口）
        :param index: 要更新的元素索引
        :param val: 新值
        :raises ValueError: 当索引超出范围时
        """
        if index < 0 or index >= self.n:
            raise ValueError("索引超出范围")
        
        # 计算增量
        delta = val - self.nums[index]
        self.nums[index] = val  # 更新原数组
        
        # 更新树状数组
        index += 1  # 转换为1-based索引
        while index <= self.n:
            self.bit[index] += delta
            index += self._lowbit(index)
    
    def _prefix_sum(self, index):
        """
        前缀和查询（树状数组内部方法）
        :param index: 查询到index的前缀和（0-based）
        :return: 前缀和
        """
        index += 1  # 转换为1-based索引
        sum_val = 0
        
        # 累加所有包含的区间
        while index > 0:
            sum_val += self.bit[index]
            index -= self._lowbit(index)
        
        return sum_val
    
    def sum_range(self, left, right):
        """
        区间和查询（公共接口）
        :param left: 区间左边界
        :param right: 区间右边界
        :return: 区间和
        :raises ValueError: 当查询区间无效时
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询区间无效")
        
        # 区间和 = [0,right]的前缀和 - [0,left-1]的前缀和
        if left == 0:
            return self._prefix_sum(right)
        else:
            return self._prefix_sum(right) - self._prefix_sum(left - 1)

# 测试函数
def test_segment_tree():
    print("=== 线段树实现测试 ===")
    
    # 测试用例1：基本操作
    nums1 = [1, 3, 5, 7, 9, 11]
    seg_tree = Code15_RangeSumQueryMutable(nums1)
    
    print(f"原始数组: {nums1}")
    print(f"sum_range(0, 2) = {seg_tree.sum_range(0, 2)}")  # 应为9 (1+3+5)
    seg_tree.update(1, 10)  # 将索引1的值从3更新为10
    print("更新索引1为10后")
    print(f"sum_range(0, 2) = {seg_tree.sum_range(0, 2)}")  # 应为16 (1+10+5)
    print(f"sum_range(1, 5) = {seg_tree.sum_range(1, 5)}")  # 应为42 (10+5+7+9+11)
    
    # 测试用例2：边界情况
    nums2 = [5]
    seg_tree2 = Code15_RangeSumQueryMutable(nums2)
    print("\n测试边界情况")
    print(f"sum_range(0, 0) = {seg_tree2.sum_range(0, 0)}")  # 应为5
    seg_tree2.update(0, 10)
    print("更新索引0为10后")
    print(f"sum_range(0, 0) = {seg_tree2.sum_range(0, 0)}")  # 应为10
    
    # 测试用例3：空数组
    nums3 = []
    seg_tree3 = Code15_RangeSumQueryMutable(nums3)
    print("\n测试空数组")
    try:
        seg_tree3.sum_range(0, 0)
    except ValueError as e:
        print(f"正确处理空数组异常: {e}")

def test_bit():
    print("\n=== 树状数组实现测试 ===")
    
    # 测试用例1：基本操作
    nums1 = [1, 3, 5, 7, 9, 11]
    bit = NumArrayBIT(nums1)
    
    print(f"原始数组: {nums1}")
    print(f"sum_range(0, 2) = {bit.sum_range(0, 2)}")  # 应为9 (1+3+5)
    bit.update(1, 10)  # 将索引1的值从3更新为10
    print("更新索引1为10后")
    print(f"sum_range(0, 2) = {bit.sum_range(0, 2)}")  # 应为16 (1+10+5)
    print(f"sum_range(1, 5) = {bit.sum_range(1, 5)}")  # 应为42 (10+5+7+9+11)
    
    # 测试用例2：边界情况
    nums2 = [5]
    bit2 = NumArrayBIT(nums2)
    print("\n测试边界情况")
    print(f"sum_range(0, 0) = {bit2.sum_range(0, 0)}")  # 应为5
    bit2.update(0, 10)
    print("更新索引0为10后")
    print(f"sum_range(0, 0) = {bit2.sum_range(0, 0)}")  # 应为10

def performance_test():
    print("\n=== 性能对比测试 ===")
    
    # 创建较大的测试数组
    size = 100000
    nums = [i % 100 for i in range(size)]
    
    # 测试线段树性能
    import time
    start_time = time.time()
    seg_tree = Code15_RangeSumQueryMutable(nums)
    build_time = time.time() - start_time
    
    start_time = time.time()
    for i in range(10000):
        seg_tree.update(i % size, (i % size) + 100)
        seg_tree.sum_range((i * 2) % size, ((i * 3) % size + 100) % size)
    seg_tree_time = time.time() - start_time
    
    # 测试树状数组性能
    start_time = time.time()
    bit = NumArrayBIT(nums)
    bit_build_time = time.time() - start_time
    
    start_time = time.time()
    for i in range(10000):
        bit.update(i % size, (i % size) + 100)
        bit.sum_range((i * 2) % size, ((i * 3) % size + 100) % size)
    bit_time = time.time() - start_time
    
    print(f"数组大小: {size}")
    print(f"线段树构建时间: {build_time*1000:.2f}ms")
    print(f"树状数组构建时间: {bit_build_time*1000:.2f}ms")
    print(f"线段树10000次操作时间: {seg_tree_time*1000:.2f}ms")
    print(f"树状数组10000次操作时间: {bit_time*1000:.2f}ms")

# 运行测试
if __name__ == "__main__":
    test_segment_tree()
    test_bit()
    performance_test()

'''
算法总结与比较：

1. 线段树 vs 树状数组:
   - 线段树功能更强大，可以处理更复杂的区间操作（如区间最大值、区间最小值等）
   - 树状数组代码更简洁，常数因子更小，空间效率更高
   - 树状数组更适合处理前缀和查询和单点更新
   - 线段树更适合处理多种类型的区间查询和更新

2. 应用场景:
   - 树状数组适合: 前缀和查询、单点更新、逆序对计算等
   - 线段树适合: 区间最大值/最小值查询、区间和查询、区间更新等

3. 时间复杂度分析:
   - 线段树和树状数组的单点更新和查询操作都是O(log n)
   - 线段树的区间更新可以是O(log n)（使用懒惰传播），而树状数组只能高效处理特定类型的区间更新

4. 空间复杂度:
   - 线段树需要O(4n)的空间
   - 树状数组只需要O(n)的空间
'''