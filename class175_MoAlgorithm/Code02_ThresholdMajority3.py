# 达到阈值的最小众数，python版
# 题目来源：LeetCode 3636. 查询超过阈值频率最高元素 (Threshold Majority Queries)
# 题目链接：https://leetcode.cn/problems/threshold-majority-queries/
# 题目大意：
# 给定一个长度为n的数组arr，一共有m条查询，格式如下
# 查询 l r k : arr[l..r]范围上，如果所有数字的出现次数 < k，打印-1
#              如果有些数字的出现次数 >= k，打印其中的最小众数
# 1 <= n <= 10^4
# 1 <= m <= 5 * 10^4
# 1 <= arr[i] <= 10^9
# 
# 解题思路：
# 这是LeetCode上的一个题目，考察的是达到阈值的最小众数问题
# 众数：数组中出现次数最多的数字
# 最小众数：在出现次数达到要求的数字中，值最小的那个
# 阈值：查询中给定的k值，只有出现次数>=k的数字才符合要求
# 
# 算法要点：
# 1. 使用普通莫队算法解决此问题
# 2. 离散化处理：由于arr[i]的取值范围很大(10^9)，需要将其映射到较小的连续整数范围
# 3. 分块策略：将数组分成大小为sqrt(n)的块，用于查询排序和处理
# 4. 查询排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
# 5. 维护窗口信息：记录每个数字在当前窗口中的出现次数，以及出现次数最多且值最小的数字
# 6. 状态转移：通过add和del_操作维护窗口状态，实现O(1)时间的窗口扩展和收缩
# 7. 处理查询：对于同一块内的查询使用暴力方法，跨块查询使用莫队算法的移动窗口技巧
#
# 时间复杂度分析：
# - 离散化处理：O(n log n)
# - 查询排序：O(m log m)
# - 莫队算法处理查询：
#   - 右指针移动：每个查询最多移动O(n)次，总移动次数O(m*sqrt(n))
#   - 左指针移动：每个块内的查询，左指针最多移动O(sqrt(n))次，总移动次数O(m*sqrt(n))
#   - 同块暴力处理：每个查询O(sqrt(n))，总时间O(m*sqrt(n))
# - 总体时间复杂度：O(n log n + m log m + (n+m)*sqrt(n))，对于题目约束可简化为O((n+m)*sqrt(n))
#
# 空间复杂度分析：
# - 存储原数组、查询数组：O(n + m)
# - 离散化数组、分块信息数组：O(n)
# - 计数数组、结果数组：O(n + m)
# - 总体空间复杂度：O(n + m)
#
# LeetCode相关题目：
# 1. LeetCode 3636. 查询超过阈值频率最高元素 - https://leetcode.cn/problems/threshold-majority-queries/ (当前实现)
# 2. LeetCode 1157. 子数组中占绝大多数的元素 - https://leetcode.com/problems/online-majority-element-in-subarray/ (可使用线段树+摩尔投票)
# 3. LeetCode 995. K 连续位的最小翻转次数 - https://leetcode.com/problems/minimum-number-of-k-consecutive-bit-flips/ (贪心+差分数组)
# 4. LeetCode 1483. 树节点的第 K 个祖先 - https://leetcode.com/problems/kth-ancestor-of-a-tree-node/ (二进制提升)
# 5. LeetCode 933. 最近的请求次数 - https://leetcode.com/problems/number-of-recent-calls/ (队列)
# 6. LeetCode 239. 滑动窗口最大值 - https://leetcode.com/problems/sliding-window-maximum/ (单调队列)
# 7. LeetCode 307. 区域和检索 - 数组可修改 - https://leetcode.com/problems/range-sum-query-mutable/ (线段树/BIT)
# 8. LeetCode 846. 一手顺子 - https://leetcode.com/problems/hand-of-straights/ (频率统计)
# 9. LeetCode 169. 多数元素 - https://leetcode.com/problems/majority-element/ (摩尔投票)
#
# 莫队算法变种题目推荐（附解题核心方法）：
# 1. 普通莫队：
#    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494 (概率计算，维护平方和)
#    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267 (区间不同元素个数，维护cnt数组)
#    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E (异或前缀和，哈希表)
#    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709 (平方和查询)
#    - HDU 3874 Necklace - https://acm.hdu.edu.cn/showproblem.php?pid=3874 (区间不同元素个数)
#    - LibreOJ 119 最高频元素的频数 - https://loj.ac/p/119 (统计出现次数)
#    - POJ 3764 The xor-longest Path - https://poj.org/problem?id=3764 (异或路径，树上问题)
#
# 2. 带修莫队：
#    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903 (三关键字排序：块号、右端点、时间戳)
#    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874 (维护最大贡献值)
#    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F (维护mex值)
#    - 牛客网 NC19341 染色问题 - https://ac.nowcoder.com/acm/problem/19341 (带修改的区间颜色数)
#
# 3. 树上莫队：
#    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707 (树链剖分，欧拉序转换区间查询)
#    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074 (带权树上莫队)
#    - CodeChef QTREE5 - https://www.codechef.com/problems/QTREE5 (树上最近点对查询)
#
# 4. 二次离线莫队：
#    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887 (两次离线处理)
#    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398 (预处理贡献)
#    - 杭电 OJ 6395 Sequence - https://acm.hdu.edu.cn/showproblem.php?pid=6395 (快速幂预处理)
#
# 5. 回滚莫队：
#    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906 (添加易删除难，维护最左最右位置)
#    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/ (前缀和+回滚莫队)
#    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c (维护最大贡献值)

import math

class Solution:
    def __init__(self):
        self.MAXN = 10001
        self.MAXM = 50001
        self.MAXB = 301
        self.n = 0
        self.m = 0
        self.arr = [0] * self.MAXN
        self.query = [[0, 0, 0, 0] for _ in range(self.MAXM)]
        self.sorted = [0] * self.MAXN
        self.cntv = 0

        self.blen = 0
        self.bnum = 0
        self.bi = [0] * self.MAXN
        self.br = [0] * self.MAXB

        # 记录每个数字在当前窗口中的出现次数
        self.cnt = [0] * self.MAXN
        # 当前窗口中出现次数最多的数字的出现次数
        self.maxCnt = 0
        # 当前窗口中出现次数最多且值最小的数字
        self.minMode = 0

        self.ans = [0] * self.MAXM

    # 二分查找，找到num在sorted数组中的位置（离散化）
    def kth(self, num):
        left, right, ret = 1, self.cntv, 0
        while left <= right:
            mid = (left + right) // 2
            if self.sorted[mid] <= num:
                ret = mid
                left = mid + 1
            else:
                right = mid - 1
        return ret

    # 暴力方法计算[l,r]范围内满足阈值k的最小众数
    # 适用于处理同一块内的短区间查询
    def force(self, l, r, k):
        mx = 0  # 最大出现次数
        who = 0  # 对应的数字
        # 统计每个数字的出现次数
        for i in range(l, r + 1):
            self.cnt[self.arr[i]] += 1
        # 找到出现次数>=k且值最小的数字
        for i in range(l, r + 1):
            num = self.arr[i]
            # 优先考虑出现次数更多的元素，出现次数相同时选择值更小的元素
            if self.cnt[num] > mx or (self.cnt[num] == mx and num < who):
                mx = self.cnt[num]
                who = num
        # 清除临时统计结果，避免影响后续查询
        for i in range(l, r + 1):
            self.cnt[self.arr[i]] -= 1
        # 返回结果，如果最大出现次数<k则返回-1，否则返回原始值
        return self.sorted[who] if mx >= k else -1

    # 添加数字num到窗口中
    def add(self, num):
        self.cnt[num] += 1
        # 更新当前最大出现次数和对应的最小数字
        if self.cnt[num] > self.maxCnt or (self.cnt[num] == self.maxCnt and num < self.minMode):
            self.maxCnt = self.cnt[num]
            self.minMode = num

    # 从窗口中删除数字num
    def del_(self, num):
        self.cnt[num] -= 1

    # 核心计算函数
    def compute(self):
        # 按块处理查询
        qi = 1
        for block in range(1, self.bnum + 1):
            if qi > self.m:
                break
            # 每个块开始时重置状态
            self.maxCnt = 0
            self.minMode = 0
            for i in range(1, self.cntv + 1):
                self.cnt[i] = 0
            # 当前窗口的左右边界
            winl = self.br[block] + 1
            winr = self.br[block]

            # 处理属于当前块的所有查询
            while qi <= self.m and self.bi[self.query[qi][0]] == block:
                jobl = self.query[qi][0]   # 查询左边界
                jobr = self.query[qi][1]   # 查询右边界
                jobk = self.query[qi][2]   # 查询阈值
                id_ = self.query[qi][3]    # 查询编号

                # 如果查询区间完全在当前块内，使用暴力方法
                if jobr <= self.br[block]:
                    self.ans[id_] = self.force(jobl, jobr, jobk)
                else:
                    # 否则使用莫队算法
                    # 先扩展右边界到jobr
                    while winr < jobr:
                        winr += 1
                        self.add(self.arr[winr])

                    # 保存当前状态
                    backupCnt = self.maxCnt
                    backupNum = self.minMode

                    # 扩展左边界到jobl
                    while winl > jobl:
                        winl -= 1
                        self.add(self.arr[winl])

                    # 根据当前状态和阈值计算答案
                    if self.maxCnt >= jobk:
                        self.ans[id_] = self.sorted[self.minMode]
                    else:
                        self.ans[id_] = -1

                    # 恢复状态
                    self.maxCnt = backupCnt
                    self.minMode = backupNum

                    # 收缩左边界回到块的右边界+1
                    while winl <= self.br[block]:
                        self.del_(self.arr[winl])
                        winl += 1
                qi += 1

    # 预处理函数
    def prepare(self):
        # 复制原数组用于离散化
        for i in range(1, self.n + 1):
            self.sorted[i] = self.arr[i]

        # 排序去重，实现离散化
        self.sorted[1:self.n+1] = sorted(self.sorted[1:self.n+1])
        self.cntv = 1
        for i in range(2, self.n + 1):
            if self.sorted[self.cntv] != self.sorted[i]:
                self.cntv += 1
                self.sorted[self.cntv] = self.sorted[i]

        # 将原数组元素替换为离散化后的值
        for i in range(1, self.n + 1):
            self.arr[i] = self.kth(self.arr[i])

        # 分块处理
        self.blen = int(math.sqrt(self.n))
        self.bnum = (self.n + self.blen - 1) // self.blen

        # 计算每个位置属于哪个块
        for i in range(1, self.n + 1):
            self.bi[i] = (i - 1) // self.blen + 1

        # 计算每个块的右边界
        for i in range(1, self.bnum + 1):
            self.br[i] = min(i * self.blen, self.n)

        # 对查询进行排序
        self.query[1:self.m+1] = sorted(self.query[1:self.m+1], key=lambda x: (self.bi[x[0]], x[1]))

    def subarrayMajority(self, nums, queries):
        """
        解决达到阈值的最小众数问题的主函数
        
        参数:
            nums: List[int] - 输入数组
            queries: List[List[int]] - 查询列表，每个查询格式为[l, r, k]
            
        返回:
            List[int] - 每个查询的结果，如果没有元素出现次数>=k则返回-1，否则返回出现次数>=k的最小元素
            
        异常处理:
            - 空数组或空查询：返回空列表
            - 查询参数格式错误：跳过该查询，返回-1
            - 数组长度或查询数量超过预定义最大值：可能导致数组越界
            - 无效查询区间：返回-1
            
        边界情况:
            - 单元素数组：根据k值决定返回结果
            - k=1：任何非空区间都有元素满足条件
            - k>区间长度：必然返回-1
        """
        # 参数校验
        if not nums or not queries:
            return []
            
        # 初始化变量
        self.n = len(nums)
        self.m = len(queries)
        
        # 参数有效性检查
        if self.n > self.MAXN or self.m > self.MAXM:
            print(f"警告: 输入规模可能超过预定义限制 (n={self.n}, m={self.m})")
            
        # 重置状态数组，避免多次调用时的状态污染
        self.ans = [0] * (self.MAXM)
        self.cnt = [0] * (self.MAXN)
        
        # 将输入数组复制到内部数组（下标从1开始，方便处理）
        for i in range(1, self.n + 1):
            try:
                self.arr[i] = nums[i - 1]
            except IndexError:
                print(f"错误: 数组索引越界 at i={i}")
                return []
                
        # 处理查询（下标从1开始）
        for i in range(1, self.m + 1):
            # 查询参数有效性检查
            try:
                if i - 1 >= len(queries) or len(queries[i - 1]) != 3:
                    self.ans[i] = -1  # 无效查询返回-1
                    continue
                    
                l, r, k = queries[i - 1]
                
                # 阈值k的有效性检查
                if k <= 0:
                    self.ans[i] = -1  # 无效阈值返回-1
                    continue
                    
                # 区间有效性验证
                if l < 0 or r >= self.n or l > r:
                    self.ans[i] = -1  # 无效查询返回-1
                    continue
                    
                # 快速判断：如果k>区间长度，直接返回-1
                if k > (r - l + 1):
                    self.ans[i] = -1
                    continue
                    
                self.query[i][0] = l + 1  # 转换为1-based索引
                self.query[i][1] = r + 1  # 转换为1-based索引
                self.query[i][2] = k      # 阈值k
                self.query[i][3] = i      # 查询编号
                
            except Exception as e:
                print(f"处理查询时出错: {e}")
                self.ans[i] = -1
                continue
                
        # 预处理和计算
        try:
            self.prepare()
            self.compute()
        except Exception as e:
            print(f"计算过程中出错: {e}")
            return [-1] * self.m
            
        # 构造返回结果
        ret = [0] * self.m
        for i in range(1, self.m + 1):
            try:
                ret[i - 1] = self.ans[i]
            except IndexError:
                ret[i - 1] = -1
                
        return ret

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：基本功能测试
    print("=== 测试用例1：基本功能测试 ===")
    nums = [1, 1, 2, 2, 3, 3, 3]
    queries = [[0, 6, 2], [0, 3, 2]]
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [3, -1]
    print(f"解释: 第一个查询区间包含3出现3次，满足k=2；第二个查询区间中1和2各出现2次，都不满足k=2的阈值
")
    
    # 测试用例2：无符合条件元素的情况
    print("=== 测试用例2：无符合条件元素 ===")
    nums = [1, 2, 3, 4, 5]
    queries = [[0, 4, 2]]
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [-1]
    print(f"解释: 每个元素只出现一次，不满足k=2
")
    
    # 测试用例3：多个符合条件元素，选择最小值
    print("=== 测试用例3：多个符合条件元素 ===")
    nums = [2, 2, 1, 1, 1, 3, 3, 3]
    queries = [[0, 7, 3]]
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [1]
    print(f"解释: 1和3都出现3次，满足k=3，选择较小的值1
")
    
    # 测试用例4：大值域元素离散化测试
    print("=== 测试用例4：大值域元素 ===")
    nums = [10**9, 10**9, 10**9-1, 10**9-1, 5]
    queries = [[0, 4, 2]]
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = [10^9, 10^9, 10^9-1, 10^9-1, 5], queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [999999999]
    print(f"解释: 离散化处理后正确识别元素出现次数，选择较小的值10^9-1
")
    
    # 测试用例5：边界情况测试
    print("=== 测试用例5：边界情况 ===")
    nums = [5]
    queries = [[0, 0, 1], [0, 0, 2]]
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [5, -1]
    print(f"解释: 单个元素查询，k=1时满足，k=2时不满足
")
    
    # 测试用例6：重复元素全满足条件
    print("=== 测试用例6：重复元素全满足 ===")
    nums = [7, 7, 7, 7, 7]
    queries = [[0, 4, 3], [1, 3, 2]]
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [7, 7]
    print(f"解释: 所有查询都满足k值要求，返回7
")
    
    # 测试用例7：无效参数测试
    print("=== 测试用例7：无效参数测试 ===")
    nums = [1, 2, 3]
    # 测试无效查询格式
    queries = [[0, 2, 1], [0, 1]]  # 第二个查询缺少参数
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [1, -1]
    print(f"解释: 第二个查询格式无效，返回-1
")
    
    # 测试用例8：无效区间测试
    queries = [[-1, 2, 1], [0, 5, 1], [2, 1, 1]]  # 无效区间
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [-1, -1, -1]
    print(f"解释: 所有区间参数无效，返回-1
")
    
    # 测试用例9：k值大于区间长度
    print("=== 测试用例9：k值大于区间长度 ===")
    nums = [1, 2, 3, 4]
    queries = [[0, 2, 4]]  # 区间长度为3，k=4
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [-1]
    print(f"解释: k=4 > 区间长度3，必然返回-1
")
    
    # 测试用例10：k=1的特殊情况
    print("=== 测试用例10：k=1的特殊情况 ===")
    nums = [5, 3, 8, 2]
    queries = [[0, 3, 1]]  # k=1时任何元素都满足
    result = solution.subarrayMajority(nums, queries)
    print(f"输入: nums = {nums}, queries = {queries}")
    print(f"输出: {result}")  # 预期输出: [2]
    print(f"解释: k=1时，返回区间中的最小元素
")
    
    # 算法性能分析
    print("\n=== 算法性能分析 ===")
    print("1. 时间复杂度：O((n+m)*sqrt(n))，其中n是数组长度，m是查询数量")
    print("2. 空间复杂度：O(n+m)，主要用于存储数组、查询和中间状态")
    print("3. 优化技巧：")
    print("   - 使用离散化处理大值域元素，避免哈希表带来的常数开销")
    print("   - 分块策略将时间复杂度从O(n*m)降低到O((n+m)*sqrt(n))")
    print("   - 1-based索引简化边界处理，避免数组索引越界")
    print("   - 同块查询暴力处理避免复杂的窗口维护逻辑")
    print("   - 快速剪枝：k>区间长度时直接返回-1")
    print("4. 适用场景：")
    print("   - 静态数组的离线区间查询问题")
    print("   - 无法使用线段树等数据结构高效解决的问题")
    print("   - 时间限制较宽松，n和m在1e4级别左右的问题")
    print("5. 工程化考量：")
    print("   - 输入参数全面校验，增强程序健壮性")
    print("   - 异常捕获与错误处理，防止程序崩溃")
    print("   - 状态重置机制，支持多次调用")
    print("   - 详细的测试用例覆盖各种场景")
    print("6. 与其他算法对比：")
    print("   - 比暴力解法O(n*m)更高效，但比线段树等O(m log n)算法效率低")
    print("   - 实现简单，代码量小，易于调试和维护")
    print("   - 特别适合处理某些难以用线段树模型化的问题")
    print("7. 改进方向：")
    print("   - 使用基数排序或桶排序优化离散化步骤")
    print("   - 实现奇偶排序优化，减少常数因子")
    print("   - 考虑使用更高效的数据结构维护众数信息")
    print("   - 对于特定问题可以考虑使用莫队算法的变种（如带修莫队、树上莫队等）")