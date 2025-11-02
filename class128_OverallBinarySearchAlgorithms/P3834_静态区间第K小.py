# P3834 【模板】可持久化线段树 2 / 静态区间第K小 - Python实现
# 题目来源：https://www.luogu.com.cn/problem/P3834
# 题目描述：给定一个长度为n的数组，有m次查询，每次查询[l,r]区间内第k小的数
# 解题思路：使用整体二分算法，将所有查询一起处理，避免对每个查询单独进行二分
# 时间复杂度：O((N+M) * logN * log(maxValue))
# 空间复杂度：O(N+M)
# 算法适用条件：
# 1. 询问的答案具有可二分性
# 2. 修改对判定答案的贡献互相独立
# 3. 修改如果对判定答案有贡献，则贡献为确定值
# 4. 贡献满足交换律、结合律，具有可加性
# 5. 题目允许离线操作

# 补充题目：POJ 2104 K-th Number
# 题目来源：http://poj.org/problem?id=2104
# 题目描述：给定一个长度为n的数组，有m次查询，每次查询[l,r]区间内第k小的数
# 解题思路：这是整体二分算法的经典应用，与P3834本质相同
# 时间复杂度：O((N+M) * logN * log(maxValue))
# 空间复杂度：O(N+M)
# 本题是静态区间第k小查询的标准问题，是整体二分算法的入门题目

import sys
from bisect import bisect_left

# 由于Python的性能限制，对于大数据可能超时，但在逻辑上是正确的

class Solution:
    def __init__(self):
        self.MAXN = 200001
        self.n = 0  # 数组长度
        self.m = 0  # 查询次数
        
        # 原始数组，存储输入的数值
        self.arr = [0] * self.MAXN
        
        # 离散化后的数组，用于离散化处理，将大值域映射到小下标范围
        self.sorted_arr = [0] * self.MAXN
        
        # 查询信息存储
        self.qid = [0] * self.MAXN  # 查询编号
        self.l = [0] * self.MAXN     # 查询区间左端点
        self.r = [0] * self.MAXN     # 查询区间右端点
        self.k = [0] * self.MAXN     # 查询第k小
        
        # 树状数组，用于维护当前值域范围内元素的个数
        self.tree = [0] * self.MAXN
        
        # 整体二分中用于分类查询的临时存储
        self.lset = [0] * self.MAXN  # 满足条件的查询
        self.rset = [0] * self.MAXN  # 不满足条件的查询
        
        # 查询的答案存储数组
        self.ans = [0] * self.MAXN
    
    def lowbit(self, i):
        """树状数组的lowbit操作
        计算二进制表示中最低位的1所代表的数值"""
        return i & -i
    
    def add(self, i, v):
        """树状数组单点更新
        在树状数组的第i个位置加上v"""
        while i <= self.n:
            self.tree[i] += v
            i += self.lowbit(i)
    
    def sum(self, i):
        """树状数组前缀和查询
        计算前缀和[1, i]的和"""
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret
    
    def query(self, l, r):
        """树状数组区间和查询
        计算区间和[l, r]的和"""
        return self.sum(r) - self.sum(l - 1)
    
    def compute(self, ql, qr, vl, vr):
        """整体二分核心函数
        ql, qr: 查询范围
        vl, vr: 值域范围（离散化后的下标）"""
        # 递归边界：没有查询需要处理
        if ql > qr:
            return
        
        # 如果值域范围只有一个值，说明找到了答案
        # 此时所有查询的答案都是sorted_arr[vl]
        if vl == vr:
            for i in range(ql, qr + 1):
                self.ans[self.qid[i]] = self.sorted_arr[vl]
            return
        
        # 二分中点
        mid = (vl + vr) >> 1
        
        # 将值域小于等于mid的数加入树状数组
        # 这些数对后续的查询统计有贡献
        for i in range(vl, mid + 1):
            self.add(self.arr[i], 1)
        
        # 检查每个查询，根据满足条件的元素个数划分到左右区间
        lsiz = 0
        rsiz = 0
        for i in range(ql, qr + 1):
            id = self.qid[i]
            # 查询区间[l[id], r[id]]中值小于等于sorted_arr[mid]的元素个数
            satisfy = self.query(self.l[id], self.r[id])
            
            if satisfy >= self.k[id]:
                # 说明第k小的数在左半部分
                # 将该查询加入左集合
                lsiz += 1
                self.lset[lsiz] = id
            else:
                # 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                # 更新k值，将该查询加入右集合
                self.k[id] -= satisfy
                rsiz += 1
                self.rset[rsiz] = id
        
        # 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
        for i in range(1, lsiz + 1):
            self.qid[ql + i - 1] = self.lset[i]
        for i in range(1, rsiz + 1):
            self.qid[ql + lsiz + i - 1] = self.rset[i]
        
        # 撤销对树状数组的修改，恢复到处理前的状态
        for i in range(vl, mid + 1):
            self.add(self.arr[i], -1)
        
        # 递归处理左右两部分
        # 左半部分：值域在[vl, mid]范围内的查询
        self.compute(ql, ql + lsiz - 1, vl, mid)
        # 右半部分：值域在[mid+1, vr]范围内的查询
        self.compute(ql + lsiz, qr, mid + 1, vr)
    
    def solve(self):
        """主函数"""
        # 读取输入
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        
        # 读取原始数组
        nums = sys.stdin.readline().split()
        for i in range(1, self.n + 1):
            self.arr[i] = int(nums[i - 1])
            self.sorted_arr[i] = self.arr[i]
        
        # 离散化：将大值域映射到小下标范围，减少二分的值域范围
        self.sorted_arr[1:self.n+1] = sorted(self.sorted_arr[1:self.n+1])
        unique_count = 1
        for i in range(2, self.n + 1):
            if self.sorted_arr[i] != self.sorted_arr[i - 1]:
                unique_count += 1
                self.sorted_arr[unique_count] = self.sorted_arr[i]
        
        # 重新映射arr数组为离散化后的下标
        # 这样可以将值域从[MIN_VALUE, MAX_VALUE]映射到[1, unique_count]
        for i in range(1, self.n + 1):
            # 使用二分查找找到arr[i]在sorted_arr中的位置
            self.arr[i] = bisect_left(self.sorted_arr, self.arr[i], 1, unique_count + 1)
        
        # 读取查询
        for i in range(1, self.m + 1):
            query_line = sys.stdin.readline().split()
            self.l[i] = int(query_line[0])
            self.r[i] = int(query_line[1])
            self.k[i] = int(query_line[2])
            self.qid[i] = i  # 查询编号
        
        # 整体二分求解
        # 初始查询范围[1, m]，初始值域范围[1, unique_count]
        self.compute(1, self.m, 1, unique_count)
        
        # 输出结果
        for i in range(1, self.m + 1):
            print(self.ans[i])

# 程序入口
#if __name__ == "__main__":
#    solution = Solution()
#    solution.solve()


# POJ 2104 K-th Number 完整实现类
# 这是整体二分算法的经典应用，与P3834本质相同
class POJ2104_KthNumber:
    def __init__(self):
        self.MAXN = 100001  # 题目数据范围
        self.n = 0  # 数组长度
        self.m = 0  # 查询次数
        
        # 原始数组，存储输入的数值
        self.arr = [0] * self.MAXN
        
        # 离散化后的数组，用于离散化处理，将大值域映射到小下标范围
        self.sorted_arr = [0] * self.MAXN
        
        # 查询信息存储
        self.qid = [0] * self.MAXN  # 查询编号
        self.l = [0] * self.MAXN     # 查询区间左端点
        self.r = [0] * self.MAXN     # 查询区间右端点
        self.k = [0] * self.MAXN     # 查询第k小
        
        # 树状数组，用于维护当前值域范围内元素的个数
        self.tree = [0] * self.MAXN
        
        # 整体二分中用于分类查询的临时存储
        self.lset = [0] * self.MAXN  # 满足条件的查询
        self.rset = [0] * self.MAXN  # 不满足条件的查询
        
        # 查询的答案存储数组
        self.ans = [0] * self.MAXN
    
    def lowbit(self, i):
        """树状数组的lowbit操作
        计算二进制表示中最低位的1所代表的数值"""
        return i & -i
    
    def add(self, i, v):
        """树状数组单点更新
        在树状数组的第i个位置加上v"""
        while i <= self.n:
            self.tree[i] += v
            i += self.lowbit(i)
    
    def sum(self, i):
        """树状数组前缀和查询
        计算前缀和[1, i]的和"""
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret
    
    def query(self, l, r):
        """树状数组区间和查询
        计算区间和[l, r]的和"""
        return self.sum(r) - self.sum(l - 1)
    
    def compute(self, ql, qr, vl, vr):
        """整体二分核心函数
        ql, qr: 查询范围
        vl, vr: 值域范围（离散化后的下标）"""
        # 递归边界：没有查询需要处理
        if ql > qr:
            return
        
        # 如果值域范围只有一个值，说明找到了答案
        # 此时所有查询的答案都是sorted_arr[vl]
        if vl == vr:
            for i in range(ql, qr + 1):
                self.ans[self.qid[i]] = self.sorted_arr[vl]
            return
        
        # 二分中点
        mid = (vl + vr) >> 1
        
        # 正确的做法：遍历原始数组，如果元素值对应的离散化下标<=mid，则在对应位置+1
        for i in range(1, self.n + 1):
            if self.arr[i] <= mid:
                self.add(i, 1)
        
        # 检查每个查询，根据满足条件的元素个数划分到左右区间
        lsiz = 0
        rsiz = 0
        for i in range(ql, qr + 1):
            id = self.qid[i]
            # 查询区间[l[id], r[id]]中值小于等于sorted_arr[mid]的元素个数
            satisfy = self.query(self.l[id], self.r[id])
            
            if satisfy >= self.k[id]:
                # 说明第k小的数在左半部分
                # 将该查询加入左集合
                lsiz += 1
                self.lset[lsiz] = id
            else:
                # 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                # 更新k值，将该查询加入右集合
                self.k[id] -= satisfy
                rsiz += 1
                self.rset[rsiz] = id
        
        # 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
        for i in range(1, lsiz + 1):
            self.qid[ql + i - 1] = self.lset[i]
        for i in range(1, rsiz + 1):
            self.qid[ql + lsiz + i - 1] = self.rset[i]
        
        # 撤销对树状数组的修改，恢复到处理前的状态
        for i in range(1, self.n + 1):
            if self.arr[i] <= mid:
                self.add(i, -1)
        
        # 递归处理左右两部分
        # 左半部分：值域在[vl, mid]范围内的查询
        self.compute(ql, ql + lsiz - 1, vl, mid)
        # 右半部分：值域在[mid+1, vr]范围内的查询
        self.compute(ql + lsiz, qr, mid + 1, vr)
    
    def solve(self):
        """主函数，用于解决POJ 2104问题"""
        import sys
        from bisect import bisect_left
        
        # 读取输入
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        
        # 读取原始数组
        nums = sys.stdin.readline().split()
        for i in range(1, self.n + 1):
            self.arr[i] = int(nums[i - 1])
            self.sorted_arr[i] = self.arr[i]
        
        # 离散化：将大值域映射到小下标范围，减少二分的值域范围
        self.sorted_arr[1:self.n+1] = sorted(self.sorted_arr[1:self.n+1])
        unique_count = 1
        for i in range(2, self.n + 1):
            if self.sorted_arr[i] != self.sorted_arr[i - 1]:
                unique_count += 1
                self.sorted_arr[unique_count] = self.sorted_arr[i]
        
        # 重新映射arr数组为离散化后的下标
        # 这样可以将值域从[MIN_VALUE, MAX_VALUE]映射到[1, unique_count]
        for i in range(1, self.n + 1):
            # 使用二分查找找到arr[i]在sorted_arr中的位置
            self.arr[i] = bisect_left(self.sorted_arr, self.arr[i], 1, unique_count + 1)
        
        # 读取查询
        for i in range(1, self.m + 1):
            query_line = sys.stdin.readline().split()
            self.l[i] = int(query_line[0])
            self.r[i] = int(query_line[1])
            self.k[i] = int(query_line[2])
            self.qid[i] = i  # 查询编号
        
        # 初始化树状数组为0
        self.tree = [0] * self.MAXN
        
        # 整体二分求解
        # 初始查询范围[1, m]，初始值域范围[1, unique_count]
        self.compute(1, self.m, 1, unique_count)
        
        # 输出结果
        for i in range(1, self.m + 1):
            print(self.ans[i])

# 注意：在实际提交POJ时，需要将该类作为主程序运行
# if __name__ == "__main__":
#     solution = POJ2104_KthNumber()
#     solution.solve()


# 补充题目：HDU 2665 Kth Number
# 题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=2665
# 题目描述：给定一个长度为n的数组，有m次查询，每次查询[l,r]区间内第k小的数
# 解题思路：与POJ 2104完全相同，是静态区间第k小的标准问题
# 时间复杂度：O((N+M) * logN * log(maxValue))
# 空间复杂度：O(N+M)
# 注意事项：HDU的评测系统对输入输出效率要求较高，需要使用快速IO


# 补充题目：HDU 5412 CRB and Queries
# 题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=5412
# 题目描述：给定一个数组，支持单点修改和区间第k小查询
# 解题思路：带修改的区间第k小问题，可以用整体二分算法结合树状数组解决
# 时间复杂度：O((N+Q) * logN * log(maxValue))
# 空间复杂度：O(N+Q)
# 算法说明：在整体二分过程中，需要处理两种操作：修改操作和查询操作，
#           修改操作相当于将旧值删除，新值插入，需要分别处理


# 补充题目：AGC002D Stamp Rally
# 题目来源：https://atcoder.jp/contests/agc002/tasks/agc002_d
# 题目描述：在图中寻找k条路径，使得这些路径的起点和终点分别为给定的k对节点，
#          并且所有路径的边的权值的最大值尽可能小
# 解题思路：使用整体二分结合并查集（支持撤销操作）
# 时间复杂度：O(E * α(N) * log(maxWeight))
# 空间复杂度：O(N + E)
# 算法说明：将边权排序后进行二分，用可撤销并查集判断每组查询是否可以连通


# 整体二分算法的通用技巧总结：
# 1. 适用场景：
#    - 当问题的答案具有单调性，可以进行二分
#    - 离线处理多个查询，每个查询的答案可以在二分过程中同步处理
#    - 需要统计满足某些条件的元素个数
# 2. 常用数据结构：
#    - 树状数组：处理区间查询和单点更新
#    - 线段树：处理更复杂的区间操作
#    - 并查集：处理连通性问题，特别是带有撤销功能的并查集
# 3. 优化技巧：
#    - 离散化：将大值域映射到小范围，减少二分次数
#    - 避免重复计算：通过合理的设计，避免对相同数据进行多次处理
#    - 减少常数：使用位运算等技巧优化循环和条件判断
# 4. 注意事项：
#    - 必须确保问题可以离线处理
#    - 需要正确处理撤销操作，恢复数据结构到初始状态
#    - 递归深度需要控制，避免栈溢出
# 5. 代码工程化建议：
#    - 使用类封装数据结构和算法，提高可维护性
#    - 添加详细的注释，特别是关键步骤的说明
#    - 对于不同语言，注意数据类型范围和效率问题
#    - 添加异常处理，特别是对于边界情况
#    - 编写单元测试，验证算法正确性
# 6. 语言特性差异：
#    - Java：注意数组初始化和边界检查，使用快速IO提高效率
#    - C++：可以使用STL提高开发效率，但需要注意内存管理
#    - Python：注意递归深度限制和性能问题，对于大数据可能需要优化