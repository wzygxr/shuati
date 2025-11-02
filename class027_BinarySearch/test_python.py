"""
测试Python代码
"""

import subprocess
import sys

def test_python_files():
    """测试所有Python文件"""
    python_files = [
        "Code01_BinarySearch.py",
        "Code02_InteractiveBinarySearch.py",
    ]
    
    for file in python_files:
        print(f"测试 {file}...")
        try:
            # 运行Python文件，设置超时时间
            result = subprocess.run(
                [sys.executable, file], 
                capture_output=True, 
                text=True, 
                timeout=10,
                cwd="."
            )
            print(f"STDOUT:\n{result.stdout}")
            if result.stderr:
                print(f"STDERR:\n{result.stderr}")
            print(f"返回码: {result.returncode}")
        except subprocess.TimeoutExpired:
            print(f"{file} 运行超时")
        except Exception as e:
            print(f"运行 {file} 时出错: {e}")
        print("-" * 50)

if __name__ == "__main__":
    test_python_files()