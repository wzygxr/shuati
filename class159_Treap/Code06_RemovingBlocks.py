"""
砖块消除问题 - Python实现
给定一个长度为n的数组arr，arr[i]为i号砖块的重量
选择一个没有消除的砖块进行消除，收益为被消除砖块联通区域的重量之和
一共有n!种消除方案，返回所有消除方案的收益总和，答案对 1000000007 取模
1 <= n <= 10^5    1 <= arr[i] <= 10^9
测试链接 : https://www.luogu.com.cn/problem/AT_agc028_b
测试链接 : https://atcoder.jp/contests/agc028/tasks/agc028_b

算法思路：
1. 使用组合数学和概率统计方法
2. 计算每个砖块在所有消除方案中的贡献
3. 时间复杂度：O(n)
4. 空间复杂度：O(n)

工程化考量：
- 使用线性逆元预处理提高效率
- 注意大数运算的模运算
- 优化内存使用，避免不必要的存储
- Python版本需要注意递归深度和内存限制
"""

MOD = 1000000007

def power(x, p):
    """快速幂计算 x^p % MOD"""
    result = 1
    while p > 0:
        if p & 1:
            result = (result * x) % MOD
        x = (x * x) % MOD
        p >>= 1
    return result

def precompute_inv(n):
    """线性预处理逆元"""
    inv = [0] * (n + 1)
    inv[1] = 1
    for i in range(2, n + 1):
        inv[i] = (MOD - inv[MOD % i] * (MOD // i) % MOD) % MOD
    return inv

def precompute_sum(inv, n):
    """计算前缀和数组"""
    sum_arr = [0] * (n + 1)
    for i in range(1, n + 1):
        sum_arr[i] = (sum_arr[i - 1] + inv[i]) % MOD
    return sum_arr

def main():
    """主函数"""
    import sys
    
    # 读取输入
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    arr = [0] * (n + 1)
    
    for i in range(1, n + 1):
        arr[i] = int(data[i])
    
    # 预处理逆元
    inv = precompute_inv(n)
    
    # 计算前缀和
    sum_arr = precompute_sum(inv, n)
    
    ans = 0
    
    # 计算每个砖块的贡献
    for i in range(1, n + 1):
        # 计算砖块i在所有消除方案中的期望贡献
        contribution = (sum_arr[i] + sum_arr[n - i + 1] - 1) % MOD
        if contribution < 0:
            contribution += MOD
        
        ans = (ans + contribution * arr[i]) % MOD
    
    # 乘以n!，即所有排列的数量
    factorial = 1
    for i in range(1, n + 1):
        factorial = (factorial * i) % MOD
    
    ans = (ans * factorial) % MOD
    
    print(ans)

if __name__ == "__main__":
    main()

"""
算法详细解释：
1. 对于每个砖块i，计算它在所有消除方案中的期望贡献
2. 砖块i被消除时，它的贡献是它所在连通区域的重量之和
3. 通过组合数学计算，砖块i的期望贡献系数为：sum[i] + sum[n-i+1] - 1
4. 其中sum[i] = 1/1 + 1/2 + ... + 1/i (模MOD意义下)
5. 最后乘以n!得到所有方案的总收益

时间复杂度分析：
- 预处理逆元：O(n)
- 计算前缀和：O(n)
- 计算总贡献：O(n)
- 总时间复杂度：O(n)

空间复杂度分析：
- 存储逆元数组：O(n)
- 存储前缀和数组：O(n)
- 总空间复杂度：O(n)

边界情况处理：
- n=1时，只有一个砖块，收益就是该砖块的重量
- 大数运算时注意模运算，避免溢出
- 负数的模运算需要特殊处理

Python特定优化：
- 使用迭代而非递归避免栈溢出
- 使用列表推导式提高代码可读性
- 注意Python的整数范围，及时取模
"""