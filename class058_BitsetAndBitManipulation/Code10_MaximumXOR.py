# LeetCode 421. 数组中两个数的最大异或值
# 题目链接: https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/
# 题目大意:
# 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
# 
# 进阶：你可以在 O(n) 的时间解决这个问题吗？
# 
# 示例 1:
# 输入: nums = [3,10,5,25,2,8]
# 输出: 28
# 解释: 最大的结果是 5 XOR 25 = 28.
# 
# 示例 2:
# 输入: nums = [0]
# 输出: 0
# 
# 示例 3:
# 输入: nums = [2,4]
# 输出: 6
# 
# 示例 4:
# 输入: nums = [8,10,2]
# 输出: 10
# 
# 示例 5:
# 输入: nums = [14,70,53,83,49,91,36,80,92,51,66,70]
# 输出: 127
# 
# 提示：
# 1 <= nums.length <= 2 * 10^4
# 0 <= nums[i] <= 2^31 - 1
#
# 解题思路:
# 方法一：暴力解法（O(n²)时间复杂度，不推荐）
# 方法二：前缀树（字典树）优化的位运算方法（O(n)时间复杂度）
# 方法三：基于异或性质的位运算方法（O(n)时间复杂度）
#
# 这里我们主要实现方法二和方法三，它们都是基于位运算的高效解法

# 方法一：暴力解法
def find_maximum_xor_1(nums: list) -> int:
    """
    使用暴力解法计算数组中两个数的最大异或值
    
    参数:
        nums: 整数数组
    
    返回:
        int: 最大异或结果
    
    时间复杂度: O(n²)
    空间复杂度: O(1)
    """
    # 参数验证和边界条件检查
    # 如果数组为空或只有一个元素，最大异或值为0
    if not nums or len(nums) <= 1:
        return 0
    
    # 初始化最大异或结果为0
    max_result = 0
    
    # 遍历所有可能的数对
    # 外层循环遍历第一个数
    for i in range(len(nums)):
        # 内层循环遍历第二个数（j > i避免重复计算和自己与自己异或）
        for j in range(i + 1, len(nums)):
            # 计算当前两个数的异或值
            # ^ 表示按位异或操作
            current_xor = nums[i] ^ nums[j]
            # 更新最大异或结果
            # max返回两个数中的较大值
            max_result = max(max_result, current_xor)
    
    # 返回最大异或结果
    return max_result

# 方法二：基于位运算和集合的方法
def find_maximum_xor_2(nums: list) -> int:
    """
    使用位运算和集合优化计算数组中两个数的最大异或值
    
    参数:
        nums: 整数数组
    
    返回:
        int: 最大异或结果
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    # 参数验证和边界条件检查
    # 如果数组为空或只有一个元素，最大异或值为0
    if not nums or len(nums) <= 1:
        return 0
    
    # 初始化最大异或结果为0
    max_result = 0
    # 初始化掩码为0，用于提取数字的前缀
    mask = 0
    
    # 从最高位到最低位依次确定结果的每一位
    # 31位整数，但符号位为0（因为题目中nums[i] >= 0），所以从第30位开始到第0位
    for i in range(30, -1, -1):
        # 构建当前位的掩码
        # mask |= (1 << i) 将mask的第i位设置为1
        # 这样mask就包含了从最高位到当前位的所有位
        mask |= (1 << i)
        
        # 存储所有数在当前掩码下的前缀
        # 使用set存储前缀，便于快速查找
        prefixes = set()
        # 遍历数组中的每个数
        for num in nums:
            # num & mask 提取num在当前掩码下的前缀
            # 例如：如果mask是11100000，那么num & mask就提取num的高3位
            prefixes.add(num & mask)
        
        # 假设当前位为1，构造可能的最大值
        # max_result | (1 << i) 将max_result的第i位设置为1
        # 这是我们希望得到的最大异或结果
        temp_max = max_result | (1 << i)
        
        # 检查是否存在两个数，它们的前缀异或等于temp_max
        # 遍历所有前缀
        for prefix in prefixes:
            # 如果prefix ^ target = temp_max，那么target = prefix ^ temp_max
            # 这是基于异或运算的性质：如果a ^ b = c，那么a ^ c = b
            # 我们要找是否存在另一个前缀target，使得prefix ^ target = temp_max
            if (prefix ^ temp_max) in prefixes:
                # 找到可行的解，说明最大异或结果的当前位可以为1
                # 设置当前位为1
                max_result = temp_max
                # 找到解后跳出循环，继续处理下一位
                break
        
        # 如果没有找到，当前位保持为0（max_result不变）
        # 这是因为我们初始化temp_max时已经将max_result的当前位设为1
        # 如果找不到匹配的前缀对，说明当前位必须为0
    
    # 返回最大异或结果
    return max_result

# 方法三：前缀树（字典树）方法
class TrieNode:
    """前缀树节点类"""
    def __init__(self):
        # 0和1两个子节点
        # children[0]表示当前位为0的子节点
        # children[1]表示当前位为1的子节点
        self.children = [None, None]  # type: list[TrieNode | None]

def find_maximum_xor_3(nums: list) -> int:
    """
    使用前缀树计算数组中两个数的最大异或值
    
    参数:
        nums: 整数数组
    
    返回:
        int: 最大异或结果
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    # 参数验证和边界条件检查
    # 如果数组为空或只有一个元素，最大异或值为0
    if not nums or len(nums) <= 1:
        return 0
    
    # 构建前缀树
    # 创建前缀树的根节点
    root = TrieNode()
    
    # 向前缀树中插入一个数
    def insert(num):
        # 从根节点开始
        node = root
        # 从最高位到最低位插入
        # 31位整数，忽略符号位，所以从第30位开始到第0位
        for i in range(30, -1, -1):
            # 提取num的第i位
            # (num >> i) & 1 将num右移i位，然后与1进行按位与操作
            # 这样可以提取num的第i位（0或1）
            bit = (num >> i) & 1
            # 如果当前位对应的子节点不存在，则创建新节点
            if not node.children[bit]:
                node.children[bit] = TrieNode()
            # 移动到子节点
            node = node.children[bit]
    
    # 在已有的前缀树中查找与给定数异或结果最大的数
    def search(num):
        # 从根节点开始
        node = root
        # 初始化异或结果为0
        xor = 0
        # 从最高位到最低位查找
        # 31位整数，忽略符号位，所以从第30位开始到第0位
        for i in range(30, -1, -1):
            # 提取num的第i位
            bit = (num >> i) & 1
            # 寻找相反的位以最大化异或结果
            # 1 - bit 可以得到bit的相反值（0变1，1变0）
            desired_bit = 1 - bit
            
            # 如果存在相反的位
            if node.children[desired_bit]:
                # 可以找到相反的位，异或结果的当前位为1
                # xor |= (1 << i) 将xor的第i位设置为1
                xor |= (1 << i)
                # 移动到相反位对应的子节点
                node = node.children[desired_bit]
            else:
                # 找不到相反的位，只能使用相同的位
                # 检查相同位对应的子节点是否存在
                if node.children[bit]:  # 确保子节点存在
                    # 移动到相同位对应的子节点
                    node = node.children[bit]
                else:
                    # 如果子节点不存在，提前结束
                    break
        # 返回异或结果
        return xor
    
    # 先插入第一个数到前缀树中
    insert(nums[0])
    
    # 初始化最大异或结果为0
    max_result = 0
    
    # 对于每个数，插入并查找能产生最大异或值的数
    # 从第二个数开始处理
    for num in nums[1:]:
        # 在前缀树中查找与num异或结果最大的数
        # max更新最大异或结果
        max_result = max(max_result, search(num))
        # 将当前数插入到前缀树中
        insert(num)
    
    # 返回最大异或结果
    return max_result

# 方法四：工程化版本，增加异常处理和参数验证
def find_maximum_xor_with_validation(nums: list) -> int:
    """
    工程化版本，增加异常处理和参数验证
    
    参数:
        nums: 整数数组
    
    返回:
        int: 最大异或结果
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    try:
        # 参数验证
        # 检查输入是否为列表类型
        if not isinstance(nums, list):
            # 抛出类型错误异常
            raise TypeError("Input must be a list")
        
        # 边界情况处理
        # 检查数组长度
        if len(nums) <= 1:
            # 如果数组只有一个元素或为空，最大异或值为0
            return 0
        
        # 验证所有元素是否为非负整数
        for num in nums:
            # 检查元素是否为整数且非负
            if not isinstance(num, int) or num < 0:
                # 抛出值错误异常
                raise ValueError("All elements must be non-negative integers")
        
        # 使用方法二实现
        # 调用find_maximum_xor_2方法计算最大异或值
        return find_maximum_xor_2(nums)
    except Exception as e:
        # 记录异常（在实际应用中可以使用日志）
        # 在生产环境中，应该使用日志框架记录异常
        print(f"Error in find_maximum_xor_with_validation: {e}")
        # 异常情况下返回0
        return 0

# 单元测试
def run_tests():
    """
    运行单元测试
    """
    # 定义测试用例
    # 每个测试用例是一个元组，包含输入数组和期望的输出结果
    test_cases = [
        ([3, 10, 5, 25, 2, 8], 28),      # 示例1
        ([0], 0),                        # 示例2
        ([2, 4], 6),                     # 示例3
        ([8, 10, 2], 10),                # 示例4
        ([14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70], 127)  # 示例5
    ]
    
    print("Running unit tests...\n")
    
    # 定义所有测试方法
    methods = [
        (find_maximum_xor_1, "Method 1 (Brute Force)"),
        (find_maximum_xor_2, "Method 2 (Bitwise with Set)"),
        (find_maximum_xor_3, "Method 3 (Trie)"),
        (find_maximum_xor_with_validation, "Method 4 (With Validation)")
    ]
    
    # 测试所有方法
    for method, method_name in methods:
        print(f"Testing {method_name}:")
        # 遍历所有测试用例
        for nums, expected in test_cases:
            # 调用被测试的方法
            result = method(nums)
            # 判断测试结果是否正确
            status = "PASS" if result == expected else "FAIL"
            # 输出测试结果
            print(f"  {nums} -> {result} (Expected: {expected}) - {status}")
        print()

# 性能测试
def performance_test():
    """
    性能测试
    """
    import time
    
    # 生成大规模测试数据
    # 创建一个包含0到9999的整数数组
    large_nums = [i for i in range(10000)]
    
    # 仅在小规模数据上测试方法1（避免超时）
    small_nums = [3, 10, 5, 25, 2, 8]
    # 记录开始时间
    start_time = time.time()
    # 调用方法1计算结果
    result1 = find_maximum_xor_1(small_nums)
    # 记录结束时间
    end_time = time.time()
    print(f"Performance of Method 1 (Brute Force): {((end_time - start_time) * 1000):.2f} ms")
    print(f"Result: {result1}\n")
    
    # 测试方法2
    # 记录开始时间
    start_time = time.time()
    # 调用方法2计算结果
    result2 = find_maximum_xor_2(large_nums)
    # 记录结束时间
    end_time = time.time()
    print(f"Performance of Method 2 (Bitwise with Set): {((end_time - start_time) * 1000):.2f} ms")
    print(f"Result: {result2}\n")
    
    # 测试方法3
    # 记录开始时间
    start_time = time.time()
    # 调用方法3计算结果
    result3 = find_maximum_xor_3(large_nums)
    # 记录结束时间
    end_time = time.time()
    print(f"Performance of Method 3 (Trie): {((end_time - start_time) * 1000):.2f} ms")
    print(f"Result: {result3}\n")
    
    # 测试方法4
    # 记录开始时间
    start_time = time.time()
    # 调用方法4计算结果
    result4 = find_maximum_xor_with_validation(large_nums)
    # 记录结束时间
    end_time = time.time()
    print(f"Performance of Method 4 (With Validation): {((end_time - start_time) * 1000):.2f} ms")
    print(f"Result: {result4}\n")

# 主函数
if __name__ == "__main__":
    print("LeetCode 421. 数组中两个数的最大异或值")
    print("使用位运算优化实现\n")
    
    # 运行单元测试
    run_tests()
    
    # 运行性能测试
    performance_test()
    
    # 复杂度分析
    print("复杂度分析:")
    print("方法一（暴力解法）:")
    print("  时间复杂度: O(n²)，其中n是数组长度")
    print("  空间复杂度: O(1)")
    print("  优点: 实现简单")
    print("  缺点: 对于大数组效率低")
    
    print("\n方法二（基于位运算和集合）:")
    print("  时间复杂度: O(n)，每个位处理需要O(n)时间，总共处理32个位")
    print("  空间复杂度: O(n)，用于存储前缀集合")
    print("  优点: 时间效率高，实现相对简单")
    
    print("\n方法三（前缀树）:")
    print("  时间复杂度: O(n)，构建树和查询都是O(n)时间")
    print("  空间复杂度: O(n)，用于存储前缀树")
    print("  优点: 位操作思想清晰，扩展性好")
    
    print("\n适用场景总结:")
    print("1. 对于小数组，可以使用暴力解法")
    print("2. 对于大数组，应使用方法二或方法三，它们的时间复杂度都是O(n)")
    print("3. 在工程实践中，方法二实现更简洁，而方法三更能体现位运算的思想")
    print("4. 当需要处理大量相似查询时，前缀树方法更具优势")