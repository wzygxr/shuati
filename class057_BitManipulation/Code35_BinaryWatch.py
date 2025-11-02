"""
二进制手表
测试链接：https://leetcode.cn/problems/binary-watch/

题目描述：
二进制手表顶部有 4 个 LED 代表 小时（0-11），底部的 6 个 LED 代表 分钟（0-59）。
每个 LED 代表一个 0 或 1，最低位在右侧。
给你一个整数 turnedOn ，表示当前亮着的 LED 的数量，返回所有可能的时间。
你可以按任意顺序返回答案。

解题思路：
1. 枚举法：枚举所有可能的小时和分钟组合
2. 位运算法：利用bin()函数计算亮灯数量
3. 回溯法：递归选择亮灯的位置

时间复杂度分析：
- 枚举法：O(12 * 60) = O(720)，常数时间
- 位运算法：O(2^10) = O(1024)，常数时间

空间复杂度分析：
- 枚举法：O(n)，n为结果数量
- 位运算法：O(n)，n为结果数量
"""

class Solution:
    def readBinaryWatch1(self, turnedOn: int) -> list:
        """
        方法1：枚举法（推荐）
        枚举所有可能的小时和分钟，检查亮灯数量是否匹配
        时间复杂度：O(12 * 60) = O(720)
        空间复杂度：O(n)，n为结果数量
        """
        result = []
        
        # 枚举所有可能的小时（0-11）和分钟（0-59）
        for hour in range(12):
            for minute in range(60):
                # 计算小时和分钟的二进制中1的个数
                if bin(hour).count('1') + bin(minute).count('1') == turnedOn:
                    result.append(f"{hour}:{minute:02d}")
        
        return result
    
    def readBinaryWatch2(self, turnedOn: int) -> list:
        """
        方法2：位运算法
        枚举所有10位二进制数（4位小时 + 6位分钟）
        时间复杂度：O(2^10) = O(1024)
        空间复杂度：O(n)，n为结果数量
        """
        result = []
        
        # 枚举所有10位二进制数（0-1023）
        for i in range(1024):
            # 高4位表示小时，低6位表示分钟
            hour = i >> 6
            minute = i & 0x3F  # 0x3F = 63，取低6位
            
            # 检查小时和分钟是否在有效范围内
            if hour < 12 and minute < 60 and bin(i).count('1') == turnedOn:
                result.append(f"{hour}:{minute:02d}")
        
        return result
    
    def readBinaryWatch3(self, turnedOn: int) -> list:
        """
        方法3：使用位运算优化
        利用位运算特性，避免字符串操作
        时间复杂度：O(12 * 60) = O(720)
        空间复杂度：O(n)，n为结果数量
        """
        result = []
        
        for hour in range(12):
            for minute in range(60):
                # 使用位运算计算1的个数
                if self.countBits(hour) + self.countBits(minute) == turnedOn:
                    result.append(f"{hour}:{minute:02d}")
        
        return result
    
    def countBits(self, n: int) -> int:
        """
        计算整数二进制表示中1的个数
        使用Brian Kernighan算法
        时间复杂度：O(k)，k为1的个数
        """
        count = 0
        while n:
            n &= n - 1  # 清除最低位的1
            count += 1
        return count
    
    def readBinaryWatch4(self, turnedOn: int) -> list:
        """
        方法4：回溯法
        递归选择亮灯的位置
        时间复杂度：O(C(10, turnedOn))，组合数
        空间复杂度：O(n)，递归深度和结果数量
        """
        result = []
        if turnedOn < 0 or turnedOn > 10:
            return result
        
        # 回溯选择亮灯位置
        self.backtrack(result, 0, 0, turnedOn, 0)
        return result
    
    def backtrack(self, result: list, hour: int, minute: int, 
                  remaining: int, start: int):
        """
        回溯函数
        """
        # 如果剩余亮灯数为0，检查是否有效
        if remaining == 0:
            if hour < 12 and minute < 60:
                result.append(f"{hour}:{minute:02d}")
            return
        
        # 从start位置开始选择亮灯
        for i in range(start, 10):
            new_hour = hour
            new_minute = minute
            
            # 根据位置设置小时或分钟
            if i < 4:  # 小时位（0-3）
                new_hour = hour | (1 << (3 - i))
            else:  # 分钟位（4-9）
                new_minute = minute | (1 << (9 - i))
            
            self.backtrack(result, new_hour, new_minute, remaining - 1, i + 1)

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：亮1盏灯
    result1 = solution.readBinaryWatch1(1)
    print(f"测试用例1 - turnedOn = 1")
    print(f"结果数量: {len(result1)}")
    print(f"前5个结果: {result1[:5]}")
    
    # 测试用例2：亮2盏灯
    result2 = solution.readBinaryWatch1(2)
    print(f"测试用例2 - turnedOn = 2")
    print(f"结果数量: {len(result2)}")
    
    # 测试用例3：亮9盏灯（应该为空）
    result3 = solution.readBinaryWatch1(9)
    print(f"测试用例3 - turnedOn = 9")
    print(f"结果数量: {len(result3)}")
    print(f"结果: {result3}")
    
    # 测试用例4：亮0盏灯
    result4 = solution.readBinaryWatch1(0)
    print(f"测试用例4 - turnedOn = 0")
    print(f"结果数量: {len(result4)}")
    print(f"结果: {result4}")
    
    # 性能比较
    import time
    start_time = time.time()
    result5 = solution.readBinaryWatch1(3)
    end_time = time.time()
    print(f"方法1性能 - 耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    start_time = time.time()
    result6 = solution.readBinaryWatch2(3)
    end_time = time.time()
    print(f"方法2性能 - 耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 复杂度分析
    print("\n=== 复杂度分析 ===")
    print("方法1 - 枚举法:")
    print("  时间复杂度: O(12 * 60) = O(720)")
    print("  空间复杂度: O(n)，n为结果数量")
    
    print("方法2 - 位运算法:")
    print("  时间复杂度: O(2^10) = O(1024)")
    print("  空间复杂度: O(n)，n为结果数量")
    
    print("方法3 - 位运算优化版:")
    print("  时间复杂度: O(12 * 60) = O(720)")
    print("  空间复杂度: O(n)，n为结果数量")
    
    print("方法4 - 回溯法:")
    print("  时间复杂度: O(C(10, turnedOn))")
    print("  空间复杂度: O(n)，递归深度和结果数量")
    
    # 工程化考量
    print("\n=== 工程化考量 ===")
    print("1. 算法选择：方法1最简单实用")
    print("2. 边界处理：检查turnedOn范围（0-10）")
    print("3. 性能优化：720次枚举足够快")
    print("4. 可读性：方法1逻辑清晰易懂")
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. bin().count('1'): 计算二进制中1的个数")
    print("2. 位运算：>> 和 & 操作提取高低位")
    print("3. 枚举法：当数据范围较小时，直接枚举所有可能")
    print("4. 格式化输出：f-string确保时间格式正确")
    
    # Brian Kernighan算法演示
    print("\n=== Brian Kernighan算法演示 ===")
    test_num = 23  # 二进制: 10111
    count = solution.countBits(test_num)
    print(f"数字 {test_num} 的二进制中1的个数: {count}")
    print(f"验证: bin({test_num}) = {bin(test_num)}, 1的个数: {bin(test_num).count('1')}")

if __name__ == "__main__":
    test_solution()