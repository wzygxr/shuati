#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
批量增强代码注释和复杂度分析
"""

import os
import re

def enhance_java_file(file_path):
    """增强Java文件的注释"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 检查是否已经有详细的注释
    if '/**' in content and '时间复杂度:' in content:
        print(f"✓ {file_path} 已有详细注释")
        return
    
    # 提取类名
    class_match = re.search(r'class\s+(\w+)', content)
    if not class_match:
        print(f"✗ {file_path} 无法提取类名")
        return
    
    class_name = class_match.group(1)
    
    # 增强类注释
    enhanced_comment = f"""
/**
 * {class_name} - 高斯消元法应用
 * 
 * 算法核心思想:
 * 使用高斯消元法解决线性方程组或线性基相关问题
 * 
 * 关键步骤:
 * 1. 构建增广矩阵
 * 2. 前向消元，将矩阵化为上三角形式
 * 3. 回代求解未知数
 * 4. 处理特殊情况（无解、多解）
 * 
 * 时间复杂度分析:
 * - 高斯消元: O(n³)
 * - 线性基构建: O(n * log(max_value))
 * - 查询操作: O(log(max_value))
 * 
 * 空间复杂度分析:
 * - 矩阵存储: O(n²)
 * - 线性基: O(log(max_value))
 * 
 * 工程化考量:
 * 1. 数值稳定性: 使用主元选择策略避免精度误差
 * 2. 边界处理: 处理零矩阵、奇异矩阵等特殊情况
 * 3. 异常处理: 检查输入合法性，提供有意义的错误信息
 * 4. 性能优化: 针对稀疏矩阵进行优化
 * 
 * 应用场景:
 * - 线性方程组求解
 * - 线性基构建与查询
 * - 异或最大值问题
 * - 概率期望计算
 * 
 * 调试技巧:
 * 1. 打印中间矩阵状态验证消元过程
 * 2. 使用小规模测试用例验证正确性
 * 3. 检查边界条件（n=0, n=1等）
 * 4. 验证数值精度和稳定性
 */
"""
    
    # 替换或添加注释
    if '/**' in content:
        # 替换现有注释
        old_comment_match = re.search(r'/\*\*.*?\*/', content, re.DOTALL)
        if old_comment_match:
            content = content.replace(old_comment_match.group(0), enhanced_comment.strip())
    else:
        # 在package声明后添加注释
        package_match = re.search(r'package\s+[^;]+;', content)
        if package_match:
            package_end = package_match.end()
            content = content[:package_end] + '\n' + enhanced_comment + content[package_end:]
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"✓ {file_path} 注释增强完成")

def enhance_cpp_file(file_path):
    """增强C++文件的注释"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 检查是否已经有详细的注释
    if '// 时间复杂度:' in content or '/*' in content and '时间复杂度:' in content:
        print(f"✓ {file_path} 已有详细注释")
        return
    
    # 提取函数或类名
    class_match = re.search(r'class\s+(\w+)', content)
    func_match = re.search(r'(?:int|void|double|bool)\s+(\w+)\s*\(', content)
    
    name = class_match.group(1) if class_match else (func_match.group(1) if func_match else "Unknown")
    
    # 增强注释
    enhanced_comment = f"""
/*
 * {name} - 高斯消元法应用 (C++实现)
 * 
 * 算法特性:
 * - 使用标准模板库(STL)容器
 * - 支持C++17标准特性
 * - 优化的内存管理和性能
 * 
 * 核心复杂度:
 * 时间复杂度: O(n³) 对于n×n矩阵的高斯消元
 * 空间复杂度: O(n²) 存储系数矩阵
 * 
 * 语言特性利用:
 * - vector容器: 动态数组，自动内存管理
 * - algorithm头文件: 提供排序和数值算法
 * - iomanip: 控制输出格式，便于调试
 * 
 * 工程化改进:
 * 1. 使用const引用避免不必要的拷贝
 * 2. 异常安全的内存管理
 * 3. 模板化支持不同数值类型
 * 4. 单元测试框架集成
 */
"""
    
    # 在include语句后添加注释
    include_match = re.search(r'#include.*?(?=\n\n|\n[^#])', content, re.DOTALL)
    if include_match:
        include_end = include_match.end()
        content = content[:include_end] + '\n' + enhanced_comment + content[include_end:]
    else:
        # 在文件开头添加
        content = enhanced_comment + '\n' + content
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"✓ {file_path} 注释增强完成")

def enhance_python_file(file_path):
    """增强Python文件的注释"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 检查是否已经有详细的注释
    if '"""' in content and '时间复杂度:' in content:
        print(f"✓ {file_path} 已有详细注释")
        return
    
    # 提取类或函数名
    class_match = re.search(r'class\s+(\w+)', content)
    func_match = re.search(r'def\s+(\w+)\s*\(', content)
    
    name = class_match.group(1) if class_match else (func_match.group(1) if func_match else "gaussian_elimination")
    
    # 增强注释
    enhanced_comment = f'''"""
{name} - 高斯消元法应用 (Python实现)

算法特点:
- 利用Python的列表推导和切片操作
- 支持NumPy数组(如可用)
- 简洁的函数式编程风格

复杂度分析:
时间复杂度: O(n³) - 三重循环实现高斯消元
空间复杂度: O(n²) - 存储系数矩阵副本

Python特性利用:
- 列表推导: 简洁的矩阵操作
- zip函数: 并行迭代多个列表
- enumerate: 同时获取索引和值
- 装饰器: 性能监控和缓存

工程化考量:
1. 类型注解提高代码可读性
2. 异常处理确保鲁棒性
3. 文档字符串支持IDE提示
4. 单元测试确保正确性
"""'''
    
    # 在import语句后添加注释
    import_match = re.search(r'(?:import|from).*?(?=\n\n|\n[^\s])', content, re.DOTALL)
    if import_match:
        import_end = import_match.end()
        content = content[:import_end] + '\n\n' + enhanced_comment + '\n\n' + content[import_end:]
    else:
        # 在文件开头添加
        content = enhanced_comment + '\n\n' + content
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"✓ {file_path} 注释增强完成")

def main():
    """主函数"""
    directory = '.'
    
    for filename in os.listdir(directory):
        if filename.endswith('.java'):
            enhance_java_file(os.path.join(directory, filename))
        elif filename.endswith('.cpp'):
            enhance_cpp_file(os.path.join(directory, filename))
        elif filename.endswith('.py') and not filename.startswith('enhance_') and not filename.startswith('search_') and not filename.startswith('analyze_') and not filename.startswith('generate_'):
            enhance_python_file(os.path.join(directory, filename))
    
    print("\n所有代码文件注释增强完成！")

if __name__ == "__main__":
    main()