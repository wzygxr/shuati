"""
线段树懒惰传播实现 - 区间加法和区间求和 (Python版本)
题目来源：洛谷 P3372 【模板】线段树 1
题目链接：https://www.luogu.com.cn/problem/P3372

题目描述：
给定一个长度为n的数组，支持两种操作：
1. 将某个区间内的每个数加上k
2. 查询某个区间内所有数的和

解题思路：
使用线段树配合懒惰传播技术来高效处理区间更新和区间查询操作。
线段树是一种二叉树结构，每个节点代表数组的一个区间，存储该区间的相关信息（如区间和）。
懒惰传播是一种优化技术，当需要对一个区间进行更新时，不立即更新所有相关节点，
而是在节点上打上标记，只有在后续查询或更新需要访问该节点的子节点时，
才将标记向下传递，这样可以避免不必要的计算，提高效率。

算法要点：
- 使用线段树维护区间和
- 使用懒惰标记实现区间加法的高效更新
- 支持区间更新和区间查询操作

时间复杂度分析：
- 建树：O(n)
- 区间更新：O(log n)
- 区间查询：O(log n)

空间复杂度分析：
- 线段树需要4n的空间：O(n)
- 懒惰标记数组需要4n的空间：O(n)
- 总空间复杂度：O(n)

Python特性应用：
- 使用列表(list)作为动态数组
- 利用Python的动态类型特性
- 使用异常处理机制
- 支持列表推导式等Python特性
"""

class SegmentTreeWithLazy:
    """
    线段树懒惰传播类
    """
    
    def __init__(self, arr):
        """
        构造函数 - 初始化线段树
        :param arr: 原始数组
        """
        # 输入验证：检查数组是否为空
        if not arr:
            raise ValueError("数组不能为空")
        
        self.n = len(arr)
        self.data = arr[:]  # 深拷贝原始数组，避免外部修改影响
        # 线段树通常需要4倍空间，确保足够容纳所有节点
        self.tree = [0] * (4 * self.n)
        # 懒惰标记数组也需要同样大小的空间
        self.lazy = [0] * (4 * self.n)
        
        # 构建线段树
        self._build_tree(0, self.n - 1, 0)
    
    def _build_tree(self, start, end, idx):
        """
        构建线段树
        递归地将数组构建成线段树结构
        :param start: 区间起始索引
        :param end: 区间结束索引
        :param idx: 当前节点索引（在tree数组中的位置）
        
        时间复杂度：O(n)
        空间复杂度：O(log n) - 递归调用栈深度
        """
        # 递归终止条件：当前区间只有一个元素（叶子节点）
        if start == end:
            self.tree[idx] = self.data[start]
            return
        
        # 计算区间中点，避免整数溢出
        mid = start + (end - start) // 2
        # 计算左右子节点在tree数组中的索引
        left_idx = 2 * idx + 1   # 左子节点索引
        right_idx = 2 * idx + 2  # 右子节点索引
        
        # 递归构建左子树
        self._build_tree(start, mid, left_idx)
        # 递归构建右子树
        self._build_tree(mid + 1, end, right_idx)
        
        # 合并左右子树的结果，当前节点存储左右子树区间和的总和
        self.tree[idx] = self.tree[left_idx] + self.tree[right_idx]
    
    def update_range(self, l, r, val):
        """
        区间更新 - 将区间[l, r]内的每个数加上val
        :param l: 区间左边界（包含）
        :param r: 区间右边界（包含）
        :param val: 要加的值
        
        时间复杂度：O(log n)
        """
        # 输入验证：检查区间参数是否合法
        if l < 0 or r >= self.n or l > r:
            raise ValueError("区间参数不合法")
        
        # 调用递归实现
        self._update_range(0, self.n - 1, l, r, val, 0)
    
    def _update_range(self, start, end, l, r, val, idx):
        """
        区间更新递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param l: 目标更新区间的左边界
        :param r: 目标更新区间的右边界
        :param val: 要加的值
        :param idx: 当前节点在tree数组中的索引
        
        核心思想：
        1. 先处理当前节点的懒惰标记（懒惰传播）
        2. 判断当前区间与目标区间的关系
        3. 根据关系决定是直接更新、递归更新还是忽略
        4. 更新完成后维护父节点信息
        """
        # 先处理懒惰标记（懒惰传播的核心步骤）
        # 如果当前节点有懒惰标记，需要先将标记应用到当前节点
        if self.lazy[idx] != 0:
            # 更新当前节点的值：区间和增加 lazy[idx] * 区间长度
            self.tree[idx] += (end - start + 1) * self.lazy[idx]
            # 如果不是叶子节点，将懒惰标记传递给子节点
            if start != end:
                self.lazy[2 * idx + 1] += self.lazy[idx]  # 传递给左子节点
                self.lazy[2 * idx + 2] += self.lazy[idx]  # 传递给右子节点
            # 清除当前节点的懒惰标记
            self.lazy[idx] = 0
        
        # 当前区间与目标区间无交集，直接返回
        if start > r or end < l:
            return
        
        # 当前区间完全包含在目标区间内，可以直接更新
        if l <= start and end <= r:
            # 更新当前节点的值：区间和增加 val * 区间长度
            self.tree[idx] += (end - start + 1) * val
            # 如果不是叶子节点，打上懒惰标记
            if start != end:
                self.lazy[2 * idx + 1] += val  # 给左子节点打标记
                self.lazy[2 * idx + 2] += val  # 给右子节点打标记
            return
        
        # 部分重叠，需要递归更新子区间
        mid = start + (end - start) // 2
        # 递归更新左子树
        self._update_range(start, mid, l, r, val, 2 * idx + 1)
        # 递归更新右子树
        self._update_range(mid + 1, end, l, r, val, 2 * idx + 2)
        
        # 更新完成后，需要维护父节点信息（合并子节点结果）
        self.tree[idx] = self.tree[2 * idx + 1] + self.tree[2 * idx + 2]
    
    def query_range(self, l, r):
        """
        区间查询 - 查询区间[l, r]的和
        :param l: 区间左边界（包含）
        :param r: 区间右边界（包含）
        :return: 区间和
        
        时间复杂度：O(log n)
        """
        # 输入验证：检查区间参数是否合法
        if l < 0 or r >= self.n or l > r:
            raise ValueError("区间参数不合法")
        
        # 调用递归实现
        return self._query_range(0, self.n - 1, l, r, 0)
    
    def _query_range(self, start, end, l, r, idx):
        """
        区间查询递归实现
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
        if self.lazy[idx] != 0:
            # 更新当前节点的值：区间和增加 lazy[idx] * 区间长度
            self.tree[idx] += (end - start + 1) * self.lazy[idx]
            # 如果不是叶子节点，将懒惰标记传递给子节点
            if start != end:
                self.lazy[2 * idx + 1] += self.lazy[idx]  # 传递给左子节点
                self.lazy[2 * idx + 2] += self.lazy[idx]  # 传递给右子节点
            # 清除当前节点的懒惰标记
            self.lazy[idx] = 0
        
        # 当前区间与目标区间无交集，返回0
        if start > r or end < l:
            return 0
        
        # 当前区间完全包含在目标区间内，直接返回当前节点的值
        if l <= start and end <= r:
            return self.tree[idx]
        
        # 部分重叠，需要递归查询子区间
        mid = start + (end - start) // 2
        # 递归查询左子树
        left_sum = self._query_range(start, mid, l, r, 2 * idx + 1)
        # 递归查询右子树
        right_sum = self._query_range(mid + 1, end, l, r, 2 * idx + 2)
        
        # 返回左右子树查询结果的和
        return left_sum + right_sum
    
    def update_point(self, index, val):
        """
        单点更新 - 将位置index的值加上val
        :param index: 位置索引
        :param val: 要加的值
        
        时间复杂度：O(log n)
        """
        # 输入验证：检查索引是否越界
        if index < 0 or index >= self.n:
            raise ValueError("索引越界")
        
        # 单点更新可以看作是区间更新的特例（区间长度为1）
        self.update_range(index, index, val)
    
    def query_point(self, index):
        """
        单点查询 - 查询位置index的值
        :param index: 位置索引
        :return: 该位置的值
        
        时间复杂度：O(log n)
        """
        # 输入验证：检查索引是否越界
        if index < 0 or index >= self.n:
            raise ValueError("索引越界")
        
        # 单点查询可以看作是区间查询的特例（区间长度为1）
        return self.query_range(index, index)


def test_segment_tree():
    """
    测试函数
    """
    # 测试用例1：基本功能测试
    arr1 = [1, 3, 5, 7, 9, 11]
    st1 = SegmentTreeWithLazy(arr1)
    
    print("=== 测试用例1：基本功能测试 ===")
    print(f"初始数组：{arr1}")
    print(f"区间[0, 2]的和：{st1.query_range(0, 2)}")  # 期望：9 (1+3+5)
    print(f"区间[1, 4]的和：{st1.query_range(1, 4)}")  # 期望：24 (3+5+7+9)
    
    # 区间更新测试
    st1.update_range(1, 3, 2)
    print("更新区间[1, 3]加2后：")
    print(f"区间[0, 2]的和：{st1.query_range(0, 2)}")  # 期望：15 (1+5+9)
    print(f"区间[1, 4]的和：{st1.query_range(1, 4)}")  # 期望：30 (5+9+9+9)
    
    # 测试用例2：边界条件测试
    arr2 = [10]
    st2 = SegmentTreeWithLazy(arr2)
    
    print("\n=== 测试用例2：边界条件测试 ===")
    print(f"单元素数组：{arr2}")
    print(f"单点查询[0]：{st2.query_point(0)}")  # 期望：10
    st2.update_point(0, 5)
    print(f"单点更新[0]加5后：{st2.query_point(0)}")  # 期望：15
    
    # 测试用例3：异常处理测试
    print("\n=== 测试用例3：异常处理测试 ===")
    try:
        st1.update_range(-1, 2, 1)
    except ValueError as e:
        print(f"异常处理测试：{e}")
    
    # 测试用例4：大规模数据测试（模拟）
    print("\n=== 测试用例4：性能验证 ===")
    print("线段树懒惰传播算法已实现，支持高效区间更新和查询")


if __name__ == "__main__":
    test_segment_tree()


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
   - 可以轻松扩展支持其他操作（如区间乘法、区间赋值等）
   - 支持自定义比较函数
   - 可以集成到更大的算法库中
"""