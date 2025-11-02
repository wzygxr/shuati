# Static Range Minimum Queries - CSES 1647
# 
# 【题目大意】
# 给定一个长度为n的整数数组，需要处理q个查询，每个查询给定一个范围[a,b]，
# 要求回答该范围内元素的最小值。
# 
# 【解题思路】
# 这是经典的RMQ（Range Minimum Query）问题，可以使用Sparse Table来解决。
# Sparse Table通过预处理所有长度为2的幂次的区间最小值，
# 实现O(n log n)预处理，O(1)查询的时间复杂度。
# 
# 【时间复杂度分析】
# - 预处理Sparse Table: O(n log n)
# - 单次查询: O(1)
# - 总时间复杂度: O(n log n + q)
# 
# 【空间复杂度分析】
# - Sparse Table数组: O(n log n)
# - 其他辅助数组: O(n)
# - 总空间复杂度: O(n log n)
# 
# 【算法核心思想】
# 1. 预处理阶段：使用动态规划构建Sparse Table
# 2. 查询阶段：将任意区间分解为两个重叠的预处理区间，取最小值
# 
# 【应用场景】
# 1. 大数据分析中的快速区间统计
# 2. 信号处理中的特征提取
# 3. 游戏开发中的范围检测
# 4. 网络流量监控中的异常检测

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
    q = int(input_data[ptr])
    ptr += 1
    
    # 读取数组数据（0-based）
    data = list(map(int, input_data[ptr:ptr + n]))
    ptr += n
    
    # 构建Sparse Table
    st = SparseTableRMQ(data)
    
    # 处理每个查询
    results = []
    for _ in range(q):
        a = int(input_data[ptr])
        ptr += 1
        b = int(input_data[ptr])
        ptr += 1
        # 注意：题目输入是1-based，需要转换为0-based
        results.append(str(st.query_min(a - 1, b - 1)))
    
    # 一次性输出所有结果
    print('\n'.join(results))


# 测试用例
def test():
    """
    测试函数
    """
    # 测试用例1
    # 输入: n=8, q=4
    # arr=[3,2,4,5,1,1,5,3]
    # 查询: [2,4] -> 2, [5,6] -> 1, [1,8] -> 1, [3,3] -> 4
    # 输出: 2, 1, 1, 4
    pass


if __name__ == "__main__":
    main()