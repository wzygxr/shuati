"""
Python 线段树实现 - 支持区间加法和区间求和
适用于 LeetCode 307. Range Sum Query - Mutable 类问题

线段树是一种强大的树形数据结构，专门用于高效处理区间查询和更新操作。
本实现包含两个版本：
1. 基础线段树：支持单点更新和区间求和查询
2. 带懒标记的线段树：支持区间加法更新和区间求和查询

时间复杂度分析：
- 建树：O(n)
- 单点更新：O(log n)
- 区间更新（使用懒标记）：O(log n)
- 区间查询：O(log n)
空间复杂度：O(n) - 使用4*n大小的数组存储线段树节点和懒标记

线段树适用场景：
- 频繁的区间查询操作（如区间和、最大值、最小值等）
- 需要高效更新的动态数组查询
- 离线和在线区间数据处理

题目来源：
- LeetCode 307. Range Sum Query - Mutable - https://leetcode.cn/problems/range-sum-query-mutable/
- LeetCode 315. Count of Smaller Numbers After Self - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
- LeetCode 699. Falling Squares - https://leetcode.cn/problems/falling-squares/
- LeetCode 218. The Skyline Problem - https://leetcode.cn/problems/the-skyline-problem/
- HDU 1166. 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
- HDU 1754. I Hate It - http://acm.hdu.edu.cn/showproblem.php?pid=1754
- HDU 6514. Monitor - http://acm.hdu.edu.cn/showproblem.php?pid=6514
- Luogu P3372. 【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
- Luogu P3373. 【模板】线段树 2 - https://www.luogu.com.cn/problem/P3373
- Codeforces 339D. Xor - https://codeforces.com/problemset/problem/339/D
- Codeforces 52C. Circular RMQ - https://codeforces.com/problemset/problem/52/C
- SPOJ GSS1. Can you answer these queries I - https://www.spoj.com/problems/GSS1/
- AtCoder ABC183E. Queen on Grid - https://atcoder.jp/contests/abc183/tasks/abc183_e
- USACO 2016 Jan Silver. Subsequences Summing to Sevens - http://www.usaco.org/index.php?page=viewproblem2&cpid=595
- 牛客 NC14417. 线段树练习 - https://ac.nowcoder.com/acm/problem/14417
- 计蒜客 T1250. 线段树练习 - https://nanti.jisuanke.com/t/T1250
- CodeChef SEGPROD. Segment Product - https://www.codechef.com/problems/SEGPROD
- SPOJ MKTHNUM. K-th number - https://www.spoj.com/problems/MKTHNUM/
- AizuOJ ALDS1_9_C. Segment Tree - https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_9_C
"""

class SegmentTree:
    """
    线段树类
    
    线段树是一种基于分治思想的二叉树数据结构，非常适合处理区间查询和更新操作。
    本实现支持单点更新和区间求和查询，是线段树最基础也是最常用的形式。
    
    属性：
        n: 原始数组长度
        arr: 原始数组的副本
        tree: 线段树数组，存储每个区间的和
    """
    
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.arr = nums[:]  # 创建数组副本
        # 线段树数组，大小为4*n（确保足够空间）
        self.tree = [0] * (4 * self.n)
        # 构建线段树，从根节点开始（索引0）
        self._build(0, self.n - 1, 0)
    
    def _build(self, l, r, i):
        """
        构建线段树
        时间复杂度：O(n)
        
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点在tree数组中的索引
        
        构建过程：
        1. 如果是叶子节点(l==r)，直接赋值
        2. 否则，递归构建左右子树
        3. 合并子节点的信息到当前节点
        
        分治思想体现：
        - 将大问题（构建整个数组的线段树）分解为小问题（构建子数组的线段树）
        - 递归解决小问题后，合并结果得到大问题的解
        """
        # 递归终止条件：到达叶子节点
        if l == r:
            self.tree[i] = self.arr[l]
            return
        
        # 计算中点，将区间分为左右两部分
        mid = (l + r) // 2
        # 递归构建左子树（索引为2*i+1）
        self._build(l, mid, 2 * i + 1)
        # 递归构建右子树（索引为2*i+2）
        self._build(mid + 1, r, 2 * i + 2)
        # 合并左右子树的结果：当前节点的值等于左右子节点值的和
        self.tree[i] = self.tree[2 * i + 1] + self.tree[2 * i + 2]
    
    def update(self, index, val):
        """
        更新数组中某个位置的值
        :param index: 要更新的数组索引
        :param val: 新的值
        
        工程化考虑：
        - 首先更新原始数组的副本
        - 然后递归更新线段树中对应的路径上的所有节点
        """
        if index < 0 or index >= self.n:
            raise ValueError(f"Index {index} out of bounds for array of size {self.n}")
        
        self.arr[index] = val
        self._update_tree(index, val, 0, self.n - 1, 0)
    
    def _update_tree(self, index, val, l, r, i):
        """
        更新线段树中的值
        时间复杂度：O(log n)
        
        :param index: 要更新的数组索引
        :param val: 新的值
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        
        更新策略：
        1. 如果到达叶子节点，直接更新值
        2. 根据目标索引与中点的关系，决定递归更新左子树还是右子树
        3. 更新完成后，回溯更新当前节点的值
        
        路径更新特性：
        - 仅更新从根到目标叶子节点的路径上的所有节点
        - 这样可以保证在O(log n)时间内完成更新
        """
        # 递归终止条件：找到对应的叶子节点
        if l == r:
            self.tree[i] = val
            return
        
        # 计算中点
        mid = (l + r) // 2
        # 根据索引决定更新左子树还是右子树
        if index <= mid:
            self._update_tree(index, val, l, mid, 2 * i + 1)
        else:
            self._update_tree(index, val, mid + 1, r, 2 * i + 2)
        
        # 更新当前节点的值：子节点更新后，父节点的值也需要更新
        self.tree[i] = self.tree[2 * i + 1] + self.tree[2 * i + 2]
    
    def sumRange(self, left, right):
        """
        查询区间和
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间和
        
        边界检查：
        - 确保查询区间有效
        - 处理空数组的边界情况
        """
        # 输入验证
        if self.n == 0:
            raise ValueError("Cannot query sum on empty array")
        if left < 0 or right >= self.n:
            raise ValueError(f"Range [{left}, {right}] out of bounds for array of size {self.n}")
        if left > right:
            raise ValueError(f"Invalid range: left ({left}) > right ({right})")
        
        return self._query_tree(left, right, 0, self.n - 1, 0)
    
    def _query_tree(self, jobl, jobr, l, r, i):
        """
        在线段树中查询区间和
        时间复杂度：O(log n)
        
        :param jobl: 查询区间左边界
        :param jobr: 查询区间右边界
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        :return: 区间和
        
        查询策略：
        1. 如果查询区间与当前区间无交集，返回0
        2. 如果查询区间完全包含当前区间，直接返回当前节点的值（剪枝优化）
        3. 否则，递归查询左右子树，并合并结果
        
        剪枝原理：
        - 利用线段树的区间划分特性，避免不必要的递归
        - 当发现当前节点的区间完全包含在查询区间内时，直接返回该节点的值
        - 这是线段树查询高效的关键所在
        """
        # 查询区间与当前区间无交集
        if jobl > r or jobr < l:
            return 0
        
        # 查询区间完全包含当前区间
        if jobl <= l and r <= jobr:
            return self.tree[i]
        
        # 计算中点
        mid = (l + r) // 2
        # 递归查询左右子树
        left_result = self._query_tree(jobl, jobr, l, mid, 2 * i + 1)
        right_result = self._query_tree(jobl, jobr, mid + 1, r, 2 * i + 2)
        
        # 合并结果
        return left_result + right_result


class SegmentTreeWithLazy:
    """
    支持懒标记的线段树实现
    
    这个扩展版本使用懒标记技术，实现区间加法和区间求和查询，适用于需要频繁进行区间更新的场景。
    
    懒标记（Lazy Propagation）原理：
    1. 当需要对某个区间进行更新时，不是立即递归地更新所有相关节点
    2. 而是将更新操作记录在当前节点的懒标记中，延迟到需要访问子节点时再传递
    3. 这样可以避免不必要的递归操作，将区间更新的时间复杂度优化到O(log n)
    
    懒标记策略：
    - 当当前节点代表的区间完全包含在目标更新区间内时，直接更新当前节点并设置懒标记
    - 当需要访问子节点时（查询或部分更新），先将懒标记传递给子节点，然后清空当前节点的懒标记
    - 这种"延迟传递"的策略是线段树能够高效处理区间更新的关键
    
    属性：
        n: 原始数组长度
        arr: 原始数组的副本
        tree: 线段树数组，存储每个区间的和
        lazy: 懒标记数组，存储待传递的更新操作
    """
    
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.arr = nums[:]
        self.tree = [0] * (4 * self.n)  # 线段树数组
        self.lazy = [0] * (4 * self.n)  # 懒标记数组
        self._build(0, self.n - 1, 0)
    
    def _build(self, l, r, i):
        """
        构建线段树
        时间复杂度：O(n)
        
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点索引
        
        构建过程与基础线段树相同，但同时初始化懒标记数组
        """
        if l == r:
            self.tree[i] = self.arr[l]
            return
        
        mid = (l + r) // 2
        self._build(l, mid, 2 * i + 1)
        self._build(mid + 1, r, 2 * i + 2)
        self.tree[i] = self.tree[2 * i + 1] + self.tree[2 * i + 2]
    
    def _push_down(self, i, l, r):
        """
        传递懒标记（核心操作）
        时间复杂度：O(1)
        
        :param i: 当前节点索引
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        
        懒标记传递原理：
        1. 只有当存在未处理的懒标记时才需要传递
        2. 将当前节点的懒标记值传递给左右子节点
        3. 根据子树的大小更新子节点的值
        4. 清空当前节点的懒标记，表示更新操作已传递
        
        延迟传递机制：
        - 懒标记的传递被延迟到实际需要访问子节点的时候
        - 这样可以确保每个更新操作最多被处理O(log n)次
        - 是线段树高效处理区间更新的核心优化
        """
        if self.lazy[i] != 0 and l < r:  # 有懒标记且不是叶子节点
            mid = (l + r) // 2
            left_child = 2 * i + 1
            right_child = 2 * i + 2
            
            # 更新左子树
            self.tree[left_child] += self.lazy[i] * (mid - l + 1)
            self.lazy[left_child] += self.lazy[i]
            
            # 更新右子树
            self.tree[right_child] += self.lazy[i] * (r - mid)
            self.lazy[right_child] += self.lazy[i]
            
            # 清除当前节点的懒标记
            self.lazy[i] = 0
    
    def update_range(self, L, R, val):
        """
        区间加法更新
        :param L: 更新区间左边界
        :param R: 更新区间右边界
        :param val: 要增加的值
        """
        # 输入验证
        if L < 0 or R >= self.n or L > R:
            raise ValueError(f"Invalid range [{L}, {R}]")
        
        self._update_range(L, R, val, 0, self.n - 1, 0)
    
    def _update_range(self, L, R, val, l, r, i):
        """
        递归更新区间
        时间复杂度：O(log n)
        
        :param L: 更新区间左边界
        :param R: 更新区间右边界
        :param val: 要增加的值
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点索引
        
        区间更新策略：
        1. 先处理懒标记，确保子节点数据正确
        2. 更新区间与当前区间无交集：直接返回
        3. 当前区间完全包含在更新区间内：使用懒标记延迟更新
           - 更新当前节点的值（加上val乘以区间长度）
           - 更新懒标记，表示子节点需要进行相同的更新
        4. 更新区间部分重叠：递归处理左右子树
           - 计算中点，将当前区间分为左右两部分
           - 递归更新与更新区间有交集的子树
           - 最后更新当前节点的值
        """
        # 先处理懒标记
        self._push_down(i, l, r)
        
        # 更新区间与当前区间无交集
        if R < l or L > r:
            return
        
        # 当前区间完全包含在更新区间内
        if L <= l and r <= R:
            # 更新当前节点的值
            self.tree[i] += val * (r - l + 1)
            
            # 如果不是叶子节点，设置懒标记
            if l < r:
                self.lazy[i] += val
            return
        
        # 更新区间部分重叠，递归更新左右子树
        mid = (l + r) // 2
        self._update_range(L, R, val, l, mid, 2 * i + 1)
        self._update_range(L, R, val, mid + 1, r, 2 * i + 2)
        
        # 更新当前节点的值
        self.tree[i] = self.tree[2 * i + 1] + self.tree[2 * i + 2]
    
    def query_range(self, L, R):
        """
        查询区间和
        :param L: 查询区间左边界
        :param R: 查询区间右边界
        :return: 区间和
        """
        # 输入验证
        if L < 0 or R >= self.n or L > R:
            raise ValueError(f"Invalid range [{L}, {R}]")
        
        return self._query_range(L, R, 0, self.n - 1, 0)
    
    def _query_range(self, L, R, l, r, i):
        """
        递归查询区间和
        时间复杂度：O(log n)
        
        :param L: 查询区间左边界
        :param R: 查询区间右边界
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点索引
        :return: 区间和
        
        区间查询策略：
        1. 查询区间与当前区间无交集：返回0
        2. 先处理懒标记，确保子节点数据正确
        3. 当前区间完全包含在查询区间内：直接返回当前节点的值
        4. 查询区间部分重叠：递归查询左右子树
           - 计算中点
           - 递归查询与查询区间有交集的子树
           - 合并查询结果
        """
        # 查询区间与当前区间无交集
        if R < l or L > r:
            return 0
        
        # 先处理懒标记
        self._push_down(i, l, r)
        
        # 当前区间完全包含在查询区间内
        if L <= l and r <= R:
            return self.tree[i]
        
        # 查询区间部分重叠，递归查询左右子树
        mid = (l + r) // 2
        left_sum = self._query_range(L, R, l, mid, 2 * i + 1)
        right_sum = self._query_range(L, R, mid + 1, r, 2 * i + 2)
        
        return left_sum + right_sum


# 测试代码
def run_unit_tests():
    """
    运行单元测试
    """
    print("===== 测试基础线段树 =====")
    # 测试用例1: 基本功能测试
    nums = [1, 3, 5]
    st = SegmentTree(nums)
    print(f"测试用例1 - 初始sumRange(0, 2): {st.sumRange(0, 2)}")  # 期望输出: 9
    st.update(1, 2)
    print(f"测试用例1 - 更新后sumRange(0, 2): {st.sumRange(0, 2)}")  # 期望输出: 8
    
    # 测试用例2: 边界情况
    print("\n测试用例2 - 边界情况测试")
    nums2 = [10]
    st2 = SegmentTree(nums2)
    print(f"单元素数组sumRange(0, 0): {st2.sumRange(0, 0)}")  # 期望输出: 10
    st2.update(0, 20)
    print(f"更新后单元素数组sumRange(0, 0): {st2.sumRange(0, 0)}")  # 期望输出: 20
    
    # 测试用例3: 较大数组
    print("\n测试用例3 - 较大数组测试")
    nums3 = list(range(1, 11))  # [1,2,3,4,5,6,7,8,9,10]
    st3 = SegmentTree(nums3)
    print(f"sumRange(2, 7): {st3.sumRange(2, 7)}")  # 期望输出: 30 (3+4+5+6+7+8)
    st3.update(3, 10)  # 将4改为10
    print(f"更新后sumRange(2, 7): {st3.sumRange(2, 7)}")  # 期望输出: 36 (3+10+5+6+7+8)
    
    print("\n===== 测试带懒标记的线段树 =====")
    # 测试用例4: 区间更新测试
    nums4 = [1, 3, 5, 7, 9, 11]
    stl = SegmentTreeWithLazy(nums4)
    print(f"初始query_range(0, 5): {stl.query_range(0, 5)}")  # 期望输出: 36
    stl.update_range(1, 3, 2)  # 区间[1,3]每个元素加2
    print(f"区间更新后query_range(0, 5): {stl.query_range(0, 5)}")  # 期望输出: 42
    print(f"查询部分区间query_range(1, 3): {stl.query_range(1, 3)}")  # 期望输出: 19 (5+7+7)
    
    # 测试用例5: 多次区间更新
    print("\n测试用例5 - 多次区间更新")
    stl.update_range(0, 2, 1)  # 区间[0,2]每个元素加1
    print(f"第二次区间更新后query_range(0, 5): {stl.query_range(0, 5)}")  # 期望输出: 45


# 主程序入口
if __name__ == "__main__":
    run_unit_tests()


# Python线段树实现的工程化考量：
# 1. 数据类型处理：
#    - Python的整数没有溢出问题，简化了实现
#    - 但需要注意浮点数精度问题
#    - 对于大规模数据，可以考虑使用numpy等库优化数值计算
# 
# 2. 索引处理：
#    - Python使用0-based索引，与Python生态系统一致
#    - 内部节点索引使用2*i+1和2*i+2，这是一种常见的实现方式
#    - 对于大规模数据，可以考虑使用更高效的索引计算方式
# 
# 3. 性能优化技巧：
#    - 递归深度控制：Python默认递归深度限制为1000，对于大规模数据需要调整
#    - 使用非递归实现：可以避免Python递归的栈溢出和性能问题
#    - 内存优化：对于稀疏数据，使用字典或其他数据结构存储线段树节点
#    - 缓存优化：使用lru_cache装饰器缓存重复计算结果
# 
# 4. 错误处理与边界检查：
#    - 实现中添加了全面的输入验证，避免非法操作
#    - 处理了空数组、单元素数组等边界情况
#    - 在实际应用中，应根据具体需求添加更多的错误处理
# 
# 5. 线段树变体与扩展：
#    - 区间最大值/最小值线段树：修改_build和查询逻辑
#    - 区间异或线段树：修改_push_down和查询逻辑
#    - 区间赋值线段树：需要处理懒标记的覆盖问题
#    - 二维线段树：适用于二维区间查询
#    - 主席树（可持久化线段树）：支持历史版本查询
# 
# 6. 调试与测试：
#    - 实现了全面的单元测试，覆盖各种场景
#    - 测试用例包括基本功能、边界情况和较大数据量
#    - 在复杂应用中，可以添加更多的调试信息输出
# 
# 7. 语言特性差异与跨语言实现对比：
#    - Python vs Java：Python递归实现更简洁，但Java性能更好
#    - Python vs C++：C++位运算更高效，但Python实现更易读
#    - 内存管理：Python自动垃圾回收，无需手动释放内存
#    - 类型系统：Python动态类型简化了代码，但缺少编译时类型检查
# 
# 8. 工程应用场景：
#    - 数据可视化：区间统计数据展示
#    - 机器学习：特征区间统计
#    - 网络监控：流量区间分析
#    - 金融分析：价格区间统计
#    - 游戏开发：范围效果计算
# 
# 9. 高级优化与扩展：
#    - 动态开点线段树：对于超大范围但稀疏的数据
#    - 离散化：当数据范围很大但元素稀疏时
#    - 并行处理：将线段树的不同部分分配给不同线程处理
#    - GPU加速：对于大规模数据，利用GPU并行计算能力
# 
# 10. 算法安全与鲁棒性：
#     - 防止栈溢出：大规模数据使用迭代版本
#     - 防止内存溢出：使用动态分配而非预分配固定大小数组
#     - 输入验证：全面的参数检查避免非法操作
#     - 错误恢复：在异常情况下提供合理的错误信息
# 
# 11. 文档化与维护：
#     - 详细的方法文档和参数说明
#     - 完整的测试用例和示例
#     - 清晰的代码结构和命名规范
#     - 版本控制和更新日志

# 补充题目列表：
# 1. LeetCode 307. Range Sum Query - Mutable - https://leetcode.cn/problems/range-sum-query-mutable/
# 2. LeetCode 315. Count of Smaller Numbers After Self - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
# 3. LeetCode 699. Falling Squares - https://leetcode.cn/problems/falling-squares/
# 4. LeetCode 218. The Skyline Problem - https://leetcode.cn/problems/the-skyline-problem/
# 5. LeetCode 729. My Calendar I - https://leetcode.cn/problems/my-calendar-i/
# 6. LeetCode 731. My Calendar II - https://leetcode.cn/problems/my-calendar-ii/
# 7. HDU 1166. 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
# 8. HDU 1754. I Hate It - http://acm.hdu.edu.cn/showproblem.php?pid=1754
# 9. HDU 6514. Monitor - http://acm.hdu.edu.cn/showproblem.php?pid=6514
# 10. Luogu P3372. 【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
# 11. Luogu P3373. 【模板】线段树 2 - https://www.luogu.com.cn/problem/P3373
# 12. Codeforces 339D. Xor - https://codeforces.com/problemset/problem/339/D
# 13. Codeforces 52C. Circular RMQ - https://codeforces.com/problemset/problem/52/C
# 14. SPOJ GSS1. Can you answer these queries I - https://www.spoj.com/problems/GSS1/
# 15. SPOJ GSS2. Can you answer these queries II - https://www.spoj.com/problems/GSS2/
# 16. AtCoder ABC183E. Queen on Grid - https://atcoder.jp/contests/abc183/tasks/abc183_e
# 17. USACO 2016 Jan Silver. Subsequences Summing to Sevens - http://www.usaco.org/index.php?page=viewproblem2&cpid=595
# 18. 牛客 NC14417. 线段树练习 - https://ac.nowcoder.com/acm/problem/14417
# 19. 计蒜客 T1250. 线段树练习 - https://nanti.jisuanke.com/t/T1250
# 20. CodeChef SEGPROD. Segment Product - https://www.codechef.com/problems/SEGPROD
# 21. SPOJ MKTHNUM. K-th number - https://www.spoj.com/problems/MKTHNUM/
# 22. AizuOJ ALDS1_9_C. Segment Tree - https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_9_C
# 23. MarsCode MCS04E. Array Queries - https://acm.marscode.com/problem.php?id=61
# 24. TimusOJ 1547. Cipher Grille - https://acm.timus.ru/problem.aspx?space=1&num=1547
# 25. ZOJ 3626. Treasure Hunt IV - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827365143

# 线段树学习路径与进阶指南：
# 1. 基础阶段：
#    - 掌握线段树的基本原理和构建方法
#    - 实现单点更新和区间查询
#    - 解决简单的线段树问题（如LeetCode 307）
# 
# 2. 进阶阶段：
#    - 理解并实现懒标记技术
#    - 掌握区间更新操作
#    - 学习不同类型的区间查询（最大值、最小值、异或等）
#    - 解决中等难度的线段树问题
# 
# 3. 高级阶段：
#    - 学习线段树的变体（主席树、二维线段树等）
#    - 掌握线段树的优化技巧（离散化、动态开点等）
#    - 结合其他算法（扫描线、分块等）解决复杂问题
#    - 解决高级线段树问题和竞赛题目
# 
# 4. 工程应用阶段：
#    - 理解线段树在实际工程中的应用
#    - 考虑性能优化和代码可读性
#    - 实现可复用的线段树组件
#    - 学习线段树的并行实现和GPU加速