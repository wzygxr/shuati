# 超级钢琴
# 小Z是一个小有名气的钢琴家，最近C博士送给了小Z一架超级钢琴，小Z希望能够用这架钢琴创作出世界上最美妙的音乐。
# 这架超级钢琴可以弹奏出n个音符，编号为1至n。第i个音符的美妙度为Ai，其中Ai可正可负。
# 一个"超级和弦"由若干个编号连续的音符组成，包含的音符个数不少于L且不多于R。
# 我们定义超级和弦的美妙度为其包含的所有音符的美妙度之和。
# 小Z决定创作一首由k个超级和弦组成的乐曲，为了使得乐曲更加动听，小Z要求该乐曲由k个不同的超级和弦组成。
# 我们定义一首乐曲的美妙度为其所包含的所有超级和弦的美妙度之和。
# 小Z想知道他能够创作出来的乐曲美妙度最大值是多少。
# 测试链接 : https://www.luogu.com.cn/problem/P2048

# 补充题目链接：
# 1. 超级钢琴 - 洛谷 P2048
#    来源：洛谷
#    内容：给定n个音符，选择k个长度在[L,R]之间的连续子序列，使得它们的和最大
#    网址：https://www.luogu.com.cn/problem/P2048
#
# 2. 最大子序列和 - LeetCode 53
#    来源：LeetCode
#    内容：找到一个整数数组中连续子数组的最大和
#    网址：https://leetcode.cn/problems/maximum-subarray/
#
# 3. 区间第k大 - HDU 5919
#    来源：HDU
#    内容：静态区间查询，求区间内不同数字形成的序列中第k大的数
#    网址：http://acm.hdu.edu.cn/showproblem.php?pid=5919

import heapq
import math

class SparseTable:
    """
    稀疏表（Sparse Table，ST表）是一种用于解决区间最值查询（RMQ）问题的数据结构
    预处理时间复杂度：O(n log n)
    查询时间复杂度：O(1)
    空间复杂度：O(n log n)
    """
    
    def __init__(self, arr):
        """
        初始化ST表
        :param arr: 输入数组，用于构建ST表
        """
        self.n = len(arr)  # 数组长度
        self.k = int(math.log2(self.n)) + 1  # 最大的指数k，使得2^k <= n
        # st[i][j]表示从位置i开始长度为2^j的区间内最大值的位置
        self.st = [[0] * self.k for _ in range(self.n)]
        
        # 初始化第一列，即j=0的情况
        # 长度为2^0=1的区间，最大值位置就是区间本身的位置i
        for i in range(self.n):
            self.st[i][0] = i
            
        # 动态规划填表，j表示指数（区间长度为2^j），i表示起始位置
        for j in range(1, self.k):
            i = 0
            # 注意：这里使用while循环而不是for循环，避免越界
            while i + (1 << j) <= self.n:
                # 比较两个长度为2^(j-1)的相邻区间的最大值位置对应的数组值大小
                # 左半部分：[i, i+2^(j-1)-1]，最大值位置为st[i][j-1]
                # 右半部分：[i+2^(j-1), i+2^j-1]，最大值位置为st[i + (1 << (j-1))][j-1]
                if arr[self.st[i][j-1]] >= arr[self.st[i + (1 << (j-1))][j-1]]:
                    self.st[i][j] = self.st[i][j-1]  # 左半部分的最大值更大或相等
                else:
                    self.st[i][j] = self.st[i + (1 << (j-1))][j-1]  # 右半部分的最大值更大
                i += 1
    
    def query(self, l, r, arr):
        """
        查询区间[l, r]内数组值的最大值位置
        利用ST表进行RMQ查询，时间复杂度O(1)
        :param l: 查询区间左端点（包含）
        :param r: 查询区间右端点（包含）
        :param arr: 原数组，用于比较值的大小
        :return: 区间内数组值最大值的位置
        """
        # 计算k，使得2^k <= (r-l+1) < 2^(k+1)
        # 即k = floor(log2(r-l+1))
        k = int(math.log2(r - l + 1))
        
        # 将区间[l,r]分解为两个长度为2^k的重叠区间：
        # 1. [l, l+2^k-1]
        # 2. [r-2^k+1, r]
        # 这两个区间覆盖了整个[l,r]区间
        if arr[self.st[l][k]] >= arr[self.st[r - (1 << k) + 1][k]]:
            return self.st[l][k]  # 第一个区间的最大值更大或相等
        else:
            return self.st[r - (1 << k) + 1][k]  # 第二个区间的最大值更大

def main():
    # 读取输入
    # n: 音符数量
    # k: 需要选择的超级和弦数量
    # L: 超级和弦最少包含音符数
    # R: 超级和弦最多包含音符数
    n, k, L, R = map(int, input().split())
    a = list(map(int, input().split()))  # 音符美妙度数组
    
    # 计算前缀和
    # prefix_sum[i]表示前i个元素的和
    # prefix_sum[0] = 0, prefix_sum[1] = a[0], prefix_sum[2] = a[0] + a[1], ...
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + a[i]
    
    # 构建ST表，用于后续快速查询区间内前缀和的最大值位置
    st = SparseTable(prefix_sum)
    
    # 优先队列存储五元组(value, l, r, pos, kth)
    # value: 区间和（使用负值实现最大堆）
    # l: 左端点固定为l
    # r: 右端点的有效范围上限
    # pos: 在当前有效范围内前缀和的最大值位置
    # kth: 当前第k大（在有效范围内）
    pq = []
    
    # 初始化优先队列，对于每个左端点，预先计算其对应的最大值
    for i in range(1, n + 1):
        # 对于每个左端点i，确定右端点的范围
        # 右端点至少为i+L-1（保证至少L个音符）
        # 右端点至多为min(n, i+R-1)（不能超过总音符数）
        l = i + L - 1
        r = min(n, i + R - 1)
        if l <= r:
            # 查询该范围内前缀和的最大值位置
            # 注意：我们查询的是[l-1, r-1]范围内前缀和的最大值位置
            # 因为我们实际需要的是区间[i, pos+1]的美妙度 = prefix_sum[pos+1] - prefix_sum[i-1]
            pos = st.query(l - 1, r - 1, prefix_sum)
            # 计算区间[i, pos+1]的美妙度
            value = prefix_sum[pos + 1] - prefix_sum[i - 1]
            # 将五元组加入优先队列（使用负值实现最大堆）
            # Python的heapq是最小堆，通过存储负值来实现最大堆的效果
            heapq.heappush(pq, (-value, i, r, pos, 1))
    
    ans = 0  # 最终答案，所有选中超级和弦的美妙度之和
    # 取k个最大值
    for _ in range(k):
        neg_value, l, r, pos, kth = heapq.heappop(pq)
        value = -neg_value  # 转换回正值
        ans += value  # 累加到结果中
        
        # 如果还有更大的k值，继续生成下一个候选值
        # r - l - L + 2 表示在有效范围内能选出的不同区间的数量
        if kth + 1 <= r - l - L + 2:
            # 分治查找第k+1大，通过排除已选的最大值来找到下一个最大值
            # 将原区间分为两部分：[l+L-1, pos-1] 和 [pos+1, r]
            if pos > l + L - 2:
                # 在左半部分[l+L-1, pos-1]中查找最大值
                new_pos = st.query(l + L - 2, pos - 1, prefix_sum)
                new_value = prefix_sum[new_pos + 1] - prefix_sum[l - 1]
                heapq.heappush(pq, (-new_value, l, r, new_pos, kth + 1))
            if pos < r:
                # 在右半部分[pos+1, r]中查找最大值
                new_pos = st.query(pos + 1, r - 1, prefix_sum)
                new_value = prefix_sum[new_pos + 1] - prefix_sum[l - 1]
                heapq.heappush(pq, (-new_value, l, r, new_pos, kth + 1))
    
    print(ans)

if __name__ == "__main__":
    main()

'''
算法分析:
时间复杂度: O((n + k) * log n)
  - 初始化ST表: O(n * log n)
  - 初始化优先队列: O(n * log n)
  - k次取最大值操作: O(k * log n)
空间复杂度: O(n * log n)
  - ST表: O(n * log n)
  - 优先队列: O(n)

算法思路:
1. 使用前缀和将区间和转换为两个前缀和的差
   区间[i,j]的和 = prefix_sum[j] - prefix_sum[i-1]
2. 对于每个固定的左端点，确定右端点的有效范围
   右端点范围：[i+L-1, min(n, i+R-1)]
3. 使用ST表快速查询区间内前缀和的最大值位置
   ST表可以在O(1)时间内查询任意区间内的最值位置
4. 使用优先队列维护当前所有可能区间中的最大值
   初始时，对于每个左端点，将其有效范围内的最大值加入优先队列
5. 每次取出最大值后，通过分治思想生成下一个候选值
   当取出一个最大值后，将原区间分为两部分，在这两部分中分别查找最大值作为候选

关键点:
1. ST表的构建和查询
   ST表是解决RMQ问题的经典数据结构，预处理O(nlogn)，查询O(1)
2. 优先队列的使用（Python中使用最小堆，通过负值实现最大堆）
   Python的heapq模块实现的是最小堆，通过存储负值来模拟最大堆的行为
3. 分治思想查找第k大值
   通过不断将区间分割，避免一次性计算所有可能值
4. 前缀和优化区间和计算
   将区间和计算从O(n)优化到O(1)

工程化考量:
1. 边界条件处理
   需要特别注意数组下标和区间边界，防止越界访问
2. 数据类型选择
   使用Python的int类型（任意精度整数），无需担心整数溢出
3. 空间优化
   复用ST表和优先队列，避免重复分配内存
4. 时间复杂度优化
   通过ST表将查询时间从O(n)降低到O(1)
'''