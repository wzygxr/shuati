#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
分析缺失的高斯消元法题目实现
"""

import os
import json

def analyze_missing_implementations():
    """分析缺失的实现"""
    
    # 从README中提取的题目列表
    problems_from_readme = [
        # 基础题目
        {"code": "Code01", "title": "HDU 5755 Gambler Bo", "platform": "HDU"},
        {"code": "Code02", "title": "POJ 2947 Widget Factory", "platform": "POJ"},
        {"code": "Code03", "title": "POJ 1222 EXTENDED LIGHTS OUT", "platform": "POJ"},
        {"code": "Code04", "title": "HDU 3976 Electric resistance", "platform": "HDU"},
        
        # 进阶题目
        {"code": "Code05", "title": "POJ 1681 Painter's Problem", "platform": "POJ"},
        {"code": "Code06", "title": "POJ 1830 开关问题", "platform": "POJ"},
        {"code": "Code07", "title": "SGU 275 To xor or not to xor", "platform": "SGU"},
        {"code": "Code08", "title": "Codeforces 24D Broken robot", "platform": "Codeforces"},
        {"code": "Code09", "title": "Codeforces 963E Circles of Waiting", "platform": "Codeforces"},
        
        # 补充题目
        {"code": "Code10", "title": "LeetCode 887. 鸡蛋掉落", "platform": "LeetCode"},
        {"code": "Code11", "title": "洛谷 P2455 [SDOI2006]线性方程组", "platform": "洛谷"},
        {"code": "Code12", "title": "AcWing 203. 同余方程", "platform": "AcWing"},
        {"code": "Code13", "title": "牛客 NC14255 线性方程组", "platform": "牛客"},
        {"code": "Code14", "title": "POJ 2065 SETI", "platform": "POJ"},
        
        # 更多补充题目（需要创建新的代码编号）
        {"code": "Code15", "title": "AtCoder ABC145 E - All-you-can-eat", "platform": "AtCoder"},
        {"code": "Code16", "title": "CodeChef MODULARITY", "platform": "CodeChef"},
        {"code": "Code17", "title": "计蒜客 T1214 同余方程", "platform": "计蒜客"},
        {"code": "Code18", "title": "USACO 2019 February Contest, Gold Problem 3. Mowing Moocows", "platform": "USACO"},
        {"code": "Code19", "title": "POJ 3167 Cow Patterns", "platform": "POJ"},
        {"code": "Code20", "title": "ZOJ 3644 Kitty's Game", "platform": "ZOJ"},
        {"code": "Code21", "title": "LeetCode 1820. 最多邀请的个数", "platform": "LeetCode"},
        {"code": "Code22", "title": "AtCoder ABC141 F - Xor Sum 3", "platform": "AtCoder"},
        {"code": "Code23", "title": "Codeforces 1100F - Ivan and Burgers", "platform": "Codeforces"},
        {"code": "Code24", "title": "UVa 12113 Overlapping Squares", "platform": "UVa OJ"},
        {"code": "Code25", "title": "牛客 NC19740 异或", "platform": "牛客"},
        {"code": "Code26", "title": "SPOJ XOR", "platform": "SPOJ"},
        {"code": "Code27", "title": "POJ 3276 Face The Right Way", "platform": "POJ"},
        {"code": "Code28", "title": "UVa 10109 Solving Systems of Linear Equations", "platform": "UVa OJ"},
        {"code": "Code29", "title": "LeetCode 837. 新21点", "platform": "LeetCode"},
        {"code": "Code30", "title": "Codeforces 590D. Top Secret Task", "platform": "Codeforces"},
        {"code": "Code31", "title": "HDU 4035 Maze", "platform": "HDU"},
        {"code": "Code32", "title": "POJ 3146 Interesting Yang Hui Triangle", "platform": "POJ"},
        {"code": "Code33", "title": "牛客 NC15139 逃离僵尸岛", "platform": "牛客"},
        {"code": "Code34", "title": "HDU 4418 Time travel", "platform": "HDU"},
        {"code": "Code35", "title": "Codeforces 113D Metro", "platform": "Codeforces"},
        {"code": "Code36", "title": "LeetCode 1707. 与数组中元素的最大异或值", "platform": "LeetCode"},
        {"code": "Code37", "title": "Codeforces 1100F - Ivan and Burgers", "platform": "Codeforces"},
        {"code": "Code38", "title": "AtCoder ARC084 D - Small Multiple", "platform": "AtCoder"},
        {"code": "Code39", "title": "洛谷 P3857 [TJOI2008]彩灯", "platform": "洛谷"},
        {"code": "Code40", "title": "SPOJ SUBXOR", "platform": "SPOJ"},
        {"code": "Code41", "title": "洛谷 P3812 【模板】线性基", "platform": "洛谷"},
        {"code": "Code42", "title": "洛谷 P4151 [WC2011]最大XOR和路径", "platform": "洛谷"},
        {"code": "Code43", "title": "HDU 3949 XOR", "platform": "HDU"}
    ]
    
    # 获取当前目录中的所有文件
    current_files = os.listdir('.')
    
    # 分析每个题目的实现情况
    missing_implementations = []
    existing_implementations = []
    
    for problem in problems_from_readme:
        code_prefix = problem['code']
        
        # 检查Java实现
        java_file = f"{code_prefix}_{problem['platform'].replace(' ', '_')}.java"
        java_exists = java_file in current_files
        
        # 检查C++实现
        cpp_file = f"{code_prefix}_{problem['platform'].replace(' ', '_')}.cpp"
        cpp_exists = cpp_file in current_files
        
        # 检查Python实现
        py_file = f"{code_prefix}_{problem['platform'].replace(' ', '_')}.py"
        py_exists = py_file in current_files
        
        # 检查简化的文件名（不带平台信息）
        simple_java = f"{code_prefix}_{problem['title'].split()[0]}.java"
        simple_cpp = f"{code_prefix}_{problem['title'].split()[0]}.cpp"
        simple_py = f"{code_prefix}_{problem['title'].split()[0]}.py"
        
        # 检查简化的文件名是否存在
        if not java_exists:
            java_exists = simple_java in current_files
        if not cpp_exists:
            cpp_exists = simple_cpp in current_files
        if not py_exists:
            py_exists = simple_py in current_files
        
        # 检查更简化的文件名（只有代码前缀）
        prefix_java = f"{code_prefix}_*.java"
        prefix_cpp = f"{code_prefix}_*.cpp"
        prefix_py = f"{code_prefix}_*.py"
        
        # 检查是否有以代码前缀开头的文件
        for file in current_files:
            if file.startswith(code_prefix + '_') and file.endswith('.java'):
                java_exists = True
            if file.startswith(code_prefix + '_') and file.endswith('.cpp'):
                cpp_exists = True
            if file.startswith(code_prefix + '_') and file.endswith('.py'):
                py_exists = True
        
        implementation_status = {
            'problem': problem,
            'java': java_exists,
            'cpp': cpp_exists,
            'python': py_exists
        }
        
        if not (java_exists and cpp_exists and py_exists):
            missing_implementations.append(implementation_status)
        else:
            existing_implementations.append(implementation_status)
    
    # 生成报告
    print("=== 高斯消元法题目实现分析报告 ===\n")
    
    print(f"总题目数: {len(problems_from_readme)}")
    print(f"完整实现数: {len(existing_implementations)}")
    print(f"缺失实现数: {len(missing_implementations)}")
    
    print("\n=== 缺失实现详情 ===")
    for impl in missing_implementations:
        problem = impl['problem']
        missing_langs = []
        if not impl['java']:
            missing_langs.append("Java")
        if not impl['cpp']:
            missing_langs.append("C++")
        if not impl['python']:
            missing_langs.append("Python")
        
        print(f"\n{problem['code']}: {problem['title']}")
        print(f"  平台: {problem['platform']}")
        print(f"  缺失语言: {', '.join(missing_langs)}")
    
    print("\n=== 完整实现详情 ===")
    for impl in existing_implementations:
        problem = impl['problem']
        print(f"{problem['code']}: {problem['title']} - 完整实现")
    
    # 保存分析结果
    analysis_result = {
        'total_problems': len(problems_from_readme),
        'complete_implementations': len(existing_implementations),
        'missing_implementations': len(missing_implementations),
        'missing_details': missing_implementations,
        'complete_details': existing_implementations
    }
    
    with open('implementation_analysis.json', 'w', encoding='utf-8') as f:
        json.dump(analysis_result, f, ensure_ascii=False, indent=2)
    
    print(f"\n分析结果已保存到 implementation_analysis.json")
    
    return missing_implementations

def main():
    """主函数"""
    missing_impls = analyze_missing_implementations()
    
    # 生成需要补充的实现列表
    if missing_impls:
        print("\n=== 需要补充的实现 ===")
        
        for impl in missing_impls:
            problem = impl['problem']
            
            if not impl['java']:
                print(f"需要补充Java实现: {problem['code']}_{problem['platform'].replace(' ', '_')}.java")
            if not impl['cpp']:
                print(f"需要补充C++实现: {problem['code']}_{problem['platform'].replace(' ', '_')}.cpp")
            if not impl['python']:
                print(f"需要补充Python实现: {problem['code']}_{problem['platform'].replace(' ', '_')}.py")

if __name__ == "__main__":
    main()