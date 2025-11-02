"""
裴蜀定理模版题 - Python实现

题目描述：
给定长度为n的一组整数值[a1, a2, a3...]，你找到一组数值[x1, x2, x3...]
要让a1*x1 + a2*x2 + a3*x3...得到的结果为最小正整数
返回能得到的最小正整数是多少

解题思路：
根据裴蜀定理，对于整数a1, a2, ..., an，存在整数x1, x2, ..., xn使得
a1*x1 + a2*x2 + ... + an*xn = gcd(a1, a2, ..., an)
因此，线性组合能得到的最小正整数就是这n个数的最大公约数

算法复杂度：
时间复杂度：O(n * log(min(ai)))
空间复杂度：O(1)

题目链接：
洛谷 P4549 【模板】裴蜀定理
https://www.luogu.com.cn/problem/P4549

相关题目：
1. HDU 5512 Pagodas
   链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
   本题涉及数论知识，与最大公约数有关

2. Codeforces 1011E Border
   链接：https://codeforces.com/contest/1011/problem/E
   本题需要根据裴蜀定理求解可能到达的位置

3. LeetCode 1250. 检查「好数组」
   链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
   本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组

工程化考虑：
1. 异常处理：需要处理输入非法、负数等情况
2. 边界条件：需要考虑n=1的情况
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


def bezout_lemma(nums):
    """
    裴蜀定理实现
    
    算法思路：
    1. 依次计算这n个数的最大公约数
    2. 根据裴蜀定理，线性组合能得到的最小正整数就是最大公约数
    
    :param nums: 整数列表
    :return: 最小正整数组合结果
    """
    ans = 0
    for num in nums:
        ans = gcd(abs(num), ans)
    return ans


def main():
    """
    主方法 - 裴蜀定理模板题测试
    """
    print("=== 裴蜀定理模板题测试 ===")
    
    # 测试用例1
    nums1 = [6, 10, 15]
    ans1 = bezout_lemma(nums1)
    print(f"测试1: {nums1} 的最小正整数组合 = {ans1}")
    
    # 测试用例2
    nums2 = [4, 6, 8]
    ans2 = bezout_lemma(nums2)
    print(f"测试2: {nums2} 的最小正整数组合 = {ans2}")
    
    # 测试用例3
    nums3 = [3, 6, 9]
    ans3 = bezout_lemma(nums3)
    print(f"测试3: {nums3} 的最小正整数组合 = {ans3}")
    
    # 测试用例4：单个元素
    nums4 = [7]
    ans4 = bezout_lemma(nums4)
    print(f"测试4: {nums4} 的最小正整数组合 = {ans4}")
    
    # 测试用例5：包含负数
    nums5 = [-6, 10, -15]
    ans5 = bezout_lemma(nums5)
    print(f"测试5: {nums5} 的最小正整数组合 = {ans5}")
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    main()