# 按位与为零的三元组 (Triples with Bitwise AND Equal To Zero)
# 给你一个整数数组 nums ，返回其中按位与三元组的数目。
# 按位与三元组是由下标 (i, j, k) 组成的三元组，并满足 nums[i] & nums[j] & nums[k] == 0。
# 测试链接 : https://leetcode.cn/problems/triples-with-bitwise-and-equal-to-zero/

class Code12_TriplesWithBitwiseAndEqualToZero:
    
    # 使用状态压缩动态规划解决按位与三元组问题
    # 核心思想：先计算所有两个数的按位与结果，再枚举第三个数
    # 时间复杂度: O(3^m + n^2)，其中m是最大数的位数(本题中为16)
    # 空间复杂度: O(2^m)
    @staticmethod
    def countTriplets(nums):
        # cnt[mask] 表示有多少个数与mask的按位与结果为0
        cnt = [0] * (1 << 16)
        
        # 枚举所有可能的两个数的按位与结果
        for x in nums:
            for y in nums:
                # 统计两个数按位与的结果
                cnt[x & y] += 1
        
        result = 0
        
        # 枚举第三个数
        for z in nums:
            # 枚举所有与z按位与结果为0的数
            for mask in range(1 << 16):
                # 如果mask与z的按位与结果为0
                if (mask & z) == 0:
                    # 累加满足条件的三元组数量
                    result += cnt[mask]
        
        return result
    
    # 测试方法
    @staticmethod
    def test():
        # 测试用例1
        nums1 = [2, 1, 3]
        result1 = Code12_TriplesWithBitwiseAndEqualToZero.countTriplets(nums1)
        print(f"数组: {nums1}, 按位与为零的三元组数量: {result1}")  # 期望输出: 12
        
        # 测试用例2
        nums2 = [0, 0, 0]
        result2 = Code12_TriplesWithBitwiseAndEqualToZero.countTriplets(nums2)
        print(f"数组: {nums2}, 按位与为零的三元组数量: {result2}")  # 期望输出: 27


if __name__ == "__main__":
    Code12_TriplesWithBitwiseAndEqualToZero.test()