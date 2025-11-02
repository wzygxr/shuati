"""
测试所有二维前缀和与差分数组相关算法的Python实现
"""

import subprocess
import sys
import time

def run_python_test(file_name, description):
    """运行Python测试文件"""
    try:
        print(f"正在测试 {description}...")
        start_time = time.time()
        result = subprocess.run([sys.executable, file_name], 
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
    print("开始测试所有二维前缀和与差分数组相关算法的Python实现")
    print("=" * 60)
    
    test_files = [
        ("Code01_PrefixSumMatrix.py", "二维前缀和矩阵"),
        ("Code02_LargestOneBorderedSquare.py", "边框为1的最大正方形"),
        ("Code03_DiffMatrixLuogu.py", "二维差分数组(洛谷版)"),
        ("Code06_RangeSumQuery2DImmutable.py", "二维区域和检索"),
        ("Code07_CorporateFlightBookings.py", "航班预订统计"),
        ("Code08_IncrementSubmatricesByOne.py", "子矩阵元素加1"),
        ("Code18_TrappingRainWater.py", "接雨水问题")
    ]
    
    passed = 0
    total = len(test_files)
    
    for file_name, description in test_files:
        if run_python_test(file_name, description):
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