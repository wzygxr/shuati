"""
Good Subarrays (Codeforces 1398C)

题目描述:
给定一个由数字字符组成的字符串s，定义"好数组"为：数组中所有元素的和等于元素个数。
求字符串s的所有连续子串中，有多少个"好数组"。

示例:
输入: s = "111"
输出: 3
解释: 子串"1"(位置0), "1"(位置1), "1"(位置2)都是好数组

输入: s = "123"
输出: 4
解释: 子串"1", "2", "3", "123"都是好数组

输入: s = "101010"
输出: 15

提示:
1 <= t <= 1000 (测试用例数)
1 <= |s| <= 10^5 (字符串长度)
字符串只包含数字字符'0'-'9'
所有测试用例的字符串长度总和不超过10^5

题目链接: https://codeforces.com/contest/1398/problem/C

解题思路:
将问题转换为前缀和问题。
1. 对于一个子数组s[l..r]，它是好数组当且仅当sum(s[l..r]) = r-l+1
2. 通过移项得到: sum(s[l..r]) - (r-l+1) = 0
3. 定义b[i] = a[i] - 1，其中a[i]是字符串中第i个字符的数值
4. 那么条件变为: sum(b[l..r]) = 0
5. 进一步转换为前缀和: prefix[r] - prefix[l-1] = 0，即prefix[r] = prefix[l-1]
6. 使用哈希表统计每个前缀和出现的次数，对于每个前缀和值，如果有k次出现，则可以形成k*(k-1)/2个好数组

时间复杂度: O(n) - 需要遍历字符串一次
空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和

这是最优解，因为需要处理所有字符。
"""


def good_subarrays(s):
    """
    计算好数组的个数

    :param s: 输入字符串
    :return: 好数组的个数
    """
    # 边界情况处理
    if not s:
        return 0

    n = len(s)

    # 使用字典记录前缀和及其出现次数
    prefix_count = {0: 1}  # 初始化：前缀和为0出现1次（表示空前缀）

    count = 0  # 结果计数
    prefix = 0  # 当前前缀和

    # 遍历字符串
    for i in range(n):
        # 将字符转换为数字并减1
        digit = int(s[i])
        b = digit - 1

        # 更新前缀和
        prefix += b

        # 如果当前前缀和之前出现过，说明存在好数组
        if prefix in prefix_count:
            count += prefix_count[prefix]

        # 更新当前前缀和的出现次数
        prefix_count[prefix] = prefix_count.get(prefix, 0) + 1

    return count


def main():
    """测试用例"""
    # 测试用例1
    s1 = "111"
    result1 = good_subarrays(s1)
    # 预期输出: 3
    print(f'测试用例1 "{s1}": {result1}')

    # 测试用例2
    s2 = "123"
    result2 = good_subarrays(s2)
    # 预期输出: 4
    print(f'测试用例2 "{s2}": {result2}')

    # 测试用例3
    s3 = "101010"
    result3 = good_subarrays(s3)
    # 预期输出: 15
    print(f'测试用例3 "{s3}": {result3}')

    # 测试用例4
    s4 = "000"
    result4 = good_subarrays(s4)
    # 预期输出: 6
    print(f'测试用例4 "{s4}": {result4}')


if __name__ == "__main__":
    main()