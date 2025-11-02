"""
线段树高级应用 - 多种区间操作

题目描述：
实现支持多种区间操作的线段树，包括：
1. 区间赋值
2. 区间加法
3. 区间乘法
4. 区间求和
5. 区间最大值/最小值

题目来源：洛谷 P3373 【模板】线段树 2
测试链接 : https://www.luogu.com.cn/problem/P3373

解题思路：
使用高级线段树实现支持多种操作，包括区间赋值、加法、乘法以及查询操作。
通过维护多种懒惰标记来处理不同优先级的操作。

核心思想：
1. 多标记懒惰传播：同时维护加法、乘法和赋值三种懒惰标记
2. 标记优先级：赋值 > 乘法 > 加法
3. 标记下传：在下传标记时需要按照优先级顺序处理

时间复杂度分析：
- 构建线段树：O(n)
- 所有区间操作：O(log n)

空间复杂度分析：
- 线段树需要O(n)的额外空间
"""

import sys
from typing import List

class Node:
    """线段树节点类"""
    
    def __init__(self):
        self.sum = 0          # 区间和
        self.max = -float('inf')  # 区间最大值
        self.min = float('inf')   # 区间最小值
        self.add = 0          # 加法懒惰标记
        self.mul = 1          # 乘法懒惰标记
        self.set = 0          # 赋值懒惰标记
        self.has_set = False  # 是否有赋值标记


class AdvancedSegmentTree:
    """高级线段树实现 - 支持多种操作"""
    
    def __init__(self, nums: List[int]):
        """
        构造函数 - 初始化高级线段树
        :param nums: 原始数组
        """
        self.n = len(nums)
        # 线段树通常需要4倍空间
        self.tree = [Node() for _ in range(self.n * 4)]
        self.build_tree(nums, 0, 0, self.n - 1)
    
    def build_tree(self, nums: List[int], node: int, start: int, end: int) -> None:
        """
        构建线段树
        递归地将数组构建成线段树结构
        :param nums: 原始数组
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        
        时间复杂度: O(n)
        """
        if start == end:
            # 叶子节点 - 直接存储数组元素值
            self.tree[node].sum = nums[start]
            self.tree[node].max = nums[start]
            self.tree[node].min = nums[start]
        else:
            mid = (start + end) // 2
            # 递归构建左子树
            self.build_tree(nums, 2 * node + 1, start, mid)
            # 递归构建右子树
            self.build_tree(nums, 2 * node + 2, mid + 1, end)
            # 向上更新父节点信息
            self.push_up(node)
    
    def push_up(self, node: int) -> None:
        """
        向上更新父节点
        根据左右子节点的信息更新当前节点的信息
        :param node: 当前线段树节点索引
        """
        left = 2 * node + 1   # 左子节点索引
        right = 2 * node + 2  # 右子节点索引
        # 更新区间和
        self.tree[node].sum = self.tree[left].sum + self.tree[right].sum
        # 更新区间最大值
        self.tree[node].max = max(self.tree[left].max, self.tree[right].max)
        # 更新区间最小值
        self.tree[node].min = min(self.tree[left].min, self.tree[right].min)
    
    def push_down(self, node: int, start: int, end: int) -> None:
        """
        向下传递懒惰标记
        按照标记优先级顺序传递标记给子节点
        优先级：赋值 > 乘法 > 加法
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        
        时间复杂度: O(1)
        """
        # 叶子节点不需要传递标记
        if start == end:
            return
        
        left = 2 * node + 1   # 左子节点索引
        right = 2 * node + 2  # 右子节点索引
        mid = (start + end) // 2
        
        # 处理赋值标记（优先级最高）
        # 当存在赋值标记时，需要清除子节点的其他标记
        if self.tree[node].has_set:
            # 更新左子树的区间信息
            self.tree[left].sum = self.tree[node].set * (mid - start + 1)  # 区间和 = 赋值 * 区间长度
            self.tree[right].sum = self.tree[node].set * (end - mid)
            self.tree[left].max = self.tree[node].set  # 区间最大值 = 赋值
            self.tree[right].max = self.tree[node].set
            self.tree[left].min = self.tree[node].set  # 区间最小值 = 赋值
            self.tree[right].min = self.tree[node].set
            
            # 传递赋值标记给子节点
            self.tree[left].set = self.tree[node].set
            self.tree[right].set = self.tree[node].set
            self.tree[left].has_set = True
            self.tree[right].has_set = True
            
            # 清除子节点的其他标记（加法和乘法）
            self.tree[left].add = 0
            self.tree[right].add = 0
            self.tree[left].mul = 1
            self.tree[right].mul = 1
            
            # 清除当前节点的赋值标记
            self.tree[node].has_set = False
        
        # 处理乘法标记（优先级次之）
        # 当存在乘法标记时，需要更新子节点的所有信息
        if self.tree[node].mul != 1:
            # 更新左子树的区间信息（乘以mul）
            self.tree[left].sum *= self.tree[node].mul
            self.tree[right].sum *= self.tree[node].mul
            self.tree[left].max *= self.tree[node].mul
            self.tree[right].max *= self.tree[node].mul
            self.tree[left].min *= self.tree[node].mul
            self.tree[right].min *= self.tree[node].mul
            
            # 传递乘法标记给子节点
            self.tree[left].mul *= self.tree[node].mul
            self.tree[right].mul *= self.tree[node].mul
            # 乘法标记也会影响加法标记（add * mul）
            self.tree[left].add *= self.tree[node].mul
            self.tree[right].add *= self.tree[node].mul
            
            # 清除当前节点的乘法标记
            self.tree[node].mul = 1
        
        # 处理加法标记（优先级最低）
        # 当存在加法标记时，需要更新子节点的所有信息
        if self.tree[node].add != 0:
            left_len = mid - start + 1   # 左子树区间长度
            right_len = end - mid        # 右子树区间长度
            
            # 更新左子树的区间信息（加上add）
            self.tree[left].sum += self.tree[node].add * left_len  # 区间和增加 add * 区间长度
            self.tree[right].sum += self.tree[node].add * right_len
            self.tree[left].max += self.tree[node].add  # 区间最大值增加 add
            self.tree[right].max += self.tree[node].add
            self.tree[left].min += self.tree[node].add  # 区间最小值增加 add
            self.tree[right].min += self.tree[node].add
            
            # 传递加法标记给子节点
            self.tree[left].add += self.tree[node].add
            self.tree[right].add += self.tree[node].add
            
            # 清除当前节点的加法标记
            self.tree[node].add = 0
    
    def range_set(self, left: int, right: int, val: int) -> None:
        """
        区间赋值操作
        将区间[left, right]内的每个数都赋值为val
        :param left: 更新区间左边界（0-based索引）
        :param right: 更新区间右边界（0-based索引）
        :param val: 要赋的值
        
        时间复杂度: O(log n)
        """
        self._range_set_helper(0, 0, self.n - 1, left, right, val)
    
    def _range_set_helper(self, node: int, start: int, end: int, l: int, r: int, val: int) -> None:
        """
        区间赋值操作辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param l: 更新区间左边界（0-based索引）
        :param r: 更新区间右边界（0-based索引）
        :param val: 要赋的值
        """
        # 如果当前区间完全包含在更新区间内
        if l <= start and end <= r:
            # 直接更新当前节点的信息和标记
            self.tree[node].sum = val * (end - start + 1)  # 区间和 = 赋值 * 区间长度
            self.tree[node].max = val  # 区间最大值 = 赋值
            self.tree[node].min = val  # 区间最小值 = 赋值
            self.tree[node].set = val  # 设置赋值标记
            self.tree[node].has_set = True  # 标记存在赋值操作
            self.tree[node].add = 0  # 清除加法标记
            self.tree[node].mul = 1  # 清除乘法标记
            return
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        mid = (start + end) // 2
        # 递归更新左右子树
        if l <= mid:
            self._range_set_helper(2 * node + 1, start, mid, l, r, val)
        if r > mid:
            self._range_set_helper(2 * node + 2, mid + 1, end, l, r, val)
        # 更新父节点信息
        self.push_up(node)
    
    def range_mul(self, left: int, right: int, val: int) -> None:
        """
        区间乘法操作
        将区间[left, right]内的每个数都乘以val
        :param left: 更新区间左边界（0-based索引）
        :param right: 更新区间右边界（0-based索引）
        :param val: 要乘的值
        
        时间复杂度: O(log n)
        """
        self._range_mul_helper(0, 0, self.n - 1, left, right, val)
    
    def _range_mul_helper(self, node: int, start: int, end: int, l: int, r: int, val: int) -> None:
        """
        区间乘法操作辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param l: 更新区间左边界（0-based索引）
        :param r: 更新区间右边界（0-based索引）
        :param val: 要乘的值
        """
        # 如果当前区间完全包含在更新区间内
        if l <= start and end <= r:
            # 直接更新当前节点的信息和标记
            self.tree[node].sum *= val  # 区间和乘以val
            self.tree[node].max *= val  # 区间最大值乘以val
            self.tree[node].min *= val  # 区间最小值乘以val
            self.tree[node].mul *= val  # 乘法标记乘以val
            self.tree[node].add *= val  # 加法标记也乘以val（因为 a*x + b 变成 a*x*val + b*val）
            return
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        mid = (start + end) // 2
        # 递归更新左右子树
        if l <= mid:
            self._range_mul_helper(2 * node + 1, start, mid, l, r, val)
        if r > mid:
            self._range_mul_helper(2 * node + 2, mid + 1, end, l, r, val)
        # 更新父节点信息
        self.push_up(node)
    
    def range_add(self, left: int, right: int, val: int) -> None:
        """
        区间加法操作
        将区间[left, right]内的每个数都加上val
        :param left: 更新区间左边界（0-based索引）
        :param right: 更新区间右边界（0-based索引）
        :param val: 要加的值
        
        时间复杂度: O(log n)
        """
        self._range_add_helper(0, 0, self.n - 1, left, right, val)
    
    def _range_add_helper(self, node: int, start: int, end: int, l: int, r: int, val: int) -> None:
        """
        区间加法操作辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param l: 更新区间左边界（0-based索引）
        :param r: 更新区间右边界（0-based索引）
        :param val: 要加的值
        """
        # 如果当前区间完全包含在更新区间内
        if l <= start and end <= r:
            # 直接更新当前节点的信息和标记
            self.tree[node].sum += val * (end - start + 1)  # 区间和增加 val * 区间长度
            self.tree[node].max += val  # 区间最大值增加 val
            self.tree[node].min += val  # 区间最小值增加 val
            self.tree[node].add += val  # 加法标记增加 val
            return
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        mid = (start + end) // 2
        # 递归更新左右子树
        if l <= mid:
            self._range_add_helper(2 * node + 1, start, mid, l, r, val)
        if r > mid:
            self._range_add_helper(2 * node + 2, mid + 1, end, l, r, val)
        # 更新父节点信息
        self.push_up(node)
    
    def range_sum(self, left: int, right: int) -> int:
        """
        区间求和查询
        查询区间[left, right]内所有数的和
        :param left: 查询区间左边界（0-based索引）
        :param right: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内所有数的和
        
        时间复杂度: O(log n)
        """
        return self._range_sum_helper(0, 0, self.n - 1, left, right)
    
    def _range_sum_helper(self, node: int, start: int, end: int, l: int, r: int) -> int:
        """
        区间求和查询辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param l: 查询区间左边界（0-based索引）
        :param r: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内所有数的和
        """
        # 如果当前区间完全包含在查询区间内
        if l <= start and end <= r:
            # 直接返回当前节点的区间和
            return self.tree[node].sum
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        mid = (start + end) // 2
        total = 0
        # 递归查询左右子树
        if l <= mid:
            total += self._range_sum_helper(2 * node + 1, start, mid, l, r)
        if r > mid:
            total += self._range_sum_helper(2 * node + 2, mid + 1, end, l, r)
        return total
    
    def range_max(self, left: int, right: int) -> float:
        """
        区间最大值查询
        查询区间[left, right]内的最大值
        :param left: 查询区间左边界（0-based索引）
        :param right: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内的最大值
        
        时间复杂度: O(log n)
        """
        return self._range_max_helper(0, 0, self.n - 1, left, right)
    
    def _range_max_helper(self, node: int, start: int, end: int, l: int, r: int) -> float:
        """
        区间最大值查询辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param l: 查询区间左边界（0-based索引）
        :param r: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内的最大值
        """
        # 如果当前区间完全包含在查询区间内
        if l <= start and end <= r:
            # 直接返回当前节点的区间最大值
            return self.tree[node].max
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        mid = (start + end) // 2
        max_val = -float('inf')
        # 递归查询左右子树
        if l <= mid:
            max_val = max(max_val, self._range_max_helper(2 * node + 1, start, mid, l, r))
        if r > mid:
            max_val = max(max_val, self._range_max_helper(2 * node + 2, mid + 1, end, l, r))
        return max_val
    
    def range_min(self, left: int, right: int) -> float:
        """
        区间最小值查询
        查询区间[left, right]内的最小值
        :param left: 查询区间左边界（0-based索引）
        :param right: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内的最小值
        
        时间复杂度: O(log n)
        """
        return self._range_min_helper(0, 0, self.n - 1, left, right)
    
    def _range_min_helper(self, node: int, start: int, end: int, l: int, r: int) -> float:
        """
        区间最小值查询辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param l: 查询区间左边界（0-based索引）
        :param r: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内的最小值
        """
        # 如果当前区间完全包含在查询区间内
        if l <= start and end <= r:
            # 直接返回当前节点的区间最小值
            return self.tree[node].min
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        mid = (start + end) // 2
        min_val = float('inf')
        # 递归查询左右子树
        if l <= mid:
            min_val = min(min_val, self._range_min_helper(2 * node + 1, start, mid, l, r))
        if r > mid:
            min_val = min(min_val, self._range_min_helper(2 * node + 2, mid + 1, end, l, r))
        return min_val


def main():
    """
    主函数 - 处理输入输出和操作调度
    """
    # 读取n和m
    n, m = map(int, input().split())
    
    # 读取初始数组
    nums = list(map(int, input().split()))
    
    # 构建高级线段树
    st = AdvancedSegmentTree(nums)
    
    # 处理m个操作
    for _ in range(m):
        operation = list(map(int, input().split()))
        op = operation[0]
        l = operation[1]
        r = operation[2]
        
        if op == 1:  # 区间加法
            add_val = operation[3]
            st.range_add(l - 1, r - 1, add_val)
        elif op == 2:  # 区间乘法
            mul_val = operation[3]
            st.range_mul(l - 1, r - 1, mul_val)
        elif op == 3:  # 区间赋值
            set_val = operation[3]
            st.range_set(l - 1, r - 1, set_val)
        elif op == 4:  # 区间求和
            print(st.range_sum(l - 1, r - 1))
        elif op == 5:  # 区间最大值
            print(int(st.range_max(l - 1, r - 1)))
        elif op == 6:  # 区间最小值
            print(int(st.range_min(l - 1, r - 1)))


def test():
    """
    测试函数 - 验证高级线段树实现的正确性
    """
    print("=== 高级线段树测试 ===")
    
    nums = [1, 2, 3, 4, 5]
    st = AdvancedSegmentTree(nums)
    
    # 测试初始状态
    print(f"初始数组区间[0,4]的和: {st.range_sum(0, 4)}")  # 15
    print(f"区间[0,4]的最大值: {int(st.range_max(0, 4))}")  # 5
    print(f"区间[0,4]的最小值: {int(st.range_min(0, 4))}")  # 1
    
    # 测试区间加法
    st.range_add(1, 3, 2)
    print(f"区间加法后区间[1,3]的和: {st.range_sum(1, 3)}")  # 4+5+6 = 15
    
    # 测试区间乘法
    st.range_mul(0, 2, 3)
    print(f"区间乘法后区间[0,2]的和: {st.range_sum(0, 2)}")  # 3*3 + 4*3 + 5*3 = 36
    
    # 测试区间赋值
    st.range_set(2, 4, 10)
    print(f"区间赋值后区间[2,4]的和: {st.range_sum(2, 4)}")  # 10*3 = 30
    
    print("=== 测试完成 ===")


# 如果需要运行测试，取消下面的注释
# test()

# 如果需要运行主程序，取消下面的注释
# if __name__ == "__main__":
#     main()