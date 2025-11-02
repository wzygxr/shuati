# -*- coding: utf-8 -*-

"""
01分数规划基础题 - Dropping Tests
题目来源：POJ 2976, 洛谷 P10505
题目描述：给定n个数据，每个数据有(a, b)两个值，都为整数，并且都是非负的
请舍弃掉k个数据，希望让剩下数据做到，所有a的和 / 所有b的和，这个比值尽量大
如果剩下数据所有b的和为0，认为无意义
最后，将该比值 * 100，小数部分四舍五入的整数结果返回
数据范围：1 <= n <= 100，0 <= a、b <= 10^9
测试链接：https://www.luogu.com.cn/problem/P10505
测试链接：http://poj.org/problem?id=2976

补充题目：
1. Codeforces 489E Hiking - 基础01分数规划变体
2. 洛谷 P1642 规划 - 基础01分数规划
3. UVA 1389 Hard Life - 最大密度子图问题
4. 洛谷 U581184 【模板】01-分数规划

算法思路：使用二分法求解01分数规划问题
时间复杂度：O(log(1/ε) * n log n)，其中ε是精度要求
空间复杂度：O(n)

01分数规划的数学原理：
我们需要最大化 R = (sum(a_i * x_i)) / (sum(b_i * x_i))，其中x_i∈{0,1}
对于给定的L，判断是否存在x的选择使得 R > L
等价于：sum(a_i * x_i) > L * sum(b_i * x_i)
等价于：sum((a_i - L * b_i) * x_i) > 0
我们通过二分L的值，使用贪心策略判断是否可行
"""



import sys

# 常量定义
MAXN = 1001  # 最大数据规模
sml = 1e-6   # 精度控制，用于二分结束条件

"""
二维列表存储数据：
arr[i][0] = i号数据的a值
arr[i][1] = i号数据的b值
arr[i][2] = i号数据的结余值，即a - x * b
"""
arr = [[0.0 for _ in range(3)] for _ in range(MAXN)]

# 全局变量，存储当前数据规模和需要选择的数据个数
n = 0
k = 0

def check(x):
    """
    检查函数：判断给定的比率值x是否可行
    
    原理：对于当前x，计算每个元素的结余(a_i - x*b_i)，选择结余最大的k个
    如果这k个的和大于等于0，则说明存在更优的比率，可以尝试增大x
    
    参数：
        x (float): 当前尝试的比率值
    
    返回：
        bool: 如果x可行返回True，否则返回False
    """
    # x固定的情况下，计算所有数据的结余值
    for i in range(1, n + 1):
        arr[i][2] = arr[i][0] - x * arr[i][1]
    
    # 结余从大到小排序
    # 在Python中，我们使用自定义排序键
    temp_arr = arr[1:n+1]  # 获取有效部分
    temp_arr.sort(key=lambda item: item[2], reverse=True)
    arr[1:n+1] = temp_arr  # 将排序后的结果放回原数组
    
    # 计算最大的k个结余值的累加和
    sum_val = 0.0
    for i in range(1, k + 1):
        sum_val += arr[i][2]
    
    # 如果总和大于等于0，说明x可行
    return sum_val >= 0

def main():
    """
    主函数：处理输入输出，执行二分查找
    
    处理流程：
    1. 读取输入数据
    2. 转换问题：将舍弃k个转换为选择n-k个
    3. 使用二分法查找最优比率
    4. 输出结果
    """
    global n, k
    
    try:
        while True:
            # 读取第一组数据
            line = sys.stdin.readline()
            # 处理EOF情况
            if not line:
                break
            
            line = line.strip().split()
            # 处理空行
            if not line:
                continue
            
            n = int(line[0])
            k = int(line[1])
            
            # 终止条件：输入0 0
            if n == 0 and k == 0:
                break
            
            # 题目要求舍弃k个元素，实际上是选择n-k个元素
            k = n - k
            
            # 读取a值
            line = sys.stdin.readline().strip().split()
            for i in range(1, n + 1):
                arr[i][0] = float(line[i - 1])
            
            # 读取b值
            line = sys.stdin.readline().strip().split()
            for i in range(1, n + 1):
                arr[i][1] = float(line[i - 1])
            
            # 初始化二分查找的左右边界
            # 左边界为0，右边界为可能的最大值（所有a的和）
            l = 0.0
            r = 0.0
            for i in range(1, n + 1):
                r += arr[i][0]
            
            ans = 0.0
            # 二分查找过程
            # 当左右边界的差大于精度要求时继续循环
            while l <= r and r - l >= sml:
                x = (l + r) / 2.0
                if check(x):
                    # 如果x可行，记录当前答案，并尝试更大的值
                    ans = x
                    l = x + sml  # 注意这里要加上sml，避免死循环
                else:
                    # 如果x不可行，尝试更小的值
                    r = x - sml
            
            # 输出结果，乘以100后四舍五入
            # 使用+0.005的方式实现四舍五入
            print(int(100 * (ans + 0.005)))
    except Exception as e:
        # 错误处理
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()