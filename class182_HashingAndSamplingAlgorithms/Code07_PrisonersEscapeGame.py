import random
from typing import List

# 囚徒逃脱游戏 - 循环排列算法的Python实现

def generate_random_array(length: int) -> List[int]:
    """
    生成随机排列数组
    
    原本每个位置的数都等概率出现在自己或者其他位置
    """
    arr = list(range(length))
    for i in range(length - 1, 0, -1):
        j = random.randint(0, i)
        arr[i], arr[j] = arr[j], arr[i]
    return arr


def swap(arr: List[int], i: int, j: int) -> None:
    """交换数组中两个位置的元素"""
    arr[i], arr[j] = arr[j], arr[i]


def max_circle(arr: List[int]) -> int:
    """
    求arr中最大环的长度
    
    算法原理：
    1. 遍历数组，对于每个位置i
    2. 如果arr[i] != i，说明不在正确位置，需要进行循环调整
    3. 通过交换将元素放到正确位置，统计循环长度
    """
    max_circle_length = 1
    # 创建副本避免修改原数组
    copy = arr[:]
    
    for i in range(len(copy)):
        cur_circle = 1
        # 当前元素不在正确位置时继续循环
        while i != copy[i]:
            swap(copy, i, copy[i])
            cur_circle += 1
        max_circle_length = max(max_circle_length, cur_circle)
    
    return max_circle_length


def escape1(people: int, try_times: int, test_times: int) -> float:
    """
    通过多次模拟实验得到的概率
    
    参数:
    people: 囚徒数量
    try_times: 每个囚徒的最大尝试次数
    test_times: 实验次数
    """
    escape_count = 0
    for _ in range(test_times):
        arr = generate_random_array(people)
        if max_circle(arr) <= try_times:
            escape_count += 1
    return escape_count / test_times


def escape2(people: int, try_times: int) -> float:
    """
    公式版计算逃脱概率
    
    一定要保证tryTimes大于等于people的一半，否则该函数失效
    导致死亡的情况数 : C(r,100) * (r-1)! * (100-r)!，r从51~100，累加起来
    死亡概率 : C(r,100) * (r-1)! * (100-r)! / 100!，r从51~100，累加起来
    化简后的死亡概率 : 1/r，r从51~100，累加起来
    """
    a = 0.0
    for r in range(try_times + 1, people + 1):
        a += 1.0 / r
    return 1.0 - a


def prisoner_attempt(permutation: List[int], prisoner_id: int, max_attempts: int) -> bool:
    """
    模拟囚徒逃脱过程
    
    参数:
    permutation: 盒子中囚犯编号的排列
    prisoner_id: 囚犯编号
    max_attempts: 最大尝试次数
    
    返回:
    是否成功找到自己的编号
    """
    current_box = prisoner_id
    attempts = 0
    
    while attempts < max_attempts:
        number_in_box = permutation[current_box]
        if number_in_box == prisoner_id:
            return True  # 成功找到自己的编号
        current_box = number_in_box  # 前往下一个盒子
        attempts += 1
    
    return False  # 尝试次数用完仍未找到


def all_prisoners_escape(people: int, try_times: int) -> bool:
    """
    模拟所有囚徒的逃脱尝试
    
    参数:
    people: 囚徒数量
    try_times: 每人最大尝试次数
    
    返回:
    所有囚徒是否都成功
    """
    permutation = generate_random_array(people)
    
    # 每个囚徒都尝试找到自己的编号
    for prisoner_id in range(people):
        if not prisoner_attempt(permutation, prisoner_id, try_times):
            return False  # 任何一个囚徒失败，所有人都会被处决
    
    return True  # 所有囚徒都成功


def analyze_cycles(permutation: List[int]) -> List[int]:
    """
    分析排列的循环结构
    
    参数:
    permutation: 排列数组
    
    返回:
    循环长度列表
    """
    cycles = []
    visited = [False] * len(permutation)
    copy = permutation[:]
    
    for i in range(len(copy)):
        if not visited[i]:
            cycle_length = 0
            current = i
            
            # 遍历当前循环
            while not visited[current]:
                visited[current] = True
                current = copy[current]
                cycle_length += 1
            
            cycles.append(cycle_length)
    
    return cycles


def main():
    people = 100
    # 一定要保证tryTimes大于等于people的一半
    try_times = 50
    test_times = 100000
    
    print(f"参与游戏的人数 : {people}")
    print(f"每人的尝试次数 : {try_times}")
    print(f"模拟实验的次数 : {test_times}")
    print(f"通过模拟实验得到的概率为 : {escape1(people, try_times, test_times):.6f}")
    print(f"通过公式计算得到的概率为 : {escape2(people, try_times):.6f}")
    
    print("\n=== 循环结构分析示例 ===")
    # 创建一个示例排列 [1, 2, 0, 4, 3] 表示:
    # 盒子0中有编号1，盒子1中有编号2，盒子2中有编号0（循环0->1->2->0）
    # 盒子3中有编号4，盒子4中有编号3（循环3->4->3）
    example = [1, 2, 0, 4, 3]
    cycles = analyze_cycles(example)
    print(f"示例排列: {example}")
    print(f"循环结构: {cycles}")
    print(f"最大循环长度: {max(cycles)}")
    
    print("\n=== 单次逃脱模拟 ===")
    test_permutation = generate_random_array(10)
    print(f"测试排列: {test_permutation}")
    test_cycles = analyze_cycles(test_permutation)
    print(f"循环结构: {test_cycles}")
    print(f"最大循环长度: {max(test_cycles)}")
    
    success = all_prisoners_escape(10, 5)
    print(f"10个囚徒是否全部逃脱: {success}")


if __name__ == "__main__":
    main()