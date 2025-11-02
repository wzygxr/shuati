"""
LeetCode 1206. 设计跳表 (Design Skiplist)
题目链接：https://leetcode.com/problems/design-skiplist/

题目描述：
不使用任何库函数，设计一个跳表。
跳表是一种可以在O(log(n))时间内完成增加、删除、搜索操作的数据结构。
跳表相比树堆与红黑树，其功能与性能相当，而且代码更短，设计思想与链表相似。

实现 Skiplist 类：
- Skiplist() 初始化跳表对象
- bool search(int target) 返回target是否存在于跳表中
- void add(int num) 插入一个元素到跳表
- bool erase(int num) 在跳表中删除一个值，如果 num 不存在，直接返回false

算法思路：
1. 跳表是一种多层链表结构，每一层都是一个有序链表
2. 每个节点可能出现在多个层中，通过随机化决定节点出现在哪些层
3. 查找时从最高层开始，逐层向下查找，减少比较次数
4. 插入和删除时需要维护各层链表的正确性

时间复杂度：
- search: O(log n) 平均情况
- add: O(log n) 平均情况
- erase: O(log n) 平均情况

空间复杂度：O(n) 平均情况

工程化考量：
1. 随机化：使用随机数决定节点层数，保证性能
2. 内存优化：合理设置最大层数，避免过多内存消耗
3. 异常处理：处理空值和边界情况
4. 线程安全：在多线程环境下需要加锁保护
"""

import random
import threading
import time
from typing import List, Dict, Any, Optional


class Node:
    """跳表节点类"""
    
    def __init__(self, value: int, level: int):
        self.value = value
        self.next: List[Optional['Node']] = [None] * level


class Code26_LeetCode1206_DesignSkiplist:
    """跳表实现"""
    
    # 最大层数
    MAX_LEVEL = 16
    
    def __init__(self):
        """构造函数"""
        self.random = random.Random()
        self.head = Node(-1, self.MAX_LEVEL)
        self.current_level = 0
        self.lock = threading.RLock()
    
    def search(self, target: int) -> bool:
        """
        搜索目标值是否存在
        :param target: 目标值
        :return: 如果存在返回True，否则返回False
        """
        with self.lock:
            current = self.head
            
            # 从最高层开始向下搜索
            for i in range(self.current_level - 1, -1, -1):
                # 在当前层找到小于target的最大节点
                next_node = current.next[i]
                while next_node is not None and next_node.value < target:
                    current = next_node
                    next_node = current.next[i]
            
            # 检查下一个节点是否为目标值
            current = current.next[0]  # type: ignore
            return current is not None and current.value == target
    
    def add(self, num: int) -> None:
        """
        添加元素
        :param num: 要添加的元素
        """
        with self.lock:
            # 更新数组，记录每一层需要更新的节点
            update: List[Optional[Node]] = [None] * self.MAX_LEVEL
            current = self.head
            
            # 从最高层开始向下搜索插入位置
            for i in range(self.current_level - 1, -1, -1):
                # 在当前层找到小于num的最大节点
                next_node = current.next[i]
                while next_node is not None and next_node.value < num:
                    current = next_node
                    next_node = current.next[i]
                update[i] = current
            
            # 移动到下一层
            current = current.next[0]  # type: ignore
            
            # 如果元素已存在，直接返回
            if current is not None and current.value == num:
                return
            
            # 随机生成新节点的层数
            new_level = self._random_level()
            
            # 如果新层数大于当前最高层数，更新update数组
            if new_level > self.current_level:
                for i in range(self.current_level, new_level):
                    update[i] = self.head
                self.current_level = new_level
            
            # 创建新节点
            new_node = Node(num, new_level)
            
            # 更新各层的指针
            for i in range(new_level):
                if update[i] is not None:
                    new_node.next[i] = update[i].next[i]  # type: ignore
                    update[i].next[i] = new_node  # type: ignore
    
    def erase(self, num: int) -> bool:
        """
        删除元素
        :param num: 要删除的元素
        :return: 如果删除成功返回True，否则返回False
        """
        with self.lock:
            # 更新数组，记录每一层需要更新的节点
            update: List[Optional[Node]] = [None] * self.MAX_LEVEL
            current = self.head
            
            # 从最高层开始向下搜索删除位置
            for i in range(self.current_level - 1, -1, -1):
                # 在当前层找到小于num的最大节点
                next_node = current.next[i]
                while next_node is not None and next_node.value < num:
                    current = next_node
                    next_node = current.next[i]
                update[i] = current
            
            # 移动到下一层
            current = current.next[0]  # type: ignore
            
            # 如果元素不存在，直接返回False
            if current is None or current.value != num:
                return False
            
            # 更新各层的指针
            for i in range(self.current_level):
                if update[i] is not None:
                    update_next = update[i].next[i]  # type: ignore
                    if update_next is not current:
                        break
                if update[i] is not None and current.next[i] is not None:
                    update[i].next[i] = current.next[i]  # type: ignore
            
            # 更新当前最高层数
            while (self.current_level > 1 and 
                   self.head.next[self.current_level - 1] is None):
                self.current_level -= 1
            
            return True
    
    def _random_level(self) -> int:
        """
        随机生成节点层数
        :return: 节点层数
        """
        level = 1
        # 随机生成层数，每层概率为1/2
        while self.random.randint(0, 1) == 0 and level < self.MAX_LEVEL:
            level += 1
        return level
    
    def get_statistics(self) -> Dict[str, Any]:
        """
        获取跳表统计信息
        :return: 统计信息映射
        """
        with self.lock:
            stats: Dict[str, Any] = {
                "currentLevel": self.current_level,
                "maxLevel": self.MAX_LEVEL
            }
            
            # 计算节点数量
            count = 0
            current = self.head.next[0]
            while current is not None:
                count += 1
                current = current.next[0]
            stats["nodeCount"] = count
            
            return stats


class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def testSkiplistPerformance(skiplist: Code26_LeetCode1206_DesignSkiplist, 
                               operation_count: int) -> None:
        """
        测试跳表的性能
        :param skiplist: 跳表实例
        :param operation_count: 操作数量
        """
        print("=== 跳表性能测试 ===")
        print(f"操作数量: {operation_count}")
        
        random.seed(42)
        
        # 测试插入性能
        start_time = time.perf_counter()
        
        for i in range(operation_count // 2):
            value = random.randint(0, operation_count)
            skiplist.add(value)
        
        add_time = time.perf_counter() - start_time
        
        # 测试查询性能
        start_time = time.perf_counter()
        
        hit_count = 0
        miss_count = 0
        
        for i in range(operation_count // 2):
            value = random.randint(0, operation_count * 2)
            found = skiplist.search(value)
            if found:
                hit_count += 1
            else:
                miss_count += 1
        
        search_time = time.perf_counter() - start_time
        
        # 测试删除性能
        start_time = time.perf_counter()
        
        delete_success = 0
        delete_failed = 0
        
        for i in range(operation_count // 4):
            value = random.randint(0, operation_count)
            deleted = skiplist.erase(value)
            if deleted:
                delete_success += 1
            else:
                delete_failed += 1
        
        erase_time = time.perf_counter() - start_time
        
        print(f"插入平均时间: {add_time / (operation_count // 2) * 1e9:.2f} ns")
        print(f"查询平均时间: {search_time / (operation_count // 2) * 1e9:.2f} ns")
        print(f"删除平均时间: {erase_time / (operation_count // 4) * 1e9:.2f} ns")
        print(f"查询命中率: {hit_count / (hit_count + miss_count) * 100:.2f}%")
        
        # 显示统计信息
        print("跳表统计信息:", skiplist.get_statistics())


def main():
    """主函数"""
    print("=== 跳表设计测试 ===\n")
    
    # 基本功能测试
    print("1. 基本功能测试:")
    
    skiplist = Code26_LeetCode1206_DesignSkiplist()
    
    # 添加元素
    skiplist.add(1)
    skiplist.add(2)
    skiplist.add(3)
    print("添加元素 1, 2, 3")
    
    # 查询元素
    found = skiplist.search(0)
    print("查询元素 0:", found)
    
    found = skiplist.search(1)
    print("查询元素 1:", found)
    
    found = skiplist.search(4)
    print("查询元素 4:", found)
    
    # 删除元素
    deleted = skiplist.erase(0)
    print("删除元素 0:", deleted)
    
    deleted = skiplist.erase(1)
    print("删除元素 1:", deleted)
    
    found = skiplist.search(1)
    print("查询元素 1:", found)
    
    # 复杂场景测试
    print("\n2. 复杂场景测试:")
    
    skiplist2 = Code26_LeetCode1206_DesignSkiplist()
    
    # 添加多个元素
    for i in range(1, 11):
        skiplist2.add(i * 10)
    
    # 查询所有元素
    for i in range(1, 11):
        found = skiplist2.search(i * 10)
        print(f"查询元素 {i * 10}: {found}")
    
    # 删除部分元素
    for i in range(1, 6):
        deleted = skiplist2.erase(i * 10)
        print(f"删除元素 {i * 10}: {deleted}")
    
    # 再次查询所有元素
    for i in range(1, 11):
        found = skiplist2.search(i * 10)
        print(f"查询元素 {i * 10}: {found}")
    
    # 性能测试
    print("\n3. 性能测试:")
    skiplist3 = Code26_LeetCode1206_DesignSkiplist()
    PerformanceTest.testSkiplistPerformance(skiplist3, 10000)
    
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度:")
    print("- search: O(log n) 平均情况")
    print("- add: O(log n) 平均情况")
    print("- erase: O(log n) 平均情况")
    print("空间复杂度: O(n) 平均情况")
    
    print("\n=== 工程化应用场景 ===")
    print("1. Redis有序集合: 使用跳表实现有序集合")
    print("2. 数据库索引: 某些数据库使用跳表作为索引结构")
    print("3. 内存数据库: 高性能内存数据结构")
    print("4. 实时系统: 需要快速查找和更新的场景")
    
    print("\n=== 设计要点 ===")
    print("1. 多层链表: 通过多层链表减少查找时间")
    print("2. 随机化: 使用随机数决定节点层数，保证性能")
    print("3. 指针维护: 插入和删除时正确维护各层指针")
    print("4. 内存优化: 合理设置最大层数，避免过多内存消耗")


if __name__ == "__main__":
    main()