"""
测试所有二维前缀和与差分数组相关算法的C++实现
"""

import subprocess
import sys
import time
import os

def run_cpp_test(executable_name, description):
    """运行C++测试程序"""
    try:
        print(f"正在测试 {description}...")
        start_time = time.time()
        # 检查可执行文件是否存在
        if not os.path.exists(executable_name):
            print(f"✗ {description} 可执行文件不存在: {executable_name}")
            return False
            
        result = subprocess.run([executable_name], 
                              capture_output=True, text=True, timeout=30)
        end_time = time.time()
        
        if result.returncode == 0:
            print(f"✓ {description} 测试通过 (耗时: {end_time - start_time:.2f}s)")
            return True
        else:
            print(f"✗ {description} 测试失败")
            print(f"错误输出: {result.stderr}")
            return False
    except subprocess.TimeoutExpired:
        print(f"✗ {description} 测试超时")
        return False
    except Exception as e:
        print(f"✗ {description} 测试出错: {e}")
        return False

def main():
    """主函数"""
    print("开始测试所有二维前缀和与差分数组相关算法的C++实现")
    print("=" * 60)
    
    test_files = [
        ("Code01_PrefixSumMatrix_cpp.exe", "二维前缀和矩阵"),
        ("Code02_LargestOneBorderedSquare_cpp.exe", "边框为1的最大正方形"),
        ("Code03_DiffMatrixLuogu_cpp.exe", "二维差分数组(洛谷版)"),
        ("Code06_RangeSumQuery2DImmutable_cpp.exe", "二维区域和检索"),
        ("Code07_CorporateFlightBookings_cpp.exe", "航班预订统计"),
        ("Code08_IncrementSubmatricesByOne_cpp.exe", "子矩阵元素加1"),
        ("Code18_TrappingRainWater_cpp.exe", "接雨水问题")
    ]
    
    passed = 0
    total = len(test_files)
    
    for executable_name, description in test_files:
        if run_cpp_test(executable_name, description):
            passed += 1
        print()
    
    print("=" * 60)
    print(f"测试完成: {passed}/{total} 个测试通过")
    
    if passed == total:
        print("🎉 所有测试都通过了！")
        return 0
    else:
        print(f"❌ 有 {total - passed} 个测试失败")
        return 1

if __name__ == "__main__":
    sys.exit(main())