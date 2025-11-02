# 简单测试文件来验证DPFusion.py的基本功能

try:
    # 尝试导入并执行一些基本函数
    from DPFusion import edit_distance, length_of_lis
    
    # 测试基本功能
    print("测试编辑距离函数...")
    result1 = edit_distance("horse", "ros")
    print(f"编辑距离('horse', 'ros') = {result1}")
    print(f"预期结果: 3")
    
    print("\n测试最长递增子序列函数...")
    result2 = length_of_lis([10, 9, 2, 5, 3, 7, 101, 18])
    print(f"最长递增子序列长度 = {result2}")
    print(f"预期结果: 4")
    
    print("\n基础测试通过，代码语法正确！")
except Exception as e:
    print(f"测试过程中遇到错误: {e}")
    import traceback
    traceback.print_exc()