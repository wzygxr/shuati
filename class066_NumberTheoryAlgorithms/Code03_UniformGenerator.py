"""
均匀生成器 - Python实现

题目描述：
如果有两个数字step和mod，那么可以由以下方式生成很多数字
seed(1) = 0，seed(i+1) = (seed(i) + step) % mod
比如，step = 3、mod = 5
seed(1) = 0，seed(2) = 3，seed(3) = 1，seed(4) = 4，seed(5) = 2
如果能产生0 ~ mod-1所有数字，step和mod的组合叫  "Good Choice"
如果无法产生0 ~ mod-1所有数字，step和mod的组合叫 "Bad Choice"
根据step和mod，打印结果

解题思路：
1. 根据数论知识，当gcd(step, mod) = 1时，能产生0 ~ mod-1所有数字
2. 否则无法产生所有数字

算法复杂度：
时间复杂度：O(log(min(step, mod)))
空间复杂度：O(log(min(step, mod)))

题目链接：
POJ 1597 Uniform Generator
http://poj.org/problem?id=1597

相关题目：
1. 洛谷 P1516 青蛙的约会
   链接：https://www.luogu.com.cn/problem/P1516
   本题需要求解同余方程，是扩展欧几里得算法的经典应用

2. POJ 1061 青蛙的约会
   链接：http://poj.org/problem?id=1061
   与本题完全相同，是POJ上的经典题目

3. POJ 2115 C Looooops
   链接：http://poj.org/problem?id=2115
   本题需要求解模线性方程，可以转化为线性丢番图方程

工程化考虑：
1. 异常处理：需要处理输入非法等情况
2. 边界条件：需要考虑step、mod的边界值
3. 性能优化：使用欧几里得算法计算最大公约数

调试能力：
1. 添加断言验证中间结果
2. 打印关键变量的实时值
3. 性能退化排查
"""


def gcd(a, b):
    """
    欧几里得算法计算最大公约数
    
    算法原理：
    gcd(a, b) = gcd(b, a % b)，当b为0时，gcd(a, 0) = a
    
    时间复杂度：O(log(min(a, b)))
    空间复杂度：O(log(min(a, b)))（递归调用栈）
    
    :param a: 第一个整数
    :param b: 第二个整数
    :return: a和b的最大公约数
    """
    return a if b == 0 else gcd(b, a % b)


def is_good_choice(step, mod):
    """
    判断均匀生成器是否为Good Choice
    
    :param step: 步长
    :param mod: 模数
    :return: 如果是Good Choice返回True，否则返回False
    """
    return gcd(step, mod) == 1


def main():
    """
    主方法 - 均匀生成器问题测试
    """
    print("=== 均匀生成器问题测试 ===")
    
    # 测试用例1
    step1, mod1 = 3, 5
    result1 = is_good_choice(step1, mod1)
    print(f"测试1: step={step1}, mod={mod1}, 结果: {'Good Choice' if result1 else 'Bad Choice'}")
    
    # 测试用例2
    step2, mod2 = 2, 4
    result2 = is_good_choice(step2, mod2)
    print(f"测试2: step={step2}, mod={mod2}, 结果: {'Good Choice' if result2 else 'Bad Choice'}")
    
    # 测试用例3
    step3, mod3 = 5, 7
    result3 = is_good_choice(step3, mod3)
    print(f"测试3: step={step3}, mod={mod3}, 结果: {'Good Choice' if result3 else 'Bad Choice'}")
    
    # 测试用例4
    step4, mod4 = 6, 9
    result4 = is_good_choice(step4, mod4)
    print(f"测试4: step={step4}, mod={mod4}, 结果: {'Good Choice' if result4 else 'Bad Choice'}")
    
    # 测试用例5：边界情况
    step5, mod5 = 1, 10
    result5 = is_good_choice(step5, mod5)
    print(f"测试5: step={step5}, mod={mod5}, 结果: {'Good Choice' if result5 else 'Bad Choice'}")
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    main()