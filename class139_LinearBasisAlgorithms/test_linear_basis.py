# 线性基测试文件 (Python版本)

def insert(num, basis):
    """
    线性基插入操作
    
    参数:
        num: 要插入的数字
        basis: 线性基数组
        
    返回:
        None
    """
    for i in range(63, -1, -1):
        if (num >> i) & 1:
            if basis[i] == 0:
                basis[i] = num
                return
            num ^= basis[i]

def insert_with_return(num, basis):
    """
    带返回值的线性基插入操作
    
    参数:
        num: 要插入的数字
        basis: 线性基数组
        
    返回:
        bool: 是否插入成功
    """
    for i in range(63, -1, -1):
        if (num >> i) & 1:
            if basis[i] == 0:
                basis[i] = num
                return True
            num ^= basis[i]
    return False

def find_maximum_xor(nums):
    """
    计算最大异或和
    
    参数:
        nums: 数字列表
        
    返回:
        long: 最大异或和
    """
    if not nums:
        return 0
    
    # 初始化线性基
    basis = [0] * 64
    
    # 构建线性基
    for num in nums:
        insert(num, basis)
    
    # 计算最大异或值
    result = 0
    for i in range(63, -1, -1):
        if basis[i] != 0:
            result = max(result, result ^ basis[i])
    
    return result

def query_kth_xor(nums, k):
    """
    查询第k小异或和
    
    参数:
        nums: 数字列表
        k: 查询位置
        
    返回:
        long: 第k小异或和
    """
    if k <= 0:
        raise ValueError("k must be positive")
    
    # 初始化线性基
    basis = [0] * 64
    basis_size = 0
    
    # 构建线性基
    for num in nums:
        if insert_with_return(num, basis):
            basis_size += 1
    
    # 高斯消元
    for i in range(64):
        for j in range(i + 1, 64):
            if (basis[j] & (1 << i)) != 0:
                basis[j] ^= basis[i]
    
    # 重新整理
    gaussian_basis = [b for b in basis if b != 0]
    
    # 判断是否能异或出0
    can_get_zero = (len(gaussian_basis) != len(nums))
    
    # 查询第k小
    if can_get_zero:
        if k == 1:
            return 0
        k -= 1
    
    if k > (1 << len(gaussian_basis)):
        return -1
    
    result = 0
    for i in range(len(gaussian_basis)):
        if (k & (1 << i)) != 0:
            result ^= gaussian_basis[i]
    
    return result

# 测试用例
def test_maximum_xor():
    """测试最大异或和"""
    print("=== 测试最大异或和 ===")
    arr1 = [3, 10, 5, 25, 2, 8]
    result1 = find_maximum_xor(arr1)
    print(f"输入: {arr1}")
    print(f"期望输出: 28 (5^25)")
    print(f"实际输出: {result1}")
    print(f"测试结果: {'通过' if result1 == 28 else '失败'}")
    print()

def test_linear_dependent():
    """测试线性相关情况"""
    print("=== 测试线性相关情况 ===")
    arr2 = [1, 2, 3]  # 1^2 = 3，线性相关
    result2 = find_maximum_xor(arr2)
    print(f"输入: {arr2}")
    print(f"期望输出: 3")
    print(f"实际输出: {result2}")
    print(f"测试结果: {'通过' if result2 == 3 else '失败'}")
    print()

def test_empty_array():
    """测试空数组"""
    print("=== 测试空数组 ===")
    arr3 = []
    result3 = find_maximum_xor(arr3)
    print(f"输入: {arr3}")
    print(f"期望输出: 0")
    print(f"实际输出: {result3}")
    print(f"测试结果: {'通过' if result3 == 0 else '失败'}")
    print()

def test_single_element():
    """测试单元素数组"""
    print("=== 测试单元素数组 ===")
    arr4 = [5]
    result4 = find_maximum_xor(arr4)
    print(f"输入: {arr4}")
    print(f"期望输出: 5")
    print(f"实际输出: {result4}")
    print(f"测试结果: {'通过' if result4 == 5 else '失败'}")
    print()

def test_kth_xor():
    """测试第k小异或和"""
    print("=== 测试第k小异或和 ===")
    arr5 = [1, 2, 3]
    print(f"输入: {arr5}")
    for k in range(1, 5):
        result = query_kth_xor(arr5, k)
        print(f"第{k}小异或和: {result}")
    print()

def main():
    """主函数"""
    print("线性基算法测试 (Python版本)")
    print("=" * 30)
    
    # 运行所有测试用例
    test_maximum_xor()
    test_linear_dependent()
    test_empty_array()
    test_single_element()
    test_kth_xor()
    
    print("所有测试完成！")

if __name__ == "__main__":
    main()