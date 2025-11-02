# 最大异或和问题
# 题目来源：洛谷P3812 【模板】线性基
# 题目链接：https://www.luogu.com.cn/problem/P3812
# 题目描述：给定n个整数（数字可能重复），求在这些数中选取任意个，使得他们的异或和最大。
# 算法：线性基（普通消元法）
# 时间复杂度：O(n * log(max_value))
# 空间复杂度：O(log(max_value))
# 测试链接 : https://www.luogu.com.cn/problem/P3812

MAXN = 51        # 最大数组长度
BIT = 50         # 最大位数，因为arr[i] <= 2^50

arr = [0] * MAXN                # 存储输入数组
basis = [0] * (BIT + 1)         # 线性基数组，basis[i]表示第i位的基
n = 0                           # 数组长度

def compute():
    """
    普通消元法构建线性基并计算最大异或和
    
    算法思路：
    1. 从最高位开始扫描每个数
    2. 对于每个数，尝试将其插入到线性基中
    3. 插入过程：从高位到低位扫描，如果当前位为1且线性基中该位为空，则直接插入；否则用线性基中该位的数异或当前数，继续处理
    4. 构建完成后，从高位到低位贪心地选择线性基中的元素，使异或和最大
    
    时间复杂度分析：
    - 构建线性基：O(n * BIT) = O(n * log(max_value))
    - 查询最大异或值：O(BIT) = O(log(max_value))
    - 总时间复杂度：O(n * log(max_value))
    
    空间复杂度分析：
    - 线性基数组：O(BIT) = O(log(max_value))
    - 输入数组：O(n)
    - 总空间复杂度：O(n + log(max_value))
    
    Python实现特点：
    - 使用int类型，Python整数无大小限制
    - 自动内存管理，无需手动初始化
    - 位运算效率相对较低，但代码简洁
    - 适合快速原型开发和算法验证
    
    最优解证明：
    线性基算法是解决最大异或和问题的最优解，因为：
    1. 线性基能够表示原数组的所有异或组合
    2. 贪心选择保证了最大异或值的正确性
    3. 时间复杂度已经达到理论下界
    
    返回：
        int: 最大异或和
    """
    # 构建线性基：将数组中的每个数插入线性基
    for i in range(1, n + 1):
        insert(arr[i])
    
    # 贪心选择：从高位到低位，尽可能让每一位为1
    ans = 0
    for i in range(BIT, -1, -1):
        # 如果当前位为0，尝试异或basis[i]使其变为1
        # 如果当前位为1，保持不动（因为1比0大）
        ans = max(ans, ans ^ basis[i])
    return ans

def insert(num):
    """
    线性基插入操作
    
    算法思路：
    1. 从最高位到最低位扫描数字的二进制位
    2. 如果当前位为1：
       - 如果线性基中该位为空，则将当前数插入该位置
       - 否则用线性基中该位的数异或当前数，继续处理低位
    3. 如果成功插入返回True，否则返回False（表示该数可由现有线性基表示）
    
    时间复杂度：O(BIT) = O(log(max_value))
    空间复杂度：O(1)
    
    关键细节：
    - 从高位到低位处理：保证线性基中每个基的最高位唯一
    - 异或操作：消除当前数中与现有基重叠的部分
    - 返回值：True表示线性基增加了新基，False表示该数线性相关
    
    异常场景处理：
    - 输入为0：直接返回False（0可由空集表示）
    - 输入为负数：Python中整数无符号位问题
    
    参数：
        num: 要插入的数字
    
    返回：
        bool: 如果线性基增加了新基返回True，否则返回False
    """
    global basis
    
    # 边界情况：如果num为0，直接返回False（0可由空集表示）
    if num == 0:
        return False
    
    # 从最高位到最低位扫描
    for i in range(BIT, -1, -1):
        # 检查第i位是否为1
        if (num >> i) & 1:
            if basis[i] == 0:
                # 当前位为空，插入新基
                basis[i] = num
                return True
            else:
                # 当前位已有基，用该基异或当前数，消除当前位
                num ^= basis[i]
    return False

def main():
    global n
    n = int(input())
    temp = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = temp[i - 1]
    
    # 清空线性基数组
    for i in range(BIT + 1):
        basis[i] = 0
    
    print(compute())

# ============== LeetCode 421. 数组中两个数的最大异或值 ==============
# 题目链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
# 题目描述：给你一个整数数组 nums，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n。
# 算法：线性基 / 字典树
# 时间复杂度：O(n * 32)
# 空间复杂度：O(32)
def findMaximumXORLeetCode421(nums):
    # 构建线性基
    basis421 = [0] * 32  # 因为nums[i] <= 2^31 - 1
    
    # 插入所有数字到线性基
    for num in nums:
        x = num
        for i in range(31, -1, -1):
            if (x >> i) & 1:
                if basis421[i] == 0:
                    basis421[i] = x
                    break
                else:
                    x ^= basis421[i]
    
    # 计算最大异或值
    max_xor = 0
    for num in nums:
        current = num
        for i in range(31, -1, -1):
            # 尝试让当前位为1
            if not ((current >> i) & 1) and basis421[i] != 0:
                current ^= basis421[i]
        max_xor = max(max_xor, current)
    
    return max_xor

# ============== LeetCode 1738. 找出第 K 大的异或坐标值 ==============
# 题目链接：https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/
# 题目描述：给你一个二维矩阵 matrix 和一个整数 k ，矩阵大小为 m x n 由非负整数组成。
# 要求找到矩阵中所有可能的异或坐标值中的第 k 大的值。
# 算法：二维前缀异或和 + 排序
# 时间复杂度：O(m*n + m*n*log(m*n))
# 空间复杂度：O(m*n)
def kthLargestValueLeetCode1738(matrix, k):
    m = len(matrix)
    n = len(matrix[0])
    xor_sum = [[0] * n for _ in range(m)]
    
    # 计算二维前缀异或和
    xor_sum[0][0] = matrix[0][0]
    # 初始化第一列
    for i in range(1, m):
        xor_sum[i][0] = xor_sum[i-1][0] ^ matrix[i][0]
    # 初始化第一行
    for j in range(1, n):
        xor_sum[0][j] = xor_sum[0][j-1] ^ matrix[0][j]
    # 填充其余部分
    for i in range(1, m):
        for j in range(1, n):
            xor_sum[i][j] = xor_sum[i-1][j-1] ^ xor_sum[i-1][j] ^ xor_sum[i][j-1] ^ matrix[i][j]
    
    # 收集所有异或值
    all_values = []
    for i in range(m):
        for j in range(n):
            all_values.append(xor_sum[i][j])
    
    # 排序并返回第k大的值
    all_values.sort()
    return all_values[len(all_values) - k]

if __name__ == "__main__":
    main()

'''
线性基算法详解

线性基（Linear Basis）是一种处理异或问题的重要数据结构，主要用于解决以下几类问题：
1. 求n个数中选取任意个数异或能得到的最大值
2. 求n个数中选取任意个数异或能得到的第k小值
3. 判断一个数是否能由给定数组中的数异或得到
4. 求能异或得到的数的个数

核心思想

线性基类似于线性代数中的基向量概念，它是一组线性无关的向量集合，
能够表示原集合中所有数的异或组合。线性基有以下重要性质：

1. 原序列中的任意一个数都可以由线性基中的某些数异或得到
2. 线性基中的任意一些数异或起来都不能得到0
3. 在保持性质1的前提下，线性基中的数的个数是最少的
4. 线性基中每个元素的二进制最高位互不相同

线性基的构建方法

线性基的构建主要有两种方法：普通消元法和高斯消元法。

普通消元法

普通消元法是最常用的构建线性基的方法，其基本思路是：

1. 从最高位开始扫描
2. 对于每个数，尝试将其插入到线性基中
3. 插入过程：从高位到低位扫描，如果当前位为1且线性基中该位为空，
   则直接插入；否则用线性基中该位的数异或当前数，继续处理

时间复杂度分析：
- 构建线性基：O(n * log(max_value))，其中n为数组长度，max_value为数组中的最大值
- 查询最大异或值：O(log(max_value))

空间复杂度分析：
- O(log(max_value))，用于存储线性基

相关题目：
1. https://www.luogu.com.cn/problem/P3812 - 线性基模板题（最大异或和）
2. https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/ - LeetCode 421. 数组中两个数的最大异或值
3. https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/ - LeetCode 1738. 找出第 K 大的异或坐标值
4. https://loj.ac/p/114 - 第k小异或和
5. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
6. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
7. https://www.luogu.com.cn/problem/P4151 - 最大XOR和路径
8. https://www.luogu.com.cn/problem/P4301 - 最大异或和（可持久化线性基）
9. https://www.luogu.com.cn/problem/P3292 - 幸运数字（线性基+倍增）
10. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR
11. https://codeforces.com/problemset/problem/1101/G - Codeforces 1101G (Zero XOR Subset)-less
'''