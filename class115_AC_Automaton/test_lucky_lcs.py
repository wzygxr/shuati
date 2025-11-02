# -*- coding: utf-8 -*-

"""
简化测试脚本用于验证Code12_LuckyCommonSubsequence.py的功能
"""

from Code12_LuckyCommonSubsequence import LuckyCommonSubsequence

def test_lucky_common_subsequence():
    """测试Lucky Common Subsequence功能"""
    print("=== 测试Codeforces 346B - Lucky Common Subsequence ===")
    
    # 创建实例
    lcs_solver = LuckyCommonSubsequence()
    
    # 测试用例1
    str1 = "abcdef"
    str2 = "abcxyz"
    virus = "xyz"
    print(f"测试用例1:")
    print(f"  str1: {str1}")
    print(f"  str2: {str2}")
    print(f"  virus: {virus}")
    
    result = lcs_solver.longest_common_subsequence_without_virus(str1, str2, virus)
    print(f"  结果: {result}")
    print()
    
    # 测试用例2
    str1 = "abc"
    str2 = "acb"
    virus = "b"
    print(f"测试用例2:")
    print(f"  str1: {str1}")
    print(f"  str2: {str2}")
    print(f"  virus: {virus}")
    
    result = lcs_solver.longest_common_subsequence_without_virus(str1, str2, virus)
    print(f"  结果: {result}")
    print()
    
    print("✅ 测试完成!")

if __name__ == "__main__":
    test_lucky_common_subsequence()