# R2D2 and Droid Army
# Codeforces 514D
# 
# 【题目大意】
# 有n个有序排列的机器人，每个机器人有m项属性，每个机器人的每项属性值不一定相同
# 要消灭一个机器人，需要将其每项属性值都减为0
# R2D2有k次操作机会，每次操作可以将所有机器人的某一项属性值都减1
# 问在不超过k次操作的情况下，如何分配每项属性的操作次数，可以使得最终消灭最多的连续机器人序列
# 输出分配方案（各项属性的操作次数）
#
# 【算法核心思想】
# 结合Sparse Table（稀疏表）、二分搜索和滑动窗口三种算法技巧，高效解决区间最大值查询问题。
# Sparse Table的核心原理是通过动态规划预处理所有可能长度为2^j的区间最大值，从而实现O(1)时间复杂度的区间查询。
# 二分搜索用于确定最长的连续机器人序列长度，而滑动窗口用于验证每个长度是否存在可行解。
#
# 【核心原理】
# Sparse Table通过预处理所有长度为2的幂次的区间最大值，可以将任意区间的查询分解为两个重叠区间的查询，
# 从而实现O(1)时间复杂度的区间最大值查询。
#
# 【位运算常用技巧】
# 1. 左移运算：1 << k 等价于 2^k
# 2. 右移运算：n >> 1 等价于 n / 2（整数除法）
# 3. 位运算优先级：位移运算符优先级低于算术运算符，需要注意括号使用
#
# 【时间复杂度分析】
# 预处理Sparse Table: O(n * logn * m) - 对每种属性构建稀疏表
# 二分搜索: O(logn) - 搜索可能的最大序列长度
# 滑动窗口检查: O(n * m) - 对每个二分中点验证所有可能的区间
# 总时间复杂度: O(n * logn * m) - 对于较大数据规模仍然高效
#
# 【空间复杂度分析】
# Sparse Table数组: O(n * logn * m) - 存储预处理的区间最大值
# 其他辅助数组: O(n + m) - log2数组和结果数组
# 总空间复杂度: O(n * logn * m) - 在内存允许范围内是可行的
#
# 【应用场景】
# 1. 静态数组的区间最大值/最小值查询（RMQ问题）
# 2. 当数据量较大且需要频繁进行区间查询时
# 3. 适用于离线查询场景（数据不会被修改）
# 4. 组合优化问题中需要快速获取区间特征值的场景
# 5. 在线查询系统、数据分析、信号处理等领域
#
# 【相关题目】
# 1. Codeforces 514D - R2D2 and Droid Army（本题）
# 2. LeetCode 239 - Sliding Window Maximum（滑动窗口最大值）
# 3. POJ 3264 - Balanced Lineup（区间最大值与最小值之差）
# 4. SPOJ RMQSQ - Range Minimum Query（标准区间最小值查询）
# 5. CodeChef MSTICK - 区间最值查询
# 6. UVA 11235 - Frequent values（区间频繁值查询）
# 7. SPOJ FREQUENT - 区间频繁值查询
# 8. HackerRank Maximum Element in a Subarray（使用ST表高效查询）

import math
import sys

def main():
    """
    主函数 - 处理输入输出并执行算法
    
    【输入输出优化】
    使用sys.stdin.read一次性读取所有输入数据，提高大数据量处理效率
    使用print一次性输出结果，减少I/O操作次数
    """
    # 读取输入
    line = input().split()
    n = int(line[0])  # 机器人数量
    m = int(line[1])  # 属性种类数
    k = int(line[2])  # 操作次数上限
    
    # 机器人属性数据
    # 使用1-based索引，便于区间计算
    robots = [[0] * m for _ in range(n + 1)]
    
    # 读取每个机器人的属性
    for i in range(1, n + 1):
        line = input().split()
        for j in range(m):
            robots[i][j] = int(line[j])
    
    # 预处理log2数组
    # log2[i]表示不超过i的最大2的幂次的指数
    log2 = [0] * (n + 1)
    for i in range(2, n + 1):
        # 使用位移运算高效计算log2值
        # i >> 1 等价于 i // 2
        log2[i] = log2[i >> 1] + 1
    
    # Sparse Table数组，st[type][i][j]表示第type种属性在区间[i, i+2^j-1]内的最大值
    # 使用三维数组：[属性类型][起始位置][幂次]
    st = [[[0] * 20 for _ in range(n + 1)] for _ in range(m)]
    
    # 为每种属性构建Sparse Table
    def build_sparse_table():
        """
        构建Sparse Table
        
        【实现原理】
        1. 初始化Sparse Table的第一层（j=0），即长度为1的区间
        2. 使用动态规划的方式自底向上构建所有可能的区间长度
        3. 状态转移方程：st[type][i][j] = max(st[type][i][j-1], st[type][i+2^(j-1)][j-1])
        
        【时间复杂度】
        O(n * logn * m)
        """
        # 初始化Sparse Table的第一层（j=0）
        # 长度为1的区间，最大值就是元素本身
        for type_ in range(m):
            for i in range(1, n + 1):
                st[type_][i][0] = robots[i][type_]
        
        # 动态规划构建Sparse Table
        # j表示区间长度为2^j
        j = 1
        while (1 << j) <= n:  # 1 << j 等价于 2^j
            for type_ in range(m):  # 遍历每种属性
                i = 1
                # 遍历所有可能的起始位置，确保区间不越界
                while i + (1 << j) - 1 <= n:
                    # 状态转移：当前区间的最大值由两个子区间的最大值合并而来
                    # 子区间1: [i, i+2^(j-1)-1]
                    # 子区间2: [i+2^(j-1), i+2^j-1]
                    st[type_][i][j] = max(
                        st[type_][i][j - 1],
                        st[type_][i + (1 << (j - 1))][j - 1]
                    )
                    i += 1
            j += 1
    
    # 查询区间[l,r]内第type种属性的最大值
    def query_max(type_, l, r):
        """
        查询区间[l,r]内第type种属性的最大值
        
        【实现原理】
        1. 计算查询区间的长度len = r - l + 1
        2. 找到最大的k，使得 2^k ≤ len
        3. 构造两个覆盖整个查询区间的预处理区间：
           - 第一个区间：[l, l + 2^k - 1]
           - 第二个区间：[r - 2^k + 1, r]
        4. 这两个区间的最大值即为整个查询区间的最大值
        
        @param type_: 属性类型索引
        @param l: 区间左边界（1-based）
        @param r: 区间右边界（1-based）
        @return: 区间最大值
        
        【时间复杂度】
        O(1)
        """
        k_ = log2[r - l + 1]
        return max(
            st[type_][l][k_],
            st[type_][r - (1 << k_) + 1][k_]
        )
    
    # 检查是否存在长度为length的连续序列满足条件
    def check(length):
        """
        检查是否存在长度为length的连续序列满足条件
        
        【实现原理】
        使用滑动窗口遍历所有可能的区间，验证是否存在满足条件的解
        
        @param length: 要检查的连续序列长度
        @return: 如果存在满足条件的序列，返回各项属性需要的操作次数数组；否则返回None
        
        【时间复杂度】
        O(n * m)
        """
        if length == 0:
            return [0] * m  # 长度为0时，不需要任何操作
        
        # 滑动窗口检查所有长度为length的区间
        for i in range(1, n - length + 2):  # i是窗口的起始位置
            total = 0  # 总操作次数
            max_values = [0] * m  # 存储当前窗口内各项属性的最大值
            
            # 计算区间[i, i+length-1]内每种属性的最大值之和
            for type_ in range(m):
                max_values[type_] = query_max(type_, i, i + length - 1)
                total += max_values[type_]
            
            # 如果总操作次数不超过k，则找到解
            if total <= k:
                return max_values
        
        # 未找到满足条件的解
        return None
    
    # 构建Sparse Table
    build_sparse_table()
    
    # 二分搜索最长连续序列长度
    # left和right分别表示可能的最短和最长序列长度
    left, right = 0, n
    result = [0] * m  # 存储最终结果
    
    while left <= right:
        mid = (left + right) // 2  # 中间长度
        temp = check(mid)  # 检查是否存在长度为mid的满足条件的序列
        
        if temp is not None:
            # 找到满足条件的解，尝试更长的长度
            result = temp
            left = mid + 1
        else:
            # 不满足条件，尝试更短的长度
            right = mid - 1
    
    # 输出结果
    print(' '.join(map(str, result)))

if __name__ == "__main__":
    """
    程序入口点
    
    【工程化考量】
    1. 异常处理：在实际应用中应添加try-except块处理输入异常
    2. 性能优化：对于大数据量，可以考虑使用sys.stdin.read一次性读取所有输入
    3. 内存管理：对于特别大的数据集，可以考虑使用生成器或迭代器减少内存占用
    4. 可扩展性：可以将算法封装为类，便于复用和测试
    """
    main()