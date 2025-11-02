"""
测试所有二分查找相关题目实现
"""

import subprocess
import sys
import os

def test_java_file(filename):
    """测试Java文件"""
    print(f"测试Java文件: {filename}")
    try:
        # 编译Java文件
        compile_result = subprocess.run(
            ["javac", filename], 
            capture_output=True, 
            text=True, 
            timeout=30,
            cwd="."
        )
        
        if compile_result.returncode != 0:
            print(f"编译失败: {compile_result.stderr}")
            return False
            
        # 运行Java文件
        class_name = filename.replace(".java", "")
        run_result = subprocess.run(
            ["java", "-cp", ".", class_name], 
            capture_output=True, 
            text=True, 
            timeout=30,
            cwd="."
        )
        
        print(f"STDOUT:\n{run_result.stdout}")
        if run_result.stderr:
            print(f"STDERR:\n{run_result.stderr}")
            
        return run_result.returncode == 0
    except subprocess.TimeoutExpired:
        print(f"{filename} 运行超时")
        return False
    except Exception as e:
        print(f"运行 {filename} 时出错: {e}")
        return False

def test_python_file(filename):
    """测试Python文件"""
    print(f"测试Python文件: {filename}")
    try:
        result = subprocess.run(
            [sys.executable, filename], 
            capture_output=True, 
            text=True, 
            timeout=30,
            cwd="."
        )
        print(f"STDOUT:\n{result.stdout}")
        if result.stderr:
            print(f"STDERR:\n{result.stderr}")
        return result.returncode == 0
    except subprocess.TimeoutExpired:
        print(f"{filename} 运行超时")
        return False
    except Exception as e:
        print(f"运行 {filename} 时出错: {e}")
        return False

def test_cpp_file(source_file, exe_file):
    """测试C++文件"""
    print(f"测试C++文件: {source_file}")
    try:
        # 编译C++文件
        compile_result = subprocess.run(
            ["g++", "-o", exe_file, source_file], 
            capture_output=True, 
            text=True, 
            timeout=30,
            cwd="."
        )
        
        if compile_result.returncode != 0:
            print(f"编译失败: {compile_result.stderr}")
            return False
            
        # 运行可执行文件
        run_result = subprocess.run(
            [f".\\{exe_file}"], 
            capture_output=True, 
            text=True, 
            timeout=30,
            cwd="."
        )
        
        print(f"STDOUT:\n{run_result.stdout}")
        if run_result.stderr:
            print(f"STDERR:\n{run_result.stderr}")
            
        return run_result.returncode == 0
    except subprocess.TimeoutExpired:
        print(f"{source_file} 运行超时")
        return False
    except Exception as e:
        print(f"运行 {source_file} 时出错: {e}")
        return False

def main():
    """主测试函数"""
    print("开始测试所有二分查找相关题目实现")
    print("=" * 50)
    
    # 定义要测试的文件列表
    test_files = [
        # LeetCode 704
        {
            "name": "LeetCode 704. 二分查找",
            "java": "LeetCode704_BinarySearch.java",
            "cpp": "LeetCode704_BinarySearch.cpp",
            "cpp_exe": "LeetCode704_BinarySearch.exe",
            "python": "LeetCode704_BinarySearch.py"
        },
        # LeetCode 35
        {
            "name": "LeetCode 35. 搜索插入位置",
            "java": "LeetCode35_SearchInsertPosition.java",
            "cpp": "LeetCode35_SearchInsertPosition.cpp",
            "cpp_exe": "LeetCode35_SearchInsertPosition.exe",
            "python": "LeetCode35_SearchInsertPosition.py"
        },
        # LeetCode 34
        {
            "name": "LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置",
            "java": "LeetCode34_FindFirstAndLastPosition.java",
            "cpp": "LeetCode34_FindFirstAndLastPosition.cpp",
            "cpp_exe": "LeetCode34_FindFirstAndLastPosition.exe",
            "python": "LeetCode34_FindFirstAndLastPosition.py"
        },
        # LeetCode 153
        {
            "name": "LeetCode 153. 寻找旋转排序数组中的最小值",
            "java": "LeetCode153_FindMinimumInRotatedSortedArray.java",
            "cpp": "LeetCode153_FindMinimumInRotatedSortedArray.cpp",
            "cpp_exe": "LeetCode153_FindMinimumInRotatedSortedArray.exe",
            "python": "LeetCode153_FindMinimumInRotatedSortedArray.py"
        }
    ]
    
    # 测试每个题目
    for problem in test_files:
        print(f"\n测试题目: {problem['name']}")
        print("-" * 30)
        
        # 测试Java实现
        if os.path.exists(problem["java"]):
            test_java_file(problem["java"])
        else:
            print(f"Java文件不存在: {problem['java']}")
            
        # 测试Python实现
        if os.path.exists(problem["python"]):
            test_python_file(problem["python"])
        else:
            print(f"Python文件不存在: {problem['python']}")
            
        # 测试C++实现
        if os.path.exists(problem["cpp"]):
            test_cpp_file(problem["cpp"], problem["cpp_exe"])
        else:
            print(f"C++文件不存在: {problem['cpp']}")
            
        print("-" * 50)
    
    print("\n所有测试完成！")

if __name__ == "__main__":
    main()