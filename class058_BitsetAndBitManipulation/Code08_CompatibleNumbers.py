# Codeforces 165E Compatible Numbers
# 题目链接: https://codeforces.com/problemset/problem/165/E
# 题目大意:
# 给定一个长度为n的数组，对于数组中的每个数，找到数组中另一个数，
# 使得这两个数的按位与结果为0。如果不存在这样的数，输出-1。

# 解题思路:
# 1. 两个数按位与结果为0，意味着它们在二进制表示中没有同为1的位
# 2. 对于每个数x，我们需要找到一个数y，使得x & y = 0
# 3. 这等价于找到一个数y，使得y是x的按位取反的子集
# 4. 我们可以使用SOS DP (Sum over Subsets DP)来预处理每个数的子集
# 5. 对于每个数x，我们查找x按位取反后是否有子集在数组中存在
# 时间复杂度: O(n + 3^k)，其中k是位数(22位)
# 空间复杂度: O(2^k)

def main():
    """主函数，处理输入并输出结果"""
    # 读取数组长度
    n = int(input())
    # 读取数组元素
    a = list(map(int, input().split()))
    
    # 标记数组中存在哪些数
    # exists[i]为True表示数i在数组中存在
    exists = [False] * (1 << 22)
    # 记录每个数在数组中的位置
    # pos[i]表示数i在数组中的位置，-1表示不存在
    pos = [-1] * (1 << 22)
    
    # 读取数组
    for i in range(n):
        # 标记这个数存在
        exists[a[i]] = True
        # 记录这个数在数组中的位置
        pos[a[i]] = i
    
    # 存储答案，初始化为-1表示未找到兼容数
    answer = [-1] * n
    
    # 对于每个数，找到与它兼容的数
    for i in range(n):
        # 当前处理的数
        x = a[i]
        # x的按位取反(22位)
        # (1 << 22) - 1 创建一个22位全为1的数
        # x ^ ((1 << 22) - 1) 对x进行按位异或，实现按位取反
        complement = x ^ ((1 << 22) - 1)
        
        # 查找complement的子集是否有在数组中存在的
        # 使用SOS DP的思想
        # mask表示当前检查的complement的子集
        mask = complement
        # 循环枚举complement的所有子集
        while mask > 0:
            # 检查mask对应的数是否在数组中存在
            if exists[mask]:
                # 找到兼容数，记录其在原数组中的位置
                answer[i] = pos[mask]
                # 找到后跳出循环
                break
            # 枚举下一个子集
            # (mask - 1) & complement 计算mask的下一个子集
            mask = (mask - 1) & complement
        
        # 特殊情况：检查0是否在数组中
        # 0与任何数按位与都为0，所以如果数组中有0，它与任何数都兼容
        if answer[i] == -1 and exists[0]:
            answer[i] = pos[0]
    
    # 输出答案
    result = []
    for i in range(n):
        if answer[i] == -1:
            # 未找到兼容数，输出-1
            result.append("-1")
        else:
            # 找到兼容数，输出该数的值
            result.append(str(a[answer[i]]))
    
    # 使用join将结果连接成字符串并输出
    print(" ".join(result))

# 测试用例
def test():
    """测试用例"""
    print("Codeforces 165E Compatible Numbers 解题测试")
    # 由于这是在线评测题目，测试用例需要按照题目要求构造

# 程序入口点
if __name__ == "__main__":
    # 运行测试用例
    test()
    
    # 如果需要运行主程序，取消下面的注释
    # main()