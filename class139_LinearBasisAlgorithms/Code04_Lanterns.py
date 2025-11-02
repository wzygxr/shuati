# 彩灯问题（线性基应用）
# 题目来源：洛谷P3857 [TJOI2008] 彩灯
# 题目链接：https://www.luogu.com.cn/problem/P3857
# 题目描述：一共有n个灯泡，开始都是不亮的状态，有m个开关
# 每个开关能改变若干灯泡的状态，改变是指，亮变不亮、不亮变亮
# 比如n=5，某个开关为XXOOO，表示这个开关只能改变后3个灯泡的状态
# 可以随意使用开关，返回有多少种亮灯的组合，全不亮也算一种组合
# 答案可能很大，对 2008 取模
# 算法：线性基
# 时间复杂度：O(m * n)
# 空间复杂度：O(n)
# 测试链接 : https://www.luogu.com.cn/problem/P3857

def insert(num, basis, n):
    """
    线性基里插入num，如果线性基增加了返回true，否则返回false
    
    参数:
        num: 要插入的数字
        basis: 线性基数组
        n: 灯泡数量
        
    返回:
        bool: 插入是否成功
    """
    for i in range(n - 1, -1, -1):
        if (num >> i) & 1:
            if basis[i] == 0:
                basis[i] = num
                return True
            num ^= basis[i]
    return False

def compute(switches, n):
    """
    计算线性基的大小
    
    算法思路：
    1. 清空线性基
    2. 将所有开关模式插入线性基
    3. 返回线性基的大小
    
    参数:
        switches: 开关影响列表
        n: 灯泡数量
        
    返回:
        int: 线性基的大小
    """
    # 初始化线性基
    basis = [0] * n
    
    size = 0
    for switch in switches:
        if insert(switch, basis, n):
            size += 1
            
    return size

def calculate_result(switches, n):
    """
    计算结果
    
    算法思路：
    1. 计算线性基的大小
    2. 答案就是2^(线性基大小) % MOD
    
    参数:
        switches: 开关影响列表
        n: 灯泡数量
        
    返回:
        int: 结果
    """
    MOD = 2008
    size = compute(switches, n)
    # 计算2^size % MOD
    result = 1
    for i in range(size):
        result = (result * 2) % MOD
    return result

def main():
    """主函数"""
    # 读取输入
    line = input().split()
    n = int(line[0])
    m = int(line[1])
    
    switches = []
    for _ in range(m):
        pattern = input().strip()
        num = 0
        for j in range(n):
            if pattern[j] == 'O':
                num |= 1 << j
        switches.append(num)
    
    result = calculate_result(switches, n)
    print(result)

if __name__ == "__main__":
    main()