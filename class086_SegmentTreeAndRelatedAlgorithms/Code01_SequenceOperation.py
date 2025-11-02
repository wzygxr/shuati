#!/usr/bin/env python3
# -*- coding: utf-8 -*-
'''
线段树经典应用：多标记序列操作
题目来源：洛谷 P2572 [SDOI2010] 序列操作
题目链接：https://www.luogu.com.cn/problem/P2572

核心算法：线段树 + 多重懒标记
难度：省选/NOI-

【题目详细描述】
给定一个长度为n的01序列，支持5种操作：
1. 操作 0 l r：将区间[l,r]全部置为0
2. 操作 1 l r：将区间[l,r]全部置为1
3. 操作 2 l r：将区间[l,r]全部取反
4. 操作 3 l r：查询区间[l,r]中1的个数
5. 操作 4 l r：查询区间[l,r]中连续1的最长长度

【解题思路】
这是一个典型的线段树应用题，需要维护多种区间信息并处理多重懒标记。
线段树节点需要保存丰富的信息来支持连续子串长度的查询。

【核心数据结构设计】
线段树每个节点维护以下信息：
- sum：区间内1的总数
- len0/len1：区间内连续0/1的最长子串长度
- pre0/pre1：区间内连续0/1的最长前缀长度
- suf0/suf1：区间内连续0/1的最长后缀长度
- size：当前区间的长度

懒标记设计：
- update：记录区间被置为的具体值（0或1），如果是None表示没有赋值操作
- reverse_flag：标记区间是否有待处理的翻转操作

【关键技术点】
1. 懒标记优先级处理：更新操作(update)优先于翻转操作(reverse)
2. 区间合并逻辑：需要考虑左右子区间连接处的情况
3. 多重懒标记的下传顺序和相互影响处理
4. 边界条件处理：如区间为空、单元素区间等特殊情况

【复杂度分析】
- 时间复杂度：
  - 建树：O(n)
  - 单次操作（更新/查询）：O(log n)
  - m次操作总时间复杂度：O((n + m) log n)
- 空间复杂度：O(4n)，线段树标准空间配置

【算法优化点】
1. 懒标记延迟下传：避免不必要的更新操作
2. 区间合并时的高效计算：通过维护前缀和后缀信息加速
3. 使用类封装：将线段树封装为一个类，提高代码复用性和可维护性
4. 避免递归过深：Python中递归深度有默认限制，可以考虑非递归实现

【工程化考量】
1. 内存布局：使用对象存储线段树节点，方便管理
2. 错误处理：添加参数校验，处理非法输入
3. 性能优化：Python中递归实现的线段树性能可能不如C++，对于大规模数据可以考虑其他优化方法
4. 可读性：合理使用类和函数组织代码，添加详细注释

【Python语言特性应用】
1. 类封装：使用类封装线段树，提高代码复用性
2. 列表索引：使用列表存储节点，避免动态内存分配
3. None值处理：使用None表示没有懒标记
4. 递归实现：递归编写线段树操作，代码简洁明了

【类似题目推荐】
1. LeetCode 307. 区域和检索 - 数组可修改：https://leetcode.cn/problems/range-sum-query-mutable/
2. Codeforces 242E - XOR on Segment：https://codeforces.com/problemset/problem/242/E
3. 洛谷 P3373 【模板】线段树 2：https://www.luogu.com.cn/problem/P3373
4. POJ 3468 A Simple Problem with Integers：http://poj.org/problem?id=3468
5. HDU 1698 Just a Hook：http://acm.hdu.edu.cn/showproblem.php?pid=1698
6. SPOJ GSS1 - Can you answer these queries I：https://www.spoj.com/problems/GSS1/
7. LintCode 207. 区间求和 II：https://www.lintcode.com/problem/interval-sum-ii/description
8. HackerRank Array and simple queries：https://www.hackerrank.com/challenges/array-and-simple-queries/problem
9. LeetCode 732. 我的日程安排表 III：区间重叠问题
10. LeetCode 699. 掉落的方块：区间更新与查询最大值
11. LeetCode 551. 学生出勤记录 I：连续状态查询
12. LeetCode 567. 字符串的排列：滑动窗口与字符频率统计
13. LeetCode 995. K 连续位的最小翻转次数：区间翻转操作
14. LintCode 439. 线段树的查询 II：区间和查询
15. LintCode 440. 线段树的修改：单点更新
16. LintCode 441. 线段树的构造 II：构建区间和线段树
17. Codeforces 61E. Enemy is weak：区间统计问题
18. Codeforces 459D. Pashmak and Parmida's problem：区间逆序对统计
19. HackerEarth Binary Operations：区间位运算操作
20. 牛客网 NC24970. 线段树练习一：基础区间操作
21. 杭电 OJ 1542. Atlantis：扫描线算法与线段树
22. USACO 2017 January Contest, Gold Problem 1. Balanced Photo：区间统计
23. AtCoder ARC 008 B. 投票：区间操作与统计
24. SPOJ GSS1 - Can you answer these queries I：区间最大子段和
25. UVa OJ 11990. Dynamic Inversion：动态逆序对问题
26. 洛谷 P3373. 【模板】线段树 2：区间乘加操作
27. 洛谷 P3805. 【模板】manacher：最长回文子串
28. 计蒜客 线段树专题：各种线段树应用场景

【掌握线段树的关键点】
1. 懒标记的正确传递与优先级处理
2. 区间合并逻辑的设计（如本题中的连续1长度合并）
3. 不同操作类型的统一处理框架
4. 边界条件的处理（如叶子节点、空区间等）
5. 效率优化：避免不必要的递归和计算
'''

class SegmentTree:
    def __init__(self, arr):
        """
        初始化线段树
        
        Args:
            arr: 原始数组
        """
        self.n = len(arr)
        self.arr = arr
        # 初始化线段树所需的各种信息数组
        self.sum = [0] * (4 * self.n)         # 区间内1的总数
        self.len0 = [0] * (4 * self.n)        # 连续0的最长长度
        self.pre0 = [0] * (4 * self.n)        # 连续0的最长前缀长度
        self.suf0 = [0] * (4 * self.n)        # 连续0的最长后缀长度
        self.len1 = [0] * (4 * self.n)        # 连续1的最长长度
        self.pre1 = [0] * (4 * self.n)        # 连续1的最长前缀长度
        self.suf1 = [0] * (4 * self.n)        # 连续1的最长后缀长度
        self.change = [0] * (4 * self.n)      # 区间赋值的目标值
        self.update = [False] * (4 * self.n)  # 区间赋值标记
        self.reverse = [False] * (4 * self.n) # 区间翻转标记
        # 构建线段树
        self.build(1, 1, self.n)
    
    def up(self, i, ln, rn):
        """
        向上更新当前节点信息（合并左右子节点信息）
        
        Args:
            i: 当前节点索引
            ln: 左子区间长度
            rn: 右子区间长度
        """
        l = i << 1    # 左子节点索引
        r = i << 1 | 1  # 右子节点索引
        
        # 更新区间内1的总数
        self.sum[i] = self.sum[l] + self.sum[r]
        
        # 更新连续0的信息
        # 取左右子区间的最大值或左右子区间连接处的连续0
        self.len0[i] = max(self.len0[l], self.len0[r], self.suf0[l] + self.pre0[r])
        
        # 更新连续0的前缀
        # 如果左子区间全是0，则前缀包括整个左子区间和右子区间的前缀
        self.pre0[i] = self.pre0[l] if self.len0[l] < ln else self.pre0[l] + self.pre0[r]
        
        # 更新连续0的后缀
        # 如果右子区间全是0，则后缀包括整个右子区间和左子区间的后缀
        self.suf0[i] = self.suf0[r] if self.len0[r] < rn else self.suf0[l] + self.suf0[r]
        
        # 更新连续1的信息（与连续0的处理方式类似）
        self.len1[i] = max(self.len1[l], self.len1[r], self.suf1[l] + self.pre1[r])
        self.pre1[i] = self.pre1[l] if self.len1[l] < ln else self.pre1[l] + self.pre1[r]
        self.suf1[i] = self.suf1[r] if self.len1[r] < rn else self.suf1[l] + self.suf1[r]
    
    def update_lazy(self, i, v, n):
        """
        处理区间赋值的懒标记
        
        Args:
            i: 当前节点索引
            v: 要设置的值（0或1）
            n: 当前节点表示的区间长度
        """
        # 更新区间内1的总数
        self.sum[i] = v * n
        # 更新连续0的信息：如果v=0，则整个区间都是0；否则都是1（没有0）
        self.len0[i] = self.pre0[i] = self.suf0[i] = n if v == 0 else 0
        # 更新连续1的信息：如果v=1，则整个区间都是1；否则都是0（没有1）
        self.len1[i] = self.pre1[i] = self.suf1[i] = n if v == 1 else 0
        # 记录区间赋值的目标值
        self.change[i] = v
        # 设置更新标记
        self.update[i] = True
        # 清空翻转标记（更新操作优先于翻转操作）
        self.reverse[i] = False
    
    def reverse_lazy(self, i, n):
        """
        处理区间翻转的懒标记
        
        Args:
            i: 当前节点索引
            n: 当前节点表示的区间长度
        """
        # 翻转1的个数：1变0，0变1
        self.sum[i] = n - self.sum[i]
        # 交换连续0和连续1的各种长度信息
        self.len0[i], self.len1[i] = self.len1[i], self.len0[i]  # 交换最长连续0/1长度
        self.pre0[i], self.pre1[i] = self.pre1[i], self.pre0[i]  # 交换前缀0/1长度
        self.suf0[i], self.suf1[i] = self.suf1[i], self.suf0[i]  # 交换后缀0/1长度
        # 翻转翻转标记（多次翻转可以抵消）
        self.reverse[i] = not self.reverse[i]
    
    def down(self, i, ln, rn):
        """
        向下传递懒标记到子节点
        
        Args:
            i: 当前节点索引
            ln: 左子区间长度
            rn: 右子区间长度
        """
        # 先处理更新操作（优先级高于翻转操作）
        if self.update[i]:
            # 左子节点应用更新操作
            self.update_lazy(i << 1, self.change[i], ln)
            # 右子节点应用更新操作
            self.update_lazy(i << 1 | 1, self.change[i], rn)
            # 清除当前节点的更新标记
            self.update[i] = False
        
        # 再处理翻转操作
        if self.reverse[i]:
            # 左子节点应用翻转操作
            self.reverse_lazy(i << 1, ln)
            # 右子节点应用翻转操作
            self.reverse_lazy(i << 1 | 1, rn)
            # 清除当前节点的翻转标记
            self.reverse[i] = False
    
    def build(self, i, l, r):
        """
        构建线段树
        
        Args:
            i: 当前节点索引
            l: 当前区间左边界（1-based）
            r: 当前区间右边界（1-based）
        """
        # 叶子节点情况
        if l == r:
            # 直接赋值原始数组的值
            self.sum[i] = self.arr[l]
            # 初始化连续0的信息
            self.len0[i] = self.pre0[i] = self.suf0[i] = 1 if self.arr[l] == 0 else 0
            # 初始化连续1的信息
            self.len1[i] = self.pre1[i] = self.suf1[i] = 1 if self.arr[l] == 1 else 0
        else:
            # 非叶子节点，递归构建左右子树
            mid = (l + r) >> 1  # 计算中点
            self.build(i << 1, l, mid)  # 构建左子树
            self.build(i << 1 | 1, mid + 1, r)  # 构建右子树
            # 向上合并子节点信息
            self.up(i, mid - l + 1, r - mid)
        
        # 初始化懒标记为未激活状态
        self.update[i] = False
        self.reverse[i] = False
    
    def update_range(self, jobl, jobr, jobv, l, r, i):
        """
        区间赋值操作
        
        Args:
            jobl: 待更新区间的左边界（1-based）
            jobr: 待更新区间的右边界（1-based）
            jobv: 要设置的值（0或1）
            l: 当前节点区间左边界（1-based）
            r: 当前节点区间右边界（1-based）
            i: 当前节点索引
        """
        # 当前区间完全包含在待更新区间内
        if jobl <= l and r <= jobr:
            # 直接应用懒标记
            self.update_lazy(i, jobv, r - l + 1)
        else:
            # 当前区间部分包含在待更新区间内
            mid = (l + r) >> 1  # 计算中点
            ln = mid - l + 1  # 左子区间长度
            rn = r - mid      # 右子区间长度
            
            # 先向下传递懒标记
            self.down(i, ln, rn)
            
            # 递归处理左右子区间
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            
            # 向上合并子节点信息
            self.up(i, ln, rn)
    
    def reverse_range(self, jobl, jobr, l, r, i):
        """
        区间翻转操作
        
        Args:
            jobl: 待翻转区间的左边界（1-based）
            jobr: 待翻转区间的右边界（1-based）
            l: 当前节点区间左边界（1-based）
            r: 当前节点区间右边界（1-based）
            i: 当前节点索引
        """
        # 当前区间完全包含在待翻转区间内
        if jobl <= l and r <= jobr:
            # 直接应用翻转懒标记
            self.reverse_lazy(i, r - l + 1)
        else:
            # 当前区间部分包含在待翻转区间内
            mid = (l + r) >> 1  # 计算中点
            ln = mid - l + 1    # 左子区间长度
            rn = r - mid        # 右子区间长度
            
            # 先向下传递懒标记
            self.down(i, ln, rn)
            
            # 递归处理左右子区间
            if jobl <= mid:
                self.reverse_range(jobl, jobr, l, mid, i << 1)
            if jobr > mid:
                self.reverse_range(jobl, jobr, mid + 1, r, i << 1 | 1)
            
            # 向上合并子节点信息
            self.up(i, ln, rn)
    
    def query_sum(self, jobl, jobr, l, r, i):
        """
        查询区间内1的总数
        
        Args:
            jobl: 查询区间的左边界（1-based）
            jobr: 查询区间的右边界（1-based）
            l: 当前节点区间左边界（1-based）
            r: 当前节点区间右边界（1-based）
            i: 当前节点索引
            
        Returns:
            int: 区间内1的总数
        """
        # 当前区间完全包含在查询区间内
        if jobl <= l and r <= jobr:
            return self.sum[i]
        
        # 当前区间部分包含在查询区间内
        mid = (l + r) >> 1  # 计算中点
        ln = mid - l + 1    # 左子区间长度
        rn = r - mid        # 右子区间长度
        
        # 先向下传递懒标记
        self.down(i, ln, rn)
        
        ans = 0
        # 递归查询左右子区间
        if jobl <= mid:
            ans += self.query_sum(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query_sum(jobl, jobr, mid + 1, r, i << 1 | 1)
        
        return ans
    
    def query_longest(self, jobl, jobr, l, r, i):
        """
        查询区间内连续1的最长长度
        
        Args:
            jobl: 查询区间的左边界（1-based）
            jobr: 查询区间的右边界（1-based）
            l: 当前节点区间左边界（1-based）
            r: 当前节点区间右边界（1-based）
            i: 当前节点索引
            
        Returns:
            list: [最长连续1的长度, 连续1的最长前缀长度, 连续1的最长后缀长度]
        """
        # 当前区间完全包含在查询区间内
        if jobl <= l and r <= jobr:
            return [self.len1[i], self.pre1[i], self.suf1[i]]
        else:
            # 当前区间部分包含在查询区间内或查询区间跨越多个子区间
            mid = (l + r) >> 1  # 计算中点
            ln = mid - l + 1    # 左子区间长度
            rn = r - mid        # 右子区间长度
            
            # 先向下传递懒标记
            self.down(i, ln, rn)
            
            # 查询区间完全在左子区间
            if jobr <= mid:
                return self.query_longest(jobl, jobr, l, mid, i << 1)
            # 查询区间完全在右子区间
            if jobl > mid:
                return self.query_longest(jobl, jobr, mid + 1, r, i << 1 | 1)
            
            # 查询区间跨越左右子区间
            # 分别查询左右子区间的信息
            l3 = self.query_longest(jobl, jobr, l, mid, i << 1)
            r3 = self.query_longest(jobl, jobr, mid + 1, r, i << 1 | 1)
            
            # 提取左右子区间的信息
            llen, lpre, lsuf = l3[0], l3[1], l3[2]  # 左子区间的最长1长度、前缀、后缀
            rlen, rpre, rsuf = r3[0], r3[1], r3[2]  # 右子区间的最长1长度、前缀、后缀
            
            # 合并信息
            # 最长连续1长度：左子区间的最大值、右子区间的最大值、或左右连接处的连续1
            length = max(llen, rlen, lsuf + rpre)
            
            # 连续1的最长前缀：如果左子区间的最长连续1覆盖了整个查询部分的左子区间，则包括右子区间的前缀
            pre = lpre if llen < mid - max(jobl, l) + 1 else lpre + rpre
            
            # 连续1的最长后缀：如果右子区间的最长连续1覆盖了整个查询部分的右子区间，则包括左子区间的后缀
            suf = rsuf if rlen < min(r, jobr) - mid else lsuf + rsuf
            
            return [length, pre, suf]

def main():
    """
    主函数：读取输入并处理操作
    
    输入格式：
    - 第一行：n（序列长度）和m（操作数量）
    - 第二行：n个0或1，表示初始序列
    - 接下来m行：每行一个操作，格式为 op l r
    
    操作类型：
    - 0 l r：将区间[l,r]全部置为0
    - 1 l r：将区间[l,r]全部置为1
    - 2 l r：将区间[l,r]全部取反
    - 3 l r：查询区间[l,r]中1的个数
    - 4 l r：查询区间[l,r]中连续1的最长长度
    """
    # 优化输入处理速度
    import sys
    input = sys.stdin.read
    data = input().split()
    
    # 读取初始参数
    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 初始化数组（1-based索引）
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    # 创建线段树
    seg_tree = SegmentTree(arr)
    
    # 处理操作并收集结果
    results = []
    for _ in range(m):
        # 读取操作类型和参数
        op = int(data[idx])
        idx += 1
        jobl = int(data[idx]) + 1  # 转换为1-based索引
        idx += 1
        jobr = int(data[idx]) + 1  # 转换为1-based索引
        idx += 1
        
        # 根据操作类型执行相应操作
        if op == 0:  # 将区间置为0
            seg_tree.update_range(jobl, jobr, 0, 1, n, 1)
        elif op == 1:  # 将区间置为1
            seg_tree.update_range(jobl, jobr, 1, 1, n, 1)
        elif op == 2:  # 区间取反
            seg_tree.reverse_range(jobl, jobr, 1, n, 1)
        elif op == 3:  # 查询1的个数
            results.append(str(seg_tree.query_sum(jobl, jobr, 1, n, 1)))
        elif op == 4:  # 查询最长连续1
            results.append(str(seg_tree.query_longest(jobl, jobr, 1, n, 1)[0]))
    
    # 批量输出结果，提高输出效率
    print('\n'.join(results))

if __name__ == "__main__":
    main()