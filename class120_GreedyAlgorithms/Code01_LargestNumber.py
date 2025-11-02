"""
最大数 - 贪心算法解决方案（Python实现）

题目描述：
给定一组非负整数nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数

测试链接：https://leetcode.cn/problems/largest-number/

算法思想：
使用贪心算法 + 自定义排序，关键是比较两个字符串a和b时，比较a+b和b+a的大小
如果a+b > b+a，则a应该排在b前面，这样拼接后的结果最大

时间复杂度分析：
O(n*logn*m) - 其中n是数组长度，m是数字的平均位数
- 排序需要O(n*logn)次比较
- 每次比较需要O(m)时间（字符串拼接和比较）

空间复杂度分析：
O(n*m) - 需要将整数转换为字符串存储

是否为最优解：
是，这是解决该问题的最优解

工程化考量：
1. 边界条件处理：处理全为0的特殊情况
2. 输入验证：检查输入参数的有效性
3. 异常处理：对非法输入进行检查
4. 可读性：使用清晰的变量命名和详细的注释

贪心策略证明：
对于任意两个数字a和b，如果a+b > b+a，则a应该排在b前面
这种排序方式满足传递性，因此可以得到全局最优解
"""

from functools import cmp_to_key
from typing import List

class Code01_LargestNumber:
    
    @staticmethod
    def largestNumber(nums: List[int]) -> str:
        """
        解决最大数问题的核心方法
        
        Args:
            nums: 非负整数数组
            
        Returns:
            重新排列后组成的最大整数（字符串形式）
            
        算法步骤：
        1. 将整数数组转换为字符串数组
        2. 使用自定义比较器对字符串数组进行排序
        3. 处理全为0的特殊情况
        4. 拼接排序后的字符串
        
        特殊处理：
        如果排序后的第一个字符串是"0"，说明所有数字都是0，直接返回"0"
        避免出现"000"这样的结果，应该返回"0"
        """
        # 输入验证
        if not nums:
            return "0"
        
        # 将整数转换为字符串
        strs = [str(num) for num in nums]
        
        # 自定义比较函数
        def compare(a: str, b: str) -> int:
            """
            自定义比较函数
            比较规则：比较a+b和b+a的大小
            如果a+b > b+a，则a应该排在b前面（返回-1表示a排在b前面）
            
            Args:
                a: 第一个字符串
                b: 第二个字符串
                
            Returns:
                比较结果：-1表示a排在b前面，1表示b排在a前面
            """
            if a + b > b + a:
                return -1  # a应该排在b前面
            else:
                return 1   # b应该排在a前面
        
        # 使用自定义比较器进行排序
        strs.sort(key=cmp_to_key(compare))
        
        # 处理全为0的特殊情况
        if strs[0] == "0":
            return "0"
        
        # 拼接所有字符串
        return ''.join(strs)
    
    @staticmethod
    def debug_largest_number(nums: List[int]) -> str:
        """
        调试版本：打印排序过程中的中间结果
        
        Args:
            nums: 整数数组
            
        Returns:
            最大数结果
        """
        if not nums:
            return "0"
        
        strs = [str(num) for num in nums]
        
        print("原始字符串数组:", strs)
        print("比较过程:")
        
        # 打印比较过程
        for i in range(len(strs)):
            for j in range(i + 1, len(strs)):
                ab = strs[i] + strs[j]
                ba = strs[j] + strs[i]
                print(f"比较 {strs[i]} 和 {strs[j]}: {ab} vs {ba} -> ", end="")
                if ab > ba:
                    print(f"{strs[i]} 应该在 {strs[j]} 前面")
                else:
                    print(f"{strs[j]} 应该在 {strs[i]} 前面")
        
        # 自定义比较函数
        def compare(a: str, b: str) -> int:
            if a + b > b + a:
                return -1
            else:
                return 1
        
        strs.sort(key=cmp_to_key(compare))
        print("排序后字符串数组:", strs)
        
        if strs[0] == "0":
            return "0"
        
        return ''.join(strs)
    
    @staticmethod
    def test_largest_number():
        """
        测试函数：验证最大数算法的正确性
        """
        print("最大数算法测试开始")
        print("==================")
        
        # 测试用例1: [10,2]
        nums1 = [10, 2]
        result1 = Code01_LargestNumber.largestNumber(nums1)
        print("输入: [10,2]")
        print("输出:", result1)
        print("预期: \"210\"")
        print("✓ 通过" if result1 == "210" else "✗ 失败")
        print()
        
        # 测试用例2: [3,30,34,5,9]
        nums2 = [3, 30, 34, 5, 9]
        result2 = Code01_LargestNumber.largestNumber(nums2)
        print("输入: [3,30,34,5,9]")
        print("输出:", result2)
        print("预期: \"9534330\"")
        print("✓ 通过" if result2 == "9534330" else "✗ 失败")
        print()
        
        # 测试用例3: [0,0,0] - 全为0的特殊情况
        nums3 = [0, 0, 0]
        result3 = Code01_LargestNumber.largestNumber(nums3)
        print("输入: [0,0,0]")
        print("输出:", result3)
        print("预期: \"0\"")
        print("✓ 通过" if result3 == "0" else "✗ 失败")
        print()
        
        # 测试用例4: [1] - 单个元素
        nums4 = [1]
        result4 = Code01_LargestNumber.largestNumber(nums4)
        print("输入: [1]")
        print("输出:", result4)
        print("预期: \"1\"")
        print("✓ 通过" if result4 == "1" else "✗ 失败")
        print()
        
        # 测试用例5: [432,43243] - 复杂比较
        nums5 = [432, 43243]
        result5 = Code01_LargestNumber.largestNumber(nums5)
        print("输入: [432,43243]")
        print("输出:", result5)
        print("预期: \"43243432\"")
        print("✓ 通过" if result5 == "43243432" else "✗ 失败")
        print()
        
        print("测试结束")
    
    @staticmethod
    def performance_test():
        """
        性能测试：测试算法在大规模数据下的表现
        """
        import time
        import random
        
        print("性能测试开始")
        print("============")
        
        # 生成大规模测试数据
        n = 10000
        nums = [random.randint(0, 1000000) for _ in range(n)]
        
        start_time = time.time()
        result = Code01_LargestNumber.largestNumber(nums)
        end_time = time.time()
        
        print(f"数据规模: {n}")
        print(f"执行时间: {end_time - start_time:.4f} 秒")
        print(f"结果长度: {len(result)}")
        print("性能测试结束")


def main():
    """
    主函数：运行测试
    """
    print("最大数 - 贪心算法解决方案（Python实现）")
    print("===================================")
    
    # 运行基础测试
    Code01_LargestNumber.test_largest_number()
    
    print("\n调试模式示例:")
    debug_nums = [3, 30, 34, 5, 9]
    print("对数组 [3,30,34,5,9] 进行调试跟踪:")
    debug_result = Code01_LargestNumber.debug_largest_number(debug_nums)
    print("最终结果:", debug_result)
    
    print("\n算法分析:")
    print("- 时间复杂度: O(n*logn*m) - 其中n是数组长度，m是数字的平均位数")
    print("- 空间复杂度: O(n*m) - 需要将整数转换为字符串存储")
    print("- 贪心策略: 比较a+b和b+a的大小，a+b大的排在前面")
    print("- 最优性: 这种贪心策略能够得到全局最优解")
    print("- Python特性: 使用functools.cmp_to_key实现自定义比较器")
    
    # 可选：运行性能测试（取消注释以运行）
    # print("\n性能测试:")
    # Code01_LargestNumber.performance_test()


if __name__ == "__main__":
    main()