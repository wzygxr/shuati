"""
最终验证脚本：测试关键的Java实现
"""

import subprocess
import sys
import time

def run_java_test(class_name, description):
    """运行Java测试类"""
    try:
        print(f"正在测试 {description}...")
        start_time = time.time()
        result = subprocess.run(["java", "-cp", "d:\\Upan\\src\\algorithm-journey\\src\\algorithm-journey\\src", "class048." + class_name], 
                              capture_output=True, text=True, timeout=30)
        end_time = time.time()
        
        if result.returncode == 0:
            print(f"✓ {description} 测试通过 (耗时: {end_time - start_time:.2f}s)")
            # 打印部分输出以验证结果
            output_lines = result.stdout.strip().split('\n')
            for line in output_lines[:5]:  # 只打印前5行
                print(f"  输出: {line}")
            if len(output_lines) > 5:
                print(f"  ... (还有{len(output_lines) - 5}行输出)")
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
    print("开始最终验证：测试关键的Java实现")
    print("=" * 60)
    
    # 注意：这些Java类需要在当前目录下有对应的.class文件
    # 只测试包含main方法的类
    test_classes = [
        ("Code01_PrefixSumMatrix", "二维前缀和矩阵"),
        ("Code03_DiffMatrixLuogu", "二维差分数组(洛谷版)"),
        ("Code03_DiffMatrixNowcoder", "二维差分数组(牛客版)"),
        ("Code06_RangeSumQuery2DImmutable", "二维区域和检索"),
        ("Code07_CorporateFlightBookings", "航班预订统计"),
        ("Code08_IncrementSubmatricesByOne", "子矩阵元素加1"),
        ("Code18_TrappingRainWater", "接雨水问题")
    ]
    
    passed = 0
    total = len(test_classes)
    
    for class_name, description in test_classes:
        if run_java_test(class_name, description):
            passed += 1
        print()
    
    print("=" * 60)
    print(f"最终验证完成: {passed}/{total} 个测试通过")
    
    if passed == total:
        print("🎉 所有Java实现都验证通过！")
        return 0
    else:
        print(f"❌ 有 {total - passed} 个Java测试失败")
        return 1

if __name__ == "__main__":
    sys.exit(main())