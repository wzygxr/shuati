import random
from typing import List, Optional, Dict, Set, Tuple, IO

# 蓄水池采样算法的Python实现

class Pool:
    """
    蓄水池采样池
    
    算法目标：从一个未知大小的数据流中随机选取k个元素，使得每个元素被选中的概率相等。
    
    算法原理：
    1. 保存前k个元素
    2. 对于第i个元素(i>k)，以k/i的概率决定是否将其加入蓄水池
    3. 如果决定加入，则随机替换蓄水池中的一个元素
    
    算法正确性证明：
    对于第i个元素，被选中的概率是k/i
    对于前k个元素中的任意一个，在第i轮不被替换的概率是：
    1. 第i个元素不被选中：(i-k)/i
    2. 第i个元素被选中但没有替换到当前元素：k/i * (k-1)/k = (k-1)/i
    所以当前元素在第i轮仍被保留的概率是：(i-k)/i + (k-1)/i = (i-1)/i
    
    最终每个元素被选中的概率为：k/k * k/(k+1) * (k+1)/(k+2) * ... * (n-1)/n = k/n
    
    时间复杂度：O(n)
    空间复杂度：O(k)
    """
    
    def __init__(self, size: int):
        self.size = size
        self.bag = [0] * size
    
    def pick(self, i: int) -> bool:
        """是否要i号球，size/i的几率决定要，剩下的几率决定不要"""
        return random.randint(0, i-1) < self.size
    
    def where(self) -> int:
        """袋子里0...size-1个位置，哪个空间的球扔掉，让i号球进来"""
        return random.randint(0, self.size-1)
    
    def enter(self, i: int) -> None:
        """将i号球放入袋子"""
        if i <= self.size:
            self.bag[i - 1] = i
        else:
            if self.pick(i):
                self.bag[self.where()] = i
    
    def get_bag(self) -> List[int]:
        """获取袋子中的球"""
        return self.bag


class ListNode:
    """链表节点定义"""
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


class SolutionLinkedList:
    """
    LeetCode 382. 链表随机节点
    题目描述：给定一个单链表，随机选择链表的一个节点，并返回相应的节点值。每个节点被选中的概率一样。
    
    解题思路：使用蓄水池采样算法，k=1的情况
    1. 保存第一个节点的值
    2. 遍历后续节点，对于第i个节点，以1/i的概率决定是否替换结果
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    
    def __init__(self, head: Optional[ListNode]):
        self.head = head
    
    def get_random(self) -> int:
        """获取随机节点值"""
        # 蓄水池采样 k=1
        current = self.head
        if not current:
            raise ValueError("链表为空")
            
        result = current.val
        count = 1
        
        while current:
            # 以 1/count 的概率选择当前节点
            if random.random() < 1.0 / count:
                result = current.val
            count += 1
            current = current.next
        
        return result


class SolutionRandomPickIndex:
    """
    LeetCode 398. 随机数索引
    题目描述：给定一个可能含有重复元素的整数数组，随机输出给定目标数字的索引。
    
    解题思路：使用蓄水池采样算法
    1. 遍历数组，找到所有等于target的元素
    2. 对于第k个等于target的元素，以1/k的概率决定是否选择它作为结果
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    
    def __init__(self, nums: List[int]):
        self.nums = nums
    
    def pick(self, target: int) -> int:
        """随机选择目标数字的索引"""
        result = -1
        count = 0
        
        for i, num in enumerate(self.nums):
            if num == target:
                count += 1
                # 以 1/count 的概率选择当前索引
                if random.random() < 1.0 / count:
                    result = i
        
        return result


class SolutionBlacklistRandom:
    """
    LeetCode 710. 黑名单中的随机数
    
    题目描述：给定一个包含 [0，n) 中不重复整数的黑名单 blacklist ，
    写一个函数，从 [0, n - 1] 范围内的任意整数中选取一个不在黑名单 blacklist 中的随机整数。
    要求每个有效整数被选中的概率相等。
    
    解题思路：将黑名单映射到白名单的末尾
    时间复杂度：O(B) 初始化，O(1) 每次查询，其中 B 是黑名单的大小
    空间复杂度：O(B)
    """
    
    def __init__(self, n: int, blacklist: List[int]):
        self.size = n - len(blacklist)  # 白名单的大小
        self.mapping = {}  # 黑名单映射
        
        # 将黑名单中的元素添加到集合中
        black_set = set(blacklist)
        
        # 映射黑名单中的元素到白名单末尾的可用元素
        last = n - 1
        for b in blacklist:
            # 如果b已经在末尾区域，不需要映射
            if b >= self.size:
                continue
            # 找到末尾区域的白名单元素
            while last in black_set:
                last -= 1
            self.mapping[b] = last
            last -= 1
    
    def pick(self) -> int:
        """随机选择一个不在黑名单中的数"""
        index = random.randint(0, self.size - 1)
        # 如果索引在映射中，返回映射的值
        return self.mapping.get(index, index)


class FileLineSampler:
    """
    扩展题目：从大文件中随机选择k行
    
    问题描述：给定一个非常大的文件，无法完全加载到内存，如何随机选择k行？
    
    解题思路：使用标准的蓄水池采样算法
    时间复杂度：O(n)，其中n是文件的行数
    空间复杂度：O(k)
    """
    
    def sample_lines(self, file_path: str, k: int) -> List[str]:
        """从文件中随机采样k行"""
        reservoir = []
        
        try:
            with open(file_path, 'r', encoding='utf-8') as reader:
                i = 0
                # 先填充前k行
                for line in reader:
                    if i < k:
                        reservoir.append(line.strip())
                    else:
                        break
                    i += 1
                
                # 对后续的行进行采样
                for line in reader:
                    i += 1
                    j = random.randint(0, i - 1)  # 0到i-1的随机数
                    if j < k:
                        reservoir[j] = line.strip()
        except FileNotFoundError:
            print(f"文件 {file_path} 不存在")
        except Exception as e:
            print(f"读取文件时出错: {e}")
        
        return reservoir


class DataStreamSampler:
    """
    扩展题目：数据流中随机采样k个元素
    
    问题描述：实现一个从无限长的数据流中随机选择k个元素的算法
    
    解题思路：标准的蓄水池采样算法
    时间复杂度：O(n)，其中n是已处理的元素数量
    空间复杂度：O(k)
    """
    
    def __init__(self, k: int):
        """初始化采样器"""
        self.k = k
        self.reservoir = []
        self.count = 0
    
    def add(self, value: int) -> None:
        """向数据流中添加一个元素"""
        if self.count < self.k:
            self.reservoir.append(value)
        else:
            # 以k/count的概率选择当前元素
            j = random.randint(0, self.count)  # 0到count的随机数
            if j < self.k:
                self.reservoir[j] = value
        self.count += 1
    
    def get_sample(self) -> List[int]:
        """获取当前的采样结果"""
        return self.reservoir


class WeightedSampler:
    """
    扩展题目：加权随机采样
    
    问题描述：从一个加权集合中随机选择一个元素，选择的概率与元素的权重成正比
    
    解题思路：使用前缀和方法
    时间复杂度：O(n) 每次查询
    空间复杂度：O(n)
    """
    
    def __init__(self, nums: List[int], weights: List[int]):
        """初始化加权采样器"""
        self.nums = nums
        self.weights = weights
        self.total_weight = sum(weights)  # 计算总权重
    
    def pick_index(self) -> int:
        """根据权重随机选择一个元素的索引"""
        rand = random.randint(1, self.total_weight)  # 1到total_weight的随机数
        sum_weight = 0
        
        for i, w in enumerate(self.weights):
            sum_weight += w
            if rand <= sum_weight:
                return self.nums[i]
        
        return self.nums[0]  # 理论上不会执行到这里


def validate_uniformity(results: List[int], n: int, expected_count: int) -> None:
    """
    单元测试辅助方法：验证采样的等概率性
    
    参数：
    - results: 采样结果列表
    - n: 元素总数
    - expected_count: 每个元素的期望出现次数
    """
    count_map = {}
    for result in results:
        count_map[result] = count_map.get(result, 0) + 1
    
    print("采样均匀性分析:")
    for i in range(n):
        actual = count_map.get(i, 0)
        # 允许5%的误差
        within_range = abs(actual - expected_count) <= expected_count * 0.05
        print(f"元素 {i}: 期望={expected_count}, 实际={actual}, {'通过' if within_range else '不通过'}")


def benchmark_sampling_algorithms(n: int, k: int, test_times: int = 1000) -> Dict[str, float]:
    """
    基准测试：比较不同采样算法的性能
    
    参数：
    - n: 数据规模
    - k: 采样大小
    - test_times: 测试次数
    
    返回：
    - 各算法的平均运行时间（秒）
    """
    import time
    import numpy as np
    
    times = {}
    
    # 测试标准蓄水池采样
    start_time = time.time()
    for _ in range(test_times):
        pool = Pool(k)
        for i in range(1, n + 1):
            pool.enter(i)
    times['标准蓄水池'] = (time.time() - start_time) / test_times
    
    # 测试numpy的random choice（用于对比）
    if n <= 10000:  # 只在数据量较小的时候测试，避免内存问题
        start_time = time.time()
        data = list(range(1, n + 1))
        for _ in range(test_times):
            np.random.choice(data, size=k, replace=False)
        times['numpy随机采样'] = (time.time() - start_time) / test_times
    
    return times

def main():
    print("=== 基础蓄水池采样测试 ===")
    print("测试开始")
    n = 41  # 一共吐出多少球
    m = 10  # 袋子大小多少
    test_times = 10000  # 进行多少次实验
    cnt = [0] * (n + 1)
    
    for _ in range(test_times):
        pool = Pool(m)
        for i in range(1, n + 1):
            pool.enter(i)
        bag = pool.get_bag()
        for num in bag:
            cnt[num] += 1
    
    print(f"机器吐出到{n}号球, 袋子大小为{m}")
    print(f"每个球被选中的概率应该接近{m / n:.4f}")
    print(f"一共测试{test_times}次")
    for i in range(1, n + 1):
        print(f"{i}被选中次数 : {cnt[i]}, 被选中概率 : {cnt[i] / test_times:.4f}")
    print("测试结束")
    
    print("\n=== LeetCode 382. 链表随机节点测试 ===")
    # 构造链表 1->2->3->4->5
    head = ListNode(1)
    head.next = ListNode(2)
    head.next.next = ListNode(3)
    head.next.next.next = ListNode(4)
    head.next.next.next.next = ListNode(5)
    
    solution = SolutionLinkedList(head)
    print("随机选择10次链表节点:")
    for i in range(10):
        print(f"选中节点值: {solution.get_random()}")
    
    print("\n=== LeetCode 398. 随机数索引测试 ===")
    nums = [1, 2, 3, 3, 3]
    solution2 = SolutionRandomPickIndex(nums)
    print("随机选择目标数字3的索引10次:")
    for i in range(10):
        print(f"选中索引: {solution2.pick(3)}")
    
    print("\n=== LeetCode 710. 黑名单中的随机数测试 ===")
    n710 = 10
    blacklist = [2, 3, 5]
    solution710 = SolutionBlacklistRandom(n710, blacklist)
    print("随机选择10次不在黑名单中的数:")
    for i in range(10):
        print(f"选中数字: {solution710.pick()}")
    
    print("\n=== 数据流随机采样测试 ===")
    sampler = DataStreamSampler(5)
    for i in range(1, 101):
        sampler.add(i)
    print("从100个元素中随机采样5个:")
    print(sampler.get_sample())
    
    print("\n=== 加权随机采样测试 ===")
    weighted_nums = [1, 2, 3]
    weights = [1, 2, 3]  # 权重分别为1,2,3
    weighted_sampler = WeightedSampler(weighted_nums, weights)
    weighted_results = []
    for _ in range(10000):
        weighted_results.append(weighted_sampler.pick_index())
    print("权重为[1,2,3]的元素采样结果统计:")
    weighted_count = {}
    for result in weighted_results:
        weighted_count[result] = weighted_count.get(result, 0) + 1
    for num, count in weighted_count.items():
        print(f"元素 {num}: 被选中{count}次, 概率{count / 10000:.4f}")
    
    print("\n=== 算法时间复杂度分析 ===")
    print("基础蓄水池采样: O(n) 时间, O(k) 空间")
    print("链表随机节点: O(n) 时间, O(1) 空间")
    print("随机数索引: O(n) 时间, O(1) 空间")
    print("黑名单随机数: O(B) 初始化, O(1) 查询, O(B) 空间")
    print("加权随机采样: O(n) 查询, O(n) 空间")
    
    print("\n=== Python特有优化技巧 ===")
    print("1. 使用random模块的randint而非randrange以提高代码可读性")
    print("2. 在Python中，对于大数据集可以考虑使用numpy进行向量化操作")
    print("3. 使用字典推导式和集合操作提高代码的简洁性和效率")
    print("4. 对于文件处理，使用with语句确保资源正确关闭")
    print("5. 使用异常处理提高代码的健壮性")
    
    # 可选：运行性能基准测试
    print("\n=== 性能基准测试（可选） ===")
    try:
        bench_results = benchmark_sampling_algorithms(1000, 100, 100)
        for algorithm, time_taken in bench_results.items():
            print(f"{algorithm}: 平均耗时 {time_taken:.6f} 秒")
    except Exception as e:
        print(f"基准测试失败: {e}")


if __name__ == "__main__":
    main()