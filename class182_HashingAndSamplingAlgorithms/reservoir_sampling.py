#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
蓄水池抽样算法 (Reservoir Sampling)

算法原理：
蓄水池抽样是一种随机抽样算法，用于从包含n个项目的未知大小的数据流中
随机选择k个样本，其中n非常大或未知。该算法能够在只遍历一次数据流的情况下，
保证每个元素被选中的概率相等。

算法特点：
1. 适用于数据流处理
2. 空间复杂度为O(k)，与数据流大小无关
3. 时间复杂度为O(n)
4. 保证每个元素被选中的概率相等

应用场景：
- 大数据流的随机抽样
- 无法预知数据总量的抽样
- 在线算法
- 数据库查询优化

算法流程：
1. 将前k个元素放入蓄水池
2. 对于第i个元素(i > k)：
   a. 以 k/i 的概率选择该元素
   b. 如果被选中，则随机替换蓄水池中的一个元素
3. 重复步骤2直到数据流结束

时间复杂度：O(n)，n为数据流大小
空间复杂度：O(k)，k为样本大小
"""

import random
from typing import List, Iterator, TypeVar, Generic

T = TypeVar('T')


class ReservoirSampling:
    def __init__(self):
        """构造函数"""
        self.random = random.Random()
    
    def select_random_element(self, stream: Iterator[T]) -> T:
        """
        从数据流中随机选择1个元素（k=1的简单情况）
        
        Args:
            stream: 数据流迭代器
            
        Returns:
            随机选择的元素
        """
        try:
            result = next(stream)  # 第一个元素
        except StopIteration:
            raise ValueError("数据流为空")
        
        count = 1
        
        # 遍历剩余元素
        for current in stream:
            count += 1
            
            # 以 1/count 的概率选择当前元素
            if self.random.randint(0, count - 1) == 0:
                result = current
        
        return result
    
    def select_random_elements(self, stream: Iterator[T], k: int) -> List[T]:
        """
        从数据流中随机选择k个元素（通用情况）
        
        Args:
            stream: 数据流迭代器
            k: 样本大小
            
        Returns:
            随机选择的k个元素列表
        """
        reservoir = []
        
        # 将前k个元素放入蓄水池
        count = 0
        for item in stream:
            if count < k:
                reservoir.append(item)
                count += 1
            else:
                break
        
        # 如果数据流元素少于k个，直接返回
        if count < k:
            return reservoir
        
        # 处理剩余元素
        for item in stream:
            count += 1
            
            # 以 k/count 的概率选择当前元素
            j = self.random.randint(0, count - 1)
            if j < k:
                reservoir[j] = item
        
        return reservoir
    
    def select_random_elements_from_list(self, lst: List[T], k: int) -> List[T]:
        """
        从列表中随机选择k个元素
        
        Args:
            lst: 输入列表
            k: 样本大小
            
        Returns:
            随机选择的k个元素列表
        """
        if len(lst) <= k:
            return lst.copy()
        
        # 创建结果列表
        result = lst[:k].copy()
        
        # 处理剩余元素
        for i in range(k, len(lst)):
            # 以 k/(i+1) 的概率选择当前元素
            j = self.random.randint(0, i)
            if j < k:
                result[j] = lst[i]
        
        return result
    
    def select_random_elements_from_array(self, array: List[T], k: int) -> List[T]:
        """
        从数组中随机选择k个元素（与列表版本相同）
        
        Args:
            array: 输入数组
            k: 样本大小
            
        Returns:
            随机选择的k个元素数组
        """
        return self.select_random_elements_from_list(array, k)


def main():
    """测试示例"""
    rs = ReservoirSampling()
    
    print("=== 蓄水池抽样算法测试 ===")
    
    # 测试从列表中随机选择元素
    print("\n1. 从列表中随机选择元素:")
    lst = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    print("原列表:", lst)
    
    for k in range(1, 6):
        selected = rs.select_random_elements_from_list(lst, k)
        print(f"选择{k}个元素: {selected}")
    
    # 测试从数据流中随机选择元素
    print("\n2. 从数据流中随机选择元素:")
    stream_data = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
    
    # 多次运行以验证随机性
    print("多次运行结果（选择3个元素）:")
    for i in range(5):
        selected = rs.select_random_elements(iter(stream_data), 3)
        print(f"第{i + 1}次: {selected}")
    
    # 验证概率均匀性
    print("\n3. 验证概率均匀性（选择1个元素，运行10000次）:")
    frequency = {}
    for i in range(10000):
        selected = rs.select_random_element(iter(stream_data))
        frequency[selected] = frequency.get(selected, 0) + 1
    
    print("各元素被选中的频次:")
    for element, count in sorted(frequency.items()):
        print(f"元素{element}: {count}次 ({count / 100.0:.2f}%)")


if __name__ == "__main__":
    main()