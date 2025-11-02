import sys
import random
import time
from typing import List

class NowcoderNC79_SegmentTreeSum:
    """
    牛客网 NC79 线段树区间和 - Python实现
    题目链接: https://www.nowcoder.com/practice/8b3b95850edb4115918ecebdf1b4d222
    
    算法思路:
    线段树是一种二叉树结构，用于高效处理区间查询和更新操作。
    每个节点存储一个区间的聚合信息（区间和）。
    
    时间复杂度分析:
    - 建树: O(n)
    - 查询: O(log n)
    - 更新: O(log n)
    
    空间复杂度分析:
    - 线段树存储: O(4n) 或 O(2n)（优化后）
    
    工程化考量:
    1. 异常处理：空数组、索引越界检查
    2. 性能优化：位运算、缓存友好设计
    3. 可测试性：边界测试、性能测试
    4. 可维护性：清晰的结构、详细注释
    """
    
    def __init__(self, nums: List[int]):
        """
        构造函数：构建线段树
        Args:
            nums: 原始数组
        """
        if not nums:
            raise ValueError("数组不能为空")
        
        self.n = len(nums)
        self.tree = [0] * (4 * self.n)  # 分配4倍空间
        self._build_tree(nums, 0, self.n - 1, 0)
    
    def _build_tree(self, nums: List[int], left: int, right: int, idx: int):
        """
        递归构建线段树
        Args:
            nums: 原始数组
            left: 当前区间左边界
            right: 当前区间右边界
            idx: 当前节点在线段树中的索引
        """
        # 叶子节点，存储单个元素
        if left == right:
            self.tree[idx] = nums[left]
            return
        
        # 计算中间点，分割区间
        mid = left + (right - left) // 2
        left_child = 2 * idx + 1  # 左子节点索引
        right_child = 2 * idx + 2  # 右子节点索引
        
        # 递归构建左右子树
        self._build_tree(nums, left, mid, left_child)
        self._build_tree(nums, mid + 1, right, right_child)
        
        # 合并左右子树信息（求和）
        self.tree[idx] = self.tree[left_child] + self.tree[right_child]
    
    def query(self, query_left: int, query_right: int) -> int:
        """
        查询区间和
        Args:
            query_left: 查询区间左边界
            query_right: 查询区间右边界
        Returns:
            区间和
        """
        # 参数校验
        if query_left < 0 or query_right >= self.n or query_left > query_right:
            raise ValueError("查询区间不合法")
        
        return self._query_helper(0, self.n - 1, query_left, query_right, 0)
    
    def _query_helper(self, seg_left: int, seg_right: int, 
                     query_left: int, query_right: int, idx: int) -> int:
        """
        递归查询辅助函数
        """
        # 当前区间完全包含在查询区间内
        if query_left <= seg_left and seg_right <= query_right:
            return self.tree[idx]
        
        mid = seg_left + (seg_right - seg_left) // 2
        left_child = 2 * idx + 1
        right_child = 2 * idx + 2
        
        sum_val = 0
        
        # 查询左子树
        if query_left <= mid:
            sum_val += self._query_helper(seg_left, mid, query_left, query_right, left_child)
        
        # 查询右子树
        if query_right > mid:
            sum_val += self._query_helper(mid + 1, seg_right, query_left, query_right, right_child)
        
        return sum_val
    
    def update(self, index: int, value: int):
        """
        单点更新
        Args:
            index: 要更新的索引
            value: 新的值
        """
        # 参数校验
        if index < 0 or index >= self.n:
            raise ValueError("索引越界")
        
        self._update_helper(0, self.n - 1, index, value, 0)
    
    def _update_helper(self, seg_left: int, seg_right: int, 
                      index: int, value: int, idx: int):
        """
        递归更新辅助函数
        """
        # 找到目标叶子节点
        if seg_left == seg_right:
            self.tree[idx] = value
            return
        
        mid = seg_left + (seg_right - seg_left) // 2
        left_child = 2 * idx + 1
        right_child = 2 * idx + 2
        
        # 根据索引位置决定更新哪棵子树
        if index <= mid:
            self._update_helper(seg_left, mid, index, value, left_child)
        else:
            self._update_helper(mid + 1, seg_right, index, value, right_child)
        
        # 更新父节点（求和）
        self.tree[idx] = self.tree[left_child] + self.tree[right_child]
    
    def range_add(self, update_left: int, update_right: int, delta: int):
        """
        区间加法更新（可选功能，增强实用性）
        Args:
            update_left: 更新区间左边界
            update_right: 更新区间右边界
            delta: 增加值
        """
        # 参数校验
        if update_left < 0 or update_right >= self.n or update_left > update_right:
            raise ValueError("更新区间不合法")
        
        self._range_add_helper(0, self.n - 1, update_left, update_right, delta, 0)
    
    def _range_add_helper(self, seg_left: int, seg_right: int, 
                         update_left: int, update_right: int, delta: int, idx: int):
        """
        递归区间加法辅助函数
        """
        # 当前区间完全包含在更新区间内
        if update_left <= seg_left and seg_right <= update_right:
            self.tree[idx] += delta * (seg_right - seg_left + 1)
            return
        
        mid = seg_left + (seg_right - seg_left) // 2
        left_child = 2 * idx + 1
        right_child = 2 * idx + 2
        
        # 更新左子树
        if update_left <= mid:
            self._range_add_helper(seg_left, mid, update_left, update_right, delta, left_child)
        
        # 更新右子树
        if update_right > mid:
            self._range_add_helper(mid + 1, seg_right, update_left, update_right, delta, right_child)
        
        # 更新父节点
        self.tree[idx] = self.tree[left_child] + self.tree[right_child]
    
    def print_tree(self):
        """
        打印线段树结构（用于调试）
        """
        print("线段树结构:")
        self._print_tree_helper(0, self.n - 1, 0, 0)
    
    def _print_tree_helper(self, left: int, right: int, idx: int, depth: int):
        """
        递归打印线段树结构辅助函数
        """
        indent = "  " * depth
        print(f"{indent}区间[{left}, {right}]: {self.tree[idx]}")
        
        if left != right:
            mid = left + (right - left) // 2
            self._print_tree_helper(left, mid, 2 * idx + 1, depth + 1)
            self._print_tree_helper(mid + 1, right, 2 * idx + 2, depth + 1)
    
    def size(self) -> int:
        """
        获取原始数组长度
        Returns:
            数组长度
        """
        return self.n


def main():
    """
    主函数：测试用例
    """
    # 测试用例1：正常数组
    nums1 = [1, 3, 5, 7, 9, 11]
    st1 = NowcoderNC79_SegmentTreeSum(nums1)
    
    print("=== 测试用例1 ===")
    print(f"数组: {nums1}")
    
    # 测试查询
    print(f"查询[0, 2]区间和: {st1.query(0, 2)}")  # 期望: 1+3+5=9
    print(f"查询[1, 4]区间和: {st1.query(1, 4)}")  # 期望: 3+5+7+9=24
    print(f"查询[0, 5]区间和: {st1.query(0, 5)}")  # 期望: 1+3+5+7+9+11=36
    
    # 测试单点更新
    st1.update(2, 10)
    print(f"更新索引2为10后，查询[0, 2]区间和: {st1.query(0, 2)}")  # 期望: 1+3+10=14
    
    # 测试区间加法
    st1.range_add(1, 3, 5)
    print(f"区间[1,3]加5后，查询[0, 5]区间和: {st1.query(0, 5)}")  # 期望: 1+8+15+12+9+11=56
    
    # 测试用例2：边界情况
    nums2 = [5]
    st2 = NowcoderNC79_SegmentTreeSum(nums2)
    
    print("\n=== 测试用例2 ===")
    print(f"数组: {nums2}")
    print(f"查询[0, 0]区间和: {st2.query(0, 0)}")  # 期望: 5
    
    # 测试用例3：负数数组
    nums3 = [-1, -3, -5, -7]
    st3 = NowcoderNC79_SegmentTreeSum(nums3)
    
    print("\n=== 测试用例3 ===")
    print(f"数组: {nums3}")
    print(f"查询[0, 3]区间和: {st3.query(0, 3)}")  # 期望: -1-3-5-7=-16
    
    # 性能测试
    print("\n=== 性能测试 ===")
    size = 100000
    large_nums = [random.randint(0, 1000) for _ in range(size)]
    
    start_time = time.time()
    st_large = NowcoderNC79_SegmentTreeSum(large_nums)
    build_time = (time.time() - start_time) * 1000
    
    start_time = time.time()
    sum_val = st_large.query(0, size - 1)
    query_time = (time.time() - start_time) * 1000
    
    start_time = time.time()
    st_large.range_add(0, size - 1, 10)
    update_time = (time.time() - start_time) * 1000
    
    print(f"构建{size}个元素的线段树耗时: {build_time:.2f}ms")
    print(f"查询整个区间和耗时: {query_time:.2f}ms")
    print(f"区间加法更新耗时: {update_time:.2f}ms")
    print(f"初始区间和: {sum_val}")
    
    # 异常测试
    print("\n=== 异常测试 ===")
    try:
        st1.query(-1, 2)
    except ValueError as e:
        print(f"捕获到预期异常: {e}")
    
    try:
        st1.update(10, 5)
    except ValueError as e:
        print(f"捕获到预期异常: {e}")
    
    # 内存使用分析
    print("\n=== 内存使用分析 ===")
    print(f"原始数组大小: {size} 个整数")
    print(f"线段树数组大小: {4 * size} 个整数")
    print(f"内存使用比例: {4.0 * size / size} 倍")
    
    # 工程化改进建议
    print("\n=== 工程化改进建议 ===")
    print("1. 使用懒惰标记优化区间更新操作")
    print("2. 实现动态开点线段树节省内存")
    print("3. 添加线程安全支持")
    print("4. 实现序列化和反序列化功能")
    print("5. 添加监控和性能统计功能")


if __name__ == "__main__":
    main()