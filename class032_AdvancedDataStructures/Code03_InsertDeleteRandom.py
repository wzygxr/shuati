import random

# 插入、删除和获取随机元素O(1)时间的结构
'''
一、题目解析
设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构：
1. insert(val): 当元素val不存在时返回true，并向集合中插入该项，否则返回false
2. remove(val): 元素val存在时，从集合中移除该项，返回true，否则返回false
3. getRandom: 随机返回现有集合中的一项，每个元素应该有相同的概率被返回

二、算法思路
1. 使用数组(list)存储元素，实现O(1)时间复杂度的随机访问
2. 使用哈希表(dict)存储元素值到其在数组中索引的映射，实现O(1)时间复杂度的查找
3. 插入操作：直接在数组末尾添加元素，并在哈希表中记录其索引
4. 删除操作：将要删除的元素与数组末尾元素交换，然后删除末尾元素，更新哈希表
5. 随机获取：使用random模块随机生成索引，访问数组中对应元素

三、时间复杂度分析
insert操作: O(1) - 数组末尾插入 + 哈希表插入
remove操作: O(1) - 哈希表查找 + 数组元素交换 + 数组末尾删除 + 哈希表更新
getRandom操作: O(1) - 随机索引生成 + 数组访问

四、空间复杂度分析
O(n) - n为集合中元素个数，需要数组和哈希表分别存储元素和索引映射

五、工程化考量
1. 异常处理: 处理空集合的getRandom操作
2. 边界场景: 空集合、单元素集合等
3. 随机性: 确保getRandom方法能真正等概率返回每个元素
4. 内存管理: Python有自动垃圾回收机制

六、相关题目扩展
1. LeetCode 380. 常数时间插入、删除和获取随机元素 (本题)
2. LeetCode 381. 常数时间插入、删除和获取随机元素-允许重复
'''

class RandomizedSet:
    def __init__(self):
        """构造函数"""
        self.map = {}  # 哈希表存储元素值到其在数组中索引的映射
        self.arr = []  # 数组存储元素值

    def insert(self, val: int) -> bool:
        """
        插入元素
        :param val: 要插入的元素
        :return: 如果元素不存在则插入并返回True，否则返回False
        时间复杂度: O(1)
        """
        # 检查元素是否已存在
        if val in self.map:
            return False
        # 在数组末尾添加元素
        self.map[val] = len(self.arr)
        self.arr.append(val)
        return True

    def remove(self, val: int) -> bool:
        """
        删除元素
        :param val: 要删除的元素
        :return: 如果元素存在则删除并返回True，否则返回False
        时间复杂度: O(1)
        """
        # 检查元素是否存在
        if val not in self.map:
            return False
        # 获取要删除元素的索引
        val_index = self.map[val]
        # 获取数组末尾元素的值
        end_value = self.arr[-1]
        # 将末尾元素放到要删除元素的位置
        self.map[end_value] = val_index
        self.arr[val_index] = end_value
        # 删除末尾元素
        del self.map[val]
        self.arr.pop()
        return True

    def getRandom(self) -> int:
        """
        随机获取元素
        :return: 随机返回集合中的一个元素
        时间复杂度: O(1)
        """
        # 检查集合是否为空
        if not self.arr:
            raise Exception("集合为空，无法获取随机元素")
        # 随机返回数组中的一个元素
        return random.choice(self.arr)

# 测试代码
if __name__ == "__main__":
    randomizedSet = RandomizedSet()
    
    # 测试用例: ["RandomizedSet", "insert", "remove", "insert", "getRandom", "remove", "insert", "getRandom"]
    #           [[], [1], [2], [2], [], [1], [2], []]
    
    print("insert(1):", randomizedSet.insert(1))     # True
    print("remove(2):", randomizedSet.remove(2))     # False
    print("insert(2):", randomizedSet.insert(2))     # True
    print("getRandom:", randomizedSet.getRandom())   # 1或2
    print("remove(1):", randomizedSet.remove(1))     # True
    print("insert(2):", randomizedSet.insert(2))     # False
    print("getRandom:", randomizedSet.getRandom())   # 2