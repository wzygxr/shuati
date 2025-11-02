# SPOJ PHONELST（电话列表） - Python实现
# 
# 题目描述：
# 给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
# 如果存在这样的号码对，输出"NO"；否则输出"YES"。
# 
# 测试链接：https://www.spoj.com/problems/PHONELST/
# 
# 算法思路：
# 1. 使用字典实现前缀树存储所有电话号码
# 2. 在插入过程中检查是否存在前缀关系：
#    a. 如果当前号码是某个已存在号码的前缀，返回false
#    b. 如果某个已存在号码是当前号码的前缀，返回false
# 3. 如果所有号码都成功插入且没有前缀冲突，返回true
# 
# 核心优化：
# 在插入过程中实时检查前缀关系，避免了插入完成后再进行全局检查
# 
# 时间复杂度分析：
# - 构建前缀树：O(∑len(numbers[i]))，其中∑len(numbers[i])是所有号码长度之和
# - 检查前缀关系：O(∑len(numbers[i]))
# - 总体时间复杂度：O(∑len(numbers[i]))
# 
# 空间复杂度分析：
# - 前缀树空间：O(∑len(numbers[i]))，用于存储所有号码
# - 总体空间复杂度：O(∑len(numbers[i]))
# 
# 是否最优解：是
# 理由：使用前缀树可以在线性时间内检测前缀关系
# 
# 工程化考虑：
# 1. 异常处理：输入为空或号码为空的情况
# 2. 边界情况：号码列表为空或只有一个号码的情况
# 3. 极端输入：大量号码或号码很长的情况
# 4. 鲁棒性：处理重复号码和非法字符
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现前缀树，性能较高但空间固定
# C++：可使用指针实现前缀树节点，更节省空间
# 
# 相关题目扩展：
# 1. SPOJ PHONELST（电话列表） (本题)
# 2. LeetCode 208. 实现 Trie (前缀树)
# 3. UVa 11362 - Phone List
# 4. HDU 1671 - Phone List
# 5. LintCode 1427. 前缀统计
# 6. CodeChef - PHONENUM
# 7. AtCoder - Phone Number List

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持数字字符集
    包含单词结尾标记
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        # 子节点字典，键为字符，值为TrieNode
        self.children = {}
        # 标记该节点是否是单词结尾
        self.is_end = False

class PhoneList:
    """
    电话列表一致性检查类
    
    算法思路：
    使用TrieNode构建树结构，支持号码的存储和前缀关系检查
    
    时间复杂度分析：
    - 构建：O(∑len(numbers[i]))
    - 检查：O(∑len(numbers[i]))
    """
    
    def __init__(self):
        """
        初始化电话列表检查系统
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
    
    def insert(self, number):
        """
        向前缀树中插入电话号码并检查前缀关系
        
        算法步骤：
        1. 从根节点开始遍历号码的每个字符
        2. 对于每个字符，如果子节点不存在则创建
        3. 检查当前节点是否已经是单词结尾（当前号码是已存在号码的前缀）
        4. 移动到子节点，继续处理下一个字符
        5. 号码遍历完成后，检查当前节点是否有子节点（已存在号码是当前号码的前缀）
        6. 如果存在前缀关系，返回false；否则标记当前节点为单词结尾，返回true
        
        时间复杂度：O(L)，其中L是号码长度
        空间复杂度：O(L)，最坏情况下需要创建新节点
        
        :param number: 待插入的电话号码
        :return: 如果没有前缀冲突返回True，否则返回False
        """
        if not number:
            return True  # 空字符串不插入
        
        node = self.root
        for char in number:
            # 检查当前节点是否已经是单词结尾（当前号码是已存在号码的前缀）
            if node.is_end:
                return False
            
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        
        # 检查当前节点是否有子节点（已存在号码是当前号码的前缀）
        if node.children:
            return False
        
        node.is_end = True
        return True
    
    def is_consistent(self, numbers):
        """
        检查电话号码列表是否一致
        
        算法步骤：
        1. 初始化前缀树
        2. 遍历号码列表中的每个号码：
           a. 调用insert方法插入号码并检查前缀关系
           b. 如果发现前缀冲突，返回false
        3. 如果所有号码都成功插入且没有前缀冲突，返回true
        
        时间复杂度：O(∑len(numbers[i]))
        空间复杂度：O(∑len(numbers[i]))
        
        :param numbers: 电话号码列表
        :return: 如果一致返回True，否则返回False
        """
        self.root = TrieNode()  # 重新初始化
        
        for number in numbers:
            if not self.insert(number):
                return False
        
        return True

def phone_list(test_cases):
    """
    处理电话列表问题
    
    算法步骤：
    1. 对每个测试用例调用is_consistent方法检查一致性
    2. 根据结果返回"YES"或"NO"
    
    时间复杂度：O(T * ∑len(numbers[i]))，其中T是测试用例数量
    空间复杂度：O(∑len(numbers[i]))
    
    :param test_cases: 测试用例列表
    :return: 结果列表
    """
    result = []
    phone_system = PhoneList()
    
    for numbers in test_cases:
        if phone_system.is_consistent(numbers):
            result.append("YES")
        else:
            result.append("NO")
    
    return result

def test_phone_list():
    """
    单元测试函数
    
    测试用例设计：
    1. 一致列表：验证一致情况处理
    2. 不一致列表：验证不一致情况处理
    3. 前缀关系：验证前缀检测功能
    4. 边界情况：验证空列表和单号码处理
    """
    # 测试用例1：不一致列表（存在前缀关系）
    numbers1 = ["911", "97625999", "91125426"]
    phone_system1 = PhoneList()
    assert not phone_system1.is_consistent(numbers1), "测试用例1失败"
    
    # 测试用例2：一致列表
    numbers2 = ["113", "12340", "123440", "12345", "98346"]
    phone_system2 = PhoneList()
    assert phone_system2.is_consistent(numbers2), "测试用例2失败"
    
    # 测试用例3：空列表
    numbers3 = []
    phone_system3 = PhoneList()
    assert phone_system3.is_consistent(numbers3), "测试用例3失败"
    
    # 测试用例4：单号码
    numbers4 = ["123"]
    phone_system4 = PhoneList()
    assert phone_system4.is_consistent(numbers4), "测试用例4失败"
    
    print("SPOJ PHONELST 所有测试用例通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 构建大型号码列表
    2. 执行一致性检查
    """
    import time
    
    n = 100000
    numbers = [str(100000 + i) for i in range(n)]
    
    phone_system = PhoneList()
    
    start_time = time.time()
    result = phone_system.is_consistent(numbers)
    end_time = time.time()
    
    print(f"处理{n}个号码耗时: {end_time - start_time:.3f}秒")
    print(f"结果: {'YES' if result else 'NO'}")

if __name__ == "__main__":
    # 运行单元测试
    test_phone_list()
    
    # 运行性能测试
    performance_test()