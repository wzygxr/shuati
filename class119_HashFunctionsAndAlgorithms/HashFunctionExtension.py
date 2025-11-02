"""
LeetCode 460. LFU Cache (LFU缓存)
题目来源: https://leetcode.com/problems/lfu-cache/
    
题目描述:
设计并实现最不经常使用（LFU）缓存的数据结构。
实现 LFUCache 类。
算法思路:
使用两个字典和双向链表实现:
1. key_to_node: 存储键到节点的映射
2. freq_to_list: 存储频率到双向链表的映射，每个频率对应一个双向链表
3. 同时维护一个最小频率变量，用于快速找到最不经常使用的节点

时间复杂度: O(1) - 所有操作都是常数时间
空间复杂度: O(capacity) - 存储容量大小的键值对
"""
class Node:
    def __init__(self, key=0, value=0):
        self.key = key
        self.value = value
        self.frequency = 1
        self.prev = None
        self.next = None

class DoublyLinkedList:
    def __init__(self):
        self.head = Node()
        self.tail = Node()
        self.head.next = self.tail
        self.tail.prev = self.head
        self.size = 0
    
    def add_to_head(self, node):
        node.next = self.head.next
        node.prev = self.head
        self.head.next.prev = node
        self.head.next = node
        self.size += 1
    
    def remove_node(self, node):
        node.prev.next = node.next
        node.next.prev = node.prev
        self.size -= 1
    
    def remove_tail(self):
        if self.size == 0:
            return None
        node = self.tail.prev
        self.remove_node(node)
        return node

class LFUCache:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.size = 0
        self.min_freq = 0
        self.key_to_node = {}
        self.freq_to_list = {}
    
    def get(self, key: int) -> int:
        if self.capacity == 0 or key not in self.key_to_node:
            return -1
        
        node = self.key_to_node[key]
        self._update_frequency(node)
        return node.value
    
    def put(self, key: int, value: int) -> None:
        if self.capacity == 0:
            return
        
        if key in self.key_to_node:
            node = self.key_to_node[key]
            node.value = value
            self._update_frequency(node)
        else:
            if self.size == self.capacity:
                min_freq_list = self.freq_to_list[self.min_freq]
                removed = min_freq_list.remove_tail()
                del self.key_to_node[removed.key]
                self.size -= 1
            
            self.min_freq = 1  # 新节点的频率为1
            new_node = Node(key, value)
            
            if 1 not in self.freq_to_list:
                self.freq_to_list[1] = DoublyLinkedList()
            self.freq_to_list[1].add_to_head(new_node)
            self.key_to_node[key] = new_node
            self.size += 1
    
    def _update_frequency(self, node):
        old_freq = node.frequency
        new_freq = old_freq + 1
        
        # 从旧频率列表中移除
        old_list = self.freq_to_list[old_freq]
        old_list.remove_node(node)
        
        # 如果旧频率列表为空，并且是最小频率，更新最小频率
        if old_freq == self.min_freq and old_list.size == 0:
            self.min_freq += 1
        
        # 更新节点频率
        node.frequency = new_freq
        
        # 添加到新频率列表
        if new_freq not in self.freq_to_list:
            self.freq_to_list[new_freq] = DoublyLinkedList()
        self.freq_to_list[new_freq].add_to_head(node)
"""
LeetCode 811. Subdomain Visit Count (子域名访问计数)
题目来源: https://leetcode.com/problems/subdomain-visit-count/

算法思路:
使用哈希表统计每个域名及其子域名的访问次数

时间复杂度: O(n), 其中n是域名字符串的总长度
空间复杂度: O(m), 其中m是不同域名的数量
"""
def subdomain_visits(cpdomains: List[str]) -> List[str]:
    counts = {}
    
    for cpdomain in cpdomains:
        count, domain = cpdomain.split()
        count = int(count)
        
        # 统计当前完整域名
        counts[domain] = counts.get(domain, 0) + count
        
        # 统计所有父域名
        parts = domain.split('.')
        for i in range(1, len(parts)):
            parent = '.'.join(parts[i:])
            counts[parent] = counts.get(parent, 0) + count
    
    # 构建结果
    return [f"{count} {domain}" for domain, count in counts.items()]

"""
LeetCode 554. Brick Wall (砖墙)
题目来源: https://leetcode.com/problems/brick-wall/

算法思路:
使用哈希表统计每个位置的砖块边缘数量，然后找出边缘数量最多的位置
穿过的砖块数量 = 总行数 - 该位置的边缘数量

时间复杂度: O(n), 其中n是墙中的砖块总数
空间复杂度: O(m), 其中m是不同的边缘位置数量
"""
def least_bricks(wall: List[List[int]]) -> int:
    edge_count = {}
    max_edges = 0
    
    for row in wall:
        position = 0
        # 不考虑最后一块砖的右边缘
        for i in range(len(row) - 1):
            position += row[i]
            edge_count[position] = edge_count.get(position, 0) + 1
            max_edges = max(max_edges, edge_count[position])
    
    # 穿过的砖块数量 = 总行数 - 最大边缘数量
    return len(wall) - max_edges

"""
LeetCode 957. Prison Cells After N Days (N天后的牢房)
题目来源: https://leetcode.com/problems/prison-cells-after-n-days/

算法思路:
由于状态空间有限(2^8=256种可能)，一定存在循环，使用哈希表检测循环
找到循环后，计算 n % 循环长度 来确定最终状态

时间复杂度: O(min(2^N, N)), 其中N是牢房数量(这里是8)
空间复杂度: O(2^N) = O(256), 存储所有可能的状态
"""
def prison_after_n_days(cells: List[int], n: int) -> List[int]:
    seen = {}
    
    # 将状态转换为字符串用于哈希表的键
    cells_key = ''.join(map(str, cells))
    day = 0
    
    while day < n:
        # 检测循环
        if cells_key in seen:
            cycle_length = day - seen[cells_key]
            # 跳过完整的循环
            day = day + ((n - day) // cycle_length) * cycle_length
            
            if day >= n:
                break
        
        seen[cells_key] = day
        day += 1
        
        # 计算下一天的状态
        next_cells = [0] * 8
        for i in range(1, 7):
            next_cells[i] = 1 if cells[i-1] == cells[i+1] else 0
        
        cells = next_cells
        cells_key = ''.join(map(str, cells))
    
    return cells

"""
LeetCode 1711. Count Good Meals (大餐计数)
题目来源: https://leetcode.com/problems/count-good-meals/

算法思路:
使用哈希表统计每个美味程度出现的次数
对于每个餐品，检查是否存在另一个餐品使得它们的和是2的幂

时间复杂度: O(n * log(max)), 其中n是餐品数量，max是最大美味程度
空间复杂度: O(n)
"""
def count_pairs(deliciousness: List[int]) -> int:
    freq_map = {}
    MOD = 10**9 + 7
    result = 0
    
    # 2的幂可能值 (2^0 到 2^21, 因为题目限制最大值为 2^20)
    powers = [1 << i for i in range(22)]
    
    for num in deliciousness:
        # 检查与当前num组成2的幂的可能
        for power in powers:
            complement = power - num
            if complement in freq_map:
                result = (result + freq_map[complement]) % MOD
        
        # 更新当前num的频率
        freq_map[num] = freq_map.get(num, 0) + 1
    
    return result
