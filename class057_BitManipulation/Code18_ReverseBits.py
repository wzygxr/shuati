# 反转比特位
# 测试链接 : https://leetcode.cn/problems/reverse-bits/
'''
题目描述：
颠倒给定的 32 位无符号整数的二进制位。

示例：
输入：n = 00000010100101000001111010011100
输出：964176192 (00111001011110000010100101000000)
解释：输入的二进制串 00000010100101000001111010011100 表示无符号整数 43261596，
     因此返回 964176192，其二进制表示形式为 00111001011110000010100101000000。

输入：n = 11111111111111111111111111111101
输出：3221225471 (10111111111111111111111111111111)
解释：输入的二进制串 11111111111111111111111111111101 表示无符号整数 4294967293，
     因此返回 3221225471，其二进制表示形式为 10111111111111111111111111111111。

提示：
在Python中，整数没有固定的位数限制，但我们可以通过位运算模拟32位无符号整数的行为。

解题思路：
方法1：逐位反转
1. 初始化结果res = 0
2. 对于每一位（0到31），执行以下操作：
   a. 将res左移1位，为新位腾出位置
   b. 获取n的最低位并加到res中
   c. 将n右移1位，处理下一位

方法2：位运算分治
可以使用位运算分治法，通过多次交换相邻的1位、2位、4位、8位和16位来实现反转。

方法3：字符串处理（Python特色方法）
将整数转换为二进制字符串，反转后再转回整数。

时间复杂度：O(1) - 因为我们只处理固定的32位
空间复杂度：O(1) - 只使用了常数级别的额外空间
'''

class Solution:
    """
    反转比特位解决方案类
    提供多种反转32位无符号整数的方法
    """
    
    def reverseBits(self, n: int) -> int:
        """
        反转32位无符号整数的二进制位
        使用逐位反转方法
        
        Args:
            n: 输入的32位无符号整数
            
        Returns:
            反转后的32位无符号整数
        """
        res = 0
        # 处理每一位，从最低位到最高位
        for i in range(32):
            # 将结果左移一位，为新位腾出位置
            res <<= 1
            # 获取n的最低位并加到结果中
            res |= (n & 1)
            # 将n右移一位，处理下一位
            n >>= 1
        
        return res
    
    def reverseBits2(self, n: int) -> int:
        """
        反转32位无符号整数的二进制位
        使用位运算分治方法（更高效）
        
        Args:
            n: 输入的32位无符号整数
            
        Returns:
            反转后的32位无符号整数
        """
        # 确保n是32位无符号整数
        n &= 0xFFFFFFFF
        
        # 分治反转：交换相邻的位组
        # 交换每两位
        n = ((n >> 1) & 0x55555555) | ((n & 0x55555555) << 1)
        # 交换每四位中的两位组
        n = ((n >> 2) & 0x33333333) | ((n & 0x33333333) << 2)
        # 交换每八位中的四位组
        n = ((n >> 4) & 0x0F0F0F0F) | ((n & 0x0F0F0F0F) << 4)
        # 交换每16位中的八位组
        n = ((n >> 8) & 0x00FF00FF) | ((n & 0x00FF00FF) << 8)
        # 交换高16位和低16位
        n = (n >> 16) | (n << 16)
        
        # 确保返回的是32位无符号整数
        return n & 0xFFFFFFFF
    
    def reverseBits3(self, n: int) -> int:
        """
        反转32位无符号整数的二进制位
        使用Python字符串处理方法（简洁但可能效率较低）
        
        Args:
            n: 输入的32位无符号整数
            
        Returns:
            反转后的32位无符号整数
        """
        # 将整数转换为32位二进制字符串，填充前导零
        binary = format(n, '032b')
        # 反转字符串
        reversed_binary = binary[::-1]
        # 将反转后的二进制字符串转换回整数
        return int(reversed_binary, 2)

# 辅助函数：打印二进制表示

def print_binary(n: int):
    """
    打印32位无符号整数的二进制表示
    """
    binary = format(n & 0xFFFFFFFF, '032b')
    # 每4位添加一个空格，方便阅读
    formatted = ' '.join([binary[i:i+4] for i in range(0, 32, 4)])[::-1].replace(' ', '', 1)[::-1]
    print(formatted)

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 示例1: 43261596 (00000010100101000001111010011100)
    n1 = 43261596
    print("Test 1:")
    print(f"Input: {n1}")
    print("Binary: ")
    print_binary(n1)
    print(f"Output1: {solution.reverseBits(n1)}")
    print(f"Output2: {solution.reverseBits2(n1)}")
    print(f"Output3: {solution.reverseBits3(n1)}")
    print("Expected: 964176192")
    
    # 示例2: 4294967293 (11111111111111111111111111111101)
    n2 = 4294967293
    print("\nTest 2:")
    print(f"Input: {n2}")
    print("Binary: ")
    print_binary(n2)
    print(f"Output1: {solution.reverseBits(n2)}")
    print(f"Output2: {solution.reverseBits2(n2)}")
    print(f"Output3: {solution.reverseBits3(n2)}")
    print("Expected: 3221225471")
    
    # 额外测试
    n3 = 0  # 全0
    n4 = 0xFFFFFFFF  # 全1
    print("\nAdditional Tests:")
    print(f"Input: 0, Output: {solution.reverseBits(n3)}")
    print(f"Input: 0xFFFFFFFF, Output: {solution.reverseBits(n4)}")