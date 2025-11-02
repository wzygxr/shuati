# 元素问题（线性基+贪心）
# 题目来源：洛谷P4570 [BJWC2011] 元素
# 题目链接：https://www.luogu.com.cn/problem/P4570
# 题目描述：给定n个魔法矿石，每个矿石有状态和魔力，都是整数
# 若干矿石组成的组合能否有效，根据状态异或的结果来决定
# 如果一个矿石组合内部会产生异或和为0的子集，那么这个组合无效
# 返回有效的矿石组合中，最大的魔力和是多少
# 算法：线性基 + 贪心
# 时间复杂度：O(n * log(n) + n * log(max_value))
# 空间复杂度：O(n + log(max_value))
# 测试链接 : https://www.luogu.com.cn/problem/P4570

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
    计算最大魔力和
    
    算法思路：
    1. 将所有矿石按魔力值从大到小排序
    2. 清空线性基
    3. 贪心选择矿石：依次尝试将每个矿石加入线性基，如果能成功加入则将该矿石的魔力加入答案
    
    参数:
        elements: 矿石列表，每个元素为(状态, 魔力)的元组
        BIT: 最大位数
        
    返回:
        long: 最大魔力和
    """
    # 按魔力值从大到小排序
    elements.sort(key=lambda x: x[1], reverse=True)
    
    # 初始化线性基
    basis = [0] * (BIT + 1)
    
    ans = 0
    # 贪心选择矿石
    for status, magic in elements:
        if insert(status, basis, BIT):
            ans += magic
            
    return ans

def main():
    """主函数"""
    n = int(input())
    elements = []
    
    for _ in range(n):
        line = input().split()
        status = int(line[0])
        magic = int(line[1])
        elements.append((status, magic))
    
    result = compute(elements, 60)
    print(result)

if __name__ == "__main__":
    main()