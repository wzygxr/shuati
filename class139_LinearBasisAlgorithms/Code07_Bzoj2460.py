# BZOJ 2460 元素问题（线性基+贪心）
# 题目来源：BZOJ 2460 元素
# 题目链接：https://www.lydsy.com/JudgeOnline/problem.php?id=2460
# 题目描述：有n个二元组(ai, bi)，要求选出一些二元组，
# 使得这些二元组的a值异或和不为0，且b值和最大
# 算法：线性基 + 贪心
# 时间复杂度：O(n * log(n) + n * log(max_value))
# 空间复杂度：O(n + log(max_value))
# 测试链接 : https://www.lydsy.com/JudgeOnline/problem.php?id=2460

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

def compute(elements, BIT):
    """
    普通消元法构建线性基
    返回最大b值和
    
    算法思路：
    1. 按b值从大到小排序，贪心选择
    2. 清空线性基
    3. 依次尝试插入每个元素的a值
    4. 如果能成功插入，则选择该二元组
    
    参数:
        elements: 二元组列表，每个元素为(a, b)的元组
        BIT: 最大位数
        
    返回:
        long: 最大b值和
    """
    # 按b值从大到小排序
    elements.sort(key=lambda x: x[1], reverse=True)
    
    # 初始化线性基
    basis = [0] * (BIT + 1)
    
    ans = 0
    # 依次尝试插入每个元素
    for a, b in elements:
        if insert(a, basis, BIT):
            ans += b
            
    return ans

def main():
    """主函数"""
    n = int(input())
    elements = []
    
    for _ in range(n):
        line = input().split()
        a = int(line[0])
        b = int(line[1])
        elements.append((a, b))
    
    result = compute(elements, 60)
    print(result)

if __name__ == "__main__":
    main()