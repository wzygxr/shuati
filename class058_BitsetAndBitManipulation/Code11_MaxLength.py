# LeetCode 1239. 串联字符串的最大长度
# 题目链接: https://leetcode-cn.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/
# 题目大意:
# 给定一个字符串数组 arr，字符串 s 是将 arr 某一子序列字符串连接所得的字符串，如果 s 中的每个字符都只出现过一次，
# 请返回所有可能的 s 中最长长度。
# 
# 示例 1:
# 输入：arr = ["un","iq","ue"]
# 输出：4
# 解释：所有可能的串联组合是 "","un","iq","ue","uniq" 和 "ique"，最大长度为 4。
# 
# 示例 2:
# 输入：arr = ["cha","r","act","ers"]
# 输出：6
# 解释：可能的解答是 "chaers"，长度为 6。
# 
# 示例 3:
# 输入：arr = ["abcdefghijklmnopqrstuvwxyz"]
# 输出：26
# 
# 提示：
# 1 <= arr.length <= 16
# 1 <= arr[i].length <= 26
# arr[i] 中只含有小写英文字母
#
# 解题思路:
# 由于题目中要求字符串中的每个字符只出现一次，我们可以使用位掩码来表示每个字符串中包含的字符。
# 对于长度为16的数组，我们可以使用回溯算法或动态规划来求解最长长度。
# 这里我们使用位运算优化，通过掩码来判断字符是否重复。

import time
import re

# 方法一：回溯算法 + 位运算
def max_length1(arr):
    """
    使用回溯算法和位运算求解串联字符串的最大长度
    
    Args:
        arr: 字符串列表
        
    Returns:
        满足条件的最长字符串长度
        
    时间复杂度: O(2^n)，其中n是有效字符串的数量
    空间复杂度: O(n)，递归调用栈的深度
    """
    # 边界条件检查
    if not arr:
        return 0
    
    # 过滤掉自身包含重复字符的字符串，并将其转换为位掩码
    masks = []
    for s in arr:
        mask = 0
        is_valid = True
        
        for c in s:
            bit = 1 << (ord(c) - ord('a'))
            # 检查当前字符是否已经在mask中设置
            if mask & bit:
                is_valid = False
                break
            mask |= bit
        
        if is_valid:
            masks.append(mask)
    
    # 定义回溯函数
    def backtrack(index, current_mask):
        """
        回溯函数，探索所有可能的组合
        
        Args:
            index: 当前处理到的字符串索引
            current_mask: 当前已选字符串的位掩码
            
        Returns:
            从当前状态开始能得到的最长字符串长度
        """
        # 基本情况：已经处理完所有字符串
        if index == len(masks):
            # 计算current_mask中设置的位的数量，即字符数量
            return bin(current_mask).count('1')
        
        # 不选当前字符串
        max_len = backtrack(index + 1, current_mask)
        
        # 选当前字符串（如果不会导致重复字符）
        current_mask_val = masks[index]
        if (current_mask & current_mask_val) == 0:  # 没有共同的字符
            max_len = max(max_len, backtrack(index + 1, current_mask | current_mask_val))
        
        return max_len
    
    # 调用回溯函数，从索引0和空掩码开始
    return backtrack(0, 0)

# 方法二：动态规划 + 位运算
def max_length2(arr):
    """
    使用动态规划和位运算求解串联字符串的最大长度
    
    Args:
        arr: 字符串列表
        
    Returns:
        满足条件的最长字符串长度
        
    时间复杂度: O(n * 2^m)，其中n是字符串数量，m是字符集大小（最多26）
    空间复杂度: O(2^m)，存储所有可能的掩码组合
    """
    # 边界条件检查
    if not arr:
        return 0
    
    # 过滤掉自身包含重复字符的字符串，并将其转换为位掩码
    valid_masks = []
    for s in arr:
        mask = 0
        is_valid = True
        
        for c in s:
            bit = 1 << (ord(c) - ord('a'))
            if mask & bit:
                is_valid = False
                break
            mask |= bit
        
        if is_valid and mask != 0:  # 确保mask不为0（空字符串被过滤）
            valid_masks.append(mask)
    
    # 动态规划：dp存储所有可能的有效掩码组合
    dp = [0]  # 初始状态：空字符串
    max_len = 0
    
    # 对于每个有效的字符串掩码
    for mask in valid_masks:
        # 对于当前的所有组合
        for i in range(len(dp)):
            existing_mask = dp[i]
            # 如果当前mask和已有mask没有重叠的位
            if (existing_mask & mask) == 0:
                combined_mask = existing_mask | mask
                # 检查是否已经在dp中，避免重复
                if combined_mask not in dp:
                    dp.append(combined_mask)
                    max_len = max(max_len, bin(combined_mask).count('1'))
    
    return max_len

# 方法三：优化的回溯算法
def max_length3(arr):
    """
    使用优化的回溯算法和位运算求解串联字符串的最大长度
    通过预计算长度和剪枝优化性能
    
    Args:
        arr: 字符串列表
        
    Returns:
        满足条件的最长字符串长度
        
    时间复杂度: O(2^n)，但通过剪枝优化实际运行时间
    空间复杂度: O(n)
    """
    # 过滤无效字符串并转换为掩码
    masks = []
    lengths = []
    for s in arr:
        mask = 0
        valid = True
        
        for c in s:
            bit = 1 << (ord(c) - ord('a'))
            if mask & bit:
                valid = False
                break
            mask |= bit
        
        if valid:
            masks.append(mask)
            lengths.append(bin(mask).count('1'))
    
    # 全局变量，用于存储最大长度
    max_len = [0]
    
    def optimized_backtrack(index, current_mask, current_length):
        """
        优化的回溯函数，包含剪枝
        
        Args:
            index: 当前处理到的字符串索引
            current_mask: 当前已选字符串的位掩码
            current_length: 当前已选字符串的总长度
        """
        # 更新最大长度
        if current_length > max_len[0]:
            max_len[0] = current_length
        
        # 剪枝：如果剩余的字符串即使全部选上也无法超过当前最大长度，提前返回
        remaining_max_length = current_length
        for i in range(index, len(masks)):
            if (current_mask & masks[i]) == 0:
                remaining_max_length += lengths[i]
        
        if remaining_max_length <= max_len[0]:
            return  # 剪枝
        
        # 回溯
        for i in range(index, len(masks)):
            if (current_mask & masks[i]) == 0:  # 没有共同的字符
                optimized_backtrack(i + 1, 
                                   current_mask | masks[i], 
                                   current_length + lengths[i])
    
    # 调用优化的回溯函数
    optimized_backtrack(0, 0, 0)
    return max_len[0]

# 工程化改进版本：增加参数验证和异常处理
def max_length_with_validation(arr):
    """
    工程化版本，增加了参数验证和异常处理
    
    Args:
        arr: 字符串列表
        
    Returns:
        满足条件的最长字符串长度，如果输入无效则返回0
        
    Raises:
        ValueError: 如果输入参数无效
    """
    try:
        # 参数验证
        if arr is None:
            raise ValueError("Input list cannot be None")
        
        # 检查数组长度是否在题目限制范围内
        if len(arr) > 16:
            raise ValueError("Input list size exceeds maximum allowed length of 16")
        
        # 验证每个字符串是否符合要求
        for s in arr:
            if s is None:
                raise ValueError("String elements cannot be None")
            if len(s) > 26:
                raise ValueError("String element exceeds maximum allowed length of 26")
            # 检查是否只包含小写英文字母
            if not re.match(r'^[a-z]+$', s):
                raise ValueError("String elements must contain only lowercase English letters")
        
        # 使用方法三实现
        return max_length3(arr)
    except Exception as e:
        # 记录异常日志（在实际应用中）
        print(f"Error in max_length_with_validation: {e}")
        # 异常情况下返回0
        return 0

# 单元测试函数
def run_tests():
    """
    运行单元测试，验证不同方法的正确性
    """
    print("Running unit tests...")
    
    # 测试用例1
    arr1 = ["un", "iq", "ue"]
    expected1 = 4
    result1_1 = max_length1(arr1)
    result1_2 = max_length2(arr1)
    result1_3 = max_length3(arr1)
    result1_4 = max_length_with_validation(arr1)
    
    print(f"\nTest 1: ['un','iq','ue']")
    print(f"  max_length1: {result1_1} (Expected: {expected1}) - {'PASS' if result1_1 == expected1 else 'FAIL'}")
    print(f"  max_length2: {result1_2} (Expected: {expected1}) - {'PASS' if result1_2 == expected1 else 'FAIL'}")
    print(f"  max_length3: {result1_3} (Expected: {expected1}) - {'PASS' if result1_3 == expected1 else 'FAIL'}")
    print(f"  max_length_with_validation: {result1_4} (Expected: {expected1}) - {'PASS' if result1_4 == expected1 else 'FAIL'}")
    
    # 测试用例2
    arr2 = ["cha", "r", "act", "ers"]
    expected2 = 6
    result2_1 = max_length1(arr2)
    result2_2 = max_length2(arr2)
    result2_3 = max_length3(arr2)
    result2_4 = max_length_with_validation(arr2)
    
    print(f"\nTest 2: ['cha','r','act','ers']")
    print(f"  max_length1: {result2_1} (Expected: {expected2}) - {'PASS' if result2_1 == expected2 else 'FAIL'}")
    print(f"  max_length2: {result2_2} (Expected: {expected2}) - {'PASS' if result2_2 == expected2 else 'FAIL'}")
    print(f"  max_length3: {result2_3} (Expected: {expected2}) - {'PASS' if result2_3 == expected2 else 'FAIL'}")
    print(f"  max_length_with_validation: {result2_4} (Expected: {expected2}) - {'PASS' if result2_4 == expected2 else 'FAIL'}")
    
    # 测试用例3
    arr3 = ["abcdefghijklmnopqrstuvwxyz"]
    expected3 = 26
    result3_1 = max_length1(arr3)
    result3_2 = max_length2(arr3)
    result3_3 = max_length3(arr3)
    result3_4 = max_length_with_validation(arr3)
    
    print(f"\nTest 3: ['abcdefghijklmnopqrstuvwxyz']")
    print(f"  max_length1: {result3_1} (Expected: {expected3}) - {'PASS' if result3_1 == expected3 else 'FAIL'}")
    print(f"  max_length2: {result3_2} (Expected: {expected3}) - {'PASS' if result3_2 == expected3 else 'FAIL'}")
    print(f"  max_length3: {result3_3} (Expected: {expected3}) - {'PASS' if result3_3 == expected3 else 'FAIL'}")
    print(f"  max_length_with_validation: {result3_4} (Expected: {expected3}) - {'PASS' if result3_4 == expected3 else 'FAIL'}")
    
    # 测试用例4：包含无效字符串（自身有重复字符）
    arr4 = ["abc", "def", "a", "ghi", "abb"]  # "abb"自身有重复字符，应被过滤
    expected4 = 9  # "abc" + "def" + "ghi" 但包含重复的 'a'，所以实际是 "abc" + "def" + "ghi" 中的一部分
    result4_1 = max_length1(arr4)
    result4_2 = max_length2(arr4)
    result4_3 = max_length3(arr4)
    result4_4 = max_length_with_validation(arr4)
    
    print(f"\nTest 4: ['abc','def','a','ghi','abb']")
    print(f"  max_length1: {result4_1} (Expected: {expected4}) - {'PASS' if result4_1 == expected4 else 'FAIL'}")
    print(f"  max_length2: {result4_2} (Expected: {expected4}) - {'PASS' if result4_2 == expected4 else 'FAIL'}")
    print(f"  max_length3: {result4_3} (Expected: {expected4}) - {'PASS' if result4_3 == expected4 else 'FAIL'}")
    print(f"  max_length_with_validation: {result4_4} (Expected: {expected4}) - {'PASS' if result4_4 == expected4 else 'FAIL'}")

# 性能测试函数
def performance_test():
    """
    运行性能测试，比较不同方法的效率
    """
    print("\nRunning performance tests...")
    
    # 生成测试数据：所有字母组合
    large_arr = []
    for i in range(10):
        # 生成不重复字符的字符串
        s = ''.join([chr(ord('a') + i * 5 + j) for j in range(5) if i * 5 + j < 26])
        large_arr.append(s)
    
    # 测试方法一时间
    start_time = time.time()
    result1 = max_length1(large_arr)
    end_time = time.time()
    print(f"\nPerformance of max_length1: {((end_time - start_time) * 1_000_000):.2f} μs, Result: {result1}")
    
    # 测试方法二时间
    start_time = time.time()
    result2 = max_length2(large_arr)
    end_time = time.time()
    print(f"Performance of max_length2: {((end_time - start_time) * 1_000_000):.2f} μs, Result: {result2}")
    
    # 测试方法三时间
    start_time = time.time()
    result3 = max_length3(large_arr)
    end_time = time.time()
    print(f"Performance of max_length3: {((end_time - start_time) * 1_000_000):.2f} μs, Result: {result3}")

# 主函数
def main():
    """
    主函数，运行测试并输出复杂度分析
    """
    print("LeetCode 1239. 串联字符串的最大长度")
    print("Using bitwise operations for optimization")
    
    # 运行单元测试
    run_tests()
    
    # 运行性能测试
    performance_test()
    
    # 复杂度分析
    print("\n复杂度分析:")
    print("方法一（回溯算法）:")
    print("  时间复杂度: O(2^n)，其中n是有效字符串的数量（过滤后）")
    print("  空间复杂度: O(n)，递归调用栈的深度")
    
    print("\n方法二（动态规划）:")
    print("  时间复杂度: O(n * 2^m)，其中n是字符串数量，m是字符集大小（最多26）")
    print("  空间复杂度: O(2^m)，存储所有可能的掩码组合")
    
    print("\n方法三（优化的回溯算法）:")
    print("  时间复杂度: O(2^n)，但通过剪枝优化实际运行时间")
    print("  空间复杂度: O(n)")
    print("  优点: 利用剪枝减少不必要的计算，对于大多数情况效率更高")
    
    print("\n适用场景总结:")
    print("1. 当数组长度较小时，三种方法都可以使用")
    print("2. 当字符串包含大量重复字符时，方法三的剪枝效果更好")
    print("3. 当需要更稳定的性能时，方法二的动态规划更可靠")
    print("4. 在工程实践中，应根据具体数据特征选择合适的方法")

# 运行主函数
if __name__ == "__main__":
    main()