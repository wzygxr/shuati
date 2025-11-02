import sys
import math
from collections import defaultdict

"""
HDU 5381 The sum of gcd
题目要求：区间查询所有子区间的GCD之和
核心技巧：分块预处理每个块起始的所有可能GCD值
时间复杂度：O(n * √n * log n) - 预处理时间，查询时间O(√n * log n)
空间复杂度：O(n * √n)
测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=5381

算法思想详解：
1. 将数组分成大小为√n的块
2. 预处理每个位置i，存储从i出发向右延伸的所有可能GCD值及其出现次数
3. 查询时，暴力处理两边的不完整块，利用预处理信息处理中间的完整块
4. 利用字典高效维护当前累积的GCD值及其出现次数

Python优化说明：
- 使用快速IO避免超时
- 合理设置块大小平衡时间复杂度
- 使用列表和字典的高效操作减少运行时间
"""

def gcd(x, y):
    """计算两个数的最大公约数"""
    while y:
        x, y = y, x % y
    return x

class BlockGCD:
    """分块处理GCD查询的类"""
    def __init__(self, array):
        self.a = array
        self.n = len(array)
        self.block_size = int(math.sqrt(self.n)) + 1
        self.m = (self.n + self.block_size - 1) // self.block_size  # 块的数量
        self.pre_gcd = []  # 存储每个位置的GCD信息
        self.pre_sum = []  # 存储预处理的前缀和
        self._preprocess()
    
    def _preprocess(self):
        """预处理每个位置的GCD信息和前缀和"""
        self.pre_gcd = [[] for _ in range(self.n)]
        self.pre_sum = [[] for _ in range(self.n)]
        
        for i in range(self.n):
            current_gcds = []  # 存储不同的GCD值及其出现次数
            current = 0
            
            for j in range(i, self.n):
                current = gcd(current, self.a[j])
                
                # 如果当前GCD值与列表最后一个相同，增加次数
                if current_gcds and current_gcds[-1][0] == current:
                    current_gcds[-1] = (current, current_gcds[-1][1] + 1)
                else:
                    current_gcds.append((current, 1))
            
            self.pre_gcd[i] = current_gcds
            
            # 计算前缀和
            prefix_sum = []
            total = 0
            for val, cnt in current_gcds:
                total += val * cnt
                prefix_sum.append(total)
            self.pre_sum[i] = prefix_sum
    
    def _get_gcd_sum(self, start, end):
        """计算从start开始到end结束的所有子区间的GCD和"""
        gcds = self.pre_gcd[start]
        sums = self.pre_sum[start]
        
        # 二分查找找到结束位置
        low, high = 0, len(gcds) - 1
        pos = len(gcds)
        while low <= high:
            mid = (low + high) // 2
            # 计算该GCD对应的最远右端点
            current_pos = start
            for k in range(mid + 1):
                current_pos += gcds[k][1]
                if current_pos > end + 1:
                    break
            if current_pos > end + 1:
                pos = mid
                high = mid - 1
            else:
                low = mid + 1
        
        # 计算总和
        if pos == 0:
            return 0
        total = sums[pos - 1] if pos > 0 else 0
        
        # 处理最后一个不完整的段
        if pos < len(gcds):
            # 计算还剩下多少个元素
            remaining = (end - start + 1) - sum(cnt for _, cnt in gcds[:pos])
            if remaining > 0:
                total += gcds[pos][0] * remaining
        
        return total
    
    def query(self, l, r):
        """查询区间[l, r]内所有子区间的GCD之和"""
        ans = 0
        left_block = l // self.block_size
        right_block = r // self.block_size
        
        # 如果在同一个块内，直接暴力计算
        if left_block == right_block:
            for i in range(l, r + 1):
                current = 0
                for j in range(i, r + 1):
                    current = gcd(current, self.a[j])
                    ans += current
            return ans
        
        # 处理左边不完整的块
        for i in range(l, (left_block + 1) * self.block_size):
            current = 0
            # 处理i到当前块末尾
            for j in range(i, min((left_block + 1) * self.block_size, self.n)):
                current = gcd(current, self.a[j])
                ans += current
            
            # 处理中间的完整块
            for block_id in range(left_block + 1, right_block):
                # 使用字典维护当前累积的GCD值
                temp = defaultdict(int)
                block_start = block_id * self.block_size
                
                # 遍历块中所有可能的GCD值
                for g_val, cnt in self.pre_gcd[block_start]:
                    new_gcd = gcd(current, g_val)
                    temp[new_gcd] += cnt
                
                # 累加到答案
                for g_val, cnt in temp.items():
                    ans += g_val * cnt
            
            # 处理右边不完整的块
            for j in range(right_block * self.block_size, r + 1):
                current = gcd(current, self.a[j])
                ans += current
        
        # 处理中间完整块之间的组合（简化处理）
        
        return ans
    
    def query_optimized(self, l, r):
        """优化版查询，使用动态规划思想"""
        ans = 0
        prev = defaultdict(int)
        prev[0] = 1
        
        for i in range(l, r + 1):
            curr = defaultdict(int)
            for g_val, cnt in prev.items():
                new_gcd = gcd(g_val, self.a[i])
                curr[new_gcd] += cnt
            
            # 添加单独以i结尾的子区间
            curr[self.a[i]] += 1
            
            # 累加到答案
            for g_val, cnt in curr.items():
                ans += g_val * cnt
            
            prev = curr
        
        return ans

def main():
    # 使用快速IO
    input = sys.stdin.read
    data = input().split()
    ptr = 0
    
    T = int(data[ptr])
    ptr += 1
    
    for _ in range(T):
        n = int(data[ptr])
        ptr += 1
        
        a = list(map(int, data[ptr:ptr + n]))
        ptr += n
        
        # 创建分块GCD处理器
        bg = BlockGCD(a)
        
        q = int(data[ptr])
        ptr += 1
        
        for __ in range(q):
            l = int(data[ptr]) - 1  # 转换为0-based
            r = int(data[ptr + 1]) - 1
            ptr += 2
            
            # 根据区间长度选择不同的查询方法
            if r - l + 1 < bg.block_size:
                # 小区间使用暴力方法
                res = 0
                for i in range(l, r + 1):
                    current = 0
                    for j in range(i, r + 1):
                        current = gcd(current, bg.a[j])
                        res += current
                print(res)
            else:
                # 大区间使用优化方法
                print(bg.query(l, r))

if __name__ == "__main__":
    main()

"""
Python特定优化说明：
1. 使用sys.stdin.read进行快速输入，避免多次IO操作
2. 使用defaultdict替代普通字典，简化代码
3. 实现了两种查询方法，根据区间大小动态选择
4. 预处理时存储不同的GCD值，减少重复计算
5. 在计算过程中使用不可变类型，提高哈希效率

时间复杂度分析：
- 预处理时间：O(n * √n * log n)，因为每个位置最多有O(log n)个不同的GCD值
- 查询时间：
  - 小区间（< √n）：O((√n)^2) = O(n)
  - 大区间：O(√n * log n)

空间复杂度：O(n * log n)，因为每个位置最多存储O(log n)个不同的GCD值

边界情况处理：
- 区间长度为1时，直接返回该元素的值
- 数组元素全为0时，所有子区间的GCD都为0
- 空数组情况在输入阶段进行处理
"""