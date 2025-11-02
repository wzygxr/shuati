# 全O(1)的数据结构
'''
一、题目解析
设计一个数据结构支持以下操作，所有操作的时间复杂度都为O(1)：
1. inc(key): 将key的计数增加1，如果key不存在则插入计数为1的key
2. dec(key): 将key的计数减少1，如果计数变为0则删除key
3. getMaxKey(): 返回计数最大的任意一个key，如果不存在返回空字符串
4. getMinKey(): 返回计数最小的任意一个key，如果不存在返回空字符串

二、算法思路
使用双向链表+字典的组合数据结构：
1. 双向链表节点存储计数值和拥有该计数值的所有key集合
2. 字典存储key到链表节点的映射
3. 维护头尾哨兵节点简化边界处理
4. 保证链表按计数值单调递增排列

三、时间复杂度分析
所有操作: O(1) - 字典操作和链表节点操作都是O(1)

四、空间复杂度分析
O(n) - n为不同key的个数，需要链表节点和字典存储相关信息

五、工程化考量
1. 异常处理: 处理空数据结构的getMaxKey和getMinKey操作
2. 边界场景: 空数据结构、单元素数据结构等

六、相关题目扩展
1. LeetCode 432. 全O(1)的数据结构 (本题)
2. LeetCode 146. LRU缓存
3. 牛客网相关设计题目
'''

from typing import Optional

# 定义链表节点类
class Bucket:
    def __init__(self, s: str, c: float):
        self.set = {s} if s else set()
        self.cnt = c
        self.last: Optional['Bucket'] = None
        self.next: Optional['Bucket'] = None

class AllOne:
    def __init__(self):
        """构造函数"""
        # 头节点（计数值为0的哨兵节点）
        self.head: Bucket = Bucket("", 0)
        # 尾节点（计数值为无穷大的哨兵节点）
        self.tail: Bucket = Bucket("", float('inf'))
        self.head.next = self.tail
        self.tail.last = self.head
        # 字典存储key到链表节点的映射
        self.map = {}

    def _insert(self, cur: Bucket, pos: Bucket):
        """
        在cur节点后插入pos节点
        """
        if cur.next is not None:
            cur.next.last = pos
        pos.next = cur.next
        cur.next = pos
        pos.last = cur

    def _remove(self, cur: Bucket):
        """
        移除cur节点
        """
        if cur.last is not None:
            cur.last.next = cur.next
        if cur.next is not None:
            cur.next.last = cur.last

    def inc(self, key: str) -> None:
        """
        增加key的计数
        :param key: 要增加计数的key
        时间复杂度: O(1)
        """
        if key not in self.map:
            # 如果key不存在
            if self.head.next is not None and self.head.next.cnt == 1:
                # 如果计数值为1的节点已存在，直接添加key
                self.map[key] = self.head.next
                self.head.next.set.add(key)
            else:
                # 否则创建新节点并插入链表
                new_bucket = Bucket(key, 1)  # 创建Bucket实例
                self.map[key] = new_bucket
                self._insert(self.head, new_bucket)
        else:
            # 如果key已存在
            bucket = self.map[key]
            if bucket.next is not None and bucket.next.cnt == bucket.cnt + 1:
                # 如果计数值+1的节点已存在，直接添加key
                self.map[key] = bucket.next
                bucket.next.set.add(key)
            else:
                # 否则创建新节点并插入链表
                new_bucket = Bucket(key, bucket.cnt + 1)  # 创建Bucket实例
                self.map[key] = new_bucket
                self._insert(bucket, new_bucket)
            # 从原节点中移除key
            bucket.set.discard(key)
            # 如果原节点的key集合为空，则删除该节点
            if not bucket.set:
                self._remove(bucket)

    def dec(self, key: str) -> None:
        """
        减少key的计数
        :param key: 要减少计数的key
        时间复杂度: O(1)
        """
        # 获取key对应的节点
        bucket = self.map[key]
        if bucket.cnt == 1:
            # 如果计数值为1，直接删除key
            del self.map[key]
        else:
            # 如果计数值大于1
            if bucket.last is not None and bucket.last.cnt == bucket.cnt - 1:
                # 如果计数值-1的节点已存在，直接添加key
                self.map[key] = bucket.last
                bucket.last.set.add(key)
            else:
                # 否则创建新节点并插入链表
                new_bucket = Bucket(key, bucket.cnt - 1)  # 创建Bucket实例
                self.map[key] = new_bucket
                self._insert(bucket.last, new_bucket)
        # 从原节点中移除key
        bucket.set.discard(key)
        # 如果原节点的key集合为空，则删除该节点
        if not bucket.set:
            self._remove(bucket)

    def getMaxKey(self) -> str:
        """
        获取计数最大的key
        :return: 计数最大的key，如果不存在返回空字符串
        时间复杂度: O(1)
        """
        # 如果链表为空，返回空字符串
        if self.tail.last == self.head:
            return ""
        # 返回计数最大的节点中的任意一个key
        if self.tail.last is not None:
            return next(iter(self.tail.last.set)) if self.tail.last.set else ""
        return ""

    def getMinKey(self) -> str:
        """
        获取计数最小的key
        :return: 计数最小的key，如果不存在返回空字符串
        时间复杂度: O(1)
        """
        # 如果链表为空，返回空字符串
        if self.head.next == self.tail:
            return ""
        # 返回计数最小的节点中的任意一个key
        if self.head.next is not None:
            return next(iter(self.head.next.set)) if self.head.next.set else ""
        return ""

# 测试代码
if __name__ == "__main__":
    all_one = AllOne()
    
    # 简单测试
    all_one.inc("hello")
    all_one.inc("world")
    all_one.inc("hello")
    print("getMaxKey():", all_one.getMaxKey())  # hello
    print("getMinKey():", all_one.getMinKey())  # world
    all_one.inc("world")
    all_one.inc("world")
    print("getMaxKey():", all_one.getMaxKey())  # world
    all_one.dec("world")
    print("getMinKey():", all_one.getMinKey())  # hello or world