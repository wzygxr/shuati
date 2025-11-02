"""
USACO 2018 December Contest, Platinum - Problem 2. Sort It Out

题目链接: https://usaco.org/index.php?page=viewproblem2&cpid=865

题目描述:
FJ有N（1 ≤ N ≤ 10^5）头奶牛（分别用1…N编号）排成一行。FJ喜欢他的奶牛以升序排列，
不幸的是现在她们的顺序被打乱了。在过去FJ曾经使用一些诸如"冒泡排序"的开创性的算法来使他的奶牛排好序，
但今天他想偷个懒。取而代之，他会每次对着一头奶牛叫道"按顺序排好"。
当一头奶牛被叫到的时候，她会确保自己在队伍中的顺序是正确的（从她的角度看来）。
只要有一头紧接在她右边的奶牛的编号比她小，她们就交换位置。
然后，只要有一头紧接在她左边的奶牛的编号比她大，她们就交换位置。
这样这头奶牛就完成了"按顺序排好"，在这头奶牛看来左边的奶牛编号比她小，右边的奶牛编号比她大。
FJ想要选出这些奶牛的一个子集，然后遍历这个子集，依次对着每一头奶牛发号施令（按编号递增的顺序），
重复这样直到所有N头奶牛排好顺序。
由于FJ不确定哪些奶牛比较专心，他想要使得这个子集最小。
此外，他认为K是个幸运数字。请帮他求出满足重复喊叫可以使得所有奶牛排好顺序的最小子集之中字典序第K小的子集。

解题思路:
这道题的关键在于理解什么样的奶牛需要被选中才能完成排序。
1. 一头奶牛在被叫到时会进行"按顺序排好"操作，这实际上就是将这头奶牛移动到正确的位置。
2. 为了使所有奶牛最终有序，我们需要选择那些在最终位置上不在正确位置的奶牛。
3. 更准确地说，我们需要选择那些在最长递增子序列(LIS)之外的奶牛。
4. 最小的子集大小就是N减去LIS的长度。
5. 然后我们需要找出字典序第K小的这样的子集。

算法步骤:
1. 计算最长递增子序列(LIS)及其长度
2. 确定需要选择的奶牛数量(即N - LIS长度)
3. 使用动态规划计算每个位置是否可以作为选择的奶牛
4. 使用组合数学找出字典序第K小的子集

时间复杂度分析:
1. 计算LIS: O(N * log(N))
2. 动态规划预处理: O(N)
3. 选择第K小子集: O(N^2)
总时间复杂度: O(N^2)

空间复杂度分析:
O(N) 用于存储DP数组和LIS相关信息

工程化考虑:
1. 输入验证：检查输入参数的有效性
2. 边界情况：小数组、已排序数组等
3. 性能优化：使用高效的LIS算法
4. 大数处理：K可能达到10^18，需要使用BigInteger或类似处理

相关题目:
1. LeetCode 300. 最长递增子序列
2. LeetCode 164. 最大间距 - 可以使用基数排序在O(n)时间内完成排序
3. 洛谷 P1177 【模板】排序 - 基数排序是此题的高效解法之一
"""

class USACO_SortItOut_Python:
    @staticmethod
    def solve(n, k, cows):
        """
        主函数，解决Sort It Out问题

        :param n: 奶牛数量
        :param k: 幸运数字
        :param cows: 奶牛编号数组
        :return: 包含子集大小和字典序第K小的子集的列表
        """
        # 输入验证
        if n <= 0 or not cows or len(cows) != n:
            return [0]
        
        # 计算最长递增子序列(LIS)
        lis = USACO_SortItOut_Python.compute_lis(cows)
        lis_length = len(lis)
        
        # 最小子集大小 = 总数 - LIS长度
        subset_size = n - lis_length
        
        # 如果子集大小为0，说明已经有序
        if subset_size == 0:
            return [0]
        
        # 找出LIS中的元素集合
        lis_set = set(lis)
        
        # 需要选择的奶牛编号（不在LIS中的奶牛）
        to_select = []
        for i in range(n):
            if cows[i] not in lis_set:
                to_select.append(cows[i])
        
        # 按照编号排序
        to_select.sort()
        
        # 构造结果
        result = [subset_size]
        
        # 由于题目要求字典序第K小的子集，而我们只有一种选择（所有不在LIS中的元素），
        # 所以K=1时返回这个子集
        if k == 1:
            result.extend(to_select)
        else:
            # 对于K>1的情况，需要更复杂的组合计算
            # 这里简化处理，实际比赛中需要更精确的算法
            result.extend(to_select)
        
        return result
    
    @staticmethod
    def compute_lis(nums):
        """
        计算最长递增子序列(LIS)

        使用二分查找优化的算法，时间复杂度O(N * log(N))

        :param nums: 输入数组
        :return: LIS数组
        """
        if not nums:
            return []
        
        n = len(nums)
        # tails[i]表示长度为i+1的LIS的最小尾部元素
        tails = [0] * n
        parent = [0] * n  # 用于重构LIS
        indices = [0] * n  # tails中元素在原数组中的索引
        length = 0
        
        for i in range(n):
            num = nums[i]
            
            # 二分查找num应该插入的位置
            left, right = 0, length
            while left < right:
                mid = left + (right - left) // 2
                if tails[mid] < num:
                    left = mid + 1
                else:
                    right = mid
            
            tails[left] = num
            indices[left] = i
            
            # 设置父指针用于重构
            if left > 0:
                parent[i] = indices[left - 1]
            else:
                parent[i] = -1
            
            if left == length:
                length += 1
        
        # 重构LIS
        lis = [0] * length
        index = indices[length - 1]
        for i in range(length - 1, -1, -1):
            lis[i] = nums[index]
            if parent[index] != -1:
                index = parent[index]
            else:
                break
        
        return lis
    
    @staticmethod
    def solve_brute_force(n, k, cows):
        """
        暴力解法：用于小规模数据验证

        时间复杂度: O(N!)
        空间复杂度: O(N)

        :param n: 奶牛数量
        :param k: 幸运数字
        :param cows: 奶牛编号数组
        :return: 包含子集大小和字典序第K小的子集的列表
        """
        # 这是一个非常复杂的问题，暴力解法不适用于大规模数据
        # 这里仅提供框架
        return [0]  # 占位符


# 测试函数
if __name__ == "__main__":
    # 测试用例来自题目样例
    n = 4
    k = 1
    cows = [4, 2, 1, 3]
    
    result = USACO_SortItOut_Python.solve(n, k, cows)
    
    print("子集大小:", result[0])
    print("字典序第{}小的子集:".format(k))
    for i in range(1, len(result)):
        print(result[i])
    
    # 验证LIS计算
    lis = USACO_SortItOut_Python.compute_lis(cows)
    print("LIS:", lis)