import os
import sys
import subprocess

def check_python_syntax(file_path):
    """检查Python文件语法"""
    try:
        # 使用Python -m py_compile检查语法
        result = subprocess.run([sys.executable, '-m', 'py_compile', file_path], 
                              capture_output=True, text=True)
        if result.returncode == 0:
            print(f"✓ {file_path} 语法正确")
            return True
        else:
            print(f"✗ {file_path} 语法错误:")
            print(result.stderr)
            return False
    except Exception as e:
        print(f"✗ 检查 {file_path} 时出错: {e}")
        return False

def main():
    # 获取当前目录
    current_dir = "d:\\Upan\\src\\algorithm-journey\\src\\algorithm-journey\\src\\class168"
    
    # 查找所有Python文件
    python_files = []
    for file in os.listdir(current_dir):
        if file.endswith('.py'):
            python_files.append(os.path.join(current_dir, file))
    
    # 检查每个Python文件
    all_correct = True
    for file_path in python_files:
        if not check_python_syntax(file_path):
            all_correct = False
    
    if all_correct:
        print("\n所有Python文件语法检查通过!")
    else:
        print("\n存在语法错误的文件，请检查上面的输出。")
        sys.exit(1)

if __name__ == "__main__":
    main()