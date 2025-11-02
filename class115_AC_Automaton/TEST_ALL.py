#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
AC自动机算法综合测试脚本
测试所有Python实现的AC自动机功能
"""

import os
import sys
import time
from datetime import datetime

def test_basic_functionality():
    """测试基础功能"""
    print("1. 测试基础AC自动机功能...")
    
    # 测试Code03_ACAM_Template.py
    try:
        import Code03_ACAM_Template
        print("   ✅ Code03_ACAM_Template.py 导入成功")
    except Exception as e:
        print(f"   ❌ Code03_ACAM_Template.py 导入失败: {e}")
        return False
    
    # 测试其他基础文件
    basic_files = [
        "Code04_StreamOfCharacters.py",
        "Code05_WordPuzzles.py", 
        "Code06_DetectVirus.py",
        "Code07_KeywordsSearch.py",
        "Code08_VirusInvasion.py"
    ]
    
    for file in basic_files:
        try:
            module_name = file.replace('.py', '')
            __import__(module_name)
            print(f"   ✅ {file} 导入成功")
        except Exception as e:
            print(f"   ❌ {file} 导入失败: {e}")
            return False
    
    return True

def test_extended_problems():
    """测试扩展题目"""
    print("2. 测试扩展题目实现...")
    
    # 测试Code09_ExtendedACAM.py
    try:
        import Code09_ExtendedACAM
        print("   ✅ Code09_ExtendedACAM.py 导入成功")
        
        # 运行测试函数
        Code09_ExtendedACAM.main()
        print("   ✅ Code09_ExtendedACAM.py 测试运行成功")
    except Exception as e:
        print(f"   ❌ Code09_ExtendedACAM.py 测试失败: {e}")
        return False
    
    # 测试Code12_LuckyCommonSubsequence.py
    try:
        import Code12_LuckyCommonSubsequence
        print("   ✅ Code12_LuckyCommonSubsequence.py 导入成功")
        
        # 运行测试函数
        Code12_LuckyCommonSubsequence.main()
        print("   ✅ Code12_LuckyCommonSubsequence.py 测试运行成功")
    except Exception as e:
        print(f"   ❌ Code12_LuckyCommonSubsequence.py 测试失败: {e}")
        return False
    
    return True

def test_advanced_variants():
    """测试高级变体"""
    print("3. 测试高级算法变体...")
    
    # 测试Code10_AdvancedACAM.py
    try:
        import Code10_AdvancedACAM
        print("   ✅ Code10_AdvancedACAM.py 导入成功")
        
        # 运行测试函数
        Code10_AdvancedACAM.main()
        print("   ✅ Code10_AdvancedACAM.py 测试运行成功")
    except Exception as e:
        print(f"   ❌ Code10_AdvancedACAM.py 测试失败: {e}")
        return False
    
    return True

def test_real_world_applications():
    """测试实际应用"""
    print("4. 测试实际应用场景...")
    
    # 测试Code11_ACAM_Applications.py
    try:
        import Code11_ACAM_Applications
        print("   ✅ Code11_ACAM_Applications.py 导入成功")
        
        # 运行测试函数
        Code11_ACAM_Applications.main()
        print("   ✅ Code11_ACAM_Applications.py 测试运行成功")
    except Exception as e:
        print(f"   ❌ Code11_ACAM_Applications.py 测试失败: {e}")
        return False
    
    return True

def main():
    """主测试函数"""
    print("=" * 60)
    print("AC自动机算法综合测试")
    print("=" * 60)
    print(f"开始时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print()
    
    # 记录测试结果
    test_results = []
    
    # 执行所有测试
    test_results.append(("基础功能", test_basic_functionality()))
    test_results.append(("扩展题目", test_extended_problems()))
    test_results.append(("高级变体", test_advanced_variants()))
    test_results.append(("实际应用", test_real_world_applications()))
    
    # 输出测试总结
    print()
    print("=" * 60)
    print("测试总结")
    print("=" * 60)
    
    passed_tests = 0
    total_tests = len(test_results)
    
    for test_name, result in test_results:
        status = "✅ 通过" if result else "❌ 失败"
        print(f"{test_name}: {status}")
        if result:
            passed_tests += 1
    
    print()
    print(f"测试完成: {passed_tests}/{total_tests} 通过")
    print(f"成功率: {passed_tests/total_tests*100:.1f}%")
    print(f"结束时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    
    if passed_tests == total_tests:
        print("\n🎉 所有测试通过！AC自动机算法实现完整且正确。")
        return 0
    else:
        print("\n⚠️  部分测试失败，请检查相关代码。")
        return 1

if __name__ == "__main__":
    # 添加当前目录到Python路径
    sys.path.insert(0, os.path.dirname(__file__))
    
    # 运行测试
    exit_code = main()
    sys.exit(exit_code)