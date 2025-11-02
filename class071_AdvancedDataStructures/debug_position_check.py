#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
检查位置5是否真的匹配
"""

text5 = "GCATCGCAGAGAGTATACAGTACG"
pattern5 = "GCAGAGAG"

print(f"文本串: {text5}")
print(f"模式串: {pattern5}")

print("\n逐字符比较位置5开始的匹配:")
for i in range(len(pattern5)):
    text_char = text5[5 + i]
    pattern_char = pattern5[i]
    match = text_char == pattern_char
    print(f"  位置{5+i}: 文本'{text_char}' vs 模式'{pattern_char}' -> {'匹配' if match else '不匹配'}")

print("\n完整字符串比较:")
print(f"文本串位置5开始的子串: '{text5[5:5+len(pattern5)]}'")
print(f"模式串: '{pattern5}'")
print(f"是否匹配: {text5[5:5+len(pattern5)] == pattern5}")