# 反尼姆博弈(反常游戏)
# 一共有n堆石头，两人轮流进行游戏
# 在每个玩家的回合中，玩家需要选择任何一个非空的石头堆，并从这堆石头中移除任意正数的石头数量
# 谁先拿走最后的石头就失败，返回最终谁会获胜
# 先手获胜，打印John
# 后手获胜，打印Brother
# 测试链接 : https://www.luogu.com.cn/problem/P4279
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法
# 提交以下的code，提交时请把类名改成"Main"，可以直接通过
#
# 算法思路：
# 1. 反尼姆博弈是尼姆博弈的变种，胜利条件相反
# 2. 解题需要分两种情况讨论：
#    a) 所有堆的石子数都是1：此时判断堆数的奇偶性，奇数后手胜，偶数先手胜
#    b) 存在石子数大于1的堆：此时判断所有堆石子数的异或和，异或和为0后手胜，否则先手胜
# 3. 这是因为在反尼姆博弈中，玩家需要避免拿到最后一个石子
#
# 时间复杂度：O(n) - 需要遍历所有堆计算异或和和统计石子数为1的堆数
# 空间复杂度：O(1) - 只使用了常数级别的额外空间
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 多堆石子
#    - 两人轮流从任意一堆取任意数量石子
#    - 最后取石子者失败
# 2. 解题技巧：
#    - 分情况讨论：所有堆都只有1个石子 vs 存在石子数大于1的堆
#    - 所有堆都只有1个石子时，根据堆数奇偶性判断胜负
#    - 存在石子数大于1的堆时，根据异或和判断胜负
# 3. 变种问题：
#    - 每堆可取石子数量受限
#    - 石子价值不同
#
# 相关题目链接：
# 1. 洛谷 P4279: https://www.luogu.com.cn/problem/P4279
# 2. HDU 2509: http://acm.hdu.edu.cn/showproblem.php?pid=2509
# 3. POJ 2975: http://poj.org/problem?id=2975

import sys
import threading

def compute(stones, n):
    """
    计算反尼姆博弈结果
    :param stones: 石子数组
    :param n: 堆数
    :return: 获胜者
    
    算法思路：
    1. 如果所有堆都只有1个石子，判断堆数奇偶性
    2. 如果存在石子数大于1的堆，判断异或和是否为0
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    eor = 0
    sum_one = 0
    for i in range(n):
        eor ^= stones[i]
        if stones[i] == 1:
            sum_one += 1
    
    # 所有堆都只有1个石子
    if sum_one == n:
        # 奇数堆后手胜，偶数堆先手胜
        return "Brother" if (n & 1) == 1 else "John"
    else:
        # 存在石子数大于1的堆，异或和为0后手胜，否则先手胜
        return "John" if eor != 0 else "Brother"

def main():
    # 读取测试用例数量
    try:
        t = int(input())
        for _ in range(t):
            # 读取堆数
            n = int(input())
            # 读取每堆石子数
            stones = list(map(int, input().split()))
            print(compute(stones, n))
    except (ValueError, EOFError):
        # 如果没有输入，运行演示模式
        demo()

def demo():
    """演示函数"""
    print("=== 反尼姆博弈算法演示 ===")
    
    # 测试用例
    test_cases = [
        ([1, 1, 1], 3),  # 所有堆都是1，奇数堆 → Brother胜
        ([1, 1], 2),      # 所有堆都是1，偶数堆 → John胜
        ([1, 2, 3], 3),   # 存在大于1的堆，异或和=0 → Brother胜
        ([1, 3, 5], 3),   # 存在大于1的堆，异或和=7 → John胜
        ([2, 2], 2)       # 存在大于1的堆，异或和=0 → Brother胜
    ]
    
    for i, (stones, n) in enumerate(test_cases, 1):
        result = compute(stones, n)
        print(f"测试用例{i}: {stones} → {result}")
    
    print("=== 各大平台题目链接 ===")
    print("1. 洛谷 P4279: https://www.luogu.com.cn/problem/P4279")
    print("2. HDU 2509: http://acm.hdu.edu.cn/showproblem.php?pid=2509")
    print("3. POJ 2975: http://poj.org/problem?id=2975")

# 为了提高输入输出效率，使用以下方式运行
if __name__ == "__main__":
    try:
        sys.setrecursionlimit(1 << 25)
        threading.stack_size(1 << 27)
        thread = threading.Thread(target=main)
        thread.start()
        thread.join()
    except:
        # 如果多线程失败，直接运行演示
        demo()