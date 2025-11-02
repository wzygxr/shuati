# Codeforces 1101G (Zero XOR Subset)-less问题（线性基应用）
# 题目来源：Codeforces 1101G (Zero XOR Subset)-less
# 题目链接：https://codeforces.com/problemset/problem/1101/G
# 题目描述：给定一个长度为n的数组，将数组分成尽可能多的段，
# 使得每一段的异或和都不为0，求最多能分成多少段
# 算法：线性基
# 时间复杂度：O(n * log(max_value))
# 空间复杂度：O(n + log(max_value))
# 测试链接 : https://codeforces.com/problemset/problem/1101/G

def insert(num, basis, BIT):
    """
    线性基里插入num，如果线性基增加了返回true，否则返回false
    
    参数:
        num: 要插入的数字
        basis: 线性基数组
        BIT: 最大位数
        
    返回:
        bool: 插入是否成功
    """
    for i in range(BIT, -1, -1):
        if (num >> i) & 1:
            if basis[i] == 0:
                basis[i] = num
                return True
            num ^= basis[i]
    return False

def compute(prefix, BIT):
    """
    普通消元法构建线性基
    返回线性基的大小
    
    算法思路：
    1. 清空线性基
    2. 将所有前缀异或和插入线性基
    3. 返回线性基的大小
    
    参数:
        prefix: 前缀异或和数组
        BIT: 最大位数
        
    返回:
        int: 线性基的大小
    """
    # 初始化线性基
    basis = [0] * (BIT + 1)
    
    size = 0
    # 将所有前缀异或和插入线性基
    for p in prefix:
        if insert(p, basis, BIT):
            size += 1
            
    return size

def main():
    """主函数"""
    n = int(input())
    arr = list(map(int, input().split()))
    
    # 计算前缀异或和
    prefix = [0] * (n + 1)
    for i in range(1, n + 1):
        prefix[i] = prefix[i - 1] ^ arr[i - 1]
    
    # 特殊情况处理：如果整个数组的异或和为0，则无法分割，返回-1
    if prefix[n] == 0:
        print(-1)
    else:
        # 否则答案为线性基大小减1（因为线性基中包含0）
        result = compute(prefix, 30) - 1
        print(result)

if __name__ == "__main__":
    main()