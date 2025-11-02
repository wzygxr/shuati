"""
洛谷 P1659 [国家集训队]拉拉队排练

题目描述:
给定一个字符串s和数值k，只关心所有奇数长度的回文子串
返回其中长度前k名的回文子串的长度乘积是多少
如果奇数长度的回文子串个数不够k个，返回-1

输入格式:
第一行两个整数n, k
第二行一个字符串s

输出格式:
一个整数表示答案，对19930726取模

数据范围:
n <= 10^6, k <= 10^12

题目链接: https://www.luogu.com.cn/problem/P1659

解题思路:
使用Manacher算法统计所有奇数长度回文子串的数量，然后按长度从大到小计算乘积

算法步骤:
1. 使用Manacher算法预处理字符串，得到每个位置的回文半径
2. 统计每个长度出现的次数（只关心奇数长度）
3. 从最大长度开始，依次累加计数，计算前k个长度的乘积
4. 如果总数不足k，返回-1

时间复杂度: O(n)
空间复杂度: O(n)
"""

MOD = 19930726


def fast_power(base, exp):
    """
    快速幂算法
    
    :param base: 底数
    :param exp: 指数
    :return: base^exp % MOD
    """
    result = 1
    base %= MOD
    
    while exp > 0:
        if exp & 1:
            result = (result * base) % MOD
        base = (base * base) % MOD
        exp >>= 1
    
    return result


def compute_product(s, k):
    """
    计算前k名奇数长度回文子串的长度乘积
    
    :param s: 输入字符串
    :param k: 前k名
    :return: 长度乘积，对MOD取模
    """
    n = len(s)
    if n == 0:
        return 1 if k == 0 else -1
    
    # 预处理字符串
    processed = "#" + "#".join(s) + "#"
    m = len(processed)
    
    # Manacher算法
    p = [0] * m
    center = right = 0
    
    for i in range(m):
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        # 尝试扩展回文串
        while (i + p[i] + 1 < m and 
               i - p[i] - 1 >= 0 and 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]):
            p[i] += 1
        
        if i + p[i] > right:
            center = i
            right = i + p[i]
    
    # 统计奇数长度回文子串数量
    cnt = [0] * (n + 1)
    
    for i in range(m):
        if p[i] > 0:
            # 实际回文长度等于回文半径（因为预处理后的字符串中，回文半径就是实际长度）
            actual_len = p[i]
            if actual_len > 0:
                cnt[actual_len] += 1
    
    # 计算乘积
    result = 1
    total = 0
    
    # 从最大长度开始遍历（考虑所有奇数长度）
    for length in range(n, 0, -1):
        if k <= 0:
            break
            
        # 只考虑奇数长度
        if length % 2 == 1 and cnt[length] > 0:
            total += cnt[length]
            take = min(k, cnt[length])
            result = (result * fast_power(length, take)) % MOD
            k -= take
    
    return result if k <= 0 else -1


def test_compute_product():
    """
    测试函数，验证算法的正确性
    """
    test_cases = [
        ("ababa", 3, 45),  # 5*3*3=45
        ("aaa", 2, 3),     # 3*1=3
        ("a", 1, 1),       # 1=1
        ("abc", 5, -1),    # 不足5个回文子串
    ]
    
    print("测试结果:")
    print("=" * 50)
    
    for i, (s, k, expected) in enumerate(test_cases, 1):
        result = compute_product(s, k)
        status = "通过" if result == expected else "失败"
        print(f"测试用例{i}: s='{s}', k={k}")
        print(f"  输出: {result}, 期望: {expected}, {status}")
        print()


def debug_manacher(s):
    """
    调试函数，打印Manacher算法的中间过程
    
    :param s: 输入字符串
    """
    print(f"\n调试字符串: '{s}'")
    
    # 预处理字符串
    processed = "#" + "#".join(s) + "#"
    print(f"预处理后: '{processed}'")
    
    m = len(processed)
    p = [0] * m
    center = right = 0
    
    print("位置\t字符\t半径")
    for i in range(m):
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        # 尝试扩展回文串
        while (i + p[i] + 1 < m and 
               i - p[i] - 1 >= 0 and 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]):
            p[i] += 1
        
        if i + p[i] > right:
            center = i
            right = i + p[i]
        
        print(f"{i}\t{processed[i]}\t{p[i]}")
    
    return p


def analyze_palindromes(s, p):
    """
    分析回文子串分布
    
    :param s: 原字符串
    :param p: Manacher结果
    """
    n = len(s)
    cnt = [0] * (n + 1)
    
    processed = "#" + "#".join(s) + "#"
    m = len(processed)
    
    print("\n回文子串分析:")
    print("长度\t数量")
    
    for i in range(1, m, 2):  # 只处理奇数位置
        if p[i] > 1:
            actual_len = p[i] - 1
            cnt[actual_len] += 1
    
    total = 0
    for length in range(n, 0, -1):
        if cnt[length] > 0:
            total += cnt[length]
            print(f"{length}\t{cnt[length]}")
    
    print(f"总计: {total} 个奇数长度回文子串")


def main():
    """
    主函数，处理输入输出
    """
    import sys
    
    if len(sys.argv) > 1 and sys.argv[1] == "test":
        # 测试模式
        test_compute_product()
        
        print("\n" + "=" * 50)
        print("调试信息:")
        print("=" * 50)
        
        # 调试示例
        s1 = "ababa"
        p1 = debug_manacher(s1)
        analyze_palindromes(s1, p1)
        
        print("\n" + "=" * 50)
        s2 = "aaa"
        p2 = debug_manacher(s2)
        analyze_palindromes(s2, p2)
        
    else:
        # 正常模式
        data = sys.stdin.read().split()
        if len(data) < 2:
            print(-1)
            return
            
        n = int(data[0])
        k = int(data[1])
        s = data[2] if len(data) > 2 else ""
        
        result = compute_product(s, k)
        print(result)


if __name__ == "__main__":
    main()


"""
算法正确性验证:

对于字符串"ababa":
- 奇数长度回文子串:
  - 长度5: "ababa" (1个)
  - 长度3: "aba", "bab", "aba" (3个) 
  - 长度1: "a", "b", "a", "b", "a" (5个)
- 按长度排序: 5, 3, 1
- 前3个: 取长度5的1个，长度3的2个
- 乘积: 5 * 3 * 3 = 45

对于字符串"aaa":
- 奇数长度回文子串:
  - 长度3: "aaa" (1个)
  - 长度1: "a", "a", "a" (3个)
- 前2个: 取长度3的1个，长度1的1个  
- 乘积: 3 * 1 = 3

注意: 实际计算时需要考虑每个长度的所有出现次数
"""