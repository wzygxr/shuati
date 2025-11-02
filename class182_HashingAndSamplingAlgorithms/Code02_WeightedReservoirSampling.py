import random
from typing import List, TypeVar, Tuple, Any
import heapq

T = TypeVar('T')

"""
加权蓄水池采样算法 (Weighted Reservoir Sampling)

算法原理：
Efraimidis和Spirakis算法是加权蓄水池采样的经典算法。
对于每个元素，计算 random()^(1/weight)，然后选择值最大的k个元素。

算法步骤：
1. 对于数据流中的每个元素(item, weight)：
   a. 计算 key = random()^(1/weight)
   b. 如果蓄水池未满，直接加入蓄水池
   c. 如果蓄水池已满，找到当前蓄水池中key最小的元素
   d. 如果当前元素的key大于最小key，则替换该元素

时间复杂度：O(n*log(k))，其中n是数据流长度，k是蓄水池大小
空间复杂度：O(k)

应用场景：
1. 带权重的数据流采样
2. 推荐系统中的内容推荐
3. 负载均衡中的服务器选择
4. A/B测试中的用户分组
"""

class WeightedReservoirSampler:
    """
    加权蓄水池采样类
    """
    
    def __init__(self, reservoir_size: int):
        """
        初始化加权蓄水池采样器
        
        Args:
            reservoir_size: 蓄水池大小
        """
        self.reservoir_size = reservoir_size
        # 使用最小堆维护蓄水池，存储 (-key, item, weight) 元组
        # 使用负数是因为Python的heapq是最小堆
        self.reservoir = []
        
    def add(self, item: T, weight: float) -> None:
        """
        向蓄水池中添加元素
        
        Args:
            item: 元素
            weight: 权重
        """
        if weight <= 0:
            raise ValueError("权重必须大于0")
            
        # 计算随机键值：random()^(1/weight)
        key = random.random() ** (1.0 / weight)
        
        # 如果蓄水池未满，直接加入
        if len(self.reservoir) < self.reservoir_size:
            heapq.heappush(self.reservoir, (key, item, weight))
        else:
            # 如果蓄水池已满，比较当前元素与堆顶元素的key值
            smallest_key, _, _ = self.reservoir[0]
            if key > smallest_key:
                # 替换key值最小的元素
                heapq.heapreplace(self.reservoir, (key, item, weight))
    
    def get_sample(self) -> List[T]:
        """
        获取蓄水池中的所有元素
        
        Returns:
            元素列表
        """
        return [item for _, item, _ in self.reservoir]
    
    def get_sample_with_weights(self) -> List[Tuple[T, float]]:
        """
        获取蓄水池中的所有元素及权重
        
        Returns:
            元素及权重的元组列表
        """
        return [(item, weight) for _, item, weight in self.reservoir]


def weighted_sample(items: List[T], weights: List[float], k: int) -> List[T]:
    """
    简化版本的加权采样函数
    适用于已知完整数据集的情况
    
    Args:
        items: 元素列表
        weights: 权重列表
        k: 采样数量
        
    Returns:
        采样结果
        
    Raises:
        ValueError: 当参数不合法时抛出异常
    """
    if len(items) != len(weights):
        raise ValueError("元素数量与权重数量不匹配")
        
    if k > len(items):
        raise ValueError("采样数量不能大于元素总数")
        
    for weight in weights:
        if weight <= 0:
            raise ValueError("权重必须大于0")
    
    # 计算每个元素的随机键值
    keys = []
    for weight in weights:
        # 计算 key = random()^(1/weight)
        key = random.random() ** (1.0 / weight)
        keys.append(key)
    
    # 创建索引列表并按key值降序排序
    indices = list(range(len(items)))
    indices.sort(key=lambda i: keys[i], reverse=True)
    
    # 选择前k个元素
    result = [items[indices[i]] for i in range(k)]
    
    return result


def main():
    """测试函数"""
    print("=== 加权蓄水池采样测试 ===")
    
    # 测试1: 使用WeightedReservoirSampler类
    print("\n测试1: 流式加权采样")
    sampler = WeightedReservoirSampler(3)
    
    # 模拟数据流，包含元素及其权重
    items = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]
    weights = [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0]
    
    print("数据流元素及权重:")
    for i in range(len(items)):
        print(f"{items[i]}: {weights[i]}")
        sampler.add(items[i], weights[i])
    
    print("\n采样结果:")
    sample = sampler.get_sample()
    for item in sample:
        print(item)
    
    # 测试2: 使用简化版加权采样函数
    print("\n测试2: 完整数据集加权采样")
    sample2 = weighted_sample(items, weights, 3)
    print("采样结果:")
    for item in sample2:
        print(item)
    
    # 测试3: 验证权重正确性
    print("\n测试3: 权重正确性验证")
    print("进行10000次采样，统计各元素被选中的频率:")
    
    counts = [0] * len(items)
    test_times = 10000
    
    for _ in range(test_times):
        result = weighted_sample(items, weights, 1)
        selected_item = result[0]
        
        # 找到选中元素的索引
        for j in range(len(items)):
            if items[j] == selected_item:
                counts[j] += 1
                break
    
    print("元素\t权重\t理论概率\t实际频率")
    total_weight = sum(weights)
    
    for i in range(len(items)):
        theoretical_prob = weights[i] / total_weight
        actual_freq = counts[i] / test_times
        print(f"{items[i]}\t{weights[i]:.1f}\t{theoretical_prob:.4f}\t\t{actual_freq:.4f}")


if __name__ == "__main__":
    main()