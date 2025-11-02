import random
from collections import defaultdict

# 插入、删除和获取随机元素O(1)时间且允许有重复数字的结构
'''
一、题目解析
设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构（允许重复元素）：
1. insert(val): 将一个元素val插入到集合中，返回true
2. remove(val): 如果元素val存在，则从中删除一个实例，返回true，否则返回false
3. getRandom: 随机返回集合中的一个元素，每个元素被返回的概率与其在集合中的数量成线性关系

二、算法思路
与不允许重复的版本相比，主要变化在于需要处理重复元素：
1. 使用数组(list)存储所有元素，实现O(1)时间复杂度的随机访问
2. 使用字典(defaultdict)存储元素值到其在数组中索引集合的映射
3. 插入操作：在数组末尾添加元素，并在字典中记录其索引
4. 删除操作：将要删除的元素与数组末尾元素交换，然后删除末尾元素，更新字典
5. 随机获取：使用random模块随机生成索引，访问数组中对应元素

三、时间复杂度分析
insert操作: O(1) - 数组末尾插入 + 字典更新
remove操作: O(1) - 字典查找 + 数组元素交换 + 数组末尾删除 + 字典更新
getRandom操作: O(1) - 随机索引生成 + 数组访问

四、空间复杂度分析
O(n) - n为集合中元素个数，需要数组和字典分别存储元素和索引映射

五、工程化考量
1. 异常处理: 处理空集合的getRandom操作
2. 边界场景: 空集合、单元素集合等
3. 随机性: 确保getRandom方法能真正按概率返回每个元素

六、相关题目扩展
1. LeetCode 381. 常数时间插入、删除和获取随机元素-允许重复 (本题)
2. LeetCode 380. 常数时间插入、删除和获取随机元素
3. 牛客网相关题目
'''

class RandomizedCollection:
    def __init__(self):
        """构造函数"""
        # 字典存储元素值到其在数组中索引集合的映射
        self.map = defaultdict(set)
        # 数组存储所有元素值
        self.arr = []

    def insert(self, val: int) -> bool:
        """
        插入元素
        :param val: 要插入的元素
        :return: 总是返回true
        时间复杂度: O(1)
        """
        # 在数组末尾添加元素
        self.arr.append(val)
        # 将新索引添加到该元素值对应的索引集合中
        self.map[val].add(len(self.arr) - 1)
        # 当且仅当该元素第一次插入时返回true
        return len(self.map[val]) == 1

    def remove(self, val: int) -> bool:
        """
        删除元素
        :param val: 要删除的元素
        :return: 如果元素存在则删除并返回true，否则返回false
        时间复杂度: O(1)
        """
        # 检查元素是否存在
        if val not in self.map:
            return False
        # 获取该元素值对应的索引集合
        val_set = self.map[val]
        # 获取其中一个索引（任意一个）
        val_any_index = next(iter(val_set))
        # 获取数组末尾元素的值
        end_value = self.arr[-1]
        # 如果要删除的元素就是末尾元素
        if val == end_value:
            # 直接从索引集合中删除该索引
            val_set.discard(len(self.arr) - 1)
        else:
            # 获取末尾元素值对应的索引集合
            end_value_set = self.map[end_value]
            # 将末尾元素的索引更新为要删除元素的索引
            end_value_set.add(val_any_index)
            # 更新数组中要删除元素位置的值为末尾元素值
            self.arr[val_any_index] = end_value
            # 从末尾元素的索引集合中删除原末尾索引
            end_value_set.discard(len(self.arr) - 1)
            # 从要删除元素的索引集合中删除该索引
            val_set.discard(val_any_index)
        # 删除数组末尾元素
        self.arr.pop()
        # 如果要删除元素的索引集合为空，则从字典中删除该元素
        if not val_set:
            del self.map[val]
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
    collection = RandomizedCollection()
    
    # 简单测试
    print("insert(1):", collection.insert(1))  # True
    print("insert(1):", collection.insert(1))  # False
    print("insert(2):", collection.insert(2))  # True
    print("remove(1):", collection.remove(1))  # True
    print("getRandom:", collection.getRandom()) # 1 or 2