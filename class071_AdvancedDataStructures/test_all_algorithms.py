#!/usr/bin/env python3
"""
综合测试脚本 - 测试所有高级数据结构和算法
"""

import subprocess
import os
import sys

def run_cpp_test(algorithm_name):
    """运行C++版本的测试"""
    try:
        # 编译C++程序
        compile_cmd = f"g++ -std=c++11 {algorithm_name}.cpp -o {algorithm_name}_test"
        result = subprocess.run(compile_cmd, shell=True, capture_output=True, text=True)
        
        if result.returncode != 0:
            print(f"❌ {algorithm_name} C++编译失败:")
            print(result.stderr)
            return False
        
        # 运行C++程序
        if os.name == 'nt':
            run_cmd = f"{algorithm_name}_test.exe"
        else:
            run_cmd = f"./{algorithm_name}_test"
        result = subprocess.run(run_cmd, shell=True, capture_output=True, text=True)
        
        if result.returncode != 0:
            print(f"❌ {algorithm_name} C++运行失败:")
            print(result.stderr)
            return False
        
        print(f"✅ {algorithm_name} C++测试通过")
        return True
        
    except Exception as e:
        print(f"❌ {algorithm_name} C++测试异常: {e}")
        return False

def run_python_test(algorithm_name):
    """运行Python版本的测试"""
    try:
        # 运行Python程序
        run_cmd = f"python {algorithm_name}.py"
        result = subprocess.run(run_cmd, shell=True, capture_output=True, text=True)
        
        if result.returncode != 0:
            print(f"❌ {algorithm_name} Python运行失败:")
            print(result.stderr)
            return False
        
        print(f"✅ {algorithm_name} Python测试通过")
        return True
        
    except Exception as e:
        print(f"❌ {algorithm_name} Python测试异常: {e}")
        return False

def run_java_test(algorithm_name):
    """运行Java版本的测试"""
    try:
        # 编译Java程序
        compile_cmd = f"javac {algorithm_name}.java"
        result = subprocess.run(compile_cmd, shell=True, capture_output=True, text=True)
        
        if result.returncode != 0:
            print(f"❌ {algorithm_name} Java编译失败:")
            print(result.stderr)
            return False
        
        # 运行Java程序
        run_cmd = f"java {algorithm_name}"
        result = subprocess.run(run_cmd, shell=True, capture_output=True, text=True)
        
        if result.returncode != 0:
            print(f"❌ {algorithm_name} Java运行失败:")
            print(result.stderr)
            return False
        
        print(f"✅ {algorithm_name} Java测试通过")
        return True
        
    except Exception as e:
        print(f"❌ {algorithm_name} Java测试异常: {e}")
        return False

def test_algorithm(algorithm_name):
    """测试单个算法的所有语言实现"""
    print(f"\n=== 测试{algorithm_name}算法 ===")
    
    results = []
    
    # 检查文件是否存在
    cpp_file = f"{algorithm_name}.cpp"
    python_file = f"{algorithm_name}.py"
    java_file = f"{algorithm_name}.java"
    
    if os.path.exists(cpp_file):
        results.append(("C++", run_cpp_test(algorithm_name)))
    else:
        print(f"⚠️  {algorithm_name} C++文件不存在")
    
    if os.path.exists(python_file):
        results.append(("Python", run_python_test(algorithm_name)))
    else:
        print(f"⚠️  {algorithm_name} Python文件不存在")
    
    if os.path.exists(java_file):
        results.append(("Java", run_java_test(algorithm_name)))
    else:
        print(f"⚠️  {algorithm_name} Java文件不存在")
    
    return results

def main():
    """主函数"""
    print("=== Class029 高级数据结构扩展项目综合测试 ===")
    print("开始测试所有算法的实现...")
    
    # 要测试的算法列表
    algorithms = [
        "boyer_moore_algorithm",
        "fhq_treap_algorithm", 
        "kd_tree",
        "link_cut_tree",
        "palindromic_automaton"
    ]
    
    # 切换到正确的目录
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    
    total_tests = 0
    passed_tests = 0
    
    # 测试每个算法
    for algorithm in algorithms:
        results = test_algorithm(algorithm)
        
        for language, passed in results:
            total_tests += 1
            if passed:
                passed_tests += 1
    
    # 输出测试总结
    print("\n" + "="*60)
    print("测试总结:")
    print(f"总测试数: {total_tests}")
    print(f"通过数: {passed_tests}")
    print(f"失败数: {total_tests - passed_tests}")
    
    if passed_tests == total_tests:
        print("🎉 所有测试通过！项目实现完整且正确。")
        return 0
    else:
        print("⚠️  部分测试失败，请检查相关实现。")
        return 1

if __name__ == "__main__":
    sys.exit(main())