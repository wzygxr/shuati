# Ann and Books问题 - 前缀和+字典实现 (Python版本)
# 题目来源: https://codeforces.com/problemset/problem/877/E
# 题目大意: 给定一个长度为n的数组arr，有q次查询，每次查询[l,r]区间内和等于k的子数组个数
# 约束条件: 1 <= n, q <= 10^5, |arr[i]| <= 10^9

# 定义最大数组长度
MAXN = 100005

# 全局变量
n, q, k = 0, 0, 0

# arr: 原始数组
arr = [0] * MAXN

# prefixSum: 前缀和数组
prefixSum = [0] * MAXN

def query(l, r):
    """
    查询[l,r]区间内和等于k的子数组个数
    时间复杂度: O(r-l+1)
    设计思路: 使用前缀和和字典统计满足条件的子数组个数
    对于子数组[i,j]，其和为prefixSum[j] - prefixSum[i-1]
    要使子数组和等于k，即prefixSum[j] - prefixSum[i-1] = k
    变形得prefixSum[i-1] = prefixSum[j] - k
    因此我们可以在遍历过程中统计每个前缀和出现的次数，然后查找prefixSum[j] - k是否出现过
    参数:
        l: 查询区间左边界
        r: 查询区间右边界
    返回:
        区间内和等于k的子数组个数
    """
    # 使用字典统计前缀和出现次数
    count = {0: 1}  # 前缀和为0出现1次（表示空前缀）
    
    result = 0
    
    # 遍历查询区间内的每个位置
    for i in range(l, r + 1):
        # 计算从位置l-1到位置i的前缀和
        currentSum = prefixSum[i] - prefixSum[l - 1]
        
        # 查找是否存在前缀和使得currentSum - prevSum = k
        # 即prevSum = currentSum - k
        result += count.get(currentSum - k, 0)
        
        # 更新当前前缀和的计数
        count[currentSum] = count.get(currentSum, 0) + 1
    
    return result

def main():
    global n, q, k
    
    # 读取数组长度n和目标和k
    line = input().split()
    n = int(line[0])
    k = int(line[1])
    
    # 读取初始数组并计算前缀和
    temp = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = temp[i - 1]
        prefixSum[i] = prefixSum[i - 1] + arr[i]

    # 读取查询次数q
    q = int(input())
    
    # 处理q次查询
    for i in range(1, q + 1):
        l, r = map(int, input().split())
        # 输出查询结果
        print(query(l, r))

if __name__ == "__main__":
    main()