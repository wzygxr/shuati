"""
测试所有扩展题目的正确性
"""

# 导入所有实现的函数
from LeetCode_322_CoinChange import coinChange
from LeetCode_292_NimGame import canWinNim
from LeetCode_829_ConsecutiveNumbersSum import consecutiveNumbersSum
from LeetCode_5_LongestPalindromicSubstring import longestPalindrome

def test_all_problems():
    """测试所有扩展题目"""
    print("=== 测试所有扩展题目 ===")
    
    # 测试LeetCode 322. Coin Change
    print("\n1. 测试LeetCode 322. Coin Change:")
    coins1 = [1, 3, 4]
    amount1 = 6
    result1 = coinChange(coins1, amount1)
    print(f"   coins = {coins1}, amount = {amount1}, result = {result1}")
    
    coins2 = [2]
    amount2 = 3
    result2 = coinChange(coins2, amount2)
    print(f"   coins = {coins2}, amount = {amount2}, result = {result2}")
    
    # 测试LeetCode 292. Nim Game
    print("\n2. 测试LeetCode 292. Nim Game:")
    n1 = 4
    result3 = canWinNim(n1)
    print(f"   n = {n1}, result = {result3}")
    
    n2 = 5
    result4 = canWinNim(n2)
    print(f"   n = {n2}, result = {result4}")
    
    # 测试LeetCode 829. Consecutive Numbers Sum
    print("\n3. 测试LeetCode 829. Consecutive Numbers Sum:")
    N1 = 15
    result5 = consecutiveNumbersSum(N1)
    print(f"   N = {N1}, result = {result5}")
    
    N2 = 9
    result6 = consecutiveNumbersSum(N2)
    print(f"   N = {N2}, result = {result6}")
    
    # 测试LeetCode 5. Longest Palindromic Substring
    print("\n4. 测试LeetCode 5. Longest Palindromic Substring:")
    s1 = "babad"
    result7 = longestPalindrome(s1)
    print(f"   s = '{s1}', result = '{result7}'")
    
    s2 = "cbbd"
    result8 = longestPalindrome(s2)
    print(f"   s = '{s2}', result = '{result8}'")
    
    print("\n=== 测试完成 ===")

# 运行测试
if __name__ == "__main__":
    test_all_problems()