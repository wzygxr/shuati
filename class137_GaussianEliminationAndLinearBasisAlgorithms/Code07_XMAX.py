# SPOJ XMAX - XOR Maximization
# 题目描述：
# 给定一个整数集合S，定义函数X(S)为集合中所有元素的异或值，
# 求X(S)的最大值。
# 1 <= |S| <= 10^5
# 0 <= ai <= 10^18
# 测试链接 : https://www.spoj.com/problems/XMAX/

# 线性基解决异或最大值问题 - SPOJ XMAX
# 
# 题目解析：
# 本题要求从给定的整数集合中选择一些数，使得它们的异或值最大。
# 这是一个典型的线性基问题，可以用高斯消元的思想来解决。
# 
# 解题思路：
# 1. 线性基构造：
#    - 从高位到低位依次考虑每个二进制位
#    - 对于每个二进制位，维护一个基向量，表示该位可以被表示的数
# 2. 贪心选择：
#    - 从高位到低位贪心地选择是否将对应的基向量加入结果
#    - 如果加入后结果变大，则加入；否则不加入
# 
# 时间复杂度：O(n * log(max_value))
# 空间复杂度：O(log(max_value))

MAXL = 64  # 64位整数

# 线性基数组
basis = [0] * MAXL

def insert(x):
    """
    插入一个数到线性基中
    
    线性基构造过程：
    1. 从高位到低位遍历该数的二进制位
    2. 对于每个为1的位：
       - 如果该位在线性基中还没有基向量，则直接插入
       - 否则用已有的基向量消去该位，继续处理
    
    :param x: 要插入的数
    """
    for i in range(MAXL - 1, -1, -1):
        if ((x >> i) & 1) == 0: 
            continue  # 如果第i位是0，跳过
        
        if basis[i] == 0:
            # 如果basis[i]为空，直接插入
            basis[i] = x
            break
        
        # 否则用basis[i]消去x的第i位
        x ^= basis[i]

def queryMax():
    """
    查询最大异或值
    
    贪心策略：
    从高位到低位贪心地选择是否加入basis[i]
    如果加入后结果变大，则加入；否则不加入
    
    :return: 最大异或值
    """
    result = 0
    for i in range(MAXL - 1, -1, -1):
        # 贪心地选择是否加入basis[i]
        if ((result >> i) & 1) == 0:  # 如果结果的第i位是0
            result ^= basis[i]  # 加入basis[i]可能会使结果更大
    return result

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    
    # 初始化线性基
    for i in range(MAXL):
        basis[i] = 0
    
    # 读取输入数据并插入到线性基中
    for i in range(n):
        x = int(data[i + 1])
        insert(x)
    
    # 查询最大异或值
    result = queryMax()
    print(result)

if __name__ == "__main__":
    main()