"""
数字范围按位与
测试链接：https://leetcode.cn/problems/bitwise-and-of-numbers-range/

题目描述：
给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字按位与的结果（包含 left 、right 端点）。

解题思路：
1. 暴力法：遍历区间内所有数字进行按位与操作
2. 位运算技巧：找到left和right的公共前缀
3. 位移法：不断右移直到left等于right，然后左移恢复
4. Brian Kernighan算法：利用n & (n-1)消除最低位的1

时间复杂度：O(1) - 最多32次操作
空间复杂度：O(1) - 只使用常数个变量
"""

class Code30_BitwiseANDofNumbersRange:
    """
    数字范围按位与解决方案
    """
    
    @staticmethod
    def range_bitwise_and1(left: int, right: int) -> int:
        """
        方法1：暴力法（不推荐，会超时）
        时间复杂度：O(right - left)
        空间复杂度：O(1)
        
        Args:
            left: 区间左端点
            right: 区间右端点
            
        Returns:
            区间内所有数字按位与的结果
        """
        result = left
        for i in range(left + 1, right + 1):
            result &= i
            if result == 0:
                break  # 提前终止优化
        return result
    
    @staticmethod
    def range_bitwise_and2(left: int, right: int) -> int:
        """
        方法2：位运算技巧（最优解）
        找到left和right的公共前缀
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            left: 区间左端点
            right: 区间右端点
            
        Returns:
            区间内所有数字按位与的结果
        """
        shift = 0
        # 找到公共前缀
        while left < right:
            left >>= 1
            right >>= 1
            shift += 1
        return left << shift
    
    @staticmethod
    def range_bitwise_and3(left: int, right: int) -> int:
        """
        方法3：Brian Kernighan算法
        利用n & (n-1)消除最低位的1
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            left: 区间左端点
            right: 区间右端点
            
        Returns:
            区间内所有数字按位与的结果
        """
        while left < right:
            # 消除right最低位的1
            right = right & (right - 1)
        return right
    
    @staticmethod
    def range_bitwise_and4(left: int, right: int) -> int:
        """
        方法4：位移法的另一种实现
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            left: 区间左端点
            right: 区间右端点
            
        Returns:
            区间内所有数字按位与的结果
        """
        if left == right:
            return left
        
        # 计算left和right的最高不同位
        xor_val = left ^ right
        mask = 1 << 31  # 从最高位开始找
        
        # 找到最高不同位
        while mask > 0 and (xor_val & mask) == 0:
            mask >>= 1
        
        # 创建掩码，将不同位及之后的位都置为0
        mask = (mask << 1) - 1
        mask = ~mask
        
        return left & mask
    
    @staticmethod
    def range_bitwise_and5(left: int, right: int) -> int:
        """
        方法5：利用位长度
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        Args:
            left: 区间左端点
            right: 区间右端点
            
        Returns:
            区间内所有数字按位与的结果
        """
        if left == right:
            return left
        
        # 计算位长度
        left_bits = left.bit_length()
        right_bits = right.bit_length()
        
        # 如果位长度不同，结果为0
        if left_bits != right_bits:
            return 0
        
        # 找到公共前缀
        common_prefix = left
        for i in range(left + 1, right + 1):
            common_prefix &= i
            if common_prefix == 0:
                break
        
        return common_prefix
    
    @staticmethod
    def test():
        """测试方法"""
        # 测试用例1：正常情况
        left1, right1 = 5, 7
        result1 = Code30_BitwiseANDofNumbersRange.range_bitwise_and1(left1, right1)
        result2 = Code30_BitwiseANDofNumbersRange.range_bitwise_and2(left1, right1)
        result3 = Code30_BitwiseANDofNumbersRange.range_bitwise_and3(left1, right1)
        result4 = Code30_BitwiseANDofNumbersRange.range_bitwise_and4(left1, right1)
        result5 = Code30_BitwiseANDofNumbersRange.range_bitwise_and5(left1, right1)
        print(f"测试用例1 - 输入: [{left1}, {right1}]")
        print(f"方法1结果: {result1} (预期: 4)")
        print(f"方法2结果: {result2} (预期: 4)")
        print(f"方法3结果: {result3} (预期: 4)")
        print(f"方法4结果: {result4} (预期: 4)")
        print(f"方法5结果: {result5} (预期: 4)")
        
        # 测试用例2：边界情况（相同数字）
        left2, right2 = 10, 10
        result6 = Code30_BitwiseANDofNumbersRange.range_bitwise_and2(left2, right2)
        print(f"测试用例2 - 输入: [{left2}, {right2}]")
        print(f"方法2结果: {result6} (预期: 10)")
        
        # 测试用例3：大范围情况
        left3, right3 = 1, 2**31 - 1
        result7 = Code30_BitwiseANDofNumbersRange.range_bitwise_and2(left3, right3)
        print(f"测试用例3 - 输入: [{left3}, {right3}]")
        print(f"方法2结果: {result7} (预期: 0)")
        
        # 测试用例4：包含2的幂
        left4, right4 = 8, 15
        result8 = Code30_BitwiseANDofNumbersRange.range_bitwise_and2(left4, right4)
        print(f"测试用例4 - 输入: [{left4}, {right4}]")
        print(f"方法2结果: {result8} (预期: 8)")
        
        # 复杂度分析
        print("\n=== 复杂度分析 ===")
        print("方法1 - 暴力法:")
        print("  时间复杂度: O(n) - n = right - left")
        print("  空间复杂度: O(1)")
        
        print("方法2 - 位运算技巧:")
        print("  时间复杂度: O(1) - 最多32次位移")
        print("  空间复杂度: O(1)")
        
        print("方法3 - Brian Kernighan算法:")
        print("  时间复杂度: O(1) - 最多32次操作")
        print("  空间复杂度: O(1)")
        
        print("方法4 - 位移法变种:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(1)")
        
        print("方法5 - 位长度法:")
        print("  时间复杂度: O(1)")
        print("  空间复杂度: O(1)")
        
        # 工程化考量
        print("\n=== 工程化考量 ===")
        print("1. 方法选择：")
        print("   - 实际工程：方法2（位运算技巧）最优")
        print("   - 面试场景：方法2（位运算技巧）最优")
        print("2. 性能优化：避免暴力法，使用位运算")
        print("3. 边界处理：处理left等于right的情况")
        print("4. 可读性：方法2代码简洁易懂")
        
        # Python特性考量
        print("\n=== Python特性考量 ===")
        print("1. 位长度：使用bit_length()方法获取位数")
        print("2. 大整数：Python支持任意大整数")
        print("3. 位操作：Python支持标准的位运算符")
        print("4. 范围操作：使用range进行迭代")
        
        # 算法技巧总结
        print("\n=== 算法技巧总结 ===")
        print("1. 公共前缀：区间按位与的结果就是公共前缀")
        print("2. 位移操作：通过右移找到公共前缀")
        print("3. Brian Kernighan技巧：消除最低位的1")
        print("4. 最高位掩码：使用位运算创建掩码")
        print("5. 提前终止：当结果为0时可以提前终止")

if __name__ == "__main__":
    Code30_BitwiseANDofNumbersRange.test()