# HackerRank Java BitSet
# 题目链接: https://www.hackerrank.com/challenges/java-bitset/problem
# 题目大意:
# 给定两个BitSet，大小为n，初始时所有位都为0
# 执行一系列操作，每次操作后打印两个BitSet中1的个数

# 操作包括:
# AND 1 2: 将BitSet1与BitSet2进行按位与操作，结果存储在BitSet1中
# OR 1 2: 将BitSet1与BitSet2进行按位或操作，结果存储在BitSet1中
# XOR 1 2: 将BitSet1与BitSet2进行按位异或操作，结果存储在BitSet1中
# FLIP 1 2: 将BitSet1中下标为2的位翻转
# SET 1 2: 将BitSet1中下标为2的位设置为1

# 解题思路:
# 1. 使用整数的位来模拟BitSet
# 2. 根据操作类型执行相应的位运算操作
# 3. 每次操作后计算并打印两个BitSet中1的个数
# 时间复杂度分析:
# - AND, OR, XOR: O(n/32)
# - FLIP, SET: O(1)
# - count(): O(n/32)
# 空间复杂度: O(n)

class BitSet:
    def __init__(self, n):
        """
        构造函数，初始化大小为n的BitSet，所有位都为0
        :param n: BitSet的大小
        """
        # BitSet的大小
        self.size = n
        # 计算需要多少个整数来存储n位
        # (n + 31) // 32 是向上取整的写法
        # 例如：n=100，则需要(100+31)//32 = 4个整数来存储100位
        self.bits = [0] * ((n + 31) // 32)
    
    def and_op(self, other):
        """
        按位与操作
        :param other: 另一个BitSet对象
        """
        # 对每一位进行按位与操作
        for i in range(len(self.bits)):
            self.bits[i] &= other.bits[i]
    
    def or_op(self, other):
        """
        按位或操作
        :param other: 另一个BitSet对象
        """
        # 对每一位进行按位或操作
        for i in range(len(self.bits)):
            self.bits[i] |= other.bits[i]
    
    def xor_op(self, other):
        """
        按位异或操作
        :param other: 另一个BitSet对象
        """
        # 对每一位进行按位异或操作
        for i in range(len(self.bits)):
            self.bits[i] ^= other.bits[i]
    
    def flip(self, idx):
        """
        翻转指定位置的位
        :param idx: 位的下标
        """
        # 计算idx在数组中的位置和位偏移
        # array_idx确定在bits数组中的哪个整数
        array_idx = idx // 32
        # bit_idx确定在该整数中的哪一位
        bit_idx = idx % 32
        # 使用异或操作翻转指定位
        # 1 << bit_idx 创建一个只有第bit_idx位为1的数
        # ^= 异或操作，实现翻转效果
        self.bits[array_idx] ^= (1 << bit_idx)
    
    def set_bit(self, idx):
        """
        设置指定位置的位为1
        :param idx: 位的下标
        """
        # 计算idx在数组中的位置和位偏移
        # array_idx确定在bits数组中的哪个整数
        array_idx = idx // 32
        # bit_idx确定在该整数中的哪一位
        bit_idx = idx % 32
        # 使用按位或操作将指定位设置为1
        # 1 << bit_idx 创建一个只有第bit_idx位为1的数
        # |= 按位或操作，将指定位设置为1
        self.bits[array_idx] |= (1 << bit_idx)
    
    def count(self):
        """
        计算1的个数
        :return: 1的个数
        """
        result = 0
        # 遍历每一位所在的整数
        for bit in self.bits:
            # 计算一个整数中1的个数
            # bin(bit)将整数转换为二进制字符串
            # .count('1')统计字符串中'1'的个数
            result += bin(bit).count('1')
        return result

def main():
    """主函数，处理输入并输出结果"""
    # 读取n和m
    # n表示BitSet的大小，m表示操作的数量
    n, m = map(int, input().split())
    
    # 初始化两个BitSet
    # 创建一个BitSet列表，索引0不使用，1和2分别对应题目中的BitSet1和BitSet2
    bit_sets = [None, BitSet(n), BitSet(n)]
    
    # 执行m次操作
    # 循环处理每个操作
    for _ in range(m):
        # 读取操作指令
        line = input().split()
        # 获取操作类型
        operation = line[0]
        # 获取第一个操作数（BitSet编号）
        set1 = int(line[1])
        # 获取第二个操作数（BitSet编号或位索引）
        set2 = int(line[2])
        
        # 根据操作类型执行相应的操作
        if operation == "AND":
            # 将BitSet[set1]与BitSet[set2]进行按位与操作，结果存储在BitSet[set1]中
            # 按位与操作：两个位都为1时结果才为1，否则为0
            bit_sets[set1].and_op(bit_sets[set2])
        elif operation == "OR":
            # 将BitSet[set1]与BitSet[set2]进行按位或操作，结果存储在BitSet[set1]中
            # 按位或操作：两个位中至少有一个为1时结果为1，否则为0
            bit_sets[set1].or_op(bit_sets[set2])
        elif operation == "XOR":
            # 将BitSet[set1]与BitSet[set2]进行按位异或操作，结果存储在BitSet[set1]中
            # 按位异或操作：两个位不同时结果为1，相同时为0
            bit_sets[set1].xor_op(bit_sets[set2])
        elif operation == "FLIP":
            # 将BitSet[set1]中下标为set2的位翻转
            # 翻转操作：0变1，1变0
            bit_sets[set1].flip(set2)
        elif operation == "SET":
            # 将BitSet[set1]中下标为set2的位设置为1
            # 设置操作：将指定位置为1
            bit_sets[set1].set_bit(set2)
        
        # 打印两个BitSet中1的个数
        # 每次操作后都要输出两个BitSet中1的个数
        print(bit_sets[1].count(), bit_sets[2].count())

# 测试用例
def test():
    """测试用例，验证程序的正确性"""
    print("HackerRank Java BitSet 解题测试")
    
    # 创建两个大小为5的BitSet
    bs1 = BitSet(5)
    bs2 = BitSet(5)
    
    # 初始状态: bs1 = "00000", bs2 = "00000"
    # count()返回BitSet中1的个数
    print("Initial:", bs1.count(), bs2.count())  # 应该输出 "0 0"
    
    # SET 1 4 -> bs1 = "00001"
    # 将bs1中下标为4的位设置为1
    bs1.set_bit(4)
    print("After SET 1 4:", bs1.count(), bs2.count())  # 应该输出 "1 0"
    
    # FLIP 2 2 -> bs2 = "00100"
    # 将bs2中下标为2的位翻转（0变1）
    bs2.flip(2)
    print("After FLIP 2 2:", bs1.count(), bs2.count())  # 应该输出 "1 1"
    
    # OR 2 1 -> bs2 = "00101"
    # 将bs2与bs1进行按位或操作
    bs2.or_op(bs1)
    print("After OR 2 1:", bs1.count(), bs2.count())  # 应该输出 "1 2"

# 程序入口点
if __name__ == "__main__":
    # 运行测试用例
    test()
    
    # 如果需要运行主程序，取消下面的注释
    # main()