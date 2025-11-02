# LeetCode 面试题 01.01. 判定字符是否唯一
# 题目链接: https://leetcode-cn.com/problems/is-unique-lcci/
# 题目大意:
# 实现一个算法，确定一个字符串 s 的所有字符是否全都不同。
# 
# 示例 1:
# 输入: s = "leetcode"
# 输出: false
# 
# 示例 2:
# 输入: s = "abc"
# 输出: true
# 
# 限制:
# 0 <= len(s) <= 100
# 如果你不使用额外的数据结构，会很加分。
#
# 解题思路:
# 使用位运算优化的方法:
# 1. 由于字符集可能是ASCII或Unicode，但题目中通常假设是小写字母或有限范围
# 2. 我们可以使用一个整数或位集合来表示每个字符是否出现过
# 3. 对于每个字符，检查对应的位是否已经被设置，如果是则返回false，否则设置该位
#
# 时间复杂度: O(n)，其中n是字符串的长度
# 空间复杂度: O(1)，使用了固定大小的位集合或整数

# 方法1: 使用集合实现
# 优点: 简单直观，适用于任意字符集
# 缺点: 使用了额外的数据结构

def is_unique_1(s: str) -> bool:
    """
    使用集合判断字符是否唯一
    
    参数:
        s: 输入字符串
    
    返回:
        bool: 如果所有字符都唯一返回True，否则返回False
    
    时间复杂度: O(n)
    空间复杂度: O(k)，其中k是字符集大小，最坏情况下为O(n)
    """
    # 边界条件检查
    # 如果字符串为None或为空，认为所有字符都唯一
    if s is None or len(s) == 0:
        return True
    
    # 使用集合存储已出现的字符
    # 集合的查找和插入操作平均时间复杂度为O(1)
    seen = set()
    
    # 遍历字符串中的每个字符
    for char in s:
        # 检查字符是否已经在集合中
        # char in seen 的时间复杂度为O(1)
        if char in seen:
            # 如果字符已经出现过，说明有重复
            return False
        # 将字符添加到集合中
        # seen.add(char) 的时间复杂度为O(1)
        seen.add(char)
    
    # 所有字符都不重复
    return True

# 方法2: 使用位运算模拟Bitset（仅适用于小写字母a-z）
# 优点: 空间效率更高，不使用额外的数据结构
# 缺点: 仅适用于小写字母范围

def is_unique_2(s: str) -> bool:
    """
    使用位运算判断字符是否唯一（仅适用于小写字母）
    
    参数:
        s: 输入字符串（假设只包含小写字母）
    
    返回:
        bool: 如果所有字符都唯一返回True，否则返回False
    
    时间复杂度: O(n)
    空间复杂度: O(1)，仅使用一个整数
    """
    # 边界条件检查
    # 如果字符串为None或为空，认为所有字符都唯一
    if s is None or len(s) == 0:
        return True
    
    # 鸽巢原理：如果字符串长度超过字母表大小，必然有重复
    # 小写字母只有26个，如果字符串长度超过26，必然有重复字符
    if len(s) > 26:
        return False
    
    # 使用整数的二进制位来存储字符出现情况
    # 使用一个整数的低26位来表示字符a-z是否出现
    checker = 0
    
    # 遍历字符串中的每个字符
    for char in s:
        # 检查字符是否为小写字母
        # 如果不是小写字母，回退到方法1处理任意字符集
        if not 'a' <= char <= 'z':
            # 回退到集合方法，处理任意字符集
            return is_unique_1(s)
        
        # 计算字符对应的位位置
        # ord(char)获取字符的ASCII码值
        # ord('a')获取字符'a'的ASCII码值
        # bit表示字符char在checker中的位位置（0-25）
        bit = ord(char) - ord('a')
        
        # 检查该位是否已经被设置
        # (1 << bit) 是将1左移bit位，创建一个只有第bit位为1的数
        # checker & (1 << bit) 按位与操作，检查checker的第bit位是否为1
        # 如果结果大于0，说明该位已经被设置，即字符重复
        if (checker & (1 << bit)) > 0:
            # 字符重复，返回False
            return False
        
        # 设置该位为1
        # checker |= (1 << bit) 按位或操作，将checker的第bit位设置为1
        checker |= (1 << bit)
    
    # 所有字符都不重复
    return True

# 方法3: 不使用额外数据结构（排序后比较相邻元素）
# 优点: 不使用额外的数据结构
# 缺点: 时间复杂度较高

def is_unique_3(s: str) -> bool:
    """
    通过排序后比较相邻元素判断字符是否唯一
    
    参数:
        s: 输入字符串
    
    返回:
        bool: 如果所有字符都唯一返回True，否则返回False
    
    时间复杂度: O(n log n)，排序的时间复杂度
    空间复杂度: O(n)，用于存储排序后的字符数组
    """
    # 边界条件检查
    # 如果字符串为None或为空，认为所有字符都唯一
    if s is None or len(s) == 0:
        return True
    
    # 鸽巢原理
    # 如果字符串长度超过字符集大小，必然有重复
    # 假设是ASCII字符集，最多有256个不同的字符
    if len(s) > 256:
        return False
    
    # 排序字符串
    # sorted(s)返回一个新的排序后的字符列表
    # 时间复杂度为O(n log n)
    sorted_chars = sorted(s)
    
    # 检查相邻字符是否相同
    # 遍历排序后的字符数组，比较相邻元素
    for i in range(len(sorted_chars) - 1):
        # 如果相邻字符相同，说明有重复
        if sorted_chars[i] == sorted_chars[i + 1]:
            return False
    
    # 所有字符都不重复
    return True

# 方法4: 工程化版本，增加异常处理和参数验证
def is_unique_with_validation(s: str) -> bool:
    """
    工程化版本，增加异常处理和参数验证
    
    参数:
        s: 输入字符串
    
    返回:
        bool: 如果所有字符都唯一返回True，否则返回False
    
    时间复杂度: O(n)
    空间复杂度: O(1)，使用固定大小的数组
    """
    try:
        # 参数验证
        # 检查输入参数是否为None
        if s is None:
            # 抛出异常，说明输入参数不能为None
            raise ValueError("Input string cannot be None")
        
        # 鸽巢原理快速判断
        # 如果字符串长度超过字符集大小，必然有重复
        # 假设使用扩展ASCII字符集，最多128个字符
        if len(s) > 128:
            return False
        
        # 使用固定大小的布尔数组（模拟Bitset）
        # 适用于ASCII字符（0-127）
        # 创建一个大小为128的布尔数组，初始化为False
        char_set = [False] * 128
        
        # 遍历字符串中的每个字符
        for char in s:
            # 获取字符的ASCII码值
            # ord(char)返回字符的ASCII码值
            val = ord(char)
            
            # 检查是否超出处理范围
            # 如果字符的ASCII码值超过127，说明是扩展ASCII或Unicode字符
            if val >= 128:
                # 对于扩展ASCII或Unicode字符，回退到集合方法处理
                return is_unique_1(s)
            
            # 检查字符是否已经出现过
            # char_set[val]为True表示字符已经出现过
            if char_set[val]:
                # 字符重复，返回False
                return False
            
            # 标记字符已出现
            # 将char_set[val]设置为True，表示字符已经出现
            char_set[val] = True
        
        # 所有字符都不重复
        return True
    
    except Exception as e:
        # 记录异常（在实际应用中可以使用日志）
        # 在生产环境中，应该使用日志框架记录异常
        print(f"Error in is_unique_with_validation: {e}")
        # 异常情况下保守返回False
        return False

# 单元测试
def run_tests():
    """
    运行单元测试
    """
    # 定义测试用例
    # 每个测试用例是一个元组，包含输入字符串和期望的输出结果
    test_cases = [
        ("leetcode", False),      # 有重复字符
        ("abc", True),            # 无重复字符
        ("", True),               # 空字符串
        ("AbCdEfG", True),        # 包含大小写字母
        ("a", True),              # 单个字符
        ("abcdefghijklmnopqrstuvwxyz", True),   # 包含所有小写字母
        ("abcdefghijklmnopqrstuvwxyzabc", False) # 有重复字符
    ]
    
    print("Running unit tests...\n")
    
    # 定义所有测试方法
    methods = [
        (is_unique_1, "Method 1 (Set)"),
        (is_unique_2, "Method 2 (Bitwise)"),
        (is_unique_3, "Method 3 (Sorting)"),
        (is_unique_with_validation, "Method 4 (With Validation)")
    ]
    
    # 测试所有方法
    for method, method_name in methods:
        print(f"Testing {method_name}:")
        # 遍历所有测试用例
        for s, expected in test_cases:
            # 调用被测试的方法
            result = method(s)
            # 判断测试结果是否正确
            status = "PASS" if result == expected else "FAIL"
            # 输出测试结果
            print(f"  '{s}' -> {result} (Expected: {expected}) - {status}")
        print()

# 性能测试
def performance_test():
    """
    性能测试
    """
    import time
    
    # 生成测试数据
    # 1. 长字符串，所有字符唯一
    # 创建一个包含所有小写字母的字符串
    unique_str = ''.join(chr(ord('a') + i) for i in range(26))
    
    # 2. 长字符串，有重复字符
    # 在唯一字符串的基础上添加一个重复字符
    duplicate_str = unique_str + 'a'
    
    # 定义所有测试方法
    methods = [
        (is_unique_1, "Method 1 (Set)"),
        (is_unique_2, "Method 2 (Bitwise)"),
        (is_unique_3, "Method 3 (Sorting)"),
        (is_unique_with_validation, "Method 4 (With Validation)")
    ]
    
    print("Running performance tests...\n")
    
    # 测试每种方法的性能
    for method, method_name in methods:
        print(f"Performance of {method_name}:")
        
        # 测试唯一字符串
        # 记录开始时间
        start_time = time.time()
        # 执行多次测试以获得更准确的结果
        iterations = 10000
        # 循环执行方法
        for _ in range(iterations):
            method(unique_str)
        # 记录结束时间
        end_time = time.time()
        # 计算平均执行时间（转换为微秒）
        avg_time = (end_time - start_time) * 1000000 / iterations
        print(f"  Unique string average time: {avg_time:.2f} μs")
        
        # 测试重复字符串
        # 记录开始时间
        start_time = time.time()
        # 循环执行方法
        for _ in range(iterations):
            method(duplicate_str)
        # 记录结束时间
        end_time = time.time()
        # 计算平均执行时间（转换为微秒）
        avg_time = (end_time - start_time) * 1000000 / iterations
        print(f"  Duplicate string average time: {avg_time:.2f} μs")
        print()

# 主函数
if __name__ == "__main__":
    print("LeetCode 面试题 01.01. 判定字符是否唯一")
    print("使用位运算优化实现\n")
    
    # 运行单元测试
    run_tests()
    
    # 运行性能测试
    performance_test()
    
    # 复杂度分析
    print("复杂度分析:")
    print("位运算方法 (is_unique_2):")
    print("  时间复杂度: O(n)，其中n是字符串的长度")
    print("  空间复杂度: O(1)，仅使用一个整数存储位信息")
    print("  优势: 不需要额外的数据结构，空间效率高")
    print("  限制: 仅适用于有限范围的字符（如小写字母a-z）")
    
    print("\n适用场景总结:")
    print("1. 当输入字符集较小时（如只有小写字母），位运算方法效率最高")
    print("2. 当输入字符集较大时，集合方法更通用")
    print("3. 当不允许使用额外数据结构时，排序方法是一种选择，但效率较低")
    print("4. 在工程实践中，应根据具体场景选择合适的方法，并考虑异常处理和边界情况")