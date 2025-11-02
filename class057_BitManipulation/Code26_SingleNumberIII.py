"""
只出现一次的数字 III
测试链接：https://leetcode.cn/problems/single-number-iii/

题目描述：
给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。
找出只出现一次的那两个元素。你可以按任意顺序返回答案。

解题思路：
1. 首先对所有数字进行异或操作，得到两个不同数字的异或结果
2. 找到异或结果中最低位的1，这个位置表示两个数字在该位不同
3. 根据这个位将数组分成两组，分别进行异或操作得到两个结果

时间复杂度：O(n) - 遍历数组两次
空间复杂度：O(1) - 只使用常数个变量
"""
from typing import List

class Code26_SingleNumberIII:
    """
    只出现一次的数字 III 解决方案
    """
    
    @staticmethod
    def single_number(nums: List[int]) -> List[int]:
        """
        找出只出现一次的两个数字
        
        Args:
            nums: 整数数组
            
        Returns:
            只出现一次的两个数字
            
        Raises:
            ValueError: 如果数组长度小于2
        """
        if len(nums) < 2:
            raise ValueError("数组长度必须至少为2")
        
        # 第一步：计算所有数字的异或结果
        xor_result = 0
        for num in nums:
            xor_result ^= num
        
        # 第二步：找到异或结果中最低位的1
        # 技巧：xor_result & (-xor_result) 可以快速找到最低位的1
        # 注意：Python中负数使用补码表示，需要特殊处理
        lowest_one_bit = xor_result & (-xor_result)
        
        # 第三步：根据最低位的1将数组分成两组
        result = [0, 0]
        for num in nums:
            # 根据该位是否为0进行分组
            if (num & lowest_one_bit) == 0:
                result[0] ^= num  # 第一组
            else:
                result[1] ^= num  # 第二组
        
        return result
    
    @staticmethod
    def test():
        """测试方法"""
        # 测试用例1：正常情况
        nums1 = [1, 2, 1, 3, 2, 5]
        result1 = Code26_SingleNumberIII.single_number(nums1)
        print(f"测试用例1结果: {result1}")
        # 预期输出: [3, 5] 或 [5, 3]
        
        # 测试用例2：包含负数
        nums2 = [-1, 0, -1, 2, 0, 3]
        result2 = Code26_SingleNumberIII.single_number(nums2)
        print(f"测试用例2结果: {result2}")
        # 预期输出: [2, 3] 或 [3, 2]
        
        # 测试用例3：边界情况
        nums3 = [0, 1]
        result3 = Code26_SingleNumberIII.single_number(nums3)
        print(f"测试用例3结果: {result3}")
        # 预期输出: [0, 1] 或 [1, 0]
        
        # 复杂度分析
        print("\n=== 复杂度分析 ===")
        print("时间复杂度: O(n) - 遍历数组两次")
        print("空间复杂度: O(1) - 只使用常数个变量")
        
        # 工程化考量
        print("\n=== 工程化考量 ===")
        print("1. 输入验证：检查数组长度")
        print("2. 边界处理：处理负数情况")
        print("3. 性能优化：使用位运算替代算术运算")
        print("4. 可读性：添加详细注释说明算法原理")
        print("5. 异常处理：使用异常处理输入错误")
        
        # Python特性考量
        print("\n=== Python特性考量 ===")
        print("1. 类型注解：使用typing模块提供类型提示")
        print("2. 静态方法：使用@staticmethod装饰器")
        print("3. 负数处理：Python使用补码表示负数，位运算需要特别注意")

if __name__ == "__main__":
    Code26_SingleNumberIII.test()