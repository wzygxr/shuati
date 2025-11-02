# LeetCode 2166. Design Bitset
# 题目链接: https://leetcode.com/problems/design-bitset/
# 题目大意:
# 实现一个Bitset类，支持以下操作:
# 1. Bitset(int size): 用size个位初始化Bitset，所有位都是0
# 2. void fix(int idx): 将下标为idx的位更新为1
# 3. void unfix(int idx): 将下标为idx的位更新为0
# 4. void flip(): 翻转所有位的值
# 5. boolean all(): 检查所有位是否都是1
# 6. boolean one(): 检查是否至少有一位是1
# 7. int count(): 返回所有位中1的数量
# 8. String toString(): 返回所有位的状态

# 解题思路:
# 1. 使用整数的位来模拟bitset
# 2. 使用懒标记优化flip操作，避免每次都实际翻转所有位
# 3. 维护实际的1的个数，避免每次count都重新计算
# 时间复杂度分析:
# - fix, unfix: O(1)
# - flip: O(1)
# - all, one, count: O(1)
# - toString: O(size)
# 空间复杂度: O(size/32)

class Bitset:
    def __init__(self, size):
        """
        构造函数，初始化size个位，所有位都是0
        :param size: 位的总数
        """
        # 计算需要多少个整数来存储size位
        # (size + 31) // 32 是向上取整的写法
        # 例如：size=100，则需要(100+31)//32 = 4个整数来存储100位
        self.bits = [0] * ((size + 31) // 32)
        # 位的总数
        self.size = size
        # 当前1的个数，用于优化count操作
        self.ones = 0
        # 是否翻转的标记，用于优化flip操作
        # True表示逻辑状态与实际存储状态相反
        self.flipped = False
    
    def fix(self, idx):
        """
        将下标为idx的位更新为1
        :param idx: 位的下标
        """
        # 计算idx在数组中的位置和位偏移
        # array_idx确定在bits数组中的哪个整数
        array_idx = idx // 32
        # bit_idx确定在该整数中的哪一位
        bit_idx = idx % 32
        
        # 如果当前状态(考虑翻转)下该位是0，则设置为1
        if self.flipped:
            # 如果翻转了，实际的1在bits中是0
            # 检查该位是否为1（在逻辑上是0）
            if (self.bits[array_idx] & (1 << bit_idx)) != 0:
                # 该位实际是1，但在逻辑上是0，需要设置为1（即实际设置为0）
                # 使用异或操作将该位设置为0
                self.bits[array_idx] ^= (1 << bit_idx)
                # 1的个数增加1
                self.ones += 1
        else:
            # 如果没有翻转，实际的1在bits中是1
            # 检查该位是否为0
            if (self.bits[array_idx] & (1 << bit_idx)) == 0:
                # 该位实际是0，需要设置为1
                # 使用按位或操作将该位设置为1
                self.bits[array_idx] |= (1 << bit_idx)
                # 1的个数增加1
                self.ones += 1
    
    def unfix(self, idx):
        """
        将下标为idx的位更新为0
        :param idx: 位的下标
        """
        # 计算idx在数组中的位置和位偏移
        # array_idx确定在bits数组中的哪个整数
        array_idx = idx // 32
        # bit_idx确定在该整数中的哪一位
        bit_idx = idx % 32
        
        # 如果当前状态(考虑翻转)下该位是1，则设置为0
        if self.flipped:
            # 如果翻转了，实际的0在bits中是1
            # 检查该位是否为0（在逻辑上是1）
            if (self.bits[array_idx] & (1 << bit_idx)) == 0:
                # 该位实际是0，但在逻辑上是1，需要设置为0（即实际设置为1）
                # 使用按位或操作将该位设置为1
                self.bits[array_idx] |= (1 << bit_idx)
                # 1的个数减少1
                self.ones -= 1
        else:
            # 如果没有翻转，实际的0在bits中是0
            # 检查该位是否为1
            if (self.bits[array_idx] & (1 << bit_idx)) != 0:
                # 该位实际是1，需要设置为0
                # 使用异或操作将该位设置为0
                self.bits[array_idx] ^= (1 << bit_idx)
                # 1的个数减少1
                self.ones -= 1
    
    def flip(self):
        """翻转所有位的值"""
        # 切换翻转标记
        self.flipped = not self.flipped
        # 翻转后，1的个数变为总位数减去原来的1的个数
        # 这是基于数学原理：0变1，1变0，所以1的个数变为size-ones
        self.ones = self.size - self.ones
    
    def all(self):
        """
        检查所有位是否都是1
        :return: 如果所有位都是1返回True，否则返回False
        """
        # 所有位都是1当且仅当1的个数等于总位数
        return self.ones == self.size
    
    def one(self):
        """
        检查是否至少有一位是1
        :return: 如果至少有一位是1返回True，否则返回False
        """
        # 至少有一位是1当且仅当1的个数大于0
        return self.ones > 0
    
    def count(self):
        """
        返回所有位中1的数量
        :return: 1的数量
        """
        # 直接返回维护的1的个数，避免重新计算
        return self.ones
    
    def toString(self):
        """
        返回所有位的状态
        :return: 表示所有位状态的字符串
        """
        # 存储结果列表
        result = []
        # 遍历每一位
        for i in range(self.size):
            # 计算第i位在数组中的位置和位偏移
            array_idx = i // 32
            bit_idx = i % 32
            
            # 根据是否翻转来确定实际的位值
            if self.flipped:
                # 如果翻转了，实际的1在bits中是0
                # 检查bits中该位是否为1
                bit_value = 0 if (self.bits[array_idx] & (1 << bit_idx)) != 0 else 1
            else:
                # 如果没有翻转，实际的1在bits中是1
                # 检查bits中该位是否为1
                bit_value = 1 if (self.bits[array_idx] & (1 << bit_idx)) != 0 else 0
            
            # 将位值添加到结果列表中
            result.append(str(bit_value))
        
        # 将结果列表连接成字符串并返回
        return ''.join(result)

# 测试用例
# 验证Bitset类的正确性
if __name__ == "__main__":
    print("LeetCode 2166. Design Bitset 解题测试")
    
    # 创建一个5位的Bitset
    bs = Bitset(5)
    
    # 初始状态: "00000"
    print("Initial:", bs.toString())  # 应该输出 "00000"
    
    # fix(3) -> "00010"
    bs.fix(3)
    print("After fix(3):", bs.toString())  # 应该输出 "00010"
    
    # fix(1) -> "01010"
    bs.fix(1)
    print("After fix(1):", bs.toString())  # 应该输出 "01010"
    
    # flip() -> "10101"
    bs.flip()
    print("After flip():", bs.toString())  # 应该输出 "10101"
    
    # all() -> false
    print("all():", bs.all())  # 应该输出 False
    
    # unfix(0) -> "00101"
    bs.unfix(0)
    print("After unfix(0):", bs.toString())  # 应该输出 "00101"
    
    # flip() -> "11010"
    bs.flip()
    print("After flip():", bs.toString())  # 应该输出 "11010"
    
    # one() -> true
    print("one():", bs.one())  # 应该输出 True
    
    # fix(3) -> "11010" (已经是1了，无变化)
    bs.fix(3)
    print("After fix(3):", bs.toString())  # 应该输出 "11010"
    
    # count() -> 4
    print("count():", bs.count())  # 应该输出 4
    
    # toString() -> "11010"
    print("toString():", bs.toString())  # 应该输出 "11010"