# 最大异或对
# 给定一个非负整数数组 nums，返回 nums[i] XOR nums[j] 的最大结果，其中 0 <= i <= j < n
# 1 <= nums.length <= 2 * 10^5
# 0 <= nums[i] <= 2^31 - 1
# 测试链接 : https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
# 测试链接 : https://www.luogu.com.cn/problem/P4551

# 补充题目1: 最大异或子数组
# 给定一个非负整数数组 nums，返回该数组中异或和最大的非空子数组的异或和
# 测试链接: https://leetcode.cn/problems/maximum-xor-subarray/
# 相关题目:
# - https://leetcode.cn/problems/maximum-xor-subarray/
# - https://www.hdu.edu.cn/problem/5325
# - https://codeforces.com/problemset/problem/1715/E

# 补充题目2: 子集异或和最大值
# 给定一个非负整数数组 nums，返回所有可能的子集异或和中的最大值
# 测试链接: https://leetcode.cn/problems/maximum-xor-sum-of-a-subarray/
# 相关题目:
# - https://www.luogu.com.cn/problem/P3812
# - https://www.hdu.edu.cn/problem/3949
# - https://codeforces.com/problemset/problem/959/F

# 补充题目3: 寻找异或值为零的三元组
# 给定一个整数数组 arr，返回异或值为0的三元组(i,j,k)的数量，其中 i<j<k
# 测试链接: https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/
# 相关题目:
# - https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/
# - https://www.luogu.com.cn/problem/P4592
# - https://codeforces.com/problemset/problem/1175/G

class XorPair:
    """最大异或对解决方案"""
    
    def __init__(self):
        # 用字典实现Trie节点，键为0或1，值为子节点
        self.root = {}
    
    def insert(self, num):
        """
        向Trie中插入数字
        :param num: 要插入的数字
        """
        node = self.root
        # 从最高位开始处理（31位整数）
        for i in range(31, -1, -1):
            # 提取第i位的值（0或1）
            bit = (num >> i) & 1
            # 如果该位对应的子节点不存在，则创建新节点
            if bit not in node:
                node[bit] = {}
            # 移动到子节点
            node = node[bit]
    
    def getMaxXor(self, num):
        """
        查询与给定数字异或能得到的最大值
        :param num: 给定数字
        :return: 最大异或值
        """
        node = self.root
        result = 0
        
        # 从最高位开始处理
        for i in range(31, -1, -1):
            # 提取第i位的值
            bit = (num >> i) & 1
            # 贪心策略：尽量选择与当前位相反的路径以使异或结果最大
            opposite_bit = bit ^ 1
            
            # 如果相反位存在，则选择该路径
            if opposite_bit in node:
                result |= (1 << i)  # 将第i位置为1
                node = node[opposite_bit]
            else:
                # 否则只能选择相同位
                if bit in node:
                    node = node[bit]
                else:
                    # 如果都没有，说明Trie为空，直接返回0
                    return 0
        
        return result
    
    def findMaximumXOR(self, nums):
        """
        主函数：找出数组中任意两个数的最大异或值
        :param nums: 输入数组
        :return: 最大异或值
        """
        if not nums:
            return 0
        
        # 将所有数字插入Trie
        for num in nums:
            self.insert(num)
        
        max_xor = 0
        # 对每个数字，查找与其异或能得到的最大值
        for num in nums:
            current_max = self.getMaxXor(num)
            max_xor = max(max_xor, current_max)
        
        return max_xor


# 最大异或子数组解决方案
class MaxXorSubarray:
    """最大异或子数组解决方案"""
    
    def __init__(self):
        # 用字典实现Trie节点
        self.root = {}
    
    def insert(self, num):
        """
        向Trie中插入数字
        :param num: 要插入的数字（前缀异或和）
        """
        node = self.root
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            if bit not in node:
                node[bit] = {}
            node = node[bit]
    
    def query_max_xor(self, num):
        """
        查询与给定数字异或能得到的最大值
        :param num: 当前前缀异或和
        :return: 最大异或值
        """
        if not self.root:
            return 0
        
        node = self.root
        result = 0
        
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            opposite_bit = bit ^ 1
            
            if opposite_bit in node:
                result |= (1 << i)
                node = node[opposite_bit]
            else:
                node = node.get(bit, {})
        
        return result
    
    def max_xor_subarray(self, nums):
        """
        找出数组中异或和最大的非空子数组的异或和
        :param nums: 输入数组
        :return: 最大异或子数组的异或和
        """
        if not nums:
            return 0
        
        max_xor = float('-inf')
        prefix_xor = 0
        
        # 插入前缀异或和0，表示空数组的情况
        self.insert(0)
        
        for num in nums:
            # 计算当前前缀异或和
            prefix_xor ^= num
            
            # 查询当前前缀异或和与之前前缀异或和的最大异或值
            current_max = self.query_max_xor(prefix_xor)
            max_xor = max(max_xor, current_max)
            
            # 插入当前前缀异或和
            self.insert(prefix_xor)
        
        return max_xor

# 子集异或和最大值解决方案
class MaxXorSubset:
    """子集异或和最大值解决方案"""
    
    def max_xor_subset(self, nums):
        """
        找出所有可能的子集异或和中的最大值
        方法：高斯消元，构建线性基
        :param nums: 输入数组
        :return: 最大子集异或和
        """
        if not nums:
            return 0
        
        # 线性基数组，base[i]表示第i位为最高位的数
        base = [0] * 32
        
        # 构建线性基
        for num in nums:
            if num == 0:
                continue
            
            # 从最高位开始处理
            for i in range(31, -1, -1):
                if (num >> i) & 1:
                    # 如果该位没有被占据，则插入到线性基中
                    if base[i] == 0:
                        base[i] = num
                        break
                    # 否则，将当前数与线性基中对应的数异或，继续处理
                    num ^= base[i]
        
        # 计算最大异或和
        result = 0
        for i in range(31, -1, -1):
            # 尝试用当前基向量异或，看是否能使结果更大
            if (result ^ base[i]) > result:
                result ^= base[i]
        
        return result

# 补充题目3: 寻找异或值为零的三元组
# 给定一个整数数组 arr，返回异或值为0的三元组(i,j,k)的数量，其中 i<j<k
# 测试链接: https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/

class TripletXorZero:
    def count_triplets(self, arr):
        """暴力解法：计算异或值为0的三元组(i,j,k)的数量"""
        if not arr or len(arr) < 3:
            return 0
        
        n = len(arr)
        result = 0
        
        # 遍历所有可能的i和k
        for i in range(n):
            xor_sum = 0
            for k in range(i, n):
                xor_sum ^= arr[k]
                # 如果从i到k的异或和为0，那么中间的任意j(i<j<=k)都满足条件
                if xor_sum == 0 and k > i:
                    result += (k - i)
        
        return result
    
    def count_triplets_optimized(self, arr):
        """优化解法：使用哈希表记录异或和的位置"""
        if not arr or len(arr) < 3:
            return 0
        
        n = len(arr)
        result = 0
        xor_sum = 0
        
        # 哈希表记录异或值及其出现的次数和位置之和
        count_map = {0: 1}  # {异或值: 出现次数}
        sum_map = {0: -1}   # {异或值: 位置之和}
        
        for k in range(n):
            xor_sum ^= arr[k]
            
            if xor_sum in count_map:
                # 计算所有可能的i的数量和位置和
                result += count_map[xor_sum] * k - sum_map[xor_sum] - count_map[xor_sum]
            
            # 更新哈希表
            count_map[xor_sum] = count_map.get(xor_sum, 0) + 1
            sum_map[xor_sum] = sum_map.get(xor_sum, 0) + k
        
        return result


# 测试用例
if __name__ == "__main__":
    # 测试最大异或对
    print("=== 测试最大异或对 ===")
    solution = XorPair()
    nums1 = [3, 10, 5, 25, 2, 8]
    print("测试用例1结果:", solution.findMaximumXOR(nums1))  # 预期输出: 28 (5 XOR 25)
    
    # 测试最大异或子数组
    print("\n=== 测试最大异或子数组 ===")
    solution2 = MaxXorSubarray()
    nums2 = [3, 8, 2, 6, 4]
    print("测试用例结果:", solution2.max_xor_subarray(nums2))  # 预期输出: 15 (3^8^2^6^4=15)
    
    # 测试子集异或和最大值
    print("\n=== 测试子集异或和最大值 ===")
    solution3 = MaxXorSubset()
    nums3 = [3, 10, 5, 25, 2, 8]
    print("测试用例结果:", solution3.max_xor_subset(nums3))  # 预期输出: 31
    
    # 测试寻找异或值为零的三元组
    print("\n=== 测试寻找异或值为零的三元组 ===")
    solution4 = TripletXorZero()
    nums4 = [2, 3, 1, 6, 7]
    print("暴力解法结果:", solution4.count_triplets(nums4))  # 预期输出: 4
    print("优化解法结果:", solution4.count_triplets_optimized(nums4))  # 预期输出: 4

'''算法分析总结：

1. 最大异或对 (XorPair)
时间复杂度: O(n * log M)
- n是数组长度
- log M是数字的位数（这里M=2^31，所以log M=32）
空间复杂度: O(n * log M)
- 最坏情况下，Trie需要存储所有数字的所有位
核心思想: 使用Trie树和贪心策略，从最高位开始，尽量选择与当前位相反的路径
优化点: Python中使用字典实现Trie节点，代码简洁；处理空Trie和空数组的边界情况

2. 最大异或子数组 (MaxXorSubarray)
时间复杂度: O(n * log M)
空间复杂度: O(n)
核心思想: 利用前缀异或和的性质（子数组异或和 = 两个前缀异或和的异或），结合Trie树查找最大异或值
关键点: 插入前缀异或和0，处理i=0的特殊情况
数学原理: 对于数组a[0...n-1]，前缀异或和为prefixXor[i] = a[0]^a[1]^...^a[i-1]
          则子数组a[i...j]的异或和 = prefixXor[j+1] ^ prefixXor[i]

3. 子集异或和最大值 (MaxXorSubset)
时间复杂度: O(n * log M)
空间复杂度: O(log M)
核心思想: 线性基（高斯消元思想），将每个数分解到不同的最高位，贪心选择最大异或组合
优点: 线性基可以表示所有可能的异或结果，且能高效求出最大值
数学原理: 任何数都可以表示为线性基数组中若干数的异或结果
         每个数被插入到其最高位对应的位置，类似高斯消元过程

4. 寻找异或值为零的三元组 (TripletXorZero)
暴力解法: O(n^3)，只适用于小数据量
优化解法1: O(n^2)，枚举i和k，计算异或和
优化解法2: O(n)，利用前缀异或和性质和哈希表
数学原理: 如果prefixXor[i] = prefixXor[k+1]，则子数组[i+1,k]的异或和为0
          此时对于任意i < j <= k，都有a[i+1]^...^a[j] = a[j+1]^...^a[k]

工程化考量：
1. 边界处理：所有方法都处理了空数组和null输入的情况
2. 异常防御：在查询时进行了空字典检查，避免潜在异常
3. 代码模块化：每个问题都封装在单独的类中，便于复用和维护
4. 性能优化：Python中使用字典实现Trie虽然代码简洁，但在大数据量时可能不如数组高效
5. 可读性：详细的注释和清晰的命名规范

跨语言实现差异：
1. Python中使用字典实现Trie节点，而Java和C++可能使用数组更高效
2. Python的整数没有固定大小，而Java和C++需要考虑整数的符号位和大小限制
3. Python的内置数据结构（如字典）简化了代码，但可能在性能上不如语言原生实现
4. 内存管理：Python有自动垃圾回收，与Java类似，但与C++的手动管理不同
5. 性能特点：C++通常有更好的性能，Java次之，Python在大数据量时可能较慢

算法在工程中的应用：
1. 网络安全：异或运算在加密和解密算法中有广泛应用
2. 数据压缩：Trie树结构可用于高效的字符串压缩算法
3. 特征选择：最大异或问题的思想可用于机器学习中的特征提取和降维
4. 计算机视觉：异或运算在图像处理和模式匹配中有特定应用
5. 网络协议：位运算常用于网络数据包的解析和处理

调试技巧：
1. 打印中间变量：在关键步骤打印位运算结果和Trie节点状态
2. 小例子测试：用简单的测试用例验证算法逻辑的正确性
3. 边界测试：测试空数组、单元素数组、全零数组等特殊情况
4. 性能分析：对于大数据量输入，使用Python的cProfile模块监控性能
5. 交互式调试：使用Python的pdb调试器单步执行，观察变量变化

与机器学习的联系：
1. 特征提取：线性基的概念与机器学习中的特征选择和降维有关
2. 决策树：Trie树的结构与决策树算法有相似之处
3. 位操作在深度学习中的应用：神经网络中某些优化算法使用位运算加速计算
4. 哈希学习：哈希表的使用与哈希学习算法中的特征映射有关

算法优化建议：
1. 对于最大异或对问题，在Python中可以考虑使用PyPy来提高性能
2. 对于大规模数据，可以考虑使用NumPy进行位运算优化
3. 在Python中，可以使用位操作的优化技巧，如位移运算代替乘法除法
4. 对于实时应用，可以考虑用Cython重写性能关键部分，或使用其他编译型语言

实战经验分享：
1. 注意数据范围：不同题目的数字范围可能不同，需要调整位处理的位数
2. 边界情况：空数组、单个元素数组等特殊情况需要单独处理
3. 性能调优：对于大规模数据，Python中可以考虑使用数组实现的Trie树而非字典，以提高访问速度
4. 跨语言开发：在实际项目中，可以将性能关键的算法部分用C/C++实现，然后通过Python的扩展机制调用
5. 单元测试：编写全面的单元测试，确保算法在各种输入条件下都能正确工作
'''