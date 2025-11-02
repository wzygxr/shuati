import time
import random
from typing import List

class Code35_WiggleSubsequence:
    """
    摆动序列
    
    题目描述：
    如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。
    第一个差（如果存在的话）可能是正数或负数。少于两个元素的序列也是摆动序列。
    
    例如，[1,7,4,9,2,5] 是一个摆动序列，因为差值 (6,-3,5,-7,3) 是正负交替出现的。
    相反，[1,4,7,2,5] 和 [1,7,4,5,5] 不是摆动序列，第一个序列是因为它的前两个差值都是正数，第二个序列是因为它的最后一个差值为零。
    
    给定一个整数序列，返回作为摆动序列的最长子序列的长度。通过从原始序列中删除一些（也可以不删除）元素来获得子序列，剩下的元素保持其原始顺序。
    
    来源：LeetCode 376
    链接：https://leetcode.cn/problems/wiggle-subsequence/
    
    算法思路：
    使用贪心算法：
    1. 遍历数组，记录当前趋势（上升或下降）
    2. 当趋势发生变化时，增加摆动序列长度
    3. 跳过中间的趋势相同的元素
    
    时间复杂度：O(n) - 只需要遍历一次数组
    空间复杂度：O(1) - 只使用常数空间
    
    关键点分析：
    - 贪心策略：选择趋势变化的点
    - 状态机思想：维护当前趋势状态
    - 边界处理：处理平缓区域
    
    工程化考量：
    - 输入验证：检查数组是否为空
    - 性能优化：避免不必要的计算
    - 可读性：清晰的变量命名和注释
    """
    
    @staticmethod
    def wiggle_max_length(nums: List[int]) -> int:
        """
        计算最长摆动子序列的长度
        
        Args:
            nums: 整数数组
            
        Returns:
            int: 最长摆动子序列的长度
        """
        # 输入验证
        if not nums:
            return 0
        if len(nums) < 2:
            return len(nums)
        
        n = len(nums)
        prev_diff = 0  # 前一个差值
        count = 1      # 摆动序列长度
        
        for i in range(1, n):
            diff = nums[i] - nums[i-1]
            
            # 如果当前差值与前一差值趋势相反，或者刚开始（prev_diff == 0）
            if (diff > 0 and prev_diff <= 0) or (diff < 0 and prev_diff >= 0):
                count += 1
                prev_diff = diff
        
        return count
    
    @staticmethod
    def wiggle_max_length_state_machine(nums: List[int]) -> int:
        """
        状态机实现：更清晰的逻辑
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not nums:
            return 0
        if len(nums) < 2:
            return len(nums)
        
        n = len(nums)
        up = 1    # 上升趋势的最大长度
        down = 1  # 下降趋势的最大长度
        
        for i in range(1, n):
            if nums[i] > nums[i-1]:
                up = down + 1  # 当前上升，从下降趋势转移
            elif nums[i] < nums[i-1]:
                down = up + 1  # 当前下降，从上升趋势转移
        
        return max(up, down)
    
    @staticmethod
    def wiggle_max_length_brute_force(nums: List[int]) -> int:
        """
        暴力解法：检查所有可能的子序列
        时间复杂度：O(2^n) - 指数级复杂度
        空间复杂度：O(n) - 递归栈深度
        """
        if not nums:
            return 0
        
        def is_wiggle(seq):
            """检查序列是否为摆动序列"""
            if len(seq) < 2:
                return True
            
            prev_diff = seq[1] - seq[0]
            if prev_diff == 0:
                return False
            
            for i in range(2, len(seq)):
                diff = seq[i] - seq[i-1]
                if diff == 0 or (diff > 0) == (prev_diff > 0):
                    return False
                prev_diff = diff
            
            return True
        
        def backtrack(start, path):
            """回溯法生成所有子序列"""
            nonlocal max_len
            
            if is_wiggle(path):
                max_len = max(max_len, len(path))
            
            for i in range(start, len(nums)):
                path.append(nums[i])
                backtrack(i + 1, path)
                path.pop()
        
        max_len = 0
        backtrack(0, [])
        return max_len
    
    @staticmethod
    def validate_wiggle(seq: List[int]) -> bool:
        """
        验证函数：检查序列是否为摆动序列
        
        Args:
            seq: 序列
            
        Returns:
            bool: 是否为摆动序列
        """
        if len(seq) < 2:
            return True
        
        # 找到第一个非零差值
        prev_diff = 0
        for i in range(1, len(seq)):
            diff = seq[i] - seq[i-1]
            if diff != 0:
                prev_diff = diff
                break
        
        # 如果所有差值都是0，则只有单个元素是摆动序列
        if prev_diff == 0:
            return len(seq) == 1
        
        # 检查摆动性质
        for i in range(1, len(seq)):
            diff = seq[i] - seq[i-1]
            if diff == 0:
                continue  # 跳过0差值
            if (diff > 0) == (prev_diff > 0):
                return False
            prev_diff = diff
        
        return True
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 摆动序列测试 ===")
        
        # 测试用例1: [1,7,4,9,2,5] -> 6
        nums1 = [1,7,4,9,2,5]
        print(f"测试用例1: {nums1}")
        result1 = Code35_WiggleSubsequence.wiggle_max_length(nums1)
        result2 = Code35_WiggleSubsequence.wiggle_max_length_state_machine(nums1)
        print(f"方法1结果: {result1}")  # 6
        print(f"方法2结果: {result2}")  # 6
        print(f"验证: {Code35_WiggleSubsequence.validate_wiggle(nums1[:result1])}")
        
        # 测试用例2: [1,17,5,10,13,15,10,5,16,8] -> 7
        nums2 = [1,17,5,10,13,15,10,5,16,8]
        print(f"\n测试用例2: {nums2}")
        result1 = Code35_WiggleSubsequence.wiggle_max_length(nums2)
        result2 = Code35_WiggleSubsequence.wiggle_max_length_state_machine(nums2)
        print(f"方法1结果: {result1}")  # 7
        print(f"方法2结果: {result2}")  # 7
        print(f"验证: {Code35_WiggleSubsequence.validate_wiggle(nums2[:result1])}")
        
        # 测试用例3: [1,2,3,4,5,6,7,8,9] -> 2
        nums3 = [1,2,3,4,5,6,7,8,9]
        print(f"\n测试用例3: {nums3}")
        result1 = Code35_WiggleSubsequence.wiggle_max_length(nums3)
        result2 = Code35_WiggleSubsequence.wiggle_max_length_state_machine(nums3)
        print(f"方法1结果: {result1}")  # 2
        print(f"方法2结果: {result2}")  # 2
        print(f"验证: {Code35_WiggleSubsequence.validate_wiggle(nums3[:result1])}")
        
        # 测试用例4: [3,3,3,2,5] -> 3
        nums4 = [3,3,3,2,5]
        print(f"\n测试用例4: {nums4}")
        result1 = Code35_WiggleSubsequence.wiggle_max_length(nums4)
        result2 = Code35_WiggleSubsequence.wiggle_max_length_state_machine(nums4)
        print(f"方法1结果: {result1}")  # 3
        print(f"方法2结果: {result2}")  # 3
        print(f"验证: {Code35_WiggleSubsequence.validate_wiggle(nums4[:result1])}")
        
        # 边界测试：空数组
        nums5 = []
        print(f"\n测试用例5: {nums5}")
        result1 = Code35_WiggleSubsequence.wiggle_max_length(nums5)
        result2 = Code35_WiggleSubsequence.wiggle_max_length_state_machine(nums5)
        print(f"方法1结果: {result1}")  # 0
        print(f"方法2结果: {result2}")  # 0
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        n = 1000
        nums = [random.randint(1, 100) for _ in range(n)]
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code35_WiggleSubsequence.wiggle_max_length(nums)
        end_time1 = time.time()
        print(f"方法1执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"结果: {result1}")
        
        start_time2 = time.time()
        result2 = Code35_WiggleSubsequence.wiggle_max_length_state_machine(nums)
        end_time2 = time.time()
        print(f"方法2执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"结果: {result2}")
        
        # 暴力解法太慢，只测试小规模数据
        small_nums = nums[:20]
        start_time3 = time.time()
        result3 = Code35_WiggleSubsequence.wiggle_max_length_brute_force(small_nums)
        end_time3 = time.time()
        print(f"方法3执行时间（小规模）: {(end_time3 - start_time3) * 1000:.2f} 毫秒")
        print(f"结果: {result3}")
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("方法1（贪心算法）:")
        print("- 时间复杂度: O(n)")
        print("  - 只需要遍历一次数组")
        print("  - 每个元素处理一次")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n方法2（状态机）:")
        print("- 时间复杂度: O(n)")
        print("  - 遍历一次数组")
        print("  - 状态转移操作O(1)")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n方法3（暴力解法）:")
        print("- 时间复杂度: O(2^n)")
        print("  - 生成所有子序列")
        print("  - 指数级复杂度")
        print("- 空间复杂度: O(n)")
        print("  - 递归栈深度")
        
        print("\n贪心策略证明:")
        print("1. 摆动序列的本质是趋势变化")
        print("2. 贪心选择：每次趋势变化时选择当前元素")
        print("3. 最优子结构：局部最优导致全局最优")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理空数组和边界情况")
        print("2. 性能优化：避免指数级复杂度")
        print("3. 可读性：状态机设计清晰")
        print("4. 测试覆盖：各种边界情况")

def main():
    """主函数"""
    Code35_WiggleSubsequence.run_tests()
    Code35_WiggleSubsequence.performance_test()
    Code35_WiggleSubsequence.analyze_complexity()

if __name__ == "__main__":
    main()