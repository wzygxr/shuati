#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
差分数组实现 (Python版本)

算法思路：
差分数组是一种用于高效处理区间更新操作的数据结构。
通过维护原数组的差分数组，可以将区间更新操作的时间复杂度从O(n)降低到O(1)。

应用场景：
1. 数组操作优化：批量更新处理
2. 前缀和计算：快速计算区间和
3. 算法竞赛：区间操作问题的优化

时间复杂度：
- 区间更新：O(1)
- 获取结果数组：O(n)
- 单点查询：O(n)（需要重建数组）
空间复杂度：O(n)
"""

class DifferenceArray:
    """差分数组实现类"""
    
    def __init__(self, size_or_array):
        """
        构造函数
        :param size_or_array: 数组大小或原始数组
        """
        if isinstance(size_or_array, int):
            # 从大小创建
            if size_or_array <= 0:
                raise ValueError("数组大小必须为正整数")
            
            self.size = size_or_array
            self.diff = [0] * (size_or_array + 1)  # 差分数组大小为n+1，便于处理边界
            self.original = [0] * size_or_array
        else:
            # 从原始数组创建
            if not size_or_array:
                raise ValueError("输入数组不能为空")
            
            self.size = len(size_or_array)
            self.original = size_or_array[:]
            self.diff = [0] * (self.size + 1)
            
            # 初始化差分数组
            self.diff[0] = size_or_array[0]
            for i in range(1, self.size):
                self.diff[i] = size_or_array[i] - size_or_array[i - 1]
    
    def range_update(self, start, end, val):
        """
        区间更新：将区间[start, end]的每个元素加上val
        时间复杂度：O(1)
        :param start: 起始索引（包含）
        :param end: 结束索引（包含）
        :param val: 要增加的值
        """
        if start < 0 or end >= self.size or start > end:
            raise ValueError("更新范围无效")
        
        self.diff[start] += val
        self.diff[end + 1] -= val
    
    def get_result(self):
        """
        获取更新后的数组
        时间复杂度：O(n)
        :return: 更新后的数组
        """
        result = [0] * self.size
        result[0] = self.diff[0]
        
        for i in range(1, self.size):
            result[i] = result[i - 1] + self.diff[i]
        
        return result
    
    def get_value(self, index):
        """
        直接获取数组中特定位置的值
        注意：这需要先重建数组，时间复杂度O(n)
        :param index: 索引位置
        :return: 该位置的值
        """
        if index < 0 or index >= self.size:
            raise ValueError("索引无效")
        
        result = self.get_result()
        return result[index]
    
    def reset(self):
        """重置差分数组"""
        self.diff = [0] * (self.size + 1)
        if self.original:
            for i in range(self.size):
                self.range_update(i, i, self.original[i])
    
    @staticmethod
    def test_difference_array():
        """测试差分数组"""
        print("=== 测试差分数组 ===")
        
        # 测试从大小创建
        print("测试从大小创建:")
        da1 = DifferenceArray(5)
        print("初始数组:", da1.get_result())
        
        da1.range_update(0, 2, 1)
        print("区间[0,2]加1:", da1.get_result())
        
        da1.range_update(1, 4, 2)
        print("区间[1,4]加2:", da1.get_result())
        
        da1.range_update(2, 3, -1)
        print("区间[2,3]减1:", da1.get_result())
        
        # 测试从原始数组创建
        print("\n测试从原始数组创建:")
        original = [1, 2, 3, 4, 5]
        da2 = DifferenceArray(original)
        print("原始数组:", da2.get_result())
        
        da2.range_update(1, 3, 10)
        print("区间[1,3]加10:", da2.get_result())
        
        da2.range_update(0, 4, -5)
        print("区间[0,4]减5:", da2.get_result())
        
        # 测试重置功能
        da2.reset()
        print("重置后:", da2.get_result())
        
        # 测试边界情况
        print("\n测试边界情况:")
        da3 = DifferenceArray(1)
        da3.range_update(0, 0, 100)
        print("单元素数组更新:", da3.get_result())
        
        # 性能测试
        print("\n=== 性能测试 ===")
        import time
        
        # 测试大量区间更新操作
        n = 100000
        da4 = DifferenceArray(n)
        
        # 执行大量区间更新
        start_time = time.time()
        for i in range(10000):
            start = i % (n - 100)
            end = start + 100
            da4.range_update(start, end, 1)
        update_time = time.time() - start_time
        
        # 获取结果数组
        start_time = time.time()
        result = da4.get_result()
        get_result_time = time.time() - start_time
        
        print(f"执行10000次区间更新时间: {update_time*1000:.2f} ms")
        print(f"获取100000元素结果数组时间: {get_result_time*1000:.2f} ms")
        print(f"结果数组前10个元素: {result[:10]}")

if __name__ == "__main__":
    DifferenceArray.test_difference_array()