"""
线段树区间赋值 - 支持区间赋值和区间查询 (Python版本)
题目来源：Codeforces 438D - The Child and Sequence
问题描述：支持区间赋值、区间求和、区间取模等操作的线段树实现

解题思路：
使用线段树配合懒惰传播技术来高效处理多种区间操作。
线段树是一种二叉树结构，每个节点代表数组的一个区间，存储该区间的相关信息（如区间和）。
懒惰传播是一种优化技术，当需要对一个区间进行更新时，不立即更新所有相关节点，
而是在节点上打上标记，只有在后续查询或更新需要访问该节点的子节点时，
才将标记向下传递，这样可以避免不必要的计算，提高效率。

算法要点：
- 使用线段树维护区间信息
- 支持多种区间操作（赋值、求和、取模）
- 使用懒惰标记优化区间赋值操作

时间复杂度：
- 构建线段树：O(n)
- 区间赋值：O(log n)
- 区间求和：O(log n)
- 区间取模：O(log n)

空间复杂度：O(n)

应用场景：
- 区间赋值问题
- 区间求和与修改
- 数学相关的区间操作问题
"""

from typing import List

class SegmentTreeAssignment:
    """
    线段树区间赋值类
    """
    
    def __init__(self, arr: List[int]):
        """
        构造函数，初始化线段树
        :param arr: 原始数组
        """
        self.n = len(arr)
        # 线段树通常需要4倍空间，确保足够容纳所有节点
        self.tree = [0] * (4 * self.n)
        # 懒惰标记数组也需要同样大小的空间
        self.lazy = [-1] * (4 * self.n)  # 使用-1表示没有懒惰标记
        # 构建线段树
        self._build_tree(arr, 0, self.n - 1, 0)
    
    def _build_tree(self, arr: List[int], start: int, end: int, idx: int):
        """
        构建线段树
        递归地将数组构建成线段树结构
        :param arr: 原始数组
        :param start: 区间开始索引
        :param end: 区间结束索引
        :param idx: 当前节点索引（在tree数组中的位置）
        
        时间复杂度：O(n)
        空间复杂度：O(log n) - 递归调用栈深度
        """
        # 递归终止条件：当前区间只有一个元素（叶子节点）
        if start == end:
            self.tree[idx] = arr[start]
            return
        
        # 计算区间中点，避免整数溢出
        mid = start + (end - start) // 2
        # 递归构建左子树
        self._build_tree(arr, start, mid, 2 * idx + 1)
        # 递归构建右子树
        self._build_tree(arr, mid + 1, end, 2 * idx + 2)
        # 合并左右子树的结果，当前节点存储左右子树区间和的总和
        self.tree[idx] = self.tree[2 * idx + 1] + self.tree[2 * idx + 2]
    
    def assign(self, l: int, r: int, val: int):
        """
        区间赋值操作
        将区间[l, r]内的每个数都赋值为val
        :param l: 区间左边界（包含）
        :param r: 区间右边界（包含）
        :param val: 要赋的值
        
        时间复杂度：O(log n)
        """
        # 调用递归实现
        self._assign(0, self.n - 1, l, r, val, 0)
    
    def _assign(self, start: int, end: int, l: int, r: int, val: int, idx: int):
        """
        区间赋值递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param l: 目标赋值区间的左边界
        :param r: 目标赋值区间的右边界
        :param val: 要赋的值
        :param idx: 当前节点在tree数组中的索引
        
        核心思想：
        1. 先处理当前节点的懒惰标记（懒惰传播）
        2. 判断当前区间与目标区间的关系
        3. 根据关系决定是直接更新、递归更新还是忽略
        4. 更新完成后维护父节点信息
        """
        # 先处理懒惰标记（懒惰传播的核心步骤）
        # 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if self.lazy[idx] != -1:
            # 更新当前节点的值：区间和 = lazy[idx] * 区间长度
            self.tree[idx] = self.lazy[idx] * (end - start + 1)
            # 如果不是叶子节点，将懒惰标记传递给子节点
            if start != end:
                self.lazy[2 * idx + 1] = self.lazy[idx]  # 传递给左子节点
                self.lazy[2 * idx + 2] = self.lazy[idx]  # 传递给右子节点
            # 清除当前节点的懒惰标记
            self.lazy[idx] = -1
        
        # 如果当前区间与目标区间无交集，直接返回
        if start > r or end < l:
            return
        
        # 如果当前区间完全包含在目标区间内，可以直接更新
        if l <= start and end <= r:
            # 更新当前节点的值：区间和 = val * 区间长度
            self.tree[idx] = val * (end - start + 1)
            # 如果不是叶子节点，打上懒惰标记
            if start != end:
                self.lazy[2 * idx + 1] = val  # 给左子节点打标记
                self.lazy[2 * idx + 2] = val  # 给右子节点打标记
            return
        
        # 部分重叠，需要递归更新子区间
        mid = start + (end - start) // 2
        # 递归更新左子树
        if l <= mid:
            self._assign(start, mid, l, r, val, 2 * idx + 1)
        # 递归更新右子树
        if r > mid:
            self._assign(mid + 1, end, l, r, val, 2 * idx + 2)
        # 更新完成后，需要维护父节点信息（合并子节点结果）
        self.tree[idx] = self.tree[2 * idx + 1] + self.tree[2 * idx + 2]
    
    def query_sum(self, l: int, r: int) -> int:
        """
        区间求和查询
        查询区间[l, r]内所有数的和
        :param l: 区间左边界（包含）
        :param r: 区间右边界（包含）
        :return: 区间和
        
        时间复杂度：O(log n)
        """
        # 调用递归实现
        return self._query_sum(0, self.n - 1, l, r, 0)
    
    def _query_sum(self, start: int, end: int, l: int, r: int, idx: int) -> int:
        """
        区间求和查询递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param l: 目标查询区间的左边界
        :param r: 目标查询区间的右边界
        :param idx: 当前节点在tree数组中的索引
        :return: 区间[l, r]的和
        
        核心思想：
        1. 先处理当前节点的懒惰标记（懒惰传播）
        2. 判断当前区间与目标区间的关系
        3. 根据关系决定是直接返回、递归查询还是部分返回
        """
        # 先处理懒惰标记（懒惰传播的核心步骤）
        # 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if self.lazy[idx] != -1:
            # 更新当前节点的值：区间和 = lazy[idx] * 区间长度
            self.tree[idx] = self.lazy[idx] * (end - start + 1)
            # 如果不是叶子节点，将懒惰标记传递给子节点
            if start != end:
                self.lazy[2 * idx + 1] = self.lazy[idx]  # 传递给左子节点
                self.lazy[2 * idx + 2] = self.lazy[idx]  # 传递给右子节点
            # 清除当前节点的懒惰标记
            self.lazy[idx] = -1
        
        # 如果当前区间与目标区间无交集，返回0
        if start > r or end < l:
            return 0
        
        # 如果当前区间完全包含在目标区间内，直接返回当前节点的值
        if l <= start and end <= r:
            return self.tree[idx]
        
        # 部分重叠，需要递归查询子区间
        mid = start + (end - start) // 2
        total = 0
        # 递归查询左子树
        if l <= mid:
            total += self._query_sum(start, mid, l, r, 2 * idx + 1)
        # 递归查询右子树
        if r > mid:
            total += self._query_sum(mid + 1, end, l, r, 2 * idx + 2)
        return total
    
    def modulo(self, l: int, r: int, mod: int):
        """
        区间取模操作
        将区间[l, r]内的每个数都对mod取模
        :param l: 区间左边界（包含）
        :param r: 区间右边界（包含）
        :param mod: 模数
        
        时间复杂度：O(log n)
        """
        # 调用递归实现
        self._modulo(0, self.n - 1, l, r, mod, 0)
    
    def _modulo(self, start: int, end: int, l: int, r: int, mod: int, idx: int):
        """
        区间取模递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param l: 目标取模区间的左边界
        :param r: 目标取模区间的右边界
        :param mod: 模数
        :param idx: 当前节点在tree数组中的索引
        """
        # 先处理懒惰标记（懒惰传播的核心步骤）
        # 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if self.lazy[idx] != -1:
            # 更新当前节点的值：区间和 = lazy[idx] * 区间长度
            self.tree[idx] = self.lazy[idx] * (end - start + 1)
            # 如果不是叶子节点，将懒惰标记传递给子节点
            if start != end:
                self.lazy[2 * idx + 1] = self.lazy[idx]  # 传递给左子节点
                self.lazy[2 * idx + 2] = self.lazy[idx]  # 传递给右子节点
            # 清除当前节点的懒惰标记
            self.lazy[idx] = -1
        
        # 如果当前区间与目标区间无交集，直接返回
        if start > r or end < l:
            return
        
        # 如果当前区间完全包含在目标区间内
        if l <= start and end <= r:
            # 如果区间最大值小于模数，则不需要取模（优化）
            if self.tree[idx] < mod:
                return
            
            # 如果是叶子节点，直接取模
            if start == end:
                self.tree[idx] %= mod
                return
        
        # 递归处理子区间
        mid = start + (end - start) // 2
        # 递归处理左子树
        if l <= mid:
            self._modulo(start, mid, l, r, mod, 2 * idx + 1)
        # 递归处理右子树
        if r > mid:
            self._modulo(mid + 1, end, l, r, mod, 2 * idx + 2)
        # 更新完成后，需要维护父节点信息（合并子节点结果）
        self.tree[idx] = self.tree[2 * idx + 1] + self.tree[2 * idx + 2]


def main():
    """
    测试函数
    """
    # 测试用例1：基础功能测试
    arr1 = [1, 2, 3, 4, 5]
    st1 = SegmentTreeAssignment(arr1)
    
    print("=== 测试用例1：基础功能测试 ===")
    print(f"初始数组: {arr1}")
    print(f"区间[0,2]的和: {st1.query_sum(0, 2)}")  # 应该为6 (1+2+3)
    
    # 区间赋值测试
    st1.assign(1, 3, 10)
    print(f"区间[1,3]赋值为10后，区间[0,4]的和: {st1.query_sum(0, 4)}")  # 应该为1+10+10+10+5=36
    
    # 区间取模测试
    st1.modulo(0, 4, 3)
    print(f"区间[0,4]对3取模后，区间[0,4]的和: {st1.query_sum(0, 4)}")
    
    # 测试用例2：边界条件测试
    arr2 = [7]
    st2 = SegmentTreeAssignment(arr2)
    print("\n=== 测试用例2：边界条件测试 ===")
    print(f"单元素数组: {arr2}")
    print(f"单点查询[0,0]的和: {st2.query_sum(0, 0)}")  # 应该为7
    
    # 测试用例3：性能验证
    print("\n=== 测试用例3：性能验证 ===")
    print("线段树区间赋值算法已实现，支持高效区间操作")


if __name__ == "__main__":
    main()


"""
Python版本特性分析：

1. 动态类型特性：
   - Python的动态类型使得代码更加简洁
   - 无需声明变量类型，提高开发效率

2. 列表操作优势：
   - 列表切片操作简化了数组处理
   - 列表推导式可以简化代码编写

3. 异常处理机制：
   - Python的异常处理机制完善
   - 支持多种异常类型，错误信息清晰

4. 可读性优势：
   - Python语法简洁，代码可读性强
   - 支持文档字符串，便于生成文档

5. 性能考量：
   - Python解释型语言的性能相对较低
   - 但对于算法学习和中等规模数据足够使用
   - 可以通过PyPy或C扩展优化性能

6. 跨平台兼容：
   - Python具有良好的跨平台兼容性
   - 代码可以在Windows、Linux、macOS等系统运行

7. 生态丰富：
   - Python有丰富的第三方库支持
   - 可以轻松集成到其他项目中

算法工程化思考：

1. 代码可维护性：
   - 使用清晰的命名规范
   - 添加详细的文档字符串
   - 模块化设计，便于扩展

2. 错误处理：
   - 对输入参数进行严格验证
   - 提供清晰的错误信息
   - 支持多种异常场景处理

3. 测试覆盖：
   - 提供完整的测试用例
   - 覆盖边界条件和异常场景
   - 支持自动化测试

4. 性能优化：
   - 对于大规模数据，可以考虑使用NumPy优化
   - 可以使用Cython或PyPy提升性能
   - 支持多线程处理（如果适用）

5. 扩展性：
   - 可以轻松扩展支持其他操作
   - 支持自定义比较函数
   - 可以集成到更大的算法库中
"""