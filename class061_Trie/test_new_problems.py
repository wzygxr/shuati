"""
测试新添加的前缀树题目
"""

import subprocess
import sys
import os

def test_problem(problem_name, code_file, input_data, expected_output):
    """
    测试一个题目
    :param problem_name: 题目名称
    :param code_file: 代码文件名
    :param input_data: 输入数据
    :param expected_output: 期望输出
    """
    print(f"测试 {problem_name}...")
    
    try:
        # 运行Python代码
        result = subprocess.run(
            [sys.executable, code_file],
            input=input_data,
            text=True,
            capture_output=True,
            timeout=10
        )
        
        # 检查输出
        if result.stdout.strip() == expected_output.strip():
            print(f"✅ {problem_name} 测试通过")
        else:
            print(f"❌ {problem_name} 测试失败")
            print(f"  期望输出: {expected_output}")
            print(f"  实际输出: {result.stdout}")
            
    except subprocess.TimeoutExpired:
        print(f"❌ {problem_name} 测试超时")
    except Exception as e:
        print(f"❌ {problem_name} 测试出错: {e}")

def main():
    # 获取当前目录
    current_dir = os.path.dirname(os.path.abspath(__file__))
    
    # 测试POJ 2001 Shortest Prefixes
    test_problem(
        "POJ 2001 Shortest Prefixes",
        os.path.join(current_dir, "Code19_POJ2001.py"),
        "carbohydrate\ncart\ncarburetor\ncaramel\ncaribou\ncarbonic\ncartilage\ncarbon\ncarriage\ncarton\ncar\ncarbonate\n9\n",
        "carbohydrate carboh\ncart cart\ncarburetor carbu\ncaramel cara\ncaribou cari\ncarbonic carboni\ncartilage carti\ncarbon carbon\ncarriage carr\ncarton carto\ncar car\ncarbonate carbona"
    )
    
    # 测试HDU 1671 Phone List
    test_problem(
        "HDU 1671 Phone List",
        os.path.join(current_dir, "Code20_HDU1671.py"),
        "2\n3\n911\n97625999\n91125426\n5\n113\n12340\n123440\n12345\n98346\n",
        "NO\nYES"
    )
    
    # 测试POJ 1056 IMMEDIATE DECODABILITY
    test_problem(
        "POJ 1056 IMMEDIATE DECODABILITY",
        os.path.join(current_dir, "Code21_POJ1056.py"),
        "01\n10\n0010\n0000\n9\n01\n10\n010\n0000\n9\n",
        "Set 1 is immediately decodable\nSet 2 is not immediately decodable"
    )
    
    # 测试UVa 10226 Hardwood Species
    test_problem(
        "UVa 10226 Hardwood Species",
        os.path.join(current_dir, "Code22_UVa10226.py"),
        "1\n\nRed Alder\nAsh\nAspen\nBasswood\nAsh\nBeech\nYellow Birch\nAsh\nCherry\nCottonwood\nAsh\nCypress\nRed Elm\nGum\nHackberry\nWhite Oak\nHickory\nPecan\nHard Maple\nWhite Oak\nSoft Maple\nRed Oak\nRed Oak\nWhite Oak\nPoplan\nSassafras\nSycamore\nTulip Poplan\nWhite Oak\nWiliow\n",
        "Ash 12.5000\nAspen 3.5714\nBasswood 3.5714\nBeech 3.5714\nCherry 3.5714\nCottonwood 3.5714\nCypress 3.5714\nGum 3.5714\nHackberry 3.5714\nHard Maple 3.5714\nHickory 3.5714\nPecan 3.5714\nPoplan 3.5714\nRed Alder 3.5714\nRed Elm 3.5714\nRed Oak 7.1429\nSassafras 3.5714\nSoft Maple 3.5714\nSycamore 3.5714\nTulip Poplan 3.5714\nWhite Oak 10.7143\nWiliow 3.5714\nYellow Birch 3.5714"
    )
    
    # 测试CodeChef Tries with XOR
    test_problem(
        "CodeChef Tries with XOR",
        os.path.join(current_dir, "Code23_CodeChefTriesWithXOR.py"),
        "4\n1 2 3 4\n",
        "7"
    )
    
    print("\n所有测试完成！")

if __name__ == "__main__":
    main()