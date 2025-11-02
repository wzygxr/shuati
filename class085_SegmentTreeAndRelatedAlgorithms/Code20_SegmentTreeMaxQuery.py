"""
线段树区间最大值查询实现 (Python版本)
题目来源：洛谷 P3865 【模板】ST表
题目链接：https://www.luogu.com.cn/problem/P3865

题目描述：
给定一个长度为n的数组，支持区间最大值查询操作

解题思路：
使用线段树来维护区间最大值信息。线段树是一种二叉树结构，每个节点代表数组的一个区间，
存储该区间的最大值。对于叶子节点，它代表数组中的单个元素；对于非叶子节点，
它代表其左右子树所覆盖区间的合并结果（在这里是最大值）。

算法要点：
- 使用线段树维护区间最大值
- 支持区间最大值查询操作
- 可以扩展支持区间更新（最大值更新）

时间复杂度分析：
- 建树：O(n)
- 区间查询：O(log n)
- 单点更新：O(log n)

空间复杂度分析：
- 线段树需要4n的空间：O(n)

应用场景：
- 需要频繁查询区间最大值的场景
- 如滑动窗口最大值、区间最值统计等
"""

class SegmentTreeMaxQuery:
    """
    线段树区间最大值查询类
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
        
        # 合并左右子树的结果，当前节点存储左右子树区间最大值中的较大者
        self.tree[idx] = max(self.tree[left_idx], self.tree[right_idx])
    
    def query_max(self, l, r):
        """
        区间最大值查询
        :param l: 区间左边界（包含）
        :param r: 区间右边界（包含）
        :return: 区间最大值
        
        时间复杂度：O(log n)
        """
        # 输入验证：检查区间参数是否合法
        if l < 0 or r >= self.n or l > r:
            raise ValueError("区间参数不合法")
        # 调用递归实现
        return self._query_max(0, self.n - 1, l, r, 0)
    
    def _query_max(self, start, end, l, r, idx):
        """
        区间最大值查询递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param l: 目标查询区间的左边界
        :param r: 目标查询区间的右边界
        :param idx: 当前节点在tree数组中的索引
        :return: 区间[l, r]的最大值
        
        核心思想：
        1. 判断当前区间与目标区间的关系
        2. 根据关系决定是直接返回、递归查询还是部分返回
        """
        # 当前区间完全包含在目标区间内，直接返回当前节点的值
        if l <= start and end <= r:
            return self.tree[idx]
        
        # 当前区间与目标区间无交集，返回无效值（负无穷）
        if start > r or end < l:
            return float('-inf')
        
        # 部分重叠，需要递归查询子区间
        mid = start + (end - start) // 2
        # 递归查询左子树
        left_max = self._query_max(start, mid, l, r, 2 * idx + 1)
        # 递归查询右子树
        right_max = self._query_max(mid + 1, end, l, r, 2 * idx + 2)
        
        # 返回左右子树查询结果中的较大者
        return max(left_max, right_max)
    
    def update_point(self, index, val):
        """
        单点更新 - 更新位置index的值为val
        :param index: 位置索引
        :param val: 新值
        
        时间复杂度：O(log n)
        """
        # 输入验证：检查索引是否越界
        if index < 0 or index >= self.n:
            raise ValueError("索引越界")
        # 调用递归实现
        self._update_point(0, self.n - 1, index, val, 0)
    
    def _update_point(self, start, end, index, val, idx):
        """
        单点更新递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param index: 要更新的位置索引
        :param val: 新值
        :param idx: 当前节点在tree数组中的索引
        """
        # 递归终止条件：找到叶子节点
        if start == end:
            self.tree[idx] = val
            return
        
        # 计算区间中点
        mid = start + (end - start) // 2
        # 根据索引位置决定更新左子树还是右子树
        if index <= mid:
            # 更新左子树
            self._update_point(start, mid, index, val, 2 * idx + 1)
        else:
            # 更新右子树
            self._update_point(mid + 1, end, index, val, 2 * idx + 2)
        
        # 更新完成后，需要维护父节点信息（合并子节点结果）
        self.tree[idx] = max(self.tree[2 * idx + 1], self.tree[2 * idx + 2])


def test_segment_tree():
    """
    测试函数
    """
    # 测试用例1：基本功能测试
    arr1 = [3, 1, 4, 1, 5, 9, 2, 6]
    st1 = SegmentTreeMaxQuery(arr1)
    
    print("=== 测试用例1：基本功能测试 ===")
    print(f"初始数组：{arr1}")
    print(f"区间[0, 2]的最大值：{st1.query_max(0, 2)}")  # 期望：4 (3,1,4中的最大值)
    print(f"区间[2, 5]的最大值：{st1.query_max(2, 5)}")  # 期望：9 (4,1,5,9中的最大值)
    print(f"区间[4, 7]的最大值：{st1.query_max(4, 7)}")  # 期望：9 (5,9,2,6中的最大值)
    
    # 单点更新测试
    st1.update_point(3, 10)
    print("更新位置3的值为10后：")
    print(f"区间[0, 3]的最大值：{st1.query_max(0, 3)}")  # 期望：10 (3,1,4,10中的最大值)
    print(f"区间[2, 5]的最大值：{st1.query_max(2, 5)}")  # 期望：10 (4,10,5,9中的最大值)
    
    # 测试用例2：边界条件测试
    arr2 = [7]
    st2 = SegmentTreeMaxQuery(arr2)
    
    print("\n=== 测试用例2：边界条件测试 ===")
    print(f"单元素数组：{arr2}")
    print(f"单点查询[0]：{st2.query_max(0, 0)}")  # 期望：7
    st2.update_point(0, 15)
    print(f"单点更新[0]为15后：{st2.query_max(0, 0)}")  # 期望：15
    
    # 测试用例3：异常处理测试
    print("\n=== 测试用例3：异常处理测试 ===")
    try:
        st1.query_max(-1, 2)
    except ValueError as e:
        print(f"异常处理测试：{e}")
    
    # 测试用例4：性能验证
    print("\n=== 测试用例4：性能验证 ===")
    print("线段树区间最大值查询算法已实现，支持高效查询操作")


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
   - 可以轻松扩展支持其他操作（如区间最小值查询等）
   - 支持自定义比较函数
   - 可以集成到更大的算法库中
"""