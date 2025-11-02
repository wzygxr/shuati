# 汉明距离
# 测试链接 : https://leetcode.cn/problems/hamming-distance/
'''
题目描述：
两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
给你两个整数 x 和 y，计算并返回它们之间的汉明距离。

示例：
输入：x = 1, y = 4
输出：2
解释：
1   (0 0 0 1)
4   (0 1 0 0)
       ↑   ↑
上面的箭头指出了对应二进制位不同的位置。

输入：x = 3, y = 1
输出：1

提示：
0 <= x, y <= 2^31 - 1

解题思路：
1. 首先对两个数字进行异或运算，这样不同的位就会被设置为1
2. 然后统计异或结果中1的个数，这个个数就是汉明距离

计算二进制中1的个数可以使用多种方法：
- 逐位检查（最直接的方法）
- Brian Kernighan算法：利用n & (n - 1)移除最右边的1，直到n变为0
- Python内置函数bin()和count()（最简洁的方法）

时间复杂度：O(1) - 因为整数的位数是固定的（32位）
空间复杂度：O(1) - 只使用了常数级别的额外空间
'''

class Solution:
    """
    汉明距离解决方案类
    提供多种计算汉明距离的方法
    """
    
    def hammingDistance(self, x: int, y: int) -> int:
        """
        计算两个整数之间的汉明距离
        
        Args:
            x: 第一个整数
            y: 第二个整数
            
        Returns:
            两个整数之间的汉明距离
        """
        # 方法1：使用Python内置函数（最简洁的方法）
        # 使用bin()函数将异或结果转换为二进制字符串，然后统计'1'的个数
        return bin(x ^ y).count('1')
    
    def hammingDistance2(self, x: int, y: int) -> int:
        """
        计算两个整数之间的汉明距离（使用Brian Kernighan算法）
        
        Args:
            x: 第一个整数
            y: 第二个整数
            
        Returns:
            两个整数之间的汉明距离
        """
        # 对两个数字进行异或运算
        xor_result = x ^ y
        distance = 0
        
        # Brian Kernighan算法：每次移除最右边的1，直到结果为0
        while xor_result != 0:
            distance += 1
            # 移除最右边的1
            xor_result &= xor_result - 1
        
        return distance
    
    def hammingDistance3(self, x: int, y: int) -> int:
        """
        计算两个整数之间的汉明距离（逐位检查）
        
        Args:
            x: 第一个整数
            y: 第二个整数
            
        Returns:
            两个整数之间的汉明距离
        """
        distance = 0
        # 对两个数字进行异或运算
        xor_result = x ^ y
        
        # 逐位检查，最多检查32位（因为输入限制在32位整数范围内）
        for _ in range(32):
            # 检查最低位是否为1
            distance += xor_result & 1
            # 右移一位
            xor_result >>= 1
        
        return distance

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试方法1
    print("Test 1: ", solution.hammingDistance(1, 4))   # 输出: 2
    print("Test 2: ", solution.hammingDistance(3, 1))   # 输出: 1
    print("Test 3: ", solution.hammingDistance(0, 0))   # 输出: 0
    print("Test 4: ", solution.hammingDistance(2147483647, 0)) # 输出: 31
    
    # 测试方法2
    print("\nUsing method 2:")
    print("Test 1: ", solution.hammingDistance2(1, 4))   # 输出: 2
    print("Test 2: ", solution.hammingDistance2(3, 1))   # 输出: 1
    
    # 测试方法3
    print("\nUsing method 3:")
    print("Test 1: ", solution.hammingDistance3(1, 4))   # 输出: 2
    print("Test 2: ", solution.hammingDistance3(3, 1))   # 输出: 1