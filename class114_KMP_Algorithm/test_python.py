#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
KMP算法题目Python文件测试脚本
"""

import os
import sys
import subprocess
import time

def test_python_files():
    """测试所有Python文件是否能正常运行"""
    
    print("========================================")
    print("     KMP算法题目Python文件测试")
    print("========================================")
    
    # 获取当前目录
    current_dir = os.path.dirname(os.path.abspath(__file__))
    print(f"当前目录: {current_dir}")
    
    # 要测试的Python文件列表
    python_files = [
        "Code01_RepeatMinimumLength.py",
        "Code02_DeleteAgainAndAgain.py",
        "Code03_LinkedListInBinaryTree.py",
        "Code04_FindAllGoodStrings.py",
        "Code05_Period.py",
        "Code06_NeedleInHaystack.py",
        "Code07_PeriodsOfWords.py",
        "Code08_LongestHappyPrefix.py",
        "Code09_LeetCode28_StrStr.py",
        "Code10_Codeforces126B_Password.py",
        "Code11_POJ2752_SeekName.py",
        "Code12_HDU2594_SimpsonsTalents.py",
    ]
    
    # 测试计数器
    passed_count = 0
    total_count = len(python_files)
    
    print("开始测试...")
    print("========================================")
    
    # 测试每个Python文件
    for file in python_files:
        file_path = os.path.join(current_dir, file)
        if os.path.exists(file_path):
            print(f"正在测试: {file}")
            try:
                # 运行Python文件，设置超时时间
                result = subprocess.run([
                    sys.executable, file_path
                ], capture_output=True, text=True, timeout=30)
                
                if result.returncode == 0:
                    print(f"✅ 测试成功: {file}")
                    passed_count += 1
                else:
                    print(f"❌ 测试失败: {file}")
                    print(f"   错误输出: {result.stderr}")
            except subprocess.TimeoutExpired:
                print(f"⏰ 测试超时: {file}")
            except Exception as e:
                print(f"❌ 测试出错: {file}")
                print(f"   错误信息: {e}")
            
            print("----------------------------------------")
        else:
            print(f"⚠ 文件不存在: {file}")
            print("----------------------------------------")
    
    # 输出测试结果
    print("========================================")
    print(f"测试完成: {passed_count}/{total_count} 个文件测试成功")
    
    if passed_count == total_count:
        print("🎉 所有Python文件测试成功!")
    else:
        print("⚠ 部分文件测试失败，请检查错误信息")
    
    print("========================================")

if __name__ == "__main__":
    test_python_files()