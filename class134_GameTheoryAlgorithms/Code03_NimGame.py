# 尼姆博弈(Nim Game)
# 一共有n堆石头，两人轮流进行游戏
# 在每个玩家的回合中，玩家需要选择任何一个非空的石头堆，并从这堆石头中移除任意正数的石头数量
# 谁先拿走最后的石头就获胜，返回最终谁会获胜
# 测试链接 : https://www.luogu.com.cn/problem/P2197
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法
# 提交以下的code，提交时请把类名改成"Main"，可以直接通过
#
# 算法思路：
# 1. 尼姆博弈是经典的博弈论问题
# 2. 核心思想是计算所有堆石子数的异或和(Nim-sum)
# 3. 当Nim-sum为0时，当前玩家处于必败态；否则处于必胜态
# 4. 这是因为处于必胜态的玩家总能通过一步操作使Nim-sum变为0
# 5. 而处于必败态的玩家无论如何操作都会使Nim-sum变为非0
#
# 时间复杂度：O(n) - 需要遍历所有堆计算异或和
# 空间复杂度：O(1) - 只使用了常数级别的额外空间
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 多堆石子
#    - 两人轮流从任意一堆取任意数量石子
#    - 取走最后一颗石子者获胜
# 2. 解题技巧：
#    - 计算所有堆石子数的异或和
#    - 异或和为0表示当前玩家必败，否则必胜
# 3. 变种问题：
#    - 每堆可取石子数量受限
#    - 最后取石子者失败（反尼姆博弈）
#    - 取石子规则变化（如只能取斐波那契数个石子）
#
# 相关题目链接：
# 1. 洛谷 P2197: https://www.luogu.com.cn/problem/P2197
# 2. LeetCode 292: https://leetcode.com/problems/nim-game/
# 3. HDU 1850: http://acm.hdu.edu.cn/showproblem.php?pid=1850
# 4. POJ 2234: http://poj.org/problem?id=2234
# 5. AtCoder DP Contest L - Deque: https://atcoder.jp/contests/dp/tasks/dp_l

import sys
import threading

def main():
    # 读取测试用例数量
    try:
        t = int(input())
        for _ in range(t):
            # 读取堆数
            n = int(input())
            # 读取每堆石子数并计算异或和
            eor = 0
            stones = list(map(int, input().split()))
            for stone in stones:
                eor ^= stone
            
            # 判断胜负
            if eor != 0:
                print("Yes")
            else:
                print("No")
    except (ValueError, EOFError):
        # 如果没有输入，运行演示模式
        demo()

def demo():
    """演示函数"""
    print("=== 尼姆博弈算法演示 ===")
    
    # 测试用例
    test_cases = [
        [1, 2, 3],  # 异或和=0，后手胜
        [1, 3, 5],  # 异或和=7，先手胜
        [2, 4, 6],  # 异或和=0，后手胜
        [1, 1, 1],  # 异或和=1，先手胜
        [5, 5, 5]   # 异或和=5，先手胜
    ]
    
    for i, stones in enumerate(test_cases, 1):
        eor = 0
        for stone in stones:
            eor ^= stone
        
        result = "先手胜" if eor != 0 else "后手胜"
        print(f"测试用例{i}: {stones} → 异或和={eor} → {result}")
    
    print("=== 各大平台题目链接 ===")
    print("1. 洛谷 P2197: https://www.luogu.com.cn/problem/P2197")
    print("2. LeetCode 292: https://leetcode.com/problems/nim-game/")
    print("3. HDU 1850: http://acm.hdu.edu.cn/showproblem.php?pid=1850")
    print("4. POJ 2234: http://poj.org/problem?id=2234")
    print("5. AtCoder DP Contest L - Deque: https://atcoder.jp/contests/dp/tasks/dp_l")

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