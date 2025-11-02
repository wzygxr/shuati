from typing import List

# 最大数
# 给定一组非负整数nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
# 测试链接 : https://leetcode.cn/problems/largest-number/

class Solution:
    def largestNumber(self, nums: List[int]) -> str:
        """
        最大数问题
        
        算法思路：
        使用贪心策略结合自定义排序规则：
        1. 将所有数字转换为字符串
        2. 自定义比较器：对于两个字符串a和b，比较a+b和b+a的大小
        3. 如果a+b > b+a，则a应该排在b前面
        4. 排序后拼接所有字符串
        5. 处理特殊情况：如果排序后的第一个元素是"0"，则结果只能是"0"
        
        正确性分析：
        1. 排序规则保证了对于任意两个数字的相对顺序是最优的
        2. 通过传递性可以证明整个排序后的数组拼接起来是最大的
        3. 特殊情况处理确保了当所有数字都是0时不会返回多个0
        
        时间复杂度：O(n*logn) - 主要是排序的时间复杂度，排序中比较两个字符串的时间是O(k)，k是字符串长度，但可以视为常数
        空间复杂度：O(n) - 需要额外的字符串数组来存储转换后的数字
        
        Args:
            nums: 非负整数数组
            
        Returns:
            拼接后的最大整数的字符串表示
        """
        # 边界检查
        if not nums:
            return "0"
        
        # 将整数转换为字符串
        strs = list(map(str, nums))
        
        # 自定义排序：比较a+b和b+a哪个更大
        # Python的sorted函数可以接受自定义key参数
        # 这里使用自定义的比较函数，通过functools.cmp_to_key转换为key函数
        import functools
        
        def compare(a, b):
            # 如果b+a > a+b，则b应该排在a前面
            if b + a > a + b:
                return 1
            else:
                return -1
        
        # 排序
        strs.sort(key=functools.cmp_to_key(compare))
        
        # 特殊情况：如果排序后的第一个数是0，则说明所有数都是0
        if strs[0] == "0":
            return "0"
        
        # 拼接所有字符串
        result = ''.join(strs)
        
        return result

# 测试函数
def test():
    solution = Solution()
    
    # 测试用例1: nums = [10,2] -> 输出: "210"
    nums1 = [10, 2]
    print("测试用例1:")
    print(f"输入数组: {nums1}")
    print(f"最大数: {solution.largestNumber(nums1)}")  # 期望输出: "210"
    
    # 测试用例2: nums = [3,30,34,5,9] -> 输出: "9534330"
    nums2 = [3, 30, 34, 5, 9]
    print("\n测试用例2:")
    print(f"输入数组: {nums2}")
    print(f"最大数: {solution.largestNumber(nums2)}")  # 期望输出: "9534330"
    
    # 测试用例3: nums = [0,0] -> 输出: "0"
    nums3 = [0, 0]
    print("\n测试用例3:")
    print(f"输入数组: {nums3}")
    print(f"最大数: {solution.largestNumber(nums3)}")  # 期望输出: "0"
    
    # 测试用例4: nums = [1] -> 输出: "1"
    nums4 = [1]
    print("\n测试用例4:")
    print(f"输入数组: {nums4}")
    print(f"最大数: {solution.largestNumber(nums4)}")  # 期望输出: "1"
    
    # 测试用例5: nums = [1000000000, 1000000001] -> 输出: "1000000001100000000"
    nums5 = [1000000000, 1000000001]
    print("\n测试用例5:")
    print(f"输入数组: {nums5}")
    print(f"最大数: {solution.largestNumber(nums5)}")  # 期望输出: "1000000001100000000"

# 运行测试
if __name__ == "__main__":
    test()