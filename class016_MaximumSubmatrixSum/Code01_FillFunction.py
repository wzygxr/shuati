# ==================================================================================
# 题目1：子矩阵的最大累加和（填函数风格 - Python版本）
# ==================================================================================
# 题目来源：牛客网 (NowCoder)
# 题目链接：https://www.nowcoder.com/practice/840eee05dccd4ffd8f9433ce8085946b
# 难度等级：中等
# 
# ==================================================================================
# Python实现特点
# ==================================================================================
# 1. 使用列表推导式简化代码
# 2. 使用float('-inf')代替Java的Integer.MIN_VALUE
# 3. 使用内置max()函数
# 4. Python没有类型声明，代码更简洁但需注意类型
# 
# ==================================================================================
# 算法核心：二维压缩 + Kadane算法
# ==================================================================================
#
# 时间复杂度：O(n² × m)
# 空间复杂度：O(m)
# 是否最优解：是
#
# ==================================================================================

class Solution:
    """子矩阵最大累加和求解器"""
    
    def sumOfSubMatrix(self, mat, n):
        """
        主函数：求子矩阵的最大累加和
        
        参数:
            mat: List[List[int]] - 输入矩阵
            n: int - 矩阵维度（方阵）
            
        返回:
            int - 最大子矩阵和
            
        时间复杂度: O(n³) 当n=m时
        空间复杂度: O(n)
        """
        return self.maxSumSubmatrix(mat, n, n)
    
    def maxSumSubmatrix(self, mat, n, m):
        """
        核心函数：求最大子矩阵和
        
        算法思路：
        1. 枚举所有可能的上边界(i)
        2. 对于每个上边界i，枚举下边界(j)，j >= i
        3. 将第i行到第j行的每一列相加，形成一个一维数组
        4. 对这个一维数组求最大子数组和（Kadane算法）
        5. 记录所有情况下的最大值
        
        参数:
            mat: List[List[int]] - 输入矩阵
            n: int - 行数
            m: int - 列数
            
        返回:
            int - 最大子矩阵和
            
        时间复杂度: O(n² × m)
        - 外层循环：枚举上边界i，共n次
        - 中层循环：枚举下边界j，平均n次
        - 内层循环1：更新压缩数组，m次
        - 内层循环2：Kadane算法，m次
        - 总计：n × n × m = O(n²m)
        
        空间复杂度: O(m)
        - 辅助数组arr：O(m)
        - 其他变量：O(1)
        
        是否为最优解：是的！
        - 这是经典的最优解，无法在一般情况下继续优化
        - 已有理论证明：基于比较模型，该问题的下界为Ω(n²m)
        """
        # 初始化最大值为负无穷，以应对全负数矩阵
        max_sum = float('-inf')
        
        # 外层循环：枚举上边界（起始行）
        for i in range(n):
            # 辅助数组，用于存储列压缩结果
            # arr[k]表示第k列在[i, j]行范围内的和
            arr = [0] * m
            
            # 中层循环：枚举下边界（结束行）
            for j in range(i, n):
                # 将第j行的每一列加入压缩数组
                # 这样arr[k]就代表[i,j]行范围内第k列的总和
                for k in range(m):
                    arr[k] += mat[j][k]
                
                # 对压缩后的一维数组应用Kadane算法
                # 找到当前行范围[i,j]内的最大子矩阵和
                max_sum = max(max_sum, self.maxSumSubarray(arr, m))
        
        return max_sum
    
    def maxSumSubarray(self, arr, m):
        """
        Kadane算法：求一维数组的最大子数组和
        
        这是经典的Kadane算法（卡德内算法），用于解决一维数组的最大子数组和问题
        
        算法原理：
        动态规划思想：
        - 状态定义：cur = 以当前位置结尾的最大子数组和
        - 状态转移：
          - 如果cur > 0，那么将当前元素加入子数组
          - 如果cur <= 0，那么从当前元素重新开始
        - 简化形式：cur = cur + arr[i] if cur > 0 else arr[i]
        - 更简化：cur += arr[i]; if cur < 0: cur = 0
        
        算法步骤：
        1. 初始化：max_sum = float('-inf'), cur = 0
        2. 遍历数组：
           a. cur += arr[i] (将当前元素加入)
           b. max_sum = max(max_sum, cur) (更新全局最大值)
           c. if cur < 0: cur = 0 (若变负，重新开始)
        3. 返回max_sum
        
        为什么这样做是对的？
        - 如果当前cur > 0，说明之前的子数组对后续有贡献，应该保留
        - 如果cur <= 0，之前的子数组只会拖累，应该从下一个元素重新开始
        - 这是贪心+动态规划的完美结合
        
        参数:
            arr: List[int] - 一维数组
            m: int - 数组长度
            
        返回:
            int - 最大子数组和
            
        时间复杂度: O(m) - 只需一次遍历
        空间复杂度: O(1) - 只需要两个变量
        
        是否为最优解：是的！
        - O(m)时间复杂度已经是最优（必须遍历每个元素）
        - O(1)空间复杂度已经是最优（只需常数空间）
        
        注意事项：
        1. 必须将max_sum初始化为float('-inf')，而不是0
           - 原因：如果所有元素都是负数，max_sum=0会导致错误
           - 例如：[-3, -1, -2]，正确答案是-1，但如果max_sum=0会返回0
        2. cur初始化为0是正确的
           - 因为第一个元素加入后cur=arr[0]，然后判断是否重置
        3. 必须先更新max_sum，再判断cur是否重置
           - 如果顺序颠倒，会漏掉某些情况
        """
        # 初始化最大值为负无穷，以处理全负数数组
        max_sum = float('-inf')
        
        # cur表示以当前位置结尾的最大子数组和
        cur = 0
        
        # 遍历数组
        for i in range(m):
            # 将当前元素加入子数组
            cur += arr[i]
            
            # 更新全局最大值（注意：必须在重置cur之前更新）
            max_sum = max(max_sum, cur)
            
            # 如果当前cur变为负数，从下一个元素重新开始
            # 因为负数只会拖累后续的累加
            if cur < 0:
                cur = 0
        
        return max_sum


# ==================================================================================
# Python语言特性说明
# ==================================================================================
# 1. 类型提示（Type Hints）
#    - Python 3.5+支持类型提示，增强代码可读性
#    - from typing import List
#    - def sumOfSubMatrix(self, mat: List[List[int]], n: int) -> int:
#
# 2. 列表 vs 数组
#    - Python列表：灵活，自动扩容，但效率略低
#    - NumPy数组：固定大小，效率高，适合大规模数值计算
#
# 3. float('-inf') vs Integer.MIN_VALUE
#    - Python：float('-inf') 负无穷
#    - Java：Integer.MIN_VALUE
#
# 4. 内置函数
#    - Python：max(), min() 内置函数
#    - Java：Math.max(), Math.min()
#
# ==================================================================================
# 性能优化建议
# ==================================================================================
# 1. 使用NumPy加速（适合大规模数据）
#    import numpy as np
#    mat = np.array(mat)
#
# 2. 使用列表推导式代替循环（Python风格）
#    arr = [sum(mat[r][c] for r in range(i, j+1)) for c in range(m)]
#
# 3. 使用PyPy解释器（比CPython快2-5倍）
#
# 4. 使用Cython编译为C代码
#
# ==================================================================================
# 测试用例
# ==================================================================================
# 测试用例1（全正数）：
# 输入：[[1, 2], [3, 4]]
# 输出：10
#
# 测试用例2（全负数）：
# 输入：[[-1, -2], [-3, -4]]
# 输出：-1
#
# 测试用例3（混合）：
# 输入：[[1, 2, 3], [-4, 5, -6], [7, 8, 9]]
# 输出：27
#
# ==================================================================================

# 示例测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：全正数
    mat1 = [[1, 2], [3, 4]]
    result1 = solution.sumOfSubMatrix(mat1, 2)
    print(f"Test 1 (全正数): {result1}")  # 期望：10 (整个矩阵)
    assert result1 == 10
    
    # 测试用例2：全负数
    mat2 = [[-1, -2], [-3, -4]]
    result2 = solution.sumOfSubMatrix(mat2, 2)
    print(f"Test 2 (全负数): {result2}")  # 期望：-1 (最大单元素)
    assert result2 == -1
    
    # 测试用例3：混合
    mat3 = [[1, 2, 3], [-4, 5, -6], [7, 8, 9]]
    result3 = solution.sumOfSubMatrix(mat3, 3)
    # 最优解：选择所有行[0-2]，所有列[0-2]
    # 压缩数组：[1-4+7, 2+5+8, 3-6+9] = [4, 15, 6]
    # Kadane：4+15+6 = 25
    print(f"Test 3 (混合正负): {result3}")  # 期望：25
    assert result3 == 25
    
    # 测试用例4：包含零
    mat4 = [[0, -2, -7], [9, 2, -6], [2, 4, 5]]
    result4 = solution.sumOfSubMatrix(mat4, 3)
    print(f"Test 4 (包含零): {result4}")
    
    print("\n✅ 所有测试通过！")


# ==================================================================================
# 题目2：LeetCode 363. 矩形区域不超过 K 的最大数值和
# ==================================================================================
# 题目来源：LeetCode (力扣)
# 题目链接：https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
# 难度等级：困难
#
# 【算法思路】
# 1. 二维压缩：枚举所有可能的上下边界，将矩阵压缩为一维数组
# 2. 前缀和 + 有序集合：在压缩后的一维数组中，使用前缀和和有序集合查找不超过k的最大值
#
# 【时间复杂度】O(n² × m × log m)
# - 枚举上下边界：O(n²)
# - 前缀和计算：O(m)
# - 有序集合查找：O(m × log m)
#
# 【空间复杂度】O(m)
# - 压缩数组：O(m)
# - 有序集合：O(m)
#
# 【是否为最优解】是的，对于这个问题的一般情况，这是最优解。
# ==================================================================================
class LeetCode363:
    """求矩形区域不超过 K 的最大数值和"""
    
    def maxSumSubmatrix(self, matrix, k):
        """
        主函数：求不超过k的最大矩形和
        
        参数:
            matrix: List[List[int]] - 输入矩阵
            k: int - 目标值
            
        返回:
            int - 不超过k的最大矩形和
        """
        if not matrix or not matrix[0]:
            return 0
        
        n = len(matrix)    # 行数
        m = len(matrix[0]) # 列数
        max_val = float('-inf')
        
        # 枚举上边界
        for i in range(n):
            row_sum = [0] * m  # 存储压缩后的行和
            
            # 枚举下边界
            for j in range(i, n):
                # 更新压缩后的行和
                for l in range(m):
                    row_sum[l] += matrix[j][l]
                
                # 在压缩后的一维数组中找不超过k的最大子数组和
                current_max = self._find_max_subarray_no_larger_than_k(row_sum, k)
                max_val = max(max_val, current_max)
                
                # 如果已经找到等于k的解，直接返回
                if max_val == k:
                    return k
        
        return max_val
    
    def _find_max_subarray_no_larger_than_k(self, arr, k):
        """
        辅助函数：在一维数组中找不超过k的最大子数组和
        
        使用前缀和和有序集合（bisect模块）来高效查找
        """
        import bisect
        
        max_sum = float('-inf')
        prefix_sum = 0
        
        # 使用列表存储前缀和，并保持有序
        prefix_sums = [0]
        
        for num in arr:
            prefix_sum += num
            
            # 寻找prefix_sum - x <= k → x >= prefix_sum - k
            target = prefix_sum - k
            # 使用bisect_left查找第一个大于等于target的前缀和
            idx = bisect.bisect_left(prefix_sums, target)
            
            if idx < len(prefix_sums):
                max_sum = max(max_sum, prefix_sum - prefix_sums[idx])
            
            # 将当前前缀和插入到正确位置，保持有序
            bisect.insort(prefix_sums, prefix_sum)
        
        return max_sum


# ==================================================================================
# 题目3：LeetCode 1074. 元素和为目标值的子矩阵数量
# ==================================================================================
# 题目来源：LeetCode (力扣)
# 题目链接：https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/
# 难度等级：困难
#
# 【算法思路】
# 1. 二维压缩：枚举所有可能的上下边界，将矩阵压缩为一维数组
# 2. 前缀和 + 哈希表：在压缩后的一维数组中，使用前缀和和哈希表统计和为target的子数组数量
#
# 【时间复杂度】O(n² × m)
# - 枚举上下边界：O(n²)
# - 前缀和计算和哈希表统计：O(m)
#
# 【空间复杂度】O(m)
# - 压缩数组：O(m)
# - 哈希表：O(m)
#
# 【是否为最优解】是的，这是该问题的最优解法。
# ==================================================================================
class LeetCode1074:
    """计算和为目标值的子矩阵数量"""
    
    def numSubmatrixSumTarget(self, matrix, target):
        """
        主函数：计算和为target的子矩阵数量
        
        参数:
            matrix: List[List[int]] - 输入矩阵
            target: int - 目标和
            
        返回:
            int - 和为target的子矩阵数量
        """
        if not matrix or not matrix[0]:
            return 0
        
        n = len(matrix)    # 行数
        m = len(matrix[0]) # 列数
        count = 0
        
        # 枚举上边界
        for i in range(n):
            row_sum = [0] * m  # 存储压缩后的行和
            
            # 枚举下边界
            for j in range(i, n):
                # 更新压缩后的行和
                for l in range(m):
                    row_sum[l] += matrix[j][l]
                
                # 在压缩后的一维数组中计算和为target的子数组数量
                count += self._find_subarray_sum(row_sum, target)
        
        return count
    
    def _find_subarray_sum(self, arr, target):
        """
        辅助函数：计算一维数组中和为target的子数组数量
        
        使用前缀和和哈希表来高效统计
        """
        # 哈希表记录前缀和出现的次数
        prefix_sum_count = {0: 1}  # 初始前缀和为0，出现一次
        
        prefix_sum = 0
        count = 0
        
        for num in arr:
            prefix_sum += num
            
            # 查找是否存在前缀和为 (prefix_sum - target)
            if (prefix_sum - target) in prefix_sum_count:
                count += prefix_sum_count[prefix_sum - target]
            
            # 更新当前前缀和的出现次数
            prefix_sum_count[prefix_sum] = prefix_sum_count.get(prefix_sum, 0) + 1
        
        return count


# ==================================================================================
# 题目4：洛谷 P1719 最大加权矩形
# ==================================================================================
# 题目来源：洛谷 (Luogu)
# 题目链接：https://www.luogu.com.cn/problem/P1719
# 难度等级：普及+/提高
# ==================================================================================
class LuoguP1719:
    """最大加权矩形求解器"""
    
    def __init__(self):
        self.n = 0
        self.matrix = []
    
    def input(self):
        """读取输入"""
        import sys
        self.n = int(sys.stdin.readline())
        self.matrix = []
        for _ in range(self.n):
            row = list(map(int, sys.stdin.readline().split()))
            self.matrix.append(row)
    
    def _kadane(self, arr):
        """Kadane算法实现"""
        max_val = float('-inf')
        cur = 0
        
        for num in arr:
            cur += num
            max_val = max(max_val, cur)
            if cur < 0:
                cur = 0
        
        return max_val
    
    def solve(self):
        """求解最大加权矩形"""
        max_val = float('-inf')
        
        # 枚举上边界
        for i in range(self.n):
            # 重置辅助数组
            arr = [0] * self.n
            
            # 枚举下边界
            for j in range(i, self.n):
                # 压缩列
                for k in range(self.n):
                    arr[k] += self.matrix[j][k]
                
                # 应用Kadane算法
                max_val = max(max_val, self._kadane(arr))
        
        return max_val


# ==================================================================================
# 题目5：牛客网 BM97 子矩阵最大和
# ==================================================================================
# 题目来源：牛客网 (NowCoder)
# 题目链接：https://www.nowcoder.com/practice/840eee05dccd4ffd8f9433ce8085946b
# 难度等级：中等
# ==================================================================================
class NowCoder_BM97:
    """子矩阵最大和求解器"""
    
    def _max_subarray(self, arr):
        """计算一维数组的最大子数组和"""
        max_val = float('-inf')
        cur = 0
        
        for num in arr:
            cur += num
            max_val = max(max_val, cur)
            if cur < 0:
                cur = 0
        
        return max_val
    
    def maxsumofSubmatrix(self, matrix):
        """
        主函数：求子矩阵的最大和
        
        参数:
            matrix: List[List[int]] - 输入矩阵
            
        返回:
            int - 最大子矩阵和
        """
        if not matrix or not matrix[0]:
            return 0
        
        n = len(matrix)
        m = len(matrix[0])
        max_val = float('-inf')
        
        for i in range(n):
            arr = [0] * m
            for j in range(i, n):
                for k in range(m):
                    arr[k] += matrix[j][k]
                max_val = max(max_val, self._max_subarray(arr))
        
        return max_val


# ==================================================================================
# 题目6：CodeChef - MAXREC
# ==================================================================================
# 题目来源：CodeChef
# 题目链接：https://www.codechef.com/problems/MAXREC
# 难度等级：中等
# ==================================================================================
class CodeChef_MAXREC:
    """CodeChef MAXREC问题求解器"""
    
    @staticmethod
    def _kadane(arr):
        """Kadane算法静态方法"""
        max_val = float('-inf')
        cur = 0
        
        for num in arr:
            cur += num
            max_val = max(max_val, cur)
            if cur < 0:
                cur = 0
        
        return max_val
    
    @staticmethod
    def solve(matrix, n, m):
        """
        静态方法求解最大子矩阵和
        
        参数:
            matrix: List[List[int]] - 输入矩阵
            n: int - 行数
            m: int - 列数
            
        返回:
            int - 最大子矩阵和
        """
        max_val = float('-inf')
        
        for i in range(n):
            arr = [0] * m
            for j in range(i, n):
                for k in range(m):
                    arr[k] += matrix[j][k]
                max_val = max(max_val, CodeChef_MAXREC._kadane(arr))
        
        return max_val


# ==================================================================================
# 题目7：SPOJ - MAXSUBM
# ==================================================================================
# 题目来源：SPOJ
# 题目链接：https://www.spoj.com/problems/MAXSUBM/
# 难度等级：中等
# ==================================================================================
class SPOJ_MAXSUBM:
    """"SPOJ MAXSUBM问题求解器"""
    
    @staticmethod
    def _kadane(arr):
        """Kadane算法静态方法"""
        max_val = float('-inf')
        cur = 0
        
        for num in arr:
            cur += num
            max_val = max(max_val, cur)
            if cur < 0:
                cur = 0
        
        return max_val
    
    @staticmethod
    def solve(matrix, r, c):
        """
        静态方法求解最大子矩阵和
        
        参数:
            matrix: List[List[int]] - 输入矩阵
            r: int - 行数
            c: int - 列数
            
        返回:
            int - 最大子矩阵和
        """
        max_val = float('-inf')
        
        for i in range(r):
            arr = [0] * c
            for j in range(i, r):
                for k in range(c):
                    arr[k] += matrix[j][k]
                max_val = max(max_val, SPOJ_MAXSUBM._kadane(arr))
        
        return max_val


# ==================================================================================
# Python测试示例（扩展功能）
# ==================================================================================
if __name__ == "__main__":
    # 测试LeetCode 363
    print("\n=== 测试 LeetCode 363 ===")
    leetcode363 = LeetCode363()
    test_matrix_363 = [[1, 0, 1], [0, -2, 3]]
    k = 2
    result_363 = leetcode363.maxSumSubmatrix(test_matrix_363, k)
    print(f"输入: matrix=[[1,0,1],[0,-2,3]], k=2")
    print(f"输出: {result_363}")
    print(f"期望: 2")
    
    # 测试LeetCode 1074
    print("\n=== 测试 LeetCode 1074 ===")
    leetcode1074 = LeetCode1074()
    test_matrix_1074 = [[0, 1, 0], [1, 1, 1], [0, 1, 0]]
    target = 0
    result_1074 = leetcode1074.numSubmatrixSumTarget(test_matrix_1074, target)
    print(f"输入: matrix=[[0,1,0],[1,1,1],[0,1,0]], target=0")
    print(f"输出: {result_1074}")
    print(f"期望: 4")


# ==================================================================================
# 题目8：LeetCode 152. 乘积最大子数组
# ==================================================================================
# 题目来源：LeetCode (力扣)
# 题目链接：https://leetcode.com/problems/maximum-product-subarray/
# 难度等级：中等
#
# 【算法思路】
# 1. 同时维护当前最大值和最小值（因为负数×负数可能得到正数）
# 2. 对于每个元素，考虑三种情况：当前元素本身、当前元素×最大值、当前元素×最小值
# 3. 更新全局最大值
#
# 【时间复杂度】O(n)
# - 只需一次遍历数组
#
# 【空间复杂度】O(1)
# - 只需要常数空间存储变量
#
# 【是否为最优解】是的！
# - 这是该问题的最优解法，无法继续优化
# ==================================================================================
class LeetCode152:
    """乘积最大子数组求解器"""
    
    def maxProduct(self, nums):
        """
        主函数：求乘积最大子数组
        
        参数:
            nums: List[int] - 输入数组
            
        返回:
            int - 乘积最大子数组的乘积值
        """
        if not nums:
            return 0
        
        max_prod = nums[0]    # 全局最大值
        cur_max = nums[0]     # 当前最大值
        cur_min = nums[0]     # 当前最小值
        
        for i in range(1, len(nums)):
            # 由于负数×负数可能得到正数，需要同时考虑三种情况
            temp_max = cur_max
            cur_max = max(nums[i], nums[i] * cur_max, nums[i] * cur_min)
            cur_min = min(nums[i], nums[i] * temp_max, nums[i] * cur_min)
            
            max_prod = max(max_prod, cur_max)
        
        return max_prod


# ==================================================================================
# 题目9：LeetCode 918. 环形子数组的最大和
# ==================================================================================
# 题目来源：LeetCode (力扣)
# 题目链接：https://leetcode.com/problems/maximum-sum-circular-subarray/
# 难度等级：中等
#
# 【算法思路】
# 环形数组的最大子数组和有两种情况：
# 1. 情况一：最大子数组在数组中间（非环形）→ 标准Kadane算法
# 2. 情况二：最大子数组跨越数组首尾（环形）→ 总和 - 最小子数组和
#
# 特殊情况：如果数组全为负数，则直接返回最大元素
#
# 【时间复杂度】O(n)
# - 需要两次遍历数组
#
# 【空间复杂度】O(1)
# - 只需要常数空间
#
# 【是否为最优解】是的！
# - 这是环形数组最大子数组和的最优解法
# ==================================================================================
class LeetCode918:
    """环形子数组的最大和求解器"""
    
    def _kadane(self, nums):
        """标准Kadane算法：求最大子数组和"""
        max_val = float('-inf')
        cur = 0
        
        for num in nums:
            cur += num
            max_val = max(max_val, cur)
            if cur < 0:
                cur = 0
        
        return max_val
    
    def _min_kadane(self, nums):
        """求最小子数组和（Kadane算法变种）"""
        min_val = float('inf')
        cur = 0
        
        for num in nums:
            cur += num
            min_val = min(min_val, cur)
            if cur > 0:
                cur = 0
        
        return min_val
    
    def maxSubarraySumCircular(self, nums):
        """
        主函数：求环形数组的最大子数组和
        
        参数:
            nums: List[int] - 输入数组（环形）
            
        返回:
            int - 环形数组的最大子数组和
        """
        if not nums:
            return 0
        
        max_kadane = self._kadane(nums)  # 情况一：标准最大子数组和
        
        # 如果最大子数组和为负数，说明整个数组都是负数
        if max_kadane < 0:
            return max_kadane
        
        # 计算数组总和
        total_sum = sum(nums)
        
        # 情况二：环形情况的最大和 = 总和 - 最小子数组和
        min_kadane = self._min_kadane(nums)
        max_circular = total_sum - min_kadane
        
        # 返回两种情况的最大值
        return max(max_kadane, max_circular)


# ==================================================================================
# 扩展测试代码
# ==================================================================================
if __name__ == "__main__":
    # 测试LeetCode 152
    print()
    print("=== 测试 LeetCode 152 ===")
    leetcode152 = LeetCode152()
    test_nums_152 = [2, 3, -2, 4]
    result_152 = leetcode152.maxProduct(test_nums_152)
    print("输入: nums=[2,3,-2,4]")
    print("输出:", result_152)
    print("期望: 6")
    
    # 测试LeetCode 918
    print()
    print("=== 测试 LeetCode 918 ===")
    leetcode918 = LeetCode918()
    test_nums_918 = [5, -3, 5]
    result_918 = leetcode918.maxSubarraySumCircular(test_nums_918)
    print("输入: nums=[5,-3,5]")
    print("输出:", result_918)
    print("期望: 10")
    
    print()
    print("所有扩展测试完成！")
