# 分割数组的最大值
# 给定一个非负整数数组 nums 和一个整数 m ，你需要将这个数组分成 m 个非空的连续子数组。
# 设计一个算法使得这 m 个子数组各自和的最大值最小。
# 
# 算法思路：
# 这是一个典型的二分答案问题。
# 1. 答案具有单调性：最大值越小，需要分割的子数组越多；最大值越大，需要分割的子数组越少
# 2. 二分搜索答案的范围：左边界是数组中的最大值，右边界是数组元素之和
# 3. 对于每个中间值，使用贪心算法检查是否能将数组分割成不超过 m 个子数组，
#    使得每个子数组的和都不超过该中间值
# 
# 时间复杂度：O(n * log(sum))
# 空间复杂度：O(1)
# 
# 测试链接 : https://leetcode.cn/problems/split-array-largest-sum/

def can_split(nums, m, max_sum):
    """
    检查是否能将数组分割成不超过 m 个子数组，使得每个子数组的和都不超过给定值
    使用贪心算法实现
    
    Args:
        nums (List[int]): 非负整数数组
        m (int): 分割成的子数组数量上限
        max_sum (int): 每个子数组和的上限
    
    Returns:
        bool: 是否能满足分割要求
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    count = 1  # 当前分割的子数组数量，初始为1
    current_sum = 0  # 当前子数组的和
    
    for num in nums:
        # 如果当前数字本身就超过了上限，无法满足要求
        if num > max_sum:
            return False
        
        # 如果加上当前数字会超过上限，则需要新开一个子数组
        if current_sum + num > max_sum:
            count += 1
            current_sum = num
            
            # 如果子数组数量超过了 m，无法满足要求
            if count > m:
                return False
        else:
            # 否则将当前数字加入当前子数组
            current_sum += num
    
    return True

def split_array(nums, m):
    """
    分割数组使得子数组各自和的最大值最小
    
    Args:
        nums (List[int]): 非负整数数组
        m (int): 分割成的子数组数量
    
    Returns:
        int: 分割后子数组各自和的最大值的最小值
    
    时间复杂度：O(n * log(sum))
    空间复杂度：O(1)
    """
    # 确定二分搜索的边界
    # 左边界：数组中的最大值（每个元素单独成一组的情况）
    # 右边界：数组元素之和（所有元素成一组的情况）
    left = max(nums)
    right = sum(nums)
    
    result = right
    
    # 二分搜索答案
    while left <= right:
        mid = left + (right - left) // 2
        
        # 检查是否能将数组分割成不超过 m 个子数组，使得每个子数组的和都不超过 mid
        if can_split(nums, m, mid):
            result = mid
            right = mid - 1  # 尝试寻找更小的最大值
        else:
            left = mid + 1   # 需要更大的最大值才能满足分割要求
    
    return result

# 为了测试
if __name__ == "__main__":
    # 测试用例1
    nums1 = [7, 2, 5, 10, 8]
    m1 = 2
    print(f"数组: {nums1}, m = {m1}, 结果 = {split_array(nums1, m1)}")  # 输出: 18
    
    # 测试用例2
    nums2 = [1, 2, 3, 4, 5]
    m2 = 2
    print(f"数组: {nums2}, m = {m2}, 结果 = {split_array(nums2, m2)}")  # 输出: 9
    
    # 测试用例3
    nums3 = [1, 4, 4]
    m3 = 3
    print(f"数组: {nums3}, m = {m3}, 结果 = {split_array(nums3, m3)}")  # 输出: 4