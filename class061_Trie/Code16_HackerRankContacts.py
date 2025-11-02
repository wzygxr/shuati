# HackerRank Contacts（联系人） - Python实现
# 
# 题目描述：
# 我们要制作自己的通讯录应用程序！该应用程序必须执行两种类型的操作：
# 1. add name：添加联系人
# 2. find partial：查找以指定前缀开头的联系人数量
# 
# 测试链接：https://www.hackerrank.com/challenges/contacts/problem
# 
# 算法思路：
# 1. 使用字典实现前缀树存储所有联系人姓名
# 2. 每个节点记录经过该节点的字符串数量
# 3. 添加联系人时，沿路径增加计数
# 4. 查找前缀时，返回前缀末尾节点的计数
# 
# 核心优化：
# 在前缀树节点中维护经过计数，可以在O(L)时间内完成查找操作
# 
# 时间复杂度分析：
# - 添加联系人：O(L)，其中L是姓名长度
# - 查找前缀：O(L)，其中L是前缀长度
# - 总体时间复杂度：O(N*L)，其中N是操作数量，L是平均字符串长度
# 
# 空间复杂度分析：
# - 前缀树空间：O(N*L)，用于存储所有联系人
# - 总体空间复杂度：O(N*L)
# 
# 是否最优解：是
# 理由：使用前缀树可以高效地处理前缀查询操作
# 
# 工程化考虑：
# 1. 异常处理：输入为空或姓名为空的情况
# 2. 边界情况：没有匹配联系人的情况
# 3. 极端输入：大量联系人或姓名很长的情况
# 4. 鲁棒性：处理重复姓名和特殊字符
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现前缀树，性能较高但空间固定
# C++：可使用指针实现前缀树节点，更节省空间
# 
# 相关题目扩展：
# 1. HackerRank Contacts（联系人） (本题)
# 2. LeetCode 208. 实现 Trie (前缀树)
# 3. LeetCode 677. 键值映射
# 4. 牛客网 NC141. 判断是否为回文字符串
# 5. LintCode 442. 实现前缀树
# 6. CodeChef - CONTACTS
# 7. SPOJ - CONTACT
# 8. AtCoder - Contact List

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持任意字符集
    包含经过该节点的字符串数量
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        # 子节点字典，键为字符，值为TrieNode
        self.children = {}
        # 经过该节点的字符串数量
        self.pass_count = 0

class Contacts:
    """
    联系人管理系统类
    
    算法思路：
    使用TrieNode构建树结构，支持联系人的添加和前缀查询
    
    时间复杂度分析：
    - 添加：O(L)，L为姓名长度
    - 查询：O(L)，L为前缀长度
    """
    
    def __init__(self):
        """
        初始化联系人管理系统
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
    
    def add(self, name):
        """
        添加联系人
        
        算法步骤：
        1. 从根节点开始遍历姓名的每个字符
        2. 对于每个字符，如果子节点不存在则创建
        3. 移动到子节点，增加通过计数
        4. 姓名遍历完成后，操作完成
        
        时间复杂度：O(L)，其中L是姓名长度
        空间复杂度：O(L)，最坏情况下需要创建新节点
        
        :param name: 联系人姓名
        """
        if not name:
            return  # 空字符串不添加
        
        node = self.root
        node.pass_count += 1
        
        for char in name:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.pass_count += 1
    
    def find(self, partial):
        """
        查找以指定前缀开头的联系人数量
        
        算法步骤：
        1. 从根节点开始遍历前缀的每个字符
        2. 对于每个字符，如果子节点不存在，说明没有匹配的联系人，返回0
        3. 移动到子节点，继续处理下一个字符
        4. 前缀遍历完成后，返回当前节点的通过计数
        
        时间复杂度：O(L)，其中L是前缀长度
        空间复杂度：O(1)
        
        :param partial: 前缀
        :return: 匹配的联系人数量
        """
        if not partial:
            return self.root.pass_count  # 空前缀匹配所有联系人
        
        node = self.root
        for char in partial:
            if char not in node.children:
                return 0
            node = node.children[char]
        
        return node.pass_count

def contacts(operations):
    """
    处理联系人操作
    
    算法步骤：
    1. 创建联系人管理系统
    2. 遍历所有操作：
       a. 如果是add操作，调用add方法添加联系人
       b. 如果是find操作，调用find方法查找联系人数量
    3. 收集find操作的结果
    
    时间复杂度：O(N*L)，其中N是操作数量，L是平均字符串长度
    空间复杂度：O(N*L)
    
    :param operations: 操作列表
    :return: find操作的结果列表
    """
    contact_system = Contacts()
    result = []
    
    for operation in operations:
        op = operation[0]
        param = operation[1]
        
        if op == "add":
            contact_system.add(param)
        elif op == "find":
            result.append(contact_system.find(param))
    
    return result

def test_contacts():
    """
    单元测试函数
    
    测试用例设计：
    1. 正常添加和查找：验证基本功能正确性
    2. 前缀匹配：验证前缀查询功能
    3. 重复姓名：验证重复处理
    4. 空输入处理：验证边界条件处理
    """
    # 测试用例1：正常添加和查找
    operations1 = [
        ["add", "hack"],
        ["add", "hackerrank"],
        ["find", "hac"],
        ["find", "hak"]
    ]
    result1 = contacts(operations1)
    expected1 = [2, 0]
    assert result1 == expected1, f"测试用例1失败: 期望 {expected1}, 实际 {result1}"
    
    # 测试用例2：重复姓名
    operations2 = [
        ["add", "s"],
        ["add", "ss"],
        ["add", "sss"],
        ["add", "ssss"],
        ["add", "sssss"],
        ["find", "s"],
        ["find", "ss"],
        ["find", "sss"]
    ]
    result2 = contacts(operations2)
    expected2 = [5, 4, 3]
    assert result2 == expected2, f"测试用例2失败: 期望 {expected2}, 实际 {result2}"
    
    # 测试用例3：空输入处理
    operations3 = [
        ["find", ""]
    ]
    result3 = contacts(operations3)
    expected3 = [0]
    assert result3 == expected3, f"测试用例3失败: 期望 {expected3}, 实际 {result3}"
    
    print("HackerRank Contacts 所有测试用例通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 添加大量联系人
    2. 执行大量查找操作
    """
    import time
    
    n = 100000
    operations = []
    
    # 添加操作
    for i in range(n):
        operations.append(["add", f"name{i}"])
    
    # 查找操作
    for i in range(n):
        operations.append(["find", "name"])
    
    start_time = time.time()
    result = contacts(operations)
    end_time = time.time()
    
    print(f"处理{n * 2}个操作耗时: {end_time - start_time:.3f}秒")
    print(f"结果数量: {len(result)}")

if __name__ == "__main__":
    # 运行单元测试
    test_contacts()
    
    # 运行性能测试
    performance_test()