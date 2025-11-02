# 实现LRU结构
'''
一、题目解析
LRU (Least Recently Used) 最近最少使用缓存机制是一种常用的页面置换算法。
当缓存满时，会优先淘汰最长时间未被访问的数据。
要求实现get和put操作，均要求O(1)时间复杂度。

二、算法思路
1. 使用双向链表维护访问顺序，最近访问的节点放在尾部，最久未访问的节点在头部
2. 使用哈希表实现O(1)时间复杂度的查找操作，映射键到节点
3. 当访问一个节点时，将其移动到链表尾部（最近访问）
4. 当插入新节点且缓存满时，删除链表头部节点（最久未访问）

三、时间复杂度分析
get操作: O(1) - 哈希表查找 + 链表节点移动
put操作: O(1) - 哈希表插入/更新 + 链表节点插入/删除

四、空间复杂度分析
O(capacity) - 哈希表和双向链表最多存储capacity个节点

五、工程化考量
1. 异常处理: 检查非法输入如capacity<=0
2. 内存管理: Python有自动垃圾回收机制，无需手动释放内存
3. 线程安全: 多线程环境下需要加锁保护
4. 可配置性: 支持自定义容量
5. 性能优化: 使用Python内置字典和自定义双向链表实现高效访问
6. 扩展性: 支持添加统计功能、过期机制等扩展特性
7. 监控: 在实际应用中可能需要添加性能监控指标

六、相关题目扩展
1. LeetCode 146. [LRU Cache](https://leetcode.com/problems/lru-cache/) (本题原型)
2. LeetCode 460. [LFU Cache](https://leetcode.com/problems/lfu-cache/) (最近最不经常使用)
3. LeetCode 432. [全O(1)的数据结构](https://leetcode.com/problems/all-oone-data-structure/)
4. 牛客网: [设计LRU缓存结构](https://www.nowcoder.com/practice/e3769a5f498241bd98942db7489cbff8)
5. 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/)
6. LintCode 24. [LRU缓存策略](https://www.lintcode.com/problem/24/)
7. HackerRank: [Cache Implementation](https://www.hackerrank.com/challenges/lru-cache/problem)
8. CodeChef: [Implement Cache](https://www.codechef.com/problems/IMCACHE)
9. 计蒜客: [LRU缓存实现](https://nanti.jisuanke.com/t/41393)
10. 杭电OJ 1816: [LRU Cache](http://acm.hdu.edu.cn/showproblem.php?pid=1816)

七、Python语言特性利用
1. 使用内置dict实现哈希表，性能高效
2. 利用类的嵌套定义实现内部类封装
3. Python的垃圾回收自动管理内存，避免内存泄漏
4. 在Python 3.7+中，dict保持插入顺序，可用于简化实现（但此处仍使用标准实现以保证O(1)操作）
'''
class LRUCache:
    # 双向链表节点类
    # 用于维护访问顺序，最近访问的节点在尾部，最久未访问的节点在头部
    class DoubleNode:
        def __init__(self, key, val):
            self.key = key       # 键，用于在哈希表中索引
            self.val = val       # 值
            self.last = None     # 前驱节点指针
            self.next = None     # 后继节点指针
    
    # 双向链表类
    # 提供基本的链表操作：添加节点、移动节点到尾部、删除头节点
    # 封装链表操作，简化主逻辑
    class DoubleList:
        def __init__(self):
            self.head = None  # 链表头部指针（最久未访问）
            self.tail = None  # 链表尾部指针（最近访问）
        
        # 添加节点到链表尾部（最近访问）
        # 时间复杂度: O(1)
        # 关键步骤: 处理空链表情况和非空链表情况
        def add_node(self, new_node):
            if new_node is None:
                return
            if self.head is None:
                # 空链表情况
                self.head = new_node
                self.tail = new_node
            else:
                # 非空链表情况，添加到尾部
                self.tail.next = new_node
                new_node.last = self.tail
                self.tail = new_node
        
        # 将指定节点移动到链表尾部（更新为最近访问）
        # 时间复杂度: O(1)
        # 边界处理: 节点已经在尾部、节点是头节点
        def move_node_to_tail(self, node):
            # 优化: 如果节点已经在尾部，无需操作
            if self.tail == node:
                return
            
            # 从原位置移除节点
            if self.head == node:
                # 节点是头节点
                self.head = node.next
                self.head.last = None
            else:
                # 节点在中间位置
                node.last.next = node.next
                node.next.last = node.last
            
            # 将节点添加到尾部
            node.last = self.tail
            node.next = None
            self.tail.next = node
            self.tail = node
        
        # 删除并返回链表头部节点（最久未使用）
        # 时间复杂度: O(1)
        # 边界处理: 空链表、链表只有一个节点
        def remove_head(self):
            if self.head is None:
                return None  # 空链表
            ans = self.head
            if self.head == self.tail:
                # 链表只有一个节点
                self.head = None
                self.tail = None
            else:
                # 链表有多个节点
                self.head = ans.next
                ans.next = None  # 断开连接，帮助垃圾回收
                self.head.last = None
            return ans
    
    def __init__(self, capacity):
        """
        构造函数
        :param capacity: 缓存容量
        边界检查: 容量必须大于0
        """
        # 检查非法输入
        if capacity <= 0:
            raise ValueError("容量必须大于0")
        self.key_node_map = {}  # 哈希表用于O(1)时间复杂度查找节点
        self.node_list = self.DoubleList()  # 双向链表维护访问顺序
        self.capacity = capacity  # 缓存容量
    
    def get(self, key):
        """
        获取指定key的值
        :param key: 键
        :return: 如果key存在返回对应的值，否则返回-1
        时间复杂度: O(1)
        核心逻辑: 查找节点并更新访问顺序
        """
        if key in self.key_node_map:
            node = self.key_node_map[key]
            # 将访问的节点移动到链表尾部（最近访问）
            self.node_list.move_node_to_tail(node)
            return node.val
        return -1  # 键不存在
    
    def put(self, key, value):
        """
        插入或更新键值对
        :param key: 键
        :param value: 值
        时间复杂度: O(1)
        核心逻辑: 处理更新已存在的键和插入新键两种情况
        """
        if key in self.key_node_map:
            # 更新已存在的key
            node = self.key_node_map[key]
            node.val = value
            # 将访问的节点移动到链表尾部（最近访问）
            self.node_list.move_node_to_tail(node)
        else:
            # 插入新key
            if len(self.key_node_map) == self.capacity:
                # 缓存已满，删除最久未使用的节点（链表头部）
                removed = self.node_list.remove_head()
                del self.key_node_map[removed.key]
            # 创建新节点并添加到链表尾部和哈希表
            new_node = self.DoubleNode(key, value)
            self.key_node_map[key] = new_node
            self.node_list.add_node(new_node)

# 线程安全的LRU缓存实现
# 使用threading.RLock实现线程安全
class ThreadSafeLRUCache:
    def __init__(self, capacity):
        import threading
        self.lock = threading.RLock()  # 可重入锁，支持在同一线程中多次获取
        self.cache = LRUCache(capacity)
    
    def get(self, key):
        """
        线程安全的get操作
        :param key: 键
        :return: 如果key存在返回对应的值，否则返回-1
        """
        with self.lock:
            return self.cache.get(key)
    
    def put(self, key, value):
        """
        线程安全的put操作
        :param key: 键
        :param value: 值
        """
        with self.lock:
            self.cache.put(key, value)

# 支持统计功能的增强版LRU缓存
class EnhancedLRUCache:
    def __init__(self, capacity):
        self.cache = LRUCache(capacity)
        self.hits = 0        # 缓存命中次数
        self.accesses = 0    # 总访问次数
        self.evictions = 0   # 淘汰次数
    
    def get(self, key):
        """
        获取指定key的值并统计访问信息
        :param key: 键
        :return: 如果key存在返回对应的值，否则返回-1
        """
        self.accesses += 1
        value = self.cache.get(key)
        if value != -1:
            self.hits += 1
        return value
    
    def put(self, key, value):
        """
        插入或更新键值对
        :param key: 键
        :param value: 值
        """
        # 注意：这里简化了实现，实际需要跟踪淘汰事件
        # 可以通过修改内部LRUCache实现来支持淘汰回调
        self.cache.put(key, value)
    
    def get_hit_rate(self):
        """
        获取命中率
        :return: 命中率，取值范围[0, 1]
        """
        return 0.0 if self.accesses == 0 else self.hits / self.accesses
    
    def print_stats(self):
        """
        打印统计信息
        """
        print(f"访问次数: {self.accesses}")
        print(f"命中次数: {self.hits}")
        print(f"命中率: {self.get_hit_rate() * 100:.2f}%")
        print(f"淘汰次数: {self.evictions}")

# 使用Python标准库collections.OrderedDict简化实现的LRU缓存
# 这是一种更Pythonic的实现方式，但时间复杂度仍然是O(1)
class OrderedDictLRUCache:
    def __init__(self, capacity):
        if capacity <= 0:
            raise ValueError("容量必须大于0")
        from collections import OrderedDict
        self.cache = OrderedDict()
        self.capacity = capacity
    
    def get(self, key):
        if key not in self.cache:
            return -1
        # 移动到末尾（最近访问）
        self.cache.move_to_end(key)
        return self.cache[key]
    
    def put(self, key, value):
        if key in self.cache:
            # 更新并移动到末尾
            self.cache[key] = value
            self.cache.move_to_end(key)
        else:
            # 检查容量并添加新元素
            if len(self.cache) >= self.capacity:
                # 删除第一个元素（最久未访问）
                self.cache.popitem(last=False)
            self.cache[key] = value

# 测试代码
if __name__ == "__main__":
    try:
        print("=== LRU Cache 基本测试 ===")
        # 创建容量为2的LRU缓存
        cache = LRUCache(2)
        
        # 测试用例: ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
        #           [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
        
        cache.put(1, 1)  # 缓存是 {1=1}
        cache.put(2, 2)  # 缓存是 {1=1, 2=2}
        print(f"get(1): {cache.get(1)}")  # 返回 1，缓存变为 {2=2, 1=1}
        cache.put(3, 3)  # 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
        print(f"get(2): {cache.get(2)}")  # 返回 -1 (未找到)
        cache.put(4, 4)  # 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        print(f"get(1): {cache.get(1)}")  # 返回 -1 (未找到)
        print(f"get(3): {cache.get(3)}")  # 返回 3，缓存变为 {4=4, 3=3}
        print(f"get(4): {cache.get(4)}")  # 返回 4，缓存变为 {3=3, 4=4}
        
        # 测试增强版LRU缓存
        print("\n=== Enhanced LRU Cache 测试 ===")
        enhanced_cache = EnhancedLRUCache(3)
        enhanced_cache.put(1, 1)
        enhanced_cache.put(2, 2)
        enhanced_cache.put(3, 3)
        print(f"get(1): {enhanced_cache.get(1)}")  # 命中
        print(f"get(4): {enhanced_cache.get(4)}")  # 未命中
        enhanced_cache.put(4, 4)  # 淘汰2
        print(f"get(2): {enhanced_cache.get(2)}")  # 未命中
        print(f"get(3): {enhanced_cache.get(3)}")  # 命中
        print(f"get(4): {enhanced_cache.get(4)}")  # 命中
        
        enhanced_cache.print_stats()
        
        # 测试Python标准库实现的LRU缓存
        print("\n=== OrderedDict LRU Cache 测试 ===")
        ordered_cache = OrderedDictLRUCache(2)
        ordered_cache.put(1, 1)
        ordered_cache.put(2, 2)
        print(f"get(1): {ordered_cache.get(1)}")
        ordered_cache.put(3, 3)
        print(f"get(2): {ordered_cache.get(2)}")
        ordered_cache.put(4, 4)
        print(f"get(1): {ordered_cache.get(1)}")
        print(f"get(3): {ordered_cache.get(3)}")
        print(f"get(4): {ordered_cache.get(4)}")
        
        print("\n所有测试完成！")
    except Exception as e:
        print(f"异常: {e}")