import time
import random
from typing import List

class Code36_JumpGame:
    """
    跳跃游戏
    
    题目描述：
    给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
    数组中的每个元素代表你在该位置可以跳跃的最大长度。
    判断你是否能够到达最后一个下标。
    
    来源：LeetCode 55
    链接：https://leetcode.cn/problems/jump-game/
    
    算法思路：
    使用贪心算法：
    1. 维护当前能够到达的最远位置
    2. 遍历数组，更新最远位置
    3. 如果当前位置超过最远位置，说明无法到达
    
    时间复杂度：O(n) - 只需要遍历一次数组
    空间复杂度：O(1) - 只使用常数空间
    
    关键点分析：
    - 贪心策略：每次选择能够到达的最远位置
    - 数学证明：局部最优导致全局最优
    - 边界处理：处理0值情况
    
    工程化考量：
    - 输入验证：检查数组是否为空
    - 性能优化：提前终止遍历
    - 可读性：清晰的变量命名和注释
    """
    
    @staticmethod
    def can_jump(nums: List[int]) -> bool:
        """
        判断是否能够到达最后一个下标
        
        Args:
            nums: 非负整数数组
            
        Returns:
            bool: 是否能够到达最后一个下标
        """
        # 输入验证
        if not nums:
            return False
        if len(nums) == 1:
            return True
        
        n = len(nums)
        max_reach = 0  # 当前能够到达的最远位置
        
        for i in range(n):
            # 如果当前位置已经超过能够到达的最远位置
            if i > max_reach:
                return False
            
            # 更新能够到达的最远位置
            max_reach = max(max_reach, i + nums[i])
            
            # 如果已经能够到达最后一个位置
            if max_reach >= n - 1:
                return True
        
        return False
    
    @staticmethod
    def can_jump_backward(nums: List[int]) -> bool:
        """
        另一种实现：从后向前遍历
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not nums:
            return False
        if len(nums) == 1:
            return True
        
        n = len(nums)
        last_pos = n - 1  # 需要到达的位置
        
        for i in range(n - 2, -1, -1):
            if i + nums[i] >= last_pos:
                last_pos = i
        
        return last_pos == 0
    
    @staticmethod
    def can_jump_brute_force(nums: List[int]) -> bool:
        """
        暴力解法：DFS搜索
        时间复杂度：O(2^n)
        空间复杂度：O(n)
        """
        if not nums:
            return False
        
        def dfs(position):
            # 如果已经到达或超过最后一个位置
            if position >= len(nums) - 1:
                return True
            
            # 尝试所有可能的跳跃步数
            max_jump = nums[position]
            for i in range(1, max_jump + 1):
                if dfs(position + i):
                    return True
            
            return False
        
        return dfs(0)
    
    @staticmethod
    def validate_jump(nums: List[int], result: bool) -> bool:
        """
        验证函数：检查路径是否正确
        """
        if not nums:
            return not result
        return result == Code36_JumpGame.can_jump(nums)
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 跳跃游戏测试 ===")
        
        # 测试用例1: [2,3,1,1,4] -> True
        nums1 = [2, 3, 1, 1, 4]
        print(f"测试用例1: {nums1}")
        result1 = Code36_JumpGame.can_jump(nums1)
        result2 = Code36_JumpGame.can_jump_backward(nums1)
        print(f"方法1结果: {result1}")  # True
        print(f"方法2结果: {result2}")  # True
        print(f"验证: {Code36_JumpGame.validate_jump(nums1, result1)}")
        
        # 测试用例2: [3,2,1,0,4] -> False
        nums2 = [3, 2, 1, 0, 4]
        print(f"\n测试用例2: {nums2}")
        result1 = Code36_JumpGame.can_jump(nums2)
        result2 = Code36_JumpGame.can_jump_backward(nums2)
        print(f"方法1结果: {result1}")  # False
        print(f"方法2结果: {result2}")  # False
        print(f"验证: {Code36_JumpGame.validate_jump(nums2, result1)}")
        
        # 测试用例3: [0] -> True
        nums3 = [0]
        print(f"\n测试用例3: {nums3}")
        result1 = Code36_JumpGame.can_jump(nums3)
        result2 = Code36_JumpGame.can_jump_backward(nums3)
        print(f"方法1结果: {result1}")  # True
        print(f"方法2结果: {result2}")  # True
        print(f"验证: {Code36_JumpGame.validate_jump(nums3, result1)}")
        
        # 测试用例4: [1,0,1,0] -> False
        nums4 = [1, 0, 1, 0]
        print(f"\n测试用例4: {nums4}")
        result1 = Code36_JumpGame.can_jump(nums4)
        result2 = Code36_JumpGame.can_jump_backward(nums4)
        print(f"方法1结果: {result1}")  # False
        print(f"方法2结果: {result2}")  # False
        print(f"验证: {Code36_JumpGame.validate_jump(nums4, result1)}")
        
        # 边界测试：空数组
        nums5 = []
        print(f"\n测试用例5: {nums5}")
        result1 = Code36_JumpGame.can_jump(nums5)
        result2 = Code36_JumpGame.can_jump_backward(nums5)
        print(f"方法1结果: {result1}")  # False
        print(f"方法2结果: {result2}")  # False
        print(f"验证: {Code36_JumpGame.validate_jump(nums5, result1)}")
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        n = 10000
        nums = [random.randint(0, 9) for _ in range(n)]
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code36_JumpGame.can_jump(nums)
        end_time1 = time.time()
        print(f"方法1执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"结果: {result1}")
        print(f"验证: {Code36_JumpGame.validate_jump(nums, result1)}")
        
        start_time2 = time.time()
        result2 = Code36_JumpGame.can_jump_backward(nums)
        end_time2 = time.time()
        print(f"方法2执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"结果: {result2}")
        print(f"验证: {Code36_JumpGame.validate_jump(nums, result2)}")
        
        # 暴力解法太慢，只测试小规模数据
        small_nums = nums[:20]
        start_time3 = time.time()
        result3 = Code36_JumpGame.can_jump_brute_force(small_nums)
        end_time3 = time.time()
        print(f"方法3执行时间（小规模）: {(end_time3 - start_time3) * 1000:.2f} 毫秒")
        print(f"结果: {result3}")
        print(f"验证: {Code36_JumpGame.validate_jump(small_nums, result3)}")
    
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
        
        print("\n方法2（从后向前）:")
        print("- 时间复杂度: O(n)")
        print("  - 遍历一次数组")
        print("  - 反向遍历同样高效")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n方法3（暴力解法）:")
        print("- 时间复杂度: O(2^n)")
        print("  - 最坏情况下指数级复杂度")
        print("  - 每个位置有多种选择")
        print("- 空间复杂度: O(n)")
        print("  - 递归栈深度")
        
        print("\n贪心策略证明:")
        print("1. 维护当前能够到达的最远位置")
        print("2. 如果当前位置能够到达，则更新最远位置")
        print("3. 数学归纳法证明贪心选择性质")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理空数组和边界情况")
        print("2. 性能优化：提前终止遍历")
        print("3. 可读性：清晰的算法逻辑")
        print("4. 测试覆盖：各种边界情况")

def main():
    """主函数"""
    Code36_JumpGame.run_tests()
    Code36_JumpGame.performance_test()
    Code36_JumpGame.analyze_complexity()

if __name__ == "__main__":
    main()