# Maximum of Maximums of Minimums - Codeforces 870B
# 
# 【题目大意】
# 给定一个长度为n的数组和一个整数k，要求将数组分成恰好k个连续的非空子数组，
# 每个子数组的值为该子数组中所有元素的最小值，
# 求所有子数组的值的最大值的最大可能值。
# 
# 【解题思路】
# 这是一个贪心问题，可以通过分析不同k值的情况来解决：
# 1. 当k=1时，只能分成一段，答案就是整个数组的最小值
# 2. 当k>=3时，可以将最大值单独分为一段，其余元素分为另外两段，答案就是数组的最大值
# 3. 当k=2时，需要枚举所有可能的分割点，找到使两段最小值的最大值最大的分割方案
# 
# 但也可以使用Sparse Table来预处理区间最小值，然后通过枚举分割点来求解。
# 
# 【时间复杂度分析】
# - 预处理Sparse Table: O(n log n)
# - 枚举分割点查询: O(n)
# - 总时间复杂度: O(n log n)
# 
# 【空间复杂度分析】
# - Sparse Table数组: O(n log n)
# - 其他辅助数组: O(n)
# - 总空间复杂度: O(n log n)
# 
# 【算法核心思想】
# 使用Sparse Table预处理区间最小值，然后通过贪心策略或枚举分割点来找到最优解。
# 
# 【应用场景】
# 1. 数组分割优化问题
# 2. 区间最值查询问题
# 3. 贪心算法与数据结构结合的问题

import sys

class SparseTableRMQ:
    """
    Sparse Table数据结构实现 - 区间最小值查询
    
    该类实现了基于Sparse Table算法的高效区间最小值查询
    适用于静态数组的频繁区间查询场景，预处理时间O(n log n)，查询时间O(1)
    """
    
    def __init__(self, data):
        """
        初始化Sparse Table
        
        Args:
            data: 输入数组（0-based索引）
        """
        self.n = len(data)
        self.data = data.copy()  # 复制数据，避免外部修改影响内部状态
        
        # 预处理log_table数组，用于快速查询任意长度对应的最大2的幂次
        self.log_table = [0] * (self.n + 1)
        for i in range(2, self.n + 1):
            # 使用整数除法高效计算log2值
            self.log_table[i] = self.log_table[i // 2] + 1
        
        # 计算需要的最大k值
        self.k_max = self.log_table[self.n] + 1
        
        # 初始化最小值ST表
        self.st_min = [[0] * self.k_max for _ in range(self.n)]  # st_min[i][k]表示从i开始，长度为2^k的区间的最小值
        
        # 构建ST表
        self._build()
    
    def _build(self):
        """
        构建Sparse Table
        使用动态规划方法，自底向上构建所有可能长度的区间信息
        
        实现原理：
        1. 初始化：长度为1的区间（k=0）的最值即为元素自身
        2. 动态规划：对于每个可能的区间长度的幂次k，构建所有可能的起始位置i的区间最值
        3. 状态转移：当前区间的最值由两个等长子区间的最值合并而来
        
        时间复杂度：O(n log n)
        """
        # 初始化长度为1的区间（k=0）
        for i in range(self.n):
            self.st_min[i][0] = self.data[i]
        
        # 动态规划构建更长区间的信息
        # 枚举区间长度的幂次k
        for k in range(1, self.k_max):
            # 枚举区间起始位置i，确保区间[i, i+2^k-1]不越界
            for i in range(self.n - (1 << k) + 1):
                # 计算子区间的起始位置
                mid = i + (1 << (k-1))
                # 状态转移：当前区间的最值由两个子区间的最值合并而来
                # 子区间1: [i, i+2^(k-1)-1]
                # 子区间2: [i+2^(k-1), i+2^k-1]
                self.st_min[i][k] = min(self.st_min[i][k-1], self.st_min[mid][k-1])
    
    def query_min(self, l, r):
        """
        查询区间[l, r]的最小值（0-based索引，包含端点）
        
        Args:
            l: 区间左边界（必须满足0 <= l <= r < n）
            r: 区间右边界（必须满足0 <= l <= r < n）
            
        Returns:
            区间最小值
            
        Raises:
            ValueError: 如果输入的区间边界无效
            
        Time complexity: O(1)
        """
        # 输入验证
        if not (0 <= l <= r < self.n):
            raise ValueError(f"Invalid range: [{l}, {r}] for array of length {self.n}")
        
        length = r - l + 1
        k = self.log_table[length]
        return min(self.st_min[l][k], self.st_min[r - (1 << k) + 1][k])


def main():
    """
    主函数 - 处理输入输出并解决问题
    """
    # 读取输入（一次性读取所有数据，提高效率）
    input_data = sys.stdin.read().split()
    ptr = 0
    
    n = int(input_data[ptr])
    ptr += 1
    k = int(input_data[ptr])
    ptr += 1
    
    # 读取数组数据（0-based）
    data = list(map(int, input_data[ptr:ptr + n]))
    
    # 特殊情况处理
    if k == 1:
        # 只能分成一段，答案是整个数组的最小值
        result = min(data)
        print(result)
    elif k >= 3:
        # 可以将最大元素单独分为一段，答案是数组的最大值
        result = max(data)
        print(result)
    else:
        # k == 2，需要找到最优的分割点
        # 构建Sparse Table
        st = SparseTableRMQ(data)
        
        result = float('-inf')
        
        # 枚举分割点，第一段为[0, i]，第二段为[i+1, n-1]
        for i in range(n - 1):
            min1 = st.query_min(0, i)      # 第一段的最小值
            min2 = st.query_min(i + 1, n - 1)  # 第二段的最小值
            max_min = max(min1, min2)      # 两段最小值的最大值
            result = max(result, max_min)
        
        print(int(result))


# 测试用例
def test():
    """
    测试函数
    """
    # 测试用例1
    # 输入: n=5, k=1, arr=[1,2,3,4,5]
    # 输出: 1 (整个数组的最小值)
    
    # 测试用例2
    # 输入: n=5, k=2, arr=[1,2,3,4,5]
    # 输出: 5 (分割点在4后面，第一段[1,2,3,4]最小值为1，第二段[5]最小值为5，max(1,5)=5)
    
    # 测试用例3
    # 输入: n=5, k=3, arr=[1,2,3,4,5]
    # 输出: 5 (可以将最大值5单独分为一段)
    pass


if __name__ == "__main__":
    main()