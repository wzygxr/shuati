"""
UTF-8 编码验证
测试链接：https://leetcode.cn/problems/utf-8-validation/

题目描述：
给定一个表示数据的整数数组 data，返回它是否为有效的 UTF-8 编码。
UTF-8 中的一个字符可能的长度为 1 到 4 字节，遵循以下规则：
1. 对于 1 字节的字符，字节的第一位设为 0，后面 7 位为这个符号的 Unicode 码。
2. 对于 n 字节的字符 (n > 1)，第一个字节的前 n 位都设为 1，第 n+1 位设为 0，
   后面字节的前两位一律设为 10。

解题思路：
1. 逐字节验证：按UTF-8编码规则逐个字节验证
2. 状态机：使用状态机跟踪当前字符的字节数
3. 位运算：使用位掩码检查字节格式

时间复杂度分析：
- 所有方法：O(n)，n为数组长度

空间复杂度分析：
- 所有方法：O(1)，只使用常数空间
"""

class Solution:
    def validUtf8_1(self, data: list[int]) -> bool:
        """
        方法1：逐字节验证（推荐）
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        n = len(data)
        i = 0
        
        while i < n:
            # 获取当前字节
            current = data[i]
            
            # 判断当前字节的类型
            byte_type = self.get_byte_type(current)
            
            # 检查类型是否有效
            if byte_type == -1:
                return False
            
            # 检查后续字节数量是否足够
            if i + byte_type > n:
                return False
            
            # 验证后续字节（如果是多字节字符）
            for j in range(1, byte_type):
                if not self.is_continuation_byte(data[i + j]):
                    return False
            
            i += byte_type
        
        return True
    
    def validUtf8_2(self, data: list[int]) -> bool:
        """
        方法2：状态机实现
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        expected_bytes = 0  # 期望的后续字节数
        
        for current in data:
            if expected_bytes == 0:
                # 新的字符开始
                if (current & 0x80) == 0:
                    # 1字节字符：0xxxxxxx
                    expected_bytes = 0
                elif (current & 0xE0) == 0xC0:
                    # 2字节字符：110xxxxx
                    expected_bytes = 1
                elif (current & 0xF0) == 0xE0:
                    # 3字节字符：1110xxxx
                    expected_bytes = 2
                elif (current & 0xF8) == 0xF0:
                    # 4字节字符：11110xxx
                    expected_bytes = 3
                else:
                    return False  # 无效的首字节
            else:
                # 检查后续字节格式：10xxxxxx
                if (current & 0xC0) != 0x80:
                    return False
                expected_bytes -= 1
        
        return expected_bytes == 0  # 所有字符必须完整
    
    def validUtf8_3(self, data: list[int]) -> bool:
        """
        方法3：位掩码优化版
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        count = 0  # 剩余需要验证的后续字节数
        
        for num in data:
            if count == 0:
                if (num >> 5) == 0b110:
                    count = 1
                elif (num >> 4) == 0b1110:
                    count = 2
                elif (num >> 3) == 0b11110:
                    count = 3
                elif (num >> 7) != 0:
                    return False  # 无效的首字节
            else:
                if (num >> 6) != 0b10:
                    return False
                count -= 1
        
        return count == 0
    
    def validUtf8_4(self, data: list[int]) -> bool:
        """
        方法4：详细的位运算验证
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        index = 0
        n = len(data)
        
        while index < n:
            first_byte = data[index]
            
            # 检查1字节字符
            if (first_byte & 0x80) == 0:
                index += 1
                continue
            
            # 检查多字节字符
            byte_count = self.get_byte_count(first_byte)
            if byte_count == -1 or index + byte_count > n:
                return False
            
            # 验证后续字节
            for i in range(1, byte_count):
                if not self.is_valid_continuation(data[index + i]):
                    return False
            
            index += byte_count
        
        return True
    
    # ========== 辅助方法 ==========
    
    def get_byte_type(self, b: int) -> int:
        """获取字节类型（字符的字节数）"""
        if (b & 0x80) == 0:
            return 1        # 0xxxxxxx
        elif (b & 0xE0) == 0xC0:
            return 2        # 110xxxxx
        elif (b & 0xF0) == 0xE0:
            return 3        # 1110xxxx
        elif (b & 0xF8) == 0xF0:
            return 4        # 11110xxx
        else:
            return -1      # 无效字节
    
    def is_continuation_byte(self, b: int) -> bool:
        """检查是否为有效的后续字节"""
        return (b & 0xC0) == 0x80  # 10xxxxxx
    
    def get_byte_count(self, first_byte: int) -> int:
        """获取字符字节数"""
        if (first_byte & 0x80) == 0:
            return 1
        elif (first_byte & 0xE0) == 0xC0:
            return 2
        elif (first_byte & 0xF0) == 0xE0:
            return 3
        elif (first_byte & 0xF8) == 0xF0:
            return 4
        else:
            return -1
    
    def is_valid_continuation(self, b: int) -> bool:
        """检查是否为有效的后续字节"""
        return (b & 0xC0) == 0x80

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：有效UTF-8编码
    data1 = [197, 130, 1]  # 2字节字符 + 1字节字符
    result1 = solution.validUtf8_1(data1)
    print(f"测试用例1 - 输入: {data1}")
    print(f"结果: {result1} (预期: True)")
    
    # 测试用例2：无效UTF-8编码
    data2 = [235, 140, 4]  # 3字节字符但第二个字节无效
    result2 = solution.validUtf8_1(data2)
    print(f"测试用例2 - 输入: {data2}")
    print(f"结果: {result2} (预期: False)")
    
    # 测试用例3：单字节字符
    data3 = [65, 66, 67]  # ASCII字符
    result3 = solution.validUtf8_1(data3)
    print(f"测试用例3 - 输入: {data3}")
    print(f"结果: {result3} (预期: True)")
    
    # 测试用例4：混合字符
    data4 = [227, 129, 130, 65]  # 3字节字符 + ASCII字符
    result4 = solution.validUtf8_1(data4)
    print(f"测试用例4 - 输入: {data4}")
    print(f"结果: {result4} (预期: True)")
    
    # 测试用例5：不完整的字符
    data5 = [240, 162, 130]  # 4字节字符但缺少最后一个字节
    result5 = solution.validUtf8_1(data5)
    print(f"测试用例5 - 输入: {data5}")
    print(f"结果: {result5} (预期: False)")
    
    # 性能测试
    import time
    large_data = [65] * 10000  # 全部是ASCII字符
    
    start_time = time.time()
    perf_result = solution.validUtf8_1(large_data)
    end_time = time.time()
    print(f"性能测试 - 输入长度: {len(large_data)}")
    print(f"结果: {perf_result}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("所有方法:")
    print("  时间复杂度: O(n) - 遍历数组一次")
    print("  空间复杂度: O(1) - 只使用常数空间")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 边界处理：检查数组长度和字节范围")
    print("2. 性能优化：使用位运算提高效率")
    print("3. 可读性：清晰的变量命名和注释")
    print("4. 错误处理：详细的错误信息（实际工程中）")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 位掩码：使用位掩码检查字节格式")
    print("2. 状态机：跟踪当前字符的字节数")
    print("3. 提前终止：发现无效字节立即返回")
    print("4. 边界检查：确保不越界访问数组")
    
    # UTF-8编码规则总结
    print("\n=== UTF-8编码规则 ===")
    print("1字节: 0xxxxxxx")
    print("2字节: 110xxxxx 10xxxxxx")
    print("3字节: 1110xxxx 10xxxxxx 10xxxxxx")
    print("4字节: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx")
    
    # 位运算演示
    print("\n=== 位运算演示 ===")
    test_byte = 197  # 二进制: 11000101
    print(f"字节 {test_byte} 的二进制: {bin(test_byte)}")
    print(f"检查是否为2字节字符: {(test_byte & 0xE0) == 0xC0}")

if __name__ == "__main__":
    test_solution()