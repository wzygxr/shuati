# HDU 3949 XOR问题（线性基求第k小异或和）
# 题目来源：HDU 3949 XOR
# 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3949
# 题目描述：给定n个数，求这些数能异或出的第k小值
# 算法：线性基（高斯消元法）
# 时间复杂度：构建线性基O(n * log(max_value))，单次查询O(log(max_value))
# 空间复杂度：O(log(max_value))
# 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=3949

import sys

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

def compute(arr, BIT):
    """
    高斯消元构建线性基
    
    算法思路：
    1. 先用普通消元法构建线性基
    2. 再用高斯消元法整理线性基，使其具有阶梯状结构
    3. 重新整理basis数组，把非0的元素移到前面
    4. 判断是否能异或出0
    
    参数:
        arr: 输入数组
        BIT: 最大位数
        
    返回:
        tuple: (basis, len, zero) 线性基数组、线性基大小、是否能异或出0
    """
    n = len(arr)
    
    # 初始化线性基
    basis = [0] * (BIT + 1)
    
    # 先用普通消元法构建线性基
    for num in arr:
        insert(num, basis, BIT)
    
    # 再用高斯消元法整理线性基，使其具有阶梯状结构
    for i in range(BIT + 1):
        for j in range(i + 1, BIT + 1):
            if (basis[j] & (1 << i)) != 0:
                basis[j] ^= basis[i]
    
    # 重新整理basis数组，把非0的元素移到前面
    temp_basis = [b for b in basis if b != 0]
    
    # 判断是否能异或出0
    zero = (len(temp_basis) != n)
    
    return temp_basis, len(temp_basis), zero

def query(k, basis, len_basis, zero):
    """
    返回第k小的异或和
    
    算法思路：
    1. 特殊情况处理：如果能异或出0，0是第1小的
    2. 如果能异或出0，且k=1，返回0
    3. 如果能异或出0，且k>1，将k减1后继续处理
    4. 根据k的二进制表示选择线性基中的元素进行异或
    
    参数:
        k: 查询的第k小值
        basis: 线性基数组
        len_basis: 线性基大小
        zero: 是否能异或出0
        
    返回:
        long: 第k小的异或和，如果不存在则返回-1
    """
    # 异常处理：k必须大于0
    if k <= 0:
        raise ValueError("k must be positive")
    
    # 如果能异或出0，那么0是第1小的
    if zero:
        if k == 1:
            return 0
        k -= 1  # 跳过0
    
    # 如果k超过了可能的异或和数量，返回-1
    if k > (1 << len_basis):
        return -1
    
    ans = 0
    # 根据k的二进制表示选择线性基中的元素进行异或
    for i in range(len_basis):
        if (k & (1 << i)) != 0:
            ans ^= basis[i]
    
    return ans

def main():
    """主函数"""
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    t = int(data[idx])
    idx += 1
    
    for cases in range(1, t + 1):
        print(f"Case #{cases}:")
        
        n = int(data[idx])
        idx += 1
        
        arr = []
        for i in range(n):
            arr.append(int(data[idx]))
            idx += 1
        
        basis, len_basis, zero = compute(arr, 60)
        
        q = int(data[idx])
        idx += 1
        
        for i in range(q):
            k = int(data[idx])
            idx += 1
            try:
                result = query(k, basis, len_basis, zero)
                print(result)
            except ValueError:
                print(-1)  # 发生异常时输出-1

if __name__ == "__main__":
    main()

'''


'''