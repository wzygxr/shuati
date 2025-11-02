#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
约瑟夫环问题算法实现
经典约瑟夫问题：n个人围成一圈，每次数到k的人出列，求最后剩下的人的位置

适用场景：
- 循环淘汰问题
- 环状结构中的选择问题
- 递推算法的典型应用

相关题目:
1. LeetCode 390. Elimination Game (消除游戏)
   链接: https://leetcode.cn/problems/elimination-game/
2. LeetCode 1823. Find the Winner of the Circular Game (找出游戏的获胜者)
   链接: https://leetcode.cn/problems/find-the-winner-of-the-circular-game/
3. POJ 1012 Joseph
   链接: http://poj.org/problem?id=1012
4. POJ 2886 Who Gets the Most Candies?
   链接: http://poj.org/problem?id=2886
5. Luogu P1996 约瑟夫问题
   链接: https://www.luogu.com.cn/problem/P1996
"""

import time
import sys


class Josephus:
    """
    约瑟夫环问题的多种实现方法
    提供了递推法、模拟法、递归法等多种解决方案
    每一种方法都有详细的时间复杂度和空间复杂度分析
    """
    
    @staticmethod
    def compute(n: int, k: int) -> int:
        """
        使用递推公式求解约瑟夫环问题的最优解
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        递推公式：f(n,k) = (f(n-1,k) + k) % n
        其中f(n,k)表示n个人数k时最后剩下的人的索引（从0开始）
        这里返回的是从1开始计数的结果
        
        Args:
            n: 总人数
            k: 每次数到k的人出列
            
        Returns:
            最后剩下的人的位置（从1开始计数）
            
        Raises:
            ValueError: 当参数不合法时抛出异常
        """
        # 参数校验
        if n <= 0 or k <= 0:
            raise ValueError("n和k必须为正整数")
        
        # 特殊情况优化：当k=1时，最后剩下的是第n个人
        if k == 1:
            return n
        
        # 特殊情况优化：当n=1时，只剩一个人，就是他自己
        if n == 1:
            return 1
        
        # 使用递推法求解
        # 初始条件：当只有1个人时，位置就是1
        ans = 1
        
        # 从2个人开始递推，直到n个人
        for c in range(2, n + 1):
            # 递推公式：新位置 = (旧位置 + k - 1) % 当前人数 + 1
            # +k-1是因为数到第k个人，-1是为了从0开始计算
            # %c是为了处理环形结构
            # +1是为了将结果转换回从1开始计数
            ans = (ans + k - 1) % c + 1
        
        return ans
    
    @staticmethod
    def josephus_0_based(n: int, k: int) -> int:
        """
        使用递推公式（索引从0开始）
        这是标准的约瑟夫环递推公式实现
        
        Args:
            n: 总人数
            k: 每次数到k的人出列
            
        Returns:
            最后剩下的人的索引（从0开始）
            
        Raises:
            ValueError: 当参数不合法时抛出异常
        """
        if n <= 0 or k <= 0:
            raise ValueError("n和k必须为正整数")
        
        res = 0  # f(1) = 0
        for i in range(2, n + 1):
            res = (res + k) % i
        return res
    
    @staticmethod
    def simulate(n: int, k: int) -> int:
        """
        使用模拟法求解约瑟夫环问题
        适用于小数据量，直观但效率较低
        时间复杂度: O(nk)
        空间复杂度: O(n)
        
        Args:
            n: 总人数
            k: 每次数到k的人出列
            
        Returns:
            最后剩下的人的位置（从1开始计数）
            
        Raises:
            ValueError: 当参数不合法时抛出异常
        """
        if n <= 0 or k <= 0:
            raise ValueError("n和k必须为正整数")
        
        # 创建列表存储所有人的位置
        people = list(range(1, n + 1))
        
        index = 0  # 当前开始计数的位置
        
        # 不断删除出列的人，直到只剩一个人
        while len(people) > 1:
            # 计算要删除的人的位置
            # (index + k - 1) % len(people) 确保在列表范围内循环
            index = (index + k - 1) % len(people)
            people.pop(index)
        
        # 返回最后剩下的人的位置
        return people[0]
    
    @staticmethod
    def recursive(n: int, k: int) -> int:
        """
        递归求解约瑟夫环问题
        适用于理解算法原理，但对于大n可能导致递归深度超过Python限制
        时间复杂度: O(n)
        空间复杂度: O(n) 递归调用栈
        
        Args:
            n: 总人数
            k: 每次数到k的人出列
            
        Returns:
            最后剩下的人的索引（从0开始）
            
        Raises:
            ValueError: 当参数不合法时抛出异常
            RecursionError: 当递归深度超过Python限制时抛出异常
        """
        if n <= 0 or k <= 0:
            raise ValueError("n和k必须为正整数")
        
        # 基本情况：只有一个人时，索引为0
        if n == 1:
            return 0
        
        # 递推公式：f(n,k) = (f(n-1,k) + k) % n
        return (Josephus.recursive(n - 1, k) + k) % n
    
    @staticmethod
    def optimized_josephus(n: int, k: int) -> int:
        """
        优化的约瑟夫环算法，当k远小于n时可以进一步优化
        时间复杂度: O(n) 最坏情况，但在k较小的情况下性能更好
        
        Args:
            n: 总人数
            k: 每次数到k的人出列
            
        Returns:
            最后剩下的人的位置（从1开始计数）
            
        Raises:
            ValueError: 当参数不合法时抛出异常
        """
        if n <= 0 or k <= 0:
            raise ValueError("n和k必须为正整数")
        
        # 当k=1时，最后剩下的是第n个人
        if k == 1:
            return n
        
        # 当k较大时，使用标准递推
        if k > n:
            return Josephus.compute(n, n if k % n == 0 else k % n)
        
        res = 0
        for i in range(2, n + 1):
            if res + k < i:
                # 可以跳过多个步骤
                res += k
            else:
                res = (res + k) % i
        
        return res + 1  # 转换为从1开始计数
    
    @staticmethod
    def get_elimination_order(n: int, k: int) -> list:
        """
        输出完整的出列顺序
        
        Args:
            n: 总人数
            k: 每次数到k的人出列
            
        Returns:
            出列顺序的列表
            
        Raises:
            ValueError: 当参数不合法时抛出异常
        """
        if n <= 0 or k <= 0:
            raise ValueError("n和k必须为正整数")
        
        people = list(range(1, n + 1))
        order = []
        index = 0
        
        for _ in range(n):
            index = (index + k - 1) % len(people)
            order.append(people.pop(index))
        
        return order


# 为了保持与原代码的兼容性，实现经典LeetCode 1823题的解法
def find_the_winner(n: int, k: int) -> int:
    """
    LeetCode 1823. Find the Winner of the Circular Game 的解决方案
    题目描述：n个朋友围成一个圈，从朋友1开始，顺时针方向数到第k个人离开圈。
    继续从下一个人开始，重复这一过程，直到只剩下一个人。
    
    Args:
        n: 朋友的数量
        k: 数到第k个人离开
        
    Returns:
        最后剩下的朋友的编号（从1开始计数）
    """
    return Josephus.compute(n, k)

def compute(n: int, k: int) -> int:
    """
    为了兼容题目测试要求，保留原始的compute函数接口
    
    Args:
        n: 总人数
        k: 每次数到k的人出列
        
    Returns:
        最后剩下的人的位置（从1开始计数）
    """
    return Josephus.compute(n, k)


def run_performance_test():
    """
    运行性能测试，比较不同实现方法的效率
    测试各种不同规模的数据，验证算法的实际性能
    """
    test_cases = [
        (5, 3),        # 小数据量基本测试
        (100, 5),      # 中等数据量
        (1000, 10),    # 较大数据量
        (10000, 100)   # 大数据量
    ]
    
    print("性能测试结果:")
    print("=" * 60)
    print("性能优化建议:")
    print("1. 对于大数据量(n > 10^4)，推荐使用递推法或优化法")
    print("2. 对于小数据量，模拟法更直观但效率较低")
    print("3. 递归法仅适用于理解原理，实际应用中应避免使用")
    print(f"{'测试用例':<15}{'递推法(ms)':<15}{'模拟法(ms)':<15}{'优化法(ms)':<15}")
    print("=" * 60)
    
    for n, k in test_cases:
        # 测试递推法
        start_time = time.time()
        res1 = Josephus.compute(n, k)
        recursive_time = (time.time() - start_time) * 1000
        
        # 只在小数据量时测试模拟法
        if n <= 10000:
            start_time = time.time()
            res2 = Josephus.simulate(n, k)
            simulate_time = (time.time() - start_time) * 1000
        else:
            simulate_time = "-"
            res2 = res1  # 保持一致
        
        # 测试优化法
        start_time = time.time()
        res3 = Josephus.optimized_josephus(n, k)
        optimized_time = (time.time() - start_time) * 1000
        
        print(f"(n={n},k={k}){'':<5}{recursive_time:.3f}{'':<10}{simulate_time if simulate_time == '-' else f'{simulate_time:.3f}'}{'':<10}{optimized_time:.3f}")
    
    print("=" * 60)


def run_correctness_test():
    """
    运行正确性测试，验证所有实现方法的结果一致性
    覆盖各种边界情况和典型用例
    """
    test_cases = [
        (1, 1, 1),     # n=1特殊情况
        (5, 3, 2),     # 经典示例
        (10, 2, 5),    # 常见测试用例
        (7, 3, 4),     # 另一个示例
        (1, 100, 1),   # k远大于n的情况
        (10, 1, 10),   # k=1的特殊情况
        (100, 100, 73), # k=n的情况
        (1000, 3, 604)  # 大数据量测试
    ]
    
    print("正确性测试结果:")
    print("=" * 80)
    print(f"{'测试用例':<15}{'预期结果':<10}{'递推法':<10}{'递推法(0基)':<15}{'模拟法':<10}{'优化法':<10}")
    print("=" * 80)
    
    all_passed = True
    
    for n, k, expected in test_cases:
        try:
            res1 = Josephus.compute(n, k)
            res2 = Josephus.josephus_0_based(n, k) + 1  # 转换为从1开始
            res3 = Josephus.simulate(n, k) if n <= 10000 else "-"
            res4 = Josephus.optimized_josephus(n, k)
            
            # 检查结果是否正确
            passed1 = res1 == expected
            passed2 = res2 == expected
            passed3 = res3 == expected if res3 != "-" else True
            passed4 = res4 == expected
            
            all_tests_passed = passed1 and passed2 and passed3 and passed4
            status = "✓" if all_tests_passed else "✗"
            
            print(f"(n={n},k={k}){'':<5}{expected:<10}{res1:<10}{res2:<15}{res3:<10}{res4:<10}{status}")
            
            if not all_tests_passed:
                all_passed = False
                
        except Exception as e:
            print(f"(n={n},k={k}){'':<5}测试出错: {str(e)}")
            all_passed = False
    
    print("=" * 80)
    print(f"整体测试结果: {'全部通过' if all_passed else '存在错误'}")


def analyze_complexity():
    """
    分析各种约瑟夫环算法实现的时间和空间复杂度
    并提供工程选择依据
    """
    print("约瑟夫环算法复杂度分析:")
    print("=" * 80)
    print(f"{'算法':<15}{'时间复杂度':<15}{'空间复杂度':<15}{'适用场景':<20}{'优势':<15}")
    print("=" * 80)
    print(f"{'递推法':<15}{'O(n)':<15}{'O(1)':<15}{'所有场景':<20}{'高效稳定':<15}")
    print(f"{'模拟法':<15}{'O(nk)':<15}{'O(n)':<15}{'小数据量':<20}{'直观易懂':<15}")
    print(f"{'递归法':<15}{'O(n)':<15}{'O(n)':<15}{'教学目的':<20}{'思路清晰':<15}")
    print(f"{'优化法':<15}{'O(n)':<15}{'O(1)':<15}{'k<<n':<20}{'常数项小':<15}")
    print("=" * 80)
    print("工程选择建议:")
    print("1. 实际生产环境中首选递推法，效率最高且空间占用最小")
    print("2. 对于需要跟踪出列顺序的场景，模拟法是必要的选择")
    print("3. 递归法应避免在生产环境使用，仅用于教学或原理展示")
    print("4. 当k远小于n时，优化法可能有更好的常数项性能")

def main():
    """
    主函数，提供交互式界面和测试功能
    支持命令行参数和标准输入两种输入方式
    """
    try:
        # 尝试从命令行参数读取
        if len(sys.argv) == 3:
            n = int(sys.argv[1])
            k = int(sys.argv[2])
        else:
            # 从标准输入读取，提供友好的提示
            print("约瑟夫环问题求解器")
            print("==================")
            print("经典约瑟夫问题：n个人围成一圈，每次数到k的人出列，求最后剩下的人的位置")
            
            # 输入验证循环
            while True:
                try:
                    n_input = input("请输入总人数n (1-1000000): ")
                    n = int(n_input)
                    if n <= 0:
                        print("错误: n必须为正整数")
                        continue
                    break
                except ValueError:
                    print("错误: 请输入有效的整数")
            
            while True:
                try:
                    k_input = input("请输入报数k (1-1000000): ")
                    k = int(k_input)
                    if k <= 0:
                        print("错误: k必须为正整数")
                        continue
                    break
                except ValueError:
                    print("错误: 请输入有效的整数")
        
        # 计算并输出结果
        start_time = time.time()
        result = Josephus.compute(n, k)
        end_time = time.time()
        
        print(f"\n计算结果：")
        print(f"最后剩下的人的位置是: {result}")
        print(f"计算耗时: {(end_time - start_time) * 1000:.3f} ms")
        
        # 测试其他实现方法
        print("\n不同实现方法结果对比:")
        print(f"递推法结果(从0开始): {Josephus.josephus_0_based(n, k)}")
        
        # 只在小数据量时测试模拟法，避免超时
        if n <= 10000:
            start_time = time.time()
            simulate_result = Josephus.simulate(n, k)
            end_time = time.time()
            print(f"模拟法结果: {simulate_result}，耗时: {(end_time - start_time) * 1000:.3f} ms")
        else:
            print(f"模拟法对于大数据量n={n}可能耗时较长，跳过测试")
        
        # 只在小数据量时测试递归法，避免递归深度错误
        if n <= 1000:
            try:
                start_time = time.time()
                recursive_result = Josephus.recursive(n, k) + 1  # 转换为从1开始
                end_time = time.time()
                print(f"递归法结果: {recursive_result}，耗时: {(end_time - start_time) * 1000:.3f} ms")
            except RecursionError:
                print(f"递归法对于n={n}导致递归深度超过Python限制")
                print("注意：Python默认的递归深度限制为约1000层")
            except Exception as e:
                print(f"递归法执行出错: {str(e)}")
        else:
            print(f"递归法对于大数据量n={n}可能导致递归深度错误，跳过测试")
        
        start_time = time.time()
        optimized_result = Josephus.optimized_josephus(n, k)
        end_time = time.time()
        print(f"优化法结果: {optimized_result}，耗时: {(end_time - start_time) * 1000:.3f} ms")
        
        # 只在小数据量时输出出列顺序
        if n <= 100:
            print("\n出列顺序:")
            order = Josephus.get_elimination_order(n, k)
            print(", ".join(map(str, order)))
        else:
            print(f"\n出列顺序共有{n}个元素，仅在n≤100时显示")
        
        # 输出时间复杂度分析
        analyze_complexity()
        
        # 询问是否运行性能测试
        run_perf = input("\n是否运行性能测试? (y/n): ")
        if run_perf.lower() == 'y':
            run_performance_test()
        
        # 询问是否运行正确性测试
        run_correct = input("是否运行正确性测试? (y/n): ")
        if run_correct.lower() == 'y':
            run_correctness_test()
            
    except ValueError as e:
        # 处理非法参数
        print(f"错误: {str(e)}")
    except KeyboardInterrupt:
        print("\n程序被用户中断")
    except Exception as e:
        # 处理其他异常
            print(f"发生错误: {str(e)}")
            import traceback
            traceback.print_exc()


if __name__ == "__main__":
    # 设置递归深度限制，防止小数据量的递归测试失败
    sys.setrecursionlimit(10000)
    
    # 保持原始接口兼容性
    try:
        # 尝试使用原始的输入方式
        import sys
        if len(sys.argv) == 3:
            main()
        else:
            # 读取一行输入，尝试用空格分隔
            line = input().strip()
            if ' ' in line:
                n, k = map(int, line.split())
                print(compute(n, k))
            else:
                # 如果输入格式不符合原始期望，运行完整的main函数
                main()
    except EOFError:
        # 当没有输入时，运行完整的main函数
        main()
    except Exception as e:
        # 如果出现任何异常，运行完整的main函数
        main()

"""
使用示例：
1. 直接运行：python Code03_Joseph.py
   然后输入n和k的值

2. 命令行参数运行：python Code03_Joseph.py 5 3
   直接指定n=5, k=3

3. 作为模块导入：
   from Code03_Joseph import Josephus
   result = Josephus.compute(5, 3)
   print(result)  # 输出: 2

4. LeetCode 1823题解：
   from Code03_Joseph import find_the_winner
   result = find_the_winner(5, 3)
   print(result)  # 输出: 2

算法题解总结：
1. 约瑟夫环问题是典型的递推问题，最优解法是O(n)时间复杂度的递推公式
2. 适用于环形结构中的淘汰问题，如游戏、调度算法等场景
3. 常见的变种包括双向淘汰（如LeetCode 390）、带权重淘汰等
4. 递推公式的核心思想是从子问题的解推导出原问题的解
5. 实际应用中需要注意边界条件处理和大规模数据的性能优化
"""