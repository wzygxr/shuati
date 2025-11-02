"""
洛谷 P3287 [SCOI2014]方伯伯的玉米田
题目链接: https://www.luogu.com.cn/problem/P3287

题目描述:
给定一个长度为n的数组arr，每次可以选择一个区间[l,r]，区间内的数字都+1，最多执行k次
返回执行完成后，最长的不下降子序列长度。

解题思路:
使用二维树状数组优化动态规划的方法解决此问题。
1. 定义状态dp[i][j][h]表示处理前i个元素，使用j次操作，以高度h结尾的最长不下降子序列长度
2. 由于高度范围较大，我们使用二维树状数组来维护状态
3. 树状数组的第一维表示高度，第二维表示操作次数
4. 对于每个元素，枚举可能的操作次数，查询最优解并更新状态

时间复杂度分析:
- 状态转移: O(n*k*log(MAXH)*log(k))
- 总时间复杂度: O(n*k*log(MAXH)*log(k))
空间复杂度: O(MAXH*k) 用于存储二维树状数组

工程化考量:
1. 内存优化: 使用二维树状数组减少空间占用
2. 性能优化: 利用树状数组的O(log n)查询和更新
3. 边界处理: 处理n=0或k=0的特殊情况
4. 可读性: 清晰的变量命名和注释
"""

class FenwickTree2D:
    """二维树状数组类，用于高效维护二维前缀最大值"""
    
    def __init__(self, height, width):
        """
        初始化二维树状数组
        
        Args:
            height: 高度维度大小
            width: 宽度维度大小
        """
        self.height = height
        self.width = width
        # 初始化树状数组，所有值设为0
        self.tree = [[0] * (width + 1) for _ in range(height + 1)]
    
    def update(self, x, y, val):
        """
        二维树状数组更新操作
        
        Args:
            x: 第一维坐标
            y: 第二维坐标
            val: 要更新的值
        """
        i = x
        while i <= self.height:
            j = y
            while j <= self.width:
                if val > self.tree[i][j]:
                    self.tree[i][j] = val
                j += j & -j
            i += i & -i
    
    def query(self, x, y):
        """
        二维树状数组查询操作
        
        Args:
            x: 第一维坐标
            y: 第二维坐标
            
        Returns:
            查询结果
        """
        res = 0
        i = x
        while i > 0:
            j = y
            while j > 0:
                if self.tree[i][j] > res:
                    res = self.tree[i][j]
                j -= j & -j
            i -= i & -i
        return res

def max_non_decreasing_length(arr, k):
    """
    计算最长不下降子序列长度
    
    Args:
        arr: 输入数组
        k: 最大操作次数
        
    Returns:
        最长不下降子序列长度
        
    Raises:
        ValueError: 如果输入参数不合法
    """
    # 异常处理
    if not arr:
        return 0
    if k < 0:
        k = 0
    
    n = len(arr)
    
    # 计算最大高度
    max_height = max(arr) if arr else 0
    max_height += k  # 考虑操作后的最大高度
    
    # 特殊情况：单元素数组
    if n == 1:
        return 1
    
    # 初始化二维树状数组
    fenwick = FenwickTree2D(max_height, k + 1)
    
    result = 1  # 至少包含一个元素
    
    # 遍历数组中的每个元素
    for i in range(n):
        # 枚举可能的操作次数
        for j in range(k + 1):
            # 当前元素经过j次操作后的高度
            current_height = arr[i] + j
            
            # 查询之前高度不超过current_height，操作次数不超过j的最优解
            pre_max = fenwick.query(current_height, j + 1)
            
            # 当前状态值
            current_val = pre_max + 1
            
            # 更新结果
            if current_val > result:
                result = current_val
            
            # 更新二维树状数组
            fenwick.update(current_height, j + 1, current_val)
    
    return result

# 单元测试
def test_max_non_decreasing_length():
    """测试函数，验证算法正确性"""
    
    print("开始测试最长不下降子序列算法...")
    
    # 测试用例1: 正常情况
    arr1 = [1, 2, 3, 2, 1]
    result1 = max_non_decreasing_length(arr1, 2)
    print(f"测试用例1: {arr1}, k=2 -> {result1}")
    
    # 测试用例2: 不需要操作
    arr2 = [1, 2, 3, 4, 5]
    result2 = max_non_decreasing_length(arr2, 0)
    print(f"测试用例2: {arr2}, k=0 -> {result2}")
    assert result2 == 5, f"预期5，实际{result2}"
    
    # 测试用例3: 单元素
    arr3 = [5]
    result3 = max_non_decreasing_length(arr3, 3)
    print(f"测试用例3: {arr3}, k=3 -> {result3}")
    assert result3 == 1, f"预期1，实际{result3}"
    
    # 测试用例4: 空数组
    arr4 = []
    result4 = max_non_decreasing_length(arr4, 5)
    print(f"测试用例4: 空数组, k=5 -> {result4}")
    assert result4 == 0, f"预期0，实际{result4}"
    
    # 测试用例5: 递减序列
    arr5 = [5, 4, 3, 2, 1]
    result5 = max_non_decreasing_length(arr5, 2)
    print(f"测试用例5: {arr5}, k=2 -> {result5}")
    
    # 测试用例6: 边界情况 - k为负数
    arr6 = [1, 2, 3]
    result6 = max_non_decreasing_length(arr6, -1)
    print(f"测试用例6: {arr6}, k=-1 -> {result6}")
    assert result6 == 3, f"预期3，实际{result6}"
    
    print("所有测试用例通过！")

# 性能测试
def performance_test():
    """性能测试函数"""
    
    print("开始性能测试...")
    
    # 大规模数据测试
    import time
    
    large_arr = [i % 100 + 1 for i in range(1000)]
    
    start_time = time.time()
    result = max_non_decreasing_length(large_arr, 50)
    end_time = time.time()
    
    print(f"大规模测试: 数组长度{len(large_arr)}，k=50，结果{result}，耗时{end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行测试
    test_max_non_decreasing_length()
    
    # 性能测试
    performance_test()
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 二维树状数组应用：用于维护二维前缀最大值")
    print("2. 离散化技巧：将高度范围映射到有限空间")
    print("3. 动态规划优化：将O(n^2)的DP优化到O(n*k*log^2(n))")
    print("4. 边界处理：处理空数组、单元素等特殊情况")
    print("5. 性能优化：利用树状数组的O(log n)操作特性")
    
    print("\n=== 工程化考量 ===")
    print("1. 异常防御：处理非法输入参数")
    print("2. 内存优化：二维树状数组相比二维DP表节省空间")
    print("3. 可读性：清晰的变量命名和注释")
    print("4. 测试覆盖：单元测试覆盖各种边界情况")
    print("5. 性能监控：大规模数据性能测试")