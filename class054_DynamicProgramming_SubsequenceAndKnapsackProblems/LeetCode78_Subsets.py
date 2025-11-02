"""
LeetCode 78. 子集
给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
解集不能包含重复的子集。你可以按任意顺序返回解集。
测试链接：https://leetcode.cn/problems/subsets/

算法详解：
使用多种方法生成数组的所有子集，包括位掩码法、回溯法和迭代法。

时间复杂度：O(n * 2^n)，其中n是数组长度
空间复杂度：O(n * 2^n) 用于存储所有子集

工程化考量：
1. 异常处理：检查输入参数有效性
2. 边界处理：空数组的情况
3. 性能优化：使用生成器避免内存爆炸
4. 代码质量：清晰的变量命名和类型注解

Python特性：
1. 列表推导式简化代码
2. 生成器表达式支持惰性求值
3. 内置函数提高开发效率
4. 动态类型使得代码简洁
"""

from typing import List, Generator
import time
import itertools

class SubsetsSolution:
    """
    子集问题解决方案类
    提供多种算法实现和测试功能
    """
    
    @staticmethod
    def subsets_bitmask(nums: List[int]) -> List[List[int]]:
        """
        位掩码法生成所有子集
        时间复杂度：O(n * 2^n)
        空间复杂度：O(n * 2^n)
        
        Args:
            nums: 输入数组，元素互不相同
            
        Returns:
            List[List[int]]: 所有子集的列表
            
        Raises:
            TypeError: 输入不是列表类型
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        n = len(nums)
        result = []
        
        # 特殊情况：空数组
        if n == 0:
            return [[]]
            
        # 总子集数：2^n
        total = 1 << n
        
        for mask in range(total):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            result.append(subset)
            
        return result
    
    @staticmethod
    def subsets_backtrack(nums: List[int]) -> List[List[int]]:
        """
        回溯法生成所有子集
        时间复杂度：O(n * 2^n)
        空间复杂度：O(n) 递归栈空间
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        result = []
        
        def backtrack(index: int, path: List[int]) -> None:
            # 基本情况：处理完所有元素
            if index == len(nums):
                result.append(path.copy())
                return
                
            # 不包含当前元素
            backtrack(index + 1, path)
            
            # 包含当前元素
            path.append(nums[index])
            backtrack(index + 1, path)
            path.pop()  # 回溯
            
        backtrack(0, [])
        return result
    
    @staticmethod
    def subsets_iterative(nums: List[int]) -> List[List[int]]:
        """
        迭代法生成所有子集
        时间复杂度：O(n * 2^n)
        空间复杂度：O(n * 2^n)
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        result = [[]]  # 初始包含空集
        
        for num in nums:
            # 为每个已有子集创建新子集并添加当前元素
            new_subsets = []
            for subset in result:
                new_subset = subset + [num]
                new_subsets.append(new_subset)
            result.extend(new_subsets)
            
        return result
    
    @staticmethod
    def subsets_optimized(nums: List[int]) -> List[List[int]]:
        """
        优化的回溯法（避免不必要的拷贝）
        时间复杂度：O(n * 2^n)
        空间复杂度：O(n) 递归栈空间
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        result = []
        
        def dfs(start: int, path: List[int]) -> None:
            # 每次递归都将当前路径加入结果
            result.append(path.copy())
            
            for i in range(start, len(nums)):
                path.append(nums[i])
                dfs(i + 1, path)
                path.pop()  # 回溯
                
        dfs(0, [])
        return result
    
    @staticmethod
    def subsets_builtin(nums: List[int]) -> List[List[int]]:
        """
        使用Python内置函数生成子集
        时间复杂度：O(n * 2^n)
        空间复杂度：O(n * 2^n)
        
        注意：这种方法简洁但可能不够直观
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        result = []
        n = len(nums)
        
        # 使用内置组合函数生成所有子集
        for k in range(n + 1):
            for subset in itertools.combinations(nums, k):
                result.append(list(subset))
                
        return result
    
    @staticmethod
    def subsets_generator(nums: List[int]) -> Generator[List[int], None, None]:
        """
        生成器版本，支持惰性求值
        适用于大规模数据，避免内存爆炸
        
        Yields:
            List[int]: 一个个子集
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        n = len(nums)
        
        for mask in range(1 << n):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            yield subset
    
    @staticmethod
    def run_tests() -> None:
        """
        运行单元测试，验证算法的正确性
        """
        print("=== LeetCode 78 子集问题测试 ===\n")
        
        test_cases = [
            # (描述, 输入数组)
            ("空数组", []),
            ("单元素", [1]),
            ("双元素", [1, 2]),
            ("三元素", [1, 2, 3]),
            ("四元素", [1, 2, 3, 4]),
        ]
        
        methods = [
            ("位掩码法", SubsetsSolution.subsets_bitmask),
            ("回溯法", SubsetsSolution.subsets_backtrack),
            ("迭代法", SubsetsSolution.subsets_iterative),
            ("优化回溯", SubsetsSolution.subsets_optimized),
            ("内置函数", SubsetsSolution.subsets_builtin),
        ]
        
        all_passed = True
        
        for description, nums in test_cases:
            print(f"{description}:")
            print(f"  输入数组: {nums}")
            
            # 使用第一种方法作为基准
            try:
                baseline = SubsetsSolution.subsets_bitmask(nums)
                expected_size = 1 << len(nums) if nums else 1
                
                print(f"  预期子集数: {expected_size}")
                print(f"  实际子集数: {len(baseline)}")
                
                case_passed = True
                results = [baseline]
                
                for method_name, method in methods[1:]:  # 跳过基准方法
                    try:
                        result = method(nums)
                        results.append(result)
                        
                        # 检查子集数量
                        size_ok = len(result) == len(baseline)
                        # 检查内容（转换为集合比较）
                        content_ok = (set(tuple(sorted(sub)) for sub in result) == 
                                    set(tuple(sorted(sub)) for sub in baseline))
                        
                        status = "✓" if size_ok and content_ok else "✗"
                        print(f"  {method_name}: {len(result)}个子集 {status}")
                        
                        if not (size_ok and content_ok):
                            case_passed = False
                            all_passed = False
                            
                    except Exception as e:
                        print(f"  {method_name}: 错误 - {e}")
                        case_passed = False
                        all_passed = False
                
                if case_passed:
                    # 打印前几个子集作为示例
                    if len(baseline) <= 16:
                        print(f"  所有子集: {baseline}")
                    else:
                        print(f"  前4个子集: {baseline[:4]} ... 等{len(baseline)}个子集")
                    print("  测试通过 ✓")
                else:
                    print("  测试失败 ✗")
                    
            except Exception as e:
                print(f"  基准方法错误: {e}")
                all_passed = False
                
            print()
            
        if all_passed:
            print("所有测试通过！ ✓")
        else:
            print("部分测试失败！ ✗")
            
        print()
    
    @staticmethod
    def performance_test() -> None:
        """
        性能测试，测试算法在大规模数据下的表现
        """
        print("=== 性能测试 ===")
        
        # 生成测试数据：中等规模数组
        n = 15  # 2^15 = 32768个子集
        nums = list(range(1, n + 1))
        
        print(f"测试数据规模: {n}个元素")
        print(f"预期子集数量: {1 << n}")
        print()
        
        methods = [
            ("位掩码法", SubsetsSolution.subsets_bitmask),
            ("优化回溯", SubsetsSolution.subsets_optimized),
            ("内置函数", SubsetsSolution.subsets_builtin),
        ]
        
        results = {}
        
        for method_name, method in methods:
            start_time = time.time()
            result = method(nums)
            end_time = time.time()
            duration = (end_time - start_time) * 1000  # 转换为毫秒
            
            results[method_name] = len(result)
            
            print(f"{method_name}:")
            print(f"  子集数量: {len(result)}")
            print(f"  耗时: {duration:.2f} 毫秒")
            print()
        
        # 测试生成器版本（避免内存爆炸）
        print("生成器版本测试（避免内存爆炸）:")
        start_time = time.time()
        count = 0
        for subset in SubsetsSolution.subsets_generator(nums):
            count += 1
            # 这里可以处理每个子集，但为了性能测试只计数
        end_time = time.time()
        gen_duration = (end_time - start_time) * 1000
        
        print(f"  子集数量: {count}")
        print(f"  耗时: {gen_duration:.2f} 毫秒")
        print("  注意：生成器版本可以处理更大规模的数据")
        print()
        
        # 验证结果一致性
        if len(set(results.values())) == 1 and count == results["位掩码法"]:
            print("结果一致性验证: 通过 ✓")
        else:
            print("结果一致性验证: 失败 ✗")


def main():
    """
    主函数，运行测试和性能测试
    """
    try:
        # 运行单元测试
        SubsetsSolution.run_tests()
        
        # 运行性能测试
        SubsetsSolution.performance_test()
        
        print("所有测试完成！")
        
    except Exception as e:
        print(f"测试过程中发生错误: {e}")
        return 1
        
    return 0


if __name__ == "__main__":
    exit(main())


"""
复杂度分析详细计算：

位掩码法：
- 时间：外层循环2^n次，内层循环n次 → O(n * 2^n)
- 空间：需要存储所有子集 → O(n * 2^n)

回溯法：
- 时间：生成2^n个子集 → O(n * 2^n)
- 空间：递归深度n → O(n)

迭代法：
- 时间：外层循环n次，内层循环2^i次 → O(n * 2^n)
- 空间：需要存储所有子集 → O(n * 2^n)

优化回溯法：
- 时间：O(n * 2^n)
- 空间：递归深度n → O(n)

内置函数法：
- 时间：O(n * 2^n)，但实际性能可能因实现而异
- 空间：O(n * 2^n)

生成器版本：
- 时间：O(n * 2^n)
- 空间：O(n) 每次只生成一个子集

Python特性说明：
1. 列表推导式使得代码非常简洁
2. itertools模块提供了强大的组合功能
3. 生成器支持惰性求值，适合处理大规模数据
4. 动态类型使得代码灵活但需要更多测试

调试技巧：
1. 使用小规模数据验证算法正确性
2. 打印中间结果观察生成过程
3. 使用断言验证关键假设
4. 使用memory_profiler分析内存使用

工程化建议：
1. 对于小规模数据使用任意方法
2. 对于中等规模数据使用优化回溯法
3. 对于大规模数据使用生成器版本
4. 添加类型注解提高代码可读性
5. 编写单元测试覆盖各种边界情况
"""